package com.pixelmonmod.pixelmon.api.events.legendary;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPlateHolder;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTimespaceAltar;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class ArceusEvent extends Event {
   private final UUID playerUUID;

   public ArceusEvent(UUID playerUUID) {
      this.playerUUID = playerUUID;
   }

   @Nullable
   public EntityPlayerMP getPlayer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.playerUUID);
   }

   public UUID getPlayerUUID() {
      return this.playerUUID;
   }

   @Cancelable
   public static class PlayFlute extends ArceusEvent {
      private final ItemStack azureFlute;
      private final TileEntityTimespaceAltar altar;

      public PlayFlute(EntityPlayerMP player, ItemStack azureFlute, TileEntityTimespaceAltar altar) {
         super(player.func_110124_au());
         this.azureFlute = azureFlute;
         this.altar = altar;
      }

      public ItemStack getAzureFlute() {
         return this.azureFlute;
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }
   }

   public static class CreateFlute extends ArceusEvent {
      private final TileEntityPlateHolder chalice;
      private final EntityItem azureFlute;

      public CreateFlute(UUID playerUUID, TileEntityPlateHolder chalice, EntityItem azureFlute) {
         super(playerUUID);
         this.chalice = chalice;
         this.azureFlute = azureFlute;
      }

      public TileEntityPlateHolder getChalice() {
         return this.chalice;
      }

      public EntityItem getAzureFlute() {
         return this.azureFlute;
      }
   }

   @Cancelable
   public static class AddPlate extends ArceusEvent {
      private final TileEntityPlateHolder chalice;
      private final ItemStack stack;

      public AddPlate(EntityPlayerMP player, TileEntityPlateHolder chalice, ItemStack stack) {
         super(player.func_110124_au());
         this.chalice = chalice;
         this.stack = stack;
      }

      public EnumPlate getPlate() {
         return EnumPlate.getPlateForItem(this.stack.func_77973_b());
      }

      public TileEntityPlateHolder getChalice() {
         return this.chalice;
      }

      public ItemStack getStack() {
         return this.stack;
      }
   }
}
