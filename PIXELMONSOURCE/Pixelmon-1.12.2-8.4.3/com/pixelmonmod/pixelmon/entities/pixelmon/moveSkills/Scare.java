package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemRepel;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Scare {
   public static float range = 30.0F;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("scare")).setName("pixelmon.moveskill.scare.name").describe("pixelmon.moveskill.scare.description1", "pixelmon.moveskill.scare.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/scare.png")).setUsePP(true).setAnyMoves("Mean Look", "Roar", "Scary Face").setIntrinsicSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("ab:Intimidate")})).setDefaultCooldownTicks(6000);
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         RepelHandler.applyRepel((EntityPlayerMP)pixelmon.func_70902_q(), ItemRepel.EnumRepel.SUPER_REPEL);
         List nearby = pixelmon.field_70170_p.func_175644_a(EntityPixelmon.class, (entity) -> {
            return entity.func_70032_d(pixelmon) <= range && entity.func_70902_q() == null;
         });
         nearby.forEach((pixelmon2) -> {
            pixelmon2.aggression = EnumAggression.passive;
            pixelmon2.resetAI();
         });
         pixelmon.func_184185_a(EnumSpecies.Charizard.getBaseStats().getSoundForGender(Gender.Male), 1.5F, 1.0F);
         ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.YELLOW, "pixelmon.moveskill.scare.onscare", pixelmon.func_145748_c_());
         return moveSkill.cooldownTicks;
      });
   }
}
