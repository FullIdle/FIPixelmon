package info.pixelmon.repack.com.typesafe.config.impl;

final class SubstitutionExpression {
   private final Path path;
   private final boolean optional;

   SubstitutionExpression(Path path, boolean optional) {
      this.path = path;
      this.optional = optional;
   }

   Path path() {
      return this.path;
   }

   boolean optional() {
      return this.optional;
   }

   SubstitutionExpression changePath(Path newPath) {
      return newPath == this.path ? this : new SubstitutionExpression(newPath, this.optional);
   }

   public String toString() {
      return "${" + (this.optional ? "?" : "") + this.path.render() + "}";
   }

   public boolean equals(Object other) {
      if (!(other instanceof SubstitutionExpression)) {
         return false;
      } else {
         SubstitutionExpression otherExp = (SubstitutionExpression)other;
         return otherExp.path.equals(this.path) && otherExp.optional == this.optional;
      }
   }

   public int hashCode() {
      int h = 41 * (41 + this.path.hashCode());
      h = 41 * (h + (this.optional ? 1 : 0));
      return h;
   }
}
