package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public enum EnumAlcremie implements IEnumForm {
   STRAWBERRY(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   STRAWBERRYRUBYCREAM(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   STRAWBERRYMATCHA(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   STRAWBERRYMINT(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.MINT),
   STRAWBERRYLEMON(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.LEMON),
   STRAWBERRYSALTED(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.SALTED),
   STRAWBERRYRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   STRAWBERRYCARAMEL(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   STRAWBERRYRAINBOW(EnumAlcremie.EnumAlcremieModel.STRAWBERRY, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   BERRY(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   BERRYRUBYCREAM(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   BERRYMATCHA(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   BERRYMINT(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.MINT),
   BERRYLEMON(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.LEMON),
   BERRYSALTED(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.SALTED),
   BERRYRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   BERRYCARAMEL(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   BERRYRAINBOW(EnumAlcremie.EnumAlcremieModel.BERRY, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   LOVE(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   LOVERUBYCREAM(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   LOVEMATCHA(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   LOVEMINT(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.MINT),
   LOVELEMON(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.LEMON),
   LOVESALTED(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.SALTED),
   LOVERUBYSWIRL(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   LOVECARAMEL(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   LOVERAINBOW(EnumAlcremie.EnumAlcremieModel.LOVE, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   STAR(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   STARRUBYCREAM(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   STARMATCHA(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   STARMINT(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.MINT),
   STARLEMON(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.LEMON),
   STARSALTED(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.SALTED),
   STARRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   STARCARAMEL(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   STARRAINBOW(EnumAlcremie.EnumAlcremieModel.STAR, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   CLOVER(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   CLOVERRUBYCREAM(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   CLOVERMATCHA(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   CLOVERMINT(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.MINT),
   CLOVERLEMON(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.LEMON),
   CLOVERSALTED(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.SALTED),
   CLOVERRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   CLOVERCARAMEL(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   CLOVERRAINBOW(EnumAlcremie.EnumAlcremieModel.CLOVER, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   FLOWER(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   FLOWERRUBYCREAM(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   FLOWERMATCHA(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   FLOWERMINT(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.MINT),
   FLOWERLEMON(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.LEMON),
   FLOWERSALTED(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.SALTED),
   FLOWERRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   FLOWERCARAMEL(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   FLOWERRAINBOW(EnumAlcremie.EnumAlcremieModel.FLOWER, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   RIBBON(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.VANILLA),
   RIBBONRUBYCREAM(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.RUBYCREAM),
   RIBBONMATCHA(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.MATCHA),
   RIBBONMINT(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.MINT),
   RIBBONLEMON(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.LEMON),
   RIBBONSALTED(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.SALTED),
   RIBBONRUBYSWIRL(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.RUBYSWIRL),
   RIBBONCARAMEL(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.CARAMEL),
   RIBBONRAINBOW(EnumAlcremie.EnumAlcremieModel.RIBBON, EnumAlcremie.EnumAlcremieTexture.RAINBOW),
   PUMPKIN(EnumAlcremie.EnumAlcremieModel.PUMPKIN, EnumAlcremie.EnumAlcremieTexture.PUMPKIN),
   GMAX(EnumAlcremie.EnumAlcremieModel.GMAX, EnumAlcremie.EnumAlcremieTexture.GMAX);

   private static final List strawberryList = Lists.newArrayList(new EnumAlcremie[]{STRAWBERRY, STRAWBERRYRUBYCREAM, STRAWBERRYMATCHA, STRAWBERRYMINT, STRAWBERRYLEMON, STRAWBERRYSALTED, STRAWBERRYRUBYSWIRL, STRAWBERRYCARAMEL, STRAWBERRYRAINBOW});
   private static final List berryList = Lists.newArrayList(new EnumAlcremie[]{BERRY, BERRYRUBYCREAM, BERRYMATCHA, BERRYMINT, BERRYLEMON, BERRYSALTED, BERRYRUBYSWIRL, BERRYCARAMEL, BERRYRAINBOW});
   private static final List loveList = Lists.newArrayList(new EnumAlcremie[]{LOVE, LOVERUBYCREAM, LOVEMATCHA, LOVEMINT, LOVELEMON, LOVESALTED, LOVERUBYSWIRL, LOVECARAMEL, LOVERAINBOW});
   private static final List starList = Lists.newArrayList(new EnumAlcremie[]{STAR, STARRUBYCREAM, STARMATCHA, STARMINT, STARLEMON, STARSALTED, STARRUBYSWIRL, STARCARAMEL, STARRAINBOW});
   private static final List cloverList = Lists.newArrayList(new EnumAlcremie[]{CLOVER, CLOVERRUBYCREAM, CLOVERMATCHA, CLOVERMINT, CLOVERLEMON, CLOVERSALTED, CLOVERRUBYSWIRL, CLOVERCARAMEL, CLOVERRAINBOW});
   private static final List flowerList = Lists.newArrayList(new EnumAlcremie[]{FLOWER, FLOWERRUBYCREAM, FLOWERMATCHA, FLOWERMINT, FLOWERLEMON, FLOWERSALTED, FLOWERRUBYSWIRL, FLOWERCARAMEL, FLOWERRAINBOW});
   private static final List ribbonList = Lists.newArrayList(new EnumAlcremie[]{RIBBON, RIBBONRUBYCREAM, RIBBONMATCHA, RIBBONMINT, RIBBONLEMON, RIBBONSALTED, RIBBONRUBYSWIRL, RIBBONCARAMEL, RIBBONRAINBOW});
   private final EnumAlcremieModel model;
   private final EnumAlcremieTexture texture;

   public static List getStrawberryList() {
      return ImmutableList.copyOf(strawberryList);
   }

   public static List getBerryList() {
      return ImmutableList.copyOf(berryList);
   }

   public static List getLoveList() {
      return ImmutableList.copyOf(loveList);
   }

   public static List getStarList() {
      return ImmutableList.copyOf(starList);
   }

   public static List getCloverList() {
      return ImmutableList.copyOf(cloverList);
   }

   public static List getFlowerList() {
      return ImmutableList.copyOf(flowerList);
   }

   public static List getRibbonList() {
      return ImmutableList.copyOf(ribbonList);
   }

   private EnumAlcremie(EnumAlcremieModel model, EnumAlcremieTexture texture) {
      this.model = model;
      this.texture = texture;
   }

   private String getBase_(boolean isShiny) {
      return isShiny ? "shinyalcremie" : "alcremie";
   }

   private String getModel_(boolean isShiny) {
      return "-" + this.model.name().toLowerCase();
   }

   private String getTexture_(boolean isShiny) {
      return isShiny ? "" : "-" + this.texture.name().toLowerCase();
   }

   public String getTexture() {
      return this.getBase_(false) + (this == GMAX ? "-gmax" : this.getModel_(false) + this.getTexture_(false)) + ".png";
   }

   public String getTexture(boolean isShiny) {
      return this.getBase_(isShiny) + this.getModel_(isShiny) + this.getTexture_(isShiny) + ".png";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == GMAX;
   }

   public boolean isDefaultForm() {
      return this != PUMPKIN && this != GMAX;
   }

   public String getUnlocalizedName() {
      return this == GMAX ? "pixelmon.generic.form.gigantamax" : "pixelmon.alcremie.form." + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return this == GMAX ? "-gmax" : this.getModel_(false) + this.getTexture_(false);
   }

   public String getFormSuffix(boolean isShiny) {
      return this == GMAX ? "-gmax" : this.getModel_(isShiny) + this.getTexture_(isShiny);
   }

   public String getName() {
      return this.name();
   }

   public static enum EnumAlcremieTexture {
      VANILLA,
      RUBYCREAM,
      MATCHA,
      MINT,
      LEMON,
      SALTED,
      RUBYSWIRL,
      CARAMEL,
      RAINBOW,
      PUMPKIN,
      GMAX;
   }

   public static enum EnumAlcremieModel {
      STRAWBERRY,
      BERRY,
      LOVE,
      STAR,
      CLOVER,
      FLOWER,
      RIBBON,
      PUMPKIN,
      GMAX;
   }
}
