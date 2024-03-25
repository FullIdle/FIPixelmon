package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.trading.NPCTrades;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.EnumPokegiftType;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokegift;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Objects;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPokegift extends BlockContainer implements IBlockHasOwner {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.28999999165534973, 0.0, 0.28999999165534973, 0.7200000286102295, 0.4399999976158142, 0.7200000286102295);
   protected String itemName = "item.pokeGift.name";
   protected double xVel = 0.1;
   protected double yVel = 0.2;
   protected double zVel = 0.1;
   protected EnumPokegiftType TYPE;

   public BlockPokegift() {
      super(Material.field_151592_s);
      this.TYPE = EnumPokegiftType.GIFT;
      this.func_149711_c(0.5F);
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

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!worldIn.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, worldIn, pos);
         if (tile != null) {
            Pokemon pokemon;
            if (!Objects.equals(playerIn.func_110124_au(), tile.getOwner())) {
               if (tile.canClaim(playerIn.func_110124_au())) {
                  if (tile.shouldBreakBlock()) {
                     if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(worldIn, pos, state, playerIn))) {
                        return true;
                     }

                     worldIn.func_175698_g(pos);
                     if (worldIn.func_180495_p(pos).func_177230_c() == this) {
                        return true;
                     }
                  }

                  if (tile.getPokemon() == null) {
                     ChatHandler.sendChat(playerIn, "pixelutilities.blocks.emptygift", new TextComponentTranslation(this.itemName, new Object[0]));
                     worldIn.func_184148_a((EntityPlayer)null, playerIn.field_70165_t, playerIn.field_70163_u, playerIn.field_70161_v, SoundEvents.field_187576_at, SoundCategory.BLOCKS, 0.8F, 1.0F);
                     return true;
                  }

                  if (NPCTrades.UNTRADEABLE.matches(tile.getPokemon())) {
                     ChatHandler.sendChat(playerIn, "pixelutilities.blocks.untradablegift");
                     return true;
                  }

                  ChatHandler.sendChat(playerIn, "pixelmon.blocks.chestfound", new TextComponentTranslation(this.itemName, new Object[0]));
                  PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)playerIn);
                  Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP)playerIn, ReceiveType.PokeBall, tile.getPokemon()));
                  pokemon = tile.getPokemon();
                  pokemon.setUUID(UUID.randomUUID());
                  party.add(pokemon);
                  tile.addClaimer(playerIn.func_110124_au());
                  worldIn.func_184148_a((EntityPlayer)null, playerIn.field_70165_t, playerIn.field_70163_u, playerIn.field_70161_v, PixelSounds.pokelootObtained, SoundCategory.BLOCKS, 0.2F, 1.0F);
               } else {
                  ChatHandler.sendChat(playerIn, "pixelmon.blocks.claimedloot");
               }
            } else {
               if (playerIn.func_70093_af()) {
                  if (tile.getPokemon() != null && playerIn.func_184812_l_()) {
                     tile.setOwner((UUID)null);
                     ChatHandler.sendChat(playerIn, "pixelmon.blocks.ownerchanged");
                     return true;
                  }

                  if (playerIn.func_184812_l_()) {
                     ChatHandler.sendChat(playerIn, "pixelmon.blocks.fillmefirst");
                  }

                  return false;
               }

               EntityPlayerMP playerMP = (EntityPlayerMP)playerIn;
               pokemon = tile.getPokemon();
               if (pokemon != null) {
                  if (playerIn.func_184812_l_()) {
                     String mode = "";
                     if (tile.getChestMode() && tile.getDropMode()) {
                        tile.setChestOneTime(false);
                        tile.setDropOneTime(true);
                        mode = "pixelmon.blocks.chestmodePL1D";
                     } else {
                        tile.setDropOneTime(true);
                        tile.setChestOneTime(true);
                        mode = "pixelmon.blocks.chestmodeFCFS";
                     }

                     ChatHandler.sendChat(playerIn, "pixelmon.blocks.chestmode", new TextComponentTranslation(mode, new Object[0]));
                  } else {
                     ChatHandler.sendChat(playerIn, "pixelmon.blocks.alreadyfilled");
                  }
               } else {
                  PlayerPartyStorage party = Pixelmon.storageManager.getParty(playerMP);
                  if (party.countPokemon() <= 1) {
                     ChatHandler.sendChat(playerMP, "pixelmon.blocks.lastpoke");
                     return false;
                  }

                  Pokemon firstPokeinWorld = null;

                  int position;
                  for(position = 0; position < 6; ++position) {
                     Pokemon partypoke = party.get(position);
                     if (partypoke != null && partypoke.getPixelmonIfExists() != null) {
                        firstPokeinWorld = partypoke;
                        break;
                     }
                  }

                  if (firstPokeinWorld == null) {
                     ChatHandler.sendChat(playerIn, "pixelmon.blocks.nothingtoadd");
                     return false;
                  }

                  firstPokeinWorld.ifEntityExists(EntityPixelmon::retrieve);
                  position = party.getPosition(firstPokeinWorld).order;
                  party.set(position, (Pokemon)null);
                  firstPokeinWorld.setStorage((PokemonStorage)null, (StoragePosition)null);
                  tile.setPokemon(firstPokeinWorld);
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public IBlockState func_180642_a(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player) {
      int i = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5) & 3;
      EnumFacing enumfacing = EnumFacing.func_176731_b(i);
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, enumfacing);
   }

   public float func_180647_a(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
      TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, worldIn, pos);
      return tile.getOwner() != null && !Objects.equals(tile.getOwner(), player.func_110124_au()) ? -1.0F : super.func_180647_a(state, player, worldIn, pos);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if (!world.field_72995_K) {
         TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, world, pos);
         Pokemon pokemon = tile.getPokemon();
         if (pokemon != null) {
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(tile.getOwner());
            pokemon.setUUID(UUID.randomUUID());
            storage.add(pokemon);
         } else {
            DropItemHelper.giveItemStack((EntityPlayerMP)player, new ItemStack(PixelmonBlocks.pokegiftBlock), false);
         }

         return super.removedByPlayer(state, world, pos, player, willHarvest);
      } else {
         return false;
      }
   }

   public TileEntity func_149915_a(World par1World, int var1) {
      return new TileEntityPokegift();
   }

   public void func_180655_c(IBlockState stateIn, World world, BlockPos pos, Random random) {
      TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, world, pos);
      if (tile.getVisibility() == EnumPokechestVisibility.Hidden) {
         float rand = random.nextFloat() * 0.5F + 1.0F;
         world.func_175688_a(EnumParticleTypes.SPELL_INSTANT, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, this.xVel * (double)rand, this.yVel * (double)rand, this.zVel * (double)rand, new int[0]);
      }

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
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, playerIn.field_70170_p, pos);
      tile.setOwner(playerID);
      if (PixelmonConfig.pokegiftMany) {
         tile.setChestOneTime(false);
      }

   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }
}
