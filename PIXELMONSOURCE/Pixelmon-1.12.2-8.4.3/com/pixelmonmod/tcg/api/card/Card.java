package com.pixelmonmod.tcg.api.card;

import com.google.common.collect.Lists;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Card {
   private String code;
   private int grade;
   private List tags = Lists.newArrayList();
   private transient ImmutableCard base = null;

   public Card(ByteBuf buffer) {
      this.code = ByteBufUtils.readUTF8String(buffer);
      this.grade = buffer.readInt();
      int size = buffer.readInt();

      for(int i = 0; i < size; ++i) {
         this.tags.add(ByteBufUtils.readUTF8String(buffer));
      }

   }

   public Card(String code, int grade, String... tags) {
      this.code = code;
      this.grade = grade;
      this.tags.addAll(Lists.newArrayList(tags));
   }

   public ImmutableCard getBase() {
      if (this.base == null) {
         this.base = CardRegistry.fromCode(this.code);
      }

      return this.base;
   }

   public boolean is(String... tags) {
      String[] var2 = tags;
      int var3 = tags.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String tag = var2[var4];
         if (this.tags.contains(tag)) {
            return true;
         }
      }

      return false;
   }

   public int getGrade() {
      return this.grade;
   }

   public void load(ItemStack itemStack) {
      this.load(itemStack.func_77978_p());
   }

   public void load(NBTTagCompound tag) {
      this.code = tag.func_74779_i("CardCode");
      this.grade = tag.func_74762_e("CardGrade");
      Iterator var2 = tag.func_150295_c("CardTags", 8).iterator();

      while(var2.hasNext()) {
         NBTBase nbtBase = (NBTBase)var2.next();
         this.tags.add(((NBTTagString)nbtBase).func_150285_a_());
      }

   }

   public void write(ItemStack itemStack) {
      NBTTagCompound tag = itemStack.func_77978_p();
      if (tag == null) {
         tag = new NBTTagCompound();
      }

      this.write(tag);
      itemStack.func_77982_d(tag);
   }

   public void write(NBTTagCompound tag) {
      tag.func_74778_a("CardCode", this.code);
      tag.func_74768_a("CardGrade", this.grade);
      NBTTagList tags = new NBTTagList();
      Iterator var3 = this.tags.iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         tags.func_74742_a(new NBTTagString(s));
      }

      tag.func_74782_a("CardTags", tags);
   }

   public void write(ByteBuf buf) {
      ByteBufUtil.writeUtf8(buf, this.code);
      buf.writeInt(this.grade);
      buf.writeInt(this.tags.size());
      Iterator var2 = this.tags.iterator();

      while(var2.hasNext()) {
         String tag = (String)var2.next();
         ByteBufUtil.writeUtf8(buf, tag);
      }

   }
}
