package com.pixelmonmod.pixelmon.client.gui.starter;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.IShadowDelete;
import com.pixelmonmod.pixelmon.client.gui.selectPokemon.MoveDirection;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class Shadow {
   EnumShadow shadowType;
   MoveDirection moveDirection;
   IShadowDelete parent;
   float position;
   float yPos;
   float moveSpeed;

   public Shadow(EnumShadow shadowType, IShadowDelete parent) {
      this.shadowType = shadowType;
      this.parent = parent;
      Random r = RandomHelper.rand;
      this.moveDirection = r.nextFloat() < 0.5F ? MoveDirection.Left : MoveDirection.Right;
      if (this.moveDirection == MoveDirection.Left) {
         this.position = 1.0F;
      } else {
         this.position = 0.0F;
      }

      this.moveSpeed = r.nextFloat() * shadowType.moveSpeedModifier * 0.01F + 0.001F;
      this.yPos = r.nextFloat();
   }

   public Shadow(EnumShadow shadowType, IShadowDelete parent, float startPos) {
      this(shadowType, parent);
      this.position = startPos;
   }

   public void update() {
      if (this.moveDirection == MoveDirection.Left) {
         this.position -= this.moveSpeed;
         if (this.position <= 0.0F) {
            this.parent.removeShadow(this);
         }
      } else {
         this.position += this.moveSpeed;
         if (this.position >= 1.0F) {
            this.parent.removeShadow(this);
         }
      }

   }

   public void draw(Minecraft mc, int screenWidth, int screenHeight) {
      mc.field_71446_o.func_110577_a(GuiResources.shadow);
      float totWidth = (float)(screenWidth + this.shadowType.width * 2);
      float x = totWidth * this.position - (float)this.shadowType.width;
      float totHeight = (float)(screenHeight - 50);
      float y = this.yPos * totHeight - (float)this.shadowType.height + 50.0F;
      GlStateManager.func_179147_l();
      GuiHelper.drawImageQuad((double)((int)x), (double)((int)y), (double)this.shadowType.width, (float)this.shadowType.height, 0.0, 0.0, 1.0, 1.0, 0.0F);
   }
}
