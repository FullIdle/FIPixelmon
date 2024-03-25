package com.pixelmonmod.tcg.network;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public class ByteBufTCG {
   private static final int COMMON_TYPE = 0;
   private static final int POKEMON_TYPE = 1;
   private static final int TRAINER_TYPE = 2;

   public static void writeBlockPos(ByteBuf buf, BlockPos blockPos) {
      buf.writeInt(blockPos.func_177958_n());
      buf.writeInt(blockPos.func_177956_o());
      buf.writeInt(blockPos.func_177952_p());
   }

   public static BlockPos readBlockPos(ByteBuf buf) {
      return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
   }

   public static void writeCard(ByteBuf buf, ImmutableCard card) {
      buf.writeInt(card.getID());
   }

   public static void writeFaceDownCard(ByteBuf buf) {
      buf.writeInt(ImmutableCard.FACE_DOWN_ID);
   }

   public static ImmutableCard readCard(ByteBuf buf) {
      int cardId = buf.readInt();
      return cardId == ImmutableCard.FACE_DOWN_ID ? new ImmutableCard() : CardRegistry.fromId(cardId);
   }

   public static void writeCardList(ByteBuf buf, List cards) {
      buf.writeInt(cards.size());
      Iterator var2 = cards.iterator();

      while(var2.hasNext()) {
         ImmutableCard card = (ImmutableCard)var2.next();
         writeCard(buf, card);
      }

   }

   public static void writeFaceDownCardList(ByteBuf buf, List cards) {
      buf.writeInt(cards.size());
      Iterator var2 = cards.iterator();

      while(var2.hasNext()) {
         ImmutableCard card = (ImmutableCard)var2.next();
         writeFaceDownCard(buf);
      }

   }

   public static List readCardList(ByteBuf buf) {
      int size = buf.readInt();
      List cards = new ArrayList();

      for(int i = 0; i < size; ++i) {
         cards.add(readCard(buf));
      }

      return cards;
   }

   public static CommonCardState readCardState(ByteBuf buf) {
      int type = buf.readInt();
      switch (type) {
         case 0:
            return new CommonCardState(buf);
         case 1:
            return new PokemonCardState(buf);
         case 2:
            return new TrainerCardState(buf);
         default:
            return null;
      }
   }

   public static void writeCardState(ByteBuf buf, CommonCardState cardState) {
      if (cardState instanceof PokemonCardState) {
         buf.writeInt(1);
      } else if (cardState instanceof TrainerCardState) {
         buf.writeInt(2);
      } else {
         buf.writeInt(0);
      }

      cardState.write(buf, true);
   }
}
