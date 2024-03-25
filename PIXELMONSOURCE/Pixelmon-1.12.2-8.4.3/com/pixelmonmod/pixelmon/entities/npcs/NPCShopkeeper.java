package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCEditData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetShopkeeperClient;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperChat;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.items.helpers.ItemHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class NPCShopkeeper extends EntityIndexedNPC {
   private long lastUpdatedTime = 0L;
   private ArrayList itemList;
   ArrayList playerList = new ArrayList();

   public NPCShopkeeper(World world) {
      super(world);
   }

   public void init(ShopkeeperData data) {
      this.npcIndex = data.id;
      this.nameIndex = data.getRandomNameIndex();
      this.chatIndex = data.getRandomChatIndex();
      this.setCustomSteveTexture(data.getRandomTexture());
      if (this.getId() == -1) {
         this.setId(idIndex++);
      }

   }

   public void init(String name) {
      super.init(name);
      if (this.getCustomSteveTexture().equals("")) {
         this.setCustomSteveTexture("npcchat1.png");
      }

   }

   public boolean func_70104_M() {
      return false;
   }

   public void initRandom(String biomeID) {
      ShopkeeperData data = ServerNPCRegistry.shopkeepers.getRandomSpawning(biomeID);
      if (data == null) {
         this.func_70106_y();
      } else {
         this.init(data);
      }

      this.npcLocation = SpawnLocationType.LandNPC;
   }

   public String getTexture() {
      return "pixelmon:textures/steve/" + this.getCustomSteveTexture();
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.initDefaultAI();
      if (nbt.func_74764_b("ShopItems")) {
         NBTTagList list = nbt.func_150295_c("ShopItems", 10);
         this.itemList = new ArrayList();

         for(int i = 0; i < list.func_74745_c(); ++i) {
            ShopItemWithVariation item = ShopItemWithVariation.getFromNBT(this.npcIndex, list.func_150305_b(i));
            if (item != null) {
               this.getItemList().add(item);
            }
         }
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      NBTTagList list = new NBTTagList();
      if (this.getItemList() != null) {
         Iterator var3 = this.getItemList().iterator();

         while(var3.hasNext()) {
            ShopItemWithVariation item = (ShopItemWithVariation)var3.next();
            item.writeToNBT(list);
         }

         nbt.func_74782_a("ShopItems", list);
      }

   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      ItemStack itemStack = player.func_184586_b(hand);
      if (player instanceof EntityPlayerMP) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         if (Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.Shopkeeper, playerMP))) {
            return false;
         }

         if (itemStack.func_190926_b() || !(itemStack.func_77973_b() instanceof ItemNPCEditor)) {
            if (this.getItemList() == null || this.getItemList().isEmpty()) {
               this.loadItems();
            }

            this.sendItemsToPlayer(player);
            OpenScreen.open(player, EnumGuiScreen.Shopkeeper, this.getId());
            return true;
         }

         if (!ItemNPCEditor.checkPermission(playerMP)) {
            return false;
         }

         String loc = playerMP.field_71148_cg;
         Pixelmon.network.sendTo(new SetShopkeeperClient(loc), playerMP);
         Pixelmon.network.sendTo(new SetNPCEditData(ServerNPCRegistry.shopkeepers.getJsonName(this.npcIndex), this.getShopkeeperName(loc), this.getCustomSteveTexture()), playerMP);
         OpenScreen.open(player, EnumGuiScreen.ShopkeeperEditor, this.getId());
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return true;
   }

   public void sendItemsToPlayer(EntityPlayer player) {
      ArrayList sellList = this.getSellList(player);
      String loc = ((EntityPlayerMP)player).field_71148_cg;
      this.playerList.add(player.func_110124_au());
      Pixelmon.network.sendTo(new SetNPCData(this.getShopkeeperName(loc), this.getShopkeeperChat(loc), this.getItemList(), sellList), (EntityPlayerMP)player);
   }

   public ArrayList getSellList(EntityPlayer player) {
      ArrayList sellList = new ArrayList();
      Iterator var3 = player.field_71071_by.field_70462_a.iterator();

      while(var3.hasNext()) {
         ItemStack item = (ItemStack)var3.next();
         if (!item.func_190926_b()) {
            ShopItemWithVariation shopItem = this.getExistingItem(item);
            if (shopItem == null) {
               BaseShopItem baseItem = ServerNPCRegistry.shopkeepers.getItem(item);
               if (baseItem != null) {
                  shopItem = new ShopItemWithVariation(new ShopItem(baseItem, 1.0F, 1.0F, false), 1.0F);
               }
            }

            if (shopItem != null && shopItem.canSell() && !this.alreadyListed(sellList, shopItem)) {
               sellList.add(shopItem);
            }
         }
      }

      return sellList;
   }

   private boolean alreadyListed(ArrayList sellList, ShopItemWithVariation shopItem) {
      Iterator var3 = sellList.iterator();

      ShopItemWithVariation item;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         item = (ShopItemWithVariation)var3.next();
      } while(item.getItemStack() != shopItem.getItemStack());

      return true;
   }

   private ShopItemWithVariation getExistingItem(ItemStack item) {
      Iterator var2 = this.getItemList().iterator();

      ShopItemWithVariation si;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         si = (ShopItemWithVariation)var2.next();
      } while(!ItemHelper.isItemStackEqual(si.getItemStack(), item));

      return si;
   }

   private boolean isItemStackEqual(ItemStack a, ItemStack b) {
      if (a.func_77973_b() != b.func_77973_b()) {
         return false;
      } else if (a.func_77952_i() != b.func_77952_i()) {
         return false;
      } else {
         return (a.func_77978_p() != null || b.func_77978_p() == null) && (b.func_77978_p() != null || a.func_77978_p() == null) ? false : false;
      }
   }

   private ShopkeeperChat getShopkeeperChat(String langCode) {
      int index = this.chatIndex;
      return ServerNPCRegistry.shopkeepers.getTranslatedChat(langCode, this.npcIndex, index);
   }

   public String getShopkeeperName(String langCode) {
      return ServerNPCRegistry.shopkeepers.getTranslatedName(langCode, this.npcIndex, this.nameIndex);
   }

   public void loadItems() {
      this.lastUpdatedTime = this.field_70170_p.func_82737_E();
      ShopkeeperData skd = ServerNPCRegistry.shopkeepers.getById(this.npcIndex);
      this.itemList = skd != null ? skd.getItemList() : new ArrayList();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K && this.playerList.size() > 0) {
         for(int i = 0; i < this.playerList.size(); ++i) {
            EntityPlayer player = this.field_70170_p.func_152378_a((UUID)this.playerList.get(i));
            if (player == null) {
               this.playerList.remove(i);
               --i;
            }
         }
      }

      if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_82737_E() > this.lastUpdatedTime + 24000L && this.playerList.size() == 0) {
         this.loadItems();
      }

   }

   public ArrayList getItemList() {
      return this.itemList;
   }

   public void cycleJson(EntityPlayerMP p, String newJSON) {
      ShopkeeperData id = ServerNPCRegistry.shopkeepers.getById(newJSON);
      if (id != null) {
         this.init(id);
         String loc = p.field_71148_cg;
         Pixelmon.network.sendTo(new SetNPCEditData(ServerNPCRegistry.shopkeepers.getJsonName(this.npcIndex), this.getShopkeeperName(loc), this.getCustomSteveTexture()), p);
      }
   }

   public void cycleName(EntityPlayerMP p, int nameIndex) {
      this.nameIndex = nameIndex;
      String loc = p.field_71148_cg;
      Pixelmon.network.sendTo(new SetNPCEditData(ServerNPCRegistry.shopkeepers.getJsonName(this.npcIndex), this.getShopkeeperName(loc), this.getCustomSteveTexture()), p);
   }

   public String getDisplayText() {
      return I18n.func_74838_a("gui.shopkeeper.name");
   }

   public String getSubTitleText() {
      return null;
   }
}
