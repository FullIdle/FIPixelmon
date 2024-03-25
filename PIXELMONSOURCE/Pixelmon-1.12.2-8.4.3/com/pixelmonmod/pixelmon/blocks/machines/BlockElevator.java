package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ElevatorEvent;
import com.pixelmonmod.pixelmon.blocks.GenericBlock;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockElevator extends GenericBlock {
   public BlockElevator() {
      super(Material.field_151573_f);
      this.func_149711_c(0.8F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      return false;
   }

   public void takeElevator(World world, BlockPos pos, EntityPlayerMP player, boolean upwards) {
      BlockPos elevatorPos = this.findNearestElevator(world, pos, upwards);
      if (elevatorPos != null) {
         ElevatorEvent elevatorEvent = new ElevatorEvent(player, this, upwards, new BlockPos(player.field_70165_t, (double)elevatorPos.func_177956_o(), player.field_70161_v));
         if (!Pixelmon.EVENT_BUS.post(elevatorEvent)) {
            BlockPos destination = elevatorEvent.getDestination();
            player.func_70634_a(player.field_70165_t, (double)destination.func_177956_o() + 0.5, player.field_70161_v);
         }
      }

   }

   private BlockPos findNearestElevator(World world, BlockPos pos, boolean upwards) {
      int searchRangeY = PixelmonConfig.elevatorSearchRange;
      if (!upwards) {
         searchRangeY *= -1;
      }

      int posY = pos.func_177956_o();
      BlockPos.MutableBlockPos newPos = new BlockPos.MutableBlockPos();
      int y = posY;

      while(true) {
         if (upwards) {
            if (y >= posY + searchRangeY) {
               break;
            }
         } else if (y <= posY + searchRangeY) {
            break;
         }

         newPos.func_181079_c(pos.func_177958_n(), y, pos.func_177952_p());
         if (!newPos.equals(pos) && !newPos.func_177984_a().equals(pos) && world.func_180495_p(newPos).func_177230_c() == this) {
            return newPos.func_177984_a();
         }

         y += upwards ? 1 : -1;
      }

      return null;
   }
}
