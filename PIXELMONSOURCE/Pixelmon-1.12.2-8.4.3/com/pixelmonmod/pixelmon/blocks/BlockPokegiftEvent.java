package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.tileEntities.EnumPokegiftType;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokegift;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokegiftEventType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPokegiftEvent extends BlockPokegift {
   public BlockPokegiftEvent() {
      this.TYPE = EnumPokegiftType.EVENT;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         if (PixelmonConfig.doPokegiftEvents) {
            TileEntityPokegift tile = (TileEntityPokegift)BlockHelper.getTileEntity(TileEntityPokegift.class, world, pos);
            if (tile.canClaim(player.func_110124_au())) {
               if (tile.shouldBreakBlock()) {
                  if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player))) {
                     return true;
                  }

                  world.func_175698_g(pos);
               }

               if (tile.getSpecialPixelmon().isEmpty()) {
                  ChatHandler.sendChat(player, "pixelutilities.blocks.emptygift", new TextComponentTranslation(this.itemName, new Object[0]));
                  world.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187576_at, SoundCategory.BLOCKS, 0.7F, 1.0F);
                  return true;
               }

               ChatHandler.sendChat(player, "pixelmon.blocks.chestfound", new TextComponentTranslation(this.itemName, new Object[0]));
               PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
               Iterator var12 = tile.getSpecialPixelmon().iterator();

               while(var12.hasNext()) {
                  PokemonSpec p = (PokemonSpec)var12.next();
                  Pokemon pokemon = p.create();
                  pokemon.setFriendship(150);
                  Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP)player, ReceiveType.PokeBall, pokemon));
                  storage.add(pokemon);
               }

               tile.addClaimer(player.func_110124_au());
               world.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, PixelSounds.pokelootObtained, SoundCategory.BLOCKS, 0.2F, 1.0F);
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.claimedloot");
            }
         } else {
            ChatHandler.sendChat(player, "pixelutilities.event.noevents");
         }

         return true;
      } else {
         return true;
      }
   }

   public IBlockState func_180642_a(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player) {
      PokeGiftHandler handler = new PokeGiftHandler();
      if (handler.checkTime() == EnumPokegiftEventType.Christmas) {
         this.itemName = "pixelmon.blocks.christmaspokegift";
      } else if (handler.checkTime() == EnumPokegiftEventType.Halloween) {
         this.itemName = "pixelmon.blocks.halloweenpokegift";
      } else if (handler.checkTime() == EnumPokegiftEventType.Custom) {
         this.itemName = "pixelmon.blocks.custompokegift";
      }

      return super.func_180642_a(world, pos, facing, hitX, hitY, hitZ, meta, player);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if (!world.field_72995_K) {
         this.func_176208_a(world, pos, state, player);
         return world.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), world.field_72995_K ? 11 : 3);
      } else {
         return false;
      }
   }

   public TileEntity func_149915_a(World world, int var1) {
      try {
         TileEntityPokegift tileP = new TileEntityPokegift();
         tileP.setChestOneTime(false);
         tileP.setOwner((UUID)null);
         if (!world.field_72995_K) {
            tileP.setAllSpecialPixelmon(this.generatePixelmon(world));
         }

         return tileP;
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   private ArrayList generatePixelmon(World world) {
      ArrayList pixelmon = new ArrayList();
      Random rng = world.field_73012_v;
      int max = PixelmonConfig.pokegiftEventMaxPokes;
      int numberToCreate = rng.nextInt(max);
      if (numberToCreate == 0 || numberToCreate > 6 || numberToCreate > max) {
         numberToCreate = 1;
      }

      for(int i = 0; i < numberToCreate; ++i) {
         PokemonSpec spec = new PokemonSpec(new String[0]);
         spec.name = EnumSpecies.randomPoke(PixelmonConfig.pokegiftEventLegendaries).name;
         spec.level = 5;
         spec.growth = EnumGrowth.getRandomGrowth().getForm();
         spec.nature = (byte)EnumNature.getRandomNature().index;
         spec.ball = (byte)EnumPokeballs.CherishBall.getIndex();
         if (PixelmonConfig.pokegiftEventShinies && rng.nextInt(PixelmonConfig.pokegiftEventShinyRate) == 0) {
            spec.shiny = true;
         }

         pixelmon.add(spec);
      }

      return pixelmon;
   }
}
