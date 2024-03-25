package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AsOne;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.CheekPouch;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Klutz;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Unnerve;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.heldItems.ChoiceItem;
import com.pixelmonmod.pixelmon.items.heldItems.EVAdjusting;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBlackSludge;
import com.pixelmonmod.pixelmon.items.heldItems.ItemFlameOrb;
import com.pixelmonmod.pixelmon.items.heldItems.ItemIronBall;
import com.pixelmonmod.pixelmon.items.heldItems.ItemLaggingTail;
import com.pixelmonmod.pixelmon.items.heldItems.ItemRingTarget;
import com.pixelmonmod.pixelmon.items.heldItems.ItemStickyBarb;
import com.pixelmonmod.pixelmon.items.heldItems.ItemToxicOrb;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemHeld extends PixelmonItem {
   private EnumHeldItems heldItemType;

   public ItemHeld(EnumHeldItems heldItemType, String itemName) {
      super(itemName);
      this.heldItemType = heldItemType;
      this.func_77637_a(PixelmonCreativeTabs.held);
      this.canRepair = false;
   }

   public EnumHeldItems getHeldItemType() {
      return this.heldItemType;
   }

   public static boolean isItemOfType(ItemStack itemStack, EnumHeldItems type) {
      if (itemStack != null) {
         Item item = itemStack.func_77973_b();
         if (item instanceof ItemHeld && ((ItemHeld)item).heldItemType == type) {
            return true;
         }
      }

      return false;
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      return false;
   }

   public void onStartOfBattle(PixelmonWrapper pw) {
   }

   public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
   }

   public void tookDamage(PixelmonWrapper attacker, PixelmonWrapper defender, float damage, DamageTypeEnum damageType) {
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return damage;
   }

   public double preProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return damage;
   }

   public void postProcessAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
   }

   public void postProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
   }

   public double modifyDamageIncludeFixed(double damage, PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      return damage;
   }

   public void onMiss(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return true;
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
   }

   public void applySwitchOutEffect(PixelmonWrapper pw) {
   }

   public void onStatusAdded(PixelmonWrapper user, PixelmonWrapper opponent, StatusBase status) {
   }

   public void onGlobalStatusAdded(PixelmonWrapper itemHolder, GlobalStatusBase globalStatus) {
   }

   public void onStatModified(PixelmonWrapper affected) {
   }

   public void onTerrainSwitch(BattleControllerBase bc, PixelmonWrapper user, Terrain terrain) {
   }

   public int adjustCritStage(PixelmonWrapper user) {
      return 0;
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
   }

   public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
   }

   public void applyEffectOnContact(PixelmonWrapper pokemon, PixelmonWrapper opponent) {
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return target.type;
   }

   public void eatBerry(PixelmonWrapper user) {
      if (user.getBattleAbility() instanceof CheekPouch && !user.hasFullHealth()) {
         user.healByPercent(33.0F);
         user.bc.sendToAll("pixelmon.abilities.cheekpouch", user.getNickname());
      }

      user.eatenBerry = true;
   }

   public static boolean canEatBerry(PixelmonWrapper user) {
      if (user.bc == null) {
         return true;
      } else if (!canUseItem(user)) {
         return false;
      } else {
         ArrayList opponents = user.bc.getOpponentPokemon(user.getParticipant());
         Iterator var2 = opponents.iterator();

         PixelmonWrapper opponent;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            opponent = (PixelmonWrapper)var2.next();
         } while(!(opponent.getBattleAbility() instanceof Unnerve) && !(opponent.getBattleAbility() instanceof AsOne));

         return false;
      }
   }

   public int[] modifyPowerAndAccuracyUser(int[] modifiedMoveStats, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return modifiedMoveStats;
   }

   public int[] modifyPowerAndAccuracyTarget(int[] modifiedMoveStats, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return modifiedMoveStats;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      return priority;
   }

   public boolean affectMultiturnMove(PixelmonWrapper user) {
      return false;
   }

   public float modifyWeight(float initWeight) {
      return initWeight;
   }

   public boolean hasNegativeEffect() {
      return this instanceof ChoiceItem || this instanceof EVAdjusting || this instanceof ItemBlackSludge || this instanceof ItemFlameOrb || this instanceof ItemIronBall || this instanceof ItemLaggingTail || this instanceof ItemRingTarget || this instanceof ItemStickyBarb || this instanceof ItemToxicOrb;
   }

   public boolean isBerry() {
      return this instanceof ItemBerry;
   }

   public static boolean canUseItem(PixelmonWrapper user) {
      return user.getHeldItem() != null && !(user.getBattleAbility() instanceof Klutz) && !user.hasStatus(StatusType.Embargo, StatusType.CorrosiveGas) && !user.bc.globalStatusController.hasStatus(StatusType.MagicRoom);
   }

   public static ItemHeld getItemHeld(ItemStack itemStack) {
      if (itemStack != null && itemStack != ItemStack.field_190927_a) {
         Item item = itemStack.func_77973_b();
         return (ItemHeld)(item instanceof ItemHeld ? (ItemHeld)item : NoItem.noItem);
      } else {
         return NoItem.noItem;
      }
   }
}
