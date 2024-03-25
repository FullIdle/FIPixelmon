package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.AnvilEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonOres;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemPokeballDisc;
import com.pixelmonmod.pixelmon.items.ItemPokeballLid;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;

public class TileEntityAnvil extends TileEntity {
   @Nonnull
   public ItemStack stack;
   public int state;
   int count;

   public TileEntityAnvil() {
      this.stack = ItemStack.field_190927_a;
      this.state = 0;
      this.count = 0;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (!this.stack.func_190926_b()) {
         NBTTagCompound compound = this.stack.func_77955_b(new NBTTagCompound());
         nbt.func_74782_a("ItemStackOnAnvil", compound);
      }

      nbt.func_74768_a("AnvilItemState", this.state);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("ItemOnAnvil")) {
         this.stack = new ItemStack(Item.func_150899_d(nbt.func_74762_e("ItemOnAnvil")));
      } else if (nbt.func_74764_b("ItemStackOnAnvil")) {
         NBTTagCompound compound = nbt.func_74775_l("ItemStackOnAnvil");
         this.stack = new ItemStack(compound);
      } else {
         this.stack = ItemStack.field_190927_a;
      }

      if (nbt.func_74764_b("AnvilItemState")) {
         this.state = nbt.func_74762_e("AnvilItemState");
      }

   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public boolean blockHit(int f, EntityPlayerMP player) {
      int neededHits = 0;
      if (PixelmonOres.itemMatches(this.stack, "ingotAluminum")) {
         neededHits = 15;
      } else if (this.stack.func_77973_b() == PixelmonItems.aluminiumPlate || this.stack.func_77973_b() == PixelmonItemsPokeballs.aluBase || this.stack.func_77973_b() == PixelmonItemsPokeballs.ironBase || this.stack.func_77973_b() instanceof ItemPokeballLid || this.stack.func_77973_b() instanceof ItemPokeballDisc || this.stack.func_77973_b() == PixelmonItemsPokeballs.ironDisc || this.stack.func_77973_b() == PixelmonItemsPokeballs.aluDisc) {
         neededHits = 5;
      }

      AnvilEvent.BeatAnvil beatAnvilEvent = new AnvilEvent.BeatAnvil(player, this, this.stack, neededHits, f);
      if (Pixelmon.EVENT_BUS.post(beatAnvilEvent)) {
         return false;
      } else {
         f = beatAnvilEvent.getForce();
         boolean returnVal = false;
         AnvilEvent.FinishedSmith finishedSmithEvent;
         if (PixelmonOres.itemMatches(this.stack, "ingotAluminum")) {
            this.count += f;
            if (this.count >= beatAnvilEvent.getNeededHits()) {
               this.count = 0;
               ++this.state;
               returnVal = true;
               this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187689_f, SoundCategory.BLOCKS, 0.25F, 1.0F);
               if (this.state == 3) {
                  this.state = 0;
                  this.stack = new ItemStack(PixelmonItems.aluminiumPlate);
                  finishedSmithEvent = new AnvilEvent.FinishedSmith(player, this, this.stack);
                  Pixelmon.EVENT_BUS.post(finishedSmithEvent);
               }

               ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
            }
         } else if (this.stack.func_77973_b() != PixelmonItems.aluminiumPlate && this.stack.func_77973_b() != PixelmonItemsPokeballs.aluBase && this.stack.func_77973_b() != PixelmonItemsPokeballs.ironBase && !(this.stack.func_77973_b() instanceof ItemPokeballLid)) {
            if (this.stack.func_77973_b() instanceof ItemPokeballDisc || this.stack.func_77973_b() == PixelmonItemsPokeballs.ironDisc || this.stack.func_77973_b() == PixelmonItemsPokeballs.aluDisc) {
               this.count += f;
               if (this.count >= beatAnvilEvent.getNeededHits()) {
                  this.count = 0;
                  ++this.state;
                  returnVal = true;
                  this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187689_f, SoundCategory.BLOCKS, 0.15F, 1.0F);
                  if (this.state == 3) {
                     this.count = 0;
                     this.state = 0;
                     if (this.stack.func_77973_b() == PixelmonItemsPokeballs.ironDisc) {
                        this.stack = new ItemStack(PixelmonItemsPokeballs.ironBase);
                     } else if (this.stack.func_77973_b() == PixelmonItemsPokeballs.aluDisc) {
                        this.stack = new ItemStack(PixelmonItemsPokeballs.aluBase);
                     } else {
                        this.stack = new ItemStack(PixelmonItemsPokeballs.getLidFromEnum(((ItemPokeballDisc)this.stack.func_77973_b()).pokeball));
                     }

                     this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187883_gR, SoundCategory.BLOCKS, 0.1F, 3.0F);
                     finishedSmithEvent = new AnvilEvent.FinishedSmith(player, this, this.stack);
                     Pixelmon.EVENT_BUS.post(finishedSmithEvent);
                  }

                  ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               }
            }
         } else {
            this.count += f;
            if (this.count >= beatAnvilEvent.getNeededHits()) {
               AnvilEvent.MaterialChanged materialChangedEvent = new AnvilEvent.MaterialChanged(player, this, this.stack, true);
               if (!Pixelmon.EVENT_BUS.post(materialChangedEvent)) {
                  this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187883_gR, SoundCategory.BLOCKS, 0.1F, 3.0F);
                  DropItemHelper.giveItemStack(player, materialChangedEvent.material, false);
                  if (PixelmonConfig.allowAnvilAutoloading) {
                     Item itemReplacement = null;
                     if (this.stack.func_77973_b() == PixelmonItems.aluminiumPlate) {
                        itemReplacement = PixelmonItems.aluminiumIngot;
                     } else if (this.stack.func_77973_b() == PixelmonItemsPokeballs.aluBase) {
                        itemReplacement = PixelmonItemsPokeballs.aluDisc;
                     } else if (this.stack.func_77973_b() == PixelmonItemsPokeballs.ironBase) {
                        itemReplacement = PixelmonItemsPokeballs.ironDisc;
                     } else if (this.stack.func_77973_b() instanceof ItemPokeballLid) {
                        itemReplacement = PixelmonItemsPokeballs.getDiscFromEnum(((ItemPokeballLid)this.stack.func_77973_b()).pokeball);
                     }

                     if (itemReplacement != null && player.field_71071_by.func_70431_c(new ItemStack((Item)itemReplacement))) {
                        boolean itemFound = false;

                        for(int i = 0; i < player.field_71071_by.field_70462_a.size(); ++i) {
                           ItemStack stack = (ItemStack)player.field_71071_by.field_70462_a.get(i);
                           if (!stack.func_190926_b() && stack.func_77973_b() == itemReplacement) {
                              materialChangedEvent = new AnvilEvent.MaterialChanged(player, this, stack, false);
                              if (!Pixelmon.EVENT_BUS.post(materialChangedEvent)) {
                                 player.field_71071_by.func_174925_a((Item)itemReplacement, stack.func_77960_j(), 1, (NBTTagCompound)null);
                                 this.stack = new ItemStack((Item)itemReplacement);
                                 this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187689_f, SoundCategory.BLOCKS, 0.15F, 0.9F);
                                 this.count = 0;
                                 this.state = 0;
                                 itemFound = true;
                              }
                              break;
                           }
                        }

                        if (!itemFound && !player.func_184592_cb().func_190926_b() && player.func_184592_cb().func_77973_b() == itemReplacement) {
                           materialChangedEvent = new AnvilEvent.MaterialChanged(player, this, player.func_184592_cb(), false);
                           if (!Pixelmon.EVENT_BUS.post(materialChangedEvent)) {
                              player.field_71071_by.func_174925_a((Item)itemReplacement, player.func_184592_cb().func_77960_j(), 1, (NBTTagCompound)null);
                              this.stack = new ItemStack((Item)itemReplacement);
                              this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187689_f, SoundCategory.BLOCKS, 0.15F, 0.9F);
                              this.count = 0;
                              this.state = 0;
                              itemFound = true;
                           }
                        }

                        if (!itemFound) {
                           this.stack = ItemStack.field_190927_a;
                        }
                     } else {
                        this.stack = ItemStack.field_190927_a;
                        this.state = 0;
                        this.count = 0;
                     }
                  } else {
                     this.stack = ItemStack.field_190927_a;
                     this.state = 0;
                     this.count = 0;
                  }

                  ((WorldServer)player.field_70170_p).func_184164_w().func_180244_a(this.field_174879_c);
               }
            }
         }

         return returnVal;
      }
   }
}
