package com.pixelmonmod.pixelmon.entities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.blocks.PokestopEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ModifyPokestopPacket;
import java.awt.Color;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPokestop extends EntityLivingBase {
   private static final Rotations DEFAULT_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private static final Color DEFAULT_COLOR = new Color(63, 221, 255);
   private static final float DEFAULT_SIZE = 2.0F;
   private static final int DEFAULT_CUBE_RANGE = 30;
   public static final DataParameter STATUS;
   public static final DataParameter COLOR_R;
   public static final DataParameter COLOR_G;
   public static final DataParameter COLOR_B;
   public static final DataParameter SIZE;
   public static final DataParameter CUBE_RANGE;
   public static final DataParameter ROTATION;
   private boolean canInteract;
   private Rotations rotation;
   private Color color;
   private float size;
   private int cubeRange;
   private float prevRenderYawOffset;
   private float renderYawOffset;
   private float prevRotationYawHead;
   private float rotationYawHead;
   private int animationTicks;
   private int animationTicksTotal;
   private int animationTickCube;
   private static final ArrayList EMPTY_LIST;

   public EntityPokestop(World worldIn) {
      super(worldIn);
      this.animationTicks = 0;
      this.animationTicksTotal = 0;
      this.animationTickCube = 0;
      this.rotation = DEFAULT_ROTATION;
      this.color = DEFAULT_COLOR;
      this.field_70145_X = this.func_189652_ae();
      this.size = 2.0F;
      this.cubeRange = 30;
      this.setSize(this.size);
   }

   public EntityPokestop(World worldIn, double posX, double posY, double posZ) {
      this(worldIn);
      this.func_70107_b(posX, posY, posZ);
   }

   protected final void func_70105_a(float width, float height) {
      double d0 = this.field_70165_t;
      double d1 = this.field_70163_u;
      double d2 = this.field_70161_v;
      super.func_70105_a(width, width);
      this.size = width;
      this.func_70107_b(d0, d1, d2);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(STATUS, (byte)0);
      this.field_70180_af.func_187214_a(ROTATION, DEFAULT_ROTATION);
      this.field_70180_af.func_187214_a(COLOR_R, DEFAULT_COLOR.getRed());
      this.field_70180_af.func_187214_a(COLOR_G, DEFAULT_COLOR.getGreen());
      this.field_70180_af.func_187214_a(COLOR_B, DEFAULT_COLOR.getBlue());
      this.field_70180_af.func_187214_a(SIZE, 2.0F);
      this.field_70180_af.func_187214_a(CUBE_RANGE, 30);
   }

   public void func_70014_b(NBTTagCompound compound) {
      super.func_70014_b(compound);
      compound.func_74757_a("NoBasePlate", this.hasNoBasePlate());
      compound.func_74757_a("AlwaysAnimate", this.doesAlwaysAnimate());
      compound.func_74757_a("AlwaysCube", this.isAlwaysCube());
      compound.func_74768_a("CubeRange", this.getCubeRange());
      compound.func_74776_a("Size", this.getSize());
      compound.func_74768_a("ColorR", this.color.getRed());
      compound.func_74768_a("ColorG", this.color.getGreen());
      compound.func_74768_a("ColorB", this.color.getBlue());
      if (!DEFAULT_ROTATION.equals(this.rotation)) {
         compound.func_74782_a("Rotations", this.rotation.func_179414_a());
      }

   }

   public void func_70037_a(NBTTagCompound compound) {
      super.func_70037_a(compound);
      this.setNoBasePlate(compound.func_74767_n("NoBasePlate"));
      this.setAlwaysAnimate(compound.func_74767_n("AlwaysAnimate"));
      this.setAlwaysCube(compound.func_74767_n("AlwaysCube"));
      this.setCubeRange(compound.func_74762_e("CubeRange"));
      this.setSize(compound.func_74760_g("Size"));
      this.setColor(compound.func_74762_e("ColorR"), compound.func_74762_e("ColorG"), compound.func_74762_e("ColorB"));
      this.field_70145_X = this.func_189652_ae();
      NBTTagList nbttaglist = compound.func_150295_c("Rotations", 5);
      this.setRotation(nbttaglist.func_82582_d() ? DEFAULT_ROTATION : new Rotations(nbttaglist));
   }

   public void func_70091_d(MoverType type, double x, double y, double z) {
      super.func_70091_d(type, x, y, z);
   }

   public boolean func_70104_M() {
      return false;
   }

   protected void func_82167_n(Entity entityIn) {
      Pixelmon.EVENT_BUS.post(new PokestopEvent.Collide(this, entityIn));
   }

   protected void func_85033_bc() {
      super.func_85033_bc();
   }

   public EnumActionResult func_184199_a(EntityPlayer player, Vec3d vec, EnumHand hand) {
      Pixelmon.EVENT_BUS.post(new PokestopEvent.Interact(this, player, true));
      return EnumActionResult.PASS;
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (source.func_76364_f() instanceof EntityPlayer) {
         Pixelmon.EVENT_BUS.post(new PokestopEvent.Interact(this, (EntityPlayer)source.func_76364_f(), false));
      }

      return false;
   }

   public void animate() {
      if (!this.field_70170_p.field_72995_K) {
         WorldServer ws = (WorldServer)this.field_70170_p;
         ws.func_72960_a(this, (byte)1);
      }

   }

   public void animateFor(EntityPlayerMP... players) {
      if (!this.field_70170_p.field_72995_K) {
         SPacketEntityStatus packet = new SPacketEntityStatus(this, (byte)1);
         EntityPlayerMP[] var3 = players;
         int var4 = players.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            EntityPlayerMP player = var3[var5];
            player.field_71135_a.func_147359_a(packet);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte id) {
      if (id == 1) {
         if (this.field_70170_p.field_72995_K && this.animationTicks == 0) {
            this.animationTicks = 156;
         }
      } else {
         super.func_70103_a(id);
      }

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
   public boolean isAnimating() {
      boolean animating = this.animationTicks > 0;
      if (this.doesAlwaysAnimate() && !animating) {
         animating = true;
         this.animationTicks = 156;
      }

      if (animating) {
         --this.animationTicks;
         if (this.animationTicks % 2 == 0) {
            ++this.animationTicksTotal;
         }
      }

      return animating;
   }

   @SideOnly(Side.CLIENT)
   public int getAnimationTicksTotal() {
      return this.animationTicksTotal;
   }

   @SideOnly(Side.CLIENT)
   public boolean isCube(EntityPlayer player) {
      return this.isAlwaysCube() || player.func_70032_d(this) > (float)this.getCubeRange();
   }

   @SideOnly(Side.CLIENT)
   public int getAnimationTickCube() {
      if (this.isAnimating()) {
         this.animationTickCube += 10;
      }

      ++this.animationTickCube;
      return this.animationTickCube;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double distance) {
      double d0 = this.func_174813_aQ().func_72320_b() * 4.0;
      if (Double.isNaN(d0) || d0 == 0.0) {
         d0 = 4.0;
      }

      d0 *= 64.0;
      return distance < d0 * d0;
   }

   protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
      this.prevRenderYawOffset = this.field_70126_B;
      this.renderYawOffset = this.field_70177_z;
      return 0.0F;
   }

   public float func_70047_e() {
      return this.field_70131_O * 0.9F;
   }

   public double func_70033_W() {
      return 0.10000000149011612;
   }

   public void func_181013_g(float offset) {
      this.prevRenderYawOffset = this.field_70126_B = offset;
      this.prevRotationYawHead = this.rotationYawHead = offset;
   }

   public EnumHandSide func_184591_cq() {
      return null;
   }

   public void func_70034_d(float rotation) {
      this.prevRenderYawOffset = this.field_70126_B = rotation;
      this.prevRotationYawHead = this.rotationYawHead = rotation;
   }

   public boolean func_85031_j(Entity entityIn) {
      return false;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      Rotations rotations = (Rotations)this.field_70180_af.func_187225_a(ROTATION);
      if (!this.rotation.equals(rotations)) {
         this.setRotation(rotations);
      }

      float size = (Float)this.field_70180_af.func_187225_a(SIZE);
      if (this.size != size) {
         this.setSize(size);
      }

      int cubeRange = (Integer)this.field_70180_af.func_187225_a(CUBE_RANGE);
      if (this.cubeRange != cubeRange) {
         this.setCubeRange(cubeRange);
      }

   }

   private void updateBoundingBox(boolean p_181550_1_) {
      if (p_181550_1_) {
         this.func_70105_a(0.0F, 0.0F);
      } else {
         this.func_70105_a(0.5F, 1.975F);
      }

   }

   protected void func_175135_B() {
      this.func_82142_c(this.canInteract);
   }

   public void func_82142_c(boolean invisible) {
      this.canInteract = invisible;
      super.func_82142_c(invisible);
   }

   public void setInvisibleFor(boolean invisible, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setInvisible(invisible).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

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

   public void setSize(float size) {
      this.field_70180_af.func_187227_b(SIZE, size);
      this.func_70105_a(size, size);
   }

   public void setSizeFor(float size, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setSize(size).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public float getSize() {
      return (Float)this.field_70180_af.func_187225_a(SIZE);
   }

   public void setNoBasePlate(boolean noBasePlate) {
      this.field_70180_af.func_187227_b(STATUS, this.setBit((Byte)this.field_70180_af.func_187225_a(STATUS), 1, noBasePlate));
   }

   public void setNoBasePlateFor(boolean noBasePlate, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setNoBasePlate(noBasePlate).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public boolean hasNoBasePlate() {
      return ((Byte)this.field_70180_af.func_187225_a(STATUS) & 1) != 0;
   }

   public void setAlwaysAnimate(boolean alwaysAnimate) {
      this.field_70180_af.func_187227_b(STATUS, this.setBit((Byte)this.field_70180_af.func_187225_a(STATUS), 2, alwaysAnimate));
   }

   public void setAlwaysAnimateFor(boolean alwaysAnimate, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setAlwaysAnimate(alwaysAnimate).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public boolean doesAlwaysAnimate() {
      return ((Byte)this.field_70180_af.func_187225_a(STATUS) & 2) != 0;
   }

   public void setAlwaysCube(boolean alwaysCube) {
      this.field_70180_af.func_187227_b(STATUS, this.setBit((Byte)this.field_70180_af.func_187225_a(STATUS), 4, alwaysCube));
   }

   public void setAlwaysCubeFor(boolean alwaysCube, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setAlwaysCube(alwaysCube).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public boolean isAlwaysCube() {
      return ((Byte)this.field_70180_af.func_187225_a(STATUS) & 4) != 0;
   }

   public void setColor(int r, int g, int b) {
      this.color = new Color(r, g, b);
      this.field_70180_af.func_187227_b(COLOR_R, r);
      this.field_70180_af.func_187227_b(COLOR_G, g);
      this.field_70180_af.func_187227_b(COLOR_B, b);
   }

   public void setColorFor(int r, int g, int b, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setRGB(r, g, b).build();
      EntityPlayerMP[] var6 = players;
      int var7 = players.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EntityPlayerMP player = var6[var8];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public int[] getColor() {
      return new int[]{(Integer)this.field_70180_af.func_187225_a(COLOR_R), (Integer)this.field_70180_af.func_187225_a(COLOR_G), (Integer)this.field_70180_af.func_187225_a(COLOR_B)};
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

   public void setCubeRange(int cubeRange) {
      this.cubeRange = cubeRange;
      this.field_70180_af.func_187227_b(CUBE_RANGE, cubeRange);
   }

   public void setCubeRangeFor(int cubeRange, EntityPlayerMP... players) {
      ModifyPokestopPacket packet = ModifyPokestopPacket.builder(this).setCubeRange(cubeRange).build();
      EntityPlayerMP[] var4 = players;
      int var5 = players.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EntityPlayerMP player = var4[var6];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public int getCubeRange() {
      return this.cubeRange;
   }

   public boolean func_70067_L() {
      return super.func_70067_L();
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
      if (STATUS.equals(key)) {
         this.func_70105_a(0.5F, 1.975F);
      }

      super.func_184206_a(key);
   }

   public boolean func_190631_cK() {
      return false;
   }

   public boolean func_96092_aw() {
      return false;
   }

   public boolean func_70027_ad() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_90999_ad() {
      return false;
   }

   public void func_70653_a(Entity entityIn, float strength, double xRatio, double zRatio) {
   }

   static {
      STATUS = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187191_a);
      COLOR_R = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187192_b);
      COLOR_G = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187192_b);
      COLOR_B = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187192_b);
      SIZE = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187193_c);
      CUBE_RANGE = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187192_b);
      ROTATION = EntityDataManager.func_187226_a(EntityPokestop.class, DataSerializers.field_187199_i);
      EMPTY_LIST = new ArrayList();
   }
}
