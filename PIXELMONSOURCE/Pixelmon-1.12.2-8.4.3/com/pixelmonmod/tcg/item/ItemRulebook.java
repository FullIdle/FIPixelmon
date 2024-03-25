package com.pixelmonmod.tcg.item;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.gui.GuiBattleSpectator;
import com.pixelmonmod.tcg.gui.GuiTCGRuleEditor;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleRule;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRulebook extends Item {
   public ItemRulebook() {
      this.func_77655_b(getName());
      this.func_77637_a(TCG.tabTCG);
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!player.func_70093_af() && world.field_72995_K) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71476_x != null && mc.field_71476_x.func_178782_a() != null) {
            TileEntity te = mc.field_71441_e.func_175625_s(mc.field_71476_x.func_178782_a());
            if (mc.field_71441_e.func_180495_p(mc.field_71476_x.func_178782_a()) != null) {
               IBlockState blockState = mc.field_71441_e.func_180495_p(mc.field_71476_x.func_178782_a());
               if (blockState.func_177230_c() instanceof MultiBlock) {
                  MultiBlock mb = (MultiBlock)blockState.func_177230_c();
                  te = mc.field_71441_e.func_175625_s(mb.findBaseBlock(mc.field_71441_e, new BlockPos.MutableBlockPos(mc.field_71476_x.func_178782_a()), blockState));
               }
            }

            if (!(te instanceof TileEntityBattleRule)) {
               if (te instanceof TileEntityBattleSpectator) {
                  Minecraft.func_71410_x().func_147108_a(new GuiBattleSpectator((TileEntityBattleSpectator)te));
               } else {
                  Minecraft.func_71410_x().func_147108_a(new GuiTCGRuleEditor(player, player.func_184586_b(hand)));
               }
            }
         }
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public static String getName() {
      return "rulebook";
   }
}
