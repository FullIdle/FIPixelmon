package com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OverlayNew extends OverlayBase {
   private static final ResourceLocation ACTIVE = new ResourceLocation("pixelmon", "textures/gui/battle/pokemon_active.png");
   private final List opponents = Lists.newLinkedList();
   private final List allies = Lists.newLinkedList();
   private final List our = Lists.newLinkedList();
   private PixelmonInGui current;

   public OverlayNew(GuiBattle parent) {
      super(parent);
      this.current = this.bm.getCurrentPokemon();
   }

   public void draw(int width, int height, int guiWidth, int guiHeight) {
      try {
         this.bm.processBattleTasks();
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      if (this.bm.displayedOurPokemon != null && this.bm.displayedOurPokemon.length != 0) {
         GlStateManager.func_179141_d();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179147_l();
         this.generateElements();
         float scaleOpponents;
         float wa;
         int i;
         if (this.bm.rules.battleType == EnumBattleType.Raid && this.opponents.size() == 1) {
            scaleOpponents = 1.0F;
            if ((float)this.parent.field_146294_l < 700.0F) {
               scaleOpponents = 1.0F / (new BigDecimal((double)(700.0F / (float)this.parent.field_146294_l))).setScale(2, RoundingMode.HALF_UP).floatValue();
            }

            OpponentElement opponent = (OpponentElement)this.opponents.get(0);
            i = opponent.getEnemy().xPos;
            float center = (float)this.parent.field_146294_l / 2.0F;
            float offset = 125.0F * scaleOpponents;
            if (this.parent.showTargeting() && this.parent.isTargeted(opponent.getEnemy().pokemonUUID)) {
               opponent.drawSelected((int)((center - offset) * (1.0F / scaleOpponents)), i, 250, 70, scaleOpponents);
            }

            opponent.drawElementScaled((int)((center - offset) * (1.0F / scaleOpponents)), i, 250, 70, scaleOpponents, 0, this, this.particleEngine);
         } else {
            scaleOpponents = (float)this.parent.field_146294_l;
            wa = 1.0F;
            if (scaleOpponents < (float)(180 * this.bm.displayedEnemyPokemon.length)) {
               wa = 1.0F / (new BigDecimal((double)(180.0F * (float)this.bm.displayedEnemyPokemon.length / scaleOpponents))).setScale(2, RoundingMode.HALF_UP).floatValue();
            }

            for(i = 0; i < this.opponents.size(); ++i) {
               OpponentElement opponent = (OpponentElement)this.opponents.get(i);
               int yPos = opponent.getEnemy().xPos;
               if (this.parent.showTargeting() && this.parent.isTargeted(opponent.getEnemy().pokemonUUID)) {
                  opponent.drawSelected(180 * i, yPos, 160, 50, wa);
               }

               opponent.drawElementScaled(180 * i, yPos, 160, 50, wa, i, this, this.particleEngine);
            }
         }

         PixelmonInGui pig = this.current;
         if (pig == null) {
            if (!this.bm.isSpectating) {
               return;
            }

            pig = this.bm.displayedOurPokemon[0];
         }

         wa = (float)this.parent.field_146294_l;
         float scaleAllies = 1.0F;
         if (wa < (float)(190 * this.our.size())) {
            scaleAllies = 1.0F / (new BigDecimal((double)(190.0F * (float)this.our.size() / wa))).setScale(2, RoundingMode.HALF_UP).floatValue();
         }

         for(int i = 0; i < this.our.size(); ++i) {
            AllyElement our = (AllyElement)this.our.get(i);
            int yPos = our.getAlly().xPos;
            if (this.parent.showTargeting() && this.parent.isTargeted(our.getAlly().pokemonUUID)) {
               our.drawSelected(this.parent.field_146294_l + 25 - 190 * (i + 1), this.parent.field_146295_m - 6 - yPos, 160, 40, scaleAllies);
            }

            our.drawElementScaled(this.parent.field_146294_l + 25 - 190 * (i + 1), this.parent.field_146295_m - 6 - yPos, 160, 40, scaleAllies, i, this, this.particleEngine);
         }

         this.particleEngine.draw("");
      }
   }

   public int mouseOverEnemyPokemon(int guiWidth, int guiHeight, int mouseX, int mouseY) {
      for(int i = 0; i < this.opponents.size(); ++i) {
         OpponentElement opponent = (OpponentElement)this.opponents.get(i);
         if (opponent.isMouseOver(mouseX, mouseY)) {
            return i;
         }
      }

      return -1;
   }

   public int mouseOverUserPokemon(int width, int height, int guiWidth, int guiHeight, int mouseX, int mouseY) {
      int i = 0;

      Iterator var8;
      AllyElement ally;
      for(var8 = this.allies.iterator(); var8.hasNext(); ++i) {
         ally = (AllyElement)var8.next();
         if (ally.isMouseOver(mouseX, mouseY)) {
            return Lists.newArrayList(this.bm.teamPokemon).indexOf(ally.getAlly().pokemonUUID);
         }
      }

      i = 0;

      for(var8 = this.our.iterator(); var8.hasNext(); ++i) {
         ally = (AllyElement)var8.next();
         if (ally.isMouseOver(mouseX, mouseY)) {
            return Lists.newArrayList(this.bm.teamPokemon).indexOf(ally.getAlly().pokemonUUID);
         }
      }

      return -1;
   }

   private void generateElements() {
      if (this.bm.getCurrentPokemon() != null) {
         this.current = this.bm.getCurrentPokemon();
      }

      this.opponents.clear();
      if (this.bm.displayedEnemyPokemon != null) {
         for(int i = 0; i < this.bm.displayedEnemyPokemon.length; ++i) {
            PixelmonInGui enemy = this.bm.displayedEnemyPokemon[i];
            if (this.bm.rules.battleType == EnumBattleType.Raid) {
               this.opponents.add(new RaidElement(enemy, this, this.particleEngine));
            } else {
               this.opponents.add(new OpponentElement(enemy, this, this.particleEngine));
            }
         }
      }

      this.our.clear();
      this.allies.clear();
      Set added = new HashSet();
      PixelmonInGui ally;
      int i;
      if (this.bm.displayedOurPokemon != null) {
         for(i = 0; i < this.bm.displayedOurPokemon.length; ++i) {
            ally = this.bm.displayedOurPokemon[i];
            if (!added.contains(ally.pokemonUUID)) {
               this.our.add(new AllyElement(ally, this, this.particleEngine));
               added.add(ally.pokemonUUID);
            }
         }
      }

      if (this.bm.displayedAllyPokemon != null) {
         for(i = 0; i < this.bm.displayedAllyPokemon.length; ++i) {
            ally = this.bm.displayedAllyPokemon[i];
            if (!added.contains(ally.pokemonUUID)) {
               this.our.add(new AllyElement(ally, this, this.particleEngine));
               added.add(ally.pokemonUUID);
            }
         }
      }

      this.our.sort(Comparator.comparingInt((a) -> {
         return -a.getAlly().position;
      }));
   }
}
