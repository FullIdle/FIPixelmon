package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.RenamePokemon;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiRenamePokemon extends GuiScreen {
   protected int xSize = 176;
   protected int ySize = 166;
   protected GuiScreenPokeChecker parent;
   private GuiTextFieldTransparent textField;

   public GuiRenamePokemon(GuiScreenPokeChecker parent) {
      this.parent = (GuiScreenPokeChecker)Preconditions.checkNotNull(parent);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiRenameButton(0, this.field_146294_l / 2 - 98, this.field_146295_m / 4 + 80, I18n.func_135052_a("gui.renamePoke.renamebutton", new Object[0])));
      this.field_146292_n.add(new GuiRenameButton(1, this.field_146294_l / 2 + 48, this.field_146295_m / 4 + 80, I18n.func_135052_a("gui.renamePoke.cancel", new Object[0])));
      this.field_146292_n.add(new GuiRenameButton(2, this.field_146294_l / 2 - 25, this.field_146295_m / 4 + 80, I18n.func_135052_a("gui.renamePoke.Reset", new Object[0])));
      Keyboard.enableRepeatEvents(true);
      this.textField = new GuiTextFieldTransparent(this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 68, this.field_146295_m / 4 + 37, 140, 30);
      this.textField.setFocused(true);
      this.textField.setText(this.parent.pokemon.getNickname() == null ? this.parent.pokemon.getSpecies().getPokemonName() : this.parent.pokemon.getNickname());
   }

   public void func_73876_c() {
      super.func_73876_c();
      this.textField.updateCursorCounter();
   }

   public void func_146281_b() {
      super.func_146281_b();
      Keyboard.enableRepeatEvents(false);
   }

   protected void func_146284_a(GuiButton button) {
      switch (button.field_146127_k) {
         case 0:
            Pixelmon.network.sendToServer(new RenamePokemon(this.parent.position, this.parent.pokemon.getUUID(), this.textField.getText()));
            this.parent.pokemon.setNickname(this.textField.getText());
            this.field_146297_k.func_147108_a(this.parent);
            break;
         case 1:
            this.field_146297_k.func_147108_a(this.parent);
            break;
         case 2:
            Pixelmon.network.sendToServer(new RenamePokemon(this.parent.position, this.parent.pokemon.getUUID(), ""));
            this.parent.pokemon.setNickname((String)null);
            this.field_146297_k.func_147108_a(this.parent);
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) {
      if (typedChar != '%') {
         String old = this.textField.getText();
         this.textField.textboxKeyTyped(typedChar, keyCode);
         if (this.textField.getText().replaceAll("[&§][0-9a-fk-orA-FK-OR]", "").length() > 16) {
            this.textField.setText(old);
         }

         ((GuiButton)this.field_146292_n.get(0)).field_146124_l = !this.textField.getText().trim().isEmpty();
         if (keyCode == 28) {
            this.func_146284_a((GuiButton)this.field_146292_n.get(0));
         }

      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.textField.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.rename);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, this.field_146295_m / 4, 0, 0, 256, 114);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.renamePoke.renamePokemon", new Object[0]), this.field_146294_l / 2, this.field_146295_m / 4 - 60 + 80, 16777215);
      this.textField.drawTextBox();
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }
}
