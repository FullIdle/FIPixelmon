package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CurryFinishedEvent extends Event {
   public final EntityPlayerMP player;
   public final EnumCurryRating rating;
   public final EnumCurryKey curryKey;
   public final EnumBerryFlavor cookingFlavor;

   public CurryFinishedEvent(@Nullable EntityPlayerMP player, EnumCurryRating rating, EnumCurryKey curryKey, EnumBerryFlavor cookingFlavor) {
      this.player = player;
      this.rating = rating;
      this.curryKey = curryKey;
      this.cookingFlavor = cookingFlavor;
   }
}
