package com.pixelmonmod.pixelmon.quests.client.rendering;

import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class QuestMarkerRenderer {
   @SubscribeEvent
   public void onRenderEvent(RenderWorldLastEvent event) {
      Iterator var2 = QuestDataClient.getInstance().getQuests().iterator();

      label58:
      while(var2.hasNext()) {
         QuestProgressClient progress = (QuestProgressClient)var2.next();
         Iterator var4 = progress.getMarkers().iterator();

         while(true) {
            QuestMarker marker;
            Minecraft mc;
            World world;
            do {
               if (!var4.hasNext()) {
                  continue label58;
               }

               marker = (QuestMarker)var4.next();
               mc = Minecraft.func_71410_x();
               world = mc.field_71439_g.func_130014_f_();
            } while(marker.dim != mc.field_71441_e.field_73011_w.getDimension());

            double x = (double)marker.x;
            double y = (double)marker.y;
            double z = (double)marker.z;
            boolean found = false;
            if (marker.uuid != null) {
               for(int i = 0; i < world.field_72996_f.size(); ++i) {
                  try {
                     Entity entity = (Entity)world.field_72996_f.get(i);
                     if (entity != null && entity.getPersistentID().equals(marker.uuid)) {
                        x = (double)((float)(entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)mc.func_184121_ak()));
                        y = (double)((float)(entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)mc.func_184121_ak()) + entity.func_70047_e()) + 0.8;
                        z = (double)((float)(entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)mc.func_184121_ak()));
                        found = true;
                        break;
                     }
                  } catch (Exception var21) {
                  }
               }
            } else {
               found = true;
            }

            if (found) {
               GlStateManager.func_179094_E();
               GlStateManager.func_179147_l();
               GlStateManager.func_179140_f();
               GlStateManager.func_179112_b(770, 771);
               Entity viewpoint = mc.func_175606_aa() == null ? mc.field_71439_g : mc.func_175606_aa();
               float tx = (float)(((Entity)viewpoint).field_70169_q + (((Entity)viewpoint).field_70165_t - ((Entity)viewpoint).field_70169_q) * (double)mc.func_184121_ak());
               float ty = (float)(((Entity)viewpoint).field_70167_r + (((Entity)viewpoint).field_70163_u - ((Entity)viewpoint).field_70167_r) * (double)mc.func_184121_ak());
               float tz = (float)(((Entity)viewpoint).field_70166_s + (((Entity)viewpoint).field_70161_v - ((Entity)viewpoint).field_70166_s) * (double)mc.func_184121_ak());
               GlStateManager.func_179109_b(-tx, -ty, -tz);
               mc.field_71446_o.func_110577_a(QuestResources.getMarkerTexture(progress, marker));
               QuestColor qc = marker.getColor(world.func_72820_D());
               GlStateManager.func_179131_c(qc.floatR(), qc.floatG(), qc.floatB(), 1.0F);
               GlStateManager.func_179137_b(x, y, z);
               GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
               float angle = (float)(Math.atan2(z - (double)tz, x - (double)tx) * 180.0 / Math.PI) + 90.0F;
               GlStateManager.func_179114_b(angle, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179139_a(0.075, 0.075, 0.075);
               QuestResources.getMarkerModel(marker).render();
               GlStateManager.func_179084_k();
               GlStateManager.func_179145_e();
               GlStateManager.func_179121_F();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            }
         }
      }

   }

   @SubscribeEvent
   public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
      QuestDataClient.getInstance().clear();
   }

   @SubscribeEvent
   public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
      QuestDataClient.getInstance().clear();
   }
}
