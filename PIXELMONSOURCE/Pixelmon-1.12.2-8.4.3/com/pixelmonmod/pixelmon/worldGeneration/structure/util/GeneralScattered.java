package com.pixelmonmod.pixelmon.worldGeneration.structure.util;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import com.pixelmonmod.pixelmon.util.PixelBlockSnapshot;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class GeneralScattered extends StructureScattered {
   protected StructureInfo si;
   private boolean forceGeneration;
   private boolean recreateTechnicalBlocks;
   protected StructureSnapshot snapshot;

   public GeneralScattered(Random par1Random, BlockPos pos, StructureSnapshot snapshot, StructureInfo structureData, boolean doRotation, boolean forceGeneration, boolean recreateTechnicalBlocks) {
      this(par1Random, pos, snapshot, structureData, doRotation, forceGeneration, (EnumFacing)null, recreateTechnicalBlocks);
   }

   public GeneralScattered(Random par1Random, BlockPos pos, StructureSnapshot snapshot, StructureInfo structureData, boolean doRotation, boolean forceGeneration, EnumFacing facing, boolean recreateTechnicalBlocks) {
      super(par1Random, pos, snapshot.getWidth(), snapshot.getHeight(), snapshot.getLength(), doRotation);
      this.snapshot = snapshot;
      this.forceGeneration = forceGeneration;
      this.recreateTechnicalBlocks = recreateTechnicalBlocks;
      this.si = structureData;
      if (facing == null) {
         this.func_186164_a(EnumFacing.func_176741_a(par1Random));

         while(this.func_186165_e().func_176740_k() == Axis.Y) {
            this.func_186164_a(EnumFacing.func_176741_a(par1Random));
         }
      } else {
         this.func_186164_a(facing);
      }

      int width = snapshot.getWidth();
      int height = snapshot.getHeight();
      int length = snapshot.getLength();
      switch (this.func_186165_e()) {
         case EAST:
         case WEST:
            this.field_74887_e = new StructureBoundingBox(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), pos.func_177958_n() + width - 1, pos.func_177956_o() + height - 1, pos.func_177952_p() + length - 1);
            break;
         default:
            this.field_74887_e = new StructureBoundingBox(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), pos.func_177958_n() + length - 1, pos.func_177956_o() + height - 1, pos.func_177952_p() + width - 1);
      }

   }

   public boolean func_74875_a(World world, Random par2Random, StructureBoundingBox bb) {
      if (!this.forceGeneration && !this.canStructureFitAtCoords(world)) {
         return false;
      } else {
         ArrayList multiblocks = new ArrayList();
         int width = this.snapshot.getWidth();
         int height = this.snapshot.getHeight();
         int length = this.snapshot.getLength();

         int y;
         int x;
         int z;
         PixelBlockSnapshot snapshot;
         BlockPos newPos;
         for(y = 0; y < height; ++y) {
            for(x = 0; x < width; ++x) {
               for(z = 0; z < length; ++z) {
                  snapshot = this.snapshot.getBlockAt(x, y, z);
                  snapshot.world = world;
                  if (snapshot.getReplacedBlock().func_185904_a() != Material.field_151594_q) {
                     newPos = new BlockPos(this.func_74865_a(x, z), this.func_74862_a(y), this.func_74873_b(x, z));
                     snapshot.restoreToLocationWithRotation(newPos, this.func_186165_e(), y, this.recreateTechnicalBlocks, this.signItem);
                     Block newBlock = world.func_180495_p(newPos).func_177230_c();
                     if (newBlock instanceof MultiBlock) {
                        multiblocks.add(newPos);
                     }

                     if (newBlock instanceof BlockChest) {
                        this.si.populateChest(world, newPos);
                     }
                  }
               }
            }
         }

         for(y = 0; y < height; ++y) {
            for(x = 0; x < width; ++x) {
               for(z = 0; z < length; ++z) {
                  snapshot = this.snapshot.getBlockAt(x, y, z);
                  snapshot.world = world;
                  if (snapshot.getReplacedBlock().func_185904_a() == Material.field_151594_q) {
                     newPos = new BlockPos(this.func_74865_a(x, z), this.func_74862_a(y), this.func_74873_b(x, z));
                     snapshot.restoreToLocationWithRotation(newPos, this.func_186165_e(), y, this.recreateTechnicalBlocks);
                     if (world.func_180495_p(newPos).func_177230_c() instanceof MultiBlock) {
                        multiblocks.add(newPos);
                     }
                  } else if (y + 1 < height) {
                     PixelBlockSnapshot nextSnapshot = this.snapshot.getBlockAt(x, y, z);
                     snapshot.world = world;
                     if (snapshot.getReplacedBlock().func_177230_c() instanceof BlockDoublePlant && nextSnapshot.getReplacedBlock().func_177230_c() instanceof BlockDoublePlant) {
                        BlockPos newPos = new BlockPos(this.func_74865_a(x, z), this.func_74862_a(y), this.func_74873_b(x, z));
                        snapshot.restoreToLocationWithRotation(newPos, this.func_186165_e(), y, this.recreateTechnicalBlocks);
                     }
                  }
               }
            }
         }

         Iterator var23 = multiblocks.iterator();

         while(var23.hasNext()) {
            BlockPos pos = (BlockPos)var23.next();
            IBlockState state = world.func_180495_p(pos);
            Block block = state.func_177230_c();
            if (block instanceof MultiBlock) {
               EnumFacing blockRot = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
               EnumMultiPos multiPos = (EnumMultiPos)state.func_177229_b(MultiBlock.MULTIPOS);
               if (multiPos == EnumMultiPos.BASE) {
                  PixelmonItemBlock.setMultiBlocksWidth(pos, blockRot, world, (MultiBlock)block, block, (EntityPlayer)null);
               }
            }
         }

         var23 = this.snapshot.getStatues().iterator();

         while(var23.hasNext()) {
            NBTTagCompound statueTag = (NBTTagCompound)var23.next();
            EntityStatue statue = new EntityStatue(world);
            statue.func_70020_e(statueTag);
            double posX = statue.field_70165_t;
            double posY = statue.field_70163_u;
            double posZ = statue.field_70161_v;
            double x = this.getXWithOffsetDouble(posX, posZ);
            double y = this.getYWithOffsetDouble(posY);
            double z = this.getZWithOffsetDouble(posX, posZ);
            if (this.func_186165_e() != EnumFacing.EAST) {
               if (this.func_186165_e() == EnumFacing.WEST) {
                  statue.setRotation(statue.getRotation() + 180.0F);
               } else if (this.func_186165_e() == EnumFacing.NORTH) {
                  statue.setRotation(statue.getRotation() - 90.0F);
               } else if (this.func_186165_e() == EnumFacing.SOUTH) {
                  statue.setRotation(statue.getRotation() + 90.0F);
               }

               statue.field_70126_B = statue.field_70177_z;
            }

            statue.func_70107_b(x, y, z);
            statue.func_184221_a(UUID.randomUUID());
            world.func_72838_d(statue);
         }

         return true;
      }
   }

   public int getX(int x, int z) {
      return this.func_74865_a(x, z);
   }

   public int getY(int y) {
      return this.func_74862_a(y);
   }

   public int getZ(int x, int z) {
      return this.func_74873_b(x, z);
   }

   protected int func_74865_a(int x, int z) {
      if (this.func_186165_e() == null) {
         return x;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return this.field_74887_e.field_78897_a + x;
            case WEST:
               return this.field_74887_e.field_78893_d - x;
            case NORTH:
               return this.field_74887_e.field_78897_a + z;
            case SOUTH:
               return this.field_74887_e.field_78893_d - z;
            default:
               return x;
         }
      }
   }

   protected int func_74873_b(int x, int z) {
      if (this.func_186165_e() == null) {
         return z;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return this.field_74887_e.field_78896_c + z;
            case WEST:
               return this.field_74887_e.field_78892_f - z;
            case NORTH:
               return this.field_74887_e.field_78892_f - x;
            case SOUTH:
               return this.field_74887_e.field_78896_c + x;
            default:
               return z;
         }
      }
   }

   protected double getXWithOffsetDouble(double x, double z) {
      if (this.func_186165_e() == null) {
         return x;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return (double)this.field_74887_e.field_78897_a + x;
            case WEST:
               return (double)this.field_74887_e.field_78893_d - x + x % 1.0 * 2.0;
            case NORTH:
               return (double)this.field_74887_e.field_78897_a + z;
            case SOUTH:
               return (double)this.field_74887_e.field_78893_d - z + z % 1.0 * 2.0;
            default:
               return x;
         }
      }
   }

   protected double getYWithOffsetDouble(double y) {
      return this.func_186165_e() == null ? y : y + (double)this.field_74887_e.field_78895_b;
   }

   protected double getZWithOffsetDouble(double x, double z) {
      if (this.func_186165_e() == null) {
         return z;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return (double)this.field_74887_e.field_78896_c + z;
            case WEST:
               return (double)this.field_74887_e.field_78892_f - z + z % 1.0 * 2.0;
            case NORTH:
               return (double)this.field_74887_e.field_78892_f - x + x % 1.0 * 2.0;
            case SOUTH:
               return (double)this.field_74887_e.field_78896_c + x;
            default:
               return z;
         }
      }
   }

   public int getNPCXWithOffset(int x, int z) {
      if (this.func_186165_e() == null) {
         return x;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return this.field_74887_e.field_78897_a + z;
            case WEST:
               return this.field_74887_e.field_78893_d - z;
            case NORTH:
               return this.field_74887_e.field_78893_d - x;
            case SOUTH:
               return this.field_74887_e.field_78897_a + x;
            default:
               return x;
         }
      }
   }

   public int getNPCZWithOffset(int x, int z) {
      if (this.func_186165_e() == null) {
         return z;
      } else {
         switch (this.func_186165_e()) {
            case EAST:
               return this.field_74887_e.field_78892_f - x;
            case WEST:
               return this.field_74887_e.field_78896_c + x;
            case NORTH:
               return this.field_74887_e.field_78892_f - z;
            case SOUTH:
               return this.field_74887_e.field_78896_c + z;
            default:
               return z;
         }
      }
   }

   protected int getAverageGroundLevel(World worldIn) {
      int i = 0;
      int j = 0;
      int min = -1;
      int max = -1;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k = this.field_74887_e.field_78896_c; k <= this.field_74887_e.field_78892_f; ++k) {
         for(int l = this.field_74887_e.field_78897_a; l <= this.field_74887_e.field_78893_d; ++l) {
            blockpos$mutableblockpos.func_181079_c(l, 64, k);
            int value = Math.max(worldIn.func_175672_r(blockpos$mutableblockpos).func_177956_o(), worldIn.field_73011_w.func_76557_i());
            if (min == -1 || value < min) {
               min = value;
            }

            if (max == -1 || value > max) {
               max = value;
            }

            i += value;
            ++j;
         }
      }

      if (j != 0 && max - min <= 5) {
         return i / j;
      } else {
         return -1;
      }
   }

   protected boolean canStructureFitAtCoords(World world) {
      int height = this.getAverageGroundLevel(world);
      if (height == -1) {
         return false;
      } else {
         this.field_74887_e.func_78886_a(0, height - this.si.getDepth() - this.field_74887_e.field_78895_b + 1, 0);
         return true;
      }
   }

   public boolean generateImpl(World world, Random random) {
      return this.func_74875_a(world, random, this.field_74887_e);
   }

   public String getName() {
      return "";
   }

   protected void func_143012_a(NBTTagCompound tagCompound) {
   }

   protected void func_143011_b(NBTTagCompound tagCompound, TemplateManager templateManager) {
   }
}
