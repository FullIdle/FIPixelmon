package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ThrowPokeballEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityEmptyPokeball;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemPokeball extends PixelmonItem {
   public EnumPokeballs type;

   public ItemPokeball(EnumPokeballs type) {
      super(type.getFilenamePrefix());
      this.field_77777_bU = 64;
      this.func_77656_e(0);
      this.type = type;
      this.func_77637_a(PixelmonCreativeTabs.pokeball);
      this.canRepair = false;
   }

   public ItemPokeball(EnumPokeballs type, String name) {
      super(name);
      this.field_77777_bU = 64;
      this.func_77656_e(0);
      this.type = type;
      this.func_77637_a(PixelmonCreativeTabs.pokeball);
      this.canRepair = false;
   }

   public ActionResult func_77659_a(World world, EntityPlayer entityPlayer, EnumHand handIn) {
      ItemStack itemStack = entityPlayer.func_184586_b(handIn);
      ThrowPokeballEvent throwPokeBallEvent = new ThrowPokeballEvent(entityPlayer, itemStack, this.type, false);
      Pixelmon.EVENT_BUS.post(throwPokeBallEvent);
      if (throwPokeBallEvent.isCanceled()) {
         return new ActionResult(EnumActionResult.FAIL, itemStack);
      } else {
         if (PixelmonServerConfig.allowCapturingOutsideBattle) {
            if (!entityPlayer.field_71075_bZ.field_75098_d) {
               itemStack.func_77979_a(1);
            }

            world.func_184133_a((EntityPlayer)null, entityPlayer.func_180425_c(), SoundEvents.field_187737_v, SoundCategory.PLAYERS, 0.5F, 1.0F);
            if (!world.field_72995_K) {
               world.func_72838_d(new EntityEmptyPokeball(world, entityPlayer, this.type, !entityPlayer.field_71075_bZ.field_75098_d));
            }
         }

         return new ActionResult(EnumActionResult.SUCCESS, itemStack);
      }
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      EntityPlayer thrower = userWrapper.getPlayerOwner();
      if (!(targetWrapper.getParticipant() instanceof WildPixelmonParticipant)) {
         return false;
      } else {
         ThrowPokeballEvent throwPokeballEvent = new ThrowPokeballEvent(thrower, (ItemStack)null, this.type, true);
         Pixelmon.EVENT_BUS.post(throwPokeballEvent);
         World world = thrower.field_70170_p;
         EntityPokeBall p = new EntityEmptyPokeball(world, thrower, targetWrapper.entity, this.type, BattleRegistry.getBattle(thrower));
         world.func_72838_d(p);
         return super.useFromBag(userWrapper, targetWrapper);
      }
   }
}
