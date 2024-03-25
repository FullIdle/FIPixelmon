package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.quests.util.PosAlignment;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class SpawnPokemonAction implements IAction {
   public String identifier() {
      return "POKEMON_SPAWN";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("x", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("y", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("z", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("x_alignment", false, false, ArgumentType.TEXT, new String[]{"Relative", "Random", "Absolute"}), new QuestElementArgument("y_alignment", false, false, ArgumentType.TEXT, new String[]{"Relative", "Random", "Absolute"}), new QuestElementArgument("z_alignment", false, false, ArgumentType.TEXT, new String[]{"Relative", "Random", "Absolute"}), new QuestElementArgument("engage", false, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("spec", false, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Double::parseDouble, 0.0), Argument.from(args.get(1), Double::parseDouble, 0.0), Argument.from(args.get(2), Double::parseDouble, 0.0), Argument.from(args.get(3), PosAlignment::getForName, PosAlignment.Relative), Argument.from(args.get(4), PosAlignment::getForName, PosAlignment.Relative), Argument.from(args.get(5), PosAlignment::getForName, PosAlignment.Relative), Argument.from(args.get(6), Boolean::parseBoolean, false), Argument.from(args.get(7), (s) -> {
         return PokemonSpec.from(s.replace("_", " ").split(" "));
      }));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      EntityPlayerMP player = data.getPlayer();
      PosAlignment aX = (PosAlignment)arguments.value(3, progress);
      PosAlignment aY = (PosAlignment)arguments.value(4, progress);
      PosAlignment aZ = (PosAlignment)arguments.value(5, progress);
      Pos pos = new Pos((Double)arguments.value(0, progress), (Double)arguments.value(1, progress), (Double)arguments.value(2, progress), aX == PosAlignment.Relative, aY == PosAlignment.Relative, aZ == PosAlignment.Relative, aX == PosAlignment.Random, aY == PosAlignment.Random, aZ == PosAlignment.Random);
      PokemonSpec spec = (PokemonSpec)arguments.value(7, progress);
      EntityPixelmon pixelmon = spec.create(player.func_71121_q());
      BlockPos blockPos = pos.toBlockPos(player.func_180425_c());
      pixelmon.func_70634_a((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p());
      player.func_71121_q().func_72838_d(pixelmon);
      pixelmon.func_70634_a((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p());
      boolean engage = (Boolean)arguments.value(6, progress);
      if (engage) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         EntityPixelmon startingPixelmon = pps.getAndSendOutFirstAblePokemon(player);
         if (startingPixelmon != null) {
            PlayerParticipant playerParticipant = new PlayerParticipant(player, new EntityPixelmon[]{startingPixelmon});
            WildPixelmonParticipant wildPixelmonParticipant = new WildPixelmonParticipant(false, new EntityPixelmon[]{pixelmon});
            wildPixelmonParticipant.startedBattle = true;
            BattleRegistry.startBattle(playerParticipant, wildPixelmonParticipant);
         }
      }

   }

   private static class Pos {
      private final double x;
      private final double y;
      private final double z;
      private final boolean pX;
      private final boolean pY;
      private final boolean pZ;
      private final boolean rX;
      private final boolean rY;
      private final boolean rZ;

      public Pos(double x, double y, double z, boolean pX, boolean pY, boolean pZ, boolean rX, boolean rY, boolean rZ) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.pX = pX;
         this.pY = pY;
         this.pZ = pZ;
         this.rX = rX;
         this.rY = rY;
         this.rZ = rZ;
      }

      public BlockPos toBlockPos(BlockPos playerPos) {
         double x = (double)(!this.rX && !this.pX ? 0 : playerPos.func_177958_n()) + (this.rX ? (double)RandomHelper.rand.nextInt((int)(this.x * 2.0)) - this.x : this.x);
         double y = (double)(!this.rY && !this.pY ? 0 : playerPos.func_177956_o()) + (this.rY ? (double)RandomHelper.rand.nextInt((int)(this.y * 2.0)) - this.y : this.y);
         double z = (double)(!this.rZ && !this.pZ ? 0 : playerPos.func_177952_p()) + (this.rZ ? (double)RandomHelper.rand.nextInt((int)(this.z * 2.0)) - this.z : this.z);
         return new BlockPos(x, y, z);
      }
   }
}
