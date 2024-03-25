package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.EnumTier;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.TierHierarchical;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelection;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Chlorophyll;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Drizzle;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Drought;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Moody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandRush;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandStream;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandVeil;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ShadowTag;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SnowCloak;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SnowWarning;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SwiftSwim;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class BattleClauseRegistry {
   private final Map clauses = new HashMap();
   private final List customClauses = new ArrayList();
   public static final String BAG_CLAUSE = "bag";
   public static final String FORFEIT_CLAUSE = "forfeit";
   public static final String INVERSE_BATTLE = "inverse";
   public static final String SKY_BATTLE = "sky";
   public static final String SLEEP_CLAUSE = "sleep";
   private static int clauseVersion;
   private static final BattleClauseRegistry clauseRegistry = new BattleClauseRegistry((id) -> {
      return new BattleClause(id);
   });
   private static final BattleClauseRegistry tierRegistry = new BattleClauseRegistry((id) -> {
      return null;
   });
   private Function getDefault;

   private BattleClauseRegistry(Function getDefault) {
      this.getDefault = getDefault;
   }

   public static BattleClauseRegistry getClauseRegistry() {
      return clauseRegistry;
   }

   public static BattleClauseRegistry getTierRegistry() {
      return tierRegistry;
   }

   private static void registerDefaultClauses() {
      clauseRegistry.registerClause(new BattleClause("bag"));
      BattleClause batonPassClause = new MoveClause("batonpass", new String[]{"Baton Pass"});
      clauseRegistry.registerClause(batonPassClause);
      clauseRegistry.registerClause(new BatonPass1Clause());
      clauseRegistry.registerClause(new MoveClause("chatter", new String[]{"Chatter"}));
      clauseRegistry.registerClause(new AbilityClause("drizzle", new Class[]{Drizzle.class}));
      BattleClause drizzleSwimClause = new AbilityComboClause("drizzleswim", new Class[]{Drizzle.class, SwiftSwim.class});
      clauseRegistry.registerClause(drizzleSwimClause);
      clauseRegistry.registerClause(new AbilityClause("drought", new Class[]{Drought.class}));
      clauseRegistry.registerClause(new BattleClauseSingleAll("endlessbattle", new BattleClause[]{new ItemPreventClause("", new EnumHeldItems[]{EnumHeldItems.leppa}), new MoveClause("", new String[]{"Recycle"}), new MoveClause("", new String[]{"Fling", "Heal Pulse", "Pain Split"})}));
      clauseRegistry.registerClause(new AbilityClause("evasionability", new Class[]{SandVeil.class, SnowCloak.class}));
      clauseRegistry.registerClause(new MoveClause("evasion", new String[]{"Double Team", "Minimize"}));
      clauseRegistry.registerClause(new BattleClause("forfeit"));
      clauseRegistry.registerClause(new BattleClause("inverse"));
      clauseRegistry.registerClause(new ItemClause());
      clauseRegistry.registerClause(new PokemonClause("legendary", EnumSpecies.LEGENDARY_ENUMS));
      clauseRegistry.registerClause(new ItemPreventClause("mega", new EnumHeldItems[]{EnumHeldItems.megaStone}));
      clauseRegistry.registerClause(new AbilityClause("moody", new Class[]{Moody.class}));
      clauseRegistry.registerClause(new MoveClause("ohko", new String[]{"Fissure", "Guillotine", "Horn Drill", "Sheer Cold"}));
      clauseRegistry.registerClause(new AbilityClause("sandstream", new Class[]{SandStream.class}));
      clauseRegistry.registerClause(new AbilityClause("shadowtag", new Class[]{ShadowTag.class}));
      clauseRegistry.registerClause(new SkyBattle());
      clauseRegistry.registerClause(new BattleClause("sleep"));
      clauseRegistry.registerClause(new BattleClauseSingleAll("smashpass", new BattleClause[]{batonPassClause, new MoveClause("", new String[]{"Shell Smash"})}));
      clauseRegistry.registerClause(new AbilityClause("snowwarning", new Class[]{SnowWarning.class}));
      clauseRegistry.registerClause(new BattleClauseSingleAll("souldew", new BattleClause[]{new PokemonClause("", new EnumSpecies[]{EnumSpecies.Latias, EnumSpecies.Latios}), new ItemPreventClause("", new EnumHeldItems[]{EnumHeldItems.soulDew})}));
      clauseRegistry.registerClause(new SpeciesClause());
      clauseRegistry.registerClause(new SpeedPassClause());
      clauseRegistry.registerClause(new MoveClause("swagger", new String[]{"Swagger"}));
      clauseRegistry.registerClause(new BattleClauseAny("weatherspeed", new BattleClause[]{drizzleSwimClause, new AbilityComboClause("", new Class[]{Drought.class, Chlorophyll.class}), new AbilityComboClause("", new Class[]{SandStream.class, SandRush.class})}));
   }

   private static void registerDefaultTiers() {
      TierHierarchical lastTier = null;
      EnumTier[] var1 = EnumTier.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumTier tier = var1[var3];
         lastTier = new TierHierarchical(tier.getTierID(), new HashSet(), lastTier);
         tierRegistry.registerClause(lastTier);
      }

   }

   public BattleClause getClause(String id) {
      BattleClause clause = (BattleClause)this.clauses.get(id);
      if (clause == null) {
         clause = (BattleClause)this.getDefault.apply(id);
      }

      return clause;
   }

   public boolean hasClause(String id) {
      return this.clauses.containsKey(id);
   }

   private void registerClause(BattleClause newClause) {
      String newClauseID = newClause.getID();
      if (newClauseID != null && !newClauseID.isEmpty()) {
         if (this.clauses.containsKey(newClauseID)) {
            throw new IllegalArgumentException("Clause is already registered: " + newClauseID);
         } else {
            String[] var3 = TeamSelection.getReservedKeys();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String reserved = var3[var5];
               if (reserved.equals(newClauseID)) {
                  throw new IllegalArgumentException("Clause cannot used reserved ID: " + reserved);
               }
            }

            this.clauses.put(newClauseID, newClause);
         }
      } else {
         throw new IllegalArgumentException("Clause must have a key to be registered.");
      }
   }

   public void registerCustomClause(BattleClause newClause) {
      this.registerClause(newClause);
      this.customClauses.add(newClause);
      incrementVersion();
   }

   public static void incrementVersion() {
      ++clauseVersion;
   }

   public void replaceCustomClauses(List newClauses) {
      this.replaceCustomClauses(newClauses, clauseVersion + 1);
   }

   public void replaceCustomClauses(List newClauses, int newClauseVersion) {
      this.removeCustomClauses();
      Iterator var3 = newClauses.iterator();

      while(var3.hasNext()) {
         BattleClause clause = (BattleClause)var3.next();
         this.registerCustomClause(clause);
      }

      clauseVersion = newClauseVersion;
   }

   public void removeCustomClauses() {
      Iterator var1 = this.customClauses.iterator();

      while(var1.hasNext()) {
         BattleClause clause = (BattleClause)var1.next();
         this.clauses.remove(clause.getID());
      }

      this.customClauses.clear();
   }

   public List getCustomClauses() {
      return new ArrayList(this.customClauses);
   }

   public List getClauseList() {
      List clauseList = new ArrayList();
      clauseList.addAll(this.clauses.values());
      Collections.sort(clauseList);
      return clauseList;
   }

   public static int getClauseVersion() {
      return clauseVersion;
   }

   static {
      registerDefaultClauses();
      registerDefaultTiers();
   }
}
