package com.pixelmonmod.tcg.tileentity;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityBattleRule extends TileEntityHasOwner {
   private static final String DECK_SIZE_KEY = "DeckSize";
   private static final String PRIZE_COUNT_KEY = "PrizeCount";
   private static final String STARTING_COMMAND_KEY = "StartingCommand";
   private static final String ENDING_COMMAND_KEY = "EndingCommand";
   private static final String STARTING_MESSAGE_KEY = "StartingMessage";
   private static final String ENDING_MESSAGE_KEY = "EndingMessage";
   private static final String CUSTOM_NAME_KEY = "CustomName";
   private static final String TIME_LIMIT_KEY = "TimeLimit";
   private static final String ELO_MINIMUM_KEY = "EloMinimum";
   private static final String SHADOW_GAME_KEY = "ShadowGame";
   private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
   protected int deckSize = 60;
   protected int prizeCount = 6;
   protected int eloMinimum = 0;
   protected String startingCommand = "";
   protected String endingCommand = "";
   protected String startingMessage = "";
   protected String endingMessage = "";
   private String customName = "@";
   protected int timeLimit = 0;
   protected boolean isShadowGame = false;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_74764_b("DeckSize")) {
         this.deckSize = nbt.func_74762_e("DeckSize");
      }

      if (nbt.func_74764_b("PrizeCount")) {
         this.prizeCount = nbt.func_74762_e("PrizeCount");
      }

      if (nbt.func_74764_b("TimeLimit")) {
         this.timeLimit = nbt.func_74762_e("TimeLimit");
      }

      if (nbt.func_74764_b("EloMinimum")) {
         this.eloMinimum = nbt.func_74762_e("EloMinimum");
      }

      if (nbt.func_74764_b("StartingCommand")) {
         this.startingCommand = nbt.func_74779_i("StartingCommand");
      }

      if (nbt.func_74764_b("EndingCommand")) {
         this.endingCommand = nbt.func_74779_i("EndingCommand");
      }

      if (nbt.func_74764_b("StartingMessage")) {
         this.startingMessage = nbt.func_74779_i("StartingMessage");
      }

      if (nbt.func_74764_b("EndingMessage")) {
         this.endingMessage = nbt.func_74779_i("EndingMessage");
      }

      if (nbt.func_74764_b("CustomName")) {
         this.customName = nbt.func_74779_i("CustomName");
      }

      if (nbt.func_74764_b("ShadowGame")) {
         this.isShadowGame = nbt.func_74767_n("ShadowGame");
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("DeckSize", this.deckSize);
      nbt.func_74768_a("PrizeCount", this.prizeCount);
      nbt.func_74768_a("TimeLimit", this.timeLimit);
      nbt.func_74768_a("EloMinimum", this.eloMinimum);
      nbt.func_74778_a("StartingCommand", this.startingCommand);
      nbt.func_74778_a("EndingCommand", this.endingCommand);
      nbt.func_74778_a("StartingMessage", this.startingMessage);
      nbt.func_74778_a("EndingMessage", this.endingMessage);
      nbt.func_74778_a("CustomName", this.customName);
      nbt.func_74757_a("ShadowGame", this.isShadowGame);
      return nbt;
   }

   int trigger(World world, final String command, String winnerName, String loserName) {
      if (world.field_72995_K) {
         return 0;
      } else {
         MinecraftServer minecraftServer = world.func_73046_m();
         if (minecraftServer != null && minecraftServer.func_175578_N() && minecraftServer.func_82356_Z()) {
            ICommandManager commandManager = minecraftServer.func_71187_D();

            try {
               TCG var10000 = TCG.instance;
               TCG.logger.info("Try to trigger \"" + command + "\"");
               if (!command.isEmpty()) {
                  String formattedCommand = this.formatCommand(command, winnerName, loserName);
                  commandManager.func_71556_a(minecraftServer, formattedCommand);
               }

               return 1;
            } catch (Throwable var10) {
               CrashReport crashreport = CrashReport.func_85055_a(var10, "Executing command of BattleBox");
               CrashReportCategory crashreportcategory = crashreport.func_85058_a("Command to be executed");
               crashreportcategory.func_71507_a("Command", new Callable() {
                  public String call() throws Exception {
                     return command;
                  }
               });
               crashreportcategory.func_71507_a("Name", new Callable() {
                  public String call() throws Exception {
                     return "BattleRule";
                  }
               });
               throw new ReportedException(crashreport);
            }
         } else {
            return 0;
         }
      }
   }

   public String formatCommand(String command, String winnerName, String loserName) {
      return command;
   }

   public int getDeckSize() {
      return this.deckSize;
   }

   public void setDeckSize(int deckSize) {
      this.deckSize = deckSize;
   }

   public int getTimeLimit() {
      return this.timeLimit;
   }

   public int getPrizeCount() {
      return this.prizeCount;
   }

   public void setPrizeCount(int prizeCount) {
      this.prizeCount = prizeCount;
   }

   public int getEloMinimum() {
      return this.eloMinimum;
   }

   public void setEloMinimum(int eloMinimum) {
      this.eloMinimum = eloMinimum;
   }

   public String getStartingCommand() {
      return this.startingCommand;
   }

   public void setStartingCommand(String startingCommand) {
      this.startingCommand = startingCommand;
   }

   public String getEndingCommand() {
      return this.endingCommand;
   }

   public void setEndingCommand(String endingCommand) {
      this.endingCommand = endingCommand;
   }

   public String getStartingMessage() {
      return this.startingMessage;
   }

   public void setStartingMessage(String startingMessage) {
      this.startingMessage = startingMessage;
   }

   public String getEndingMessage() {
      return this.endingMessage;
   }

   public void setEndingMessage(String endingMessage) {
      this.endingMessage = endingMessage;
   }

   public void setTimeLimit(int timeLimit) {
      this.timeLimit = timeLimit;
   }

   public ITextComponent func_145748_c_() {
      return new TextComponentString(this.getName());
   }

   public String getName() {
      return "BattleBox";
   }

   public BlockPos getPosition() {
      return this.field_174879_c;
   }

   public Vec3d getPositionVector() {
      return new Vec3d((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p());
   }

   public World getEntityWorld() {
      return this.field_145850_b;
   }

   @Nullable
   public MinecraftServer getServer() {
      return null;
   }

   public void endGame(PlayerServerState winner, PlayerServerState loser, boolean tiedGame) {
   }

   public boolean isShadowGame() {
      return this.isShadowGame;
   }

   public void setShadowGame(boolean b) {
      this.isShadowGame = b;
   }

   public void toggleShadowGame() {
      this.isShadowGame = !this.isShadowGame;
   }
}
