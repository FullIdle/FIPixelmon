package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPartyEditorBase;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.AddTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.RandomiseTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.UpdateTrainerParty;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GuiTrainerEditorPartyScreen extends GuiPartyEditorBase {
   public GuiTrainerEditorPartyScreen() {
      super(GuiTrainerEditor.pokemonList);
   }

   public String getTitle() {
      return I18n.func_74838_a("gui.trainereditor.pokemoneditor");
   }

   protected void exitScreen() {
      this.field_146297_k.func_147108_a(new GuiTrainerEditor(GuiTrainerEditor.currentTrainerID));
   }

   protected void randomizeParty() {
      Pixelmon.network.sendToServer(new RandomiseTrainerPokemon(GuiTrainerEditor.currentTrainerID));
   }

   protected void addPokemon(int partySlot) {
      Pixelmon.network.sendToServer(new AddTrainerPokemon(GuiTrainerEditor.currentTrainerID));
   }

   protected void editPokemon(int partySlot) {
      this.field_146297_k.func_147108_a(new GuiTrainerEditorPokemonScreen(partySlot, this.getTitle()));
   }

   protected IMessage getImportSavePacket() {
      return new UpdateTrainerParty(this.pokemonList);
   }
}
