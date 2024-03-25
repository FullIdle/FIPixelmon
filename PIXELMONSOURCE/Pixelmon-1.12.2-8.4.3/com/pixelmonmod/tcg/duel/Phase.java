package com.pixelmonmod.tcg.duel;

public enum Phase {
   INIT(0, "Init", true),
   MULLIGAN(1, "Mulligan", true),
   DRAW(2, "Pre-Turn Handshake", false),
   PLAYBASIC(3, "Summon Pokemon", false),
   EVOLVE(4, "Evolution", false),
   ENERGY(5, "Energy", false),
   TRAINER(6, "Trainer", false),
   RETREAT(7, "Switchout", false),
   POWER(8, "Abilities", false),
   ATTACK(9, "Attack", false),
   END(10, "Post-Turn Handshake", false),
   SELECT(11, "Phase Select", false),
   WAIT(12, "Waiting", false),
   ADMIN(13, "Admin Override", false),
   WIN(14, "Victory", true),
   LOSE(15, "Defeat", true),
   QUIT(16, "Concede", true);

   private final int id;
   private final String name;
   private final boolean onlyOnce;

   private Phase(int id, String name, boolean onlyOnce) {
      this.id = id;
      this.name = name;
      this.onlyOnce = onlyOnce;
   }

   public static int getIDFromPhase(Phase phase) {
      return phase.id;
   }

   public static Phase getPhaseFromID(int id) {
      Phase[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Phase value = var1[var3];
         if (getIDFromPhase(value) == id) {
            return value;
         }
      }

      return null;
   }
}
