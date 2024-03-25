package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SetNicknameEvent extends Event {
   public final EntityPlayerMP player;
   public final Pokemon pokemon;
   public String nickname;

   public SetNicknameEvent(EntityPlayerMP player, Pokemon pokemon, String nickname) {
      this.player = player;
      this.pokemon = pokemon;
      this.nickname = nickname;
   }
}
