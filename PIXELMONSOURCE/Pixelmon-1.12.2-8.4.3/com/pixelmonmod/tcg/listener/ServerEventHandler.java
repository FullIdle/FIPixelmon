package com.pixelmonmod.tcg.listener;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.raids.EndRaidEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import com.pixelmonmod.tcg.item.containers.ContainerBinder;
import com.pixelmonmod.tcg.item.containers.ContainerDeck;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ServerEventHandler {
   Random rand = new Random();
   public static final EnumSet RARES;

   @SubscribeEvent
   public void onPlayerSpawnEvent(PlayerEvent.PlayerLoggedInEvent event) {
      EntityPlayerMP player = (EntityPlayerMP)event.player;
   }

   @SubscribeEvent
   public void onRaidWonEvent(EndRaidEvent event) {
      if (TCGConfig.getInstance().essenceForRaidWins && event.didRaidersWin()) {
         ArrayList players = new ArrayList();
         Iterator var3 = event.getRaid().getPlayers().iterator();

         while(var3.hasNext()) {
            RaidData.RaidPlayer p = (RaidData.RaidPlayer)var3.next();
            if (p != null) {
               players.add(p.name);
            }
         }

         EnumType pokemonType = event.getRaid().getSpecies().getBaseStats().getType1();
         EnumType pokemonTypeTwo = event.getRaid().getSpecies().getBaseStats().getType2();
         int raidStars = event.getRaid().getStars();
         if (event.getRaid().getForm() != null) {
            pokemonType = event.getRaid().getSpecies().getBaseStats(event.getRaid().getForm()).getType1();
            pokemonTypeTwo = event.getRaid().getSpecies().getBaseStats(event.getRaid().getForm()).getType2();
         }

         Energy e = Energy.getEnergyFromType(pokemonType);
         if (e != null) {
            Iterator var7 = players.iterator();

            while(var7.hasNext()) {
               String player = (String)var7.next();
               EntityPlayer ep = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_152612_a(player);
               if (ep != null) {
                  Energy.givePlayerEssence(ep, e.getUnlocalizedName(), TCGConfig.getInstance().getRaidEssence(raidStars));
                  if (TCGConfig.getInstance().typeTwoDropsEssence() && pokemonTypeTwo != null && TCGConfig.getInstance().getTypeTwoEssenceDropRate() > 0.0) {
                     EssenceHelper.givePlayerEssenceFromType(ep, pokemonTypeTwo, (int)((double)TCGConfig.getInstance().getRaidEssence(raidStars) * TCGConfig.getInstance().getTypeTwoEssenceDropRate()));
                  }
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onClonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone e) {
      if (e.isWasDeath()) {
         if (!TCGConfig.getInstance().loseEssenceOnDeath()) {
            Energy.copyAllEssence(e.getOriginal(), e.getEntityPlayer());
         }
      } else {
         Energy.copyAllEssence(e.getOriginal(), e.getEntityPlayer());
      }

   }

   @SubscribeEvent
   public void onDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.player.field_71070_bA instanceof ContainerBinder) {
         ((ContainerBinder)event.player.field_71070_bA).inventory.func_174886_c(event.player);
      }

      if (event.player.field_71070_bA instanceof ContainerDeck) {
         ((ContainerDeck)event.player.field_71070_bA).inventory.func_174886_c(event.player);
      }

   }

   @SubscribeEvent
   public void onKnockoutEvent(BeatWildPixelmonEvent event) {
      PixelmonWrapper[] var2 = event.wpp.allPokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PixelmonWrapper wrapper = var2[var4];
         Pokemon pix = wrapper.pokemon;
         World w = event.player.func_130014_f_();
         EntityPlayerMP player = event.player;
         if (pix.getOwnerPlayerUUID() == null && pix.getOwnerTrainerUUID() == null) {
            double essenceToGive = TCGConfig.getInstance().getBaseEssence();
            double level = (double)pix.getLevel();
            EnumType typeOne = pix.getBaseStats().getType1();
            EnumType typeTwo = pix.getBaseStats().getType2();
            if (RARES.contains(pix.getSpecies())) {
               essenceToGive *= TCGConfig.getInstance().getRarePokemonEssenceModifier();
            }

            essenceToGive *= level;
            essenceToGive *= TCGConfig.getInstance().getLevelEssenceMultiplier(level);
            if (TCGConfig.getInstance().getEssenceDropRate() > 0.0) {
               if (essenceToGive <= TCGConfig.getInstance().getBaseEssence()) {
                  essenceToGive = TCGConfig.getInstance().getBaseEssence();
               }

               w.func_184148_a(player, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187604_bf, SoundCategory.PLAYERS, 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
               EssenceHelper.givePlayerEssenceFromType(player, typeOne, (int)(essenceToGive * TCGConfig.getInstance().getEssenceDropRate()));
            }

            if (TCGConfig.getInstance().typeTwoDropsEssence() && typeTwo != null && TCGConfig.getInstance().getTypeTwoEssenceDropRate() > 0.0) {
               EssenceHelper.givePlayerEssenceFromType(player, typeTwo, (int)(essenceToGive * TCGConfig.getInstance().getTypeTwoEssenceDropRate()));
            }
         }
      }

   }

   static {
      RARES = EnumSet.of(EnumSpecies.Articuno, EnumSpecies.Zapdos, EnumSpecies.Moltres, EnumSpecies.Mewtwo, EnumSpecies.Mew, EnumSpecies.Entei, EnumSpecies.Raikou, EnumSpecies.Suicune, EnumSpecies.Hooh, EnumSpecies.Lugia, EnumSpecies.Celebi, EnumSpecies.Regirock, EnumSpecies.Regice, EnumSpecies.Registeel, EnumSpecies.Latios, EnumSpecies.Latias, EnumSpecies.Groudon, EnumSpecies.Kyogre, EnumSpecies.Rayquaza, EnumSpecies.Jirachi, EnumSpecies.Deoxys, EnumSpecies.Uxie, EnumSpecies.Azelf, EnumSpecies.Mesprit, EnumSpecies.Dialga, EnumSpecies.Palkia, EnumSpecies.Giratina, EnumSpecies.Cresselia, EnumSpecies.Darkrai, EnumSpecies.Manaphy, EnumSpecies.Phione, EnumSpecies.Heatran, EnumSpecies.Regigigas, EnumSpecies.Shaymin, EnumSpecies.Arceus, EnumSpecies.Victini, EnumSpecies.Cobalion, EnumSpecies.Terrakion, EnumSpecies.Virizion, EnumSpecies.Keldeo, EnumSpecies.Thundurus, EnumSpecies.Tornadus, EnumSpecies.Landorus, EnumSpecies.Zekrom, EnumSpecies.Reshiram, EnumSpecies.Kyurem, EnumSpecies.Genesect, EnumSpecies.Meloetta, EnumSpecies.Xerneas, EnumSpecies.Yveltal, EnumSpecies.Zygarde, EnumSpecies.Diancie, EnumSpecies.Hoopa, EnumSpecies.Volcanion, EnumSpecies.Cosmog, EnumSpecies.Cosmoem, EnumSpecies.Solgaleo, EnumSpecies.Lunala, EnumSpecies.Necrozma, EnumSpecies.Magearna, EnumSpecies.Marshadow, EnumSpecies.Tapu_Koko, EnumSpecies.Tapu_Lele, EnumSpecies.Tapu_Bulu, EnumSpecies.Tapu_Fini, EnumSpecies.Nihilego, EnumSpecies.Buzzwole, EnumSpecies.Pheromosa, EnumSpecies.Xurkitree, EnumSpecies.Celesteela, EnumSpecies.Kartana, EnumSpecies.Guzzlord);
   }
}
