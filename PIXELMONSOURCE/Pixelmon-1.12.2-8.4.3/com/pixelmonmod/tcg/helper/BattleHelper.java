package com.pixelmonmod.tcg.helper;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.CameraTargetEntity;
import com.pixelmonmod.pixelmon.client.camera.ICameraTarget;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BattleHelper {
   public static int thirdP = 0;

   public static void setViewEntity(Entity entity) {
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_175607_a(entity);
      Minecraft.func_71410_x().field_71474_y.field_74319_N = true;
      thirdP = mc.field_71474_y.field_74320_O;
      mc.field_71474_y.field_74320_O = 0;
      GuiIngameForge.renderHotbar = false;
      GuiIngameForge.renderCrosshairs = false;
      GuiIngameForge.renderExperiance = false;
      GuiIngameForge.renderAir = false;
      GuiIngameForge.renderHealth = false;
      GuiIngameForge.renderFood = false;
      GuiIngameForge.renderArmor = false;
   }

   public static Entity getViewEntity() {
      return Minecraft.func_71410_x().func_175606_aa();
   }

   public static void resetViewEntity() {
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_175607_a(mc.field_71439_g);
      GuiIngameForge.renderHotbar = true;
      GuiIngameForge.renderCrosshairs = true;
      GuiIngameForge.renderExperiance = true;
      GuiIngameForge.renderAir = true;
      GuiIngameForge.renderHealth = true;
      GuiIngameForge.renderFood = true;
      GuiIngameForge.renderArmor = true;
      mc.field_71474_y.field_74319_N = false;
      mc.field_71474_y.field_74320_O = thirdP;
      if (ClientProxy.camera != null) {
         ClientProxy.camera.func_70106_y();
      }

      ClientProxy.camera = null;
   }

   public static EntityPlayer getViewPlayer() {
      Minecraft minecraft = Minecraft.func_71410_x();
      return minecraft.field_71439_g;
   }

   public static void setCameraToPlayer() {
      if (ClientProxy.camera != null) {
         ICameraTarget tar = ClientProxy.camera.getTarget();
         if (tar != null) {
            if (tar.getTargetData() != getViewPlayer()) {
               if (tar instanceof CameraTargetEntity) {
                  tar.setTargetData(getViewPlayer());
               } else {
                  ClientProxy.camera.setTarget(new CameraTargetEntity(getViewPlayer()));
               }
            }
         } else if (ClientProxy.camera != null && getViewPlayer() != null) {
            ClientProxy.camera.setTarget(new CameraTargetEntity(getViewPlayer()));
         } else {
            Pixelmon.LOGGER.warn("Problem finding battle camera");
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public static void alertServerOfCardDrop(int posIdentifier, ImmutableCard card) {
   }
}
