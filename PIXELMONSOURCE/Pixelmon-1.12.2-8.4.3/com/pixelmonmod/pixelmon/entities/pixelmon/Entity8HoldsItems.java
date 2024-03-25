package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class Entity8HoldsItems extends Entity6Moves {
   boolean dropped = false;

   public Entity8HoldsItems(World par1World) {
      super(par1World);
   }

   public void func_70645_a(DamageSource cause) {
      EntityPixelmon pix;
      if (cause.func_76364_f() instanceof EntityPixelmon && this.getBossMode() != EnumBossMode.NotBoss && !this.field_70170_p.field_72995_K) {
         pix = (EntityPixelmon)cause.func_76364_f();
         if (pix.func_70902_q() != null && !this.dropped) {
            this.dropBossItems((EntityPlayerMP)pix.func_70902_q());
            this.dropped = true;
         } else if (pix.battleController != null && !this.dropped) {
            pix.battleController.participants.stream().filter((p) -> {
               return p instanceof PlayerParticipant;
            }).forEach((p) -> {
               this.dropBossItems(((PlayerParticipant)p).player);
               this.dropped = true;
            });
         }
      }

      if (cause.func_76364_f() instanceof EntityPixelmon && this.getBossMode() == EnumBossMode.NotBoss && !this.field_70170_p.field_72995_K) {
         pix = (EntityPixelmon)cause.func_76364_f();
         if (pix.func_70902_q() != null && !this.dropped) {
            this.dropNormalItems((EntityPlayerMP)pix.func_70902_q());
            this.dropped = true;
         } else if (pix.battleController != null && !this.dropped) {
            pix.battleController.participants.stream().filter((p) -> {
               return p instanceof PlayerParticipant && p.team != this.getPixelmonWrapper().getParticipant().team;
            }).forEach((p) -> {
               this.dropNormalItems(((PlayerParticipant)p).player);
               this.dropped = true;
            });
         }
      }

      super.func_70645_a(cause);
   }

   public void dropNormalItems(EntityPlayerMP player) {
      if (PixelmonConfig.pokemonDropsEnabled && !this.isBossPokemon() && !this.hasOwner() && this.getTrainer() == null) {
         ArrayList items = DropItemRegistry.getDropsForPokemon(this);
         int id = 0;
         ArrayList givenDrops = new ArrayList();
         Iterator var5 = items.iterator();

         while(var5.hasNext()) {
            ItemStack item = (ItemStack)var5.next();
            if (item != null) {
               givenDrops.add(new DroppedItem(item, id++));
            }
         }

         if (givenDrops.size() > 0) {
            DropItemQueryList.register(this, givenDrops, player);
         }

      }
   }

   public void dropBossItems(EntityPlayerMP player) {
      ArrayList items = DropItemRegistry.getDropsForPokemon(this);
      int id = 0;
      ArrayList givenDrops = new ArrayList();
      Iterator var5 = items.iterator();

      while(var5.hasNext()) {
         ItemStack item = (ItemStack)var5.next();
         if (item != null) {
            givenDrops.add(new DroppedItem(item, id++));
         }
      }

      ArrayList bossDrops = DropItemRegistry.getBossDrops(this, player);
      Iterator var9 = bossDrops.iterator();

      while(var9.hasNext()) {
         ItemStack item = (ItemStack)var9.next();
         givenDrops.add(new DroppedItem(item, id++));
      }

      if (givenDrops.size() > 0) {
         DropItemQueryList.register(this, givenDrops, player);
      }

   }
}
