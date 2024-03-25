package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;

public class WildPixelmonParticipant extends BattleParticipant {
   private boolean isBlockBattleParticipant = false;
   private static int idCounter = 0;

   public WildPixelmonParticipant(boolean isGrassBattleParticipant, EntityPixelmon... pixelmon) {
      super(pixelmon.length);
      this.init(pixelmon);
      this.isBlockBattleParticipant = isGrassBattleParticipant;
   }

   public WildPixelmonParticipant(EntityPixelmon... pixelmon) {
      super(pixelmon.length);
      this.init(pixelmon);
   }

   private void init(EntityPixelmon[] pixelmon) {
      this.allPokemon = new PixelmonWrapper[pixelmon.length];

      for(int i = 0; i < pixelmon.length; ++i) {
         PixelmonWrapper pw = new PixelmonWrapper(this, pixelmon[i], i);
         this.allPokemon[i] = pw;
         this.controlledPokemon.add(pw);
      }

   }

   public ParticipantType getType() {
      return ParticipantType.WildPokemon;
   }

   public boolean hasMorePokemon() {
      return this.countAblePokemon() == 1;
   }

   public boolean hasMorePokemonReserve() {
      return false;
   }

   public boolean canGainXP() {
      return false;
   }

   public void startBattle(BattleControllerBase bc) {
      super.startBattle(bc);
      boolean isBoss = false;
      Iterator var3 = this.controlledPokemon.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         EnumBossMode bossMode = pw.entity.getBossMode();
         if (bossMode != EnumBossMode.NotBoss) {
            isBoss = true;
            int lvl = 1;
            Iterator var7 = bc.participants.iterator();

            while(var7.hasNext()) {
               BattleParticipant p = (BattleParticipant)var7.next();
               if (p.team != this.team && p instanceof PlayerParticipant) {
                  int highestLevel = ((PlayerParticipant)p).getHighestLevel();
                  lvl = Math.max(lvl, highestLevel);
               }
            }

            lvl += bossMode.getExtraLevels();
            Stats stats = pw.getStats();
            stats.ivs.maximizeIVs();
            stats.evs.randomizeMaxEVs();
            pw.setTempLevel(lvl);
         }

         pw.setHealth(pw.getMaxHealth());
         pw.getMoveset().forEach((attack) -> {
            if (attack != null) {
               attack.pp = attack.getMaxPP();
            }

         });
      }

      this.setBattleAI(isBoss ? PixelmonConfig.battleAIBoss.createAI(this) : PixelmonConfig.battleAIWild.createAI(this));
   }

   public void endBattle(EnumBattleEndCause cause) {
      Iterator var2 = this.controlledPokemon.iterator();

      while(true) {
         PixelmonWrapper pw;
         do {
            if (!var2.hasNext()) {
               return;
            }

            pw = (PixelmonWrapper)var2.next();
            pw.resetBattleEvolution();
            pw.entity.onEndBattle();
         } while(!pw.isFainted() && !this.shouldDespawn() && !this.isBlockBattleParticipant);

         pw.entity.func_70106_y();
      }
   }

   private boolean shouldDespawn() {
      if (PixelmonConfig.despawnOnFleeOrLoss) {
         if (this.bc.rules.battleType != EnumBattleType.Single) {
            return true;
         }

         if (this.bc.getOpponents(this).get(0) instanceof PlayerParticipant) {
            return true;
         }
      }

      return false;
   }

   public void getNextPokemon(int position) {
   }

   public UUID getNextPokemonUUID() {
      return null;
   }

   public TextComponentBase getName() {
      String key = "";
      if (!this.controlledPokemon.isEmpty()) {
         key = "pixelmon." + ((PixelmonWrapper)this.controlledPokemon.get(0)).entity.getLocalizedName().toLowerCase() + ".name";
      }

      return new TextComponentTranslation(key, new Object[0]);
   }

   public MoveChoice getMove(PixelmonWrapper pw) {
      if (this.bc == null) {
         return null;
      } else if (!pw.getMoveset().isEmpty()) {
         return this.getBattleAI().getNextMove(pw);
      } else {
         this.bc.setFlee(pw.getPokemonUUID());
         return null;
      }
   }

   public PixelmonWrapper switchPokemon(PixelmonWrapper pw, UUID newPixelmonUUID) {
      return null;
   }

   public boolean checkPokemon() {
      boolean allGood = true;
      Iterator var2 = this.controlledPokemon.iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         if (pw.getMoveset().isEmpty()) {
            pw.entity.getPokemonData().getMoveset().clear();
            pw.entity.getPokemonData().getMoveset().addAll(pw.entity.getPokemonData().getBaseStats().loadMoveset(pw.getLevelNum()));
            if (pw.getMoveset().isEmpty()) {
               if (PixelmonConfig.printErrors) {
                  Pixelmon.LOGGER.info("Couldn't load " + pw.entity.getLocalizedName() + "'s moves.");
               }

               allGood = false;
            }
         }
      }

      return allGood;
   }

   public void updatePokemon(PixelmonWrapper pw) {
   }

   public EntityLiving getEntity() {
      return this.controlledPokemon.isEmpty() ? null : ((PixelmonWrapper)this.controlledPokemon.get(0)).entity;
   }

   public void updateOtherPokemon() {
   }

   public void setPosition(double[] ds) {
   }

   public boolean canDynamax() {
      return false;
   }

   public String getDisplayName() {
      Iterator var1 = this.controlledPokemon.iterator();
      if (var1.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var1.next();
         return pw.getNickname();
      } else {
         return super.getDisplayName();
      }
   }
}
