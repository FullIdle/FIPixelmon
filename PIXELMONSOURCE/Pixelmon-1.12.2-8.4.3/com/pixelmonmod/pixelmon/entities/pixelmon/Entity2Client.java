package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.client.render.IHasTexture;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.Transform;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.AnimationVariables;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects.ParticleEffects;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGastrodon;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.ICosmeticForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.vecmath.Vector3d;
import net.minecraft.client.model.ModelBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Entity2Client extends Entity1Base implements IHasTexture {
   static final DataParameter dwScale;
   static final DataParameter dwShiny;
   static final DataParameter dwCustomTexture;
   public static final DataParameter dwTransformation;
   static final DataParameter dwDynamaxScale;
   private static final int animationFlyingDelayLimit = 10;
   private static final int animationDelayLimit = 3;
   static int flyingDelayLimit;
   public PokemonBase transformed = null;
   public ResourceLocation transformedTexture = null;
   boolean animationFlyingCounting = false;
   boolean animationFlyingSwap = false;
   boolean animationCounting = false;
   boolean animationSwap = false;
   int animationFlyingDelayCounter = 0;
   int animationDelayCounter = 0;
   int flyingDelayCounter = 0;
   private AnimationVariables animationVariables;
   private AnimationType animationType;
   private ResourceLocation lastTexture;
   private boolean exists = true;
   private ResourceLocation lastSprite;
   private boolean spriteExists = true;
   public int evolvingVal = 0;
   public int evoAnimTicks = 0;
   public EvolutionStage evoStage = null;
   public int fadeCount = 0;
   private boolean fadeDirection = true;
   public float heightDiff;
   public float widthDiff;
   public float lengthDiff;

   public Entity2Client(World par1World) {
      super(par1World);
      this.field_70180_af.func_187214_a(dwScale, 1.0F);
      this.field_70180_af.func_187214_a(dwShiny, false);
      this.field_70180_af.func_187214_a(dwCustomTexture, "");
      this.field_70180_af.func_187214_a(dwTransformation, 0);
      this.field_70180_af.func_187214_a(dwDynamaxScale, 0.0F);
   }

   @Nonnull
   public ResourceLocation getTexture() {
      if (this.transformedTexture != null) {
         return this.transformedTexture;
      } else {
         ResourceLocation location = this.getRealTexture();
         if (!Objects.equals(this.lastTexture, location)) {
            this.lastTexture = location;
            this.exists = Pixelmon.proxy.resourceLocationExists(location);
         }

         return this.exists ? location : getOGTexture(this.getSpecies(), this.pokemon.getFormEnum(), this.getPokemonData().getGender(), this.getPokemonData().isShiny());
      }
   }

   public ResourceLocation getTextureNoCheck() {
      return this.transformedTexture != null ? this.transformedTexture : this.getRealTexture();
   }

   public ResourceLocation getRealTexture() {
      EnumSpecies pokemon = this.getSpecies();
      return getTextureFor(pokemon, this.pokemon.getFormEnum(), this.pokemon.getGender(), this.pokemon.getCustomTexture(), this.pokemon.isShiny());
   }

   public String getExtraTexture() {
      return (String)this.field_70180_af.func_187225_a(dwCustomTexture);
   }

   public ResourceLocation getSprite() {
      Pokemon pokemon = this.getPokemonData();
      ResourceLocation location = GuiResources.getPokemonSprite(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender(), pokemon.getCustomTexture(), pokemon.isShiny());
      if (!Objects.equals(this.lastSprite, location)) {
         this.lastSprite = location;
         this.spriteExists = Pixelmon.proxy.resourceLocationExists(location);
      }

      return this.spriteExists ? location : GuiResources.getPokemonSprite(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender(), "", pokemon.isShiny());
   }

   public void evolve(PokemonSpec evolveTo) {
      if (EnumSpecies.hasPokemon(evolveTo.name)) {
         if (this.getPokemonData().getSpecies() == EnumSpecies.Magikarp && this.getPokemonData().getFormEnum() == EnumMagikarp.ROASTED) {
            this.getPokemonData().setForm(EnumSpecial.Zombie);
         }

         if (this.getPokemonData().getSpecies() == EnumSpecies.Shellos) {
            if (this.getPokemonData().getForm() % 2 == 0) {
               this.getPokemonData().setForm(EnumGastrodon.East);
            } else {
               this.getPokemonData().setForm(EnumGastrodon.West);
            }
         }

         EnumSpecies evolvedTo = (EnumSpecies)EnumSpecies.getFromName(evolveTo.name).orElse(EnumSpecies.Bulbasaur);
         if (this.getSpecies() == evolvedTo) {
            Pixelmon.LOGGER.error("A Pokemon has evolved to the same species! Dumping data:");
            Pixelmon.LOGGER.error("Entity: " + this);
            Pixelmon.LOGGER.error("Species: " + this.getSpecies() + " -> " + evolvedTo);
         }

         this.pokemon.setSpecies(evolvedTo);
         this.update(new EnumUpdateType[]{EnumUpdateType.Name, EnumUpdateType.Nickname});
      }
   }

   @SideOnly(Side.CLIENT)
   public ModelBase getModel() {
      if (this.transformed != null) {
         return PixelmonModelRegistry.getModel(this.transformed);
      } else {
         return PixelmonModelRegistry.hasFlyingModel(this.getSpecies(), this.pokemon.getFormEnum()) && this.flyingDelayCounter >= flyingDelayLimit ? PixelmonModelRegistry.getFlyingModel(this.getSpecies(), this.pokemon.getFormEnum()) : PixelmonModelRegistry.getModel(this.getSpecies(), this.pokemon.getFormEnum());
      }
   }

   @SideOnly(Side.CLIENT)
   public Vector3d getModelSize() {
      return PixelmonModelRegistry.getModelSize(this.getPokemonData());
   }

   @SideOnly(Side.CLIENT)
   public void transform(EnumSpecies transformedModel, int transformedForm, String transformedTexture) {
      this.transformed = new PokemonBase(transformedModel, transformedForm, Gender.Male);
      this.transformedTexture = new ResourceLocation(transformedTexture);
   }

   public void cancelTransform() {
      this.transformed = null;
      this.transformedTexture = null;
      this.updateTransformed();
   }

   public void updateTransformed() {
      Transform transform;
      if (this.transformed != null) {
         transform = new Transform(this.func_145782_y(), this.transformed.getSpecies(), this.transformedTexture.toString(), this.transformed.getFormEnum());
      } else {
         transform = new Transform(this.func_145782_y(), this.pokemon.getSpecies(), this.getRealTexture().toString(), this.pokemon.getFormEnum());
      }

      Pixelmon.network.sendToAll(transform);
   }

   public void transformServer(EnumSpecies transformedModel, int form, String texture) {
      this.transformed = new PokemonBase(transformedModel, form, Gender.Male);
      this.transformedTexture = new ResourceLocation(texture);
      this.updateTransformed();
   }

   public int getTransformed() {
      return (Integer)this.field_70180_af.func_187225_a(dwTransformation);
   }

   public float getScaleFactor() {
      return (float)Math.pow((double)this.getPokemonData().getGrowth().scaleValue, PixelmonConfig.getGrowthModifier()) * this.getBossMode().scaleFactor;
   }

   public float getPixelmonScale() {
      return (Float)this.field_70180_af.func_187225_a(dwScale);
   }

   public void setPixelmonScale(float scale) {
      this.field_70180_af.func_187227_b(dwScale, scale);
   }

   public float getDynamaxScale() {
      return (Float)this.field_70180_af.func_187225_a(EntityPixelmon.dwDynamaxScale);
   }

   public void setDynamaxScale(float scale) {
      this.field_70180_af.func_187227_b(EntityPixelmon.dwDynamaxScale, Math.max(0.0F, scale));
   }

   public void addDynamaxScale(float scale) {
      this.field_70180_af.func_187227_b(EntityPixelmon.dwDynamaxScale, this.getDynamaxScale() + scale);
   }

   public AnimationVariables getAnimationVariables() {
      if (this.animationVariables == null) {
         this.animationVariables = new AnimationVariables();
      }

      return this.animationVariables;
   }

   public void initAnimation() {
      if (this.field_70170_p.field_72995_K) {
         ModelBase base = this.getModel();
         if (base instanceof PixelmonModelSmd) {
            ValveStudioModel model = ((PixelmonModelSmd)base).theModel;
            model.animate();
         }
      }

   }

   public void checkAnimation() {
      if (this.getModel() instanceof PixelmonModelSmd) {
         PixelmonModelSmd smdModel = (PixelmonModelSmd)this.getModel();
         Entity3HasStats pixelmon = (Entity3HasStats)this;
         float f1 = pixelmon.field_184618_aE + (pixelmon.field_70721_aZ - pixelmon.field_184618_aE) * 1.0F;
         if (f1 > 1.0F) {
            f1 = 1.0F;
         }

         IncrementingVariable inc = this.getAnimationVariables().getCounter(-1);
         if (inc == null) {
            this.getAnimationVariables().setCounter(-1, 2.14748365E9F, smdModel.animationIncrement);
         } else {
            inc.increment = smdModel.animationIncrement;
         }

         if (!pixelmon.animationCounting) {
            this.setAnimation(AnimationType.IDLE);
            pixelmon.animationCounting = true;
         }

         if (pixelmon.func_70090_H()) {
            if (f1 > smdModel.movementThreshold) {
               if (pixelmon.animationSwap) {
                  this.setAnimation(AnimationType.SWIM);
               }
            } else if (pixelmon.animationSwap) {
               this.setAnimation(AnimationType.IDLE_SWIM);
            }
         } else if (!pixelmon.field_70122_E) {
            if (!pixelmon.animationFlyingSwap) {
               pixelmon.animationFlyingCounting = true;
            }

            if (pixelmon.animationFlyingSwap) {
               this.setAnimation(AnimationType.FLY);
            }
         } else if (f1 > smdModel.movementThreshold) {
            if (pixelmon.animationSwap) {
               this.setAnimation(AnimationType.WALK);
            }
         } else if (pixelmon.animationSwap) {
            this.setAnimation(AnimationType.IDLE);
         }

         if (smdModel.theModel.currentSequence == null) {
            this.setAnimation(AnimationType.IDLE);
         }

      }
   }

   public void setAnimation(AnimationType animation) {
      this.animationType = animation;
   }

   public AnimationType getCurrentAnimation() {
      return this.animationType == null ? AnimationType.IDLE : this.animationType;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70122_E && !this.field_70171_ac) {
         if (this.flyingDelayCounter < flyingDelayLimit) {
            ++this.flyingDelayCounter;
         }
      } else {
         this.flyingDelayCounter = 0;
      }

      if (this.field_70170_p.field_72995_K) {
         if (!this.func_82150_aj()) {
            Iterator var1 = ParticleEffects.getParticleEffects(this).iterator();

            while(var1.hasNext()) {
               ParticleEffects particleEffect = (ParticleEffects)var1.next();
               particleEffect.onUpdate(this);
            }
         }

         this.tickEvolveAnimation();
         if (this.animationVariables != null) {
            this.animationVariables.tick();
         }

         if (this.animationFlyingCounting) {
            if (this.animationFlyingDelayCounter < 10) {
               ++this.animationFlyingDelayCounter;
               this.animationFlyingSwap = false;
            }

            if (this.animationFlyingDelayCounter >= 10) {
               this.animationFlyingSwap = true;
               this.animationFlyingDelayCounter = 0;
            }
         } else {
            this.animationFlyingDelayCounter = 0;
            this.animationFlyingSwap = false;
         }

         if (this.animationCounting) {
            if (this.animationDelayCounter < 3) {
               ++this.animationDelayCounter;
               this.animationSwap = false;
            }

            if (this.animationDelayCounter >= 3) {
               this.animationSwap = true;
               this.animationDelayCounter = 0;
            }
         } else {
            this.animationDelayCounter = 0;
            this.animationSwap = false;
         }

         if (this instanceof EntityPixelmon) {
            try {
               this.checkAnimation();
            } catch (NullPointerException var3) {
               System.out.println(this.func_70005_c_());
            }
         }
      }

   }

   public boolean isEvolving() {
      return this.evoStage != null && this.evoStage != EvolutionStage.End;
   }

   public void setEvolutionAnimationStage(EvolutionStage stage) {
      if (stage == EvolutionStage.End) {
         this.evoStage = null;
         this.updateSize();
      } else {
         this.evoStage = stage;
         this.evoAnimTicks = 0;
         if (stage == EvolutionStage.PreChoice) {
            this.fadeCount = 20;
         }
      }

   }

   @SideOnly(Side.CLIENT)
   private void tickEvolveAnimation() {
      ++this.evoAnimTicks;
      if (this.evoStage != null) {
         ++this.evoAnimTicks;
         if (this.evoStage == EvolutionStage.Choice) {
            if (this.fadeDirection) {
               ++this.fadeCount;
               if (this.fadeCount >= 20) {
                  this.fadeDirection = false;
               }
            } else {
               --this.fadeCount;
               if (this.fadeCount <= 0) {
                  this.fadeDirection = true;
               }
            }
         } else if (this.evoStage == EvolutionStage.PreAnimation) {
            if (this.fadeDirection) {
               if (this.fadeCount < 20) {
                  ++this.fadeCount;
               }
            } else if (this.fadeCount > 0 && this.fadeCount <= 0) {
               this.fadeDirection = true;
            }
         }

         int numEffects;
         if (this.evoStage == EvolutionStage.Choice || this.evoStage == EvolutionStage.PreAnimation) {
            Random random = this.func_70681_au();
            int numEffects = random.nextInt(10);

            for(numEffects = 0; numEffects < numEffects; ++numEffects) {
               Color color = RandomHelper.getRandomHighSaturationColor();
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getWidth(), this.field_70163_u + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getHeight(), this.field_70161_v + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getWidth(), (double)((float)color.getRed() / 255.0F - 1.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F), new int[0]);
            }
         }

         if (this.evoStage == EvolutionStage.PreAnimation || this.evoStage == EvolutionStage.PostAnimation) {
            if (this.evoStage == EvolutionStage.PreAnimation) {
               if (this.evoAnimTicks > EvolutionStage.PreAnimation.ticks) {
                  --this.evoAnimTicks;
               }
            } else {
               if (this.evoAnimTicks > EvolutionStage.PostAnimation.ticks) {
                  --this.evoAnimTicks;
               }

               if (this.evoAnimTicks > EvolutionStage.PostAnimation.ticks - 21 && this.fadeCount > 0) {
                  --this.fadeCount;
               }
            }

            int ticks = this.evoAnimTicks;
            if (this.evoStage == EvolutionStage.PostAnimation) {
               ticks += EvolutionStage.PreAnimation.ticks;
            }

            Random random = this.func_70681_au();
            numEffects = random.nextInt(50);

            for(int i = 0; i < numEffects; ++i) {
               Color color = RandomHelper.getRandomHighSaturationColor();
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getWidth(), this.field_70163_u + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getHeight(), this.field_70161_v + (double)(random.nextFloat() * 2.2F - 1.0F) * this.getBaseStats().getBoundsData().getWidth(), (double)((float)color.getRed() / 255.0F - 1.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F), new int[0]);
            }
         }

      }
   }

   public void func_70107_b(double x, double y, double z) {
      this.field_70165_t = x;
      this.field_70163_u = y;
      this.field_70161_v = z;
      this.updateSize();
   }

   public float func_70047_e() {
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      float scale = this.getPixelmonScale() * scaleFactor * this.getScaleFactor();
      EntityBoundsData data = this.getBaseStats().getBoundsData();
      return Math.max(0.2F, data.getEyeHeight() * scale);
   }

   void initBaseEntity() {
      super.initBaseEntity();
      this.updateSize();
   }

   public void updateSize() {
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      float scale = 1.0F;
      if (this.getBaseStats() != null) {
         scale = this.getPixelmonScale() * scaleFactor * this.getScaleFactor();
         EntityBoundsData data = this.getBaseStats().getBoundsData();
         this.field_70131_O = (float)(data.getHeight() * (double)scale);
         this.field_70130_N = (float)(data.getWidth() * (double)scale);
         this.func_174826_a(data.createBoundingBox(this, (double)scale));
      } else {
         float halfWidth = this.field_70130_N * scale / 2.0F;
         this.func_174826_a(new AxisAlignedBB(this.field_70165_t - (double)halfWidth, this.field_70163_u, this.field_70161_v - (double)halfWidth, this.field_70165_t + (double)halfWidth, this.field_70163_u - this.func_70033_W() + (double)this.field_70131_O * (double)scale, this.field_70161_v + (double)halfWidth));
      }

   }

   protected SoundEvent func_184639_G() {
      return this.getBaseStats().getSoundForGender(this.getPokemonData().getGender());
   }

   protected float func_70599_aP() {
      return 0.4F;
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      if (Pixelmon.devEnvironment) {
         nbt.func_74778_a("DebugTexture", this.getTexture().toString());
      }

   }

   /** @deprecated */
   @Deprecated
   public static ResourceLocation getTextureFor(EnumSpecies species, IEnumForm form, Gender gender, String customTexture, EnumSpecialTexture specialTexture, boolean shiny) {
      return getTextureFor(species, form, gender, customTexture, shiny);
   }

   public static ResourceLocation getTextureFor(EnumSpecies species, IEnumForm form, Gender gender, String customTexture, boolean shiny) {
      String path = "textures/pokemon/";
      String folder = "";
      String prefix = "";
      if (shiny && form instanceof ICosmeticForm && ((ICosmeticForm)form).isCosmetic()) {
         shiny = ((ICosmeticForm)form).hasShiny(species);
      }

      if (shiny) {
         folder = "pokemon-shiny/";
         prefix = "shiny";
      }

      if (!customTexture.isEmpty() && (!(form instanceof ICosmeticForm) || !((ICosmeticForm)form).isCosmetic())) {
         folder = "custom-" + customTexture + "/";
      }

      String mf = EnumSpecies.mfTextured.contains(species) && !(form instanceof Gender) ? (gender == Gender.Male ? "male" : "female") : "";
      String formSuffix = form.getFormSuffix(shiny);
      return new ResourceLocation("pixelmon", path + folder + prefix + species.name.toLowerCase() + mf + formSuffix + ".png");
   }

   public static ResourceLocation getOGTexture(EnumSpecies pokemon, IEnumForm form, Gender gender, boolean shiny) {
      String mf = EnumSpecies.mfTextured.contains(pokemon) ? (gender == Gender.Male ? "male" : "female") : "";
      String formSuffix = form instanceof ICosmeticForm && ((ICosmeticForm)form).isCosmetic() ? "" : form.getFormSuffix(shiny);
      return new ResourceLocation("pixelmon", "textures/pokemon/" + (shiny ? "pokemon-shiny/shiny" : "") + pokemon.name.toLowerCase() + mf + formSuffix + ".png");
   }

   static {
      dwScale = EntityDataManager.func_187226_a(Entity2Client.class, DataSerializers.field_187193_c);
      dwShiny = EntityDataManager.func_187226_a(Entity2Client.class, DataSerializers.field_187198_h);
      dwCustomTexture = EntityDataManager.func_187226_a(Entity2Client.class, DataSerializers.field_187194_d);
      dwTransformation = EntityDataManager.func_187226_a(Entity2Client.class, DataSerializers.field_187192_b);
      dwDynamaxScale = EntityDataManager.func_187226_a(Entity2Client.class, DataSerializers.field_187193_c);
      flyingDelayLimit = 10;
   }
}
