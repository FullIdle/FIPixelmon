package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class MegaEvolutionEvent extends Event {
   public final ItemMegaStone stone;
   public final boolean ultraBurst;

   public MegaEvolutionEvent(ItemMegaStone stone, boolean ultraBurst) {
      this.stone = stone;
      this.ultraBurst = ultraBurst;
   }

   public static class PostEvolve extends MegaEvolutionEvent {
      public final EntityPlayerMP player;
      public final EntityPixelmon preEvo;
      public final EntityPixelmon postEvo;

      public PostEvolve(EntityPlayerMP player, EntityPixelmon preEvo, ItemMegaStone stone, boolean ultraBurst, EntityPixelmon postEvo) {
         super(stone, ultraBurst);
         this.player = player;
         this.preEvo = preEvo;
         this.postEvo = postEvo;
      }
   }

   @Cancelable
   public static class BattleEvolve extends MegaEvolutionEvent {
      public final PixelmonWrapper pw;

      public BattleEvolve(PixelmonWrapper pw, ItemMegaStone stone, boolean ultraBurst) {
         super(stone, ultraBurst);
         this.pw = pw;
      }
   }
}
