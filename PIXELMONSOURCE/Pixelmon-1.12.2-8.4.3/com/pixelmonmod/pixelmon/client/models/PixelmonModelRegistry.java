package com.pixelmonmod.pixelmon.client.models;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.client.models.smd.DeformVertex;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumAegislash;
import com.pixelmonmod.pixelmon.enums.forms.EnumAlcremie;
import com.pixelmonmod.pixelmon.enums.forms.EnumBasculin;
import com.pixelmonmod.pixelmon.enums.forms.EnumBidoof;
import com.pixelmonmod.pixelmon.enums.forms.EnumBlastoise;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurmy;
import com.pixelmonmod.pixelmon.enums.forms.EnumCalyrex;
import com.pixelmonmod.pixelmon.enums.forms.EnumCastform;
import com.pixelmonmod.pixelmon.enums.forms.EnumCharizard;
import com.pixelmonmod.pixelmon.enums.forms.EnumCherrim;
import com.pixelmonmod.pixelmon.enums.forms.EnumCramorant;
import com.pixelmonmod.pixelmon.enums.forms.EnumDarmanitan;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumEiscue;
import com.pixelmonmod.pixelmon.enums.forms.EnumEternatus;
import com.pixelmonmod.pixelmon.enums.forms.EnumFlabebe;
import com.pixelmonmod.pixelmon.enums.forms.EnumGastrodon;
import com.pixelmonmod.pixelmon.enums.forms.EnumGengar;
import com.pixelmonmod.pixelmon.enums.forms.EnumGigantamax;
import com.pixelmonmod.pixelmon.enums.forms.EnumGiratina;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import com.pixelmonmod.pixelmon.enums.forms.EnumHoopa;
import com.pixelmonmod.pixelmon.enums.forms.EnumKeldeo;
import com.pixelmonmod.pixelmon.enums.forms.EnumKyurem;
import com.pixelmonmod.pixelmon.enums.forms.EnumLunatone;
import com.pixelmonmod.pixelmon.enums.forms.EnumLycanroc;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeloetta;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeowth;
import com.pixelmonmod.pixelmon.enums.forms.EnumMinior;
import com.pixelmonmod.pixelmon.enums.forms.EnumMissingNo;
import com.pixelmonmod.pixelmon.enums.forms.EnumMorpeko;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.EnumOricorio;
import com.pixelmonmod.pixelmon.enums.forms.EnumOrigin;
import com.pixelmonmod.pixelmon.enums.forms.EnumPichu;
import com.pixelmonmod.pixelmon.enums.forms.EnumPikachu;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.EnumRotom;
import com.pixelmonmod.pixelmon.enums.forms.EnumSandile;
import com.pixelmonmod.pixelmon.enums.forms.EnumShaymin;
import com.pixelmonmod.pixelmon.enums.forms.EnumShearable;
import com.pixelmonmod.pixelmon.enums.forms.EnumShellos;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowbro;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowking;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowpoke;
import com.pixelmonmod.pixelmon.enums.forms.EnumSnorlax;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.EnumTherian;
import com.pixelmonmod.pixelmon.enums.forms.EnumToxtricity;
import com.pixelmonmod.pixelmon.enums.forms.EnumUnown;
import com.pixelmonmod.pixelmon.enums.forms.EnumUrshifu;
import com.pixelmonmod.pixelmon.enums.forms.EnumVenusaur;
import com.pixelmonmod.pixelmon.enums.forms.EnumWishiwashi;
import com.pixelmonmod.pixelmon.enums.forms.EnumWormadam;
import com.pixelmonmod.pixelmon.enums.forms.EnumXerneas;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import com.pixelmonmod.pixelmon.enums.forms.SeasonForm;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.vecmath.Vector3d;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class PixelmonModelRegistry {
   private static final EnumMap modelRegistry = new EnumMap(EnumSpecies.class);
   private static final EnumMap formModelRegistry = new EnumMap(EnumSpecies.class);
   private static final Map modelSizes = new HashMap();
   private static final Map flyingModelRegistry = new HashMap(32);

   private static void addModel(EnumSpecies species, PixelmonSmdFactory factory) {
      modelRegistry.put(species, new PixelmonModelHolder(factory));
   }

   private static void addModel(EnumSpecies species, IEnumForm form, PixelmonSmdFactory factory) {
      if (form == EnumNoForm.NoForm) {
         modelRegistry.put(species, new PixelmonModelHolder(factory));
      } else {
         if (!formModelRegistry.containsKey(species)) {
            formModelRegistry.put(species, new HashMap());
         }

         ((Map)formModelRegistry.get(species)).put(form, new PixelmonModelHolder(factory));
         if (!modelRegistry.containsKey(species)) {
            modelRegistry.put(species, new PixelmonModelHolder(factory));
         }

      }
   }

   private static void addFlyingModel(EnumSpecies species, PixelmonSmdFactory factory) {
      addFlyingModel(species, EnumNoForm.NoForm, factory);
   }

   private static void addFlyingModel(EnumSpecies species, IEnumForm form, PixelmonSmdFactory factory) {
      if (!flyingModelRegistry.containsKey(species)) {
         flyingModelRegistry.put(species, new HashMap());
      }

      ((Map)flyingModelRegistry.get(species)).put(form, new PixelmonModelHolder(factory));
   }

   private static void init() {
      modelRegistry.clear();
      formModelRegistry.clear();
      flyingModelRegistry.clear();
      modelSizes.clear();
      addModel(EnumSpecies.MissingNo, EnumMissingNo.MissingNo, new PixelmonSmdFactory(EnumPokemonModel.MissingNo));
      addModel(EnumSpecies.Abra, (new PixelmonSmdFactory(EnumPokemonModel.Abra)).setYRotation(28.0F));
      addModel(EnumSpecies.Abomasnow, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Abomasnow)).setYRotation(24.12F));
      addModel(EnumSpecies.Abomasnow, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaAbomasnow)).setYRotation(24.2F));
      addModel(EnumSpecies.Absol, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Absol));
      addModel(EnumSpecies.Absol, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAbsol));
      addModel(EnumSpecies.Accelgor, new PixelmonSmdFactory(EnumPokemonModel.Accelgor));
      addModel(EnumSpecies.Aegislash, EnumAegislash.SHIELD, new PixelmonSmdFactory(EnumPokemonModel.AegislashShield));
      addModel(EnumSpecies.Aegislash, EnumAegislash.BLADE, new PixelmonSmdFactory(EnumPokemonModel.AegislashBlade));
      addModel(EnumSpecies.Aegislash, EnumAegislash.SHIELD_ALTER, new PixelmonSmdFactory(EnumPokemonModel.AegislashShield));
      addModel(EnumSpecies.Aegislash, EnumAegislash.BLADE_ALTER, new PixelmonSmdFactory(EnumPokemonModel.AegislashBlade));
      addModel(EnumSpecies.Aerodactyl, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Aerodactyl));
      addModel(EnumSpecies.Aerodactyl, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaAerodactyl)).setYRotation(27.0F));
      addModel(EnumSpecies.Aggron, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Aggron));
      addModel(EnumSpecies.Aggron, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAggron));
      addModel(EnumSpecies.Aipom, new PixelmonSmdFactory(EnumPokemonModel.Aipom));
      addModel(EnumSpecies.Alakazam, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Alakazam));
      addModel(EnumSpecies.Alakazam, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAlakazam));
      Iterator var0 = EnumAlcremie.getStrawberryList().iterator();

      EnumAlcremie e;
      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieStrawberry));
      }

      var0 = EnumAlcremie.getBerryList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieBerry));
      }

      var0 = EnumAlcremie.getLoveList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieLove));
      }

      var0 = EnumAlcremie.getStarList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieStar));
      }

      var0 = EnumAlcremie.getCloverList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieClover));
      }

      var0 = EnumAlcremie.getFlowerList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieFlower));
      }

      var0 = EnumAlcremie.getRibbonList().iterator();

      while(var0.hasNext()) {
         e = (EnumAlcremie)var0.next();
         addModel(EnumSpecies.Alcremie, e, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremieRibbon));
      }

      addModel(EnumSpecies.Alcremie, EnumAlcremie.PUMPKIN, new DualModelFactory(EnumPokemonModel.Alcremie, EnumPokemonModel.AlcremiePumpkin));
      addModel(EnumSpecies.Alcremie, EnumAlcremie.GMAX, new PixelmonSmdFactory(EnumPokemonModel.AlcremieGmax));
      addModel(EnumSpecies.Alomomola, new PixelmonSmdFactory(EnumPokemonModel.Alomomola));
      addModel(EnumSpecies.Altaria, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Altaria)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Altaria, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAltaria));
      addModel(EnumSpecies.Amaura, new PixelmonSmdFactory(EnumPokemonModel.Amaura));
      addModel(EnumSpecies.Ambipom, new PixelmonSmdFactory(EnumPokemonModel.Ambipom));
      addModel(EnumSpecies.Amoonguss, new PixelmonSmdFactory(EnumPokemonModel.Amoonguss));
      addModel(EnumSpecies.Ampharos, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Ampharos));
      addModel(EnumSpecies.Ampharos, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAmpharos));
      addModel(EnumSpecies.Anorith, (new PixelmonSmdFactory(EnumPokemonModel.Anorith)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Appletun, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Appletun));
      addModel(EnumSpecies.Appletun, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxAppletun));
      addModel(EnumSpecies.Araquanid, (new DualModelFactory(EnumPokemonModel.Araquanid, EnumPokemonModel.AraquanidBubble)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Arbok, (new PixelmonSmdFactory(EnumPokemonModel.Arbok)).setMovementThreshold(0.05F));
      addModel(EnumSpecies.Arcanine, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Arcanine));
      addModel(EnumSpecies.Arcanine, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.ArcanineHisuian));
      addModel(EnumSpecies.Arceus, new PixelmonSmdFactory(EnumPokemonModel.Arceus));
      addModel(EnumSpecies.Archen, new PixelmonSmdFactory(EnumPokemonModel.Archen));
      addModel(EnumSpecies.Archeops, new PixelmonSmdFactory(EnumPokemonModel.Archeops));
      addModel(EnumSpecies.Ariados, (new PixelmonSmdFactory(EnumPokemonModel.Ariados)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Armaldo, (new PixelmonSmdFactory(EnumPokemonModel.Armaldo)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Aromatisse, new PixelmonSmdFactory(EnumPokemonModel.Aromatisse));
      addModel(EnumSpecies.Aron, new PixelmonSmdFactory(EnumPokemonModel.Aron));
      addModel(EnumSpecies.Articuno, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Articuno)).setYRotation(24.14F).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Articuno, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.ArticunoFlying)).setYRotation(23.14F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Articuno, RegionalForms.GALARIAN, (new PixelmonSmdFactory(EnumPokemonModel.ArticunoGalar)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Audino, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Audino));
      addModel(EnumSpecies.Audino, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaAudino));
      addModel(EnumSpecies.Aurorus, new PixelmonSmdFactory(EnumPokemonModel.Aurorus));
      addModel(EnumSpecies.Avalugg, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Avalugg));
      addModel(EnumSpecies.Avalugg, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.AvaluggHisuian));
      addModel(EnumSpecies.Axew, new PixelmonSmdFactory(EnumPokemonModel.Axew));
      addModel(EnumSpecies.Azelf, (new PixelmonSmdFactory(EnumPokemonModel.Azelf)).setYRotation(28.0F));
      addModel(EnumSpecies.Azumarill, new PixelmonSmdFactory(EnumPokemonModel.Azumarill));
      addModel(EnumSpecies.Azurill, new PixelmonSmdFactory(EnumPokemonModel.Azurill));
      addModel(EnumSpecies.Bagon, new PixelmonSmdFactory(EnumPokemonModel.Bagon));
      addModel(EnumSpecies.Baltoy, new PixelmonSmdFactory(EnumPokemonModel.Baltoy));
      addModel(EnumSpecies.Banette, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Banette)).setZRotation(-2.0F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Banette, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaBanette));
      addModel(EnumSpecies.Barbaracle, new PixelmonSmdFactory(EnumPokemonModel.Barbaracle));
      addModel(EnumSpecies.Barboach, (new PixelmonSmdFactory(EnumPokemonModel.Barboach)).setYRotation(25.2F));
      addModel(EnumSpecies.Basculin, EnumBasculin.RED, (new PixelmonSmdFactory(EnumPokemonModel.BasculinRed)).setYRotation(22.0F));
      addModel(EnumSpecies.Basculin, EnumBasculin.BLUE, (new PixelmonSmdFactory(EnumPokemonModel.BasculinBlue)).setYRotation(22.0F));
      addModel(EnumSpecies.Basculin, EnumBasculin.WHITE, (new PixelmonSmdFactory(EnumPokemonModel.BasculinWhite)).setYRotation(22.0F));
      addModel(EnumSpecies.Bastiodon, new PixelmonSmdFactory(EnumPokemonModel.Bastiodon));
      addModel(EnumSpecies.Bayleef, new PixelmonSmdFactory(EnumPokemonModel.Bayleef));
      addModel(EnumSpecies.Beautifly, (new PixelmonSmdFactory(EnumPokemonModel.Beautifly)).setYRotation(26.8F));
      addModel(EnumSpecies.Beartic, new PixelmonSmdFactory(EnumPokemonModel.Beartic));
      addModel(EnumSpecies.Beedrill, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Beedrill)).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Beedrill, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaBeedrill));
      addModel(EnumSpecies.Beheeyem, new PixelmonSmdFactory(EnumPokemonModel.Beheeyem));
      addModel(EnumSpecies.Beldum, (new PixelmonSmdFactory(EnumPokemonModel.Beldum)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Bellsprout, new PixelmonSmdFactory(EnumPokemonModel.Bellsprout));
      addModel(EnumSpecies.Bergmite, new PixelmonSmdFactory(EnumPokemonModel.Bergmite));
      addModel(EnumSpecies.Bewear, new PixelmonSmdFactory(EnumPokemonModel.Bewear));
      addModel(EnumSpecies.Bibarel, new PixelmonSmdFactory(EnumPokemonModel.Bibarel));
      addModel(EnumSpecies.Bidoof, EnumBidoof.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Bidoof));
      addModel(EnumSpecies.Bidoof, EnumBidoof.SIRDOOFUSIII, new PixelmonSmdFactory(EnumPokemonModel.BidoofSir));
      addModel(EnumSpecies.Binacle, new PixelmonSmdFactory(EnumPokemonModel.Binacle));
      addModel(EnumSpecies.Bisharp, new PixelmonSmdFactory(EnumPokemonModel.Bisharp));
      addModel(EnumSpecies.Blacephalon, new PixelmonSmdFactory(EnumPokemonModel.Blacephalon));
      addModel(EnumSpecies.Blastoise, EnumBlastoise.Normal, new PixelmonSmdFactory(EnumPokemonModel.Blastoise));
      addModel(EnumSpecies.Blastoise, EnumBlastoise.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaBlastoise));
      addModel(EnumSpecies.Blastoise, EnumBlastoise.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxBlastoise));
      addModel(EnumSpecies.Blaziken, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Blaziken)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Blaziken, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaBlaziken));
      addModel(EnumSpecies.Blissey, new PixelmonSmdFactory(EnumPokemonModel.Blissey));
      addModel(EnumSpecies.Blitzle, new PixelmonSmdFactory(EnumPokemonModel.Blitzle));
      addModel(EnumSpecies.Boldore, new PixelmonSmdFactory(EnumPokemonModel.Boldore));
      addModel(EnumSpecies.Bonsly, new PixelmonSmdFactory(EnumPokemonModel.Bonsly));
      addModel(EnumSpecies.Bouffalant, new PixelmonSmdFactory(EnumPokemonModel.Bouffalant));
      addModel(EnumSpecies.Bounsweet, new PixelmonSmdFactory(EnumPokemonModel.Bounsweet));
      addModel(EnumSpecies.Braixen, (new PixelmonSmdFactory(EnumPokemonModel.Braixen)).setYRotation(24.1F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Braviary, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Braviary)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addFlyingModel(EnumSpecies.Braviary, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.BraviaryFlying)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Braviary, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.BraviaryHisuian));
      addFlyingModel(EnumSpecies.Braviary, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.BraviaryHisuianFlying));
      addModel(EnumSpecies.Breloom, new PixelmonSmdFactory(EnumPokemonModel.Breloom));
      addModel(EnumSpecies.Brionne, new PixelmonSmdFactory(EnumPokemonModel.Brionne));
      addModel(EnumSpecies.Bronzong, new PixelmonSmdFactory(EnumPokemonModel.Bronzong));
      addModel(EnumSpecies.Bronzor, (new PixelmonSmdFactory(EnumPokemonModel.Bronzor)).setYRotation(30.0F));
      addModel(EnumSpecies.Budew, new PixelmonSmdFactory(EnumPokemonModel.Budew));
      addModel(EnumSpecies.Buizel, new PixelmonSmdFactory(EnumPokemonModel.Buizel));
      addModel(EnumSpecies.Bulbasaur, new PixelmonSmdFactory(EnumPokemonModel.Bulbasaur));
      addModel(EnumSpecies.Buneary, new PixelmonSmdFactory(EnumPokemonModel.Buneary));
      addModel(EnumSpecies.Buneary, new PixelmonSmdFactory(EnumPokemonModel.Buneary));
      addModel(EnumSpecies.Bunnelby, (new PixelmonSmdFactory(EnumPokemonModel.Bunnelby)).setYRotation(21.0F));
      addModel(EnumSpecies.Burmy, EnumBurmy.Plant, (new PixelmonSmdFactory(EnumPokemonModel.BurmyPlant)).setRotateAngleX(0.0F).setYRotation(22.0F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Burmy, EnumBurmy.Trash, (new PixelmonSmdFactory(EnumPokemonModel.BurmyTrash)).setRotateAngleX(0.0F).setYRotation(22.0F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Burmy, EnumBurmy.Sandy, (new PixelmonSmdFactory(EnumPokemonModel.BurmySandy)).setRotateAngleX(0.0F).setYRotation(22.0F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Butterfree, EnumGigantamax.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Butterfree)).setYRotation(25.8F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Butterfree, EnumGigantamax.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxButterfree)).setYRotation(25.8F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Buzzwole, new PixelmonSmdFactory(EnumPokemonModel.Buzzwole));
      addModel(EnumSpecies.Bruxish, new PixelmonSmdFactory(EnumPokemonModel.Bruxish));
      addModel(EnumSpecies.Cacnea, (new PixelmonSmdFactory(EnumPokemonModel.Cacnea)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Cacturne, (new PixelmonSmdFactory(EnumPokemonModel.Cacturne)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Camerupt, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Camerupt));
      addModel(EnumSpecies.Camerupt, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaCamerupt));
      addModel(EnumSpecies.Carbink, new PixelmonSmdFactory(EnumPokemonModel.Carbink));
      addModel(EnumSpecies.Carnivine, new PixelmonSmdFactory(EnumPokemonModel.Carnivine));
      addModel(EnumSpecies.Carracosta, new PixelmonSmdFactory(EnumPokemonModel.Carracosta));
      addModel(EnumSpecies.Carvanha, new PixelmonSmdFactory(EnumPokemonModel.Carvanha));
      addModel(EnumSpecies.Caterpie, (new PixelmonSmdFactory(EnumPokemonModel.Caterpie)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Cascoon, (new PixelmonSmdFactory(EnumPokemonModel.Cascoon)).setYRotation(24.1F));
      addModel(EnumSpecies.Castform, EnumCastform.Normal, new PixelmonSmdFactory(EnumPokemonModel.CastForm));
      addModel(EnumSpecies.Castform, EnumCastform.Ice, (new DualModelFactory(EnumPokemonModel.CastFormIce, EnumPokemonModel.CastFormIceOrb)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Castform, EnumCastform.Rain, (new DualModelFactory(EnumPokemonModel.CastFormRain, EnumPokemonModel.CastFormRainOrb)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Castform, EnumCastform.Sun, (new DualModelFactory(EnumPokemonModel.CastFormSun, EnumPokemonModel.CastFormSunOrb)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Celebi, new PixelmonSmdFactory(EnumPokemonModel.Celebi));
      addModel(EnumSpecies.Celesteela, new PixelmonSmdFactory(EnumPokemonModel.Celesteela));
      addModel(EnumSpecies.Chandelure, (new DualModelFactory(EnumPokemonModel.Chandelure, EnumPokemonModel.ChandelureTransparent)).setModel2Transparency(0.5F));
      addModel(EnumSpecies.Chansey, new PixelmonSmdFactory(EnumPokemonModel.Chansey));
      addModel(EnumSpecies.Charjabug, new PixelmonSmdFactory(EnumPokemonModel.Charjabug));
      addModel(EnumSpecies.Charizard, EnumCharizard.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Charizard)).setYRotation(24.12F));
      addModel(EnumSpecies.Charizard, EnumCharizard.MegaX, (new PixelmonSmdFactory(EnumPokemonModel.MegaCharizardX)).setYRotation(24.12F));
      addModel(EnumSpecies.Charizard, EnumCharizard.MegaY, (new PixelmonSmdFactory(EnumPokemonModel.MegaCharizardY)).setYRotation(24.12F));
      addModel(EnumSpecies.Charizard, EnumCharizard.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxCharizard)).setYRotation(24.12F));
      addFlyingModel(EnumSpecies.Charizard, EnumCharizard.Normal, (new PixelmonSmdFactory(EnumPokemonModel.CharizardFlying)).setAnimationIncrement(2.0F));
      addFlyingModel(EnumSpecies.Charizard, EnumCharizard.MegaY, (new PixelmonSmdFactory(EnumPokemonModel.MegaCharizardYFly)).setAnimationIncrement(2.0F));
      addFlyingModel(EnumSpecies.Charizard, EnumSpecial.Zombie, (new PixelmonSmdFactory(EnumPokemonModel.CharizardFlying)).setAnimationIncrement(2.0F));
      addModel(EnumSpecies.Charmander, (new PixelmonSmdFactory(EnumPokemonModel.Charmander)).setYRotation(22.8F));
      addModel(EnumSpecies.Charmeleon, new PixelmonSmdFactory(EnumPokemonModel.Charmeleon));
      addModel(EnumSpecies.Chatot, (new PixelmonSmdFactory(EnumPokemonModel.Chatot)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addFlyingModel(EnumSpecies.Chatot, (new PixelmonSmdFactory(EnumPokemonModel.ChatotFlying)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Cherubi, new PixelmonSmdFactory(EnumPokemonModel.Cherubi));
      addModel(EnumSpecies.Cherrim, EnumCherrim.OVERCAST, new PixelmonSmdFactory(EnumPokemonModel.CherrimOvercast));
      addModel(EnumSpecies.Cherrim, EnumCherrim.SUNSHINE, new PixelmonSmdFactory(EnumPokemonModel.CherrimSunshine));
      addModel(EnumSpecies.Chesnaught, (new PixelmonSmdFactory(EnumPokemonModel.Chesnaught)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Chespin, (new PixelmonSmdFactory(EnumPokemonModel.Chespin)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Chikorita, new PixelmonSmdFactory(EnumPokemonModel.Chikorita));
      addModel(EnumSpecies.Chimchar, new PixelmonSmdFactory(EnumPokemonModel.Chimchar));
      addModel(EnumSpecies.Chimecho, (new PixelmonSmdFactory(EnumPokemonModel.Chimecho)).setYRotation(24.4F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Chinchou, new PixelmonSmdFactory(EnumPokemonModel.Chinchou));
      addModel(EnumSpecies.Chingling, (new PixelmonSmdFactory(EnumPokemonModel.Chingling)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Cinccino, new PixelmonSmdFactory(EnumPokemonModel.Cinccino));
      addModel(EnumSpecies.Cinderace, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Cinderace));
      addModel(EnumSpecies.Cinderace, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.CinderaceGmax));
      addModel(EnumSpecies.Cinderace, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.CinderaceOnline));
      addModel(EnumSpecies.Clamperl, (new PixelmonSmdFactory(EnumPokemonModel.Clamperl)).setYRotation(24.1F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Clauncher, new PixelmonSmdFactory(EnumPokemonModel.Clauncher));
      addModel(EnumSpecies.Clawitzer, new PixelmonSmdFactory(EnumPokemonModel.Clawitzer));
      addModel(EnumSpecies.Claydol, new PixelmonSmdFactory(EnumPokemonModel.Claydol));
      addModel(EnumSpecies.Clefable, new PixelmonSmdFactory(EnumPokemonModel.Clefable));
      addModel(EnumSpecies.Clefairy, new PixelmonSmdFactory(EnumPokemonModel.Clefairy));
      addModel(EnumSpecies.Cleffa, new PixelmonSmdFactory(EnumPokemonModel.Cleffa));
      addModel(EnumSpecies.Cloyster, new PixelmonSmdFactory(EnumPokemonModel.Cloyster));
      addModel(EnumSpecies.Clobbopus, new PixelmonSmdFactory(EnumPokemonModel.Clobbopus));
      addModel(EnumSpecies.Grapploct, new PixelmonSmdFactory(EnumPokemonModel.Grapploct));
      addModel(EnumSpecies.Coalossal, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxCoalossal));
      addModel(EnumSpecies.Coalossal, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Coalossal));
      addModel(EnumSpecies.Combee, (new PixelmonSmdFactory(EnumPokemonModel.Combee)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Combusken, (new PixelmonSmdFactory(EnumPokemonModel.Combusken)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Comfey, (new PixelmonSmdFactory(EnumPokemonModel.Comfey)).setYRotation(19.0F));
      addModel(EnumSpecies.Conkeldurr, new PixelmonSmdFactory(EnumPokemonModel.Conkeldurr));
      addModel(EnumSpecies.Cobalion, new PixelmonSmdFactory(EnumPokemonModel.Cobalion));
      addModel(EnumSpecies.Cofagrigus, (new DualModelFactory(EnumPokemonModel.Cofagrigus, EnumPokemonModel.CofagrigusAura)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Corphish, new PixelmonSmdFactory(EnumPokemonModel.Corphish));
      addModel(EnumSpecies.Cosmoem, (new DualModelFactory(EnumPokemonModel.Cosmoem, EnumPokemonModel.CosmoemOrb)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Cosmog, new PixelmonSmdFactory(EnumPokemonModel.Cosmog));
      addModel(EnumSpecies.Cottonee, new PixelmonSmdFactory(EnumPokemonModel.Cottonee));
      addModel(EnumSpecies.Crabominable, new PixelmonSmdFactory(EnumPokemonModel.Crabominable));
      addModel(EnumSpecies.Crabrawler, new PixelmonSmdFactory(EnumPokemonModel.Crabrawler));
      addModel(EnumSpecies.Cradily, (new PixelmonSmdFactory(EnumPokemonModel.Cradily)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Cranidos, new PixelmonSmdFactory(EnumPokemonModel.Cranidos));
      addModel(EnumSpecies.Crawdaunt, (new PixelmonSmdFactory(EnumPokemonModel.Crawdaunt)).setYRotation(19.5F));
      addModel(EnumSpecies.Cresselia, new PixelmonSmdFactory(EnumPokemonModel.Cresselia));
      addModel(EnumSpecies.Croagunk, new PixelmonSmdFactory(EnumPokemonModel.Croagunk));
      addModel(EnumSpecies.Crobat, (new PixelmonSmdFactory(EnumPokemonModel.Crobat)).setYRotation(18.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Croconaw, new PixelmonSmdFactory(EnumPokemonModel.Croconaw));
      addModel(EnumSpecies.Crustle, new PixelmonSmdFactory(EnumPokemonModel.Crustle));
      addModel(EnumSpecies.Cryogonal, new PixelmonSmdFactory(EnumPokemonModel.Cryogonal));
      addModel(EnumSpecies.Cubchoo, new PixelmonSmdFactory(EnumPokemonModel.Cubchoo));
      addModel(EnumSpecies.Cubone, (new PixelmonSmdFactory(EnumPokemonModel.Cubone)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Cutiefly, new PixelmonSmdFactory(EnumPokemonModel.Cutiefly));
      addModel(EnumSpecies.Cyndaquil, new PixelmonSmdFactory(EnumPokemonModel.Cyndaquil));
      addModel(EnumSpecies.Darkrai, (new PixelmonSmdFactory(EnumPokemonModel.Darkrai)).setYRotation(25.5F));
      addModel(EnumSpecies.Darmanitan, EnumDarmanitan.STANDARD, new PixelmonSmdFactory(EnumPokemonModel.DarmanitanStandard));
      addModel(EnumSpecies.Darmanitan, EnumDarmanitan.ZEN, (new PixelmonSmdFactory(EnumPokemonModel.DarmanitanZen)).setRotateAngleX(1.5707964F));
      addModel(EnumSpecies.Darmanitan, EnumDarmanitan.GALAR_STANDARD, new PixelmonSmdFactory(EnumPokemonModel.DarmanitanGalarStandard));
      addModel(EnumSpecies.Darmanitan, EnumDarmanitan.GALAR_ZEN, new PixelmonSmdFactory(EnumPokemonModel.DarmanitanGalarZen));
      addModel(EnumSpecies.Darumaka, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Darumaka));
      addModel(EnumSpecies.Darumaka, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.DarumakaGalar));
      addModel(EnumSpecies.Dartrix, new PixelmonSmdFactory(EnumPokemonModel.Dartrix));
      addModel(EnumSpecies.Decidueye, new PixelmonSmdFactory(EnumPokemonModel.Decidueye));
      addModel(EnumSpecies.Decidueye, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.HisuianDecidueye));
      addModel(EnumSpecies.Dedenne, new PixelmonSmdFactory(EnumPokemonModel.Dedenne));
      addModel(EnumSpecies.Deerling, new PixelmonSmdFactory(EnumPokemonModel.Deerling));
      addModel(EnumSpecies.Deino, new PixelmonSmdFactory(EnumPokemonModel.Deino));
      addModel(EnumSpecies.Delcatty, new PixelmonSmdFactory(EnumPokemonModel.Delcatty));
      addModel(EnumSpecies.Delibird, new PixelmonSmdFactory(EnumPokemonModel.Delibird));
      addModel(EnumSpecies.Delphox, (new PixelmonSmdFactory(EnumPokemonModel.Delphox)).setYRotation(24.1F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Deoxys, EnumDeoxys.Normal, (new PixelmonSmdFactory(EnumPokemonModel.DeoxysNormal)).setYRotation(24.1F));
      addModel(EnumSpecies.Deoxys, EnumDeoxys.Attack, (new PixelmonSmdFactory(EnumPokemonModel.DeoxysAttack)).setYRotation(24.1F));
      addModel(EnumSpecies.Deoxys, EnumDeoxys.Defense, (new PixelmonSmdFactory(EnumPokemonModel.DeoxysDefense)).setYRotation(24.1F));
      addModel(EnumSpecies.Deoxys, EnumDeoxys.Speed, (new PixelmonSmdFactory(EnumPokemonModel.DeoxysSpeed)).setYRotation(24.1F));
      addModel(EnumSpecies.Deoxys, EnumDeoxys.Sus, (new PixelmonSmdFactory(EnumPokemonModel.DeoxysSus)).setYRotation(24.1F));
      addModel(EnumSpecies.Dewgong, new PixelmonSmdFactory(EnumPokemonModel.Dewgong));
      addModel(EnumSpecies.Dewpider, (new DualModelFactory(EnumPokemonModel.Dewpider, EnumPokemonModel.DewpiderBubble)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Dewott, new PixelmonSmdFactory(EnumPokemonModel.Dewott));
      addModel(EnumSpecies.Dhelmise, new PixelmonSmdFactory(EnumPokemonModel.Dhelmise));
      addModel(EnumSpecies.Dialga, EnumOrigin.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Dialga));
      addModel(EnumSpecies.Dialga, EnumOrigin.ORIGIN, new PixelmonSmdFactory(EnumPokemonModel.DialgaOrigin));
      addModel(EnumSpecies.Diancie, EnumMega.Normal, (new DualModelFactory(EnumPokemonModel.Diancie, EnumPokemonModel.DiancieCrystals)).setModel2Transparency(0.5F).setYRotation(27.0F));
      addModel(EnumSpecies.Diancie, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaDiancie));
      addModel(EnumSpecies.Diggersby, new PixelmonSmdFactory(EnumPokemonModel.Diggersby));
      addModel(EnumSpecies.Diglett, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Diglett));
      addModel(EnumSpecies.Diglett, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanDiglett));
      addModel(EnumSpecies.Ditto, (new PixelmonSmdFactory(EnumPokemonModel.Ditto)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Doduo, new PixelmonSmdFactory(EnumPokemonModel.Doduo));
      addModel(EnumSpecies.Dodrio, new PixelmonSmdFactory(EnumPokemonModel.Dodrio));
      addModel(EnumSpecies.Donphan, (new PixelmonSmdFactory(EnumPokemonModel.Donphan)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Doublade, new PixelmonSmdFactory(EnumPokemonModel.Doublade));
      addModel(EnumSpecies.Dragalge, (new PixelmonSmdFactory(EnumPokemonModel.Dragalge)).setYRotation(10.0F));
      addModel(EnumSpecies.Drampa, new PixelmonSmdFactory(EnumPokemonModel.Drampa));
      addModel(EnumSpecies.Drapion, new PixelmonSmdFactory(EnumPokemonModel.Drapion));
      addModel(EnumSpecies.Dratini, (new PixelmonSmdFactory(EnumPokemonModel.Dratini)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Dragonair, (new PixelmonSmdFactory(EnumPokemonModel.Dragonair)).setZRotation(-2.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Dragonite, (new PixelmonSmdFactory(EnumPokemonModel.Dragonite)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Dragonite, EnumSpecial.Creator, new PixelmonSmdFactory(EnumPokemonModel.DragoniteCreator));
      addModel(EnumSpecies.Drednaw, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Drednaw));
      addModel(EnumSpecies.Drednaw, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.DrednawGmax));
      addModel(EnumSpecies.Drifblim, (new PixelmonSmdFactory(EnumPokemonModel.Drifblim)).setYRotation(23.0F).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Drifloon, (new PixelmonSmdFactory(EnumPokemonModel.Drifloon)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Drilbur, new PixelmonSmdFactory(EnumPokemonModel.Drilbur));
      addModel(EnumSpecies.Druddigon, new PixelmonSmdFactory(EnumPokemonModel.Druddigon));
      addModel(EnumSpecies.Ducklett, new PixelmonSmdFactory(EnumPokemonModel.Ducklett));
      addModel(EnumSpecies.Dugtrio, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Dugtrio));
      addModel(EnumSpecies.Dugtrio, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanDugtrio));
      addModel(EnumSpecies.Dunsparce, new PixelmonSmdFactory(EnumPokemonModel.Dunsparce));
      addModel(EnumSpecies.Duosion, (new DualModelFactory(EnumPokemonModel.Duosion, EnumPokemonModel.DuosionTransparent)).setModel2Transparency(0.3F));
      addModel(EnumSpecies.Durant, new PixelmonSmdFactory(EnumPokemonModel.Durant));
      addModel(EnumSpecies.Dusclops, new PixelmonSmdFactory(EnumPokemonModel.Dusclops));
      addModel(EnumSpecies.Dusknoir, new PixelmonSmdFactory(EnumPokemonModel.Dusknoir));
      addModel(EnumSpecies.Duskull, new PixelmonSmdFactory(EnumPokemonModel.Duskull));
      addModel(EnumSpecies.Dustox, (new PixelmonSmdFactory(EnumPokemonModel.Dustox)).setYRotation(27.2F));
      addModel(EnumSpecies.Eelektrik, new PixelmonSmdFactory(EnumPokemonModel.Eelektrik));
      addModel(EnumSpecies.Eelektross, new PixelmonSmdFactory(EnumPokemonModel.Eelektross));
      addModel(EnumSpecies.Eevee, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Eevee));
      addModel(EnumSpecies.Eevee, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxEevee));
      addModel(EnumSpecies.Eevee, EnumSpecial.Creator, new PixelmonSmdFactory(EnumPokemonModel.EeveeRocket));
      addModel(EnumSpecies.Ekans, (new PixelmonSmdFactory(EnumPokemonModel.Ekans)).setMovementThreshold(0.05F));
      addModel(EnumSpecies.Electabuzz, new PixelmonSmdFactory(EnumPokemonModel.Electabuzz));
      addModel(EnumSpecies.Electivire, new PixelmonSmdFactory(EnumPokemonModel.Electivire));
      addModel(EnumSpecies.Electrike, new PixelmonSmdFactory(EnumPokemonModel.Electrike));
      addModel(EnumSpecies.Electrode, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Electrode));
      addModel(EnumSpecies.Electrode, EnumSpecial.Summer, new PixelmonSmdFactory(EnumPokemonModel.Electrode));
      addModel(EnumSpecies.Electrode, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.ElectrodeHisuian));
      addModel(EnumSpecies.Elekid, new PixelmonSmdFactory(EnumPokemonModel.Elekid));
      addModel(EnumSpecies.Elgyem, new PixelmonSmdFactory(EnumPokemonModel.Elgyem));
      addModel(EnumSpecies.Emboar, new PixelmonSmdFactory(EnumPokemonModel.Emboar));
      addModel(EnumSpecies.Empoleon, new PixelmonSmdFactory(EnumPokemonModel.Empoleon));
      addModel(EnumSpecies.Entei, new PixelmonSmdFactory(EnumPokemonModel.Entei));
      addModel(EnumSpecies.Enamorus, EnumTherian.THERIAN, new PixelmonSmdFactory(EnumPokemonModel.EnamorusTherian));
      addModel(EnumSpecies.Enamorus, EnumTherian.INCARNATE, new PixelmonSmdFactory(EnumPokemonModel.EnamorusIncarnate));
      addModel(EnumSpecies.Escavalier, new PixelmonSmdFactory(EnumPokemonModel.Escavalier));
      addModel(EnumSpecies.Espeon, new PixelmonSmdFactory(EnumPokemonModel.Espeon));
      addModel(EnumSpecies.Espurr, new PixelmonSmdFactory(EnumPokemonModel.Espurr));
      addModel(EnumSpecies.Excadrill, new PixelmonSmdFactory(EnumPokemonModel.Excadrill));
      addModel(EnumSpecies.Exeggcute, (new PixelmonSmdFactory(EnumPokemonModel.Exeggcute)).setYRotation(20.0F));
      addModel(EnumSpecies.Exeggutor, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Exeggutor));
      addModel(EnumSpecies.Exeggutor, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanExeggutor));
      addModel(EnumSpecies.Exploud, new PixelmonSmdFactory(EnumPokemonModel.Exploud));
      addModel(EnumSpecies.Farfetchd, new PixelmonSmdFactory(EnumPokemonModel.Farfetchd));
      addModel(EnumSpecies.Fearow, (new PixelmonSmdFactory(EnumPokemonModel.Fearow)).setMovementThreshold(0.03F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Feebas, (new PixelmonSmdFactory(EnumPokemonModel.Feebas)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Fennekin, (new PixelmonSmdFactory(EnumPokemonModel.Fennekin)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Feraligatr, new PixelmonSmdFactory(EnumPokemonModel.Feraligatr));
      addModel(EnumSpecies.Ferroseed, (new PixelmonSmdFactory(EnumPokemonModel.Ferroseed)).setYRotation(22.0F));
      addModel(EnumSpecies.Ferrothorn, (new PixelmonSmdFactory(EnumPokemonModel.Ferrothorn)).setYRotation(22.0F));
      addModel(EnumSpecies.Finneon, new PixelmonSmdFactory(EnumPokemonModel.Finneon));
      addModel(EnumSpecies.Flaaffy, new PixelmonSmdFactory(EnumPokemonModel.Flaaffy));
      addModel(EnumSpecies.Flabebe, (new PixelmonSmdFactory(EnumPokemonModel.Flabebe)).setYRotation(60.0F).setRotateAngleX(-1.5707964F));
      addModel(EnumSpecies.Flapple, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Flapple));
      addModel(EnumSpecies.Flapple, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxFlapple));
      addModel(EnumSpecies.Flareon, new PixelmonSmdFactory(EnumPokemonModel.Flareon));
      addModel(EnumSpecies.Fletchinder, new PixelmonSmdFactory(EnumPokemonModel.Fletchinder));
      addModel(EnumSpecies.Fletchling, new PixelmonSmdFactory(EnumPokemonModel.Fletchling));
      addModel(EnumSpecies.Floatzel, new PixelmonSmdFactory(EnumPokemonModel.Floatzel));
      addModel(EnumSpecies.Floette, (new PixelmonSmdFactory(EnumPokemonModel.Floette)).setYRotation(60.0F));
      addModel(EnumSpecies.Floette, EnumFlabebe.AZ, (new PixelmonSmdFactory(EnumPokemonModel.FloetteAZ)).setYRotation(60.0F));
      addModel(EnumSpecies.Florges, new PixelmonSmdFactory(EnumPokemonModel.Florges));
      addModel(EnumSpecies.Flygon, (new PixelmonSmdFactory(EnumPokemonModel.Flygon)).setYRotation(23.5F).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addFlyingModel(EnumSpecies.Flygon, (new PixelmonSmdFactory(EnumPokemonModel.FlygonFlying)).setYRotation(27.0F).setZRotation(-8.0F).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addFlyingModel(EnumSpecies.Flygon, EnumSpecial.Drowned, (new PixelmonSmdFactory(EnumPokemonModel.FlygonFlying)).setYRotation(27.0F).setZRotation(-8.0F).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Fomantis, new PixelmonSmdFactory(EnumPokemonModel.Fomantis));
      addModel(EnumSpecies.Foongus, new PixelmonSmdFactory(EnumPokemonModel.Foongus));
      addModel(EnumSpecies.Forretress, new PixelmonSmdFactory(EnumPokemonModel.Forretress));
      addModel(EnumSpecies.Fraxure, new PixelmonSmdFactory(EnumPokemonModel.Fraxure));
      addModel(EnumSpecies.Frillish, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.FrillishMale));
      addModel(EnumSpecies.Frillish, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.FrillishFemale));
      addModel(EnumSpecies.Froakie, (new PixelmonSmdFactory(EnumPokemonModel.Froakie)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Frogadier, (new PixelmonSmdFactory(EnumPokemonModel.Frogadier)).setYRotation(24.1F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Froslass, new PixelmonSmdFactory(EnumPokemonModel.Froslass));
      addModel(EnumSpecies.Furfrou, new PixelmonSmdFactory(EnumPokemonModel.Furfrou));
      addModel(EnumSpecies.Furret, new PixelmonSmdFactory(EnumPokemonModel.Furret));
      addModel(EnumSpecies.Gabite, new PixelmonSmdFactory(EnumPokemonModel.Gabite));
      addModel(EnumSpecies.Gallade, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Gallade)).setYRotation(24.2F));
      addModel(EnumSpecies.Gallade, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaGallade)).setYRotation(24.2F));
      addModel(EnumSpecies.Gastly, new PixelmonSmdFactory(EnumPokemonModel.Gastly));
      addModel(EnumSpecies.Garbodor, new PixelmonSmdFactory(EnumPokemonModel.Garbodor));
      addModel(EnumSpecies.Garbodor, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GarbodorGmax));
      addModel(EnumSpecies.Garchomp, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Garchomp)).setYRotation(24.2F));
      addModel(EnumSpecies.Garchomp, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaGarchomp));
      addModel(EnumSpecies.Gardevoir, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Gardevoir));
      addModel(EnumSpecies.Gardevoir, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaGardevoir));
      addModel(EnumSpecies.Gastrodon, EnumGastrodon.East, (new PixelmonSmdFactory(EnumPokemonModel.GastrodonEast)).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Gastrodon, EnumGastrodon.West, (new PixelmonSmdFactory(EnumPokemonModel.GastrodonWest)).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Genesect, new PixelmonSmdFactory(EnumPokemonModel.Genesect));
      addModel(EnumSpecies.Gengar, EnumGengar.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Gengar)).setYRotation(24.12F));
      addModel(EnumSpecies.Gengar, EnumGengar.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaGengar));
      addModel(EnumSpecies.Gengar, EnumGengar.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxGengar)).setRotateAngleX(-1.5707964F));
      addModel(EnumSpecies.Geodude, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Geodude));
      addModel(EnumSpecies.Geodude, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanGeodude));
      addModel(EnumSpecies.Gible, new PixelmonSmdFactory(EnumPokemonModel.Gible));
      addModel(EnumSpecies.Gigalith, new PixelmonSmdFactory(EnumPokemonModel.Gigalith));
      addModel(EnumSpecies.Girafarig, new PixelmonSmdFactory(EnumPokemonModel.Girafarig));
      addModel(EnumSpecies.Giratina, EnumGiratina.ALTERED, (new PixelmonSmdFactory(EnumPokemonModel.GiratinaAltered)).setYRotation(24.12F));
      addModel(EnumSpecies.Giratina, EnumGiratina.ORIGIN, new PixelmonSmdFactory(EnumPokemonModel.GiratinaOrigin));
      addModel(EnumSpecies.Glaceon, new PixelmonSmdFactory(EnumPokemonModel.Glaceon));
      addModel(EnumSpecies.Glalie, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Glalie));
      addModel(EnumSpecies.Glalie, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaGlalie));
      addModel(EnumSpecies.Glameow, new PixelmonSmdFactory(EnumPokemonModel.Glameow));
      addModel(EnumSpecies.Gligar, new PixelmonSmdFactory(EnumPokemonModel.Gligar));
      addModel(EnumSpecies.Gliscor, new PixelmonSmdFactory(EnumPokemonModel.Gliscor));
      addModel(EnumSpecies.Gogoat, new PixelmonSmdFactory(EnumPokemonModel.Gogoat));
      addModel(EnumSpecies.Golbat, new PixelmonSmdFactory(EnumPokemonModel.Golbat));
      addModel(EnumSpecies.Golduck, new PixelmonSmdFactory(EnumPokemonModel.Golduck));
      addModel(EnumSpecies.Golem, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Golem));
      addModel(EnumSpecies.Golem, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanGolem));
      addModel(EnumSpecies.Golett, new PixelmonSmdFactory(EnumPokemonModel.Golett));
      addModel(EnumSpecies.Golisopod, new PixelmonSmdFactory(EnumPokemonModel.Golisopod));
      addModel(EnumSpecies.Golurk, new PixelmonSmdFactory(EnumPokemonModel.Golurk));
      addFlyingModel(EnumSpecies.Golurk, new PixelmonSmdFactory(EnumPokemonModel.GolurkFlying));
      addModel(EnumSpecies.Goodra, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Goodra));
      addModel(EnumSpecies.Goodra, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.GoodraHisuian));
      addModel(EnumSpecies.Goomy, new PixelmonSmdFactory(EnumPokemonModel.Goomy));
      addModel(EnumSpecies.Gourgeist, new PixelmonSmdFactory(EnumPokemonModel.Gourgeist));
      addModel(EnumSpecies.Gorebyss, (new PixelmonSmdFactory(EnumPokemonModel.Gorebyss)).setYRotation(25.1F).setZRotation(2.1F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Gothita, new PixelmonSmdFactory(EnumPokemonModel.Gothita));
      addModel(EnumSpecies.Gothitelle, new PixelmonSmdFactory(EnumPokemonModel.Gothitelle));
      addModel(EnumSpecies.Gothorita, new PixelmonSmdFactory(EnumPokemonModel.Gothorita));
      addModel(EnumSpecies.Granbull, (new PixelmonSmdFactory(EnumPokemonModel.Granbull)).setYRotation(21.65F).setAnimationIncrement(2.0F));
      addModel(EnumSpecies.Greninja, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Greninja));
      addModel(EnumSpecies.Greninja, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.GreninjaOnline));
      addModel(EnumSpecies.Greninja, EnumGreninja.ASH, new PixelmonSmdFactory(EnumPokemonModel.AshGreninja));
      addModel(EnumSpecies.Greninja, EnumGreninja.ASH_ZOMBIE, new PixelmonSmdFactory(EnumPokemonModel.AshGreninja));
      addModel(EnumSpecies.Greninja, EnumGreninja.ASH_ALTER, new PixelmonSmdFactory(EnumPokemonModel.AshGreninja));
      addModel(EnumSpecies.Graveler, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Graveler));
      addModel(EnumSpecies.Graveler, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanGraveler));
      addModel(EnumSpecies.Grimer, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Grimer));
      addModel(EnumSpecies.Grimer, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanGrimer));
      addModel(EnumSpecies.Grotle, new PixelmonSmdFactory(EnumPokemonModel.Grotle));
      addModel(EnumSpecies.Groudon, EnumPrimal.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Groudon));
      addModel(EnumSpecies.Groudon, EnumPrimal.PRIMAL, new PixelmonSmdFactory(EnumPokemonModel.GroudonPrimal));
      addModel(EnumSpecies.Grovyle, (new PixelmonSmdFactory(EnumPokemonModel.Grovyle)).setYRotation(21.8F));
      addModel(EnumSpecies.Growlithe, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Growlithe));
      addModel(EnumSpecies.Growlithe, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.GrowlitheHisuian));
      addModel(EnumSpecies.Grubbin, new PixelmonSmdFactory(EnumPokemonModel.Grubbin));
      addModel(EnumSpecies.Grumpig, new PixelmonSmdFactory(EnumPokemonModel.Grumpig));
      addModel(EnumSpecies.Gulpin, new PixelmonSmdFactory(EnumPokemonModel.Gulpin));
      addModel(EnumSpecies.Gumshoos, new PixelmonSmdFactory(EnumPokemonModel.Gumshoos));
      addModel(EnumSpecies.Gurdurr, new PixelmonSmdFactory(EnumPokemonModel.Gurdurr));
      addModel(EnumSpecies.Guzzlord, new PixelmonSmdFactory(EnumPokemonModel.Guzzlord));
      addModel(EnumSpecies.Gyarados, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Gyarados));
      addModel(EnumSpecies.Gyarados, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaGyarados));
      addModel(EnumSpecies.Hakamoo, new PixelmonSmdFactory(EnumPokemonModel.Hakamoo));
      addModel(EnumSpecies.Happiny, new PixelmonSmdFactory(EnumPokemonModel.Happiny));
      addModel(EnumSpecies.Hariyama, (new PixelmonSmdFactory(EnumPokemonModel.Hariyama)).setYRotation(24.1F));
      addModel(EnumSpecies.Haunter, new PixelmonSmdFactory(EnumPokemonModel.Haunter));
      addModel(EnumSpecies.Hawlucha, new PixelmonSmdFactory(EnumPokemonModel.Hawlucha));
      addModel(EnumSpecies.Hatenna, new PixelmonSmdFactory(EnumPokemonModel.Hatenna));
      addModel(EnumSpecies.Hattrem, new PixelmonSmdFactory(EnumPokemonModel.Hattrem));
      addModel(EnumSpecies.Hatterene, new PixelmonSmdFactory(EnumPokemonModel.Hatterene));
      addModel(EnumSpecies.Hatterene, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.HattereneGmax));
      addModel(EnumSpecies.Haxorus, new PixelmonSmdFactory(EnumPokemonModel.Haxorus));
      addModel(EnumSpecies.Heatmor, new PixelmonSmdFactory(EnumPokemonModel.Heatmor));
      addModel(EnumSpecies.Heatran, new PixelmonSmdFactory(EnumPokemonModel.Heatran));
      addModel(EnumSpecies.Heliolisk, new PixelmonSmdFactory(EnumPokemonModel.Heliolisk));
      addModel(EnumSpecies.Helioptile, new PixelmonSmdFactory(EnumPokemonModel.Helioptile));
      addModel(EnumSpecies.Heracross, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Heracross));
      addFlyingModel(EnumSpecies.Heracross, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.HeracrossFlying));
      addModel(EnumSpecies.Heracross, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaHeracross));
      addModel(EnumSpecies.Herdier, new PixelmonSmdFactory(EnumPokemonModel.Herdier));
      addModel(EnumSpecies.Hippopotas, new PixelmonSmdFactory(EnumPokemonModel.Hippopotas));
      addModel(EnumSpecies.Hippowdon, new PixelmonSmdFactory(EnumPokemonModel.Hippowdon));
      addModel(EnumSpecies.Hitmonlee, (new PixelmonSmdFactory(EnumPokemonModel.Hitmonlee)).setYRotation(24.1F));
      addModel(EnumSpecies.Hitmonchan, (new PixelmonSmdFactory(EnumPokemonModel.Hitmonchan)).setYRotation(24.1F));
      addModel(EnumSpecies.Hitmontop, (new PixelmonSmdFactory(EnumPokemonModel.Hitmontop)).setRotateAngleY(-3.1415927F));
      addModel(EnumSpecies.Honedge, new PixelmonSmdFactory(EnumPokemonModel.Honedge));
      addModel(EnumSpecies.Honchkrow, (new PixelmonSmdFactory(EnumPokemonModel.Honchkrow)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Honchkrow, (new PixelmonSmdFactory(EnumPokemonModel.HonchkrowFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Hooh, (new PixelmonSmdFactory(EnumPokemonModel.Hooh)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Hooh, (new PixelmonSmdFactory(EnumPokemonModel.HoohFlying)).setYRotation(15.0F));
      addFlyingModel(EnumSpecies.Hooh, EnumSpecial.Rainbow, (new PixelmonSmdFactory(EnumPokemonModel.HoohFlying)).setYRotation(15.0F));
      addFlyingModel(EnumSpecies.Hooh, EnumSpecial.Online, (new PixelmonSmdFactory(EnumPokemonModel.HoohFlying)).setYRotation(15.0F));
      addModel(EnumSpecies.Hoopa, EnumHoopa.CONFINED, (new PixelmonSmdFactory(EnumPokemonModel.Hoopa)).setYRotation(27.0F));
      addModel(EnumSpecies.Hoopa, EnumHoopa.UNBOUND, new PixelmonSmdFactory(EnumPokemonModel.HoopaUnbound));
      addModel(EnumSpecies.Hoothoot, (new PixelmonSmdFactory(EnumPokemonModel.Hoothoot)).setMovementThreshold(0.015F));
      addModel(EnumSpecies.Hoppip, (new PixelmonSmdFactory(EnumPokemonModel.Hoppip)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Horsea, new PixelmonSmdFactory(EnumPokemonModel.Horsea));
      addModel(EnumSpecies.Houndoom, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Houndoom));
      addModel(EnumSpecies.Houndoom, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaHoundoom));
      addModel(EnumSpecies.Houndour, new PixelmonSmdFactory(EnumPokemonModel.Houndour));
      addModel(EnumSpecies.Huntail, (new PixelmonSmdFactory(EnumPokemonModel.Huntail)).setYRotation(25.5F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Hydreigon, new PixelmonSmdFactory(EnumPokemonModel.Hydreigon));
      addModel(EnumSpecies.Hypno, new PixelmonSmdFactory(EnumPokemonModel.Hypno));
      addModel(EnumSpecies.Illumise, new PixelmonSmdFactory(EnumPokemonModel.Illumise));
      addModel(EnumSpecies.Incineroar, new PixelmonSmdFactory(EnumPokemonModel.Incineroar));
      addModel(EnumSpecies.Indeedee, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.IndeedeeMale));
      addModel(EnumSpecies.Indeedee, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.IndeedeeFemale));
      addModel(EnumSpecies.Infernape, new PixelmonSmdFactory(EnumPokemonModel.Infernape));
      addModel(EnumSpecies.Inkay, new PixelmonSmdFactory(EnumPokemonModel.Inkay));
      addModel(EnumSpecies.Inteleon, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxInteleon));
      addModel(EnumSpecies.Inteleon, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Inteleon));
      addModel(EnumSpecies.Ivysaur, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Ivysaur));
      addModel(EnumSpecies.Ivysaur, EnumSpecial.Halloween, new PixelmonSmdFactory(EnumPokemonModel.IvysaurHalloween));
      addModel(EnumSpecies.Jangmoo, new PixelmonSmdFactory(EnumPokemonModel.Jangmoo));
      addModel(EnumSpecies.Jellicent, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.JellicentMale));
      addModel(EnumSpecies.Jellicent, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.JellicentFemale));
      addModel(EnumSpecies.Jirachi, (new PixelmonSmdFactory(EnumPokemonModel.Jirachi)).setYRotation(25.0F));
      addModel(EnumSpecies.Jolteon, new PixelmonSmdFactory(EnumPokemonModel.Jolteon));
      addModel(EnumSpecies.Jumpluff, (new PixelmonSmdFactory(EnumPokemonModel.Jumpluff)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Jynx, new PixelmonSmdFactory(EnumPokemonModel.Jynx));
      addModel(EnumSpecies.Kabuto, new PixelmonSmdFactory(EnumPokemonModel.Kabuto));
      addModel(EnumSpecies.Kabutops, new PixelmonSmdFactory(EnumPokemonModel.Kabutops));
      addModel(EnumSpecies.Kadabra, new PixelmonSmdFactory(EnumPokemonModel.Kadabra));
      addModel(EnumSpecies.Kakuna, new PixelmonSmdFactory(EnumPokemonModel.Kakuna));
      addModel(EnumSpecies.Kangaskhan, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Kangaskhan)).setYRotation(24.11F).setRotateAngleX(0.0F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Kangaskhan, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaKangaskhan));
      addModel(EnumSpecies.Karrablast, new PixelmonSmdFactory(EnumPokemonModel.Karrablast));
      addModel(EnumSpecies.Kartana, new PixelmonSmdFactory(EnumPokemonModel.Kartana));
      addModel(EnumSpecies.Kecleon, (new PixelmonSmdFactory(EnumPokemonModel.Kecleon)).setZRotation(-0.2F));
      addModel(EnumSpecies.Keldeo, EnumKeldeo.ORDINARY, new PixelmonSmdFactory(EnumPokemonModel.KeldeoOrdinary));
      addModel(EnumSpecies.Keldeo, EnumKeldeo.RESOLUTE, new PixelmonSmdFactory(EnumPokemonModel.KeldeoResolute));
      addModel(EnumSpecies.Kingdra, new PixelmonSmdFactory(EnumPokemonModel.Kingdra));
      addModel(EnumSpecies.Kingler, new PixelmonSmdFactory(EnumPokemonModel.Kingler));
      addModel(EnumSpecies.Kingler, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.KinglerGmax));
      addModel(EnumSpecies.Kirlia, new PixelmonSmdFactory(EnumPokemonModel.Kirlia));
      addModel(EnumSpecies.Klefki, new PixelmonSmdFactory(EnumPokemonModel.Klefki));
      addModel(EnumSpecies.Klink, (new PixelmonSmdFactory(EnumPokemonModel.Klink)).setYRotation(22.0F));
      addModel(EnumSpecies.Koffing, new PixelmonSmdFactory(EnumPokemonModel.Koffing));
      addModel(EnumSpecies.Komala, new PixelmonSmdFactory(EnumPokemonModel.Komala));
      addModel(EnumSpecies.Kommoo, new PixelmonSmdFactory(EnumPokemonModel.Kommoo));
      addModel(EnumSpecies.Krabby, new PixelmonSmdFactory(EnumPokemonModel.Krabby));
      addModel(EnumSpecies.Kricketot, new PixelmonSmdFactory(EnumPokemonModel.Kricketot));
      addModel(EnumSpecies.Kricketune, new PixelmonSmdFactory(EnumPokemonModel.Kricketune));
      addModel(EnumSpecies.Krokorok, new PixelmonSmdFactory(EnumPokemonModel.Krokorok));
      addModel(EnumSpecies.Krookodile, new PixelmonSmdFactory(EnumPokemonModel.Krookodile));
      addModel(EnumSpecies.Kyogre, EnumPrimal.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Kyogre)).setYRotation(22.3F));
      addModel(EnumSpecies.Kyogre, EnumPrimal.PRIMAL, new PixelmonSmdFactory(EnumPokemonModel.KyogrePrimal));
      addModel(EnumSpecies.Kyurem, EnumKyurem.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Kyurem));
      addModel(EnumSpecies.Kyurem, EnumKyurem.BLACK, new PixelmonSmdFactory(EnumPokemonModel.KyuremBlack));
      addModel(EnumSpecies.Kyurem, EnumKyurem.WHITE, new PixelmonSmdFactory(EnumPokemonModel.KyuremWhite));
      addModel(EnumSpecies.Lairon, new PixelmonSmdFactory(EnumPokemonModel.Lairon));
      addModel(EnumSpecies.Lampent, (new DualModelFactory(EnumPokemonModel.Lampent, EnumPokemonModel.LampentTransparent)).setModel2Transparency(0.5F));
      addModel(EnumSpecies.Landorus, EnumTherian.INCARNATE, new PixelmonSmdFactory(EnumPokemonModel.LandorusIncarnate));
      addModel(EnumSpecies.Landorus, EnumTherian.THERIAN, (new PixelmonSmdFactory(EnumPokemonModel.LandorusTherian)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Lanturn, new PixelmonSmdFactory(EnumPokemonModel.Lanturn));
      addModel(EnumSpecies.Lapras, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Lapras));
      addModel(EnumSpecies.Lapras, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxLapras));
      addModel(EnumSpecies.Larvesta, new PixelmonSmdFactory(EnumPokemonModel.Larvesta));
      addModel(EnumSpecies.Larvitar, (new PixelmonSmdFactory(EnumPokemonModel.Larvitar)).setYRotation(24.3F));
      addModel(EnumSpecies.Latias, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Latias)).setYRotation(25.2F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Latias, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaLatias));
      addModel(EnumSpecies.Latios, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Latios)).setYRotation(22.7F).setZRotation(3.0F));
      addModel(EnumSpecies.Latios, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaLatios));
      addModel(EnumSpecies.Leafeon, new PixelmonSmdFactory(EnumPokemonModel.Leafeon));
      addModel(EnumSpecies.Leavanny, new PixelmonSmdFactory(EnumPokemonModel.Leavanny));
      addModel(EnumSpecies.Ledian, (new PixelmonSmdFactory(EnumPokemonModel.Ledian)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Ledian, new PixelmonSmdFactory(EnumPokemonModel.LedianFlying));
      addModel(EnumSpecies.Ledyba, (new PixelmonSmdFactory(EnumPokemonModel.Ledyba)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Ledyba, new PixelmonSmdFactory(EnumPokemonModel.LedybaFlying));
      addModel(EnumSpecies.Lickilicky, new PixelmonSmdFactory(EnumPokemonModel.Lickilicky));
      addModel(EnumSpecies.Lickitung, new PixelmonSmdFactory(EnumPokemonModel.Lickitung));
      addModel(EnumSpecies.Liepard, new PixelmonSmdFactory(EnumPokemonModel.Liepard));
      addModel(EnumSpecies.Lileep, (new PixelmonSmdFactory(EnumPokemonModel.Lileep)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Lilligant, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Lilligant));
      addModel(EnumSpecies.Lilligant, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.LilligantHisuian));
      addModel(EnumSpecies.Lillipup, new PixelmonSmdFactory(EnumPokemonModel.Lillipup));
      addModel(EnumSpecies.Litleo, (new PixelmonSmdFactory(EnumPokemonModel.Litleo)).setYRotation(24.1F));
      addModel(EnumSpecies.Litten, new PixelmonSmdFactory(EnumPokemonModel.Litten));
      addModel(EnumSpecies.Litwick, new DualModelFactory(EnumPokemonModel.Litwick, EnumPokemonModel.LitwickFlame));
      addModel(EnumSpecies.Lombre, (new PixelmonSmdFactory(EnumPokemonModel.Lombre)).setYRotation(24.1F));
      addModel(EnumSpecies.Lopunny, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Lopunny));
      addModel(EnumSpecies.Lopunny, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaLopunny));
      addModel(EnumSpecies.Lotad, (new PixelmonSmdFactory(EnumPokemonModel.Lotad)).setYRotation(24.1F));
      addModel(EnumSpecies.Loudred, new PixelmonSmdFactory(EnumPokemonModel.Loudred));
      addModel(EnumSpecies.Lucario, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Lucario)).setYRotation(24.2F));
      addModel(EnumSpecies.Lucario, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaLucario)).setYRotation(24.2F));
      addModel(EnumSpecies.Lucario, EnumSpecial.Online, (new PixelmonSmdFactory(EnumPokemonModel.OnlineLucario)).setYRotation(24.2F));
      addModel(EnumSpecies.Ludicolo, (new PixelmonSmdFactory(EnumPokemonModel.Ludicolo)).setYRotation(24.1F));
      addModel(EnumSpecies.Lugia, (new PixelmonSmdFactory(EnumPokemonModel.Lugia)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Lugia, new PixelmonSmdFactory(EnumPokemonModel.LugiaFlying));
      addFlyingModel(EnumSpecies.Lugia, EnumSpecial.Drowned, new PixelmonSmdFactory(EnumPokemonModel.LugiaFlying));
      addFlyingModel(EnumSpecies.Lugia, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.LugiaFlying));
      addModel(EnumSpecies.Lumineon, (new PixelmonSmdFactory(EnumPokemonModel.Lumineon)).setMovementThreshold(0.05F));
      addModel(EnumSpecies.Lunala, new PixelmonSmdFactory(EnumPokemonModel.Lunala));
      addModel(EnumSpecies.Lunatone, EnumLunatone.CRESCENT, new PixelmonSmdFactory(EnumPokemonModel.LunatoneCrescent));
      addModel(EnumSpecies.Lunatone, EnumLunatone.QUARTER, new PixelmonSmdFactory(EnumPokemonModel.LunatoneQuarter));
      addModel(EnumSpecies.Lunatone, EnumLunatone.GIBBOUS, new PixelmonSmdFactory(EnumPokemonModel.LunatoneGibbous));
      addModel(EnumSpecies.Lunatone, EnumLunatone.FULL, new PixelmonSmdFactory(EnumPokemonModel.LunatoneFull));
      addModel(EnumSpecies.Lunatone, EnumLunatone.NEW_MOON, new PixelmonSmdFactory(EnumPokemonModel.LunatoneNewMoon));
      addModel(EnumSpecies.Lurantis, new PixelmonSmdFactory(EnumPokemonModel.Lurantis));
      addModel(EnumSpecies.Luvdisc, new PixelmonSmdFactory(EnumPokemonModel.Luvdisc));
      addModel(EnumSpecies.Luxio, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.LuxioMale));
      addModel(EnumSpecies.Luxio, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.LuxioFemale));
      addModel(EnumSpecies.Luxray, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.LuxrayMale));
      addModel(EnumSpecies.Luxray, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.LuxrayFemale));
      addModel(EnumSpecies.Lycanroc, EnumLycanroc.MIDDAY, new PixelmonSmdFactory(EnumPokemonModel.LycanrocMidday));
      addModel(EnumSpecies.Lycanroc, EnumLycanroc.DUSK, new PixelmonSmdFactory(EnumPokemonModel.LycanrocDusk));
      addModel(EnumSpecies.Lycanroc, EnumLycanroc.MIDNIGHT, new PixelmonSmdFactory(EnumPokemonModel.LycanrocMidnight));
      addModel(EnumSpecies.Machamp, EnumGigantamax.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Machamp)).setZRotation(0.6F));
      addModel(EnumSpecies.Machamp, EnumGigantamax.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxMachamp)).setZRotation(0.6F));
      addModel(EnumSpecies.Machop, new PixelmonSmdFactory(EnumPokemonModel.Machop));
      addModel(EnumSpecies.Machoke, new PixelmonSmdFactory(EnumPokemonModel.Machoke));
      addModel(EnumSpecies.Magby, new PixelmonSmdFactory(EnumPokemonModel.Magby));
      addModel(EnumSpecies.Magcargo, new PixelmonSmdFactory(EnumPokemonModel.Magcargo));
      addModel(EnumSpecies.Magearna, new PixelmonSmdFactory(EnumPokemonModel.Magearna));
      addModel(EnumSpecies.Magikarp, (new PixelmonSmdFactory(EnumPokemonModel.Magikarp)).setYRotation(24.3F).setMovementThreshold(0.05F));
      addModel(EnumSpecies.Magmar, new PixelmonSmdFactory(EnumPokemonModel.Magmar));
      addModel(EnumSpecies.Magmortar, new PixelmonSmdFactory(EnumPokemonModel.Magmortar));
      addModel(EnumSpecies.Magnemite, new PixelmonSmdFactory(EnumPokemonModel.Magnemite));
      addModel(EnumSpecies.Magneton, new PixelmonSmdFactory(EnumPokemonModel.Magneton));
      addModel(EnumSpecies.Magnezone, new PixelmonSmdFactory(EnumPokemonModel.Magnezone));
      addModel(EnumSpecies.Makuhita, (new PixelmonSmdFactory(EnumPokemonModel.Makuhita)).setYRotation(24.1F));
      addModel(EnumSpecies.Malamar, new PixelmonSmdFactory(EnumPokemonModel.Malamar));
      addModel(EnumSpecies.Mamoswine, new PixelmonSmdFactory(EnumPokemonModel.Mamoswine));
      addModel(EnumSpecies.Manaphy, (new PixelmonSmdFactory(EnumPokemonModel.Manaphy)).setYRotation(19.0F));
      addModel(EnumSpecies.Mandibuzz, (new PixelmonSmdFactory(EnumPokemonModel.Mandibuzz)).setYRotation(10.0F));
      addModel(EnumSpecies.Manectric, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Manectric));
      addModel(EnumSpecies.Manectric, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaManectric));
      addModel(EnumSpecies.Mankey, new PixelmonSmdFactory(EnumPokemonModel.Mankey));
      addModel(EnumSpecies.Mantine, new PixelmonSmdFactory(EnumPokemonModel.Mantine));
      addModel(EnumSpecies.Mantyke, (new PixelmonSmdFactory(EnumPokemonModel.Mantyke)).setYRotation(27.0F));
      addModel(EnumSpecies.Maractus, new PixelmonSmdFactory(EnumPokemonModel.Maractus));
      addModel(EnumSpecies.Mareanie, new PixelmonSmdFactory(EnumPokemonModel.Mareanie));
      addModel(EnumSpecies.Mareep, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Mareep));
      addModel(EnumSpecies.Mareep, EnumShearable.SHORN, new PixelmonSmdFactory(EnumPokemonModel.Mareep_Shorn));
      addModel(EnumSpecies.Wooloo, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Wooloo));
      addModel(EnumSpecies.Wooloo, EnumShearable.SHORN, new PixelmonSmdFactory(EnumPokemonModel.Wooloo_Shorn));
      addModel(EnumSpecies.Dubwool, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Dubwool));
      addModel(EnumSpecies.Dubwool, EnumShearable.SHORN, new PixelmonSmdFactory(EnumPokemonModel.Dubwool_Shorn));
      addModel(EnumSpecies.Marill, new PixelmonSmdFactory(EnumPokemonModel.Marill));
      addModel(EnumSpecies.Marowak, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Marowak)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Marshadow, new PixelmonSmdFactory(EnumPokemonModel.Marshadow));
      addModel(EnumSpecies.Marowak, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanMarowak));
      addModel(EnumSpecies.Marshtomp, new PixelmonSmdFactory(EnumPokemonModel.Marshtomp));
      addModel(EnumSpecies.Masquerain, new PixelmonSmdFactory(EnumPokemonModel.Masquerain));
      addModel(EnumSpecies.Mawile, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Mawile));
      addModel(EnumSpecies.Mawile, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaMawile));
      addModel(EnumSpecies.Medicham, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Medicham)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Medicham, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaMedicham)).setMovementThreshold(0.1F));
      addModel(EnumSpecies.Meditite, (new PixelmonSmdFactory(EnumPokemonModel.Meditite)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Meganium, new PixelmonSmdFactory(EnumPokemonModel.Meganium));
      addModel(EnumSpecies.Meltan, (new PixelmonSmdFactory(EnumPokemonModel.Meltan)).setYRotation(23.95F));
      addModel(EnumSpecies.Melmetal, new PixelmonSmdFactory(EnumPokemonModel.Melmetal));
      addModel(EnumSpecies.Melmetal, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.MelmetalGmax));
      addModel(EnumSpecies.Meloetta, EnumMeloetta.ARIA, (new PixelmonSmdFactory(EnumPokemonModel.MeloettaAria)).setYRotation(40.0F));
      addModel(EnumSpecies.Meloetta, EnumMeloetta.PIROUETTE, (new PixelmonSmdFactory(EnumPokemonModel.MeloettaPirouette)).setYRotation(25.0F));
      addModel(EnumSpecies.Meowstic, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.MeowsticMale));
      addModel(EnumSpecies.Meowstic, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.MeowsticFemale));
      addModel(EnumSpecies.Mesprit, (new PixelmonSmdFactory(EnumPokemonModel.Mesprit)).setYRotation(26.0F));
      addModel(EnumSpecies.Metagross, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Metagross)).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Metagross, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaMetagross)).setMovementThreshold(0.03F));
      addFlyingModel(EnumSpecies.Metagross, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.MetagrossFlying)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Metang, (new PixelmonSmdFactory(EnumPokemonModel.Metang)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Metapod, (new PixelmonSmdFactory(EnumPokemonModel.Metapod)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Mewtwo, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Mewtwo)).setYRotation(24.2F));
      addModel(EnumSpecies.Mewtwo, EnumMega.MegaX, new PixelmonSmdFactory(EnumPokemonModel.MegaMewtwoX));
      addModel(EnumSpecies.Mewtwo, EnumMega.MegaY, new PixelmonSmdFactory(EnumPokemonModel.MegaMewtwoY));
      addModel(EnumSpecies.Mewtwo, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.SFXMewtwo));
      addModel(EnumSpecies.Mew, (new PixelmonSmdFactory(EnumPokemonModel.Mew)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Mienfoo, new PixelmonSmdFactory(EnumPokemonModel.Mienfoo));
      addModel(EnumSpecies.Mienshao, new PixelmonSmdFactory(EnumPokemonModel.Mienshao));
      addModel(EnumSpecies.Mightyena, (new PixelmonSmdFactory(EnumPokemonModel.Mightyena)).setYRotation(18.9F));
      addModel(EnumSpecies.Milotic, (new PixelmonSmdFactory(EnumPokemonModel.Milotic)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Milcery, new PixelmonSmdFactory(EnumPokemonModel.Milcery));
      addModel(EnumSpecies.Miltank, new PixelmonSmdFactory(EnumPokemonModel.Miltank));
      addModel(EnumSpecies.MimeJr, new PixelmonSmdFactory(EnumPokemonModel.MimeJr));
      addModel(EnumSpecies.Mimikyu, new PixelmonSmdFactory(EnumPokemonModel.Mimikyu));
      addModel(EnumSpecies.Minccino, new PixelmonSmdFactory(EnumPokemonModel.Minccino));
      EnumMinior[] var4 = EnumMinior.values();
      int var6 = var4.length;

      int var2;
      for(var2 = 0; var2 < var6; ++var2) {
         EnumMinior em = var4[var2];
         if (em == EnumMinior.METEOR) {
            addModel(EnumSpecies.Minior, em, (new PixelmonSmdFactory(EnumPokemonModel.MiniorMeteor)).setYRotation(12.0F).setXRotation(7.0F));
         } else {
            addModel(EnumSpecies.Minior, em, (new PixelmonSmdFactory(EnumPokemonModel.MiniorCore)).setYRotation(20.0F).setXRotation(6.0F));
         }
      }

      addModel(EnumSpecies.Misdreavus, new PixelmonSmdFactory(EnumPokemonModel.Misdreavus));
      addModel(EnumSpecies.Mismagius, new PixelmonSmdFactory(EnumPokemonModel.Mismagius));
      addModel(EnumSpecies.Moltres, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Moltres)).setYRotation(24.14F).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Moltres, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.MoltresFlying)).setYRotation(23.14F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Moltres, RegionalForms.GALARIAN, (new PixelmonSmdFactory(EnumPokemonModel.MoltresGalar)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Monferno, new PixelmonSmdFactory(EnumPokemonModel.Monferno));
      addModel(EnumSpecies.Morelull, (new PixelmonSmdFactory(EnumPokemonModel.Morelull)).setYRotation(20.0F));
      addModel(EnumSpecies.Mothim, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.Mothim)).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Mothim, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.LuckMothim));
      addModel(EnumSpecies.Mudbray, new PixelmonSmdFactory(EnumPokemonModel.Mudbray));
      addModel(EnumSpecies.Mudkip, (new PixelmonSmdFactory(EnumPokemonModel.Mudkip)).setYRotation(23.2F));
      addModel(EnumSpecies.Mudsdale, new PixelmonSmdFactory(EnumPokemonModel.Mudsdale));
      addModel(EnumSpecies.Muk, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Muk));
      addModel(EnumSpecies.Muk, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanMuk));
      addModel(EnumSpecies.Munchlax, new PixelmonSmdFactory(EnumPokemonModel.Munchlax));
      addModel(EnumSpecies.Munna, (new PixelmonSmdFactory(EnumPokemonModel.Munna)).setYRotation(17.0F));
      addModel(EnumSpecies.Murkrow, (new PixelmonSmdFactory(EnumPokemonModel.Murkrow)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Murkrow, new PixelmonSmdFactory(EnumPokemonModel.MurkrowFlying));
      addModel(EnumSpecies.Musharna, new PixelmonSmdFactory(EnumPokemonModel.Musharna));
      addModel(EnumSpecies.Natu, new PixelmonSmdFactory(EnumPokemonModel.Natu));
      addModel(EnumSpecies.Naganadel, new PixelmonSmdFactory(EnumPokemonModel.Naganadel));
      addModel(EnumSpecies.Necrozma, EnumNecrozma.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Necrozma));
      addModel(EnumSpecies.Necrozma, EnumNecrozma.DUSK, (new DualModelFactory(EnumPokemonModel.NecrozmaDusk, EnumPokemonModel.NecrozmaDuskVisor)).setModel2Transparency(0.6F));
      addModel(EnumSpecies.Necrozma, EnumNecrozma.DAWN, (new DualModelFactory(EnumPokemonModel.NecrozmaDawn, EnumPokemonModel.NecrozmaDawnVisor)).setModel2Transparency(0.6F).setYRotation(20.0F));
      addModel(EnumSpecies.Necrozma, EnumNecrozma.ULTRA, new PixelmonSmdFactory(EnumPokemonModel.NecrozmaUltra));
      addModel(EnumSpecies.Nihilego, (new DualModelFactory(EnumPokemonModel.Nihilego, EnumPokemonModel.NihilegoExtra)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Nincada, (new PixelmonSmdFactory(EnumPokemonModel.Nincada)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Nidoranfemale, new PixelmonSmdFactory(EnumPokemonModel.Nidoranfemale));
      addModel(EnumSpecies.Nidorina, new PixelmonSmdFactory(EnumPokemonModel.Nidorina));
      addModel(EnumSpecies.Nidoqueen, new PixelmonSmdFactory(EnumPokemonModel.Nidoqueen));
      addModel(EnumSpecies.Nidoranmale, new PixelmonSmdFactory(EnumPokemonModel.Nidoranmale));
      addModel(EnumSpecies.Nidorino, new PixelmonSmdFactory(EnumPokemonModel.Nidorino));
      addModel(EnumSpecies.Nidoking, new PixelmonSmdFactory(EnumPokemonModel.Nidoking));
      addModel(EnumSpecies.Ninetales, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Ninetales));
      addModel(EnumSpecies.Ninetales, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanNinetales));
      addModel(EnumSpecies.Ninjask, (new PixelmonSmdFactory(EnumPokemonModel.Ninjask)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Noctowl, (new PixelmonSmdFactory(EnumPokemonModel.Noctowl)).setMovementThreshold(0.015F));
      addFlyingModel(EnumSpecies.Noctowl, new PixelmonSmdFactory(EnumPokemonModel.NoctowlFlying));
      addModel(EnumSpecies.Noibat, (new PixelmonSmdFactory(EnumPokemonModel.Noibat)).setYRotation(80.0F));
      addModel(EnumSpecies.Noivern, new PixelmonSmdFactory(EnumPokemonModel.Noivern));
      addModel(EnumSpecies.Nosepass, (new PixelmonSmdFactory(EnumPokemonModel.Nosepass)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Numel, new PixelmonSmdFactory(EnumPokemonModel.Numel));
      addModel(EnumSpecies.Nuzleaf, (new PixelmonSmdFactory(EnumPokemonModel.Nuzleaf)).setYRotation(23.65F));
      addModel(EnumSpecies.Octillery, new PixelmonSmdFactory(EnumPokemonModel.Octillery));
      addModel(EnumSpecies.Omanyte, new PixelmonSmdFactory(EnumPokemonModel.Omanyte));
      addModel(EnumSpecies.Omanyte, EnumSpecial.Summer, new PixelmonSmdFactory(EnumPokemonModel.OmanyteSummer));
      addModel(EnumSpecies.Omastar, new PixelmonSmdFactory(EnumPokemonModel.Omastar));
      addModel(EnumSpecies.Onix, new PixelmonSmdFactory(EnumPokemonModel.Onix));
      addModel(EnumSpecies.Oranguru, new PixelmonSmdFactory(EnumPokemonModel.Oranguru));
      addModel(EnumSpecies.Orbeetle, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Orbeetle));
      addModel(EnumSpecies.Orbeetle, EnumGigantamax.Gigantamax, (new DualModelFactory(EnumPokemonModel.GigantamaxOrbeetle, EnumPokemonModel.GigantamaxOrbeetleTransparent)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Oricorio, EnumOricorio.BAILE, (new PixelmonSmdFactory(EnumPokemonModel.OricorioBaile)).setYRotation(1.0F));
      addModel(EnumSpecies.Oricorio, EnumOricorio.SENSU, (new PixelmonSmdFactory(EnumPokemonModel.OricorioSensu)).setYRotation(1.0F));
      addModel(EnumSpecies.Oricorio, EnumOricorio.PAU, (new PixelmonSmdFactory(EnumPokemonModel.OricorioPau)).setYRotation(1.0F));
      addModel(EnumSpecies.Oricorio, EnumOricorio.POMPOM, (new PixelmonSmdFactory(EnumPokemonModel.OricorioPompom)).setYRotation(1.0F));
      addModel(EnumSpecies.Oshawott, new PixelmonSmdFactory(EnumPokemonModel.Oshawott));
      addModel(EnumSpecies.Palkia, EnumOrigin.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Palkia));
      addModel(EnumSpecies.Palkia, EnumOrigin.ORIGIN, new PixelmonSmdFactory(EnumPokemonModel.PalkiaOrigin));
      addModel(EnumSpecies.Palossand, new PixelmonSmdFactory(EnumPokemonModel.Palossand));
      addModel(EnumSpecies.Palpitoad, new PixelmonSmdFactory(EnumPokemonModel.Palpitoad));
      addModel(EnumSpecies.Pancham, new PixelmonSmdFactory(EnumPokemonModel.Pancham));
      addModel(EnumSpecies.Pangoro, new PixelmonSmdFactory(EnumPokemonModel.Pangoro));
      addModel(EnumSpecies.Panpour, new PixelmonSmdFactory(EnumPokemonModel.Panpour));
      addModel(EnumSpecies.Pansage, new PixelmonSmdFactory(EnumPokemonModel.Pansage));
      addModel(EnumSpecies.Pansear, new PixelmonSmdFactory(EnumPokemonModel.Pansear));
      addModel(EnumSpecies.Patrat, new PixelmonSmdFactory(EnumPokemonModel.Patrat));
      addModel(EnumSpecies.Passimian, new PixelmonSmdFactory(EnumPokemonModel.Passimian));
      addModel(EnumSpecies.Pawniard, new PixelmonSmdFactory(EnumPokemonModel.Pawniard));
      addModel(EnumSpecies.Pelipper, new PixelmonSmdFactory(EnumPokemonModel.Pelipper));
      addModel(EnumSpecies.Petilil, (new PixelmonSmdFactory(EnumPokemonModel.Petilil)).setRotateAngleY(1.5707964F));
      addModel(EnumSpecies.Persian, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Persian));
      addModel(EnumSpecies.Persian, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanPersian));
      addModel(EnumSpecies.Phanpy, new PixelmonSmdFactory(EnumPokemonModel.Phanpy));
      addModel(EnumSpecies.Phantump, new PixelmonSmdFactory(EnumPokemonModel.Phantump));
      addModel(EnumSpecies.Pheromosa, new PixelmonSmdFactory(EnumPokemonModel.Pheromosa));
      addModel(EnumSpecies.Phione, (new PixelmonSmdFactory(EnumPokemonModel.Phione)).setYRotation(19.0F));
      addModel(EnumSpecies.Pichu, new PixelmonSmdFactory(EnumPokemonModel.Pichu));
      addModel(EnumSpecies.Pichu, EnumPichu.SPIKY, new PixelmonSmdFactory(EnumPokemonModel.PichuSpiky));
      addModel(EnumSpecies.Pikachu, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Pikachu));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Cosplay, new PixelmonSmdFactory(EnumPokemonModel.PikachuCosplay));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Libre, new PixelmonSmdFactory(EnumPokemonModel.PikachuCosplay));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Phd, new PixelmonSmdFactory(EnumPokemonModel.PikachuPhd));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Popstar, new PixelmonSmdFactory(EnumPokemonModel.PikachuPopstar));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Rockstar, new PixelmonSmdFactory(EnumPokemonModel.PikachuRockstar));
      addModel(EnumSpecies.Pikachu, EnumPikachu.Belle, new PixelmonSmdFactory(EnumPokemonModel.PikachuBelle));
      addModel(EnumSpecies.Pikachu, EnumGigantamax.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxPikachu)).setRotateAngleX(3.1415927F).setRotateAngleY(6.2831855F));
      addModel(EnumSpecies.Pidove, new PixelmonSmdFactory(EnumPokemonModel.Pidove));
      addModel(EnumSpecies.Pidgey, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.Pidgey)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pidgey, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.Pidgey)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Pidgey, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.PidgeyFlying)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Pidgey, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.PidgeyFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pidgeotto, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.Pidgeotto)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pidgeotto, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.Pidgeotto)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Pidgeotto, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.PidgeottoFlying)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addFlyingModel(EnumSpecies.Pidgeotto, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.PidgeottoFlying)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Pidgeot, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Pidgeot)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pidgeot, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.Pidgeot)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pidgeot, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaPidgeot)).setYRotation(41.0F));
      addFlyingModel(EnumSpecies.Pidgeot, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.PidgeotFlying)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Pidgeot, EnumSpecial.Pink, (new PixelmonSmdFactory(EnumPokemonModel.PidgeotFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Pignite, new PixelmonSmdFactory(EnumPokemonModel.Pignite));
      addModel(EnumSpecies.Pikipek, (new PixelmonSmdFactory(EnumPokemonModel.Pikipek)).setYRotation(9.0F));
      addModel(EnumSpecies.Piloswine, new PixelmonSmdFactory(EnumPokemonModel.Piloswine));
      addModel(EnumSpecies.Pincurchin, new PixelmonSmdFactory(EnumPokemonModel.Pincurchin));
      addModel(EnumSpecies.Pineco, new PixelmonSmdFactory(EnumPokemonModel.Pineco));
      addModel(EnumSpecies.Pinsir, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Pinsir));
      addModel(EnumSpecies.Pinsir, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaPinsir)).setYRotation(22.0F));
      addModel(EnumSpecies.Piplup, new PixelmonSmdFactory(EnumPokemonModel.Piplup));
      addModel(EnumSpecies.Politoed, new PixelmonSmdFactory(EnumPokemonModel.Politoed));
      addModel(EnumSpecies.Poliwag, new PixelmonSmdFactory(EnumPokemonModel.Poliwag));
      addModel(EnumSpecies.Poliwhirl, new PixelmonSmdFactory(EnumPokemonModel.Poliwhirl));
      addModel(EnumSpecies.Poliwrath, new PixelmonSmdFactory(EnumPokemonModel.Poliwrath));
      addModel(EnumSpecies.Poipole, new PixelmonSmdFactory(EnumPokemonModel.Poipole));
      addModel(EnumSpecies.Ponyta, new PixelmonSmdFactory(EnumPokemonModel.Ponyta));
      addModel(EnumSpecies.Popplio, new PixelmonSmdFactory(EnumPokemonModel.Popplio));
      addModel(EnumSpecies.Poochyena, new PixelmonSmdFactory(EnumPokemonModel.Poochyena));
      addModel(EnumSpecies.Porygon, (new PixelmonSmdFactory(EnumPokemonModel.Porygon)).setYRotation(25.8F));
      addModel(EnumSpecies.Porygon2, new PixelmonSmdFactory(EnumPokemonModel.Porygon2));
      addModel(EnumSpecies.PorygonZ, EnumNoForm.NoForm, (new PixelmonSmdFactory(EnumPokemonModel.PorygonZ)).setYRotation(24.5F));
      addModel(EnumSpecies.PorygonZ, EnumSpecial.Online, (new PixelmonSmdFactory(EnumPokemonModel.PorygonZOnline)).setYRotation(24.5F));
      addModel(EnumSpecies.Primeape, new PixelmonSmdFactory(EnumPokemonModel.Primeape));
      addModel(EnumSpecies.Primarina, new PixelmonSmdFactory(EnumPokemonModel.Primarina));
      addModel(EnumSpecies.Prinplup, new PixelmonSmdFactory(EnumPokemonModel.Prinplup));
      addModel(EnumSpecies.Probopass, (new PixelmonSmdFactory(EnumPokemonModel.Probopass)).setYRotation(24.2F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Psyduck, new PixelmonSmdFactory(EnumPokemonModel.Psyduck));
      addModel(EnumSpecies.Pumpkaboo, new PixelmonSmdFactory(EnumPokemonModel.Pumpkaboo));
      addModel(EnumSpecies.Pupitar, (new PixelmonSmdFactory(EnumPokemonModel.Pupitar)).setYRotation(24.3F));
      addModel(EnumSpecies.Purrloin, new PixelmonSmdFactory(EnumPokemonModel.Purrloin));
      addModel(EnumSpecies.Purugly, new PixelmonSmdFactory(EnumPokemonModel.Purugly));
      addModel(EnumSpecies.Pyroar, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.PyroarMale));
      addModel(EnumSpecies.Pyroar, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.PyroarFemale));
      addModel(EnumSpecies.Pyukumuku, new PixelmonSmdFactory(EnumPokemonModel.Pyukumuku));
      addModel(EnumSpecies.Quagsire, new PixelmonSmdFactory(EnumPokemonModel.Quagsire));
      addModel(EnumSpecies.Quilava, new PixelmonSmdFactory(EnumPokemonModel.Quilava));
      addModel(EnumSpecies.Quilladin, (new PixelmonSmdFactory(EnumPokemonModel.Quilladin)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Qwilfish, new PixelmonSmdFactory(EnumPokemonModel.Qwilfish));
      addModel(EnumSpecies.Qwilfish, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.HisuianQwilfish));
      addModel(EnumSpecies.Raichu, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Raichu));
      addModel(EnumSpecies.Raichu, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanRaichu));
      addModel(EnumSpecies.Raichu, EnumSpecial.Summer, new PixelmonSmdFactory(EnumPokemonModel.AlolanRaichu));
      addModel(EnumSpecies.Raikou, new PixelmonSmdFactory(EnumPokemonModel.Raikou));
      addModel(EnumSpecies.Ralts, new PixelmonSmdFactory(EnumPokemonModel.Ralts));
      addModel(EnumSpecies.Rampardos, new PixelmonSmdFactory(EnumPokemonModel.Rampardos));
      addModel(EnumSpecies.Rapidash, new PixelmonSmdFactory(EnumPokemonModel.Rapidash));
      addModel(EnumSpecies.Rattata, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Rattata));
      addModel(EnumSpecies.Rattata, RegionalForms.ALOLAN, (new PixelmonSmdFactory(EnumPokemonModel.AlolanRattata)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Raticate, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Raticate));
      addModel(EnumSpecies.Raticate, RegionalForms.ALOLAN, (new PixelmonSmdFactory(EnumPokemonModel.AlolanRaticate)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Rayquaza, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Rayquaza)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Rayquaza, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaRayquaza)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Regice, (new PixelmonSmdFactory(EnumPokemonModel.Regice)).setMovementThreshold(0.03F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Regigigas, (new PixelmonSmdFactory(EnumPokemonModel.Regigigas)).setMovementThreshold(0.03F).setAnimationIncrement(1.5F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Regirock, (new PixelmonSmdFactory(EnumPokemonModel.Regirock)).setYRotation(24.1F).setMovementThreshold(0.03F).setAnimationIncrement(1.5F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Registeel, (new PixelmonSmdFactory(EnumPokemonModel.Registeel)).setMovementThreshold(0.03F).setAnimationIncrement(1.5F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Relicanth, (new PixelmonSmdFactory(EnumPokemonModel.Relicanth)).setYRotation(24.3F));
      addModel(EnumSpecies.Remoraid, new PixelmonSmdFactory(EnumPokemonModel.Remoraid));
      addModel(EnumSpecies.Reshiram, new PixelmonSmdFactory(EnumPokemonModel.Reshiram));
      addModel(EnumSpecies.Reuniclus, (new DualModelFactory(EnumPokemonModel.Reuniclus, EnumPokemonModel.ReuniclusTransparent)).setModel2Transparency(0.3F));
      addModel(EnumSpecies.Rhydon, new PixelmonSmdFactory(EnumPokemonModel.Rhydon));
      addModel(EnumSpecies.Rhyhorn, new PixelmonSmdFactory(EnumPokemonModel.Rhyhorn));
      addModel(EnumSpecies.Rhyperior, new PixelmonSmdFactory(EnumPokemonModel.Rhyperior));
      addModel(EnumSpecies.Ribombee, new PixelmonSmdFactory(EnumPokemonModel.Ribombee));
      addModel(EnumSpecies.Riolu, new PixelmonSmdFactory(EnumPokemonModel.Riolu));
      addModel(EnumSpecies.Rillaboom, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Rillaboom));
      addModel(EnumSpecies.Rillaboom, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.RillaboomGmax));
      addModel(EnumSpecies.Rockruff, new PixelmonSmdFactory(EnumPokemonModel.Rockruff));
      addModel(EnumSpecies.Roggenrola, new PixelmonSmdFactory(EnumPokemonModel.Roggenrola));
      addModel(EnumSpecies.Roselia, new PixelmonSmdFactory(EnumPokemonModel.Roselia));
      addModel(EnumSpecies.Roserade, new PixelmonSmdFactory(EnumPokemonModel.Roserade));
      addModel(EnumSpecies.Rotom, EnumRotom.NORMAL, (new DualModelFactory(EnumPokemonModel.Rotom, EnumPokemonModel.RotomSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rotom, EnumRotom.HEAT, (new DualModelFactory(EnumPokemonModel.RotomHeat, EnumPokemonModel.RotomHeatSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rotom, EnumRotom.WASH, (new DualModelFactory(EnumPokemonModel.RotomWash, EnumPokemonModel.RotomWashSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rotom, EnumRotom.FROST, (new DualModelFactory(EnumPokemonModel.RotomFrost, EnumPokemonModel.RotomFrostSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rotom, EnumRotom.FAN, (new DualModelFactory(EnumPokemonModel.RotomFan, EnumPokemonModel.RotomFanSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rotom, EnumRotom.MOW, (new DualModelFactory(EnumPokemonModel.RotomMow, EnumPokemonModel.RotomMowSpectre)).setModel2Transparency(0.8F));
      addModel(EnumSpecies.Rowlet, new PixelmonSmdFactory(EnumPokemonModel.Rowlet));
      addModel(EnumSpecies.Rufflet, (new PixelmonSmdFactory(EnumPokemonModel.Rufflet)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addFlyingModel(EnumSpecies.Rufflet, (new PixelmonSmdFactory(EnumPokemonModel.RuffletFlying)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Sableye, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Sableye));
      addModel(EnumSpecies.Sableye, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSableye));
      addModel(EnumSpecies.Salamence, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Salamence));
      addModel(EnumSpecies.Salamence, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSalamence));
      addModel(EnumSpecies.Salandit, new PixelmonSmdFactory(EnumPokemonModel.Salandit));
      addModel(EnumSpecies.Salazzle, new PixelmonSmdFactory(EnumPokemonModel.Salazzle));
      addModel(EnumSpecies.Samurott, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Samurott));
      addModel(EnumSpecies.Samurott, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.SamurottHisuian));
      addModel(EnumSpecies.Sandygast, new PixelmonSmdFactory(EnumPokemonModel.Sandygast));
      addModel(EnumSpecies.Sandaconda, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Sandaconda));
      addModel(EnumSpecies.Sandaconda, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.SandacondaGmax));
      addModel(EnumSpecies.Sandshrew, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Sandshrew)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Sandshrew, RegionalForms.ALOLAN, (new PixelmonSmdFactory(EnumPokemonModel.AlolanSandshrew)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Sandslash, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Sandslash)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Sandslash, RegionalForms.ALOLAN, (new PixelmonSmdFactory(EnumPokemonModel.AlolanSandslash)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Sandile, EnumSandile.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Sandile));
      addModel(EnumSpecies.Sandile, EnumSandile.BLOCKY, new PixelmonSmdFactory(EnumPokemonModel.SandileBlocky));
      addModel(EnumSpecies.Sawk, new PixelmonSmdFactory(EnumPokemonModel.Sawk));
      addModel(EnumSpecies.Sawsbuck, SeasonForm.Spring, (new PixelmonSmdFactory(EnumPokemonModel.SawsbuckSpring)).setYRotation(49.0F));
      addModel(EnumSpecies.Sawsbuck, SeasonForm.Summer, (new PixelmonSmdFactory(EnumPokemonModel.SawsbuckSummer)).setYRotation(49.0F));
      addModel(EnumSpecies.Sawsbuck, SeasonForm.Autumn, (new PixelmonSmdFactory(EnumPokemonModel.SawsbuckAutumn)).setYRotation(49.0F));
      addModel(EnumSpecies.Sawsbuck, SeasonForm.Winter, (new PixelmonSmdFactory(EnumPokemonModel.SawsbuckWinter)).setYRotation(49.0F));
      addModel(EnumSpecies.Scrafty, new PixelmonSmdFactory(EnumPokemonModel.Scrafty));
      addModel(EnumSpecies.Scraggy, new PixelmonSmdFactory(EnumPokemonModel.Scraggy));
      addModel(EnumSpecies.Scatterbug, new PixelmonSmdFactory(EnumPokemonModel.Scatterbug));
      addModel(EnumSpecies.Sceptile, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Sceptile)).setYRotation(22.5F));
      addModel(EnumSpecies.Sceptile, EnumMega.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaSceptile)).setYRotation(24.2F));
      addModel(EnumSpecies.Scizor, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Scizor));
      addModel(EnumSpecies.Scizor, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaScizor));
      addModel(EnumSpecies.Scolipede, new PixelmonSmdFactory(EnumPokemonModel.Scolipede));
      addModel(EnumSpecies.Scyther, (new PixelmonSmdFactory(EnumPokemonModel.Scyther)).setYRotation(22.0F));
      addModel(EnumSpecies.Seadra, new PixelmonSmdFactory(EnumPokemonModel.Seadra));
      addModel(EnumSpecies.Sealeo, new PixelmonSmdFactory(EnumPokemonModel.Sealeo));
      addModel(EnumSpecies.Seedot, new PixelmonSmdFactory(EnumPokemonModel.Seedot));
      addModel(EnumSpecies.Seel, new PixelmonSmdFactory(EnumPokemonModel.Seel));
      addModel(EnumSpecies.Seismitoad, new PixelmonSmdFactory(EnumPokemonModel.Seismitoad));
      addModel(EnumSpecies.Sentret, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Sentret));
      addModel(EnumSpecies.Sentret, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.SentretOnline));
      addModel(EnumSpecies.Serperior, new PixelmonSmdFactory(EnumPokemonModel.Serperior));
      addModel(EnumSpecies.Servine, new PixelmonSmdFactory(EnumPokemonModel.Servine));
      addModel(EnumSpecies.Seviper, (new PixelmonSmdFactory(EnumPokemonModel.Seviper)).setYRotation(24.1F));
      addModel(EnumSpecies.Sewaddle, new PixelmonSmdFactory(EnumPokemonModel.Sewaddle));
      addModel(EnumSpecies.Sharpedo, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Sharpedo));
      addModel(EnumSpecies.Sharpedo, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSharpedo));
      addModel(EnumSpecies.Shaymin, EnumShaymin.LAND, new PixelmonSmdFactory(EnumPokemonModel.ShayminLand));
      addModel(EnumSpecies.Shaymin, EnumShaymin.SKY, new PixelmonSmdFactory(EnumPokemonModel.ShayminSky));
      addModel(EnumSpecies.Shedinja, (new PixelmonSmdFactory(EnumPokemonModel.Shedinja)).setMovementThreshold(0.02F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Shelgon, new PixelmonSmdFactory(EnumPokemonModel.Shelgon));
      addModel(EnumSpecies.Shellder, new PixelmonSmdFactory(EnumPokemonModel.Shellder));

      for(int i = 0; i < EnumShellos.values().length; ++i) {
         EnumPokemonModel model = i % 2 == 0 ? EnumPokemonModel.ShellosEast : EnumPokemonModel.ShellosWest;
         addModel(EnumSpecies.Shellos, EnumShellos.getFromIndex(i), (new PixelmonSmdFactory(model)).setAnimationIncrement(1.5F));
      }

      addModel(EnumSpecies.Shelmet, new PixelmonSmdFactory(EnumPokemonModel.Shelmet));
      addModel(EnumSpecies.Shieldon, new PixelmonSmdFactory(EnumPokemonModel.Shieldon));
      addModel(EnumSpecies.Shiftry, new PixelmonSmdFactory(EnumPokemonModel.Shiftry));
      addModel(EnumSpecies.Shiinotic, new PixelmonSmdFactory(EnumPokemonModel.Shiinotic));
      addModel(EnumSpecies.Shinx, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.ShinxMale));
      addModel(EnumSpecies.Shinx, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.ShinxFemale));
      addModel(EnumSpecies.Shroomish, new PixelmonSmdFactory(EnumPokemonModel.Shroomish));
      addModel(EnumSpecies.Shuckle, new PixelmonSmdFactory(EnumPokemonModel.Shuckle));
      addModel(EnumSpecies.Shuppet, (new PixelmonSmdFactory(EnumPokemonModel.Shuppet)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Sigilyph, new PixelmonSmdFactory(EnumPokemonModel.Sigilyph));
      addModel(EnumSpecies.Silcoon, (new PixelmonSmdFactory(EnumPokemonModel.Silcoon)).setYRotation(24.1F));
      addModel(EnumSpecies.Silvally, new PixelmonSmdFactory(EnumPokemonModel.Silvally));
      addModel(EnumSpecies.Simipour, new PixelmonSmdFactory(EnumPokemonModel.Simipour));
      addModel(EnumSpecies.Simisage, new PixelmonSmdFactory(EnumPokemonModel.Simisage));
      addModel(EnumSpecies.Simisear, new PixelmonSmdFactory(EnumPokemonModel.Simisear));
      addModel(EnumSpecies.Skarmory, new PixelmonSmdFactory(EnumPokemonModel.Skarmory));
      addModel(EnumSpecies.Skiddo, new PixelmonSmdFactory(EnumPokemonModel.Skiddo));
      addModel(EnumSpecies.Skiploom, (new PixelmonSmdFactory(EnumPokemonModel.Skiploom)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Skitty, new PixelmonSmdFactory(EnumPokemonModel.Skitty));
      addModel(EnumSpecies.Skorupi, new PixelmonSmdFactory(EnumPokemonModel.Skorupi));
      addModel(EnumSpecies.Skrelp, (new PixelmonSmdFactory(EnumPokemonModel.Skrelp)).setYRotation(10.0F));
      addModel(EnumSpecies.Skuntank, new PixelmonSmdFactory(EnumPokemonModel.Skuntank));
      addModel(EnumSpecies.Slaking, (new PixelmonSmdFactory(EnumPokemonModel.Slaking)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Slakoth, (new PixelmonSmdFactory(EnumPokemonModel.Slakoth)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Sliggoo, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Sliggoo));
      addModel(EnumSpecies.Sliggoo, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.SliggooHisuian));
      addModel(EnumSpecies.Slowpoke, EnumSlowpoke.Normal, new PixelmonSmdFactory(EnumPokemonModel.Slowpoke));
      addModel(EnumSpecies.Slowpoke, EnumSlowpoke.Galarian, new PixelmonSmdFactory(EnumPokemonModel.SlowpokeGalar));
      addModel(EnumSpecies.Slowbro, EnumSlowbro.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Slowbro)).setYRotation(24.12F));
      addModel(EnumSpecies.Slowbro, EnumSlowbro.Galarian, new PixelmonSmdFactory(EnumPokemonModel.SlowbroGalar));
      addModel(EnumSpecies.Slowbro, EnumSlowbro.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSlowbro));
      addModel(EnumSpecies.Slowking, EnumSlowking.Normal, new PixelmonSmdFactory(EnumPokemonModel.Slowking));
      addModel(EnumSpecies.Slowking, EnumSlowking.Galarian, new PixelmonSmdFactory(EnumPokemonModel.SlowkingGalar));
      addModel(EnumSpecies.Slugma, new PixelmonSmdFactory(EnumPokemonModel.Slugma));
      addModel(EnumSpecies.Slurpuff, (new PixelmonSmdFactory(EnumPokemonModel.Slurpuff)).setYRotation(34.0F));
      addModel(EnumSpecies.Smeargle, new PixelmonSmdFactory(EnumPokemonModel.Smeargle));
      addModel(EnumSpecies.Smoochum, new PixelmonSmdFactory(EnumPokemonModel.Smoochum));
      addModel(EnumSpecies.Sneasel, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Sneasel));
      addModel(EnumSpecies.Sneasel, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.SneaselHisuian));
      addModel(EnumSpecies.Snivy, new PixelmonSmdFactory(EnumPokemonModel.Snivy));
      addModel(EnumSpecies.Snorlax, new PixelmonSmdFactory(EnumPokemonModel.Snorlax));
      addModel(EnumSpecies.Snorlax, EnumSnorlax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.SnorlaxGmax));
      addModel(EnumSpecies.Snorunt, new PixelmonSmdFactory(EnumPokemonModel.Snorunt));
      addModel(EnumSpecies.Snover, (new PixelmonSmdFactory(EnumPokemonModel.Snover)).setYRotation(24.1F));
      addModel(EnumSpecies.Snubbull, (new PixelmonSmdFactory(EnumPokemonModel.Snubbull)).setAnimationIncrement(2.0F));
      addModel(EnumSpecies.Solgaleo, new PixelmonSmdFactory(EnumPokemonModel.Solgaleo));
      addModel(EnumSpecies.Solosis, (new DualModelFactory(EnumPokemonModel.Solosis, EnumPokemonModel.SolosisTransparent)).setModel2Transparency(0.3F));
      addModel(EnumSpecies.Solrock, new PixelmonSmdFactory(EnumPokemonModel.Solrock));
      addModel(EnumSpecies.Spearow, (new PixelmonSmdFactory(EnumPokemonModel.Spearow)).setMovementThreshold(0.03F).setAnimationIncrement(1.5F));
      addFlyingModel(EnumSpecies.Spearow, (new PixelmonSmdFactory(EnumPokemonModel.SpearowFlying)).setMovementThreshold(0.02F).setAnimationIncrement(2.0F));
      addModel(EnumSpecies.Spewpa, new PixelmonSmdFactory(EnumPokemonModel.Spewpa));
      addModel(EnumSpecies.Spheal, new PixelmonSmdFactory(EnumPokemonModel.Spheal));
      addModel(EnumSpecies.Spheal, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.SphealOnline));
      addModel(EnumSpecies.Spinarak, (new PixelmonSmdFactory(EnumPokemonModel.Spinarak)).setMovementThreshold(0.02F).setAnimationIncrement(2.0F));
      addModel(EnumSpecies.Spinda, new PixelmonSmdFactory(EnumPokemonModel.Spinda));
      addModel(EnumSpecies.Spiritomb, (new DualModelFactory(EnumPokemonModel.Spiritomb, EnumPokemonModel.SpiritombTransparent)).setModel2Transparency(0.3F));
      addModel(EnumSpecies.Spoink, new PixelmonSmdFactory(EnumPokemonModel.Spoink));
      addModel(EnumSpecies.Spritzee, new PixelmonSmdFactory(EnumPokemonModel.Spritzee));
      addModel(EnumSpecies.Squirtle, (new PixelmonSmdFactory(EnumPokemonModel.Squirtle)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Stakataka, new PixelmonSmdFactory(EnumPokemonModel.Stakataka));
      addModel(EnumSpecies.Stantler, new PixelmonSmdFactory(EnumPokemonModel.Stantler));
      addModel(EnumSpecies.Staraptor, (new PixelmonSmdFactory(EnumPokemonModel.Staraptor)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Staraptor, (new PixelmonSmdFactory(EnumPokemonModel.StaraptorFlying)).setYRotation(29.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Staravia, (new PixelmonSmdFactory(EnumPokemonModel.Staravia)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Staravia, (new PixelmonSmdFactory(EnumPokemonModel.StaraviaFlying)).setYRotation(28.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Starly, (new PixelmonSmdFactory(EnumPokemonModel.Starly)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Starly, (new PixelmonSmdFactory(EnumPokemonModel.StarlyFlying)).setYRotation(28.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Staryu, new PixelmonSmdFactory(EnumPokemonModel.Staryu));
      addModel(EnumSpecies.Starmie, new PixelmonSmdFactory(EnumPokemonModel.Starmie));
      addModel(EnumSpecies.Steelix, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Steelix));
      addModel(EnumSpecies.Steelix, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSteelix));
      addModel(EnumSpecies.Steenee, new PixelmonSmdFactory(EnumPokemonModel.Steenee));
      addModel(EnumSpecies.Stoutland, new PixelmonSmdFactory(EnumPokemonModel.Stoutland));
      addModel(EnumSpecies.Stufful, new PixelmonSmdFactory(EnumPokemonModel.Stufful));
      addModel(EnumSpecies.Stunky, new PixelmonSmdFactory(EnumPokemonModel.Stunky));
      addModel(EnumSpecies.Sudowoodo, new PixelmonSmdFactory(EnumPokemonModel.Sudowoodo));
      addModel(EnumSpecies.Suicune, new PixelmonSmdFactory(EnumPokemonModel.Suicune));
      addModel(EnumSpecies.Sunflora, new PixelmonSmdFactory(EnumPokemonModel.Sunflora));
      addModel(EnumSpecies.Sunkern, new PixelmonSmdFactory(EnumPokemonModel.Sunkern));
      addModel(EnumSpecies.Surskit, new PixelmonSmdFactory(EnumPokemonModel.Surskit));
      addModel(EnumSpecies.Swablu, (new PixelmonSmdFactory(EnumPokemonModel.Swablu)).setYRotation(24.2F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Swadloon, new PixelmonSmdFactory(EnumPokemonModel.Swadloon));
      addModel(EnumSpecies.Swalot, new PixelmonSmdFactory(EnumPokemonModel.Swalot));
      addModel(EnumSpecies.Swampert, EnumMega.Normal, new PixelmonSmdFactory(EnumPokemonModel.Swampert));
      addModel(EnumSpecies.Swampert, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaSwampert));
      addModel(EnumSpecies.Swanna, new PixelmonSmdFactory(EnumPokemonModel.Swanna));
      addModel(EnumSpecies.Swellow, (new PixelmonSmdFactory(EnumPokemonModel.Swellow)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Swellow, (new PixelmonSmdFactory(EnumPokemonModel.SwellowFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Swinub, (new PixelmonSmdFactory(EnumPokemonModel.Swinub)).setYRotation(23.5F));
      addModel(EnumSpecies.Swirlix, (new PixelmonSmdFactory(EnumPokemonModel.Swirlix)).setYRotation(21.0F));
      addModel(EnumSpecies.Swoobat, new PixelmonSmdFactory(EnumPokemonModel.Swoobat));
      addModel(EnumSpecies.Sylveon, new PixelmonSmdFactory(EnumPokemonModel.Sylveon));
      addModel(EnumSpecies.Taillow, (new PixelmonSmdFactory(EnumPokemonModel.Taillow)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Taillow, (new PixelmonSmdFactory(EnumPokemonModel.TaillowFlying)).setYRotation(25.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Talonflame, new PixelmonSmdFactory(EnumPokemonModel.Talonflame));
      addModel(EnumSpecies.Tangela, new PixelmonSmdFactory(EnumPokemonModel.Tangela));
      addModel(EnumSpecies.Tangrowth, new PixelmonSmdFactory(EnumPokemonModel.Tangrowth));
      addModel(EnumSpecies.Tapu_Koko, new PixelmonSmdFactory(EnumPokemonModel.Tapu_Koko));
      addModel(EnumSpecies.Tapu_Lele, new PixelmonSmdFactory(EnumPokemonModel.Tapu_Lele));
      addModel(EnumSpecies.Tapu_Bulu, new PixelmonSmdFactory(EnumPokemonModel.Tapu_Bulu));
      addModel(EnumSpecies.Tapu_Fini, new PixelmonSmdFactory(EnumPokemonModel.Tapu_Fini));
      addModel(EnumSpecies.Tauros, (new PixelmonSmdFactory(EnumPokemonModel.Tauros)).setYRotation(25.0F));
      addModel(EnumSpecies.Teddiursa, new PixelmonSmdFactory(EnumPokemonModel.Teddiursa));
      addModel(EnumSpecies.Tentacool, new PixelmonSmdFactory(EnumPokemonModel.Tentacool));
      addModel(EnumSpecies.Tentacruel, new PixelmonSmdFactory(EnumPokemonModel.Tentacruel));
      addModel(EnumSpecies.Tepig, new PixelmonSmdFactory(EnumPokemonModel.Tepig));
      addModel(EnumSpecies.Terrakion, new PixelmonSmdFactory(EnumPokemonModel.Terrakion));
      addModel(EnumSpecies.Throh, new PixelmonSmdFactory(EnumPokemonModel.Throh));
      addModel(EnumSpecies.Thundurus, EnumTherian.INCARNATE, new PixelmonSmdFactory(EnumPokemonModel.ThundurusIncarnate));
      addModel(EnumSpecies.Thundurus, EnumTherian.THERIAN, (new PixelmonSmdFactory(EnumPokemonModel.ThundurusTherian)).setYRotation(32.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Timburr, new PixelmonSmdFactory(EnumPokemonModel.Timburr));
      addModel(EnumSpecies.Togedemaru, (new PixelmonSmdFactory(EnumPokemonModel.Togedemaru)).setYRotation(20.0F));
      addModel(EnumSpecies.Tirtouga, new PixelmonSmdFactory(EnumPokemonModel.Tirtouga));
      addModel(EnumSpecies.Togekiss, (new PixelmonSmdFactory(EnumPokemonModel.Togekiss)).setYRotation(28.0F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Togepi, new PixelmonSmdFactory(EnumPokemonModel.Togepi));
      addModel(EnumSpecies.Togetic, new PixelmonSmdFactory(EnumPokemonModel.Togetic));
      addModel(EnumSpecies.Torracat, new PixelmonSmdFactory(EnumPokemonModel.Torracat));
      addModel(EnumSpecies.Torchic, (new PixelmonSmdFactory(EnumPokemonModel.Torchic)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Torkoal, (new PixelmonSmdFactory(EnumPokemonModel.Torkoal)).setZRotation(0.5F));
      addModel(EnumSpecies.Tornadus, EnumTherian.INCARNATE, new PixelmonSmdFactory(EnumPokemonModel.TornadusIncarnate));
      addModel(EnumSpecies.Tornadus, EnumTherian.THERIAN, (new PixelmonSmdFactory(EnumPokemonModel.TornadusTherian)).setYRotation(45.0F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Torterra, new PixelmonSmdFactory(EnumPokemonModel.Torterra));
      addModel(EnumSpecies.Totodile, new PixelmonSmdFactory(EnumPokemonModel.Totodile));
      addModel(EnumSpecies.Toucannon, (new PixelmonSmdFactory(EnumPokemonModel.Toucannon)).setYRotation(19.0F));
      addModel(EnumSpecies.Toxapex, new PixelmonSmdFactory(EnumPokemonModel.Toxapex));
      addModel(EnumSpecies.Toxel, new PixelmonSmdFactory(EnumPokemonModel.Toxel));
      addModel(EnumSpecies.Toxicroak, new PixelmonSmdFactory(EnumPokemonModel.Toxicroak));
      addModel(EnumSpecies.Toxtricity, EnumToxtricity.AMPED, new PixelmonSmdFactory(EnumPokemonModel.ToxtricityAmped));
      addModel(EnumSpecies.Toxtricity, EnumToxtricity.LOWKEY, new PixelmonSmdFactory(EnumPokemonModel.ToxtricityLowKey));
      addModel(EnumSpecies.Toxtricity, EnumToxtricity.GIGANTAMAX, new PixelmonSmdFactory(EnumPokemonModel.ToxtricityGigantamax));
      addModel(EnumSpecies.Tranquill, new PixelmonSmdFactory(EnumPokemonModel.Tranquill));
      addModel(EnumSpecies.Trapinch, (new PixelmonSmdFactory(EnumPokemonModel.Trapinch)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Treecko, (new PixelmonSmdFactory(EnumPokemonModel.Treecko)).setYRotation(22.5F));
      addModel(EnumSpecies.Trevenant, new PixelmonSmdFactory(EnumPokemonModel.Trevenant));
      addModel(EnumSpecies.Tropius, new PixelmonSmdFactory(EnumPokemonModel.Tropius));
      addModel(EnumSpecies.Trubbish, new PixelmonSmdFactory(EnumPokemonModel.Trubbish));
      addModel(EnumSpecies.Trumbeak, (new PixelmonSmdFactory(EnumPokemonModel.Trumbeak)).setYRotation(19.0F));
      addModel(EnumSpecies.Turtwig, new PixelmonSmdFactory(EnumPokemonModel.Turtwig));
      addModel(EnumSpecies.Turtonator, (new PixelmonSmdFactory(EnumPokemonModel.Turtonator)).setYRotation(28.0F));
      addModel(EnumSpecies.Tsareena, new PixelmonSmdFactory(EnumPokemonModel.Tsareena));
      addModel(EnumSpecies.Tympole, new PixelmonSmdFactory(EnumPokemonModel.Tympole));
      addModel(EnumSpecies.Tynamo, new PixelmonSmdFactory(EnumPokemonModel.Tynamo));
      addModel(EnumSpecies.TypeNull, new PixelmonSmdFactory(EnumPokemonModel.TypeNull));
      addModel(EnumSpecies.Typhlosion, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Typhlosion));
      addModel(EnumSpecies.Typhlosion, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.TyphlosionHisuian));
      addModel(EnumSpecies.Tyranitar, EnumMega.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Tyranitar)).setYRotation(24.2F));
      addModel(EnumSpecies.Tyranitar, EnumMega.Mega, new PixelmonSmdFactory(EnumPokemonModel.MegaTyranitar));
      addModel(EnumSpecies.Tyrantrum, new PixelmonSmdFactory(EnumPokemonModel.Tyrantrum));
      addModel(EnumSpecies.Tyrogue, new PixelmonSmdFactory(EnumPokemonModel.Tyrogue));
      addModel(EnumSpecies.Tyrunt, new PixelmonSmdFactory(EnumPokemonModel.Tyrunt));
      addModel(EnumSpecies.Umbreon, new PixelmonSmdFactory(EnumPokemonModel.Umbreon));
      addModel(EnumSpecies.Unfezant, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.UnfezantMale));
      addModel(EnumSpecies.Unfezant, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.UnfezantFemale));
      EnumUnown[] var7 = EnumUnown.values();
      var6 = var7.length;

      for(var2 = 0; var2 < var6; ++var2) {
         EnumUnown unown = var7[var2];
         addModel(EnumSpecies.Unown, unown, new PixelmonSmdFactory(EnumPokemonModel.valueOf("Unown" + unown.name())));
      }

      addModel(EnumSpecies.Ursaring, new PixelmonSmdFactory(EnumPokemonModel.Ursaring));
      addModel(EnumSpecies.Uxie, (new PixelmonSmdFactory(EnumPokemonModel.Uxie)).setYRotation(23.9F));
      addModel(EnumSpecies.Vanillite, new PixelmonSmdFactory(EnumPokemonModel.Vanillite));
      addModel(EnumSpecies.Vanillish, new PixelmonSmdFactory(EnumPokemonModel.Vanillish));
      addModel(EnumSpecies.Vanilluxe, new PixelmonSmdFactory(EnumPokemonModel.Vanilluxe));
      addModel(EnumSpecies.Vaporeon, new PixelmonSmdFactory(EnumPokemonModel.Vaporeon));
      addModel(EnumSpecies.Venipede, new PixelmonSmdFactory(EnumPokemonModel.Venipede));
      addModel(EnumSpecies.Venomoth, new PixelmonSmdFactory(EnumPokemonModel.Venomoth));
      addModel(EnumSpecies.Venonat, new PixelmonSmdFactory(EnumPokemonModel.Venonat));
      addModel(EnumSpecies.Venusaur, EnumVenusaur.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Venusaur)).setYRotation(24.12F));
      addModel(EnumSpecies.Venusaur, EnumVenusaur.Mega, (new PixelmonSmdFactory(EnumPokemonModel.MegaVenusaur)).setYRotation(24.12F));
      addModel(EnumSpecies.Venusaur, EnumVenusaur.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxVenusaur)).setYRotation(24.12F));
      addModel(EnumSpecies.Vespiquen, (new PixelmonSmdFactory(EnumPokemonModel.Vespiquen)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Vibrava, (new PixelmonSmdFactory(EnumPokemonModel.Vibrava)).setMovementThreshold(0.03F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Victini, new PixelmonSmdFactory(EnumPokemonModel.Victini));
      addModel(EnumSpecies.Victreebel, new PixelmonSmdFactory(EnumPokemonModel.Victreebel));
      addModel(EnumSpecies.Vigoroth, (new PixelmonSmdFactory(EnumPokemonModel.Vigoroth)).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Vikavolt, new PixelmonSmdFactory(EnumPokemonModel.Vikavolt));
      addModel(EnumSpecies.Virizion, new PixelmonSmdFactory(EnumPokemonModel.Virizion));
      addModel(EnumSpecies.Vivillon, new PixelmonSmdFactory(EnumPokemonModel.Vivillon));
      addModel(EnumSpecies.Volbeat, (new PixelmonSmdFactory(EnumPokemonModel.Volbeat)).setZRotation(0.5F).setMovementThreshold(0.03F));
      addModel(EnumSpecies.Volcanion, (new DualModelFactory(EnumPokemonModel.Volcanion, EnumPokemonModel.VolcanionSteam)).setModel2Transparency(0.2F));
      addModel(EnumSpecies.Volcarona, new PixelmonSmdFactory(EnumPokemonModel.Volcarona));
      addModel(EnumSpecies.Voltorb, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Voltorb));
      addModel(EnumSpecies.Voltorb, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.VoltorbHisuian));
      addModel(EnumSpecies.Vullaby, new PixelmonSmdFactory(EnumPokemonModel.Vullaby));
      addModel(EnumSpecies.Vulpix, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Vulpix));
      addModel(EnumSpecies.Vulpix, RegionalForms.ALOLAN, new PixelmonSmdFactory(EnumPokemonModel.AlolanVulpix));
      addModel(EnumSpecies.Wailmer, (new PixelmonSmdFactory(EnumPokemonModel.Wailmer)).setYRotation(27.0F).setMovementThreshold(0.01F));
      addModel(EnumSpecies.Wailord, (new PixelmonSmdFactory(EnumPokemonModel.Wailord)).setYRotation(12.0F).setMovementThreshold(0.01F));
      addModel(EnumSpecies.Walrein, new PixelmonSmdFactory(EnumPokemonModel.Walrein));
      addModel(EnumSpecies.Wartortle, (new PixelmonSmdFactory(EnumPokemonModel.Wartortle)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Watchog, new PixelmonSmdFactory(EnumPokemonModel.Watchog));
      addModel(EnumSpecies.Weavile, new PixelmonSmdFactory(EnumPokemonModel.Weavile));
      addModel(EnumSpecies.Weedle, new PixelmonSmdFactory(EnumPokemonModel.Weedle));
      addModel(EnumSpecies.Weepinbell, new PixelmonSmdFactory(EnumPokemonModel.Weepinbell));
      addModel(EnumSpecies.Whimsicott, new PixelmonSmdFactory(EnumPokemonModel.Whimsicott));
      addModel(EnumSpecies.Whirlipede, new PixelmonSmdFactory(EnumPokemonModel.Whirlipede));
      addModel(EnumSpecies.Whiscash, new PixelmonSmdFactory(EnumPokemonModel.Whiscash));
      addModel(EnumSpecies.Whismur, new PixelmonSmdFactory(EnumPokemonModel.Whismur));
      addModel(EnumSpecies.Wishiwashi, EnumWishiwashi.SOLO, (new PixelmonSmdFactory(EnumPokemonModel.WishiwashiSolo)).setYRotation(16.0F));
      addModel(EnumSpecies.Wishiwashi, EnumWishiwashi.SCHOOL, new PixelmonSmdFactory(EnumPokemonModel.WishiwashiSchool));
      addModel(EnumSpecies.Wigglytuff, new PixelmonSmdFactory(EnumPokemonModel.Wigglytuff));
      addModel(EnumSpecies.Wimpod, new PixelmonSmdFactory(EnumPokemonModel.Wimpod));
      addModel(EnumSpecies.Wingull, new PixelmonSmdFactory(EnumPokemonModel.Wingull));
      addModel(EnumSpecies.Wobbuffet, EnumNoForm.NoForm, new PixelmonSmdFactory(EnumPokemonModel.Wobbuffet));
      addModel(EnumSpecies.Wobbuffet, EnumSpecial.Online, new PixelmonSmdFactory(EnumPokemonModel.WobbuffetSpecial));
      addModel(EnumSpecies.Woobat, new PixelmonSmdFactory(EnumPokemonModel.Woobat));
      addModel(EnumSpecies.Wooper, new PixelmonSmdFactory(EnumPokemonModel.Wooper));
      addModel(EnumSpecies.Wormadam, EnumWormadam.Plant, (new PixelmonSmdFactory(EnumPokemonModel.WormadamPlant)).setYRotation(22.0F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Wormadam, EnumWormadam.Sandy, (new PixelmonSmdFactory(EnumPokemonModel.WormadamSandy)).setYRotation(22.0F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Wormadam, EnumWormadam.Trash, (new PixelmonSmdFactory(EnumPokemonModel.WormadamTrash)).setYRotation(22.0F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Wurmple, (new PixelmonSmdFactory(EnumPokemonModel.Wurmple)).setYRotation(24.1F));
      addModel(EnumSpecies.Wynaut, new PixelmonSmdFactory(EnumPokemonModel.Wynaut));
      addModel(EnumSpecies.Xatu, new PixelmonSmdFactory(EnumPokemonModel.Xatu));
      addFlyingModel(EnumSpecies.Xatu, new PixelmonSmdFactory(EnumPokemonModel.XatuFlying));
      addModel(EnumSpecies.Xerneas, EnumXerneas.NEUTRAL, new PixelmonSmdFactory(EnumPokemonModel.Xerneas));
      addModel(EnumSpecies.Xerneas, EnumXerneas.ACTIVE, new PixelmonSmdFactory(EnumPokemonModel.Xerneas));
      addModel(EnumSpecies.Xerneas, EnumXerneas.NEUTRAL_CREATOR, new PixelmonSmdFactory(EnumPokemonModel.XerneasCreator));
      addModel(EnumSpecies.Xerneas, EnumXerneas.ACTIVE_CREATOR, new PixelmonSmdFactory(EnumPokemonModel.XerneasCreator));
      addModel(EnumSpecies.Xurkitree, new PixelmonSmdFactory(EnumPokemonModel.Xurkitree));
      addModel(EnumSpecies.Yanma, new PixelmonSmdFactory(EnumPokemonModel.Yanma));
      addModel(EnumSpecies.Yungoos, new PixelmonSmdFactory(EnumPokemonModel.Yungoos));
      addModel(EnumSpecies.Yanmega, new PixelmonSmdFactory(EnumPokemonModel.Yanmega));
      addModel(EnumSpecies.Yveltal, new PixelmonSmdFactory(EnumPokemonModel.Yveltal));
      addModel(EnumSpecies.Zangoose, new PixelmonSmdFactory(EnumPokemonModel.Zangoose));
      addModel(EnumSpecies.Zapdos, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Zapdos)).setYRotation(24.14F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Zapdos, RegionalForms.GALARIAN, (new PixelmonSmdFactory(EnumPokemonModel.ZapdosGalar)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Zapdos, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.ZapdosFlying)).setYRotation(23.14F).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Zebstrika, new PixelmonSmdFactory(EnumPokemonModel.Zebstrika));
      addModel(EnumSpecies.Zekrom, new PixelmonSmdFactory(EnumPokemonModel.Zekrom));
      addModel(EnumSpecies.Zeraora, new PixelmonSmdFactory(EnumPokemonModel.Zeraora));
      addModel(EnumSpecies.Zubat, new PixelmonSmdFactory(EnumPokemonModel.Zubat));
      addModel(EnumSpecies.Zoroark, new PixelmonSmdFactory(EnumPokemonModel.Zoroark));
      addModel(EnumSpecies.Zoroark, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.HisuianZoroark));
      addModel(EnumSpecies.Zorua, new PixelmonSmdFactory(EnumPokemonModel.Zorua));
      addModel(EnumSpecies.Zorua, RegionalForms.HISUIAN, new PixelmonSmdFactory(EnumPokemonModel.HisuianZorua));
      addModel(EnumSpecies.Zweilous, new PixelmonSmdFactory(EnumPokemonModel.Zweilous));
      addModel(EnumSpecies.Zygarde, EnumZygarde.FIFTY_PERCENT, new PixelmonSmdFactory(EnumPokemonModel.Zygarde));
      addModel(EnumSpecies.Zygarde, EnumZygarde.TEN_PERCENT, (new PixelmonSmdFactory(EnumPokemonModel.ZygardeTen)).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Zygarde, EnumZygarde.COMPLETE, (new PixelmonSmdFactory(EnumPokemonModel.ZygardeComplete)).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Rookidee, (new PixelmonSmdFactory(EnumPokemonModel.Rookidee)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Rookidee, (new PixelmonSmdFactory(EnumPokemonModel.RookideeFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Corvisquire, (new PixelmonSmdFactory(EnumPokemonModel.Corvisquire)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Corvisquire, (new PixelmonSmdFactory(EnumPokemonModel.CorvisquireFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Corviknight, EnumGigantamax.Normal, (new PixelmonSmdFactory(EnumPokemonModel.Corviknight)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Corviknight, EnumGigantamax.Gigantamax, (new PixelmonSmdFactory(EnumPokemonModel.GigantamaxCorviknight)).setMovementThreshold(0.02F));
      addFlyingModel(EnumSpecies.Corviknight, EnumGigantamax.Normal, (new PixelmonSmdFactory(EnumPokemonModel.CorviknightFlying)).setMovementThreshold(0.02F));
      addModel(EnumSpecies.Eiscue, EnumEiscue.ICE_FACE, new PixelmonSmdFactory(EnumPokemonModel.EiscueHead));
      addModel(EnumSpecies.Eiscue, EnumEiscue.NOICE_FACE, new PixelmonSmdFactory(EnumPokemonModel.Eiscue));
      addModel(EnumSpecies.Sizzlipede, new PixelmonSmdFactory(EnumPokemonModel.Sizzlipede));
      addModel(EnumSpecies.Centiskorch, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Centiskorch));
      addModel(EnumSpecies.Centiskorch, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.CentiskorchGmax));
      addModel(EnumSpecies.Falinks, new PixelmonSmdFactory(EnumPokemonModel.Falinks));
      addModel(EnumSpecies.Toxel, new PixelmonSmdFactory(EnumPokemonModel.Toxel));
      addModel(EnumSpecies.Impidimp, new PixelmonSmdFactory(EnumPokemonModel.Impidimp));
      addModel(EnumSpecies.Morgrem, new PixelmonSmdFactory(EnumPokemonModel.Morgrem));
      addModel(EnumSpecies.Grimmsnarl, EnumGigantamax.Normal, new PixelmonSmdFactory(EnumPokemonModel.Grimmsnarl));
      addModel(EnumSpecies.Grimmsnarl, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.GrimmsnarlGmax));
      addModel(EnumSpecies.Morpeko, EnumMorpeko.FULLBELLY, new PixelmonSmdFactory(EnumPokemonModel.Morpeko));
      addModel(EnumSpecies.Morpeko, EnumMorpeko.HANGRY, new PixelmonSmdFactory(EnumPokemonModel.Morpeko));
      addModel(EnumSpecies.Stunfisk, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Stunfisk));
      addModel(EnumSpecies.Stunfisk, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.StunfiskGalar));
      addModel(EnumSpecies.Yamask, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Yamask));
      addModel(EnumSpecies.Yamask, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.YamaskGalar));
      addModel(EnumSpecies.Corsola, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Corsola)).setRotateAngleY(-3.1415927F));
      addModel(EnumSpecies.Corsola, RegionalForms.GALARIAN, (new DualModelFactory(EnumPokemonModel.CorsolaGalar, EnumPokemonModel.CorsolaFeelers)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Cursola, (new DualModelFactory(EnumPokemonModel.Cursola, EnumPokemonModel.CursolaGhost)).setModel2Transparency(0.4F));
      addModel(EnumSpecies.Zigzagoon, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Zigzagoon)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Zigzagoon, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.ZigzagoonGalar));
      addModel(EnumSpecies.Linoone, RegionalForms.NORMAL, (new PixelmonSmdFactory(EnumPokemonModel.Linoone)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Linoone, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.LinooneGalar));
      addModel(EnumSpecies.Obstagoon, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Obstagoon));
      addModel(EnumSpecies.Weezing, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Weezing));
      addModel(EnumSpecies.Weezing, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.WeezingGalar));
      addModel(EnumSpecies.Ponyta, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.PonytaGalar));
      addModel(EnumSpecies.Rapidash, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.RapidashGalar));
      addModel(EnumSpecies.Farfetchd, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.FarfetchdGalar));
      addModel(EnumSpecies.Sirfetchd, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Sirfetchd));
      addModel(EnumSpecies.Slowpoke, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.SlowpokeGalar));
      addModel(EnumSpecies.Slowbro, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.SlowbroGalar));
      addModel(EnumSpecies.Snom, (new DualModelFactory(EnumPokemonModel.Snom, EnumPokemonModel.SnomIce)).setModel2Transparency(0.3F));
      addModel(EnumSpecies.Frosmoth, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Frosmoth));
      addModel(EnumSpecies.MrMime, RegionalForms.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.MrMime));
      addModel(EnumSpecies.MrMime, RegionalForms.GALARIAN, new PixelmonSmdFactory(EnumPokemonModel.MrMimeGalar));
      addModel(EnumSpecies.Meowth, EnumMeowth.Normal, new PixelmonSmdFactory(EnumPokemonModel.Meowth));
      addModel(EnumSpecies.Meowth, EnumMeowth.Alolan, (new PixelmonSmdFactory(EnumPokemonModel.Meowth)).setMovementThreshold(0.02F).setAnimationIncrement(1.5F));
      addModel(EnumSpecies.Meowth, EnumMeowth.Galarian, new PixelmonSmdFactory(EnumPokemonModel.MeowthGalar));
      addModel(EnumSpecies.Meowth, EnumMeowth.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.MeowthGmax));
      addModel(EnumSpecies.Perrserker, new PixelmonSmdFactory(EnumPokemonModel.Perrserker));
      addModel(EnumSpecies.Sinistea, (new PixelmonSmdFactory(EnumPokemonModel.Sinistea)).setRotateAngleX(0.0F));
      addModel(EnumSpecies.Polteageist, new PixelmonSmdFactory(EnumPokemonModel.Polteageist));
      addModel(EnumSpecies.Cramorant, EnumCramorant.NORMAL, new PixelmonSmdFactory(EnumPokemonModel.Cramorant));
      addModel(EnumSpecies.Cramorant, EnumCramorant.GULPING, new PixelmonSmdFactory(EnumPokemonModel.CramorantGulping));
      addModel(EnumSpecies.Cramorant, EnumCramorant.GORGING, new PixelmonSmdFactory(EnumPokemonModel.CramorantGorging));
      addModel(EnumSpecies.Dreepy, new PixelmonSmdFactory(EnumPokemonModel.Dreepy));
      addModel(EnumSpecies.Drakloak, new PixelmonSmdFactory(EnumPokemonModel.Drakloak));
      addModel(EnumSpecies.Dragapult, new PixelmonSmdFactory(EnumPokemonModel.Dragapult));
      addModel(EnumSpecies.Cufant, new PixelmonSmdFactory(EnumPokemonModel.Cufant));
      addModel(EnumSpecies.Copperajah, new PixelmonSmdFactory(EnumPokemonModel.Copperajah));
      addModel(EnumSpecies.Copperajah, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.CopperajahGmax));
      addModel(EnumSpecies.Dracozolt, new PixelmonSmdFactory(EnumPokemonModel.Dracozolt));
      addModel(EnumSpecies.Arctozolt, new PixelmonSmdFactory(EnumPokemonModel.Arctozolt));
      addModel(EnumSpecies.Dracovish, new PixelmonSmdFactory(EnumPokemonModel.Dracovish));
      addModel(EnumSpecies.Arctovish, new PixelmonSmdFactory(EnumPokemonModel.Arctovish));
      addModel(EnumSpecies.Duraludon, new PixelmonSmdFactory(EnumPokemonModel.Duraludon));
      addModel(EnumSpecies.Duraludon, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.DuraludonGmax));
      addModel(EnumSpecies.Meowth, EnumGigantamax.Gigantamax, new PixelmonSmdFactory(EnumPokemonModel.MeowthGmax));
      addModel(EnumSpecies.Zacian, EnumHeroDuo.HERO, new PixelmonSmdFactory(EnumPokemonModel.ZacianHero));
      addModel(EnumSpecies.Zacian, EnumHeroDuo.CROWNED, new PixelmonSmdFactory(EnumPokemonModel.ZacianCrowned));
      addModel(EnumSpecies.Zamazenta, EnumHeroDuo.HERO, new PixelmonSmdFactory(EnumPokemonModel.ZamazentaHero));
      addModel(EnumSpecies.Zamazenta, EnumHeroDuo.CROWNED, new PixelmonSmdFactory(EnumPokemonModel.ZamazentaCrowned));
      addModel(EnumSpecies.Eternatus, EnumEternatus.ORDINARY, (new DualModelFactory(EnumPokemonModel.EternatusOrdinary, EnumPokemonModel.EternatusTransparent)).setModel2Transparency(0.7F));
      addModel(EnumSpecies.Eternatus, EnumEternatus.ETERNAMAX, new PixelmonSmdFactory(EnumPokemonModel.EternatusEternamax));
      addModel(EnumSpecies.Eternatus, EnumEternatus.ETERNAMAXCOSMETIC, new PixelmonSmdFactory(EnumPokemonModel.EternatusEternamax));
      addModel(EnumSpecies.Kubfu, new PixelmonSmdFactory(EnumPokemonModel.Kubfu));
      addModel(EnumSpecies.Urshifu, EnumUrshifu.SingleStrike, new PixelmonSmdFactory(EnumPokemonModel.UrshifuSingleStrike));
      addModel(EnumSpecies.Urshifu, EnumUrshifu.RapidStrike, new PixelmonSmdFactory(EnumPokemonModel.UrshifuRapidStrike));
      addModel(EnumSpecies.Urshifu, EnumUrshifu.GigantamaxSS, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxUrshifuSingleStrike));
      addModel(EnumSpecies.Urshifu, EnumUrshifu.GigantamaxRS, new PixelmonSmdFactory(EnumPokemonModel.GigantamaxUrshifuRapidStrike));
      addModel(EnumSpecies.Zarude, new PixelmonSmdFactory(EnumPokemonModel.Zarude));
      addModel(EnumSpecies.Glastrier, new PixelmonSmdFactory(EnumPokemonModel.Glastrier));
      addModel(EnumSpecies.Spectrier, new PixelmonSmdFactory(EnumPokemonModel.Spectrier));
      addModel(EnumSpecies.Calyrex, EnumCalyrex.ORDINARY, new PixelmonSmdFactory(EnumPokemonModel.Calyrex));
      addModel(EnumSpecies.Calyrex, EnumCalyrex.ICERIDER, new DualModelDualTextureFactory(EnumPokemonModel.CalyrexIceRiderCalyrex, EnumPokemonModel.CalyrexIceRiderGlastrier, new ResourceLocation("pixelmon", "textures/pokemon/calyrex-icerider-horse.png")));
      addModel(EnumSpecies.Calyrex, EnumCalyrex.SHADOWRIDER, new DualModelDualTextureFactory(EnumPokemonModel.CalyrexShadowRiderCalyrex, EnumPokemonModel.CalyrexShadowRiderSpectrier, new ResourceLocation("pixelmon", "textures/pokemon/calyrex-shadowrider-horse.png")));
      addModel(EnumSpecies.Wyrdeer, new PixelmonSmdFactory(EnumPokemonModel.Wyrdeer));
      addModel(EnumSpecies.Basculegion, Gender.Male, new PixelmonSmdFactory(EnumPokemonModel.BasculegionMale));
      addModel(EnumSpecies.Basculegion, Gender.Female, new PixelmonSmdFactory(EnumPokemonModel.BasculegionFemale));
      addModel(EnumSpecies.Overqwil, new PixelmonSmdFactory(EnumPokemonModel.Overqwil));
   }

   public static ModelBase getModel(EnumSpecies species, IEnumForm form) {
      if (form != EnumNoForm.NoForm && formModelRegistry.containsKey(species)) {
         Map forms = (Map)formModelRegistry.get(species);
         if (forms.containsKey(form)) {
            return ((PixelmonModelHolder)forms.get(form)).getModel();
         }
      }

      if (modelRegistry.containsKey(species)) {
         return ((PixelmonModelHolder)modelRegistry.get(species)).getModel();
      } else {
         try {
            ResourceLocation rl = new ResourceLocation("pixelmon", "models/pokemon/" + species.getPokemonName().toLowerCase() + "/" + species.getPokemonName().toLowerCase() + form.getFormSuffix() + ".pqc");
            if (Pixelmon.proxy.resourceLocationExists(rl)) {
               addModel(species, form, new PixelmonSmdFactory(rl));
               return getModel(species, form);
            }

            rl = new ResourceLocation("pixelmon", "models/pokemon/" + species.getPokemonName().toLowerCase() + "/" + species.getPokemonName().toLowerCase() + ".pqc");
            if (Pixelmon.proxy.resourceLocationExists(rl)) {
               addModel(species, form, new PixelmonSmdFactory(rl));
               return getModel(species, form);
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         return null;
      }
   }

   public static ModelBase getModel(PokemonBase pokemon) {
      return getModel(pokemon.getSpecies(), pokemon.getFormEnum());
   }

   public static Vector3d getModelSize(PokemonBase pokemon) {
      return getModelSize(pokemon.getSpecies(), pokemon.getFormEnum());
   }

   public static Vector3d getModelSize(EnumSpecies species, IEnumForm form) {
      PixelmonModelHolder holder = null;
      if (modelRegistry.containsKey(species)) {
         holder = (PixelmonModelHolder)modelRegistry.get(species);
      } else if (form != EnumNoForm.NoForm && formModelRegistry.containsKey(species)) {
         Map forms = (Map)formModelRegistry.get(species);
         if (forms.containsKey(form)) {
            holder = (PixelmonModelHolder)forms.get(form);
         }
      }

      if (holder == null) {
         return null;
      } else if (modelSizes.containsKey(holder)) {
         return (Vector3d)modelSizes.get(holder);
      } else {
         ModelBase model = holder.getModel();
         if (model == ResourceLoader.DUMMY) {
            return new Vector3d(1.0, 1.0, 1.0);
         } else {
            Vector3d min;
            Vector3d max;
            double scale;
            if (model instanceof PixelmonModelSmd) {
               min = new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
               max = new Vector3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
               Iterator var17 = ((PixelmonModelSmd)model).theModel.body.verts.iterator();

               while(var17.hasNext()) {
                  DeformVertex vertex = (DeformVertex)var17.next();
                  if ((double)vertex.x < min.x) {
                     min.x = (double)vertex.x;
                  } else if ((double)vertex.x > max.x) {
                     max.x = (double)vertex.x;
                  }

                  if ((double)vertex.y < min.y) {
                     min.y = (double)vertex.y;
                  } else if ((double)vertex.y > max.y) {
                     max.y = (double)vertex.y;
                  }

                  if ((double)vertex.z < min.z) {
                     min.z = (double)vertex.z;
                  } else if ((double)vertex.z > max.z) {
                     max.z = (double)vertex.z;
                  }
               }

               max.sub(min);
               scale = (double)((PixelmonModelSmd)model).getScale();
               modelSizes.put(holder, new Vector3d(max.getX() * scale, max.getY() * scale, max.getZ() * scale));
               return max;
            } else {
               try {
                  min = new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
                  max = new Vector3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
                  Field[] var6 = model.getClass().getDeclaredFields();
                  int var7 = var6.length;

                  for(int var8 = 0; var8 < var7; ++var8) {
                     Field field = var6[var8];
                     if (!field.isAccessible()) {
                        field.setAccessible(true);
                     }

                     if (field.getType() == ModelRenderer.class) {
                        ModelRenderer value = (ModelRenderer)field.get(model);
                        List cubes = Lists.newArrayList(value.field_78804_l);
                        Iterator var12;
                        if (value.field_78805_m != null) {
                           var12 = value.field_78805_m.iterator();

                           while(var12.hasNext()) {
                              ModelRenderer childModel = (ModelRenderer)var12.next();
                              cubes.addAll(childModel.field_78804_l);
                           }
                        }

                        var12 = cubes.iterator();

                        while(var12.hasNext()) {
                           ModelBox box = (ModelBox)var12.next();
                           if ((double)box.field_78252_a < min.y) {
                              min.x = (double)box.field_78252_a;
                           }

                           if ((double)box.field_78248_d > max.x) {
                              max.x = (double)box.field_78248_d;
                           }

                           if ((double)box.field_78250_b < min.y) {
                              min.y = (double)box.field_78250_b;
                           }

                           if ((double)box.field_78249_e > max.y) {
                              max.y = (double)box.field_78249_e;
                           }

                           if ((double)box.field_78251_c < min.z) {
                              min.z = (double)box.field_78251_c;
                           }

                           if ((double)box.field_78246_f > max.z) {
                              max.z = (double)box.field_78246_f;
                           }
                        }
                     }
                  }

                  max.sub(min);
                  scale = model instanceof PixelmonModelBase ? (double)((PixelmonModelBase)model).scale : 1.0;
                  modelSizes.put(holder, new Vector3d(max.getX() * scale, max.getY() * scale, max.getZ() * scale));
               } catch (Exception var14) {
                  var14.printStackTrace();
               }

               return new Vector3d(1.0, 1.0, 1.0);
            }
         }
      }
   }

   public static ModelBase getFlyingModel(EnumSpecies species, IEnumForm form) {
      return flyingModelRegistry.containsKey(species) && ((Map)flyingModelRegistry.get(species)).containsKey(form) ? ((PixelmonModelHolder)((Map)flyingModelRegistry.get(species)).get(form)).getModel() : getModel(species, form);
   }

   public static boolean hasFlyingModel(EnumSpecies species, IEnumForm form) {
      return flyingModelRegistry.containsKey(species) && ((Map)flyingModelRegistry.get(species)).containsKey(form);
   }

   static {
      init();
   }
}
