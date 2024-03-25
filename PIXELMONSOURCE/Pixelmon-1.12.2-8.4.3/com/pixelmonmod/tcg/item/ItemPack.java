package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.gui.GuiPackConfirm;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.PackSyncPacket;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPack extends Item {
   private static final String name = "pack";

   public ItemPack() {
      this.func_77637_a(TCG.tabPacks);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public String func_77653_i(ItemStack stack) {
      if (stack.func_77978_p() != null) {
         NBTTagCompound tag = stack.func_77978_p();
         int setID = tag.func_74762_e("SetID");
         if (setID > 0) {
            CardSet set = CardSetRegistry.get(setID);
            if (set != null) {
               return LanguageMapTCG.translateKey(set.getUnlocalizedName().toLowerCase()) + " " + I18n.func_74838_a("item.pack.name");
            }
         }
      }

      return super.func_77653_i(stack);
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world != null && world.field_72995_K) {
         Minecraft.func_71410_x().func_147108_a(new GuiPackConfirm());
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (entityIn != null && !worldIn.field_72995_K && entityIn instanceof EntityPlayerMP) {
         EntityPlayerMP p = (EntityPlayerMP)entityIn;
         if (stack.func_77978_p() == null) {
            stack.func_77982_d(new NBTTagCompound());
         }

         NBTTagCompound tag = stack.func_77978_p();
         if (!tag.func_74764_b("SetID") && !tag.func_74764_b("Weight")) {
            int setID = CardSetRegistry.getRandom().getID();
            tag.func_74768_a("SetID", setID);
            tag.func_74776_a("Weight", 0.5F);
            PacketHandler.net.sendTo(new PackSyncPacket(itemSlot, setID, 0.5F), p);
         }
      }

   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         Iterator var3 = CardSetRegistry.getAll().iterator();

         while(var3.hasNext()) {
            CardSet c = (CardSet)var3.next();
            if (c.hasPack()) {
               items.add(c.getItemStack(1));
            }
         }
      }

   }

   public static String getName() {
      return "pack";
   }
}
