package com.pixelmonmod.pixelmon.client.gui.machines.washingmachine;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileWashingMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiWashingMachine extends GuiContainer {
   public static final int WIDTH = 176;
   public static final int HEIGHT = 166;
   private TileWashingMachine furnace;
   private static final ResourceLocation background = new ResourceLocation("pixelmon", "textures/gui/machines/electricfurnace.png");

   public GuiWashingMachine(InventoryPlayer inventoryPlayer, TileWashingMachine tileEntityFurnace) {
      super(new ContainerWashingMachine(inventoryPlayer, tileEntityFurnace));
      this.furnace = tileEntityFurnace;
      this.field_146999_f = 176;
      this.field_147000_g = 166;
   }

   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.func_110434_K().func_110577_a(background);
      this.func_73729_b(this.field_147003_i, this.field_147009_r, 0, 0, this.field_146999_f, this.field_147000_g);
      int i1 = this.furnace.getSmeltTimeRemainingScaled(13) * 2;
      this.func_73729_b(this.field_147003_i + 79, this.field_147009_r + 35, 176, 14, i1, 14);
      this.func_73729_b(this.field_147003_i + 58, this.field_147009_r + 53, 176, 0, 14, i1);
   }
}
