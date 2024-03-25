package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.StoreTrainerData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GuiTrainerEditorMore extends GuiScreen {
   private Map itemMap = new HashMap();
   private Map idMap = new HashMap();
   private Map modAndIdMap = new HashMap();
   GuiTextField tfGreeting;
   GuiTextField tfWin;
   GuiTextField tfLose;
   GuiTextField tfDrop;
   GuiTextField tfWinMoney;
   protected int listTop;
   protected int listLeft;
   protected int listHeight;
   protected int listWidth;
   ArrayList dropList = new ArrayList();
   GuiTrainerDropListSlot list;
   int lastWidth;
   int lastHeight;

   public GuiTrainerEditorMore() {
      if (GuiTrainerEditor.trainerData == null) {
         GuiTrainerEditor.trainerData = new SetTrainerData("", "", "", "", 0, new ItemStack[0], new BattleRules());
      }

      Collections.addAll(this.dropList, GuiTrainerEditor.trainerData.winnings);
      ForgeRegistries.ITEMS.getValuesCollection().stream().map(ItemStack::new).forEach((itemStack) -> {
         NonNullList list = NonNullList.func_191196_a();
         itemStack.func_77973_b().func_150895_a(CreativeTabs.field_78027_g, list);
         list.add(itemStack);
         list.forEach((stack) -> {
            this.itemMap.put(stack.func_82833_r(), stack);
            ResourceLocation location = stack.func_77973_b().getRegistryName();
            this.idMap.put(location.func_110623_a().toLowerCase(), stack);
            this.modAndIdMap.put((location.func_110624_b() + ":" + location.func_110623_a()).toLowerCase(), stack);
         });
      });
   }

   private void initList() {
      this.lastWidth = this.field_146294_l;
      this.lastHeight = this.field_146295_m;
      this.listTop = this.field_146295_m / 2 + 30;
      this.listLeft = this.field_146294_l / 2 - 160;
      this.listHeight = 80;
      this.listWidth = 100;
      this.list = new GuiTrainerDropListSlot(this);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.initList();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 70, 80, 20, I18n.func_135052_a("gui.trainereditor.additem", new Object[0])));
      this.tfGreeting = new GuiTextField(3, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 70, 280, 17);
      this.tfGreeting.func_146203_f(2000);
      this.tfGreeting.func_146180_a(GuiTrainerEditor.trainerData.greeting);
      this.tfWin = new GuiTextField(4, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 50, 280, 17);
      this.tfWin.func_146203_f(2000);
      this.tfWin.func_146180_a(GuiTrainerEditor.trainerData.win);
      this.tfLose = new GuiTextField(5, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 - 30, 280, 17);
      this.tfLose.func_146203_f(2000);
      this.tfLose.func_146180_a(GuiTrainerEditor.trainerData.lose);
      this.tfDrop = (new GuiTabCompleteTextField(7, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 50, 120, 17)).setCompletions(this.itemMap.keySet());
      this.tfWinMoney = new GuiTextField(9, this.field_146289_q, this.field_146294_l / 2 + 110, this.field_146295_m / 2 + 50, 80, 20);
      this.tfWinMoney.func_146203_f(2000);
      this.tfWinMoney.func_146180_a(String.valueOf(GuiTrainerEditor.trainerData.winMoney));
   }

   public void func_73863_a(int mouseX, int mouseY, float mfloat) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.moreinfo", new Object[0]), this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(I18n.func_135052_a("gui.trainereditor.moreinfo", new Object[0])) / 2, this.field_146295_m / 2 - 90, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.greeting", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 65, 0);
      this.tfGreeting.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.win", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 45, 0);
      this.tfWin.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.lose", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 - 25, 0);
      this.tfLose.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.winningdrops", new Object[0]), this.field_146294_l / 2 - 157, this.field_146295_m / 2 + 15, 0);
      this.list.drawScreen(mouseX, mouseY, mfloat);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.adddrops", new Object[0]), this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 15, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.enteritemname", new Object[0]), this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 30, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.winmoney", new Object[0]), this.field_146294_l / 2 + 110, this.field_146295_m / 2 + 30, 0);
      this.tfDrop.func_146194_f();
      this.tfWinMoney.func_146194_f();
      super.func_73863_a(mouseX, mouseY, mfloat);
   }

   protected void func_73869_a(char key, int par2) {
      this.tfGreeting.func_146201_a(key, par2);
      this.tfWin.func_146201_a(key, par2);
      this.tfLose.func_146201_a(key, par2);
      this.tfDrop.func_146201_a(key, par2);
      this.tfWinMoney.func_146201_a(key, par2);
      if (par2 == 1 || par2 == 28) {
         this.saveFields();
      }

   }

   protected void func_73864_a(int x, int y, int z) throws IOException {
      super.func_73864_a(x, y, z);
      this.tfGreeting.func_146192_a(x, y, z);
      this.tfWin.func_146192_a(x, y, z);
      this.tfLose.func_146192_a(x, y, z);
      this.tfDrop.func_146192_a(x, y, z);
      this.tfWinMoney.func_146192_a(x, y, z);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146127_k == 1) {
         this.saveFields();
      } else if (button.field_146127_k == 2) {
         String itemString = this.tfDrop.func_146179_b();
         if (this.itemMap.containsKey(itemString)) {
            this.dropList.add(((ItemStack)this.itemMap.get(itemString)).func_77946_l());
         } else if (this.idMap.containsKey(itemString.toLowerCase())) {
            this.dropList.add(((ItemStack)this.idMap.get(itemString.toLowerCase())).func_77946_l());
         } else if (this.modAndIdMap.containsKey(itemString.toLowerCase())) {
            this.dropList.add(((ItemStack)this.modAndIdMap.get(itemString.toLowerCase())).func_77946_l());
         } else {
            try {
               String[] split = itemString.split(":", 2);
               int meta = 0;
               if (split.length > 1) {
                  try {
                     meta = Integer.parseInt(split[1]);
                  } catch (NumberFormatException var6) {
                  }
               }

               int id = Integer.parseInt(split[0]);
               if (Item.func_150899_d(id) != null) {
                  this.dropList.add(new ItemStack(Item.func_150899_d(id), 1, meta));
               }
            } catch (Exception var7) {
            }
         }
      }

   }

   private void saveFields() {
      ItemStack[] drops = new ItemStack[this.dropList.size()];

      int winMoneyValue;
      for(winMoneyValue = 0; winMoneyValue < this.dropList.size(); ++winMoneyValue) {
         drops[winMoneyValue] = (ItemStack)this.dropList.get(winMoneyValue);
      }

      winMoneyValue = GuiTrainerEditor.trainerData.winMoney;

      try {
         winMoneyValue = Math.max(0, Integer.parseInt(this.tfWinMoney.func_146179_b()));
      } catch (NumberFormatException var4) {
      }

      SetTrainerData p = new SetTrainerData(GuiTrainerEditor.trainerData.name, this.tfGreeting.func_146179_b(), this.tfWin.func_146179_b(), this.tfLose.func_146179_b(), winMoneyValue, drops);
      p.id = GuiTrainerEditor.currentTrainerID;
      if (!this.tfGreeting.func_146179_b().equals(GuiTrainerEditor.trainerData.greeting) || !this.tfWin.func_146179_b().equals(GuiTrainerEditor.trainerData.win) || !this.tfLose.func_146179_b().equals(GuiTrainerEditor.trainerData.lose) || winMoneyValue != GuiTrainerEditor.trainerData.winMoney) {
         Pixelmon.network.sendToServer(new StoreTrainerData(GuiTrainerEditor.currentTrainerID, p));
      }

      Pixelmon.network.sendToServer(new StoreTrainerData(GuiTrainerEditor.currentTrainerID, drops));
      GuiTrainerEditor.trainerData = p;
      this.field_146297_k.func_147108_a(new GuiTrainerEditor(GuiTrainerEditor.currentTrainerID));
   }

   public int getDropListCount() {
      return this.dropList.size();
   }

   public void removeFromList(int ind) {
      this.dropList.remove(ind);
   }

   public ItemStack getDropListEntry(int ind) {
      return ind < this.dropList.size() && ind >= 0 ? (ItemStack)this.dropList.get(ind) : null;
   }
}
