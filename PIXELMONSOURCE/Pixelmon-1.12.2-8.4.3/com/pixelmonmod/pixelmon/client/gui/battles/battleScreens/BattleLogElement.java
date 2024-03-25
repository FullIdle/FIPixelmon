package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.tasks.IBattleMessage;
import com.pixelmonmod.pixelmon.client.gui.GuiElement;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

public class BattleLogElement extends GuiElement implements ITickable {
   private static final ResourceLocation LOG_UP = new ResourceLocation("pixelmon", "textures/gui/battle/log_up.png");
   private static final ResourceLocation LOG_DOWN = new ResourceLocation("pixelmon", "textures/gui/battle/log_down.png");
   protected boolean autoAcknowledge;
   private final ClientBattleManager bm;
   private final LinkedList messageLog = new LinkedList();
   private int activeCounter = -1;
   private int scrollPos = -1;
   private String activeMessage = "";
   private BattleMode lastMode;

   public BattleLogElement(ClientBattleManager bm) {
      this.lastMode = BattleMode.Waiting;
      this.bm = bm;
   }

   public void drawElement(float scale) {
      int centerX = this.x + this.width / 2;
      int centerY = this.y + this.height / 2;
      Minecraft.func_71410_x().func_110434_K().func_110577_a(LOG_UP);
      GuiHelper.drawImage((double)(centerX - 12), (double)this.y, 12.0, 6.0, 0.0F);
      Minecraft.func_71410_x().func_110434_K().func_110577_a(LOG_DOWN);
      GuiHelper.drawImage((double)(centerX - 12), (double)(this.y + this.height - 8), 12.0, 6.0, 0.0F);
      LinkedList lines = this.compileLines();
      if (!this.activeMessage.isEmpty() && this.scrollPos == -1) {
         List active = GuiHelper.splitString(this.activeMessage, this.width - 16);
         lines.addAll(active);
      }

      int activeHeight = centerY;
      if (this.scrollPos != -1) {
         activeHeight = this.y + this.height - 20;
      }

      for(int i = lines.size() - (this.scrollPos <= 0 ? 1 : this.scrollPos + 1); i >= 0; --i) {
         if (activeHeight >= this.y + 10) {
            int gray = 220 - (lines.size() - i - (this.scrollPos == -1 ? 0 : this.scrollPos)) * (this.scrollPos == -1 ? 20 : 5);
            GuiHelper.drawCenteredString((String)lines.get(i), (float)(centerX - 6), (float)activeHeight, gray + (gray << 16) + (gray << 8));
            activeHeight -= 10;
         }
      }

   }

   public void func_73660_a() {
      this.activeCounter = Math.max(-1, this.activeCounter - 1);
      if (this.autoAcknowledge) {
         if (this.activeCounter == -1) {
            this.activeCounter = 80;
         } else if (this.activeCounter == 0) {
            this.acknowledge();
         }
      }

      if (this.bm.mode != this.lastMode) {
         this.lastMode = this.bm.mode;
         this.activeMessage = "";
      }

   }

   public void drawElement(int x, int y, int width, int height, boolean autoAcknowledge) {
      this.setPosition(x, y, width, height);
      this.autoAcknowledge = autoAcknowledge;
      this.drawElement(this.scale);
   }

   public void setActiveMessage(String message) {
      this.activeMessage = message;
   }

   public void forceAddMessage(String message) {
      this.messageLog.push(message);
   }

   public void acknowledge() {
      if (this.bm.hasMoreMessages() && this.bm.getNextBattleTask() instanceof IBattleMessage && !this.bm.isProcessingTask() && ((IBattleMessage)this.bm.getNextBattleTask()).getMessage() != null) {
         String text = ((IBattleMessage)this.bm.getNextBattleTask()).getMessage().func_150254_d();
         String[] var2 = text.split("\\n");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            this.messageLog.push(line);
         }

         this.bm.removeBattleTask();
         if (this.messageLog.size() > 100) {
            this.messageLog.removeLast();
         }
      }

      this.bm.checkClearedMessages();
   }

   public boolean withinBounds(int mouseX, int mouseY) {
      return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
   }

   public boolean processUpDown(int mouseX, int mouseY) {
      if (this.withinBounds(mouseX, mouseY) && mouseX >= this.x + this.width / 4 && mouseX < this.x + this.width - this.width / 4) {
         if (mouseY >= this.y && mouseY < this.y + 10) {
            if (this.compileLines().size() - 7 >= this.scrollPos) {
               ++this.scrollPos;
            }
         } else {
            if (mouseY < this.y + this.height - 10 || mouseY >= this.y + this.height) {
               return false;
            }

            if (this.scrollPos <= 0) {
               this.scrollPos = -1;
            } else {
               --this.scrollPos;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private LinkedList compileLines() {
      LinkedList lines = Lists.newLinkedList();
      List message;
      if (this.bm.hasMoreMessages() && this.bm.getNextBattleTask() instanceof IBattleMessage) {
         String text = ((IBattleMessage)this.bm.getNextBattleTask()).getMessage().func_150254_d();
         String[] var4 = text.split("\\n");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            message = GuiHelper.splitString(string, this.width - 16);

            for(int i = message.size() - 1; i >= 0; --i) {
               lines.push(message.get(i));
            }
         }
      }

      int messageIndex = 0;

      while(messageIndex < this.messageLog.size()) {
         message = GuiHelper.splitString((String)this.messageLog.get(messageIndex++), this.width - 16);

         for(int i = message.size() - 1; i >= 0; --i) {
            lines.push(message.get(i));
         }
      }

      return lines;
   }
}
