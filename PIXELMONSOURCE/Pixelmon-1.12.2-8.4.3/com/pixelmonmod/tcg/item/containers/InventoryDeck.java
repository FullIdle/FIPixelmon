package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ThemeDeck;
import com.pixelmonmod.tcg.api.registries.ThemeDeckRegistry;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.item.ItemCard;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class InventoryDeck extends InventoryBasic {
   private String name = "Deck";
   public static String INVENTORY_TAG_NAME = "DeckInventory";
   private final EntityPlayer player;
   private final ItemStack item;
   public static final int size = 62;
   private NonNullList inventory;
   public boolean isLocked = false;

   public InventoryDeck(ItemStack item, EntityPlayer player) {
      super("container.deck", false, 62);
      this.item = item;
      this.player = player;
      this.inventory = NonNullList.func_191197_a(62, ItemStack.field_190927_a);
      NBTTagCompound nbt = item.func_77978_p();
      if (nbt == null) {
         nbt = new NBTTagCompound();
         item.func_77982_d(nbt);
      }

      String deckName = nbt.func_74779_i("deck");
      ThemeDeck deck = ThemeDeckRegistry.get(deckName);
      if (deck != null) {
         String name = LanguageMapTCG.translateKey("deck." + deck.getName().toLowerCase() + ".name");
         if (item.func_77978_p().func_74767_n("locked")) {
            name = name + " (" + I18n.func_74838_a("item.deck.locked") + ")";
         }

         item.func_151001_c(name);
         int i = 0;
         Iterator var8 = deck.getCards().iterator();

         ImmutableCard card;
         while(var8.hasNext()) {
            card = (ImmutableCard)var8.next();
            this.func_70299_a(i, card.getItemStack(1));
            ++i;
            if (i > 60) {
               break;
            }
         }

         if (!player.field_70170_p.field_72995_K) {
            var8 = deck.getExtraCards().iterator();

            while(var8.hasNext()) {
               card = (ImmutableCard)var8.next();
               player.func_146105_b(new TextComponentString(TextFormatting.GOLD + "You got an extra " + LanguageMapTCG.translateKey(card.getName().toLowerCase()) + " card!"), false);
               ItemStack stack = card.getItemStack(1);
               if (!player.field_71071_by.func_70441_a(stack)) {
                  player.func_146097_a(stack, false, true);
               }
            }
         }

         nbt.func_82580_o("deck");
         item.func_77982_d(nbt);
         this.writeInventoryToNBT(item.func_77978_p());
      }

      this.isLocked = nbt.func_74767_n("locked");
      this.readInventoryFromNBT(item.func_77978_p());
   }

   public String func_70005_c_() {
      return this.name;
   }

   public void writeInventoryToNBT(NBTTagCompound nbtDeck) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.func_70302_i_(); ++i) {
         if (this.func_70301_a(i) != ItemStack.field_190927_a) {
            NBTTagCompound nbtSlot = new NBTTagCompound();
            nbtSlot.func_74774_a("Slot", (byte)i);
            this.func_70301_a(i).func_77955_b(nbtSlot);
            nbttaglist.func_74742_a(nbtSlot);
         }
      }

      nbtDeck.func_74782_a(INVENTORY_TAG_NAME, nbttaglist);
   }

   public void readInventoryFromNBT(NBTTagCompound nbtDeck) {
      NBTTagList items = nbtDeck.func_150295_c(INVENTORY_TAG_NAME, nbtDeck.func_74732_a());

      for(int i = 0; i < items.func_74745_c(); ++i) {
         NBTTagCompound item = items.func_150305_b(i);
         byte slot = item.func_74771_c("Slot");
         if (slot >= 0 && slot < this.func_70302_i_()) {
            this.func_70299_a(slot, new ItemStack(item));
         }
      }

   }

   public boolean func_145818_k_() {
      return this.name.length() > 0;
   }

   public ITextComponent func_145748_c_() {
      return new TextComponentString(this.name);
   }

   public int func_70302_i_() {
      return this.inventory.size();
   }

   public ItemStack func_70301_a(int index) {
      return (ItemStack)this.inventory.get(index);
   }

   public ItemStack func_70298_a(int index, int count) {
      if (this.inventory.get(index) != ItemStack.field_190927_a) {
         ItemStack itemstack;
         if (((ItemStack)this.inventory.get(index)).func_190916_E() <= count) {
            itemstack = (ItemStack)this.inventory.get(index);
            this.inventory.set(index, ItemStack.field_190927_a);
            return itemstack;
         } else {
            itemstack = ((ItemStack)this.inventory.get(index)).func_77979_a(count);
            if (((ItemStack)this.inventory.get(index)).func_190916_E() == 0) {
               this.inventory.set(index, ItemStack.field_190927_a);
            }

            return itemstack;
         }
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int index) {
      ((ItemStack)this.inventory.get(index)).func_190920_e(((ItemStack)this.inventory.get(index)).func_190916_E() - 1);
      return (ItemStack)this.inventory.get(index);
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.inventory.set(index, stack);
      if (stack != ItemStack.field_190927_a && stack.func_190916_E() > this.func_70297_j_()) {
         stack.func_190920_e(this.func_70297_j_());
      }

      this.func_70296_d();
   }

   public int func_70297_j_() {
      return 1;
   }

   public void func_70296_d() {
      super.func_70296_d();
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
      this.writeInventoryToNBT(this.item.func_77978_p());
   }

   public boolean func_94041_b(int index, ItemStack stack) {
      return stack.func_77973_b() instanceof ItemCard;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }
}
