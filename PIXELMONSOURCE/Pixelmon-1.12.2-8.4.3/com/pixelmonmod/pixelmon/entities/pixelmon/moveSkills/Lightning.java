package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class Lightning {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("lightning_bolt")).setName("pixelmon.moveskill.lightning_bolt.name").describe("pixelmon.moveskill.lightning_bolt.description").setDefaultCooldownTicks(500).setRange(7).setUsePP(true).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/lightning.png")).setAnyMoves("Thunderbolt", "Bolt Strike", "Thunder", "Spark", "Discharge", "Fusion Bolt", "Thunder Shock");
      return moveSkill.setBehaviourMultiTarget((pixelmon, data) -> {
         EntityPlayerMP player = (EntityPlayerMP)pixelmon.func_70902_q();
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else {
            WorldServer world = (WorldServer)pixelmon.field_70170_p;
            BlockPos pos;
            if (data instanceof EntityLivingBase) {
               pos = ((EntityLivingBase)data).func_180425_c();
            } else {
               pos = (BlockPos)((Tuple)data).func_76341_a();
            }

            if (canLightningStrike(world, pos) && !world.func_73046_m().func_175579_a(world, pos, player)) {
               world.func_72942_c(new EntityLightningBolt(world, (double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), false));
               return moveSkill.cooldownTicks - pixelmon.getPokemonData().getStat(StatsType.Speed);
            } else {
               return -1;
            }
         }
      }, MoveSkill.EnumTargetType.BLOCK, MoveSkill.EnumTargetType.MISC_ENTITY);
   }

   private static boolean canLightningStrike(World world, BlockPos strikePosition) {
      Biome Biome = world.func_180494_b(strikePosition);
      return !Biome.func_76746_c() && !world.func_175708_f(strikePosition, false) && Biome.func_76738_d();
   }
}
