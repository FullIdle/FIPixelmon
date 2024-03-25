package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetChattingNPCTextures;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCEditData;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class NPCQuestGiver extends NPCChatting {
   public NPCQuestGiver(World world) {
      super(world);
      this.npcType = EnumNPCType.QuestGiver;
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
            OpenScreen.open(player, EnumGuiScreen.NPCQuestGiverEditor, this.getId());
         } else if (!this.getChat(loc).isEmpty()) {
            NPCChatEvent event = new NPCChatEvent(this, player, this.getChat(loc));
            if (Pixelmon.EVENT_BUS.post(event)) {
               return false;
            }

            Pixelmon.network.sendTo(new SetNPCData(this.getName(loc), event.getChat(), this.npcType), (EntityPlayerMP)player);
            OpenScreen.open(player, EnumGuiScreen.NPCQuestGiver, this.getId());
         }
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return true;
   }
}
