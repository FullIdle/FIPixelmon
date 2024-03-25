package com.pixelmonmod.tcg.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityBattleSpectator extends TileEntityHasOwner {
   private static final String CONTROLLER_KEY = "Controller";
   private static final String X_KEY = "X";
   private static final String Y_KEY = "Y";
   private static final String Z_KEY = "Z";
   private static final String PLAYER_INDEX_KEY = "PlayerIndex";
   private BlockPos controllerPosition;
   private int playerIndex;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      int x = 0;
      int y = 0;
      int z = 0;
      if (nbt.func_74764_b("Controller")) {
         NBTTagCompound controllerNBT = nbt.func_74775_l("Controller");
         if (controllerNBT.func_74764_b("X")) {
            x = controllerNBT.func_74762_e("X");
         }

         if (controllerNBT.func_74764_b("Y")) {
            y = controllerNBT.func_74762_e("Y");
         }

         if (controllerNBT.func_74764_b("Z")) {
            z = controllerNBT.func_74762_e("Z");
         }
      }

      this.controllerPosition = new BlockPos(x, y, z);
      if (nbt.func_74764_b("PlayerIndex")) {
         this.playerIndex = nbt.func_74762_e("PlayerIndex");
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.controllerPosition != null) {
         NBTTagCompound controllerNBT = new NBTTagCompound();
         controllerNBT.func_74768_a("X", this.controllerPosition.func_177958_n());
         controllerNBT.func_74768_a("Y", this.controllerPosition.func_177956_o());
         controllerNBT.func_74768_a("Z", this.controllerPosition.func_177952_p());
         nbt.func_74782_a("Controller", controllerNBT);
      }

      nbt.func_74768_a("PlayerIndex", this.playerIndex);
      return nbt;
   }

   public TileEntityBattleController getBattleController() {
      if (this.controllerPosition == null) {
         return null;
      } else {
         TileEntity te = this.field_145850_b.func_175625_s(this.controllerPosition);
         return te instanceof TileEntityBattleController ? (TileEntityBattleController)te : null;
      }
   }

   public BlockPos getControllerPosition() {
      return this.controllerPosition;
   }

   public void setControllerPosition(BlockPos controllerPosition) {
      this.controllerPosition = controllerPosition;
   }

   public int getPlayerIndex() {
      return this.playerIndex;
   }

   public void setPlayerIndex(int playerIndex) {
      this.playerIndex = playerIndex;
   }
}
