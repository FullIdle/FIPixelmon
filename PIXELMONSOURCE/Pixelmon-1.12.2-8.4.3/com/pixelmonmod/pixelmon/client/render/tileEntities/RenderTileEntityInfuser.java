package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.machines.BlockInfuser;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderTileEntityInfuser extends TileEntityRenderer {
   private static final GenericModelHolder machineBase = new GenericModelHolder("blocks/infuser/infuser.pqc");
   private static final GenericSmdModel machineGlass = new GenericSmdModel("models/blocks/infuser", "infuser_glass.pqc");
   private static final ResourceLocation MACHINE_TEXTURE_ON = new ResourceLocation("pixelmon", "textures/blocks/infuser_on.png");
   private static final ResourceLocation MACHINE_TEXTURE_OFF = new ResourceLocation("pixelmon", "textures/blocks/infuser_off.png");

   public RenderTileEntityInfuser() {
      machineGlass.modelRenderer.setTransparent(0.5F);
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityInfuser machine, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(machine.isRunning ? MACHINE_TEXTURE_ON : MACHINE_TEXTURE_OFF);
      if (machine.renderPass == 1) {
         machineGlass.renderModel(1.0F);
      } else {
         ((GenericSmdModel)machineBase.getModel()).renderModel(1.0F);
         EnumFacing facing = EnumFacing.NORTH;
         if (state.func_177230_c() instanceof BlockInfuser) {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         }

         ItemStack item = machine.func_70301_a(2);
         if (item != null) {
            GlStateManager.func_179094_E();
            double offset = Math.sin((double)(((float)machine.func_145831_w().func_82737_E() + partialTicks) / 32.0F)) / 6.0;
            GlStateManager.func_179137_b(0.0, -0.8 + offset, 0.25);
            GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179152_a(0.75F, 0.75F, 0.75F);
            IBakedModel model = Minecraft.func_71410_x().func_175599_af().func_184393_a(item, machine.func_145831_w(), (EntityLivingBase)null);
            model = ForgeHooksClient.handleCameraTransforms(model, TransformType.GROUND, false);
            Minecraft.func_71410_x().func_110434_K().func_110577_a(TextureMap.field_110575_b);
            Minecraft.func_71410_x().func_175599_af().func_180454_a(item, model);
            GlStateManager.func_179121_F();
         }

         ItemStack item2 = machine.func_70301_a(1);
         if (item != null) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b(0.0, -1.15, -0.25);
            GlStateManager.func_179114_b(((float)machine.func_145831_w().func_82737_E() + partialTicks) * 4.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            IBakedModel model = Minecraft.func_71410_x().func_175599_af().func_184393_a(item2, machine.func_145831_w(), (EntityLivingBase)null);
            model = ForgeHooksClient.handleCameraTransforms(model, TransformType.GROUND, false);
            Minecraft.func_71410_x().func_110434_K().func_110577_a(TextureMap.field_110575_b);
            Minecraft.func_71410_x().func_175599_af().func_180454_a(item2, model);
            GlStateManager.func_179121_F();
         }

      }
   }
}
