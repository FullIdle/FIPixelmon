package com.pixelmonmod.pixelmon.client.models.obj;

public class Texture {
   private int textureID;
   private int width;
   private int height;
   private float widthRatio;
   private float heightRatio;
   private int textureWidth;
   private int textureHeigth;

   public Texture(int textureID, int width, int height) {
      this(textureID, width, height, 1.0F, 1.0F, width, height);
   }

   public Texture(int textureID, int width, int height, float widthRatio, float heightRatio, int textureWidth, int textureHeight) {
      this.textureID = textureID;
      this.width = width;
      this.height = height;
      this.widthRatio = widthRatio;
      this.heightRatio = heightRatio;
      this.textureWidth = textureWidth;
      this.textureHeigth = textureHeight;
   }

   public int getTextureID() {
      return this.textureID;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public String toString() {
      return "Texture [" + this.textureID + ", " + this.width + ", " + this.height + ", " + this.textureWidth + ", " + this.textureHeigth + ", " + this.widthRatio + ", " + this.heightRatio + "]";
   }
}
