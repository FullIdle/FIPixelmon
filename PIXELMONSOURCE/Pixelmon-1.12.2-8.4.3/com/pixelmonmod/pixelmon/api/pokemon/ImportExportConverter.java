package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.exceptions.ShowdownImportException;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.FormData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MiniorStats;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ImportExportConverter {
   public static final String SPECIES_TEXT = "Pokémon";
   public static final String GENDER_TEXT = "Gender";
   public static final String ABILITY_TEXT = "Ability";
   public static final String LEVEL_TEXT = "Level";
   public static final String SHINY_TEXT = "Shiny";
   public static final String HAPPINESS_TEXT = "Happiness";
   public static final String EV_TEXT = "EVs";
   public static final String NATURE_TEXT = "Nature";
   public static final String IV_TEXT = "IVs";
   public static final String POKE_BALL_TEXT = "Poke Ball";
   public static final String GROWTH_TEXT = "Growth";
   public static final String CLONES_TEXT = "Clones";
   public static final String RUBY_TEXT = "Rubies";
   public static final String MINIOR_CORE_TEXT = "MiniorCore";
   public static final String SMELT_TEXT = "Smelts";
   public static final String MOVE_TEXT = "Moves";
   public static final String[] STAT_TEXT = new String[]{"hp", "Atk", "Def", "SpA", "SpD", "Spe"};
   public static final char MALE_SYMBOL = 'M';
   public static final char FEMALE_SYMBOL = 'F';
   public static final String SHINY_YES = "Yes";
   private static Map importNameMap;

   private ImportExportConverter() {
   }

   public static String getExportText(Pokemon data) {
      StringBuilder exportText = new StringBuilder();
      String exportName = ImportExportForm.getInstance().getFormName(data.getSpecies(), (short)data.getForm());
      String nickname = data.getNickname();
      if (nickname != null && !nickname.equals(exportName)) {
         exportText.append(nickname);
         exportText.append(" (");
         exportText.append(exportName);
         exportText.append(")");
      } else {
         exportText.append(exportName);
      }

      if (data.getGender() != Gender.None) {
         exportText.append(" (");
         exportText.append((char)(data.getGender() == Gender.Male ? 'M' : 'F'));
         exportText.append(")");
      }

      if (!data.getHeldItem().func_190926_b()) {
         exportText.append(" @ ");
         exportText.append(I18n.func_150826_b(data.getHeldItem().func_77977_a() + ".name"));
      }

      exportText.append("\n");
      addColonSeparated(exportText, "Ability", convertCamelCaseToWords(data.getAbilityName()));
      if (data.getLevel() != PixelmonServerConfig.maxLevel) {
         addColonSeparated(exportText, "Level", data.getLevel());
      }

      if (data.isShiny()) {
         addColonSeparated(exportText, "Shiny", data.isShiny() ? "Yes" : "NO");
      }

      if (data.getFriendship() < 255) {
         addColonSeparated(exportText, "Happiness", data.getFriendship());
      }

      writeStats(exportText, data.getEVs().getArray(), "EVs", 0);
      exportText.append(data.getBaseNature().toString());
      exportText.append(" ");
      exportText.append("Nature");
      exportText.append("\n");
      writeStats(exportText, data.getIVs().getArray(), "IVs", 31);
      if (data.getCaughtBall() != EnumPokeballs.PokeBall) {
         addColonSeparated(exportText, "Poke Ball", data.getCaughtBall());
      }

      addColonSeparated(exportText, "Growth", data.getGrowth());
      if (data.getExtraStats(MewStats.class) != null && ((MewStats)data.getExtraStats(MewStats.class)).numCloned > 0) {
         addColonSeparated(exportText, "Clones", ((MewStats)data.getExtraStats(MewStats.class)).numCloned);
      }

      if (data.getExtraStats(LakeTrioStats.class) != null && ((LakeTrioStats)data.getExtraStats(LakeTrioStats.class)).numEnchanted > 0) {
         addColonSeparated(exportText, "Rubies", ((LakeTrioStats)data.getExtraStats(LakeTrioStats.class)).numEnchanted);
      }

      if (data.getExtraStats(MeltanStats.class) != null && ((MeltanStats)data.getExtraStats(MeltanStats.class)).oresSmelted > 0) {
         addColonSeparated(exportText, "Smelts", ((MeltanStats)data.getExtraStats(MeltanStats.class)).oresSmelted);
      }

      if (data.getExtraStats(MiniorStats.class) != null) {
         addColonSeparated(exportText, "MiniorCore", ((MiniorStats)data.getExtraStats(MiniorStats.class)).color);
      }

      for(int i = 0; i < data.getMoveset().size(); ++i) {
         if (data.getMoveset().get(i) != null) {
            Attack attack = data.getMoveset().get(i);
            if (attack != null) {
               exportText.append("- ");
               exportText.append(attack.getMove().getAttackName());
               exportText.append("\n");
            }
         }
      }

      return exportText.toString();
   }

   public static void addLine(StringBuilder builder, String label) {
      builder.append(label);
      builder.append("\n");
   }

   public static void addColonSeparated(StringBuilder builder, String label, Object value) {
      builder.append(label);
      builder.append(": ");
      builder.append(value.toString());
      builder.append("\n");
   }

   private static String convertCamelCaseToWords(String text) {
      if (text != null && text.length() >= 2) {
         StringBuilder newText = new StringBuilder();
         int textLength = text.length();

         for(int i = 0; i < textLength; ++i) {
            char currentChar = text.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z' && i > 0 && i < textLength) {
               newText.append(' ');
            }

            newText.append(currentChar);
         }

         return newText.toString();
      } else {
         return text;
      }
   }

   private static void writeStats(StringBuilder exportText, int[] statArray, String statType, int defaultValue) {
      boolean defaultStats = true;
      int[] var5 = statArray;
      int i = statArray.length;

      for(int var7 = 0; var7 < i; ++var7) {
         int stat = var5[var7];
         if (stat != defaultValue) {
            defaultStats = false;
            break;
         }
      }

      if (!defaultStats) {
         exportText.append(statType);
         exportText.append(": ");
         boolean hasPrevious = false;

         for(i = 0; i < statArray.length; ++i) {
            if (statArray[i] != defaultValue) {
               if (hasPrevious) {
                  exportText.append(" / ");
               }

               exportText.append(statArray[i]);
               exportText.append(" ");
               exportText.append(STAT_TEXT[i]);
               hasPrevious = true;
            }
         }

         exportText.append("\n");
      }

   }

   public static Pokemon importText(String importText) throws ShowdownImportException {
      Gender gender = null;
      String speciesName = null;
      String nickname = "";
      int form = -2;
      int nationalPokedexNumber = true;
      Item heldItem = NoItem.noItem;
      int friendship = true;
      int[] ivs = new int[6];
      int[] evs = new int[6];
      String ability = null;
      Attack[] moves = new Attack[4];
      int numMoves = 0;
      EnumNature nature = null;
      int level = 0;
      boolean shiny = false;
      EnumGrowth growth = null;
      EnumPokeballs caughtBall = null;
      int numClones = -1;
      int miniorCore = true;
      int numEnchants = -1;
      int numSmelts = -1;
      String[] importTextSplit = importText.split("\n");

      int friendship;
      try {
         String currentLine = importTextSplit[0];
         int leftParenthesesIndex = currentLine.lastIndexOf(40);
         int genderIndex = -1;
         int rightParenthesesIndex;
         if (leftParenthesesIndex > -1) {
            rightParenthesesIndex = currentLine.indexOf(41, leftParenthesesIndex);
            String nextField = currentLine.substring(leftParenthesesIndex + 1, rightParenthesesIndex).trim();
            if (nextField.length() == 1) {
               genderIndex = leftParenthesesIndex;
               char genderChar = nextField.charAt(0);
               if (genderChar == 'M') {
                  gender = Gender.Male;
               } else {
                  if (genderChar != 'F') {
                     throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.GENDER, "" + genderChar);
                  }

                  gender = Gender.Female;
               }

               leftParenthesesIndex = currentLine.lastIndexOf(40, leftParenthesesIndex - 1);
            }
         }

         int atIndex;
         if (leftParenthesesIndex > -1) {
            nickname = currentLine.substring(0, leftParenthesesIndex).trim();
            rightParenthesesIndex = currentLine.indexOf(41, leftParenthesesIndex);
            speciesName = currentLine.substring(leftParenthesesIndex + 1, rightParenthesesIndex);
            atIndex = currentLine.indexOf(64, rightParenthesesIndex);
         } else {
            atIndex = currentLine.indexOf(64);
            if (genderIndex > -1) {
               speciesName = currentLine.substring(0, genderIndex);
            } else if (atIndex > -1) {
               speciesName = currentLine.substring(0, atIndex);
            } else {
               speciesName = currentLine;
            }

            nickname = "";
         }

         speciesName = speciesName.trim();
         Optional formDataOptional = ImportExportForm.getInstance().getFormData(speciesName);
         if (formDataOptional.isPresent()) {
            FormData formData = (FormData)formDataOptional.get();
            speciesName = formData.species.name;
            form = formData.form;
         } else {
            speciesName = speciesName.trim();
            speciesName = convertName(speciesName);
         }

         EnumSpecies species;
         BaseStats stats;
         try {
            species = EnumSpecies.getFromNameAnyCase(speciesName);
            stats = species.getBaseStats(species.getFormEnum(form));
         } catch (NullPointerException | NoSuchElementException var50) {
            throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.SPECIES, currentLine);
         }

         int nationalPokedexNumber = stats.getSpecies().getNationalPokedexInteger();
         if (atIndex > -1) {
            String heldItemName = currentLine.substring(atIndex + 1).trim();
            heldItem = PixelmonItems.getItemFromName(heldItemName);
         }

         friendship = -1;

         for(int i = 0; i < 6; ++i) {
            ivs[i] = 31;
         }

         boolean setIVs = false;
         int currentIndex = 1;

         while(true) {
            if (currentIndex >= importTextSplit.length) {
               if (ability == null) {
                  ability = species.getBaseStats().getAbilitiesArray()[0];
               }
               break;
            }

            currentLine = importTextSplit[currentIndex];
            String moveText;
            if (ability == null && currentLine.startsWith("Ability")) {
               moveText = getStringAfterColon(currentLine).replace(" ", "");
               if (moveText.equals("BattleArmor") || moveText.equals("ShellArmor")) {
                  moveText = moveText.replaceAll("o", "ou");
               }

               if (moveText.equals("BattleBond")) {
                  form = 1;
                  stats = species.getBaseStats(species.getFormEnum(form));
               }

               String[] var66 = stats.getAbilitiesArray();
               int var67 = var66.length;

               for(int var35 = 0; var35 < var67; ++var35) {
                  String testability = var66[var35];
                  if (moveText.equals(testability)) {
                     ability = moveText;
                     break;
                  }
               }

               if (ability == null) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.ABILITY, currentLine);
               }
            } else if (level == 0 && currentLine.startsWith("Level")) {
               try {
                  level = Integer.parseInt(getStringAfterColon(currentLine));
               } finally {
                  if (level <= 0 || level > PixelmonServerConfig.maxLevel) {
                     int level = false;
                     throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.LEVEL, currentLine);
                  }

               }
            } else if (!shiny && currentLine.startsWith("Shiny")) {
               moveText = getStringAfterColon(currentLine);
               if ("Yes".equals(moveText)) {
                  shiny = true;
               } else if (!"No".equals(moveText)) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.SHINY, currentLine);
               }
            } else if (friendship == -1 && currentLine.startsWith("Happiness")) {
               try {
                  friendship = Integer.parseInt(getStringAfterColon(currentLine));
               } finally {
                  if (friendship < 0 || friendship > 255) {
                     throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.FRIENDSHIP, getStringAfterColon(currentLine));
                  }

               }
            } else if (currentLine.startsWith("EVs")) {
               parseStats(currentLine, evs, (statValue, totalStats) -> {
                  int limitedStatValue = Math.max(0, Math.min(255, statValue));
                  return Math.min(limitedStatValue, 510 - totalStats);
               });
            } else if (nature == null && currentLine.trim().endsWith("Nature")) {
               moveText = currentLine.substring(0, currentLine.indexOf(32));
               nature = EnumNature.natureFromString(moveText);
               if (nature == null) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.NATURE, currentLine);
               }
            } else if (currentLine.startsWith("IVs")) {
               setIVs = true;
               parseStats(currentLine, ivs, (statValue, totalStats) -> {
                  return Math.max(0, Math.min(31, statValue));
               });
            } else if (caughtBall == null && currentLine.startsWith("Poke Ball")) {
               try {
                  caughtBall = EnumPokeballs.valueOf(getStringAfterColon(currentLine));
               } catch (IllegalArgumentException var49) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.CAUGHT_BALL, currentLine);
               }
            } else if (growth == null && currentLine.startsWith("Growth")) {
               growth = EnumGrowth.growthFromString(getStringAfterColon(currentLine));
               if (growth == null) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.GROWTH, currentLine);
               }
            } else if (currentLine.startsWith("Clones")) {
               numClones = Integer.parseInt(getStringAfterColon(currentLine));
               if (stats.getSpecies() != EnumSpecies.Mew || numClones < 0 || numClones > 3) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.CLONES, currentLine);
               }

               numClones = numClones;
            } else if (!currentLine.startsWith("Rubies")) {
               if (currentLine.startsWith("MiniorCore")) {
                  int miniorCore = Integer.parseInt(getStringAfterColon(currentLine));
                  if (stats.getSpecies() != EnumSpecies.Minior) {
                     throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.SPECIES, currentLine);
                  }

                  numEnchants = numEnchants;
               } else if (currentLine.startsWith("Smelts")) {
                  numSmelts = Integer.parseInt(getStringAfterColon(currentLine));
                  if (stats.getSpecies() != EnumSpecies.Meltan || numSmelts < 0) {
                     throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.SMELTS, currentLine);
                  }

                  numSmelts = numSmelts;
               } else if (!currentLine.isEmpty() && currentLine.charAt(0) == '-') {
                  currentLine = currentLine.trim();
                  if (currentLine.length() > 1) {
                     if (numMoves >= 4) {
                        throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.ATTACKS, currentLine);
                     }

                     moveText = currentLine.substring(1).trim();
                     moveText = convertName(moveText);
                     if (moveText.contains("Hidden Power")) {
                        if (!setIVs) {
                           String typeText = moveText.replace("Hidden Power ", "").replace("[", "").replace("]", "");
                           EnumType hiddenPowerType = EnumType.parseType(typeText);
                           if (hiddenPowerType == null) {
                              throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.ATTACKS, currentLine);
                           }

                           ivs = HiddenPower.getOptimalIVs(hiddenPowerType).getArray();
                        }

                        moveText = "Hidden Power";
                     }

                     if (!Attack.hasAttack(moveText)) {
                        throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.ATTACKS, currentLine);
                     }

                     Attack move = new Attack(moveText);
                     moves[numMoves++] = move;
                  }
               }
            } else {
               numEnchants = Integer.parseInt(getStringAfterColon(currentLine));
               if (stats.getSpecies() != EnumSpecies.Azelf && stats.getSpecies() != EnumSpecies.Mesprit && stats.getSpecies() != EnumSpecies.Uxie || numEnchants < 0 || numEnchants > PixelmonConfig.lakeTrioMaxEnchants) {
                  throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.RUBY, currentLine);
               }

               numEnchants = numEnchants;
            }

            ++currentIndex;
         }

         if (numMoves == 0) {
            throw new ShowdownImportException(ShowdownImportException.ShowdownFieldType.ATTACKS, "No moves");
         }

         if (gender == null) {
            gender = Gender.getRandomGender(stats);
         }

         if (level == 0) {
            level = PixelmonServerConfig.maxLevel;
         }

         if (friendship == -1) {
            friendship = 255;
         }

         if (nature == null) {
            nature = EnumNature.Hardy;
         }

         if (caughtBall == null) {
            caughtBall = EnumPokeballs.PokeBall;
         }

         if (growth == null) {
            growth = EnumGrowth.Ordinary;
         }
      } catch (NullPointerException | NoSuchElementException | NumberFormatException | IndexOutOfBoundsException var53) {
         var53.printStackTrace();
         return null;
      }

      Pokemon pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.getFromNameAnyCase(speciesName));
      pokemon.setForm(form);
      pokemon.setGender(gender);
      pokemon.setGrowth(growth);
      pokemon.setNickname(nickname);
      pokemon.setLevel(level);
      pokemon.setHeldItem(heldItem == null ? ItemStack.field_190927_a : new ItemStack((Item)heldItem));
      pokemon.setAbility(ability);
      pokemon.setShiny(shiny);
      pokemon.setFriendship(friendship);
      pokemon.getMoveset().clear();
      pokemon.getMoveset().addAll(Arrays.asList(moves));
      pokemon.getEVs().fillFromArray(evs);
      pokemon.getIVs().fillFromArray(ivs);
      pokemon.setNature(nature);
      if (pokemon.getSpecies() == EnumSpecies.Mew) {
         ((MewStats)pokemon.getExtraStats(MewStats.class)).numCloned = numClones;
      }

      if (pokemon.getSpecies() == EnumSpecies.Azelf || pokemon.getSpecies() == EnumSpecies.Mesprit || pokemon.getSpecies() == EnumSpecies.Uxie) {
         ((LakeTrioStats)pokemon.getExtraStats(LakeTrioStats.class)).numEnchanted = numEnchants;
      }

      if (pokemon.getSpecies() == EnumSpecies.Meltan) {
         ((MeltanStats)pokemon.getExtraStats(MeltanStats.class)).oresSmelted = numSmelts;
      }

      return pokemon;
   }

   public static int getIntAfterColon(String string) {
      return Integer.parseInt(getStringAfterColon(string));
   }

   public static String getStringAfterColon(String string) {
      return string.substring(string.indexOf(58) + 1).trim();
   }

   private static void parseStats(String statString, int[] statArray, StatValidator validator) {
      String[] splitStats = getStringAfterColon(statString).split("\\/");
      int totalStats = 0;
      String[] var5 = splitStats;
      int var6 = splitStats.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String stat = var5[var7];
         stat = stat.trim();
         String statType = stat.substring(stat.lastIndexOf(32) + 1, stat.length());

         for(int i = 0; i < STAT_TEXT.length; ++i) {
            if (STAT_TEXT[i].equalsIgnoreCase(statType)) {
               int statAmount = Integer.parseInt(stat.substring(0, stat.indexOf(32)));
               statAmount = validator.validateStat(statAmount, totalStats);
               statArray[i] = statAmount;
               totalStats += statAmount;
            }
         }
      }

   }

   private static String convertName(String nameText) {
      importNameMap = null;
      if (importNameMap == null) {
         initializeNameMap();
      }

      return importNameMap.containsKey(nameText) ? (String)importNameMap.get(nameText) : nameText;
   }

   private static void initializeNameMap() {
      importNameMap = new HashMap();
      importNameMap.put("Mime Jr.", "Mime_Jr.");
      importNameMap.put("Mr. Mime", "MrMime");
      importNameMap.put("Nidoran-F", "Nidoranfemale");
      importNameMap.put("Nidoran-M", "Nidoranmale");
      importNameMap.put("AncientPower", "Ancient Power");
      importNameMap.put("BubbleBeam", "Bubble Beam");
      importNameMap.put("DoubleSlap", "Double Slap");
      importNameMap.put("DragonBreath", "Dragon Breath");
      importNameMap.put("DynamicPunch", "Dynamic Punch");
      importNameMap.put("ExtremeSpeed", "Extreme speed");
      importNameMap.put("FeatherDance", "Feather Dance");
      importNameMap.put("Faint attack", "Feint attack");
      importNameMap.put("GrassWhistle", "Grass Whistle");
      importNameMap.put("Hi Jump Kick", "High Jump Kick");
      importNameMap.put("Sand-attack", "Sand attack");
      importNameMap.put("Selfdestruct", "Self-Destruct");
      importNameMap.put("SmellingSalt", "Smelling Salts");
      importNameMap.put("SmokeScreen", "Smokescreen");
      importNameMap.put("Softboiled", "Soft-Boiled");
      importNameMap.put("SolarBeam", "Solar Beam");
      importNameMap.put("SonicBoom", "Sonic Boom");
      importNameMap.put("ThunderShock", "Thunder Shock");
      importNameMap.put("U-Turn", "U-turn");
   }

   private interface StatValidator {
      int validateStat(int var1, int var2);
   }
}
