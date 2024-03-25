package com.pixelmonmod.pixelmon.client.models.obj;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class TextureLoader {
   private static TextureLoader instance = null;
   private HashMap bufferedImageCache = new HashMap();

   private TextureLoader() {
   }

   public static TextureLoader instance() {
      if (instance == null) {
         instance = new TextureLoader();
      }

      return instance;
   }

   Texture[] loadAnimation(String path, int cols, int rows, int textWidth, int textHeight) {
      return this.loadAnimation(path, cols, rows, textWidth, textHeight, 0, 0);
   }

   private Texture[] loadAnimation(String path, int cols, int rows, int textWidth, int textHeight, int xOffSet, int yOffSet) {
      Texture[] toReturntextures = new Texture[cols * rows];

      for(int i = 0; i < rows; ++i) {
         for(int j = 0; j < cols; ++j) {
            toReturntextures[i * cols + j] = this.loadTexture(path, j * textWidth + xOffSet, i * textHeight + yOffSet, textWidth, textHeight);
         }
      }

      return toReturntextures;
   }

   public Texture loadTexture(String path) {
      return this.loadTexture(path, 0, 0, 0, 0);
   }

   private Texture loadTexture(String path, int xOffSet, int yOffSet, int textWidth, int textHeight) {
      Texture toReturn = null;
      BufferedImage buffImage = (BufferedImage)this.bufferedImageCache.get(path);
      if (buffImage == null) {
         try {
            buffImage = ImageIO.read(this.getClass().getResourceAsStream(path));
         } catch (Exception var13) {
            try {
               buffImage = ImageIO.read(new File(path));
            } catch (Exception var12) {
               System.err.println("Could not load path '" + path + "'");
               var13.printStackTrace();
               var12.printStackTrace();
               return null;
            }
         }
      }

      this.bufferedImageCache.put(path, buffImage);
      int bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;
      if (textWidth == 0) {
         textWidth = buffImage.getWidth();
      }

      if (textHeight == 0) {
         textHeight = buffImage.getHeight();
      }

      ByteBuffer scratch = ByteBuffer.allocateDirect(textWidth * textHeight * bytesPerPixel).order(ByteOrder.nativeOrder());
      DataBufferByte data = (DataBufferByte)buffImage.getRaster().getDataBuffer();

      for(int i = 0; i < textHeight; ++i) {
         scratch.put(data.getData(), (xOffSet + (yOffSet + i) * buffImage.getWidth()) * bytesPerPixel, textWidth * bytesPerPixel);
      }

      scratch.rewind();
      IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
      toReturn = new Texture(buf.get(0), textWidth, textHeight);
      return toReturn;
   }
}
