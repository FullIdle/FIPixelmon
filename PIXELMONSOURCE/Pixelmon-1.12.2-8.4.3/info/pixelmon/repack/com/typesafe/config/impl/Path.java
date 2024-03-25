package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import java.util.Iterator;
import java.util.List;

final class Path {
   private final String first;
   private final Path remainder;

   Path(String first, Path remainder) {
      this.first = first;
      this.remainder = remainder;
   }

   Path(String... elements) {
      if (elements.length == 0) {
         throw new ConfigException.BugOrBroken("empty path");
      } else {
         this.first = elements[0];
         if (elements.length > 1) {
            PathBuilder pb = new PathBuilder();

            for(int i = 1; i < elements.length; ++i) {
               pb.appendKey(elements[i]);
            }

            this.remainder = pb.result();
         } else {
            this.remainder = null;
         }

      }
   }

   Path(List pathsToConcat) {
      this(pathsToConcat.iterator());
   }

   Path(Iterator i) {
      if (!i.hasNext()) {
         throw new ConfigException.BugOrBroken("empty path");
      } else {
         Path firstPath = (Path)i.next();
         this.first = firstPath.first;
         PathBuilder pb = new PathBuilder();
         if (firstPath.remainder != null) {
            pb.appendPath(firstPath.remainder);
         }

         while(i.hasNext()) {
            pb.appendPath((Path)i.next());
         }

         this.remainder = pb.result();
      }
   }

   String first() {
      return this.first;
   }

   Path remainder() {
      return this.remainder;
   }

   Path parent() {
      if (this.remainder == null) {
         return null;
      } else {
         PathBuilder pb = new PathBuilder();

         for(Path p = this; p.remainder != null; p = p.remainder) {
            pb.appendKey(p.first);
         }

         return pb.result();
      }
   }

   String last() {
      Path p;
      for(p = this; p.remainder != null; p = p.remainder) {
      }

      return p.first;
   }

   Path prepend(Path toPrepend) {
      PathBuilder pb = new PathBuilder();
      pb.appendPath(toPrepend);
      pb.appendPath(this);
      return pb.result();
   }

   int length() {
      int count = 1;

      for(Path p = this.remainder; p != null; p = p.remainder) {
         ++count;
      }

      return count;
   }

   Path subPath(int removeFromFront) {
      int count = removeFromFront;

      Path p;
      for(p = this; p != null && count > 0; p = p.remainder) {
         --count;
      }

      return p;
   }

   Path subPath(int firstIndex, int lastIndex) {
      if (lastIndex < firstIndex) {
         throw new ConfigException.BugOrBroken("bad call to subPath");
      } else {
         Path from = this.subPath(firstIndex);
         PathBuilder pb = new PathBuilder();
         int count = lastIndex - firstIndex;

         do {
            if (count <= 0) {
               return pb.result();
            }

            --count;
            pb.appendKey(from.first());
            from = from.remainder();
         } while(from != null);

         throw new ConfigException.BugOrBroken("subPath lastIndex out of range " + lastIndex);
      }
   }

   boolean startsWith(Path other) {
      Path myRemainder = this;
      Path otherRemainder = other;
      if (other.length() <= this.length()) {
         while(otherRemainder != null) {
            if (!otherRemainder.first().equals(myRemainder.first())) {
               return false;
            }

            myRemainder = myRemainder.remainder();
            otherRemainder = otherRemainder.remainder();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof Path)) {
         return false;
      } else {
         Path that = (Path)other;
         return this.first.equals(that.first) && ConfigImplUtil.equalsHandlingNull(this.remainder, that.remainder);
      }
   }

   public int hashCode() {
      return 41 * (41 + this.first.hashCode()) + (this.remainder == null ? 0 : this.remainder.hashCode());
   }

   static boolean hasFunkyChars(String s) {
      int length = s.length();
      if (length == 0) {
         return false;
      } else {
         for(int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_') {
               return true;
            }
         }

         return false;
      }
   }

   private void appendToStringBuilder(StringBuilder sb) {
      if (!hasFunkyChars(this.first) && !this.first.isEmpty()) {
         sb.append(this.first);
      } else {
         sb.append(ConfigImplUtil.renderJsonString(this.first));
      }

      if (this.remainder != null) {
         sb.append(".");
         this.remainder.appendToStringBuilder(sb);
      }

   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Path(");
      this.appendToStringBuilder(sb);
      sb.append(")");
      return sb.toString();
   }

   String render() {
      StringBuilder sb = new StringBuilder();
      this.appendToStringBuilder(sb);
      return sb.toString();
   }

   static Path newKey(String key) {
      return new Path(key, (Path)null);
   }

   static Path newPath(String path) {
      return PathParser.parsePath(path);
   }
}
