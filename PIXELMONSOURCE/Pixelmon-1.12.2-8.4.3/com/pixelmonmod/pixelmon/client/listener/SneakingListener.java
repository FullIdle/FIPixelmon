package com.pixelmonmod.pixelmon.client.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.machines.BlockElevator;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ElevatorUsed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(
   value = {Side.CLIENT},
   modid = "pixelmon"
)
public class SneakingListener {
   @SubscribeEvent
   public static void onInput(InputEvent.KeyInputEvent event) {
      if (Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151468_f()) {
         IBlockState state = Minecraft.func_71410_x().field_71439_g.field_70170_p.func_180495_p(new BlockPos((double)((int)Math.floor(Minecraft.func_71410_x().field_71439_g.field_70165_t)), (double)((int)Math.floor(Minecraft.func_71410_x().field_71439_g.field_70163_u) - 1), Math.floor(Minecraft.func_71410_x().field_71439_g.field_70161_v)));
         if (state.func_177230_c() instanceof BlockElevator) {
            Pixelmon.network.sendToServer(new ElevatorUsed());
         }
      }

   }
}
