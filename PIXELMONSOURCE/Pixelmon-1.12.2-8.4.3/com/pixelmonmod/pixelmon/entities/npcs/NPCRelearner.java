package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SelectPokemonResponse;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class NPCRelearner extends EntityNPC {
   private static final DataParameter COST;

   public NPCRelearner(World world) {
      super(world);
      this.init(I18n.func_74838_a("pixelmon.npc.relearnername"));
      this.field_70180_af.func_187214_a(COST, ItemStack.field_190927_a);
   }

   public String getTexture() {
      return "pixelmon:textures/steve/npcchat1.png";
   }

   public String getDisplayText() {
      return I18n.func_74838_a("pixelmon.npc.relearnername");
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   public ItemStack getCost() {
      ItemStack cost = (ItemStack)this.field_70180_af.func_187225_a(COST);
      return !cost.func_190926_b() && !(cost.func_77973_b() instanceof ItemNPCEditor) && cost.func_77973_b() != Items.field_190931_a ? cost : ItemStack.field_190927_a;
   }

   public void setCost(ItemStack item) {
      this.field_70180_af.func_187227_b(COST, item);
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      if (player instanceof EntityPlayerMP) {
         if (Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.Relearner, player))) {
            return false;
         }

         ItemStack cost = this.getCost();
         ItemStack itemstack = player.func_184586_b(hand);
         if (player.field_71075_bZ.field_75098_d) {
            if (!itemstack.func_190926_b()) {
               if (itemstack.func_77973_b() instanceof ItemNPCEditor) {
                  this.openRelearnerGui(player);
               } else if (!cost.func_190926_b() && cost.func_77973_b() == itemstack.func_77973_b() && cost.func_77952_i() == itemstack.func_77952_i()) {
                  if (cost.func_190916_E() != cost.func_77976_d()) {
                     cost.func_190917_f(1);
                     this.setCost(cost);
                     ChatHandler.sendChat(player, "pixelmon.npc.relearnersetitem", cost.func_82833_r(), cost.func_190916_E());
                  } else {
                     ChatHandler.sendChat(player, "pixelmon.npc.relearnerfull");
                  }
               } else {
                  if (player.func_70093_af()) {
                     cost = new ItemStack(itemstack.func_77973_b(), 1, itemstack.func_77952_i());
                  } else {
                     cost = itemstack.func_77946_l();
                  }

                  this.setCost(cost);
                  ChatHandler.sendChat(player, "pixelmon.npc.relearnersetitem", cost.func_82833_r(), cost.func_190916_E());
               }
            } else if (!cost.func_190926_b()) {
               cost.func_190918_g(1);
               if (cost.func_190916_E() <= 0) {
                  cost = ItemStack.field_190927_a;
                  ChatHandler.sendChat(player, "pixelmon.npc.relearnernoitem");
                  this.setCost(cost);
               } else {
                  this.setCost(cost);
                  ChatHandler.sendChat(player, "pixelmon.npc.relearnersetitem", cost.func_82833_r(), cost.func_190916_E());
               }
            }
         } else if (this.checkCost(itemstack)) {
            this.openRelearnerGui(player);
         } else {
            ChatHandler.sendChat(player, "pixelmon.npc.relearnercost", cost.func_82833_r(), cost.func_190916_E());
         }
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return true;
   }

   public boolean checkCost(ItemStack hand) {
      ItemStack cost = this.getCost();
      if (cost.func_190926_b()) {
         return true;
      } else {
         return ItemStack.func_179545_c(cost, hand) && hand.func_190916_E() >= cost.func_190916_E() && (!cost.func_77942_o() || cost.func_77978_p().equals(hand.func_77978_p()));
      }
   }

   public void openRelearnerGui(EntityPlayer player) {
      if (Pixelmon.storageManager.getParty((EntityPlayerMP)player).countAblePokemon() > 0) {
         OpenScreen.open(player, EnumGuiScreen.PickPokemon, SelectPokemonResponse.Mode.Relearner.ordinal(), this.getId());
      } else {
         ChatHandler.sendChat(player, "pixelmon.npc.tutor.nopokemon");
      }

   }

   public void handlePickedPokemon(EntityPlayerMP player, Pokemon pokemon) {
      if (!pokemon.isEgg()) {
         OpenScreen.open(player, EnumGuiScreen.Relearner, pokemon.getPosition().order, this.getId());
      }
   }

   public static List getRelearnableMoves(Pokemon pokemon) {
      List attackList = new ArrayList();
      Iterator var2 = pokemon.getRelearnableMoves().iterator();

      int i;
      while(var2.hasNext()) {
         i = (Integer)var2.next();
         attackList.add(new Attack(i));
      }

      ArrayList levelUpList = pokemon.getBaseStats().getMovesUpToLevel(pokemon.getLevel());

      Attack attack;
      for(i = levelUpList.size() - 1; i >= 0; --i) {
         attack = (Attack)levelUpList.get(i);
         if (!attackList.contains(attack)) {
            attackList.add(attack);
         }
      }

      Iterator var6 = pokemon.getMoveset().iterator();

      while(var6.hasNext()) {
         attack = (Attack)var6.next();
         if (attack != null) {
            attackList.remove(attack);
         }
      }

      return attackList;
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      ItemStack cost = this.getCost();
      if (!cost.func_190926_b()) {
         NBTTagCompound stack = new NBTTagCompound();
         cost.func_77955_b(stack);
         nbt.func_74782_a("RelearnerCostStack", stack);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (nbt.func_74764_b("RelearnerCostStack")) {
         this.setCost(new ItemStack(nbt.func_74775_l("RelearnerCostStack")));
      } else if (nbt.func_74764_b("Cost")) {
         ItemStack cost = new ItemStack(Item.func_150899_d(nbt.func_74762_e("Cost")), nbt.func_74762_e("costNum"));
         cost.func_77964_b(nbt.func_74762_e("CostDamage"));
         this.setCost(cost);
      }

   }

   public void initAI() {
      this.field_70714_bg.field_75782_a.clear();
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      switch (this.getAIMode()) {
         case StandStill:
            this.field_70714_bg.func_75776_a(1, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
            this.field_70714_bg.func_75776_a(2, new EntityAIWatchClosest(this, EntityPixelmon.class, 6.0F));
            break;
         case Wander:
            this.field_70714_bg.func_75776_a(1, new EntityAIWander(this, SharedMonsterAttributes.field_111263_d.func_111110_b()));
      }

   }

   public boolean func_70104_M() {
      return this.getAIMode() != EnumTrainerAI.StandStill;
   }

   protected boolean func_70692_ba() {
      return this.getAIMode() == EnumTrainerAI.Wander;
   }

   static {
      COST = EntityDataManager.func_187226_a(NPCRelearner.class, DataSerializers.field_187196_f);
   }
}
