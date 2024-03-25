package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiIndividualEditorBase;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.UpdateTrainerPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.List;

public class GuiTrainerEditorPokemonScreen extends GuiIndividualEditorBase {
   int index = -1;

   public GuiTrainerEditorPokemonScreen(int index, String titleText) {
      super((Pokemon)GuiTrainerEditor.pokemonList.get(index), titleText);
      int numPokemon = GuiTrainerEditor.pokemonList.size();
      if (index >= numPokemon) {
         index = numPokemon - 1;
         if (index < 0) {
            return;
         }
      }

      this.index = index;
   }

   protected boolean showDeleteButton() {
      return GuiTrainerEditor.pokemonList.size() > 1;
   }

   protected void changePokemon(EnumSpecies species) {
      Pixelmon.network.sendToServer(new NPCServerPacket(GuiTrainerEditor.currentTrainerID, species, this.index));
   }

   protected void deletePokemon() {
      Pixelmon.network.sendToServer(new DeleteTrainerPokemon(GuiTrainerEditor.currentTrainerID, this.index));
      this.field_146297_k.func_147108_a(new GuiTrainerEditorPartyScreen());
   }

   protected void saveAndClose() {
      Pixelmon.network.sendToServer(new UpdateTrainerPokemon(this.p));
      this.field_146297_k.func_147108_a(new GuiTrainerEditorPartyScreen());
   }

   public List getPokemonList() {
      return GuiTrainerEditor.pokemonList;
   }
}
