package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import java.util.Stack;

final class PathBuilder {
   private final Stack keys = new Stack();
   private Path result;

   private void checkCanAppend() {
      if (this.result != null) {
         throw new ConfigException.BugOrBroken("Adding to PathBuilder after getting result");
      }
   }

   void appendKey(String key) {
      this.checkCanAppend();
      this.keys.push(key);
   }

   void appendPath(Path path) {
      this.checkCanAppend();
      String first = path.first();
      Path remainder = path.remainder();

      while(true) {
         this.keys.push(first);
         if (remainder == null) {
            return;
         }

         first = remainder.first();
         remainder = remainder.remainder();
      }
   }

   Path result() {
      if (this.result == null) {
         Path remainder;
         String key;
         for(remainder = null; !this.keys.isEmpty(); remainder = new Path(key, remainder)) {
            key = (String)this.keys.pop();
         }

         this.result = remainder;
      }

      return this.result;
   }
}
