package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.translation.I18n;

public enum EnumType implements ITranslatable, IStringSerializable {
   Normal(0, "Normal", 14540253, 8.0F, 520.0F),
   Fire(1, "Fire", 16746496, 8.0F, 264.0F),
   Water(2, "Water", 5727743, 1288.0F, 520.0F),
   Electric(3, "Electric", 16118348, 776.0F, 8.0F),
   Grass(4, "Grass", 54304, 776.0F, 264.0F),
   Ice(5, "Ice", 11599871, 1288.0F, 264.0F),
   Fighting(6, "Fighting", 12189752, 1288.0F, 8.0F),
   Poison(7, "Poison", 13174256, 264.0F, 520.0F),
   Ground(8, "Ground", 10053184, 1031.0F, 264.0F),
   Flying(9, "Flying", 13424127, 264.0F, 264.0F),
   Psychic(10, "Psychic", 16733419, 520.0F, 520.0F),
   Bug(11, "Bug", 11067475, 8.0F, 8.0F),
   Rock(12, "Rock", 10712917, 775.0F, 519.0F),
   Ghost(13, "Ghost", 7089034, 520.0F, 264.0F),
   Dragon(14, "Dragon", 2830540, 520.0F, 8.0F),
   Dark(15, "Dark", 4210752, 264.0F, 8.0F),
   Steel(16, "Steel", 12369090, 1032.0F, 520.0F),
   Mystery(17, "???", 3184256, 1544.0F, 8.0F),
   Fairy(18, "Fairy", 16614535, 1032.0F, 8.0F);

   private int index;
   private String name;
   private int color;
   public float textureX;
   public float textureY;

   private EnumType(int i, String s, int c, float texX, float texY) {
      this.index = i;
      this.name = s;
      this.color = c;
      this.textureX = texX;
      this.textureY = texY;
   }

   public int getColor() {
      return this.color;
   }

   public String func_176610_l() {
      return this.name;
   }

   public String getUnlocalizedName() {
      return "type." + this.name.toLowerCase();
   }

   public String getLocalizedName() {
      return I18n.func_74838_a(this.getUnlocalizedName());
   }

   public static EnumType parseType(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return Normal;
      }
   }

   public static EnumType parseType(String name) {
      try {
         return valueOf(name);
      } catch (Exception var2) {
         return Normal;
      }
   }

   public static EnumType parseOrNull(String name) {
      try {
         return valueOf(name);
      } catch (Exception var2) {
         return null;
      }
   }

   public static EnumType parseOrNull(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return null;
      }
   }

   public static ArrayList getAllTypes() {
      ArrayList list = new ArrayList();
      EnumType[] t = values();
      Collections.addAll(list, t);
      return list;
   }

   public static float getTotalEffectiveness(List enumTypes, EnumType attackType) {
      return getTotalEffectiveness(enumTypes, attackType, false);
   }

   public static float getTotalEffectiveness(List enumTypes, EnumType attackType, boolean inverse) {
      float f = 1.0F;
      Iterator var4 = enumTypes.iterator();

      while(var4.hasNext()) {
         EnumType type = (EnumType)var4.next();
         if (type != null) {
            f *= getEffectiveness(attackType, type, inverse);
         }
      }

      return f;
   }

   public static float inverseEffectiveness(float effectiveness) {
      return effectiveness == 0.0F ? 2.0F : 1.0F / effectiveness;
   }

   public static float getEffectiveness(EnumType t, EnumType t1) {
      return getEffectiveness(t, t1, false);
   }

   public static float getEffectiveness(EnumType t, EnumType t1, boolean inverse) {
      if (t != Mystery && t1 != Mystery) {
         float e = 1.0F;
         if (t == Normal) {
            if (t1 != Rock && t1 != Steel) {
               if (t1 == Ghost) {
                  e = Effectiveness.None.value;
               }
            } else {
               e = Effectiveness.Not.value;
            }
         } else if (t == Fire) {
            if (t1 != Grass && t1 != Ice && t1 != Bug && t1 != Steel) {
               if (t1 == Fire || t1 == Water || t1 == Rock || t1 == Dragon) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Water) {
            if (t1 != Fire && t1 != Ground && t1 != Rock) {
               if (t1 == Water || t1 == Grass || t1 == Dragon) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Electric) {
            if (t1 != Water && t1 != Flying) {
               if (t1 != Electric && t1 != Grass && t1 != Dragon) {
                  if (t1 == Ground) {
                     e = Effectiveness.None.value;
                  }
               } else {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Grass) {
            if (t1 != Water && t1 != Ground && t1 != Rock) {
               if (t1 == Fire || t1 == Grass || t1 == Poison || t1 == Flying || t1 == Bug || t1 == Dragon || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Ice) {
            if (t1 != Grass && t1 != Ground && t1 != Flying && t1 != Dragon) {
               if (t1 == Fire || t1 == Water || t1 == Ice || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Fighting) {
            if (t1 != Normal && t1 != Ice && t1 != Rock && t1 != Dark && t1 != Steel) {
               if (t1 != Poison && t1 != Flying && !(t1 == Psychic | t1 == Bug) && t1 != Fairy) {
                  if (t1 == Ghost) {
                     e = Effectiveness.None.value;
                  }
               } else {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Poison) {
            if (t1 != Grass && t1 != Fairy) {
               if (t1 != Poison && t1 != Ground && t1 != Rock && t1 != Ghost) {
                  if (t1 == Steel) {
                     e = Effectiveness.None.value;
                  }
               } else {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Ground) {
            if (t1 != Fire && t1 != Electric && t1 != Poison && t1 != Rock && t1 != Steel) {
               if (t1 != Grass && t1 != Bug) {
                  if (t1 == Flying) {
                     e = Effectiveness.None.value;
                  }
               } else {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Flying) {
            if (t1 != Grass && t1 != Fighting && t1 != Bug) {
               if (t1 == Electric || t1 == Rock || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Psychic) {
            if (t1 != Fighting && t1 != Poison) {
               if (t1 != Psychic && t1 != Steel) {
                  if (t1 == Dark) {
                     e = Effectiveness.None.value;
                  }
               } else {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Bug) {
            if (t1 != Grass && t1 != Psychic && t1 != Dark) {
               if (t1 == Fire || t1 == Fighting || t1 == Poison || t1 == Flying || t1 == Ghost || t1 == Steel || t1 == Fairy) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Rock) {
            if (t1 != Fire && t1 != Ice && t1 != Flying && t1 != Bug) {
               if (t1 == Fighting || t1 == Ground || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Ghost) {
            if (t1 != Psychic && t1 != Ghost) {
               if (t1 == Dark) {
                  e = Effectiveness.Not.value;
               } else if (t1 == Normal) {
                  e = Effectiveness.None.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Dragon) {
            if (t1 == Dragon) {
               e = Effectiveness.Super.value;
            } else if (t1 == Steel) {
               e = Effectiveness.Not.value;
            } else if (t1 == Fairy) {
               e = Effectiveness.None.value;
            }
         } else if (t == Dark) {
            if (t1 != Psychic && t1 != Ghost) {
               if (t1 == Fighting || t1 == Dark || t1 == Fairy) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Steel) {
            if (t1 != Ice && t1 != Rock && t1 != Fairy) {
               if (t1 == Fire || t1 == Water || t1 == Electric || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         } else if (t == Fairy) {
            if (t1 != Dark && t1 != Dragon && t1 != Fighting) {
               if (t1 == Fire || t1 == Poison || t1 == Steel) {
                  e = Effectiveness.Not.value;
               }
            } else {
               e = Effectiveness.Super.value;
            }
         }

         if (inverse) {
            if (e == Effectiveness.Super.value) {
               return Effectiveness.Not.value;
            } else if (e == Effectiveness.Max.value) {
               return Effectiveness.Barely.value;
            } else if (e == Effectiveness.Not.value) {
               return Effectiveness.Super.value;
            } else if (e == Effectiveness.Barely.value) {
               return Effectiveness.Max.value;
            } else {
               return e == Effectiveness.None.value ? Effectiveness.Super.value : Effectiveness.Normal.value;
            }
         } else {
            return e;
         }
      } else {
         return Effectiveness.Normal.value;
      }
   }

   public int getIndex() {
      return this.index;
   }

   public ArrayList makeTypeList() {
      ArrayList typeList = new ArrayList(1);
      typeList.add(this);
      return typeList;
   }

   public static boolean hasType(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public static List ignoreType(List origTypes, EnumType ignoreType) {
      List newTypes = (List)origTypes.stream().filter((type) -> {
         return type != ignoreType;
      }).collect(Collectors.toList());
      if (newTypes.isEmpty()) {
         newTypes.add(Mystery);
      }

      return newTypes;
   }

   public static EnumType getRandomType(boolean mystery) {
      EnumType type;
      do {
         type = values()[RandomHelper.rand.nextInt(values().length)];
      } while(!mystery && type == Mystery);

      return type;
   }

   public static String[] getTypeNames(boolean mystery) {
      String[] names = new String[mystery ? values().length : values().length - 1];
      int i = 0;
      EnumType[] var3 = values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumType type = var3[var5];
         if (type != Mystery || mystery) {
            names[i] = type.name();
            ++i;
         }
      }

      return names;
   }
}
