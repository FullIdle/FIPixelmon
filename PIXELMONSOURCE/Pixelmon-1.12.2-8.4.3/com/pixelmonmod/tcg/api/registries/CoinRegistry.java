package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.api.card.personalization.Coin;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CoinRegistry {
   private static final ConcurrentMap REGISTRY = Maps.newConcurrentMap();
   public static final Coin CHARMANDER = register(new Coin("Charmander", "Pokenerd", new ResourceLocation("tcg", "gui/coins/heads/charmander.png"), new ResourceLocation("tcg", "gui/coins/tails/default.png")));
   public static final Coin BULBASAUR = register(new Coin("Bulbasaur", "Pokenerd", new ResourceLocation("tcg", "gui/coins/heads/bulbasaur.png"), new ResourceLocation("tcg", "gui/coins/tails/default.png")));
   public static final Coin SQUIRTLE = register(new Coin("Squirtle", "Pokenerd", new ResourceLocation("tcg", "gui/coins/heads/squirtle.png"), new ResourceLocation("tcg", "gui/coins/tails/default.png")));
   public static final Coin PIKACHU = register(new Coin("Pikachu", "Pokenerd", new ResourceLocation("tcg", "gui/coins/heads/pikachu.png"), new ResourceLocation("tcg", "gui/coins/tails/default.png")));
   public static final Coin JIGGLYPUFF = register(new Coin("Jigglypuff", "Pokenerd", new ResourceLocation("tcg", "gui/coins/heads/jigglypuff.png"), new ResourceLocation("tcg", "gui/coins/tails/default.png")));

   public static Coin register(Coin t) {
      REGISTRY.put(t.getName().toLowerCase().replace(" ", "_"), t);
      return t;
   }

   public static Coin fromNBT(NBTTagCompound tag) {
      return get(tag.func_74779_i("CoinID"));
   }

   public static List getAll() {
      return Lists.newArrayList(REGISTRY.values());
   }

   public static Coin get(String name) {
      return (Coin)REGISTRY.get(name.toLowerCase().replace(" ", "_"));
   }

   public static Coin getRandomCoin() {
      return (Coin)RandomHelper.getRandomElementFromCollection(REGISTRY.values());
   }

   public static void load() {
   }
}
