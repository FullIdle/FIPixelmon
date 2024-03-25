package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class BattleScreen extends GuiScreen {
   protected GuiBattle parent;
   protected ClientBattleManager bm;
   protected BattleMode mode;

   public abstract void drawScreen(int var1, int var2, int var3, int var4);

   public abstract void click(int var1, int var2, int var3, int var4);

   public BattleScreen(GuiBattle parent, BattleMode mode) {
      this.field_146297_k = Minecraft.func_71410_x();
      this.field_146296_j = this.field_146297_k.func_175599_af();
      this.field_146289_q = this.field_146297_k.field_71466_p;
      this.mode = mode;
      this.parent = parent;
      this.bm = ClientProxy.battleManager;
   }

   public boolean isScreen() {
      return this.bm.mode == this.mode;
   }

   public BattleMode getMode() {
      return this.mode;
   }
}
