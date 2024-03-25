package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.obj.ObjLoader;
import net.minecraft.util.ResourceLocation;

public class ModelBrokenCloningMachine extends ModelEntityBlock {
   ResourceLocation baseLoc = new ResourceLocation("pixelmon:models/blocks/cloning_machine/broken/brokenclonerbase.obj");
   ResourceLocation glassLoc = new ResourceLocation("pixelmon:models/blocks/cloning_machine/broken/brokenclonerglass.obj");
   PixelmonModelRenderer machine = new PixelmonModelRenderer(this);
   PixelmonModelRenderer glass;
   boolean travDown = true;

   public ModelBrokenCloningMachine() {
      this.machine.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.baseLoc)));
      this.glass = new PixelmonModelRenderer(this);
      this.glass.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.glassLoc)));
      this.glass.setTransparent(1.0F);
   }

   public void renderTileEntity(TileEntityDecorativeBase tileEnt, float scale) {
      this.machine.func_78785_a(0.0625F);
   }

   public void renderGlass(TileEntityCloningMachine tileEntity, float f) {
      this.glass.func_78785_a(0.0625F);
   }
}
