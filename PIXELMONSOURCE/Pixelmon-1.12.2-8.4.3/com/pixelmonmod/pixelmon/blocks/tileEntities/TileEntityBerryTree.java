package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class TileEntityBerryTree extends TileEntity implements ITickable, ISpecialTexture {
   private byte typeOrdinal;
   private transient EnumBerry typeCache;
   private byte stage = 1;
   private short projectedYield;
   private short hours = 0;
   private static boolean DEBUG = false;
   private int hoursTillDeath = -1;
   private boolean isGrowthBoosted = false;
   private short nextGrowthStage = -1;
   public int timesReplanted = 0;
   private transient BlockBerryTree cache = null;
   public boolean isGenerated = false;
   long lastWorldTime = -1L;
   private static final ResourceLocation berryTreeSeedTexture = new ResourceLocation("pixelmon:textures/berries/seeded.png");
   private static final ResourceLocation berryTreeSproutTexture = new ResourceLocation("pixelmon:textures/berries/sprouted.png");
   private ResourceLocation textureLocation = null;

   public EnumBerry getType() {
      return this.typeCache;
   }

   public byte getStage() {
      return this.stage;
   }

   public void setStage(byte i) {
      this.stage = i;
   }

   public short getProjectedYield() {
      if (this.isGenerated && this.projectedYield > 2) {
         this.projectedYield = (short)(this.field_145850_b.field_73012_v.nextInt(2) + 1);
      }

      return this.projectedYield;
   }

   public TileEntityBerryTree() {
   }

   public TileEntityBerryTree(byte typeOrdinal) {
      this.typeOrdinal = typeOrdinal;
      this.typeCache = EnumBerry.values()[typeOrdinal];
      this.projectedYield = (short)this.typeCache.maxYield;
   }

   public void boostGrowth() {
      this.isGrowthBoosted = true;
      if (this.field_145850_b.field_72995_K) {
         ClientProxy.spawnBoostedTreeParticles(this.field_145850_b, this.func_174877_v().func_177958_n(), this.func_174877_v().func_177956_o(), this.func_174877_v().func_177952_p(), this.getStage());
      }

   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.stage < 0) {
            this.field_145850_b.func_175698_g(this.field_174879_c);
            return;
         }

         long currentWorldTime = PixelmonConfig.useSystemTimeForBerries ? System.currentTimeMillis() : this.field_145850_b.func_82737_E();
         if (this.lastWorldTime == -1L || this.lastWorldTime > currentWorldTime) {
            this.lastWorldTime = currentWorldTime;
         }

         if (this.lastWorldTime < 1576392266000L && PixelmonConfig.useSystemTimeForBerries) {
            this.lastWorldTime = currentWorldTime;
         }

         if (this.nextGrowthStage == -1) {
            this.calculateNextGrowthStage();
         }

         int ticksPerHour = DEBUG && Pixelmon.devEnvironment ? 10 : (int)(36000.0F / PixelmonConfig.berryTreeGrowthMultiplier);
         long multiplier = PixelmonConfig.useSystemTimeForBerries ? 50L : 1L;
         int originalStage = this.getStage();

         while(!this.isGenerated && currentWorldTime - this.lastWorldTime > (long)ticksPerHour * multiplier && this.getBlock() != null) {
            this.onHour();
            this.lastWorldTime += (long)ticksPerHour * multiplier;
            if (this.getStage() < originalStage) {
               break;
            }
         }
      }

   }

   private void calculateNextGrowthStage() {
      this.nextGrowthStage = (short)((int)((double)(this.getType().growthTime / 4) * (0.75 + (double)this.field_145850_b.field_73012_v.nextFloat() * 0.5)));
   }

   public void onHour() {
      boolean markForUpdate = false;
      EnumBerry type = this.getType();
      if (this.projectedYield != type.minYield) {
         IBlockState state = this.field_145850_b.func_180495_p(this.func_174877_v().func_177977_b());
         boolean isWatered = false;
         if (state.func_177230_c() == Blocks.field_150458_ak && (Integer)state.func_177229_b(BlockFarmland.field_176531_a) == 7) {
            isWatered = true;
         }

         if (!isWatered) {
            this.projectedYield = (short)((int)((double)this.projectedYield - Math.ceil((double)type.maxYield / 5.0)));
            if (this.projectedYield < type.minYield) {
               this.projectedYield = (short)type.minYield;
            }
         }
      }

      if (++this.hours > this.nextGrowthStage && this.stage != 5) {
         this.calculateNextGrowthStage();
         if (this.isGrowthBoosted) {
            ++this.projectedYield;
            this.isGrowthBoosted = false;
         }

         this.getBlock().growStage(this.field_145850_b, this.field_145850_b.field_73012_v, this.func_174877_v(), this.field_145850_b.func_180495_p(this.func_174877_v()));
         markForUpdate = true;
         if (this.stage == 5) {
            this.hoursTillDeath = (int)((double)type.growthTime * (0.5 + (double)this.field_145850_b.field_73012_v.nextFloat()));
         }
      } else if (this.hoursTillDeath != -1 && this.hours >= this.hoursTillDeath) {
         markForUpdate = true;
         if (this.timesReplanted < 9) {
            this.getBlock().replant(this.field_145850_b, this.func_174877_v(), this.field_145850_b.func_180495_p(this.func_174877_v()));
            this.replant();
         } else {
            this.field_145850_b.func_180501_a(this.field_174879_c, Blocks.field_150350_a.func_176223_P(), 3);
         }
      }

      if (markForUpdate && this.func_145830_o()) {
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   private BlockBerryTree getBlock() {
      if (this.cache != null) {
         return this.cache;
      } else {
         Block block = this.field_145850_b.func_180495_p(this.func_174877_v()).func_177230_c();
         if (block instanceof BlockBerryTree) {
            this.cache = (BlockBerryTree)block;
            return this.cache;
         } else {
            return null;
         }
      }
   }

   public void replant() {
      this.projectedYield = (short)this.getType().maxYield;
      this.hoursTillDeath = -1;
      ++this.timesReplanted;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74774_a("typeOrdinal", this.typeOrdinal);
      nbt.func_74774_a("stage", this.stage);
      nbt.func_74777_a("hours", this.hours);
      nbt.func_74777_a("nextGrowth", this.nextGrowthStage);
      nbt.func_74777_a("projectedYield", this.projectedYield);
      nbt.func_74772_a("worldTime", this.lastWorldTime);
      nbt.func_74757_a("isGenerated", this.isGenerated);
      nbt.func_74757_a("isBoosted", this.isGrowthBoosted);
      nbt.func_74774_a("timesReplanted", (byte)this.timesReplanted);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.typeOrdinal = nbt.func_74771_c("typeOrdinal");
      this.typeCache = EnumBerry.values()[this.typeOrdinal];
      this.stage = nbt.func_74771_c("stage");
      this.hours = nbt.func_74765_d("hours");
      this.nextGrowthStage = nbt.func_74765_d("nextGrowth");
      this.projectedYield = nbt.func_74765_d("projectedYield");
      this.lastWorldTime = nbt.func_74763_f("worldTime");
      this.isGenerated = nbt.func_74767_n("isGenerated");
      this.isGrowthBoosted = nbt.func_74767_n("isBoosted");
      this.timesReplanted = nbt.func_74771_c("timesReplanted");
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.writeToNBTClient(nbt);
      return new SPacketUpdateTileEntity(this.field_174879_c, 1, nbt);
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.readFromNBTClient(pkt.func_148857_g());
   }

   private void readFromNBTClient(NBTTagCompound nbt) {
      this.typeOrdinal = nbt.func_74771_c("typeOrdinal");
      this.typeCache = EnumBerry.values()[this.typeOrdinal];
      this.stage = nbt.func_74771_c("stage");
   }

   private void writeToNBTClient(NBTTagCompound nbt) {
      nbt.func_74774_a("typeOrdinal", this.typeOrdinal);
      nbt.func_74774_a("stage", this.stage);
   }

   public ResourceLocation getTexture() {
      if (this.getStage() == 1) {
         return berryTreeSeedTexture;
      } else if (this.getStage() == 2) {
         return berryTreeSproutTexture;
      } else {
         if (this.textureLocation == null) {
            this.textureLocation = new ResourceLocation("pixelmon:textures/berries/" + this.getType().name().toLowerCase() + ".png");
         }

         return this.textureLocation;
      }
   }

   public boolean isGrowthBoosted() {
      return this.isGrowthBoosted;
   }

   public void setGenerated() {
      this.isGenerated = true;
      this.stage = 5;
   }
}
