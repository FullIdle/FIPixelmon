package com.pixelmonmod.pixelmon.util;

import io.netty.buffer.ByteBuf;

public interface IEncodeable {
   void encodeInto(ByteBuf var1);

   void decodeInto(ByteBuf var1);
}
