package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.attackAnimations.VariableParticleEffect;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import java.util.ArrayList;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class Smelt {
   public static boolean instaCookForMedHighStrength = true;
   public static ArrayList highStrength = Lists.newArrayList(new String[]{"Inferno", "Burn Up", "Blue Flare"});
   public static ArrayList medStrength = Lists.newArrayList(new String[]{"Incinerate"});

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("smelt")).setName("pixelmon.moveskill.smelt.name").describe("pixelmon.moveskill.smelt.description1", "pixelmon.moveskill.smelt.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/smelt.png")).setDefaultCooldownTicks(2400).setUsePP(true).setRange(8).setAnyMoves("Incinerate", "Inferno", "Ember", "Burn Up", "Blue Flare");
      return moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         BlockPos pos = (BlockPos)tup.func_76341_a();
         IBlockState state = pixelmon.field_70170_p.func_180495_p(pos);
         if (!(state.func_177230_c() instanceof BlockFurnace)) {
            return -1;
         } else {
            TileEntity te = pixelmon.field_70170_p.func_175625_s(pos);
            if (!(te instanceof TileEntityFurnace)) {
               return -1;
            } else {
               TileEntityFurnace furnace = (TileEntityFurnace)te;
               int strength = 0;
               if (pixelmon.getPokemonData().getMoveset().hasAttack((String[])highStrength.toArray(new String[0]))) {
                  strength = 32;
               } else if (pixelmon.getPokemonData().getMoveset().hasAttack((String[])medStrength.toArray(new String[0]))) {
                  strength = 16;
               }

               int cooked = 0;

               while(strength > cooked++ && instaCookForMedHighStrength) {
                  furnace.func_145949_j();
               }

               furnace.func_174885_b(0, (32 + strength) * 20);
               pixelmon.field_70170_p.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + 0.5F), (double)pos.func_177952_p(), SoundEvents.field_187606_E, SoundCategory.BLOCKS, 0.2F, 1.0F);
               ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.smelt.use", pixelmon.func_145748_c_());
               (new VariableParticleEffect(pixelmon.field_71093_bK, AttackAnimationData.beam().setScale(0.25F).setPower(30))).setAttackBase((AttackBase)AttackBase.getAttackBaseFromEnglishName("Flamethrower").get()).setStartPosition((EntityLivingBase)pixelmon).setEndPosition(new Vec3d((double)furnace.func_174877_v().func_177958_n() + 0.5, (double)furnace.func_174877_v().func_177956_o() + 0.5, (double)furnace.func_174877_v().func_177952_p() + 0.5)).showAllWithin(20);
               BlockFurnace.func_176446_a(furnace.func_145950_i(), pixelmon.field_70170_p, furnace.func_174877_v());
               return moveSkill.cooldownTicks / (strength == 0 ? 2 : 1);
            }
         }
      });
   }
}
