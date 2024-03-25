package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.listener.RotomListener;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReplaceMove implements IMessage {
   private UUID pokemonUUID;
   private int attackId;
   private int replaceIndex;
   private boolean checkEvo;

   /** @deprecated */
   @Deprecated
   public ReplaceMove() {
   }

   public ReplaceMove(UUID pokemonUUID, int attackId, int replaceIndex, boolean checkEvo) {
      this.pokemonUUID = pokemonUUID;
      this.attackId = attackId;
      this.replaceIndex = replaceIndex;
      this.checkEvo = checkEvo;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.attackId = buffer.readInt();
      this.replaceIndex = buffer.readInt();
      this.checkEvo = buffer.readBoolean();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeInt(this.attackId);
      buffer.writeInt(this.replaceIndex);
      buffer.writeBoolean(this.checkEvo);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ReplaceMove message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         BattleControllerBase bc = BattleRegistry.getBattle(player);
         PokemonLink pokemonLink = null;
         PixelmonWrapper pw = null;
         if (bc != null) {
            PlayerParticipant participant = bc.getPlayer((EntityPlayer)player);
            if (participant != null) {
               pw = participant.getPokemonFromParty(message.pokemonUUID);
               if (pw != null) {
                  pokemonLink = new WrapperLink(pw);
               }
            }
         }

         if (pokemonLink == null) {
            pokemonLink = new DelegateLink(Pixelmon.storageManager.getParty(player).find(message.pokemonUUID));
         }

         if (((PokemonLink)pokemonLink).getPokemon() != null) {
            Attack attack = new Attack(message.attackId);
            boolean learnMove = true;
            if (attack.getMove() == null) {
               learnMove = false;
            }

            if (learnMove) {
               if (message.replaceIndex == -1) {
                  learnMove = false;
               }

               if (learnMove) {
                  boolean messageSent = LearnMoveController.hasCondition(player, message.pokemonUUID, attack.getActualMove());
                  if (!LearnMoveController.canLearnMove(player, message.pokemonUUID, attack.getActualMove())) {
                     if (!messageSent) {
                        ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.npc.cantpay");
                     }

                     learnMove = false;
                  }

                  if (learnMove && message.replaceIndex > -1) {
                     Moveset moveset = ((PokemonLink)pokemonLink).getMoveset();
                     TextComponentTranslation oldMoveName = moveset.get(message.replaceIndex).getMove().getTranslatedName();
                     moveset.set(message.replaceIndex, attack);
                     TextComponentTranslation chatMessage = ChatHandler.getMessage("replacemove.replace", ((PokemonLink)pokemonLink).getRealNickname(), oldMoveName, attack.getMove().getTranslatedName());
                     if (bc != null && !bc.battleEnded) {
                        bc.sendToPlayer(player, chatMessage);
                     } else {
                        ChatHandler.sendChat(player, chatMessage);
                     }

                     if (pw != null && bc.battleEnded) {
                        pw.writeToNBT();
                     }

                     ((PokemonLink)pokemonLink).update(EnumUpdateType.Moveset);
                     if (((PokemonLink)pokemonLink).getSpecies() == EnumSpecies.Rotom) {
                        RotomListener.replacedMove((PokemonLink)pokemonLink, attack);
                     }
                  }
               }
            }

            if (message.checkEvo) {
               if (pw != null) {
                  pw.bc.checkedPokemon.add(pw.pokemon);
               } else {
                  ((PokemonLink)pokemonLink).getPokemon().tryEvolution();
               }
            }

         }
      }
   }
}
