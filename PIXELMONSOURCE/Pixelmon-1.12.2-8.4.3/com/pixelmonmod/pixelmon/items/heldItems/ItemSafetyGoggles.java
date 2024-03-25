package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.IEquippable;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSafetyGoggles extends ItemHeld implements IEquippable {
   private static final ResourceLocation TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/equippables/safety_goggles.png");

   public ItemSafetyGoggles() {
      super(EnumHeldItems.safetyGoggles, "safety_goggles");
      this.field_77777_bU = 1;
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (Overcoat.isPowderMove(a)) {
         String nickname = pokemon.getNickname();
         pokemon.bc.sendToAll("pixelmon.abilities.activated", nickname, pokemon.getHeldItem().getLocalizedName());
         pokemon.bc.sendToAll("pixelmon.battletext.noeffect", nickname);
         return false;
      } else {
         return true;
      }
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.HEAD;
   }

   public String getEquippableModelKey() {
      return "safety_goggles";
   }

   public ResourceLocation getEquippableTexture() {
      return TEXTURE;
   }

   public Item getEquippableItem() {
      return this;
   }

   public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
      return (!stack.func_77942_o() || !stack.func_77978_p().func_74767_n("Unequippable")) && this.getEquipmentSlot(stack) == armorType;
   }
}
