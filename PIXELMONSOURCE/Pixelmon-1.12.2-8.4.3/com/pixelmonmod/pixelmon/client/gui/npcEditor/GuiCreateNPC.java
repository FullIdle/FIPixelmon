package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.CreateNPCPacket;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

public class GuiCreateNPC extends GuiScreen {
   BlockPos pos;

   public GuiCreateNPC(BlockPos pos) {
      this.pos = pos;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(EnumNPCType.Trainer.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 - 80, 250, 20, I18n.func_135052_a("gui.chooseNPC.trainer", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.Trader.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 - 60, 250, 20, I18n.func_135052_a("gui.chooseNPC.trader", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.ChattingNPC.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 - 40, 126, 20, I18n.func_135052_a("gui.chooseNPC.chatting", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.QuestGiver.ordinal(), this.field_146294_l / 2 + 1, this.field_146295_m / 2 - 40, 125, 20, I18n.func_135052_a("gui.chooseNPC.quest", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.Relearner.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 - 20, 250, 20, I18n.func_135052_a("gui.chooseNPC.relearner", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.Tutor.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2, 126, 20, I18n.func_135052_a("gui.chooseNPC.tutor", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.TransferTutor.ordinal(), this.field_146294_l / 2 + 1, this.field_146295_m / 2, 125, 20, I18n.func_135052_a("gui.chooseNPC.transfer_tutor", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.NurseJoy.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 + 20, 126, 20, I18n.func_135052_a("gui.chooseNPC.nursejoy", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.Doctor.ordinal(), this.field_146294_l / 2 + 1, this.field_146295_m / 2 + 20, 125, 20, I18n.func_135052_a("gui.chooseNPC.doctor", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.Shopkeeper.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 + 40, 250, 20, I18n.func_135052_a("gui.chooseNPC.shopkeeper", new Object[0])));
      this.field_146292_n.add(new GuiButton(EnumNPCType.OldFisherman.ordinal(), this.field_146294_l / 2 - 125, this.field_146295_m / 2 + 60, 250, 20, I18n.func_135052_a("gui.chooseNPC.oldfisherman", new Object[0])));
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      Pixelmon.network.sendToServer(new CreateNPCPacket(EnumNPCType.getFromOrdinal((short)button.field_146127_k), this.pos, Minecraft.func_71410_x().field_71439_g.field_70759_as));
      this.field_146297_k.field_71439_g.func_71053_j();
   }
}
