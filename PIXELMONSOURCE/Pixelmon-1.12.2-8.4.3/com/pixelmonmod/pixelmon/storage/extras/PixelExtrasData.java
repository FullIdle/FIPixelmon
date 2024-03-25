package com.pixelmonmod.pixelmon.storage.extras;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.enums.ServerCosmetics;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.DeoxysStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import java.awt.Color;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.minecraft.util.Tuple;

public class PixelExtrasData {
   public final UUID id;
   Tuple apiData;
   EnumSet serverCosmetics = null;
   private final EnumSet enabled = EnumSet.allOf(Category.class);
   private HatType hatType;
   private MonocleType monocleType;
   SashType sashType;
   RobeType robeType;
   SphealType sphealType;
   private int[] sashColors;
   private int[] capColors;
   private int[] monocleOffset;

   public PixelExtrasData(UUID id) {
      this.hatType = PixelExtrasData.HatType.NONE;
      this.monocleType = PixelExtrasData.MonocleType.NONE;
      this.sashType = null;
      this.robeType = PixelExtrasData.RobeType.NONE;
      this.sphealType = null;
      this.sashColors = null;
      this.capColors = new int[]{Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue()};
      this.monocleOffset = new int[]{5, 4, 3};
      this.id = id;
   }

   public boolean isReady() {
      return this.apiData != null;
   }

   public boolean hasData() {
      return this.has(ExtrasContact.Groups.values()) || this.has(ServerCosmetics.values());
   }

   public void updateServerCosmetics(Set serverCosmetics) {
      if (serverCosmetics.isEmpty()) {
         this.serverCosmetics = EnumSet.noneOf(ServerCosmetics.class);
      } else {
         this.serverCosmetics = EnumSet.copyOf(serverCosmetics);
      }

      this.getHatType();
      this.getMonocleType();
      this.getRobeType();
      this.getSashType();
      this.getSphealType();
   }

   public Set getServerCosmetics() {
      return (Set)(this.serverCosmetics != null ? this.serverCosmetics : Collections.emptySet());
   }

   public int dataHash() {
      int hash = 0;
      if (!this.isReady()) {
         return hash;
      } else {
         Iterator var2 = ((EnumSet)this.apiData.func_76341_a()).iterator();

         while(var2.hasNext()) {
            ExtrasContact.Groups group = (ExtrasContact.Groups)var2.next();
            if (group != null) {
               hash = 31 * hash + group.ordinal() + 1;
            }
         }

         return hash;
      }
   }

   public int[] getColours(Category category) {
      if (category == PixelExtrasData.Category.SASH) {
         if (this.sashColors != null) {
            return this.sashColors;
         } else {
            return this.apiData != null ? (int[])this.apiData.func_76340_b() : new int[]{0, 0, 0};
         }
      } else if (category == PixelExtrasData.Category.HAT) {
         return this.capColors;
      } else {
         return category == PixelExtrasData.Category.MONOCLE ? this.monocleOffset : null;
      }
   }

   public void setColours(Category category, int[] array) {
      if (category == PixelExtrasData.Category.SASH) {
         this.sashColors = array;
      } else if (category == PixelExtrasData.Category.HAT) {
         this.capColors = array;
      } else if (category == PixelExtrasData.Category.MONOCLE) {
         this.monocleOffset = array;
      }

   }

   public boolean isEnabled(Category category) {
      return this.enabled.contains(category);
   }

   public void setEnabled(Category category, boolean bool) {
      if (bool) {
         this.enabled.add(category);
      } else {
         this.enabled.remove(category);
      }

   }

   public HatType getHatType() {
      if (this.isEnabled(PixelExtrasData.Category.HAT)) {
         switch (this.hatType) {
            case SCOR_HOOD:
            case TRAINER_CAP:
               return this.has(ExtrasContact.Groups.Trainer_Cap) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case PIKA_HOOD:
               return this.has(ExtrasContact.Groups.Pikahood) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case EEVEE_HOOD:
               return this.has(ExtrasContact.Groups.EeveeHood) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case WIKI:
               return this.has(ExtrasContact.Groups.Wiki) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case TOP_HAT:
            case FEZ:
            case FEDORA:
               return this.has(ExtrasContact.Groups.CompWinner) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case SPHEAL_HAT:
               return this.has(ExtrasContact.Groups.SphealHats) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case WELKIN_HAT:
               return this.has(ExtrasContact.Groups.Welkin) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case SABLEYE_HOOD:
               return this.has(ExtrasContact.Groups.AnniversaryHood) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case ESPEON_SCARF:
               return this.has(ServerCosmetics.ESPEON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case FLAREON_SCARF:
               return this.has(ServerCosmetics.FLAREON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case GLACEON_SCARF:
               return this.has(ServerCosmetics.GLACEON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case JOLTEON_SCARF:
               return this.has(ServerCosmetics.JOLTEON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case LEAFEON_SCARF:
               return this.has(ServerCosmetics.LEAFEON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case SYLVEON_SCARF:
               return this.has(ServerCosmetics.SYLVEON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case UMBREON_SCARF:
               return this.has(ServerCosmetics.UMBREON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
            case VAPOREON_SCARF:
               return this.has(ServerCosmetics.VAPOREON_SCARF) ? this.hatType : (this.hatType = PixelExtrasData.HatType.NONE);
         }
      }

      return PixelExtrasData.HatType.NONE;
   }

   public boolean setHatType(HatType hat) {
      if ((hat != PixelExtrasData.HatType.TRAINER_CAP || !this.has(ExtrasContact.Groups.Trainer_Cap)) && (hat != PixelExtrasData.HatType.PIKA_HOOD || !this.has(ExtrasContact.Groups.Pikahood)) && (hat != PixelExtrasData.HatType.EEVEE_HOOD || !this.has(ExtrasContact.Groups.EeveeHood)) && (hat != PixelExtrasData.HatType.SCOR_HOOD || !this.has(ExtrasContact.Groups.Trainer_Cap)) && (hat != PixelExtrasData.HatType.WIKI || !this.has(ExtrasContact.Groups.Wiki)) && (hat != PixelExtrasData.HatType.SPHEAL_HAT || !this.has(ExtrasContact.Groups.SphealHats)) && (hat != PixelExtrasData.HatType.WELKIN_HAT || !this.has(ExtrasContact.Groups.Welkin)) && (hat != PixelExtrasData.HatType.SABLEYE_HOOD || !this.has(ExtrasContact.Groups.AnniversaryHood)) && (hat != PixelExtrasData.HatType.ESPEON_SCARF || !this.has(ServerCosmetics.ESPEON_SCARF)) && (hat != PixelExtrasData.HatType.FLAREON_SCARF || !this.has(ServerCosmetics.FLAREON_SCARF)) && (hat != PixelExtrasData.HatType.GLACEON_SCARF || !this.has(ServerCosmetics.GLACEON_SCARF)) && (hat != PixelExtrasData.HatType.JOLTEON_SCARF || !this.has(ServerCosmetics.JOLTEON_SCARF)) && (hat != PixelExtrasData.HatType.LEAFEON_SCARF || !this.has(ServerCosmetics.LEAFEON_SCARF)) && (hat != PixelExtrasData.HatType.SYLVEON_SCARF || !this.has(ServerCosmetics.SYLVEON_SCARF)) && (hat != PixelExtrasData.HatType.UMBREON_SCARF || !this.has(ServerCosmetics.UMBREON_SCARF)) && (hat != PixelExtrasData.HatType.VAPOREON_SCARF || !this.has(ServerCosmetics.VAPOREON_SCARF)) && !this.has(ExtrasContact.Groups.CompWinner) && hat != PixelExtrasData.HatType.NONE) {
         return false;
      } else {
         this.hatType = hat;
         return true;
      }
   }

   public EnumSet getAvailableHats() {
      EnumSet set = EnumSet.noneOf(HatType.class);
      if (this.has(ExtrasContact.Groups.Trainer_Cap)) {
         set.add(PixelExtrasData.HatType.TRAINER_CAP);
         set.add(PixelExtrasData.HatType.SCOR_HOOD);
      }

      if (this.has(ExtrasContact.Groups.CompWinner)) {
         set.add(PixelExtrasData.HatType.TOP_HAT);
         set.add(PixelExtrasData.HatType.FEZ);
         set.add(PixelExtrasData.HatType.FEDORA);
      }

      if (this.has(ExtrasContact.Groups.Pikahood)) {
         set.add(PixelExtrasData.HatType.PIKA_HOOD);
      }

      if (this.has(ExtrasContact.Groups.EeveeHood)) {
         set.add(PixelExtrasData.HatType.EEVEE_HOOD);
      }

      if (this.has(ExtrasContact.Groups.Wiki)) {
         set.add(PixelExtrasData.HatType.WIKI);
      }

      if (this.has(ExtrasContact.Groups.SphealHats)) {
         set.add(PixelExtrasData.HatType.SPHEAL_HAT);
      }

      if (this.has(ExtrasContact.Groups.Welkin)) {
         set.add(PixelExtrasData.HatType.WELKIN_HAT);
      }

      if (this.has(ExtrasContact.Groups.AnniversaryHood)) {
         set.add(PixelExtrasData.HatType.SABLEYE_HOOD);
      }

      if (this.has(ServerCosmetics.ESPEON_SCARF)) {
         set.add(PixelExtrasData.HatType.ESPEON_SCARF);
      }

      if (this.has(ServerCosmetics.FLAREON_SCARF)) {
         set.add(PixelExtrasData.HatType.FLAREON_SCARF);
      }

      if (this.has(ServerCosmetics.GLACEON_SCARF)) {
         set.add(PixelExtrasData.HatType.GLACEON_SCARF);
      }

      if (this.has(ServerCosmetics.JOLTEON_SCARF)) {
         set.add(PixelExtrasData.HatType.JOLTEON_SCARF);
      }

      if (this.has(ServerCosmetics.LEAFEON_SCARF)) {
         set.add(PixelExtrasData.HatType.LEAFEON_SCARF);
      }

      if (this.has(ServerCosmetics.SYLVEON_SCARF)) {
         set.add(PixelExtrasData.HatType.SYLVEON_SCARF);
      }

      if (this.has(ServerCosmetics.UMBREON_SCARF)) {
         set.add(PixelExtrasData.HatType.UMBREON_SCARF);
      }

      if (this.has(ServerCosmetics.VAPOREON_SCARF)) {
         set.add(PixelExtrasData.HatType.VAPOREON_SCARF);
      }

      return set;
   }

   public MonocleType getMonocleType() {
      if (this.isEnabled(PixelExtrasData.Category.MONOCLE)) {
         switch (this.monocleType) {
            case GOLD_MONOCLE:
            case BLACK_MONOCLE:
               return this.has(ExtrasContact.Groups.Monocle_Gold) ? this.monocleType : (this.monocleType = PixelExtrasData.MonocleType.NONE);
            case QUAGSIRE_MASK:
               return this.has(ExtrasContact.Groups.HaaMask) ? this.monocleType : (this.monocleType = PixelExtrasData.MonocleType.NONE);
            case QUARANTINE_MASK:
               return this.has(ExtrasContact.Groups.QuarantineMask) ? this.monocleType : (this.monocleType = PixelExtrasData.MonocleType.NONE);
         }
      }

      return PixelExtrasData.MonocleType.NONE;
   }

   public boolean setMonocleType(MonocleType monocle) {
      if ((monocle != PixelExtrasData.MonocleType.GOLD_MONOCLE || !this.has(ExtrasContact.Groups.Monocle_Gold)) && (monocle != PixelExtrasData.MonocleType.BLACK_MONOCLE || !this.has(ExtrasContact.Groups.Monocle_Gold)) && (monocle != PixelExtrasData.MonocleType.QUAGSIRE_MASK || !this.has(ExtrasContact.Groups.HaaMask)) && (monocle != PixelExtrasData.MonocleType.QUARANTINE_MASK || !this.has(ExtrasContact.Groups.QuarantineMask)) && monocle != PixelExtrasData.MonocleType.NONE) {
         return false;
      } else {
         this.monocleType = monocle;
         return true;
      }
   }

   public EnumSet getAvailableMonocles() {
      EnumSet set = EnumSet.noneOf(MonocleType.class);
      if (this.has(ExtrasContact.Groups.Monocle_Gold)) {
         set.add(PixelExtrasData.MonocleType.GOLD_MONOCLE);
      }

      if (this.has(ExtrasContact.Groups.Monocle_Gold)) {
         set.add(PixelExtrasData.MonocleType.BLACK_MONOCLE);
      }

      if (this.has(ExtrasContact.Groups.HaaMask)) {
         set.add(PixelExtrasData.MonocleType.QUAGSIRE_MASK);
      }

      if (this.has(ExtrasContact.Groups.QuarantineMask)) {
         set.add(PixelExtrasData.MonocleType.QUARANTINE_MASK);
      }

      return set;
   }

   public SashType getSashType() {
      if (this.isEnabled(PixelExtrasData.Category.SASH)) {
         if (this.sashType == null) {
            return this.has(ExtrasContact.Groups.Rainbow_Sash) ? PixelExtrasData.SashType.RAINBOW : (this.has(ExtrasContact.Groups.Admin, ExtrasContact.Groups.Junior_Admin) ? PixelExtrasData.SashType.RANK_ADMIN : (this.has(ExtrasContact.Groups.Developer) ? PixelExtrasData.SashType.RANK_DEV : (this.has(ExtrasContact.Groups.Modeler) ? PixelExtrasData.SashType.RANK_MODELER : (this.has(ExtrasContact.Groups.Support) ? PixelExtrasData.SashType.RANK_SUPPORT : (this.has(ExtrasContact.Groups.NitroBooster) ? PixelExtrasData.SashType.BOOSTER : (this.has(ExtrasContact.Groups.Sash) ? PixelExtrasData.SashType.REGULAR : PixelExtrasData.SashType.NONE))))));
         }

         switch (this.sashType) {
            case REGULAR:
               return this.has(ExtrasContact.Groups.Sash) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RAINBOW:
               return this.has(ExtrasContact.Groups.Rainbow_Sash) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RANK_ADMIN:
               return this.has(ExtrasContact.Groups.Admin) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RANK_JR:
               return this.has(ExtrasContact.Groups.Junior_Admin) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RANK_DEV:
               return this.has(ExtrasContact.Groups.Developer) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RANK_MODELER:
               return this.has(ExtrasContact.Groups.Modeler) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case RANK_SUPPORT:
               return this.has(ExtrasContact.Groups.Support) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
            case BOOSTER:
               return this.has(ExtrasContact.Groups.NitroBooster) ? this.sashType : (this.sashType = PixelExtrasData.SashType.NONE);
         }
      }

      return PixelExtrasData.SashType.NONE;
   }

   public boolean setSashType(SashType sash) {
      if ((sash != PixelExtrasData.SashType.REGULAR || !this.has(ExtrasContact.Groups.Sash)) && (sash != PixelExtrasData.SashType.RAINBOW || !this.has(ExtrasContact.Groups.Rainbow_Sash)) && (sash != PixelExtrasData.SashType.RANK_ADMIN || !this.has(ExtrasContact.Groups.Admin)) && (sash != PixelExtrasData.SashType.RANK_JR || !this.has(ExtrasContact.Groups.Junior_Admin)) && (sash != PixelExtrasData.SashType.RANK_DEV || !this.has(ExtrasContact.Groups.Developer)) && (sash != PixelExtrasData.SashType.RANK_MODELER || !this.has(ExtrasContact.Groups.Modeler)) && (sash != PixelExtrasData.SashType.RANK_SUPPORT || !this.has(ExtrasContact.Groups.Support)) && (sash != PixelExtrasData.SashType.BOOSTER || !this.has(ExtrasContact.Groups.NitroBooster)) && sash != PixelExtrasData.SashType.NONE) {
         return false;
      } else {
         this.sashType = sash;
         return true;
      }
   }

   public EnumSet getAvailableSashs() {
      EnumSet set = EnumSet.noneOf(SashType.class);
      if (this.has(ExtrasContact.Groups.Sash)) {
         set.add(PixelExtrasData.SashType.REGULAR);
      }

      if (this.has(ExtrasContact.Groups.Rainbow_Sash)) {
         set.add(PixelExtrasData.SashType.RAINBOW);
      }

      if (this.has(ExtrasContact.Groups.Admin)) {
         set.add(PixelExtrasData.SashType.RANK_ADMIN);
      }

      if (this.has(ExtrasContact.Groups.Junior_Admin)) {
         set.add(PixelExtrasData.SashType.RANK_JR);
      }

      if (this.has(ExtrasContact.Groups.Developer)) {
         set.add(PixelExtrasData.SashType.RANK_DEV);
      }

      if (this.has(ExtrasContact.Groups.Modeler)) {
         set.add(PixelExtrasData.SashType.RANK_MODELER);
      }

      if (this.has(ExtrasContact.Groups.Support)) {
         set.add(PixelExtrasData.SashType.RANK_SUPPORT);
      }

      if (this.has(ExtrasContact.Groups.NitroBooster)) {
         set.add(PixelExtrasData.SashType.BOOSTER);
      }

      return set;
   }

   public EnumSet getAvailableTextures() {
      EnumSet set = EnumSet.noneOf(EnumSpecies.class);
      if (this.has(ExtrasContact.Groups.Spectral_Jeweller)) {
         set.add(EnumSpecies.Gastly);
         set.add(EnumSpecies.Haunter);
         set.add(EnumSpecies.Gengar);
      }

      if (this.has(ExtrasContact.Groups.Shadow_Lugia)) {
         set.add(EnumSpecies.Lugia);
         set.add(EnumSpecies.Hooh);
      }

      if (this.has(ExtrasContact.Groups.Wobbuffet)) {
         set.add(EnumSpecies.Wobbuffet);
      }

      if (this.has(ExtrasContact.Groups.PDSentret)) {
         set.add(EnumSpecies.Sentret);
      }

      if (this.has(ExtrasContact.Groups.ELGreninja)) {
         set.add(EnumSpecies.Greninja);
      }

      if (this.has(ExtrasContact.Groups.SFXMewtwo)) {
         set.add(EnumSpecies.Mewtwo);
      }

      if (this.has(ExtrasContact.Groups.CinderaceGoku)) {
         set.add(EnumSpecies.Cinderace);
      }

      if (this.has(ExtrasContact.Groups.BugcatcherMothim)) {
         set.add(EnumSpecies.Mothim);
      }

      if (this.has(ExtrasContact.Groups.DiscordLinked)) {
         set.add(EnumSpecies.Deoxys);
      }

      if (this.has(ExtrasContact.Groups.ChristmasSpheal)) {
         set.add(EnumSpecies.Spheal);
      }

      return set;
   }

   public boolean canSeeTexture(EnumSpecies pokemon) {
      switch (pokemon) {
         case Gastly:
         case Haunter:
         case Gengar:
            return this.has(ExtrasContact.Groups.Spectral_Jeweller);
         case Lugia:
         case Hooh:
            return this.has(ExtrasContact.Groups.Shadow_Lugia);
         case Wobbuffet:
            return this.has(ExtrasContact.Groups.Wobbuffet);
         case Sentret:
            return this.has(ExtrasContact.Groups.PDSentret);
         case Greninja:
            return this.has(ExtrasContact.Groups.ELGreninja);
         case Mewtwo:
            return this.has(ExtrasContact.Groups.SFXMewtwo);
         case Lucario:
            return this.has(ExtrasContact.Groups.IRWLucario);
         case Cinderace:
            return this.has(ExtrasContact.Groups.CinderaceGoku);
         case Mothim:
            return this.has(ExtrasContact.Groups.BugcatcherMothim);
         case Deoxys:
            return this.has(ExtrasContact.Groups.DiscordLinked);
         case Spheal:
            return this.has(ExtrasContact.Groups.ChristmasSpheal);
         default:
            return false;
      }
   }

   public void checkPokemon(Pokemon pokemon) {
      if (this.isReady()) {
         if (pokemon.getFormEnum() == EnumSpecial.Online) {
            if (!this.canSeeTexture(pokemon.getSpecies())) {
               pokemon.setForm(pokemon.getSpecies().getFormEnum(-1));
            }
         } else if (pokemon.getSpecies() == EnumSpecies.Deoxys && pokemon.getFormEnum() == EnumDeoxys.Sus && !this.canSeeTexture(EnumSpecies.Deoxys)) {
            pokemon.setForm(EnumDeoxys.Normal);
            ((DeoxysStats)pokemon.getExtraStats(DeoxysStats.class)).setSus(false);
         }

      }
   }

   public boolean setRobeType(RobeType type) {
      if ((type != PixelExtrasData.RobeType.WIZARD || !this.has(ExtrasContact.Groups.Trainer_Cap)) && (type != PixelExtrasData.RobeType.ALTER || !this.has(ExtrasContact.Groups.AlterRobe)) && (type != PixelExtrasData.RobeType.STRIKE || !this.has(ExtrasContact.Groups.StrikeRobe)) && (type != PixelExtrasData.RobeType.ASHEN || !this.has(ExtrasContact.Groups.AshenRobe)) && (type != PixelExtrasData.RobeType.WINTER_CLOAK || !this.has(ExtrasContact.Groups.Trainer_Cap)) && (type != PixelExtrasData.RobeType.FROSLASS_YUKATA || !this.has(ExtrasContact.Groups.Froslass_Yukata)) && (type != PixelExtrasData.RobeType.DROWNED || !this.has(ServerCosmetics.DROWNED_ROBE)) && type != PixelExtrasData.RobeType.NONE) {
         return false;
      } else {
         this.robeType = type;
         return true;
      }
   }

   public RobeType getRobeType() {
      if (this.isEnabled(PixelExtrasData.Category.ROBE)) {
         switch (this.robeType) {
            case ALTER:
               return this.has(ExtrasContact.Groups.AlterRobe) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
            case STRIKE:
               return this.has(ExtrasContact.Groups.StrikeRobe) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
            case ASHEN:
               return this.has(ExtrasContact.Groups.AshenRobe) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
            case WIZARD:
            case WINTER_CLOAK:
               return this.has(ExtrasContact.Groups.Trainer_Cap) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
            case DROWNED:
               return this.has(ServerCosmetics.DROWNED_ROBE) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
            case FROSLASS_YUKATA:
               return this.has(ExtrasContact.Groups.Froslass_Yukata) ? this.robeType : (this.robeType = PixelExtrasData.RobeType.NONE);
         }
      }

      return PixelExtrasData.RobeType.NONE;
   }

   public EnumSet getAvailableRobes() {
      EnumSet set = EnumSet.noneOf(RobeType.class);
      if (this.has(ExtrasContact.Groups.Trainer_Cap)) {
         set.add(PixelExtrasData.RobeType.WIZARD);
      }

      if (this.has(ExtrasContact.Groups.AlterRobe)) {
         set.add(PixelExtrasData.RobeType.ALTER);
      }

      if (this.has(ExtrasContact.Groups.StrikeRobe)) {
         set.add(PixelExtrasData.RobeType.STRIKE);
      }

      if (this.has(ExtrasContact.Groups.AshenRobe)) {
         set.add(PixelExtrasData.RobeType.ASHEN);
      }

      if (this.has(ExtrasContact.Groups.Trainer_Cap)) {
         set.add(PixelExtrasData.RobeType.WINTER_CLOAK);
      }

      if (this.has(ExtrasContact.Groups.Froslass_Yukata)) {
         set.add(PixelExtrasData.RobeType.FROSLASS_YUKATA);
      }

      if (this.has(ServerCosmetics.DROWNED_ROBE)) {
         set.add(PixelExtrasData.RobeType.DROWNED);
      }

      return set;
   }

   public boolean hasBoostedNecklace() {
      return this.has(ExtrasContact.Groups.NitroBooster);
   }

   public SphealType getSphealType() {
      return this.sphealType == null ? PixelExtrasData.SphealType.DEFAULT : this.sphealType;
   }

   public void setSphealType(SphealType type) {
      this.sphealType = type;
   }

   public void randomiseSpheal() {
      boolean shiny = RandomHelper.getRandomChance(10);
      boolean stitched = RandomHelper.getRandomChance();
      this.sphealType = PixelExtrasData.SphealType.from(shiny, stitched);
   }

   final boolean has(ExtrasContact.Groups... groups) {
      if (this.apiData != null) {
         ExtrasContact.Groups[] var2 = groups;
         int var3 = groups.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ExtrasContact.Groups group = var2[var4];
            if (((EnumSet)this.apiData.func_76341_a()).contains(group)) {
               return true;
            }
         }
      }

      return false;
   }

   final boolean has(ServerCosmetics... cosmetics) {
      if (this.serverCosmetics != null) {
         ServerCosmetics[] var2 = cosmetics;
         int var3 = cosmetics.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ServerCosmetics cosmetic = var2[var4];
            if (this.serverCosmetics.contains(cosmetic)) {
               return true;
            }
         }
      }

      return false;
   }

   public static enum Category {
      SASH,
      HAT,
      ROBE,
      MONOCLE;
   }

   public static enum SphealType {
      DEFAULT(false, false),
      SHINY(true, false),
      STITCHED(false, true),
      SHINY_STITCHED(true, true);

      public final boolean shiny;
      public final boolean stitched;

      private SphealType(boolean shiny, boolean stitched) {
         this.shiny = shiny;
         this.stitched = stitched;
      }

      public static SphealType from(boolean shiny, boolean stitched) {
         SphealType[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SphealType type = var2[var4];
            if (type.shiny == shiny && type.stitched == stitched) {
               return type;
            }
         }

         return DEFAULT;
      }
   }

   public static enum RobeType {
      NONE,
      WIZARD,
      DROWNED,
      ALTER,
      STRIKE,
      ASHEN,
      WINTER_CLOAK,
      FROSLASS_YUKATA;
   }

   public static enum SashType {
      NONE,
      REGULAR,
      RAINBOW,
      RANK_ADMIN,
      RANK_JR,
      RANK_DEV,
      RANK_MODELER,
      RANK_SUPPORT,
      BOOSTER;
   }

   public static enum MonocleType {
      NONE(-1),
      GOLD_MONOCLE(0),
      BLACK_MONOCLE(1),
      QUAGSIRE_MASK(2),
      QUARANTINE_MASK(3);

      public final int id;

      private MonocleType(int id) {
         this.id = id;
      }

      public static MonocleType getFromId(int id) {
         MonocleType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MonocleType monocle = var1[var3];
            if (monocle.id == id) {
               return monocle;
            }
         }

         return NONE;
      }
   }

   public static enum HatType {
      NONE(-1),
      TOP_HAT(0),
      FEZ(1),
      FEDORA(2),
      TRAINER_CAP(4),
      PIKA_HOOD(5),
      WIKI(6),
      EEVEE_HOOD(7),
      SCOR_HOOD(8),
      SPHEAL_HAT(9),
      ESPEON_SCARF(10),
      FLAREON_SCARF(11),
      GLACEON_SCARF(12),
      JOLTEON_SCARF(13),
      LEAFEON_SCARF(14),
      SYLVEON_SCARF(15),
      UMBREON_SCARF(16),
      VAPOREON_SCARF(17),
      WELKIN_HAT(18),
      SABLEYE_HOOD(19);

      public final int id;

      private HatType(int id) {
         this.id = id;
      }

      public static HatType getFromId(int id) {
         HatType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            HatType hat = var1[var3];
            if (hat.id == id) {
               return hat;
            }
         }

         return NONE;
      }

      public static HatType getFromName(String name) {
         HatType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            HatType hat = var1[var3];
            if (hat.name().replace("_", "").equalsIgnoreCase(name)) {
               return hat;
            }
         }

         return NONE;
      }
   }
}
