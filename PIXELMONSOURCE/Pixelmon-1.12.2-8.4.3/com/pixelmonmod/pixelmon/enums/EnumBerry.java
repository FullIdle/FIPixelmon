package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public enum EnumBerry {
   Cheri(2, 5, 12, 15, EnumBerryModel.cheriTaller, EnumBerryModel.cheriBloom, EnumBerryModel.cheriBerry, 0.35F, 1.0F, 0.45F, 10, 0, 0, 0, 0, EnumBerryColor.RED, 1),
   Chesto(2, 5, 12, 15, EnumBerryModel.chestoTaller, EnumBerryModel.chestoBloom, EnumBerryModel.chestoBerry, 0.3F, 1.0F, 0.5F, 0, 10, 0, 0, 0, EnumBerryColor.PURPLE, 1),
   Pecha(2, 5, 12, 15, EnumBerryModel.pechaTaller, EnumBerryModel.pechaBloom, EnumBerryModel.pechaBerry, 0.4F, 0.9F, 0.5F, 0, 0, 10, 0, 0, EnumBerryColor.PINK, 1),
   Rawst(2, 5, 12, 15, EnumBerryModel.rawstTaller, EnumBerryModel.rawstBloom, EnumBerryModel.rawstBerry, 0.065F, 0.2F, 0.5F, 0, 0, 0, 10, 0, EnumBerryColor.GREEN, 1),
   Aspear(2, 5, 12, 15, EnumBerryModel.aspearTaller, EnumBerryModel.aspearBloom, EnumBerryModel.aspearBerry, 0.285F, 0.85F, 0.55F, 0, 0, 0, 0, 10, EnumBerryColor.YELLOW, 1),
   Leppa(2, 5, 16, 15, EnumBerryModel.leppaTaller, EnumBerryModel.leppaBloom, EnumBerryModel.leppaBerry, 0.15F, 0.75F, 0.6F, 10, 0, 10, 10, 10, EnumBerryColor.RED, 1),
   Oran(2, 3, 12, 15, EnumBerryModel.oranTaller, EnumBerryModel.oranBloom, EnumBerryModel.oranBerry, 0.35F, 1.0F, 0.5F, 10, 10, 0, 10, 10, EnumBerryColor.BLUE, 1),
   Persim(2, 5, 16, 15, EnumBerryModel.persimTaller, EnumBerryModel.persimBloom, EnumBerryModel.persimBerry, 0.125F, 0.8F, 0.6F, 10, 10, 10, 0, 10, EnumBerryColor.PINK, 2),
   Lum(2, 5, 48, 8, EnumBerryModel.lumTaller, EnumBerryModel.lumBloom, EnumBerryModel.lumBerry, 0.17F, 0.8F, 0.4F, 10, 10, 10, 10, 0, EnumBerryColor.GREEN, 2),
   Sitrus(2, 5, 32, 7, EnumBerryModel.sitrusTaller, EnumBerryModel.sitrusBloom, EnumBerryModel.sitrusBerry, 0.35F, 1.0F, 0.5F, 0, 10, 10, 10, 10, EnumBerryColor.YELLOW, 2),
   Figy(1, 5, 20, 10, EnumBerryModel.figyTaller, EnumBerryModel.figyBloom, EnumBerryModel.figyBerry, 0.37F, 0.8F, 0.6F, 15, 0, 0, 0, 0, EnumBerryColor.RED, 3),
   Wiki(1, 5, 20, 10, EnumBerryModel.wikiTaller, EnumBerryModel.wikiBloom, EnumBerryModel.wikiBerry, 0.3F, 0.75F, 0.6F, 0, 15, 0, 0, 0, EnumBerryColor.PURPLE, 3),
   Mago(1, 5, 20, 10, EnumBerryModel.magoTaller, EnumBerryModel.magoBloom, EnumBerryModel.magoBerry, 0.12F, 0.8F, 0.5F, 0, 0, 15, 0, 0, EnumBerryColor.PINK, 3),
   Aguav(1, 5, 20, 10, EnumBerryModel.aguavTaller, EnumBerryModel.aguavBloom, EnumBerryModel.aguavBerry, 0.25F, 0.8F, 0.4F, 0, 0, 0, 15, 0, EnumBerryColor.GREEN, 3),
   Iapapa(1, 5, 20, 10, EnumBerryModel.iapapaTaller, EnumBerryModel.iapapaBloom, EnumBerryModel.iapapaBerry, 0.16F, 0.85F, 0.5F, 0, 0, 0, 0, 15, EnumBerryColor.YELLOW, 3),
   Razz(3, 6, 4, 35, EnumBerryModel.razzTaller, EnumBerryModel.razzBloom, EnumBerryModel.razzBerry, 10, 10, 0, 0, 0, EnumBerryColor.RED, 1),
   Bluk(3, 6, 4, 35, EnumBerryModel.blukTaller, EnumBerryModel.blukBloom, EnumBerryModel.blukBerry, 0, 10, 10, 0, 0, EnumBerryColor.PURPLE, 1),
   Nanab(3, 6, 4, 35, EnumBerryModel.nanabTaller, EnumBerryModel.nanabBloom, EnumBerryModel.nanabBerry, 0, 0, 10, 10, 0, EnumBerryColor.PINK, 1),
   Wepear(3, 6, 4, 35, EnumBerryModel.wepearTaller, EnumBerryModel.wepearBloom, EnumBerryModel.wepearBerry, 0, 0, 0, 10, 10, EnumBerryColor.GREEN, 1),
   Pinap(2, 10, 8, 35, EnumBerryModel.pinapTaller, EnumBerryModel.pinapBloom, EnumBerryModel.pinapBerry, 10, 0, 0, 0, 10, EnumBerryColor.YELLOW, 1),
   Pomeg(1, 5, 32, 8, EnumBerryModel.pomegTaller, EnumBerryModel.pomegBloom, EnumBerryModel.pomegBerry, 0.23F, 0.8F, 0.55F, 10, 0, 10, 10, 0, EnumBerryColor.RED, 3),
   Kelpsy(1, 5, 32, 8, EnumBerryModel.kelpsyTaller, EnumBerryModel.kelpsyBloom, EnumBerryModel.kelpsyBerry, 0.2F, 0.35F, 0.65F, 0, 10, 0, 10, 10, EnumBerryColor.BLUE, 3),
   Qualot(1, 5, 32, 8, EnumBerryModel.qualotTaller, EnumBerryModel.qualotBloom, EnumBerryModel.qualotBerry, 0.22F, 0.75F, 0.6F, 10, 0, 10, 0, 10, EnumBerryColor.YELLOW, 3),
   Hondew(1, 5, 32, 8, EnumBerryModel.hondewTaller, EnumBerryModel.hondewBloom, EnumBerryModel.hondewBerry, 0.2F, 0.9F, 0.3F, 10, 10, 0, 10, 0, EnumBerryColor.GREEN, 3),
   Grepa(1, 5, 32, 8, EnumBerryModel.grepaTaller, EnumBerryModel.grepaBloom, EnumBerryModel.grepaBerry, 0.16F, 0.85F, 0.55F, 0, 10, 10, 0, 10, EnumBerryColor.YELLOW, 3),
   Tamato(1, 5, 32, 8, EnumBerryModel.tamatoTaller, EnumBerryModel.tamatoBloom, EnumBerryModel.tamatoBerry, 0.16F, 0.85F, 0.3F, 20, 10, 0, 0, 0, EnumBerryColor.RED, 3),
   Cornn(2, 10, 24, 10, EnumBerryModel.cornnTaller, EnumBerryModel.cornnBloom, EnumBerryModel.cornnBerry, 0, 20, 10, 0, 0, EnumBerryColor.PURPLE, 1),
   Magost(2, 10, 24, 10, EnumBerryModel.magostTaller, EnumBerryModel.magostBloom, EnumBerryModel.magostBerry, 0, 0, 20, 10, 0, EnumBerryColor.PINK, 1),
   Rabuta(2, 10, 24, 10, EnumBerryModel.rabutaTaller, EnumBerryModel.rabutaBloom, EnumBerryModel.rabutaBerry, 0, 0, 0, 20, 10, EnumBerryColor.GREEN, 1),
   Nomel(2, 10, 24, 10, EnumBerryModel.nomelTaller, EnumBerryModel.nomelBloom, EnumBerryModel.nomelBerry, 10, 0, 0, 0, 20, EnumBerryColor.YELLOW, 1),
   Spelon(2, 15, 60, 8, EnumBerryModel.spelonTaller, EnumBerryModel.spelonBloom, EnumBerryModel.spelonBerry, 30, 10, 0, 0, 0, EnumBerryColor.RED, 1),
   Pamtre(3, 15, 60, 8, EnumBerryModel.pamtreTaller, EnumBerryModel.pamtreBloom, EnumBerryModel.pamtreBerry, 0, 30, 10, 0, 0, EnumBerryColor.PURPLE, 1),
   Watmel(2, 15, 60, 8, EnumBerryModel.watmelTaller, EnumBerryModel.watmelBloom, EnumBerryModel.watmelBerry, 0, 0, 30, 10, 0, EnumBerryColor.PINK, 1),
   Durin(3, 15, 60, 8, EnumBerryModel.durinTaller, EnumBerryModel.durinBloom, EnumBerryModel.durinBerry, 0, 0, 0, 30, 10, EnumBerryColor.GREEN, 1),
   Belue(2, 15, 60, 8, EnumBerryModel.belueTaller, EnumBerryModel.belueBloom, EnumBerryModel.belueBerry, 10, 0, 0, 0, 30, EnumBerryColor.PURPLE, 1),
   Occa(1, 5, 72, 6, EnumBerryModel.occaTaller, EnumBerryModel.occaBloom, EnumBerryModel.occaBerry, 0.3F, 0.75F, 0.6F, 15, 0, 10, 0, 0, EnumBerryColor.RED, 2),
   Passho(1, 5, 72, 6, EnumBerryModel.passhoTaller, EnumBerryModel.passhoBloom, EnumBerryModel.passhoBerry, 0.35F, 0.75F, 0.55F, 0, 15, 0, 10, 0, EnumBerryColor.BLUE, 2),
   Wacan(1, 5, 72, 6, EnumBerryModel.wacanTaller, EnumBerryModel.wacanBloom, EnumBerryModel.wacanBerry, 0.2F, 0.75F, 0.55F, 0, 0, 15, 0, 10, EnumBerryColor.YELLOW, 2),
   Rindo(1, 5, 72, 6, EnumBerryModel.rindoTaller, EnumBerryModel.rindoBloom, EnumBerryModel.rindoBerry, 0.17F, 0.85F, 0.3F, 10, 0, 0, 15, 0, EnumBerryColor.GREEN, 2),
   Yache(1, 5, 72, 6, EnumBerryModel.yacheTaller, EnumBerryModel.yacheBloom, EnumBerryModel.yacheBerry, 0.16F, 0.85F, 0.5F, 0, 10, 0, 0, 15, EnumBerryColor.BLUE, 2),
   Chople(1, 5, 72, 6, EnumBerryModel.chopleTaller, EnumBerryModel.chopleBloom, EnumBerryModel.chopleBerry, 0.125F, 0.8F, 0.6F, 15, 0, 0, 10, 0, EnumBerryColor.RED, 2),
   Kebia(1, 5, 72, 6, EnumBerryModel.kebiaTaller, EnumBerryModel.kebiaBloom, EnumBerryModel.kebiaBerry, 0.15F, 0.85F, 0.55F, 0, 15, 0, 0, 10, EnumBerryColor.GREEN, 2),
   Shuca(1, 5, 72, 6, EnumBerryModel.shucaTaller, EnumBerryModel.shucaBloom, EnumBerryModel.shucaBerry, 0.12F, 0.75F, 0.6F, 10, 0, 15, 0, 0, EnumBerryColor.YELLOW, 2),
   Coba(1, 5, 72, 6, EnumBerryModel.cobaTaller, EnumBerryModel.cobaBloom, EnumBerryModel.cobaBerry, 0.06F, 0.2F, 0.5F, 0, 10, 0, 15, 0, EnumBerryColor.BLUE, 2),
   Payapa(1, 5, 72, 6, EnumBerryModel.payapaTaller, EnumBerryModel.payapaBloom, EnumBerryModel.payapaBerry, 0, 0, 10, 0, 15, EnumBerryColor.PURPLE, 2),
   Tanga(1, 5, 72, 6, EnumBerryModel.tangaTaller, EnumBerryModel.tangaBloom, EnumBerryModel.tangaBerry, 0.27F, 0.75F, 0.6F, 20, 0, 0, 0, 10, EnumBerryColor.GREEN, 2),
   Charti(1, 5, 72, 6, EnumBerryModel.chartiTaller, EnumBerryModel.chartiBloom, EnumBerryModel.chartiBerry, 0.15F, 0.75F, 0.5F, 10, 20, 0, 0, 0, EnumBerryColor.YELLOW, 2),
   Kasib(1, 5, 72, 6, EnumBerryModel.kasibTaller, EnumBerryModel.kasibBloom, EnumBerryModel.kasibBerry, 0.2F, 0.9F, 0.3F, 0, 10, 20, 0, 0, EnumBerryColor.PURPLE, 2),
   Haban(1, 5, 72, 6, EnumBerryModel.habanTaller, EnumBerryModel.habanBloom, EnumBerryModel.habanBerry, 0.13F, 0.75F, 0.6F, 0, 0, 10, 20, 0, EnumBerryColor.RED, 2),
   Colbur(1, 5, 72, 6, EnumBerryModel.colburTaller, EnumBerryModel.colburBloom, EnumBerryModel.colburBerry, 0.27F, 0.9F, 0.55F, 0, 0, 0, 10, 20, EnumBerryColor.PURPLE, 2),
   Babiri(1, 5, 72, 6, EnumBerryModel.babiriTaller, EnumBerryModel.babiriBloom, EnumBerryModel.babiriBerry, 0.15F, 0.75F, 0.4F, 25, 10, 0, 0, 0, EnumBerryColor.GREEN, 2),
   Chilan(1, 5, 72, 6, EnumBerryModel.chilanTaller, EnumBerryModel.chilanBloom, EnumBerryModel.chilanBerry, 0.15F, 0.85F, 0.55F, 0, 25, 10, 0, 0, EnumBerryColor.YELLOW, 2),
   Roseli(1, 5, 72, 6, EnumBerryModel.roseliTaller, EnumBerryModel.roseliBloom, EnumBerryModel.roseliBerry, 0.3F, 0.9F, 0.6F, 0, 0, 25, 10, 0, EnumBerryColor.PINK, 3),
   Liechi(1, 5, 96, 4, EnumBerryModel.liechiTaller, EnumBerryModel.liechiBloom, EnumBerryModel.liechiBerry, 0.14F, 0.75F, 0.4F, 30, 10, 30, 0, 0, EnumBerryColor.RED, 3),
   Ganlon(1, 5, 96, 4, EnumBerryModel.ganlonTaller, EnumBerryModel.ganlonBloom, EnumBerryModel.ganlonBerry, 0.18F, 0.9F, 0.3F, 0, 30, 10, 30, 0, EnumBerryColor.PURPLE, 3),
   Salac(1, 5, 96, 4, EnumBerryModel.salacTaller, EnumBerryModel.salacBloom, EnumBerryModel.salacBerry, 0.25F, 0.8F, 0.4F, 0, 0, 30, 10, 30, EnumBerryColor.GREEN, 3),
   Petaya(1, 2, 96, 4, EnumBerryModel.petayaTaller, EnumBerryModel.petayaBloom, EnumBerryModel.petayaBerry, 0.23F, 0.75F, 0.5F, 30, 0, 0, 30, 10, EnumBerryColor.PINK, 3),
   Apicot(1, 5, 96, 4, EnumBerryModel.apicotTaller, EnumBerryModel.apicotBloom, EnumBerryModel.apicotBerry, 0.15F, 0.85F, 0.55F, 10, 30, 0, 0, 30, EnumBerryColor.BLUE, 3),
   Lansat(1, 5, 96, 4, EnumBerryModel.lansatTaller, EnumBerryModel.lansatBloom, EnumBerryModel.lansatBerry, 0.18F, 0.8F, 0.5F, 30, 10, 30, 10, 30, EnumBerryColor.RED, 3),
   Starf(1, 2, 96, 4, EnumBerryModel.starfTaller, EnumBerryModel.starfBloom, EnumBerryModel.starfBerry, 0.3F, 0.75F, 0.6F, 30, 10, 30, 10, 30, EnumBerryColor.GREEN, 3),
   Pumkin(1, 5, 96, 7, EnumBerryModel.pumkinTaller, EnumBerryModel.pumkinBloom, EnumBerryModel.pumkinBerry, 40, 10, 0, 0, 0, EnumBerryColor.YELLOW, 3),
   Drash(1, 5, 96, 7, EnumBerryModel.drashTaller, EnumBerryModel.drashBloom, EnumBerryModel.drashBerry, 40, 10, 0, 0, 0, EnumBerryColor.PINK, 3),
   Eggant(1, 5, 96, 7, EnumBerryModel.eggantTaller, EnumBerryModel.eggantBloom, EnumBerryModel.eggantBerry, 40, 10, 0, 0, 0, EnumBerryColor.PURPLE, 3),
   Strib(1, 5, 96, 7, EnumBerryModel.stribTaller, EnumBerryModel.stribBloom, EnumBerryModel.stribBerry, 40, 10, 0, 0, 0, EnumBerryColor.GREEN, 3),
   Nutpea(1, 5, 96, 7, EnumBerryModel.nutpeaTaller, EnumBerryModel.nutpeaBloom, EnumBerryModel.nutpeaBerry, 40, 10, 0, 0, 0, EnumBerryColor.YELLOW, 3),
   Ginema(1, 5, 96, 7, EnumBerryModel.ginemaTaller, EnumBerryModel.ginemaBloom, EnumBerryModel.ginemaBerry, 40, 10, 0, 0, 0, EnumBerryColor.YELLOW, 3),
   Kuo(1, 5, 96, 7, EnumBerryModel.kuoTaller, EnumBerryModel.kuoBloom, EnumBerryModel.kuoBerry, 40, 10, 0, 0, 0, EnumBerryColor.GREEN, 3),
   Yago(1, 5, 96, 7, EnumBerryModel.yagoTaller, EnumBerryModel.yagoBloom, EnumBerryModel.yagoBerry, 40, 10, 0, 0, 0, EnumBerryColor.GREEN, 3),
   Touga(1, 5, 96, 7, EnumBerryModel.tougaTaller, EnumBerryModel.tougaBloom, EnumBerryModel.tougaBerry, 40, 10, 0, 0, 0, EnumBerryColor.RED, 3),
   Niniku(1, 5, 96, 7, EnumBerryModel.ninikuTaller, EnumBerryModel.ninikuBloom, EnumBerryModel.ninikuBerry, 40, 10, 0, 0, 0, EnumBerryColor.BLUE, 3),
   Topo(1, 5, 96, 7, EnumBerryModel.topoTaller, EnumBerryModel.topoBloom, EnumBerryModel.topoBerry, 40, 10, 0, 0, 0, EnumBerryColor.PINK, 3),
   Enigma(1, 5, 96, 7, EnumBerryModel.enigmaTaller, EnumBerryModel.enigmaBloom, EnumBerryModel.enigmaBerry, 0.22F, 0.9F, 0.55F, 40, 10, 0, 0, 0, EnumBerryColor.PURPLE, 3),
   Micle(1, 5, 96, 7, EnumBerryModel.micleTaller, EnumBerryModel.micleBloom, EnumBerryModel.micleBerry, 0.25F, 0.8F, 0.5F, 0, 40, 10, 0, 0, EnumBerryColor.GREEN, 3),
   Custap(1, 5, 96, 7, EnumBerryModel.custapTaller, EnumBerryModel.custapBloom, EnumBerryModel.custapBerry, 0.15F, 0.75F, 0.4F, 0, 0, 40, 10, 0, EnumBerryColor.RED, 3),
   Jaboca(1, 5, 96, 7, EnumBerryModel.jabocaTaller, EnumBerryModel.jabocaBloom, EnumBerryModel.jabocaBerry, 0.24F, 0.83F, 0.5F, 0, 0, 0, 40, 10, EnumBerryColor.YELLOW, 3),
   Rowap(1, 5, 96, 7, EnumBerryModel.rowapTaller, EnumBerryModel.rowapBloom, EnumBerryModel.rowapBerry, 0.15F, 0.75F, 0.6F, 10, 0, 0, 0, 40, EnumBerryColor.BLUE, 3),
   Kee(1, 5, 96, 7, EnumBerryModel.keeTaller, EnumBerryModel.keeBloom, EnumBerryModel.keeBerry, 30, 30, 10, 10, 10, EnumBerryColor.YELLOW, 3),
   Maranga(1, 5, 96, 7, EnumBerryModel.marangaTaller, EnumBerryModel.marangaBloom, EnumBerryModel.marangaBerry, 10, 10, 30, 30, 10, EnumBerryColor.BLUE, 3);

   public final byte minYield;
   public final byte maxYield;
   public final byte growthTime;
   public final byte moistureDrainRate;
   public final boolean isImplemented;
   public final float scale;
   public final float height;
   public final float width;
   public final byte spicy;
   public final byte dry;
   public final byte sweet;
   public final byte bitter;
   public final byte sour;
   public final EnumBerryColor color;
   public final byte juiceGroup;
   public final EnumBerryModel[] models;
   private static ArrayList implemented;

   public Item getBerry() {
      return Item.func_111206_d("pixelmon:" + this.name().toLowerCase() + "_berry");
   }

   private EnumBerry(int minYield, int maxYield, int growthTime, int moistureDrainRate, EnumBerryModel tallerModel, EnumBerryModel bloomModel, EnumBerryModel berryModel, float scale, float height, float width, int spicy, int dry, int sweet, int bitter, int sour, EnumBerryColor color, int juiceGroup, boolean isImplemented) {
      this.minYield = (byte)minYield;
      this.maxYield = (byte)maxYield;
      this.growthTime = (byte)growthTime;
      this.moistureDrainRate = (byte)moistureDrainRate;
      this.models = new EnumBerryModel[]{EnumBerryModel.seeded, EnumBerryModel.sprouted, tallerModel, bloomModel, berryModel};
      this.isImplemented = isImplemented;
      this.scale = scale;
      this.height = height;
      this.width = width;
      this.spicy = (byte)spicy;
      this.dry = (byte)dry;
      this.sweet = (byte)sweet;
      this.bitter = (byte)bitter;
      this.sour = (byte)sour;
      this.color = color;
      this.juiceGroup = (byte)juiceGroup;
   }

   private EnumBerry(int minYield, int maxYield, int growthTime, int moistureDrainRate, EnumBerryModel tallerModel, EnumBerryModel bloomModel, EnumBerryModel berryModel, float scale, float height, float width, int spicy, int dry, int sweet, int bitter, int sour, EnumBerryColor color, int juiceGroup) {
      this(minYield, maxYield, growthTime, moistureDrainRate, tallerModel, bloomModel, berryModel, scale, height, width, spicy, dry, sweet, bitter, sour, color, juiceGroup, true);
   }

   private EnumBerry(int minYield, int maxYield, int growthTime, int moistureDrainRate, EnumBerryModel tallerModel, EnumBerryModel bloomModel, EnumBerryModel berryModel, int spicy, int dry, int sweet, int bitter, int sour, EnumBerryColor color, int juiceGroup) {
      this(minYield, maxYield, growthTime, moistureDrainRate, tallerModel, bloomModel, berryModel, 0.3F, 0.75F, 0.6F, spicy, dry, sweet, bitter, sour, color, juiceGroup, false);
   }

   public Block getTreeBlock() {
      return Block.func_149684_b("pixelmon:berrytree_" + this.name().toLowerCase());
   }

   @Nonnull
   public static EnumBerry getImplementedBerry() {
      if (implemented == null) {
         implemented = new ArrayList();
         EnumBerry[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            EnumBerry berry = var0[var2];
            if (berry.isImplemented) {
               implemented.add(berry);
            }
         }
      }

      return (EnumBerry)RandomHelper.getRandomElementFromCollection(implemented);
   }
}
