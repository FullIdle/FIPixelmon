package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.HealerEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityHealer;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class NPCNurseJoy extends EntityNPC {
   public NPCNurseJoy(World world) {
      super(world);
      this.npcLocation = SpawnLocationType.LandVillager;
      this.setTextureIndex(RandomHelper.getRandomNumberBetween(0, 1));
   }

   public NPCNurseJoy(World world, int texture) {
      super(world);
      this.npcLocation = SpawnLocationType.LandVillager;
      this.setTextureIndex(texture);
   }

   public String getDisplayText() {
      return "";
   }

   public String getTexture() {
      return this.getTextureIndex() == 1 ? "pixelmon:textures/steve/nursejoy.png" : "pixelmon:textures/steve/doctor.png";
   }

   public boolean func_70104_M() {
      return false;
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      if (player instanceof EntityPlayerMP) {
         if (Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.NurseJoy, player))) {
            return false;
         }

         TileEntityHealer healer = (TileEntityHealer)BlockHelper.findClosestTileEntity(TileEntityHealer.class, this, 8.0, (h) -> {
            return !h.beingUsed;
         });
         if (healer != null) {
            if (!Pixelmon.EVENT_BUS.post(new HealerEvent.Pre(player, healer.func_174877_v(), true))) {
               Pokerus.informPlayer((EntityPlayerMP)player);
               healer.use(this, player);
            }
         } else {
            ChatHandler.sendChat(player, "gui.nursejoy.full");
         }
      }

      return true;
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.initDefaultAI();
   }
}
