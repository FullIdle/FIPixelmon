package com.pixelmonmod.pixelmon.items.armor;

import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ArmorEffectEvent;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.IArmorEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.IItemAttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GenericArmor extends ItemArmor {
   public IArmorEffect effect = null;
   public IItemAttributeModifier itemAttributeModifier = null;
   public final ItemArmor.ArmorMaterial field_77878_bZ;

   public GenericArmor(String itemName, ItemArmor.ArmorMaterial material, EntityEquipmentSlot armorType) {
      super(material, 0, armorType);
      this.field_77878_bZ = material;
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public GenericArmor setEffect(IArmorEffect effect) {
      this.effect = effect;
      return this;
   }

   public GenericArmor setItemAttributeModifiers(IItemAttributeModifier modifier) {
      this.itemAttributeModifier = modifier;
      return this;
   }

   public Multimap getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
      ArmorEffectEvent.Attribute event = new ArmorEffectEvent.Attribute(this, (World)null, (EntityPlayer)null, stack, this.effect, this.itemAttributeModifier);
      Multimap multimap = super.getAttributeModifiers(slot, stack);
      if (!Pixelmon.EVENT_BUS.post(event) && event.getAttributeModifier() != null && slot == EntityEquipmentSlot.FEET) {
         multimap.putAll(event.getAttributeModifier().getAttributeModifiers(stack, this));
      }

      return multimap;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
      ArmorEffectEvent.Effect event = new ArmorEffectEvent.Effect(this, world, player, itemStack, this.effect, this.itemAttributeModifier);
      if (!Pixelmon.EVENT_BUS.post(event) && event.getArmorEffect() != null) {
         event.getArmorEffect().onArmorTick(world, player, itemStack, this);
      }

   }

   public String func_77658_a() {
      return super.func_77658_a().substring(5);
   }
}
