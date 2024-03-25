package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ClearTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.UpdateClientRules;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCFisherman;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryTrainers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumNPCTutorType;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CreateNPCPacket implements IMessage {
   EnumNPCType type;
   BlockPos pos;
   float rotation;

   public CreateNPCPacket() {
   }

   public CreateNPCPacket(EnumNPCType type, BlockPos pos, float rotation) {
      this.type = type;
      this.pos = pos;
      this.rotation = rotation;
   }

   public void fromBytes(ByteBuf buf) {
      this.type = EnumNPCType.getFromOrdinal((short)buf.readByte());
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.rotation = buf.readFloat();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.type.ordinal());
      buf.writeInt(this.pos.func_177958_n());
      buf.writeInt(this.pos.func_177956_o());
      buf.writeInt(this.pos.func_177952_p());
      buf.writeFloat(this.rotation);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(CreateNPCPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (ItemNPCEditor.checkPermission(player)) {
            BlockPos blockPos = message.pos;
            float rotationYaw = message.rotation;
            switch (message.type) {
               case Trainer:
                  NPCTrainer trainer = new NPCTrainer(player.field_70170_p);
                  trainer.init(NPCRegistryTrainers.Steve);
                  trainer.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  trainer.setAIMode(EnumTrainerAI.StandStill);
                  trainer.initAI();
                  player.field_70170_p.func_72838_d(trainer);
                  trainer.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  trainer.setStartRotationYaw(rotationYaw + 180.0F);
                  trainer.func_110163_bv();
                  Pixelmon.network.sendTo(new ClearTrainerPokemon(), player);

                  for(int i = 0; i < trainer.getPokemonStorage().countAll(); ++i) {
                     Pixelmon.network.sendTo(new StoreTrainerPokemon(trainer.getPokemonStorage().get(i)), player);
                  }

                  SetTrainerData trainerData = new SetTrainerData(trainer, player.field_71148_cg);
                  Pixelmon.network.sendTo(new SetNPCEditData(trainerData), player);
                  if (BattleClauseRegistry.getClauseVersion() > 0) {
                     Pixelmon.network.sendTo(new UpdateClientRules(), player);
                  }

                  OpenScreen.open(player, EnumGuiScreen.TrainerEditor, trainer.getId());
                  break;
               case Trader:
                  NPCTrader trader = new NPCTrader(player.field_70170_p);
                  trader.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  player.field_70170_p.func_72838_d(trader);
                  trader.func_110163_bv();
                  break;
               case ChattingNPC:
                  NPCChatting npc = new NPCChatting(player.field_70170_p);
                  GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
                  npc.init(data);
                  npc.initDefaultAI();
                  npc.setCustomSteveTexture(data.getRandomTexture());
                  npc.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  npc.func_110163_bv();
                  player.field_70170_p.func_72838_d(npc);
                  break;
               case QuestGiver:
                  NPCQuestGiver questGiver = new NPCQuestGiver(player.field_70170_p);
                  GeneralNPCData data2 = ServerNPCRegistry.villagers.getRandom();
                  questGiver.init(data2);
                  questGiver.initDefaultAI();
                  questGiver.setCustomSteveTexture(data2.getRandomTexture());
                  questGiver.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  questGiver.func_110163_bv();
                  player.field_70170_p.func_72838_d(questGiver);
                  break;
               case Relearner:
                  NPCRelearner relearner = new NPCRelearner(player.field_70170_p);
                  relearner.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  relearner.setAIMode(EnumTrainerAI.StandStill);
                  relearner.initAI();
                  relearner.func_110163_bv();
                  player.field_70170_p.func_72838_d(relearner);
                  break;
               case Tutor:
                  NPCTutor tutor = new NPCTutor(player.field_70170_p);
                  tutor.init("Tutor");
                  tutor.setTutorType(EnumNPCTutorType.TUTOR);
                  tutor.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  tutor.setAIMode(EnumTrainerAI.StandStill);
                  tutor.initAI();
                  tutor.func_110163_bv();
                  player.field_70170_p.func_72838_d(tutor);
                  break;
               case TransferTutor:
                  NPCTutor transfer = new NPCTutor(player.field_70170_p);
                  transfer.init("Tutor");
                  transfer.setTutorType(EnumNPCTutorType.TRANSFER);
                  transfer.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  transfer.setAIMode(EnumTrainerAI.StandStill);
                  transfer.initAI();
                  transfer.func_110163_bv();
                  player.field_70170_p.func_72838_d(transfer);
                  break;
               case Shopkeeper:
                  ShopkeeperData shopData = ServerNPCRegistry.shopkeepers.getRandom();
                  if (shopData == null) {
                     return;
                  }

                  NPCShopkeeper shopkeeper = new NPCShopkeeper(player.field_70170_p);
                  shopkeeper.init(shopData);
                  shopkeeper.initDefaultAI();
                  shopkeeper.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  shopkeeper.func_110163_bv();
                  player.field_70170_p.func_72838_d(shopkeeper);
                  break;
               case NurseJoy:
                  NPCNurseJoy nursejoy = new NPCNurseJoy(player.field_70170_p, 1);
                  nursejoy.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  nursejoy.initDefaultAI();
                  nursejoy.func_110163_bv();
                  player.field_70170_p.func_72838_d(nursejoy);
                  break;
               case Doctor:
                  NPCNurseJoy doctor = new NPCNurseJoy(player.field_70170_p, 0);
                  doctor.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  doctor.initDefaultAI();
                  doctor.func_110163_bv();
                  player.field_70170_p.func_72838_d(doctor);
                  break;
               case OldFisherman:
                  NPCFisherman fisherman = new NPCFisherman(player.field_70170_p);
                  fisherman.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), (double)(blockPos.func_177956_o() + 1), (double)((float)blockPos.func_177952_p() + 0.5F));
                  fisherman.initDefaultAI();
                  fisherman.func_110163_bv();
                  player.field_70170_p.func_72838_d(fisherman);
            }

         }
      }
   }
}
