package com.pixelmonmod.pixelmon.api.events.legendary;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBell;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class Gen2BellEvent extends Event {
   public final UUID playerUUID;

   public Gen2BellEvent(UUID playerUUID) {
      this.playerUUID = playerUUID;
   }

   @Nullable
   public EntityPlayerMP getPlayer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.playerUUID);
   }

   public static class SummonLegendary extends ArceusEvent {
      public final TileEntityBell bell;

      public SummonLegendary(UUID playerUUID, TileEntityBell bell) {
         super(playerUUID);
         this.bell = bell;
      }
   }

   @Cancelable
   public static class RollSuccessEvent extends Gen2BellEvent {
      public final TileEntityBell bell;
      public double chance;

      public RollSuccessEvent(UUID playerUUID, TileEntityBell bell) {
         super(playerUUID);
         this.chance = PixelmonConfig.bellSuccessChance;
         this.bell = bell;
      }
   }
}
