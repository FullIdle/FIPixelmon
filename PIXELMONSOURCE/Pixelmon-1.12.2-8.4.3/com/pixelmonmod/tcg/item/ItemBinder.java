package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.OpenBinderPacket;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBinder extends Item {
   private static final String name = "binder";

   public ItemBinder() {
      this.func_77637_a(TCG.tabTCG);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public void func_77624_a(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
   }

   public int func_77626_a(ItemStack par1ItemStack) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world.field_72995_K && !player.func_70093_af()) {
         PacketHandler.net.sendToServer(new OpenBinderPacket());
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (stack.func_77978_p() == null) {
         NBTTagCompound nbt = new NBTTagCompound();
         stack.func_77982_d(nbt);
      }

   }

   public static String getName() {
      return "binder";
   }
}
