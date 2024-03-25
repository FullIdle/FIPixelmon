package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.machines.BlockFossilMachine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilMachine;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumStatueTextureType;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTileFossilMachine extends TileEntityRenderer {
   private static final ResourceLocation MACHINE_TEXTURE = new ResourceLocation("pixelmon", "textures/blocks/Fossil_Extractor.png");
   private static final GenericModelHolder machineBase = new GenericModelHolder("blocks/fossil_machine/fossil_extractor.pqc");
   private static final GenericSmdModel machineGlass = new GenericSmdModel("models/blocks/fossil_machine", "fossil_extractor_glass.pqc");
   private static final Map overrideMap = new HashMap();
   private static final StatueConfig DEFAULT;

   public RenderTileFossilMachine() {
      machineGlass.modelRenderer.setTransparent(0.5F);
      this.correctionAngles = 180;
      overrideMap.put(EnumSpecies.Omanyte, new StatueConfig(0.15F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Kabuto, new StatueConfig(0.001F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Aerodactyl, new StatueConfig(0.6F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Lileep, new StatueConfig(0.16F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Anorith, new StatueConfig(0.1F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Cranidos, new StatueConfig(0.22F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Shieldon, new StatueConfig(0.001F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Tirtouga, new StatueConfig(0.05F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Archen, new StatueConfig(0.15F, 6, AnimationType.FLY));
      overrideMap.put(EnumSpecies.Tyrunt, new StatueConfig(0.25F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Amaura, new StatueConfig(0.01F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Arctozolt, new StatueConfig(0.5F, 20, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Arctovish, new StatueConfig(0.45F, 20, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Dracovish, new StatueConfig(0.5F, 0, AnimationType.IDLE));
      overrideMap.put(EnumSpecies.Dracozolt, new StatueConfig(0.45F, 20, AnimationType.IDLE));
   }

   public void renderTileEntity(TileEntityFossilMachine machine, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(MACHINE_TEXTURE);
      if (machine.renderPass == 1) {
         machineGlass.renderModel(1.0F);
      } else {
         ((GenericSmdModel)machineBase.getModel()).renderModel(1.0F);
         GlStateManager.func_179101_C();
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         int rotate = this.getRotation(state);
         EnumFacing facing = EnumFacing.NORTH;
         if (state.func_177230_c() instanceof BlockFossilMachine) {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         }

         this.renderCompletionLevel(machine, machine.completionRate + "%", facing, rotate, x, y, z, false, -1);
         if (machine.staticFlicker && machine.completionRate == 100 && machine.pokeball == null) {
            this.renderCompletionLevel(machine, I18n.func_135052_a("pixelmon.blocks.fossilmachine.pokeball", new Object[0]), facing, rotate, x, y + 0.07999999821186066, z, true, -65536);
         } else if (machine.completionRate > 0 && machine.completionRate < 100) {
            this.renderCompletionLevel(machine, I18n.func_135052_a("pixelmon.blocks.fossilmachine.working", new Object[0]) + machine.dots, facing, rotate, x, y + 0.07999999821186066, z, true, -16711936);
         } else if (machine.staticFlicker && machine.completionRate == 0 && machine.currentFossil == EnumFossils.NULL) {
            this.renderCompletionLevel(machine, I18n.func_135052_a("pixelmon.blocks.fossilmachine.fossil", new Object[0]), facing, rotate, x, y + 0.07999999821186066, z, true, -65536);
         } else if (machine.staticFlicker && machine.completionRate == 0 && machine.currentFossil.getPokemon() == null) {
            this.renderCompletionLevel(machine, I18n.func_135052_a("pixelmon.blocks.fossilmachine.incomplete", new Object[0]), facing, rotate, x, y + 0.07999999821186066, z, true, -65536);
         } else if (machine.staticFlicker && machine.completionRate == 100 && machine.pokeball != null) {
            this.renderCompletionLevel(machine, I18n.func_135052_a("pixelmon.blocks.fossilmachine.retrieve", new Object[0]), facing, rotate, x, y + 0.07999999821186066, z, true, -16711936);
         }

         this.renderBarLevel(machine, facing, rotate, x, y, z);
         if (machine.currentFossil != EnumFossils.NULL) {
            this.renderFossil(machine.currentFossil, machine, rotate, x, y, z, facing);
         }

         if (machine.pokeball != null) {
            this.renderPokeball(machine.pokeball, machine, rotate, x, y, z, facing);
         }

         StatueConfig config;
         if ((machine.statue == null || !machine.statue.func_70005_c_().equalsIgnoreCase(machine.currentPokemon)) && !machine.currentPokemon.equals("") && EnumSpecies.hasPokemon(machine.currentPokemon)) {
            machine.statue = new EntityStatue(this.func_178459_a());
            machine.statue.setPokemon(new PokemonBase(EnumSpecies.getFromNameAnyCase(machine.currentPokemon)));
            machine.statue.setGrowth(EnumGrowth.Small);
            if (machine.isShiny) {
               machine.statue.setTextureType(EnumStatueTextureType.Shiny);
            }

            config = (StatueConfig)overrideMap.getOrDefault(machine.statue.getSpecies(), DEFAULT);
            machine.statue.setAnimation(config.type);
            machine.statue.setAnimationFrame(config.frame);
         }

         if (machine.statue != null) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.9F, (float)z + 0.5F);
            GlStateManager.func_179114_b((float)rotate, 0.0F, 1.0F, 0.0F);
            config = (StatueConfig)overrideMap.getOrDefault(machine.statue.getSpecies(), DEFAULT);
            float scale = Math.max(machine.pokemonProgress / 1000.0F / 4.0F - config.scale, 0.0F);
            GlStateManager.func_179152_a(scale, scale, scale);
            machine.statue.func_70012_b(x, y, z, 0.0F, 0.0F);
            Minecraft.func_71410_x().func_175598_ae().func_188391_a(machine.statue, 0.0, 0.0, 0.0, 0.0F, partialTicks, false);
            GlStateManager.func_179121_F();
         }

      }
   }

   protected int getRotation(IBlockState state) {
      if (state.func_177230_c() instanceof BlockFossilMachine) {
         EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         if (facing == EnumFacing.WEST) {
            return 90;
         } else if (facing == EnumFacing.SOUTH) {
            return 180;
         } else {
            return facing == EnumFacing.EAST ? 270 : 0;
         }
      } else {
         return 0;
      }
   }

   private void renderBarLevel(TileEntityFossilMachine tile, EnumFacing facing, int rotate, double x1, double y1, double z1) {
      GlStateManager.func_179094_E();
      float f2 = 1.5F;
      float f3 = 0.00666667F * f2;
      float x = (float)x1;
      float y = (float)y1 + 0.32F;
      float z = (float)z1;
      if (facing == EnumFacing.NORTH) {
         GlStateManager.func_179109_b(x + 0.5F, y, z + 0.97F);
      } else if (facing == EnumFacing.WEST) {
         GlStateManager.func_179109_b(x + 0.97F, y, z + 0.5F);
      } else if (facing == EnumFacing.SOUTH) {
         GlStateManager.func_179109_b(x + 0.5F, y, z + 0.03F);
      } else {
         GlStateManager.func_179109_b(x + 0.03F, y, z + 0.5F);
      }

      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b((float)(rotate + 180), 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179152_a(-f3, -f3, f3);
      GlStateManager.func_179140_f();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      byte byte0 = -20;
      GlStateManager.func_179090_x();
      float f8 = (float)(tile.completionRate / 2);
      buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      buffer.func_181662_b((double)(-25.0F + f8), (double)(-7 + byte0), 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(-25.0F + f8), (double)(-6 + byte0), 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
      buffer.func_181662_b(25.0, (double)(-6 + byte0), 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
      buffer.func_181662_b(25.0, (double)(-7 + byte0), 0.0).func_181666_a(0.0039F, 0.03137F, 0.4196F, 1.0F).func_181675_d();
      buffer.func_181662_b(-25.0, (double)(-7 + byte0), 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
      buffer.func_181662_b(-25.0, (double)(-6 + byte0), 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(f8 - 25.0F), (double)(-6 + byte0), 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
      buffer.func_181662_b((double)(f8 - 25.0F), (double)(-7 + byte0), 0.0).func_181666_a(0.0F, 0.8901F, 0.8901F, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179098_w();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   private void renderCompletionLevel(TileEntityFossilMachine tile, String text, EnumFacing facing, int rotation, double x1, double y1, double z1, boolean par10, int color) {
      FontRenderer fontRenderer = this.func_147498_b();
      GlStateManager.func_179094_E();
      float var13 = 1.5F;
      float var14 = 0.011666667F * var13;
      float x = (float)x1;
      float y = (float)y1 + 0.75F;
      float z = (float)z1;
      if (facing == EnumFacing.NORTH) {
         GlStateManager.func_179109_b(x + 0.5F, y, z + 0.97F);
      } else if (facing == EnumFacing.WEST) {
         GlStateManager.func_179109_b(x + 0.97F, y, z + 0.5F);
      } else if (facing == EnumFacing.SOUTH) {
         GlStateManager.func_179109_b(x + 0.5F, y, z + 0.03F);
      } else {
         GlStateManager.func_179109_b(x + 0.03F, y, z + 0.5F);
      }

      GlStateManager.func_179114_b((float)(rotation + 180), 0.0F, 1.0F, 0.0F);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      if (!par10) {
         GlStateManager.func_179152_a(-var14, -var14, var14);
      } else {
         GlStateManager.func_179152_a(-var14 + 0.012F, -var14 + 0.012F, var14 + 0.012F);
      }

      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      int stringWidth = fontRenderer.func_78256_a(text) / 2;
      GlStateManager.func_179098_w();
      fontRenderer.func_78276_b(text, -stringWidth, 0, color);
      buffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      buffer.func_181662_b((double)(-stringWidth - 1), -1.0, 0.0).func_181675_d();
      buffer.func_181662_b((double)(-stringWidth - 1), 8.0, 0.0).func_181675_d();
      buffer.func_181662_b((double)(stringWidth + 1), 8.0, 0.0).func_181675_d();
      buffer.func_181662_b((double)(stringWidth + 1), -1.0, 0.0).func_181675_d();
      tessellator.func_78381_a();
      fontRenderer.func_78276_b(text, -stringWidth, 0, color);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
   }

   private void renderFossil(EnumFossils fossil, TileEntityFossilMachine machine, int rotate, double x, double y, double z, EnumFacing facing) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 1.0F + machine.fossilJitter, (float)z + 0.5F);
      GlStateManager.func_179152_a(0.8F - machine.fossilProgress / 1000.0F / 2.0F, -0.8F + machine.fossilProgress / 1000.0F / 2.0F, -0.8F + machine.fossilProgress / 1000.0F / 2.0F);
      if (facing != EnumFacing.NORTH && facing != EnumFacing.SOUTH) {
         GlStateManager.func_179114_b((float)rotate + 180.0F, 0.0F, 0.5F, 0.0F);
      } else {
         GlStateManager.func_179114_b((float)rotate, 0.0F, 1.0F, 0.0F);
      }

      this.func_147499_a(fossil.getTexture());
      ((GenericModelHolder)fossil.getModel()).render(1.0F);
      GlStateManager.func_179121_F();
   }

   private void renderPokeball(EnumPokeballs pokeBall, TileEntityFossilMachine machine, int rotate, double x, double y, double z, EnumFacing facing) {
      GlStateManager.func_179094_E();
      if (rotate == 270) {
         GlStateManager.func_179137_b(x - 0.1, y + 0.41999998688697815, z + 0.86);
      } else if (rotate == 180) {
         GlStateManager.func_179137_b(x + 0.14, y + 0.41999998688697815, z - 0.1);
      } else if (rotate == 90) {
         GlStateManager.func_179137_b(x + 1.1, y + 0.41999998688697815, z + 0.14);
      } else {
         GlStateManager.func_179137_b(x + 0.86, y + 0.41999998688697815, z + 1.1);
      }

      GlStateManager.func_179114_b((float)rotate, 0.0F, 1.0F, 0.0F);
      this.func_147499_a(pokeBall.getTextureLocation());
      GenericModelHolder pokeball = SharedModels.getPokeballModel(pokeBall);
      ((ModelPokeballs)pokeball.getModel()).theModel.setAnimation(AnimationType.IDLE);
      ((ModelPokeballs)pokeball.getModel()).theModel.animate();
      pokeball.render(0.002F);
      GlStateManager.func_179121_F();
   }

   static {
      DEFAULT = new StatueConfig(0.15F, 0, AnimationType.WALK);
   }

   private static class StatueConfig {
      float scale = 0.15F;
      int frame = 0;
      AnimationType type;

      public StatueConfig(float scale, int frame, AnimationType type) {
         this.type = AnimationType.WALK;
         this.scale = scale;
         this.frame = frame;
         this.type = type;
      }
   }
}
