package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TerrainExamine;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurmy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class Entity5Battle extends Entity4Interactions {
   public BattleControllerBase battleController;
   private PixelmonWrapper pixelmonWrapper;
   public List relearnableEvolutionMoves = new ArrayList();

   public Entity5Battle(World par1World) {
      super(par1World);
   }

   public boolean func_70692_ba() {
      return this.battleController != null ? false : super.func_70692_ba();
   }

   public void startBattle(BattleParticipant p1, BattleParticipant p2) {
      this.startBattle(p1, p2, EnumBattleType.Single);
   }

   public void startBattle(BattleParticipant p1, BattleParticipant p2, EnumBattleType battleType) {
      this.startBattle(p1, p2, new BattleRules(battleType));
   }

   public void startBattle(BattleParticipant p1, BattleParticipant p2, BattleRules rules) {
      if (this.getPokemonData().getMoveset().isEmpty()) {
         this.getPokemonData().getMoveset().addAll(this.getSpecies().getBaseStats(this.getPokemonData().getFormEnum()).loadMoveset(this.getPokemonData().getLevel()));
      }

      BattleParticipant[] var4 = new BattleParticipant[]{p1, p2};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BattleParticipant p = var4[var6];
         if (p.countAblePokemon() <= 0) {
            return;
         }
      }

      try {
         p1.startedBattle = true;
         this.battleController = BattleRegistry.startBattle(new BattleParticipant[]{p1}, new BattleParticipant[]{p2}, rules);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void onEndBattle() {
      if (this.getBaseStats().getSpecies() == EnumSpecies.Burmy && this.pixelmonWrapper != null) {
         Iterator var1 = this.pixelmonWrapper.getOpponentPokemon().iterator();
         if (var1.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var1.next();
            this.setForm(EnumBurmy.getFromType(TerrainExamine.getTerrain(pw)));
         }
      }

      this.battleController = null;
      this.pixelmonWrapper = null;
   }

   @Nullable
   public NPCTrainer getTrainer() {
      EntityLivingBase owner = this.func_70902_q();
      return owner instanceof NPCTrainer ? (NPCTrainer)owner : null;
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (this.field_70170_p.field_72995_K) {
         return false;
      } else if ((this.getBossMode() != EnumBossMode.NotBoss || this.getPokemonData().getBonusStats().preventsCapture()) && source.field_76373_n.equals("mob") && !(source.func_76364_f() instanceof EntityPixelmon)) {
         return false;
      } else {
         if (source.field_76373_n.equals("player") || source.field_76373_n.equals("arrow")) {
            if (!PixelmonConfig.canPokemonBeHit || this.battleController != null || this.isEvolving()) {
               return false;
            }

            amount *= 3.0F;
         }

         if ((this.battleController != null || this.isEvolving()) && (source == DamageSource.field_76367_g || source == DamageSource.field_76369_e || source == DamageSource.field_76379_h || source == DamageSource.field_76372_a || source == DamageSource.field_76368_d || source == DamageSource.field_76371_c || source == DamageSource.field_76370_b || source == DamageSource.field_82729_p || source == DamageSource.field_82728_o || source == DamageSource.field_76376_m)) {
            return false;
         } else {
            boolean flag = super.func_70097_a(source, amount);
            if (this.battleController == null) {
               this.updateHealth();
            }

            if (this.func_110143_aJ() <= 0.0F) {
               this.func_70645_a(source);
            }

            Entity entity = source.func_76364_f();
            if (this.battleController == null && this.func_70902_q() != null) {
               this.update(new EnumUpdateType[]{EnumUpdateType.HP});
            }

            if (this.isValidTarget(entity)) {
               this.func_70624_b((EntityLiving)entity);
            }

            return flag;
         }
      }
   }

   public ArrayList getAttacksAtLevel(int level) {
      return this.getBaseStats().getMovesAtLevel(level);
   }

   public boolean learnsAttackAtLevel(int level) {
      return !this.getBaseStats().getMovesAtLevel(level).isEmpty();
   }

   protected boolean isValidTarget(Entity entity) {
      return entity instanceof EntityPixelmon;
   }

   public void func_70606_j(float par1) {
      super.func_70606_j(par1);
      if (this.battleController != null) {
         this.battleController.updatePokemonHealth();
      }

   }

   public BattleParticipant getParticipant() {
      if (this.battleController == null) {
         return null;
      } else {
         Iterator var1 = this.battleController.participants.iterator();

         while(var1.hasNext()) {
            BattleParticipant p = (BattleParticipant)var1.next();
            Iterator var3 = p.controlledPokemon.iterator();

            while(var3.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var3.next();
               if (pw.entity == this) {
                  return p;
               }
            }
         }

         return null;
      }
   }

   public PixelmonWrapper getPixelmonWrapper() {
      if (this.battleController == null) {
         return null;
      } else if (this.pixelmonWrapper != null && this.pixelmonWrapper.bc != null) {
         return this.pixelmonWrapper;
      } else {
         Iterator var1 = this.battleController.participants.iterator();

         while(true) {
            BattleParticipant bp;
            do {
               if (!var1.hasNext()) {
                  return null;
               }

               bp = (BattleParticipant)var1.next();
            } while(bp.controlledPokemon == null);

            Iterator var3 = bp.controlledPokemon.iterator();

            while(var3.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var3.next();
               if (pw.entity == this) {
                  this.pixelmonWrapper = pw;
                  return pw;
               }
            }
         }
      }
   }

   public void setPixelmonWrapper(PixelmonWrapper newWrapper) {
      this.pixelmonWrapper = newWrapper;
   }

   public int getPartyPosition() {
      int partyPosition = super.getPartyPosition();
      if (partyPosition == -1) {
         PixelmonWrapper pw = this.getPixelmonWrapper();
         if (pw != null) {
            partyPosition = pw.getPartyPosition();
         }
      }

      return partyPosition;
   }
}
