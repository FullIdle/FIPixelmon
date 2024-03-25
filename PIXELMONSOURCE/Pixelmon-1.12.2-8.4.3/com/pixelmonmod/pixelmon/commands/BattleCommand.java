package com.pixelmonmod.pixelmon.commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class BattleCommand extends PixelmonCommand {
   public BattleCommand() {
      super("pokebattle", "/pokebattle <participant1> <participant2> [<participant3> <participant4>]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 2 && args.length != 3 && args.length != 4) {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      } else {
         BattleRules br = new BattleRules();
         br.battleType = args.length == 2 ? EnumBattleType.Single : EnumBattleType.Double;
         Entity[] entities = new Entity[args.length];

         int i;
         for(i = 0; i < entities.length; ++i) {
            entities[i] = (Entity)require(this.getParticipant(sender, args[i]), "pixelmon.command.battle2.invalid", new Object[]{i + 1});
         }

         if (CollectionHelper.find(Lists.newArrayList(entities), (ex) -> {
            return ex instanceof NPCTrainer;
         }) != null) {
            NPCTrainer trainer = (NPCTrainer)CollectionHelper.find(Lists.newArrayList(entities), (ex) -> {
               return ex instanceof NPCTrainer;
            });
            if (!trainer.battleRules.isDefault()) {
               br.importText(trainer.battleRules.exportText());
            }
         }

         for(i = 0; i < entities.length; ++i) {
            Entity e = entities[i];

            int requiredPokemon;
            for(requiredPokemon = 0; requiredPokemon < entities.length; ++requiredPokemon) {
               if (i != requiredPokemon && e.field_70170_p != entities[requiredPokemon].field_70170_p) {
                  endCommand("pixelmon.command.battle.dimension", new Object[0]);
               }
            }

            requiredPokemon = 1;
            if (!(e instanceof EntityPlayerMP)) {
               if (e instanceof NPCTrainer) {
                  NPCTrainer trainer = (NPCTrainer)e;
                  if (trainer.battleController != null) {
                     endCommand("pixelmon.command.battle.cannotchallenge", new Object[]{trainer.func_70005_c_()});
                  }
               }
            } else {
               EntityPlayerMP player = (EntityPlayerMP)e;

               for(int j = 0; j < entities.length; ++j) {
                  if (i != j && e == entities[j]) {
                     if (br.battleType == EnumBattleType.Single) {
                        endCommand("pixelmon.command.battle2.duplicate", new Object[0]);
                     } else if (br.battleType == EnumBattleType.Double) {
                        ++requiredPokemon;
                        if (i < 2 && j >= 2 || j < 2 && i >= 2) {
                           endCommand("pixelmon.command.battle2.differing", new Object[0]);
                        }
                     }
                  }
               }

               if (BattleRegistry.getBattle((EntityPlayer)e) != null) {
                  endCommand("pixelmon.command.battle.cannotchallenge", new Object[]{player.func_70005_c_()});
               }

               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               require(party.countAblePokemon() >= requiredPokemon, requiredPokemon > 1 ? "pixelmon.command.battle.notenoughpokemon" : "pixelmon.command.battle.nopokemon", new Object[]{player.getDisplayNameString()});
            }
         }

         Entity source = sender instanceof EntityPlayer ? (EntityPlayer)sender : (Entity)Arrays.stream(entities).filter((entity) -> {
            return entity instanceof EntityPlayerMP || entity instanceof EntityNPC;
         }).findFirst().get();
         if (br.battleType == EnumBattleType.Single) {
            BattleParticipant one = this.prepareParticipant(entities[0], br.battleType.numPokemon, (Entity)source);
            BattleParticipant two = this.prepareParticipant(entities[1], br.battleType.numPokemon, (Entity)source);
            BattleRegistry.startBattle(new BattleParticipant[]{one}, new BattleParticipant[]{two}, br);
            func_152374_a(sender, this, 0, "pixelmon.command.battle.started", new Object[]{one.getDisplayName(), two.getDisplayName()});
         } else if (br.battleType == EnumBattleType.Double) {
            BattleParticipant[][] teams = new BattleParticipant[2][];
            Entity four;
            Entity one;
            Entity two;
            Entity three;
            if (entities.length == 2) {
               one = entities[0];
               two = entities[0];
               three = entities[1];
               four = entities[1];
            } else if (entities.length == 3) {
               one = entities[0];
               two = entities[0];
               three = entities[1];
               four = entities[2];
            } else {
               one = entities[0];
               two = entities[1];
               three = entities[2];
               four = entities[3];
            }

            teams[0] = one == two ? new BattleParticipant[]{this.prepareParticipant(one, 2, (Entity)source)} : new BattleParticipant[]{this.prepareParticipant(one, 1, (Entity)source), this.prepareParticipant(two, 1, (Entity)source)};
            teams[1] = three == four ? new BattleParticipant[]{this.prepareParticipant(three, 2, (Entity)source)} : new BattleParticipant[]{this.prepareParticipant(three, 1, (Entity)source), this.prepareParticipant(four, 1, (Entity)source)};
            BattleRegistry.startBattle(teams[0], teams[1], br);
            func_152374_a(sender, this, 0, "pixelmon.command.battle.started", new Object[]{Arrays.toString(Arrays.stream(teams[0]).map(BattleParticipant::getDisplayName).toArray()), Arrays.toString(Arrays.stream(teams[1]).map(BattleParticipant::getDisplayName).toArray())});
         }
      }

   }

   private BattleParticipant prepareParticipant(Entity e, int numPokemon, Entity source) {
      if (!(e instanceof EntityPlayerMP)) {
         if (e instanceof NPCTrainer) {
            NPCTrainer trainer = (NPCTrainer)e;
            return new TrainerParticipant(trainer, numPokemon);
         } else if (e instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)e;
            pixelmon.aggression = EnumAggression.passive;
            pixelmon.func_70107_b(source.field_70165_t + 2.0, (double)Battle2.getTopEarthBlock(source.field_70170_p, (int)source.field_70165_t, (int)source.field_70161_v), source.field_70161_v);
            pixelmon.field_70170_p.func_72838_d(pixelmon);
            return new WildPixelmonParticipant(new EntityPixelmon[]{pixelmon});
         } else {
            return null;
         }
      } else {
         EntityPlayerMP player = (EntityPlayerMP)e;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         List pokemon = party.findAll((pk) -> {
            return pk.getHealth() > 0 && !pk.isEgg();
         });
         EntityPixelmon[] pixelmons = new EntityPixelmon[numPokemon];

         for(int i = 0; i < pixelmons.length; ++i) {
            pixelmons[i] = ((Pokemon)pokemon.get(i)).getOrSpawnPixelmon(player);
         }

         return new PlayerParticipant(player, pixelmons);
      }
   }

   private Entity getParticipant(ICommandSender sender, String value) {
      EntityPlayerMP playerMP = getEntityPlayer(value);
      if (playerMP != null) {
         return playerMP;
      } else {
         playerMP = getPlayer(sender, value);
         if (playerMP != null) {
            return playerMP;
         } else {
            PokemonSpec spec = new PokemonSpec(value.split(","));
            if (spec.name != null) {
               BlockPos pos = sender.func_180425_c();
               EntityPixelmon pixelmon = spec.create(sender.func_130014_f_());
               pixelmon.func_70107_b((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5);
               return pixelmon;
            } else {
               String langCode = sender instanceof EntityPlayerMP ? ((EntityPlayerMP)sender).field_71148_cg : "en_US";
               Optional trainer = EntityNPC.locateNPCServer(sender.func_130014_f_(), value, NPCTrainer.class, langCode);
               return (Entity)trainer.orElse((Object)null);
            }
         }
      }
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length <= 4) {
         List list = tabCompleteUsernames(args);
         if (list.isEmpty()) {
            list = tabCompletePokemon(args);
         }

         return list;
      } else {
         return Collections.emptyList();
      }
   }
}
