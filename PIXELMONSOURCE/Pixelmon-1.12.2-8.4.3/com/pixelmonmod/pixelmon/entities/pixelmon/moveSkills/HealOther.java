package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.attackAnimations.VariableParticleEffect;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class HealOther {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("heal_other")).setName("pixelmon.moveskill.heal_other.name").describe("pixelmon.moveskill.heal_other.description").setAnyMoves("Soft-Boiled", "Milk Drink").setUsePP(true).setRange(7).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/heal.png")).setDefaultCooldownTicks(300);
      return moveSkill.setBehaviourPokemonTarget((pixelmon, target) -> {
         if (target.battleController != null) {
            return -1;
         } else if (target.func_110143_aJ() >= (float)target.getPokemonData().getMaxHealth()) {
            return -1;
         } else {
            float healAmount = (float)pixelmon.getPokemonData().getMaxHealth() / 5.0F;
            target.func_70691_i(healAmount);
            (new VariableParticleEffect(pixelmon.field_71093_bK, AttackAnimationData.beam().setSegments(10).setVariation(0.7F).setInverted(true).setSpeed(0.3F).setScale(0.4F).setPower(30).setTexture(AttackEffect.EnumParticleTexture.NORMAL))).setStartPosition((EntityLivingBase)target).setEndPosition((EntityLivingBase)pixelmon).setAttackBase((AttackBase)AttackBase.getAttackBaseFromEnglishName("Mega Drain").get()).showAllWithin(20);
            return moveSkill.cooldownTicks;
         }
      });
   }
}
