package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

public enum BaseStatsLearnType {
   LEVELUP_MOVES((bs, ab) -> {
      Iterator var2 = bs.getLevelupMoves().values().iterator();

      List list;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         list = (List)var2.next();
      } while(!list.contains(ab));

      return true;
   }),
   ALL_TR_MOVES((bs, ab) -> {
      Iterator var2 = bs.getTrMoves().iterator();

      ITechnicalMove tr;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         tr = (ITechnicalMove)var2.next();
      } while(tr.getAttack() != ab);

      return true;
   }),
   ALL_TM_MOVES((bs, ab) -> {
      for(int i = 1; i <= 8; ++i) {
         Iterator var3 = bs.getTMMovesFor(i).iterator();

         while(var3.hasNext()) {
            ITechnicalMove tm = (ITechnicalMove)var3.next();
            if (tm.getAttack() == ab) {
               return true;
            }
         }
      }

      return false;
   }),
   HM_MOVES((bs, ab) -> {
      return bs.getHmMoves().contains(ab);
   }),
   TUTOR_MOVES((bs, ab) -> {
      return bs.getTutorMoves().stream().map(Attack::getMove).anyMatch((it) -> {
         return it == ab;
      });
   }),
   TRANSFER_MOVES((bs, ab) -> {
      return bs.getTransferMoves().stream().map(Attack::getMove).anyMatch((it) -> {
         return it == ab;
      });
   }),
   EGG_MOVES((bs, ab) -> {
      return bs.getEggMoves().stream().map(Attack::getMove).anyMatch((it) -> {
         return it == ab;
      });
   }),
   GEN8_TM_MOVES(technicalMachine(8)),
   GEN7_TM_MOVES(technicalMachine(7)),
   GEN6_TM_MOVES(technicalMachine(6)),
   GEN5_TM_MOVES(technicalMachine(5)),
   GEN4_TM_MOVES(technicalMachine(4)),
   GEN3_TM_MOVES(technicalMachine(3)),
   GEN2_TM_MOVES(technicalMachine(2)),
   GEN1_TM_MOVES(technicalMachine(1));

   public static final BaseStatsLearnType[] GEN8_DEFAULT = new BaseStatsLearnType[]{LEVELUP_MOVES, ALL_TR_MOVES, HM_MOVES, TUTOR_MOVES, TRANSFER_MOVES, EGG_MOVES, GEN8_TM_MOVES};
   private BiPredicate canLearn;
   private static final BaseStatsLearnType[] VALUES = values();

   private BaseStatsLearnType(BiPredicate canLearn) {
      this.canLearn = canLearn;
   }

   public boolean canLearn(BaseStats bs, AttackBase ab) {
      return this.canLearn.test(bs, ab);
   }

   public static BaseStatsLearnType fromOrdinal(int orinal) {
      return orinal >= 0 && orinal < VALUES.length ? VALUES[orinal] : null;
   }

   private static BiPredicate technicalMachine(int gen) {
      return (bs, ab) -> {
         Iterator var3 = bs.getTMMovesFor(gen).iterator();

         ITechnicalMove tm;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            tm = (ITechnicalMove)var3.next();
         } while(tm.getAttack() != ab);

         return true;
      };
   }
}
