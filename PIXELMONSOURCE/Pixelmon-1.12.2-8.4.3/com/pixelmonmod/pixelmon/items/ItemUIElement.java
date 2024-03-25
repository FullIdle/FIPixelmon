package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.TextJustification;
import java.util.Optional;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUIElement extends PixelmonItem {
   public static final String BAKE_MODEL = "UIBakeModel";
   public static final String IMAGE = "UIImage";
   public static final String HOVER = "UIHoverImage";
   public static final String TEXT = "UIText";
   public static final String HOVER_TEXT = "UIHoverText";
   public static final String TEXT_JUSTIFICATION = "UITextJustification";
   public static final String TEXT_SCALE = "UITextScale";
   public static final String X_OFFSET = "UIXOffset";
   public static final String Y_OFFSET = "UIYOffset";
   public static final String X_OVERRIDE = "UIXOverride";
   public static final String Y_OVERRIDE = "UIYOverride";
   public static final String Z_LEVEL = "UIZLevel";
   public static final String IMAGE_WIDTH = "UIImageWidth";
   public static final String IMAGE_HEIGHT = "UIImageHeight";
   public static final String TEX_WIDTH = "UITexWidth";
   public static final String TEX_HEIGHT = "UITexHeight";
   public static final String IMAGE_U = "UIImageU";
   public static final String IMAGE_V = "UIImageV";
   public static final String R = "UIImageR";
   public static final String G = "UIImageG";
   public static final String B = "UIImageB";
   public static final String A = "UIImageA";
   public static final String HR = "UIImageHR";
   public static final String HG = "UIImageHG";
   public static final String HB = "UIImageHB";
   public static final String HA = "UIImageHA";

   public ItemUIElement() {
      super("ui_element");
      this.func_77625_d(Integer.MAX_VALUE);
      this.func_77656_e(Integer.MAX_VALUE);
      this.func_77637_a((CreativeTabs)null);
   }

   public static Builder builder() {
      return new Builder(new ItemStack(PixelmonItems.uiElement));
   }

   public static Builder builder(ItemStack stack) {
      return new Builder(stack);
   }

   @SideOnly(Side.CLIENT)
   public static boolean isBakedModel(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIBakeModel") ? tag.func_74767_n("UIBakeModel") : false;
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImage(ItemStack stack, boolean hover) {
      return hover ? getHoverImage(stack) : getImage(stack);
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImage(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      if (tag != null && tag.func_74764_b("UIImage")) {
         ResourceLocation rl = new ResourceLocation(tag.func_74779_i("UIImage"));
         return Optional.of(rl);
      } else {
         return Optional.empty();
      }
   }

   @SideOnly(Side.CLIENT)
   public static Optional getHoverImage(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      if (tag != null && tag.func_74764_b("UIHoverImage")) {
         ResourceLocation rl = new ResourceLocation(tag.func_74779_i("UIHoverImage"));
         return Optional.of(rl);
      } else {
         return getImage(stack);
      }
   }

   @SideOnly(Side.CLIENT)
   public static Optional getXOffset(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIXOffset") ? Optional.of(tag.func_74762_e("UIXOffset")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getYOffset(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIYOffset") ? Optional.of(tag.func_74762_e("UIYOffset")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getXOverride(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIXOverride") ? Optional.of(tag.func_74762_e("UIXOverride")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getYOverride(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIYOverride") ? Optional.of(tag.func_74762_e("UIYOverride")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getZLevel(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIZLevel") ? Optional.of(tag.func_74762_e("UIZLevel")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImageWidth(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageWidth") ? Optional.of(tag.func_74762_e("UIImageWidth")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImageHeight(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageHeight") ? Optional.of(tag.func_74762_e("UIImageHeight")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getTextureWidth(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UITexWidth") ? Optional.of(tag.func_74760_g("UITexWidth")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getTextureHeight(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UITexHeight") ? Optional.of(tag.func_74760_g("UITexHeight")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImageU(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageU") ? Optional.of(tag.func_74760_g("UIImageU")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getImageV(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageV") ? Optional.of(tag.func_74760_g("UIImageV")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getColor(ItemStack stack, boolean hover) {
      return hover ? getHoverColor(stack) : getColor(stack);
   }

   @SideOnly(Side.CLIENT)
   public static Optional getColor(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageR") ? Optional.of(new float[]{tag.func_74760_g("UIImageR"), tag.func_74760_g("UIImageG"), tag.func_74760_g("UIImageB"), tag.func_74760_g("UIImageA")}) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getHoverColor(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIImageHR") ? Optional.of(new float[]{tag.func_74760_g("UIImageHR"), tag.func_74760_g("UIImageHG"), tag.func_74760_g("UIImageHB"), tag.func_74760_g("UIImageHA")}) : getColor(stack);
   }

   @SideOnly(Side.CLIENT)
   public static Optional getText(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIText") ? Optional.of(tag.func_74779_i("UIText")) : Optional.empty();
   }

   @SideOnly(Side.CLIENT)
   public static Optional getHoverText(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UIHoverText") ? Optional.of(tag.func_74779_i("UIHoverText")) : getText(stack);
   }

   @SideOnly(Side.CLIENT)
   public static Optional getText(ItemStack stack, boolean hover) {
      return hover ? getHoverText(stack) : getText(stack);
   }

   @SideOnly(Side.CLIENT)
   public static TextJustification getTextJustification(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UITextJustification") ? TextJustification.get(tag.func_74765_d("UITextJustification")) : TextJustification.RIGHT;
   }

   @SideOnly(Side.CLIENT)
   public static short getTextScale(ItemStack stack) {
      NBTTagCompound tag = stack.func_77978_p();
      return tag != null && tag.func_74764_b("UITextScale") ? tag.func_74765_d("UITextScale") : 12;
   }

   public static class Builder {
      private final ItemStack stack;

      private Builder(ItemStack stack) {
         this.stack = stack;
         if (!this.stack.func_77942_o()) {
            this.stack.func_77982_d(new NBTTagCompound());
         }

      }

      public Builder setBakeModel(boolean bakeModel) {
         this.stack.func_77978_p().func_74757_a("UIBakeModel", bakeModel);
         return this;
      }

      public Builder setImage(String image) {
         this.stack.func_77978_p().func_74778_a("UIImage", image);
         return this;
      }

      public Builder setHoverImage(String image) {
         this.stack.func_77978_p().func_74778_a("UIHoverImage", image);
         return this;
      }

      public Builder setText(String text) {
         this.stack.func_77978_p().func_74778_a("UIText", text);
         return this;
      }

      public Builder setHoverText(String text) {
         this.stack.func_77978_p().func_74778_a("UIHoverText", text);
         return this;
      }

      public Builder setTextJustification(TextJustification justification) {
         this.stack.func_77978_p().func_74777_a("UITextJustification", (short)(justification.ordinal() - 1));
         return this;
      }

      public Builder setTextScale(float scale) {
         this.stack.func_77978_p().func_74776_a("UITextScale", scale);
         return this;
      }

      public Builder setPosOffset(int x, int y) {
         this.stack.func_77978_p().func_74768_a("UIXOffset", x);
         this.stack.func_77978_p().func_74768_a("UIYOffset", y);
         return this;
      }

      public Builder setPosOverride(int x, int y) {
         this.stack.func_77978_p().func_74768_a("UIXOverride", x);
         this.stack.func_77978_p().func_74768_a("UIYOverride", y);
         return this;
      }

      public Builder setZLevel(int zLevel) {
         this.stack.func_77978_p().func_74768_a("UIZLevel", zLevel);
         return this;
      }

      public Builder setSize(int w, int h) {
         this.stack.func_77978_p().func_74768_a("UIImageWidth", w);
         this.stack.func_77978_p().func_74768_a("UIImageHeight", h);
         return this;
      }

      public Builder setTextureSize(float w, float h) {
         this.stack.func_77978_p().func_74776_a("UITexWidth", w);
         this.stack.func_77978_p().func_74776_a("UITexHeight", h);
         return this;
      }

      public Builder setUV(float u, float v) {
         this.stack.func_77978_p().func_74776_a("UIImageU", u);
         this.stack.func_77978_p().func_74776_a("UIImageV", v);
         return this;
      }

      public Builder setColor(int r, int g, int b, int a) {
         return this.setColor((float)r / 255.0F, (float)g / 255.0F, (float)b / 255.0F, (float)a / 255.0F);
      }

      public Builder setColor(float r, float g, float b, float a) {
         this.stack.func_77978_p().func_74776_a("UIImageR", r);
         this.stack.func_77978_p().func_74776_a("UIImageG", g);
         this.stack.func_77978_p().func_74776_a("UIImageB", b);
         this.stack.func_77978_p().func_74776_a("UIImageA", a);
         return this;
      }

      public Builder setHoverColor(int r, int g, int b, int a) {
         return this.setHoverColor((float)r / 255.0F, (float)g / 255.0F, (float)b / 255.0F, (float)a / 255.0F);
      }

      public Builder setHoverColor(float r, float g, float b, float a) {
         this.stack.func_77978_p().func_74776_a("UIImageHR", r);
         this.stack.func_77978_p().func_74776_a("UIImageHG", g);
         this.stack.func_77978_p().func_74776_a("UIImageHB", b);
         this.stack.func_77978_p().func_74776_a("UIImageHA", a);
         return this;
      }

      public ItemStack build() {
         return this.stack;
      }

      // $FF: synthetic method
      Builder(ItemStack x0, Object x1) {
         this(x0);
      }
   }
}
