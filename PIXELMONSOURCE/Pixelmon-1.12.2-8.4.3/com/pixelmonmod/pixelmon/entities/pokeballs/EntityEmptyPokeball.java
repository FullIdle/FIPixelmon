package com.pixelmonmod.pixelmon.entities.pokeballs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.advancements.PixelmonAdvancements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.Experience;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BonusStats;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.forms.EnumBidoof;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import java.util.ArrayList;
import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class EntityEmptyPokeball extends EntityPokeBall {
   private int breakChance;
   private int waitTime;
   private int criticalTime;
   private BattleControllerBase battleController;
   private boolean isBattleThrown;
   private int totalTime;
   private boolean captureFinished;
   private double pokemonPosX;
   private double pokemonPosY;
   private double pokemonPosZ;
   boolean capturedPokemon;

   public EntityEmptyPokeball(World world) {
      super(world);
      this.breakChance = this.field_70146_Z.nextInt(30);
      this.criticalTime = 0;
      this.isBattleThrown = false;
      this.capturedPokemon = false;
   }

   public EntityEmptyPokeball(World world, EntityLivingBase entityliving, EnumPokeballs type, boolean dropItem) {
      super(type, world, entityliving, EnumPokeBallMode.empty);
      this.breakChance = this.field_70146_Z.nextInt(30);
      this.criticalTime = 0;
      this.isBattleThrown = false;
      this.capturedPokemon = false;
      this.field_70192_c = entityliving;
      this.dropItem = dropItem;
      this.func_184538_a(entityliving, entityliving.field_70125_A, entityliving.field_70177_z, 0.0F, 1.0F, 0.5F);
   }

   public EntityEmptyPokeball(World world, EntityLivingBase thrower, EntityPixelmon target, EnumPokeballs type, BattleControllerBase battleController) {
      super(type, world, thrower, EnumPokeBallMode.battle);
      this.breakChance = this.field_70146_Z.nextInt(30);
      this.criticalTime = 0;
      this.isBattleThrown = false;
      this.capturedPokemon = false;
      this.field_70192_c = thrower;
      this.dropItem = false;
      this.endRotationYaw = thrower.field_70759_as;
      this.pixelmon = target;
      this.isBattleThrown = true;
      this.battleController = battleController;
      this.field_70170_p = thrower.field_70170_p;
      battleController.pauseBattle();
      this.func_70012_b(thrower.field_70165_t, thrower.field_70163_u + (double)thrower.func_70047_e(), thrower.field_70161_v, thrower.field_70177_z, thrower.field_70125_A);
      this.field_70165_t -= (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.field_70163_u -= 0.10000000149011612;
      this.field_70161_v -= (double)(MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
      this.field_70159_w = (double)(-MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F)) * 0.8;
      this.field_70179_y = (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F)) * 0.8;
      this.field_70181_x = (double)(-MathHelper.func_76126_a(0.0F)) * 0.8;
   }

   protected void func_70184_a(RayTraceResult movingobjectposition) {
      if (!this.field_70170_p.field_72995_K && !(this.field_70181_x > 0.0)) {
         if (!Pixelmon.EVENT_BUS.post(new PokeballImpactEvent(this, movingobjectposition)) || this.isBattleThrown) {
            if (this.dropItem && this.breakChance == 1 && this.getType().getBreakChance() != 0) {
               this.field_70170_p.func_184148_a((EntityPlayer)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187635_cQ, SoundCategory.PLAYERS, 0.8F, 0.8F + this.field_70170_p.field_73012_v.nextFloat() * 0.4F);
               this.func_70099_a(new ItemStack(Blocks.field_150430_aB), 0.0F);
               this.func_70099_a(new ItemStack(PixelmonItemsPokeballs.ironBase), 0.0F);
               this.func_70099_a(new ItemStack(this.breakBall()), 0.0F);
               this.func_70106_y();
            } else {
               if (this.isBattleThrown && !this.field_70170_p.field_72995_K) {
                  if (!this.getIsWaiting()) {
                     this.startBattleCapture();
                  } else {
                     this.startShake();
                  }
               } else {
                  if (movingobjectposition.field_72313_a == Type.BLOCK) {
                     IBlockState state = this.field_70170_p.func_180495_p(movingobjectposition.func_178782_a());
                     Block hitBlock = state.func_177230_c();
                     Material mat = state.func_185904_a();
                     if (hitBlock == Blocks.field_150329_H || mat == Material.field_151585_k) {
                        return;
                     }

                     if (!this.getIsWaiting() && mat != null && mat.func_76220_a()) {
                        if (this.dropItem) {
                           this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                        }

                        this.func_70106_y();
                        return;
                     }
                  }

                  if (movingobjectposition.field_72308_g != null && movingobjectposition.field_72308_g instanceof EntityPixelmon) {
                     if (this.getIsWaiting()) {
                        return;
                     }

                     this.pixelmon = (EntityPixelmon)movingobjectposition.field_72308_g;
                     this.pokemonPosX = this.pixelmon.field_70165_t;
                     this.pokemonPosY = this.pixelmon.field_70163_u;
                     this.pokemonPosZ = this.pixelmon.field_70161_v;
                     if (this.pixelmon.battleController != null) {
                        boolean inBattle = false;
                        if (this.pixelmon.battleController.checkValid()) {
                           inBattle = true;
                        } else if (this.pixelmon.getTrainer() != null) {
                           this.pixelmon.func_70106_y();
                           inBattle = true;
                        }

                        if (inBattle) {
                           ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.pokeinbattle");
                           if (this.dropItem) {
                              this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                           }

                           this.func_70106_y();
                           return;
                        }
                     }

                     if (this.pixelmon.getBossMode() != EnumBossMode.NotBoss) {
                        ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.boss");
                        if (this.dropItem) {
                           this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                        }

                        this.func_70106_y();
                        return;
                     }

                     if (this.pixelmon.getPokemonData().getBonusStats().preventsCapture()) {
                        ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.aura");
                        if (this.dropItem) {
                           this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                        }

                        this.func_70106_y();
                        return;
                     }

                     if (this.pixelmon.hasOwner() || this.pixelmon.getTrainer() != null) {
                        if (this.pixelmon.func_70902_q() == this.field_70192_c) {
                           ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.alreadyown");
                        } else {
                           ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.otherspokemon");
                        }

                        if (this.dropItem) {
                           this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                        }

                        this.func_70106_y();
                        return;
                     }

                     if (this.pixelmon.hitByPokeball != null) {
                        return;
                     }

                     this.pixelmon.hitByPokeball = this;
                     if (!this.getIsWaiting()) {
                        this.startCapture();
                     }
                  } else {
                     if (!this.getIsWaiting()) {
                        if (this.dropItem) {
                           this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
                        }

                        this.func_70106_y();
                        return;
                     }

                     this.startShake();
                  }
               }

               super.func_70184_a(movingobjectposition);
            }
         }
      }
   }

   private void startCapture() {
      this.doCaptureCalc(this.pixelmon);
      this.pixelmon.field_70159_w = this.pixelmon.field_70181_x = this.pixelmon.field_70179_y = 0.0;
      this.initialScale = this.pixelmon.getPixelmonScale();
      this.setAnimation(AnimationType.BOUNCEOPEN);
      this.setIsWaiting(true);
      this.setId(this.canCatch ? this.numShakes : -1 * this.numShakes);
      this.field_70181_x = 0.0;
      this.field_70159_w = 0.0;
      this.field_70179_y = 0.0;
      this.field_70125_A = 0.0F;

      int i;
      for(i = -2; !this.field_70170_p.func_175623_d(new BlockPos((int)this.field_70165_t, (int)Math.ceil(this.field_70163_u) + i, (int)this.field_70161_v)); ++i) {
      }

      this.field_70163_u = Math.ceil(this.field_70163_u) + (double)i + 0.10000000149011612;
   }

   private void startBattleCapture() {
      this.pokemonPosX = this.pixelmon.field_70165_t;
      this.pokemonPosY = this.pixelmon.field_70163_u;
      this.pokemonPosZ = this.pixelmon.field_70161_v;
      this.pixelmon.hitByPokeball = this;
      this.startCapture();
      this.field_70165_t = (double)((int)this.field_70165_t);
      this.field_70161_v = (double)((int)this.field_70161_v);
   }

   private void forceBattleCapture() {
      this.startBattleCapture();
      this.pixelmon.unloadEntity();
      this.setIsOnGround(true);
   }

   private void startShake() {
      this.field_70181_x = 0.0;
      this.field_70159_w = 0.0;
      this.field_70179_y = 0.0;
      this.setIsOnGround(true);
   }

   public void func_70071_h_() {
      if (this.field_70170_p.field_72995_K) {
         super.func_70071_h_();
      } else {
         ++this.totalTime;
         ++this.criticalTime;
         if (this.criticalTime == 43 && this.getCritical()) {
            WorldServer world = (WorldServer)this.field_70170_p;
            this.field_70170_p.func_184148_a((EntityPlayer)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187718_dS, SoundCategory.PLAYERS, 0.6F, 1.2F);

            for(int i = 0; i < 15; ++i) {
               world.func_180505_a(EnumParticleTypes.CRIT, false, this.field_70165_t, this.field_70163_u, this.field_70161_v, 1, 0.0, 0.0, 0.0, 0.75, new int[0]);
            }
         }

         if (this.getIsOnGround()) {
            if (this.waitTime < 0) {
               this.waitTime = 0;
            }

            ++this.waitTime;
         }

         if (this.capturedPokemon) {
            if (this.waitTime > 20) {
               this.storeCapture();
               this.func_70106_y();
            }
         } else {
            int var10000 = this.waitTime;
            int var10001 = this.numShakes;
            this.getClass();
            if (var10000 > var10001 * 25) {
               this.catchPokemon();
               this.waitTime = 0;
            } else if (this.totalTime > 100 && this.isBattleThrown && !this.getIsWaiting() && !this.captureFinished) {
               this.forceBattleCapture();
            }
         }

         super.func_70071_h_();
      }
   }

   private void storeCapture() {
      this.pixelmon.retrieve();
      Pokemon pokemon = this.pixelmon.getPokemonData();
      if (pokemon.getOwnerPlayer() != null) {
         this.setIsWaiting(false);
      } else {
         pokemon.setOriginalTrainer((EntityPlayerMP)this.field_70192_c);
         pokemon.setCaughtBall(this.getType());
         pokemon.setFriendship(pokemon.getBaseStats().getBaseFriendship());
         PokeballTypeHelper.doAfterEffect(this.getType(), this.pixelmon);
         Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP)this.field_70192_c, ReceiveType.PokeBall, pokemon));
         if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(this.field_70192_c.func_110124_au(), pokemon, EnumPokedexRegisterStatus.caught, "capture"))) {
            Pokedex.set(this.field_70192_c.func_110124_au(), pokemon, EnumPokedexRegisterStatus.caught);
         }

         Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c).add(pokemon);
         if (this.getMode() == EnumPokeBallMode.battle) {
            PlayerParticipant p = (PlayerParticipant)this.battleController.getParticipantForEntity(this.field_70192_c);
            Experience.awardExp(this.battleController.participants, p, this.pixelmon.getPixelmonWrapper());
            this.battleController.endBattle(EnumBattleEndCause.FORCE);
         }

         this.setIsWaiting(false);
         Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c).stats.addCaughtTypes(pokemon.getBaseStats().types);
         Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c).transientData.captureCombo.onCapture((EntityPlayerMP)this.field_70192_c, pokemon.getSpecies());
         PixelmonAdvancements.throwCaptureTriggers((EntityPlayerMP)this.field_70192_c, this.getType(), pokemon);
      }
   }

   protected void catchPokemon() {
      if (!this.isBattleThrown && this.pixelmon.battleController != null) {
         boolean inBattle = false;
         if (this.pixelmon.battleController.checkValid()) {
            inBattle = true;
         } else if (this.pixelmon.getTrainer() != null) {
            inBattle = true;
         }

         if (inBattle) {
            ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.pokeinbattle");
            if (this.dropItem) {
               this.func_70099_a(new ItemStack(this.getType().getItem()), 0.0F);
            }

            this.func_70106_y();
            return;
         }
      }

      this.captureFinished = true;
      if (this.canCatch) {
         int level;
         if (this.getType() == EnumPokeballs.LuxuryBall) {
            if (PixelmonConfig.chanceToGetSpecialBidoof > 0 && this.pixelmon.getSpecies() == EnumSpecies.Bidoof && RandomHelper.getRandomChance(1.0F / (float)PixelmonConfig.chanceToGetSpecialBidoof)) {
               this.pixelmon.setForm(EnumBidoof.SIRDOOFUSIII);
            }
         } else if (this.getType() == EnumPokeballs.LoveBall) {
            Calendar currentCalendar = Calendar.getInstance();
            int month = currentCalendar.get(2);
            level = currentCalendar.get(5);
            if (this.pixelmon.getSpecies().getPossibleForms(false).contains(EnumSpecial.Valentine)) {
               if (month == 1 && level >= 14 && level <= 20) {
                  this.pixelmon.getPokemonData().setForm(EnumSpecial.Valentine);
               } else if (RandomHelper.getRandomChance(this.field_70146_Z, 5)) {
                  this.pixelmon.getPokemonData().setForm(EnumSpecial.Valentine);
               }
            }
         }

         CaptureEvent.SuccessfulCapture capEvent = new CaptureEvent.SuccessfulCapture((EntityPlayerMP)this.field_70192_c, this.pixelmon, this);
         if (Pixelmon.EVENT_BUS.post(capEvent)) {
            this.failCapture();
            return;
         }

         this.pixelmon = capEvent.getPokemon();
         this.pixelmon.getPokemonData().setOriginalTrainer((EntityPlayerMP)this.field_70192_c);
         if (!this.pixelmon.getPokemonData().getBonusStats().isPersistentAfterCapture()) {
            this.pixelmon.getPokemonData().setBonusStats(new BonusStats());
         }

         boolean transformToDitto = this.field_70146_Z.nextDouble() <= PixelmonConfig.transformToDittoOnCatch;
         if (this.pixelmon.getSpecies().getPossibleForms(false).contains(EnumSpecial.Alien) && this.getType() == EnumPokeballs.BeastBall) {
            this.pixelmon.setForm(EnumSpecial.Alien);
         }

         if (this.pixelmon.isLegendary() || !PixelmonConfig.canTransformToDittoOnCatch.contains(this.pixelmon.getPokemonName())) {
            transformToDitto = false;
         }

         if (transformToDitto || this.pixelmon.getSpecies() == EnumSpecies.Meltan && this.field_70146_Z.nextDouble() <= PixelmonConfig.meltanTransformChance) {
            transformToDitto = true;
            level = this.pixelmon.getPokemonData().getLevel();
            this.pixelmon.getPokemonData().setSpecies(EnumSpecies.Ditto, true);
            this.pixelmon.getPokemonData().initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL, EnumInitializeCategory.SPECIES);
            this.pixelmon.getPokemonData().setLevel(level);
         }

         TextComponentTranslation message = ChatHandler.getMessage(transformToDitto ? "pixelmon.pokeballs.capturetransform" : "pixelmon.pokeballs.capture", this.pixelmon.getLocalizedName());
         if (this.pixelmon.battleController == null) {
            ChatHandler.sendChat(this.field_70192_c, message);
         } else {
            PixelmonWrapper pw = this.pixelmon.getPixelmonWrapper();
            if (pw != null) {
               pw.resetOnSwitch();
               ItemHeld currentHeldItem = pw.getHeldItem();
               if (currentHeldItem != NoItem.noItem && currentHeldItem != pw.initialCopyOfPokemon.getHeldItemAsItemHeld()) {
                  pw.enableReturnHeldItem();
               }
            }

            this.pixelmon.battleController.sendToAll(message);
            Experience.awardExp(this.pixelmon.battleController.participants, this.pixelmon.getParticipant(), pw);
         }

         int rolls = 1;
         if (this.func_85052_h() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)this.func_85052_h();
            if (Pixelmon.storageManager.getParty(player).getMarkCharm().isActive()) {
               rolls = PixelmonConfig.markCharmRolls;
            }
         }

         for(int i = 0; i < rolls && this.pixelmon.getPokemonData().getRibbons().size() == 0; ++i) {
            if (Math.random() <= 0.001) {
               this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.RARE);
            } else if (Math.random() <= 0.01) {
               this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.group28[this.field_70170_p.field_73012_v.nextInt(28)]);
            } else if (Math.random() <= 0.02) {
               this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.UNCOMMON);
            } else if (Math.random() <= 0.02) {
               Biome biome;
               WeatherType weatherType;
               if (this.pixelmon.battleController != null) {
                  if (this.pixelmon.battleController.globalStatusController.hasStatus(StatusType.MistyTerrain)) {
                     this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.MISTY);
                  } else if (this.pixelmon.battleController.globalStatusController.hasStatus(StatusType.Sandstorm)) {
                     this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.SANDSTORM);
                  } else if (this.pixelmon.battleController.globalStatusController.hasStatus(StatusType.Sunny)) {
                     this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.DRY);
                  } else {
                     weatherType = WeatherType.get(this.field_70170_p);
                     biome = this.field_70170_p.func_180494_b(this.pixelmon.func_180425_c());
                     if (weatherType == WeatherType.RAIN) {
                        if (biome.func_150559_j()) {
                           this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.SNOWY);
                        } else if (biome.func_76738_d()) {
                           this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.RAINY);
                        }
                     } else if (weatherType == WeatherType.STORM) {
                        if (biome.func_150559_j()) {
                           this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.BLIZZARD);
                        } else if (biome.func_76738_d()) {
                           this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.STORMY);
                        }
                     } else {
                        this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.CLOUDY);
                     }
                  }
               } else {
                  weatherType = WeatherType.get(this.field_70170_p);
                  biome = this.field_70170_p.func_180494_b(this.pixelmon.func_180425_c());
                  if (weatherType == WeatherType.RAIN) {
                     if (biome.func_150559_j()) {
                        this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.SNOWY);
                     } else if (biome.func_76738_d()) {
                        this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.RAINY);
                     }
                  } else if (weatherType == WeatherType.STORM) {
                     if (biome.func_150559_j()) {
                        this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.BLIZZARD);
                     } else if (biome.func_76738_d()) {
                        this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.STORMY);
                     }
                  } else {
                     this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.CLOUDY);
                  }
               }
            } else if (Math.random() <= 0.02) {
               ArrayList current = WorldTime.getCurrent(this.field_70170_p);
               if (current.contains(WorldTime.AFTERNOON)) {
                  this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.LUNCHTIME);
               } else if (current.contains(WorldTime.DUSK)) {
                  this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.DUSK);
               } else if (!current.contains(WorldTime.NIGHT) && !current.contains(WorldTime.DAWN)) {
                  this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.DAWN);
               } else {
                  this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.SLEEPY_TIME);
               }
            } else if (Math.random() <= 0.04 && this.battleController != null && this.battleController.wasFishing) {
               this.pixelmon.getPokemonData().addRibbon(EnumRibbonType.FISHING);
            }
         }

         this.capturedPokemon = true;
         this.waitTime = 0;
      } else {
         this.failCapture();
      }

   }

   private void failCapture() {
      Pixelmon.EVENT_BUS.post(new CaptureEvent.FailedCapture((EntityPlayerMP)this.field_70192_c, this.pixelmon, this));
      this.openAngle = -1.5707964F;
      this.waitTime = 0;
      this.setIsWaiting(false);
      this.pixelmon.func_70107_b(this.pokemonPosX, this.pokemonPosY, this.pokemonPosZ);
      this.pixelmon.hitByPokeball = null;
      if (this.field_70170_p.func_73045_a(this.pixelmon.func_145782_y()) != null) {
         this.pixelmon.unloadEntity();
      }

      this.pixelmon.field_70128_L = false;

      try {
         this.field_70170_p.func_72838_d(this.pixelmon);
      } catch (IllegalStateException var3) {
      }

      this.pixelmon.func_70107_b(this.pokemonPosX, this.pokemonPosY, this.pokemonPosZ);
      this.pixelmon.setPixelmonScale(this.initialScale);
      this.pixelmon.field_70128_L = false;
      TextComponentTranslation message = ChatHandler.getMessage("pixelmon.pokeballs.brokefree", this.pixelmon.getLocalizedName());
      if (this.getMode() == EnumPokeBallMode.battle && !this.battleController.battleEnded) {
         this.pixelmon.battleController = this.battleController;
         if (this.pixelmon.transformed != null) {
            this.pixelmon.updateTransformed();
         }

         PlayerParticipant playerParticipant = (PlayerParticipant)this.battleController.getParticipantForEntity(this.field_70192_c);
         playerParticipant.lastFailedCapture = this.getType();
         this.battleController.sendToAll(message);
         this.battleController.endPause();
      } else {
         ChatHandler.sendChat(this.field_70192_c, message);
      }

      this.func_70106_y();
   }

   public void func_70106_y() {
      if (this.isBattleThrown && !this.captureFinished) {
         if (!this.getIsWaiting()) {
            this.forceBattleCapture();
         }

         this.catchPokemon();
      } else {
         if (this.capturedPokemon && this.getIsWaiting()) {
            this.storeCapture();
         }

         if (this.pixelmon != null && this.pixelmon.hitByPokeball == this) {
            this.pixelmon.hitByPokeball = null;
         }

         super.func_70106_y();
      }
   }
}
