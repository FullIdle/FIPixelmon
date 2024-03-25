package com.pixelmonmod.pixelmon.client.gui.inventory;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonPokeChecker;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetCharm;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetHeldItem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.ChangeLurePacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerSwap;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class InventoryPixelmon {
   private InventoryEffectRenderer gui;
   private int partyWidth;
   private SlotInventoryPixelmon[] pixelmonSlots;
   private StoragePosition selected;
   private GuiButtonPokeChecker pokeChecker;
   private boolean drawerOut = false;
   private int drawerOffset = 0;
   private int[] lureSlot;
   private int ticksTillClick = 0;

   InventoryPixelmon(InventoryEffectRenderer gui, int partyWidth) {
      this.gui = gui;
      this.pixelmonSlots = new SlotInventoryPixelmon[6];
      this.partyWidth = partyWidth;
   }

   void initGui() {
      int slotLeft = ((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth + 34;
      int slotTop = this.gui.field_146295_m / 2 + 21;
      this.lureSlot = new int[]{slotLeft - 16 - (this.gui instanceof GuiContainerCreative ? 1 : 0), slotTop + 20};
      ((IInventoryPixelmon)this.gui).getButtonList().add(this.pokeChecker = new GuiButtonPokeChecker(this.gui));
      this.reloadSlots();
   }

   private void reloadSlots() {
      int i;
      for(i = 0; i < this.pixelmonSlots.length; ++i) {
         this.pixelmonSlots[i] = null;
      }

      for(i = 0; i < this.pixelmonSlots.length; ++i) {
         StoragePosition position = new StoragePosition(-1, i);
         Pokemon pokemon = ClientStorageManager.party.get(i);
         if (pokemon != null) {
            int x = ((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth + 8;
            int y = this.gui.field_146295_m / 2 + i * 18 - 75;
            this.pixelmonSlots[i] = new SlotInventoryPixelmon(x, y, position);
         }
      }

   }

   void drawScreen(int mouseX, int mouseY, float partialTicks) {
      boolean recipeBookVisible = this.gui instanceof GuiInventory && ((GuiInventory)this.gui).func_194310_f().func_191878_b();
      if (this.gui.field_147045_u && !recipeBookVisible) {
         this.gui.field_147045_u = false;
         ((IInventoryPixelmon)this.gui).superDrawScreen(mouseX, mouseY, partialTicks);
         this.gui.field_147045_u = true;
      } else {
         ((IInventoryPixelmon)this.gui).superDrawScreen(mouseX, mouseY, partialTicks);
      }

      if (!recipeBookVisible) {
         GlStateManager.func_179140_f();
         this.gui.field_146297_k.field_71466_p.func_78264_a(true);
         SlotInventoryPixelmon[] var5 = this.pixelmonSlots;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            SlotInventoryPixelmon slot = var5[var7];
            if (slot != null) {
               Pokemon pokemon = ClientStorageManager.party.get(slot.position);
               if (pokemon != null) {
                  if ((!this.pokeChecker.field_146124_l || mouseY < this.pokeChecker.field_146129_i || mouseY > this.pokeChecker.field_146129_i + this.pokeChecker.field_146121_g) && slot.getBounds().contains(mouseX, mouseY)) {
                     GuiHelper.drawPokemonHoverInfo(pokemon, mouseX, mouseY);
                  } else if (!pokemon.isEgg() && slot.getHeldItemBounds().contains(mouseX, mouseY) && this.heldItemQualifies(pokemon) && this.ticksTillClick <= 0) {
                     this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonCreativeInventory);
                     if (!pokemon.getHeldItem().func_190926_b()) {
                        ((IInventoryPixelmon)this.gui).renderToolTipPublic(pokemon.getHeldItem(), mouseX, mouseY);
                     }
                  }
               }
            }
         }

         this.gui.field_146297_k.field_71466_p.func_78264_a(false);
         if (ClientStorageManager.party.getLure() != null) {
            this.gui.field_146297_k.func_175599_af().func_180450_b(ClientStorageManager.party.getLureStack(), this.lureSlot[0], this.lureSlot[1]);
            this.gui.field_146297_k.func_175599_af().func_180453_a(this.gui.field_146297_k.field_71466_p, ClientStorageManager.party.getLureStack(), this.lureSlot[0], this.lureSlot[1], (String)null);
            if (mouseX >= this.lureSlot[0] && mouseX <= this.lureSlot[0] + 16 && mouseY >= this.lureSlot[1] && mouseY <= this.lureSlot[1] + 16) {
               ((IInventoryPixelmon)this.gui).renderToolTipPublic(ClientStorageManager.party.getLureStack(), mouseX, mouseY);
            }
         }

         if (Pixelmon.devEnvironment) {
            this.gui.field_146297_k.field_71466_p.func_175063_a("oX: " + (mouseX - (((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth)) + " oY: " + (mouseY - this.gui.field_146295_m / 2), (float)(mouseX - 80), (float)(mouseY - 3), Color.red.getRGB());
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.drawerOut && this.drawerOffset < 42) {
            this.drawerOffset += 3;
            if (this.drawerOffset > 42) {
               this.drawerOffset = 42;
            }
         } else if (!this.drawerOut && this.drawerOffset > 0) {
            this.drawerOffset -= 3;
            if (this.drawerOffset < 0) {
               this.drawerOffset = 0;
            }
         }

      }
   }

   void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.func_179140_f();
      GlStateManager.func_179106_n();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.gui.field_146297_k.field_71460_t.func_78478_c();
      this.gui.field_146297_k.field_71466_p.func_78264_a(true);
      SlotInventoryPixelmon[] var4 = this.pixelmonSlots;
      int bY = var4.length;

      for(int var6 = 0; var6 < bY; ++var6) {
         SlotInventoryPixelmon slot = var4[var6];
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (slot != null) {
            Pokemon pokemon = ClientStorageManager.party.get(slot.position);
            if (pokemon != null) {
               slot.setX(((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth + 8);
               GuiHelper.bindPokemonSprite(pokemon, this.gui.field_146297_k);
               GlStateManager.func_179140_f();
               GuiHelper.drawImageQuad((double)slot.x, (double)slot.y, 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               if (!pokemon.isEgg()) {
                  if (pokemon.getHeldItem() != ItemStack.field_190927_a) {
                     this.gui.field_146297_k.func_175599_af().func_175042_a(pokemon.getHeldItem(), slot.heldItemX, slot.heldItemY);
                  } else {
                     this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
                     GuiHelper.drawImageQuad((double)(slot.heldItemX + 3), (double)(slot.heldItemY + 3), 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
                  }
               }

               if (slot.position.equals(this.selected)) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.pcResources);
                  GuiHelper.drawImageQuad((double)((float)slot.x + 0.5F), (double)((float)slot.y + 0.5F), 15.0, 15.0F, 0.0, 0.11328125, 0.11328125, 0.2265625, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }
         }
      }

      if ((ClientStorageManager.party.getLure() != null || this.gui.field_146297_k.field_71439_g.field_71071_by.func_70445_o().func_77973_b() instanceof ItemLure) && mouseX >= this.lureSlot[0] && mouseX <= this.lureSlot[0] + 16 && mouseY >= this.lureSlot[1] && mouseY <= this.lureSlot[1] + 16) {
         this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonOverlayExtended2);
         GuiHelper.drawImageQuad((double)(this.lureSlot[0] - 1), (double)(this.lureSlot[1] - 1), 18.0, 18.0F, 0.23046875, 0.7265625, 0.30078125, 0.796875, ((IInventoryPixelmon)this.gui).getZLevel());
      }

      this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonOverlayExtended2);
      int bX = ((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth;
      bY = this.gui.field_146295_m / 2;
      if (this.drawerOffset > 0) {
         GuiHelper.drawImageQuad((double)(bX - 2 - this.drawerOffset), (double)(bY - 83), (double)(7 + this.drawerOffset), 166.0F, 0.609375, 0.0, (double)((163.0F + (float)this.drawerOffset) / 256.0F), 0.6484375, ((IInventoryPixelmon)this.gui).getZLevel());
         if (this.drawerOut && mouseX >= bX - 38 && mouseX <= bX - 28 && mouseY >= bY - 18 && mouseY <= bY + 18) {
            GuiHelper.drawImageQuad((double)(bX - 35), (double)(bY - 5), 5.0, 10.0F, 0.01953125, 0.671875, 0.0390625, 0.7109375, ((IInventoryPixelmon)this.gui).getZLevel());
         }

         if (this.drawerOffset > 40) {
            EnumFeatureState markCharm = EntityPlayerExtension.getPlayerMarkCharm(this.gui.field_146297_k.field_71439_g);
            if (markCharm.isAvailable()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.markCharmBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 55), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!markCharm.isActive()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 55), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            EnumFeatureState catchingCharm = EntityPlayerExtension.getPlayerCatchingCharm(this.gui.field_146297_k.field_71439_g);
            if (catchingCharm.isAvailable()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.catchingCharmBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 32), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!catchingCharm.isActive()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 32), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            EnumFeatureState expCharm = EntityPlayerExtension.getPlayerExpCharm(this.gui.field_146297_k.field_71439_g);
            if (expCharm.isAvailable()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.expCharmBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 9), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!expCharm.isActive()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY + 9), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            EnumFeatureState shinyCharm = EntityPlayerExtension.getPlayerShinyCharm(this.gui.field_146297_k.field_71439_g);
            if (shinyCharm.isAvailable()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.shinyCharmBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 14), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!shinyCharm.isActive()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 14), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            EnumFeatureState ovalCharm = EntityPlayerExtension.getPlayerOvalCharm(this.gui.field_146297_k.field_71439_g);
            if (ovalCharm.isAvailable()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.ovalCharmBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 36), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!ovalCharm.isActive()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 36), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            EnumMegaItemsUnlocked megaItems = EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.field_146297_k.field_71439_g);
            if (megaItems.canMega()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.keyStoneBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 59), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!EntityPlayerExtension.getPlayerMegaItem(this.gui.field_146297_k.field_71439_g).canEvolve()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 59), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }

            if (megaItems.canDynamax()) {
               this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.dynamaxBandBig);
               GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 81), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (!EntityPlayerExtension.getPlayerMegaItem(this.gui.field_146297_k.field_71439_g).canDynamax()) {
                  this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.disabled);
                  GuiHelper.drawImageQuad((double)(bX + 17 - this.drawerOffset), (double)(bY - 81), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, ((IInventoryPixelmon)this.gui).getZLevel());
               }
            }
         }
      } else if (mouseX >= bX - 9 && mouseX <= bX + 1 && mouseY >= bY - 18 && mouseY <= bY + 18) {
         GuiHelper.drawImageQuad((double)(bX - 6), (double)(bY - 5), 5.0, 10.0F, 0.0, 0.671875, 0.01953125, 0.7109375, ((IInventoryPixelmon)this.gui).getZLevel());
      }

      if (this.drawerOffset > 0) {
         this.gui.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonOverlayExtended2);
         GuiHelper.drawImageQuad((double)bX, (double)(bY - 83), 5.0, 166.0F, 0.1484375, 0.0, 0.16796875, 0.6484375, ((IInventoryPixelmon)this.gui).getZLevel());
      }

      GuiHelper.drawStringRightAligned(NumberFormat.getInstance().format((long)ClientData.playerMoney), (float)(((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth + 42), (float)this.gui.field_146295_m / 2.0F + 66.0F, 15790320, false);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.gui.field_146297_k.field_71466_p.func_78264_a(false);
      RenderHelper.func_74518_a();
      GlStateManager.func_179140_f();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179126_j();
   }

   private boolean heldItemQualifies(Pokemon pokemon) {
      ItemStack handStack = this.gui.field_146297_k.field_71439_g.field_71071_by.func_70445_o();
      ItemStack heldStack = pokemon.getHeldItem();
      if (handStack.func_190926_b() && !heldStack.func_190926_b()) {
         return true;
      } else {
         return !handStack.func_190926_b() && handStack.func_77973_b() instanceof ItemHeld;
      }
   }

   boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseX >= this.lureSlot[0] && mouseX <= this.lureSlot[0] + 16 && mouseY >= this.lureSlot[1] && mouseY <= this.lureSlot[1] + 16 && this.ticksTillClick <= 0) {
         ItemStack held = this.gui.field_146297_k.field_71439_g.field_71071_by.func_70445_o();
         ItemStack lure = ClientStorageManager.party.getLureStack();
         boolean creative = this.gui.field_146297_k.field_71439_g.field_71075_bZ.field_75098_d;
         if (held.func_190926_b() && lure.func_190926_b()) {
            return false;
         } else {
            if (held.func_77973_b() instanceof ItemLure) {
               if (lure.func_190926_b()) {
                  ClientStorageManager.party.setLureStack(lure);
                  this.gui.field_146297_k.field_71439_g.field_71071_by.func_70437_b(ItemStack.field_190927_a);
                  Pixelmon.network.sendToServer(creative ? new ChangeLurePacket(ChangeLurePacket.Change.PUT, held.func_77973_b(), held.func_77952_i()) : new ChangeLurePacket(ChangeLurePacket.Change.PUT));
               } else {
                  this.gui.field_146297_k.field_71439_g.field_71071_by.func_70437_b(lure);
                  ClientStorageManager.party.setLureStack(held);
                  Pixelmon.network.sendToServer(creative ? new ChangeLurePacket(ChangeLurePacket.Change.SWAP, held.func_77973_b(), held.func_77952_i()) : new ChangeLurePacket(ChangeLurePacket.Change.SWAP));
               }
            } else if (!lure.func_190926_b() && held.func_190926_b()) {
               this.gui.field_146297_k.field_71439_g.field_71071_by.func_70437_b(lure);
               ClientStorageManager.party.setLureStack(ItemStack.field_190927_a);
               Pixelmon.network.sendToServer(new ChangeLurePacket(ChangeLurePacket.Change.TAKE));
            }

            this.ticksTillClick = 10;
            return false;
         }
      } else if (this.pokeChecker.func_146115_a()) {
         if (mouseButton == 0) {
            this.pokeChecker.mouseClicked(mouseX, mouseY);
         }

         return false;
      } else {
         int i;
         for(i = 0; i < this.pixelmonSlots.length; ++i) {
            SlotInventoryPixelmon slot = this.pixelmonSlots[i];
            StoragePosition position = slot == null ? null : slot.position;
            Pokemon pokemon = position == null ? null : ClientStorageManager.party.get(position);
            int slotX;
            int slotY;
            if (slot != null) {
               slotX = slot.x;
               slotY = slot.y;
            } else {
               slotX = ((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth + 6;
               slotY = this.gui.field_146295_m / 2 + i * 18 - 75;
            }

            if (mouseX >= slotX && mouseX <= slotX + 16 && mouseY >= slotY && mouseY <= slotY + 16) {
               if (mouseButton == 1) {
                  this.pokeChecker.setPokemon(ClientStorageManager.party, position, pokemon, mouseX, mouseY);
               } else {
                  this.pokeChecker.setPokemon((PokemonStorage)null, (StoragePosition)null, (Pokemon)null, mouseX, mouseY);
               }

               if (mouseButton == 0) {
                  if (this.selected == null) {
                     this.selected = position;
                  } else {
                     Pixelmon.network.sendToServer(new ServerSwap(this.selected, ClientStorageManager.party.get(this.selected), new StoragePosition(-1, i), pokemon));
                     ClientStorageManager.party.swap(this.selected.order, i);
                     this.selected = null;
                     this.reloadSlots();
                  }
               }

               return false;
            }

            if (this.ticksTillClick <= 0 && pokemon != null && !pokemon.isEgg() && this.heldItemQualifies(pokemon) && slot.getHeldItemBounds().contains(mouseX, mouseY)) {
               SetHeldItem packet = new SetHeldItem(position, pokemon.getUUID());
               InventoryPlayer inventory = this.gui.field_146297_k.field_71439_g.field_71071_by;
               ItemStack currentItem = inventory.func_70445_o();
               ItemStack oldItem = pokemon.getHeldItem();
               ItemStack singleItem;
               if (this.gui.field_146297_k.field_71439_g.field_71075_bZ.field_75098_d) {
                  if (!currentItem.func_190926_b()) {
                     singleItem = currentItem.func_77946_l();
                     singleItem.func_190920_e(1);
                     pokemon.setHeldItem(singleItem);
                     packet.setItem(currentItem.func_77973_b());
                  } else {
                     pokemon.setHeldItem(ItemStack.field_190927_a);
                     packet.setItem((Item)null);
                  }
               } else {
                  if (oldItem.func_190926_b()) {
                     if (!currentItem.func_190926_b()) {
                        singleItem = currentItem.func_77946_l();
                        singleItem.func_190920_e(1);
                        pokemon.setHeldItem(singleItem);
                        if (currentItem.func_190916_E() <= 1) {
                           inventory.func_70437_b(ItemStack.field_190927_a);
                        } else {
                           currentItem.func_190918_g(1);
                        }
                     }
                  } else if (currentItem.func_190926_b()) {
                     pokemon.setHeldItem(ItemStack.field_190927_a);
                     inventory.func_70437_b(oldItem);
                  } else if (ItemStack.func_179545_c(oldItem, currentItem) && ItemStack.func_77970_a(oldItem, currentItem)) {
                     pokemon.setHeldItem(ItemStack.field_190927_a);
                     currentItem.func_190917_f(1);
                  } else if (currentItem.func_190916_E() <= 1) {
                     singleItem = currentItem.func_77946_l();
                     singleItem.func_190920_e(1);
                     pokemon.setHeldItem(singleItem);
                     inventory.func_70437_b(oldItem);
                  } else {
                     singleItem = currentItem.func_77946_l();
                     singleItem.func_190920_e(1);
                     pokemon.setHeldItem(singleItem);
                     currentItem.func_190918_g(1);
                     inventory.func_70441_a(oldItem);
                  }

                  ItemStack playerItem = inventory.func_70445_o();
                  if (!playerItem.func_190926_b() && playerItem.func_190916_E() > 64) {
                     playerItem.func_190920_e(64);
                  }
               }

               Pixelmon.network.sendToServer(packet);
               this.ticksTillClick = 10;
               return false;
            }
         }

         i = ((IInventoryPixelmon)this.gui).getGUILeft() - this.partyWidth;
         int bY = this.gui.field_146295_m / 2;
         if (this.drawerOut) {
            if (mouseX >= i - 38 && mouseX <= i - 28 && mouseY >= bY - 18 && mouseY <= bY + 18) {
               this.drawerOut = false;
               return false;
            }

            if (mouseX >= i - 25 && mouseX <= i - 1) {
               EnumFeatureState charm;
               if (mouseY > bY + 55 && mouseY < bY + 79) {
                  charm = EntityPlayerExtension.getPlayerMarkCharm(this.gui.field_146297_k.field_71439_g);
                  if (charm.isAvailable()) {
                     Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Mark, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                  }

                  return false;
               }

               if (mouseY > bY + 33 && mouseY < bY + 55) {
                  charm = EntityPlayerExtension.getPlayerCatchingCharm(this.gui.field_146297_k.field_71439_g);
                  if (charm.isAvailable()) {
                     Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Catching, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                  }

                  return false;
               }

               if (mouseY > bY + 9 && mouseY < bY + 33) {
                  charm = EntityPlayerExtension.getPlayerExpCharm(this.gui.field_146297_k.field_71439_g);
                  if (charm.isAvailable()) {
                     Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Exp, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                  }

                  return false;
               }

               if (mouseY > bY - 13 && mouseY < bY + 9) {
                  charm = EntityPlayerExtension.getPlayerShinyCharm(this.gui.field_146297_k.field_71439_g);
                  if (charm.isAvailable()) {
                     Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Shiny, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                  }

                  return false;
               }

               if (mouseY > bY - 35 && mouseY < bY - 13) {
                  charm = EntityPlayerExtension.getPlayerOvalCharm(this.gui.field_146297_k.field_71439_g);
                  if (charm.isAvailable()) {
                     Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Oval, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                  }

                  return false;
               }

               if (mouseY > bY - 58 && mouseY < bY - 36) {
                  if (EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.field_146297_k.field_71439_g).canMega()) {
                     this.gui.field_146297_k.func_147108_a(new GuiMegaItem(false));
                  }

                  return false;
               }

               if (mouseY > bY - 79 && mouseY < bY - 58) {
                  if (EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.field_146297_k.field_71439_g).canDynamax()) {
                     this.gui.field_146297_k.func_147108_a(new GuiMegaItem(true));
                  }

                  return false;
               }
            }
         } else if (mouseX >= i - 9 && mouseX <= i + 1 && mouseY >= bY - 18 && mouseY <= bY + 18) {
            this.drawerOut = true;
            return false;
         }

         return true;
      }
   }

   public void tick() {
      if (this.ticksTillClick > 0) {
         --this.ticksTillClick;
      }

   }
}
