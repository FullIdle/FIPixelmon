package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.EnumNPCServerPacketType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientShopkeeperData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiShopkeeperEditor extends GuiScreenDropDown {
   public static int shopkeeperID;
   public static String json;
   public static String name;
   public static List shopkeeperData = new ArrayList();
   NPCShopkeeper npc;
   private GuiDropDown jsonDropDown;
   private GuiDropDown nameDropDown;
   private GuiDropDown textureDropDown;

   public GuiShopkeeperEditor(int shopkeeperID) {
      Keyboard.enableRepeatEvents(true);
      Optional npcShopkeeperOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, shopkeeperID, NPCShopkeeper.class);
      if (!npcShopkeeperOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.npc = (NPCShopkeeper)npcShopkeeperOptional.get();
         GuiShopkeeperEditor.shopkeeperID = shopkeeperID;
         if (this.npc == null) {
            GuiHelper.closeScreen();
         }

      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 + 100, this.field_146295_m / 2 - 120, 80, 20, I18n.func_135052_a("gui.npceditor.despawn", new Object[0])));
      this.jsonDropDown = (new GuiDropDown(shopkeeperData, new ClientShopkeeperData(json), this.field_146294_l / 2 - 10, this.field_146295_m / 2 - 81, 140, 100)).setGetOptionString((shopkeeper) -> {
         return I18n.func_135052_a("npc.shopkeeper." + shopkeeper.getID(), new Object[0]);
      }).setOnSelected((shopkeeper) -> {
         Pixelmon.network.sendToServer(new NPCServerPacket(shopkeeperID, EnumNPCServerPacketType.CycleJson, shopkeeper.getID()));
      }).setOrdered();
      this.addDropDown(this.jsonDropDown);
      ClientShopkeeperData currentShopkeeper = null;
      Iterator var2 = shopkeeperData.iterator();

      while(var2.hasNext()) {
         ClientShopkeeperData data = (ClientShopkeeperData)var2.next();
         if (data.getID().equals(name)) {
            currentShopkeeper = data;
            break;
         }
      }

      if (currentShopkeeper == null) {
         if (shopkeeperData.isEmpty()) {
            GuiHelper.closeScreen();
            return;
         }

         currentShopkeeper = (ClientShopkeeperData)shopkeeperData.get(0);
      }

      this.resetDropDowns(currentShopkeeper, this.npc.getCustomSteveTexture());
      this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 5, 140, 20, I18n.func_135052_a("gui.shopkeepereditor.refreshItems", new Object[0])));
   }

   public void updateShopkeeper(String texture) {
      if (this.jsonDropDown != null) {
         int selectedIndex = this.jsonDropDown.getSelectedIndex();
         if (selectedIndex >= 0) {
            this.resetDropDowns((ClientShopkeeperData)shopkeeperData.get(selectedIndex), texture);
         }
      }

   }

   private void resetDropDowns(ClientShopkeeperData currentShopkeeper, String newTexture) {
      this.removeDropDown(this.nameDropDown);
      this.nameDropDown = (new GuiDropDown(currentShopkeeper.getNames(), name, this.field_146294_l / 2 - 10, this.field_146295_m / 2 - 51, 140, 100)).setOnSelectedIndex((index) -> {
         Pixelmon.network.sendToServer(new NPCServerPacket(shopkeeperID, EnumNPCServerPacketType.CycleName, index));
      });
      this.addDropDown(this.nameDropDown);
      this.removeDropDown(this.textureDropDown);
      this.textureDropDown = (new GuiDropDown(currentShopkeeper.getTextures(), newTexture, this.field_146294_l / 2 - 10, this.field_146295_m / 2 - 21, 140, 100)).setGetOptionString((texture) -> {
         return I18n.func_135052_a("npc.model." + texture.replace(".png", ""), new Object[0]);
      }).setOnSelected((texture) -> {
         Pixelmon.network.sendToServer(new NPCServerPacket(shopkeeperID, EnumNPCServerPacketType.CustomSteveTexture, texture));
      });
      this.addDropDown(this.textureDropDown);
   }

   protected void drawBackgroundUnderMenus(float f, int i, int j) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      GuiHelper.drawEntity(this.npc, this.field_146294_l / 2 - 140, this.field_146295_m / 2 + 50, 60.0F, 0.0F, 0.0F);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.shopkeeper.json", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 80, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.name", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 50, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.model", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 20, 0);
   }

   protected void func_73869_a(char key, int par2) {
      if (par2 == 1 || par2 == 28) {
         GuiHelper.closeScreen();
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 1) {
            GuiHelper.closeScreen();
         } else if (button.field_146127_k == 5) {
            Pixelmon.network.sendToServer(new NPCServerPacket(shopkeeperID, EnumNPCServerPacketType.RefreshItems));
         } else if (button.field_146127_k == 6) {
            Pixelmon.network.sendToServer(new DeleteNPC(shopkeeperID));
            GuiHelper.closeScreen();
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
