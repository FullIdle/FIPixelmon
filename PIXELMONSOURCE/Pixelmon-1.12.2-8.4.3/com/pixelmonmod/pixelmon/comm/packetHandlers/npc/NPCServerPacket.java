package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ClearTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.ChangePokemonOpenGUI;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ClientNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleAIMode;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NPCServerPacket implements IMessage {
   EnumNPCServerPacketType mode;
   int npcID;
   EnumTrainerAI ai;
   EnumBattleAIMode battleAI;
   EnumBossMode bm;
   EnumEncounterMode em;
   BattleRules battleRules;
   int modelIndex;
   String data;
   EnumSpecies pokemon;
   int pos;
   private int integer;
   ArrayList pages;
   PokemonSpec exchange;
   PokemonSpec offer;
   String description;
   ClientNPCData npcData;
   String shopkeeperType;
   private int engageDistance;
   EnumMegaItemsUnlocked mi;
   EnumOldGenMode og;

   public NPCServerPacket() {
   }

   public NPCServerPacket(int npcID) {
      this.npcID = npcID;
   }

   public NPCServerPacket(int npcID, EnumTrainerAI ai) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.AI;
      this.ai = ai;
   }

   public NPCServerPacket(int npcID, EnumBattleAIMode battleAI) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.BattleAI;
      this.battleAI = battleAI;
   }

   public NPCServerPacket(int npcID, EnumBossMode bm) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.BossMode;
      this.bm = bm;
   }

   public NPCServerPacket(int npcID, EnumEncounterMode bm) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.EncounterMode;
      this.em = bm;
   }

   public NPCServerPacket(int npcID, EnumMegaItemsUnlocked mi) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.MegaItem;
      this.mi = mi;
   }

   public NPCServerPacket(int npcID, EnumOldGenMode og) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.OldGen;
      this.og = og;
   }

   public NPCServerPacket(int npcID, BattleRules battleRules) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.BattleRules;
      this.battleRules = battleRules;
   }

   public NPCServerPacket(int npcID, int modelIndex) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.Model;
      this.modelIndex = modelIndex;
   }

   public NPCServerPacket(int npcID, EnumNPCServerPacketType mode, String data) {
      this(npcID);
      this.mode = mode;
      this.data = data;
   }

   public NPCServerPacket(int npcID, EnumSpecies pokemon, int pos) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.Pokemon;
      this.pokemon = pokemon;
      this.pos = pos;
   }

   public NPCServerPacket(int npcID, EnumNPCServerPacketType mode, int textureIndex) {
      this(npcID);
      this.mode = mode;
      this.integer = textureIndex;
   }

   public NPCServerPacket(int npcID, EnumNPCServerPacketType mode) {
      this(npcID);
      this.mode = mode;
   }

   public NPCServerPacket(int npcID, ClientNPCData npcData) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.CycleTexture;
      this.npcData = npcData;
   }

   public NPCServerPacket(int npcID, ArrayList pages) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.ChatPages;
      this.pages = pages;
   }

   public NPCServerPacket(int npcID, PokemonSpec offer, PokemonSpec exchange, String description) {
      this(npcID);
      this.mode = EnumNPCServerPacketType.Trader;
      this.offer = offer;
      this.exchange = exchange;
      this.description = description;
   }

   public NPCServerPacket(int npcID, int engageDistance, EnumNPCServerPacketType type) {
      this(npcID);
      this.mode = type;
      this.engageDistance = engageDistance;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.mode.ordinal());
      buffer.writeInt(this.npcID);
      switch (this.mode) {
         case AI:
            buffer.writeInt(this.ai.ordinal());
            break;
         case BattleAI:
            buffer.writeInt(this.battleAI.ordinal());
            break;
         case BossMode:
            buffer.writeInt(this.bm.ordinal());
            break;
         case EncounterMode:
            buffer.writeInt(this.em.ordinal());
            break;
         case OldGen:
            buffer.writeInt(this.og.ordinal());
            break;
         case MegaItem:
            buffer.writeInt(this.mi.ordinal());
            break;
         case BattleRules:
            this.battleRules.encodeInto(buffer);
            break;
         case Model:
            buffer.writeInt(this.modelIndex);
            break;
         case CustomSteveTexture:
         case Name:
         case CycleJson:
            ByteBufUtils.writeUTF8String(buffer, this.data);
            break;
         case Pokemon:
            buffer.writeInt(this.pokemon.getNationalPokedexInteger());
            buffer.writeInt(this.pos);
            break;
         case TextureIndex:
         case CycleName:
            buffer.writeInt(this.integer);
            break;
         case ChatPages:
            buffer.writeInt(this.pages.size());
            Iterator var2 = this.pages.iterator();

            while(var2.hasNext()) {
               String page = (String)var2.next();
               ByteBufUtils.writeUTF8String(buffer, page);
            }

            return;
         case Trader:
            ByteBufUtils.writeTag(buffer, this.offer.writeToNBT(new NBTTagCompound()));
            ByteBufUtils.writeTag(buffer, this.exchange.writeToNBT(new NBTTagCompound()));
            if (this.description == null) {
               this.description = "null";
            }

            ByteBufUtils.writeUTF8String(buffer, this.description);
            break;
         case CycleTexture:
            if (this.npcData == null) {
               this.npcData = new ClientNPCData("");
            }

            this.npcData.encodeInto(buffer);
            break;
         case EngageDistance:
            buffer.writeInt(this.engageDistance);
         case Relearner:
         case RefreshItems:
         case Tutor:
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.mode = EnumNPCServerPacketType.values()[buffer.readShort()];
      this.npcID = buffer.readInt();
      switch (this.mode) {
         case AI:
            this.ai = EnumTrainerAI.getFromOrdinal(buffer.readInt());
            break;
         case BattleAI:
            this.battleAI = EnumBattleAIMode.getFromIndex(buffer.readInt());
            break;
         case BossMode:
            this.bm = EnumBossMode.getMode(buffer.readInt());
            break;
         case EncounterMode:
            this.em = EnumEncounterMode.getFromIndex(buffer.readInt());
            break;
         case OldGen:
            this.og = EnumOldGenMode.getFromIndex(buffer.readInt());
            break;
         case MegaItem:
            this.mi = EnumMegaItemsUnlocked.values()[buffer.readInt()];
            break;
         case BattleRules:
            this.battleRules = new BattleRules(buffer);
            break;
         case Model:
            this.modelIndex = buffer.readInt();
            break;
         case CustomSteveTexture:
         case Name:
         case CycleJson:
            this.data = ByteBufUtils.readUTF8String(buffer);
            break;
         case Pokemon:
            this.pokemon = EnumSpecies.getFromDex(buffer.readInt());
            this.pos = buffer.readInt();
            break;
         case TextureIndex:
         case CycleName:
            this.integer = buffer.readInt();
            break;
         case ChatPages:
            int numPages = buffer.readInt();
            this.pages = new ArrayList();

            for(int i = 0; i < numPages; ++i) {
               this.pages.add(ByteBufUtils.readUTF8String(buffer));
            }

            return;
         case Trader:
            this.offer = (new PokemonSpec(new String[0])).readFromNBT(ByteBufUtils.readTag(buffer));
            this.exchange = (new PokemonSpec(new String[0])).readFromNBT(ByteBufUtils.readTag(buffer));
            this.description = ByteBufUtils.readUTF8String(buffer);
            if (this.description.equals("null")) {
               this.description = null;
            }
            break;
         case CycleTexture:
            this.npcData = new ClientNPCData(buffer);
            break;
         case EngageDistance:
            this.engageDistance = buffer.readInt();
         case Relearner:
         case RefreshItems:
         case Tutor:
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(NPCServerPacket message, MessageContext ctx) {
         EntityPlayerMP p = ctx.getServerHandler().field_147369_b;
         if (ItemNPCEditor.checkPermission(p)) {
            NPCTrader trader = null;
            Optional npcOptional = EntityNPC.locateNPCServer(p.field_70170_p, message.npcID, EntityNPC.class);
            if (npcOptional.isPresent()) {
               EntityNPC npc = (EntityNPC)npcOptional.get();
               NPCTrainer trainer = null;
               if (npc instanceof NPCTrainer) {
                  trainer = (NPCTrainer)npc;
               } else if (npc instanceof NPCTrader) {
                  trader = (NPCTrader)npc;
               }

               switch (message.mode) {
                  case AI:
                     trainer.setAIMode(message.ai);
                     trainer.initAI();
                     break;
                  case BattleAI:
                     trainer.setBattleAIMode(message.battleAI);
                     break;
                  case BossMode:
                     trainer.setBossMode(message.bm);
                     break;
                  case EncounterMode:
                     trainer.setEncounterMode(message.em);
                     break;
                  case OldGen:
                     trainer.setOldGenMode(message.og);
                     break;
                  case MegaItem:
                     trainer.setMegaItem(message.mi);
                     break;
                  case BattleRules:
                     message.battleRules.validateRules();
                     trainer.battleRules = message.battleRules;
                     break;
                  case Model:
                     if (npc instanceof NPCTrainer) {
                        ((NPCTrainer)npc).setTrainerType(ServerNPCRegistry.trainers.getById(message.modelIndex), p);
                     } else {
                        npc.setBaseTrainer(ServerNPCRegistry.trainers.getById(message.modelIndex));
                     }
                     break;
                  case CustomSteveTexture:
                     npc.setCustomSteveTexture(message.data);
                     break;
                  case Name:
                     if (message.data != null) {
                        npc.setName(message.data);
                     }

                     if (trainer != null) {
                        trainer.initAI();
                     }
                     break;
                  case CycleJson:
                     if (npc instanceof NPCShopkeeper) {
                        ((NPCShopkeeper)npc).cycleJson(p, message.data);
                     }
                     break;
                  case Pokemon:
                     Pokemon pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.getFromNameAnyCase(message.pokemon.name));
                     TrainerPartyStorage storage = trainer.getPokemonStorage();
                     storage.set(message.pos, (Pokemon)null);
                     storage.add(pokemon);
                     trainer.updateLvl();
                     List storageList = storage.getTeam();
                     Pixelmon.network.sendTo(new ClearTrainerPokemon(), p);

                     for(int i = 0; i < storageList.size(); ++i) {
                        Pixelmon.network.sendTo(new StoreTrainerPokemon((Pokemon)storageList.get(i)), p);
                     }

                     Pixelmon.network.sendTo(new ChangePokemonOpenGUI(message.pos), ctx.getServerHandler().field_147369_b);
                     break;
                  case TextureIndex:
                     npc.setTextureIndex(message.integer);
                     break;
                  case CycleName:
                     if (npc instanceof NPCShopkeeper) {
                        ((NPCShopkeeper)npc).cycleName(p, message.integer);
                     }
                     break;
                  case ChatPages:
                     if (npc instanceof NPCChatting) {
                        ((NPCChatting)npc).setChat(message.pages);
                     }
                     break;
                  case Trader:
                     if (trader != null) {
                        trader.updateTrade(message.offer, message.exchange, message.description);
                     }
                     break;
                  case CycleTexture:
                     if (npc instanceof NPCChatting) {
                        ((NPCChatting)npc).cycleTexture(p, message.npcData);
                     }
                     break;
                  case EngageDistance:
                     if (npc instanceof NPCTrainer) {
                        ((NPCTrainer)npc).setEngageDistance(message.engageDistance);
                     }

                     npc.initAI();
                  case Relearner:
                  default:
                     break;
                  case RefreshItems:
                     if (npc instanceof NPCShopkeeper) {
                        ((NPCShopkeeper)npc).loadItems();
                     }
               }

            }
         }
      }
   }
}
