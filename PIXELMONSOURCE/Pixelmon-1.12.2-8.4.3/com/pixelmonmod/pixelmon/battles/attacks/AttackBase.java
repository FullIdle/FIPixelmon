package com.pixelmonmod.pixelmon.battles.attacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimationTypeAdapter;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationDataAdapter;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.EnumEffectType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import com.pixelmonmod.pixelmon.util.ResourceLocationAdapter;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public class AttackBase implements ITranslatable {
   private int attackIndex;
   private String attackName;
   private EnumType attackType;
   private AttackCategory attackCategory;
   private int basePower;
   private int ppBase;
   private int ppMax;
   private int accuracy;
   private EnumEffectType effectType;
   private boolean makesContact;
   private boolean ignoreAbility;
   private TargetingInfo targetingInfo;
   private MoveFlags flags;
   public ArrayList effects;
   public ArrayList animations;
   public ArrayList z;
   public static final transient ArrayList ATTACKS = new ArrayList();
   public static final Gson GSON;
   private static final Map ATTACK_MAP;

   public AttackBase(EnumType attackType, int basePower, AttackCategory attackCategory) {
      this.attackType = EnumType.Mystery;
      this.ignoreAbility = false;
      this.effects = new ArrayList();
      this.animations = new ArrayList();
      this.z = new ArrayList();
      this.attackType = attackType;
      this.basePower = basePower;
      this.attackCategory = attackCategory;
      this.attackName = "";
   }

   public int getAttackId() {
      return this.attackIndex;
   }

   public String getAttackName() {
      return this.attackName;
   }

   public EnumType getAttackType() {
      return this.attackType;
   }

   public AttackCategory getAttackCategory() {
      return this.attackCategory;
   }

   public int getBasePower() {
      return this.basePower;
   }

   /** @deprecated */
   @Deprecated
   public void setBasePower(int basePower) {
      this.basePower = basePower;
   }

   public int getPPBase() {
      return this.ppBase;
   }

   public int getPPMax() {
      return this.ppMax;
   }

   public int getAccuracy() {
      return this.accuracy;
   }

   /** @deprecated */
   @Deprecated
   public void setAccuracy(int accuracy) {
      this.accuracy = accuracy;
   }

   public boolean getMakesContact() {
      return this.makesContact;
   }

   public void setMakesContact(boolean makesContact) {
      this.makesContact = makesContact;
   }

   public boolean getIgnoresAbilities() {
      return this.ignoreAbility;
   }

   public TargetingInfo getTargetingInfo() {
      return this.targetingInfo;
   }

   public MoveFlags getFlags() {
      return this.flags == null ? MoveFlags.DEFAULT : this.flags;
   }

   public boolean isAttack(String name) {
      return this.attackName.equalsIgnoreCase(name) || this.getLocalizedName().equalsIgnoreCase(name);
   }

   public boolean isAttack(Attack attack) {
      return this == attack.getMove();
   }

   public int getPriority(PixelmonWrapper pixelmon) {
      int priority = 0;

      EffectBase effect;
      for(Iterator var3 = this.effects.iterator(); var3.hasNext(); priority = effect.modifyPriority(priority, this, pixelmon)) {
         effect = (EffectBase)var3.next();
      }

      return priority;
   }

   public boolean hasSecondaryEffect() {
      Iterator var1 = this.effects.iterator();

      EffectBase effect;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         effect = (EffectBase)var1.next();
      } while(!effect.isChance());

      return true;
   }

   public boolean hasEffect(Class clazz) {
      if (this.effects != null) {
         Iterator var2 = this.effects.iterator();

         while(var2.hasNext()) {
            EffectBase effect = (EffectBase)var2.next();
            if (clazz.isInstance(effect)) {
               return true;
            }
         }
      }

      return false;
   }

   public ZMove getZMove(Pokemon pokemon, boolean bypassItemRequirement) {
      if (this.z != null) {
         Iterator var3 = this.z.iterator();

         ZMove zMove;
         String itemName;
         String paraName;
         do {
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               zMove = (ZMove)var3.next();
               itemName = pokemon.getHeldItemAsItemHeld().func_77658_a();
               paraName = "item." + zMove.crystal;
            } while(!bypassItemRequirement && !itemName.equalsIgnoreCase(paraName));
         } while(!zMove.allowedPokemon.isEmpty() && !zMove.allowedPokemon.contains(pokemon.getSpecies().name) && !zMove.allowedPokemon.contains(pokemon.getSpecies().name + pokemon.getFormEnum().getFormSuffix()));

         return zMove;
      } else {
         return null;
      }
   }

   public ZMove getZMove(EnumSpecies species, int form, ItemHeld held, boolean bypassItemRequirement) {
      if (this.z != null) {
         Iterator var5 = this.z.iterator();

         ZMove zMove;
         String itemName;
         String paraName;
         do {
            do {
               if (!var5.hasNext()) {
                  return null;
               }

               zMove = (ZMove)var5.next();
               itemName = held.func_77658_a();
               paraName = "item." + zMove.crystal;
            } while(!bypassItemRequirement && !itemName.equalsIgnoreCase(paraName));
         } while(!zMove.allowedPokemon.isEmpty() && !zMove.allowedPokemon.contains(species.name) && !zMove.allowedPokemon.contains(species.name + species.getFormEnum(form).getFormSuffix()));

         return zMove;
      } else {
         return null;
      }
   }

   public boolean hasZMove(Pokemon pokemon) {
      if (this.z != null) {
         Iterator var2 = this.z.iterator();

         while(var2.hasNext()) {
            ZMove zMove = (ZMove)var2.next();
            if (pokemon != null) {
               String itemName = pokemon.getHeldItemAsItemHeld().func_77658_a();
               String paraName = "item." + zMove.crystal;
               if (itemName.equalsIgnoreCase(paraName) && (zMove.allowedPokemon.isEmpty() || zMove.allowedPokemon.contains(pokemon.getSpecies().name) || zMove.allowedPokemon.contains(pokemon.getSpecies().name + pokemon.getFormEnum().getFormSuffix()))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public String getUnlocalizedName() {
      return "attack." + this.attackName.toLowerCase().replace(" ", "_") + ".name";
   }

   public static void loadAllAttacks() {
      ATTACKS.clear();
      File file = new File("pixelmon/moves");
      if (PixelmonConfig.useExternalJSONFilesMoves && file.exists()) {
         ArrayList files = new ArrayList();
         RCFileHelper.recursiveJSONSearch("pixelmon/moves", files);
         Iterator var15 = files.iterator();

         while(var15.hasNext()) {
            File moveFile = (File)var15.next();

            try {
               AttackBase ab = (AttackBase)GSON.fromJson(new FileReader(moveFile), AttackBase.class);
               ATTACKS.add(ab);
               ATTACK_MAP.put(ab.attackName.toLowerCase(), ab);
            } catch (JsonIOException | FileNotFoundException | JsonSyntaxException var10) {
               Pixelmon.LOGGER.error("Unable to load external move JSON " + moveFile.getName());
               var10.printStackTrace();
            }
         }
      } else {
         try {
            Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "moves"));
            List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
               return true;
            }, true);
            Iterator var3 = ls.iterator();

            while(var3.hasNext()) {
               Path path = (Path)var3.next();
               InputStream is = AttackBase.class.getResourceAsStream(path.toUri().toString().substring(path.toUri().toString().indexOf("/assets")).replace("%20", " "));
               Scanner s = new Scanner(is);
               s.useDelimiter("\\A");
               String json = s.hasNext() ? s.next() : "";
               s.close();
               AttackBase attackBase = null;

               try {
                  attackBase = (AttackBase)GSON.fromJson(json, AttackBase.class);
               } catch (JsonSyntaxException var11) {
                  var11.printStackTrace();
               }

               if (attackBase == null) {
                  Pixelmon.LOGGER.error("Unable to load move JSON: " + path.toString());
               } else {
                  ATTACKS.add(attackBase);
                  ATTACK_MAP.put(attackBase.attackName.toLowerCase(), attackBase);
                  if (PixelmonConfig.useExternalJSONFilesMoves) {
                     file.mkdirs();
                     PrintWriter pw = new PrintWriter(new File("pixelmon/moves/" + attackBase.attackName + ".json"));
                     pw.write(json);
                     pw.close();
                  }
               }
            }
         } catch (URISyntaxException | IOException var12) {
            var12.printStackTrace();
         }
      }

      Iterator var14 = ATTACKS.iterator();

      while(var14.hasNext()) {
         AttackBase attackBase = (AttackBase)var14.next();
         if (attackBase.effects != null) {
            attackBase.effects.removeIf(Objects::isNull);
         }
      }

   }

   public static Optional getAttackBase(String moveName) {
      Iterator var1 = ATTACKS.iterator();

      AttackBase attackBase;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         attackBase = (AttackBase)var1.next();
         if (attackBase.getLocalizedName().equalsIgnoreCase(moveName)) {
            return Optional.of(attackBase);
         }
      } while(!attackBase.attackName.equalsIgnoreCase(moveName));

      return Optional.of(attackBase);
   }

   public static List getAttackBases(String... moveNames) {
      List attacks = new ArrayList();
      String[] var2 = moveNames;
      int var3 = moveNames.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String moveName = var2[var4];
         getAttackBase(moveName).ifPresent(attacks::add);
      }

      return attacks;
   }

   public static Optional getAttackBaseFromEnglishName(String name) {
      return Optional.ofNullable(ATTACK_MAP.get(name.toLowerCase()));
   }

   public static Optional getAttackBase(int attackIndex) {
      Iterator var1 = ATTACKS.iterator();

      AttackBase attackBase;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         attackBase = (AttackBase)var1.next();
      } while(attackBase.getAttackId() != attackIndex);

      return Optional.of(attackBase);
   }

   public static AttackBase getAttack(int attackIndex) {
      Iterator var1 = ATTACKS.iterator();

      AttackBase attackBase;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         attackBase = (AttackBase)var1.next();
      } while(attackBase.getAttackId() != attackIndex);

      return attackBase;
   }

   public static Set getAllAttackNames() {
      return Collections.unmodifiableSet(ATTACK_MAP.keySet());
   }

   public String toString() {
      return "AttackBase{attackIndex=" + this.attackIndex + ", attackName='" + this.attackName + '\'' + '}';
   }

   static {
      GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(EffectBase.class, new EffectTypeAdapter()).registerTypeAdapter(AttackAnimation.class, AttackAnimationTypeAdapter.ADAPTER).registerTypeAdapter(AttackAnimationData.class, AttackAnimationDataAdapter.ADAPTER).registerTypeAdapter(MoveFlags.class, MoveFlags.ADAPTER).registerTypeAdapter(ResourceLocation.class, ResourceLocationAdapter.ADAPTER).registerTypeAdapter(ZMove.class, new ZMoveAdapter()).create();
      ATTACK_MAP = new HashMap();
   }
}
