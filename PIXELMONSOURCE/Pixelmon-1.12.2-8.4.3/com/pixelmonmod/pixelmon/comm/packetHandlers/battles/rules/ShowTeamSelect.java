package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectPokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShowTeamSelect implements IMessage {
   public int teamSelectID;
   public String[] disabled;
   public List opponentTeam;
   public int opponentSize;
   public int npcID;
   public String npcName;
   public UUID opponentUUID;
   BattleRules rules;
   boolean showRules;

   public ShowTeamSelect() {
      this.npcName = "";
   }

   private ShowTeamSelect(int teamSelectID, String[] disabled, List opponentTeam, BattleRules rules, boolean showRules) {
      this.npcName = "";
      this.teamSelectID = teamSelectID;
      this.disabled = disabled;
      if (rules.teamPreview) {
         this.opponentTeam = opponentTeam;
      } else {
         this.opponentSize = opponentTeam.size();
      }

      this.rules = rules;
      this.showRules = showRules;
   }

   public ShowTeamSelect(int teamSelectID, String[] disabled, List opponentTeam, int npcID, String npcName, BattleRules rules, boolean showRules) {
      this(teamSelectID, disabled, opponentTeam, rules, showRules);
      this.npcID = npcID;
      this.npcName = npcName;
   }

   public ShowTeamSelect(int teamSelectID, String[] disabled, List opponentTeam, UUID opponentUUID, BattleRules rules, boolean showRules) {
      this(teamSelectID, disabled, opponentTeam, rules, showRules);
      this.opponentUUID = opponentUUID;
   }

   public boolean isAllDisabled() {
      String[] var1 = this.disabled;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String s = var1[var3];
         if (s == null || s.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.teamSelectID);
      ArrayHelper.encodeStringArray(buf, this.disabled);
      buf.writeBoolean(this.rules.teamPreview);
      if (this.rules.teamPreview) {
         ArrayHelper.encodeList(buf, this.opponentTeam);
      } else {
         buf.writeInt(this.opponentSize);
      }

      if (this.opponentUUID == null) {
         buf.writeBoolean(true);
         buf.writeInt(this.npcID);
         ByteBufUtils.writeUTF8String(buf, this.npcName);
      } else {
         buf.writeBoolean(false);
         PixelmonMethods.toBytesUUID(buf, this.opponentUUID);
      }

      this.rules.encodeInto(buf);
      buf.writeBoolean(this.showRules);
   }

   public void fromBytes(ByteBuf buf) {
      this.teamSelectID = buf.readInt();
      this.disabled = ArrayHelper.decodeStringArray(buf);
      if (buf.readBoolean()) {
         int length = buf.readInt();
         this.opponentTeam = new ArrayList();

         for(int i = 0; i < length; ++i) {
            this.opponentTeam.add(new TeamSelectPokemon(buf));
         }
      } else {
         this.opponentSize = buf.readInt();
      }

      if (buf.readBoolean()) {
         this.npcID = buf.readInt();
         this.npcName = ByteBufUtils.readUTF8String(buf);
      } else {
         this.opponentUUID = PixelmonMethods.fromBytesUUID(buf);
      }

      this.rules = new BattleRules(buf);
      this.showRules = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ShowTeamSelect message, MessageContext ctx) {
         ClientProxy.battleManager.rules = message.rules;
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(ShowTeamSelect message) {
         EnumGuiScreen gui = message.showRules ? EnumGuiScreen.BattleRulesFixed : EnumGuiScreen.TeamSelect;
         GuiTeamSelect.teamSelectPacket = message;
         OpenScreen.open(Minecraft.func_71410_x().field_71439_g, gui);
      }
   }
}
