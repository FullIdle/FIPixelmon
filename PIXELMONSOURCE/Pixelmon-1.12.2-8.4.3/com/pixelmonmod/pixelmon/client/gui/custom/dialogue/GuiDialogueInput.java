package com.pixelmonmod.pixelmon.client.gui.custom.dialogue;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiTextFieldTransparent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueInputAction;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;

public class GuiDialogueInput extends GuiScreen {
   private final ITextComponent title;
   private final ITextComponent text;
   private final String defaultText;
   GuiTextFieldTransparent input;
   private boolean sent = false;

   public GuiDialogueInput(ITextComponent name, ITextComponent text, String defaultText) {
      this.field_146297_k = Minecraft.func_71410_x();
      this.title = name;
      this.text = text;
      this.defaultText = defaultText;
   }

   public void func_73866_w_() {
      this.input = new GuiTextFieldTransparent(this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 90, this.field_146295_m / 4 + 50, 190, 30);
      this.input.setText(this.defaultText);
      this.input.setFocused(true);
      this.input.setMaxStringLength(50);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.drawDialogueBox(this, (ITextComponent)this.title, (ITextComponent)this.text, 0.0F);
      this.field_146297_k.func_110434_K().func_110577_a(GuiResources.dialogueInput);
      func_146110_a(this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50, 0.0F, 0.0F, 200, 30, 200.0F, 30.0F);
      this.input.drawTextBox();
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   public void func_73876_c() {
      this.input.updateCursorCounter();
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.input.textboxKeyTyped(typedChar, keyCode);
      super.func_73869_a(typedChar, keyCode);
      if (keyCode == 28) {
         this.sent = true;
         Pixelmon.network.sendToServer(new DialogueInputAction(this.input.getText()));
         GuiHelper.closeScreen();
      }

   }

   public void func_146281_b() {
      if (!this.sent) {
         Pixelmon.network.sendToServer(new DialogueInputAction((String)null));
      }

      super.func_146281_b();
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.input.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public boolean func_73868_f() {
      return super.func_73868_f();
   }
}
