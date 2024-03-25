package com.pixelmonmod.pixelmon.items;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.badgecase.OpenBadgeCasePacket;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBadgeCase extends PixelmonItem implements IEquippable {
   private final EnumBadgeCase color;

   public ItemBadgeCase(EnumBadgeCase color) {
      super(color.toString().toLowerCase() + "badgecase");
      this.color = color;
      this.func_77625_d(1);
      this.func_77637_a(PixelmonCreativeTabs.badges);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         openBadgeCase(stack, (EntityPlayerMP)player);
      }

      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }

   public String getTooltipText() {
      return I18n.func_74838_a("item.badgecase.tooltip");
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.CHEST;
   }

   public String getEquippableModelKey() {
      return "badgecase";
   }

   public ResourceLocation getEquippableTexture() {
      return null;
   }

   public Item getEquippableItem() {
      return this;
   }

   public static void openBadgeCase(ItemStack stack, EntityPlayerMP player) {
      if (stack.func_77973_b() instanceof ItemBadgeCase) {
         BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
         if (badgeCase == null) {
            Pixelmon.network.sendTo(new OpenBadgeCasePacket("", true, false, ((ItemBadgeCase)stack.func_77973_b()).color, Collections.emptyList(), Collections.emptyList()), player);
         } else {
            Pixelmon.network.sendTo(new OpenBadgeCasePacket(badgeCase.owner, badgeCase.isOwner(player), false, badgeCase.color, badgeCase.badges, Collections.emptyList()), player);
         }
      }

   }

   public static boolean registerBadgeCase(ItemStack stack, EntityPlayer player) {
      if (stack.func_77973_b() instanceof ItemBadgeCase) {
         BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
         if (badgeCase == null) {
            badgeCase = new BadgeCase(player.func_110124_au(), player.func_70005_c_(), ((ItemBadgeCase)stack.func_77973_b()).color, Lists.newArrayList());
            badgeCase.writeToStack(stack);
            return true;
         }

         if (badgeCase.uuid == null) {
            badgeCase.uuid = player.func_110124_au();
            badgeCase.owner = player.func_70005_c_();
            badgeCase.color = ((ItemBadgeCase)stack.func_77973_b()).color;
            badgeCase.writeToStack(stack);
            return true;
         }
      }

      return false;
   }

   @Nonnull
   public static ItemStack removeBadge(ItemStack stack, EntityPlayer player, int index) {
      if (stack.func_77973_b() instanceof ItemBadgeCase) {
         BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
         if (badgeCase != null && badgeCase.isOwner(player) && index >= 0 && index < badgeCase.badges.size()) {
            ItemStack removed = (ItemStack)badgeCase.badges.remove(index);
            badgeCase.writeToStack(stack);
            return removed != null ? removed : ItemStack.field_190927_a;
         }
      }

      return ItemStack.field_190927_a;
   }

   public static void swampBadge(ItemStack stack, EntityPlayer player, int index1, int index2) {
      if (stack.func_77973_b() instanceof ItemBadgeCase) {
         BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
         if (badgeCase != null && badgeCase.isOwner(player) && index1 >= 0 && index2 >= 0 && index1 < badgeCase.badges.size() && index2 < badgeCase.badges.size()) {
            ItemStack stack1 = (ItemStack)badgeCase.badges.get(index1);
            ItemStack stack2 = (ItemStack)badgeCase.badges.get(index2);
            badgeCase.badges.set(index1, stack2);
            badgeCase.badges.set(index2, stack1);
            badgeCase.writeToStack(stack);
         }
      }

   }

   public static boolean addBadge(ItemStack stack, EntityPlayer player, ItemStack badge) {
      if (stack.func_77973_b() instanceof ItemBadgeCase) {
         BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
         if (badgeCase != null && badgeCase.isOwner(player) && badgeCase.badges.size() < 64) {
            badgeCase.badges.add(badge);
            badgeCase.writeToStack(stack);
            return true;
         }
      }

      return false;
   }

   @Nullable
   public static ItemStack findFirstRegisteredBadgeCase(EntityPlayer player) {
      Iterator var1 = player.func_184214_aD().iterator();

      ItemStack stack;
      BadgeCase badgeCase;
      while(var1.hasNext()) {
         stack = (ItemStack)var1.next();
         if (stack.func_77973_b() instanceof ItemBadgeCase) {
            badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
            if (badgeCase != null && badgeCase.isOwner(player)) {
               return stack;
            }
         }
      }

      var1 = player.field_71071_by.field_70462_a.iterator();

      while(var1.hasNext()) {
         stack = (ItemStack)var1.next();
         if (stack.func_77973_b() instanceof ItemBadgeCase) {
            badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
            if (badgeCase != null && badgeCase.isOwner(player)) {
               return stack;
            }
         }
      }

      return null;
   }

   public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
      return (!stack.func_77942_o() || !stack.func_77978_p().func_74767_n("Unequippable")) && this.getEquipmentSlot(stack) == armorType;
   }

   public static class BadgeCase {
      UUID uuid;
      String owner;
      EnumBadgeCase color;
      public List badges;

      public BadgeCase(UUID uuid, String owner, EnumBadgeCase color, List badges) {
         this.uuid = uuid;
         this.owner = owner;
         this.color = color;
         this.badges = badges;
      }

      public boolean isOwner(EntityPlayer player) {
         return player.func_110124_au().equals(this.uuid);
      }

      public void writeToStack(ItemStack stack) {
         NBTTagCompound compound = stack.func_77978_p();
         if (compound == null) {
            stack.func_77982_d(compound = new NBTTagCompound());
         }

         if (this.uuid != null) {
            compound.func_186854_a("owner", this.uuid);
         } else {
            compound.func_82580_o("owner");
         }

         if (this.owner != null) {
            compound.func_74778_a("owner", this.owner);
         } else {
            compound.func_82580_o("ownerMost");
            compound.func_82580_o("ownerLeast");
         }

         if (!this.badges.isEmpty()) {
            NBTTagList list = new NBTTagList();
            compound.func_74782_a("badge_list", list);
            Iterator var4 = this.badges.iterator();

            while(var4.hasNext()) {
               ItemStack badge = (ItemStack)var4.next();
               list.func_74742_a(badge.func_77955_b(new NBTTagCompound()));
            }
         } else {
            compound.func_82580_o("badge_list");
         }

      }

      public static BadgeCase readFromItemStack(ItemStack stack) {
         if (!(stack.func_77973_b() instanceof ItemBadgeCase)) {
            return null;
         } else {
            NBTTagCompound compound = stack.func_77978_p();
            if (compound == null) {
               return null;
            } else {
               EnumBadgeCase color = ((ItemBadgeCase)stack.func_77973_b()).color;
               List badges = Lists.newArrayList();
               UUID uuid;
               if (compound.func_74764_b("Owners-UUID")) {
                  uuid = UUID.fromString(compound.func_74779_i("Owners-UUID"));
                  compound.func_82580_o("Owners-UUID");
               } else if (compound.func_186855_b("owner")) {
                  uuid = compound.func_186857_a("owner");
               } else {
                  uuid = null;
               }

               String owner;
               if (compound.func_74764_b("Owners-Username")) {
                  owner = compound.func_74779_i("Owners-Username");
                  compound.func_82580_o("Owners-Username");
               } else if (compound.func_74764_b("owner")) {
                  owner = compound.func_74779_i("owner");
               } else {
                  owner = "";
               }

               if (compound.func_74764_b("Badges")) {
                  String str = compound.func_74779_i("Badges");
                  compound.func_82580_o("Badges");
                  if (!str.isEmpty()) {
                     int count = -1;
                     String[] var8 = str.split(",");
                     int var9 = var8.length;

                     for(int var10 = 0; var10 < var9; ++var10) {
                        String badge = var8[var10];
                        ItemStack bs = new ItemStack(Item.func_111206_d("pixelmon:" + badge));
                        if (!bs.func_190926_b()) {
                           NBTTagCompound var10000 = compound.func_74775_l("BadgeCompounds");
                           StringBuilder var10001 = (new StringBuilder()).append("badge");
                           ++count;
                           if (var10000.func_74764_b(var10001.append(count).toString())) {
                              bs.func_77982_d(compound.func_74775_l("BadgeCompounds").func_74775_l("badge" + count));
                           }

                           compound.func_82580_o("BadgeCompounds");
                           badges.add(bs);
                        }
                     }
                  }
               } else if (compound.func_74764_b("badge_list")) {
                  NBTTagList list = compound.func_150295_c("badge_list", 10);
                  Iterator var14 = list.iterator();

                  while(var14.hasNext()) {
                     NBTBase tag = (NBTBase)var14.next();
                     badges.add(new ItemStack((NBTTagCompound)tag));
                  }
               }

               return new BadgeCase(uuid, owner, color, badges);
            }
         }
      }
   }
}
