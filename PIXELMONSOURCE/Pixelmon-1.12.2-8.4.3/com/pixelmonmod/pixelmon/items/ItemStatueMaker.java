package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.StatueEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor.StatuePacketClient;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemStatueMaker extends PixelmonItem {
   public ItemStatueMaker() {
      super("chisel");
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
   }

   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (side != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else if (!world.field_72995_K && player instanceof EntityPlayerMP) {
         if (!checkPermission((EntityPlayerMP)player)) {
            return EnumActionResult.FAIL;
         } else {
            EntityStatue statue = new EntityStatue(world);
            EnumSpecies pokemon = player.func_70093_af() && player.func_184812_l_() ? EnumSpecies.randomPoke() : EnumSpecies.Bulbasaur;
            statue.setPokemon(new PokemonBase(pokemon));
            statue.func_70107_b((double)((float)pos.func_177958_n() + hitX), (double)(pos.func_177956_o() + 1), (double)((float)pos.func_177952_p() + hitZ));
            StatueEvent.CreateStatue createStatueEvent = new StatueEvent.CreateStatue((EntityPlayerMP)player, (WorldServer)world, pos, statue);
            if (Pixelmon.EVENT_BUS.post(createStatueEvent)) {
               return EnumActionResult.FAIL;
            } else {
               statue = createStatueEvent.getStatue();
               world.func_72838_d(statue);
               statue.func_70107_b((double)((float)pos.func_177958_n() + hitX), (double)(pos.func_177956_o() + 1), (double)((float)pos.func_177952_p() + hitZ));
               statue.setRotation(player.field_70177_z + 180.0F);
               Pixelmon.network.sendTo(new StatuePacketClient(statue.func_110124_au()), (EntityPlayerMP)player);
               OpenScreen.open(player, EnumGuiScreen.StatueEditor, statue.func_145782_y());
               return EnumActionResult.SUCCESS;
            }
         }
      } else {
         return EnumActionResult.PASS;
      }
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (entity instanceof EntityStatue && !player.field_70170_p.field_72995_K && !Pixelmon.EVENT_BUS.post(new StatueEvent.DestroyStatue((EntityPlayerMP)player, (EntityStatue)entity))) {
         entity.func_70106_y();
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public static boolean checkPermission(EntityPlayerMP editingPlayer) {
      if (PixelmonConfig.allowChisels && editingPlayer.func_70003_b(4, "pixelmon.chisel.use")) {
         return true;
      } else {
         ChatHandler.sendChat(editingPlayer, "gui.chisel.notallowedserver");
         return false;
      }
   }
}
