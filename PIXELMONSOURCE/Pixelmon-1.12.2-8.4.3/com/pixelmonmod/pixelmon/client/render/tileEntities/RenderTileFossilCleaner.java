package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilCleaner;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RenderTileFossilCleaner extends TileEntityRenderer {
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/fossilCleaner.png");
   private static final GenericModelHolder fossilCleaner = new GenericModelHolder("blocks/fossil_cleaner/fossil_cleaner.pqc");
   private static final GenericModelHolder fossilCleanerArm = new GenericModelHolder("blocks/fossil_cleaner/fossil_cleaner_arm.pqc");
   private static final GenericSmdModel fossilCleanerGlass = new GenericSmdModel("models/blocks/fossil_cleaner", "fossil_cleaner_glass.pqc");
   private static final GenericModelHolder coveredHelixFossil = new GenericModelHolder("fossils/covered/covered_helix.pqc");
   private static final GenericModelHolder coveredDomeFossil = new GenericModelHolder("fossils/covered/covered_dome.pqc");
   private static final GenericModelHolder coveredOldAmber = new GenericModelHolder("fossils/covered/covered_oldamber.pqc");
   private static final GenericModelHolder coveredClawFossil = new GenericModelHolder("fossils/covered/covered_claw.pqc");
   private static final GenericModelHolder coveredSkullFossil = new GenericModelHolder("fossils/covered/covered_skull.pqc");
   private static final GenericModelHolder coveredArmorFossil = new GenericModelHolder("fossils/covered/covered_armor.pqc");
   private static final GenericModelHolder coveredCoverFossil = new GenericModelHolder("fossils/covered/covered_cover.pqc");
   private static final GenericModelHolder coveredPlumeFossil = new GenericModelHolder("fossils/covered/covered_plume.pqc");
   private static final GenericModelHolder coveredJawFossil = new GenericModelHolder("fossils/covered/covered_jaw.pqc");
   private static final GenericModelHolder coveredSailFossil = new GenericModelHolder("fossils/covered/covered_sail.pqc");

   public RenderTileFossilCleaner() {
      this.correctionAngles = 180;
      fossilCleanerGlass.modelRenderer.setTransparent(0.5F);
   }

   public void renderTileEntity(TileEntityFossilCleaner cleaner, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (cleaner.renderPass == 1) {
         this.func_147499_a(texture);
         fossilCleanerGlass.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
      } else {
         int frame;
         if (cleaner.timer >= 360) {
            frame = 0;
         } else {
            frame = cleaner.timer * 2;
         }

         this.func_147499_a(texture);
         ((GenericSmdModel)fossilCleaner.getModel()).setFrame(frame);
         fossilCleaner.render();
         ((GenericSmdModel)fossilCleanerArm.getModel()).setFrame(frame);
         fossilCleanerArm.render();
         Item itemInside = cleaner.getItemInCleaner();
         if (itemInside != null) {
            GenericModelHolder fossilModel = null;
            if (itemInside instanceof ItemCoveredFossil) {
               ItemCoveredFossil coveredFossil = (ItemCoveredFossil)itemInside;
               switch (coveredFossil.cleanedFossil.getFossil()) {
                  case HELIX:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  case DOME:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_dome_fossilmodel.png"));
                     fossilModel = coveredDomeFossil;
                     break;
                  case OLD_AMBER:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_old_amber_fossilmodel.png"));
                     fossilModel = coveredOldAmber;
                     break;
                  case ROOT:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  case CLAW:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_claw_fossilmodel.png"));
                     fossilModel = coveredClawFossil;
                     break;
                  case SKULL:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_skull_fossilmodel.png"));
                     fossilModel = coveredSkullFossil;
                     break;
                  case ARMOR:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_armor_fossilmodel.png"));
                     fossilModel = coveredArmorFossil;
                     break;
                  case COVER:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_cover_fossilmodel.png"));
                     fossilModel = coveredCoverFossil;
                     break;
                  case PLUME:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_plume_fossilmodel.png"));
                     fossilModel = coveredPlumeFossil;
                     break;
                  case JAW:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_jaw_fossilmodel.png"));
                     fossilModel = coveredJawFossil;
                     break;
                  case SAIL:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_sail_fossilmodel.png"));
                     fossilModel = coveredSailFossil;
                     break;
                  case BIRD:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  case DINO:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  case DRAKE:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  case FISH:
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/fossils/covered/covered_helix_fossilmodel.png"));
                     fossilModel = coveredHelixFossil;
                     break;
                  default:
                     Pixelmon.LOGGER.warn("Unknown covered Fossil present. Name: " + coveredFossil.toString());
               }
            } else if (itemInside instanceof ItemFossil) {
               ItemFossil fossil = (ItemFossil)itemInside;
               this.func_147499_a(fossil.getFossil().getTexture());
               fossilModel = (GenericModelHolder)fossil.getFossil().getModel();
            } else {
               Pixelmon.LOGGER.warn("Unrecognised item type in Fossil cleaner: " + itemInside.func_77658_a());
            }

            if (fossilModel != null) {
               if (itemInside instanceof ItemCoveredFossil) {
                  ((GenericSmdModel)fossilModel.getModel()).setFrame(frame);
                  fossilModel.render();
               } else {
                  GlStateManager.func_179137_b(0.0, -0.555, 0.0);
                  fossilModel.render();
               }
            }
         }

      }
   }
}
