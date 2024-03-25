package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class PoisonBadly extends Poison {
   private transient int poisonSeverity = 1;

   public PoisonBadly() {
      super(StatusType.PoisonBadly);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         poisonBadly(user, target, user.attack, true);
      }

   }

   public static boolean poisonBadly(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      if (canPoison(user, target, attack, showMessage)) {
         TextComponentTranslation message = null;
         if (showMessage) {
            message = ChatHandler.getMessage("pixelmon.effect.badlypoisoned", target.getNickname());
         }

         boolean result = target.addStatus(new PoisonBadly(), user, message);
         if (!result && attack != null && attack.getAttackCategory() == AttackCategory.STATUS) {
            user.setAttackFailed();
         }

         return result;
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      super.applyRepeatedEffect(pw);
      if (!pw.isRaidPokemon()) {
         ++this.poisonSeverity;
      }

   }

   protected float getPoisonDamage(PixelmonWrapper pw) {
      return (float)pw.getPercentMaxHealth((float)this.poisonSeverity * 100.0F / 16.0F);
   }

   public void applySwitchOutEffect(PixelmonWrapper outgoing, PixelmonWrapper incoming) {
      this.poisonSeverity = 1;
   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
      int index = pokemon.getStatusIndex(StatusType.PoisonBadly);
      if (index >= 0) {
         pokemon.setStatus(index, new Poison());
      }

   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return new PoisonBadly();
   }

   public StatusBase copy() {
      return new PoisonBadly();
   }
}
