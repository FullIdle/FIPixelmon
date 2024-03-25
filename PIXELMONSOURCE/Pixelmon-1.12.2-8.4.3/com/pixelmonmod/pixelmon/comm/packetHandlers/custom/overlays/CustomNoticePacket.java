package com.pixelmonmod.pixelmon.comm.packetHandlers.custom.overlays;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.overlay.notice.EnumOverlayLayout;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomNoticeOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.OverlayGraphicType;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomNoticePacket implements IMessage {
   private boolean enabled = true;
   private String[] lines;
   private EnumOverlayLayout layout = null;
   private OverlayGraphicType type = null;
   private PokemonSpec spec = null;
   private ItemStack itemStack = null;

   public CustomNoticePacket setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public CustomNoticePacket setLines(String[] lines) {
      this.lines = lines;
      return this;
   }

   public CustomNoticePacket setPokemonSprite(PokemonSpec spec, EnumOverlayLayout layout) {
      Preconditions.checkArgument(((PokemonSpec)Preconditions.checkNotNull(spec, "spec")).name != null, "The spec doesn't specify a pokémon.");
      this.type = OverlayGraphicType.PokemonSprite;
      this.spec = spec;
      this.layout = (EnumOverlayLayout)Preconditions.checkNotNull(layout, "layout");
      return this;
   }

   public CustomNoticePacket setPokemon3D(PokemonSpec spec, EnumOverlayLayout layout) {
      Preconditions.checkArgument(((PokemonSpec)Preconditions.checkNotNull(spec, "spec")).name != null, "The spec doesn't specify a pokémon.");
      this.type = OverlayGraphicType.Pokemon3D;
      this.spec = spec;
      this.layout = (EnumOverlayLayout)Preconditions.checkNotNull(layout, "layout");
      return this;
   }

   public CustomNoticePacket setItemStack(ItemStack itemStack, EnumOverlayLayout layout) {
      Preconditions.checkNotNull(itemStack, "itemStack");
      Preconditions.checkNotNull(layout, "layout");
      if (itemStack.func_190926_b()) {
         return this.setEmpty();
      } else {
         this.type = OverlayGraphicType.ItemStack;
         this.itemStack = itemStack;
         this.layout = layout;
         return this;
      }
   }

   public CustomNoticePacket setEmpty() {
      this.type = null;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public void setPokemonSprite(EnumSpecies species, EnumOverlayLayout displayType) {
      this.setPokemonSprite(new PokemonSpec(species.name), displayType);
   }

   /** @deprecated */
   @Deprecated
   public void setPokemon3D(EnumSpecies species, EnumOverlayLayout displayType, float scale) {
      this.setPokemon3D(new PokemonSpec(species.name), displayType);
   }

   /** @deprecated */
   @Deprecated
   public void setItemSprite(String itemName, EnumOverlayLayout displayType) {
      this.setItemStack(new ItemStack((Item)Item.field_150901_e.func_82594_a(new ResourceLocation(itemName))), displayType);
   }

   /** @deprecated */
   @Deprecated
   public void setItem3D(String itemName, EnumOverlayLayout displayType) {
      this.setItemStack(new ItemStack((Item)Item.field_150901_e.func_82594_a(new ResourceLocation(itemName))), displayType);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.enabled);
      if (this.enabled) {
         buf.writeByte(this.lines == null ? -1 : this.lines.length);
         String[] var2 = this.lines;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            ByteBufUtils.writeUTF8String(buf, line);
         }

         buf.writeByte(this.type == null ? -1 : this.type.ordinal());
         if (this.type != null) {
            buf.writeByte(this.layout.ordinal());
            switch (this.type) {
               case PokemonSprite:
               case Pokemon3D:
                  ByteBufUtils.writeTag(buf, this.spec.writeToNBT(new NBTTagCompound()));
                  break;
               case ItemStack:
                  ByteBufUtils.writeItemStack(buf, this.itemStack);
            }
         }

      }
   }

   public void fromBytes(ByteBuf buf) {
      this.enabled = buf.readBoolean();
      if (this.enabled) {
         int linesLength = buf.readByte();
         if (linesLength != -1) {
            this.lines = new String[linesLength];

            for(int i = 0; i < linesLength; ++i) {
               this.lines[i] = ByteBufUtils.readUTF8String(buf);
            }
         }

         byte typeOrdinal = buf.readByte();
         if (typeOrdinal != -1) {
            this.type = OverlayGraphicType.values()[typeOrdinal];
            this.layout = EnumOverlayLayout.values()[buf.readByte()];
            switch (this.type) {
               case PokemonSprite:
               case Pokemon3D:
                  this.spec = (new PokemonSpec(new String[0])).readFromNBT(ByteBufUtils.readTag(buf));
                  if (this.spec.growth == null) {
                     this.spec.growth = (byte)EnumGrowth.Ordinary.ordinal();
                  }
                  break;
               case ItemStack:
                  this.itemStack = ByteBufUtils.readItemStack(buf);
            }
         }

      }
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CustomNoticePacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            if (!message.enabled) {
               CustomNoticeOverlay.setEnabled(false);
            } else {
               CustomNoticeOverlay.resetNotice();

               try {
                  CustomNoticeOverlay.populate(new ArrayList(Arrays.asList(message.lines)));
                  if (message.type != null) {
                     switch (message.type) {
                        case PokemonSprite:
                           CustomNoticeOverlay.setPokemonSprite(message.spec, message.layout);
                           break;
                        case Pokemon3D:
                           CustomNoticeOverlay.setPokemon3D(message.spec, message.layout);
                           break;
                        case ItemStack:
                           CustomNoticeOverlay.setItemStack(message.itemStack, message.layout);
                     }
                  }

                  CustomNoticeOverlay.setEnabled(true);
               } catch (Exception var2) {
                  CustomNoticeOverlay.resetNotice();
                  Pixelmon.LOGGER.error("The server is sending weird values to the overlay.", var2);
               }
            }

         });
         return null;
      }
   }
}
