package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.bag;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleMenuGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BagPacket;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.enums.battle.BagSection;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.items.ItemData;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class ChooseBag extends BattleMenuGui {
   private final List BUTTONS = Lists.newArrayList();

   public ChooseBag(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (this.BUTTONS.isEmpty()) {
         this.BUTTONS.add(new ItemMenuButton(0, new ItemStack(PixelmonItemsPokeballs.pokeBall), BagSection.Pokeballs.getLocalizedName(), this));
         this.BUTTONS.add(new ItemMenuButton(1, new ItemStack(PixelmonItems.potion), BagSection.HP.getLocalizedName(), this));
         this.BUTTONS.add(new ItemMenuButton(2, new ItemStack(PixelmonItems.xAttack), BagSection.BattleItems.getLocalizedName(), this));
         this.BUTTONS.add(new ItemMenuButton(3, new ItemStack(PixelmonItems.fullRestore), BagSection.StatusRestore.getLocalizedName(), this));
      }

      this.bm.bagSection = null;
   }

   public String getTitle() {
      return this.bm.bagSection == null ? I18n.func_135052_a("gui.mainmenu.bag", new Object[0]) : this.bm.bagSection.getLocalizedName();
   }

   public List generateButtons() {
      if (this.bm.getLastUsedItemIfStillAvailable().isPresent()) {
         ItemStack stack = new ItemStack(((ItemData)this.bm.getLastUsedItemIfStillAvailable().get()).getItem());
         this.menuElement.setReuseMessage("gui.choosebag.lastitem", stack);
      }

      if (this.bm.bagSection == null) {
         return this.BUTTONS;
      } else {
         List list = Lists.newArrayList();
         List bag = this.bm.getBagItems(this.bm.bagSection);
         int index = 0;
         Iterator var4 = bag.iterator();

         while(var4.hasNext()) {
            ItemData entry = (ItemData)var4.next();
            list.add(new ItemMenuButton(index++, new ItemStack(entry.getItem()), entry.count, this));
         }

         return list;
      }
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      super.drawScreen(width, height, mouseX, mouseY);
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (this.menuElement.isMouseOver(mouseX, mouseY)) {
         if (this.menuElement.isOverReturnButton(mouseX, mouseY)) {
            if (this.bm.bagSection == null) {
               this.bm.mode = BattleMode.MainMenu;
            } else {
               this.bm.bagSection = null;
            }
         } else if (this.menuElement.isOverReuseButton(mouseX, mouseY)) {
            this.bm.bagSection = (BagSection)this.bm.lastItem.getKey();
            this.bm.itemToUse = (ItemData)this.bm.lastItem.getValue();
            if (this.bm.bagSection == BagSection.Pokeballs) {
               if (this.bm.canCatchOpponent()) {
                  this.bm.selectedActions.clear();
                  Pixelmon.network.sendToServer(new BagPacket(this.bm.getCurrentPokemon().pokemonUUID, this.bm.itemToUse.id, this.bm.battleControllerIndex, 0));
                  this.bm.mode = BattleMode.Waiting;
               } else {
                  this.bm.addMessage(I18n.func_135052_a("gui.choosebag.nopokeballs", new Object[0]));
                  this.bm.mode = BattleMode.MainMenu;
               }
            } else {
               this.bm.mode = BattleMode.ApplyToPokemon;
            }
         } else if (!this.menuElement.handleClickPageTurn(mouseX, mouseY)) {
            List buttons = this.menuElement.getPageButtons();
            Iterator var6 = buttons.iterator();

            while(var6.hasNext()) {
               ItemMenuButton button = (ItemMenuButton)var6.next();
               if (button.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
                  if (this.bm.bagSection == null) {
                     BagSection bs = BagSection.getSection(button.field_146127_k);
                     if (bs == BagSection.Pokeballs) {
                        if (!this.bm.canCatchOpponent()) {
                           this.bm.addMessage(I18n.func_135052_a("gui.choosebag.nopokeballs", new Object[0]));
                           this.bm.mode = BattleMode.MainMenu;
                           break;
                        }

                        if (Arrays.stream(this.bm.displayedEnemyPokemon).filter((p) -> {
                           return p.health > 0.0F;
                        }).count() > 1L) {
                           this.bm.addMessage(I18n.func_135052_a("gui.choosebag.multiple", new Object[0]));
                           this.bm.mode = BattleMode.MainMenu;
                           break;
                        }
                     }

                     this.bm.bagSection = BagSection.getSection(button.field_146127_k);
                  } else {
                     List bag = this.bm.getBagItems(this.bm.bagSection);
                     this.bm.itemToUse = (ItemData)bag.get(button.field_146127_k);
                     this.bm.lastItem = new ImmutablePair(this.bm.bagSection, bag.get(button.field_146127_k));
                     if (this.bm.bagSection == BagSection.Pokeballs) {
                        this.bm.selectedActions.clear();
                        Pixelmon.network.sendToServer(new BagPacket(this.bm.getCurrentPokemon().pokemonUUID, this.bm.itemToUse.id, this.bm.battleControllerIndex, 0));
                        this.bm.mode = BattleMode.Waiting;
                     } else {
                        this.bm.mode = BattleMode.ApplyToPokemon;
                     }
                  }
               }
            }
         }
      }

   }
}
