package com.pixelmonmod.tcg.network.packets;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.events.PackOpenEvent;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.item.ItemPack;
import com.pixelmonmod.tcg.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenPackPacket implements IMessage {
   private int id;

   public OpenPackPacket() {
   }

   public OpenPackPacket(int id) {
      this.id = id;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.id);
   }

   public int getId() {
      return this.id;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(OpenPackPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_71121_q().func_152344_a(() -> {
            if (player.func_184614_ca() != ItemStack.field_190927_a) {
               if (player.func_184614_ca().func_77973_b() instanceof ItemPack) {
                  if (!TCG.EVENT_BUS.post(new PackOpenEvent.Pre(player, player.func_184614_ca()))) {
                     int setID = player.func_184614_ca().func_77978_p().func_74762_e("SetID");
                     float weight = player.func_184614_ca().func_77978_p().func_74760_g("Weight");
                     ImmutableCard[] c = new ImmutableCard[]{CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.COMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.UNCOMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.UNCOMMON, false), CardRegistry.getRandomCardAdvanced(setID, CardRarity.UNCOMMON, false), CardRegistry.getRareOrBetterCard(setID, weight, true)};
                     int dropped;
                     int givenCards;
                     if (Pixelmon.devEnvironment) {
                        ImmutableCard[] var5 = c;
                        dropped = c.length;

                        for(givenCards = 0; givenCards < dropped; ++givenCards) {
                           ImmutableCard immutableCardx = var5[givenCards];
                           player.func_145747_a(new TextComponentString("Got card : " + immutableCardx.getTranslatedName() + " with rarity: " + immutableCardx.getRarity().toString()));
                        }
                     }

                     ItemStack packItem = player.field_71071_by.func_70448_g();
                     player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
                     dropped = 0;
                     givenCards = 0;

                     for(int i = 0; i < c.length; ++i) {
                        ImmutableCard cz = c[i];
                        if (cz != null) {
                           ItemStack itemStack = c[i].getItemStack(1);
                           if (!player.func_191521_c(itemStack)) {
                              player.func_71019_a(itemStack, true);
                              ++dropped;
                           }

                           ++givenCards;
                        }
                     }

                     TCG.EVENT_BUS.post(new PackOpenEvent.Post(player, packItem, Arrays.asList(c)));
                     List nonNullCards = Lists.newArrayList();
                     ImmutableCard[] nonNullCardArray = c;
                     int var17 = c.length;

                     for(int var11 = 0; var11 < var17; ++var11) {
                        ImmutableCard immutableCard = nonNullCardArray[var11];
                        if (immutableCard != null) {
                           nonNullCards.add(immutableCard);
                        }
                     }

                     nonNullCardArray = (ImmutableCard[])nonNullCards.toArray(new ImmutableCard[0]);
                     player.func_71120_a(player.field_71069_bz);
                     if (dropped > 0) {
                        ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.fullinventory", dropped + " " + I18n.func_74838_a(TCG.itemCard.func_77658_a() + ".name"));
                     }

                     if (givenCards > 0) {
                        PacketHandler.net.sendTo(new OpenPackGuiPacket(nonNullCardArray), player);
                     } else {
                        TCG.logger.error("Card Set: " + CardSetRegistry.get(setID).getName() + " has no valid cards!");
                     }
                  }
               } else {
                  ctx.getServerHandler().func_194028_b(new TextComponentString("[TCG] Malformed packet"));
               }
            }

         });
         return null;
      }
   }
}
