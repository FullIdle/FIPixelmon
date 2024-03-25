package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CardBackRegistry {
   private static final ConcurrentMap REGISTRY = Maps.newConcurrentMap();
   public static final CardBack STANDARD = register(new CardBack("Standard", "Ivory", new ResourceLocation("tcg:gui/cards/backs/default.png")));
   public static final CardBack MIDNIGHT = register(new CardBack("Midnight Star", "TheMidnightMage", new ResourceLocation("tcg:gui/cards/backs/themidnightmage.png")));
   public static final CardBack MRM = register(new CardBack("La Charizard Glorieuse Dans L'herbe Sauvage", "MrM", new ResourceLocation("tcg:gui/cards/backs/mrm.png")));
   public static final CardBack ARTICUNOSHINING = register(new CardBack("Shining Articuno", "Ivory", new ResourceLocation("tcg:gui/cards/backs/shiningarticuno.png")));
   public static final CardBack BUG = register(new CardBack("Bug", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/bug.png")));
   public static final CardBack DARK = register(new CardBack("Dark", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/dark.png")));
   public static final CardBack DRAGON = register(new CardBack("Dragon", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/dragon.png")));
   public static final CardBack ELECTRIC = register(new CardBack("Electric", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/electric.png")));
   public static final CardBack FAIRY = register(new CardBack("Fairy", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/fairy.png")));
   public static final CardBack FIGHTING = register(new CardBack("Fighting", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/fighting.png")));
   public static final CardBack FIRE = register(new CardBack("Fire", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/fire.png")));
   public static final CardBack FLYING = register(new CardBack("Flying", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/flying.png")));
   public static final CardBack GHOST = register(new CardBack("Ghost", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/ghost.png")));
   public static final CardBack RAINBOW = register(new CardBack("Rainbow", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/rainbow.png")));
   public static final CardBack GRASS = register(new CardBack("Grass", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/grass.png")));
   public static final CardBack OLD_REFORGED = register(new CardBack("Old Reforged", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/reforged_old.png")));
   public static final CardBack GROUND = register(new CardBack("Ground", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/ground.png")));
   public static final CardBack ICE = register(new CardBack("Ice", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/ice.png")));
   public static final CardBack NORMAL = register(new CardBack("Normal", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/normal.png")));
   public static final CardBack POISON = register(new CardBack("Poison", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/poison.png")));
   public static final CardBack COTTONCANDY = register(new CardBack("Cotton Candy", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/cottoncandy.png")));
   public static final CardBack LIME = register(new CardBack("Lime", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/lime.png")));
   public static final CardBack OCEAN = register(new CardBack("Ocean", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/ocean.png")));
   public static final CardBack ORANGE = register(new CardBack("Orange", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/orange.png")));
   public static final CardBack PSYCHIC = register(new CardBack("Psychic", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/psychic.png")));
   public static final CardBack ROCK = register(new CardBack("Rock", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/rock.png")));
   public static final CardBack SUNRISE = register(new CardBack("Sunrise", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/sunrise.png")));
   public static final CardBack SUNSET = register(new CardBack("Sunset", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/sunset.png")));
   public static final CardBack BLACK = register(new CardBack("Black", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/black.png")));
   public static final CardBack GALAXYREFORGED = register(new CardBack("Galaxy Reforged", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/galaxyreforged.png")));
   public static final CardBack GALAXYRAYQUAZA = register(new CardBack("Galaxy Rayquaza", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/galaxyray.png")));
   public static final CardBack GREATBALL = register(new CardBack("Greatball", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/greatball.png")));
   public static final CardBack LOVEBALL = register(new CardBack("Loveball", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/loveball.png")));
   public static final CardBack MASTERBALL = register(new CardBack("Masterball", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/masterball.png")));
   public static final CardBack POKEBALL = register(new CardBack("Pokeball", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/pokeball.png")));
   public static final CardBack PURPLE = register(new CardBack("Purple", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/purple.png")));
   public static final CardBack RED = register(new CardBack("Red", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/red.png")));
   public static final CardBack STEEL = register(new CardBack("Steel", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/steel.png")));
   public static final CardBack ULTRABALL = register(new CardBack("Ultraball", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/ultraball.png")));
   public static final CardBack WATER = register(new CardBack("Water", "mezzbeans", new ResourceLocation("tcg:gui/cards/backs/water.png")));

   public static CardBack register(CardBack t) {
      REGISTRY.put(t.getName().toLowerCase().replace(" ", "_"), t);
      return t;
   }

   public static CardBack fromNBT(NBTTagCompound tag) {
      return get(tag.func_74779_i("CardBackID"));
   }

   public static List getAll() {
      return Lists.newArrayList(REGISTRY.values());
   }

   public static CardBack get(String name) {
      return (CardBack)REGISTRY.get(name.toLowerCase().replace(" ", "_"));
   }

   public static CardBack getRandomCardBack() {
      return (CardBack)RandomHelper.getRandomElementFromCollection(REGISTRY.values());
   }

   public static void load() {
   }
}
