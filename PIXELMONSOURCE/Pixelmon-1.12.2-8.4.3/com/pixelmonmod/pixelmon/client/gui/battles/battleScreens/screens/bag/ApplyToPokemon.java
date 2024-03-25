package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.bag;

import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleMenuElement;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.PigMenuButton;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BagPacket;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ApplyToPokemon extends BattleGui {
   private BattleMenuElement bagMenu;
   private BattleMenuElement pokeMenu;

   public ApplyToPokemon(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      this.drawBackground(width, height, mouseX, mouseY);
      this.drawButtons(mouseX, mouseY);

      try {
         this.parent.battleLog.drawElement(40, height - 80, Math.min(this.field_146294_l - 80, 260), 80, false);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      AtomicInteger index = new AtomicInteger();
      List bag = this.bm.getBagItems(this.bm.bagSection);
      List items = (List)bag.stream().map((it) -> {
         return new ItemMenuButton(index.getAndIncrement(), new ItemStack(it.getItem(), it.count), this);
      }).collect(Collectors.toList());
      this.bagMenu = new BattleMenuElement(this, this.bm.bagSection.getLocalizedName(), items);
      this.bagMenu.setPosition(0, this.field_146295_m - 240, 180, 240);
      this.bagMenu.drawElement(1.0F);
      this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, 1879048192, 1879048192);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      index.set(0);
      List pigs = (List)this.bm.fullOurPokemon.stream().map((it) -> {
         return new PigMenuButton(index.getAndIncrement(), it, this);
      }).collect(Collectors.toList());
      this.pokeMenu = new BattleMenuElement(this, I18n.func_135052_a("gui.choosepokemon.select", new Object[0]), pigs);
      this.pokeMenu.setPosition(180, this.field_146295_m - 240, 180, 240);
      this.pokeMenu.drawElement(1.0F);
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (!this.bm.isHealing) {
         if (this.pokeMenu.isMouseOver(mouseX, mouseY)) {
            if (this.pokeMenu.isOverReturnButton(mouseX, mouseY)) {
               this.bm.mode = BattleMode.ChooseBag;
            } else if (!this.pokeMenu.handleClickPageTurn(mouseX, mouseY)) {
               List buttons = this.pokeMenu.getPageButtons();
               Iterator var6 = buttons.iterator();

               while(true) {
                  while(true) {
                     PixelmonInGui pokemonToApplyTo;
                     Item item;
                     do {
                        PigMenuButton button;
                        do {
                           if (!var6.hasNext()) {
                              return;
                           }

                           button = (PigMenuButton)var6.next();
                        } while(!button.func_146116_c(this.field_146297_k, mouseX, mouseY));

                        pokemonToApplyTo = null;
                        PixelmonInGui chosenPokemon = button.getPig();
                        item = Item.func_150899_d(this.bm.itemToUse.id);
                        if (chosenPokemon != null) {
                           if (item != PixelmonItems.revive && item != PixelmonItems.maxRevive && item != PixelmonItems.revivalHerb) {
                              if (chosenPokemon.health > 0.0F) {
                                 pokemonToApplyTo = chosenPokemon;
                              }
                           } else if (chosenPokemon.health <= 0.0F) {
                              pokemonToApplyTo = chosenPokemon;
                           }
                        }
                     } while(pokemonToApplyTo == null);

                     if (item != PixelmonItems.ether && item != PixelmonItems.maxEther && item != PixelmonItemsHeld.leppaBerry) {
                        this.bm.selectedActions.add(new BagPacket(this.bm.getCurrentPokemon().pokemonUUID, pokemonToApplyTo.pokemonUUID, this.bm.itemToUse.id, this.bm.battleControllerIndex));
                        this.bm.selectedMove();
                     } else {
                        this.bm.mode = BattleMode.ChooseEther;
                     }
                  }
               }
            }
         }

      }
   }
}
