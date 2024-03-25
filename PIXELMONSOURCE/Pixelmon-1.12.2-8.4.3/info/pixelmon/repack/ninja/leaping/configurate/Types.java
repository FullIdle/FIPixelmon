package info.pixelmon.repack.ninja.leaping.configurate;

public class Types {
   private Types() {
   }

   public static String asString(Object value) {
      return value == null ? null : value.toString();
   }

   public static String strictAsString(Object value) {
      return value instanceof String ? (String)value : null;
   }

   public static Float asFloat(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Float) {
         return (Float)value;
      } else if (value instanceof Integer) {
         return ((Number)value).floatValue();
      } else {
         try {
            return Float.parseFloat(value.toString());
         } catch (IllegalArgumentException var2) {
            return null;
         }
      }
   }

   public static Float strictAsFloat(Object value) {
      if (value == null) {
         return null;
      } else {
         return !(value instanceof Float) && !(value instanceof Integer) ? null : ((Number)value).floatValue();
      }
   }

   public static Double asDouble(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Double) {
         return (Double)value;
      } else if (!(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Float)) {
         try {
            return Double.parseDouble(value.toString());
         } catch (IllegalArgumentException var2) {
            return null;
         }
      } else {
         return ((Number)value).doubleValue();
      }
   }

   public static Double strictAsDouble(Object value) {
      if (value == null) {
         return null;
      } else {
         return !(value instanceof Double) && !(value instanceof Float) && !(value instanceof Integer) && !(value instanceof Long) ? null : ((Number)value).doubleValue();
      }
   }

   public static Integer asInt(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Integer) {
         return (Integer)value;
      } else {
         if (value instanceof Float || value instanceof Double) {
            double val = ((Number)value).doubleValue();
            if (val == Math.floor(val)) {
               return (int)val;
            }
         }

         try {
            return Integer.parseInt(value.toString());
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static Integer strictAsInt(Object value) {
      if (value == null) {
         return null;
      } else {
         return value instanceof Integer ? (Integer)value : null;
      }
   }

   public static Long asLong(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Long) {
         return (Long)value;
      } else if (value instanceof Integer) {
         return ((Number)value).longValue();
      } else {
         if (value instanceof Float || value instanceof Double) {
            double val = ((Number)value).doubleValue();
            if (val == Math.floor(val)) {
               return (long)val;
            }
         }

         try {
            return Long.parseLong(value.toString());
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static Long strictAsLong(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Long) {
         return (Long)value;
      } else {
         return value instanceof Integer ? ((Number)value).longValue() : null;
      }
   }

   public static Boolean asBoolean(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof Boolean) {
         return (Boolean)value;
      } else if (value instanceof Number) {
         return !value.equals(0);
      } else {
         String potential = value.toString();
         if (!potential.equals("true") && !potential.equals("t") && !potential.equals("yes") && !potential.equals("y") && !potential.equals("1")) {
            return !potential.equals("false") && !potential.equals("f") && !potential.equals("no") && !potential.equals("n") && !potential.equals("0") ? null : false;
         } else {
            return true;
         }
      }
   }

   public static Boolean strictAsBoolean(Object value) {
      if (value == null) {
         return null;
      } else {
         return value instanceof Boolean ? (Boolean)value : null;
      }
   }
}
