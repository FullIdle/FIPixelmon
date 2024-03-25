package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperChat;
import com.pixelmonmod.pixelmon.items.helpers.ItemHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

public class GuiShopkeeper extends GuiShopScreen {
   public static String name;
   public static ShopkeeperChat chat;
   GuiShopkeeperScreens screen;
   private int shopkeeperID;
   private int mousePressed;

   public GuiShopkeeper(int trainerId) {
      this.screen = GuiShopkeeperScreens.Hello;
      Minecraft mc = Minecraft.func_71410_x();
      Optional entityNPCOptional = EntityNPC.locateNPCClient(mc.field_71441_e, trainerId, NPCShopkeeper.class);
      if (!entityNPCOptional.isPresent()) {
         mc.field_71439_g.func_71053_j();
      } else {
         entityNPCOptional.get();
         this.shopkeeperID = trainerId;
      }
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      if (this.screen == GuiShopkeeperScreens.Buy) {
         this.handleMouseScroll();
      }
   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      if (this.screen == GuiShopkeeperScreens.Buy && keyCode == 1) {
         this.closeScreen();
      } else {
         super.func_73869_a(key, keyCode);
      }

      if (keyCode == 28) {
         this.continueDialogue();
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.mousePressed = 1;
      this.continueDialogue();
      if (this.screen == GuiShopkeeperScreens.Buy) {
         this.clickBuyScreen(mouseX, mouseY);
         if (this.currentTab == EnumBuySell.Buy && mouseX > this.field_146294_l / 2 + 28 && mouseX < this.field_146294_l / 2 + 28 + 58 && mouseY > this.field_146295_m / 2 - 93 && mouseY < this.field_146295_m / 2 - 93 + 30) {
            this.currentTab = EnumBuySell.Sell;
            this.selectedItem = -1;
         } else if (this.currentTab == EnumBuySell.Sell && mouseX > this.field_146294_l / 2 - 31 && mouseX < this.field_146294_l / 2 - 31 + 58 && mouseY > this.field_146295_m / 2 - 93 && mouseY < this.field_146295_m / 2 - 93 + 30) {
            this.currentTab = EnumBuySell.Buy;
            this.selectedItem = -1;
         }
      }

   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      this.mousePressed = 0;
   }

   private void continueDialogue() {
      if (this.screen == GuiShopkeeperScreens.Hello) {
         this.screen = GuiShopkeeperScreens.Buy;
      } else {
         if (this.screen == GuiShopkeeperScreens.Goodbye) {
            Minecraft.func_71410_x().field_71439_g.func_71053_j();
         }

      }
   }

   protected void closeScreen() {
      this.screen = GuiShopkeeperScreens.Goodbye;
   }

   protected void sendBuyPacket() {
      Pixelmon.network.sendToServer(new ShopKeeperPacket(EnumBuySell.Buy, this.shopkeeperID, ((ClientShopItem)buyItems.get(this.selectedItem)).getItemID(), this.quantity));
   }

   protected void sendSellPacket() {
      Pixelmon.network.sendToServer(new ShopKeeperPacket(EnumBuySell.Sell, this.shopkeeperID, ((ClientShopItem)sellItems.get(this.selectedItem)).getItemID(), this.quantity));
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      NonNullList inv = Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70462_a;
      Iterator var5 = sellItems.iterator();

      while(var5.hasNext()) {
         ClientShopItem sellItem = (ClientShopItem)var5.next();
         ItemStack item = sellItem.getItemStack();
         sellItem.amount = 0;
         Iterator var8 = inv.iterator();

         while(var8.hasNext()) {
            ItemStack stack = (ItemStack)var8.next();
            if (stack != null && ItemHelper.isItemStackEqual(item, stack)) {
               sellItem.amount += stack.func_190916_E();
            }
         }
      }

      if (this.screen == GuiShopkeeperScreens.Hello && (chat == null || chat.hello == null || chat.hello.trim().isEmpty())) {
         this.screen = GuiShopkeeperScreens.Buy;
      } else if (this.screen == GuiShopkeeperScreens.Goodbye && (chat == null || chat.goodbye == null || chat.goodbye.trim().isEmpty())) {
         this.continueDialogue();
         return;
      }

      if (this.screen != GuiShopkeeperScreens.Hello && this.screen != GuiShopkeeperScreens.Goodbye) {
         this.renderBuyScreen(mouseX, mouseY);
         int colour = 16777215;
         if (this.currentTab == EnumBuySell.Sell && mouseX > this.field_146294_l / 2 - 31 && mouseX < this.field_146294_l / 2 - 31 + 58 && mouseY > this.field_146295_m / 2 - 93 && mouseY < this.field_146295_m / 2 - 93 + 30) {
            colour = 13421772;
         }

         String buyLabel = I18n.func_74838_a("gui.shopkeeper.buy");
         this.func_73731_b(this.field_146297_k.field_71466_p, buyLabel, this.field_146294_l / 2 - 2 - this.field_146297_k.field_71466_p.func_78256_a(buyLabel) / 2, this.field_146295_m / 2 - 82, colour);
         colour = 16777215;
         if (this.currentTab == EnumBuySell.Buy && mouseX > this.field_146294_l / 2 + 28 && mouseX < this.field_146294_l / 2 + 28 + 58 && mouseY > this.field_146295_m / 2 - 93 && mouseY < this.field_146295_m / 2 - 93 + 30) {
            colour = 13421772;
         }

         String sellLabel = I18n.func_74838_a("gui.shopkeeper.sell");
         this.func_73731_b(this.field_146297_k.field_71466_p, sellLabel, this.field_146294_l / 2 + 57 - this.field_146297_k.field_71466_p.func_78256_a(sellLabel) / 2, this.field_146295_m / 2 - 82, colour);
      } else {
         ArrayList chatText = new ArrayList();
         if (this.screen == GuiShopkeeperScreens.Hello) {
            chatText.add(chat.hello);
         } else {
            chatText.add(chat.goodbye);
         }

         GuiHelper.drawDialogueBox(this, (String)name, (List)chatText, this.field_73735_i);
      }

      if (this.mousePressed > 0 && ++this.mousePressed > 5) {
         this.clickBuyMiniScreen(mouseX, mouseY, false);
      }

      super.func_73863_a(mouseX, mouseY, f);
   }

   public boolean func_73868_f() {
      return false;
   }
}
