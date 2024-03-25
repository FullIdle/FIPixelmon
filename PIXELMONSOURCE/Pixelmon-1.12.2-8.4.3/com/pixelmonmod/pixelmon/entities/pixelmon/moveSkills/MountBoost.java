package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.util.Scheduling;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class MountBoost {
   public static int durationSeconds = 15;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("mount_boost")).setName("pixelmon.moveskill.mount_boost.name").describe("pixelmon.moveskill.mount_boost.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mountboost.png")).setDefaultCooldownTicks(1200).setUsePP(true).setAnyMoves("Extreme Speed", "Agility").setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("Garchomp"), new PokemonSpec("Dragonite"), new PokemonSpec("Latios"), new PokemonSpec("Rapidash"), new PokemonSpec("Arcanine")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         EntityPlayerMP player = (EntityPlayerMP)pixelmon.func_70902_q();
         if (player == null) {
            return -1;
         } else if (pixelmon.func_184179_bs() == null) {
            ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.moveskill.mount_boost.notriding", pixelmon.func_145748_c_());
            return -1;
         } else {
            pixelmon.mountBoost *= 2.0F;
            pixelmon.func_184185_a(SoundEvents.field_187616_bj, 1.5F, 1.0F);
            Scheduling.schedule(20 * durationSeconds, () -> {
               pixelmon.mountBoost /= 2.0F;
               if (!pixelmon.field_70128_L) {
                  pixelmon.func_184185_a(SoundEvents.field_187524_aN, 0.5F, 1.0F);
               }

            }, false);
            return moveSkill.cooldownTicks - 20 * (int)Math.min(20.0F, (float)pixelmon.getPokemonData().getStat(StatsType.Speed) / 10.0F);
         }
      });
   }
}
