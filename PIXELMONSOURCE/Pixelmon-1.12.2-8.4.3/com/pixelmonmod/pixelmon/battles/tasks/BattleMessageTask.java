package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

public class BattleMessageTask implements IBattleMessage {
   private ITextComponent component;

   /** @deprecated */
   @Deprecated
   public BattleMessageTask() {
   }

   public BattleMessageTask(ITextComponent component) {
      this.component = component;
   }

   public boolean process(ClientBattleManager bm) {
      return false;
   }

   public ITextComponent getMessage() {
      return this.component;
   }

   @Nullable
   public UUID getPokemonID() {
      return null;
   }

   public void fromBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);

      try {
         this.component = Serializer.func_150699_a(pb.func_150789_c(32767));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void toBytes(ByteBuf buf) {
      PacketBuffer pb = new PacketBuffer(buf);

      try {
         pb.func_180714_a(Serializer.func_150696_a(this.component));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
