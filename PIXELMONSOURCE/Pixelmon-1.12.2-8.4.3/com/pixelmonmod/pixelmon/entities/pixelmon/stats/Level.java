package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.enums.ExperienceGroup;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class Level {
   protected DelegateLink pixelmon;
   public int expToNextLevel = 0;
   int oldLevel = -1;

   public Level(DelegateLink p) {
      this.pixelmon = p;
   }

   protected void updateStats() {
      this.pixelmon.updateStats();
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.func_74768_a("Level", this.getLevel());
      var1.func_74768_a("EXP", this.getExp());
      var1.func_74768_a("EXPToNextLevel", this.canLevelUp() ? this.getExpForLevel(this.getLevel() + 1) - this.getExpForLevel(this.getLevel()) : 0);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.setExp(var1.func_74762_e("EXP"));
      this.setLevel(var1.func_74762_e("Level"));
   }

   public int getLevel() {
      return this.pixelmon.getLevel();
   }

   public void setLevel(int i) {
      this.pixelmon.setLevel(i);
      this.updateExpToNextLevel();
      Stats stats = this.pixelmon.getStats();
      if (this.pixelmon.getHealth() != stats.hp && this.pixelmon.getHealth() != -1) {
         float oldHP = (float)stats.hp;
         float oldHealth = (float)this.pixelmon.getHealth();
         this.updateStats();
         float newHealth = (float)stats.hp;
         if (oldHP != 0.0F) {
            newHealth = oldHealth / oldHP * (float)stats.hp;
         }

         this.pixelmon.setHealthDirect((int)Math.ceil((double)newHealth));
      } else {
         this.updateStats();
         this.pixelmon.setHealthDirect(this.pixelmon.getStats().hp);
      }

   }

   public void updateExpToNextLevel() {
      if (this.getLevel() == 1) {
         this.expToNextLevel = this.getExpForLevel(this.getLevel() + 1);
      } else {
         this.expToNextLevel = this.canLevelUp() ? this.getExpForLevel(this.getLevel() + 1) - this.getExpForLevel(this.getLevel()) : 0;
      }

   }

   public int getExpForLevel(int l) {
      ExperienceGroup ex = this.pixelmon.getBaseStats().getExperienceGroup();
      if (ex == ExperienceGroup.Erratic) {
         if (l <= 50) {
            return (100 - l) * l * l * l / 50;
         } else if (l <= 68) {
            return (150 - l) * l * l * l / 100;
         } else if (l <= 98) {
            return (191 - l) * l * l * l / 150;
         } else {
            return l <= 100 ? (160 - l) * l * l * l / 100 : 60 * l * l * l / 100;
         }
      } else if (ex == ExperienceGroup.Fast) {
         return (int)(0.8 * (double)l * (double)l * (double)l);
      } else if (ex == ExperienceGroup.MediumFast) {
         return l * l * l;
      } else if (ex == ExperienceGroup.MediumSlow) {
         return l == 2 ? 9 : (int)(1.2 * (double)l * (double)l * (double)l - (double)(15 * l * l) + (double)(100 * l) - 140.0);
      } else if (ex == ExperienceGroup.Slow) {
         return (int)(1.25 * (double)l * (double)l * (double)l);
      } else if (ex == ExperienceGroup.Fluctuating) {
         if (l <= 15) {
            return (l + 73) * l * l * l / 150;
         } else {
            return l <= 36 ? (l + 14) * l * l * l / 50 : (l + 64) * l * l * l / 100;
         }
      } else {
         return -1;
      }
   }

   public int getExp() {
      return this.pixelmon.getExp();
   }

   public void setExp(int experience) {
      this.pixelmon.setExp(experience);
   }

   public boolean canLevelUp() {
      return this.getLevel() < PixelmonServerConfig.maxLevel;
   }

   protected void onLevelUp(PixelmonStatsData stats) {
      this.updateStats();
      this.pixelmon.updateLevelUp(stats);
      byte amount;
      if (this.pixelmon.getFriendship() < 100) {
         amount = 5;
      } else if (this.pixelmon.getFriendship() < 200) {
         amount = 3;
      } else {
         amount = 2;
      }

      this.pixelmon.adjustFriendship(amount + (this.pixelmon.getCaughtBall() == EnumPokeballs.LuxuryBall ? 1 : 0));
   }

   public void awardEXP(int experience) {
      this.awardEXP(experience, ExperienceGainType.UNKNOWN);
   }

   public void awardEXP(int experience, ExperienceGainType type) {
      if (this.pixelmon.doesLevel()) {
         if (type != ExperienceGainType.RARE_CANDY && this.pixelmon.hasOwner()) {
            EntityPlayerMP player = this.pixelmon.getPlayerOwner();
            if (player != null) {
               PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
               if (pps.getExpCharm().isActive()) {
                  experience = (int)((double)experience * PixelmonConfig.expCharmMultiplier);
               }
            }
         }

         ExperienceGainEvent event = new ExperienceGainEvent(this.pixelmon, experience, type);
         if (!Pixelmon.EVENT_BUS.post(event)) {
            experience = event.getExperience();
            this.setExp(this.getExp() + experience);
            EntityPlayerMP owner = this.pixelmon.getPlayerOwner();
            BattleControllerBase bc = this.pixelmon.getBattleController();
            if (owner != null && this.canLevelUp() && experience > 0) {
               TextComponentTranslation message = ChatHandler.getMessage("pixelmon.stats.gainexp", this.pixelmon.getRealNickname(), experience);
               ChatHandler.sendBattleMessage((Entity)owner, message);
            }

            if (this.canLevelUp() && this.expToNextLevel != -1) {
               if (owner != null) {
                  boolean isReplacingMove = false;
                  boolean didLevel = false;

                  while(this.getExp() >= this.expToNextLevel) {
                     int xp = this.getExp();
                     this.pixelmon.update(EnumUpdateType.Name);
                     int newExp = this.getExp() - this.expToNextLevel;
                     if (!this.canLevelUp() || Pixelmon.EVENT_BUS.post(new LevelUpEvent(owner, this.pixelmon, this.getLevel() + 1))) {
                        break;
                     }

                     didLevel = true;
                     PixelmonStatsData stats = PixelmonStatsData.createPacket(this.pixelmon);
                     this.setLevel(this.getLevel() + 1);
                     this.onLevelUp(stats);
                     this.setExp(newExp);
                     int newLevel = this.getLevel();
                     ArrayList newAttacks = this.pixelmon.getBaseStats().getMovesAtLevel(newLevel);
                     Iterator var13 = newAttacks.iterator();

                     while(var13.hasNext()) {
                        Attack a = (Attack)var13.next();
                        Moveset moveset = this.pixelmon.getMoveset();
                        if (!moveset.hasAttack(a)) {
                           if (moveset.size() >= 4) {
                              isReplacingMove = true;
                              LearnMoveController.sendLearnMove(owner, this.pixelmon.getPokemonUUID(), a.getActualMove(), true);
                           } else {
                              moveset.add(a);
                              this.pixelmon.update(EnumUpdateType.Moveset);
                              TextComponentTranslation message = ChatHandler.getMessage("pixelmon.stats.learnedmove", this.pixelmon.getRealNickname(), a.getActualMove().getTranslatedName());
                              if (bc != null) {
                                 ChatHandler.sendBattleMessage((Entity)owner, message);
                              } else {
                                 ChatHandler.sendChat(owner, message);
                              }
                           }
                        }
                     }

                     if (bc != null && bc.rules.levelCap < PixelmonServerConfig.maxLevel && bc.rules.levelCap >= newLevel) {
                        this.setExp(0);
                        break;
                     }
                  }

                  this.pixelmon.update(EnumUpdateType.Stats);
                  if (didLevel && !isReplacingMove) {
                     if (this.pixelmon.getBattleController() == null) {
                        this.pixelmon.pokemon.tryEvolution();
                     } else {
                        this.pixelmon.getBattleController().checkedPokemon.add(this.pixelmon.pokemon);
                     }
                  }

               }
            } else {
               this.setExp(0);
            }
         }
      }
   }

   private boolean checkForExistingEvolutionQuery() {
      synchronized(EvolutionQueryList.queryList) {
         for(int i = 0; i < EvolutionQueryList.queryList.size(); ++i) {
            if (((EvolutionQuery)EvolutionQueryList.queryList.get(i)).pokemonUUID.equals(this.pixelmon.getPokemonUUID())) {
               return true;
            }
         }

         return false;
      }
   }

   public void recalculateXP() {
      this.setExp(0);
      this.expToNextLevel = this.getExpForLevel(this.getLevel() + 1) - this.getExpForLevel(this.getLevel());
   }

   public int getExpForNextLevelClient() {
      if (this.oldLevel != this.getLevel()) {
         this.expToNextLevel = this.getExpForLevel(this.getLevel() + 1) - this.getExpForLevel(this.getLevel());
         this.oldLevel = this.getLevel();
      }

      return this.expToNextLevel;
   }

   public float getExpFraction() {
      return getExpFraction(this.getExp(), this.expToNextLevel);
   }

   public static float getExpFraction(int exp, int expToNextLevel) {
      return expToNextLevel == 0 ? 0.0F : (float)exp / (float)expToNextLevel;
   }
}
