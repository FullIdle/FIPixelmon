package com.pixelmonmod.pixelmon.client.gui.pokedex;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.pokedex.PokedexEntry;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiPokedex extends GuiScreen {
   private static ResourceLocation[] textures;
   private static String[] names;
   private Map animations = Maps.newConcurrentMap();
   private int centerW;
   private double scalar = 10900.0;
   private EnumSpecies currentPokemon;
   private List forms = Lists.newArrayList();
   private int currentForm = 0;
   private int currentRow = 0;
   private int dexVersion = 0;
   private EntityPixelmon displayedPokemon;
   private List pokemon = Lists.newArrayList();
   private int currentInfoRow = 0;
   private int currentPage = 0;
   private GuiTextField searchBar;
   private GuiButton upButton;
   private GuiButton downButton;
   private GuiButton dexOptionsMenu;
   private List dexTypeButtons;
   private boolean isDexMenuOpen = false;
   private int dexMenuFrame = 0;

   public GuiPokedex(int pos) {
      this.setCurrentPokemon(EnumSpecies.getFromDex(pos));
      Keyboard.enableRepeatEvents(true);
      if (this.currentPokemon != null && ClientStorageManager.pokedex.get(this.currentPokemon.getNationalPokedexInteger()) == EnumPokedexRegisterStatus.unknown) {
         this.setCurrentPokemon((EnumSpecies)null);
      }

      this.animations.put("open", new AnimationHelper(90, -1, this::drawScreenBackground));
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerW = this.field_146294_l / 2;
      this.searchBar = (new GuiTabCompleteTextField(0, this.field_146297_k.field_71466_p, this.centerW - 84, 141, 150, 10)).setCompletions(EnumSpecies.getNameList());
      this.searchBar.func_146185_a(false);
      int id = 0;
      ++id;
      this.upButton = this.func_189646_b(new GuiButton(id, this.centerW + 69, 153, 10, 24, ""));
      ++id;
      this.downButton = this.func_189646_b(new GuiButton(id, this.centerW + 69, 181, 10, 24, ""));
      ++id;
      this.dexOptionsMenu = this.func_189646_b(new GuiButton(id, this.centerW - 120, 110, 15, 15, ""));
      this.dexTypeButtons = Lists.newArrayList();

      for(int i = 0; i <= 8; ++i) {
         int height = 14;
         ++id;
         this.dexTypeButtons.add(this.func_189646_b(new GuiButton(id, this.centerW - 120, 108 + i * height, 15, height + 1, names[i])));
      }

      this.generatePokemonList();
      this.currentRow = this.pokemon.indexOf(this.currentPokemon) / 7;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.animations.values().stream().filter((anim) -> {
         return anim.getLevel() < 0;
      }).forEach(AnimationHelper::update);
      if (((AnimationHelper)this.animations.get("open")).isComplete()) {
         if (this.displayedPokemon == null && this.currentPokemon != null) {
            this.setupDisplayedPokemon();
         }

         if (!this.animations.containsKey("selection")) {
            this.beginSelectAnimation();
         }

         this.drawBottomPage(mouseX, mouseY, partialTicks);
         this.animations.values().stream().filter((anim) -> {
            return anim.getLevel() >= 0;
         }).sorted(Comparator.comparingInt(AnimationHelper::getLevel)).forEach(AnimationHelper::update);
         if (this.currentPage == 0) {
            this.drawFirstPage(mouseX, mouseY, partialTicks);
         } else if (this.currentPage == 1) {
            this.drawSecondPage(mouseX, mouseY, partialTicks);
         }
      }

   }

   private void drawScreenBackground(int frame) {
      double thing = frame >= 60 ? 46.0 : (frame++ < 20 ? 0.0 : 0.18143666666666666 * Math.pow((double)(frame - 20), 1.5));
      ResourceLocation texture = textures[this.dexVersion];
      if (frame < 60) {
         this.field_146297_k.field_71446_o.func_110577_a(texture);
         GuiHelper.drawImageQuad((double)(this.centerW - 119), 59.0 + thing, 15.08117961883545, 134.0F, 0.4639587998390198, 0.42222222685813904, 0.4988558292388916, 0.9957671761512756, this.field_73735_i);
      } else {
         if (this.isDexMenuOpen) {
            if (this.dexMenuFrame <= 20) {
               ++this.dexMenuFrame;
            }
         } else if (this.dexMenuFrame > 0) {
            --this.dexMenuFrame;
         }

         if (this.dexMenuFrame > 0) {
            func_73734_a(this.centerW - 104 - this.dexMenuFrame, 106, this.centerW - 104, 237, -13421773);
         }

         int xPos = this.centerW - 119 - this.dexMenuFrame;
         this.dexOptionsMenu.field_146128_h = xPos;

         for(int i = 0; i < this.dexTypeButtons.size(); ++i) {
            GuiButton dexTypeButton = (GuiButton)this.dexTypeButtons.get(i);
            dexTypeButton.field_146128_h = xPos + 15;
            dexTypeButton.field_146120_f = (int)((double)this.dexMenuFrame * 1.03);
            if (this.dexMenuFrame != 0) {
               func_73734_a(dexTypeButton.field_146128_h, dexTypeButton.field_146129_i, dexTypeButton.field_146128_h + dexTypeButton.field_146120_f, dexTypeButton.field_146129_i + dexTypeButton.field_146121_g, -9341834);
               if (this.dexVersion != i) {
                  func_73734_a(dexTypeButton.field_146128_h + 1, dexTypeButton.field_146129_i + 1, dexTypeButton.field_146128_h + dexTypeButton.field_146120_f - 1, dexTypeButton.field_146129_i + dexTypeButton.field_146121_g - 1, -11118503);
               }

               GuiHelper.drawCenteredString(dexTypeButton.field_146126_j, Math.max((float)this.centerW - 97.0F - (float)this.dexMenuFrame, (float)this.centerW - 113.5F), (float)(dexTypeButton.field_146129_i + 4), -1);
            }
         }

         this.field_146297_k.field_71446_o.func_110577_a(texture);
         GuiHelper.drawImageQuad((double)xPos, 105.0, 15.08117961883545, 134.0F, 0.4639587998390198, 0.42222222685813904, 0.4988558292388916, 0.9957671761512756, this.field_73735_i);
      }

      GuiHelper.drawImageQuad((double)(this.centerW - 104), 59.0 + thing, 215.09225463867188, 134.0F, 0.5005720853805542, 0.42222222685813904, 0.9982837438583374, 0.9957671761512756, this.field_73735_i);
      float start = this.currentPage != 0 ? 0.0F : 0.50476193F;
      GuiHelper.drawImageQuad((double)((float)this.centerW - 103.5F), 59.0 - thing, 199.0, 115.69193F, 0.0, (double)start, 0.46052631735801697, (double)(start + 0.4952381F), this.field_73735_i);
   }

   private void drawFirstPage(int mouseX, int mouseY, float partialTicks) {
      if (this.currentPokemon != null && Pokedex.fullPokedex.containsKey(this.currentPokemon.getNationalPokedexInteger())) {
         PokedexEntry entry = (PokedexEntry)Pokedex.fullPokedex.get(this.currentPokemon.getNationalPokedexInteger());
         String nameS = this.displayedPokemon.getLocalizedName();
         if (this.displayedPokemon.getPokemonData().getForm() > 0) {
            nameS = nameS + " " + this.displayedPokemon.getPokemonData().getFormEnum().getLocalizedName();
         }

         this.field_146297_k.field_71466_p.func_78276_b(nameS + " #" + entry.getPokedexDisplayNumber(), this.centerW - 86, 25, 16777215);

         for(int i = 0; i < this.displayedPokemon.getPokemonData().getBaseStats().getTypeList().size(); ++i) {
            EnumType type = (EnumType)this.displayedPokemon.getPokemonData().getBaseStats().getTypeList().get(i);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
            float x = type.textureX;
            float y = type.textureY;
            GuiHelper.drawImageQuad((double)(this.centerW + 67 - 13 * i), 24.0, 10.0, 10.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
         }

         GuiHelper.drawScaledString(this.currentPokemon.getBaseStats(this.currentForm - 1).getHeight() + I18n.func_74838_a("gui.pokedex.meters"), (float)(this.centerW - 85), 40.0F, 16777215, 10.0F);
         GuiHelper.drawScaledStringRightAligned(this.currentPokemon.getBaseStats(this.currentForm - 1).getWeight() + I18n.func_74838_a("gui.pokedex.kilograms"), (float)(this.centerW - 29), 40.0F, 16777215, false, 10.0F);
         GuiHelper.drawScaledCenteredString(net.minecraft.client.resources.I18n.func_135052_a("gui.pokedex.description", new Object[0]), (float)(this.centerW + 28), 40.0F, 16777215, 14.0F);
         String s;
         if (ClientStorageManager.pokedex.get(this.currentPokemon.getNationalPokedexInteger()) == EnumPokedexRegisterStatus.caught) {
            String key = this.currentPokemon.getFormEnum(this.currentForm).getUnlocalizedName() + ".description";
            s = net.minecraft.client.resources.I18n.func_135052_a(key, new Object[0]);
            if (s.equals(key)) {
               s = net.minecraft.client.resources.I18n.func_135052_a("pixelmon." + this.currentPokemon.name.replace(" ", "").toLowerCase() + ".description", new Object[0]);
            }
         } else {
            s = "???";
         }

         s = GuiHelper.splitStringToFit(s, 10, 97);

         for(int i = 0; i < s.split("\n").length; ++i) {
            String s1 = s.split("\n")[i];
            GuiHelper.drawScaledString(s1, (float)(this.centerW - 22), 55.0F + (float)this.field_146297_k.field_71466_p.field_78288_b * 0.625F * (float)i, 16777215, 8.0F);
         }

         if (ClientStorageManager.pokedex.get(this.currentPokemon.getNationalPokedexInteger()) == EnumPokedexRegisterStatus.caught) {
            if (this.currentForm > 0) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedexBack);
               GuiHelper.drawImageQuad((double)(this.centerW - 88), 102.0, 15.0, 15.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }

            if (((Pokemon)this.forms.get(this.currentForm)).isShiny()) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedexStar);
               GuiHelper.drawImageQuad((double)(this.centerW - 86 + this.field_146297_k.field_71466_p.func_78256_a(nameS)), 25.0, 3.5, 3.5F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }

            if (this.currentForm + 1 < this.forms.size()) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedexForward);
               GuiHelper.drawImageQuad((double)(this.centerW - 44), 102.0, 15.0, 15.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }
         }

         this.drawEntityToScreen(this.centerW - 58, 105, 50, this.displayedPokemon, mouseX, partialTicks);
      }
   }

   private void drawSecondPage(int mouseX, int mouseY, float partialTicks) {
      if (this.currentPokemon != null) {
         PokedexEntry entry = (PokedexEntry)Pokedex.fullPokedex.get(this.currentPokemon.getNationalPokedexInteger());
         this.field_146297_k.field_71466_p.func_78276_b(this.displayedPokemon.getLocalizedName() + " #" + entry.getPokedexDisplayNumber(), this.centerW - 86, 25, 16777215);

         for(int i = 0; i < this.displayedPokemon.getPokemonData().getBaseStats().getTypeList().size(); ++i) {
            EnumType type = (EnumType)this.displayedPokemon.getPokemonData().getBaseStats().getTypeList().get(i);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
            float x = type.textureX;
            float y = type.textureY;
            GuiHelper.drawImageQuad((double)(this.centerW + 67 - 13 * i), 24.0, 10.0, 10.0F, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), this.field_73735_i);
         }

         GuiHelper.drawScaledCenteredString(net.minecraft.client.resources.I18n.func_135052_a("gui.pokedex.spawns", new Object[0]), (float)this.centerW, 40.0F, 16777215, 14.0F);
         List spawnInfo = this.getSpawnInfo();

         for(int i = this.currentInfoRow; i < Math.min(this.currentInfoRow + 5, spawnInfo.size()); ++i) {
            String s = GuiHelper.splitStringToFit((String)spawnInfo.get(i), 9, 155);

            for(int j = 0; j < s.split("\n").length; ++j) {
               String s1 = s.split("\n")[j];
               GuiHelper.drawScaledString(s1, (float)(this.centerW - 86), 55.0F + (float)this.field_146297_k.field_71466_p.field_78288_b * 0.5625F * (float)j + (float)((i - this.currentInfoRow) * 9), 16777215, 9.0F);
            }
         }

      }
   }

   private void drawBottomPage(int mouseX, int mouseY, float partialTicks) {
      this.searchBar.func_146194_f();

      for(int i = this.currentRow * 7; 0 <= i && i < Math.min(this.pokemon.size(), this.currentRow * 7 + 21); ++i) {
         int j = i - this.currentRow * 7;
         int x = (int)((double)this.centerW + (double)(j % 7) * 21.9 - 86.0);
         int y = j / 7 * 19 + 148;
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.getPokemonSprite((EnumSpecies)this.pokemon.get(i), 0, Gender.Male, "", false));
         EnumPokedexRegisterStatus status = ClientStorageManager.pokedex.get(((EnumSpecies)this.pokemon.get(i)).getNationalPokedexInteger());
         if (status == EnumPokedexRegisterStatus.unknown) {
            GlStateManager.func_179124_c(0.0F, 0.0F, 0.0F);
         }

         if (this.currentPokemon != this.pokemon.get(i)) {
            GuiHelper.drawImageQuad((double)x, (double)y, 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         if (status == EnumPokedexRegisterStatus.seen) {
            GlStateManager.func_179124_c(0.0F, 0.0F, 0.0F);
         }

         if (status != EnumPokedexRegisterStatus.unknown) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedexCaught);
            GuiHelper.drawImageQuad((double)(x + 15), (double)y + 3.5, 5.0, 5.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
         if (x <= mouseX && mouseX <= x + 20 && y + 4 <= mouseY && mouseY <= y + 16 && status != EnumPokedexRegisterStatus.unknown) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonCreativeInventory);
            GuiHelper.drawImageQuad((double)(x - 2), (double)(y + 2), 24.0, 20.0F, 0.2265625, 0.72265625, 0.3046875, 0.80078125, this.field_73735_i);
         }
      }

      String s = net.minecraft.client.resources.I18n.func_135052_a("gui.pokedex.caught", new Object[0]) + " " + ClientStorageManager.pokedex.countCaught(this.dexVersion);
      GuiHelper.drawScaledString(s, (float)(this.centerW - 47), 211.0F, 16777215, 8.0F);
      s = net.minecraft.client.resources.I18n.func_135052_a("gui.pokedex.seen", new Object[0]) + " " + ClientStorageManager.pokedex.countSeen(this.dexVersion);
      GuiHelper.drawScaledString(s, (float)(this.centerW - 5), 211.0F, 16777215, 8.0F);
      s = net.minecraft.client.resources.I18n.func_135052_a("gui.pokedex.completion", new Object[0]) + " " + (int)((double)ClientStorageManager.pokedex.countCaught(this.dexVersion) / (double)Pokedex.pokedexSize * 100.0) + "%";
      GuiHelper.drawScaledString(s, (float)(this.centerW + 40), 211.0F, 16777215, 8.0F);
   }

   protected void func_73869_a(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
      super.func_73869_a(p_keyTyped_1_, p_keyTyped_2_);
      if (this.searchBar.func_146201_a(p_keyTyped_1_, p_keyTyped_2_)) {
         this.generatePokemonList();
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button == this.upButton) {
         this.currentRow = Math.max(0, this.currentRow - 1);
      } else if (button == this.downButton) {
         this.currentRow = Math.min((int)Math.ceil((double)this.pokemon.size() / 7.0) - 1, this.currentRow + 1);
      } else if (button == this.dexOptionsMenu) {
         this.isDexMenuOpen = !this.isDexMenuOpen;
      } else if (this.dexTypeButtons.contains(button)) {
         this.dexVersion = this.dexTypeButtons.indexOf(button);
         this.generatePokemonList();
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int button) throws IOException {
      if (((AnimationHelper)this.animations.get("open")).isComplete()) {
         if (!this.searchBar.func_146192_a(mouseX, mouseY, button)) {
            super.func_73864_a(mouseX, mouseY, button);
            int centerX = this.field_146294_l / 2;
            int mouseCenterX = mouseX - centerX;
            if (mouseCenterX >= -86 && mouseCenterX <= 72 && mouseY >= 148 && mouseY <= 202) {
               int i = (int)((double)((this.currentRow + (mouseY - 148) / 19) * 7) + (double)(mouseX - (centerX - 86)) / 21.9);
               if (i >= 0 && i < this.pokemon.size() && ClientStorageManager.pokedex.get(((EnumSpecies)this.pokemon.get(i)).getNationalPokedexInteger()) != EnumPokedexRegisterStatus.unknown) {
                  this.currentInfoRow = 0;
                  this.setCurrentPokemon((EnumSpecies)this.pokemon.get(i));
                  this.field_146297_k.func_147118_V().func_147682_a(PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
                  this.beginSelectAnimation();
               }
            }

            if (this.currentPage == 0 && this.currentPokemon != null && this.displayedPokemon != null && this.displayedPokemon.getPokemonData() != null) {
               if (ClientStorageManager.pokedex.get(this.currentPokemon.getNationalPokedexInteger()) == EnumPokedexRegisterStatus.caught) {
                  if (this.currentForm - 1 >= 0 && mouseCenterX >= -88 && mouseCenterX <= -74 && mouseY >= 105 && mouseY <= 120) {
                     --this.currentForm;
                     this.setupDisplayedPokemon();
                  } else if (this.currentForm + 1 < this.forms.size() && mouseCenterX >= -42 && mouseCenterX <= -26 && mouseY >= 105 && mouseY <= 120) {
                     ++this.currentForm;
                     this.setupDisplayedPokemon();
                  }
               }

               if (mouseCenterX >= 54 && mouseCenterX <= 80 && mouseY >= 100 && mouseY <= 125) {
                  ++this.currentPage;
               }
            } else if (mouseCenterX >= -81 && mouseCenterX <= -55 && mouseY >= 100 && mouseY <= 125) {
               --this.currentPage;
            }

         }
      }
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int i = Mouse.getEventDWheel();
      int mouseX = Mouse.getEventX() * this.field_146294_l / this.field_146297_k.field_71443_c;
      int mouseY = this.field_146295_m - Mouse.getEventY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1;
      int mouseCenterX = mouseX - this.field_146294_l / 2;
      int temp;
      if (mouseCenterX >= -86 && mouseCenterX <= 72 && mouseY >= 148 && mouseY <= 202) {
         temp = this.currentRow;
         if (i > 0) {
            this.currentRow = Math.max(0, this.currentRow - 1);
         } else if (i < 0) {
            this.currentRow = Math.min((int)Math.ceil((double)this.pokemon.size() / 7.0) - 1, this.currentRow + 1);
         }
      } else if (this.currentPage == 1 && mouseCenterX >= -90 && mouseCenterX <= 88 && mouseY >= 40 && mouseY <= 85) {
         temp = this.currentInfoRow;
         if (i > 0) {
            this.currentInfoRow = Math.max(0, this.currentInfoRow - 1);
         } else if (i < 0) {
            this.currentInfoRow = Math.min(Math.max(this.getSpawnInfo().size() - 5, 0), this.currentInfoRow + 1);
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void setCurrentPokemon(EnumSpecies species) {
      this.currentPokemon = species;
      this.currentForm = 0;
      if (species == null) {
         this.forms = Lists.newArrayList();
      } else {
         this.forms = (List)species.getPossibleForms(true).stream().map((form) -> {
            Pokemon pokemon = Pixelmon.pokemonFactory.create(species);
            pokemon.setShiny(false);
            pokemon.setForm(form);
            return pokemon;
         }).collect(Collectors.toList());

         for(int i = 0; i < this.forms.size(); ++i) {
            Pokemon basePokemon = (Pokemon)this.forms.get(i);
            if (basePokemon.getFormEnum().getFormSuffix(true) == basePokemon.getFormEnum().getFormSuffix(false)) {
               Pokemon pokemon = Pixelmon.pokemonFactory.create(species);
               pokemon.setForm(basePokemon.getFormEnum());
               pokemon.setShiny(true);
               this.forms.add(i + 1, pokemon);
               ++i;
            }
         }
      }

      this.setupDisplayedPokemon();
   }

   public void beginSelectAnimation() {
      int i = this.pokemon.indexOf(this.currentPokemon);
      if (i != -1) {
         this.animations.put("selection", new AnimationHelper(4, (frame) -> {
            int row = i / 7;
            if (row <= this.currentRow + 2 && row >= this.currentRow) {
               int j = i - this.currentRow * 7;
               int x = (int)((double)(this.field_146294_l / 2) + (double)(j % 7) * 21.9 - 86.0);
               int y = j / 7 * 19 + 148;
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonCreativeInventory);
               GuiHelper.drawImageQuad((double)(x - 2), (double)(y + 2), 24.0, 20.0F, 0.2265625, 0.72265625, 0.3046875, 0.80078125, this.field_73735_i);
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.getPokemonSprite(this.currentPokemon, 0, Gender.Male, "", false));
               float offset = (float)frame / 2.0F;
               GuiHelper.drawImageQuad((double)((float)x - offset), (double)((float)y - offset), (double)(20.0F + offset * 2.0F), 20.0F + offset * 2.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }
         }));
      }

   }

   public void generatePokemonList() {
      this.pokemon = this.getPokemonList();
      if (!this.pokemon.contains(this.currentPokemon)) {
         this.setCurrentPokemon((EnumSpecies)this.pokemon.stream().filter((species) -> {
            return ClientStorageManager.pokedex.isRegistered(species);
         }).findFirst().orElse((Object)null));
      }

      if (this.currentPokemon != null) {
         this.currentRow = this.pokemon.indexOf(this.currentPokemon) / 7;
      } else {
         this.currentRow = 0;
      }

      this.animations.remove("selection");
      this.displayedPokemon = null;
   }

   private List getPokemonList() {
      List species = Lists.newArrayList();
      EnumSpecies[] var2 = Pokedex.actualPokedex;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumSpecies actualPokedex = var2[var4];
         if (actualPokedex.name.toLowerCase().startsWith(this.searchBar.func_146179_b().toLowerCase()) && (this.dexVersion == 0 || this.dexVersion == actualPokedex.getGeneration())) {
            species.add(actualPokedex);
         }
      }

      species.sort(Comparator.naturalOrder());
      return species;
   }

   private void setupDisplayedPokemon() {
      if (this.field_146297_k != null) {
         if (this.forms.isEmpty()) {
            this.displayedPokemon = null;
         } else {
            IncrementingVariable variable = null;
            if (this.displayedPokemon != null && this.displayedPokemon.getSpecies() == this.currentPokemon) {
               variable = this.displayedPokemon.getAnimationVariables().getCounter(-1);
            }

            Pokemon pokemon = Pixelmon.pokemonFactory.create(((Pokemon)this.forms.get(this.currentForm)).getSpecies());
            if (((Pokemon)this.forms.get(this.currentForm)).getFormEnum() != null) {
               pokemon.setForm(((Pokemon)this.forms.get(this.currentForm)).getFormEnum());
            }

            pokemon.setGrowth(EnumGrowth.Ordinary);
            pokemon.setShiny(((Pokemon)this.forms.get(this.currentForm)).isShiny());
            this.displayedPokemon = new EntityPixelmon(this.field_146297_k.field_71441_e);
            this.displayedPokemon.setPokemon(pokemon);
            this.displayedPokemon.setAnimation(AnimationType.IDLE);
            this.displayedPokemon.checkAnimation();
            this.displayedPokemon.initAnimation();
            if (variable != null && this.displayedPokemon.getAnimationVariables().getCounter(-1) != null) {
               this.displayedPokemon.getAnimationVariables().getCounter(-1).value = variable.value;
            }

            double referenceW = 40.0;
            double referenceH = 40.0;
            double dh = this.displayedPokemon.getBaseStats().getBoundsData().getHeight() - referenceH;
            double dw = this.displayedPokemon.getBaseStats().getBoundsData().getWidth() - referenceW;
            if (dh > dw) {
               this.scalar = referenceH / this.displayedPokemon.getBaseStats().getBoundsData().getHeight();
            } else {
               this.scalar = referenceW / this.displayedPokemon.getBaseStats().getBoundsData().getWidth();
            }

            this.currentPage = 0;
         }
      }
   }

   private List getSpawnInfo() {
      return (List)ClientStorageManager.pokedex.getSpawnData(this.currentPokemon.getNationalPokedexInteger()).stream().map(Object::toString).collect(Collectors.toList());
   }

   public void func_73876_c() {
      if (this.displayedPokemon != null && this.displayedPokemon.getAnimationVariables() != null) {
         this.displayedPokemon.getAnimationVariables().tick();
      }

   }

   private void drawEntityToScreen(int x, int y, int l, EntityPixelmon e, int mouseX, float pt) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179142_g();
      GlStateManager.func_179126_j();
      GlStateManager.func_179109_b((float)x, (float)y, 100.0F);
      if (l != 0) {
         GlStateManager.func_179139_a(this.scalar, this.scalar, this.scalar);
      }

      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179114_b((float)(x - mouseX) / 2.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();

      try {
         RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
         Render entityClassRenderObject = renderManager.func_78715_a(EntityPixelmon.class);
         RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
         rp.renderPixelmon(e, 0.0, 0.0, 0.0, pt, true);
         renderManager.field_78735_i = 180.0F;
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      GlStateManager.func_179114_b(360.0F - (float)(x - mouseX) / 2.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      if (l != 0) {
         GlStateManager.func_179139_a(1.0 / this.scalar, 1.0 / this.scalar, 1.0 / this.scalar);
      }

      GlStateManager.func_179121_F();
   }

   static {
      String prefix = "pixelmon:textures/gui/pokedex/";
      textures = new ResourceLocation[]{new ResourceLocation(prefix + "pokedex.png"), new ResourceLocation(prefix + "pokedex_blue.png"), new ResourceLocation(prefix + "pokedex_gold.png"), new ResourceLocation(prefix + "pokedex_green.png"), new ResourceLocation(prefix + "pokedex_pink.png"), new ResourceLocation(prefix + "pokedex_grey.png"), new ResourceLocation(prefix + "pokedex_orange.png"), new ResourceLocation(prefix + "pokedex_purple.png"), new ResourceLocation(prefix + "pokedex_blue_gold.png")};
      names = new String[]{"N", "RB", "GS", "RS", "DP", "BW", "XY", "SM", "SS"};
   }
}
