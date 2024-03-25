package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import io.netty.buffer.ByteBuf;

public abstract class DuelLogParameters {
   public abstract void write(ByteBuf var1, GamePhase var2, int var3, int var4, boolean var5);
}
