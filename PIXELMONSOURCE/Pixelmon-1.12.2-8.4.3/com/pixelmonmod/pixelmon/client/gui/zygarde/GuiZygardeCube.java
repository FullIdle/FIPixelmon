package com.pixelmonmod.pixelmon.client.gui.zygarde;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiRoundButton;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.zygarde.ZygardeCubePacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PowerConstruct;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class GuiZygardeCube extends GuiScreen {
   EnumHand hand;
   StoragePosition position;
   Pokemon zygarde;
   ItemStack cube;
   Mode mode;
   List buttons;
   List lines;
   private int centerW;
   private int centerH;

   public GuiZygardeCube(EnumHand hand, int partyPos) {
      this.mode = GuiZygardeCube.Mode.SELECT;
      this.buttons = Lists.newArrayList();
      this.lines = Lists.newArrayList();
      this.field_146297_k = Minecraft.func_71410_x();
      this.hand = hand;
      this.position = new StoragePosition(-1, partyPos);
      Pokemon pokemon = ClientStorageManager.party.get(this.position);
      if (pokemon.getSpecies() == EnumSpecies.Zygarde) {
         this.zygarde = pokemon;
      }

      ItemStack stack = this.field_146297_k.field_71439_g.func_184586_b(hand);
      if (stack.func_77973_b() instanceof ItemZygardeCube) {
         this.cube = stack;
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.buttons.clear();
      this.lines.clear();
      if (this.cube != null && this.zygarde != null) {
         if (this.mode == GuiZygardeCube.Mode.SELECT) {
            if (!(this.zygarde.getAbility() instanceof PowerConstruct) && ItemZygardeCube.getCoreTypes(this.cube).size() == 0) {
               this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_cube.nomoves", new Object[0]));
               this.buttons.add((new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20)).setId(-1));
            } else if (this.zygarde.getAbility() instanceof PowerConstruct) {
               this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_cube.select", new Object[0]));
               if (this.zygarde.getAbility() instanceof PowerConstruct) {
                  this.buttons.add((new GuiRoundButton(0, 0, I18n.func_135052_a("pixelmon.gui.zygarde_cube.toggleforme", new Object[0]), 150, 20)).setId(0));
               }

               if (ItemZygardeCube.getCoreTypes(this.cube).size() > 0) {
                  this.buttons.add((new GuiRoundButton(0, 0, I18n.func_135052_a("pixelmon.gui.zygarde_cube.teachmove", new Object[0]), 150, 20)).setId(1));
               }
            } else {
               this.mode = GuiZygardeCube.Mode.LEARN_MOVE;
            }
         }

         if (this.mode == GuiZygardeCube.Mode.LEARN_MOVE) {
            this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_cube.learnmove", new Object[0]));
            Iterator var1 = ItemZygardeCube.getCoreTypes(this.cube).iterator();

            while(var1.hasNext()) {
               ItemZygardeCube.CoreType type = (ItemZygardeCube.CoreType)var1.next();
               AttackBase base = (AttackBase)AttackBase.getAttackBaseFromEnglishName(type.getMoveName()).get();
               this.buttons.add((new GuiRoundButton(0, 0, base.getLocalizedName(), 150, 20)).setId(type.ordinal()));
            }
         }

      } else {
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (!this.lines.isEmpty()) {
         GuiHelper.drawDialogueBox(this, (String)"", (List)this.lines, 0.0F);
      }

      for(int i = 0; i < this.buttons.size(); ++i) {
         GuiRoundButton button = (GuiRoundButton)this.buttons.get(i);
         button.drawButton(this.centerW - 70, this.field_146295_m / 4 + 20 + 30 * (i + 1), mouseX, mouseY, 0.0F);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      for(int i = 0; i < this.buttons.size(); ++i) {
         GuiRoundButton button = (GuiRoundButton)this.buttons.get(i);
         if (button.isMouseOver(this.centerW - 70, this.field_146295_m / 4 + 20 + 30 * (i + 1), mouseX, mouseY)) {
            if (this.mode == GuiZygardeCube.Mode.SELECT) {
               if (button.getId() == 0) {
                  Pixelmon.network.sendToServer(new ZygardeCubePacket(this.position, this.field_146297_k.field_71439_g.field_71071_by.func_184429_b(this.cube)));
                  this.field_146297_k.func_147108_a((GuiScreen)null);
               } else if (button.getId() == 1) {
                  this.mode = GuiZygardeCube.Mode.LEARN_MOVE;
                  this.func_73866_w_();
               } else if (button.getId() == -1) {
                  this.field_146297_k.func_147108_a((GuiScreen)null);
               }
            } else if (this.mode == GuiZygardeCube.Mode.LEARN_MOVE) {
               Pixelmon.network.sendToServer(new ZygardeCubePacket(this.position, this.field_146297_k.field_71439_g.field_71071_by.func_184429_b(this.cube), button.getId()));
               this.field_146297_k.func_147108_a((GuiScreen)null);
            }
         }
      }

   }

   static enum Mode {
      SELECT,
      LEARN_MOVE,
      TOGGLE_FORM;
   }
}
