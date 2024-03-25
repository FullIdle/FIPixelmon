package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiTradeEditor extends GuiScreenDropDown {
   public int top;
   public int left;
   GuiTextField offerName;
   GuiTextField exchangeName;
   GuiTextField descriptionTextBox;
   NPCTrader trader;
   PokemonSpec offer;
   boolean offerChanged = false;
   PokemonSpec exchange;
   boolean exchangeChanged = false;
   String description;
   private TextureEditorNPC textureEditor;

   public GuiTradeEditor(int traderId) {
      Keyboard.enableRepeatEvents(true);
      this.xSize = 256;
      this.ySize = 226;
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, traderId, NPCTrader.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.trader = (NPCTrader)entityNPCOptional.get();
         this.offer = ClientProxy.currentTradePair.offer;
         this.exchange = ClientProxy.currentTradePair.exchange;
         this.description = ClientProxy.currentTradePair.description;
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if (this.trader == null) {
         GuiHelper.closeScreen();
      } else {
         if (ClientProxy.currentTradePair == null) {
            ClientProxy.currentTradePair = new TradePair(new PokemonSpec("random"), new PokemonSpec("random"));
         }

         this.left = (this.field_146294_l - this.xSize) / 2;
         this.top = (this.field_146295_m - this.ySize) / 2;
         this.offerName = (new GuiTabCompleteTextField(6, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 45, this.field_146295_m / 2 + 10, 90, 17)).setCompletions(EnumSpecies.getNameList());
         this.offerName.func_146203_f(150);
         this.offerName.func_146180_a(Entity1Base.getLocalizedName(this.offer.name));
         this.exchangeName = (new GuiTabCompleteTextField(7, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 100, this.field_146295_m / 2 + 10, 90, 17)).setCompletions(EnumSpecies.getNameList());
         this.exchangeName.func_146203_f(150);
         this.exchangeName.func_146180_a(Entity1Base.getLocalizedName(this.exchange.name));
         this.descriptionTextBox = new GuiTextField(8, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 45, this.field_146295_m / 2 + 50, 235, 15);
         this.descriptionTextBox.func_146203_f(150);
         if (this.description != null) {
            this.descriptionTextBox.func_146180_a(this.description);
         }

         this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
         this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 100, this.field_146295_m / 2 - 110, 80, 20, I18n.func_135052_a("gui.tradereditor.delete", new Object[0])));
         this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 40, this.field_146295_m / 2 - 110, 80, 20, I18n.func_135052_a("gui.tradereditor.random", new Object[0])));
         this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 58, this.field_146295_m / 2 + 9, 30, 20, "<->"));
         this.textureEditor = new TextureEditorNPC(this, this.trader, this.field_146294_l / 2 - 190, this.field_146295_m / 2 + 50, 130, -23);
      }
   }

   protected void drawBackgroundUnderMenus(float var1, int var2, int var3) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      GuiHelper.drawEntity(this.trader, this.field_146294_l / 2 - 125, this.field_146295_m / 2 + 40, 50.0F, 0.0F, 0.0F);
      if (ClientProxy.currentTradePair.offer.shiny == Boolean.TRUE) {
         this.offerName.func_146193_g(16777011);
      } else {
         this.offerName.func_146193_g(16777215);
      }

      this.offerName.func_146194_f();
      if (ClientProxy.currentTradePair.exchange.shiny == Boolean.TRUE) {
         this.exchangeName.func_146193_g(16777011);
      } else {
         this.exchangeName.func_146193_g(16777215);
      }

      this.exchangeName.func_146194_f();
      this.descriptionTextBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.tradereditor.offer", new Object[0]), this.left + 85, this.top + 110, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.tradereditor.exchange", new Object[0]), this.left + 225, this.top + 110, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.tradereditor.description", new Object[0]), this.left + 85, this.top + 150, 0);
      this.textureEditor.drawCustomTextBox();
   }

   protected void func_73869_a(char key, int keyCode) {
      String beforeText = this.offerName.func_146179_b();
      this.offerName.func_146201_a(key, keyCode);
      String afterText = this.offerName.func_146179_b();
      if (!beforeText.equals(afterText)) {
         this.offerChanged = true;
      }

      beforeText = this.exchangeName.func_146179_b();
      this.exchangeName.func_146201_a(key, keyCode);
      afterText = this.exchangeName.func_146179_b();
      if (!beforeText.equals(afterText)) {
         this.exchangeChanged = true;
      }

      this.descriptionTextBox.func_146201_a(key, keyCode);
      if (keyCode != 15) {
         this.textureEditor.keyTyped(key, keyCode);
      }

      if (keyCode == 1 || keyCode == 28) {
         this.saveFields();
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      this.offerName.func_146192_a(x, y, mouseButton);
      this.exchangeName.func_146192_a(x, y, mouseButton);
      this.descriptionTextBox.func_146192_a(x, y, mouseButton);
      this.textureEditor.mouseClicked(x, y, mouseButton);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 0) {
            this.saveFields();
         } else if (button.field_146127_k == 1) {
            GuiHelper.closeScreen();
            Minecraft.func_71410_x().func_175607_a(Minecraft.func_71410_x().field_71439_g);
            Pixelmon.network.sendToServer(new DeleteNPC(this.trader.getId()));
         } else if (button.field_146127_k == 2) {
            EnumSpecies[] list = EnumSpecies.values();
            String poke1 = list[RandomHelper.getRandomNumberBetween(0, list.length - 1)].name;
            String poke2 = list[RandomHelper.getRandomNumberBetween(0, list.length - 1)].name;
            if (poke2.equalsIgnoreCase(poke1)) {
               poke2 = list[RandomHelper.getRandomNumberBetween(0, list.length - 1)].name;
            }

            this.offerName.func_146180_a(Entity1Base.getLocalizedName(poke1));
            this.exchangeName.func_146180_a(Entity1Base.getLocalizedName(poke2));
            this.offerChanged = this.exchangeChanged = true;
         } else if (button.field_146127_k == 3) {
            String text = this.offerName.func_146179_b();
            this.offerName.func_146180_a(this.exchangeName.func_146179_b());
            this.exchangeName.func_146180_a(text);
            this.offerChanged = this.exchangeChanged = true;
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   private void saveFields() {
      String offerText = this.offerName.func_146179_b();
      String exchangeText = this.exchangeName.func_146179_b();
      this.description = this.descriptionTextBox.func_146179_b();
      PokemonSpec offer = new PokemonSpec(offerText.split(" "));
      PokemonSpec exchange = new PokemonSpec(exchangeText.split(" "));
      if (exchange.name != null && offer.name != null) {
         this.setNewTradePokemon(this.offerChanged ? offer : this.offer, this.exchangeChanged ? exchange : this.exchange);
         this.offerChanged = this.exchangeChanged = false;
         GuiHelper.closeScreen();
         Minecraft.func_71410_x().func_175607_a(Minecraft.func_71410_x().field_71439_g);
      }

      this.textureEditor.saveCustomTexture();
   }

   private void setNewTradePokemon(PokemonSpec offer, PokemonSpec exchange) {
      ClientProxy.currentTradePair = new TradePair(offer, exchange, this.description);
      Pixelmon.network.sendToServer(new NPCServerPacket(this.trader.getId(), offer, exchange, this.description));
   }
}
