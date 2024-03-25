package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.EnumNPCServerPacketType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryTrainers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.translation.I18n;

class TextureEditorNPC {
   private GuiScreenDropDown gui;
   private EntityNPC npc;
   private BaseTrainer model;
   private int x;
   private int y;
   private int width;
   private int yOffset;
   private GuiTextField customTextureBox;
   private GuiDropDown modelTextureDropDown;
   private boolean customFocus;

   TextureEditorNPC(GuiScreenDropDown gui, EntityNPC npc, int x, int y, int width) {
      this(gui, npc, x, y, width, 0);
   }

   TextureEditorNPC(GuiScreenDropDown gui, EntityNPC npc, int x, int y, int width, int yOffset) {
      this.customFocus = false;
      this.gui = gui;
      this.npc = npc;
      this.model = npc.getBaseTrainer();
      this.x = x;
      this.y = y;
      this.width = width;
      this.yOffset = yOffset;
      gui.addDropDown((new GuiDropDown(ServerNPCRegistry.trainers.getTypes(), this.model, x, y + yOffset, width, 92)).setGetOptionString((trainer) -> {
         return I18n.func_74838_a("trainer." + trainer.name);
      }).setOnSelected(this::selectTrainerType).setInactiveTop(y).setOrdered());
      if (this.model.textures.size() > 1) {
         this.displayTextureMenu();
      }

      this.customTextureBox = new GuiTextField(11, Minecraft.func_71410_x().field_71466_p, x, y + 38, width, 20);
      String tex = this.npc.getCustomSteveTexture();
      this.customTextureBox.func_146180_a(tex.contains(";") ? tex.split(";")[0] : tex);
   }

   private void displayTextureMenu() {
      int modelDistance = 16;
      this.modelTextureDropDown = (new GuiDropDown(this.model.textures, this.npc.getTextureIndex(), this.x, this.y + Math.max(this.yOffset, (this.model.textures.size() - 1) * -10) + modelDistance, this.width, 70)).setGetOptionString((texture) -> {
         return I18n.func_74838_a("trainer.model." + texture);
      }).setOnSelectedIndex(this::selectTexture).setInactiveTop(this.y + modelDistance);
      this.gui.addDropDown(this.modelTextureDropDown);
   }

   private void selectTrainerType(BaseTrainer model) {
      this.model = model;
      this.npc.func_184212_Q().func_187227_b(EntityNPC.dwModel, this.model.id);
      Pixelmon.network.sendToServer(new NPCServerPacket(this.npc.getId(), this.model.id));
      this.npc.setTextureIndex(0);
      this.gui.removeDropDown(this.modelTextureDropDown);
      if (this.model.textures.size() > 1) {
         this.npc.func_184212_Q().func_187227_b(EntityNPC.dwNickname, "Steve");
         this.displayTextureMenu();
      }

   }

   private void selectTexture(int textureIndex) {
      if (!this.model.textures.isEmpty()) {
         Pixelmon.network.sendToServer(new NPCServerPacket(this.npc.getId(), EnumNPCServerPacketType.TextureIndex, textureIndex));
      }

   }

   void drawCustomTextBox() {
      if (this.isCustomTexture()) {
         this.customTextureBox.func_146194_f();
         this.customTextureBox.func_146189_e(true);
      } else {
         this.customTextureBox.func_146189_e(false);
      }

   }

   void keyTyped(char key, int keyCode, GuiTextField... otherFields) {
      if (this.customFocus && !this.customTextureBox.func_146206_l()) {
         this.saveCustomTexture();
      }

      this.customFocus = this.customTextureBox.func_146206_l();
      this.customTextureBox.func_146201_a(key, keyCode);
      if (this.customTextureBox.func_146176_q()) {
         List fields = new ArrayList(Arrays.asList(otherFields));
         fields.add(this.customTextureBox);
         GuiHelper.switchFocus(keyCode, (List)fields);
      } else {
         GuiHelper.switchFocus(keyCode, otherFields);
      }

   }

   void mouseClicked(int x, int y, int mouseButton) {
      this.customTextureBox.func_146192_a(x, y, mouseButton);
      if (this.customFocus && !this.customTextureBox.func_146206_l()) {
         this.saveCustomTexture();
      }

      this.customFocus = this.customTextureBox.func_146206_l();
   }

   void saveCustomTexture() {
      if (this.isCustomTexture() && !this.customTextureBox.func_146179_b().equals(this.npc.getCustomSteveTexture().split(";")[0])) {
         Pixelmon.network.sendToServer(new NPCServerPacket(this.npc.getId(), EnumNPCServerPacketType.CustomSteveTexture, this.customTextureBox.func_146179_b()));
      }

   }

   private boolean isCustomTexture() {
      if (this.model != NPCRegistryTrainers.Steve && !this.model.name.equals("Steve")) {
         return false;
      } else {
         String texture = (String)this.model.textures.get(this.npc.getTextureIndex());
         return texture.equals("Custom_RP") || texture.equals("Custom_PN");
      }
   }
}
