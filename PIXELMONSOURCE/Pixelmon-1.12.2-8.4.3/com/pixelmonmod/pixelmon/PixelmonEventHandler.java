package com.pixelmonmod.pixelmon;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.advancements.PixelmonAdvancements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.HeldItemChangedEvent;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.api.events.pokemon.MovesetEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Competitive;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumArceus;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurningSalt;
import com.pixelmonmod.pixelmon.enums.forms.EnumCherrim;
import com.pixelmonmod.pixelmon.enums.forms.EnumClobbopus;
import com.pixelmonmod.pixelmon.enums.forms.EnumFeebas;
import com.pixelmonmod.pixelmon.enums.forms.EnumGenesect;
import com.pixelmonmod.pixelmon.enums.forms.EnumGiratina;
import com.pixelmonmod.pixelmon.enums.forms.EnumHoopa;
import com.pixelmonmod.pixelmon.enums.forms.EnumKeldeo;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.enums.forms.EnumPichu;
import com.pixelmonmod.pixelmon.enums.forms.EnumShaymin;
import com.pixelmonmod.pixelmon.enums.forms.EnumShellos;
import com.pixelmonmod.pixelmon.enums.forms.EnumSilvally;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpheal;
import com.pixelmonmod.pixelmon.enums.forms.EnumToxtricity;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.items.ItemMemory;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.ItemZCrystal;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PixelmonEventHandler {
   @SubscribeEvent
   public void onMoveSkillUsed(UseMoveSkillEvent event) {
      EntityPlayerMP trainer = event.pixelmon.getPokemonData().getOwnerPlayer();
      if (event.moveSkill.id == "forage" && event.pixelmon.getSpecies() == EnumSpecies.Pichu && event.pixelmon.getPokemonData().getFriendship() == 255 && !event.pixelmon.getPokemonData().getPersistentData().func_74767_n("Pichu_friend") && event.data instanceof Tuple && Pixelmon.storageManager.getParty(trainer).hasSpace()) {
         Tuple data = (Tuple)event.data;
         if (event.pixelmon.field_70170_p.func_180495_p((BlockPos)data.func_76341_a()).func_177230_c() == PixelmonBlocks.shrineIlex) {
            event.pixelmon.getPokemonData().getPersistentData().func_74757_a("Pichu_friend", true);
            Pokemon pichu = Pixelmon.pokemonFactory.create(EnumSpecies.Pichu);
            pichu.setGender(Gender.Female);
            pichu.setLevel(30);
            pichu.setForm(EnumPichu.SPIKY);
            pichu.setAbility("Static");
            pichu.setHeldItem(new ItemStack(PixelmonItemsHeld.zapPlate));
            pichu.getMoveset().set(0, (Attack)(new Attack("Volt Tackle")));
            pichu.getMoveset().set(1, (Attack)(new Attack("Helping Hand")));
            pichu.getMoveset().set(2, (Attack)(new Attack("Swagger")));
            pichu.getMoveset().set(3, (Attack)(new Attack("Pain Split")));
            pichu.getPersistentData().func_74757_a("Pichu_friend", true);
            Pixelmon.storageManager.getParty(trainer).add(pichu);
            NPCChatEvent event2 = new NPCChatEvent((EntityNPC)null, trainer, Lists.newArrayList(new String[]{"event.spikepichu"}));
            if (!Pixelmon.EVENT_BUS.post(event2)) {
               Pixelmon.network.sendTo(new SetNPCData("", event2.getChat()), trainer);
               OpenScreen.open(trainer, EnumGuiScreen.NPCChat, -1);
            }
         }
      }

   }

   @SubscribeEvent
   public void onBeatPokemon(BeatWildPixelmonEvent event) {
      PixelmonAdvancements.throwBattleTriggers(event.player);
      if (PixelmonConfig.awardTokens) {
         Iterator var2 = event.wpp.controlledPokemon.iterator();

         while(var2.hasNext()) {
            PixelmonWrapper wrapper = (PixelmonWrapper)var2.next();
            EntityPixelmon pokemon = wrapper.entity;
            event.player.field_71071_by.func_70441_a(ItemPixelmonSprite.getPhoto(pokemon.getPokemonData()));
         }
      }

   }

   @SubscribeEvent
   public void christmasGifts(BeatWildPixelmonEvent event) {
      if (event.wpp.controlledPokemon.stream().anyMatch((it) -> {
         return it.pokemon.getSpecies() == EnumSpecies.Delibird;
      })) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(event.player);
         LocalDate now = LocalDate.now();
         int year = now.get(ChronoField.YEAR);
         Month month = now.getMonth();
         int day = now.getDayOfMonth();
         if (month == Month.DECEMBER && day >= 20 && day <= 31 && !party.playerData.getWasGifted(year)) {
            ChatHandler.sendChat(event.player, "christmas.received");
            event.player.field_71071_by.func_70441_a(new ItemStack(PixelmonItems.gift));
            party.playerData.receivedGift(year);
         }
      }

   }

   @SubscribeEvent
   public void onHeldItemChanged(HeldItemChangedEvent event) {
      if (event.getSpecies() == EnumSpecies.Giratina) {
         EnumGiratina curForm = (EnumGiratina)event.pokemon.getFormEnum();
         EnumGiratina newForm = curForm;
         if (curForm == EnumGiratina.ALTERED && event.newHeldItem.func_77973_b() == PixelmonItemsHeld.griseous_orb) {
            newForm = EnumGiratina.ORIGIN;
         } else if (curForm == EnumGiratina.ORIGIN && (event.newHeldItem.func_190926_b() || event.newHeldItem.func_77973_b() != PixelmonItemsHeld.griseous_orb)) {
            newForm = EnumGiratina.ALTERED;
         }

         if (curForm != newForm) {
            event.pokemon.setForm(newForm);
            if (event.player != null) {
               ChatHandler.sendChat(event.player, "pixelmon.abilities.changeform", "Giratina");
            }
         }
      } else if (event.getSpecies() == EnumSpecies.Arceus) {
         EnumArceus curForm = (EnumArceus)event.pokemon.getFormEnum();
         EnumType type;
         if (!event.newHeldItem.func_190926_b() && (event.newHeldItem.func_77973_b() instanceof ItemPlate || event.newHeldItem.func_77973_b() instanceof ItemZCrystal)) {
            if (event.newHeldItem.func_77973_b() instanceof ItemPlate) {
               type = ((ItemPlate)event.newHeldItem.func_77973_b()).getType();
            } else {
               type = ((ItemZCrystal)event.newHeldItem.func_77973_b()).type.affiliatedType;
            }
         } else {
            type = EnumType.Normal;
         }

         EnumArceus newForm = EnumArceus.getForm(type);
         if (curForm != newForm) {
            event.pokemon.setForm(newForm);
         }
      } else {
         Item item;
         if (event.getSpecies() == EnumSpecies.Silvally) {
            EnumSilvally curForm = (EnumSilvally)event.pokemon.getFormEnum();
            EnumSilvally newForm = EnumSilvally.NORMAL;
            if (!event.newHeldItem.func_190926_b()) {
               item = event.newHeldItem.func_77973_b();
               if (item instanceof ItemMemory) {
                  newForm = EnumSilvally.getForm(((ItemMemory)item).type);
               }
            }

            if (curForm != newForm) {
               event.pokemon.setForm(newForm);
            }
         } else if (event.getSpecies() == EnumSpecies.Genesect) {
            EnumGenesect curForm = (EnumGenesect)event.pokemon.getFormEnum();
            EnumGenesect newForm = EnumGenesect.NORMAL;
            if (!event.newHeldItem.func_190926_b()) {
               item = event.newHeldItem.func_77973_b();
               if (item == PixelmonItemsHeld.burnDrive) {
                  newForm = EnumGenesect.BURN;
               } else if (item == PixelmonItemsHeld.chillDrive) {
                  newForm = EnumGenesect.CHILL;
               } else if (item == PixelmonItemsHeld.douseDrive) {
                  newForm = EnumGenesect.DOUSE;
               } else if (item == PixelmonItemsHeld.shockDrive) {
                  newForm = EnumGenesect.SHOCK;
               }
            }

            if (curForm != newForm) {
               event.pokemon.setForm(newForm);
               if (event.player != null) {
                  ChatHandler.sendChat(event.player, "pixelmon.abilities.changeform", "Genesect");
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onSentOut(PixelmonSendOutEvent event) {
      if (event.pokemon.getSpecies() == EnumSpecies.Shaymin) {
         if (event.pokemon.getFormEnum() == EnumShaymin.SKY && !event.player.func_71121_q().func_72935_r()) {
            event.pokemon.setForm(EnumShaymin.LAND);
            ChatHandler.sendChat(event.player, "pixelmon.abilities.changeform", event.pokemon.getDisplayName());
         }
      } else if (event.pokemon.getSpecies() == EnumSpecies.Cherrim) {
         if (event.pokemon.getFormEnum() == EnumCherrim.SUNSHINE) {
            event.pokemon.setForm(EnumCherrim.OVERCAST);
         }
      } else if (event.pokemon.getSpecies() == EnumSpecies.Toxtricity) {
         IEnumForm forme = event.pokemon.getFormEnum();
         if (forme instanceof EnumToxtricity) {
            EnumToxtricity form = (EnumToxtricity)forme;
            if (!form.getNatures().contains(event.pokemon.getBaseNature())) {
               event.pokemon.setForm(form.getDefaultFromTemporary(event.pokemon));
            }
         }
      } else if (event.pokemon.getSpecies() == EnumSpecies.Hoopa && event.pokemon.getFormEnum() == EnumHoopa.UNBOUND && System.currentTimeMillis() > event.pokemon.getPersistentData().func_74763_f("unboundTime") + 3600000L) {
         event.pokemon.setForm(EnumHoopa.CONFINED);
         event.pokemon.getPersistentData().func_82580_o("unboundTime");
         ChatHandler.sendChat(event.player, "pixelmon.abilities.changeform", event.pokemon.getDisplayName());
      }

   }

   @SubscribeEvent
   public void onMoveLearned(MovesetEvent.LearntMoveEvent event) {
      if (event.pokemon.getSpecies() == EnumSpecies.Keldeo) {
         if (event.learntAttack.isAttack("Secret Sword") && event.pokemon.getForm() != EnumKeldeo.RESOLUTE.getForm()) {
            event.pokemon.setForm(EnumKeldeo.RESOLUTE);
         } else if (!event.moveset.hasAttack("Secret Sword") && event.pokemon.getForm() == EnumKeldeo.RESOLUTE.getForm()) {
            event.pokemon.setForm(EnumKeldeo.ORDINARY);
         }
      }

   }

   @SubscribeEvent
   public void onMoveForgot(MovesetEvent.ForgotMoveEvent event) {
      if (event.pokemon.getSpecies() == EnumSpecies.Keldeo && event.forgottenAttack.isAttack("Secret Sword")) {
         event.pokemon.setForm(EnumKeldeo.ORDINARY);
      }

   }

   @SubscribeEvent
   public void onPostEvo(EvolveEvent.PostEvolve event) {
      if (event.pokemon.getSpecies() == EnumSpecies.Meowstic && event.pokemon.getPokemonData().getGender() == Gender.Female) {
         event.pokemon.setForm(1);
         if (event.pokemon.getPokemonData().getAbilitySlot() == 2) {
            event.pokemon.getPokemonData().setAbility((AbilityBase)(new Competitive()));
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onPokemonSpawn(SpawnEvent event) {
      if (event.action instanceof SpawnActionPokemon) {
         SpawnActionPokemon spawnPokemon = (SpawnActionPokemon)event.action;
         if (event.action.spawnLocation.types.contains(LocationType.OAS_ROD)) {
            IEnumForm form = FormLogRegistry.getRandomFish();
            Pokemon pokemon = ((EntityPixelmon)spawnPokemon.getOrCreateEntity()).getPokemonData();
            if (form instanceof EnumShellos) {
               pokemon.setSpecies(EnumSpecies.Shellos);
               pokemon.setForm(form.getForm());
            } else if (form instanceof EnumMagikarp) {
               pokemon.setSpecies(EnumSpecies.Magikarp);
               pokemon.setForm(form.getForm());
            } else if (form instanceof EnumClobbopus) {
               pokemon.setSpecies(EnumSpecies.Clobbopus);
               pokemon.setForm(form.getForm());
            } else if (form instanceof EnumSpheal) {
               pokemon.setSpecies(EnumSpecies.Spheal);
               pokemon.setForm(form.getForm());
            } else if (form instanceof EnumFeebas) {
               pokemon.setSpecies(EnumSpecies.Feebas);
               pokemon.setForm(form.getForm());
            }

            pokemon.initialize(EnumInitializeCategory.SPECIES);
         } else if (!event.action.spawnLocation.types.contains(LocationType.WATER) && !event.action.spawnLocation.types.contains(LocationType.SURFACE_WATER) && !event.action.spawnLocation.types.contains(LocationType.UNDERGROUND_WATER) && !event.action.spawnLocation.types.contains(LocationType.SEAFLOOR) && !event.action.spawnLocation.types.contains(LocationType.SEAWEED) && !event.action.spawnLocation.types.contains(LocationType.LAVA) && !event.action.spawnLocation.types.contains(LocationType.SURFACE_LAVA) && !event.action.spawnLocation.types.contains(LocationType.UNDERGROUND_LAVA)) {
            int rodQuality = false;
            byte rodQuality;
            if (event.action.spawnLocation.types.contains(LocationType.SUPREME_ROD_QUALITY)) {
               rodQuality = 6;
            } else if (event.action.spawnLocation.types.contains(LocationType.PRO_ROD_QUALITY)) {
               rodQuality = 5;
            } else if (event.action.spawnLocation.types.contains(LocationType.RARE_ROD_QUALITY)) {
               rodQuality = 4;
            } else if (event.action.spawnLocation.types.contains(LocationType.GREAT_ROD_QUALITY)) {
               rodQuality = 3;
            } else if (event.action.spawnLocation.types.contains(LocationType.GOOD_ROD_QUALITY)) {
               rodQuality = 2;
            } else if (event.action.spawnLocation.types.contains(LocationType.SO_SO_ROD_QUALITY)) {
               rodQuality = 1;
            } else {
               if (!event.action.spawnLocation.types.contains(LocationType.OK_ROD_QUALITY)) {
                  return;
               }

               rodQuality = 0;
            }

            EntityPixelmon pokemon;
            if (EnumSpecies.Magikarp.name.equals(spawnPokemon.usingSpec.name)) {
               pokemon = (EntityPixelmon)spawnPokemon.getOrCreateEntity();
               if (pokemon != null) {
                  if (CollectionHelper.anyMatch(event.action.spawnLocation.types, Sets.newHashSet(new LocationType[]{LocationType.LAVA, LocationType.OLD_ROD_LAVA, LocationType.GOOD_ROD_LAVA, LocationType.SUPER_ROD_LAVA}))) {
                     pokemon.setForm(EnumMagikarp.ROASTED);
                  } else {
                     IEnumForm form = EnumMagikarp.getWeightedRodForm(rodQuality);
                     if (form == EnumFeebas.KARP) {
                        pokemon.getPokemonData().setSpecies(EnumSpecies.Feebas);
                     }

                     pokemon.setForm(form);
                  }

                  pokemon.update(new EnumUpdateType[]{EnumUpdateType.Form});
               }
            }

            if (EnumSpecies.Shellos.name.equals(spawnPokemon.usingSpec.name)) {
               pokemon = (EntityPixelmon)spawnPokemon.getOrCreateEntity();
               if (pokemon != null) {
                  pokemon.setForm(EnumShellos.getWeightedRodForm(rodQuality));
                  if (pokemon.getPokemonData().isShiny()) {
                     pokemon.setForm(pokemon.getPokemonData().getForm() % 2);
                  }

                  pokemon.update(new EnumUpdateType[]{EnumUpdateType.Form});
               }
            }

            if (EnumSpecies.Clobbopus.name.equals(spawnPokemon.usingSpec.name)) {
               pokemon = (EntityPixelmon)spawnPokemon.getOrCreateEntity();
               if (pokemon != null) {
                  pokemon.setForm(EnumClobbopus.getWeightedRodForm(rodQuality));
                  pokemon.update(new EnumUpdateType[]{EnumUpdateType.Form});
               }
            }

            if (EnumSpecies.Slugma.name.equals(spawnPokemon.usingSpec.name)) {
               pokemon = (EntityPixelmon)spawnPokemon.getOrCreateEntity();
               if (pokemon != null) {
                  pokemon.setForm(EnumBurningSalt.getWeightedRodForm(rodQuality));
                  pokemon.update(new EnumUpdateType[]{EnumUpdateType.Form});
               }
            }

            if (EnumSpecies.Spheal.name.equals(spawnPokemon.usingSpec.name)) {
               pokemon = (EntityPixelmon)spawnPokemon.getOrCreateEntity();
               if (pokemon != null) {
                  pokemon.setForm(EnumSpheal.getWeightedRodForm(rodQuality));
                  pokemon.update(new EnumUpdateType[]{EnumUpdateType.Form});
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onReceive(PixelmonReceivedEvent event) {
      EntityPlayerMP player = event.player;
      PixelmonAdvancements.POKEDEX_TRIGGER.trigger(player);
      if (event.receiveType == ReceiveType.Starter) {
         PixelmonAdvancements.STARTER_TRIGGER.trigger(player);
      }

   }

   @SubscribeEvent
   public void onSmelt(PlayerEvent.ItemSmeltedEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         Item item = event.smelting.func_77973_b();
         if (item == Items.field_151042_j || item == Items.field_151043_k || item == PixelmonItems.aluminiumIngot || item == PixelmonItems.siliconItem) {
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
            Pokemon pokemon;
            if (pps != null) {
               Iterator var5 = pps.getTeam().iterator();

               while(var5.hasNext()) {
                  pokemon = (Pokemon)var5.next();
                  if (pokemon.getSpecies() == EnumSpecies.Meltan) {
                     MeltanStats meltanStats = (MeltanStats)pokemon.getExtraStats(MeltanStats.class);
                     meltanStats.oresSmelted += event.smelting.func_190916_E();
                     pokemon.markDirty(EnumUpdateType.Stats);
                     break;
                  }
               }
            }

            for(int i = 0; i < event.smelting.func_190916_E(); ++i) {
               if (PixelmonConfig.allowLegendariesSpawn && PixelmonConfig.meltanSpawnChance > 0 && player.field_70170_p.field_73012_v.nextInt(PixelmonConfig.meltanSpawnChance) == 0) {
                  pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.Meltan);
                  int meltanRadius = 5;
                  pokemon.getOrSpawnPixelmon(player.func_71121_q(), player.field_70165_t + (double)RandomHelper.getRandomNumberBetween(-meltanRadius, meltanRadius), player.field_70163_u + 0.5, player.field_70161_v + (double)RandomHelper.getRandomNumberBetween(-meltanRadius, meltanRadius));
                  break;
               }
            }
         }
      }

   }

   @SubscribeEvent
   public static void structureVoidHelper(PlayerInteractEvent.RightClickBlock event) {
      if (Pixelmon.devEnvironment && event.getEntityPlayer() instanceof EntityPlayerMP && event.getHand() == EnumHand.MAIN_HAND && event.getFace() != null && event.getItemStack().func_77973_b() == Item.func_150898_a(Blocks.field_189881_dj) && event.getWorld().func_180495_p(new BlockPos(event.getHitVec())).func_177230_c() == Blocks.field_189881_dj) {
         BlockPos pos = event.getPos().func_177972_a(event.getFace());

         for(int i = 0; i < event.getItemStack().func_190916_E(); ++i) {
            event.getWorld().func_175656_a(pos, Blocks.field_189881_dj.func_176223_P());
            event.getWorld().func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187845_fY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            pos = pos.func_177972_a(event.getFace());
         }
      }

   }
}
