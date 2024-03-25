package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.IArmorEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.IItemAttributeModifier;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class ArmorEffectEvent extends Event {
   private final GenericArmor armorItem;
   private final World world;
   private final EntityPlayer player;
   private final ItemStack stack;
   private IArmorEffect armorEffect;
   private IItemAttributeModifier attributeModifier;

   public ArmorEffectEvent(GenericArmor armorItem, World world, EntityPlayer player, ItemStack stack, IArmorEffect armorEffect, IItemAttributeModifier attributeModifier) {
      this.armorItem = armorItem;
      this.world = world;
      this.player = player;
      this.stack = stack;
      this.armorEffect = armorEffect;
      this.attributeModifier = attributeModifier;
   }

   public GenericArmor getArmorItem() {
      return this.armorItem;
   }

   @Nullable
   public World getWorld() {
      return this.world;
   }

   @Nullable
   public EntityPlayer getPlayer() {
      return this.player;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void setArmorEffect(IArmorEffect armorEffect) {
      this.armorEffect = armorEffect;
   }

   public IArmorEffect getArmorEffect() {
      return this.armorEffect;
   }

   public void setAttributeModifier(IItemAttributeModifier attributeModifier) {
      this.attributeModifier = attributeModifier;
   }

   public IItemAttributeModifier getAttributeModifier() {
      return this.attributeModifier;
   }

   @Cancelable
   public static class Attribute extends ArmorEffectEvent {
      public Attribute(GenericArmor armorItem, World world, EntityPlayer player, ItemStack stack, IArmorEffect armorEffect, IItemAttributeModifier attributeModifier) {
         super(armorItem, world, player, stack, armorEffect, attributeModifier);
      }
   }

   @Cancelable
   public static class Effect extends ArmorEffectEvent {
      public Effect(GenericArmor armorItem, World world, EntityPlayer player, ItemStack stack, IArmorEffect armorEffect, IItemAttributeModifier attributeModifier) {
         super(armorItem, world, player, stack, armorEffect, attributeModifier);
      }
   }
}
