package com.pixelmonmod.pixelmon.battles.raids;

public enum EnumRaidKickReason {
   RAID_EXPIRED("raid.kick.expired"),
   LEADER_LEFT("raid.kick.leader"),
   INVALID_POKEMON("raid.kick.invalid");

   private final String message;

   private EnumRaidKickReason(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }
}
