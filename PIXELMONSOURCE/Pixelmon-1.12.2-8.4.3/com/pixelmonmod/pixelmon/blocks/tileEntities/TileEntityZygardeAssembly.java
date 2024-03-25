package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.legendary.ZygardeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PowerConstruct;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import com.pixelmonmod.pixelmon.spawning.ZygardeCellsSpawner;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;

public class TileEntityZygardeAssembly extends TileEntity implements ITickable {
   private int ticks = 0;
   private Mode mode;
   private UUID uuid;
   private StoragePosition position;
   private int cubeSlot;

   public TileEntityZygardeAssembly() {
      this.mode = TileEntityZygardeAssembly.Mode.INACTIVE;
      this.uuid = null;
      this.position = null;
      this.cubeSlot = -999;
   }

   public void activate(EntityPlayerMP player, IBlockState state, ItemStack hand) {
      if (ZygardeCellsSpawner.checkForCube(player)) {
         if (!Pixelmon.EVENT_BUS.post(new ZygardeEvent.Activate(player, this))) {
            OpenScreen.open(player, EnumGuiScreen.ZygardeAssembly, this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p());
         }
      } else {
         ChatHandler.sendChat(player, "pixelmon.blocks.reassembly_unit.nocube");
      }

   }

   public void onSelection(EntityPlayerMP player, Mode mode, StoragePosition position, int cubeSlot) {
      ItemStack stack = player.field_71071_by.func_70301_a(cubeSlot);
      if (stack.func_77973_b() instanceof ItemZygardeCube) {
         if (!Pixelmon.EVENT_BUS.post(new ZygardeEvent.Select(player, this, mode, position, stack, cubeSlot))) {
            this.uuid = player.func_110124_au();
            this.setMode(mode);
            this.position = position;
            this.cubeSlot = cubeSlot;
         }

      }
   }

   public void setMode(Mode mode) {
      this.mode = mode;
      if (mode == TileEntityZygardeAssembly.Mode.INACTIVE) {
         this.ticks = 0;
      }

      this.func_70296_d();
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }

   public Mode getMode() {
      return this.mode;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.mode != TileEntityZygardeAssembly.Mode.INACTIVE) {
            ++this.ticks;
            if (this.ticks > 30) {
               EntityPlayer ep = this.func_145831_w().func_152378_a(this.uuid);
               if (!(ep instanceof EntityPlayerMP)) {
                  this.reset();
                  return;
               }

               EntityPlayerMP player = (EntityPlayerMP)ep;
               ItemStack cube = player.field_71071_by.func_70301_a(this.cubeSlot);
               if (!(cube.func_77973_b() instanceof ItemZygardeCube)) {
                  this.reset();
                  return;
               }

               int cellsToRemove;
               PlayerPartyStorage storage;
               Pokemon pokemon;
               if (this.mode == TileEntityZygardeAssembly.Mode.SEPARATING && this.position != null) {
                  storage = Pixelmon.storageManager.getParty(this.uuid);
                  pokemon = storage.get(this.position);
                  if (storage.countPokemon() == 1) {
                     ChatHandler.sendChat(player, "pixelmon.gui.zygarde_reassembly.last_pokemon");
                     this.reset();
                     return;
                  }

                  if (pokemon != null && pokemon.getSpecies() == EnumSpecies.Zygarde && !(pokemon.getAbility() instanceof PowerConstruct)) {
                     cellsToRemove = pokemon.getFormEnum() == EnumZygarde.TEN_PERCENT ? 10 : (pokemon.getFormEnum() == EnumZygarde.FIFTY_PERCENT ? 50 : -1);
                     int cores = 1;
                     if (cellsToRemove != -1) {
                        ZygardeEvent.Seperate event = new ZygardeEvent.Seperate(player, this, pokemon, this.position, cube, cellsToRemove, cores);
                        if (!Pixelmon.EVENT_BUS.post(event)) {
                           storage.retrieveAll();
                           storage.set(this.position, (Pokemon)null);
                           ItemZygardeCube.setCellCount(cube, ItemZygardeCube.getCellCount(cube) + event.cells);
                           ItemZygardeCube.setCoreCount(cube, ItemZygardeCube.getCoreCount(cube) + event.cores);
                        }
                     }
                  }
               } else if (this.mode == TileEntityZygardeAssembly.Mode.ASSEMBLING && this.position != null) {
                  storage = Pixelmon.storageManager.getParty(this.uuid);
                  pokemon = storage.get(this.position);
                  if (pokemon != null && pokemon.getSpecies() == EnumSpecies.Zygarde && !(pokemon.getAbility() instanceof PowerConstruct)) {
                     cellsToRemove = ItemZygardeCube.getCellCount(cube);
                     int cores = ItemZygardeCube.getCoreCount(cube);
                     ZygardeEvent.Assemble.Merge.Start startEvent = new ZygardeEvent.Assemble.Merge.Start(player, this, pokemon, this.position, cube, cellsToRemove, cores);
                     if (!Pixelmon.EVENT_BUS.post(startEvent)) {
                        cellsToRemove = startEvent.cells;
                        cores = startEvent.cores;
                        int cellsExpended = 0;
                        int coresExpended = 0;
                        if (pokemon.getFormEnum() == EnumZygarde.TEN_PERCENT) {
                           if (cellsToRemove >= 90 && cores >= 1) {
                              pokemon.setForm(EnumZygarde.FIFTY_PERCENT);
                              pokemon.setAbility("PowerConstruct");
                              cellsExpended = 90;
                              coresExpended = 1;
                           } else if (cellsToRemove >= 40) {
                              pokemon.setForm(EnumZygarde.FIFTY_PERCENT);
                              pokemon.setAbility("AuraBreak");
                              cellsExpended = 40;
                           }
                        } else if (pokemon.getFormEnum() == EnumZygarde.FIFTY_PERCENT && cellsToRemove >= 50 && cores >= 1) {
                           pokemon.setForm(EnumZygarde.FIFTY_PERCENT);
                           pokemon.setAbility("PowerConstruct");
                           cellsExpended = 50;
                           coresExpended = 1;
                        }

                        ZygardeEvent.Assemble.Merge.End endEvent = new ZygardeEvent.Assemble.Merge.End(player, this, pokemon, this.position, cube, cellsExpended, coresExpended);
                        Pixelmon.EVENT_BUS.post(endEvent);
                        ItemZygardeCube.setCellCount(cube, ItemZygardeCube.getCellCount(cube) - endEvent.cells);
                        ItemZygardeCube.setCoreCount(cube, ItemZygardeCube.getCoreCount(cube) - endEvent.cores);
                     }
                  }
               } else if (this.mode == TileEntityZygardeAssembly.Mode.ASSEMBLING) {
                  int count = ItemZygardeCube.getCellCount(cube);
                  int cores = ItemZygardeCube.getCoreCount(cube);
                  Pokemon zygarde = Pixelmon.pokemonFactory.create(EnumSpecies.Zygarde);
                  zygarde.initialize();
                  ZygardeEvent.Assemble.New.Start startEvent = new ZygardeEvent.Assemble.New.Start(player, this, zygarde, this.position, cube, count, cores);
                  if (!Pixelmon.EVENT_BUS.post(startEvent)) {
                     count = startEvent.cells;
                     cores = startEvent.cores;
                     if (count >= 100 && cores >= 2) {
                        zygarde.setForm(EnumZygarde.FIFTY_PERCENT);
                        zygarde.setAbility("PowerConstruct");
                        cellsToRemove = 100;
                     } else if (count >= 50 && cores >= 1) {
                        zygarde.setForm(EnumZygarde.FIFTY_PERCENT);
                        zygarde.setAbility("AuraBreak");
                        cellsToRemove = 50;
                     } else {
                        if (count < 10 || cores < 1) {
                           this.reset();
                           return;
                        }

                        zygarde.setForm(EnumZygarde.TEN_PERCENT);
                        zygarde.setAbility("AuraBreak");
                        cellsToRemove = 10;
                     }

                     zygarde.setLevel(30);
                     Pixelmon.storageManager.getParty(this.uuid).add(zygarde);
                     ZygardeEvent.Assemble.New.End endEvent = new ZygardeEvent.Assemble.New.End(player, this, zygarde, this.position, cube, cellsToRemove, zygarde.getAbility() instanceof PowerConstruct ? 2 : 1);
                     Pixelmon.EVENT_BUS.post(endEvent);
                     ItemZygardeCube.setCellCount(cube, ItemZygardeCube.getCellCount(cube) - endEvent.cells);
                     ItemZygardeCube.setCoreCount(cube, ItemZygardeCube.getCoreCount(cube) - endEvent.cores);
                  }
               }

               this.reset();
            }
         }

      }
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      return super.func_189515_b(compound);
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound compound = super.func_189517_E_();
      compound.func_74774_a("Mode", (byte)this.mode.ordinal());
      return compound;
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.func_148857_g();
      this.func_145839_a(compound);
      this.mode = TileEntityZygardeAssembly.Mode.fromOrdinal(compound.func_74771_c("Mode"));
   }

   public boolean shouldRenderInPass(int pass) {
      return pass == 0 || pass == 1;
   }

   private void reset() {
      this.mode = TileEntityZygardeAssembly.Mode.INACTIVE;
      this.ticks = 0;
      this.uuid = null;
      this.position = null;
      this.cubeSlot = -999;
      this.func_70296_d();
      ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
   }

   public static enum Mode {
      INACTIVE,
      ASSEMBLING,
      SEPARATING;

      private static final Mode[] VALUES = values();

      public static Mode fromOrdinal(int ordinal) {
         return VALUES[ordinal];
      }
   }
}
