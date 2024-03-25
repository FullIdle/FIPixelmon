package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBadge extends PixelmonItem implements IEquippable {
   public EnumBadges badge;

   public ItemBadge(EnumBadges badges) {
      super(badges.getFileName());
      this.badge = badges;
      this.func_77625_d(64);
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.badges);
      this.canRepair = false;
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K) {
         ItemStack stack = ItemBadgeCase.findFirstRegisteredBadgeCase(player);
         if (stack != null) {
            ItemStack badge = player.func_184586_b(hand);
            ItemStack toAdd = badge.func_77946_l();
            toAdd.func_190920_e(1);
            if (ItemBadgeCase.addBadge(stack, player, toAdd)) {
               badge.func_190918_g(1);
               return ActionResult.newResult(EnumActionResult.SUCCESS, badge);
            }
         }
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, player.func_184586_b(hand));
   }

   public String getTooltipText() {
      return I18n.func_74838_a("item.badge.tooltip");
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.CHEST;
   }

   public String getEquippableModelKey() {
      return "badge";
   }

   public ResourceLocation getEquippableTexture() {
      return null;
   }

   public Item getEquippableItem() {
      return this;
   }

   public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
      return (!stack.func_77942_o() || !stack.func_77978_p().func_74767_n("Unequippable")) && this.getEquipmentSlot(stack) == armorType;
   }
}
