package com.pixelmonmod.pixelmon.client.gui.pc;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorageSearch;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiArrowButton;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonImageClickable;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiKeybindIcon;
import com.pixelmonmod.pixelmon.client.listener.WallpapersListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ServerSetLastOpenBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiPC extends GuiPokemonScreen {
   public static PCStorageSearch search;
   protected GuiButtonImageClickable leftArrow;
   protected GuiButtonImageClickable rightArrow;
   protected GuiButtonImageClickable boxButton;
   protected GuiArrowButton leftArrowWallpaper;
   protected GuiArrowButton rightArrowWallpaper;
   protected GuiTextField nameField;
   protected GuiTextField searchField;
   protected List icons;
   protected GuiKeybindIcon searchIcon;
   protected GuiKeybindIcon renameIcon;
   protected GuiKeybindIcon wallpaperIcon;
   protected ResourceLocation boxWallpaper;
   private int normalBoxCount;

   public GuiPC(@Nullable StoragePosition selected) {
      super(selected);
      Preconditions.checkArgument(WallpapersListener.getWallpapers().size() > 0, "There must be at least one wallpaper to initialize the pc");
      this.icons = new ArrayList();
      this.icons.add(this.wallpaperIcon = new GuiKeybindIcon(ClientProxy.pcWallpaperKeyBind, GuiResources.pcWallpaperIcon));
      this.icons.add(this.renameIcon = new GuiKeybindIcon(ClientProxy.pcRenameKeyBind, GuiResources.pcRenameIcon));
      this.icons.add(this.searchIcon = new GuiKeybindIcon(ClientProxy.pcSearchKeyBind, GuiResources.pcSearchIcon));
      this.leftArrow = new GuiButtonImageClickable(0, 0, 0, 0, 186, 17, 23);
      this.rightArrow = new GuiButtonImageClickable(1, 0, 0, 143, 186, 17, 23);
      this.boxButton = new GuiButtonImageClickable(2, 0, 0, 22, 186, 116, 23);
      this.boxButton.field_146124_l = false;
      this.leftArrowWallpaper = new GuiArrowButton(4, 0, 0, GuiArrowButton.Direction.LEFT);
      this.leftArrowWallpaper.field_146124_l = false;
      this.leftArrowWallpaper.field_146125_m = false;
      this.rightArrowWallpaper = new GuiArrowButton(5, 0, 0, GuiArrowButton.Direction.RIGHT);
      this.rightArrowWallpaper.field_146124_l = false;
      this.rightArrowWallpaper.field_146125_m = false;
      this.nameField = new GuiTextField(0, this.field_146289_q, 0, 0, 116, 23);
      this.nameField.func_146193_g(16777215);
      this.nameField.func_146203_f(17);
      this.nameField.func_146185_a(false);
      this.nameField.func_146205_d(false);
      this.nameField.func_146189_e(false);
      this.searchField = new GuiTextField(1, this.field_146289_q, 0, 0, 114, 23);
      this.searchField.func_146193_g(16777215);
      this.searchField.func_146203_f(35);
      this.searchField.func_146189_e(false);
      this.updateBox(this.boxNumber);
   }

   public GuiPC() {
      this((StoragePosition)null);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146292_n.add(this.leftArrow.updatePosition(this.field_146294_l / 2 - 80, this.field_146295_m / 6 - 21));
      this.field_146292_n.add(this.rightArrow.updatePosition(this.field_146294_l / 2 + 63, this.field_146295_m / 6 - 21));
      this.field_146292_n.add(this.boxButton.updatePosition(this.field_146294_l / 2 - 58, this.field_146295_m / 6 - 21));
      this.field_146292_n.add(new GuiButtonTrashCan(this, 3, this.field_146294_l / 2 + 111, this.field_146295_m / 6 + 110));
      this.field_146292_n.add(this.leftArrowWallpaper.updatePosition(this.field_146294_l / 2 - 125, this.field_146295_m / 6 + 58));
      this.field_146292_n.add(this.rightArrowWallpaper.updatePosition(this.field_146294_l / 2 + 108, this.field_146295_m / 6 + 58));
      this.nameField.field_146211_a = this.field_146289_q;
      this.nameField.field_146209_f = this.field_146294_l / 2 - 51;
      this.nameField.field_146210_g = this.field_146295_m / 6 - 14;
      this.searchField.field_146211_a = this.field_146289_q;
      this.searchField.field_146209_f = this.field_146294_l / 2 - 57;
      this.searchField.field_146210_g = this.field_146295_m / 6 - 20;
      Keyboard.enableRepeatEvents(true);
      GuiPixelmonOverlay.icons.forEach((icon) -> {
         icon.setEnabled(false);
      });
   }

   private void updateBox(int boxNumber) {
      int oldBox = this.boxNumber;
      this.boxNumber = boxNumber;
      if (this.boxNumber < 0) {
         this.boxNumber = this.storage.getBoxCount() - 1;
      } else if (this.boxNumber >= this.storage.getBoxCount()) {
         this.boxNumber = 0;
      }

      this.storage.setLastBox(this.boxNumber);
      PCBox data = this.storage.getBox(this.boxNumber);
      this.updateName(data);
      this.updateWallpaper(data);
      if (oldBox != boxNumber) {
         this.leftArrowWallpaper.field_146124_l = false;
         this.leftArrowWallpaper.field_146125_m = false;
         this.rightArrowWallpaper.field_146124_l = false;
         this.rightArrowWallpaper.field_146125_m = false;
      }

   }

   protected void updateName(PCBox data) {
      this.boxButton.setText(data.getName() == null ? I18n.func_135052_a("gui.pc.box", new Object[]{this.boxNumber + 1}) : data.getName());
   }

   protected void updateWallpaper(PCBox data) {
      if (!WallpapersListener.hasWallpaper(data.getWallpaper())) {
         data.setWallpaper((String)null);
      }

      this.boxWallpaper = WallpapersListener.getWallpaper(data.getWallpaper());
      this.leftArrow.setImage(this.boxWallpaper);
      this.rightArrow.setImage(this.boxWallpaper);
      this.boxButton.setImage(this.boxWallpaper);
   }

   protected void switchWallpaper(boolean forward) {
      PCBox data = this.storage.getBox(this.boxNumber);
      int index = (data.getWallpaper() == null ? 0 : WallpapersListener.getWallpapers().indexOf(data.getWallpaper())) + (forward ? 1 : -1);
      if (index < 0) {
         index = WallpapersListener.getWallpapers().size() - 1;
      } else if (index >= WallpapersListener.getWallpapers().size()) {
         index = 0;
      }

      data.setWallpaper((String)WallpapersListener.getWallpapers().get(index));
      this.updateWallpaper(data);
   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_146284_a(GuiButton button) {
      switch (button.field_146127_k) {
         case 0:
            this.updateBox(this.boxNumber - 1);
            break;
         case 1:
            this.updateBox(this.boxNumber + 1);
         case 2:
         default:
            break;
         case 3:
            this.field_146297_k.func_147108_a(new GuiReleaseWarning(this));
            break;
         case 4:
            this.switchWallpaper(false);
            break;
         case 5:
            this.switchWallpaper(true);
      }

   }

   private void closeSearch() {
      search = null;
      this.searchField.func_146189_e(false);
      this.updateStorage(ClientStorageManager.openPC, this.selectedPokemon == null ? null : this.selectedPokemon.getPosition());
      this.updateBox(this.normalBoxCount);
   }

   private void closeName(boolean save) {
      if (save) {
         this.leftArrow.field_146124_l = true;
         this.rightArrow.field_146124_l = true;
         PCBox data = this.storage.getBox(this.boxNumber);
         data.setName(this.nameField.func_146179_b().equals(I18n.func_135052_a("gui.pc.box", new Object[]{this.boxNumber + 1})) ? null : this.nameField.func_146179_b());
         this.updateName(data);
         this.nameField.func_146189_e(false);
      } else {
         this.leftArrow.field_146124_l = true;
         this.rightArrow.field_146124_l = true;
         this.updateName(this.storage.getBox(this.boxNumber));
         this.nameField.func_146189_e(false);
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (this.searchField.func_146176_q()) {
         if (keyCode == 1) {
            this.closeSearch();
         } else if (keyCode == 28) {
            this.searchField.func_146195_b(false);
         } else if (this.searchField.func_146201_a(typedChar, keyCode)) {
            if (this.boxNumber != 0) {
               this.updateBox(0);
            }

            search.query(this.searchField.func_146179_b());
         } else if (keyCode == 31) {
            this.searchField.func_146195_b(true);
         }
      } else if (this.nameField.func_146176_q()) {
         if (keyCode == 1) {
            this.closeName(false);
         } else if (keyCode == 28) {
            this.closeName(true);
         } else {
            this.nameField.func_146201_a(typedChar, keyCode);
         }
      } else if (keyCode == ClientProxy.pcSearchKeyBind.func_151463_i()) {
         if (!this.searchField.func_146176_q()) {
            this.searchField.func_146189_e(true);
            this.searchField.func_146180_a("");
            this.searchField.func_146195_b(true);
            this.updateSelected((StoragePosition)null);
            this.updateStorage(search = new PCStorageSearch(6, 5), this.selected);
            this.normalBoxCount = this.boxNumber;
            this.updateBox(this.boxNumber);
         }
      } else if (keyCode == ClientProxy.pcRenameKeyBind.func_151463_i()) {
         if (!this.nameField.func_146176_q()) {
            this.leftArrow.field_146124_l = false;
            this.rightArrow.field_146124_l = false;
            this.nameField.func_146189_e(true);
            this.nameField.func_146180_a(this.boxButton.field_146126_j);
            this.boxButton.setText("");
            this.nameField.func_146195_b(true);
         }
      } else if (keyCode == ClientProxy.pcWallpaperKeyBind.func_151463_i()) {
         boolean state = !this.leftArrowWallpaper.field_146124_l;
         this.leftArrowWallpaper.field_146124_l = state;
         this.leftArrowWallpaper.field_146125_m = state;
         this.rightArrowWallpaper.field_146124_l = state;
         this.rightArrowWallpaper.field_146125_m = state;
         if (!state) {
            Arrays.stream(ClientStorageManager.openPC.getBoxes()).forEach(PCBox::sendChangesToServer);
         }
      } else if (keyCode == 203) {
         this.switchOrSound((GuiButton)(this.leftArrowWallpaper.field_146124_l ? this.leftArrowWallpaper : this.leftArrow));
      } else if (keyCode == 205) {
         this.switchOrSound((GuiButton)(this.rightArrowWallpaper.field_146124_l ? this.rightArrowWallpaper : this.rightArrow));
      } else {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   private void press(GuiButton button) {
      if (button.field_146124_l) {
         this.func_146284_a(button);
         button.func_146113_a(this.field_146297_k.func_147118_V());
      }

   }

   private void switchOrSound(GuiButton button) {
      if (this.storage.getBoxCount() > 1) {
         this.press(button);
      } else {
         this.field_146297_k.func_147118_V().func_147682_a(PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.nameField.func_146192_a(mouseX, mouseY, mouseButton);
      this.searchField.func_146192_a(mouseX, mouseY, mouseButton);
      super.func_73864_a(mouseX, mouseY, mouseButton);
   }

   public void func_146281_b() {
      if (this.searchField.func_146176_q()) {
         this.closeSearch();
      }

      if (this.nameField.func_146176_q()) {
         this.closeName(true);
      }

      Arrays.stream(ClientStorageManager.openPC.getBoxes()).forEach(PCBox::sendChangesToServer);
      Pixelmon.network.sendToServer(new ServerSetLastOpenBox(this.boxNumber));
      super.func_146281_b();
      Keyboard.enableRepeatEvents(false);
      GuiPixelmonOverlay.icons.forEach((icon) -> {
         icon.setEnabled(true);
      });
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      GlStateManager.func_179147_l();
      this.field_146297_k.func_110434_K().func_110577_a(this.boxWallpaper);
      this.func_73729_b(this.field_146294_l / 2 - 103, this.field_146295_m / 6 - 30, 0, 0, 206, 186);
      this.field_146292_n.forEach((b) -> {
         b.func_191745_a(this.field_146297_k, mouseX, mouseY, partialTicks);
      });

      for(int i = 0; i < this.icons.size(); ++i) {
         ((GuiKeybindIcon)this.icons.get(i)).draw(this.field_146294_l - 30 - i * 25, this.field_146295_m - 30, this.field_73735_i);
      }

      this.nameField.func_146194_f();
      this.searchField.func_146194_f();
      GlStateManager.func_179147_l();
      this.drawBox();
      this.drawFooter();
      this.drawPokeChecker(mouseX, mouseY, partialTicks);
      this.drawHover(mouseX, mouseY);
      this.drawCursor(mouseX, mouseY);
      GlStateManager.func_179084_k();
   }

   public void func_73876_c() {
      super.func_73876_c();
      this.nameField.func_146178_a();
      this.searchField.func_146178_a();
   }
}
