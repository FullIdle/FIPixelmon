package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.NoTerrain;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UpdateTerrain;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UpdateWeather;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class GlobalStatusController {
   public BattleControllerBase bc;
   private ArrayList globalStatuses = new ArrayList();

   public GlobalStatusController(BattleControllerBase bc) {
      this.bc = bc;
   }

   public ArrayList getGlobalStatuses() {
      ArrayList effectiveStatuses = new ArrayList(this.globalStatuses.size());
      effectiveStatuses.addAll((Collection)this.globalStatuses.stream().filter((status) -> {
         return !status.isWeather() || !GlobalStatusBase.ignoreWeather(this.bc);
      }).collect(Collectors.toList()));
      return effectiveStatuses;
   }

   public Weather getWeather() {
      if (this.getWeatherIgnoreAbility() == null || GlobalStatusBase.ignoreWeather(this.bc) && !this.getWeatherIgnoreAbility().extreme) {
         return null;
      } else {
         Iterator var1 = this.globalStatuses.iterator();

         GlobalStatusBase g;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            g = (GlobalStatusBase)var1.next();
         } while(!g.isWeather());

         return (Weather)g;
      }
   }

   public Weather getWeatherIgnoreAbility() {
      Iterator var1 = this.globalStatuses.iterator();

      GlobalStatusBase g;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         g = (GlobalStatusBase)var1.next();
      } while(!g.isWeather());

      return (Weather)g;
   }

   public Terrain getTerrain() {
      Iterator var1 = this.globalStatuses.iterator();

      GlobalStatusBase g;
      do {
         if (!var1.hasNext()) {
            return new NoTerrain();
         }

         g = (GlobalStatusBase)var1.next();
      } while(!g.isTerrain());

      return (Terrain)g;
   }

   public boolean removeGlobalStatus(StatusType status) {
      for(int i = 0; i < this.globalStatuses.size(); ++i) {
         GlobalStatusBase g = (GlobalStatusBase)this.globalStatuses.get(i);
         if (g.type == status) {
            if (!this.bc.simulateMode) {
               this.globalStatuses.remove(i);
            }

            return true;
         }
      }

      return false;
   }

   public boolean removeGlobalStatuses(StatusType... statuses) {
      boolean hadStatus = false;
      StatusType[] var3 = statuses;
      int var4 = statuses.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StatusType status = var3[var5];
         hadStatus = this.removeGlobalStatus(status) || hadStatus;
      }

      return hadStatus;
   }

   public void removeGlobalStatus(GlobalStatusBase g) {
      if (!this.bc.simulateMode) {
         this.globalStatuses.remove(g);
         if (g.isWeather()) {
            this.triggerWeatherChange((Weather)null);
         }

         if (g.isTerrain()) {
            this.triggerTerrainChange((Terrain)null);
         }
      }

   }

   public void addGlobalStatus(GlobalStatusBase g) {
      if (!this.bc.simulateMode) {
         if (g.isWeather()) {
            this.globalStatuses.removeIf(GlobalStatusBase::isWeather);
            if (!GlobalStatusBase.ignoreWeather(this.bc) && this.bc.globalStatusController.canWeatherChange((Weather)g)) {
               this.triggerWeatherChange((Weather)g);
            }
         }

         if (g.isTerrain()) {
            this.triggerTerrainChange((Terrain)g);
         }

         this.globalStatuses.add(g);
      }

   }

   public GlobalStatusBase getGlobalStatus(int index) {
      return (GlobalStatusBase)this.globalStatuses.get(index);
   }

   public GlobalStatusBase getGlobalStatus(StatusType type) {
      Iterator var2 = this.globalStatuses.iterator();

      GlobalStatusBase g;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         g = (GlobalStatusBase)var2.next();
      } while(g.type != type);

      return g;
   }

   public int getGlobalStatusSize() {
      return this.globalStatuses.size();
   }

   public boolean hasStatus(StatusType type) {
      return this.getGlobalStatus(type) != null;
   }

   public void endBattle() {
      this.globalStatuses.clear();
   }

   public void triggerWeatherChange(Weather weather) {
      Iterator var2 = this.bc.getActiveUnfaintedPokemon().iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         pw.getBattleAbility().onWeatherChange(pw, weather);
      }

      this.bc.sendToPlayers(new UpdateWeather(weather == null ? null : weather.type));
   }

   public void triggerTerrainChange(Terrain terrain) {
      Iterator var2 = this.bc.getActiveUnfaintedPokemon().iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         pw.getUsableHeldItem().onTerrainSwitch(this.bc, pw, terrain);
         pw.getBattleAbility().onTerrainSwitch(this.bc, pw, terrain);
      }

      this.bc.sendToPlayers(new UpdateTerrain(terrain == null ? null : terrain.type));
   }

   public boolean canWeatherChange(Weather newWeather) {
      Weather weather = this.getWeather();
      if (weather == null) {
         return true;
      } else {
         return !weather.extreme || newWeather != null && newWeather.extreme;
      }
   }
}
