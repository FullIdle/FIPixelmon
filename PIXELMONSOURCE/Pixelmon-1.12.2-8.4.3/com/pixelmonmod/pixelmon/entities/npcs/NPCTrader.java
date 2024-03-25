package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.trading.NPCTrades;
import com.pixelmonmod.pixelmon.api.trading.PossibleTradeList;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import javax.annotation.Nullable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class NPCTrader extends EntityNPC {
   public TradePair tradePair = null;

   public NPCTrader(World par1World) {
      super(par1World);
      this.setName(I18n.func_74838_a("pixelmon.npc.tradername"));
      this.init("Youngster");
   }

   public void func_110159_bB() {
      if (this.func_110167_bD()) {
         this.func_110160_i(true, true);
      }

   }

   public void init(String name) {
      super.init(name);
      if ((Integer)this.field_70180_af.func_187225_a(dwModel) == -1) {
         BaseTrainer trainer = ServerNPCRegistry.trainers.getRandomBase();
         this.setBaseTrainer(trainer);
         if (trainer.textures.size() > 1) {
            this.field_70180_af.func_187227_b(dwTextureIndex, this.field_70170_p.field_73012_v.nextInt(trainer.textures.size()));
         }
      }

      if (!this.hasTrade()) {
         this.setNewTrade();
      }

      if (((String)this.field_70180_af.func_187225_a(EntityNPC.dwNickname)).equalsIgnoreCase("")) {
         this.field_70180_af.func_187227_b(EntityNPC.dwNickname, ServerNPCRegistry.getRandomName());
      }

   }

   public void setNewTrade() {
      this.tradePair = PossibleTradeList.getRandomTrade();

      try {
         if (Entity3HasStats.hasForms(this.tradePair.offer.name)) {
            this.tradePair.offer.form = (byte)RandomHelper.getRandomNumberBetween(0, Entity3HasStats.getNumForms(this.tradePair.offer.name) - 1);
         }

         if (Entity3HasStats.hasForms(this.tradePair.exchange.name)) {
            this.tradePair.exchange.form = (byte)RandomHelper.getRandomNumberBetween(0, Entity3HasStats.getNumForms(this.tradePair.exchange.name) - 1);
         }
      } catch (Exception var2) {
         if (this.tradePair.offer != null) {
            Pixelmon.LOGGER.error("Problem in a trade with offer: " + this.tradePair.offer.name);
         }

         if (this.tradePair.exchange != null) {
            Pixelmon.LOGGER.error("Problem in a trade with exchange: " + this.tradePair.exchange.name);
         }

         var2.printStackTrace();
      }

   }

   public void unloadEntity() {
      this.func_70623_bb();
      this.func_70106_y();
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      if (player instanceof EntityPlayerMP) {
         this.updateTradePair();
         ItemStack stack = player.func_184586_b(hand);
         int id = this.getId();
         if (stack != null && stack.func_77973_b() == PixelmonItems.trainerEditor) {
            this.setAIMode(EnumTrainerAI.StandStill);
            this.initAI();
            NPCTrades.updateClientTradeData((EntityPlayerMP)player, this.tradePair);
            OpenScreen.open(player, EnumGuiScreen.NPCTrade, id);
         } else {
            NPCTrades.showTrade((EntityPlayerMP)player, this.tradePair, id);
         }
      }

      return true;
   }

   public boolean hasTrade() {
      return this.tradePair != null;
   }

   public void updateTradePair() {
      if (!this.hasTrade()) {
         this.setNewTrade();
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      this.updateTradePair();
      nbt.func_74782_a("Offer", this.tradePair.offer.writeToNBT(new NBTTagCompound()));
      nbt.func_74782_a("Exchange", this.tradePair.exchange.writeToNBT(new NBTTagCompound()));
      if (this.tradePair.description != null) {
         nbt.func_74778_a("TradeDescription", this.tradePair.description);
      }

      nbt.func_74777_a("ModelIndex", (short)(Integer)this.field_70180_af.func_187225_a(dwModel));
      if (this.getBaseTrainer().textures.size() > 1) {
         nbt.func_74768_a("TextureIndex", (Integer)this.field_70180_af.func_187225_a(dwTextureIndex));
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (nbt.func_74764_b("ModelIndex")) {
         this.field_70180_af.func_187227_b(dwModel, Integer.valueOf(nbt.func_74765_d("ModelIndex")));
         if (this.getBaseTrainer().textures.size() > 1) {
            this.field_70180_af.func_187227_b(dwTextureIndex, nbt.func_74762_e("TextureIndex"));
         }
      }

      this.tradePair = new TradePair(new PokemonSpec("random"), new PokemonSpec("random"));
      PokemonSpec offeredSpec;
      if (nbt.func_74764_b("Exchange")) {
         offeredSpec = new PokemonSpec(new String[0]);
         if (nbt.func_74781_a("Exchange") instanceof NBTTagCompound) {
            offeredSpec.readFromNBT(nbt.func_74775_l("Exchange"));
         } else {
            offeredSpec = new PokemonSpec(new String[]{nbt.func_74779_i("Exchange"), String.valueOf(nbt.func_74762_e("Lvl"))});
         }

         this.tradePair.exchange = offeredSpec;
      } else {
         this.tradePair.exchange = new PokemonSpec("random");
      }

      if (nbt.func_74764_b("Offer")) {
         offeredSpec = new PokemonSpec(new String[0]);
         if (nbt.func_74781_a("Offer") instanceof NBTTagCompound) {
            offeredSpec.readFromNBT(nbt.func_74775_l("Offer"));
         } else {
            offeredSpec = new PokemonSpec(new String[]{nbt.func_74779_i("Offer"), String.valueOf(nbt.func_74762_e("Lvl")), String.valueOf(nbt.func_74762_e("Variant"))});
         }

         if (nbt.func_74767_n("Shiny")) {
            offeredSpec.shiny = true;
         }

         this.tradePair.offer = offeredSpec;
      } else {
         this.tradePair.offer = new PokemonSpec("random");
      }

      if (nbt.func_74764_b("TradeDescription")) {
         this.tradePair.description = nbt.func_74779_i("TradeDescription");
      }

   }

   public void updateTrade(PokemonSpec offer, PokemonSpec exchange, @Nullable String description) {
      this.tradePair = new TradePair(offer, exchange, description);
   }

   public String getDisplayText() {
      return I18n.func_74838_a("pixelmon.npc.tradername");
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
}
