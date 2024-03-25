package com.pixelmonmod.pixelmon.commands;

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
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Battle2 extends PixelmonCommand {
   public String func_71517_b() {
      return "pokebattle2";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/pokebattle2 <player> <player|pokemon> <player|pokemon> <player|pokemon>";
   }

   public int func_82362_a() {
      return 2;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 2) {
         this.sendMessage(sender, this.func_71518_a(sender), new Object[0]);
      } else {
         try {
            ArrayList participants = new ArrayList();
            ArrayList wildPixelmon = new ArrayList();
            ArrayList playerNames = new ArrayList();
            int numParticipants = 0;
            String[] var7 = args;
            int i = args.length;

            for(int var9 = 0; var9 < i; ++var9) {
               String arg = var7[var9];
               if (!arg.startsWith("lvl")) {
                  ++numParticipants;
               }
            }

            if (numParticipants < 2 || numParticipants > 4) {
               this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.cantstart", new Object[0]);
            }

            int i = 0;

            while(true) {
               EntityPixelmon wildPart;
               if (i >= args.length) {
                  EntityPlayerMP player = ((PlayerParticipant)participants.get(0)).player;

                  for(i = 0; i < wildPixelmon.size(); ++i) {
                     wildPart = (EntityPixelmon)wildPixelmon.get(i);
                     wildPart.aggression = EnumAggression.passive;
                     if (i == 0) {
                        wildPart.func_70107_b(player.field_70165_t + 2.0, (double)getTopEarthBlock(player.field_70170_p, (int)player.field_70165_t, (int)player.field_70161_v), player.field_70161_v);
                     } else if (i == 1) {
                        wildPart.func_70107_b(player.field_70165_t, (double)getTopEarthBlock(player.field_70170_p, (int)player.field_70165_t, (int)player.field_70161_v), player.field_70161_v + 2.0);
                     } else if (i == 2) {
                        wildPart.func_70107_b(player.field_70165_t + 2.0, (double)getTopEarthBlock(player.field_70170_p, (int)player.field_70165_t, (int)player.field_70161_v), player.field_70161_v + 2.0);
                     }

                     player.field_70170_p.func_72838_d(wildPart);
                  }

                  new ArrayList();
                  BattleParticipant[] team1;
                  BattleParticipant[] team2;
                  if (numParticipants == 2) {
                     team1 = new BattleParticipant[]{(BattleParticipant)participants.get(0)};
                     team2 = new BattleParticipant[]{(BattleParticipant)participants.get(1)};
                  } else if (numParticipants == 3) {
                     team1 = new BattleParticipant[]{(BattleParticipant)participants.get(0)};
                     team2 = new BattleParticipant[]{(BattleParticipant)participants.get(1), (BattleParticipant)participants.get(2)};
                     BattleParticipant[] var29 = team2;
                     int var31 = team2.length;

                     for(int var13 = 0; var13 < var31; ++var13) {
                        BattleParticipant participant = var29[var13];
                        if (participant instanceof TrainerParticipant) {
                           participant.setNumControlledPokemon(1);
                        }
                     }
                  } else {
                     if (numParticipants != 4) {
                        CommandChatHandler.sendChat(sender, "pixelmon.command.battle2.cantstart");
                        return;
                     }

                     team1 = new BattleParticipant[]{(BattleParticipant)participants.get(0), (BattleParticipant)participants.get(1)};
                     team2 = new BattleParticipant[]{(BattleParticipant)participants.get(2), (BattleParticipant)participants.get(3)};
                     Iterator var28 = participants.iterator();

                     while(var28.hasNext()) {
                        BattleParticipant participant = (BattleParticipant)var28.next();
                        if (participant instanceof TrainerParticipant) {
                           participant.setNumControlledPokemon(1);
                        }
                     }
                  }

                  ((PlayerParticipant)participants.get(0)).startedBattle = true;
                  BattleRegistry.startBattle(team1, team2, new BattleRules(EnumBattleType.Double));
                  break;
               }

               if (!args[i].startsWith("lvl")) {
                  Object part = this.getPart(getServer(), sender, args[i]);
                  if (part == null) {
                     this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.invalid", new Object[]{i + 1});
                     return;
                  }

                  if (part instanceof EntityPlayerMP) {
                     EntityPlayerMP playerPart = (EntityPlayerMP)part;
                     if (BattleRegistry.getBattle(playerPart) != null) {
                        this.sendMessage(sender, TextFormatting.RED, "sendpixelmon.inbattle", new Object[]{playerPart.func_145748_c_()});
                        return;
                     }

                     if (playerNames.contains(playerPart.getDisplayNameString())) {
                        this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.duplicate", new Object[0]);
                        return;
                     }

                     playerNames.add(playerPart.getDisplayNameString());
                     PlayerPartyStorage party = Pixelmon.storageManager.getParty(playerPart);
                     List availablePokemon = party.findAll((pokemon) -> {
                        return !pokemon.isEgg() && pokemon.getHealth() > 0;
                     });
                     EntityPixelmon[] leadPokemon;
                     if (numParticipants == 2 || numParticipants == 3 && i == 0) {
                        if (availablePokemon.isEmpty()) {
                           leadPokemon = new EntityPixelmon[]{null, null};
                        } else if (availablePokemon.size() == 1) {
                           leadPokemon = new EntityPixelmon[]{((Pokemon)availablePokemon.get(0)).getOrSpawnPixelmon(playerPart)};
                        } else {
                           leadPokemon = new EntityPixelmon[]{((Pokemon)availablePokemon.get(0)).getOrSpawnPixelmon(playerPart), ((Pokemon)availablePokemon.get(1)).getOrSpawnPixelmon(playerPart)};
                        }
                     } else {
                        leadPokemon = new EntityPixelmon[]{party.getAndSendOutFirstAblePokemon(playerPart)};
                     }

                     if (leadPokemon.length <= 0 || leadPokemon[0] == null) {
                        this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle.nopokemon", new Object[]{playerPart.func_145748_c_()});
                        return;
                     }

                     participants.add(new PlayerParticipant(playerPart, leadPokemon));
                  } else if (part instanceof EntityPixelmon) {
                     wildPart = (EntityPixelmon)part;
                     if (i == 0) {
                        this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.first", new Object[0]);
                        return;
                     }

                     if (i + 1 < args.length && args[i + 1].startsWith("lvl")) {
                        int level = Integer.parseInt(RegexPatterns.LETTERS.matcher(args[i + 1]).replaceAll(""));
                        if (level > 0 && level <= PixelmonServerConfig.maxLevel) {
                           wildPart.getLvl().setLevel(level);
                        } else {
                           this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.cheater", new Object[0]);
                        }
                     } else {
                        wildPart.getLvl().setLevel(((PlayerParticipant)participants.get(0)).getHighestLevel());
                     }

                     wildPixelmon.add(wildPart);
                     participants.add(new WildPixelmonParticipant(new EntityPixelmon[]{wildPart}));
                  } else if (part instanceof NPCTrainer) {
                     NPCTrainer trainer = (NPCTrainer)part;
                     if (trainer.battleController != null) {
                        this.sendMessage(sender, TextFormatting.RED, "sendpixelmon.inbattle", new Object[]{trainer.func_70005_c_()});
                        return;
                     }

                     participants.add(new TrainerParticipant(trainer, 2));
                  }
               }

               ++i;
            }
         } catch (NumberFormatException var15) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.level", new Object[0]);
         } catch (ClassCastException var16) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.battle2.cantstart", new Object[0]);
         } catch (Exception var17) {
            System.out.println("Error loading player for command /pokebattle2 " + args[0] + "  " + args[1]);
            var17.printStackTrace();
         }

      }
   }

   public static int getTopEarthBlock(World world, int cpX, int cpZ) {
      Chunk chunk = world.func_175726_f(new BlockPos(cpX, 0, cpZ));
      Integer k = null;

      for(k = Math.max(0, Math.min(chunk.func_76625_h() + 15, 255)); k > 0; k = k - 1) {
         Block block = world.func_180495_p(new BlockPos(cpX, k, cpZ)).func_177230_c();
         if (block != Blocks.field_150350_a && !block.isFoliage(world, new BlockPos(cpX, k, cpZ))) {
            return k + 1;
         }
      }

      return chunk.func_76625_h() + 15;
   }

   private Object getPart(MinecraftServer server, ICommandSender sender, String value) {
      EntityPlayerMP player = getPlayer(sender, value);
      if (player != null) {
         return player;
      } else {
         PokemonSpec spec = new PokemonSpec(value.split(","));
         if (spec.name == null) {
            String langCode;
            if (sender instanceof EntityPlayerMP) {
               langCode = ((EntityPlayerMP)sender).field_71148_cg;
            } else {
               langCode = "en_US";
            }

            Optional trainer = EntityNPC.locateNPCServer(sender.func_130014_f_(), value, NPCTrainer.class, langCode);
            return trainer.isPresent() ? trainer.get() : null;
         } else {
            return spec.create(sender.func_130014_f_());
         }
      }
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length >= 1 && args.length <= 4 ? tabCompleteUsernames(args) : Collections.emptyList();
   }
}
