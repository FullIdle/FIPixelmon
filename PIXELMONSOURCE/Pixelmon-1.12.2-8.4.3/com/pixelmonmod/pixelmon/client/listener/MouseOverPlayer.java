package com.pixelmonmod.pixelmon.client.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.CheckPlayerBattle;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class MouseOverPlayer {
   private static final int range = 10;
   private static final int tickInterval = 10;
   private int tick = 0;
   private UUID lastPlayer;

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.type == Type.PLAYER && event.player instanceof EntityPlayerSP && event.player == Minecraft.func_71410_x().field_71439_g) {
         GuiPixelmonOverlay.onPlayerTick();
         ++this.tick;
         if (this.tick >= 10) {
            this.tick = 0;
            EntityPlayer player = this.getLookAtPlayer();
            if (player != null) {
               if (!player.func_110124_au().equals(this.lastPlayer)) {
                  this.sendCheckBattlePacket(player);
                  this.lastPlayer = player.func_110124_au();
               }
            } else {
               if (this.lastPlayer != null) {
                  GuiPixelmonOverlay.hideSpectateMessage(this.lastPlayer);
               }

               this.lastPlayer = null;
            }
         }
      }

   }

   private void sendCheckBattlePacket(EntityPlayer player) {
      CheckPlayerBattle message = new CheckPlayerBattle(player);
      Pixelmon.network.sendToServer(message);
   }

   private EntityPlayer getLookAtPlayer() {
      Minecraft mc = Minecraft.func_71410_x();
      EntityPlayer pointedEntity = null;
      float partialTick = 1.0F;
      Vec3d Vec3d = mc.func_175606_aa().func_174824_e(partialTick);
      Vec3d Vec3d1 = mc.func_175606_aa().func_70676_i(partialTick);
      Vec3d Vec3d2 = Vec3d.func_72441_c(Vec3d1.field_72450_a * 10.0, Vec3d1.field_72448_b * 10.0, Vec3d1.field_72449_c * 10.0);
      double d1 = 10.0;
      Vec3d = mc.func_175606_aa().func_174824_e(partialTick);
      float f1 = 1.0F;
      List list = mc.field_71441_e.func_72872_a(EntityPlayer.class, mc.func_175606_aa().func_174813_aQ().func_72321_a(Vec3d1.field_72450_a * 10.0, Vec3d1.field_72448_b * 10.0, Vec3d1.field_72449_c * 10.0).func_72321_a((double)f1, (double)f1, (double)f1));
      double d2 = d1;
      Iterator var13 = list.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            while(true) {
               do {
                  do {
                     if (!var13.hasNext()) {
                        return pointedEntity;
                     }

                     entity = (EntityPlayer)var13.next();
                  } while(entity == Minecraft.func_71410_x().field_71439_g);
               } while(!entity.func_70067_L());

               float f2 = entity.func_70111_Y();
               AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72321_a((double)f2, (double)f2, (double)f2);
               RayTraceResult movingobjectposition = axisalignedbb.func_72327_a(Vec3d, Vec3d2);
               if (axisalignedbb.func_72318_a(Vec3d)) {
                  break;
               }

               if (movingobjectposition != null) {
                  double d3 = Vec3d.func_72438_d(movingobjectposition.field_72307_f);
                  if (d3 < d2 || d2 == 0.0) {
                     if (entity == mc.func_175606_aa().func_184179_bs() && !entity.canRiderInteract()) {
                        if (d2 == 0.0) {
                           pointedEntity = entity;
                        }
                     } else {
                        pointedEntity = entity;
                        d2 = d3;
                     }
                  }
               }
            }
         } while(!(0.0 < d2) && d2 != 0.0);

         pointedEntity = entity;
         d2 = 0.0;
      }
   }
}
