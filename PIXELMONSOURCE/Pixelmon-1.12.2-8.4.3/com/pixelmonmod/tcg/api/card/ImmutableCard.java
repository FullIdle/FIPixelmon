package com.pixelmonmod.tcg.api.card;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.trainer.BaseTrainerEffect;
import com.pixelmonmod.tcg.duel.trainer.Bill;
import com.pixelmonmod.tcg.duel.trainer.ClefairyDoll;
import com.pixelmonmod.tcg.duel.trainer.ComputerSearch;
import com.pixelmonmod.tcg.duel.trainer.Defender;
import com.pixelmonmod.tcg.duel.trainer.DevolutionSpray;
import com.pixelmonmod.tcg.duel.trainer.EnergyRemoval;
import com.pixelmonmod.tcg.duel.trainer.EnergyRetrieval;
import com.pixelmonmod.tcg.duel.trainer.EnergySearch;
import com.pixelmonmod.tcg.duel.trainer.FullHeal;
import com.pixelmonmod.tcg.duel.trainer.Gambler;
import com.pixelmonmod.tcg.duel.trainer.GustOfWind;
import com.pixelmonmod.tcg.duel.trainer.ImposterProfOak;
import com.pixelmonmod.tcg.duel.trainer.ItemFinder;
import com.pixelmonmod.tcg.duel.trainer.Lass;
import com.pixelmonmod.tcg.duel.trainer.Maintenance;
import com.pixelmonmod.tcg.duel.trainer.MrFuji;
import com.pixelmonmod.tcg.duel.trainer.MysteriousFossil;
import com.pixelmonmod.tcg.duel.trainer.PlusPower;
import com.pixelmonmod.tcg.duel.trainer.PokeBall;
import com.pixelmonmod.tcg.duel.trainer.PokeFlute;
import com.pixelmonmod.tcg.duel.trainer.Pokedex;
import com.pixelmonmod.tcg.duel.trainer.PokemonBreeder;
import com.pixelmonmod.tcg.duel.trainer.PokemonCenter;
import com.pixelmonmod.tcg.duel.trainer.PokemonTrader;
import com.pixelmonmod.tcg.duel.trainer.Potion;
import com.pixelmonmod.tcg.duel.trainer.ProfOak;
import com.pixelmonmod.tcg.duel.trainer.Recycle;
import com.pixelmonmod.tcg.duel.trainer.Revive;
import com.pixelmonmod.tcg.duel.trainer.ScoopUp;
import com.pixelmonmod.tcg.duel.trainer.SuperEnergyRemoval;
import com.pixelmonmod.tcg.duel.trainer.SuperPotion;
import com.pixelmonmod.tcg.duel.trainer.Switch;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ImmutableCard {
   private static final ResourceLocation FALL_BACK_TEXTURE = new ResourceLocation("tcg", "gui/cards/foreground/fallback.png");
   public static int FACE_DOWN_ID = -1;
   public static String CARD_ID_NBT_KEY = "CardID";
   public static String IS_HOLO_NBT_KEY = "ReverseHolo";
   private int id;
   private int setId;
   private CardType cardType;
   private boolean isSpecial;
   private int hp;
   private Energy energy1;
   private Energy energy2;
   private int pokemonId;
   private String code;
   private String name;
   private String description;
   private int previousPokemonId;
   private AbilityCard cardAbility;
   private AttackCard[] cardAttacks;
   private Energy energyResistance;
   private int resistanceValue;
   private CardModifier resistanceCardModifier;
   private Energy energyWeakness;
   private int weaknessValue;
   private CardModifier weaknessCardModifier;
   private int retreatCost;
   private CardRarity cardRarity;
   private ResourceLocation customTexture;
   private int customTextureX;
   private int customTextureY;
   private BaseTrainerEffect effect;

   public ImmutableCard() {
      this.id = FACE_DOWN_ID;
      this.setId = 0;
      this.cardType = null;
      this.isSpecial = false;
      this.hp = 0;
      this.energy1 = null;
      this.energy2 = null;
      this.pokemonId = 0;
      this.code = null;
      this.name = null;
      this.description = null;
      this.previousPokemonId = 0;
      this.cardAbility = null;
      this.cardAttacks = null;
      this.energyResistance = null;
      this.resistanceValue = 0;
      this.resistanceCardModifier = null;
      this.energyWeakness = null;
      this.weaknessValue = 0;
      this.weaknessCardModifier = null;
      this.retreatCost = 0;
      this.cardRarity = null;
      this.customTexture = null;
      this.customTextureX = 0;
      this.customTextureY = 0;
   }

   public boolean isHolo() {
      return this.cardRarity == CardRarity.HOLORARE;
   }

   public void calculateTrainerEffect() {
      this.effect = this.attachTrainerEffect();
   }

   private BaseTrainerEffect attachTrainerEffect() {
      switch (this.id) {
         case 70:
            return new ClefairyDoll();
         case 71:
            return new ComputerSearch();
         case 72:
            return new DevolutionSpray();
         case 73:
            return new ImposterProfOak();
         case 74:
            return new ItemFinder();
         case 75:
            return new Lass();
         case 76:
            return new PokemonBreeder();
         case 77:
            return new PokemonTrader();
         case 78:
            return new ScoopUp();
         case 79:
            return new SuperEnergyRemoval();
         case 80:
            return new Defender();
         case 81:
            return new EnergyRetrieval();
         case 82:
            return new FullHeal();
         case 83:
            return new Maintenance();
         case 84:
            return new PlusPower();
         case 85:
            return new PokemonCenter();
         case 86:
            return new PokeFlute();
         case 87:
            return new Pokedex();
         case 88:
            return new ProfOak();
         case 89:
            return new Revive();
         case 90:
            return new SuperPotion();
         case 91:
            return new Bill();
         case 92:
            return new EnergyRemoval();
         case 93:
            return new GustOfWind();
         case 94:
            return new Potion();
         case 95:
            return new Switch();
         case 166:
            return new PokeBall();
         case 224:
            return new MrFuji();
         case 225:
            return new EnergySearch();
         case 226:
            return new Gambler();
         case 227:
            return new Recycle();
         case 228:
            return new MysteriousFossil();
         default:
            return null;
      }
   }

   public int getID() {
      return this.id;
   }

   public int getSetID() {
      return this.setId;
   }

   public String getCode() {
      return this.code;
   }

   public int getHP() {
      return this.hp;
   }

   public CardType getCardType() {
      return this.cardType;
   }

   public boolean isSpecial() {
      return this.isSpecial;
   }

   public BaseTrainerEffect getEffect() {
      return this.effect;
   }

   public boolean isPokemonCard() {
      switch (this.cardType) {
         case BASIC:
         case STAGE1:
         case STAGE2:
         case LVLX:
         case EX:
         case BREAK:
         case LEGEND:
         case MEGA:
            return true;
         default:
            return false;
      }
   }

   public boolean isEvolution() {
      switch (this.cardType) {
         case STAGE1:
         case STAGE2:
         case MEGA:
            return true;
         default:
            return false;
      }
   }

   public boolean isCosmetic() {
      return this.cardType.isCosmetic();
   }

   public Energy getMainEnergy() {
      return this.energy1;
   }

   public Energy getSecondaryEnergy() {
      return this.energy2;
   }

   public int getPokemonID() {
      return this.pokemonId;
   }

   public String getName() {
      return this.name;
   }

   public String getTranslatedName() {
      return LanguageMapTCG.translateKey(this.getName().toLowerCase());
   }

   public String getDescription() {
      return this.description;
   }

   public int getPrevEvoID() {
      return this.previousPokemonId;
   }

   public AbilityCard getAbility() {
      return this.cardAbility;
   }

   public AttackCard[] getAttacks() {
      return this.cardAttacks;
   }

   public Energy getEnergyResistance() {
      return this.energyResistance;
   }

   public int getResistanceValue() {
      return this.resistanceValue;
   }

   public CardModifier getResistanceModifier() {
      return this.resistanceCardModifier;
   }

   public Energy getEnergyWeakness() {
      return this.energyWeakness;
   }

   public int getWeaknessValue() {
      return this.weaknessValue;
   }

   public CardModifier getWeaknessModifier() {
      return this.weaknessCardModifier;
   }

   public int getRetreatCost() {
      return this.retreatCost;
   }

   public CardRarity getRarity() {
      return this.cardRarity;
   }

   public boolean getHasCustomTex() {
      return this.customTexture != null;
   }

   public ResourceLocation getCustomTexture() {
      return this.customTexture != null && this.customTexture.func_110624_b() == null ? FALL_BACK_TEXTURE : this.customTexture;
   }

   public int getImageXFromCard() {
      return this.customTextureX;
   }

   public int getImageYFromCard() {
      return this.customTextureY;
   }

   public ItemStack getItemStack(int count) {
      ItemStack itemStack = new ItemStack(TCG.itemCard, count);
      NBTTagCompound tag = new NBTTagCompound();
      itemStack.func_151001_c(this.getTranslatedName());
      this.write(tag);
      itemStack.func_77982_d(tag);
      return itemStack;
   }

   public void write(NBTTagCompound tag) {
      tag.func_74768_a(CARD_ID_NBT_KEY, this.id);
   }
}
