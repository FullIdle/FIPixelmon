package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.legendary.IlexShrineEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.enums.EnumUsed;
import com.pixelmonmod.pixelmon.blocks.machines.BlockIlexShrine;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.EncounterData;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityIlexShrine extends TileEntity {
   public EncounterData encounters;

   public TileEntityIlexShrine() {
      this.encounters = new EncounterData(PixelmonConfig.shrineEncounterMode);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74782_a("EncounterData", this.encounters.writeToNBT(new NBTTagCompound()));
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.encounters = new EncounterData(PixelmonConfig.shrineEncounterMode);
      if (nbt.func_74764_b("EncounterData")) {
         this.encounters.readFromNBT(nbt.func_74775_l("EncounterData"));
      }

      if (this.getTileData().func_74764_b("encounters")) {
         NBTTagList data = this.getTileData().func_150295_c("encounters", 8);
         Iterator var3 = data.iterator();

         while(var3.hasNext()) {
            NBTBase base = (NBTBase)var3.next();
            NBTTagString str = (NBTTagString)base;
            UUID uuid = UUID.fromString(str.func_150285_a_());
            this.encounters.addEncounter(uuid, 1L);
         }

         this.getTileData().func_82580_o("encounters");
      }

   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
      return false;
   }

   public void activate(EntityPlayer player, BlockIlexShrine block, IBlockState state, ItemStack item) {
      if (item != null && item.func_77973_b() == PixelmonItemsPokeballs.gsBall && !player.field_70170_p.field_72995_K) {
         EnumSpecies species = EnumSpecies.Celebi;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
         EntityPixelmon startingPixelmon = party.getAndSendOutFirstAblePokemon(player);
         if (startingPixelmon != null && BattleRegistry.getBattle(player) == null) {
            String spec = species.name;
            EntityPixelmon pixelmonEntity = PokemonSpec.from(spec).create(player.field_70170_p);
            boolean shiny = PixelmonConfig.getShinyRate(this.field_145850_b.field_73011_w.getDimension()) > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(this.field_145850_b.field_73011_w.getDimension()));
            pixelmonEntity.getPokemonData().setShiny(shiny);
            pixelmonEntity.func_70107_b((double)this.field_174879_c.func_177958_n(), (double)(this.field_174879_c.func_177956_o() + 2), (double)this.field_174879_c.func_177952_p());
            if (Pixelmon.EVENT_BUS.post(new IlexShrineEvent((EntityPlayerMP)player, block, pixelmonEntity, this, this.func_174877_v()))) {
               return;
            }

            if (this.encounters.canEncounter(player)) {
               if (this.encounters.getMode() == EnumEncounterMode.Once) {
                  player.field_70170_p.func_180501_a(this.field_174879_c, state.func_177226_a(BlockIlexShrine.USED, EnumUsed.YES), 2);
                  player.field_70170_p.func_180501_a(this.field_174879_c.func_177984_a(), state.func_177226_a(BlockIlexShrine.USED, EnumUsed.YES).func_177226_a(BlockIlexShrine.BLOCKPOS, EnumBlockPos.TOP), 2);
               }

               this.encounters.registerEncounter(player);
               player.field_70170_p.func_72838_d(pixelmonEntity);
               item.func_190918_g(1);
               PlayerParticipant playerParticipant = new PlayerParticipant((EntityPlayerMP)player, new EntityPixelmon[]{startingPixelmon});
               WildPixelmonParticipant wildPixelmonParticipant = new WildPixelmonParticipant(false, new EntityPixelmon[]{pixelmonEntity});
               wildPixelmonParticipant.startedBattle = true;
               BattleRegistry.startBattle(playerParticipant, wildPixelmonParticipant);
            } else if (this.encounters.getMode().isTimedAccess()) {
               ChatHandler.sendChat(player, "pixelmon.blocks.shrine.today");
            } else {
               ChatHandler.sendChat(player, "pixelmon.blocks.shrine.encountered");
            }
         } else {
            ChatHandler.sendChat(player, "pixelmon.blocks.partyfainted");
         }
      }

   }
}
