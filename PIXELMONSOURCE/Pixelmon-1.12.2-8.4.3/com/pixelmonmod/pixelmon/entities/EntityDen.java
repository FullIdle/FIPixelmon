package com.pixelmonmod.pixelmon.entities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.raids.DenEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.battles.raids.RaidRandomizer;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.items.ItemSpawnDen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.NBTHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDen extends EntityLivingBase {
   private static final Rotations DEFAULT_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private static final Color DEFAULT_COLOR_RGBA = new Color(255, 36, 134, 255);
   private static final Color DEFAULT_COLOR_UI_A = new Color(255, 108, 92);
   private static final Color DEFAULT_COLOR_UI_B = new Color(168, 59, 57);
   private static final Color DEFAULT_COLOR_UI_C = new Color(204, 0, 0, 51);
   public static final DataParameter STATUS;
   public static final DataParameter COLOR_RGBA;
   public static final DataParameter COLOR_UI_A;
   public static final DataParameter COLOR_UI_B;
   public static final DataParameter COLOR_UI_C;
   public static final DataParameter DECORATION;
   public static final DataParameter BEAMS;
   public static final DataParameter ANIMATE;
   public static final DataParameter HIDE_BASE;
   public static final DataParameter NO_COLLIDE;
   public static final DataParameter BEAM_ON;
   public static final DataParameter BEAM_TAPER;
   public static final DataParameter BEAM_LENGTH;
   public static final DataParameter BEAM_WIDTH;
   public static final DataParameter ROTATION;
   public static final DataParameter STARS;
   public static final DataParameter SPECIES;
   public static final DataParameter FORM;
   public static final DataParameter LAST_ROLL_DAY;
   private boolean canInteract;
   private Rotations rotation;
   private boolean decoration;
   private int beams;
   private boolean animate;
   private boolean hideBase;
   private boolean noCollide;
   private boolean beamOn;
   private boolean beamTaper;
   private float beamLength;
   private float beamWidth;
   private Color colorRGBA;
   private Color colorUIA;
   private Color colorUIB;
   private Color colorUIC;
   private RaidData raidData;
   private RaidData inUseRaidData;
   private boolean lockout;
   private int lastRollDay;
   private long lastPickTime;
   private int pickHits;
   public int ticker;
   public EntityPixelmon display;
   private static final ArrayList EMPTY_LIST;

   public EntityDen(World worldIn) {
      super(worldIn);
      this.lockout = false;
      this.lastRollDay = -1;
      this.lastPickTime = -1L;
      this.pickHits = 0;
      this.setDefaults();
      this.updateBoundingBox();
      this.field_70158_ak = true;
   }

   public void setDefaults() {
      this.rotation = DEFAULT_ROTATION;
      this.colorRGBA = DEFAULT_COLOR_RGBA;
      this.colorUIA = DEFAULT_COLOR_UI_A;
      this.colorUIB = DEFAULT_COLOR_UI_B;
      this.colorUIC = DEFAULT_COLOR_UI_C;
      this.decoration = false;
      this.animate = true;
      this.beams = -1;
      this.hideBase = false;
      this.noCollide = false;
      this.beamOn = false;
      this.beamTaper = true;
      this.beamLength = 65.0F;
      this.beamWidth = -1.0F;
      this.field_70145_X = this.func_189652_ae();
      this.lastRollDay = -1;
   }

   public EntityDen(World worldIn, double posX, double posY, double posZ) {
      this(worldIn);
      this.func_70107_b(posX, posY, posZ);
   }

   protected final void func_70105_a(float width, float height) {
      double d0 = this.field_70165_t;
      double d1 = this.field_70163_u;
      double d2 = this.field_70161_v;
      super.func_70105_a(width, width);
      this.func_70107_b(d0, d1, d2);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(STATUS, (byte)0);
      this.field_70180_af.func_187214_a(SPECIES, -1);
      this.field_70180_af.func_187214_a(STARS, -1);
      this.field_70180_af.func_187214_a(FORM, -1);
      this.field_70180_af.func_187214_a(ROTATION, DEFAULT_ROTATION);
      this.field_70180_af.func_187214_a(COLOR_RGBA, DEFAULT_COLOR_RGBA.getRGB());
      this.field_70180_af.func_187214_a(COLOR_UI_A, DEFAULT_COLOR_UI_A.getRGB());
      this.field_70180_af.func_187214_a(COLOR_UI_B, DEFAULT_COLOR_UI_B.getRGB());
      this.field_70180_af.func_187214_a(COLOR_UI_C, DEFAULT_COLOR_UI_C.getRGB());
      this.field_70180_af.func_187214_a(DECORATION, false);
      this.field_70180_af.func_187214_a(ANIMATE, true);
      this.field_70180_af.func_187214_a(BEAMS, -1);
      this.field_70180_af.func_187214_a(HIDE_BASE, false);
      this.field_70180_af.func_187214_a(NO_COLLIDE, false);
      this.field_70180_af.func_187214_a(BEAM_ON, false);
      this.field_70180_af.func_187214_a(BEAM_TAPER, true);
      this.field_70180_af.func_187214_a(BEAM_LENGTH, 65.0F);
      this.field_70180_af.func_187214_a(BEAM_WIDTH, -1.0F);
      this.field_70180_af.func_187214_a(LAST_ROLL_DAY, -1);
      this.clearData();
   }

   public void func_70014_b(NBTTagCompound compound) {
      super.func_70014_b(compound);
      compound.func_74774_a("Status", this.getStatus());
      compound.func_74768_a("ColorRGBA", this.colorRGBA.getRGB());
      compound.func_74768_a("ColorUIA", this.colorUIA.getRGB());
      compound.func_74768_a("ColorUIB", this.colorUIB.getRGB());
      compound.func_74768_a("ColorUIC", this.colorUIC.getRGB());
      if (!DEFAULT_ROTATION.equals(this.rotation)) {
         compound.func_74782_a("Rotations", this.rotation.func_179414_a());
      }

      compound.func_74757_a("Decoration", this.decoration);
      compound.func_74757_a("Animate", this.animate);
      compound.func_74768_a("Beams", this.beams);
      compound.func_74757_a("HideBase", this.hideBase);
      compound.func_74757_a("NoCollide", this.noCollide);
      compound.func_74757_a("BeamOn", this.beamOn);
      compound.func_74757_a("BeamTaper", this.beamTaper);
      compound.func_74776_a("BeamLength", this.beamLength);
      compound.func_74776_a("BeamWidth", this.beamWidth);
      compound.func_74768_a("LastRollDay", this.lastRollDay);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setColorRGBA((Integer)NBTHelper.getOrDefault(nbt, "ColorRGBA", DEFAULT_COLOR_RGBA.getRGB(), NBTTagCompound::func_74762_e));
      this.setColorUIA((Integer)NBTHelper.getOrDefault(nbt, "ColorUIA", DEFAULT_COLOR_UI_A.getRGB(), NBTTagCompound::func_74762_e));
      this.setColorUIB((Integer)NBTHelper.getOrDefault(nbt, "ColorUIB", DEFAULT_COLOR_UI_B.getRGB(), NBTTagCompound::func_74762_e));
      this.setColorUIC((Integer)NBTHelper.getOrDefault(nbt, "ColorUIC", DEFAULT_COLOR_UI_C.getRGB(), NBTTagCompound::func_74762_e));
      this.field_70145_X = this.func_189652_ae();
      NBTTagList nbttaglist = nbt.func_150295_c("Rotations", 5);
      this.setRotation(nbttaglist.func_82582_d() ? DEFAULT_ROTATION : new Rotations(nbttaglist));
      this.setDecoration((Boolean)NBTHelper.getOrDefault(nbt, "Decoration", false, NBTTagCompound::func_74767_n));
      this.setAnimate((Boolean)NBTHelper.getOrDefault(nbt, "Animate", true, NBTTagCompound::func_74767_n));
      this.setBeams((Integer)NBTHelper.getOrDefault(nbt, "Beams", -1, NBTTagCompound::func_74762_e));
      this.setBaseHidden((Boolean)NBTHelper.getOrDefault(nbt, "HideBase", false, NBTTagCompound::func_74767_n));
      this.setNoCollide((Boolean)NBTHelper.getOrDefault(nbt, "NoCollide", false, NBTTagCompound::func_74767_n));
      this.setBeamOn((Boolean)NBTHelper.getOrDefault(nbt, "BeamOn", false, NBTTagCompound::func_74767_n));
      this.setBeamTaper((Boolean)NBTHelper.getOrDefault(nbt, "BeamTaper", false, NBTTagCompound::func_74767_n));
      this.setBeamLength((Float)NBTHelper.getOrDefault(nbt, "BeamLength", 65.0F, NBTTagCompound::func_74760_g));
      this.setBeamWidth((Float)NBTHelper.getOrDefault(nbt, "BeamWidth", -1.0F, NBTTagCompound::func_74760_g));
      this.setLastRollDay((Integer)NBTHelper.getOrDefault(nbt, "LastRollDay", -1, NBTTagCompound::func_74762_e));
   }

   public void func_70091_d(MoverType type, double x, double y, double z) {
      super.func_70091_d(type, x, y, z);
   }

   public boolean func_70104_M() {
      return false;
   }

   protected void func_85033_bc() {
      super.func_85033_bc();
   }

   protected void func_82167_n(Entity entityIn) {
      Pixelmon.EVENT_BUS.post(new DenEvent.Collide(this, entityIn));
   }

   public EnumActionResult func_184199_a(EntityPlayer player, Vec3d vec, EnumHand hand) {
      if (!this.decoration && hand == EnumHand.MAIN_HAND && player instanceof EntityPlayerMP && !Pixelmon.EVENT_BUS.post(new DenEvent.Interact(this, player, true))) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(playerMP);
         Pokemon pokemon = pps.getFirstAblePokemon();
         if (this.getServerData() != null) {
            if (pokemon != null && pokemon.getPosition() != null) {
               if (!this.getServerData().addPlayer(4, playerMP, pokemon, pokemon.getPosition().order)) {
                  player.func_145747_a(new TextComponentTranslation("raid.interact.inuse", new Object[0]));
               }
            } else {
               player.func_145747_a(new TextComponentTranslation("raid.interact.empty", new Object[0]));
            }
         } else {
            Item item = player.func_184614_ca().func_77973_b();
            boolean standard = item == PixelmonItems.wishingPiece;
            boolean special = item == PixelmonItems.waterdudeWishingPiece;
            if (standard || special) {
               Optional raid;
               if (standard) {
                  raid = RaidRandomizer.getRandomRaid(this, true);
               } else {
                  EnumSpecies species = EnumSpecies.randomLegendary();
                  raid = Optional.of(new RaidData(this.func_145782_y(), 5, species, (IEnumForm)RandomHelper.getRandomElementFromCollection(species.getPossibleForms(false))));
               }

               if (raid.isPresent()) {
                  this.lockout = true;
                  this.setData((RaidData)raid.get());
                  if (!player.func_184812_l_()) {
                     player.func_184614_ca().func_190918_g(1);
                  }

                  player.func_145747_a(new TextComponentTranslation("raid.interact.wished", new Object[0]));
                  this.field_70170_p.func_184133_a((EntityPlayer)null, this.func_180425_c(), SoundEvents.field_187616_bj, SoundCategory.BLOCKS, 1.0F, 1.0F);
               } else {
                  player.func_145747_a(new TextComponentTranslation("raid.interact.wishfailed", new Object[0]));
               }
            }
         }

         return EnumActionResult.PASS;
      } else {
         return EnumActionResult.FAIL;
      }
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (source.func_76364_f() instanceof EntityPlayer && !Pixelmon.EVENT_BUS.post(new DenEvent.Interact(this, (EntityPlayer)source.func_76364_f(), false))) {
         EntityPlayer player = (EntityPlayer)source.func_76364_f();
         if (!player.func_130014_f_().field_72995_K) {
            WorldServer world = (WorldServer)player.func_130014_f_();
            if (player.func_184614_ca().func_77973_b() == PixelmonItems.denSpawner) {
               if (ItemSpawnDen.isOPOnly && !player.func_70003_b(4, "pixelmon.denspawner.use")) {
                  return false;
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  return false;
               }

               world.func_184133_a((EntityPlayer)null, this.func_180425_c(), SoundEvents.field_187835_fT, SoundCategory.BLOCKS, 1.0F, 0.5F);
               this.func_70106_y();
               return true;
            }

            if (PixelmonConfig.raidDensBreakable && player.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
               ItemPickaxe pick = (ItemPickaxe)player.func_184614_ca().func_77973_b();
               long time = world.func_82737_E();
               if (time - this.lastPickTime > 60L) {
                  this.pickHits = 0;
               }

               this.lastPickTime = time;
               ++this.pickHits;
               int rank = 7 - pick.field_77862_b.func_77996_d();
               int i;
               Vec3d sphere;
               if (this.pickHits >= rank) {
                  this.func_70106_y();
                  player.func_184614_ca().func_77972_a(1, player);
                  world.func_184148_a((EntityPlayer)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187718_dS, SoundCategory.BLOCKS, 2.0F, 1.0F);

                  for(i = 0; i < 100; ++i) {
                     sphere = RandomHelper.nextSpherePoint(1.5);
                     world.func_180505_a(EnumParticleTypes.BLOCK_DUST, false, this.field_70165_t + sphere.field_72450_a, this.field_70163_u + RandomHelper.rand.nextDouble() * 1.5, this.field_70161_v + sphere.field_72449_c, 1, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() * 0.5, new int[]{Block.func_176210_f(Blocks.field_150348_b.func_176223_P())});
                  }
               } else {
                  world.func_184148_a((EntityPlayer)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187843_fX, SoundCategory.BLOCKS, 1.0F, 1.0F);

                  for(i = 0; i < 10 * this.pickHits; ++i) {
                     sphere = RandomHelper.nextSpherePoint(1.5);
                     world.func_180505_a(EnumParticleTypes.BLOCK_DUST, false, this.field_70165_t + sphere.field_72450_a, this.field_70163_u + RandomHelper.rand.nextDouble() * 1.5, this.field_70161_v + sphere.field_72449_c, 1, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() - 0.5, RandomHelper.rand.nextDouble() * 0.5, new int[]{Block.func_176210_f(Blocks.field_150348_b.func_176223_P())});
                  }
               }
            }
         }
      }

      return false;
   }

   public void setStatus(byte status) {
      this.field_70180_af.func_187227_b(STATUS, status);
   }

   public byte getStatus() {
      return (Byte)this.field_70180_af.func_187225_a(STATUS);
   }

   public void setData(RaidData data) {
      this.raidData = data;
      this.field_70180_af.func_187227_b(STARS, data.getStars());
      this.field_70180_af.func_187227_b(SPECIES, data.getSpecies().ordinal());
      this.field_70180_af.func_187227_b(FORM, Integer.valueOf(data.getForm() != null ? data.getForm().getForm() : -1));
   }

   public void setInUseRaidData() {
      this.inUseRaidData = this.raidData;
   }

   public RaidData getInUseRaidData() {
      return this.inUseRaidData;
   }

   public void clearData() {
      this.raidData = null;
      this.field_70180_af.func_187227_b(STARS, -1);
      this.field_70180_af.func_187227_b(SPECIES, -1);
      this.field_70180_af.func_187227_b(FORM, -1);
   }

   public RaidData getServerData() {
      return this.raidData;
   }

   public Optional getData() {
      int stars = (Integer)this.field_70180_af.func_187225_a(STARS);
      return stars < 0 ? Optional.empty() : Optional.of(new RaidData(this.func_145782_y(), stars, (Integer)this.field_70180_af.func_187225_a(SPECIES), (Integer)this.field_70180_af.func_187225_a(FORM)));
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte id) {
      super.func_70103_a(id);
   }

   public Iterable func_184193_aE() {
      return EMPTY_LIST;
   }

   public ItemStack func_184582_a(EntityEquipmentSlot slotIn) {
      return ItemStack.field_190927_a;
   }

   public void func_184201_a(EntityEquipmentSlot slotIn, ItemStack stack) {
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_184177_bl() {
      return TileEntity.INFINITE_EXTENT_AABB;
   }

   @Nullable
   public AxisAlignedBB func_70046_E() {
      if (this.field_70170_p.field_72995_K) {
         return this.func_70089_S() && !this.isNoCollide() ? this.func_174813_aQ() : null;
      } else {
         return this.func_70089_S() && !this.noCollide ? this.func_174813_aQ() : null;
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double distance) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_145770_h(double x, double y, double z) {
      return true;
   }

   protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
      this.field_70760_ar = this.field_70126_B;
      this.field_70761_aq = this.field_70177_z;
      return 0.0F;
   }

   public float func_70047_e() {
      return this.field_70131_O * 0.9F;
   }

   public double func_70033_W() {
      return 0.10000000149011612;
   }

   public void func_181013_g(float offset) {
      this.field_70760_ar = this.field_70126_B = offset;
      this.field_70758_at = this.field_70759_as = offset;
   }

   public EnumHandSide func_184591_cq() {
      return null;
   }

   public void func_70034_d(float rotation) {
      this.field_70760_ar = this.field_70126_B = rotation;
      this.field_70758_at = this.field_70759_as = rotation;
   }

   public boolean func_85031_j(Entity entityIn) {
      return false;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70171_ac && !this.func_180799_ab()) {
         if (this.field_70170_p.func_72918_a(this.func_174813_aQ().func_72314_b(0.0, -2.0, 0.0).func_186664_h(0.001), Material.field_151586_h, this)) {
            this.field_70181_x = 0.0;
         } else if (this.field_70170_p.func_72918_a(this.func_174813_aQ().func_72314_b(0.0, -2.0, 0.0).func_186664_h(0.001), Material.field_151587_i, this)) {
            this.field_70181_x = 0.0;
         }
      } else {
         this.field_70181_x = 0.1;
      }

      Rotations rotations = (Rotations)this.field_70180_af.func_187225_a(ROTATION);
      if (!this.rotation.equals(rotations)) {
         this.setRotation(rotations);
      }

      if (this.field_70170_p instanceof WorldServer) {
         if (!this.decoration) {
            long now = this.field_70170_p.func_82737_E();
            int day = (int)(now / 24000L);
            long marker = (long)(this.lastRollDay + 1) * 24000L;
            if (this.field_70170_p.func_72820_D() % 24000L == (long)PixelmonConfig.denRespawnTime || now > marker) {
               this.roll(day);
            }
         }
      } else {
         this.getDisplay().ifPresent((display) -> {
            display.getAnimationVariables().tick();
         });
      }

      if (this.getServerData() != null) {
         this.getServerData().onUpdate(this);
      }

   }

   public void roll(int day) {
      this.setLastRollDay(day);
      if (this.getServerData() != null) {
         if (!this.getServerData().hasPlayers()) {
            this.clearData();
         }
      } else if (this.lockout) {
         this.lockout = false;
      } else {
         RaidRandomizer.getRandomRaid(this, false).ifPresent((raid) -> {
            this.lockout = true;
            this.setData(raid);
            this.announce(this, raid.getSpecies(), this.field_70170_p.func_180494_b(this.func_180425_c()));
         });
      }

   }

   private void announce(EntityDen den, EnumSpecies species, Biome biome) {
      if (PixelmonConfig.doLegendaryRaidEvent && species.isLegendary()) {
         ITextComponent translatePoke = new TextComponentTranslation("pixelmon." + species.name.toLowerCase() + ".name", new Object[0]);
         translatePoke.func_150256_b().func_150238_a(TextFormatting.GREEN);
         ITextComponent translateMessage = new TextComponentTranslation("spawn.legendaryraidmessage", new Object[]{translatePoke, biome.field_76791_y});
         translateMessage.func_150256_b().func_150238_a(TextFormatting.GREEN);
         MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
         server.func_184103_al().func_148539_a(new TextComponentTranslation("chat.type.announcement", new Object[]{TextFormatting.LIGHT_PURPLE + "Pixelmon" + TextFormatting.RESET + TextFormatting.GREEN, translateMessage}));
         BlockPos pos = den.func_180425_c();
         server.func_145747_a(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Spawned " + species.getPokemonName() + " at: " + den.field_70170_p.func_72912_H().func_76065_j() + " x:" + pos.func_177958_n() + ", y:" + pos.func_177956_o() + ", z:" + pos.func_177952_p()));
      }

   }

   private void updateBoundingBox() {
      this.func_70105_a(2.25F, 0.5F);
   }

   protected void func_175135_B() {
      this.func_82142_c(this.canInteract);
   }

   public void func_82142_c(boolean invisible) {
      this.canInteract = invisible;
      super.func_82142_c(invisible);
   }

   public void func_174812_G() {
      this.func_70106_y();
   }

   public boolean func_180427_aV() {
      return true;
   }

   public EnumPushReaction func_184192_z() {
      return EnumPushReaction.IGNORE;
   }

   public boolean func_96092_aw() {
      return false;
   }

   public void setColorRGBA(int rgb) {
      this.colorRGBA = new Color(rgb);
      this.field_70180_af.func_187227_b(COLOR_RGBA, rgb);
   }

   public Color getColorRGBA() {
      return new Color((Integer)this.field_70180_af.func_187225_a(COLOR_RGBA));
   }

   public void setColorUIA(int rgb) {
      this.colorUIA = new Color(rgb);
      this.field_70180_af.func_187227_b(COLOR_UI_A, rgb);
   }

   public Color getColorUIA() {
      return new Color((Integer)this.field_70180_af.func_187225_a(COLOR_UI_A));
   }

   public void setColorUIB(int rgb) {
      this.colorUIB = new Color(rgb);
      this.field_70180_af.func_187227_b(COLOR_UI_B, rgb);
   }

   public Color getColorUIB() {
      return new Color((Integer)this.field_70180_af.func_187225_a(COLOR_UI_B));
   }

   public void setColorUIC(int rgb) {
      this.colorUIC = new Color(rgb);
      this.field_70180_af.func_187227_b(COLOR_UI_C, rgb);
   }

   public Color getColorUIC() {
      return new Color((Integer)this.field_70180_af.func_187225_a(COLOR_UI_C));
   }

   public void setDecoration(boolean decoration) {
      this.decoration = decoration;
      this.field_70180_af.func_187227_b(DECORATION, decoration);
   }

   public boolean isDecoration() {
      return (Boolean)this.field_70180_af.func_187225_a(DECORATION);
   }

   public void setAnimate(boolean animate) {
      this.animate = animate;
      this.field_70180_af.func_187227_b(ANIMATE, animate);
   }

   public boolean isAnimating() {
      return (Boolean)this.field_70180_af.func_187225_a(ANIMATE);
   }

   public void setBeams(int beams) {
      this.beams = beams;
      this.field_70180_af.func_187227_b(BEAMS, beams);
   }

   public int getBeams() {
      return (Integer)this.field_70180_af.func_187225_a(BEAMS);
   }

   public void setBaseHidden(boolean hideBase) {
      this.hideBase = hideBase;
      this.field_70180_af.func_187227_b(HIDE_BASE, hideBase);
   }

   public boolean isBaseHidden() {
      return (Boolean)this.field_70180_af.func_187225_a(HIDE_BASE);
   }

   public void setNoCollide(boolean noCollide) {
      this.noCollide = noCollide;
      this.canInteract = !noCollide;
      this.field_70180_af.func_187227_b(NO_COLLIDE, noCollide);
   }

   public boolean isNoCollide() {
      boolean noCollide = (Boolean)this.field_70180_af.func_187225_a(NO_COLLIDE);
      this.canInteract = !noCollide;
      return noCollide;
   }

   public void setBeamOn(boolean beamOn) {
      this.beamOn = beamOn;
      this.field_70180_af.func_187227_b(BEAM_ON, beamOn);
   }

   public boolean isBeamOn() {
      return (Boolean)this.field_70180_af.func_187225_a(BEAM_ON);
   }

   public void setBeamTaper(boolean beamTaper) {
      this.beamTaper = beamTaper;
      this.field_70180_af.func_187227_b(BEAM_TAPER, beamTaper);
   }

   public boolean isBeamTaper() {
      return (Boolean)this.field_70180_af.func_187225_a(BEAM_TAPER);
   }

   public void setBeamLength(float beamLength) {
      this.beamLength = beamLength;
      this.field_70180_af.func_187227_b(BEAM_LENGTH, beamLength);
   }

   public float getBeamLength() {
      return (Float)this.field_70180_af.func_187225_a(BEAM_LENGTH);
   }

   public void setBeamWidth(float beamWidth) {
      this.beamWidth = beamWidth;
      this.field_70180_af.func_187227_b(BEAM_WIDTH, beamWidth);
   }

   public float getBeamWidth() {
      return (Float)this.field_70180_af.func_187225_a(BEAM_WIDTH);
   }

   public void setLastRollDay(int lastRollDay) {
      this.lastRollDay = lastRollDay;
      this.field_70180_af.func_187227_b(LAST_ROLL_DAY, lastRollDay);
   }

   @SideOnly(Side.CLIENT)
   public Optional getDisplay() {
      Optional raid = this.getData();
      if (raid.isPresent()) {
         if (this.display == null) {
            this.display = new EntityPixelmon(this.field_70170_p);
            Pokemon pokemon = Pixelmon.pokemonFactory.create(((RaidData)raid.get()).getSpecies());
            if (((RaidData)raid.get()).getForm() != null) {
               pokemon.setForm(((RaidData)raid.get()).getForm());
            }

            pokemon.setGrowth(EnumGrowth.Ordinary);
            this.display.setPokemon(pokemon);
            this.display.setForm(pokemon.getForm());
            this.display.setAnimation(AnimationType.IDLE);
            this.display.checkAnimation();
            this.display.initAnimation();
         }

         if (this.display != null && (this.display.getPokemonData().getSpecies() != ((RaidData)raid.get()).getSpecies() || ((RaidData)raid.get()).getForm() != null && this.display.getFormEnum() != ((RaidData)raid.get()).getForm())) {
            this.display = null;
         }

         return Optional.ofNullable(this.display);
      } else {
         return Optional.empty();
      }
   }

   private byte setBit(byte p_184797_1_, int p_184797_2_, boolean p_184797_3_) {
      if (p_184797_3_) {
         p_184797_1_ = (byte)(p_184797_1_ | p_184797_2_);
      } else {
         p_184797_1_ = (byte)(p_184797_1_ & ~p_184797_2_);
      }

      return p_184797_1_;
   }

   public void setRotation(Rotations vec) {
      this.rotation = vec;
      this.field_70180_af.func_187227_b(ROTATION, vec);
   }

   public Rotations getRotation() {
      return this.rotation;
   }

   public boolean func_70067_L() {
      return true;
   }

   protected SoundEvent func_184588_d(int heightIn) {
      return SoundEvents.field_187841_fW;
   }

   @Nullable
   protected SoundEvent func_184601_bQ(DamageSource damageSourceIn) {
      return SoundEvents.field_187843_fX;
   }

   @Nullable
   protected SoundEvent func_184615_bR() {
      return SoundEvents.field_187835_fT;
   }

   protected float func_70599_aP() {
      return 0.0F;
   }

   public void func_70077_a(EntityLightningBolt lightningBolt) {
   }

   public boolean func_184603_cC() {
      return false;
   }

   public void func_184206_a(DataParameter key) {
      super.func_184206_a(key);
   }

   public boolean func_190631_cK() {
      return false;
   }

   public boolean func_70027_ad() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_90999_ad() {
      return false;
   }

   public ItemStack getPickedResult(RayTraceResult target) {
      return new ItemStack(PixelmonItems.denSpawner);
   }

   public void func_70653_a(Entity entityIn, float strength, double xRatio, double zRatio) {
   }

   static {
      STATUS = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187191_a);
      COLOR_RGBA = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      COLOR_UI_A = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      COLOR_UI_B = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      COLOR_UI_C = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      DECORATION = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      BEAMS = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      ANIMATE = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      HIDE_BASE = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      NO_COLLIDE = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      BEAM_ON = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      BEAM_TAPER = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187198_h);
      BEAM_LENGTH = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187193_c);
      BEAM_WIDTH = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187193_c);
      ROTATION = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187199_i);
      STARS = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      SPECIES = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      FORM = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      LAST_ROLL_DAY = EntityDataManager.func_187226_a(EntityDen.class, DataSerializers.field_187192_b);
      EMPTY_LIST = new ArrayList();
   }
}
