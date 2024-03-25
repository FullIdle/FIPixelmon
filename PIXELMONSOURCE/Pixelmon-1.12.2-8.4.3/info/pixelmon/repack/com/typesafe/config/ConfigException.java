package info.pixelmon.repack.com.typesafe.config;

import info.pixelmon.repack.com.typesafe.config.impl.ConfigImplUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;

public abstract class ConfigException extends RuntimeException implements Serializable {
   private static final long serialVersionUID = 1L;
   private final transient ConfigOrigin origin;

   protected ConfigException(ConfigOrigin origin, String message, Throwable cause) {
      super(origin.description() + ": " + message, cause);
      this.origin = origin;
   }

   protected ConfigException(ConfigOrigin origin, String message) {
      this((String)(origin.description() + ": " + message), (Throwable)null);
   }

   protected ConfigException(String message, Throwable cause) {
      super(message, cause);
      this.origin = null;
   }

   protected ConfigException(String message) {
      this((String)message, (Throwable)null);
   }

   public ConfigOrigin origin() {
      return this.origin;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      ConfigImplUtil.writeOrigin(out, this.origin);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      ConfigOrigin origin = ConfigImplUtil.readOrigin(in);

      Field f;
      try {
         f = ConfigException.class.getDeclaredField("origin");
      } catch (NoSuchFieldException var7) {
         throw new IOException("ConfigException has no origin field?", var7);
      } catch (SecurityException var8) {
         throw new IOException("unable to fill out origin field in ConfigException", var8);
      }

      f.setAccessible(true);

      try {
         f.set(this, origin);
      } catch (IllegalArgumentException var5) {
         throw new IOException("unable to set origin field", var5);
      } catch (IllegalAccessException var6) {
         throw new IOException("unable to set origin field", var6);
      }
   }

   public static class Generic extends ConfigException {
      private static final long serialVersionUID = 1L;

      public Generic(String message, Throwable cause) {
         super(message, cause);
      }

      public Generic(String message) {
         this(message, (Throwable)null);
      }
   }

   public static class BadBean extends BugOrBroken {
      private static final long serialVersionUID = 1L;

      public BadBean(String message, Throwable cause) {
         super(message, cause);
      }

      public BadBean(String message) {
         this(message, (Throwable)null);
      }
   }

   public static class ValidationFailed extends ConfigException {
      private static final long serialVersionUID = 1L;
      private final Iterable problems;

      public ValidationFailed(Iterable problems) {
         super((String)makeMessage(problems), (Throwable)null);
         this.problems = problems;
      }

      public Iterable problems() {
         return this.problems;
      }

      private static String makeMessage(Iterable problems) {
         StringBuilder sb = new StringBuilder();
         Iterator var2 = problems.iterator();

         while(var2.hasNext()) {
            ValidationProblem p = (ValidationProblem)var2.next();
            sb.append(p.origin().description());
            sb.append(": ");
            sb.append(p.path());
            sb.append(": ");
            sb.append(p.problem());
            sb.append(", ");
         }

         if (sb.length() == 0) {
            throw new BugOrBroken("ValidationFailed must have a non-empty list of problems");
         } else {
            sb.setLength(sb.length() - 2);
            return sb.toString();
         }
      }
   }

   public static class ValidationProblem {
      private final String path;
      private final ConfigOrigin origin;
      private final String problem;

      public ValidationProblem(String path, ConfigOrigin origin, String problem) {
         this.path = path;
         this.origin = origin;
         this.problem = problem;
      }

      public String path() {
         return this.path;
      }

      public ConfigOrigin origin() {
         return this.origin;
      }

      public String problem() {
         return this.problem;
      }

      public String toString() {
         return "ValidationProblem(" + this.path + "," + this.origin + "," + this.problem + ")";
      }
   }

   public static class NotResolved extends BugOrBroken {
      private static final long serialVersionUID = 1L;

      public NotResolved(String message, Throwable cause) {
         super(message, cause);
      }

      public NotResolved(String message) {
         this(message, (Throwable)null);
      }
   }

   public static class UnresolvedSubstitution extends Parse {
      private static final long serialVersionUID = 1L;

      public UnresolvedSubstitution(ConfigOrigin origin, String detail, Throwable cause) {
         super(origin, "Could not resolve substitution to a value: " + detail, cause);
      }

      public UnresolvedSubstitution(ConfigOrigin origin, String detail) {
         this(origin, detail, (Throwable)null);
      }
   }

   public static class Parse extends ConfigException {
      private static final long serialVersionUID = 1L;

      public Parse(ConfigOrigin origin, String message, Throwable cause) {
         super(origin, message, cause);
      }

      public Parse(ConfigOrigin origin, String message) {
         this(origin, message, (Throwable)null);
      }
   }

   public static class IO extends ConfigException {
      private static final long serialVersionUID = 1L;

      public IO(ConfigOrigin origin, String message, Throwable cause) {
         super(origin, message, cause);
      }

      public IO(ConfigOrigin origin, String message) {
         this(origin, message, (Throwable)null);
      }
   }

   public static class BugOrBroken extends ConfigException {
      private static final long serialVersionUID = 1L;

      public BugOrBroken(String message, Throwable cause) {
         super(message, cause);
      }

      public BugOrBroken(String message) {
         this(message, (Throwable)null);
      }
   }

   public static class BadPath extends ConfigException {
      private static final long serialVersionUID = 1L;

      public BadPath(ConfigOrigin origin, String path, String message, Throwable cause) {
         super(origin, path != null ? "Invalid path '" + path + "': " + message : message, cause);
      }

      public BadPath(ConfigOrigin origin, String path, String message) {
         this(origin, path, message, (Throwable)null);
      }

      public BadPath(String path, String message, Throwable cause) {
         super(path != null ? "Invalid path '" + path + "': " + message : message, cause);
      }

      public BadPath(String path, String message) {
         this((String)path, message, (Throwable)null);
      }

      public BadPath(ConfigOrigin origin, String message) {
         this((ConfigOrigin)origin, (String)null, (String)message);
      }
   }

   public static class BadValue extends ConfigException {
      private static final long serialVersionUID = 1L;

      public BadValue(ConfigOrigin origin, String path, String message, Throwable cause) {
         super(origin, "Invalid value at '" + path + "': " + message, cause);
      }

      public BadValue(ConfigOrigin origin, String path, String message) {
         this(origin, path, message, (Throwable)null);
      }

      public BadValue(String path, String message, Throwable cause) {
         super("Invalid value at '" + path + "': " + message, cause);
      }

      public BadValue(String path, String message) {
         this((String)path, message, (Throwable)null);
      }
   }

   public static class Null extends Missing {
      private static final long serialVersionUID = 1L;

      private static String makeMessage(String path, String expected) {
         return expected != null ? "Configuration key '" + path + "' is set to null but expected " + expected : "Configuration key '" + path + "' is null";
      }

      public Null(ConfigOrigin origin, String path, String expected, Throwable cause) {
         super(origin, makeMessage(path, expected), cause);
      }

      public Null(ConfigOrigin origin, String path, String expected) {
         this(origin, path, expected, (Throwable)null);
      }
   }

   public static class Missing extends ConfigException {
      private static final long serialVersionUID = 1L;

      public Missing(String path, Throwable cause) {
         super("No configuration setting found for key '" + path + "'", cause);
      }

      public Missing(String path) {
         this((String)path, (Throwable)null);
      }

      protected Missing(ConfigOrigin origin, String message, Throwable cause) {
         super(origin, message, cause);
      }

      protected Missing(ConfigOrigin origin, String message) {
         this(origin, message, (Throwable)null);
      }
   }

   public static class WrongType extends ConfigException {
      private static final long serialVersionUID = 1L;

      public WrongType(ConfigOrigin origin, String path, String expected, String actual, Throwable cause) {
         super(origin, path + " has type " + actual + " rather than " + expected, cause);
      }

      public WrongType(ConfigOrigin origin, String path, String expected, String actual) {
         this(origin, path, expected, actual, (Throwable)null);
      }

      public WrongType(ConfigOrigin origin, String message, Throwable cause) {
         super(origin, message, cause);
      }

      public WrongType(ConfigOrigin origin, String message) {
         super(origin, message, (Throwable)null);
      }
   }
}
