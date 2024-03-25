package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureNetBall extends CaptureBase {
   public CaptureNetBall() {
      super(EnumPokeballs.NetBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      PixelmonWrapper pw = p2.getPixelmonWrapperIfExists();
      return !p2.getBaseStats().getTypeList().contains(EnumType.Bug) && !p2.getBaseStats().getTypeList().contains(EnumType.Water) && (pw == null || !pw.hasType(EnumType.Bug, EnumType.Water)) ? 1.0 : 3.5;
   }
}
