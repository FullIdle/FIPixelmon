package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.events.dialogue.DialogueChoiceEvent;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DialogueChoiceMade implements IMessage {
   private Choice choice;

   public DialogueChoiceMade() {
   }

   public DialogueChoiceMade(Choice choice) {
      this.choice = choice;
   }

   public void fromBytes(ByteBuf buffer) {
      this.choice = new Choice(buffer);
   }

   public void toBytes(ByteBuf buffer) {
      this.choice.toBytes(buffer);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DialogueChoiceMade message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            DialogueChoiceEvent event = new DialogueChoiceEvent(ctx.getServerHandler().field_147369_b, message.choice);

            try {
               if (Choice.handleMap.containsKey(event.player.func_110124_au()) && ((HashMap)Choice.handleMap.get(event.player.func_110124_au())).get(event.choice.choiceID) != null) {
                  ((Consumer)((HashMap)Choice.handleMap.get(event.player.func_110124_au())).get(event.choice.choiceID)).accept(event);
               }

               if (!event.isCanceled()) {
                  Pixelmon.EVENT_BUS.post(event);
               }

               Choice.handleMap.keySet().removeIf((uuid) -> {
                  return ctx.getServerHandler().field_147369_b.func_184102_h().func_184103_al().func_177451_a(uuid) == null;
               });
            } catch (Exception var4) {
               var4.printStackTrace();
               event.setAction(DialogueNextAction.DialogueGuiAction.CLOSE);
               event.setNewDialogues(new ArrayList());
            }

            Pixelmon.network.sendTo(new DialogueNextAction(event.getAction(), event.getNewDialogues()), ctx.getServerHandler().field_147369_b);
         });
         return null;
      }
   }
}
