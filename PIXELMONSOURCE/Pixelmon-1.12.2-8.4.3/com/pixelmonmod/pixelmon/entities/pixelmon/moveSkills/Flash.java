package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.attackAnimations.VariableParticleEffect;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTemporaryLight;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class Flash {
   public static int durationTicks = 600;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("flash")).setName("pixelmon.moveskill.flash.name").describe("pixelmon.moveskill.flash.description1", "pixelmon.moveskill.flash.description2").setDefaultCooldownTicks(600).setUsePP(true).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/flash.png")).setAnyMoves("Flash").setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("ab:Illuminate")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         BlockPos pos = pixelmon.func_180425_c().func_177982_a(0, 1, 0);
         if (pixelmon.field_70170_p.func_175623_d(pos)) {
            pixelmon.field_70170_p.func_175656_a(pos, PixelmonBlocks.temporaryLight.func_176223_P());
            TileEntity tileEntity = pixelmon.field_70170_p.func_175625_s(pos);
            if (tileEntity != null && tileEntity instanceof TileEntityTemporaryLight) {
               TileEntityTemporaryLight light = (TileEntityTemporaryLight)tileEntity;
               light.durationTicks = durationTicks;
               pixelmon.func_184185_a(SoundEvents.field_187649_bu, 1.0F, 1.0F);
               (new VariableParticleEffect(pixelmon.field_71093_bK, AttackAnimationData.explosion().setScale(3.0F).setPower(30).setLifetimeTicks(40))).setAttackBase((AttackBase)AttackBase.getAttackBaseFromEnglishName("Explosion").get()).setStartPosition((EntityLivingBase)pixelmon).showAllWithin(20);
            }
         }

         return moveSkill.cooldownTicks;
      });
   }
}
