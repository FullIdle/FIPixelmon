package com.pixelmonmod.pixelmon.battles.rules;

import com.pixelmonmod.pixelmon.api.pokemon.ImportExportConverter;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.EnumTier;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.Tier;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BattleRules implements IEncodeable {
   public int levelCap;
   public boolean raiseToCap;
   public EnumBattleType battleType;
   public EnumOldGenMode oldgen;
   public int numPokemon;
   public int turnTime;
   public int teamSelectTime;
   public boolean teamPreview;
   public boolean fullHeal;
   public Tier tier;
   private Set clauses;
   public static final int MAX_NUM_POKEMON = 6;
   private static final String LEVEL_CAP_TEXT = "LevelCap";
   private static final String RAISE_TO_CAP_TEXT = "RaiseToCap";
   private static final String BATTLE_TYPE_TEXT = "BattleType";
   private static final String BATTLE_OLD_GEN = "OldGen";
   private static final String NUM_POKEMON_TEXT = "NumPokemon";
   private static final String TURN_TIME_TEXT = "TurnTime";
   private static final String TEAM_SELECT_TIME_TEXT = "TeamSelectTime";
   private static final String TEAM_PREVIEW_TEXT = "TeamPreview";
   private static final String FULL_HEAL_TEXT = "FullHeal";
   private static final String TIER_TEXT = "Tier";
   private static final String CLAUSES_TEXT = "Clauses";

   public BattleRules() {
      this.levelCap = PixelmonServerConfig.maxLevel;
      this.battleType = EnumBattleType.Single;
      this.oldgen = EnumOldGenMode.World;
      this.numPokemon = 6;
      this.clauses = new HashSet();
      this.tier = this.getDefaultTier();
   }

   public BattleRules(EnumBattleType battleType) {
      this();
      this.battleType = battleType;
   }

   public BattleRules(String text) {
      this.levelCap = PixelmonServerConfig.maxLevel;
      this.battleType = EnumBattleType.Single;
      this.oldgen = EnumOldGenMode.World;
      this.numPokemon = 6;
      this.clauses = new HashSet();
      this.importText(text);
   }

   public BattleRules(ByteBuf buf) {
      this.levelCap = PixelmonServerConfig.maxLevel;
      this.battleType = EnumBattleType.Single;
      this.oldgen = EnumOldGenMode.World;
      this.numPokemon = 6;
      this.clauses = new HashSet();
      this.decodeInto(buf);
   }

   public void validateRules() {
      this.levelCap = MathHelper.func_76125_a(this.levelCap, 1, PixelmonServerConfig.maxLevel);
      this.numPokemon = MathHelper.func_76125_a(this.numPokemon, this.battleType.numPokemon, 6);
      this.turnTime = Math.max(this.turnTime, 0);
      if (PixelmonServerConfig.afkHandlerOn) {
         this.turnTime = Math.min(this.turnTime, PixelmonServerConfig.afkTimerActivateSeconds);
      }

      this.teamSelectTime = Math.max(this.teamSelectTime, 0);
   }

   public String exportText() {
      StringBuilder builder = new StringBuilder();
      if (this.levelCap < PixelmonServerConfig.maxLevel) {
         ImportExportConverter.addColonSeparated(builder, "LevelCap", this.levelCap);
      }

      if (this.raiseToCap) {
         ImportExportConverter.addLine(builder, "RaiseToCap");
      }

      if (this.battleType != EnumBattleType.Single) {
         ImportExportConverter.addColonSeparated(builder, "BattleType", this.battleType.toString());
      }

      if (this.oldgen != null) {
         ImportExportConverter.addColonSeparated(builder, "OldGen", this.oldgen.toString());
      }

      if (this.numPokemon < 6) {
         ImportExportConverter.addColonSeparated(builder, "NumPokemon", this.numPokemon);
      }

      if (this.turnTime > 0) {
         ImportExportConverter.addColonSeparated(builder, "TurnTime", this.turnTime);
      }

      if (this.teamSelectTime > 0) {
         ImportExportConverter.addColonSeparated(builder, "TeamSelectTime", this.teamSelectTime);
      }

      if (this.teamPreview) {
         ImportExportConverter.addLine(builder, "TeamPreview");
      }

      if (this.fullHeal) {
         ImportExportConverter.addLine(builder, "FullHeal");
      }

      if (this.tier != this.getDefaultTier()) {
         ImportExportConverter.addColonSeparated(builder, "Tier", this.tier.getID());
      }

      if (!this.clauses.isEmpty()) {
         ImportExportConverter.addLine(builder, "Clauses");
         List clauseIDs = this.getClauseIDs();
         Iterator var3 = clauseIDs.iterator();

         while(var3.hasNext()) {
            String clauseID = (String)var3.next();
            ImportExportConverter.addLine(builder, clauseID);
         }
      }

      return builder.toString();
   }

   public String importText(String text) {
      String currentSection = "Format";
      String[] importTextSplit = text.split("\n");
      BattleRules newRules = new BattleRules();

      try {
         int i = 0;
         boolean hasClauses = false;
         String[] var7 = importTextSplit;
         int var8 = importTextSplit.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String line = var7[var9];
            if (line.startsWith("LevelCap")) {
               currentSection = "LevelCap";
               newRules.levelCap = ImportExportConverter.getIntAfterColon(line);
            } else if (line.equals("RaiseToCap")) {
               newRules.raiseToCap = true;
            } else if (line.startsWith("BattleType")) {
               currentSection = "BattleType";
               newRules.battleType = EnumBattleType.valueOf(ImportExportConverter.getStringAfterColon(line));
            } else if (line.startsWith("OldGen")) {
               currentSection = "OldGen";
               newRules.oldgen = EnumOldGenMode.valueOf(ImportExportConverter.getStringAfterColon(line));
            } else if (line.startsWith("NumPokemon")) {
               currentSection = "NumPokemon";
               newRules.numPokemon = ImportExportConverter.getIntAfterColon(line);
            } else if (line.startsWith("TurnTime")) {
               currentSection = "TurnTime";
               newRules.turnTime = ImportExportConverter.getIntAfterColon(line);
            } else if (line.startsWith("TeamSelectTime")) {
               currentSection = "TeamSelectTime";
               newRules.teamSelectTime = ImportExportConverter.getIntAfterColon(line);
            } else if (line.equals("TeamPreview")) {
               newRules.teamPreview = true;
            } else if (line.equals("FullHeal")) {
               newRules.fullHeal = true;
            } else if (line.startsWith("Tier")) {
               currentSection = "Tier";
               String tierID = ImportExportConverter.getStringAfterColon(line);
               Tier tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(tierID);
               if (tier == null) {
                  tier = this.getDefaultTier();
               }

               newRules.tier = tier;
            } else if (line.equals("Clauses")) {
               currentSection = "Clauses";
               hasClauses = true;
            }

            ++i;
            if (hasClauses) {
               break;
            }
         }

         if (hasClauses) {
            for(; i < importTextSplit.length; ++i) {
               String clauseID = importTextSplit[i];
               BattleClauseRegistry clauseRegistry = BattleClauseRegistry.getClauseRegistry();
               if (clauseRegistry.hasClause(clauseID)) {
                  newRules.clauses.add(clauseRegistry.getClause(clauseID));
               }
            }
         }
      } catch (IndexOutOfBoundsException | IllegalArgumentException var13) {
         return currentSection;
      }

      this.levelCap = newRules.levelCap;
      this.raiseToCap = newRules.raiseToCap;
      this.battleType = newRules.battleType;
      this.oldgen = newRules.oldgen;
      this.numPokemon = newRules.numPokemon;
      this.turnTime = newRules.turnTime;
      this.teamSelectTime = newRules.teamSelectTime;
      this.teamPreview = newRules.teamPreview;
      this.fullHeal = newRules.fullHeal;
      this.tier = newRules.tier;
      this.clauses = newRules.clauses;
      this.validateRules();
      return null;
   }

   public void encodeInto(ByteBuf buf) {
      buf.writeInt(this.levelCap);
      buf.writeBoolean(this.raiseToCap);
      buf.writeByte(this.battleType.ordinal());
      if (this.oldgen == null) {
         this.oldgen = EnumOldGenMode.World;
      }

      buf.writeByte(this.oldgen.ordinal());
      buf.writeByte(this.numPokemon);
      buf.writeInt(this.turnTime);
      buf.writeInt(this.teamSelectTime);
      buf.writeBoolean(this.teamPreview);
      buf.writeBoolean(this.fullHeal);
      ByteBufUtils.writeUTF8String(buf, this.tier.getID());
      buf.writeShort(this.clauses.size());
      Iterator var2 = this.clauses.iterator();

      while(var2.hasNext()) {
         BattleClause clause = (BattleClause)var2.next();
         ByteBufUtils.writeUTF8String(buf, clause.getID());
      }

   }

   public void decodeInto(ByteBuf buf) {
      try {
         this.levelCap = buf.readInt();
         this.raiseToCap = buf.readBoolean();
         this.battleType = EnumBattleType.getFromOrdinal(buf.readByte());
         this.oldgen = EnumOldGenMode.getFromIndex(buf.readByte());
         this.numPokemon = buf.readByte();
         this.turnTime = buf.readInt();
         this.teamSelectTime = buf.readInt();
         this.teamPreview = buf.readBoolean();
         this.fullHeal = buf.readBoolean();
         this.tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(ByteBufUtils.readUTF8String(buf));
         if (this.tier == null) {
            this.tier = this.getDefaultTier();
         }

         int numClauses = buf.readShort();
         this.clauses.clear();

         for(int i = 0; i < numClauses; ++i) {
            String clauseID = ByteBufUtils.readUTF8String(buf);
            BattleClauseRegistry clauseRegistry = BattleClauseRegistry.getClauseRegistry();
            if (clauseRegistry.hasClause(clauseID)) {
               this.clauses.add(clauseRegistry.getClause(clauseID));
            }
         }
      } catch (IndexOutOfBoundsException var6) {
      }

      this.validateRules();
   }

   private void importFromOldVersions(ByteBuf buffer) {
      try {
         this.levelCap = buffer.readInt();
         this.raiseToCap = buffer.readBoolean();
         this.battleType = EnumBattleType.getFromOrdinal(buffer.readInt());
         this.numPokemon = buffer.readInt();
         this.turnTime = buffer.readInt();
         this.teamSelectTime = buffer.readInt();
         this.teamPreview = buffer.readBoolean();
         this.fullHeal = buffer.readBoolean();
         this.tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(ByteBufUtils.readUTF8String(buffer));
         if (this.tier == null) {
            this.tier = this.getDefaultTier();
         }

         int numClauses = buffer.readInt();
         this.clauses.clear();

         for(int i = 0; i < numClauses; ++i) {
            String clauseID = ByteBufUtils.readUTF8String(buffer);
            BattleClauseRegistry clauseRegistry = BattleClauseRegistry.getClauseRegistry();
            if (clauseRegistry.hasClause(clauseID)) {
               this.clauses.add(clauseRegistry.getClause(clauseID));
            }
         }
      } catch (IndexOutOfBoundsException var6) {
      }

   }

   public void writeToNBT(NBTTagCompound nbt) {
      NBTTagCompound compound = new NBTTagCompound();
      nbt.func_74782_a("rules_data", compound);
      compound.func_74768_a("levelCap", this.levelCap);
      compound.func_74757_a("raiseToCap", this.raiseToCap);
      compound.func_74774_a("battleType", (byte)this.battleType.ordinal());
      compound.func_74774_a("oldGen", (byte)this.oldgen.ordinal());
      compound.func_74774_a("numPokemon", (byte)this.numPokemon);
      compound.func_74768_a("turnTime", this.turnTime);
      compound.func_74768_a("teamSelectTime", this.teamSelectTime);
      compound.func_74757_a("teamPreview", this.teamPreview);
      compound.func_74757_a("fullHeal", this.fullHeal);
      compound.func_74778_a("tier", this.tier.getID());
      NBTTagList list = new NBTTagList();
      Iterator var4 = this.clauses.iterator();

      while(var4.hasNext()) {
         BattleClause clause = (BattleClause)var4.next();
         list.func_74742_a(new NBTTagString(clause.getID()));
      }

      compound.func_74782_a("clauses", list);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("BattleRules")) {
         byte[] bytes = nbt.func_74770_j("BattleRules");
         ByteBuf buffer = Unpooled.copiedBuffer(bytes);
         this.importFromOldVersions(buffer);
      } else if (nbt.func_74764_b("rules_data")) {
         NBTTagCompound compound = nbt.func_74775_l("rules_data");
         this.levelCap = compound.func_74762_e("levelCap");
         this.raiseToCap = compound.func_74767_n("raiseToCap");
         this.battleType = EnumBattleType.getFromOrdinal(compound.func_74771_c("battleType"));
         this.oldgen = EnumOldGenMode.getFromIndex(compound.func_74771_c("oldgen"));
         this.numPokemon = compound.func_74771_c("numPokemon");
         this.turnTime = compound.func_74762_e("turnTime");
         this.teamSelectTime = compound.func_74762_e("teamSelectTime");
         this.teamPreview = compound.func_74767_n("teamPreview");
         this.fullHeal = compound.func_74767_n("fullHeal");
         this.tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(compound.func_74779_i("tier"));
         if (this.tier == null) {
            this.tier = this.getDefaultTier();
         }

         this.clauses.clear();
         NBTTagList list = compound.func_150295_c("clauses", 8);
         BattleClauseRegistry clauseRegistry = BattleClauseRegistry.getClauseRegistry();
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            NBTBase tag = (NBTBase)var5.next();
            NBTTagString str = (NBTTagString)tag;
            if (clauseRegistry.hasClause(str.func_150285_a_())) {
               this.clauses.add(clauseRegistry.getClause(str.func_150285_a_()));
            }
         }

         this.validateRules();
      }

   }

   public List getClauseList() {
      List clauseList = new ArrayList();
      clauseList.addAll(this.clauses);
      Collections.sort(clauseList);
      return clauseList;
   }

   public List getClauseIDs() {
      List clauseIDs = new ArrayList();
      Iterator var2 = this.clauses.iterator();

      while(var2.hasNext()) {
         BattleClause clause = (BattleClause)var2.next();
         clauseIDs.add(clause.getID());
      }

      Collections.sort(clauseIDs);
      return clauseIDs;
   }

   public boolean hasClause(String id) {
      return this.clauses.contains(BattleClauseRegistry.getClauseRegistry().getClause(id));
   }

   public void setNewClauses(List newClauses) {
      this.clauses.clear();
      this.clauses.addAll(newClauses);
   }

   public boolean isDefault() {
      boolean defaultClauses = this.clauses.isEmpty();
      if (this.clauses.size() == 1) {
         defaultClauses = this.hasClause("forfeit");
      }

      return defaultClauses && this.oldgen == EnumOldGenMode.World && this.battleType == EnumBattleType.Single && this.levelCap == PixelmonServerConfig.maxLevel && !this.raiseToCap && this.numPokemon == 6 && this.turnTime == 0 && this.teamSelectTime == 0 && !this.teamPreview && this.tier == this.getDefaultTier();
   }

   public String validateSingle(Pokemon pokemon) {
      this.reloadTier();
      if (!this.tier.validateSingle(pokemon)) {
         return this.tier.getID();
      } else {
         Iterator var2 = this.clauses.iterator();

         BattleClause clause;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            clause = (BattleClause)var2.next();
         } while(clause.validateSingle(pokemon));

         return clause.getID();
      }
   }

   public String validateTeam(List team) {
      this.reloadTier();
      if (!this.tier.validateTeam(team)) {
         return this.tier.getID();
      } else {
         Iterator var2 = this.clauses.iterator();

         BattleClause clause;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            clause = (BattleClause)var2.next();
         } while(clause.validateTeam(team));

         return clause.getID();
      }
   }

   private void reloadTier() {
      this.tier = (Tier)BattleClauseRegistry.getTierRegistry().getClause(this.tier.getID());
   }

   private Tier getDefaultTier() {
      return (Tier)BattleClauseRegistry.getTierRegistry().getClause(EnumTier.Unrestricted.getTierID());
   }
}
