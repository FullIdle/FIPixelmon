package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.gui.GuiCard;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.CardSyncPacket;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCard extends Item {
   private static final String name = "card";

   public ItemCard() {
      this.func_77637_a(TCG.tabCards);
      this.func_77655_b(getName());
      this.setRegistryName("tcg", getName());
   }

   public Item func_77625_d(int stackSize) {
      this.field_77777_bU = stackSize;
      return this;
   }

   public String func_77653_i(ItemStack itemStack) {
      if (itemStack.func_77978_p() != null) {
         ImmutableCard c = CardRegistry.fromNBT(itemStack.func_77978_p());
         if (c != null) {
            if (c.getCardType() != null) {
               return LanguageMapTCG.translateKey(c.getName()) + " (" + toTitleCase(c.getCardType().getUnlocalizedString().toLowerCase()) + ")";
            }

            TCG.logger.warn("Card: " + c.getName() + " has an invalid card type! Please fix the json!");
         }
      }

      return super.func_77653_i(itemStack);
   }

   public static String toTitleCase(String s) {
      return s.substring(0, 1).toUpperCase() + s.substring(1);
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world.field_72995_K && player.func_184586_b(hand).func_77978_p() != null) {
         Minecraft.func_71410_x().func_147108_a(new GuiCard(CardRegistry.fromNBT(player.func_184586_b(hand).func_77978_p())));
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public int getItemStackLimit(ItemStack stack) {
      if (stack.func_77978_p() != null) {
         NBTTagCompound tag = stack.func_77978_p();
         ImmutableCard card = CardRegistry.fromId(tag.func_74762_e(ImmutableCard.CARD_ID_NBT_KEY));
         if (card != null) {
            if (card.getCardType() == CardType.ENERGY && !card.isSpecial()) {
               return 64;
            }

            if (card.getCardType() != CardType.ASPEC) {
               return 4;
            }
         }
      }

      return 1;
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (entityIn != null && !worldIn.field_72995_K && entityIn instanceof EntityPlayerMP) {
         EntityPlayerMP p = (EntityPlayerMP)entityIn;
         if (stack.func_77978_p() == null) {
            stack.func_77982_d(new NBTTagCompound());
         }

         NBTTagCompound tag = stack.func_77978_p();
         if (!tag.func_74764_b(ImmutableCard.CARD_ID_NBT_KEY)) {
            ImmutableCard c = CardRegistry.getRandomCard();
            if (c != null) {
               tag.func_74768_a(ImmutableCard.CARD_ID_NBT_KEY, c.getID());
               PacketHandler.net.sendTo(new CardSyncPacket(itemSlot, c.getID(), false), p);
            }
         }
      }

   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         Iterator var3 = CardRegistry.getAll().iterator();

         while(var3.hasNext()) {
            ImmutableCard c = (ImmutableCard)var3.next();
            items.add(c.getItemStack(1));
         }
      }

   }

   public static String getName() {
      return "card";
   }
}
