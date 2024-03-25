package com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiParticleEngine;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import java.util.NoSuchElementException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class OverlayBase extends GuiScreen {
   protected GuiBattle parent;
   protected ClientBattleManager bm;
   protected GuiParticleEngine particleEngine = new GuiParticleEngine();

   public OverlayBase(GuiBattle parent) {
      this.parent = parent;
      this.field_146297_k = Minecraft.func_71410_x();
      this.bm = ClientProxy.battleManager;
   }

   public abstract void draw(int var1, int var2, int var3, int var4);

   public abstract int mouseOverEnemyPokemon(int var1, int var2, int var3, int var4);

   public abstract int mouseOverUserPokemon(int var1, int var2, int var3, int var4, int var5, int var6);

   protected boolean hasCaught(PixelmonInGui targetPokemon) {
      Pokedex pokedex = ClientStorageManager.pokedex;
      if (pokedex == null) {
         return false;
      } else {
         try {
            return pokedex.hasCaught(targetPokemon.getDexNumber());
         } catch (NoSuchElementException var4) {
            return false;
         }
      }
   }
}
