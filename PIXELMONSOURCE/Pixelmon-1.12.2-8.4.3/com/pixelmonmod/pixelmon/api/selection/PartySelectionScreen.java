package com.pixelmonmod.pixelmon.api.selection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.selection.OpenSelectionPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.selection.SetSelectionOptionsPacket;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class PartySelectionScreen {
   public static void openSelection(EntityPlayerMP player, BiConsumer consumer) {
      builder().party(player).consumer(consumer).sendTo(player);
   }

   public static void openSelection(EntityPlayerMP player, BiConsumer consumer, Pokemon... options) {
      builder().options(options).consumer(consumer).sendTo(player);
   }

   public static void openSelection(EntityPlayerMP player, int selections, BiConsumer consumer, Pokemon... options) {
      builder().selections(selections).consumer(consumer).options(options).sendTo(player);
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {
      private ITextComponent title = new TextComponentString("");
      private ITextComponent text = new TextComponentString("");
      private List options = Lists.newArrayList();
      private int selections = 6;
      private boolean allowExit = false;
      private BiConsumer consumer = null;

      public Builder title(ITextComponent title) {
         this.title = (ITextComponent)Preconditions.checkNotNull(title, "title");
         return this;
      }

      public Builder title(String title) {
         this.title = new TextComponentString((String)Preconditions.checkNotNull(title, "title"));
         return this;
      }

      public Builder text(ITextComponent text) {
         this.text = (ITextComponent)Preconditions.checkNotNull(text, "text");
         return this;
      }

      public Builder text(String text) {
         this.text = new TextComponentString((String)Preconditions.checkNotNull(text, "text"));
         return this;
      }

      public Builder selections(int selections) {
         if (selections > 6) {
            this.selections = 6;
         } else {
            this.selections = Math.max(selections, 1);
         }

         return this;
      }

      public Builder option(Pokemon pokemon) {
         return this.options(pokemon);
      }

      public Builder options(Pokemon... pokemons) {
         return this.options((List)Lists.newArrayList(pokemons));
      }

      public Builder options(List options) {
         if (this.options.size() + options.size() > 6) {
            return this;
         } else {
            this.options.addAll(options);
            return this;
         }
      }

      public Builder clearOptions() {
         this.options.clear();
         return this;
      }

      public Builder party(EntityPlayer player) {
         this.options.clear();
         this.options(Pixelmon.storageManager.getParty(player.func_110124_au()).getAll());
         return this;
      }

      public Builder consumer(BiConsumer consumer) {
         this.consumer = consumer;
         return this;
      }

      public Builder enforceSelection() {
         this.allowExit = false;
         return this;
      }

      public Builder allowExit() {
         this.allowExit = true;
         return this;
      }

      public void sendTo(EntityPlayerMP player) {
         PartySelectionFactory.beginSelection(player, this.consumer, this.options);
         Pixelmon.network.sendTo(new OpenSelectionPacket(this.title, this.text, this.selections, this.allowExit), player);

         for(int i = 0; i < this.options.size(); ++i) {
            Pixelmon.network.sendTo(new SetSelectionOptionsPacket(i, (Pokemon)this.options.get(i)), player);
         }

      }
   }
}
