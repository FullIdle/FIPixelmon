package com.pixelmonmod.pixelmon.client.render.player;

import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class ModelPlayerOnBike extends ModelPlayer {
   public ModelPlayerOnBike(float modelSize, boolean smallArmsIn) {
      super(modelSize, smallArmsIn);
   }

   public void func_78087_a(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      EntityPlayer player = (EntityPlayer)entityIn;
      EntityBike bike = (EntityBike)player.func_184187_bx();
      if (bike != null) {
         float armOffsetSide = 4.75F;
         float armOffsetForward = 3.0F;
         float armAngle = 1.2F;
         this.field_78116_c.field_78795_f = (float)Math.toRadians(15.0);
         this.field_78116_c.field_82907_q = -0.2F;
         this.field_78115_e.field_78795_f = (float)Math.toRadians(10.0);
         this.field_78115_e.field_82907_q = -0.2F;
         this.field_178724_i.field_78795_f = -armAngle;
         this.field_178724_i.field_78808_h = 0.31415927F;
         this.field_178724_i.field_82907_q = -0.2F;
         this.field_178723_h.field_78795_f = -armAngle;
         this.field_178723_h.field_78808_h = -0.31415927F;
         this.field_178723_h.field_82907_q = -0.2F;
         ModelRenderer var10000 = this.field_178723_h;
         var10000.field_78808_h += MathHelper.func_76134_b((float)player.field_70173_aa * 0.09F) * 0.025F + 0.05F;
         var10000 = this.field_178724_i;
         var10000.field_78808_h -= MathHelper.func_76134_b((float)player.field_70173_aa * 0.09F) * 0.025F + 0.05F;
         var10000 = this.field_178723_h;
         var10000.field_78795_f += MathHelper.func_76126_a((float)player.field_70173_aa * 0.067F) * 0.025F;
         var10000 = this.field_178724_i;
         var10000.field_78795_f -= MathHelper.func_76126_a((float)player.field_70173_aa * 0.067F) * 0.025F;
         float legOffsetSide = 2.25F;
         float legOffsetHeight = 12.0F;
         float legOffsetForward = 0.5F;
         float rotation = 0.0F;
         float offsetYLeft = 0.0F;
         float offsetZLeft = 0.0F;
         float offsetYRight = 0.0F;
         float offsetZRight = 0.0F;
         float offsetYMultiplier = 0.6F;
         float offsetZMultiplier = 1.0F;
         if (bike.inc != null) {
            rotation = bike.inc.value / 6.4F % 360.0F;
            offsetYLeft = (float)Math.sin((double)rotation + Math.PI) * offsetYMultiplier;
            offsetZLeft = -((float)Math.cos((double)rotation + Math.PI)) * offsetZMultiplier;
            offsetYRight = (float)Math.sin((double)rotation) * offsetYMultiplier;
            offsetZRight = -((float)Math.cos((double)rotation)) * offsetZMultiplier;
         }

         this.field_178722_k.field_82906_o = 0.05F;
         this.field_178721_j.field_82906_o = -0.05F;
         this.field_178722_k.field_78798_e = legOffsetSide;
         this.field_178722_k.field_78797_d = legOffsetHeight + offsetYLeft;
         this.field_178722_k.field_78800_c = legOffsetSide;
         var10000 = this.field_178722_k;
         var10000.field_78798_e += legOffsetForward + offsetZLeft;
         this.field_178721_j.field_78798_e = legOffsetSide;
         this.field_178721_j.field_78797_d = legOffsetHeight + offsetYRight;
         this.field_178721_j.field_78800_c = -legOffsetSide;
         var10000 = this.field_178721_j;
         var10000.field_78798_e += legOffsetForward + offsetZRight;
         this.field_178722_k.field_82907_q = -0.25F;
         this.field_178721_j.field_82907_q = -0.25F;
         this.field_178722_k.field_78795_f = -0.2F + (float)(-Math.cos((double)rotation + Math.PI)) / 8.0F;
         this.field_178721_j.field_78795_f = -0.2F + (float)(-Math.cos((double)rotation)) / 8.0F;
         copyModelSettings(this.field_178722_k, this.field_178733_c);
         copyModelSettings(this.field_178721_j, this.field_178731_d);
         copyModelSettings(this.field_178724_i, this.field_178734_a);
         copyModelSettings(this.field_178723_h, this.field_178732_b);
         copyModelSettings(this.field_78115_e, this.field_178730_v);
         copyModelSettings(this.field_78116_c, this.field_178720_f);
      }
   }

   public static void copyModelSettings(ModelRenderer source, ModelRenderer dest) {
      func_178685_a(source, dest);
      dest.field_82906_o = source.field_82906_o;
      dest.field_82908_p = source.field_82908_p;
      dest.field_82907_q = source.field_82907_q;
   }
}
