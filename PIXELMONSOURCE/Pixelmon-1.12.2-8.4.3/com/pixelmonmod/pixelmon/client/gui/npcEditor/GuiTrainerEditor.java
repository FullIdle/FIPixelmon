package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.EnumNPCServerPacketType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleAIMode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class GuiTrainerEditor extends GuiScreenDropDown {
   public static ArrayList pokemonList = new ArrayList();
   public static int currentTrainerID;
   public static SetTrainerData trainerData;
   NPCTrainer trainer;
   GuiTextField nameBox;
   private TextureEditorNPC textureEditor;
   String oldName = "";
   private static final int BUTTON_ID_RULES = 11;
   private static final int DROP_DOWN_HEIGHT_OFFSET = 23;
   BaseTrainer model;

   public GuiTrainerEditor(int trainerId) {
      Keyboard.enableRepeatEvents(true);
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, trainerId, NPCTrainer.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.trainer = (NPCTrainer)entityNPCOptional.get();
         currentTrainerID = trainerId;
         if (trainerData == null) {
            trainerData = new SetTrainerData("", "", "", "", 0, new ItemStack[0], new BattleRules());
         }

         this.trainer.update(trainerData);
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (this.trainer == null) {
         GuiHelper.closeScreen();
      } else {
         this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
         this.textureEditor = new TextureEditorNPC(this, this.trainer, this.field_146294_l / 2 - 20, this.field_146295_m / 2 - 84, 180);
         this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 80, this.field_146295_m / 2 + 90, 100, 20, I18n.func_135052_a("gui.trainereditor.edit", new Object[0])));
         int dropDownLeft = this.field_146294_l / 2 + 40;
         int dropDownHeightStart = this.field_146295_m / 2 - 40;
         int dropDownWidth = 100;
         int dropDownHeight = 100;
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumBossMode.values()), this.trainer.getBossMode(), dropDownLeft, dropDownHeightStart, dropDownWidth, dropDownHeight)).setGetOptionString(EnumBossMode::getLocalizedName).setOnSelected(this::selectBossMode));
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumTrainerAI.values()), this.trainer.getAIMode(), dropDownLeft, dropDownHeightStart + 22, dropDownWidth, dropDownHeight)).setGetOptionString(EnumTrainerAI::getLocalizedName).setOnSelected((aiMode) -> {
            this.trainer.setAIMode(aiMode);
         }));
         int battleAITop = dropDownHeightStart + 44;
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumBattleAIMode.values()), this.trainer.getBattleAIMode(), dropDownLeft, battleAITop - 10, dropDownWidth, dropDownHeight)).setGetOptionString(EnumBattleAIMode::getLocalizedName).setOnSelected(this::selectBattleAIMode).setInactiveTop(battleAITop));
         int encounterModeTop = dropDownHeightStart + 66;
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumEncounterMode.values()), this.trainer.getEncounterMode(), dropDownLeft, encounterModeTop - 35, dropDownWidth, dropDownHeight)).setGetOptionString(EnumEncounterMode::getLocalizedName).setOnSelected(this::selectEncounterMode).setInactiveTop(encounterModeTop));
         int oldGenModeTop = dropDownHeightStart + 88;
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumOldGenMode.values()), this.trainer.getOldGen(), dropDownLeft, oldGenModeTop - 35, dropDownWidth, dropDownHeight)).setGetOptionString(EnumOldGenMode::getLocalizedName).setOnSelected(this::selectOldGenMode).setInactiveTop(oldGenModeTop));
         int megaItemModeTop = dropDownHeightStart + 110;
         this.addDropDown((new GuiDropDown(Arrays.asList(EnumMegaItemsUnlocked.values()), this.trainer.getMegaItem(), dropDownLeft, megaItemModeTop - 35, dropDownWidth, dropDownHeight)).setGetOptionString(EnumMegaItemsUnlocked::getLocalizedName).setOnSelected(this::selectMegaItemMode).setInactiveTop(megaItemModeTop));
         int engageDistanceTop = dropDownHeightStart + 132;
         this.addDropDown((new GuiDropDown((List)IntStream.range(1, 21).boxed().collect(Collectors.toList()), this.trainer.getEngageDistance() - 1, dropDownLeft, engageDistanceTop - 35, dropDownWidth, dropDownHeight)).setGetOptionString(Object::toString).setOnSelected(this::selectEngageDistance).setInactiveTop(engageDistanceTop));
         this.field_146292_n.add(new GuiButton(11, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 18, 100, 20, I18n.func_135052_a("gui.battlerules.title", new Object[0])));
         this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2 - 190, this.field_146295_m / 2 + 90, 100, 20, I18n.func_135052_a("gui.trainereditor.more", new Object[0])));
         this.field_146292_n.add(new GuiButton(8, this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 120, 80, 20, I18n.func_135052_a("gui.trainereditor.delete", new Object[0])));
         this.nameBox = new GuiTextField(12, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 20, this.field_146295_m / 2 - 116, 180, 20);
         if (trainerData != null) {
            this.nameBox.func_146180_a(trainerData.name);
            this.oldName = trainerData.name;
         }

      }
   }

   private void selectBossMode(EnumBossMode bossMode) {
      this.trainer.setBossMode(bossMode);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, bossMode));
   }

   private void selectBattleAIMode(EnumBattleAIMode battleAI) {
      this.trainer.setBattleAIMode(battleAI);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, battleAI));
   }

   private void selectEncounterMode(EnumEncounterMode encounterMode) {
      this.trainer.setEncounterMode(encounterMode);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, encounterMode));
   }

   private void selectOldGenMode(EnumOldGenMode oldGenMode) {
      this.trainer.setOldGenMode(oldGenMode);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, oldGenMode));
   }

   private void selectMegaItemMode(EnumMegaItemsUnlocked megaItem) {
      this.trainer.setMegaItem(megaItem);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, megaItem));
   }

   private void selectEngageDistance(int distance) {
      this.trainer.setEngageDistance(distance);
      Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, distance, EnumNPCServerPacketType.EngageDistance));
   }

   protected void drawBackgroundUnderMenus(float f, int i, int j) {
      if (this.trainer == null) {
         GuiHelper.closeScreen();
      } else {
         if (trainerData != null && !trainerData.name.equals(this.oldName)) {
            this.oldName = trainerData.name;
            this.nameBox.func_146180_a(trainerData.name);
         }

         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         RenderHelper.func_74518_a();
         GuiHelper.drawEntity(this.trainer, this.field_146294_l / 2 - 140, this.field_146295_m / 2 + 60, 60.0F, 0.0F, 0.0F);
         int dropDownLabelX = this.field_146294_l / 2 + 90;
         int dropDownHeightStart = this.field_146295_m / 2 - 50;
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.bosstype", new Object[0]), (float)dropDownLabelX, (float)dropDownHeightStart, 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.aimode", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 22), 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.battleaimode", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 44), 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.encountermode", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 66), 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.oldgen", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 88), 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.megaitem", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 110), 0);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.engagedistance", new Object[0]), (float)dropDownLabelX, (float)(dropDownHeightStart + 132), 0);
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.name", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 110, 0);
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.model", new Object[0]), this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 83, 0);
         this.nameBox.func_146194_f();
         this.textureEditor.drawCustomTextBox();
         this.drawPokemonList();
      }
   }

   private void drawPokemonList() {
      int top = this.field_146295_m / 2 + 20;
      int ySize = 68;
      int left = this.field_146294_l / 2 - 82;
      int xSize = 104;
      func_73734_a(left - 1, top - 1, left + xSize, top + ySize, -16777215);
      func_73734_a(left, top, left + xSize, top + ySize, -6777215);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.partypokemon", new Object[0]), left + 1, top - 11, 0);

      for(int n = 0; n < pokemonList.size(); ++n) {
         Pokemon pokemon = (Pokemon)pokemonList.get(n);
         if (pokemon != null) {
            this.field_146297_k.field_71466_p.func_78276_b(pokemon.getDisplayName(), left + 4, top + 4 + n * 10, 0);
            this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.lvl", new Object[0]) + " " + pokemon.getLevel(), left + 65, top + 4 + n * 10, 0);
         }
      }

   }

   protected void func_73869_a(char key, int keyCode) {
      this.nameBox.func_146201_a(key, keyCode);
      this.textureEditor.keyTyped(key, keyCode, this.nameBox);
      if (keyCode == 1 || keyCode == 28) {
         this.saveFields();
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      if (this.nameBox == null) {
         this.func_73866_w_();
      }

      this.nameBox.func_146192_a(x, y, mouseButton);
      this.textureEditor.mouseClicked(x, y, mouseButton);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 1) {
            this.saveFields();
         } else if (button.field_146127_k == 3) {
            this.field_146297_k.func_147108_a(new GuiTrainerEditorPartyScreen());
         } else if (button.field_146127_k == 7) {
            this.field_146297_k.func_147108_a(new GuiTrainerEditorMore());
         } else if (button.field_146127_k == 8) {
            Pixelmon.network.sendToServer(new DeleteNPC(currentTrainerID));
            GuiHelper.closeScreen();
         } else if (button.field_146127_k == 11) {
            this.field_146297_k.func_147108_a(new GuiBattleRulesNPCEditor());
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
      if (!this.nameBox.func_146179_b().equals("") && trainerData != null) {
         if (currentTrainerID <= 0) {
            currentTrainerID = this.trainer.getId();
         }

         if (!this.nameBox.func_146179_b().equals(trainerData.name)) {
            Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, EnumNPCServerPacketType.Name, this.nameBox.func_146179_b()));
         }

         Pixelmon.network.sendToServer(new NPCServerPacket(currentTrainerID, this.trainer.getAIMode()));
         this.textureEditor.saveCustomTexture();
         return true;
      } else {
         return false;
      }
   }
}
