package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.obj.ObjLoader;
import net.minecraft.util.ResourceLocation;

public class ModelCloningMachine extends ModelEntityBlock {
   ResourceLocation base = new ResourceLocation("pixelmon:models/blocks/cloning_machine/cloner_base.obj");
   ResourceLocation glass1 = new ResourceLocation("pixelmon:models/blocks/cloning_machine/cloner_glass1.obj");
   ResourceLocation glass2 = new ResourceLocation("pixelmon:models/blocks/cloning_machine/cloner_glass2.obj");
   ResourceLocation laser = new ResourceLocation("pixelmon:models/blocks/cloning_machine/cloner_laser.obj");
   ResourceLocation balls = new ResourceLocation("pixelmon:models/blocks/cloning_machine/cloner_laserballs.obj");
   PixelmonModelRenderer machine = new PixelmonModelRenderer(this);
   PixelmonModelRenderer glass;
   PixelmonModelRenderer las;
   PixelmonModelRenderer ball;
   boolean travDown = true;

   public ModelCloningMachine() {
      this.machine.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.base)));
      this.las = new PixelmonModelRenderer(this);
      this.las.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.laser)));
      this.las.setTransparent(1.0F);
      this.ball = new PixelmonModelRenderer(this);
      this.ball.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.balls)));
      this.glass = new PixelmonModelRenderer(this);
      this.glass.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.glass1)));
      this.glass.addCustomModel(new ModelCustomWrapper(ObjLoader.loadModel(this.glass2)));
      this.glass.setTransparent(1.0F);
   }

   public void renderTileEntity(TileEntityDecorativeBase tileEnt, float scale) {
      TileEntityCloningMachine te = (TileEntityCloningMachine)tileEnt;
      this.machine.func_78785_a(0.0625F);
      this.ball.field_78797_d = te.lasPos;
      this.ball.func_78785_a(0.0625F);
   }

   public void renderGlass(TileEntityCloningMachine tileEntity, float f) {
      this.las.field_78797_d = this.ball.field_78797_d;
      this.las.func_78785_a(0.0625F);
      this.glass.func_78785_a(0.0625F);
   }
}
