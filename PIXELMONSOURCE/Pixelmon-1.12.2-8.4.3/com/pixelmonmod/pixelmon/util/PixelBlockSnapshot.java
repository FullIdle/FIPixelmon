package com.pixelmonmod.pixelmon.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonSyntaxException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityGymSign;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.ConcurrentModificationException;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockStructureVoid;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockDoor.EnumHingePosition;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PixelBlockSnapshot {
   public static final String PIXEL_SNAPSHOT_BLOCK_NAME = "blockName";
   public final BlockPos pos;
   public transient IBlockState replacedBlock;
   private final NBTTagCompound nbt;
   public transient World world;
   public final ResourceLocation blockIdentifier;
   public final int meta;

   public static PixelBlockSnapshot readFromNBT(World newWorld, NBTTagCompound tag) {
      NBTTagCompound nbt = tag.func_74767_n("hasTE") ? tag.func_74775_l("tileEntity") : null;
      PixelBlockSnapshot snap = new PixelBlockSnapshot(new BlockPos(tag.func_74762_e("x"), tag.func_74762_e("y"), tag.func_74762_e("z")), tag.func_74764_b("blockMod") ? tag.func_74779_i("blockMod") : "minecraft", tag.func_74779_i("blockName"), tag.func_74762_e("metadata"), nbt);
      snap.world = newWorld;
      return snap;
   }

   public PixelBlockSnapshot(World world, BlockPos pos, IBlockState state) {
      this.world = world;
      this.pos = pos;
      this.replacedBlock = state;
      this.blockIdentifier = state.func_177230_c().getRegistryName();
      this.meta = state.func_177230_c().func_176201_c(state);
      TileEntity te = world.func_175625_s(pos);
      if (te != null) {
         this.nbt = new NBTTagCompound();
         te.func_189515_b(this.nbt);
      } else {
         this.nbt = null;
      }

   }

   public PixelBlockSnapshot(World world, BlockPos pos, IBlockState state, NBTTagCompound nbt) {
      this.world = world;
      this.pos = pos;
      this.replacedBlock = state;
      this.blockIdentifier = state.func_177230_c().getRegistryName();
      this.meta = state.func_177230_c().func_176201_c(state);
      this.nbt = nbt;
   }

   public PixelBlockSnapshot(BlockPos pos, String modid, String blockName, int meta, NBTTagCompound nbt) {
      this.pos = pos;
      this.blockIdentifier = new ResourceLocation(modid, blockName);
      this.meta = meta;
      this.nbt = nbt;
   }

   public PixelBlockSnapshot(BlockPos pos, IBlockState state, NBTTagCompound nbt) {
      this.pos = pos;
      this.blockIdentifier = state.func_177230_c().getRegistryName();
      this.meta = state.func_177230_c().func_176201_c(state);
      this.nbt = nbt;
   }

   public static PixelBlockSnapshot getBlockSnapshot(World world, BlockPos pos) {
      return new PixelBlockSnapshot(world, pos, world.func_180495_p(pos));
   }

   public static PixelBlockSnapshot readFromNBT(NBTTagCompound tag) {
      NBTTagCompound nbt = tag.func_74767_n("hasTE") ? null : tag.func_74775_l("tileEntity");
      return new PixelBlockSnapshot(new BlockPos(tag.func_74762_e("x"), tag.func_74762_e("y"), tag.func_74762_e("z")), tag.func_74764_b("blockMod") ? tag.func_74779_i("blockMod") : "minecraft", tag.func_74779_i("blockName"), tag.func_74762_e("metadata"), nbt);
   }

   public IBlockState getCurrentBlock() {
      return this.world.func_180495_p(this.pos);
   }

   public TileEntity getTileEntity() {
      return this.nbt != null ? TileEntity.func_190200_a(this.world, this.nbt) : null;
   }

   public boolean restore() {
      return this.restore(false);
   }

   public boolean restore(boolean force) {
      return this.restore(force, true);
   }

   public boolean restore(boolean force, boolean applyPhysics) {
      IBlockState current = this.getCurrentBlock();
      IBlockState replaced = this.getReplacedBlock();
      Block currentBlock = current.func_177230_c();
      Block replacedBlock = replaced.func_177230_c();
      if (currentBlock != replacedBlock || currentBlock.func_176201_c(current) != replacedBlock.func_176201_c(replaced)) {
         if (!force) {
            return false;
         }

         this.world.func_180501_a(this.pos, replaced, applyPhysics ? 3 : 2);
      }

      this.world.func_180501_a(this.pos, replaced, applyPhysics ? 3 : 2);
      this.world.func_184138_a(this.pos, current, replaced, applyPhysics ? 3 : 2);
      TileEntity te = null;
      if (this.nbt != null) {
         te = this.world.func_175625_s(this.pos);
         if (te != null) {
            te.func_145839_a(this.nbt);
         }
      }

      return true;
   }

   public boolean restoreToLocation(World world, BlockPos pos, boolean force, boolean applyPhysics) {
      IBlockState current = this.getCurrentBlock();
      IBlockState replaced = this.getReplacedBlock();
      if (current.func_177230_c() != replaced.func_177230_c() || current.func_177230_c().func_176201_c(current) != replaced.func_177230_c().func_176201_c(replaced)) {
         if (!force) {
            return false;
         }

         world.func_180501_a(pos, replaced, applyPhysics ? 3 : 2);
      }

      world.func_180501_a(pos, replaced, applyPhysics ? 3 : 2);
      world.func_184138_a(pos, current, replaced, applyPhysics ? 3 : 2);
      TileEntity te = null;
      if (this.nbt != null) {
         te = world.func_175625_s(pos);
         if (te != null) {
            te.func_145839_a(this.nbt);
         }
      }

      return true;
   }

   public void writeToNBT(NBTTagCompound compound) {
      if (!this.blockIdentifier.func_110624_b().equals("minecraft")) {
         compound.func_74778_a("blockMod", this.blockIdentifier.func_110624_b());
      }

      compound.func_74778_a("blockName", this.blockIdentifier.func_110623_a());
      compound.func_74768_a("x", this.pos.func_177958_n());
      compound.func_74768_a("y", this.pos.func_177956_o());
      compound.func_74768_a("z", this.pos.func_177952_p());
      compound.func_74768_a("metadata", this.meta);
      compound.func_74757_a("hasTE", this.nbt != null);
      if (this.nbt != null) {
         compound.func_74782_a("tileEntity", this.nbt);
      }

   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         PixelBlockSnapshot other = (PixelBlockSnapshot)obj;
         if (!this.pos.equals(other.pos)) {
            return false;
         } else if (this.meta != other.meta) {
            return false;
         } else if (this.nbt == other.nbt || this.nbt != null && this.nbt.equals(other.nbt)) {
            if (this.world == other.world || this.world != null && this.world.equals(other.world)) {
               return this.blockIdentifier == other.blockIdentifier || this.blockIdentifier != null && this.blockIdentifier.equals(other.blockIdentifier);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int hash = 7;
      hash = 73 * hash + this.pos.func_177958_n();
      hash = 73 * hash + this.pos.func_177956_o();
      hash = 73 * hash + this.pos.func_177952_p();
      hash = 73 * hash + this.meta;
      hash = 73 * hash + (this.nbt != null ? this.nbt.hashCode() : 0);
      hash = 73 * hash + (this.world != null ? this.world.hashCode() : 0);
      hash = 73 * hash + (this.blockIdentifier != null ? this.blockIdentifier.hashCode() : 0);
      return hash;
   }

   public IBlockState getReplacedBlock() {
      if (this.replacedBlock == null) {
         IForgeRegistry blockRegistry = GameRegistry.findRegistry(Block.class);

         try {
            this.replacedBlock = ((Block)blockRegistry.getValue(this.blockIdentifier)).func_176203_a(this.meta);
         } catch (Exception var3) {
            System.out.println("Cannot find Block " + this.blockIdentifier.func_110624_b() + ": " + this.blockIdentifier.func_110623_a());
         }
      }

      return this.replacedBlock;
   }

   public void restoreToLocationWithRotation(BlockPos pos, EnumFacing facing, int yLevel, boolean recreateTechnicalBlocks) {
      this.restoreToLocationWithRotation(pos, facing, yLevel, recreateTechnicalBlocks, (ItemStack)null);
   }

   public void restoreToLocationWithRotation(BlockPos pos, EnumFacing facing, int yLevel, boolean recreateTechnicalBlocks, ItemStack replacement) {
      IBlockState replaced = this.getReplacedBlock();
      replaced = this.checkOrientation(replaced, facing);
      if (recreateTechnicalBlocks || !(replaced.func_177230_c() instanceof BlockStructureVoid)) {
         int toCheck;
         try {
            if (replaced.func_177230_c() instanceof BlockDoor) {
               if (replaced.func_177229_b(BlockDoor.field_176523_O) == EnumDoorHalf.LOWER) {
                  ItemDoor.func_179235_a(this.world, pos, (EnumFacing)replaced.func_177229_b(BlockDoor.field_176520_a), replaced.func_177230_c(), replaced.func_177229_b(BlockDoor.field_176521_M) == EnumHingePosition.RIGHT);
               }
            } else if (replaced.func_177230_c() instanceof BlockBed) {
               if (replaced.func_177229_b(BlockBed.field_176472_a) == EnumPartType.FOOT) {
                  toCheck = this.nbt == null ? 0 : this.nbt.func_74762_e("color");
                  BlockHelper.placeBed(this.world, pos, (EnumFacing)replaced.func_177229_b(BlockBed.field_185512_D), EnumDyeColor.func_176764_b(toCheck));
               }
            } else {
               this.world.func_180501_a(pos, replaced, 3);
            }
         } catch (ConcurrentModificationException var13) {
            return;
         }

         this.world.func_184138_a(pos, replaced, replaced, 2);
         if (yLevel == 0) {
            for(toCheck = pos.func_177956_o() - 1; toCheck > 0; --toCheck) {
               BlockPos posDown = new BlockPos(pos.func_177958_n(), toCheck, pos.func_177952_p());
               IBlockState blockDown = this.world.func_180495_p(posDown);
               if (!(blockDown.func_177230_c() instanceof BlockAir) && !(blockDown.func_177230_c() instanceof BlockLiquid)) {
                  break;
               }

               try {
                  this.world.func_180501_a(posDown, replaced, 3);
               } catch (ConcurrentModificationException var12) {
                  return;
               }

               this.world.func_184138_a(posDown, replaced, replaced, 2);
            }
         }

         if (replaced.func_177230_c() instanceof BlockLever) {
            EnumFacing[] aenumfacing = EnumFacing.values();
            EnumFacing[] var16 = aenumfacing;
            int var18 = aenumfacing.length;

            for(int var10 = 0; var10 < var18; ++var10) {
               EnumFacing enumfacing = var16[var10];
               this.world.func_175685_c(pos.func_177972_a(enumfacing), replaced.func_177230_c(), false);
            }
         }

         if (this.nbt != null) {
            TileEntity te = this.world.func_175625_s(pos);
            if (te != null) {
               this.nbt.func_74768_a("x", pos.func_177958_n());
               this.nbt.func_74768_a("y", pos.func_177956_o());
               this.nbt.func_74768_a("z", pos.func_177952_p());

               try {
                  te.func_145839_a(this.nbt);
               } catch (JsonSyntaxException var14) {
                  if (Pixelmon.devEnvironment) {
                     var14.printStackTrace();
                  }
               }

               ((WorldServer)this.world).func_184164_w().func_180244_a(pos);
               if (te instanceof TileEntityGymSign) {
                  ((TileEntityGymSign)te).setDroppable(false);
                  if (replacement != null) {
                     ((TileEntityGymSign)te).setItemInSign(replacement);
                  }
               }
            }
         }

      }
   }

   private IBlockState checkOrientation(IBlockState replaced, EnumFacing facing) {
      if (facing != EnumFacing.EAST) {
         ImmutableMap properties = replaced.func_177228_b();
         if (properties.containsKey(BlockLever.field_176360_a)) {
            BlockLever.EnumOrientation orientation = (BlockLever.EnumOrientation)replaced.func_177229_b(BlockLever.field_176360_a);
            if (orientation == EnumOrientation.DOWN_X) {
               if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                  orientation = EnumOrientation.DOWN_Z;
               }
            } else if (orientation == EnumOrientation.DOWN_Z) {
               if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                  orientation = EnumOrientation.DOWN_X;
               }
            } else if (orientation == EnumOrientation.UP_X) {
               if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                  orientation = EnumOrientation.UP_Z;
               }
            } else if (orientation == EnumOrientation.UP_Z) {
               if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                  orientation = EnumOrientation.UP_X;
               }
            } else if (facing == EnumFacing.SOUTH) {
               if (orientation == EnumOrientation.EAST) {
                  orientation = EnumOrientation.SOUTH;
               } else if (orientation == EnumOrientation.SOUTH) {
                  orientation = EnumOrientation.WEST;
               } else if (orientation == EnumOrientation.WEST) {
                  orientation = EnumOrientation.NORTH;
               } else if (orientation == EnumOrientation.NORTH) {
                  orientation = EnumOrientation.EAST;
               }
            } else if (facing == EnumFacing.WEST) {
               if (orientation == EnumOrientation.EAST) {
                  orientation = EnumOrientation.WEST;
               } else if (orientation == EnumOrientation.SOUTH) {
                  orientation = EnumOrientation.NORTH;
               } else if (orientation == EnumOrientation.WEST) {
                  orientation = EnumOrientation.EAST;
               } else if (orientation == EnumOrientation.NORTH) {
                  orientation = EnumOrientation.SOUTH;
               }
            } else if (facing == EnumFacing.NORTH) {
               if (orientation == EnumOrientation.EAST) {
                  orientation = EnumOrientation.NORTH;
               } else if (orientation == EnumOrientation.SOUTH) {
                  orientation = EnumOrientation.EAST;
               } else if (orientation == EnumOrientation.WEST) {
                  orientation = EnumOrientation.SOUTH;
               } else if (orientation == EnumOrientation.NORTH) {
                  orientation = EnumOrientation.WEST;
               }
            }

            replaced = replaced.func_177226_a(BlockLever.field_176360_a, orientation);
         } else if (replaced.func_177230_c() instanceof BlockStandingSign) {
            int rotation = (Integer)replaced.func_177229_b(BlockStandingSign.field_176413_a);
            if (facing == EnumFacing.WEST) {
               rotation += 8;
            } else if (facing == EnumFacing.SOUTH) {
               rotation += 4;
            } else if (facing == EnumFacing.NORTH) {
               rotation += 12;
            }

            if (rotation > 15) {
               rotation -= 16;
            }

            replaced = replaced.func_177226_a(BlockStandingSign.field_176413_a, rotation);
         } else if (replaced.func_177230_c() == Blocks.field_150395_bd) {
            boolean east = false;
            boolean west = false;
            boolean south = false;
            boolean north = false;
            if (facing == EnumFacing.WEST) {
               east = (Boolean)replaced.func_177229_b(BlockVine.field_176280_O);
               west = (Boolean)replaced.func_177229_b(BlockVine.field_176278_M);
               north = (Boolean)replaced.func_177229_b(BlockVine.field_176279_N);
               south = (Boolean)replaced.func_177229_b(BlockVine.field_176273_b);
            } else if (facing == EnumFacing.SOUTH) {
               east = (Boolean)replaced.func_177229_b(BlockVine.field_176273_b);
               west = (Boolean)replaced.func_177229_b(BlockVine.field_176279_N);
               north = (Boolean)replaced.func_177229_b(BlockVine.field_176280_O);
               south = (Boolean)replaced.func_177229_b(BlockVine.field_176278_M);
            } else if (facing == EnumFacing.NORTH) {
               east = (Boolean)replaced.func_177229_b(BlockVine.field_176279_N);
               west = (Boolean)replaced.func_177229_b(BlockVine.field_176273_b);
               north = (Boolean)replaced.func_177229_b(BlockVine.field_176278_M);
               south = (Boolean)replaced.func_177229_b(BlockVine.field_176280_O);
            }

            replaced = replaced.func_177226_a(BlockVine.field_176278_M, east).func_177226_a(BlockVine.field_176280_O, west).func_177226_a(BlockVine.field_176279_N, south).func_177226_a(BlockVine.field_176273_b, north);
         } else {
            UnmodifiableIterator var12 = properties.entrySet().iterator();

            while(var12.hasNext()) {
               Map.Entry property = (Map.Entry)var12.next();
               if (property.getValue() instanceof EnumFacing) {
                  EnumFacing orientation = (EnumFacing)replaced.func_177229_b((IProperty)property.getKey());
                  if (orientation != EnumFacing.UP && orientation != EnumFacing.DOWN) {
                     if (facing == EnumFacing.WEST) {
                        replaced = replaced.func_177226_a((IProperty)property.getKey(), orientation.func_176746_e().func_176746_e());
                     } else if (facing == EnumFacing.NORTH) {
                        replaced = replaced.func_177226_a((IProperty)property.getKey(), orientation.func_176735_f());
                     } else if (facing == EnumFacing.SOUTH) {
                        replaced = replaced.func_177226_a((IProperty)property.getKey(), orientation.func_176746_e());
                     }
                  }
               }
            }
         }
      }

      return replaced;
   }
}
