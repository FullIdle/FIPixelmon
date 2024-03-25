package com.pixelmonmod.pixelmon.tools;

public class Quarternion {
   public float w;
   public float x;
   public float y;
   public float z;

   public Quarternion(float w, float x, float y, float z) {
      this.w = w;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void multiplyBy(Quarternion q) {
      float w1 = this.w;
      float w2 = q.w;
      float x1 = this.x;
      float x2 = q.x;
      float y1 = this.y;
      float y2 = q.y;
      float z1 = this.z;
      float z2 = q.z;
      this.w = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
      this.x = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2;
      this.y = w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2;
      this.z = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2;
   }

   public static Quarternion toQuarternion(float w, float x, float y, float z) {
      Quarternion q = new Quarternion((float)Math.cos((double)w * Math.PI / 360.0), (float)Math.sin((double)w * Math.PI / 360.0) * x, (float)Math.sin((double)w * Math.PI / 360.0) * y, (float)Math.sin((double)w * Math.PI / 360.0) * z);
      return q;
   }

   public void fromQuarternion(Quarternion q) {
      float scale = (float)Math.sqrt((double)(q.x * q.x + q.y * q.y + q.z * q.z));
      this.w = (float)(Math.acos((double)q.w) * 360.0 / Math.PI);
      this.x = q.x / scale;
      this.y = q.y / scale;
      this.z = q.z / scale;
   }
}
