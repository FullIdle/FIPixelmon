package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.ForceEndBattleEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Mist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleForceEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleItems;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBattleItem extends PixelmonItem {
   public EnumBattleItems type;
   private boolean infinite;

   public ItemBattleItem(EnumBattleItems type, String itemName) {
      super(itemName);
      this.type = type;
      this.infinite = type == EnumBattleItems.redFlute || type == EnumBattleItems.greenFlute || type == EnumBattleItems.blueFlute || type == EnumBattleItems.yellowFlute;
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
   }

   public boolean execute(PixelmonWrapper pxm) {
      switch (this.type) {
         case redFlute:
            pxm.bc.sendToAll("pixelmon.abilities.flute", pxm.getOwnerName(), this.getLocalizedName());
            if (pxm.hasStatus(StatusType.Infatuated)) {
               pxm.bc.sendToAll("pixelmon.status.infatuatedcureitem", pxm.getOwnerName(), pxm.getNickname());
               pxm.removeStatus(StatusType.Infatuated, false);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case greenFlute:
            pxm.bc.sendToAll("pixelmon.abilities.flute", pxm.getOwnerName(), this.getLocalizedName());
            if (pxm.getMaxHealth() > pxm.getHealth()) {
               pxm.bc.sendToAll("pixelmon.effect.restorehealth", pxm.getNickname());
               pxm.healEntityBy(20);
               pxm.update(EnumUpdateType.HP);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case yellowFlute:
            pxm.bc.sendToAll("pixelmon.abilities.flute", pxm.getOwnerName(), this.getLocalizedName());
            if (pxm.hasStatus(StatusType.Confusion)) {
               pxm.bc.sendToAll("pixelmon.status.confusioncureitem", pxm.getOwnerName(), pxm.getNickname());
               pxm.removeStatus(StatusType.Confusion, false);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case blueFlute:
            pxm.bc.sendToAll("pixelmon.abilities.flute", pxm.getOwnerName(), this.getLocalizedName());
            if (pxm.hasStatus(StatusType.Sleep)) {
               pxm.bc.sendToAll("pixelmon.status.wokeup", pxm.getNickname());
               pxm.removeStatus(StatusType.Sleep, false);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case direHit:
            if (pxm.getBattleStats().getCritStage() == 0) {
               pxm.getBattleStats().increaseCritStage(2, false);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case maxMushroom:
            pxm.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
            pxm.getBattleStats().modifyStat(1, (StatsType)StatsType.Defence);
            pxm.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
            pxm.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialDefence);
            pxm.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed);
            return true;
         case guardSpec:
            if (pxm.addTeamStatus(new Mist(), pxm)) {
               pxm.bc.sendToAll("pixelmon.effect.usemist", pxm.getNickname());
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         case fluffyTail:
            ForceEndBattleEvent event = new ForceEndBattleEvent(pxm.bc, EnumBattleForceEndCause.FLUFFY_TAIL);
            if (!Pixelmon.EVENT_BUS.post(event)) {
               pxm.bc.endBattle(EnumBattleEndCause.FORCE);
               return true;
            }

            pxm.bc.sendToAll("pixelmon.general.noeffect");
            return false;
         default:
            pxm.getBattleStats().modifyStat(2, (StatsType)this.type.effectType);
            return true;
      }
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      return this.execute(userWrapper) && super.useFromBag(userWrapper, targetWrapper) && !this.infinite;
   }

   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (hand == EnumHand.MAIN_HAND && this.type == EnumBattleItems.maxMushroom && side != null && side == EnumFacing.UP) {
         BlockPos up = pos.func_177984_a();
         if (world.func_175623_d(up)) {
            IBlockState lower = world.func_180495_p(pos);
            if (lower.func_177230_c() instanceof BlockMycelium) {
               if (!world.field_72995_K) {
                  if (!player.func_184812_l_()) {
                     player.func_184614_ca().func_190918_g(1);
                  }

                  world.func_175656_a(up, PixelmonBlocks.maxMushroomsBlock.func_176223_P());
                  world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187577_bU, SoundCategory.BLOCKS, 1.0F, 1.0F);
               }

               player.func_184609_a(EnumHand.MAIN_HAND);
               return EnumActionResult.SUCCESS;
            }
         }
      }

      return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
   }
}
