package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.client.models.obj.ObjLoader;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public enum EnumCustomModel {
   PillarPlatform("blocks/pillar/pillar_platform.obj"),
   PillarColumn("blocks/pillar/pillar_column.obj"),
   PillarColumnFracturedBottom("blocks/pillar/pillar_column_fractured_bottom.obj"),
   PillarColumnFracturedTop("blocks/pillar/pillar_column_fractured_top.obj"),
   Pokeball("pokeballs/base.pqc"),
   Cherishball("pokeballs/cherishball.pqc"),
   Greatball("pokeballs/greatball.pqc"),
   Heavyball("pokeballs/heavyball.pqc"),
   Masterball("pokeballs/masterball.pqc"),
   Netball("pokeballs/netball.pqc"),
   Timerball("pokeballs/timerball.pqc"),
   Beastball("pokeballs/beastball.pqc"),
   MegaBraceletORAS("playeritems/megaitems/megabraceletoras.pqc"),
   MegaBraceletORASStone("playeritems/megaitems/megabraceletorasstone.pqc"),
   MegaGlasses("playeritems/megaitems/mega_glasses.pqc"),
   MegaAnchor("playeritems/megaitems/mega_anchor.pqc"),
   MegaBoostNecklace("playeritems/megaitems/boost_necklace.pqc"),
   DynamaxBand("playeritems/megaitems/dynamax_band.pqc"),
   OvalCharm("playeritems/oval_charm.pqc"),
   ShinyCharm("playeritems/shiny_charm.pqc"),
   ExpCharm("playeritems/exp_charm.pqc"),
   CatchingCharm("playeritems/catching_charm.pqc"),
   MarkCharm("playeritems/mark_charm.pqc"),
   MachBike("bikes/mbike.pqc"),
   AcroBike("bikes/abike.pqc"),
   DynamaxClouds("fluff/clouds.pqc");

   public String fileName;
   public IModel theModel;
   private boolean initialised = false;

   private EnumCustomModel(String fileName) {
      this.fileName = fileName;
   }

   public IModel getModel() {
      if (!this.initialised) {
         try {
            ResourceLocation rl = new ResourceLocation("pixelmon:models/" + this.fileName);
            if (ValveStudioModelLoader.instance.accepts(rl)) {
               this.theModel = ValveStudioModelLoader.instance.loadModel(rl);
            } else if (ObjLoader.accepts(rl)) {
               this.theModel = ObjLoader.loadModel(rl);
            }
         } catch (Exception var2) {
            System.out.println("Could not load the model: " + this.fileName);
            var2.printStackTrace();
         }

         this.initialised = true;
      }

      return this.theModel;
   }
}
