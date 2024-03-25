package com.pixelmonmod.pixelmon.entities.npcs;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.AI.AIHarvestFarmLand;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryTrainers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityNPC extends EntityCreature implements INpc {
   public static int idIndex = 0;
   public static final DataParameter dwName;
   public static final DataParameter dwNickname;
   public static final DataParameter dwID;
   public static final DataParameter dwModel;
   public static final DataParameter dwTextureIndex;
   public static final DataParameter dwCustomSteveTexture;
   public static final DataParameter dwProfession;
   private static final DataParameter dwNPCAI;
   public static final int TICKSPERSECOND = 20;
   public static int intMinTicksToDespawn;
   public static int intMaxTicksToDespawn;
   public SpawnLocationType npcLocation;
   public int despawnCounter = -1;
   private InventoryBasic npcInventory = new InventoryBasic("Items", false, 8);
   protected int chatIndex;
   public ArrayList interactCommands = new ArrayList();
   private int randomTickDivider;

   public EntityNPC(World world) {
      super(world);
      this.field_70180_af.func_187214_a(dwName, "");
      this.field_70180_af.func_187214_a(dwNickname, "");
      this.field_70180_af.func_187214_a(dwID, -1);
      this.field_70180_af.func_187214_a(dwModel, 0);
      this.field_70180_af.func_187214_a(dwTextureIndex, 0);
      this.field_70180_af.func_187214_a(dwCustomSteveTexture, "");
      this.field_70180_af.func_187214_a(dwProfession, -1);
      this.field_70180_af.func_187214_a(dwNPCAI, EnumTrainerAI.Wander.ordinal());
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(100.0);
      this.initAI();
   }

   public void init(String name) {
      this.setName(name);
      this.func_70606_j(100.0F);
      if (this.getId() == -1) {
         this.setId(idIndex++);
      }

   }

   @SideOnly(Side.CLIENT)
   public String getTexture() {
      BaseTrainer t = this.getBaseTrainer();
      if (!t.textures.isEmpty() && this.getTextureIndex() > -1) {
         if (this.getTextureIndex() >= t.textures.size()) {
            this.setTextureIndex(t.textures.size() - 1);
         }

         return ((String)t.textures.get(this.getTextureIndex())).equals("Custom_RP") ? "pixelmon:textures/steve/" + this.getCustomSteveTexture() + ".png" : "pixelmon:textures/steve/" + (String)t.textures.get(this.getTextureIndex()) + ".png";
      } else if (this.getTextureIndex() == -1) {
         return "pixelmon:textures/steve/" + this.getCustomSteveTexture();
      } else {
         return t != NPCRegistryTrainers.Steve && !t.name.equals("Steve") ? "pixelmon:textures/steve/" + t.name.toLowerCase() + ".png" : "pixelmon:textures/steve/" + this.getCustomSteveTexture().toLowerCase() + ".png";
      }
   }

   public String getCustomSteveTexture() {
      return (String)this.field_70180_af.func_187225_a(dwCustomSteveTexture);
   }

   public void setCustomSteveTexture(String tex) {
      BaseTrainer t = this.getBaseTrainer();

      try {
         if (this.getTextureIndex() >= 0 && t.textures.size() > this.getTextureIndex()) {
            String textureName = (String)t.textures.get(this.getTextureIndex());
            if (textureName != null && textureName.equals("Custom_PN")) {
               Thread thread = new Thread(() -> {
                  String texture = this.getBase64Texture(tex);
                  this.func_184102_h().func_152344_a(() -> {
                     this.field_70180_af.func_187227_b(dwCustomSteveTexture, tex + ";" + texture);
                  });
               });
               thread.setName("Texture loader");
               thread.setDaemon(false);
               thread.start();
               return;
            }
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      this.field_70180_af.func_187227_b(dwCustomSteveTexture, tex);
   }

   private String getBase64Texture(String name) {
      if (name != null && !name.trim().isEmpty()) {
         try {
            GameProfile profile = FMLCommonHandler.instance().getMinecraftServerInstance().func_152358_ax().func_152655_a(name);
            if (profile != null) {
               String base64;
               if (!profile.getProperties().isEmpty()) {
                  base64 = ((Property)profile.getProperties().get("textures").iterator().next()).getValue();
               } else {
                  InputStream is = (new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + profile.getId().toString().replace("-", ""))).openConnection().getInputStream();
                  base64 = (new JsonParser()).parse(new InputStreamReader(is)).getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
               }

               return (new JsonParser()).parse(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(base64)))).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString().replace("http://textures.minecraft.net/texture/", "");
            }
         } catch (Throwable var5) {
         }

         return "dc1c77ce8e54925ab58125446ec53b0cdd3d0ca3db273eb908d5482787ef4016";
      } else {
         return "dc1c77ce8e54925ab58125446ec53b0cdd3d0ca3db273eb908d5482787ef4016";
      }
   }

   protected boolean func_70692_ba() {
      return !this.func_104002_bU();
   }

   public String func_70005_c_() {
      return (String)this.field_70180_af.func_187225_a(dwName);
   }

   public void setName(String name) {
      this.field_70180_af.func_187227_b(dwName, name);
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return false;
   }

   public boolean func_70104_M() {
      return false;
   }

   public boolean func_184652_a(EntityPlayer player) {
      return false;
   }

   public boolean func_70039_c(NBTTagCompound nbt) {
      return !this.field_70128_L && (!this.func_70692_ba() || PixelmonConfig.writeEntitiesToWorld) && super.func_70039_c(nbt);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74778_a("Name", this.func_70005_c_());
      if (this.npcLocation == null || this.npcLocation == SpawnLocationType.Land) {
         this.npcLocation = SpawnLocationType.LandVillager;
      }

      nbt.func_74768_a("trainerLocation", this.npcLocation.ordinal());
      nbt.func_74778_a("BaseTrainer", this.getBaseTrainer().name);
      if (this.getBaseTrainer() == NPCRegistryTrainers.Steve || this.getBaseTrainer().textures.size() > 1) {
         nbt.func_74768_a("TextureIndex", this.getTextureIndex());
      }

      nbt.func_74778_a("CustomSteveTexture", this.getCustomSteveTexture());
      nbt.func_74777_a("Profession", (short)this.getProfession());
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.npcInventory.func_70302_i_(); ++i) {
         ItemStack itemstack = this.npcInventory.func_70301_a(i);
         if (!itemstack.func_190926_b()) {
            nbttaglist.func_74742_a(itemstack.func_77955_b(new NBTTagCompound()));
         }
      }

      nbt.func_74782_a("Inventory", nbttaglist);
      nbt.func_74777_a("AIMode", (short)this.getAIMode().ordinal());
      NBTTagCompound commandsNbt = new NBTTagCompound();
      NBTTagList interactList = new NBTTagList();
      Iterator var5 = this.interactCommands.iterator();

      while(var5.hasNext()) {
         String interactCommand = (String)var5.next();
         interactList.func_74742_a(new NBTTagString(interactCommand));
      }

      commandsNbt.func_74782_a("interactCommands", interactList);
      nbt.func_74782_a("Commands", commandsNbt);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setName(nbt.func_74779_i("Name"));
      if (nbt.func_74764_b("trainerLocation")) {
         this.npcLocation = SpawnLocationType.getFromIndex(nbt.func_74762_e("trainerLocation"));
         if (this.npcLocation == null) {
            this.npcLocation = SpawnLocationType.LandVillager;
         }
      } else {
         this.npcLocation = SpawnLocationType.Land;
      }

      this.init(this.func_70005_c_());
      BaseTrainer trainer = null;
      if (nbt.func_74764_b("ModelIndex")) {
         trainer = ServerNPCRegistry.trainers.getById(nbt.func_74762_e("ModelIndex"));
      } else if (nbt.func_74764_b("BaseTrainer")) {
         trainer = NPCRegistryTrainers.getByName(nbt.func_74779_i("BaseTrainer"));
      }

      if (trainer != null) {
         this.setBaseTrainer(trainer);
         if (this.getBaseTrainer().textures.size() > 1) {
            this.setTextureIndex(nbt.func_74762_e("TextureIndex"));
         }
      }

      if (nbt.func_74779_i("CustomSteveTexture").contains(";")) {
         this.field_70180_af.func_187227_b(dwCustomSteveTexture, nbt.func_74779_i("CustomSteveTexture"));
      } else {
         this.setCustomSteveTexture(nbt.func_74779_i("CustomSteveTexture"));
      }

      this.setProfession(nbt.func_74765_d("Profession"));
      NBTTagList nbttaglist = nbt.func_150295_c("Inventory", 10);

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         ItemStack itemstack = new ItemStack(nbttaglist.func_150305_b(i));
         if (!itemstack.func_190926_b()) {
            this.npcInventory.func_174894_a(itemstack);
         }
      }

      if (this.getProfession() == 0) {
         this.initVilagerAI();
         this.field_70714_bg.func_75776_a(6, new AIHarvestFarmLand(this, 0.6));
      }

      if (nbt.func_74764_b("IsPersistent") && nbt.func_74767_n("IsPersistent")) {
         this.func_110163_bv();
      }

      if (nbt.func_74764_b("AIMode")) {
         this.setAIMode(EnumTrainerAI.getFromOrdinal(nbt.func_74765_d("AIMode")));
         this.initAI();
      }

      if (nbt.func_74764_b("Commands")) {
         NBTTagCompound cmdNbt = nbt.func_74775_l("Commands");
         if (cmdNbt.func_74764_b("interactCommands")) {
            NBTTagList wins = cmdNbt.func_150295_c("interactCommands", 8);

            for(int i = 0; i < wins.func_74745_c(); ++i) {
               this.interactCommands.add(wins.func_150307_f(i));
            }
         }
      }

   }

   public void initAI() {
      this.initDefaultAI();
   }

   public boolean func_70601_bi() {
      int var1 = MathHelper.func_76128_c(this.field_70165_t);
      int var2 = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
      int var3 = MathHelper.func_76128_c(this.field_70161_v);
      Block block = this.field_70170_p.func_180495_p(new BlockPos(var1, var2 - 1, var3)).func_177230_c();
      return block == Blocks.field_150349_c || block == Blocks.field_150354_m;
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      return this.interactWithNPC(player, hand);
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      return false;
   }

   public void setId(int i) {
      this.field_70180_af.func_187227_b(dwID, i);
   }

   public int func_70641_bl() {
      return 1;
   }

   public int getId() {
      return (Integer)this.field_70180_af.func_187225_a(dwID);
   }

   public abstract String getDisplayText();

   public String getSubTitleText() {
      return null;
   }

   public EnumBossMode getBossMode() {
      return EnumBossMode.NotBoss;
   }

   public BaseTrainer getBaseTrainer() {
      int trainerId = (Integer)this.field_70180_af.func_187225_a(dwModel);
      return ServerNPCRegistry.trainers.getById(trainerId);
   }

   public void setBaseTrainer(BaseTrainer trainer) {
      this.field_70180_af.func_187227_b(dwModel, trainer.id);
   }

   @SideOnly(Side.CLIENT)
   public boolean bindTexture() {
      if (this.getTextureIndex() == -1) {
         return false;
      } else {
         BaseTrainer t = this.getBaseTrainer();
         if (t == NPCRegistryTrainers.Steve || t.name.equals("Steve")) {
            String textureName = (String)t.textures.get(this.getTextureIndex());
            if (textureName.equals("Custom_PN") && this.getCustomSteveTexture().contains(";")) {
               String id = this.getCustomSteveTexture().split(";")[1];
               ResourceLocation rl = new ResourceLocation("playerskins", id);
               if (Minecraft.func_71410_x().field_71446_o.func_110581_b(rl) == null) {
                  try {
                     BufferedImage image = ImageIO.read(new URL("http://textures.minecraft.net/texture/" + id));
                     if (image.getHeight() < 64) {
                        BufferedImage resized = new BufferedImage(image.getWidth(), image.getWidth(), 2);
                        int x = 0;

                        while(true) {
                           int y;
                           if (x >= image.getWidth()) {
                              for(x = 32; x < resized.getWidth(); ++x) {
                                 for(y = 0; y < 16; ++y) {
                                    int rgb = resized.getRGB(x, y);
                                    if (rgb == -16777216) {
                                       resized.setRGB(x, y, 0);
                                    }
                                 }
                              }

                              for(x = 0; x < 16; ++x) {
                                 for(y = 16; y < 32; ++y) {
                                    resized.setRGB(x + 16, y + 32, resized.getRGB(x, y));
                                 }
                              }

                              for(x = 40; x < 56; ++x) {
                                 for(y = 16; y < 32; ++y) {
                                    resized.setRGB(x - 8, y + 32, resized.getRGB(x, y));
                                 }
                              }

                              image = resized;
                              break;
                           }

                           for(y = 0; y < image.getHeight(); ++y) {
                              resized.setRGB(x, y, image.getRGB(x, y));
                           }

                           ++x;
                        }
                     }

                     Minecraft.func_71410_x().field_71446_o.func_110579_a(rl, new DynamicTexture(image));
                     Minecraft.func_71410_x().field_71446_o.func_110577_a(rl);
                  } catch (IOException var10) {
                     var10.printStackTrace();
                     Minecraft.func_71410_x().field_71446_o.func_110577_a(DefaultPlayerSkin.func_177334_a(this.func_110124_au()));
                  }
               } else {
                  Minecraft.func_71410_x().field_71446_o.func_110577_a(rl);
               }

               return true;
            }

            if (textureName.equals("Custom_RP") && (this.getCustomSteveTexture().equals("") || !Pixelmon.proxy.resourceLocationExists(new ResourceLocation(this.getTexture())))) {
               Minecraft.func_71410_x().field_71446_o.func_110577_a(DefaultPlayerSkin.func_177334_a(this.func_110124_au()));
               return true;
            }

            if (textureName.equals("Steve")) {
               Minecraft.func_71410_x().field_71446_o.func_110577_a(DefaultPlayerSkin.func_177334_a(this.func_110124_au()));
               return true;
            }
         }

         return false;
      }
   }

   public Vector3f getScale() {
      BaseTrainer base = this.getBaseTrainer();
      return !base.name.equals("Youngster") && !base.name.equals("Lass") && !base.name.equals("PreschoolerGirl") ? new Vector3f(1.0F, 1.0F, 1.0F) : new Vector3f(0.85F, 0.8F, 0.85F);
   }

   public static Optional locateNPCClient(World world, int id, Class type) {
      try {
         for(int i = 0; i < world.field_72996_f.size(); ++i) {
            Entity entity = (Entity)world.field_72996_f.get(i);
            if (type.isInstance(entity) && ((EntityNPC)entity).getId() == id) {
               return Optional.of((EntityNPC)entity);
            }
         }

         return Optional.empty();
      } catch (Exception var5) {
         var5.printStackTrace();
         throw new IllegalStateException("How did this happen..");
      }
   }

   public static Optional locateNPCServer(World world, int id, Class type) {
      try {
         return (Optional)world.func_73046_m().func_175586_a(() -> {
            for(int i = 0; i < world.field_72996_f.size(); ++i) {
               Entity entity = (Entity)world.field_72996_f.get(i);
               if (type.isInstance(entity) && ((EntityNPC)entity).getId() == id) {
                  return Optional.of((EntityNPC)entity);
               }
            }

            return Optional.empty();
         }).get();
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new IllegalStateException("How did this happen..");
      }
   }

   public static Optional locateNPCServer(World world, String name, Class type, String langCode) {
      try {
         return (Optional)world.func_73046_m().func_175586_a(() -> {
            for(int i = 0; i < world.field_72996_f.size(); ++i) {
               Entity entity = (Entity)world.field_72996_f.get(i);
               if (type.isInstance(entity)) {
                  String npcName;
                  if (type == NPCTrainer.class) {
                     npcName = ((NPCTrainer)entity).getName(langCode);
                  } else {
                     npcName = ((EntityNPC)entity).func_70005_c_();
                  }

                  if (name.equals(npcName)) {
                     return Optional.of((EntityNPC)entity);
                  }
               }
            }

            return Optional.empty();
         }).get();
      } catch (Exception var5) {
         var5.printStackTrace();
         return Optional.empty();
      }
   }

   public void unloadEntity() {
      this.func_70623_bb();
      this.func_70106_y();
   }

   public int getTextureIndex() {
      return (Integer)this.field_70180_af.func_187225_a(dwTextureIndex);
   }

   public void setTextureIndex(int index) {
      this.field_70180_af.func_187227_b(dwTextureIndex, index);
   }

   public void func_70071_h_() {
      if (this.func_70692_ba() && !this.field_70170_p.field_72995_K) {
         this.checkForRarityDespawn();
      }

      try {
         super.func_70071_h_();
      } catch (ClassCastException var2) {
      }

   }

   protected void checkForRarityDespawn() {
      if (this.despawnCounter > 0) {
         --this.despawnCounter;
      } else if (this.despawnCounter == 0) {
         if (!this.playersNearby()) {
            this.func_70106_y();
         }
      } else {
         this.despawnCounter = (int)(Math.random() * (double)(intMaxTicksToDespawn - intMinTicksToDespawn) + (double)intMinTicksToDespawn);
      }

   }

   protected boolean playersNearby() {
      for(int i = 0; i < this.field_70170_p.field_73010_i.size(); ++i) {
         EntityPlayer player = (EntityPlayer)this.field_70170_p.field_73010_i.get(i);
         double distancex = player.field_70165_t - this.field_70165_t;
         double distancey = player.field_70163_u - this.field_70163_u;
         double distancez = player.field_70161_v - this.field_70161_v;
         double distancesquared = distancex * distancex + distancey * distancey + distancez * distancez;
         if (distancesquared < (double)(PixelmonConfig.despawnRadius * PixelmonConfig.despawnRadius)) {
            return true;
         }
      }

      return false;
   }

   public void setProfession(int professionId) {
      this.field_70180_af.func_187227_b(dwProfession, professionId);
   }

   public int getProfession() {
      return (Integer)this.field_70180_af.func_187225_a(dwProfession);
   }

   public void initDefaultAI() {
      ((PathNavigateGround)this.func_70661_as()).func_179693_d(false);
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(1, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.field_70714_bg.func_75776_a(2, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
   }

   public void initWanderingAI() {
      ((PathNavigateGround)this.func_70661_as()).func_179693_d(false);
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(1, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.field_70714_bg.func_75776_a(2, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.6));
   }

   public void initVilagerAI() {
      ((PathNavigateGround)this.func_70661_as()).func_179688_b(true);
      ((PathNavigateGround)this.func_70661_as()).func_179693_d(false);
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.800000011920929, 0.6000000238418579));
      this.field_70714_bg.func_75776_a(2, new EntityAIMoveIndoors(this));
      this.field_70714_bg.func_75776_a(3, new EntityAIRestrictOpenDoor(this));
      this.field_70714_bg.func_75776_a(4, new EntityAIOpenDoor(this, true));
      this.field_70714_bg.func_75776_a(5, new EntityAIMoveTowardsRestriction(this, 0.6));
      this.field_70714_bg.func_75776_a(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.6));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.func_98053_h(true);
   }

   public boolean isFarmItemInInventory() {
      for(int i = 0; i < this.npcInventory.func_70302_i_(); ++i) {
         ItemStack itemstack = this.npcInventory.func_70301_a(i);
         if (itemstack != null && (itemstack.func_77973_b() == Items.field_151014_N || itemstack.func_77973_b() == Items.field_151174_bG || itemstack.func_77973_b() == Items.field_151172_bF)) {
            return true;
         }
      }

      return false;
   }

   public boolean func_174820_d(int inventorySlot, ItemStack itemStackIn) {
      if (super.func_174820_d(inventorySlot, itemStackIn)) {
         return true;
      } else {
         int j = inventorySlot - 300;
         if (j >= 0 && j < this.npcInventory.func_70302_i_()) {
            this.npcInventory.func_70299_a(j, itemStackIn);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hasItemToPlant() {
      boolean flag = this.getProfession() == 0;
      return flag ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
   }

   private boolean hasEnoughItems(int multiplier) {
      boolean flag = this.getProfession() == 0;

      for(int j = 0; j < this.npcInventory.func_70302_i_(); ++j) {
         ItemStack itemstack = this.npcInventory.func_70301_a(j);
         if (itemstack != null) {
            if (itemstack.func_77973_b() == Items.field_151025_P && itemstack.func_190916_E() >= 3 * multiplier || itemstack.func_77973_b() == Items.field_151174_bG && itemstack.func_190916_E() >= 12 * multiplier || itemstack.func_77973_b() == Items.field_151172_bF && itemstack.func_190916_E() >= 12 * multiplier) {
               return true;
            }

            if (flag && itemstack.func_77973_b() == Items.field_151015_O && itemstack.func_190916_E() >= 9 * multiplier) {
               return true;
            }
         }
      }

      return false;
   }

   public InventoryBasic getNPCInventory() {
      return this.npcInventory;
   }

   protected void func_175445_a(EntityItem itemEntity) {
      ItemStack itemstack = itemEntity.func_92059_d();
      Item item = itemstack.func_77973_b();
      if (this.canVillagerPickupItem(item)) {
         ItemStack itemstack1 = this.npcInventory.func_174894_a(itemstack);
         if (itemstack1 == null) {
            itemEntity.func_70106_y();
         } else {
            itemstack.func_190920_e(itemstack1.func_190916_E());
         }
      }

   }

   private boolean canVillagerPickupItem(Item itemIn) {
      return itemIn == Items.field_151025_P || itemIn == Items.field_151174_bG || itemIn == Items.field_151172_bF || itemIn == Items.field_151015_O || itemIn == Items.field_151014_N;
   }

   protected void func_70619_bc() {
      if (this.getProfession() != -1 && --this.randomTickDivider <= 0) {
         BlockPos blockpos = new BlockPos(this);
         this.field_70170_p.func_175714_ae().func_176060_a(blockpos);
         this.randomTickDivider = 70 + this.field_70146_Z.nextInt(50);
         Village villageObj = this.field_70170_p.func_175714_ae().func_176056_a(blockpos, 32);
         if (villageObj == null) {
            this.func_110177_bN();
         } else {
            BlockPos blockpos1 = villageObj.func_180608_a();
            this.func_175449_a(blockpos1, (int)((float)villageObj.func_75568_b() * 1.0F));
         }
      }

      super.func_70619_bc();
   }

   public EnumTrainerAI getAIMode() {
      return EnumTrainerAI.getFromOrdinal((Integer)this.field_70180_af.func_187225_a(dwNPCAI));
   }

   public void setAIMode(EnumTrainerAI mode) {
      this.field_70180_af.func_187227_b(dwNPCAI, mode.ordinal());
   }

   static {
      dwName = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187194_d);
      dwNickname = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187194_d);
      dwID = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187192_b);
      dwModel = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187192_b);
      dwTextureIndex = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187192_b);
      dwCustomSteveTexture = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187194_d);
      dwProfession = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187192_b);
      dwNPCAI = EntityDataManager.func_187226_a(EntityNPC.class, DataSerializers.field_187192_b);
      intMinTicksToDespawn = 600;
      intMaxTicksToDespawn = 1200;
   }
}
