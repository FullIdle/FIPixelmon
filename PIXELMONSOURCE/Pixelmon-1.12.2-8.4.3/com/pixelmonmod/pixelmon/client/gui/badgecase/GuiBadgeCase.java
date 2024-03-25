package com.pixelmonmod.pixelmon.client.gui.badgecase;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.badgecase.BadgeCaseActionPacket;
import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBadgeCase extends GuiScreen {
   private ResourceLocation upTexture;
   private ResourceLocation downTexture;
   private ResourceLocation registerTexture;
   private ResourceLocation badgeCaseTexture;
   private GuiButton buttonUp;
   private GuiButton buttonRegister;
   private GuiButton buttonDown;
   private final GuiButton[] badgeButtons = new GuiButton[8];
   private int page = 0;
   private int selectedBadge = -1;
   private String owner;
   boolean allowChanges;
   EnumBadgeCase color;
   List badges;
   public Pokemon[] party = new Pokemon[6];
   private boolean custom;
   private static final int SPRITE_HEIGHT = 40;
   private static final int SPRITE_WIDTH = 40;
   private static final int POKEBALL_HEIGHT = 43;
   private static final int POKEBALL_WIDTH = 44;

   public GuiBadgeCase(String owner, boolean allowChanges, boolean custom, EnumBadgeCase color, List badges, Pokemon[] party) {
      this.updateBadgeCase(owner, allowChanges, custom, color, badges, party);
   }

   public void func_73866_w_() {
      int buttonID = 0;
      int xOffset = (this.field_146294_l - 252) / 2;
      int yOffset = 2;
      this.buttonUp = new GuiButton(buttonID++, xOffset + 37, yOffset + 208, 42, 20, "");
      this.buttonDown = new GuiButton(buttonID++, xOffset + 79, yOffset + 208, 42, 20, "");
      this.buttonRegister = new GuiButton(buttonID++, xOffset + 37, yOffset + 7, 80, 20, "");
      int[] xvalue = new int[]{49, 91, 133, 175};

      int i;
      for(i = 0; i < 4; ++i) {
         this.badgeButtons[i] = new GuiButton(buttonID++, xOffset + xvalue[i], yOffset + 135, 32, 22, "");
      }

      for(i = 4; i < 8; ++i) {
         this.badgeButtons[i] = new GuiButton(buttonID++, xOffset + xvalue[i - 4], yOffset + 169, 32, 22, "");
      }

      this.field_146292_n.add(this.buttonUp);
      this.field_146292_n.add(this.buttonDown);
      this.field_146292_n.add(this.buttonRegister);
      this.field_146292_n.addAll(Arrays.asList(this.badgeButtons));
      if (!this.allowChanges) {
         this.buttonRegister.field_146124_l = false;
         Arrays.stream(this.badgeButtons).forEach((badge) -> {
            badge.field_146124_l = false;
         });
      }

      if (this.owner != null && !this.owner.isEmpty()) {
         this.buttonRegister.field_146124_l = false;
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      int guiWidth = true;
      int guiHeight = true;
      int xOffset = (this.field_146294_l - 205) / 2;
      int yOffset = 2;
      this.field_146297_k.field_71446_o.func_110577_a(this.badgeCaseTexture);
      Gui.func_146110_a(xOffset, yOffset, 0.0F, 0.0F, 210, 235, 210.0F, 235.0F);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.upTexture);
      Gui.func_146110_a(xOffset + 17, yOffset + 208, 0.0F, 0.0F, 40, 14, 40.0F, 14.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.downTexture);
      Gui.func_146110_a(xOffset + 59, yOffset + 208, 0.0F, 0.0F, 40, 14, 40.0F, 14.0F);
      if (this.buttonRegister.field_146124_l) {
         this.field_146297_k.field_71446_o.func_110577_a(this.registerTexture);
         Gui.func_146110_a(xOffset + 16, yOffset + 10, 0.0F, 0.0F, 80, 14, 80.0F, 14.0F);
      }

      int textColor;
      switch (this.color) {
         case Black:
            textColor = 9539985;
            break;
         case Blue:
            if (this.buttonRegister.field_146124_l) {
               textColor = 1645055;
            } else {
               textColor = 35020;
            }
            break;
         case Green:
            textColor = 13064;
            break;
         case Pink:
            if (!this.buttonRegister.field_146124_l) {
               textColor = 16751103;
            } else {
               textColor = 15073510;
            }
            break;
         case Red:
            if (this.buttonRegister.field_146124_l) {
               textColor = 11730944;
            } else {
               textColor = 16731469;
            }
            break;
         case White:
         case Yellow:
            textColor = 0;
            break;
         default:
            textColor = 1048575;
      }

      if (this.buttonRegister.field_146124_l) {
         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.badgecase.register", new Object[0]), xOffset + 55, yOffset + 13, textColor);
      } else {
         this.func_73732_a(this.field_146297_k.field_71466_p, this.owner, xOffset + 55, yOffset + 13, textColor);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      int firstRowHeight = 32;
      int secondRowHeight = 64;
      int collum = 17;
      int offset = 27;
      this.drawPokemon(this.party[0], xOffset, yOffset, collum, firstRowHeight, collum + 2, firstRowHeight + 2);
      this.drawPokemon(this.party[3], xOffset, yOffset, collum += offset, secondRowHeight, collum + 2, secondRowHeight + 2);
      this.drawPokemon(this.party[1], xOffset, yOffset, collum += offset, firstRowHeight, collum + 2, firstRowHeight + 2);
      this.drawPokemon(this.party[4], xOffset, yOffset, collum += offset, secondRowHeight, collum + 2, secondRowHeight + 2);
      this.drawPokemon(this.party[2], xOffset, yOffset, collum += offset, firstRowHeight, collum + 2, firstRowHeight + 2);
      this.drawPokemon(this.party[5], xOffset, yOffset, collum += offset, secondRowHeight, collum + 2, secondRowHeight + 2);
      int count = 0;
      GuiButton[] var14 = this.badgeButtons;
      int var15 = var14.length;

      int rowY;
      for(rowY = 0; rowY < var15; ++rowY) {
         GuiButton button = var14[rowY];
         if (this.selectedBadge == this.page * 8 + count) {
            func_73734_a(button.field_146128_h, button.field_146129_i, button.field_146128_h + button.field_146120_f, button.field_146129_i + button.field_146121_g, 1191182335);
         }

         if (this.page * 8 + count < this.badges.size() && button.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
            func_73734_a(button.field_146128_h, button.field_146129_i, button.field_146128_h + button.field_146120_f, button.field_146129_i + button.field_146121_g, 855638015);
         }

         ++count;
      }

      int i;
      for(i = 0; i < 8 && this.page * 8 + i != this.badges.size(); ++i) {
         int rowX = 34;
         rowY = i < 4 ? 137 : 170;
         int rowOffset = 42;
         this.field_146296_j.func_180450_b((ItemStack)this.badges.get(this.page * 8 + i), xOffset + rowX + (i + 1 <= 4 ? i * rowOffset : (i - 4) * rowOffset), yOffset + rowY);
      }

      for(i = 0; i < 8; ++i) {
         if (this.page * 8 + i < this.badges.size() && this.badgeButtons[i].func_146116_c(this.field_146297_k, mouseX, mouseY)) {
            ItemStack badge = (ItemStack)this.badges.get(this.page * 8 + i);
            List lore = Lists.newArrayList();
            if (badge.func_82837_s()) {
               lore.add(badge.func_82833_r());
            }

            if (badge.func_179543_a("display") != null && badge.func_179543_a("display").func_150297_b("Lore", 9)) {
               NBTTagList loreList = badge.func_179543_a("display").func_150295_c("Lore", 8);
               if (!loreList.func_82582_d()) {
                  for(int l1 = 0; l1 < loreList.func_74745_c(); ++l1) {
                     lore.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + loreList.func_150307_f(l1));
                  }
               }
            }

            if (!lore.isEmpty()) {
               this.func_146283_a(lore, mouseX, mouseY);
            }
         }
      }

   }

   protected void actionPerformed(GuiButton button, int mouseButton) throws IOException {
      if (button == this.buttonUp) {
         this.pageUp();
      } else if (button == this.buttonDown) {
         this.pageDown();
      } else if (this.allowChanges) {
         if (button == this.buttonRegister) {
            if (this.buttonRegister.field_146124_l) {
               Pixelmon.network.sendToServer(new BadgeCaseActionPacket(BadgeCaseActionPacket.Action.REGISTER, this.custom));
               this.buttonRegister.field_146124_l = false;
            }
         } else {
            for(int i = 0; i < this.badgeButtons.length; ++i) {
               if (button == this.badgeButtons[i] && this.badges.size() >= this.page * 8 + 1) {
                  if (mouseButton == 0) {
                     if (this.selectedBadge == -1) {
                        this.selectedBadge = this.page * 8 + i;
                     } else {
                        Pixelmon.network.sendToServer(new BadgeCaseActionPacket(this.selectedBadge, this.page * 8 + i, this.custom));
                        this.selectedBadge = -1;
                     }
                  } else if (mouseButton == 1) {
                     Pixelmon.network.sendToServer(new BadgeCaseActionPacket(this.page * 8 + i, this.custom));
                     this.selectedBadge = -1;
                  }
               }
            }
         }

      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      for(int i = 0; i < this.field_146292_n.size(); ++i) {
         GuiButton guibutton = (GuiButton)this.field_146292_n.get(i);
         if (guibutton.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
            GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.field_146292_n);
            if (MinecraftForge.EVENT_BUS.post(event)) {
               break;
            }

            guibutton = event.getButton();
            this.field_146290_a = guibutton;
            guibutton.func_146113_a(this.field_146297_k.func_147118_V());
            this.actionPerformed(guibutton, mouseButton);
            if (this.equals(this.field_146297_k.field_71462_r)) {
               MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.field_146292_n));
            }
         }
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (this.field_146297_k.field_71474_y.field_151445_Q.isActiveAndMatches(keyCode)) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
         if (this.field_146297_k.field_71462_r == null) {
            this.field_146297_k.func_71381_h();
         }
      } else {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   private void pageDown() {
      if ((this.page + 1) * 8 < this.badges.size()) {
         ++this.page;
      }
   }

   private void pageUp() {
      if (this.page != 0) {
         --this.page;
      }
   }

   private void drawPokemon(Pokemon pokemon, int xOffset, int yOffset, int ballX, int ballY, int pokeX, int pokeY) {
      if (pokemon != null) {
         GuiHelper.bindPokeballTexture(pokemon.getCaughtBall());
         Gui.func_152125_a(xOffset + ballX, yOffset + ballY, 0.0F, 0.0F, 44, 43, 36, 36, 44.0F, 43.0F);
         GuiHelper.bindPokemonSprite(pokemon, this.field_146297_k);
         Gui.func_152125_a(xOffset + pokeX, yOffset + pokeY - 3, 0.0F, 0.0F, 40, 40, 36, 36, 40.0F, 40.0F);
      }

   }

   public void updateBadgeCase(String owner, boolean allowChanges, boolean custom, EnumBadgeCase color, List badges, Pokemon[] party) {
      this.owner = owner;
      this.allowChanges = allowChanges;
      this.custom = custom;
      this.color = color;
      this.badges = badges;
      if (party.length == 6) {
         this.party = party;
      }

      this.badgeCaseTexture = new ResourceLocation(GuiResources.prefix + "gui/badgecases/badgecase_" + color.toString().toLowerCase() + ".png");
      this.upTexture = new ResourceLocation(GuiResources.prefix + "gui/badgecases/up_button_" + color.toString().toLowerCase() + ".png");
      this.downTexture = new ResourceLocation(GuiResources.prefix + "gui/badgecases/down_button_" + color.toString().toLowerCase() + ".png");
      this.registerTexture = new ResourceLocation(GuiResources.prefix + "gui/badgecases/register_" + color.toString().toLowerCase() + ".png");
   }
}
