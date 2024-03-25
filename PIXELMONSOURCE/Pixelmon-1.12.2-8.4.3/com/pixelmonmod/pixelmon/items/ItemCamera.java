package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiCameraOverlay;
import com.pixelmonmod.pixelmon.client.keybindings.TargetKeyBinding;
import com.pixelmonmod.pixelmon.comm.packetHandlers.camera.ItemCameraPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCamera extends GenericItem {
   public ItemCamera() {
      super("camera");
      this.func_77625_d(1);
      this.func_77637_a(CreativeTabs.field_78026_f);
   }

   @SideOnly(Side.CLIENT)
   public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
      if (entityLiving.field_70170_p.field_72995_K) {
         if (!GuiCameraOverlay.isCameraGuiOn) {
            return false;
         }

         RayTraceResult objectPosition = TargetKeyBinding.getTarget(false, 20.0);
         if (objectPosition != null && objectPosition.field_72313_a == Type.ENTITY) {
            if (!(objectPosition.field_72308_g instanceof EntityPixelmon)) {
               return false;
            }

            EntityPixelmon pixelmon = (EntityPixelmon)objectPosition.field_72308_g;
            Pixelmon.network.sendToServer(new ItemCameraPacket(pixelmon.func_145782_y()));
            GuiCameraOverlay.isCameraGuiOn = false;
         }
      }

      return false;
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
      if (worldIn.field_72995_K) {
         GuiCameraOverlay.isCameraGuiOn = !GuiCameraOverlay.isCameraGuiOn;
      }

      return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
