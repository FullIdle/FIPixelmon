package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetChattingNPCTextures;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCEditData;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GymNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class NPCChatting extends EntityIndexedNPC {
   private ArrayList chatPages = new ArrayList();
   boolean usingDefaultName = true;
   boolean usingDefaultChat = true;
   protected EnumNPCType npcType;

   public NPCChatting(World world) {
      super(world);
      this.npcType = EnumNPCType.ChattingNPC;
   }

   public void init(GeneralNPCData data) {
      this.npcIndex = data.id;
      this.nameIndex = data.getRandomNameIndex();
      this.chatIndex = data.getRandomChatIndex();
      if (this.getId() == -1) {
         this.setId(idIndex++);
      }

      this.npcLocation = SpawnLocationType.LandVillager;
   }

   public void init(GymNPCData data) {
      this.npcIndex = "_gym_" + data.id;
      this.nameIndex = data.getRandomNameIndex();
      this.chatIndex = data.getRandomChatIndex();
      if (this.getId() == -1) {
         this.setId(idIndex++);
      }

      this.npcLocation = SpawnLocationType.LandVillager;
   }

   public void init(String name) {
      super.init(name);
      this.setCustomSteveTexture("npcchat1.png");
   }

   public boolean func_70104_M() {
      return false;
   }

   public String getTexture() {
      return "pixelmon:textures/steve/" + this.getCustomSteveTexture();
   }

   public String getDisplayText() {
      return "";
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74757_a("DefaultName", this.usingDefaultName);
      nbt.func_74757_a("DefaultGreet", this.usingDefaultChat);
      nbt.func_74768_a("chatNum", this.chatPages.size());
      int i = 0;

      for(Iterator var3 = this.chatPages.iterator(); var3.hasNext(); ++i) {
         String page = (String)var3.next();
         nbt.func_74778_a("chat" + i, page);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.usingDefaultName = nbt.func_74767_n("DefaultName");
      this.usingDefaultChat = nbt.func_74767_n("DefaultGreet");
      int numPages = nbt.func_74762_e("chatNum");

      for(int i = 0; i < numPages; ++i) {
         this.chatPages.add(nbt.func_74779_i("chat" + i));
      }

      if (this.getProfession() != 0) {
         this.initDefaultAI();
      }

   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.func_184586_b(hand);
      if (player instanceof EntityPlayerMP) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         if (Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, this.npcType, playerMP))) {
            return false;
         }

         String loc = playerMP.field_71148_cg;
         if (!itemstack.func_190926_b() && itemstack.func_77973_b() instanceof ItemNPCEditor) {
            if (!ItemNPCEditor.checkPermission(playerMP)) {
               return false;
            }

            Pixelmon.network.sendTo(new SetNPCEditData(this.getName(loc), this.getChat(loc), this.npcType), playerMP);
            Pixelmon.network.sendTo(new SetChattingNPCTextures(), playerMP);
            OpenScreen.open(player, EnumGuiScreen.NPCChatEditor, this.getId());
         } else if (!this.getChat(loc).isEmpty()) {
            NPCChatEvent event = new NPCChatEvent(this, player, this.getChat(loc));
            if (Pixelmon.EVENT_BUS.post(event)) {
               return false;
            }

            Pixelmon.network.sendTo(new SetNPCData(this.getName(loc), event.getChat(), this.npcType), (EntityPlayerMP)player);
            OpenScreen.open(player, EnumGuiScreen.NPCChat, this.getId());
         }
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return true;
   }

   public ArrayList getChat(String langCode) {
      if (this.usingDefaultChat) {
         int index = this.chatIndex;
         return this.npcIndex.startsWith("_gym_") ? new ArrayList(Arrays.asList(ServerNPCRegistry.getTranslatedGymMemberChat(langCode, this.npcIndex.substring(5), index))) : new ArrayList(Arrays.asList(ServerNPCRegistry.villagers.getTranslatedChat(langCode, this.npcIndex, index)));
      } else {
         return this.chatPages;
      }
   }

   public String getName(String langCode) {
      if (this.usingDefaultName) {
         int index = this.nameIndex;
         return this.npcIndex.startsWith("_gym_") ? ServerNPCRegistry.getTranslatedGymMemberName(langCode, this.npcIndex.substring(5), index) : ServerNPCRegistry.villagers.getTranslatedName(langCode, this.npcIndex, index);
      } else {
         return this.func_70005_c_();
      }
   }

   public void setChat(ArrayList pages) {
      this.chatPages = pages;
      this.usingDefaultChat = false;
   }

   public void setName(String name) {
      super.setName(name);
      this.usingDefaultName = false;
   }

   public void cycleTexture(EntityPlayerMP p, ClientNPCData newData) {
      GeneralNPCData data = ServerNPCRegistry.villagers.getData(newData.getID());
      if (data != null) {
         this.init(data);
         this.setTextureIndex(0);
         this.setCustomSteveTexture(newData.getTexture());
         Pixelmon.network.sendTo(new SetNPCEditData(this.getName(p.field_71148_cg), this.getChat(p.field_71148_cg), this.npcType), p);
      }
   }
}
