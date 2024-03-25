package com.pixelmonmod.pixelmon.client.gui.npc;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiInvisibleButton;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SelectPokemonResponse;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiSelectPartyPokemon extends GuiScreen {
   String mainText;
   String backText;
   boolean faintedOnly;
   private final List party = Lists.newArrayList();
   private final SelectPokemonResponse.Mode mode;
   private final int npcId;

   public GuiSelectPartyPokemon(int mode, int npcId) {
      this.mode = SelectPokemonResponse.Mode.fromOrdinal(mode);
      this.npcId = npcId;
      if (this.mode == SelectPokemonResponse.Mode.Revive) {
         this.mainText = "gui.selectpokemon.message";
         this.backText = "gui.battle.back";
         this.faintedOnly = true;
      } else {
         this.mainText = "gui.selectpokemon.message";
         this.backText = "gui.battle.back";
         this.faintedOnly = false;
      }

   }

   public GuiSelectPartyPokemon(String mainText, String backText, boolean faintedOnly, int mode, int npcId) {
      this.mainText = mainText;
      this.backText = backText;
      this.faintedOnly = faintedOnly;
      this.mode = SelectPokemonResponse.Mode.fromOrdinal(mode);
      this.npcId = npcId;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.party.clear();
      Iterator var1 = ClientStorageManager.party.getTeam().iterator();

      while(true) {
         Pokemon pokemon;
         while(true) {
            if (!var1.hasNext()) {
               if (this.party.isEmpty()) {
                  GuiHelper.closeScreen();
                  return;
               }

               this.func_189646_b((new GuiInvisibleButton(0, this)).setPosition(this.field_146294_l / 2 - 119, this.field_146295_m - 179, 88, 66));
               int pos = 1;
               this.func_189646_b((new GuiInvisibleButton(1, this)).setPosition(this.field_146294_l / 2 - 29, this.field_146295_m - 223 + pos * 30, 148, 29));
               pos = 2;
               this.func_189646_b((new GuiInvisibleButton(2, this)).setPosition(this.field_146294_l / 2 - 29, this.field_146295_m - 223 + pos * 30, 148, 29));
               pos = 3;
               this.func_189646_b((new GuiInvisibleButton(3, this)).setPosition(this.field_146294_l / 2 - 29, this.field_146295_m - 223 + pos * 30, 148, 29));
               pos = 4;
               this.func_189646_b((new GuiInvisibleButton(4, this)).setPosition(this.field_146294_l / 2 - 29, this.field_146295_m - 223 + pos * 30, 148, 29));
               pos = 5;
               this.func_189646_b((new GuiInvisibleButton(5, this)).setPosition(this.field_146294_l / 2 - 29, this.field_146295_m - 223 + pos * 30, 148, 29));
               this.func_189646_b((new GuiInvisibleButton(-1, this)).setPosition(this.field_146294_l / 2 + 63, this.field_146295_m - 27, 48, 17));
               return;
            }

            pokemon = (Pokemon)var1.next();
            if (pokemon.getHealth() <= 0) {
               if (!this.faintedOnly) {
                  continue;
               }
            } else if (this.faintedOnly) {
               continue;
            }
            break;
         }

         this.party.add(new PixelmonInGui(pokemon));
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      int width = this.field_146294_l;
      int height = this.field_146295_m;
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)((float)width / 2.0F - 128.0F), (double)(height - 203), 256.0, 203.0F, 0.0, 0.0, 1.0, 0.79296875, this.field_73735_i);
      if (this.mainText != null && !this.mainText.isEmpty()) {
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a(this.mainText, new Object[0]), width / 2 - 40, height - 23, 16777215);
      }

      if (this.backText != null && !this.backText.isEmpty()) {
         if (mouseX > width / 2 + 63 && mouseX < width / 2 + 63 + 48 && mouseY > height - 27 && mouseY < height - 27 + 17) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
            GuiHelper.drawImageQuad((double)((float)width / 2.0F + 63.0F), (double)(height - 27), 48.0, 17.0F, 0.7734375, 0.8203125, 0.9609375, 0.88671875, this.field_73735_i);
         }

         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a(this.backText, new Object[0]), width / 2 + 87, height - 22, 16777215);
      }

      PixelmonInGui mainPokemon = (PixelmonInGui)this.party.get(0);
      if (mainPokemon != null) {
         GuiHelper.bindPokemonSprite(mainPokemon, this.field_146297_k);
         GuiHelper.drawImageQuad((double)(width / 2 - 121), (double)(height - 179), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         GuiBattle.drawHealthBar(width / 2 - 85, height - 135, 56, 9, mainPokemon.health, mainPokemon.maxHealth);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
         GuiHelper.drawImageQuad((double)(width / 2 - 95), (double)(height - 135), 61.0, 9.0F, 0.3359375, 0.9375, 0.57421875, 0.97265625, this.field_73735_i);
         this.func_73732_a(this.field_146297_k.field_71466_p, (int)mainPokemon.health + "/" + mainPokemon.maxHealth, width / 2 - 59, height - 123, 16777215);
         String name = mainPokemon.getDisplayName();
         this.func_73731_b(this.field_146297_k.field_71466_p, name, width / 2 - 90, height - 161, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.overlay1v1.lv", new Object[0]) + mainPokemon.level, width / 2 - 90, height - 148, 16777215);
         float[] texturePair = mainPokemon.getStatusTexturePos();
         if (texturePair[0] != -1.0F) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
            GuiHelper.drawImageQuad((double)((float)width / 2.0F - 117.0F), (double)(height - 145), 20.0, 20.0F, (double)(texturePair[0] / 768.0F), (double)(texturePair[1] / 512.0F), (double)((texturePair[0] + 240.0F) / 768.0F), (double)((texturePair[1] + 240.0F) / 512.0F), this.field_73735_i);
         }

         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
         if (mainPokemon.getGender() == Gender.Male) {
            GuiHelper.drawImageQuad((double)((float)width / 2.0F - 90.0F + (float)this.field_146297_k.field_71466_p.func_78256_a(name)), (double)(height - 161), 6.0, 9.0F, 0.125, 0.8125, 0.1484375, 0.84765625, this.field_73735_i);
         } else if (mainPokemon.getGender() == Gender.Female) {
            GuiHelper.drawImageQuad((double)((float)width / 2.0F - 90.0F + (float)this.field_146297_k.field_71466_p.func_78256_a(name)), (double)(height - 161), 6.0, 9.0F, 0.125, 0.8515625, 0.1484375, 0.88671875, this.field_73735_i);
         }

         if (mouseX > width / 2 - 119 && mouseX < width / 2 - 31 && mouseY > height - 165 && mouseY < height - 113) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.selectCurrentPokemon);
            GuiHelper.drawImageQuad((double)((float)width / 2.0F - 120.0F), (double)(height - 165), 89.0, 52.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }
      }

      for(int i = 1; i < this.party.size(); ++i) {
         PixelmonInGui pig = (PixelmonInGui)this.party.get(i);
         int pos = i - 1;
         if (pig != null) {
            GuiHelper.bindPokemonSprite(pig, this.field_146297_k);
            GuiHelper.drawImageQuad((double)((float)width / 2.0F - 23.0F), (double)(height - 192 + pos * 30), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            GuiBattle.drawHealthBar(width / 2 + 65, height - 192 + pos * 30, 56, 9, pig.health, pig.maxHealth);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
            GuiHelper.drawImageQuad((double)((float)width / 2.0F + 55.0F), (double)(height - 192 + pos * 30), 61.0, 9.0F, 0.3359375, 0.9375, 0.57421875, 0.97265625, this.field_73735_i);
            this.func_73731_b(this.field_146297_k.field_71466_p, (int)pig.health + "/" + pig.maxHealth, width / 2 + 75, height - 180 + pos * 30, 16777215);
            this.func_73731_b(this.field_146297_k.field_71466_p, pig.getDisplayName(), width / 2 + 5, height - 190 + pos * 30, 16777215);
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.overlay1v1.lv", new Object[0]) + pig.level, width / 2 + 5, height - 176 + pos * 30, 16777215);
            float[] texturePair = pig.getStatusTexturePos();
            if (texturePair[0] != -1.0F) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.status);
               GuiHelper.drawImageQuad((double)((float)width / 2.0F + 50.0F), (double)(height - 177 + pos * 26), 14.0, 14.0F, (double)(texturePair[0] / 768.0F), (double)(texturePair[1] / 512.0F), (double)((texturePair[0] + 240.0F) / 768.0F), (double)((texturePair[1] + 240.0F) / 512.0F), this.field_73735_i);
            }

            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.choosePokemon);
            if (pig.getGender() == Gender.Male) {
               GuiHelper.drawImageQuad((double)((float)width / 2.0F + 40.0F), (double)(height - 176 + pos * 30), 6.0, 9.0F, 0.125, 0.8125, 0.1484375, 0.84765625, this.field_73735_i);
            } else if (pig.getGender() == Gender.Female) {
               GuiHelper.drawImageQuad((double)((float)width / 2.0F + 40.0F), (double)(height - 176 + pos * 30), 6.0, 9.0F, 0.125, 0.8515625, 0.1484375, 0.88671875, this.field_73735_i);
            }

            int xPos = width / 2 - 30;
            int yPos = height - 195 + pos * 30;
            if (mouseX > xPos && mouseX < xPos + 150 && mouseY > yPos + 1 && mouseY < yPos + 31) {
               GuiHelper.drawImageQuad((double)xPos, (double)yPos, 150.0, 32.0F, 0.16796875, 0.80078125, 0.7578125, 0.92578125, this.field_73735_i);
            }
         }
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == -1) {
         GuiHelper.closeScreen();
      } else if (button.field_146127_k < this.party.size()) {
         PixelmonInGui pokemon = (PixelmonInGui)this.party.get(button.field_146127_k);
         if (pokemon != null) {
            Pixelmon.network.sendToServer(new SelectPokemonResponse(this.mode, this.npcId, pokemon.pokemonUUID));
         }
      }

   }
}
