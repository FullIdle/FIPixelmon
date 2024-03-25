package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.items.ItemHirokusLens;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

public class AnalysePokemon {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("analyse_pokemon")).setName("pixelmon.moveskill.analyse_pokemon.name").describe("pixelmon.moveskill.analyse_pokemon.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/analysepokemon.png")).setAnyMoves("Foresight", "Mind Reader").setDefaultCooldownTicks(2400).setRange(10).setUsePP(true);
      return moveSkill.setBehaviourPokemonTarget((pixelmon, target) -> {
         if (target.func_70902_q() != null) {
            return -1;
         } else {
            target.exposeInfo((EntityPlayerMP)pixelmon.func_70902_q());
            ChatHandler.sendFormattedChat(pixelmon.func_70902_q(), TextFormatting.WHITE, "pixelmon.moveskill.analyse_pokemon.use", target.func_145748_c_());
            pixelmon.field_70170_p.func_184148_a((EntityPlayer)null, pixelmon.field_70165_t, pixelmon.field_70163_u + 1.0, pixelmon.field_70161_v, ItemHirokusLens.sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return moveSkill.cooldownTicks;
         }
      });
   }
}
