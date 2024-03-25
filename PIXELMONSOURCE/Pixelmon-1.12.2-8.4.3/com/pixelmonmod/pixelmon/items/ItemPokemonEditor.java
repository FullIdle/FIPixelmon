package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.CheckPokemonEditorAllowed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.SetEditedPlayer;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdateSinglePokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPokemonEditor extends PixelmonItem {
   public ItemPokemonEditor() {
      super("pokemon_editor");
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K && player.field_71075_bZ.field_75098_d && hand == EnumHand.MAIN_HAND && checkPermission((EntityPlayerMP)player)) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(playerMP);
         party.retrieveAll();
         Pixelmon.network.sendTo(new SetEditedPlayer(player.func_110124_au(), player.getDisplayNameString(), party.getAll()), playerMP);
         OpenScreen.open(player, EnumGuiScreen.PokemonEditor);
      }

      return new ActionResult(EnumActionResult.SUCCESS, player.func_184586_b(hand));
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (entity instanceof EntityPlayerMP && player.field_71075_bZ.field_75098_d) {
         if (checkPermission((EntityPlayerMP)player)) {
            EntityPlayerMP otherPlayer = (EntityPlayerMP)entity;
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(otherPlayer);
            if (party.guiOpened) {
               ChatHandler.sendChat(player, "pixelmon.general.playerbusy");
               return true;
            }

            Pixelmon.network.sendTo(new CheckPokemonEditorAllowed(player.func_110124_au()), otherPlayer);
         }

         return true;
      } else {
         return false;
      }
   }

   public static void updateEditedPlayer(EntityPlayerMP editingPlayer, UUID editedPlayer) {
      if (checkPermission(editingPlayer)) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(editedPlayer);
         Pixelmon.network.sendTo(new SetEditedPlayer(editedPlayer, party.getAll()), editingPlayer);
      }
   }

   public static void updateSinglePokemon(EntityPlayerMP editingPlayer, UUID editedPlayer, int slot) {
      if (checkPermission(editingPlayer)) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(editedPlayer);
         Pokemon pokemonData = party.get(slot);
         Pixelmon.network.sendTo(new UpdateSinglePokemon(slot, pokemonData), editingPlayer);
      }
   }

   public static boolean checkPermission(EntityPlayerMP editingPlayer) {
      if (PixelmonConfig.allowPokemonEditors && editingPlayer.func_70003_b(4, "pixelmon.pokemoneditor.use")) {
         return true;
      } else {
         ChatHandler.sendChat(editingPlayer, "gui.pokemoneditor.notallowedserver");
         return false;
      }
   }
}
