package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureFriendBall extends CaptureBase {
   public CaptureFriendBall() {
      super(EnumPokeballs.FriendBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return type.getBallBonus();
   }

   public void doAfterEffect(EnumPokeballs type, EntityPixelmon p) {
      p.getPokemonData().setFriendship(200);
      p.update(new EnumUpdateType[]{EnumUpdateType.Friendship});
   }
}
