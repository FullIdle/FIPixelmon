package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.init.SoundEvents;

public class RaidShieldsTask implements IBattleTask {
   public UUID pokemonUUID;
   public int shields;
   public int maxShields;

   /** @deprecated */
   @Deprecated
   public RaidShieldsTask() {
   }

   public RaidShieldsTask(UUID pokemonUUID, int shields, int maxShields) {
      this.pokemonUUID = pokemonUUID;
      this.shields = shields;
      this.maxShields = maxShields;
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

   private void updatePokemon(PixelmonInGui pokemon, RaidShieldsTask message) {
      if (pokemon != null && pokemon.pokemonUUID.equals(message.pokemonUUID)) {
         int currentShields = pokemon.shields;
         pokemon.shields = message.shields;
         pokemon.maxShields = message.maxShields;
         if (currentShields > pokemon.shields) {
            pokemon.lostShield = true;
         }

         if (currentShields == 0 && pokemon.shields > 0) {
            SoundHelper.playSound(SoundEvents.field_187716_o, 1.0F, 1.0F);
         }
      }

   }

   @Nullable
   public UUID getPokemonID() {
      return this.pokemonUUID;
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = UUIDHelper.readUUID(buf);
      this.shields = buf.readInt();
      this.maxShields = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.pokemonUUID, buf);
      buf.writeInt(this.shields);
      buf.writeInt(this.maxShields);
   }
}
