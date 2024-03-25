package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemFishingRod;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class NPCFisherman extends NPCChatting {
   public NPCFisherman(World world) {
      super(world);
      this.npcLocation = SpawnLocationType.LandVillager;
   }

   public String getDisplayText() {
      return I18n.func_74838_a("pixelmon.npc.fishermanname");
   }

   public String getTexture() {
      return "pixelmon:textures/steve/oldfisherman.png";
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.func_184586_b(hand);
      if (player instanceof EntityPlayerMP) {
         EntityPlayerMP playerMP = (EntityPlayerMP)player;
         if (Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.OldFisherman, playerMP))) {
            return false;
         }

         String loc = playerMP.field_71148_cg;
         ArrayList chat = new ArrayList();
         if (!itemstack.func_190926_b() && itemstack.func_77973_b() instanceof ItemFishingRod) {
            chat.add("gui.oldfisherman.assess");
            if (!itemstack.func_77942_o()) {
               itemstack.func_77982_d(new NBTTagCompound());
            }

            if (itemstack.func_77973_b() != PixelmonItems.oasFishingRod) {
               if (!itemstack.func_77978_p().func_74764_b("rodQuality")) {
                  int quality = this.getRodQuality();
                  itemstack.func_77978_p().func_74768_a("rodQuality", quality);
                  chat.add("gui.oldfisherman.rod.quality" + quality);
               } else {
                  chat.add("gui.oldfisherman.alreadychecked");
               }
            } else {
               chat.add("gui.oldfisherman.rod.oas");
            }
         } else {
            boolean hasLog = false;
            Iterator var8 = playerMP.field_71071_by.field_70462_a.iterator();

            while(var8.hasNext()) {
               ItemStack stack1 = (ItemStack)var8.next();
               if (stack1.func_77973_b() == PixelmonItems.fishingLog) {
                  hasLog = true;
                  break;
               }
            }

            if (playerMP.field_71071_by.field_184439_c.get(0) != null && ((ItemStack)playerMP.field_71071_by.field_184439_c.get(0)).func_77973_b() == PixelmonItems.fishingLog) {
               hasLog = true;
            }

            ItemStack log = new ItemStack(PixelmonItems.fishingLog);
            Pokemon[] var16 = Pixelmon.storageManager.getParty(playerMP).getAll();
            int var10 = var16.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Pokemon pokemon = var16[var11];
               if (!hasLog && pokemon != null && pokemon.getSpecies() == EnumSpecies.Magikarp && pokemon.getFormEnum().getForm() > 0) {
                  playerMP.func_191521_c(log);
                  chat.add("gui.oldfisherman.givekarplog");
                  break;
               }

               if (!hasLog && pokemon != null && pokemon.getSpecies() == EnumSpecies.Shellos && pokemon.getFormEnum().getForm() > 1) {
                  playerMP.func_191521_c(log);
                  chat.add("gui.oldfisherman.giveshelloslog");
                  break;
               }

               if (!hasLog && pokemon != null && pokemon.getSpecies() == EnumSpecies.Clobbopus && pokemon.getFormEnum().getForm() > 0) {
                  playerMP.func_191521_c(log);
                  chat.add("gui.oldfisherman.givecloblog");
                  break;
               }
            }

            if (chat.isEmpty()) {
               chat.add("gui.oldfisherman.general");
               chat.add("gui.oldfisherman.general2");
            }
         }

         NPCChatEvent event = new NPCChatEvent(this, player, chat);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return false;
         }

         Pixelmon.network.sendTo(new SetNPCData(this.getName(loc), event.getChat()), (EntityPlayerMP)player);
         OpenScreen.open(player, EnumGuiScreen.NPCChat, this.getId());
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return true;
   }

   public String getName(String langCode) {
      return "Old Fisherman";
   }

   public int getRodQuality() {
      double percent = (new Random()).nextDouble();
      if (percent <= 0.3856) {
         return 0;
      } else if (percent <= 0.5785) {
         return 1;
      } else if (percent <= 0.7079) {
         return 2;
      } else if (percent <= 0.8044) {
         return 3;
      } else if (percent <= 0.8815) {
         return 4;
      } else {
         return percent <= 0.9449 ? 5 : 6;
      }
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.initDefaultAI();
   }
}
