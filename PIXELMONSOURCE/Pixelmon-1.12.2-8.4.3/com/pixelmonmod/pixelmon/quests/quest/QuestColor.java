package com.pixelmonmod.pixelmon.quests.quest;

import io.netty.buffer.ByteBuf;
import java.awt.Color;

public class QuestColor {
   private int r;
   private int g;
   private int b;

   public QuestColor(int r, int g, int b) {
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public QuestColor(int rgb) {
      Color color = new Color(rgb);
      this.r = color.getRed();
      this.g = color.getGreen();
      this.b = color.getBlue();
   }

   public QuestColor(ByteBuf buf) {
      this.readFromByteBuf(buf);
   }

   public int getRGB() {
      return (new Color(this.r, this.g, this.b)).getRGB();
   }

   public float floatR() {
      return (float)this.r / 255.0F;
   }

   public float floatG() {
      return (float)this.g / 255.0F;
   }

   public float floatB() {
      return (float)this.b / 255.0F;
   }

   public int getR() {
      return this.r;
   }

   public int getG() {
      return this.g;
   }

   public int getB() {
      return this.b;
   }

   public void setR(int r) {
      this.r = r;
   }

   public void setG(int g) {
      this.g = g;
   }

   public void setB(int b) {
      this.b = b;
   }

   public void readFromByteBuf(ByteBuf buf) {
      this.r = buf.readInt();
      this.g = buf.readInt();
      this.b = buf.readInt();
   }

   public void writeToByteBuf(ByteBuf buf) {
      buf.writeInt(this.r);
      buf.writeInt(this.g);
      buf.writeInt(this.b);
   }
}
