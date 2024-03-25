package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public abstract class GuiShopScreen extends GuiScreen {
   public static ArrayList buyItems;
   public static ArrayList sellItems;
   protected int selectedItem = -1;
   protected int quantity = 1;
   protected float floatQuantity = 1.0F;
   private float incrementInterval = 8.0F;
   private static final float START_INTERVAL = 8.0F;
   protected int buyStartIndex = 0;
   protected int sellStartIndex = 0;
   protected boolean allowMultiple = true;
   EnumBuySell currentTab;

   public GuiShopScreen() {
      this.currentTab = EnumBuySell.Buy;
   }

   public void func_73876_c() {
   }

   public boolean func_73868_f() {
      return false;
   }

   protected void handleMouseScroll() {
      int j = Mouse.getEventDWheel();
      if (j != 0) {
         if (j > 0) {
            j = 1;
         }

         if (j < 0) {
            j = -1;
         }

         if (this.currentTab == EnumBuySell.Buy) {
            this.buyStartIndex -= j;
            if (this.buyStartIndex + 6 >= buyItems.size()) {
               this.buyStartIndex = buyItems.size() - 6;
            }

            if (this.buyStartIndex < 0) {
               this.buyStartIndex = 0;
            }
         } else {
            this.sellStartIndex -= j;
            if (this.sellStartIndex + 6 >= sellItems.size()) {
               this.sellStartIndex = sellItems.size() - 6;
            }

            if (this.sellStartIndex < 0) {
               this.sellStartIndex = 0;
            }
         }
      }

   }

   protected boolean isBuyMiniScreenVisible() {
      return this.selectedItem != -1;
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   protected void clickBuyScreen(int mouseX, int mouseY) {
      if (mouseX > this.field_146294_l / 2 + 51 && mouseX < this.field_146294_l / 2 + 51 + 16 && mouseY > this.field_146295_m / 2 - 61 && mouseY < this.field_146295_m / 2 - 61 + 10) {
         if (this.currentTab == EnumBuySell.Buy) {
            if (this.buyStartIndex > 0) {
               --this.buyStartIndex;
               return;
            }
         } else if (this.sellStartIndex > 0) {
            --this.sellStartIndex;
            return;
         }
      }

      if (mouseX > this.field_146294_l / 2 + 51 && mouseX < this.field_146294_l / 2 + 51 + 16 && mouseY > this.field_146295_m / 2 + 78 && mouseY < this.field_146295_m / 2 + 78 + 10) {
         if (this.currentTab == EnumBuySell.Buy) {
            if (this.buyStartIndex + 6 < buyItems.size()) {
               ++this.buyStartIndex;
               return;
            }
         } else if (this.sellStartIndex + 6 < sellItems.size()) {
            ++this.sellStartIndex;
            return;
         }
      }

      if (mouseX > this.field_146294_l / 2 + 139 && mouseX < this.field_146294_l / 2 + 139 + 14 && mouseY > this.field_146295_m / 2 + 79 && mouseY < this.field_146295_m / 2 + 79 + 17) {
         this.closeScreen();
      } else {
         int leftLimit = this.field_146294_l / 2 - 28;
         int buyPrice;
         float h;
         float topLimit;
         short w;
         if (this.currentTab == EnumBuySell.Buy) {
            for(buyPrice = this.buyStartIndex; buyPrice < 6 + this.buyStartIndex; ++buyPrice) {
               if (buyPrice < buyItems.size()) {
                  h = 21.0F;
                  topLimit = (float)(this.field_146295_m / 2 - 49) + (float)(buyPrice - this.buyStartIndex) * h;
                  w = 174;
                  if (mouseX > leftLimit && mouseX < leftLimit + w && (float)mouseY > topLimit && (float)mouseY < topLimit + h && this.selectedItem != buyPrice) {
                     this.selectedItem = buyPrice;
                     this.quantity = 1;
                  }
               }
            }
         } else {
            for(buyPrice = this.sellStartIndex; buyPrice < 6 + this.sellStartIndex; ++buyPrice) {
               if (buyPrice < sellItems.size()) {
                  h = 21.0F;
                  topLimit = (float)(this.field_146295_m / 2 - 49) + (float)(buyPrice - this.sellStartIndex) * h;
                  w = 174;
                  if (mouseX > leftLimit && mouseX < leftLimit + w && (float)mouseY > topLimit && (float)mouseY < topLimit + h && this.selectedItem != buyPrice) {
                     this.selectedItem = buyPrice;
                     this.quantity = 1;
                  }
               }
            }
         }

         this.clickBuyMiniScreen(mouseX, mouseY, true);
         if (this.isBuyMiniScreenVisible() && mouseX > this.field_146294_l / 2 - 94 && mouseX < this.field_146294_l / 2 - 94 + 49 && mouseY > this.field_146295_m / 2 - 29 && mouseY < this.field_146295_m / 2 - 29 + 18) {
            if (this.currentTab == EnumBuySell.Buy) {
               if (this.selectedItem < buyItems.size()) {
                  buyPrice = ((ClientShopItem)buyItems.get(this.selectedItem)).getBuy();
                  if (this.quantity * buyPrice <= ClientData.playerMoney) {
                     this.sendBuyPacket();
                     this.selectedItem = -1;
                  }
               }
            } else if (this.selectedItem < sellItems.size()) {
               ClientShopItem sellItem = (ClientShopItem)sellItems.get(this.selectedItem);
               this.sendSellPacket();
               sellItem.amount -= this.quantity;
               this.selectedItem = -1;
            }
         }

      }
   }

   private int checkRemainingSlots(ItemStack buying) {
      if (buying != null && buying != ItemStack.field_190927_a && buying.func_77973_b() != Items.field_190931_a) {
         try {
            EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
            int available = 0;
            Iterator var4 = (new ArrayList(player.field_71071_by.field_70462_a)).iterator();

            while(true) {
               while(var4.hasNext()) {
                  ItemStack curStack = (ItemStack)var4.next();
                  if (curStack != null && curStack.func_77969_a(buying)) {
                     available += buying.func_77976_d() - curStack.func_190916_E();
                  } else if (curStack == null || curStack == ItemStack.field_190927_a || curStack.func_77973_b() == Items.field_190931_a) {
                     available += buying.func_77976_d();
                  }
               }

               return Math.min(2304, available);
            }
         } catch (Throwable var6) {
            var6.printStackTrace();
            return 2304;
         }
      } else {
         return 0;
      }
   }

   protected void clickBuyMiniScreen(int mouseX, int mouseY, boolean isInstant) {
      if (!(this.floatQuantity <= 0.0F) && !isInstant) {
         this.floatQuantity -= 1.0F / this.incrementInterval;
      } else if (this.isBuyMiniScreenVisible() && this.allowMultiple) {
         ClientShopItem item;
         if (mouseX > this.field_146294_l / 2 - 71 && mouseX < this.field_146294_l / 2 - 71 + 25 && mouseY > this.field_146295_m / 2 - 58 && mouseY < this.field_146295_m / 2 - 58 + 6) {
            if (this.currentTab != EnumBuySell.Buy) {
               if (this.quantity + 1 <= ((ClientShopItem)sellItems.get(this.selectedItem)).amount) {
                  ++this.quantity;
               } else {
                  this.quantity = 1;
               }

               this.floatQuantity = 2.0F;
               this.decreaseIncrementInterval();
            } else {
               item = (ClientShopItem)buyItems.get(this.selectedItem);
               if ((this.quantity + 1) * item.getBuy() <= ClientData.playerMoney && this.quantity + 1 <= Math.min(this.checkRemainingSlots(item.getItemStack()), PixelmonConfig.getShopMaxStackSize(item.getItemStack()))) {
                  ++this.quantity;
               } else {
                  this.quantity = 1;
               }

               this.floatQuantity = 2.0F;
               this.decreaseIncrementInterval();
            }
         }

         if (mouseX > this.field_146294_l / 2 - 71 && mouseX < this.field_146294_l / 2 - 71 + 25 && mouseY > this.field_146295_m / 2 - 42 && mouseY < this.field_146295_m / 2 - 42 + 7) {
            --this.quantity;
            this.floatQuantity = 2.0F;
            if (this.quantity < 1) {
               if (this.currentTab == EnumBuySell.Buy) {
                  item = (ClientShopItem)buyItems.get(this.selectedItem);
                  this.quantity = Math.min(this.checkRemainingSlots(item.getItemStack()), PixelmonConfig.getShopMaxStackSize(item.getItemStack()));
               } else {
                  this.quantity = ((ClientShopItem)sellItems.get(this.selectedItem)).amount;
               }

               if (this.quantity == 0) {
                  this.quantity = 1;
               }

               this.floatQuantity = 2.0F;
            }

            this.decreaseIncrementInterval();
         }
      }

      if (isInstant) {
         this.incrementInterval = 8.0F;
      }

   }

   private void decreaseIncrementInterval() {
      this.incrementInterval = Math.max(0.5F, this.incrementInterval * 0.75F);
   }

   protected void closeScreen() {
      Minecraft.func_71410_x().field_71439_g.func_71053_j();
   }

   protected void sendBuyPacket() {
   }

   protected void sendSellPacket() {
   }

   protected void renderBuyScreen(int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 40), (double)(this.field_146295_m / 2 - 102), 197.0, 201.0F, 0.23046875, 0.0, 1.0, 0.78515625, this.field_73735_i);
      if (this.isBuyMiniScreenVisible() && mouseX > this.field_146294_l / 2 + 139 && mouseX < this.field_146294_l / 2 + 139 + 14 && mouseY > this.field_146295_m / 2 + 79 && mouseY < this.field_146295_m / 2 + 79 + 17) {
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 139), (double)(this.field_146295_m / 2 + 79), 14.0, 17.0F, 0.9296875, 0.87890625, 0.984375, 0.9453125, this.field_73735_i);
      }

      String moneyLabel = I18n.func_135052_a("gui.shopkeeper.money", new Object[0]);
      this.func_73731_b(this.field_146297_k.field_71466_p, moneyLabel, this.field_146294_l / 2 + 118 - this.field_146297_k.field_71466_p.func_78256_a(moneyLabel) / 2, this.field_146295_m / 2 - 90, 16777215);
      String playerMoneyLabel = "" + ClientData.playerMoney;
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedollar);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 118 - this.field_146297_k.field_71466_p.func_78256_a(playerMoneyLabel + 8) / 2), (double)(this.field_146295_m / 2 - 78), 6.0, 9.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.func_73731_b(this.field_146297_k.field_71466_p, playerMoneyLabel, this.field_146294_l / 2 + 118 - this.field_146297_k.field_71466_p.func_78256_a(playerMoneyLabel + 8) / 2 + 8, this.field_146295_m / 2 - 77, 16777215);
      if (this.currentTab == EnumBuySell.Buy) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 31), (double)(this.field_146295_m / 2 - 93), 58.0, 30.0F, 0.265625, 0.8828125, 0.4921875, 1.0, this.field_73735_i);
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 28), (double)(this.field_146295_m / 2 - 93), 58.0, 30.0F, 0.265625, 0.8828125, 0.4921875, 1.0, this.field_73735_i);
      }

      this.renderMenu(mouseX, mouseY);
   }

   protected void renderMenu(int mouseX, int mouseY) {
      int colour = 16777215;
      int leftLimit = this.field_146294_l / 2 - 28;
      int startIndex = this.currentTab == EnumBuySell.Buy ? this.buyStartIndex : this.sellStartIndex;
      ArrayList listItems = this.currentTab == EnumBuySell.Buy ? buyItems : sellItems;

      int i;
      float h;
      float topLimit;
      ItemStack item;
      for(i = startIndex; i < 6 + startIndex; ++i) {
         if (i < listItems.size()) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
            h = 21.0F;
            topLimit = (float)(this.field_146295_m / 2 - 49) + (float)(i - startIndex) * h;
            int w = 174;
            if (mouseX > leftLimit && mouseX < leftLimit + w && (float)mouseY > topLimit && (float)mouseY < topLimit + h || this.selectedItem == i) {
               GuiHelper.drawImageQuad((double)leftLimit, (double)topLimit, (double)w, 20.0F, 0.27734375, 0.79296875, 0.95703125, 0.87109375, this.field_73735_i);
            }

            int itemLeft = this.field_146294_l / 2;
            int itemRight = this.field_146294_l / 2 + 140;
            item = ((ClientShopItem)listItems.get(i)).getItemStack();
            String itemName = item.func_82833_r();
            if (this.currentTab == EnumBuySell.Sell) {
               itemName = itemName + " x" + ((ClientShopItem)listItems.get(i)).amount;
            }

            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedollar);
            String cost = "";
            cost = cost + (this.currentTab == EnumBuySell.Buy ? ((ClientShopItem)listItems.get(i)).getBuy() : ((ClientShopItem)listItems.get(i)).getSell());
            int costWidth = Math.min(24, this.field_146297_k.field_71466_p.func_78256_a(cost));
            GuiHelper.drawImageQuad((double)(itemRight - costWidth - 8), (double)((int)(topLimit + 6.0F)), 6.0, 9.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            GuiHelper.drawSquashedString(this.field_146297_k.field_71466_p, itemName, false, (double)(140 - costWidth - 15), (float)itemLeft, (float)((int)(topLimit + 7.0F)), 16777215, true);
            colour = 14540253;
            if (this.currentTab == EnumBuySell.Buy && ((ClientShopItem)listItems.get(i)).getBuy() > ClientData.playerMoney) {
               colour = 16729156;
            }

            GuiHelper.drawSquashedString(this.field_146297_k.field_71466_p, cost, false, (double)costWidth, (float)(itemRight - costWidth), (float)((int)(topLimit + 7.0F)), colour, false);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
      if (startIndex > 0) {
         if (mouseX > this.field_146294_l / 2 + 51 && mouseX < this.field_146294_l / 2 + 51 + 16 && mouseY > this.field_146295_m / 2 - 61 && mouseY < this.field_146295_m / 2 - 61 + 10) {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 51), (double)(this.field_146295_m / 2 - 61), 17.0, 10.0F, 0.58203125, 0.8828125, 0.6484375, 0.921875, this.field_73735_i);
         } else {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 51), (double)(this.field_146295_m / 2 - 61), 17.0, 10.0F, 0.65625, 0.8828125, 0.72265625, 0.921875, this.field_73735_i);
         }
      }

      if (startIndex + 6 < listItems.size()) {
         if (mouseX > this.field_146294_l / 2 + 51 && mouseX < this.field_146294_l / 2 + 51 + 16 && mouseY > this.field_146295_m / 2 + 78 && mouseY < this.field_146295_m / 2 + 78 + 10) {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 51), (double)(this.field_146295_m / 2 + 78), 17.0, 10.0F, 0.58203125, 0.9375, 0.6484375, 0.9765625, this.field_73735_i);
         } else {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 51), (double)(this.field_146295_m / 2 + 78), 17.0, 10.0F, 0.65625, 0.9375, 0.72265625, 0.9765625, this.field_73735_i);
         }
      }

      if (this.isBuyMiniScreenVisible()) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 99), (double)(this.field_146295_m / 2 - 102), 59.0, 97.0F, 0.0, 0.0, 0.23046875, 0.37890625, this.field_73735_i);
         String priceLabel = I18n.func_135052_a("gui.shopkeeper.price", new Object[0]);
         this.func_73731_b(this.field_146297_k.field_71466_p, priceLabel, this.field_146294_l / 2 - 69 - this.field_146297_k.field_71466_p.func_78256_a(priceLabel) / 2, this.field_146295_m / 2 - 96, 16777215);
         int price = this.currentTab == EnumBuySell.Buy ? ((ClientShopItem)listItems.get(this.selectedItem)).getBuy() : ((ClientShopItem)listItems.get(this.selectedItem)).getSell();
         String priceAmount = "" + this.quantity * price;
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedollar);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 70 - (this.field_146297_k.field_71466_p.func_78256_a(priceAmount) + 8) / 2 - 4), (double)(this.field_146295_m / 2 - 84), 6.0, 9.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         if (this.currentTab == EnumBuySell.Buy && this.quantity * price > ClientData.playerMoney) {
            colour = 16729156;
         }

         colour = 14540253;
         this.field_146297_k.field_71466_p.func_78276_b(priceAmount, this.field_146294_l / 2 - 69 - (this.field_146297_k.field_71466_p.func_78256_a(priceAmount) + 8) / 2 + 4, this.field_146295_m / 2 - 84, colour);
         String quantityLabel = I18n.func_135052_a("gui.shopkeeper.quantity", new Object[0]);
         this.func_73731_b(this.field_146297_k.field_71466_p, quantityLabel, this.field_146294_l / 2 - 69 - this.field_146297_k.field_71466_p.func_78256_a(quantityLabel) / 2, this.field_146295_m / 2 - 69, 16777215);
         String quantityAmount = "" + this.quantity;
         this.func_73731_b(this.field_146297_k.field_71466_p, quantityAmount, this.field_146294_l / 2 - 58 - this.field_146297_k.field_71466_p.func_78256_a(quantityAmount) / 2, this.field_146295_m / 2 - 51, 16777215);
         if (mouseX > this.field_146294_l / 2 - 94 && mouseX < this.field_146294_l / 2 - 94 + 49 && mouseY > this.field_146295_m / 2 - 29 && mouseY < this.field_146295_m / 2 - 29 + 18) {
            boolean validTransaction = this.currentTab == EnumBuySell.Buy ? this.quantity * price <= ClientData.playerMoney : this.quantity <= ((ClientShopItem)listItems.get(this.selectedItem)).amount;
            if (validTransaction) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 94), (double)(this.field_146295_m / 2 - 29), 49.0, 18.0F, 0.01953125, 0.796875, 0.2109375, 0.8671875, this.field_73735_i);
            }
         }

         colour = 16777215;
         if (this.currentTab == EnumBuySell.Buy && this.quantity * price > ClientData.playerMoney) {
            colour = 7829367;
         }

         String buyLabel = I18n.func_135052_a(this.currentTab == EnumBuySell.Buy ? "gui.shopkeeper.buy" : "gui.shopkeeper.sell", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(buyLabel, this.field_146294_l / 2 - 69 - this.field_146297_k.field_71466_p.func_78256_a(buyLabel) / 2, this.field_146295_m / 2 - 24, colour);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shopkeeper);
         if (this.allowMultiple) {
            if (mouseX > this.field_146294_l / 2 - 72 && mouseX < this.field_146294_l / 2 - 72 + 25 && mouseY > this.field_146295_m / 2 - 58 && mouseY < this.field_146295_m / 2 - 58 + 6) {
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 63), (double)(this.field_146295_m / 2 - 58), 9.0, 6.0F, 0.09765625, 0.8828125, 0.1328125, 0.90625, this.field_73735_i);
            } else {
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 63), (double)(this.field_146295_m / 2 - 58), 9.0, 6.0F, 0.140625, 0.8828125, 0.17578125, 0.90625, this.field_73735_i);
            }

            if (mouseX > this.field_146294_l / 2 - 72 && mouseX < this.field_146294_l / 2 - 72 + 25 && mouseY > this.field_146295_m / 2 - 42 && mouseY < this.field_146295_m / 2 - 42 + 7) {
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 63), (double)(this.field_146295_m / 2 - 42), 9.0, 6.0F, 0.09765625, 0.9140625, 0.1328125, 0.9375, this.field_73735_i);
            } else {
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 63), (double)(this.field_146295_m / 2 - 42), 9.0, 6.0F, 0.140625, 0.9140625, 0.17578125, 0.9375, this.field_73735_i);
            }
         }

         item = ((ClientShopItem)listItems.get(this.selectedItem)).getItemStack();
         this.field_146296_j.func_175042_a(item, this.field_146294_l / 2 - 91, this.field_146295_m / 2 - 55);
      }

      for(i = startIndex; i < 6 + startIndex; ++i) {
         h = 21.0F;
         topLimit = (float)(this.field_146295_m / 2 - 49) + (float)(i - startIndex) * h;
         if (i < listItems.size()) {
            ItemStack item = ((ClientShopItem)listItems.get(i)).getItemStack();
            this.field_146296_j.func_175042_a(item, leftLimit + 2, (int)(topLimit + 2.0F));
         }
      }

   }
}
