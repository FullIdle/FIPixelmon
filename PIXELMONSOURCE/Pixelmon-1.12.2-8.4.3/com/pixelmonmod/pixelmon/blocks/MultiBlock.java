package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.client.render.ParticleBlocks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MultiBlock extends BlockContainer {
   public static final PropertyEnum MULTIPOS = PropertyEnum.func_177709_a("multipos", EnumMultiPos.class);
   public int width;
   public int length;
   public double height;
   private static HashMap boundingBoxes = new HashMap();

   protected MultiBlock(Material material, int width, double height, int length) {
      super(material);
      this.width = width;
      this.height = height;
      this.length = length;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, MULTIPOS});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3)).func_177226_a(MULTIPOS, EnumMultiPos.fromMeta((meta & 15) >> 2));
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
      i |= ((EnumMultiPos)state.func_177229_b(MULTIPOS)).toMeta() << 2;
      return i;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return this.getMultiBlockBoundingBox(source, pos, (EnumMultiPos)state.func_177229_b(MULTIPOS), (EnumFacing)state.func_177229_b(BlockProperties.FACING));
   }

   protected AxisAlignedBB getMultiBlockBoundingBox(IBlockAccess worldIn, BlockPos pos, EnumMultiPos multiPos, EnumFacing facing) {
      if (!boundingBoxes.containsKey(this)) {
         BoundingBoxSet set = new BoundingBoxSet(this.width, this.height, this.length);
         boundingBoxes.put(this, set);
      }

      if (worldIn.func_180495_p(pos).func_177230_c() != this) {
         return new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, this.height, (double)this.length);
      } else if (multiPos == EnumMultiPos.BASE) {
         if (facing == EnumFacing.SOUTH) {
            return ((BoundingBoxSet)boundingBoxes.get(this)).AABBBaseSouth;
         } else if (facing == EnumFacing.NORTH) {
            return ((BoundingBoxSet)boundingBoxes.get(this)).AABBBaseNorth;
         } else {
            return facing == EnumFacing.EAST ? ((BoundingBoxSet)boundingBoxes.get(this)).AABBBaseEast : ((BoundingBoxSet)boundingBoxes.get(this)).AABBBaseWest;
         }
      } else {
         BlockPos base = this.findBaseBlock(worldIn, new BlockPos.MutableBlockPos(pos), worldIn.func_180495_p(pos));

         try {
            return this.getMultiBlockBoundingBox(worldIn, base, EnumMultiPos.BASE, (EnumFacing)worldIn.func_180495_p(base).func_177229_b(BlockProperties.FACING)).func_72317_d((double)(base.func_177958_n() - pos.func_177958_n()), (double)(base.func_177956_o() - pos.func_177956_o()), (double)(base.func_177952_p() - pos.func_177952_p()));
         } catch (IllegalArgumentException var7) {
            return new AxisAlignedBB(0.0, 0.0, 0.0, (double)this.width, this.height, (double)this.length);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer) {
      byte nb = 4;
      BlockPos base = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), world.func_180495_p(pos));

      for(int i = 0; i < nb; ++i) {
         for(int j = 0; j < nb; ++j) {
            for(int k = 0; k < nb; ++k) {
               double fxX = (double)pos.func_177958_n() + ((double)i + 0.5) / (double)nb;
               double fxY = (double)pos.func_177956_o() + ((double)j + 0.5) / (double)nb;
               double fxZ = (double)pos.func_177952_p() + ((double)k + 0.5) / (double)nb;

               try {
                  ParticleBlocks fx = new ParticleBlocks(world, fxX, fxY, fxZ, 0.0, 0.0, 0.0, world.func_180495_p(base));
                  effectRenderer.func_78873_a(fx);
               } catch (Exception var17) {
                  var17.printStackTrace();
               }
            }
         }
      }

      return true;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List ret = new ArrayList();
      if (world instanceof World) {
         Item item = this.getDroppedItem((World)world, pos);
         if (item != null) {
            ret.add(new ItemStack(item, 1, this.func_180651_a(state)));
         }
      }

      return ret;
   }

   public abstract Item getDroppedItem(World var1, BlockPos var2);

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return state.func_177229_b(MULTIPOS) == EnumMultiPos.BASE ? (TileEntity)this.getTileEntity(world, state).orElse((Object)null) : null;
   }

   protected abstract Optional getTileEntity(World var1, IBlockState var2);

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      EnumMultiPos multiPos = (EnumMultiPos)state.func_177229_b(MULTIPOS);
      BlockPos location = this.findBaseBlock(world, new BlockPos.MutableBlockPos(pos), state);
      if (location.func_177986_g() != pos.func_177986_g()) {
         state = world.func_180495_p(location);

         try {
            multiPos = (EnumMultiPos)state.func_177229_b(MULTIPOS);
         } catch (Exception var9) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
         }
      }

      if (multiPos == EnumMultiPos.BASE && !player.field_71075_bZ.field_75098_d) {
         this.func_176226_b(world, pos, state, 0);
      }

      this.setMultiBlocksWidth(location, world, state);
      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public BlockPos findBaseBlock(IBlockAccess world, BlockPos.MutableBlockPos pos, IBlockState state) {
      try {
         if (state.func_177230_c() != this) {
            return pos;
         } else {
            EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
            EnumMultiPos multipos = (EnumMultiPos)state.func_177229_b(MULTIPOS);
            if (multipos == EnumMultiPos.TOP && world.func_180495_p(pos.func_177972_a(EnumFacing.DOWN)).func_177230_c() == this) {
               pos.func_189536_c(EnumFacing.DOWN);
            } else {
               if (multipos != EnumMultiPos.BOTTOM || world.func_180495_p(pos.func_177972_a(facing.func_176734_d())).func_177230_c() != this) {
                  return new BlockPos(pos);
               }

               pos.func_189534_c(facing, -1);
            }

            return this.findBaseBlock(world, pos, world.func_180495_p(pos));
         }
      } catch (Exception var6) {
         return pos;
      }
   }

   private void setMultiBlocksWidth(BlockPos pos, World world, IBlockState state) {
      EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
      int l;
      if (facing == EnumFacing.EAST) {
         for(l = 0; l < this.width; ++l) {
            this.setMultiBlocksLength(pos, 0, l, state, world);
         }
      } else if (facing == EnumFacing.NORTH) {
         for(l = 0; l < this.width; ++l) {
            this.setMultiBlocksLength(pos, l, 0, state, world);
         }
      } else if (facing == EnumFacing.WEST) {
         for(l = 0; l < this.width; ++l) {
            this.setMultiBlocksLength(pos, 0, -1 * l, state, world);
         }
      } else {
         for(l = 0; l < this.width; ++l) {
            this.setMultiBlocksLength(pos, -1 * l, 0, state, world);
         }
      }

   }

   private void setMultiBlocksLength(BlockPos pos, int xd, int zd, IBlockState state, World world) {
      EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
      int w;
      if (facing == EnumFacing.EAST) {
         for(w = 0; w < this.length; ++w) {
            this.setMultiBlocksHeight(pos, xd + w, zd, state, world);
         }
      } else if (facing == EnumFacing.NORTH) {
         for(w = 0; w < this.length; ++w) {
            this.setMultiBlocksHeight(pos, xd, zd - w, state, world);
         }
      } else if (facing == EnumFacing.WEST) {
         for(w = 0; w < this.length; ++w) {
            this.setMultiBlocksHeight(pos, xd - w, zd, state, world);
         }
      } else {
         for(w = 0; w < this.length; ++w) {
            this.setMultiBlocksHeight(pos, xd, zd + w, state, world);
         }
      }

   }

   private void setMultiBlocksHeight(BlockPos pos, int xd, int zd, IBlockState state, World world) {
      for(int h = 0; (double)h < this.height; ++h) {
         BlockPos p = new BlockPos(pos.func_177958_n() + xd, pos.func_177956_o() + h, pos.func_177952_p() + zd);
         world.func_175698_g(p);
      }

   }
}
