package com.pixelmonmod.pixelmon.api.economy;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IPixelmonBankAccountManager {
   @Nullable
   default IPixelmonBankAccount getBankAccountUnsafe(UUID uuid) {
      return (IPixelmonBankAccount)this.getBankAccount(uuid).orElse((Object)null);
   }

   @Nullable
   default IPixelmonBankAccount getBankAccountUnsafe(EntityPlayerMP player) {
      return this.getBankAccountUnsafe(player.func_110124_au());
   }

   Optional getBankAccount(UUID var1);

   default Optional getBankAccount(EntityPlayerMP player) {
      return this.getBankAccount(player.func_110124_au());
   }
}
