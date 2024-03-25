package com.pixelmonmod.pixelmon.client.gui.ranchblock;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.EnumRanchServerPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.RanchBlockServerPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiExtendRanch extends GuiScreen {
   public static boolean[] extendDirections;
   private TileEntityRanchBlock ranch;

   public GuiExtendRanch(TileEntityRanchBlock ranch) {
      Preconditions.checkArgument(ranch != null, "The provided ranch cannot be null");
      this.ranch = ranch;
   }

   public GuiExtendRanch(World world, int x, int y, int z) {
      this((TileEntityRanchBlock)world.func_175625_s(new BlockPos(x, y, z)));
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (extendDirections[0]) {
         this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 10, 40, 20, I18n.func_135052_a("gui.ranch.extendplusz", new Object[0])));
      }

      if (extendDirections[1]) {
         this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 20, this.field_146295_m / 2 - 50, 40, 20, I18n.func_135052_a("gui.ranch.extendminusx", new Object[0])));
      }

      if (extendDirections[2]) {
         this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 + 20, this.field_146295_m / 2 - 10, 40, 20, I18n.func_135052_a("gui.ranch.extendminusz", new Object[0])));
      }

      if (extendDirections[3]) {
         this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 60, this.field_146295_m / 2 - 50, 40, 20, I18n.func_135052_a("gui.ranch.extendplusx", new Object[0])));
      }

      this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 30, 60, 20, I18n.func_135052_a("gui.cancel.text", new Object[0])));
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      String s = I18n.func_135052_a("gui.ranch.extendTitle", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 2 - 80, 16777215);
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_146284_a(GuiButton button) {
      if (button.field_146127_k == 0) {
         Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), EnumRanchServerPacketMode.ExtendRanch, new boolean[]{true, false, false, false}));
      } else if (button.field_146127_k == 1) {
         Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), EnumRanchServerPacketMode.ExtendRanch, new boolean[]{false, true, false, false}));
      } else if (button.field_146127_k == 2) {
         Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), EnumRanchServerPacketMode.ExtendRanch, new boolean[]{false, false, true, false}));
      } else if (button.field_146127_k == 3) {
         Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), EnumRanchServerPacketMode.ExtendRanch, new boolean[]{false, false, false, true}));
      }

      this.field_146297_k.field_71439_g.func_71053_j();
   }

   public boolean func_73868_f() {
      return false;
   }
}
