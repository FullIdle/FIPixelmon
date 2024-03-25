package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityGymSign;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GymSignRenderer extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder("pixelutilities/GymSign/GymSign.pqc");
   private static final GenericModelHolder m3d = new GenericModelHolder("pixelutilities/GymSign/3d.pqc");
   private static final GenericModelHolder m2d = new GenericModelHolder("pixelutilities/GymSign/2d.pqc");

   public GymSignRenderer() {
      super.disableCulling = true;
   }

   public void renderTileEntity(TileEntityGymSign gymSign, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(gymSign.getTexture());
      GymSignRenderer.model.render();
      ItemStack item = gymSign.getItemInSign();
      if (item != null) {
         try {
            IBakedModel model = Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178089_a(item);
            if (!model.func_188618_c()) {
               model = model.func_188617_f().handleItemState(model, item, this.func_178459_a(), (EntityLivingBase)null);
            }

            String[] split = model.func_177554_e().func_94215_i().split(":");
            if (split.length < 1 || split.length > 2) {
               return;
            }

            ResourceLocation resource = split.length == 1 ? new ResourceLocation(split[0]) : new ResourceLocation(split[0], "textures/" + split[1] + ".png");
            if (model.func_177556_c()) {
               this.func_147499_a(resource);
               m3d.render();
            } else {
               this.func_147499_a(resource);
               m2d.render();
            }
         } catch (NullPointerException var15) {
         }
      }

   }
}
