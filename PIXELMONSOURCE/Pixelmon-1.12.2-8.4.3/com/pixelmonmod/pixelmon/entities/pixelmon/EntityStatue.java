package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimationSequence;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.client.render.IHasTexture;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.StatuePacketClient;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.AnimationVariables;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumBoundingBoxMode;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumStatueTextureType;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.vecmath.Vector3d;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityStatue extends EntityLiving implements IHasTexture, IAnimals {
   private static final DataParameter dwSpecies;
   private static final DataParameter dwForm;
   private static final DataParameter dwGender;
   private static final DataParameter dwGrowth;
   private static final DataParameter dwBoundMode;
   private static final DataParameter dwRotation;
   private static final DataParameter dwScale;
   private static final DataParameter dwLabel;
   private static final DataParameter dwTextures;
   private static final DataParameter dwCustomTexture;
   private static final DataParameter dwAnimation;
   private static final DataParameter dwAnimationFrame;
   private static final DataParameter dwAnimationMode;
   private static final DataParameter dwIsFlying;
   private static final Map dwMap;
   private PokemonBase pokemon;
   private EnumGrowth growth;
   private EnumBoundingBoxMode boundingMode;
   private float rotation;
   private float scale;
   private String label;
   private EnumStatueTextureType textureMode;
   private String customTexture;
   private AnimationType animation;
   private int animationFrame;
   private boolean animate;
   private boolean isFlying;
   private AnimationType lastAnimation;
   private int lastFrame;
   private ResourceLocation lastTexture;
   private boolean exists;
   private AnimationVariables animationVariables;

   public EntityStatue(World world) {
      super(world);
      this.growth = EnumGrowth.Ordinary;
      this.boundingMode = EnumBoundingBoxMode.Solid;
      this.rotation = 0.0F;
      this.scale = 1.0F;
      this.label = "";
      this.textureMode = EnumStatueTextureType.OriginalTexture;
      this.customTexture = "";
      this.animation = AnimationType.WALK;
      this.animationFrame = 0;
      this.animate = false;
      this.isFlying = false;
      this.lastAnimation = null;
      this.lastFrame = -1;
      this.exists = true;
      this.field_70180_af.func_187214_a(dwSpecies, -1);
      this.field_70180_af.func_187214_a(dwForm, EnumNoForm.NoForm.getForm());
      this.field_70180_af.func_187214_a(dwGender, Gender.None.getForm());
      this.field_70180_af.func_187214_a(dwGrowth, (byte)this.growth.index);
      this.field_70180_af.func_187214_a(dwBoundMode, (byte)this.boundingMode.ordinal());
      this.field_70180_af.func_187214_a(dwRotation, this.rotation);
      this.field_70180_af.func_187214_a(dwScale, this.scale);
      this.field_70180_af.func_187214_a(dwLabel, this.label);
      this.field_70180_af.func_187214_a(dwTextures, this.textureMode.ordinal());
      this.field_70180_af.func_187214_a(dwCustomTexture, this.customTexture);
      this.field_70180_af.func_187214_a(dwAnimation, this.animation.ordinal());
      this.field_70180_af.func_187214_a(dwAnimationFrame, this.animationFrame);
      this.field_70180_af.func_187214_a(dwAnimationMode, this.animate);
      this.field_70180_af.func_187214_a(dwIsFlying, this.isFlying);
      this.func_189654_d(true);
   }

   public void setPokemon(PokemonBase pokemon) {
      this.pokemon = pokemon.copyBase();
      this.pokemon.registerDataParameters(dwMap);
      this.field_70180_af.func_187227_b(dwSpecies, pokemon.getSpecies().getNationalPokedexInteger());
      this.field_70180_af.func_187227_b(dwForm, (byte)pokemon.getForm());
      this.field_70180_af.func_187227_b(dwGender, pokemon.getGender().getForm());
      this.initAnimation();
   }

   public PokemonBase getPokemon() {
      return this.pokemon;
   }

   public void setSpecies(EnumSpecies pokemon) {
      this.setPokemon(new PokemonBase(pokemon));
   }

   public EnumSpecies getSpecies() {
      return this.pokemon.getSpecies();
   }

   public void setForm(int form) {
      if (this.getSpecies().getNumForms(true) > 0) {
         if (this.getSpecies().getFormEnum(form) == EnumNoForm.NoForm) {
            form = -1;
         }
      } else if (EnumSpecies.genderForm.contains(this.getSpecies())) {
         this.field_70180_af.func_187227_b(dwGender, Gender.getGender((short)form).getForm());
      } else {
         form = -1;
      }

      this.field_70180_af.func_187227_b(dwForm, (byte)form);
   }

   public int getForm() {
      return this.pokemon.getForm();
   }

   public void setGender(Gender gender) {
      if (EnumSpecies.genderForm.contains(this.getSpecies())) {
         this.field_70180_af.func_187227_b(dwForm, (byte)gender.ordinal());
      }

      this.field_70180_af.func_187227_b(dwGender, (byte)gender.ordinal());
   }

   public Gender getGender() {
      return this.pokemon.getGender();
   }

   public IEnumForm getFormEnum() {
      return this.getSpecies().getFormEnum(this.getForm());
   }

   public void setGrowth(EnumGrowth growth) {
      this.field_70180_af.func_187227_b(dwGrowth, (byte)growth.index);
   }

   public EnumGrowth getGrowth() {
      return this.growth;
   }

   public void setRotation(float rotation) {
      this.field_70180_af.func_187227_b(dwRotation, rotation);
   }

   public float getRotation() {
      return this.rotation;
   }

   public void setPixelmonScale(float scale) {
      this.field_70180_af.func_187227_b(dwScale, scale);
   }

   public float getPixelmonScale() {
      return this.scale;
   }

   public void setLabel(String label) {
      this.field_70180_af.func_187227_b(dwLabel, label);
   }

   public String getLabel() {
      return this.label;
   }

   public void setTextureType(EnumStatueTextureType type) {
      this.field_70180_af.func_187227_b(dwTextures, type.ordinal());
   }

   public EnumStatueTextureType getTextureType() {
      return this.textureMode;
   }

   public void setAnimation(AnimationType animationType) {
      this.field_70180_af.func_187227_b(dwAnimation, animationType.ordinal());
   }

   public AnimationType getAnimation() {
      AnimationType animationType = AnimationType.values()[(Integer)this.field_70180_af.func_187225_a(dwAnimation)];
      if (animationType == null) {
         animationType = AnimationType.WALK;
         this.setAnimation(animationType);
      }

      return animationType;
   }

   public void setAnimationFrame(int animationFrame) {
      if (animationFrame < 0) {
         animationFrame = 0;
      }

      this.field_70180_af.func_187227_b(dwAnimationFrame, animationFrame);
   }

   public int getAnimationFrame() {
      return this.animationFrame;
   }

   public void setAnimate(boolean value) {
      this.field_70180_af.func_187227_b(dwAnimationMode, value);
   }

   public boolean getShouldAnimate() {
      return this.animate;
   }

   public BaseStats getBaseStats() {
      return this.getSpecies().getBaseStats(this.getFormEnum());
   }

   public EnumBossMode getBossMode() {
      return this.textureMode.bossMode;
   }

   public Color getColor() {
      return this.textureMode.color;
   }

   public void func_70071_h_() {
      this.field_70159_w = this.field_70181_x = this.field_70179_y = 0.0;
      this.field_70177_z = this.field_70759_as = this.getRotation();
      super.func_70071_h_();
      if (this.field_70173_aa % 64 == 1) {
         this.updateSize();
      }

      if (this.pokemon != null) {
         if (this.field_70170_p.field_72995_K) {
            if (this.animate && this.animationVariables != null) {
               this.animationVariables.tick();
               if (this.getModel() instanceof PixelmonModelSmd) {
                  PixelmonModelSmd smdModel = (PixelmonModelSmd)this.getModel();
                  IncrementingVariable inc = this.getAnimationVariables().getCounter(-1);
                  if (inc == null) {
                     this.getAnimationVariables().setCounter(-1, 2.14748365E9F, smdModel.animationIncrement);
                  } else {
                     inc.increment = smdModel.animationIncrement;
                  }
               }
            }

            if ((this.lastAnimation == null || this.lastFrame == -1 || this.lastFrame != this.getAnimationFrame() || !this.lastAnimation.equals(this.getAnimation())) && this.isSmd()) {
               this.lastAnimation = this.getAnimation();
               this.lastFrame = this.getAnimationFrame();
               ValveStudioModel model = ((PixelmonModelSmd)this.getModel()).theModel;
               model.setAnimation(this.getAnimation());
               SmdAnimation theAnim = model.currentSequence == null ? null : model.currentSequence.current();
               if (theAnim == null || theAnim.totalFrames == 0) {
                  return;
               }

               if (this.getAnimationFrame() >= theAnim.totalFrames) {
                  this.setAnimationFrame(theAnim.totalFrames - 1);
               }

               theAnim.setCurrentFrame(this.getAnimationFrame());
               model.animate();
               this.updateSize();
            }
         }

      }
   }

   public void func_70636_d() {
      if (this.field_70716_bi > 0 && !this.func_184186_bw()) {
         double d0 = this.field_70165_t + (this.field_184623_bh - this.field_70165_t) / (double)this.field_70716_bi;
         double d1 = this.field_70163_u + (this.field_184624_bi - this.field_70163_u) / (double)this.field_70716_bi;
         double d2 = this.field_70161_v + (this.field_184625_bj - this.field_70161_v) / (double)this.field_70716_bi;
         double d3 = MathHelper.func_76138_g(this.field_184626_bk - (double)this.field_70177_z);
         this.field_70177_z = (float)((double)this.field_70177_z + d3 / (double)this.field_70716_bi);
         this.field_70125_A = (float)((double)this.field_70125_A + (this.field_70709_bj - (double)this.field_70125_A) / (double)this.field_70716_bi);
         --this.field_70716_bi;
         this.func_70107_b(d0, d1, d2);
         this.func_70101_b(this.field_70177_z, this.field_70125_A);
      } else if (!this.func_70613_aW()) {
         this.field_70159_w *= 0.98;
         this.field_70181_x *= 0.98;
         this.field_70179_y *= 0.98;
      }

      if (Math.abs(this.field_70159_w) < 0.003) {
         this.field_70159_w = 0.0;
      }

      if (Math.abs(this.field_70181_x) < 0.003) {
         this.field_70181_x = 0.0;
      }

      if (Math.abs(this.field_70179_y) < 0.003) {
         this.field_70179_y = 0.0;
      }

      this.field_70170_p.field_72984_F.func_76320_a("travel");
      this.field_70702_br *= 0.98F;
      this.field_191988_bg *= 0.98F;
      this.field_70704_bt *= 0.9F;
      this.func_191986_a(this.field_70702_br, this.field_70701_bs, this.field_191988_bg);
      this.field_70170_p.field_72984_F.func_76319_b();
      if (this.boundingMode == EnumBoundingBoxMode.Pushout) {
         this.field_70170_p.field_72984_F.func_76320_a("push");
         this.func_85033_bc();
         this.field_70170_p.field_72984_F.func_76319_b();
      }

   }

   public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (player instanceof EntityPlayerMP && !stack.func_190926_b() && stack.func_77973_b() == PixelmonItems.chisel) {
         Pixelmon.network.sendTo(new StatuePacketClient(this.func_110124_au()), (EntityPlayerMP)player);
         OpenScreen.open(player, EnumGuiScreen.StatueEditor, this.func_145782_y());
      }

      return false;
   }

   public void func_184206_a(DataParameter key) {
      if (key.func_187155_a() == dwSpecies.func_187155_a() && this.pokemon == null) {
         this.pokemon = new PokemonBase(EnumSpecies.getFromDex((Integer)this.field_70180_af.func_187225_a(dwSpecies)));
         this.pokemon.registerDataParameters(dwMap);
      }

      if (!dwMap.values().contains(key) || !this.pokemon.dataManagerChange(key, this.field_70180_af.func_187225_a(key))) {
         if (key.func_187155_a() == dwGrowth.func_187155_a()) {
            this.growth = EnumGrowth.getGrowthFromIndex((Byte)this.field_70180_af.func_187225_a(dwGrowth));
         } else if (key.func_187155_a() == dwBoundMode.func_187155_a()) {
            this.boundingMode = EnumBoundingBoxMode.getMode((Byte)this.field_70180_af.func_187225_a(dwBoundMode));
         } else if (key.func_187155_a() == dwRotation.func_187155_a()) {
            this.rotation = (Float)this.field_70180_af.func_187225_a(dwRotation);
         } else if (key.func_187155_a() == dwScale.func_187155_a()) {
            this.scale = (Float)this.field_70180_af.func_187225_a(dwScale);
         } else if (key.func_187155_a() == dwLabel.func_187155_a()) {
            this.label = (String)this.field_70180_af.func_187225_a(dwLabel);
         } else if (key.func_187155_a() == dwTextures.func_187155_a()) {
            this.textureMode = EnumStatueTextureType.getFromOrdinal((Integer)this.field_70180_af.func_187225_a(dwTextures));
         } else if (key.func_187155_a() == dwCustomTexture.func_187155_a()) {
            this.customTexture = (String)this.field_70180_af.func_187225_a(dwCustomTexture);
         } else if (key.func_187155_a() == dwAnimation.func_187155_a()) {
            this.animation = AnimationType.values()[(Integer)this.field_70180_af.func_187225_a(dwAnimation)];
         } else if (key.func_187155_a() == dwAnimationFrame.func_187155_a()) {
            this.animationFrame = (Integer)this.field_70180_af.func_187225_a(dwAnimationFrame);
         } else if (key.func_187155_a() == dwAnimationMode.func_187155_a()) {
            this.animate = (Boolean)this.field_70180_af.func_187225_a(dwAnimationMode);
         } else if (key.func_187155_a() == dwIsFlying.func_187155_a()) {
            this.isFlying = (Boolean)this.field_70180_af.func_187225_a(dwIsFlying);
         }
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      this.pokemon.writeToNBT(nbt);
      nbt.func_74774_a("Variant", (byte)this.pokemon.getForm());
      nbt.func_74774_a("Growth", (byte)this.growth.index);
      nbt.func_74774_a("BoundingBoxMode", (byte)this.boundingMode.ordinal());
      nbt.func_74776_a("StartingYaw", this.field_70177_z);
      nbt.func_74778_a("statueLabel", this.getLabel());
      nbt.func_74774_a("statueTexture", (byte)this.getTextureType().ordinal());
      if (!this.customTexture.isEmpty()) {
         nbt.func_74778_a("CustomTexture", this.customTexture);
      } else {
         nbt.func_82580_o("CustomTexture");
      }

      nbt.func_74778_a("statueAnimation", this.getAnimation().toString().toLowerCase());
      nbt.func_74768_a("statueFrame", this.getAnimationFrame());
      nbt.func_74757_a("Animate", this.animate);
      nbt.func_74757_a("statueModelType", !this.getIsFlying());
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      PokemonBase base = new PokemonBase();
      base.readFromNBT(nbt);
      this.setPokemon(base);
      if (nbt.func_74764_b("Growth")) {
         this.setGrowth(EnumGrowth.getFromNBT(nbt));
      }

      if (nbt.func_74764_b("BoundingBoxMode")) {
         this.field_70180_af.func_187227_b(dwBoundMode, nbt.func_74771_c("BoundingBoxMode"));
      }

      if (nbt.func_74764_b("StartingYaw")) {
         this.setRotation(nbt.func_74760_g("StartingYaw"));
      }

      if (nbt.func_74764_b("statueLabel")) {
         this.setLabel(nbt.func_74779_i("statueLabel"));
      }

      if (nbt.func_74764_b("statueTexture")) {
         this.setTextureType(EnumStatueTextureType.getFromOrdinal(nbt.func_74771_c("statueTexture")));
      }

      if (nbt.func_74764_b("CustomTexture")) {
         this.field_70180_af.func_187227_b(dwCustomTexture, nbt.func_74779_i("CustomTexture"));
      }

      if (nbt.func_74764_b("statueAnimation")) {
         this.setAnimation(AnimationType.getTypeFor(nbt.func_74779_i("statueAnimation")));
         this.setAnimationFrame(nbt.func_74762_e("statueFrame"));
         this.setIsFlying(!nbt.func_74767_n("statueModelType"));
         this.setAnimate(nbt.func_74767_n("Animate"));
      }

   }

   public String func_70005_c_() {
      return this.getSpecies().getLocalizedName();
   }

   protected boolean func_70692_ba() {
      return false;
   }

   public void func_180430_e(float distance, float damageMultiplier) {
   }

   public boolean func_70648_aU() {
      return true;
   }

   @Nullable
   public AxisAlignedBB func_70046_E() {
      return this.boundingMode == EnumBoundingBoxMode.Solid ? this.func_174813_aQ() : null;
   }

   public boolean func_70104_M() {
      return false;
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      return false;
   }

   public ItemStack getPickedResult(RayTraceResult target) {
      return new ItemStack(PixelmonItems.chisel);
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
      return data.getEyeHeight() * scale;
   }

   public void updateSize() {
      if (this.pokemon != null && this.getBaseStats() != null) {
         float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
         float scale = this.getPixelmonScale() * scaleFactor * this.getScaleFactor();
         EntityBoundsData data = this.getBaseStats().getBoundsData();
         this.field_70131_O = (float)(data.getHeight() * (double)scale);
         this.field_70130_N = (float)(data.getWidth() * (double)scale);
         this.func_174826_a(data.createBoundingBox(this, (double)scale));
      }
   }

   public float getScaleFactor() {
      return (float)Math.pow((double)this.getGrowth().scaleValue, PixelmonConfig.getGrowthModifier()) * this.getBossMode().scaleFactor;
   }

   public ResourceLocation getTexture() {
      EnumStatueTextureType type = this.getTextureType();
      if (type == EnumStatueTextureType.Stone) {
         return new ResourceLocation("pixelmon:textures/pokemon/statue.png");
      } else if (type == EnumStatueTextureType.Gold) {
         return new ResourceLocation("pixelmon:textures/pokemon/gold.png");
      } else if (type == EnumStatueTextureType.Silver) {
         return new ResourceLocation("pixelmon:textures/pokemon/silver.png");
      } else if (type == EnumStatueTextureType.Bronze) {
         return new ResourceLocation("pixelmon:textures/pokemon/bronze.png");
      } else {
         EnumSpecies pokemon = this.getSpecies();
         ResourceLocation location = Entity2Client.getTextureFor(pokemon, this.getFormEnum(), this.getGender(), (String)this.field_70180_af.func_187225_a(dwCustomTexture), type == EnumStatueTextureType.Shiny);
         if (!Objects.equals(this.lastTexture, location)) {
            this.lastTexture = location;
            this.exists = Pixelmon.proxy.resourceLocationExists(location);
         }

         return this.exists ? location : Entity2Client.getOGTexture(pokemon, this.getFormEnum(), this.getGender(), type == EnumStatueTextureType.Shiny);
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean isSmd() {
      return this.getModel() instanceof PixelmonModelSmd;
   }

   public AnimationType getCurrentAnimation() {
      return this.getAnimation();
   }

   private void initAnimation() {
      if (this.field_70170_p.field_72995_K) {
         ModelBase base = this.getModel();
         if (base instanceof PixelmonModelSmd) {
            ValveStudioModel model = ((PixelmonModelSmd)base).theModel;
            model.animate();
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public int getFrameCount() {
      if (!this.isSmd()) {
         return 0;
      } else {
         ValveStudioModel model = ((PixelmonModelSmd)this.getModel()).theModel;
         model.setAnimation(this.getAnimation());
         SmdAnimationSequence sequence = model.currentSequence;
         return sequence != null && sequence.current() != null ? sequence.current().totalFrames : 0;
      }
   }

   @SideOnly(Side.CLIENT)
   public AnimationType nextAnimation() {
      if (this.isSmd()) {
         AnimationType animation = this.getAnimation();
         Iterator it = ((PixelmonModelSmd)this.getModel()).theModel.anims.entrySet().iterator();
         AnimationType first = null;
         boolean takeNext = false;

         while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            AnimationType key = (AnimationType)entry.getKey();
            if (first == null) {
               first = key;
            }

            if (takeNext) {
               return key;
            }

            if (key.equals(animation)) {
               takeNext = true;
            }
         }

         return first;
      } else {
         return null;
      }
   }

   @SideOnly(Side.CLIENT)
   public List getAllAnimations() {
      List animations = new ArrayList();
      if (this.isSmd()) {
         animations.addAll(((PixelmonModelSmd)this.getModel()).theModel.anims.keySet());
      }

      return animations;
   }

   public AnimationVariables getAnimationVariables() {
      if (this.animationVariables == null) {
         this.animationVariables = new AnimationVariables();
      }

      return this.animationVariables;
   }

   @SideOnly(Side.CLIENT)
   public ModelBase getModel() {
      return this.getIsFlying() && this.hasFlyingModel() ? PixelmonModelRegistry.getFlyingModel(this.getSpecies(), this.getFormEnum()) : PixelmonModelRegistry.getModel(this.getSpecies(), this.getFormEnum());
   }

   @SideOnly(Side.CLIENT)
   public boolean hasFlyingModel() {
      return PixelmonModelRegistry.hasFlyingModel(this.getSpecies(), this.getSpecies().getFormEnum(this.getForm()));
   }

   public void setIsFlying(boolean isFlying) {
      this.field_70180_af.func_187227_b(dwIsFlying, isFlying);
   }

   public boolean getIsFlying() {
      return this.isFlying;
   }

   public Vector3d getModelSize() {
      return PixelmonModelRegistry.getModelSize(this.pokemon);
   }

   static {
      dwSpecies = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187192_b);
      dwForm = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187191_a);
      dwGender = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187191_a);
      dwGrowth = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187191_a);
      dwBoundMode = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187191_a);
      dwRotation = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187193_c);
      dwScale = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187193_c);
      dwLabel = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187194_d);
      dwTextures = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187192_b);
      dwCustomTexture = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187194_d);
      dwAnimation = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187192_b);
      dwAnimationFrame = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187192_b);
      dwAnimationMode = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187198_h);
      dwIsFlying = EntityDataManager.func_187226_a(EntityStatue.class, DataSerializers.field_187198_h);
      dwMap = new HashMap();
      dwMap.put("pokemon", dwSpecies);
      dwMap.put("form", dwForm);
      dwMap.put("gender", dwGender);
   }
}
