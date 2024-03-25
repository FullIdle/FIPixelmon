package com.pixelmonmod.pixelmon.api.drops;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.EnumPositionTriState;
import com.pixelmonmod.pixelmon.comm.packetHandlers.custom.drops.CustomDropsOpenPacket;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class CustomDropScreen {
   public static void openDropScreen(EntityPlayerMP player, ITextComponent title, List items, String... buttons) {
      Builder builder = builder().setTitle(title).setItems(items);

      for(int i = 0; i < 3; ++i) {
         EnumPositionTriState position = EnumPositionTriState.naturalOrder()[i];
         if (buttons.length > i) {
            builder.setButtonText(position, buttons[i]);
         }
      }

      builder.sendTo(player);
   }

   public static Builder builder() {
      return new Builder();
   }

   public static Builder builder(ITextComponent title) {
      return (new Builder()).setTitle(title);
   }

   public static class Builder {
      private ITextComponent title = null;
      private List items = Lists.newArrayList();
      private String[] buttons = new String[]{"", "", ""};

      public Builder setTitle(ITextComponent title) {
         this.title = (ITextComponent)Preconditions.checkNotNull(title, "title");
         return this;
      }

      public Builder setItems(List items) {
         Preconditions.checkNotNull(items, "items");
         this.items = items;
         return this;
      }

      public Builder addItem(ItemStack stack) {
         Preconditions.checkNotNull(stack, "stack");
         Preconditions.checkArgument(!stack.func_190926_b(), "Item stack cannot be empty");
         this.items.add(stack);
         return this;
      }

      public Builder setButtonText(EnumPositionTriState position, String text) {
         Preconditions.checkNotNull(position, "position");
         Preconditions.checkNotNull(text, "text");
         this.buttons[position.ordinal()] = text;
         return this;
      }

      public void sendTo(EntityPlayerMP player) {
         Pixelmon.network.sendTo(new CustomDropsOpenPacket(this.title, this.items, this.buttons), player);
      }
   }
}
