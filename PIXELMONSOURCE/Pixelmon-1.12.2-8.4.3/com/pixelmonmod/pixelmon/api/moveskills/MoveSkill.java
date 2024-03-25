package com.pixelmonmod.pixelmon.api.moveskills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.moveskills.MoveSkillCooldownEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.moveskills.RegisterMoveSkillPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;

public class MoveSkill {
   public static final ArrayList moveSkills = new ArrayList();
   public String id;
   public String name;
   public ArrayList descriptions = new ArrayList();
   public ResourceLocation sprite;
   public int cooldownTicks = 100;
   public int range = 3;
   public ArrayList anyMoves = new ArrayList();
   public ArrayList combinationMoves = new ArrayList();
   public ArrayList ableSpecs = new ArrayList();
   public ArrayList intrinsicSpecs = new ArrayList();
   public boolean usePP = false;
   public Map behaviours = new HashMap();

   public static boolean isMoveSkill(String moveSkillID) {
      return getMoveSkillByID(moveSkillID) != null;
   }

   public static MoveSkill getMoveSkillByID(String moveSkillID) {
      return (MoveSkill)CollectionHelper.find(moveSkills, (moveSkill) -> {
         return moveSkill.id.equalsIgnoreCase(moveSkillID);
      });
   }

   public static List getMoveSkills(Pokemon pokemon) {
      ArrayList moveSkills = Lists.newArrayList(MoveSkill.moveSkills);
      moveSkills.removeIf((moveSkill) -> {
         return !moveSkill.hasMoveSkill(pokemon);
      });
      return moveSkills;
   }

   public MoveSkill(String id) {
      this.id = id;
      this.name = id;
   }

   public MoveSkill describe(String... descriptions) {
      Collections.addAll(this.descriptions, descriptions);
      return this;
   }

   public void onUsed(EntityPixelmon pixelmon, Object data, EnumTargetType targetType) {
      if (targetType != MoveSkill.EnumTargetType.PLAYER || data instanceof EntityPlayerMP) {
         if (targetType != MoveSkill.EnumTargetType.POKEMON || data instanceof EntityPixelmon) {
            if (targetType != MoveSkill.EnumTargetType.MISC_ENTITY || data instanceof EntityLivingBase) {
               if (targetType != MoveSkill.EnumTargetType.BLOCK || data instanceof Tuple) {
                  BiFunction cooldown = (BiFunction)this.behaviours.getOrDefault(targetType, (p, d) -> {
                     return -1;
                  });
                  UseMoveSkillEvent event = new UseMoveSkillEvent(pixelmon, this, data);
                  if (this.usePP && !this.checkForPP(pixelmon.getPokemonData())) {
                     ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.RED, "pixelmon.moveskill.nopp", pixelmon.func_145748_c_());
                  } else {
                     if (!Pixelmon.EVENT_BUS.post(event)) {
                        int cooldownTicks = (Integer)cooldown.apply(pixelmon, data);
                        if (cooldownTicks > 0) {
                           if (this.usePP) {
                              this.takePP(pixelmon.getPokemonData());
                           }

                           MoveSkillCooldownEvent cooldownEvent = new MoveSkillCooldownEvent(pixelmon, this, cooldownTicks);
                           if (event.handler != null) {
                              event.handler.accept(cooldownEvent);
                           } else if (Pixelmon.EVENT_BUS.post(cooldownEvent) || cooldownEvent.cooldownTicks <= 0) {
                              return;
                           }

                           this.applyCooldown(pixelmon.getPokemonData(), cooldownEvent.cooldownTicks);
                        }
                     }

                  }
               }
            }
         }
      }
   }

   public void applyCooldown(Pokemon pokemon, int cooldownTicks) {
      if (cooldownTicks < 1) {
         cooldownTicks = 1;
      }

      pokemon.setMoveSkillCooldown(this, cooldownTicks);
   }

   public boolean hasMoveSkill(Pokemon pokemon) {
      if (!this.intrinsicSpecs.isEmpty() && CollectionHelper.find(this.intrinsicSpecs, (spec) -> {
         return spec.matches(pokemon);
      }) != null) {
         return true;
      } else if (!this.ableSpecs.isEmpty() && CollectionHelper.find(this.ableSpecs, (spec) -> {
         return spec.matches(pokemon);
      }) == null) {
         return false;
      } else if (!this.combinationMoves.isEmpty() && CollectionHelper.find(this.combinationMoves, (moveID) -> {
         try {
            return !pokemon.getMoveset().hasAttack(new Attack(moveID));
         } catch (Exception var3) {
            return true;
         }
      }) != null) {
         return false;
      } else {
         return this.anyMoves.isEmpty() || CollectionHelper.find(this.anyMoves, (moveID) -> {
            try {
               return pokemon.getMoveset().hasAttack(new Attack(moveID));
            } catch (Exception var3) {
               return false;
            }
         }) != null;
      }
   }

   public MoveSkill setName(String name) {
      this.name = name;
      return this;
   }

   public MoveSkill setDefaultCooldownTicks(int cooldownTicks) {
      this.cooldownTicks = cooldownTicks;
      return this;
   }

   public MoveSkill setRange(int range) {
      this.range = range;
      return this;
   }

   public MoveSkill setIcon(ResourceLocation resourceLocation) {
      this.sprite = resourceLocation;
      return this;
   }

   public MoveSkill setAnyMoves(String... anyMoves) {
      this.anyMoves = new ArrayList();
      String[] var2 = anyMoves;
      int var3 = anyMoves.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String anyMove = var2[var4];
         AttackBase ab = (AttackBase)AttackBase.getAttackBase(anyMove).orElse((Object)null);
         if (ab != null) {
            this.anyMoves.add(ab.getAttackId());
         }
      }

      return this;
   }

   public MoveSkill setAnyMoves(ArrayList anyMoves) {
      return this.setAnyMoves((List)anyMoves.stream().map(AttackBase::getAttackId).collect(Collectors.toList()));
   }

   public MoveSkill setAnyMoves(List anyMoves) {
      this.anyMoves = Lists.newArrayList(anyMoves);
      return this;
   }

   public MoveSkill setCombinationMoves(String... combinationMoves) {
      this.combinationMoves = new ArrayList();
      String[] var2 = combinationMoves;
      int var3 = combinationMoves.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String combinationMove = var2[var4];
         AttackBase ab = (AttackBase)AttackBase.getAttackBase(combinationMove).orElse((Object)null);
         if (ab != null) {
            this.combinationMoves.add(ab.getAttackId());
         }
      }

      return this;
   }

   public MoveSkill setCombinationMoves(ArrayList combinationMoves) {
      return this.setCombinationMoves((List)combinationMoves.stream().map(AttackBase::getAttackId).collect(Collectors.toList()));
   }

   public MoveSkill setCombinationMoves(List combinationMoves) {
      this.combinationMoves = Lists.newArrayList(combinationMoves);
      return this;
   }

   public MoveSkill setAbleSpecs(ArrayList specs) {
      this.ableSpecs = specs;
      return this;
   }

   public MoveSkill setIntrinsicSpecs(ArrayList specs) {
      this.intrinsicSpecs = specs;
      return this;
   }

   public MoveSkill setBehaviourNoTarget(Function cooldown) {
      this.behaviours.put(MoveSkill.EnumTargetType.NOTHING, (pixelmon, Void) -> {
         return (Integer)cooldown.apply(pixelmon);
      });
      return this;
   }

   public MoveSkill setBehaviourPokemonTarget(BiFunction cooldown) {
      this.behaviours.put(MoveSkill.EnumTargetType.POKEMON, (user, target) -> {
         return (Integer)cooldown.apply(user, (EntityPixelmon)target);
      });
      return this;
   }

   public MoveSkill setBehaviourBlockTarget(BiFunction cooldown) {
      this.behaviours.put(MoveSkill.EnumTargetType.BLOCK, (pixelmon, tup) -> {
         return (Integer)cooldown.apply(pixelmon, (Tuple)tup);
      });
      return this;
   }

   public MoveSkill setBehaviourPlayerTarget(BiFunction cooldown) {
      this.behaviours.put(MoveSkill.EnumTargetType.PLAYER, (pixelmon, playerTarget) -> {
         return (Integer)cooldown.apply(pixelmon, (EntityPlayerMP)playerTarget);
      });
      return this;
   }

   public MoveSkill setBehaviourMultiTarget(BiFunction cooldown, EnumTargetType... targetTypes) {
      EnumTargetType[] var3 = targetTypes;
      int var4 = targetTypes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumTargetType targetType = var3[var5];
         this.behaviours.put(targetType, cooldown);
      }

      return this;
   }

   public MoveSkill setUsePP(boolean usePP) {
      this.usePP = usePP;
      return this;
   }

   public boolean hasTargetType(EnumTargetType targetType) {
      return this.behaviours.keySet().contains(targetType);
   }

   public boolean checkForPP(Pokemon pokemon) {
      if (!this.intrinsicSpecs.isEmpty() && CollectionHelper.find(this.intrinsicSpecs, (spec) -> {
         return spec.matches(pokemon);
      }) != null) {
         return true;
      } else if (this.anyMoves.isEmpty() && this.combinationMoves.isEmpty()) {
         return true;
      } else {
         Moveset moveset = pokemon.getMoveset();
         Iterator var3 = this.combinationMoves.iterator();

         boolean had;
         do {
            Integer moveID;
            if (!var3.hasNext()) {
               if (!this.combinationMoves.isEmpty()) {
                  return true;
               }

               var3 = this.anyMoves.iterator();

               while(var3.hasNext()) {
                  moveID = (Integer)var3.next();
                  Iterator var8 = moveset.iterator();

                  while(var8.hasNext()) {
                     Attack attack = (Attack)var8.next();
                     if (attack.getActualMove().getAttackId() == moveID && attack.pp > 0) {
                        return true;
                     }
                  }
               }

               return false;
            }

            moveID = (Integer)var3.next();
            had = false;
            Iterator var6 = moveset.iterator();

            while(var6.hasNext()) {
               Attack attack = (Attack)var6.next();
               if (attack.getActualMove().getAttackId() == moveID) {
                  if (attack.pp <= 0) {
                     return false;
                  }

                  had = true;
               }
            }
         } while(had);

         return false;
      }
   }

   public void takePP(Pokemon pokemon) {
      if (this.intrinsicSpecs.isEmpty() || CollectionHelper.find(this.intrinsicSpecs, (spec) -> {
         return spec.matches(pokemon);
      }) == null) {
         if (!this.anyMoves.isEmpty() || !this.combinationMoves.isEmpty()) {
            Moveset moveset = pokemon.getMoveset();
            Iterator var3 = this.combinationMoves.iterator();

            while(true) {
               Integer moveID;
               Iterator var5;
               Attack attack;
               while(var3.hasNext()) {
                  moveID = (Integer)var3.next();
                  var5 = moveset.iterator();

                  while(var5.hasNext()) {
                     attack = (Attack)var5.next();
                     if (attack.getActualMove().getAttackId() == moveID && attack.pp > 0) {
                        --attack.pp;
                        break;
                     }
                  }
               }

               if (!this.combinationMoves.isEmpty()) {
                  pokemon.markDirty(EnumUpdateType.Moveset);
                  return;
               }

               var3 = this.anyMoves.iterator();

               while(var3.hasNext()) {
                  moveID = (Integer)var3.next();
                  var5 = moveset.iterator();

                  while(var5.hasNext()) {
                     attack = (Attack)var5.next();
                     if (attack.getActualMove().getAttackId() == moveID && attack.pp > 0) {
                        --attack.pp;
                        pokemon.markDirty(EnumUpdateType.Moveset);
                        return;
                     }
                  }
               }

               return;
            }
         }
      }
   }

   public void register(EntityPlayerMP player) {
      RegisterMoveSkillPacket packet = new RegisterMoveSkillPacket(this, false);
      Pixelmon.network.sendTo(packet, player);
   }

   public void unregister(EntityPlayerMP player) {
      RegisterMoveSkillPacket packet = new RegisterMoveSkillPacket(this, true);
      Pixelmon.network.sendTo(packet, player);
   }

   public static enum EnumTargetType {
      NOTHING,
      BLOCK,
      POKEMON,
      PLAYER,
      MISC_ENTITY;
   }
}
