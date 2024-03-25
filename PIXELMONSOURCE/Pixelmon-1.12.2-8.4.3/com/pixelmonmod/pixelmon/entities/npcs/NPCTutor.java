package com.pixelmonmod.pixelmon.entities.npcs;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.LoadTutorData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SelectPokemonResponse;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLearnType;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCTutorType;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class NPCTutor extends EntityNPC {
   static final DataParameter dwTutorType;
   public static ArrayList allTutorMoves;
   public static ArrayList allTransferMoves;
   public final List moveList = Lists.newArrayList();
   public EnumSet learnTypes = EnumSet.noneOf(BaseStatsLearnType.class);

   public NPCTutor(World world) {
      super(world);
      this.init("Tutor");
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(dwTutorType, (byte)EnumNPCTutorType.TUTOR.ordinal());
   }

   public void init(String name) {
      if (name.equals("Tutor")) {
         BaseTrainer trainer = ServerNPCRegistry.trainers.getRandomBase();
         name = trainer.name;
         this.setBaseTrainer(trainer);
         if (trainer.textures.size() > 1) {
            this.field_70180_af.func_187227_b(dwTextureIndex, this.field_70170_p.field_73012_v.nextInt(trainer.textures.size()));
         }
      }

      super.init(name);
      this.npcLocation = SpawnLocationType.LandNPC;
      if (this.moveList.isEmpty()) {
         this.randomiseMoveList();
      }

      if (this.learnTypes.isEmpty()) {
         if (this.getTutorType() == EnumNPCTutorType.TUTOR) {
            this.learnTypes.addAll(Arrays.asList(BaseStatsLearnType.GEN8_DEFAULT));
            this.learnTypes.remove(BaseStatsLearnType.TRANSFER_MOVES);
         } else {
            this.learnTypes.add(BaseStatsLearnType.TRANSFER_MOVES);
         }
      }

      if (this.getAIMode() != EnumTrainerAI.StandStill) {
         this.setAIMode(EnumTrainerAI.Wander);
      }

      this.initAI();
   }

   public String getDisplayText() {
      return this.getTutorType() == EnumNPCTutorType.TRANSFER ? I18n.func_74838_a("pixelmon.npc.transfername") : I18n.func_74838_a("pixelmon.npc.tutorname");
   }

   public void func_70024_g(double par1, double par3, double par5) {
      if (this.func_70104_M()) {
         super.func_70024_g(par1, par3, par5);
      }

   }

   public boolean func_70104_M() {
      return this.getAIMode() != EnumTrainerAI.StandStill;
   }

   protected boolean func_70692_ba() {
      return this.getAIMode() == EnumTrainerAI.Wander;
   }

   public void setTutorType(EnumNPCTutorType type) {
      this.field_70180_af.func_187227_b(dwTutorType, (byte)type.ordinal());
      this.learnTypes.clear();
      if (this.getTutorType() == EnumNPCTutorType.TUTOR) {
         this.learnTypes.addAll(Arrays.asList(BaseStatsLearnType.GEN8_DEFAULT));
         this.learnTypes.remove(BaseStatsLearnType.TRANSFER_MOVES);
      } else {
         this.learnTypes.add(BaseStatsLearnType.TRANSFER_MOVES);
      }

      this.randomiseMoveList();
   }

   public EnumNPCTutorType getTutorType() {
      return EnumNPCTutorType.fromOrdinal((Byte)this.field_70180_af.func_187225_a(dwTutorType));
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74768_a("TutorType", this.getTutorType().ordinal());
      if (this.moveList.isEmpty()) {
         nbt.func_74768_a("numAttacks", 0);
      } else {
         nbt.func_74768_a("numAttacks", this.moveList.size());

         for(int i = 0; i < this.moveList.size(); ++i) {
            LearnableMove learnableMove = (LearnableMove)this.moveList.get(i);
            nbt.func_74778_a("attack" + i, learnableMove.attack().getAttackName());
            nbt.func_74768_a("attack" + i + "costNum", learnableMove.costs().size());
            List costs = learnableMove.costs();

            for(int j = 0; j < costs.size(); ++j) {
               ItemStack current = (ItemStack)costs.get(j);
               nbt.func_74768_a("attack" + i + "cost" + j, Item.func_150891_b(current.func_77973_b()));
               nbt.func_74768_a("attack" + i + "cost" + j + "Num", current.func_190916_E());
               nbt.func_74768_a("attack" + i + "cost" + j + "Damage", current.func_77952_i());
            }
         }

         if (!this.isDefaultLearnList()) {
            NBTTagList list = new NBTTagList();
            Iterator var8 = this.learnTypes.iterator();

            while(var8.hasNext()) {
               BaseStatsLearnType type = (BaseStatsLearnType)var8.next();
               list.func_74742_a(new NBTTagString(type.name()));
            }

            nbt.func_74782_a("learnTypes", list);
         }

      }
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (nbt.func_74764_b("TutorType")) {
         this.setTutorType(EnumNPCTutorType.values()[nbt.func_74762_e("TutorType")]);
      } else {
         this.setTutorType(EnumNPCTutorType.random());
      }

      int numAttacks = nbt.func_74762_e("numAttacks");
      if (numAttacks > 0) {
         this.moveList.clear();
      }

      for(int i = 0; i < numAttacks; ++i) {
         Optional optional = AttackBase.getAttackBase(nbt.func_74779_i("attack" + i));
         if (optional.isPresent()) {
            int numCosts = nbt.func_74762_e("attack" + i + "costNum");
            List cost = new ArrayList(numCosts);

            for(int j = 0; j < numCosts; ++j) {
               ItemStack current = new ItemStack(Item.func_150899_d(nbt.func_74762_e("attack" + i + "cost" + j)), nbt.func_74762_e("attack" + i + "cost" + j + "Num"));
               current.func_77964_b(nbt.func_74762_e("attack" + i + "cost" + j + "Damage"));
               cost.add(current);
            }

            this.moveList.add(new LearnableMove((AttackBase)optional.get(), cost, true));
         }
      }

      if (nbt.func_150297_b("learnTypes", 9)) {
         this.learnTypes.clear();
         Iterator var10 = nbt.func_150295_c("learnTypes", 8).iterator();

         while(var10.hasNext()) {
            NBTBase base = (NBTBase)var10.next();
            NBTTagString string = (NBTTagString)base;

            try {
               BaseStatsLearnType type = BaseStatsLearnType.valueOf(string.func_150285_a_());
               this.learnTypes.add(type);
            } catch (Exception var9) {
            }
         }
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

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND) {
         if (this.moveList.isEmpty()) {
            this.init("");
         }

         ItemStack itemStack = player.func_184586_b(hand);
         Pixelmon.network.sendTo(new LoadTutorData(this.moveList), (EntityPlayerMP)player);
         if (player.field_71075_bZ.field_75098_d && itemStack.func_77973_b() instanceof ItemNPCEditor) {
            this.setAIMode(EnumTrainerAI.StandStill);
            this.initAI();
            Pixelmon.network.sendTo(new LoadTutorData(this.moveList, this.learnTypes), (EntityPlayerMP)player);
            OpenScreen.open(player, EnumGuiScreen.TutorEditor, this.getId());
         } else if (Pixelmon.storageManager.getParty((EntityPlayerMP)player).countAblePokemon() > 0) {
            OpenScreen.open(player, EnumGuiScreen.PickPokemon, this.getTutorType() == EnumNPCTutorType.TUTOR ? SelectPokemonResponse.Mode.Tutor.ordinal() : SelectPokemonResponse.Mode.Transfer_Tutor.ordinal(), this.getId());
         } else {
            ChatHandler.sendChat(player, "pixelmon.npc.tutor.nopokemon");
         }
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return super.func_184645_a(player, hand);
   }

   public void handlePickedPokemon(EntityPlayerMP player, Pokemon pokemon) {
      if (!pokemon.isEgg()) {
         List learnableMoveList = Lists.newArrayList();
         Iterator var4 = this.moveList.iterator();

         while(var4.hasNext()) {
            LearnableMove learnableMove = (LearnableMove)var4.next();
            learnableMoveList.add(new LearnableMove(learnableMove, this.canLearn(player, pokemon, learnableMove.attack())));
         }

         Pixelmon.network.sendTo(new LoadTutorData(learnableMoveList), player);
         OpenScreen.open(player, EnumGuiScreen.Tutor, pokemon.getPosition().order, this.getId());
      }
   }

   public boolean canLearn(EntityPlayerMP player, Pokemon pokemon, AttackBase attack) {
      return pokemon.getBaseStats().canLearn(attack, (BaseStatsLearnType[])this.learnTypes.toArray(new BaseStatsLearnType[0]));
   }

   public void randomiseMoveList() {
      this.moveList.clear();
      Set moveSet = Sets.newHashSet();
      List attackPool = this.getTutorType() == EnumNPCTutorType.TUTOR ? allTutorMoves : allTransferMoves;

      AttackBase base;
      for(int i = 0; i < Math.min(PixelmonConfig.movesPerTutor, attackPool.size()); ++i) {
         do {
            base = (AttackBase)CollectionHelper.getRandomElement((List)attackPool);
         } while(!moveSet.add(base));
      }

      Iterator var5 = moveSet.iterator();

      while(var5.hasNext()) {
         base = (AttackBase)var5.next();
         this.moveList.add(new LearnableMove(base, Collections.singletonList(getRandomCost()), true));
      }

   }

   private static ItemStack getRandomCost() {
      Item[] randomItems = new Item[]{Items.field_151042_j, Items.field_151043_k, Items.field_151045_i, Items.field_151166_bC, PixelmonItems.aluminiumIngot, PixelmonItems.amethyst, PixelmonItems.ruby, PixelmonItems.crystal, PixelmonItems.sapphire, PixelmonItems.siliconItem};
      int randomItem = RandomHelper.getRandomNumberBetween(0, randomItems.length - 1);
      return new ItemStack(randomItems[randomItem], RandomHelper.getRandomNumberBetween(1, 16), 0);
   }

   private boolean isDefaultLearnList() {
      EnumSet defaults = EnumSet.noneOf(BaseStatsLearnType.class);
      if (this.getTutorType() == EnumNPCTutorType.TUTOR) {
         defaults.addAll(Arrays.asList(BaseStatsLearnType.GEN8_DEFAULT));
         defaults.remove(BaseStatsLearnType.TRANSFER_MOVES);
      } else {
         defaults.add(BaseStatsLearnType.TRANSFER_MOVES);
      }

      return this.learnTypes.containsAll(defaults);
   }

   static {
      dwTutorType = EntityDataManager.func_187226_a(NPCTutor.class, DataSerializers.field_187191_a);
      allTutorMoves = new ArrayList();
      allTransferMoves = new ArrayList();
   }

   public static class LearnableMove implements IMessage {
      private AttackBase attack;
      private List costs;
      private boolean learnable;

      public LearnableMove() {
      }

      public LearnableMove(AttackBase attack, List costs, boolean learnable) {
         this.attack = attack;
         this.costs = Lists.newArrayList();
         Iterator var4 = costs.iterator();

         while(var4.hasNext()) {
            ItemStack cost = (ItemStack)var4.next();
            this.costs.add(cost.func_77946_l());
         }

         this.learnable = learnable;
      }

      public LearnableMove(LearnableMove source, boolean learnable) {
         this.attack = source.attack;
         this.costs = Lists.newArrayList();
         Iterator var3 = source.costs.iterator();

         while(var3.hasNext()) {
            ItemStack cost = (ItemStack)var3.next();
            this.costs.add(cost.func_77946_l());
         }

         this.learnable = learnable;
      }

      public AttackBase attack() {
         return this.attack;
      }

      public List costs() {
         return this.costs;
      }

      public boolean learnable() {
         return this.learnable;
      }

      public void fromBytes(ByteBuf buf) {
         this.attack = (AttackBase)AttackBase.getAttackBase(ByteBufUtils.readUTF8String(buf)).orElse((Object)null);
         int count = buf.readInt();
         this.costs = Lists.newArrayList();

         for(int i = 0; i < count; ++i) {
            this.costs.add(ByteBufUtils.readItemStack(buf));
         }

         this.learnable = buf.readBoolean();
      }

      public void toBytes(ByteBuf buf) {
         ByteBufUtils.writeUTF8String(buf, this.attack.getAttackName());
         buf.writeInt(this.costs.size());
         Iterator var2 = this.costs.iterator();

         while(var2.hasNext()) {
            ItemStack stack = (ItemStack)var2.next();
            ByteBufUtils.writeItemStack(buf, stack);
         }

         buf.writeBoolean(this.learnable);
      }

      public void readFromNBT(NBTTagCompound compound) {
         this.attack = (AttackBase)AttackBase.getAttackBase(compound.func_74779_i("move")).orElse((Object)null);
         this.costs = Lists.newArrayList();
         NBTTagList list = compound.func_150295_c("costs", 10);
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            NBTBase base = (NBTBase)var3.next();
            NBTTagCompound cost = (NBTTagCompound)base;
            this.costs.add(new ItemStack(cost));
         }

         this.learnable = compound.func_74767_n("learnable");
      }

      public NBTTagCompound writeToNBT(NBTTagCompound compound) {
         compound.func_74778_a("move", this.attack == null ? "" : this.attack.getAttackName());
         NBTTagList list = new NBTTagList();
         Iterator var3 = this.costs.iterator();

         while(var3.hasNext()) {
            ItemStack cost = (ItemStack)var3.next();
            list.func_74742_a(cost.func_77955_b(new NBTTagCompound()));
         }

         compound.func_74782_a("costs", list);
         compound.func_74757_a("learnable", this.learnable);
         return compound;
      }
   }
}
