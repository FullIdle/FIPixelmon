package com.pixelmonmod.pixelmon.client.gui.starter;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.IShadowDelete;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ChooseStarter;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiChooseStarter extends GuiScreen implements IShadowDelete {
   private EnumStarterScreen currentScreen;
   private GuiButton confirm;
   int clickedIndex;
   ArrayList shadowList;
   int ticksToChange;

   public GuiChooseStarter() {
      this.currentScreen = EnumStarterScreen.Copyright;
      this.clickedIndex = -1;
      this.shadowList = new ArrayList();
      this.ticksToChange = 100;

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
      if (ServerStorageDisplay.starterListPacket != null && ServerStorageDisplay.starterListPacket.pokemonList != null) {
         if (this.currentScreen == EnumStarterScreen.Choose) {
            int index = 0;

            for(int col = 0; col < 8; ++col) {
               for(int row = 0; row < 3; ++row) {
                  int starterIndex = -1;
                  if (index < ServerStorageDisplay.starterListPacket.pokemonList.length && ServerStorageDisplay.starterListPacket.pokemonList[index] != null) {
                     starterIndex = index;
                  } else if (index < ServerStorageDisplay.starterListPacket.pokemonList.length) {
                     starterIndex = ServerStorageDisplay.starterListPacket.pokemonListIndex[index];
                  }

                  this.field_146292_n.add(new StarterButton(index, this.field_146294_l / 2 - 100 + 50 * (col - 2), this.field_146295_m / 2 - 64 + row * 32, starterIndex));
                  if (index == this.clickedIndex) {
                     ((StarterButton)this.field_146292_n.get(this.field_146292_n.size() - 1)).clicked = true;
                  }

                  ++index;
               }
            }

            this.field_146292_n.add(this.confirm = new GuiButton(index, this.field_146294_l / 2 - 80, this.field_146295_m * 2 / 3 + 30, 160, 20, I18n.func_135052_a("gui.starter.begin", new Object[0])));
            this.confirm.field_146125_m = this.clickedIndex != -1;
         }

      }
   }

   public void func_73869_a(char i, int i1) {
   }

   public void func_146284_a(GuiButton button) {
      if (button instanceof StarterButton && ((StarterButton)button).starterIndex != -1) {
         Iterator var2 = this.field_146292_n.iterator();

         while(var2.hasNext()) {
            GuiButton buttonL = (GuiButton)var2.next();
            if (buttonL != button && buttonL instanceof StarterButton) {
               ((StarterButton)buttonL).clicked = false;
            }
         }

         ((StarterButton)button).clicked = !((StarterButton)button).clicked;
         if (((StarterButton)button).clicked) {
            this.confirm.field_146125_m = true;
            this.clickedIndex = ((StarterButton)button).starterIndex;
         } else {
            this.confirm.field_146125_m = false;
            this.clickedIndex = -1;
         }
      } else if (button == this.confirm && button.field_146124_l && this.clickedIndex != -1) {
         Pixelmon.network.sendToServer(new ChooseStarter(this.clickedIndex));
         ServerStorageDisplay.starterListPacket = null;

         try {
            this.field_146297_k.field_71439_g.func_71053_j();
         } catch (NullPointerException var4) {
         }
      }

   }

   public void func_73863_a(int par1, int par2, float par3) {
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
      if (this.currentScreen == EnumStarterScreen.Copyright) {
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.starter.trademark1", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 - 30, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.starter.trademark2", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 - 17, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.starter.trademark3", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 + 5, 16777215);
      } else if (this.currentScreen == EnumStarterScreen.Choose) {
         if (this.field_146292_n.isEmpty()) {
            this.func_73866_w_();
            if (this.field_146292_n.isEmpty()) {
               this.field_146297_k.field_71439_g.func_71053_j();
            }
         }

         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.starter.trademark4", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 - 92, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.starter.trademark5", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 2 - 80, 16777215);
      }

      String name;
      EnumSpecies species;
      GuiButton button;
      if (this.confirm != null && !this.confirm.field_146125_m) {
         var4 = this.field_146292_n.iterator();

         while(var4.hasNext()) {
            button = (GuiButton)var4.next();
            if (button.func_146115_a() && button instanceof StarterButton) {
               if (((StarterButton)button).starterIndex == -1) {
                  name = I18n.func_135052_a("gui.starter.comingsoon", new Object[0]);
                  GlStateManager.func_179139_a(0.9, 0.9, 0.9);
               } else if (((StarterButton)button).starterIndex == -2) {
                  name = I18n.func_135052_a("gui.starter.disabled", new Object[0]);
                  GlStateManager.func_179139_a(0.9, 0.9, 0.9);
               } else {
                  species = ServerStorageDisplay.starterListPacket.pokemonList[((StarterButton)button).starterIndex].pokemon;
                  name = TextFormatting.UNDERLINE + species.getLocalizedName();
                  GlStateManager.func_179139_a(0.9, 0.9, 0.9);
                  GuiHelper.drawCenteredSplitString(I18n.func_135052_a("pixelmon." + species.name.toLowerCase() + ".description", new Object[0]), (float)this.field_146294_l / 1.8F, (float)(this.field_146295_m - 35), 300, 16777215, false);
               }

               GlStateManager.func_179139_a(1.1111111111111112, 1.1111111111111112, 1.1111111111111112);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.field_146297_k.func_110434_K().func_110577_a(GuiResources.buttonTexture);
               GuiHelper.drawCenteredString(name, (float)(this.field_146294_l / 2), (float)(this.field_146295_m * 2 / 3), 16777215);
            }
         }
      } else {
         var4 = this.field_146292_n.iterator();

         while(var4.hasNext()) {
            button = (GuiButton)var4.next();
            if (button instanceof StarterButton && ((StarterButton)button).clicked) {
               species = ServerStorageDisplay.starterListPacket.pokemonList[((StarterButton)button).starterIndex].pokemon;
               name = TextFormatting.UNDERLINE + species.getLocalizedName();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GuiHelper.drawCenteredString(name, (float)(this.field_146294_l / 2), (float)(this.field_146295_m * 2 / 3), 16777215);
            }
         }
      }

      super.func_73863_a(par1, par2, par3);
   }

   public void func_73876_c() {
      try {
         super.func_73876_c();
      } catch (NullPointerException var3) {
      }

      if (this.currentScreen == EnumStarterScreen.Copyright) {
         --this.ticksToChange;
         if (this.ticksToChange <= 0) {
            this.currentScreen = EnumStarterScreen.Choose;
            this.func_73866_w_();
         }
      }

      for(int i = 0; i < this.shadowList.size(); ++i) {
         ((Shadow)this.shadowList.get(i)).update();
      }

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
      if (this.currentScreen == EnumStarterScreen.Copyright) {
         this.currentScreen = EnumStarterScreen.Choose;
         this.func_73866_w_();
      }

   }

   public void removeShadow(Shadow shadow) {
      this.shadowList.remove(shadow);
   }
}
