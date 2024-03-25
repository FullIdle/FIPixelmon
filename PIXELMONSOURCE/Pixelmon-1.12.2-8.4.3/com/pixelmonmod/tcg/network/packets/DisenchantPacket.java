package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DisenchantPacket implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static void disenchantCard(EntityPlayerMP player) {
      if (player != null) {
         ItemStack stack = player.func_184614_ca();
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() == TCG.itemCard) {
            NBTTagCompound tag = stack.func_77978_p();
            if (tag != null) {
               ImmutableCard card = CardRegistry.fromId(tag.func_74762_e(ImmutableCard.CARD_ID_NBT_KEY));
               if (card != null && card.getMainEnergy() != null) {
                  player.field_70170_p.func_184148_a(player, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187604_bf, SoundCategory.PLAYERS, 0.1F, 0.5F * ((player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 0.7F + 1.8F));
                  int cardDisenchantEssenceDivisor = TCGConfig.getInstance().cardDisenchantEssenceDivisor;
                  switch (card.getRarity()) {
                     case COMMON:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().commonCost / cardDisenchantEssenceDivisor);
                        break;
                     case UNCOMMON:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().uncommonCost / cardDisenchantEssenceDivisor);
                        break;
                     case RARE:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().rareCost / cardDisenchantEssenceDivisor);
                        break;
                     case HOLORARE:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().holorareCost / cardDisenchantEssenceDivisor);
                        break;
                     case ULTRARARE:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().ultrarareCost / cardDisenchantEssenceDivisor);
                        break;
                     case SECRETRARE:
                        EssenceHelper.givePlayerEssenceFromEnergy(player, card.getMainEnergy(), TCGConfig.getInstance().secretrareCost / cardDisenchantEssenceDivisor);
                  }

                  player.func_184614_ca().func_190918_g(1);
               }
            }
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DisenchantPacket message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            DisenchantPacket.disenchantCard(player);
         });
         return null;
      }
   }
}
