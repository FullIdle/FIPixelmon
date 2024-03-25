package com.pixelmonmod.tcg.api.card;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.item.containers.InventoryDeck;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ThemeDeck {
   private int id;
   private String name;
   private final List cards = new ArrayList();
   private final List extraCards = new ArrayList();

   public int getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public List getCards() {
      return this.cards;
   }

   public List getExtraCards() {
      return this.extraCards;
   }

   public String getLocalizedName() {
      return LanguageMapTCG.translateKey("deck." + this.getName().toLowerCase() + ".name");
   }

   public ItemStack getItemStack(int count) {
      ItemStack itemDeck = new ItemStack(TCG.itemDeck, 1);
      NBTTagCompound tagcompound = new NBTTagCompound();
      NBTTagList slotList = new NBTTagList();

      for(int i = 0; i < Math.min(this.getCards().size(), 60); ++i) {
         ItemStack itemCard = ((ImmutableCard)this.getCards().get(i)).getItemStack(1);
         NBTTagCompound slotTag = new NBTTagCompound();
         slotTag.func_74774_a("Slot", (byte)i);
         itemCard.func_77955_b(slotTag);
         slotList.func_74742_a(slotTag);
      }

      tagcompound.func_74782_a(InventoryDeck.INVENTORY_TAG_NAME, slotList);
      tagcompound.func_74782_a("display", new NBTTagCompound());
      tagcompound.func_74775_l("display").func_74778_a("Name", this.getLocalizedName());
      itemDeck.func_190920_e(count);
      itemDeck.func_77982_d(tagcompound);
      return itemDeck;
   }

   public ItemStack getItemStack() {
      return this.getItemStack(1);
   }
}
