package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.ChangePokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.RandomizePokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.RequestCloseEditedPlayer;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdatePlayerParty;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.lwjgl.input.Keyboard;

public class GuiPokemonEditorParty extends GuiPartyEditorBase {
   public static UUID editedPlayerUUID;
   public static String editedPlayerName;

   public GuiPokemonEditorParty() {
      super(ServerStorageDisplay.editedPokemon);
      Keyboard.enableRepeatEvents(true);
   }

   public String getTitle() {
      return I18n.func_74838_a("gui.pokemoneditor.title");
   }

   protected void exitScreen() {
      if (!Minecraft.func_71410_x().field_71439_g.func_110124_au().equals(editedPlayerUUID)) {
         Pixelmon.network.sendToServer(new RequestCloseEditedPlayer(editedPlayerUUID));
      }

      this.field_146297_k.field_71439_g.func_71053_j();
   }

   protected void randomizeParty() {
      Pixelmon.network.sendToServer(new RandomizePokemon(editedPlayerUUID));
   }

   protected void addPokemon(int partySlot) {
      Pixelmon.network.sendToServer(new ChangePokemon(UUID.randomUUID(), EnumSpecies.Magikarp));
   }

   protected void editPokemon(int partySlot) {
      Pokemon pokemon = (Pokemon)this.pokemonList.get(partySlot);
      if (pokemon != null) {
         this.field_146297_k.func_147108_a(new GuiPokemonEditorIndividual(pokemon, this.getTitle()));
      }

   }

   public void func_73863_a(int i, int j, float f) {
      super.func_73863_a(i, j, f);
      this.pokemonList = ServerStorageDisplay.editedPokemon;
   }

   protected IMessage getImportSavePacket() {
      return new UpdatePlayerParty(this.pokemonList);
   }
}
