package com.pixelmonmod.pixelmon.client.gui.spawner;

import com.pixelmonmod.pixelmon.blocks.machines.PokemonRarity;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiSlotBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiPokemonList extends GuiSlotBase {
   private final Minecraft mc;
   GuiPixelmonSpawner gui;

   public GuiPokemonList(GuiPixelmonSpawner gui, int width, int height, int top, Minecraft mc) {
      super(top, 0, width, height, true);
      this.mc = mc;
      this.gui = gui;
   }

   protected void elementClicked(int index, boolean doubleClicked) {
      this.gui.removeFromList(index);
   }

   protected void drawSlot(int entryID, int x, int yTop, int yMiddle, Tessellator tessellator) {
      PokemonRarity r = this.gui.getPokemonListEntry(entryID);
      if (r != null) {
         this.mc.field_71466_p.func_175065_a(Entity1Base.getLocalizedName(r.pokemon.name), (float)(x + 10), (float)yTop, 0, false);
         this.mc.field_71466_p.func_175065_a("" + r.rarity, (float)(x + 82), (float)yTop, 0, false);
      }

   }

   protected float[] get1Color() {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   protected int getSize() {
      return this.gui.getPokemonListCount();
   }

   protected boolean isSelected(int element) {
      return false;
   }

   protected int[] getSelectionColor() {
      return new int[]{128, 128, 128};
   }
}
