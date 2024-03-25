package info.pixelmon.repack.com.typesafe.config;

public final class ConfigRenderOptions {
   private final boolean originComments;
   private final boolean comments;
   private final boolean formatted;
   private final boolean json;

   private ConfigRenderOptions(boolean originComments, boolean comments, boolean formatted, boolean json) {
      this.originComments = originComments;
      this.comments = comments;
      this.formatted = formatted;
      this.json = json;
   }

   public static ConfigRenderOptions defaults() {
      return new ConfigRenderOptions(true, true, true, true);
   }

   public static ConfigRenderOptions concise() {
      return new ConfigRenderOptions(false, false, false, true);
   }

   public ConfigRenderOptions setComments(boolean value) {
      return value == this.comments ? this : new ConfigRenderOptions(this.originComments, value, this.formatted, this.json);
   }

   public boolean getComments() {
      return this.comments;
   }

   public ConfigRenderOptions setOriginComments(boolean value) {
      return value == this.originComments ? this : new ConfigRenderOptions(value, this.comments, this.formatted, this.json);
   }

   public boolean getOriginComments() {
      return this.originComments;
   }

   public ConfigRenderOptions setFormatted(boolean value) {
      return value == this.formatted ? this : new ConfigRenderOptions(this.originComments, this.comments, value, this.json);
   }

   public boolean getFormatted() {
      return this.formatted;
   }

   public ConfigRenderOptions setJson(boolean value) {
      return value == this.json ? this : new ConfigRenderOptions(this.originComments, this.comments, this.formatted, value);
   }

   public boolean getJson() {
      return this.json;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("ConfigRenderOptions(");
      if (this.originComments) {
         sb.append("originComments,");
      }

      if (this.comments) {
         sb.append("comments,");
      }

      if (this.formatted) {
         sb.append("formatted,");
      }

      if (this.json) {
         sb.append("json,");
      }

      if (sb.charAt(sb.length() - 1) == ',') {
         sb.setLength(sb.length() - 1);
      }

      sb.append(")");
      return sb.toString();
   }
}
