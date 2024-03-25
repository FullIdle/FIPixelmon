package com.pixelmonmod.pixelmon.client.gui.selectPokemon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.IShadowDelete;
import com.pixelmonmod.pixelmon.client.gui.starter.EnumShadow;
import com.pixelmonmod.pixelmon.client.gui.starter.Shadow;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectPokemon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;

public class GuiSelectPokemon extends GuiScreen implements IShadowDelete {
   ArrayList shadowList = new ArrayList();
   boolean boolInit = false;

   public GuiSelectPokemon() {
      EnumShadow shadowType;
      for(Random r = RandomHelper.rand; this.shadowList.size() < 5; this.shadowList.add(new Shadow(shadowType, this, r.nextFloat()))) {
         float f = r.nextFloat();
         if (f < 0.2F) {
            shadowType = EnumShadow.Large;
         } else if (f < 0.5F) {
            shadowType = EnumShadow.Medium;
         } else {
            shadowType = EnumShadow.Large;
         }
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      int index = 0;
      int[] rowSizes = getEvenlySplitRowSizes(ServerStorageDisplay.selectPokemonListPacket.pokemonList.length);

      for(int row = 0; row < rowSizes.length && index < 15; ++row) {
         for(int col = 0; col < rowSizes[row]; ++col) {
            int colShift = 40;
            if (rowSizes[row] % 2 == 0) {
               colShift = 0;
            }

            if (index < ServerStorageDisplay.selectPokemonListPacket.pokemonList.length && ServerStorageDisplay.selectPokemonListPacket.pokemonList[index] != null) {
               this.field_146292_n.add(new SelectPokemonButton(index, this.field_146294_l / 2 - colShift + 80 * (col - (int)Math.floor((double)(rowSizes[row] / 2))), this.field_146295_m / 6 + 45 + row * 41, index));
            }

            ++index;
            if (index >= 15) {
               break;
            }
         }
      }

   }

   public static int[] getEvenlySplitRowSizes(int length) {
      if (length <= 3) {
         return new int[]{length};
      } else if (length <= 10 && length % 2 == 0) {
         return new int[]{length / 2, length / 2};
      } else {
         int width = (int)Math.floor((double)(length / 3));
         int mod = length % 3;
         return new int[]{width + (mod >= 1 ? 1 : 0), width + (mod == 2 ? 1 : 0), width};
      }
   }

   public void func_73869_a(char i, int i1) {
   }

   public void func_146284_a(GuiButton button) {
      if (((SelectPokemonButton)button).starterIndex >= 0) {
         Pixelmon.network.sendToServer(new SelectPokemon(button.field_146127_k));
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.starterBackground);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      Iterator var4 = this.shadowList.iterator();

      while(var4.hasNext()) {
         Shadow aShadowList = (Shadow)var4.next();
         aShadowList.draw(this.field_146297_k, this.field_146294_l, this.field_146295_m);
      }

      GlStateManager.func_179147_l();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.starterBorders);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_74838_a("gui.selectpokemon.message"), this.field_146294_l / 2, 56, 16777215);
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (!this.boolInit) {
         this.func_73866_w_();
      }

      this.shadowList.forEach(Shadow::update);
      if (Minecraft.func_71410_x().field_71439_g.func_70681_au().nextFloat() < 0.008F) {
         float f = this.field_146297_k.field_71439_g.func_70681_au().nextFloat();
         EnumShadow shadowType;
         if (f < 0.2F) {
            shadowType = EnumShadow.Large;
         } else if (f < 0.5F) {
            shadowType = EnumShadow.Medium;
         } else {
            shadowType = EnumShadow.Large;
         }

         this.shadowList.add(new Shadow(shadowType, this));
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      super.func_73864_a(par1, par2, par3);
      if (!this.boolInit) {
         this.func_73866_w_();
      }

   }

   public void removeShadow(Shadow shadowSelectPokemon) {
      this.shadowList.remove(shadowSelectPokemon);
   }
}
