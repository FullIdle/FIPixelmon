package com.pixelmonmod.pixelmon.entities.pokeballs;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureBase;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureBeastBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureDiveBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureDreamBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureDuskBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureFastBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureFriendBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureHealBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureHeavyBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureLevelBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureLoveBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureLureBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureLuxuryBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureMoonBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureNestBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureNetBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureQuickBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureRepeatBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureSafariBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureSportBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.captures.CaptureTimerBall;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PokeballTypeHelper {
   public static final List captureList;

   public static double getBallBonus(EnumPokeballs type, EntityLivingBase thrower, Pokemon p2, EnumPokeBallMode mode) {
      double ballBonus = type.getBallBonus();
      Iterator var6 = captureList.iterator();

      CaptureBase c;
      do {
         if (!var6.hasNext()) {
            return ballBonus;
         }

         c = (CaptureBase)var6.next();
      } while(c.pokeball != type);

      return c.getBallBonus(type, (EntityPlayer)thrower, p2, mode);
   }

   public static void doAfterEffect(EnumPokeballs type, EntityPixelmon p2) {
      captureList.stream().filter((c) -> {
         return c.pokeball == type;
      }).forEach((c) -> {
         c.doAfterEffect(type, p2);
      });
   }

   public static int modifyCaptureRate(EnumPokeballs type, Pokemon pixelmon, int captureRate) {
      Iterator var3 = captureList.iterator();

      CaptureBase c;
      do {
         if (!var3.hasNext()) {
            return captureRate;
         }

         c = (CaptureBase)var3.next();
      } while(c.pokeball != type);

      return c.modifyCaptureRate(pixelmon, captureRate);
   }

   static {
      ImmutableList.Builder builder = ImmutableList.builder();
      builder.add(new CaptureLoveBall());
      builder.add(new CaptureLevelBall());
      builder.add(new CaptureMoonBall());
      builder.add(new CaptureFriendBall());
      builder.add(new CaptureSafariBall());
      builder.add(new CaptureDiveBall());
      builder.add(new CaptureDuskBall());
      builder.add(new CaptureHealBall());
      builder.add(new CaptureLuxuryBall());
      builder.add(new CaptureNetBall());
      builder.add(new CaptureNestBall());
      builder.add(new CaptureHeavyBall());
      builder.add(new CaptureSportBall());
      builder.add(new CaptureQuickBall());
      builder.add(new CaptureLureBall());
      builder.add(new CaptureFastBall());
      builder.add(new CaptureTimerBall());
      builder.add(new CaptureRepeatBall());
      builder.add(new CaptureBeastBall());
      builder.add(new CaptureDreamBall());
      captureList = builder.build();
   }
}
