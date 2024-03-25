package com.pixelmonmod.pixelmon.api.screens;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.badgecase.OpenBadgeCasePacket;
import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CustomBadgeCaseScreen {
   public static void openDisplayCase(EntityPlayerMP player, String name, EnumBadgeCase color, List badges, List pokemon) {
      builder().setTitle(name).setAllowEditing(false).setColor(color).setBadges(badges).setPokemon(pokemon).sendTo(player);
   }

   public static void openDisplayCase(EntityPlayerMP player, String name, EnumBadgeCase color, List badges) {
      builder().setTitle(name).setAllowEditing(false).setColor(color).setBadges(badges).sendTo(player);
   }

   public static Builder builder() {
      return new Builder();
   }

   public static Builder builder(String title) {
      return (new Builder()).setTitle(title);
   }

   public static class Builder {
      private String owner = null;
      private boolean allowEdit;
      private EnumBadgeCase color;
      private List badges = Lists.newArrayList();
      private final List party = Lists.newArrayList();

      public Builder setTitle(String title) {
         this.owner = (String)Preconditions.checkNotNull(title, "title");
         return this;
      }

      public Builder setAllowEditing(boolean allowEdit) {
         this.allowEdit = allowEdit;
         return this;
      }

      public Builder setColor(EnumBadgeCase color) {
         this.color = (EnumBadgeCase)Preconditions.checkNotNull(color, "color");
         return this;
      }

      public Builder setBadges(List badges) {
         Preconditions.checkNotNull(badges, "badges");
         Preconditions.checkArgument(badges.size() <= 100, "Badge list size must be less then 100");

         for(int i = 0; i < badges.size(); ++i) {
            Preconditions.checkNotNull(badges.get(i), "stack #" + i);
            Preconditions.checkArgument(!((ItemStack)badges.get(i)).func_190926_b(), "stack #" + i + " cannot be empty");
         }

         this.badges = Lists.newArrayList(badges);
         return this;
      }

      public Builder addBadge(ItemStack stack) {
         Preconditions.checkNotNull(stack, "stack");
         Preconditions.checkArgument(!stack.func_190926_b(), "Item stack cannot be empty");
         this.badges.add(stack);
         return this;
      }

      public Builder setPokemon(List pokemon) {
         Preconditions.checkNotNull(pokemon, "pokemon");
         Preconditions.checkArgument(pokemon.size() <= 6, "List cannot exceed a size of 6");
         this.party.clear();

         for(int i = 0; i < 6; ++i) {
            if (pokemon.size() > i) {
               this.party.add(i, pokemon.get(i));
            } else {
               this.party.set(i, (Object)null);
            }
         }

         return this;
      }

      public void sendTo(EntityPlayerMP player) {
         if (this.owner == null) {
            this.owner = "";
         }

         Pixelmon.network.sendTo(new OpenBadgeCasePacket(this.owner, this.allowEdit, true, this.color, this.badges, this.party), player);
      }
   }
}
