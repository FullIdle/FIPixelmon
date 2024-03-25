package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.time.LocalDate;
import java.time.Month;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemGift extends PixelmonItem {
   public ItemGift(String name) {
      super(name);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.func_184586_b(hand);
      if (!world.field_72995_K) {
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
         if (!storage.playerData.giftOpened && this.isChristmas()) {
            if (player.func_70681_au().nextFloat() <= 0.8F) {
               Pokemon pokemon = PokemonSpec.from("random").create().makeEgg();
               Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP)player, ReceiveType.Christmas, pokemon));
               storage.add(pokemon);
            } else {
               ItemStack stack = new ItemStack(Items.field_151044_h);
               boolean boolAddedToInventory = player.field_71071_by.func_70441_a(stack);
               if (!boolAddedToInventory && stack.func_77952_i() == 0) {
                  player.func_145779_a(stack.func_77973_b(), 1);
                  String itemName = stack.func_77977_a();
                  ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.fullinventory", I18n.func_74838_a(itemName + ".name"));
               }
            }

            ChatHandler.sendChat(player, "christmas.ischristmas");
            player.field_71071_by.func_174925_a(itemstack.func_77973_b(), itemstack.func_77960_j(), 1, itemstack.func_77978_p());
            storage.playerData.giftOpened = true;
         } else if (storage.playerData.giftOpened) {
            ChatHandler.sendChat(player, "christmas.alreadygifted");
         } else {
            ChatHandler.sendChat(player, "christmas.notchristmas");
         }
      }

      return new ActionResult(EnumActionResult.SUCCESS, itemstack);
   }

   private boolean isChristmas() {
      LocalDate localDate = LocalDate.now();
      return localDate.getMonth() == Month.DECEMBER && localDate.getDayOfMonth() >= 25;
   }
}
