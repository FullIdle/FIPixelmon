package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class ItemPixelmonSprite extends PixelmonItem implements IEquippable {
   public ItemPixelmonSprite() {
      super("pixelmon_sprite");
      this.func_77637_a((CreativeTabs)null);
      this.func_77625_d(1);
   }

   public String func_77653_i(ItemStack stack) {
      if (!stack.func_77942_o()) {
         return super.func_77653_i(stack);
      } else if (stack.func_77978_p().func_74764_b("Nickname")) {
         return stack.func_77978_p().func_74779_i("Nickname") + " " + this.getLocalizedName();
      } else if (!stack.func_77978_p().func_74767_n("isEgg") && stack.func_77978_p().func_74762_e("eggCycles") <= 0) {
         if (!stack.func_77978_p().func_74764_b("ndex")) {
            return super.func_77653_i(stack);
         } else {
            NBTTagCompound compound = stack.func_77978_p();
            EnumSpecies species = EnumSpecies.getFromDex(compound.func_74765_d("ndex"));
            IEnumForm form = species.getFormEnum(compound.func_74771_c("form"));
            boolean shiny = compound.func_74767_n("Shiny");
            String name = species.getLocalizedName();
            if ((form instanceof EnumMega || form instanceof EnumPrimal || form instanceof RegionalForms) && !form.isDefaultForm()) {
               name = form.getLocalizedName() + " " + name;
            }

            if (shiny) {
               name = I18n.func_74838_a("gui.trainereditor.shiny") + " " + name;
            }

            return name + " " + this.getLocalizedName();
         }
      } else {
         return I18n.func_74838_a("pixelmon.egg.name");
      }
   }

   public static ItemStack getPhoto(Pokemon pokemon) {
      ItemStack itemStack = new ItemStack(PixelmonItems.itemPixelmonSprite);
      NBTTagCompound tagCompound = new NBTTagCompound();
      itemStack.func_77982_d(tagCompound);
      tagCompound.func_74777_a("ndex", (short)pokemon.getSpecies().getNationalPokedexInteger());
      tagCompound.func_74774_a("form", (byte)pokemon.getForm());
      tagCompound.func_74774_a("gender", pokemon.getGender().getForm());
      tagCompound.func_74757_a("Shiny", pokemon.isShiny());
      if (pokemon.isEgg()) {
         tagCompound.func_74768_a("eggCycles", pokemon.getEggCycles());
      }

      if (!pokemon.getCustomTexture().isEmpty()) {
         tagCompound.func_74778_a("CustomTexture", pokemon.getCustomTexture());
      }

      if (pokemon.getNickname() != null && !pokemon.getNickname().isEmpty()) {
         tagCompound.func_74778_a("Nickname", pokemon.getNickname());
      }

      return itemStack;
   }

   public static ItemStack getPhoto(EntityPixelmon pixelmon) {
      return getPhoto(pixelmon.getPokemonData());
   }

   public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
      return EntityEquipmentSlot.HEAD;
   }

   public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
      return armorType == EntityEquipmentSlot.HEAD && stack.func_77942_o() && !stack.func_77978_p().func_74767_n("Unequippable") && stack.func_77978_p().func_74765_d("ndex") == EnumSpecies.Joltik.getNationalPokedexInteger();
   }

   public String getEquippableModelKey() {
      return "pixelmon_sprite";
   }

   public ResourceLocation getEquippableTexture() {
      return null;
   }

   public Item getEquippableItem() {
      return this;
   }
}
