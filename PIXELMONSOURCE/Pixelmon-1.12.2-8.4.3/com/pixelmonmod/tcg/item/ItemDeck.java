package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ThemeDeck;
import com.pixelmonmod.tcg.api.registries.ThemeDeckRegistry;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.GenericGUIPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDeck extends Item {
   private static final String name = "deck";

   public ItemDeck() {
      this.func_77637_a(TCG.tabDecks);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public String func_77653_i(ItemStack stack) {
      String name = super.func_77653_i(stack);
      if (stack.func_77978_p() != null && stack.func_77978_p().func_74764_b("deck")) {
         ThemeDeck deck = ThemeDeckRegistry.get(stack.func_77978_p().func_74779_i("deck"));
         if (deck != null) {
            name = LanguageMapTCG.translateKey("deck." + deck.getName().toLowerCase() + ".name");
         }

         if (stack.func_77978_p().func_74767_n("locked")) {
            name = name + " (" + I18n.func_74838_a("item.deck.locked") + ")";
         }
      }

      return name;
   }

   public void func_77624_a(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
      List cards = LogicHelper.getCards(stack);
      tooltip.add("Cards: " + cards.size());
   }

   public int func_77626_a(ItemStack par1ItemStack) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world.field_72995_K && !player.func_70093_af()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71476_x != null) {
            try {
               TileEntity te = mc.field_71441_e.func_175625_s(mc.field_71476_x.func_178782_a());
               if (te != null && te instanceof TileEntityBattleController) {
                  return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
               }
            } catch (Exception var6) {
            }
         }

         if (player.func_184614_ca().func_77973_b() instanceof ItemDeck) {
            PacketHandler.net.sendToServer(new GenericGUIPacket(GenericGUIPacket.GUITypes.Deck, true, (BlockPos)null));
         }
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (stack.func_77978_p() == null) {
         NBTTagCompound nbt = new NBTTagCompound();
         stack.func_77982_d(nbt);
      }

   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         items.add(new ItemStack(TCG.itemDeck));
         Iterator var3 = ThemeDeckRegistry.getAll().iterator();

         while(var3.hasNext()) {
            ThemeDeck td = (ThemeDeck)var3.next();
            items.add(td.getItemStack());
         }
      }

   }

   public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
      PacketHandler.net.sendTo(new GenericGUIPacket(GenericGUIPacket.GUITypes.Deck, false, (BlockPos)null), (EntityPlayerMP)player);
      return true;
   }

   public static String getName() {
      return "deck";
   }
}
