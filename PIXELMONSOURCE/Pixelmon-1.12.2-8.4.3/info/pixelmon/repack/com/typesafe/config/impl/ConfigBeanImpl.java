package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.Config;
import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigList;
import info.pixelmon.repack.com.typesafe.config.ConfigMemorySize;
import info.pixelmon.repack.com.typesafe.config.ConfigObject;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import info.pixelmon.repack.com.typesafe.config.Optional;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigBeanImpl {
   public static Object createInternal(Config config, Class clazz) {
      if (((SimpleConfig)config).root().resolveStatus() != ResolveStatus.RESOLVED) {
         throw new ConfigException.NotResolved("need to Config#resolve() a config before using it to initialize a bean, see the API docs for Config#resolve()");
      } else {
         Map configProps = new HashMap();
         Map originalNames = new HashMap();
         Iterator beanInfo = config.root().entrySet().iterator();

         while(true) {
            Map.Entry configProp;
            String originalName;
            String camelName;
            do {
               if (!beanInfo.hasNext()) {
                  beanInfo = null;

                  BeanInfo beanInfo;
                  try {
                     beanInfo = Introspector.getBeanInfo(clazz);
                  } catch (IntrospectionException var18) {
                     throw new ConfigException.BadBean("Could not get bean information for class " + clazz.getName(), var18);
                  }

                  try {
                     List beanProps = new ArrayList();
                     PropertyDescriptor[] var21 = beanInfo.getPropertyDescriptors();
                     int var23 = var21.length;

                     PropertyDescriptor beanProp;
                     for(int var8 = 0; var8 < var23; ++var8) {
                        beanProp = var21[var8];
                        if (beanProp.getReadMethod() != null && beanProp.getWriteMethod() != null) {
                           beanProps.add(beanProp);
                        }
                     }

                     List problems = new ArrayList();
                     Iterator var24 = beanProps.iterator();

                     while(var24.hasNext()) {
                        PropertyDescriptor beanProp = (PropertyDescriptor)var24.next();
                        Method setter = beanProp.getWriteMethod();
                        Class parameterClass = setter.getParameterTypes()[0];
                        ConfigValueType expectedType = getValueTypeOrNull(parameterClass);
                        if (expectedType != null) {
                           String name = (String)originalNames.get(beanProp.getName());
                           if (name == null) {
                              name = beanProp.getName();
                           }

                           Path path = Path.newKey(name);
                           AbstractConfigValue configValue = (AbstractConfigValue)configProps.get(beanProp.getName());
                           if (configValue != null) {
                              SimpleConfig.checkValid(path, (ConfigValueType)expectedType, configValue, problems);
                           } else if (!isOptionalProperty(clazz, beanProp)) {
                              SimpleConfig.addMissing(problems, (ConfigValueType)expectedType, path, config.origin());
                           }
                        }
                     }

                     if (!problems.isEmpty()) {
                        throw new ConfigException.ValidationFailed(problems);
                     }

                     Object bean = clazz.newInstance();
                     Iterator var27 = beanProps.iterator();

                     while(var27.hasNext()) {
                        beanProp = (PropertyDescriptor)var27.next();
                        Method setter = beanProp.getWriteMethod();
                        Type parameterType = setter.getGenericParameterTypes()[0];
                        Class parameterClass = setter.getParameterTypes()[0];
                        String configPropName = (String)originalNames.get(beanProp.getName());
                        if (configPropName == null) {
                           if (!isOptionalProperty(clazz, beanProp)) {
                              throw new ConfigException.Missing(beanProp.getName());
                           }
                        } else {
                           Object unwrapped = getValue(clazz, parameterType, parameterClass, config, configPropName);
                           setter.invoke(bean, unwrapped);
                        }
                     }

                     return bean;
                  } catch (InstantiationException var15) {
                     throw new ConfigException.BadBean(clazz.getName() + " needs a public no-args constructor to be used as a bean", var15);
                  } catch (IllegalAccessException var16) {
                     throw new ConfigException.BadBean(clazz.getName() + " getters and setters are not accessible, they must be for use as a bean", var16);
                  } catch (InvocationTargetException var17) {
                     throw new ConfigException.BadBean("Calling bean method on " + clazz.getName() + " caused an exception", var17);
                  }
               }

               configProp = (Map.Entry)beanInfo.next();
               originalName = (String)configProp.getKey();
               camelName = ConfigImplUtil.toCamelCase(originalName);
            } while(originalNames.containsKey(camelName) && !originalName.equals(camelName));

            configProps.put(camelName, (AbstractConfigValue)configProp.getValue());
            originalNames.put(camelName, originalName);
         }
      }
   }

   private static Object getValue(Class beanClass, Type parameterType, Class parameterClass, Config config, String configPropName) {
      if (parameterClass != Boolean.class && parameterClass != Boolean.TYPE) {
         if (parameterClass != Integer.class && parameterClass != Integer.TYPE) {
            if (parameterClass != Double.class && parameterClass != Double.TYPE) {
               if (parameterClass != Long.class && parameterClass != Long.TYPE) {
                  if (parameterClass == String.class) {
                     return config.getString(configPropName);
                  } else if (parameterClass == Duration.class) {
                     return config.getDuration(configPropName);
                  } else if (parameterClass == ConfigMemorySize.class) {
                     return config.getMemorySize(configPropName);
                  } else if (parameterClass == Object.class) {
                     return config.getAnyRef(configPropName);
                  } else if (parameterClass == List.class) {
                     return getListValue(beanClass, parameterType, parameterClass, config, configPropName);
                  } else if (parameterClass == Map.class) {
                     Type[] typeArgs = ((ParameterizedType)parameterType).getActualTypeArguments();
                     if (typeArgs[0] == String.class && typeArgs[1] == Object.class) {
                        return config.getObject(configPropName).unwrapped();
                     } else {
                        throw new ConfigException.BadBean("Bean property '" + configPropName + "' of class " + beanClass.getName() + " has unsupported Map<" + typeArgs[0] + "," + typeArgs[1] + ">, only Map<String,Object> is supported right now");
                     }
                  } else if (parameterClass == Config.class) {
                     return config.getConfig(configPropName);
                  } else if (parameterClass == ConfigObject.class) {
                     return config.getObject(configPropName);
                  } else if (parameterClass == ConfigValue.class) {
                     return config.getValue(configPropName);
                  } else if (parameterClass == ConfigList.class) {
                     return config.getList(configPropName);
                  } else if (parameterClass.isEnum()) {
                     Enum enumValue = config.getEnum(parameterClass, configPropName);
                     return enumValue;
                  } else if (hasAtLeastOneBeanProperty(parameterClass)) {
                     return createInternal(config.getConfig(configPropName), parameterClass);
                  } else {
                     throw new ConfigException.BadBean("Bean property " + configPropName + " of class " + beanClass.getName() + " has unsupported type " + parameterType);
                  }
               } else {
                  return config.getLong(configPropName);
               }
            } else {
               return config.getDouble(configPropName);
            }
         } else {
            return config.getInt(configPropName);
         }
      } else {
         return config.getBoolean(configPropName);
      }
   }

   private static Object getListValue(Class beanClass, Type parameterType, Class parameterClass, Config config, String configPropName) {
      Type elementType = ((ParameterizedType)parameterType).getActualTypeArguments()[0];
      if (elementType == Boolean.class) {
         return config.getBooleanList(configPropName);
      } else if (elementType == Integer.class) {
         return config.getIntList(configPropName);
      } else if (elementType == Double.class) {
         return config.getDoubleList(configPropName);
      } else if (elementType == Long.class) {
         return config.getLongList(configPropName);
      } else if (elementType == String.class) {
         return config.getStringList(configPropName);
      } else if (elementType == Duration.class) {
         return config.getDurationList(configPropName);
      } else if (elementType == ConfigMemorySize.class) {
         return config.getMemorySizeList(configPropName);
      } else if (elementType == Object.class) {
         return config.getAnyRefList(configPropName);
      } else if (elementType == Config.class) {
         return config.getConfigList(configPropName);
      } else if (elementType == ConfigObject.class) {
         return config.getObjectList(configPropName);
      } else if (elementType == ConfigValue.class) {
         return config.getList(configPropName);
      } else if (((Class)elementType).isEnum()) {
         List enumValues = config.getEnumList((Class)elementType, configPropName);
         return enumValues;
      } else if (!hasAtLeastOneBeanProperty((Class)elementType)) {
         throw new ConfigException.BadBean("Bean property '" + configPropName + "' of class " + beanClass.getName() + " has unsupported list element type " + elementType);
      } else {
         List beanList = new ArrayList();
         List configList = config.getConfigList(configPropName);
         Iterator var8 = configList.iterator();

         while(var8.hasNext()) {
            Config listMember = (Config)var8.next();
            beanList.add(createInternal(listMember, (Class)elementType));
         }

         return beanList;
      }
   }

   private static ConfigValueType getValueTypeOrNull(Class parameterClass) {
      if (parameterClass != Boolean.class && parameterClass != Boolean.TYPE) {
         if (parameterClass != Integer.class && parameterClass != Integer.TYPE) {
            if (parameterClass != Double.class && parameterClass != Double.TYPE) {
               if (parameterClass != Long.class && parameterClass != Long.TYPE) {
                  if (parameterClass == String.class) {
                     return ConfigValueType.STRING;
                  } else if (parameterClass == Duration.class) {
                     return null;
                  } else if (parameterClass == ConfigMemorySize.class) {
                     return null;
                  } else if (parameterClass == List.class) {
                     return ConfigValueType.LIST;
                  } else if (parameterClass == Map.class) {
                     return ConfigValueType.OBJECT;
                  } else if (parameterClass == Config.class) {
                     return ConfigValueType.OBJECT;
                  } else if (parameterClass == ConfigObject.class) {
                     return ConfigValueType.OBJECT;
                  } else {
                     return parameterClass == ConfigList.class ? ConfigValueType.LIST : null;
                  }
               } else {
                  return ConfigValueType.NUMBER;
               }
            } else {
               return ConfigValueType.NUMBER;
            }
         } else {
            return ConfigValueType.NUMBER;
         }
      } else {
         return ConfigValueType.BOOLEAN;
      }
   }

   private static boolean hasAtLeastOneBeanProperty(Class clazz) {
      BeanInfo beanInfo = null;

      try {
         beanInfo = Introspector.getBeanInfo(clazz);
      } catch (IntrospectionException var6) {
         return false;
      }

      PropertyDescriptor[] var2 = beanInfo.getPropertyDescriptors();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PropertyDescriptor beanProp = var2[var4];
         if (beanProp.getReadMethod() != null && beanProp.getWriteMethod() != null) {
            return true;
         }
      }

      return false;
   }

   private static boolean isOptionalProperty(Class beanClass, PropertyDescriptor beanProp) {
      Field field = getField(beanClass, beanProp.getName());
      return ((Optional[])field.getAnnotationsByType(Optional.class)).length > 0;
   }

   private static Field getField(Class beanClass, String fieldName) {
      try {
         Field field = beanClass.getDeclaredField(fieldName);
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException var3) {
         beanClass = beanClass.getSuperclass();
         return beanClass == null ? null : getField(beanClass, fieldName);
      }
   }
}
