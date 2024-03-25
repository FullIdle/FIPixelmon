package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class StatusPersist extends StatusBase {
   public StatusPersist(StatusType type) {
      super(type);
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74777_a("Status", (short)this.type.ordinal());
   }

   public abstract StatusPersist restoreFromNBT(NBTTagCompound var1);

   public static StatusPersist readStatusFromNBT(NBTTagCompound nbt) {
      StatusPersist status = NoStatus.noStatus;
      int statusCount = false;
      if (nbt.func_74764_b("Status")) {
         StatusPersist s = StatusType.getEffectInstance(nbt.func_74762_e("Status"));
         if (s != null) {
            status = s.restoreFromNBT(nbt);
         }
      } else if (nbt.func_74764_b("StatusCount")) {
         int statusCount = nbt.func_74765_d("StatusCount");

         int i;
         for(i = 0; i < statusCount; ++i) {
            StatusPersist s = StatusType.getEffectInstance(nbt.func_74762_e("Status" + i));
            if (s != null) {
               status = s.restoreFromNBT(nbt);
               break;
            }
         }

         for(i = 0; i < statusCount; ++i) {
            nbt.func_82580_o("Status" + i);
         }

         nbt.func_82580_o("StatusCount");
      }

      return (StatusPersist)status;
   }

   protected boolean addStatus(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage, String alreadyMessage, String addMessage) {
      boolean isStatusMove = attack != null && attack.getAttackCategory() == AttackCategory.STATUS;
      if (attack != null && (target.hasStatus(StatusType.Sleep) && attack.getMove().hasEffect(Sleep.class) || target.hasStatus(StatusType.Paralysis) && attack.getMove().hasEffect(Paralysis.class))) {
         if (showMessage && isStatusMove) {
            user.bc.sendToAll(alreadyMessage, target.getNickname());
            user.setAttackFailed();
         }

         return false;
      } else if (!target.hasPrimaryStatus() && !this.isImmune(target)) {
         TextComponentTranslation message = null;
         if (showMessage) {
            message = ChatHandler.getMessage(addMessage, target.getNickname());
         }

         boolean result = target.addStatus(this, user, message);
         if (!result && isStatusMove) {
            user.setAttackFailed();
         }

         return result;
      } else {
         if (showMessage && isStatusMove) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.setAttackFailed();
         }

         return false;
      }
   }

   public boolean isStatus(StatusType type) {
      return this.type == type;
   }

   protected boolean alreadyHasStatus(PixelmonWrapper pw) {
      return pw.hasStatus(this.type);
   }
}
