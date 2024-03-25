package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityOccupiedPokeball;
import com.pixelmonmod.pixelmon.storage.playerData.CaptureCombo;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.util.Tuple;

public class TransientData {
   public int eggTick = 0;
   public int lastEggX = 0;
   public int lastEggZ = 0;
   public int passiveHealTick = 0;
   public int lastPassiveHealX = 0;
   public int lastPassiveHealZ = 0;
   public int lureTick = 0;
   public int passivePoisonTick = 0;
   public int lastPassivePoisonHealX = 0;
   public int lastPassivePoisonHealZ = 0;
   public int highestLevel;
   public int lowestLevel;
   public int averageLevel;
   public EntityOccupiedPokeball thrownPokeball = null;
   public CaptureCombo captureCombo = new CaptureCombo();
   public final ArrayList onceBattleDone = new ArrayList();
   public final HashMap resourceQueries = new HashMap();
   public Tuple keyListener = null;
   private boolean blackFlute = false;
   private boolean whiteFlute = false;
   private long fluteLastPlayed = -1L;

   public boolean isBlackFluteActive(long time) {
      return this.blackFlute && time - this.fluteLastPlayed < (long)PixelmonConfig.lureFluteDuration * 1000L;
   }

   public boolean isWhiteFluteActive(long time) {
      return this.whiteFlute && time - this.fluteLastPlayed < (long)PixelmonConfig.lureFluteDuration * 1000L;
   }

   public void useBlackFlute(long time) {
      this.blackFlute = true;
      this.whiteFlute = false;
      this.fluteLastPlayed = time;
   }

   public void useWhiteFlute(long time) {
      this.blackFlute = false;
      this.whiteFlute = true;
      this.fluteLastPlayed = time;
   }
}
