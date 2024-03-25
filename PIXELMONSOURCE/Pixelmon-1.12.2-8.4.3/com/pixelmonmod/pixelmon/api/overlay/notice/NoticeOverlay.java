package com.pixelmonmod.pixelmon.api.overlay.notice;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.OverlayGraphicType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.custom.overlays.CustomNoticePacket;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NoticeOverlay {
   public static void hide(EntityPlayerMP player) {
      CustomNoticePacket packet = new CustomNoticePacket();
      packet.setEnabled(false);
      Pixelmon.network.sendTo(packet, player);
   }

   public static Builder builder() {
      return new Builder();
   }

   /** @deprecated */
   @Deprecated
   public static Builder builder(EnumOverlayLayout iconLayout, String line, String... extraLines) {
      return new Builder(iconLayout, line, extraLines);
   }

   public static class Builder {
      protected List lines = null;
      protected EnumOverlayLayout layout = null;
      protected OverlayGraphicType type = null;
      protected PokemonSpec spec = null;
      protected ItemStack itemStack = null;

      protected Builder() {
      }

      /** @deprecated */
      @Deprecated
      public Builder(EnumOverlayLayout layout, String line, String... extraLines) {
         this.setLayout(layout);
         this.setLines((List)Stream.concat(Stream.of(line), Arrays.stream(extraLines)).collect(Collectors.toList()));
      }

      public Builder setPokemonSprite(PokemonSpec spec) {
         this.spec = (PokemonSpec)Preconditions.checkNotNull(spec, "spec");
         this.type = OverlayGraphicType.PokemonSprite;
         return this;
      }

      public Builder setPokemon3D(PokemonSpec spec) {
         this.spec = (PokemonSpec)Preconditions.checkNotNull(spec, "spec");
         this.type = OverlayGraphicType.Pokemon3D;
         return this;
      }

      public Builder setItemStack(ItemStack itemStack) {
         if (itemStack.func_190926_b()) {
            return this.setEmpty();
         } else {
            this.itemStack = (ItemStack)Preconditions.checkNotNull(itemStack, "itemStack");
            this.type = OverlayGraphicType.ItemStack;
            return this;
         }
      }

      public Builder setEmpty() {
         this.type = null;
         return this;
      }

      /** @deprecated */
      @Deprecated
      public Builder setIconToPokemonSprite(EnumSpecies pokemon) {
         this.setPokemonSprite(new PokemonSpec(pokemon.name));
         return this;
      }

      /** @deprecated */
      @Deprecated
      public Builder setIconToPokemonModel(EnumSpecies pokemon, float scale) {
         return this.setPokemon3D(new PokemonSpec(pokemon.name));
      }

      /** @deprecated */
      @Deprecated
      public Builder setIconToItemSprite(Item item) {
         return this.setItemStack(new ItemStack(item));
      }

      /** @deprecated */
      @Deprecated
      public Builder setIconToItemModel(Item item) {
         return this.setItemStack(new ItemStack(item));
      }

      public Builder setLayout(EnumOverlayLayout layout) {
         this.layout = (EnumOverlayLayout)Preconditions.checkNotNull(layout, "layout");
         return this;
      }

      public Builder addLine(String line) {
         this.lines.add(Preconditions.checkNotNull(line, "line"));
         return this;
      }

      public Builder addLines(List lines) {
         this.lines.addAll((Collection)Preconditions.checkNotNull(lines, "lines"));
         return this;
      }

      public Builder addLines(String... lines) {
         return this.addLines(Arrays.asList((Object[])Preconditions.checkNotNull(lines, "lines")));
      }

      public Builder setLines(@Nullable List lines) {
         this.lines = lines != null && !lines.isEmpty() ? new ArrayList(lines) : null;
         return this;
      }

      public Builder setLines(@Nullable String... lines) {
         this.lines = lines != null && lines.length != 0 ? new ArrayList(Arrays.asList(lines)) : null;
         return this;
      }

      public CustomNoticePacket build() {
         CustomNoticePacket packet = new CustomNoticePacket();
         if (this.lines != null && !this.lines.isEmpty()) {
            packet.setLines((String[])this.lines.toArray(new String[0]));
         }

         if (this.type != null) {
            Preconditions.checkArgument(this.layout != null, "You must specify the layout when using a sprite.");
            switch (this.type) {
               case PokemonSprite:
                  packet.setPokemonSprite(this.spec, this.layout);
                  break;
               case Pokemon3D:
                  packet.setPokemon3D(this.spec, this.layout);
                  break;
               case ItemStack:
                  packet.setItemStack(this.itemStack, this.layout);
            }
         }

         return packet;
      }

      public void sendTo(EntityPlayerMP player) {
         Pixelmon.network.sendTo(this.build(), player);
      }
   }
}
