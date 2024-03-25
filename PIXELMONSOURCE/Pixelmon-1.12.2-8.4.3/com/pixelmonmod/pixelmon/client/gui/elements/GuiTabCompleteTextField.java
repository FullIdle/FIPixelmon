package com.pixelmonmod.pixelmon.client.gui.elements;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTabCompleteTextField extends GuiTextField {
   private Collection completions = Collections.emptyList();
   protected List cache = Lists.newArrayList();
   protected boolean isCompleting = false;
   protected int index = 0;

   public GuiTabCompleteTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
      super(componentId, fontrendererObj, x, y, par5Width, par6Height);
   }

   public GuiTabCompleteTextField setCompletions(Collection completions) {
      this.completions = completions;
      return this;
   }

   public boolean func_146201_a(char typedChar, int keyCode) {
      if (!this.func_146206_l()) {
         return super.func_146201_a(typedChar, keyCode);
      } else if (keyCode == 15) {
         if (this.isCompleting) {
            this.func_146175_b(0);
            this.func_146175_b(this.func_146197_a(-1, this.func_146198_h(), false) - this.func_146198_h());
            if (this.index >= this.cache.size()) {
               this.index = 0;
            }
         } else {
            int i = this.func_146197_a(-1, this.func_146198_h(), false);
            String s = this.func_146179_b().substring(0, this.func_146198_h());
            this.index = 0;
            this.createCompletionsFor(s);
            if (this.cache.isEmpty()) {
               return true;
            }

            this.isCompleting = true;
            this.func_146175_b(i - this.func_146198_h());
         }

         this.func_146180_a((String)this.cache.get(this.index++));
         return true;
      } else {
         this.isCompleting = false;
         return super.func_146201_a(typedChar, keyCode);
      }
   }

   protected void createCompletionsFor(String leftOfCursor) {
      this.cache.clear();
      if (leftOfCursor.isEmpty()) {
         this.cache.addAll(this.completions);
      } else {
         Iterator var2 = this.completions.iterator();

         while(var2.hasNext()) {
            String completion = (String)var2.next();
            if (completion.regionMatches(true, 0, leftOfCursor, 0, leftOfCursor.length())) {
               this.cache.add(completion);
            }
         }

      }
   }
}
