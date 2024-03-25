package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class FossilMachineEvent extends Event {
   private final EntityPlayerMP player;
   private final TileEntity tileEntity;

   protected FossilMachineEvent(EntityPlayerMP player, TileEntity tileEntity) {
      this.player = player;
      this.tileEntity = tileEntity;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public TileEntity getTileEntity() {
      return this.tileEntity;
   }

   @Cancelable
   public static class RemoveFossil extends FossilMachineEvent {
      private EnumFossils[] fossils;

      public RemoveFossil(EntityPlayerMP player, EnumFossils[] fossils, TileEntity tileEntity) {
         super(player, tileEntity);
         this.fossils = fossils;
      }

      public EnumFossils[] getFossils() {
         return this.fossils;
      }

      public void setFossils(EnumFossils[] fossils) {
         this.fossils = fossils;
      }
   }

   @Cancelable
   public static class PutFossil extends FossilMachineEvent {
      private EnumFossils fossil;

      public PutFossil(EntityPlayerMP player, EnumFossils fossil, TileEntity tileEntity) {
         super(player, tileEntity);
         this.fossil = fossil;
      }

      public EnumFossils getFossil() {
         return this.fossil;
      }

      public void setFossil(EnumFossils fossil) {
         this.fossil = fossil;
      }
   }

   @Cancelable
   public static class RemovePokeball extends FossilMachineEvent {
      private EnumPokeballs pokeball;

      public RemovePokeball(EntityPlayerMP player, EnumPokeballs pokeball, TileEntity tileEntity) {
         super(player, tileEntity);
         this.pokeball = pokeball;
      }

      public EnumPokeballs getPokeball() {
         return this.pokeball;
      }

      public void setPokeball(EnumPokeballs pokeball) {
         this.pokeball = pokeball;
      }
   }

   @Cancelable
   public static class PutPokeball extends FossilMachineEvent {
      private EnumPokeballs pokeball;

      public PutPokeball(EntityPlayerMP player, EnumPokeballs pokeball, TileEntity tileEntity) {
         super(player, tileEntity);
         this.pokeball = pokeball;
      }

      public EnumPokeballs getPokeball() {
         return this.pokeball;
      }

      public void setPokeball(EnumPokeballs pokeball) {
         this.pokeball = pokeball;
      }
   }
}
