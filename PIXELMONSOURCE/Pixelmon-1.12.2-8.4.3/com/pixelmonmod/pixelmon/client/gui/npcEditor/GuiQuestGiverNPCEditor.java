package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.EnumNPCServerPacketType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientNPCData;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiQuestGiverNPCEditor extends GuiScreenDropDown {
   public static int currentNPCID;
   public static ArrayList chatPages;
   public static boolean chatChanged = false;
   public static String name;
   NPCQuestGiver npc;
   GuiTextField nameBox;
   GuiTextField page1Box;
   GuiTextField page2Box;
   GuiTextField page3Box;
   GuiTextField page4Box;
   GuiTextField textureBox;
   public static List npcData = new ArrayList();
   String oldName;

   public GuiQuestGiverNPCEditor(int npcID) {
      Keyboard.enableRepeatEvents(true);
      Optional npcOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, npcID, NPCQuestGiver.class);
      if (!npcOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.npc = (NPCQuestGiver)npcOptional.get();
         currentNPCID = npcID;
         if (this.npc == null) {
            GuiHelper.closeScreen();
         }

      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 + 70, this.field_146295_m / 2 + 90, 80, 20, I18n.func_135052_a("gui.npceditor.despawn", new Object[0])));
      this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2 - 15, this.field_146295_m / 2 + 90, 80, 20, I18n.func_135052_a("gui.npceditor.copyuuid", new Object[0])));
      this.nameBox = new GuiTextField(12, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 20, this.field_146295_m / 2 - 96, 180, 20);
      this.nameBox.func_146180_a(name);
      this.textureBox = new GuiTextField(3, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 189, this.field_146295_m / 2 + 100, 149, 10);
      String textureText = this.npc.getCustomSteveTexture();
      String type = (String)this.npc.getBaseTrainer().textures.get(this.npc.getTextureIndex());
      if (type.startsWith("Custom")) {
         this.textureBox.func_146180_a(textureText.replaceAll(";.+", ""));
         textureText = type;
      }

      List npcs = Lists.newArrayList(npcData);
      npcs.add(new ClientNPCData("Custom_RP", "Custom_RP"));
      npcs.add(new ClientNPCData("Custom_PN", "Custom_PN"));
      npcs.sort(Comparator.comparing(ClientNPCData::getID));
      this.addDropDown((new GuiDropDown(npcs, new ClientNPCData(textureText), this.field_146294_l / 2 - 190, this.field_146295_m / 2 - 85, 140, 200)).setGetOptionString((npc) -> {
         return npc.getTexture().contains(".png") ? I18n.func_135052_a("npc.model." + npc.getTexture().replace(".png", ""), new Object[0]) : I18n.func_135052_a("trainer.model." + npc.getTexture(), new Object[0]);
      }).setOnSelected((npc) -> {
         if (npc.getID().startsWith("Custom")) {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentNPCID, EnumNPCServerPacketType.TextureIndex, this.npc.getBaseTrainer().textures.indexOf(npc.getID())));
            this.textureBox.func_146180_a("");
         } else {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentNPCID, npc));
         }

      }).setInactiveTop(this.field_146295_m / 2 + 85).setOrdered());
      this.page1Box = new GuiTextField(2, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 50, 260, 20);
      this.page1Box.func_146203_f(128);
      if (!chatPages.isEmpty()) {
         this.page1Box.func_146180_a((String)chatPages.get(0));
      }

      this.page2Box = new GuiTextField(3, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 15, 260, 20);
      this.page2Box.func_146203_f(128);
      if (chatPages.size() > 1) {
         this.page2Box.func_146180_a((String)chatPages.get(1));
      }

      this.page3Box = new GuiTextField(4, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 20, 260, 20);
      this.page3Box.func_146203_f(128);
      if (chatPages.size() > 2) {
         this.page3Box.func_146180_a((String)chatPages.get(2));
      }

      this.page4Box = new GuiTextField(5, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 55, 260, 20);
      this.page4Box.func_146203_f(128);
      if (chatPages.size() > 3) {
         this.page4Box.func_146180_a((String)chatPages.get(3));
      }

      chatChanged = false;
      this.oldName = name;
   }

   protected void drawBackgroundUnderMenus(float f, int i, int j) {
      if (!this.oldName.equals(name)) {
         this.oldName = name;
         this.nameBox.func_146180_a(name);
      }

      if (chatChanged) {
         chatChanged = false;
         if (!chatPages.isEmpty()) {
            this.page1Box.func_146180_a((String)chatPages.get(0));
         }

         if (chatPages.size() > 1) {
            this.page2Box.func_146180_a((String)chatPages.get(1));
         }

         if (chatPages.size() > 2) {
            this.page3Box.func_146180_a((String)chatPages.get(2));
         }

         if (chatPages.size() > 3) {
            this.page4Box.func_146180_a((String)chatPages.get(3));
         }
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      GuiHelper.drawEntity(this.npc, this.field_146294_l / 2 - 140, this.field_146295_m / 2 + 50, 60.0F, 0.0F, 0.0F);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.name", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 90, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npceditor.page1", new Object[0]), this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 61, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npceditor.page2", new Object[0]), this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 26, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npceditor.page3", new Object[0]), this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 9, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npceditor.page4", new Object[0]), this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 44, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.model", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 72, 0);
      this.nameBox.func_146194_f();
      this.page1Box.func_146194_f();
      this.page2Box.func_146194_f();
      this.page3Box.func_146194_f();
      this.page4Box.func_146194_f();
      if (this.isCustomTexture()) {
         this.textureBox.func_146194_f();
      }

   }

   protected void func_73869_a(char key, int par2) {
      this.nameBox.func_146201_a(key, par2);
      this.page1Box.func_146201_a(key, par2);
      this.page2Box.func_146201_a(key, par2);
      this.page3Box.func_146201_a(key, par2);
      this.page4Box.func_146201_a(key, par2);
      if (this.isCustomTexture()) {
         this.textureBox.func_146201_a(key, par2);
      }

      GuiHelper.switchFocus(par2, this.nameBox, this.page1Box, this.page2Box, this.page3Box, this.page4Box, this.textureBox);
      if (par2 == 1 || par2 == 28) {
         this.saveFields();
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int button) throws IOException {
      this.nameBox.func_146192_a(x, y, button);
      this.page1Box.func_146192_a(x, y, button);
      this.page2Box.func_146192_a(x, y, button);
      this.page3Box.func_146192_a(x, y, button);
      this.page4Box.func_146192_a(x, y, button);
      this.textureBox.func_146192_a(x, y, button);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         switch (button.field_146127_k) {
            case 1:
               this.saveFields();
               break;
            case 6:
               Pixelmon.network.sendToServer(new DeleteNPC(currentNPCID));
               GuiHelper.closeScreen();
               break;
            case 7:
               StringSelection stringSelection = new StringSelection(this.npc.getPersistentID().toString());
               Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
               clipboard.setContents(stringSelection, (ClipboardOwner)null);
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   private void saveFields() {
      if (this.checkFields()) {
         GuiHelper.closeScreen();
      }

   }

   private boolean checkFields() {
      if (this.nameBox.func_146179_b().equals("")) {
         return false;
      } else {
         if (currentNPCID <= 0) {
            currentNPCID = this.npc.getId();
         }

         if (!this.nameBox.func_146179_b().equals(name)) {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentNPCID, EnumNPCServerPacketType.Name, this.nameBox.func_146179_b()));
         }

         ArrayList pages = new ArrayList();
         if (!this.page1Box.func_146179_b().equals("")) {
            pages.add(this.page1Box.func_146179_b());
         }

         if (!this.page2Box.func_146179_b().equals("")) {
            pages.add(this.page2Box.func_146179_b());
         }

         if (!this.page3Box.func_146179_b().equals("")) {
            pages.add(this.page3Box.func_146179_b());
         }

         if (!this.page4Box.func_146179_b().equals("")) {
            pages.add(this.page4Box.func_146179_b());
         }

         if (this.isCustomTexture()) {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentNPCID, EnumNPCServerPacketType.CustomSteveTexture, this.textureBox.func_146179_b()));
         }

         if (!this.compareChat(pages)) {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentNPCID, pages));
         }

         return true;
      }
   }

   private boolean compareChat(ArrayList pages) {
      if (pages.size() != chatPages.size()) {
         return false;
      } else {
         for(int i = 0; i < chatPages.size(); ++i) {
            String page = (String)pages.get(i);
            String oldPage = (String)chatPages.get(i);
            if (!page.equals(oldPage)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean isCustomTexture() {
      return ((String)this.npc.getBaseTrainer().textures.get(this.npc.getTextureIndex())).startsWith("Custom");
   }
}
