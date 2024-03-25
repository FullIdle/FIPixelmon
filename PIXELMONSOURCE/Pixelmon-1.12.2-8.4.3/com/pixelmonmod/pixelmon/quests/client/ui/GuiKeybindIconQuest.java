package com.pixelmonmod.pixelmon.quests.client.ui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiKeybindIcon;
import com.pixelmonmod.pixelmon.quests.client.ObjectiveDetail;
import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class GuiKeybindIconQuest extends GuiKeybindIcon {
   private final KeyBinding keybindA;
   private final KeyBinding keybindB;
   private boolean enabled = true;

   public GuiKeybindIconQuest(KeyBinding keybindA, KeyBinding keybindB) {
      super((KeyBinding)null, (ResourceLocation)null);
      this.keybindA = keybindA;
      this.keybindB = keybindB;
   }

   public void draw(int x, int y, float zLevel) {
      if (this.enabled) {
         Minecraft mc = Minecraft.func_71410_x();
         FontRenderer f = Minecraft.func_71410_x().field_71466_p;
         boolean flag = f.func_82883_a();
         f.func_78264_a(true);
         f.func_78276_b(GameSettings.func_74298_c(this.keybindA.func_151463_i()), x + 20, y + 17, 16777215);
         f.func_78276_b(GameSettings.func_74298_c(this.keybindB.func_151463_i()), x + 20, y - 5, 16777215);
         boolean drawnArrow = false;
         QuestProgressClient qpc = QuestDataClient.getInstance().getDisplayQuest();
         if (qpc != null && qpc.getObjectives() != null) {
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179147_l();
            String name = qpc.getName();
            GuiHelper.drawStringRightAligned(name, (float)(x - 3), (float)(y + 1), 16777215);
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b(-0.5, 0.0, 0.0);
            GuiHelper.drawStringRightAligned(name, (float)(x - 3), (float)(y + 1), 16777215);
            GlStateManager.func_179121_F();
            Iterator var10;
            if (qpc.getObjectives() != null) {
               var10 = qpc.getObjectives().iterator();

               while(var10.hasNext()) {
                  ObjectiveDetail od = (ObjectiveDetail)var10.next();
                  if (!od.isComplete()) {
                     String detail = qpc.format(od);
                     if (!detail.isEmpty()) {
                        GuiHelper.drawStringRightAligned(detail, (float)(x - 3), (float)(y + 9), 16777215);
                        break;
                     }
                  }
               }
            }

            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            mc.field_71446_o.func_110577_a(qpc.isComplete() ? GuiResources.question_mark : qpc.getIcon().getResource());
            if (!qpc.isComplete() && qpc.getColor() != null) {
               QuestColor color = qpc.getColor();
               GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
            } else {
               GlStateManager.func_179131_c(0.8509804F, 0.8509804F, 0.8509804F, 1.0F);
            }

            var10 = qpc.getMarkers().iterator();

            while(var10.hasNext()) {
               QuestMarker marker = (QuestMarker)var10.next();
               if (this.drawArrow(marker, (double)(x - 1), (double)(y + 1), zLevel)) {
                  drawnArrow = true;
                  break;
               }
            }
         } else {
            mc.field_71446_o.func_110577_a(GuiResources.exclamation_mark);
            GlStateManager.func_179131_c(0.8509804F, 0.8509804F, 0.8509804F, 1.0F);
         }

         if (!drawnArrow) {
            GuiHelper.drawImageQuad((double)(x - 2), (double)y, 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, zLevel);
         }

         f.func_78264_a(flag);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   private float getYawFromVec3(Vec3d vec, EntityPlayer player) {
      double dx = vec.field_72450_a;
      double dz = vec.field_72449_c;
      double yaw = 0.0;
      if (dx != 0.0) {
         if (dx < 0.0) {
            yaw = 4.71238898038469;
         } else {
            yaw = 1.5707963267948966;
         }

         yaw -= Math.atan(dz / dx);
      } else if (dz < 0.0) {
         yaw = Math.PI;
      }

      return (float)(-yaw * 180.0 / Math.PI - 90.0 - (double)player.field_70177_z) + 90.0F;
   }

   private boolean drawArrow(QuestMarker marker, double x, double y, float z) {
      Minecraft mc = Minecraft.func_71410_x();
      Vec3d markerPos = this.getVecForMarker(marker);
      if (markerPos != null) {
         GlStateManager.func_179094_E();
         Vec3d v1 = new Vec3d(mc.field_71439_g.field_70165_t + 0.5, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + 0.5);
         Vec3d v2 = new Vec3d(markerPos.field_72450_a, markerPos.field_72448_b, markerPos.field_72449_c);
         Vec3d v3 = v2.func_178788_d(v1).func_72432_b();
         v3 = new Vec3d(-v3.field_72450_a, v3.field_72448_b, -v3.field_72449_c);
         int arrWidth = 18;
         int arrHeight = 18;
         GlStateManager.func_179137_b(x + (double)((float)arrWidth / 2.0F), y + (double)((float)arrHeight / 2.0F), 0.0);
         GlStateManager.func_179114_b(this.getYawFromVec3(v3, mc.field_71439_g) + 180.0F, 0.0F, 0.0F, 1.0F);
         mc.field_71446_o.func_110577_a(GuiResources.arrow);
         GuiHelper.drawImageQuad((double)((float)(-arrWidth) / 2.0F), (double)((float)(-arrHeight) / 2.0F), (double)arrWidth, (float)arrHeight, 0.0, 0.0, 1.0, 1.0, z);
         GlStateManager.func_179121_F();
         return true;
      } else {
         return false;
      }
   }

   private Vec3d getVecForMarker(QuestMarker marker) {
      Minecraft mc = Minecraft.func_71410_x();
      double x = (double)marker.x;
      double y = (double)marker.y;
      double z = (double)marker.z;
      boolean found = false;
      if (marker.uuid != null) {
         Iterator var10 = mc.field_71441_e.field_72996_f.iterator();

         while(var10.hasNext()) {
            Entity entity = (Entity)var10.next();
            if (entity != null && entity.getPersistentID().equals(marker.uuid)) {
               x = (double)((float)(entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)mc.func_184121_ak())) + 0.5;
               y = (double)((float)(entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)mc.func_184121_ak()) + entity.func_70047_e()) + 0.8;
               z = (double)((float)(entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)mc.func_184121_ak())) + 0.5;
               found = true;
               break;
            }
         }
      } else {
         found = true;
      }

      return !found ? null : new Vec3d(x, y, z);
   }
}
