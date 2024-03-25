package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.client.gui.trainerCard.GuiTrainerCard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class TrainerCardKey extends KeyBinding {
   public TrainerCardKey() {
      super("key.trainercard", 22, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71441_e == null) {
            return;
         }

         RayTraceResult objectMouseOver = TargetKeyBinding.getTarget(false);
         if (objectMouseOver.field_72313_a == Type.ENTITY && objectMouseOver.field_72308_g instanceof EntityPlayer) {
            Minecraft.func_71410_x().func_147108_a(new GuiTrainerCard((EntityPlayer)objectMouseOver.field_72308_g));
         } else {
            Minecraft.func_71410_x().func_147108_a(new GuiTrainerCard());
         }
      }

   }
}
