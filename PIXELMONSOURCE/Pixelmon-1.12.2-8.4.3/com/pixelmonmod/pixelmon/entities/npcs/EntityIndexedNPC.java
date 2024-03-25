package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class EntityIndexedNPC extends EntityNPC {
   protected int nameIndex;
   protected int chatIndex;
   protected String npcIndex = "NPC";

   public EntityIndexedNPC(World world) {
      super(world);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74778_a("TrainerIndex", this.npcIndex);
      nbt.func_74768_a("ChatIndex", this.chatIndex);
      nbt.func_74768_a("NameIndex", this.nameIndex);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.npcIndex = nbt.func_74779_i("TrainerIndex");
      this.chatIndex = nbt.func_74762_e("ChatIndex");
      this.nameIndex = nbt.func_74762_e("NameIndex");
      if (this.getId() == -1) {
         this.setId(idIndex++);
      }

   }

   public String getTexture() {
      return "pixelmon:textures/steve/" + this.getCustomSteveTexture();
   }

   public String getDisplayText() {
      return "";
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      return false;
   }

   public ArrayList getChat(String langCode) {
      int index = this.chatIndex;
      return new ArrayList(Arrays.asList(ServerNPCRegistry.villagers.getTranslatedChat(langCode, this.npcIndex, index)));
   }

   public String getName(String langCode) {
      int index = this.nameIndex;
      return ServerNPCRegistry.villagers.getTranslatedName(langCode, this.npcIndex, index);
   }
}
