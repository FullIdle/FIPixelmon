package com.pixelmonmod.pixelmon.blocks.apricornTrees;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.config.PixelmonItemsApricorns;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.enums.EnumApricornTrees;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockApricornTree extends Block implements IGrowable {
   public static final PropertyEnum BLOCKPOS = PropertyEnum.func_177709_a("blockpos", EnumBlockPos.class);
   private static final AxisAlignedBB AABBBase = new AxisAlignedBB(0.05999999865889549, 0.0, 0.05999999865889549, 0.9399999976158142, 1.649999976158142, 0.9399999976158142);
   private static final AxisAlignedBB AABBBaseStage0 = new AxisAlignedBB(0.4000000059604645, 0.0, 0.4000000059604645, 0.6000000238418579, 0.20000000298023224, 0.6000000238418579);
   private static final AxisAlignedBB AABBBaseStage1 = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.699999988079071, 0.75);
   private static final AxisAlignedBB AABBBaseStage2 = new AxisAlignedBB(0.17000000178813934, 0.0, 0.17000000178813934, 0.8299999833106995, 1.0, 0.8299999833106995);
   private static final AxisAlignedBB AABBTop = new AxisAlignedBB(0.05999999865889549, -1.0, 0.05999999865889549, 0.9399999976158142, 0.10000000149011612, 0.9399999976158142);
   private static final AxisAlignedBB AABBTopStage2 = new AxisAlignedBB(0.05999999865889549, -1.0, 0.05999999865889549, 0.9399999976158142, 0.6499999761581421, 0.9399999976158142);
   public EnumApricornTrees tree;

   public BlockApricornTree(EnumApricornTrees tree) {
      super(Material.field_151575_d);
      this.tree = tree;
      this.func_149675_a(true);
      this.func_149711_c(2.0F);
      this.func_149663_c("apricorn_tree");
      this.setRegistryName("pixelmon:" + tree.name().toLowerCase() + "_apricorn_tree");
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BLOCKPOS});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BLOCKPOS, EnumBlockPos.fromMeta(meta));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumBlockPos)state.func_177229_b(BLOCKPOS)).toMeta();
   }

   public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      return false;
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

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess access, BlockPos pos) {
      return this.getBlockBounds(access, pos, blockState);
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return this.getBlockBounds(worldIn, pos, state).func_186670_a(pos);
   }

   public AxisAlignedBB getBlockBounds(IBlockAccess world, BlockPos pos, IBlockState state) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      BlockPos loc = pos;
      if (blockpos == EnumBlockPos.TOP) {
         loc = pos.func_177977_b();
      }

      if (world.func_180495_p(loc).func_177230_c() != this) {
         return Block.field_185505_j;
      } else {
         TileEntityApricornTree tile = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, world, loc);
         if (tile == null) {
            return Block.field_185505_j;
         } else {
            int stage = tile.getStage();
            if (blockpos == EnumBlockPos.TOP && world.func_180495_p(pos.func_177977_b()).func_177230_c() == this) {
               return stage == 2 ? AABBTopStage2 : AABBTop;
            } else if (stage == 0) {
               return AABBBaseStage0;
            } else if (stage == 1) {
               return AABBBaseStage1;
            } else {
               return stage == 2 ? AABBBaseStage2 : AABBBase;
            }
         }
      }
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonItemsApricorns.getApricorn(this.tree.apricorn), 1);
   }

   public boolean hasTileEntity(IBlockState state) {
      return state.func_177229_b(BLOCKPOS) == EnumBlockPos.BOTTOM;
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityApricornTree(this.tree);
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return !player.func_184812_l_();
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      if (blockpos == EnumBlockPos.TOP && world.func_180495_p(pos.func_177977_b()).func_177230_c() == this) {
         world.func_175698_g(pos.func_177977_b());
      } else if (world.func_180495_p(pos.func_177984_a()).func_177230_c() == this) {
         world.func_175698_g(pos.func_177984_a());
      }

      if (player.func_184812_l_()) {
         return super.removedByPlayer(state, world, pos, player, willHarvest);
      } else {
         if (!world.field_72995_K) {
            EntityItem drops = new EntityItem(world, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, new ItemStack(Blocks.field_150364_r));
            world.func_72838_d(drops);
         }

         return super.removedByPlayer(state, world, pos, player, willHarvest);
      }
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
         BlockPos loc = pos;
         if (blockpos == EnumBlockPos.TOP) {
            loc = pos.func_177977_b();
         }

         TileEntityApricornTree tile = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, world, loc);
         if (tile == null) {
            return false;
         } else {
            int stage = tile.getStage();
            if (stage == 5) {
               Item item = PixelmonItemsApricorns.getApricorn(this.tree.apricorn);
               ItemStack pickedStack = new ItemStack(item);
               ApricornEvent.PickApricorn pickEvent = new ApricornEvent.PickApricorn(this.tree.apricorn, pos, (EntityPlayerMP)player, tile, pickedStack);
               if (Pixelmon.EVENT_BUS.post(pickEvent)) {
                  return false;
               } else {
                  DropItemHelper.giveItemStack((EntityPlayerMP)player, pickEvent.getPickedStack(), false);
                  tile.setStage(3);
                  return true;
               }
            } else {
               return !player.func_184614_ca().func_190926_b() && player.func_184614_ca().func_77973_b() instanceof ItemBlock;
            }
         }
      } else {
         return true;
      }
   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random rand) {
      try {
         super.func_180650_b(world, pos, state, rand);
         BlockPos posUp = pos.func_177984_a();
         EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
         if (blockpos == EnumBlockPos.TOP) {
            return;
         }

         TileEntityApricornTree tile = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, world, pos);
         if (tile == null) {
            return;
         }

         int stage;
         if ((stage = tile.getStage()) >= 5) {
            return;
         }

         if (this.func_176473_a(world, pos, state, false)) {
            float growthChance = 0.25F;
            ApricornEvent.GrowthChance growthEvent = new ApricornEvent.GrowthChance(this.tree.apricorn, pos, tile, growthChance);
            if (!Pixelmon.EVENT_BUS.post(growthEvent) && RandomHelper.getRandomChance(growthEvent.getGrowthChance()) && world.func_175671_l(posUp) >= 9) {
               tile.setStage(stage + 1);
               if (stage == 5) {
                  Pixelmon.EVENT_BUS.post(new ApricornEvent.ApricornReady(this.tree.apricorn, pos, tile));
               }

               if (stage + 1 >= 3) {
                  world.func_180501_a(posUp, state.func_177226_a(BLOCKPOS, EnumBlockPos.TOP), 2);
               }

               ((WorldServer)world).func_184164_w().func_180244_a(pos);
            }
         }
      } catch (Exception var11) {
         Pixelmon.LOGGER.error("Error in Apricorn update tick.");
         var11.printStackTrace();
      }

   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos from) {
      this.checkAndDropBlock(worldIn, pos, state);
   }

   public boolean func_176473_a(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      BlockPos loc = pos;
      if (blockpos == EnumBlockPos.TOP) {
         loc = pos.func_177977_b();
      }

      TileEntityApricornTree tile = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, worldIn, loc);
      if (tile != null) {
         if (tile.getStage() + 1 == 3 && worldIn.func_180495_p(loc.func_177984_a()).func_177230_c() != Blocks.field_150350_a) {
            return false;
         } else {
            return tile.getStage() < 5;
         }
      } else {
         return false;
      }
   }

   public boolean func_180670_a(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return this.func_176473_a(worldIn, pos, state, worldIn.field_72995_K);
   }

   public void func_176474_b(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      if (blockpos == EnumBlockPos.TOP) {
         pos = pos.func_177977_b();
      }

      TileEntityApricornTree tile = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, worldIn, pos);
      if (tile != null) {
         int stage = tile.getStage();
         if (stage < 5) {
            tile.setStage(stage + 1);
            if (stage + 1 >= 3) {
               worldIn.func_180501_a(pos.func_177984_a(), state.func_177226_a(BLOCKPOS, EnumBlockPos.TOP), 2);
            }

            if (!worldIn.field_72995_K) {
               ((WorldServer)worldIn).func_184164_w().func_180244_a(pos);
            }
         }

      }
   }

   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
      BlockPos down = pos.func_177977_b();
      IBlockState soil = worldIn.func_180495_p(down);
      if (soil.func_177230_c() == this) {
         soil = worldIn.func_180495_p(down.func_177977_b());
      }

      Material material = soil.func_185904_a();
      if (material != Material.field_151577_b && material != Material.field_151578_c) {
         return soil.func_177230_c() == Blocks.field_150349_c || soil.func_177230_c() == Blocks.field_150346_d || soil.func_177230_c() == Blocks.field_150458_ak;
      } else {
         return true;
      }
   }

   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.canBlockStay(worldIn, pos, state)) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), 3);
      }

   }

   public EnumPushReaction func_149656_h(IBlockState state) {
      return EnumPushReaction.BLOCK;
   }
}
