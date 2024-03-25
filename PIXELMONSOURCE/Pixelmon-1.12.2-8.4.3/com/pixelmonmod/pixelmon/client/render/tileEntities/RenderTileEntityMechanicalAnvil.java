package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelMechanicalAnvil;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityMechanicalAnvil extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder(ModelMechanicalAnvil.class);
   private static final ResourceLocation anvilTextureRunning = new ResourceLocation("pixelmon", "textures/blocks/mechanvil_on.png");
   private static final ResourceLocation anvilTextureIdle = new ResourceLocation("pixelmon", "textures/blocks/mechanvil_off.png");
   private static final GenericModelHolder baseBallModel = new GenericModelHolder("blocks/mech_anvil/balls/base_ball/base_ball.pqc");
   private static final GenericModelHolder greatBallModel = new GenericModelHolder("blocks/mech_anvil/balls/great_ball/great_ball.pqc");
   private static final GenericModelHolder heavyBallModel = new GenericModelHolder("blocks/mech_anvil/balls/heavy_ball/heavy_ball.pqc");
   private static final GenericModelHolder netBallModel = new GenericModelHolder("blocks/mech_anvil/balls/net_ball/net_ball.pqc");
   private static final GenericModelHolder timerBallModel = new GenericModelHolder("blocks/mech_anvil/balls/timer_ball/timer_ball.pqc");

   public void renderTileEntity(TileEntityMechanicalAnvil mechAnvil, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(mechAnvil.isRunning() ? anvilTextureRunning : anvilTextureIdle);
      if ((double)partialTicks > 0.5 && mechAnvil.frame != 0) {
         ((ModelMechanicalAnvil)RenderTileEntityMechanicalAnvil.model.getModel()).setFrame(mechAnvil.frame + 1);
      } else {
         ((ModelMechanicalAnvil)RenderTileEntityMechanicalAnvil.model.getModel()).setFrame(mechAnvil.frame);
      }

      RenderTileEntityMechanicalAnvil.model.render();
      Item item = mechAnvil.pokeBallType;
      if (item != null) {
         GenericModelHolder model = null;
         if (item == PixelmonItemsPokeballs.pokeBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/PokeBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.greatBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/GreatBall.png"));
            model = greatBallModel;
         } else if (item == PixelmonItemsPokeballs.ultraBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/UltraBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.levelBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/LevelBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.moonBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/MoonBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.friendBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/FriendBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.loveBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/LoveBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.safariBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/SafariBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.heavyBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/HeavyBall.png"));
            model = heavyBallModel;
         } else if (item == PixelmonItemsPokeballs.fastBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/FastBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.repeatBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/RepeatBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.timerBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/TimerBall.png"));
            model = timerBallModel;
         } else if (item == PixelmonItemsPokeballs.nestBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/NestBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.netBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/NetBall.png"));
            model = netBallModel;
         } else if (item == PixelmonItemsPokeballs.diveBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/DiveBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.luxuryBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/LuxuryBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.healBall) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/HealBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.duskBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/DuskBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.premierBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/PremierBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.sportBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/SportBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.quickBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/QuickBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.lureBallDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/LureBall.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.ironDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/IronAluminium.png"));
            model = baseBallModel;
         } else if (item == PixelmonItemsPokeballs.aluDisc) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/mechanvilballs/IronAluminium.png"));
            model = baseBallModel;
         }

         if (model == null) {
            return;
         }

         ((GenericSmdModel)model.getModel()).setFrame(mechAnvil.frame);
         model.render();
      }

   }
}
