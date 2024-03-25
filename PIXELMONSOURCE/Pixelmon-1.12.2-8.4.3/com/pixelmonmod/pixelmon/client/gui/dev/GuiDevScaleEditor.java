package com.pixelmonmod.pixelmon.client.gui.dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelLabel;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.custom.FontRendererPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLoader;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class GuiDevScaleEditor extends GuiScreenDropDown {
   private int centerW;
   private int centerH;
   private float oldMouseX;
   private float oldMouseY;
   private GuiTextField species;
   private GuiTextField textWidth;
   private GuiTextField textHeight;
   private GuiTextField eye;
   private GuiTextField hover;
   private GuiDropDown dropDownForms;
   private int viewMode;
   private EntityStatue currentPixelmon;

   public GuiDevScaleEditor(PokemonSpec spec) {
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.viewMode = 0;
      this.field_146297_k = Minecraft.func_71410_x();
      Pokemon pokemon = Pixelmon.pokemonFactory.create(spec);
      this.currentPixelmon = new EntityStatue(this.field_146297_k.field_71441_e);
      this.currentPixelmon.setPokemon(pokemon);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146293_o.clear();
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.species = (new GuiTabCompleteTextField(0, FontRendererPixelmon.getInstance(), this.centerW - 150, 20, 100, 20)).setCompletions(EnumSpecies.getNameList());
      this.textWidth = new GuiTextField(0, FontRendererPixelmon.getInstance(), 62, this.centerH + 20, 100, 20);
      this.textHeight = new GuiTextField(0, FontRendererPixelmon.getInstance(), 62, this.centerH + 45, 100, 20);
      this.eye = new GuiTextField(0, FontRendererPixelmon.getInstance(), 62, this.centerH + 70, 100, 20);
      this.hover = new GuiTextField(0, FontRendererPixelmon.getInstance(), 62, this.centerH + 95, 100, 20);
      AnimationType type = this.currentPixelmon.getCurrentAnimation();
      this.field_146293_o.add(new GuiPixelLabel("Pokemon", 0, 50, this.centerH, 20, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel("Data", 0, 50, this.centerH, 20, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel(type.name(), 0, this.field_146294_l - 280, this.field_146295_m - 60, 20, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel("Width: ", 0, 25, this.centerH + 20, 20, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel("Height: ", 0, 25, this.centerH + 45, 20, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel("Eye Height: ", 0, 20, this.centerH + 70, 30, 20, -252645136));
      this.field_146293_o.add(new GuiPixelLabel("Hover: ", 0, 25, this.centerH + 95, 20, 20, -252645136));
      this.field_146292_n.add(new GuiButton(0, this.field_146294_l - 120, this.field_146295_m - 40, 100, 20, "Save"));
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l - 40 - 20, 20, 40, 20, "->"));
      this.field_146292_n.add(new GuiButton(2, 20, 20, 40, 20, "<-"));
      this.field_146292_n.add(new GuiButton(3, 164, this.centerH + 97, 60, 20, "Apply"));
      this.field_146292_n.add(new GuiButton(4, this.field_146294_l - 280, this.field_146295_m - 40, 40, 20, "Cycle"));
      this.field_146292_n.add(new GuiButton(5, this.field_146294_l - 240, this.field_146295_m - 40, 40, 20, "Animate"));
      this.field_146292_n.add(new GuiButton(6, this.field_146294_l - 80, this.centerH - 40, 40, 20, "Mouse"));
      this.field_146292_n.add(new GuiButton(7, this.field_146294_l - 80, this.centerH - 20, 20, 20, "F"));
      this.field_146292_n.add(new GuiButton(8, this.field_146294_l - 60, this.centerH - 20, 20, 20, "B"));
      this.field_146292_n.add(new GuiButton(9, this.field_146294_l - 80, this.centerH, 20, 20, "L"));
      this.field_146292_n.add(new GuiButton(10, this.field_146294_l - 60, this.centerH, 20, 20, "R"));
      this.field_146292_n.add(new GuiButton(11, this.field_146294_l - 80, this.centerH + 20, 20, 20, "T"));
      this.field_146292_n.add(new GuiButton(12, this.field_146294_l - 60, this.centerH + 20, 20, 20, "B"));
      this.setPokemon(this.currentPixelmon.getSpecies(), this.currentPixelmon.getFormEnum());
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.species.func_146194_f();
      this.textWidth.func_146194_f();
      this.textHeight.func_146194_f();
      this.eye.func_146194_f();
      this.hover.func_146194_f();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.currentPixelmon.updateSize();
      double modelHeight = this.currentPixelmon.getModelSize().getY();
      float m1 = (float)this.centerW - this.oldMouseX;
      float m2 = (float)this.centerH - this.oldMouseY;
      float factor = 120.0F;
      if (this.viewMode == 1) {
         m1 = 0.0F;
         m2 = 0.0F;
      } else if (this.viewMode == 2) {
         m1 = 60.0F;
         m2 = 0.0F;
         factor = 180.0F;
      } else if (this.viewMode == 3) {
         m1 = 40.0F;
         m2 = 0.0F;
      } else if (this.viewMode == 4) {
         m1 = -40.0F;
         m2 = 0.0F;
      } else if (this.viewMode == 5) {
         m1 = 0.0F;
         m2 = -62.0F;
         factor = 90.0F;
      } else if (this.viewMode == 6) {
         m1 = 0.0F;
         m2 = 62.0F;
         factor = 90.0F;
      }

      this.drawEntity(this.centerW, this.centerH + this.centerH / 2, (int)(1200.0 / modelHeight), m1, m2, factor, this.currentPixelmon);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.oldMouseX = (float)mouseX;
      this.oldMouseY = (float)mouseY;
   }

   protected void drawBackgroundUnderMenus(float partialTicks, int mouseX, int mouseY) {
   }

   protected void func_73864_a(int mouseX, int mouseY, int button) throws IOException {
      super.func_73864_a(mouseX, mouseY, button);
      this.species.func_146192_a(mouseX, mouseY, button);
      this.textWidth.func_146192_a(mouseX, mouseY, button);
      this.textHeight.func_146192_a(mouseX, mouseY, button);
      this.eye.func_146192_a(mouseX, mouseY, button);
      this.hover.func_146192_a(mouseX, mouseY, button);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146127_k == 0) {
         JsonObject baseStat = this.loadJson(this.currentPixelmon.getSpecies());
         if (!this.currentPixelmon.getSpecies().getFormEnum(-1).equals(this.currentPixelmon.getFormEnum())) {
            if (!baseStat.getAsJsonObject("forms").has(String.valueOf(this.currentPixelmon.getFormEnum().getForm()))) {
               baseStat.getAsJsonObject("forms").add(String.valueOf(this.currentPixelmon.getFormEnum().getForm()), new JsonObject());
            }

            JsonObject form = baseStat.getAsJsonObject("forms").getAsJsonObject(String.valueOf(this.currentPixelmon.getFormEnum().getForm()));
            this.insertAfter(form, "weight", "bounding_box", (new Gson()).toJsonTree(this.parseBoundsData()));
         } else {
            this.insertAfter(baseStat, "weight", "bounding_box", (new Gson()).toJsonTree(this.parseBoundsData()));
            baseStat.remove("width");
            baseStat.remove("length");
            baseStat.remove("hoverHeight");
         }

         this.updateJson(this.currentPixelmon.getSpecies(), baseStat);
      } else {
         EnumSpecies species;
         if (button.field_146127_k == 1) {
            species = EnumSpecies.getFromDex(this.currentPixelmon.getSpecies().getNationalPokedexInteger() + 1);
            if (species == null) {
               species = EnumSpecies.Bulbasaur;
            }

            this.setPokemon(species, species.getFormEnum(-1));
         } else if (button.field_146127_k == 2) {
            species = EnumSpecies.getFromDex(this.currentPixelmon.getSpecies().getNationalPokedexInteger() - 1);
            if (species == null) {
               species = EnumSpecies.Bulbasaur;
            }

            this.setPokemon(species, species.getFormEnum(-1));
         } else if (button.field_146127_k == 3) {
            EntityBoundsData data = this.parseBoundsData();
            BaseStats stats = this.currentPixelmon.getBaseStats();
            ReflectionHelper.setPrivateValue(BaseStats.class, stats, data, "bounding_box");
            ReflectionHelper.setPrivateValue(BaseStats.class, stats, (Object)null, "width");
            ReflectionHelper.setPrivateValue(BaseStats.class, stats, (Object)null, "length");
         } else if (button.field_146127_k == 4) {
            AnimationType type = this.currentPixelmon.getCurrentAnimation();
            if (type == AnimationType.IDLE) {
               this.currentPixelmon.setAnimation(AnimationType.FLY);
            } else if (type == AnimationType.FLY) {
               this.currentPixelmon.setAnimation(AnimationType.WALK);
            } else if (type == AnimationType.WALK) {
               this.currentPixelmon.setAnimation(AnimationType.IDLE);
            }

            type = this.currentPixelmon.getCurrentAnimation();
            this.field_146293_o.set(2, new GuiPixelLabel(type.name(), 2, this.field_146294_l - 280, this.field_146295_m - 60, 20, 20, -252645136));
         } else if (button.field_146127_k == 5) {
            this.currentPixelmon.setAnimate(!this.currentPixelmon.getShouldAnimate());
         } else {
            this.viewMode = button.field_146127_k - 6;
         }
      }

   }

   private EntityBoundsData parseBoundsData() {
      double width;
      double height;
      float eye;
      try {
         width = Double.parseDouble(this.textWidth.func_146179_b());
         height = Double.parseDouble(this.textHeight.func_146179_b());
         eye = Float.parseFloat(this.eye.func_146179_b());
      } catch (Throwable var7) {
         return null;
      }

      return new EntityBoundsData(width, height, eye);
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.currentPixelmon.getShouldAnimate()) {
         this.currentPixelmon.getAnimationVariables().tick();
         if (this.currentPixelmon.getModel() instanceof PixelmonModelSmd) {
            PixelmonModelSmd smdModel = (PixelmonModelSmd)this.currentPixelmon.getModel();
            IncrementingVariable inc = this.currentPixelmon.getAnimationVariables().getCounter(-1);
            if (inc == null) {
               this.currentPixelmon.getAnimationVariables().setCounter(-1, 2.14748365E9F, smdModel.animationIncrement);
            } else {
               inc.increment = smdModel.animationIncrement;
            }
         }
      }

   }

   protected void func_73869_a(char key, int keyCode) {
      if (keyCode == 1) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }

      this.species.func_146201_a(key, keyCode);
      if (Character.isDigit(key) || key == '.' || keyCode == 14) {
         this.textWidth.func_146201_a(key, keyCode);
         this.textHeight.func_146201_a(key, keyCode);
         this.eye.func_146201_a(key, keyCode);
         this.hover.func_146201_a(key, keyCode);
      }

      if (keyCode == 28 && this.species.func_146206_l()) {
         Optional opt = EnumSpecies.getFromName(this.species.func_146179_b());
         if (opt.isPresent()) {
            this.setPokemon((EnumSpecies)opt.get(), ((EnumSpecies)opt.get()).getFormEnum(-1));
         } else {
            this.species.func_146180_a(this.currentPixelmon.getSpecies().getPokemonName());
         }
      }

   }

   private void setPokemon(EnumSpecies species, IEnumForm form) {
      AnimationType animationType = this.currentPixelmon.getAnimation();
      boolean animate = this.currentPixelmon.getShouldAnimate();
      PokemonSpec spec = new PokemonSpec(new String[0]);
      spec.name = species.getPokemonName();
      spec.form = form != null ? form.getForm() : null;
      spec.growth = EnumGrowth.Ordinary.getForm();
      Pokemon pokemon = Pixelmon.pokemonFactory.create(spec);
      this.currentPixelmon = new EntityStatue(this.field_146297_k.field_71441_e);
      this.currentPixelmon.setPokemon(pokemon);
      this.currentPixelmon.updateSize();
      this.currentPixelmon.setAnimate(animate);
      this.currentPixelmon.setAnimation(animationType);
      this.species.func_146180_a(this.currentPixelmon.getSpecies().getPokemonName());
      EntityBoundsData data = this.currentPixelmon.getBaseStats().getBoundsData();
      this.textWidth.func_146180_a(Double.toString(data.getWidth()));
      this.textHeight.func_146180_a(Double.toString(data.getHeight()));
      this.eye.func_146180_a(Float.toString(data.getEyeHeight()));
      this.removeDropDown(this.dropDownForms);
      this.dropDownForms = this.addDropDown((new GuiDropDown(this.currentPixelmon.getSpecies().getPossibleForms(true), this.currentPixelmon.getFormEnum().getForm(), this.centerW + 20, 20, 60, 100)).setGetOptionString(ITranslatable::getLocalizedName).setOnSelected((f) -> {
         this.setPokemon(this.currentPixelmon.getSpecies(), f);
      }));
      this.field_146293_o.set(0, new GuiPixelLabel(pokemon.getDisplayName() + " - " + pokemon.getSpecies().getNationalPokedexNumber(), 0, 100, this.centerH, 20, 20, -252645136));
      JsonObject baseStat = this.loadJson(this.currentPixelmon.getSpecies());
      this.field_146293_o.set(1, new GuiPixelLabel(baseStat.has("bounding_box") ? "New data found" : "Old data detected", 1, this.field_146294_l - 100, this.field_146295_m - 20, 20, 20, -252645136));
   }

   private void drawEntity(int posX, int posY, int scale, float mouseX, float mouseY, float factor, EntityLivingBase ent) {
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)posX, (float)posY, 50.0F);
      GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      float f = ent.field_70761_aq;
      float f1 = ent.field_70177_z;
      float f2 = ent.field_70125_A;
      float f3 = ent.field_70758_at;
      float f4 = ent.field_70759_as;
      GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-((float)Math.atan((double)(mouseY / 40.0F))) * factor, 1.0F, 0.0F, 0.0F);
      ent.field_70761_aq = (float)Math.atan((double)(mouseX / 40.0F)) * factor;
      ent.field_70177_z = (float)Math.atan((double)(mouseX / 40.0F)) * factor * 2.0F;
      ent.field_70125_A = -((float)Math.atan((double)(mouseY / 40.0F))) * factor;
      ent.field_70759_as = ent.field_70177_z;
      ent.field_70758_at = ent.field_70177_z;
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
      rendermanager.func_178631_a(180.0F);
      rendermanager.func_178633_a(false);
      rendermanager.func_188391_a(ent, 0.0, 0.0, 0.0, 0.0F, 1.0F, false);
      rendermanager.func_178633_a(true);
      ent.field_70761_aq = f;
      ent.field_70177_z = f1;
      ent.field_70125_A = f2;
      ent.field_70758_at = f3;
      ent.field_70759_as = f4;
      GlStateManager.func_179121_F();
      RenderHelper.func_74518_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
      GlStateManager.func_179090_x();
      GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
   }

   private void insertAfter(JsonObject obj, String priorKey, String key, JsonElement element) {
      try {
         LinkedTreeMap members = (LinkedTreeMap)ReflectionHelper.getPrivateValue(JsonObject.class, obj, "members");
         if (!members.containsKey(priorKey)) {
            members.put(key, element);
            return;
         }

         if (members.containsKey(key)) {
            members.replace(key, element);
            return;
         }

         Class innerClassNode = LinkedTreeMap.class.getDeclaredClasses()[3];
         Constructor nodeConstruct = innerClassNode.getDeclaredConstructor();
         nodeConstruct.setAccessible(true);
         Method findByObject = ReflectionHelper.findMethod(LinkedTreeMap.class, "findByObject", "findByObject", Object.class);
         Object newNode = nodeConstruct.newInstance();
         Object priorNode = findByObject.invoke(members, priorKey);
         Object parent = ReflectionHelper.getPrivateValue(innerClassNode, priorNode, "parent");
         Object forwardNode = ReflectionHelper.getPrivateValue(innerClassNode, priorNode, "next");
         ReflectionHelper.setPrivateValue(innerClassNode, newNode, key, "key");
         ReflectionHelper.setPrivateValue(innerClassNode, newNode, element, "value");
         ReflectionHelper.setPrivateValue(innerClassNode, newNode, parent, "parent");
         ReflectionHelper.setPrivateValue(innerClassNode, newNode, forwardNode, "next");
         ReflectionHelper.setPrivateValue(innerClassNode, newNode, priorNode, "prev");
         ReflectionHelper.setPrivateValue(innerClassNode, priorNode, newNode, "next");
         int size = (Integer)ReflectionHelper.getPrivateValue(LinkedTreeMap.class, members, "size");
         ReflectionHelper.setPrivateValue(LinkedTreeMap.class, members, size + 1, "size");
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException var14) {
         var14.printStackTrace();
      }

   }

   private JsonObject loadJson(EnumSpecies species) {
      String dex = species.getNationalPokedexNumber();
      File file = new File("../src/main/resources/assets/pixelmon/stats", dex + ".json");

      try {
         JsonReader reader = new JsonReader(new FileReader(file));
         Throwable var6 = null;

         JsonObject baseStat;
         try {
            JsonParser parser = new JsonParser();
            baseStat = parser.parse(reader).getAsJsonObject();
         } catch (Throwable var16) {
            var6 = var16;
            throw var16;
         } finally {
            if (reader != null) {
               if (var6 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var15) {
                     var6.addSuppressed(var15);
                  }
               } else {
                  reader.close();
               }
            }

         }

         return baseStat;
      } catch (IOException var18) {
         var18.printStackTrace();
         return null;
      }
   }

   private void updateJson(EnumSpecies species, JsonObject obj) {
      String dex = species.getNationalPokedexNumber();
      File file = new File("../src/main/resources/assets/pixelmon/stats", dex + ".json");
      File file2 = new File("../out/production/Pixelmon.main/assets/pixelmon/stats", dex + ".json");
      List json = Arrays.asList((new GsonBuilder()).setPrettyPrinting().create().toJson(obj).split("\n"));

      try {
         Files.write(file.toPath(), json);
         Files.write(file2.toPath(), json);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      BaseStatsLoader.loadAllBaseStats();
      JsonObject baseStat = this.loadJson(this.currentPixelmon.getSpecies());
      this.field_146293_o.set(1, new GuiPixelLabel(baseStat.has("bounding_box") ? "New data found" : "Old data detected", 1, this.field_146294_l - 100, this.field_146295_m - 20, 20, 20, -252645136));
   }
}
