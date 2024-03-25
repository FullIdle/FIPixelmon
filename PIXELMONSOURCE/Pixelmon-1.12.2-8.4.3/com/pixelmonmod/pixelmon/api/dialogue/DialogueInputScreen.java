package com.pixelmonmod.pixelmon.api.dialogue;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.OpenDialogueInput;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class DialogueInputScreen {
   public static void openDialogueInput(EntityPlayerMP player, ITextComponent title, ITextComponent text) {
      Builder builder = builder().setTitle(title).setText(text);
      builder.sendTo(player);
   }

   public static Builder builder() {
      return new Builder();
   }

   public static Builder builder(ITextComponent title) {
      return (new Builder()).setTitle(title);
   }

   public static class Builder {
      private ITextComponent title = new TextComponentString("");
      private ITextComponent text = null;
      private String defaultText = "";

      public Builder setTitle(ITextComponent title) {
         this.title = (ITextComponent)Preconditions.checkNotNull(title, "title");
         return this;
      }

      public Builder setTitle(String title) {
         this.title = new TextComponentString((String)Preconditions.checkNotNull(title, "title"));
         return this;
      }

      public Builder setText(ITextComponent text) {
         this.text = (ITextComponent)Preconditions.checkNotNull(text, "text");
         return this;
      }

      public Builder setText(String text) {
         this.text = new TextComponentString((String)Preconditions.checkNotNull(text, "text"));
         return this;
      }

      public Builder setDefaultText(String defaultText) {
         this.defaultText = (String)Preconditions.checkNotNull(defaultText, "defaultText");
         return this;
      }

      public void sendTo(EntityPlayerMP player) {
         Pixelmon.network.sendTo(new OpenDialogueInput(this.title, this.text, this.defaultText), player);
      }
   }
}
