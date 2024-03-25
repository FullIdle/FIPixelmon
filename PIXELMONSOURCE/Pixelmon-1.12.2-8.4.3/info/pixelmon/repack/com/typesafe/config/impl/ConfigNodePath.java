package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import java.util.ArrayList;
import java.util.Collection;

final class ConfigNodePath extends AbstractConfigNode {
   private final Path path;
   final ArrayList tokens;

   ConfigNodePath(Path path, Collection tokens) {
      this.path = path;
      this.tokens = new ArrayList(tokens);
   }

   protected Collection tokens() {
      return this.tokens;
   }

   protected Path value() {
      return this.path;
   }

   protected ConfigNodePath subPath(int toRemove) {
      int periodCount = 0;
      ArrayList tokensCopy = new ArrayList(this.tokens);

      for(int i = 0; i < tokensCopy.size(); ++i) {
         if (Tokens.isUnquotedText((Token)tokensCopy.get(i)) && ((Token)tokensCopy.get(i)).tokenText().equals(".")) {
            ++periodCount;
         }

         if (periodCount == toRemove) {
            return new ConfigNodePath(this.path.subPath(toRemove), tokensCopy.subList(i + 1, tokensCopy.size()));
         }
      }

      throw new ConfigException.BugOrBroken("Tried to remove too many elements from a Path node");
   }

   protected ConfigNodePath first() {
      ArrayList tokensCopy = new ArrayList(this.tokens);

      for(int i = 0; i < tokensCopy.size(); ++i) {
         if (Tokens.isUnquotedText((Token)tokensCopy.get(i)) && ((Token)tokensCopy.get(i)).tokenText().equals(".")) {
            return new ConfigNodePath(this.path.subPath(0, 1), tokensCopy.subList(0, i));
         }
      }

      return this;
   }
}
