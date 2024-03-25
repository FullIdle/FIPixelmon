package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PokeLootClaimedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeLootEvent;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPokeChest extends BlockContainer implements IBlockHasOwner {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.28999999165534973, 0.0, 0.28999999165534973, 0.7200000286102295, 0.4399999976158142, 0.7200000286102295);
   private Class pokeChestTileEntityClass;
   protected EnumPokeChestType TYPE;
   ItemStack[] drops;

   public BlockPokeChest(Class tileEntityClass) {
      super(Material.field_151592_s);
      this.TYPE = EnumPokeChestType.POKEBALL;
      this.drops = null;
      this.func_149752_b(6000000.0F);
      this.pokeChestTileEntityClass = tileEntityClass;
      this.func_149663_c("pokechest");
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
   }

   protected ItemStack ChestDrop() {
      if (this.TYPE == EnumPokeChestType.POKEBALL) {
         return DropItemRegistry.getTier1Drop();
      } else if (this.TYPE == EnumPokeChestType.ULTRABALL) {
         return DropItemRegistry.getTier2Drop();
      } else if (this.TYPE == EnumPokeChestType.MASTERBALL) {
         return DropItemRegistry.getTier3Drop();
      } else {
         return this.TYPE == EnumPokeChestType.BEASTBALL ? DropItemRegistry.getUltraSpaceDrop() : null;
      }
   }

   private ItemStack HiddenDrop() {
      int num = RandomHelper.rand.nextInt(100);
      if (num <= 50) {
         return DropItemRegistry.getTier1Drop();
      } else {
         return num < 85 ? DropItemRegistry.getTier2Drop() : DropItemRegistry.getTier3Drop();
      }
   }

   private ItemStack[] getRandomDrops(EnumPokechestVisibility visibility) {
      return visibility == EnumPokechestVisibility.Visible ? new ItemStack[]{this.ChestDrop()} : new ItemStack[]{this.HiddenDrop()};
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntity tileEntity = world.func_175625_s(pos);
         if (!(tileEntity instanceof TileEntityPokeChest)) {
            return true;
         } else {
            TileEntityPokeChest tile = (TileEntityPokeChest)BlockHelper.getTileEntity(TileEntityPokeChest.class, world, pos);
            UUID blockOwner = tile.getOwner();
            UUID playerID = player.func_110124_au();
            if (!playerID.equals(blockOwner)) {
               EnumPokechestVisibility visibility = tile.getVisibility();
               if (tile.canClaim(playerID)) {
                  EntityPlayerMP playerMP = (EntityPlayerMP)player;
                  if (Pixelmon.EVENT_BUS.post(new PokeLootEvent.Claim(playerMP, tile))) {
                     return true;
                  }

                  if (Pixelmon.EVENT_BUS.post(new PokeLootClaimedEvent(playerMP, tile))) {
                     return true;
                  }

                  if (tile.shouldBreakBlock()) {
                     if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player))) {
                        return true;
                     }

                     world.func_175698_g(pos);
                  }

                  if (tile.isCustomDrop()) {
                     this.drops = tile.getCustomDrops();
                  } else {
                     this.drops = this.getRandomDrops(visibility);
                  }

                  PokeLootEvent.GetDrops getDropsEvent = new PokeLootEvent.GetDrops(playerMP, tile, this.drops);
                  Pixelmon.EVENT_BUS.post(getDropsEvent);
                  this.drops = getDropsEvent.getDrops();
                  ItemStack firstDrop = ItemStack.field_190927_a;
                  ItemStack[] var18 = this.drops;
                  int var19 = var18.length;

                  for(int var20 = 0; var20 < var19; ++var20) {
                     ItemStack drop = var18[var20];
                     if (drop != null) {
                        ItemStack newDrop = drop.func_77946_l();
                        PokeLootEvent.Drop dropEvent = new PokeLootEvent.Drop(playerMP, tile, newDrop);
                        if (!Pixelmon.EVENT_BUS.post(dropEvent)) {
                           if (firstDrop.func_190926_b()) {
                              firstDrop = dropEvent.getDrop();
                           }

                           DropItemHelper.giveItemStack(playerMP, dropEvent.getDrop(), false);
                        }
                     }
                  }

                  String itemName = this.drops.length != 0 && !firstDrop.func_190926_b() ? firstDrop.func_77973_b().func_77667_c(firstDrop) + ".name" : "tile.poke_chest.name";
                  ChatHandler.sendChat(player, "pixelmon.blocks.chestfound", new TextComponentTranslation(itemName, new Object[0]));
                  tile.removeClaimer(playerID);
                  tile.addClaimer(playerID);
                  world.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, PixelSounds.pokelootObtained, SoundCategory.BLOCKS, 0.2F, 1.0F);
               } else if (tile.isTimeEnabled()) {
                  ChatHandler.sendChat(player, "pixelmon.blocks.timedclaim");
               } else {
                  ChatHandler.sendChat(player, "pixelmon.blocks.claimedloot");
               }
            } else {
               boolean shiftClick = player.func_70093_af();
               if (shiftClick) {
                  tile.setOwner((UUID)null);
                  ChatHandler.sendChat(player, "pixelmon.blocks.ownerchanged");
               } else {
                  ItemStack itemStack = player.func_184614_ca();
                  if (itemStack.func_190926_b()) {
                     EnumPokechestVisibility visibility = tile.getVisibility();
                     String metaMode = "";
                     if (visibility == EnumPokechestVisibility.Hidden) {
                        tile.setVisibility(EnumPokechestVisibility.Visible);
                        metaMode = "Normal";
                     } else {
                        tile.setVisibility(EnumPokechestVisibility.Hidden);
                        metaMode = "Hidden";
                     }

                     ChatHandler.sendChat(player, "pixelmon.blocks.visible", metaMode);
                     return false;
                  }

                  tile.setCustomDrops(itemStack);
                  tile.setOwner((UUID)null);
                  ChatHandler.sendChat(player, "pixelmon.blocks.chestset", new TextComponentTranslation(itemStack.func_77977_a() + ".name", new Object[0]));
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      int i = MathHelper.func_76128_c((double)(placer.field_70177_z * 4.0F / 360.0F) + 0.5) & 3;
      EnumFacing enumfacing = EnumFacing.func_176731_b(i);
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, enumfacing);
   }

   public void func_180649_a(World world, BlockPos pos, EntityPlayer player) {
      if (!world.field_72995_K) {
         UUID playerID = player.func_110124_au();
         TileEntityPokeChest tile = (TileEntityPokeChest)BlockHelper.getTileEntity(TileEntityPokeChest.class, world, pos);
         if (playerID.equals(tile.getOwner())) {
            String mode = "pixelmon.blocks.chestmode";
            boolean chestMode = tile.getChestMode();
            boolean dropMode = tile.getDropMode();
            boolean timeEnabled = tile.isTimeEnabled();
            if (!chestMode && !dropMode && !timeEnabled) {
               tile.setChestOneTime(false);
               tile.setDropOneTime(true);
               tile.setTimeEnabled(false);
               mode = mode + "PL1D";
            } else if (!chestMode && dropMode && !timeEnabled) {
               tile.setDropOneTime(true);
               tile.setChestOneTime(false);
               tile.setTimeEnabled(true);
               mode = mode + "TD";
            } else if (timeEnabled) {
               tile.setTimeEnabled(false);
               tile.setDropOneTime(true);
               tile.setChestOneTime(true);
               mode = mode + "FCFS";
            } else if (chestMode && dropMode && !timeEnabled) {
               tile.setChestOneTime(false);
               tile.setDropOneTime(false);
               tile.setTimeEnabled(false);
               mode = mode + "PUD";
            }

            ChatHandler.sendChat(player, "pixelmon.blocks.chestmode", new TextComponentTranslation(mode, new Object[0]));
         }
      }

   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      if (this.TYPE == EnumPokeChestType.POKEBALL) {
         return new ItemStack(PixelmonBlocks.pokeChest);
      } else if (this.TYPE == EnumPokeChestType.MASTERBALL) {
         return new ItemStack(PixelmonBlocks.masterChest);
      } else if (this.TYPE == EnumPokeChestType.ULTRABALL) {
         return new ItemStack(PixelmonBlocks.ultraChest);
      } else {
         return this.TYPE == EnumPokeChestType.BEASTBALL ? new ItemStack(PixelmonBlocks.beastChest) : null;
      }
   }

   public TileEntity func_149915_a(World par1World, int var1) {
      try {
         return (TileEntity)this.pokeChestTileEntityClass.newInstance();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public void func_180655_c(IBlockState stateIn, World world, BlockPos pos, Random random) {
      TileEntityPokeChest tile = (TileEntityPokeChest)BlockHelper.getTileEntity(TileEntityPokeChest.class, world, pos);
      if (tile.getVisibility() == EnumPokechestVisibility.Hidden) {
         float rand = random.nextFloat() * 0.5F + 1.0F;
         double xVel = 0.1;
         double yVel = 0.2;
         double zVel = 0.1;
         world.func_175688_a(EnumParticleTypes.SPELL_INSTANT, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, xVel * (double)rand, yVel * (double)rand, zVel * (double)rand, new int[0]);
      }

   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return face == EnumFacing.DOWN ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED;
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess access, BlockPos pos) {
      return AABB;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityPokeChest tile = (TileEntityPokeChest)BlockHelper.getTileEntity(TileEntityPokeChest.class, playerIn.field_70170_p, pos);
      tile.setOwner(playerID);
   }
}
