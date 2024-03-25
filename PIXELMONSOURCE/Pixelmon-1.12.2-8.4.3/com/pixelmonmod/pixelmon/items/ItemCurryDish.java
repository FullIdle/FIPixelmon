package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCurryDish extends PixelmonItem {
   private static final MedicineStatus allStatuses;

   public ItemCurryDish(EnumCurryKey key) {
      super("dish_curry_" + key.name().toLowerCase());
      this.func_77637_a(PixelmonCreativeTabs.restoration);
   }

   public String func_77653_i(ItemStack stack) {
      EnumBerryFlavor flavor = EnumBerryFlavor.NONE;
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("BerryFlavor")) {
         flavor = EnumBerryFlavor.values()[stack.func_77978_p().func_74771_c("BerryFlavor")];
      }

      return I18n.func_74837_a(this.func_77657_g(stack) + ".name", new Object[]{I18n.func_74838_a("berry.flavor." + flavor.name().toLowerCase() + ".name")}).trim();
   }

   public boolean useCurry(ItemStack stack, PokemonLink pokemon) {
      EnumCurryRating rating = EnumCurryRating.KOFFING;
      if (stack.func_77942_o() && stack.func_77978_p().func_74765_d("CurryQuality") != 0) {
         rating = EnumCurryRating.values()[stack.func_77978_p().func_74771_c("CurryQuality")];
      }

      int currentHealth = pokemon.getHealth();
      int maxHealth = pokemon.getMaxHealth();
      if (currentHealth < maxHealth) {
         pokemon.setHealth((int)Math.min((double)maxHealth, (double)currentHealth + (double)maxHealth * rating.hpHeal));
         pokemon.update(EnumUpdateType.HP);
      }

      pokemon.adjustFriendship(rating.happinessBoost);
      pokemon.getPokemon().getLevelContainer().awardEXP(rating.expBoost, rating.getGainType());
      if (rating.ppRestore) {
         for(int i = 0; i < pokemon.getMoveset().size(); ++i) {
            pokemon.getMoveset().get(i).pp = pokemon.getMoveset().get(i).getMaxPP();
         }
      }

      if (rating.statusCure) {
         ((ItemMedicine)PixelmonItems.fullHeal).useMedicine(pokemon, 0);
      }

      pokemon.sendMessage("item.curry.used", stack.func_82833_r(), pokemon.getNickname());
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      NBTTagCompound nbt = stack.func_77978_p();
      short rating = 0;
      if (nbt != null && nbt.func_74764_b("CurryQuality")) {
         rating = nbt.func_74765_d("CurryQuality");
      }

      TextFormatting color = TextFormatting.DARK_PURPLE;
      switch (rating) {
         case 1:
            color = TextFormatting.BLUE;
            break;
         case 2:
            color = TextFormatting.DARK_RED;
            break;
         case 3:
            color = TextFormatting.GRAY;
            break;
         case 4:
            color = TextFormatting.GOLD;
      }

      tooltip.add(color + I18n.func_74837_a("item.curry.tooltip", new Object[]{EnumCurryRating.values()[rating].getLocalizedName()}));
      super.func_77624_a(stack, world, tooltip, advanced);
   }

   static {
      allStatuses = new MedicineStatus(new StatusType[]{StatusType.Burn, StatusType.Confusion, StatusType.Freeze, StatusType.Paralysis, StatusType.Poison, StatusType.PoisonBadly, StatusType.Sleep, StatusType.Infatuated});
   }
}
