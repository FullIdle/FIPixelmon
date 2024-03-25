package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.CardBackSyncPacket;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemCardBack extends Item {
   private static final String name = "card_back";

   public ItemCardBack() {
      this.func_77637_a(TCG.tabTCGCosmetic);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public void func_77624_a(ItemStack itemStack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
      if (itemStack.func_77978_p() != null) {
         CardBack c = CardBackRegistry.get(itemStack.func_77978_p().func_74779_i("CardBackID"));
         if (c != null && itemStack.func_179543_a("Lore") == null) {
            tooltip.clear();
            tooltip.add(itemStack.func_82833_r());
            tooltip.add(c.getName());
            if (c.getOwnerName() != null) {
               tooltip.add("Artist: " + c.getOwnerName());
            }
         }
      }

   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (entityIn != null && !worldIn.field_72995_K && entityIn instanceof EntityPlayerMP) {
         EntityPlayerMP p = (EntityPlayerMP)entityIn;
         if (stack.func_77978_p() == null) {
            stack.func_77982_d(new NBTTagCompound());
         }

         NBTTagCompound tag = stack.func_77978_p();
         if (!tag.func_74764_b("CardBackID")) {
            CardBack c = CardBackRegistry.getRandomCardBack();
            tag.func_74778_a("CardBackID", c.getName());
            PacketHandler.net.sendTo(new CardBackSyncPacket(itemSlot, c.getName()), p);
         }
      }

   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         Iterator var3 = CardBackRegistry.getAll().iterator();

         while(var3.hasNext()) {
            CardBack c = (CardBack)var3.next();
            items.add(c.getItemStack(1));
         }
      }

   }

   public static String getName() {
      return "card_back";
   }
}
