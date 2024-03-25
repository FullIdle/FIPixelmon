package com.pixelmonmod.pixelmon.api.events.legendary;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeAssembly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class ZygardeEvent extends Event {
   public final EntityPlayerMP player;
   public final TileEntityZygardeAssembly unit;

   public ZygardeEvent(EntityPlayerMP player, TileEntityZygardeAssembly unit) {
      this.player = player;
      this.unit = unit;
   }

   @Cancelable
   public abstract static class Assemble extends ZygardeEvent {
      public final Pokemon zygarde;
      public final StoragePosition storagePosition;
      public final ItemStack cube;
      public int cells;
      public int cores;

      public Assemble(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
         super(player, unit);
         this.zygarde = zygarde;
         this.storagePosition = storagePosition;
         this.cube = cube;
         this.cells = cells;
         this.cores = cores;
      }

      public abstract static class Merge extends Assemble {
         public Merge(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
            super(player, unit, zygarde, storagePosition, cube, cells, cores);
         }

         public static class End extends Merge {
            public End(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
               super(player, unit, zygarde, storagePosition, cube, cells, cores);
            }
         }

         @Cancelable
         public static class Start extends Merge {
            public Start(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
               super(player, unit, zygarde, storagePosition, cube, cells, cores);
            }
         }
      }

      public abstract static class New extends Assemble {
         public New(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
            super(player, unit, zygarde, storagePosition, cube, cells, cores);
         }

         public static class End extends New {
            public End(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
               super(player, unit, zygarde, storagePosition, cube, cells, cores);
            }
         }

         @Cancelable
         public static class Start extends New {
            public Start(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
               super(player, unit, zygarde, storagePosition, cube, cells, cores);
            }
         }
      }
   }

   @Cancelable
   public static class Seperate extends ZygardeEvent {
      public final Pokemon zygarde;
      public final StoragePosition storagePosition;
      public final ItemStack cube;
      public int cells;
      public int cores;

      public Seperate(EntityPlayerMP player, TileEntityZygardeAssembly unit, Pokemon zygarde, StoragePosition storagePosition, ItemStack cube, int cells, int cores) {
         super(player, unit);
         this.zygarde = zygarde;
         this.storagePosition = storagePosition;
         this.cube = cube;
         this.cells = cells;
         this.cores = cores;
      }
   }

   @Cancelable
   public static class Select extends ZygardeEvent {
      public final TileEntityZygardeAssembly.Mode mode;
      public final StoragePosition storagePosition;
      public final ItemStack cube;
      public final int cubeSlot;

      public Select(EntityPlayerMP player, TileEntityZygardeAssembly unit, TileEntityZygardeAssembly.Mode mode, StoragePosition storagePosition, ItemStack cube, int cubeSlot) {
         super(player, unit);
         this.mode = mode;
         this.storagePosition = storagePosition;
         this.cube = cube;
         this.cubeSlot = cubeSlot;
      }
   }

   @Cancelable
   public static class Activate extends ZygardeEvent {
      public Activate(EntityPlayerMP player, TileEntityZygardeAssembly unit) {
         super(player, unit);
      }
   }
}
