package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;

public class HPIncreaseTask implements IBattleTask {
   public UUID pokemonUUID;
   public int level;
   public int currentHP;
   public int maxHP;

   /** @deprecated */
   @Deprecated
   public HPIncreaseTask() {
   }

   public HPIncreaseTask(UUID pokemonUUID, int level, int currentHP, int maxHP) {
      this.pokemonUUID = pokemonUUID;
      this.level = level;
      this.currentHP = currentHP;
      this.maxHP = maxHP;
   }

   public boolean process(ClientBattleManager bm) {
      PixelmonInGui[] var2 = ClientProxy.battleManager.displayedOurPokemon;
      int var3 = var2.length;

      int var4;
      PixelmonInGui pokemon;
      for(var4 = 0; var4 < var3; ++var4) {
         pokemon = var2[var4];
         this.updatePokemon(pokemon, this);
      }

      var2 = ClientProxy.battleManager.displayedEnemyPokemon;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         pokemon = var2[var4];
         this.updatePokemon(pokemon, this);
      }

      Iterator var6 = ClientProxy.battleManager.fullOurPokemon.iterator();

      while(var6.hasNext()) {
         PixelmonInGui pokemon = (PixelmonInGui)var6.next();
         this.updatePokemon(pokemon, this);
      }

      if (ClientProxy.battleManager.displayedAllyPokemon != null) {
         var2 = ClientProxy.battleManager.displayedAllyPokemon;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            pokemon = var2[var4];
            this.updatePokemon(pokemon, this);
         }
      }

      return false;
   }

   private void updatePokemon(PixelmonInGui pokemon, HPIncreaseTask message) {
      if (pokemon != null && pokemon.pokemonUUID.equals(message.pokemonUUID)) {
         pokemon.level = message.level;
         pokemon.health = (float)message.currentHP;
         pokemon.maxHealth = message.maxHP;
      }

   }

   @Nullable
   public UUID getPokemonID() {
      return this.pokemonUUID;
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = UUIDHelper.readUUID(buf);
      this.level = buf.readInt();
      this.currentHP = buf.readInt();
      this.maxHP = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.pokemonUUID, buf);
      buf.writeInt(this.level);
      buf.writeInt(this.currentHP);
      buf.writeInt(this.maxHP);
   }
}
