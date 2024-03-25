package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum StatusType implements ITranslatable {
   Burn,
   Confusion,
   Cursed,
   Infatuated,
   Flee,
   Flinch,
   Flying,
   Freeze,
   Leech,
   LightScreen,
   Mist,
   Paralysis,
   Poison,
   PoisonBadly,
   Protect,
   SafeGuard,
   Sleep,
   SmackedDown,
   Substitute,
   Sunny,
   Wait,
   TrickRoom,
   Perish,
   Yawn,
   Disable,
   Immobilize,
   Recharge,
   AquaRing,
   UnderGround,
   Transformed,
   MeanLook,
   NoRetreat,
   FutureSight,
   MagnetRise,
   Spikes,
   ToxicSpikes,
   StealthRock,
   Steelsurge,
   PartialTrap,
   GMaxRepeatDamage,
   Reflect,
   Submerged,
   Raging,
   Telekinesis,
   Tailwind,
   DestinyBond,
   Taunt,
   TempMoveset,
   HealingWish,
   Roosting,
   Wish,
   Encore,
   Focus,
   MagicCoat,
   Ingrain,
   Stockpile,
   Snatch,
   Minimize,
   Gravity,
   Hail,
   Rainy,
   Sandstorm,
   Torment,
   Foresight,
   GastroAcid,
   GuardSplit,
   PowerSplit,
   WonderRoom,
   LockOn,
   Endure,
   WideGuard,
   Charge,
   Nightmare,
   MeFirst,
   PowerTrick,
   Autotomize,
   DefenseCurl,
   SkyDropping,
   SkyDropped,
   FollowMe,
   Imprison,
   HealBlock,
   MudSport,
   WaterSport,
   FirePledge,
   GrassPledge,
   WaterPledge,
   HelpingHand,
   QuickGuard,
   Embargo,
   Grudge,
   LuckyChant,
   MagicRoom,
   LunarDance,
   Vanish,
   MultiTurn,
   Bide,
   Uproar,
   EchoedVoice,
   FuryCutter,
   DarkAura,
   FairyAura,
   CraftyShield,
   None,
   ElectricTerrain,
   Electrify,
   FairyLock,
   GrassyTerrain,
   IonDeluge,
   KingsShield,
   BanefulBunker,
   MatBlock,
   MistyTerrain,
   Powder,
   SpikyShield,
   StickyWeb,
   PsychicTerrain,
   MysteriousAirCurrent,
   AuroraVeil,
   BeakBlast,
   SpeedSwap,
   TarShot,
   ShellTrap,
   CorrosiveGas,
   ThroatChop,
   MaxGuard,
   Obstruct,
   LaserFocus;

   static Map restoreStatusList = new HashMap(6);

   public String getUnlocalizedName() {
      return "status." + this.name().toLowerCase(Locale.ROOT) + ".name";
   }

   public boolean isStatus(StatusType... statuses) {
      StatusType[] var2 = statuses;
      int var3 = statuses.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StatusType status = var2[var4];
         if (this == status) {
            return true;
         }
      }

      return false;
   }

   public static StatusType getStatusEffect(String string) {
      StatusType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         StatusType t = var1[var3];
         if (t.toString().equalsIgnoreCase(string)) {
            return t;
         }
      }

      return null;
   }

   public static boolean isStatusEffect(String string) {
      return getStatusEffect(string) != null;
   }

   public static StatusType getEffect(int integer) {
      StatusType[] statuses = values();
      return integer >= 0 && integer < statuses.length ? statuses[integer] : null;
   }

   public static StatusPersist getEffectInstance(int integer) {
      StatusType type = getEffect(integer);
      return type != null ? (StatusPersist)restoreStatusList.get(type) : null;
   }

   public static boolean isPrimaryStatus(StatusType status) {
      return status.isStatus(Poison, Burn, PoisonBadly, Freeze, Sleep, Paralysis);
   }

   public boolean isPrimaryStatus() {
      return isPrimaryStatus(this);
   }

   public static float[] getTexturePos(StatusType type) {
      if (type == Burn) {
         return new float[]{8.0F, 8.0F};
      } else if (type == Freeze) {
         return new float[]{264.0F, 8.0F};
      } else if (type == Paralysis) {
         return new float[]{520.0F, 8.0F};
      } else if (type != Poison && type != PoisonBadly) {
         return type == Sleep ? new float[]{520.0F, 264.0F} : new float[]{-1.0F, -1.0F};
      } else {
         return new float[]{8.0F, 264.0F};
      }
   }

   static {
      restoreStatusList.put(Burn, new Burn());
      restoreStatusList.put(Freeze, new Freeze());
      restoreStatusList.put(Paralysis, new Paralysis());
      restoreStatusList.put(Poison, new Poison());
      restoreStatusList.put(PoisonBadly, new PoisonBadly());
      restoreStatusList.put(Sleep, new Sleep());
   }
}
