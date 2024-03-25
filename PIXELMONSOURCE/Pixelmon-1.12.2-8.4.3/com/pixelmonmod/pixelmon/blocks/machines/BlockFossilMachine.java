package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.FossilMachineEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilMachine;
import com.pixelmonmod.pixelmon.config.PixelmonItemsFossils;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockFossilMachine extends MultiBlock {
   public BlockFossilMachine() {
      super(Material.field_151573_f, 1, 2.0, 1);
      this.func_149711_c(1.0F);
      this.func_149663_c("fossilmachine");
   }

   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      if (state.func_177229_b(MULTIPOS) == EnumMultiPos.BASE) {
         TileEntityFossilMachine machine = (TileEntityFossilMachine)BlockHelper.getTileEntity(TileEntityFossilMachine.class, world, pos);
         if (machine != null && machine.fossilProgress > 1.0F && machine.pokemonProgress < 3200.0F) {
            world.func_184134_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundEvents.field_187812_eh, SoundCategory.BLOCKS, 0.01F, rand.nextFloat() * 0.4F + 0.8F, true);
         }
      }

   }

   public void capturePokemonInMachine(World world, BlockPos pos, EntityPlayer player, EnumFacing facing, float par7, float par8, float par9) {
      TileEntityFossilMachine tile = (TileEntityFossilMachine)world.func_175625_s(pos);
      if (tile != null) {
         PokemonSpec spec = PokemonSpec.from(tile.currentPokemon);
         spec.level = 1;
         Pokemon pokemon = spec.create();
         pokemon.setShiny(tile.isShiny);
         pokemon.setCaughtBall(tile.pokeball);
         boolean hasSummer = false;
         Iterator var12 = Pixelmon.storageManager.getParty((EntityPlayerMP)player).getTeam().iterator();

         while(var12.hasNext()) {
            Pokemon p = (Pokemon)var12.next();
            if (p != null && p.getFormEnum() == EnumSpecial.Summer) {
               hasSummer = true;
            }
         }

         if (hasSummer && EnumSpecies.summerTextured.contains(pokemon.getSpecies())) {
            pokemon.setForm(EnumSpecial.Summer);
         }

         Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP)player, ReceiveType.Fossil, pokemon));
         Pixelmon.storageManager.getParty((EntityPlayerMP)player).add(pokemon);
         pokemon.setFriendship(pokemon.getBaseStats().getBaseFriendship());
         ((WorldServer)world).func_184164_w().func_180244_a(pos);
      }

   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         ItemStack handStack = player.func_184586_b(hand);
         pos = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
         TileEntityFossilMachine tile = (TileEntityFossilMachine)BlockHelper.getTileEntity(TileEntityFossilMachine.class, world, pos);
         PlayerChunkMap playerChunkMap = ((WorldServer)world).func_184164_w();
         Item handItem = null;
         if (!handStack.func_190926_b()) {
            handItem = handStack.func_77973_b();
         }

         boolean heldItemIsFossil = handItem instanceof ItemFossil;
         boolean heldItemIsPokeBall = handItem instanceof ItemPokeball;
         boolean obtainedPokemon = tile.pokeball != null && tile.pokemonOccupied && tile.pokemonProgress == 3200.0F;
         if (!obtainedPokemon) {
            if (tile.pokeball != null && !heldItemIsFossil) {
               ItemPokeball item = tile.pokeball.getItem();
               FossilMachineEvent.RemovePokeball event = new FossilMachineEvent.RemovePokeball((EntityPlayerMP)player, tile.pokeball, tile);
               if (!Pixelmon.EVENT_BUS.post(event)) {
                  DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(event.getPokeball().getItem()), false);
                  tile.pokeball = null;
               }
            }

            EnumFossils prevFossil = tile.currentFossil;
            if (tile.currentFossil != null && !heldItemIsPokeBall) {
               boolean dropExisting = prevFossil != EnumFossils.NULL;
               if (heldItemIsFossil) {
                  ItemFossil itemFossil = (ItemFossil)handItem;
                  if (tile.currentFossil == EnumFossils.BIRD) {
                     if (itemFossil.fossil == EnumFossils.DRAKE) {
                        tile.currentFossil = EnumFossils.DZ;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }

                     if (itemFossil.fossil == EnumFossils.DINO) {
                        tile.currentFossil = EnumFossils.AZ;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }
                  }

                  if (tile.currentFossil == EnumFossils.FISH) {
                     if (itemFossil.fossil == EnumFossils.DRAKE) {
                        tile.currentFossil = EnumFossils.DV;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }

                     if (itemFossil.fossil == EnumFossils.DINO) {
                        tile.currentFossil = EnumFossils.AV;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }
                  }

                  if (tile.currentFossil == EnumFossils.DRAKE) {
                     if (itemFossil.fossil == EnumFossils.FISH) {
                        tile.currentFossil = EnumFossils.DV;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }

                     if (itemFossil.fossil == EnumFossils.BIRD) {
                        tile.currentFossil = EnumFossils.DZ;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }
                  }

                  if (tile.currentFossil == EnumFossils.DINO) {
                     if (itemFossil.fossil == EnumFossils.FISH) {
                        tile.currentFossil = EnumFossils.AV;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }

                     if (itemFossil.fossil == EnumFossils.BIRD) {
                        tile.currentFossil = EnumFossils.AZ;
                        if (!player.field_71075_bZ.field_75098_d) {
                           handStack.func_190918_g(1);
                        }

                        playerChunkMap.func_180244_a(pos);
                        dropExisting = false;
                     }
                  }
               }

               if (dropExisting) {
                  EnumFossils[] fossilItems = new EnumFossils[2];
                  if (prevFossil.getIndex() < -1) {
                     switch (prevFossil) {
                        case AV:
                           fossilItems[0] = EnumFossils.FISH;
                           fossilItems[1] = EnumFossils.DINO;
                           break;
                        case AZ:
                           fossilItems[0] = EnumFossils.BIRD;
                           fossilItems[1] = EnumFossils.DINO;
                           break;
                        case DV:
                           fossilItems[0] = EnumFossils.FISH;
                           fossilItems[1] = EnumFossils.DRAKE;
                           break;
                        case DZ:
                           fossilItems[0] = EnumFossils.DRAKE;
                           fossilItems[1] = EnumFossils.BIRD;
                     }
                  } else {
                     fossilItems[0] = prevFossil;
                     fossilItems[1] = null;
                  }

                  FossilMachineEvent.RemoveFossil event = new FossilMachineEvent.RemoveFossil((EntityPlayerMP)player, fossilItems, tile);
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     fossilItems = event.getFossils();

                     for(int i = 0; i < fossilItems.length; ++i) {
                        if (fossilItems[i] != null) {
                           DropItemHelper.giveItemStack((EntityPlayerMP)player, PixelmonItemsFossils.getCleanFromEnum(fossilItems[i]), false);
                        }
                     }

                     tile.currentFossil = EnumFossils.NULL;
                     tile.fossilProgress = 0.0F;
                     tile.pokemonProgress = 0.0F;
                  }
               }
            }

            if (!handStack.func_190926_b()) {
               if (heldItemIsPokeBall && tile.pokeball == null) {
                  EnumPokeballs pokeballToPut = ((ItemPokeball)player.func_184614_ca().func_77973_b()).type;
                  FossilMachineEvent.PutPokeball event = new FossilMachineEvent.PutPokeball((EntityPlayerMP)player, pokeballToPut, tile);
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     tile.pokeball = event.getPokeball();
                     handStack.func_190918_g(1);
                     playerChunkMap.func_180244_a(pos);
                  }

                  return true;
               }

               if (heldItemIsFossil && tile.currentFossil == EnumFossils.NULL && !tile.pokemonOccupied) {
                  ItemFossil itemFossil = (ItemFossil)handItem;
                  FossilMachineEvent.PutFossil event = new FossilMachineEvent.PutFossil((EntityPlayerMP)player, itemFossil.fossil, tile);
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     tile.currentFossil = event.getFossil();
                     if (!player.field_71075_bZ.field_75098_d) {
                        handStack.func_190918_g(1);
                     }

                     playerChunkMap.func_180244_a(pos);
                  }

                  return true;
               }
            }
         }

         if (obtainedPokemon) {
            this.capturePokemonInMachine(world, pos, player, facing, hitX, hitY, hitZ);
         }

         if (!obtainedPokemon && (tile.pokemonOccupied || heldItemIsFossil || heldItemIsPokeBall)) {
            playerChunkMap.func_180244_a(pos);
            return false;
         } else {
            tile.pokemonOccupied = false;
            tile.fossilProgress = 0.0F;
            tile.pokemonProgress = 0.0F;
            tile.currentFossil = EnumFossils.NULL;
            tile.currentPokemon = "";
            tile.pokeball = null;
            tile.completionRate = 0;
            playerChunkMap.func_180244_a(pos);
            return true;
         }
      } else {
         return true;
      }
   }

   public int func_149745_a(Random random) {
      return 1;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   protected Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityFossilMachine());
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return Item.func_150898_a(this);
   }
}
