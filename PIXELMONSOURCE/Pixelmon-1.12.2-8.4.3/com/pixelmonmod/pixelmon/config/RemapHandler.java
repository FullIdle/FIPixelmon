package com.pixelmonmod.pixelmon.config;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen1TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen2TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen3TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen4TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen5TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen6TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen7TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class RemapHandler {
   public static ModFixs modfix = null;
   private static final List oldTMNames = Lists.newArrayList(new String[]{"Hone Claws", "Dragon Claw", "Psyshock", "Calm Mind", "Roar", "Toxic", "Hail", "Bulk Up", "Venoshock", "Hidden Power", "Sunny Day", "Taunt", "Ice Beam", "Blizzard", "Hyper Beam", "Light Screen", "Protect", "Rain Dance", "Telekinesis", "Safeguard", "Frustration", "Solar Beam", "Smack Down", "Thunderbolt", "Thunder", "Earthquake", "Return", "Dig", "Psychic", "Shadow Ball", "Brick Break", "Double Team", "Reflect", "Sludge Wave", "Flamethrower", "Sludge Bomb", "Sandstorm", "Fire Blast", "Rock Tomb", "Aerial Ace", "Torment", "Facade", "Flame Charge", "Rest", "Attract", "Thief", "Low Sweep", "Round", "Echoed Voice", "Overheat", "Ally Switch", "Focus Blast", "Energy Ball", "False Swipe", "Scald", "Fling", "Charge Beam", "Sky Drop", "Incinerate", "Quash", "Will-O-Wisp", "Acrobatics", "Embargo", "Explosion", "Shadow Claw", "Payback", "Retaliate", "Giga Impact", "Rock Polish", "Flash", "Stone Edge", "Volt Switch", "Thunder Wave", "Gyro Ball", "Swords Dance", "Struggle Bug", "Psych Up", "Bulldoze", "Frost Breath", "Rock Slide", "X-Scissor", "Dragon Tail", "Work Up", "Poison Jab", "Dream Eater", "Grass Knot", "Swagger", "Pluck", "U-turn", "Substitute", "Flash Cannon", "Trick Room", "Wild Charge", "Rock Smash", "Snarl", "Mega Punch", "Razor Wind", "Whirlwind", "Mega Kick", "Horn Drill", "Body Slam", "Take Down", "Double-Edge", "Bubble Beam", "Water Gun", "Pay Day", "Submission", "Counter", "Seismic Toss", "Rage", "Mega Drain", "Dragon Rage", "Fissure", "Teleport", "Mimic", "Bide", "Metronome", "Self-Destruct", "Egg Bomb", "Swift", "Skull Bash", "Soft-Boiled", "Sky Attack", "Psywave", "Tri Attack", "Dynamic Punch", "Headbutt", "Curse", "Rollout", "Zap Cannon", "Sweet Scent", "Snore", "Icy Wind", "Giga Drain", "Endure", "Iron Tail", "Dragon Breath", "Mud-Slap", "Ice Punch", "Sleep Talk", "Defense Curl", "Thunder Punch", "Detect", "Steel Wing", "Fire Punch", "Fury Cutter", "Nightmare", "Focus Punch", "Water Pulse", "Bullet Seed", "Shock Wave", "Secret Power", "Skill Swap", "Snatch", "Roost", "Brine", "Dragon Pulse", "Drain Punch", "Silver Wind", "Recycle", "Avalanche", "Stealth Rock", "Captivate", "Dark Pulse", "Natural Gift", "Infestation", "Nature Power", "Power-Up Punch", "Dazzling Gleam", "Confide", "Aurora Veil", "Brutal Swing", "Smart Strike", "Leech Life"});
   private static final Pattern oldTmPattern = Pattern.compile("tm([0-9]{1,3})");
   private static final Pattern gen8TmItemPattern = Pattern.compile("tm8_([0-9]{1,3})");
   private static final Pattern gen8TrItemPattern = Pattern.compile("tr8_([0-9]{1,3})");

   static void remapBlocks(List mappings) {
      Iterator var1 = mappings.iterator();

      while(var1.hasNext()) {
         RegistryEvent.MissingMappings.Mapping mapping = (RegistryEvent.MissingMappings.Mapping)var1.next();

         try {
            String name = mapping.key.toString();
            if (!name.startsWith("pixelmon:pokedoll_") && !name.startsWith("pixelmon:shinypokedoll_") && !name.matches("pixelmon:([a-z]+)_fossil_display") && !name.matches("pixelmon:castle([a-z0-9]+)") && !name.matches("pixelmon:ice([a-z0-9]+)")) {
               switch (name.substring("pixelmon:".length())) {
                  case "lightgrey_clock":
                  case "grey_clock":
                  case "lightgrey_vending_machine":
                  case "grey_vending_machine":
                  case "lightgrey_cushion_chair":
                  case "grey_cushion_chair":
                  case "lightgrey_rug":
                  case "grey_rug":
                  case "lightgrey_water_float":
                  case "grey_water_float":
                  case "lightgrey_umbrella":
                  case "grey_umbrella":
                  case "magenta_clock":
                  case "magenta_vending_machine":
                  case "magenta_cushion_chair":
                  case "magenta_rug":
                  case "magenta_water_float":
                  case "magenta_umbrella":
                  case "lime_clock":
                  case "lime_vending_machine":
                  case "lime_cushion_chair":
                  case "lime_rug":
                  case "lime_water_float":
                  case "lime_umbrella":
                  case "lightblue_clock":
                  case "lightblue_vending_machine":
                  case "lightblue_cushion_chair":
                  case "lightblue_rug":
                  case "lightblue_water_float":
                  case "lightblue_umbrella":
                  case "bluepicketfence":
                  case "tablepc":
                  case "whitepillar":
                  case "ultra_pillar":
                  case "zygardemachine":
                  case "ultra_sand":
                  case "ultra_sandstone":
                  case "celestial_altar":
                  case "ultra":
                     mapping.ignore();
                     break;
                  default:
                     switch (name) {
                        case "pixelmon:clefairy_pokedoll":
                        case "pixelmon:cyndaquil_pokedoll":
                        case "pixelmon:blastoise_pokedoll":
                        case "pixelmon:charizard_pokedoll":
                           mapping.ignore();
                           break;
                        case "pixelmon:chair":
                           mapping.remap(PixelmonBlocks.redChairBlock);
                           break;
                        default:
                           Block block = getNewBlockFromBlockName(mapping.key.toString().toLowerCase().replace(":tile.", ":").replace("_", ""));
                           if (block == null && mapping.key.toString().endsWith("Block")) {
                              block = getNewBlockFromBlockName(mapping.key.toString().substring(0, mapping.key.toString().length() - 5).toLowerCase().replace("_", ""));
                           }

                           if (block != null) {
                              mapping.remap(block);
                           }
                     }
               }
            } else {
               mapping.ignore();
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   static void remapItems(List mappings) {
      Iterator var1 = mappings.iterator();

      while(var1.hasNext()) {
         RegistryEvent.MissingMappings.Mapping mapping = (RegistryEvent.MissingMappings.Mapping)var1.next();

         try {
            String name = mapping.key.toString();
            if (!name.startsWith("pixelmon:pokedoll_") && !name.startsWith("pixelmon:shinypokedoll_") && !name.matches("pixelmon:([a-z]+)_fossil_display") && !name.matches("pixelmon:([a-z]+)_bike") && !name.matches("pixelmon:([a-z_]+)_poke_puff") && !name.matches("pixelmon:castle([a-z0-9]+)") && !name.matches("pixelmon:ice([a-z0-9_]+)") && !name.matches("pixelmon:(aether|flare|skull)_(helm|plate|legs|boots)") && !name.matches("pixelmon:tm[0-9]{1,3}") && !name.matches("pixelmon:tm8_[0-9]{1,2}") && !name.matches("pixelmon:tr8_[0-9]{1,2}")) {
               if (name.endsWith("_memory_drive")) {
                  boolean exit = false;
                  Iterator var5 = PixelmonItemsHeld.getHeldItemList().iterator();

                  while(var5.hasNext()) {
                     Item item = (Item)var5.next();
                     if (Objects.equals(item.getRegistryName().toString(), name.substring(0, name.lastIndexOf(95)))) {
                        mapping.remap(item);
                        exit = true;
                        break;
                     }
                  }

                  if (exit) {
                     continue;
                  }
               }

               switch (name.substring("pixelmon:".length())) {
                  case "heavydutyboots":
                  case "lightgrey_clock":
                  case "lightgrey_vending_machine":
                  case "lightgrey_cushion_chair":
                  case "lightgrey_rug":
                  case "lightgrey_water_float":
                  case "lightgrey_umbrella":
                  case "magenta_clock":
                  case "magenta_vending_machine":
                  case "magenta_cushion_chair":
                  case "magenta_rug":
                  case "magenta_water_float":
                  case "magenta_umbrella":
                  case "lime_clock":
                  case "lime_vending_machine":
                  case "lime_cushion_chair":
                  case "lime_rug":
                  case "lime_water_float":
                  case "lime_umbrella":
                  case "lightblue_clock":
                  case "lightblue_vending_machine":
                  case "lightblue_cushion_chair":
                  case "lightblue_rug":
                  case "lightblue_water_float":
                  case "lightblue_umbrella":
                  case "faded_blue_orb":
                  case "faded_red_orb":
                  case "green_shard":
                  case "tm175":
                  case "tm176":
                  case "tm177":
                  case "bluepicketfence":
                  case "tablepc":
                  case "whitepillar":
                  case "legend_finder":
                  case "rockstar_costume":
                  case "belle_costume":
                  case "libre_costume":
                  case "popstar_costume":
                  case "phd_costume":
                  case "yellow_shard":
                  case "ultra":
                  case "ultra_pillar":
                  case "ultra_sand":
                  case "ultra_sandstone":
                  case "celestial_altar":
                  case "dream_ball":
                  case "bike_frame":
                  case "bike_wheel":
                  case "bike_seat":
                  case "bike_handlebars":
                  case "moon_flute":
                  case "sun_flute":
                  case "pink_nectar":
                  case "purple_nectar":
                  case "red_nectar":
                  case "yellow_nectar":
                  case "meltan_box":
                  case "zygarde_cube":
                  case "zygardemachine":
                  case "searuby_badge":
                  case "coraleye_badge":
                  case "spikeshell_badge":
                  case "jadestar_badge":
                     mapping.ignore();
                     continue;
               }

               if (!name.startsWith("pixelmon:record.") && !name.startsWith("pixelmon:item.record.")) {
                  if (name.endsWith("_apricorn_tree")) {
                     mapping.ignore();
                  } else {
                     switch (name) {
                        case "pixelmon:crackedpot":
                           mapping.remap(PixelmonItems.crackedPot);
                           break;
                        case "pixelmon:chippedpot":
                           mapping.remap(PixelmonItems.chippedPot);
                           break;
                        case "pixelmon:stick":
                           mapping.remap(PixelmonItemsHeld.leek);
                           break;
                        case "pixelmon:aluminium_torso":
                           mapping.remap(PixelmonItemsTools.chestplateAluminium);
                           break;
                        case "pixelmon:chair":
                        case "pixelmon:red_chair":
                           mapping.remap(PixelmonBlocks.getItemFromBlock(PixelmonBlocks.redChairBlock));
                           break;
                        default:
                           switch (name) {
                              case "pixelmon:charizard_pokedoll":
                              case "pixelmon:blastoise_pokedoll":
                              case "pixelmon:cyndaquil_pokedoll":
                              case "pixelmon:clefairy_pokedoll":
                              case "pixelmon:rayquazaite":
                              case "pixelmon:rayquazanite":
                                 mapping.ignore();
                                 break;
                              case "pixelmon:dna_splicer":
                                 mapping.remap(PixelmonItemsHeld.dnaSplicers);
                              case "pixelmon:mewtwonitex":
                                 mapping.remap(PixelmonItemsHeld.mewtwonite_x);
                                 break;
                              case "pixelmon:mewtwonitey":
                                 mapping.remap(PixelmonItemsHeld.mewtwonite_y);
                                 break;
                              case "pixelmon:revealglass":
                                 mapping.remap(PixelmonItems.reveal_glass);
                                 break;
                              case "pixelmon:prisonbottle":
                                 mapping.remap(PixelmonItemsHeld.prison_bottle);
                                 break;
                              case "pixelmon:griseousorb":
                                 mapping.remap(PixelmonItemsHeld.griseous_orb);
                                 break;
                              default:
                                 if (!name.equals("pixelmon:PicketFence") && !name.equals("pixelmon:item.HiddenWoodenDoor") && !name.equals("pixelmon:item.HiddenIronDoor") && (!name.equals("pixelmon:tile.PokeGift") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.pokeGift");
                                 })) && (!name.equals("pixelmon:tile.trademachine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Trade_Machine");
                                 })) && (!name.equals("pixelmon:tile.cloningMachine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Cloning_Machine");
                                 })) && (!name.equals("pixelmon:tile.fossilmachine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Fossil_Machine");
                                 })) && (!name.equals("pixelmon:tile.icyrock") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:Icy_Rock");
                                 })) && (!name.equals("pixelmon:tile.mossyrock") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Mossy_Rock");
                                 })) && !name.equals("pixelmon:tile.hiddenPressurePlate") && !name.equals("pixelmon:tile.hiddenCube") && (!name.equals("pixelmon:PokeHealer") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Healer");
                                 })) && !name.equals("pixelmon:Trading_Machine") && (!name.equals("pixelmon:Articuno_Shrine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Uno_Shrine");
                                 })) && (!name.equals("pixelmon:Zapdos_Shrine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Dos_Shrine");
                                 })) && (!name.equals("pixelmon:Moltres_Shrine") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Tres_Shrine");
                                 })) && (!name.equals("pixelmon:Pixelmon_Anvil") || !mappings.stream().anyMatch((m) -> {
                                    return m.key.toString().equals("pixelmon:item.Anvil");
                                 }))) {
                                    Optional duplicate = mappings.stream().filter((m) -> {
                                       return !m.key.toString().equals(name) && m.key.toString().replace("item.", "").replace("_Block", "").replace("_", "").equalsIgnoreCase(name.replace("_Block", "").replace("_", ""));
                                    }).findFirst();
                                    if (!duplicate.isPresent()) {
                                       Item item = getNewItem(name);
                                       if (item != null) {
                                          mapping.remap(item);
                                       } else if (!name.equals("pixelmon:item.Icy_Rock")) {
                                          Block block = getNewBlockForItemBlock(name);
                                          if (block == null && name.endsWith("Block")) {
                                             block = getNewBlockFromBlockName(name.substring(0, name.length() - 5).toLowerCase().replace("_", ""));
                                          } else if (block == null) {
                                             block = getNewBlockFromBlockName(name.toLowerCase().replace(":tile.", ":").replace(":item.", ":").replace("_", ""));
                                          }

                                          if (block != null) {
                                             item = PixelmonBlocks.getItemFromBlock(block);
                                             if (item != null && item != PixelmonItems.treeItem) {
                                                mapping.remap(item);
                                             }
                                          }
                                       }
                                    }
                                 }
                           }
                     }
                  }
               } else {
                  mapping.ignore();
               }
            } else {
               mapping.ignore();
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   static void remapPotions(List mappings) {
      mappings.forEach((p) -> {
         switch (p.key.toString()) {
            case "pixelmon:luck":
            case "pixelmon:attract":
            case "pixelmon:potion.luck":
            case "pixelmon:potion.attract":
               p.ignore();
               break;
            case "pixelmon:potion.repel":
               p.remap(PixelmonPotions.repel);
         }

      });
   }

   static void remapEntities(List mappings) {
      mappings.forEach((e) -> {
         switch (e.key.toString()) {
            case "pixelmon:hallwoeen":
            case "pixelmon:halloween":
            case "pixelmon:chairmount":
            case "pixelmon:legend_finder":
            case "pixelmon:npc_groomer":
            case "pixelmon:bike":
            case "pixelmon:zygardecell":
               e.ignore();
            default:
         }
      });
   }

   public static Block getNewBlockFromBlockName(String name) {
      switch (name) {
         case "pixelmon:fossilmachineblock":
            return PixelmonBlocks.fossilMachine;
         case "pixelmon:fossilcleanerblock":
            return PixelmonBlocks.fossilCleaner;
         case "pixelmon:picketfencecross":
         case "pixelmon:picketfencecorner":
            return PixelmonBlocks.picketFenceNormalBlock;
         case "pixelmon:pokegiftblock":
            return PixelmonBlocks.pokegiftBlock;
         case "pixelmon:eventpokegift":
         case "pixelmon:pokegifteventblock":
            return PixelmonBlocks.pokegiftEventBlock;
         case "pixelmon:amethystblock":
            return PixelmonBlocks.amethystBlock;
         case "pixelmon:amethystore":
            return PixelmonBlocks.amethystOre;
         case "pixelmon:ancientpillar":
            return PixelmonBlocks.fancyPillar;
         case "pixelmon:articunoshrine":
            return PixelmonBlocks.shrineUno;
         case "pixelmon:bauxite":
            return PixelmonBlocks.bauxite;
         case "pixelmon:blueclock":
            return PixelmonBlocks.blueClockBlock;
         case "pixelmon:bluerug":
         case "pixelmon:bluepokeballrug":
            return PixelmonBlocks.blueRugBlock;
         case "pixelmon:blueumbrella":
            return PixelmonBlocks.blueUmbrellaBlock;
         case "pixelmon:bluevendingmachine":
            return PixelmonBlocks.blueVendingMachineBlock;
         case "pixelmon:bluewaterfloat":
            return PixelmonBlocks.blueWaterFloatBlock;
         case "pixelmon:bolder":
            return PixelmonBlocks.bolderBlock;
         case "pixelmon:box":
            return PixelmonBlocks.boxBlock;
         case "pixelmon:brailleblock":
            return PixelmonBlocks.blockBraille;
         case "pixelmon:brailleblock2":
            return PixelmonBlocks.blockBraille2;
         case "pixelmon:bridgeblock":
            return PixelmonBlocks.bridgeBlockBlock;
         case "pixelmon:caverock":
            return PixelmonBlocks.caveRockBlock;
         case "pixelmon:tile.cloningmachine":
         case "pixelmon:cloningmachine":
            return PixelmonBlocks.cloningMachine;
         case "pixelmon:clothtable":
         case "pixelmon:clothedtable":
            return PixelmonBlocks.clothedTableBlock;
         case "pixelmon:crystalblock":
            return PixelmonBlocks.crystalBlock;
         case "pixelmon:crystalore":
            return PixelmonBlocks.crystalOre;
         case "pixelmon:dawnduskore":
         case "pixelmon:dawnduskstoneore":
            return PixelmonBlocks.dawnduskStoneOre;
         case "pixelmon:elevator":
            return PixelmonBlocks.elevator;
         case "pixelmon:endtable":
            return PixelmonBlocks.endTableBlock;
         case "pixelmon:firestoneore":
            return PixelmonBlocks.fireStoneOre;
         case "pixelmon:fossil":
            return PixelmonBlocks.fossil;
         case "pixelmon:fossilcleaner":
            return PixelmonBlocks.fossilCleaner;
         case "pixelmon:fossilmachine":
            return PixelmonBlocks.fossilMachine;
         case "pixelmon:fossildisplay":
            return PixelmonBlocks.fossilDisplayBlock;
         case "pixelmon:fridge":
            return PixelmonBlocks.fridgeBlock;
         case "pixelmon:greenfoldingchair":
            return PixelmonBlocks.greenFoldingChairBlock;
         case "pixelmon:greenrug":
         case "pixelmon:greenpokeballrug":
            return PixelmonBlocks.greenRugBlock;
         case "pixelmon:greenumbrella":
            return PixelmonBlocks.greenUmbrellaBlock;
         case "pixelmon:greenvendingmachine":
            return PixelmonBlocks.greenVendingMachineBlock;
         case "pixelmon:greenwaterfloat":
            return PixelmonBlocks.greenWaterFloatBlock;
         case "pixelmon:gymsign":
            return PixelmonBlocks.gymSignBlock;
         case "pixelmon:hiddencube":
            return PixelmonBlocks.hiddenCube;
         case "pixelmon:hiddenirondoor":
            return PixelmonBlocks.hiddenIronDoor;
         case "pixelmon:hiddenpressureplate":
            return PixelmonBlocks.hiddenPressurePlate;
         case "pixelmon:hiddenwoodendoor":
            return PixelmonBlocks.hiddenWoodenDoor;
         case "pixelmon:ilexshrine":
            return PixelmonBlocks.shrineIlex;
         case "pixelmon:infuser":
            return PixelmonBlocks.infuser;
         case "pixelmon:insidewall":
            return PixelmonBlocks.insideWallBlock;
         case "pixelmon:insidewallmolding":
            return PixelmonBlocks.insideWallMoldingBlock;
         case "pixelmon:leafstoneore":
            return PixelmonBlocks.leafStoneOre;
         case "pixelmon:masterchest":
            return PixelmonBlocks.masterChest;
         case "pixelmon:mechanicalanvil":
            return PixelmonBlocks.mechanicalAnvil;
         case "pixelmon:moltresshrine":
            return PixelmonBlocks.shrineTres;
         case "pixelmon:movementplate":
            return PixelmonBlocks.movementPlate;
         case "pixelmon:orangevendingmachine":
            return PixelmonBlocks.orangeVendingMachineBlock;
         case "pixelmon:orangewaterfloat":
            return PixelmonBlocks.orangeWaterFloatBlock;
         case "pixelmon:outsidewall":
            return PixelmonBlocks.outsideWallBlock;
         case "pixelmon:pc":
            return PixelmonBlocks.pc;
         case "pixelmon:picketfencenormal":
            return PixelmonBlocks.picketFenceNormalBlock;
         case "pixelmon:pinkclock":
            return PixelmonBlocks.pinkClockBlock;
         case "pixelmon:pinkvendingmachine":
            return PixelmonBlocks.pinkVendingMachineBlock;
         case "pixelmon:pinkwaterfloat":
            return PixelmonBlocks.pinkWaterFloatBlock;
         case "pixelmon:pixelmonanvil":
            return PixelmonBlocks.anvil;
         case "pixelmon:pixelmonspawner":
            return PixelmonBlocks.pixelmonSpawner;
         case "pixelmon:spawnercaverock":
            return PixelmonBlocks.caveRockBlock;
         case "pixelmon:pixelmongrass":
            return PixelmonBlocks.pixelmonGrassBlock;
         case "pixelmon:pokechest":
            return PixelmonBlocks.pokeChest;
         case "pixelmon:pokecentersign":
            return PixelmonBlocks.pokeCenterSignBlock;
         case "pixelmon:pokegrass":
            return PixelmonBlocks.pokeGrassBlock;
         case "pixelmon:pokedirt":
            return PixelmonBlocks.pokeDirtBlock;
         case "pixelmon:pokegift":
            return PixelmonBlocks.pokegiftBlock;
         case "pixelmon:pokegiftevent":
            return PixelmonBlocks.pokegiftEventBlock;
         case "pixelmon:pokehealer":
            return PixelmonBlocks.healer;
         case "pixelmon:pokemartsign":
            return PixelmonBlocks.pokeMartSignBlock;
         case "pixelmon:pokesand":
            return PixelmonBlocks.pokeSandBlock;
         case "pixelmon:pokesandc1":
            return PixelmonBlocks.pokeSandCorner1Block;
         case "pixelmon:pokesandc2":
            return PixelmonBlocks.pokeSandCorner2Block;
         case "pixelmon:pokesandc3":
            return PixelmonBlocks.pokeSandCorner3Block;
         case "pixelmon:pokesandc4":
            return PixelmonBlocks.pokeSandCorner4Block;
         case "pixelmon:pokesands1":
            return PixelmonBlocks.pokeSandSide1Block;
         case "pixelmon:pokesands2":
            return PixelmonBlocks.pokeSandSide2Block;
         case "pixelmon:pokesands3":
            return PixelmonBlocks.pokeSandSide3Block;
         case "pixelmon:pokesands4":
            return PixelmonBlocks.pokeSandSide4Block;
         case "pixelmon:purplewaterfloat":
            return PixelmonBlocks.purpleWaterFloatBlock;
         case "pixelmon:ranch":
            return PixelmonBlocks.ranchBlock;
         case "pixelmon:redcushionchair":
            return PixelmonBlocks.redCushionChairBlock;
         case "pixelmon:redrug":
         case "pixelmon:redpokeballrug":
            return PixelmonBlocks.redRugBlock;
         case "pixelmon:redumbrella":
            return PixelmonBlocks.redUmbrellaBlock;
         case "pixelmon:redvendingmachine":
            return PixelmonBlocks.redVendingMachineBlock;
         case "pixelmon:redwaterfloat":
            return PixelmonBlocks.redWaterFloatBlock;
         case "pixelmon:rock":
            return PixelmonBlocks.rockBlock;
         case "pixelmon:rubyblock":
            return PixelmonBlocks.rubyBlock;
         case "pixelmon:rubyore":
            return PixelmonBlocks.rubyOre;
         case "pixelmon:sandygrass":
            return PixelmonBlocks.sandyGrassBlock;
         case "pixelmon:sapphireblock":
            return PixelmonBlocks.sapphireBlock;
         case "pixelmon:sapphireore":
            return PixelmonBlocks.sapphireOre;
         case "pixelmon:shingles":
            return PixelmonBlocks.shinglesBlock;
         case "pixelmon:shinglesc1":
            return PixelmonBlocks.shinglesCorner1Block;
         case "pixelmon:shinglesc2":
            return PixelmonBlocks.shinglesCorner2Block;
         case "pixelmon:shrineblock":
            return PixelmonBlocks.templeBlock;
         case "pixelmon:shrinebrick":
            return PixelmonBlocks.templeBrick;
         case "pixelmon:shrinebrickstairs":
            return PixelmonBlocks.templeBrickStairs;
         case "pixelmon:shrinestairs":
            return PixelmonBlocks.templeStairs;
         case "pixelmon:ruinswall":
            return PixelmonBlocks.ruinsWallBlock;
         case "pixelmon:dustyruinswall":
            return PixelmonBlocks.dustyRuinsWallBlock;
         case "pixelmon:siliconore":
            return PixelmonBlocks.siliconOre;
         case "pixelmon:stickplate":
            return PixelmonBlocks.stickPlate;
         case "pixelmon:sunstoneore":
            return PixelmonBlocks.sunStoneOre;
         case "pixelmon:thunderstoneore":
            return PixelmonBlocks.thunderStoneOre;
         case "pixelmon:icyrock":
            return PixelmonBlocks.icyRock;
         case "pixelmon:mossyrock":
            return PixelmonBlocks.mossyRock;
         case "pixelmon:trademachine":
         case "pixelmon:tradingmachine":
            return PixelmonBlocks.tradeMachine;
         case "pixelmon:timedfall":
            return PixelmonBlocks.timedFall;
         case "pixelmon:trashcan":
            return PixelmonBlocks.trashcanBlock;
         case "pixelmon:tree":
            return PixelmonBlocks.treeBlock;
         case "pixelmon:treebottom":
            return PixelmonBlocks.treeBottomBlock;
         case "pixelmon:treetop":
            return PixelmonBlocks.treeTopBlock;
         case "pixelmon:tv":
            return PixelmonBlocks.tvBlock;
         case "pixelmon:ultrachest":
            return PixelmonBlocks.ultraChest;
         case "pixelmon:unownblock":
            return PixelmonBlocks.blockUnown;
         case "pixelmon:unownblock2":
            return PixelmonBlocks.blockUnown2;
         case "pixelmon:warpplate":
            return PixelmonBlocks.warpPlate;
         case "pixelmon:waterstoneore":
            return PixelmonBlocks.waterStoneOre;
         case "pixelmon:window1":
            return PixelmonBlocks.window1Block;
         case "pixelmon:window2":
            return PixelmonBlocks.window2Block;
         case "pixelmon:woodenflooring":
            return PixelmonBlocks.woodenFlooringBlock;
         case "pixelmon:yellowcushionchair":
            return PixelmonBlocks.yellowCushionChairBlock;
         case "pixelmon:yellowrug":
         case "pixelmon:yellowpokeballrug":
            return PixelmonBlocks.yellowRugBlock;
         case "pixelmon:yellowumbrella":
            return PixelmonBlocks.yellowUmbrellaBlock;
         case "pixelmon:yellowvendingmachine":
            return PixelmonBlocks.yellowVendingMachineBlock;
         case "pixelmon:yellowwaterfloat":
            return PixelmonBlocks.yellowWaterFloatBlock;
         case "pixelmon:zapdosshrine":
            return PixelmonBlocks.shrineDos;
         default:
            return null;
      }
   }

   public static Block getNewBlockForItemBlock(String name) {
      switch (name) {
         case "pixelmon:AmethystBlock":
            return PixelmonBlocks.amethystBlock;
         case "pixelmon:AmethystOre":
            return PixelmonBlocks.amethystOre;
         case "pixelmon:Ancient_Pillar":
            return PixelmonBlocks.fancyPillar;
         case "pixelmon:Bauxite":
            return PixelmonBlocks.bauxite;
         case "pixelmon:BlueClock":
            return PixelmonBlocks.blueClockBlock;
         case "pixelmon:item.BluePokeballRug":
            return PixelmonBlocks.blueRugBlock;
         case "pixelmon:BlueUmbrella":
            return PixelmonBlocks.blueUmbrellaBlock;
         case "pixelmon:item.BlueVendingMachine":
            return PixelmonBlocks.blueVendingMachineBlock;
         case "pixelmon:item.BlueWaterFloat":
            return PixelmonBlocks.blueWaterFloatBlock;
         case "pixelmon:Bolder":
            return PixelmonBlocks.bolderBlock;
         case "pixelmon:Box":
            return PixelmonBlocks.boxBlock;
         case "pixelmon:BrailleBlock":
            return PixelmonBlocks.blockBraille;
         case "pixelmon:BrailleBlock2":
            return PixelmonBlocks.blockBraille2;
         case "pixelmon:BridgeBlock":
            return PixelmonBlocks.bridgeBlockBlock;
         case "pixelmon:CaveRock":
            return PixelmonBlocks.caveRockBlock;
         case "pixelmon:redChair":
            return PixelmonBlocks.redChairBlock;
         case "pixelmon:yellowChair":
            return PixelmonBlocks.yellowChairBlock;
         case "pixelmon:orangeChair":
            return PixelmonBlocks.orangeChairBlock;
         case "pixelmon:pinkChair":
            return PixelmonBlocks.pinkChairBlock;
         case "pixelmon:purpleChair":
            return PixelmonBlocks.purpleChairBlock;
         case "pixelmon:whiteChair":
            return PixelmonBlocks.whiteChairBlock;
         case "pixelmon:grayChair":
            return PixelmonBlocks.grayChairBlock;
         case "pixelmon:blackChair":
            return PixelmonBlocks.blackChairBlock;
         case "pixelmon:greenChair":
            return PixelmonBlocks.greenChairBlock;
         case "pixelmon:brownChair":
            return PixelmonBlocks.brownChairBlock;
         case "pixelmon:cyanChair":
            return PixelmonBlocks.cyanChairBlock;
         case "pixelmon:blueChair":
            return PixelmonBlocks.blueChairBlock;
         case "pixelmon:item.Cloning_Machine":
            return PixelmonBlocks.cloningMachine;
         case "pixelmon:item.ClothedTable":
            return PixelmonBlocks.clothedTableBlock;
         case "pixelmon:CrystalBlock":
            return PixelmonBlocks.crystalBlock;
         case "pixelmon:CrystalOre":
            return PixelmonBlocks.crystalOre;
         case "pixelmon:DawnDuskstone_Ore":
            return PixelmonBlocks.dawnduskStoneOre;
         case "pixelmon:Elevator":
            return PixelmonBlocks.elevator;
         case "pixelmon:EndTable":
            return PixelmonBlocks.endTableBlock;
         case "pixelmon:Firestone_Ore":
            return PixelmonBlocks.fireStoneOre;
         case "pixelmon:Fossil":
            return PixelmonBlocks.fossil;
         case "pixelmon:item.Fossil_Cleaner":
            return PixelmonBlocks.fossilCleaner;
         case "pixelmon:item.FossilDisplay":
            return PixelmonBlocks.fossilDisplayBlock;
         case "pixelmon:item.Fossil_Machine":
            return PixelmonBlocks.fossilMachine;
         case "pixelmon:item.Fridge":
            return PixelmonBlocks.fridgeBlock;
         case "pixelmon:GreenFoldingChair":
            return PixelmonBlocks.greenFoldingChairBlock;
         case "pixelmon:item.GreenPokeballRug":
            return PixelmonBlocks.greenRugBlock;
         case "pixelmon:GreenUmbrella":
            return PixelmonBlocks.greenUmbrellaBlock;
         case "pixelmon:item.GreenVendingMachine":
            return PixelmonBlocks.greenVendingMachineBlock;
         case "pixelmon:item.GreenWaterFloat":
            return PixelmonBlocks.greenWaterFloatBlock;
         case "pixelmon:GymSign":
            return PixelmonBlocks.gymSignBlock;
         case "pixelmon:item.Anvil":
            return PixelmonBlocks.anvil;
         case "pixelmon:item.HiddenCube":
            return PixelmonBlocks.hiddenCube;
         case "pixelmon:item.IlexShrine":
            return PixelmonBlocks.shrineIlex;
         case "pixelmon:item.Infuser":
            return PixelmonBlocks.infuser;
         case "pixelmon:Hidden_Iron_Door":
            return PixelmonBlocks.hiddenIronDoor;
         case "pixelmon:item.HiddenPressurePlate":
            return PixelmonBlocks.hiddenPressurePlate;
         case "pixelmon:Hidden_Wooden_Door":
            return PixelmonBlocks.hiddenWoodenDoor;
         case "pixelmon:InsideWall":
            return PixelmonBlocks.insideWallBlock;
         case "pixelmon:InsideWallMolding":
            return PixelmonBlocks.insideWallMoldingBlock;
         case "pixelmon:Icy_Rock":
            return PixelmonBlocks.icyRock;
         case "pixelmon:Leafstone_Ore":
            return PixelmonBlocks.leafStoneOre;
         case "pixelmon:item.masterChest":
            return PixelmonBlocks.masterChest;
         case "pixelmon:item.MechanicalAnvil":
            return PixelmonBlocks.mechanicalAnvil;
         case "pixelmon:item.Mossy_Rock":
            return PixelmonBlocks.mossyRock;
         case "pixelmon:item.OrangeVendingMachine":
            return PixelmonBlocks.orangeVendingMachineBlock;
         case "pixelmon:item.OrangeWaterFloat":
            return PixelmonBlocks.orangeWaterFloatBlock;
         case "pixelmon:item.PC":
            return PixelmonBlocks.pc;
         case "pixelmon:item.PinkVendingMachine":
            return PixelmonBlocks.pinkVendingMachineBlock;
         case "pixelmon:item.PinkWaterFloat":
            return PixelmonBlocks.pinkWaterFloatBlock;
         case "pixelmon:item.pokeChest":
            return PixelmonBlocks.pokeChest;
         case "pixelmon:item.pokeGift":
            return PixelmonBlocks.pokegiftBlock;
         case "pixelmon:item.PurpleWaterFloat":
            return PixelmonBlocks.purpleWaterFloatBlock;
         case "pixelmon:item.Ranch_Block":
            return PixelmonBlocks.ranchBlock;
         case "pixelmon:item.RedPokeballRug":
            return PixelmonBlocks.redRugBlock;
         case "pixelmon:item.RedVendingMachine":
            return PixelmonBlocks.redVendingMachineBlock;
         case "pixelmon:item.RedWaterFloat":
            return PixelmonBlocks.redWaterFloatBlock;
         case "pixelmon:item.Trade_Machine":
            return PixelmonBlocks.tradeMachine;
         case "pixelmon:item.Tree":
            return PixelmonBlocks.treeBlock;
         case "pixelmon:item.Tres_Shrine":
            return PixelmonBlocks.shrineTres;
         case "pixelmon:item.TV":
            return PixelmonBlocks.tvBlock;
         case "pixelmon:item.ultraChest":
            return PixelmonBlocks.ultraChest;
         case "pixelmon:item.Uno_Shrine":
            return PixelmonBlocks.shrineUno;
         case "pixelmon:item.YellowPokeballRug":
            return PixelmonBlocks.yellowRugBlock;
         case "pixelmon:item.YellowVendingMachine":
            return PixelmonBlocks.yellowVendingMachineBlock;
         case "pixelmon:item.YellowWaterFloat":
            return PixelmonBlocks.yellowWaterFloatBlock;
         case "pixelmon:MovementPlate":
            return PixelmonBlocks.movementPlate;
         case "pixelmon:OutsideWall":
            return PixelmonBlocks.outsideWallBlock;
         case "pixelmon:PicketFenceNormal":
            return PixelmonBlocks.picketFenceNormalBlock;
         case "pixelmon:PinkClock":
            return PixelmonBlocks.pinkClockBlock;
         case "pixelmon:Pixelmon_Spawner":
            return PixelmonBlocks.pixelmonSpawner;
         case "pixelmon:SpawnerCaveRock":
            return PixelmonBlocks.caveRockBlock;
         case "pixelmon:PixelmonGrass":
            return PixelmonBlocks.pixelmonGrassBlock;
         case "pixelmon:PokeCenterSign":
            return PixelmonBlocks.pokeCenterSignBlock;
         case "pixelmon:PokeGrass":
            return PixelmonBlocks.pokeGrassBlock;
         case "pixelmon:PokeDirt":
            return PixelmonBlocks.pokeDirtBlock;
         case "pixelmon:tile.EventPokeGift":
            return PixelmonBlocks.pokegiftEventBlock;
         case "pixelmon:item.Healer":
            return PixelmonBlocks.healer;
         case "pixelmon:PokeMartSign":
            return PixelmonBlocks.pokeMartSignBlock;
         case "pixelmon:PokeSand":
            return PixelmonBlocks.pokeSandBlock;
         case "pixelmon:PokeSandC1":
            return PixelmonBlocks.pokeSandCorner1Block;
         case "pixelmon:PokeSandC2":
            return PixelmonBlocks.pokeSandCorner2Block;
         case "pixelmon:PokeSandC3":
            return PixelmonBlocks.pokeSandCorner3Block;
         case "pixelmon:PokeSandC4":
            return PixelmonBlocks.pokeSandCorner4Block;
         case "pixelmon:PokeSandS1":
            return PixelmonBlocks.pokeSandSide1Block;
         case "pixelmon:PokeSandS2":
            return PixelmonBlocks.pokeSandSide2Block;
         case "pixelmon:PokeSandS3":
            return PixelmonBlocks.pokeSandSide3Block;
         case "pixelmon:PokeSandS4":
            return PixelmonBlocks.pokeSandSide4Block;
         case "pixelmon:RedCushionChair":
            return PixelmonBlocks.redCushionChairBlock;
         case "pixelmon:RedUmbrella":
            return PixelmonBlocks.redUmbrellaBlock;
         case "pixelmon:Rock":
            return PixelmonBlocks.rockBlock;
         case "pixelmon:RubyBlock":
            return PixelmonBlocks.rubyBlock;
         case "pixelmon:RubyOre":
            return PixelmonBlocks.rubyOre;
         case "pixelmon:SandyGrass":
            return PixelmonBlocks.sandyGrassBlock;
         case "pixelmon:SapphireBlock":
            return PixelmonBlocks.sapphireBlock;
         case "pixelmon:SapphireOre":
            return PixelmonBlocks.sapphireOre;
         case "pixelmon:Shingles":
            return PixelmonBlocks.shinglesBlock;
         case "pixelmon:ShinglesC1":
            return PixelmonBlocks.shinglesCorner1Block;
         case "pixelmon:ShinglesC2":
            return PixelmonBlocks.shinglesCorner2Block;
         case "pixelmon:ShrineBlock":
            return PixelmonBlocks.templeBlock;
         case "pixelmon:ShrineBrick":
            return PixelmonBlocks.templeBrick;
         case "pixelmon:ShrineBrickStairs":
            return PixelmonBlocks.templeBrickStairs;
         case "pixelmon:ShrineStairs":
            return PixelmonBlocks.templeStairs;
         case "pixelmon:RuinsWall":
            return PixelmonBlocks.ruinsWallBlock;
         case "pixelmon:DustyRuinsWall":
            return PixelmonBlocks.dustyRuinsWallBlock;
         case "pixelmon:SiliconOre":
            return PixelmonBlocks.siliconOre;
         case "pixelmon:StickPlate":
            return PixelmonBlocks.stickPlate;
         case "pixelmon:Sun_Stone_Ore":
            return PixelmonBlocks.sunStoneOre;
         case "pixelmon:Thunderstone_Ore":
            return PixelmonBlocks.thunderStoneOre;
         case "pixelmon:TimedFall":
            return PixelmonBlocks.timedFall;
         case "pixelmon:Trashcan":
            return PixelmonBlocks.trashcanBlock;
         case "pixelmon:TreeBottom":
            return PixelmonBlocks.treeBottomBlock;
         case "pixelmon:TreeTop":
            return PixelmonBlocks.treeTopBlock;
         case "pixelmon:UnownBlock":
            return PixelmonBlocks.blockUnown;
         case "pixelmon:UnownBlock2":
            return PixelmonBlocks.blockUnown2;
         case "pixelmon:WarpPlate":
            return PixelmonBlocks.warpPlate;
         case "pixelmon:Waterstone_Ore":
            return PixelmonBlocks.waterStoneOre;
         case "pixelmon:Window1":
            return PixelmonBlocks.window1Block;
         case "pixelmon:Window2":
            return PixelmonBlocks.window2Block;
         case "pixelmon:WoodenFlooring":
            return PixelmonBlocks.woodenFlooringBlock;
         case "pixelmon:YellowCushionChair":
            return PixelmonBlocks.yellowCushionChairBlock;
         case "pixelmon:YellowUmbrella":
            return PixelmonBlocks.yellowUmbrellaBlock;
         case "pixelmon:item.Dos_Shrine":
            return PixelmonBlocks.shrineDos;
         default:
            return null;
      }
   }

   private static Item getNewItem(String name) {
      if ("pixelmon:fig_berry".equals(name)) {
         return PixelmonItemsHeld.figyBerry;
      } else {
         switch (name) {
            case "pixelmon:item.IcyRock":
               return PixelmonItemsHeld.icyRock;
            case "pixelmon:item.spawnGrotto":
            case "pixelmon:spawn_grotto":
               return PixelmonItems.grottoSpawner;
            default:
               String itemName;
               int var3;
               int var4;
               Field field;
               Field[] var12;
               if (name.matches("pixelmon:item\\..*_Ball")) {
                  itemName = name.substring("pixelmon:item.".length()).toLowerCase();
                  var12 = PixelmonItemsPokeballs.class.getFields();
                  var3 = var12.length;

                  for(var4 = 0; var4 < var3; ++var4) {
                     field = var12[var4];

                     try {
                        if (field.get((Object)null) instanceof ItemPokeball) {
                           Item item = (ItemPokeball)field.get((Object)null);
                           if (item.getRegistryName().func_110623_a().equals(itemName)) {
                              return item;
                           }
                        }
                     } catch (IllegalAccessException var8) {
                        var8.printStackTrace();
                     }
                  }
               }

               Item item;
               if (name.matches("pixelmon:item.*Badge")) {
                  itemName = name.substring("pixelmon:item.".length()).toLowerCase();

                  try {
                     var12 = PixelmonItemsBadges.class.getFields();
                     var3 = var12.length;

                     for(var4 = 0; var4 < var3; ++var4) {
                        field = var12[var4];
                        if (field.get((Object)null) instanceof Item) {
                           item = (Item)field.get((Object)null);
                           if (item.getRegistryName().func_110623_a().equals(itemName)) {
                              return item;
                           }
                        }
                     }
                  } catch (Exception var11) {
                     var11.printStackTrace();
                  }
               }

               if (name.matches("pixelmon:item.*Apricorn")) {
                  itemName = name.substring("pixelmon:item.".length()).toLowerCase();

                  try {
                     var12 = PixelmonItemsApricorns.class.getFields();
                     var3 = var12.length;

                     for(var4 = 0; var4 < var3; ++var4) {
                        field = var12[var4];
                        if (field.get((Object)null) instanceof Item) {
                           item = (Item)field.get((Object)null);
                           if (item.getRegistryName().func_110623_a().equals(itemName)) {
                              return item;
                           }
                        }
                     }
                  } catch (Exception var10) {
                     var10.printStackTrace();
                  }
               }

               if (name.startsWith("pixelmon:item.coveredFossil")) {
                  itemName = "covered_fossil_" + name.substring("pixelmon:item.coveredFossil".length());
                  var12 = PixelmonItemsFossils.class.getFields();
                  var3 = var12.length;

                  for(var4 = 0; var4 < var3; ++var4) {
                     field = var12[var4];

                     try {
                        if (field.get((Object)null) instanceof ItemCoveredFossil) {
                           ItemCoveredFossil item = (ItemCoveredFossil)field.get((Object)null);
                           if (item.getRegistryName().func_110623_a().equals(itemName)) {
                              return item;
                           }
                        }
                     } catch (IllegalAccessException var7) {
                        var7.printStackTrace();
                     }
                  }
               }

               if (name.startsWith("pixelmon:PokeMail-")) {
                  itemName = name.substring("pixelmon:PokeMail-".length());
                  return (Item)PixelmonItemsMail.items.stream().filter((c) -> {
                     return c.getRegistryName().func_110623_a().equals("pokemail_" + itemName);
                  }).findFirst().get();
               } else if (name.toLowerCase().contains("icy_rock")) {
                  return null;
               } else if (name.toLowerCase().contains("icyrock")) {
                  return null;
               } else {
                  itemName = name.replace(":item.", ":");
                  if (itemName.startsWith("pixelmon:")) {
                     itemName = itemName.substring("pixelmon:".length());
                  }

                  switch (itemName) {
                     case "ranchupgrade":
                        return PixelmonItems.ranchUpgrade;
                     case "King's_Rock":
                        return PixelmonItemsHeld.kingsRock;
                     case "Fossil_Machine_Tank":
                        return PixelmonItemsFossils.fossilMachineTank;
                     case "Fossil_Machine_Display":
                        return PixelmonItemsFossils.fossilMachineDisplay;
                     case "Fossil_Machine_Top":
                        return PixelmonItemsFossils.fossilMachineTop;
                     case "Fossil_Machine_Base":
                        return PixelmonItemsFossils.fossilMachineBase;
                     case "hourglasssilver":
                        return PixelmonItems.hourglassSilver;
                     case "hourglassgold":
                        return PixelmonItems.hourglassGold;
                     case "Exp._Share":
                        return PixelmonItemsHeld.expShare;
                     case "EntityPixelmonPaintingItem":
                     case "painting_item":
                        return PixelmonItems.pixelmonPaintingItem;
                     default:
                        if (!itemName.contains("Hidden_Iron_Door") && !itemName.contains("HiddenIronDoor") && !itemName.contains("Hidden_Wooden_Door") && !itemName.contains("HiddenWoodenDoor") && !itemName.contains("Hidden_Pressure_Plate") && !itemName.contains("Hidden_Cube") && !itemName.contains("Tree")) {
                           if (itemName.contains("-")) {
                              itemName = itemName.toLowerCase();
                           }

                           if (itemName.contains(" ")) {
                              itemName = itemName.replace(" ", "_").toLowerCase();
                           }

                           if (!itemName.contains("_")) {
                              itemName = toSnakeCase(itemName);
                              itemName = itemName.replace("stone", "_stone");
                           }

                           itemName = itemName.toLowerCase();

                           try {
                              var12 = PixelmonItemsHeld.class.getFields();
                              var3 = var12.length;

                              for(var4 = 0; var4 < var3; ++var4) {
                                 field = var12[var4];
                                 if (field.get((Object)null) instanceof Item) {
                                    item = (Item)field.get((Object)null);
                                    if (item.getRegistryName().func_110623_a().equals(itemName)) {
                                       return item;
                                    }
                                 }
                              }

                              var12 = PixelmonItemsTools.class.getFields();
                              var3 = var12.length;

                              for(var4 = 0; var4 < var3; ++var4) {
                                 field = var12[var4];
                                 if (field.get((Object)null) instanceof Item) {
                                    item = (Item)field.get((Object)null);
                                    if (item.getRegistryName().func_110623_a().equals(itemName)) {
                                       return item;
                                    }
                                 }
                              }

                              var12 = PixelmonItemsPokeballs.class.getFields();
                              var3 = var12.length;

                              PixelmonItem item;
                              for(var4 = 0; var4 < var3; ++var4) {
                                 field = var12[var4];
                                 if (field.get((Object)null) instanceof PixelmonItem) {
                                    item = (PixelmonItem)field.get((Object)null);
                                    if (item.getRegistryName().func_110623_a().equals(itemName)) {
                                       return item;
                                    }
                                 }
                              }

                              var12 = PixelmonItems.class.getFields();
                              var3 = var12.length;

                              for(var4 = 0; var4 < var3; ++var4) {
                                 field = var12[var4];
                                 if (field.get((Object)null) instanceof Item) {
                                    item = (Item)field.get((Object)null);
                                    if (item.getRegistryName() != null && item.getRegistryName().func_110623_a().equals(itemName)) {
                                       return item;
                                    }
                                 }
                              }

                              var12 = PixelmonItemsFossils.class.getFields();
                              var3 = var12.length;

                              for(var4 = 0; var4 < var3; ++var4) {
                                 field = var12[var4];
                                 if (field.get((Object)null) instanceof PixelmonItem) {
                                    item = (PixelmonItem)field.get((Object)null);
                                    if (item.getRegistryName().func_110623_a().equals(itemName)) {
                                       return item;
                                    }
                                 }
                              }
                           } catch (Exception var9) {
                              var9.printStackTrace();
                           }

                           return null;
                        } else {
                           return null;
                        }
                  }
               }
         }
      }
   }

   private static String toSnakeCase(String text) {
      return StringUtils.join(text.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"), "_");
   }

   public static void attemptHackyFixForRipOffPixelmonMappingIssues(MinecraftServer server) {
      File file1 = server.func_71254_M().func_186352_b(server.func_71270_I(), "level.dat");

      try {
         NBTTagCompound compound = CompressedStreamTools.func_74796_a(new FileInputStream(file1));
         NBTTagCompound dataVersions = compound.func_74775_l("Data").func_74775_l("ForgeDataVersion");
         NBTTagList modList = compound.func_74775_l("FML").func_150295_c("ModList", 10);
         String oldPixelmonVer = null;
         Iterator var6 = modList.iterator();

         NBTTagCompound itemsReg;
         while(var6.hasNext()) {
            NBTBase base = (NBTBase)var6.next();
            itemsReg = (NBTTagCompound)base;
            if (itemsReg.func_74779_i("ModId").equals("pixelmon")) {
               oldPixelmonVer = itemsReg.func_74779_i("ModVersion");
            }
         }

         NBTTagCompound entry;
         NBTTagCompound registries;
         Iterator i;
         if (oldPixelmonVer != null && (oldPixelmonVer.startsWith("2") || oldPixelmonVer.startsWith("3"))) {
            if (modfix == null) {
               modfix = FMLCommonHandler.instance().getDataFixer().init("pixelmon", 9);
            }

            FileUtils.copyFile(file1, server.func_71254_M().func_186352_b(server.func_71270_I(), "level-pixelmonBackup.dat"));
            registries = compound.func_74775_l("FML").func_74775_l("Registries");
            NBTTagCompound blocksReg = registries.func_74775_l("minecraft:blocks");
            Iterator var18 = blocksReg.func_150295_c("ids", 10).iterator();

            String id;
            while(var18.hasNext()) {
               NBTBase entry = (NBTBase)var18.next();
               entry = (NBTTagCompound)entry;
               String id = entry.func_74779_i("K");
               if (id.startsWith("pixelmon:")) {
                  id = id.substring("pixelmon:".length());
                  if (id.startsWith("grey")) {
                     entry.func_74778_a("K", id.replace("grey", "gray"));
                  }
               }
            }

            itemsReg = registries.func_74775_l("minecraft:items");
            i = itemsReg.func_150295_c("ids", 10).iterator();

            while(i.hasNext()) {
               NBTBase entry = (NBTBase)i.next();
               NBTTagCompound item = (NBTTagCompound)entry;
               id = item.func_74779_i("K");
               if (id.startsWith("pixelmon:")) {
                  String name = id.substring("pixelmon:".length());
                  if (name.endsWith("_memory_drive")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("_memory_drive", "_memory") + " generations conversion");
                     item.func_74778_a("K", id.replace("_memory_drive", "_memory"));
                  } else if (name.equals("faded_blue_orb")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("faded_blue_orb", "blue_orb") + " generations conversion");
                     item.func_74778_a("K", id.replace("faded_blue_orb", "blue_orb"));
                  } else if (name.equals("faded_red_orb")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("faded_red_orb", "red_orb") + " generations conversion");
                     item.func_74778_a("K", id.replace("faded_red_orb", "red_orb"));
                  } else if (name.equals("green_shard")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("green_shard", "jade_shard") + " generations conversion");
                     item.func_74778_a("K", id.replace("green_shard", "jade_shard"));
                  } else if (name.startsWith("grey")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("grey", "gray") + " generations conversion");
                     item.func_74778_a("K", id.replace("grey", "gray"));
                  } else if (name.equals("chair")) {
                     Pixelmon.LOGGER.warn("Renaming " + id + " to " + id.replace("chair", "red_chair") + " generations conversion");
                     item.func_74778_a("K", id.replace("chair", "red_chair"));
                  }
               }
            }

            CompressedStreamTools.func_74799_a(compound, new FileOutputStream(file1));
         }

         if (modfix != null || dataVersions.func_74764_b("pixelmon")) {
            if (modfix == null) {
               modfix = FMLCommonHandler.instance().getDataFixer().init("pixelmon", 9);
            }

            modfix.registerFix(FixTypes.ITEM_INSTANCE, new IFixableData() {
               public int func_188216_a() {
                  return 2;
               }

               public NBTTagCompound func_188217_a(NBTTagCompound item) {
                  String id = item.func_74779_i("id");
                  if (!id.startsWith("pixelmon:")) {
                     return item;
                  } else {
                     String name = id.substring("pixelmon:".length());
                     if (name.endsWith("_memory_drive")) {
                        item.func_74778_a("id", id.replace("_memory_drive", "_memory"));
                     }

                     if (name.endsWith("_fossil_display")) {
                        item.func_74778_a("id", "pixelmon:fossil_display");
                     }

                     if (name.equals("tm175")) {
                        item.func_74778_a("id", id.replace("tm175", "hm2"));
                     }

                     if (name.equals("tm176")) {
                        item.func_74778_a("id", id.replace("tm176", "hm3"));
                     }

                     if (name.equals("tm177")) {
                        item.func_74778_a("id", id.replace("tm177", "hm7"));
                     }

                     if (name.equals("faded_blue_orb")) {
                        item.func_74778_a("id", id.replace("faded_blue_orb", "blue_orb"));
                     }

                     if (name.equals("faded_red_orb")) {
                        item.func_74778_a("id", id.replace("faded_red_orb", "red_orb"));
                     }

                     if (name.equals("green_shard")) {
                        item.func_74778_a("id", id.replace("green_shard", "jade_shard"));
                     }

                     if (name.startsWith("lightgrey_")) {
                        item.func_74778_a("id", id.replace("lightgrey_", "gray_"));
                     }

                     if (name.startsWith("magenta_")) {
                        item.func_74778_a("id", id.replace("magenta_", "purple_"));
                     }

                     if (name.startsWith("lime_")) {
                        item.func_74778_a("id", id.replace("lime_", "green_"));
                     }

                     if (name.startsWith("lightblue_")) {
                        item.func_74778_a("id", id.replace("lightblue_", "blue_"));
                     }

                     if (name.startsWith("grey")) {
                        item.func_74778_a("id", id.replace("grey", "gray"));
                     }

                     if (name.equals("chair")) {
                        item.func_74778_a("id", id.replace("chair", "red_chair"));
                     }

                     return item;
                  }
               }
            });
         }

         if (compound.func_74775_l("Data").func_74762_e("DataVersion") < 1343) {
            FileUtils.copyFile(file1, server.func_71254_M().func_186352_b(server.func_71270_I(), "level-pixelmonBackup.dat"));
            registries = compound.func_74775_l("FML").func_74775_l("Registries");
            NBTTagList blocks = registries.func_74775_l("minecraft:blocks").func_150295_c("aliases", 10);
            NBTTagList items = registries.func_74775_l("minecraft:items").func_150295_c("aliases", 10);
            i = blocks.iterator();

            while(i.hasNext()) {
               entry = (NBTTagCompound)i.next();
               if (entry.func_74779_i("K").equalsIgnoreCase(entry.func_74779_i("V"))) {
                  Pixelmon.LOGGER.debug("Removing block alias: " + entry.func_74779_i("K"));
                  i.remove();
               }
            }

            i = items.iterator();

            while(i.hasNext()) {
               entry = (NBTTagCompound)i.next();
               if (entry.func_74779_i("K").equalsIgnoreCase(entry.func_74779_i("V"))) {
                  Pixelmon.LOGGER.debug("Removing item alias: " + entry.func_74779_i("K"));
                  i.remove();
               }
            }

            NBTTagList dummiedBlocks = registries.func_74775_l("minecraft:blocks").func_150295_c("dummied", 10);
            NBTTagList dummiedItems = registries.func_74775_l("minecraft:items").func_150295_c("dummied", 10);
            Iterator i = dummiedBlocks.iterator();

            NBTTagCompound entry;
            while(i.hasNext()) {
               entry = (NBTTagCompound)i.next();
               if (entry.func_74779_i("K").startsWith("pixelmon:")) {
                  Pixelmon.LOGGER.debug("Removing dummied block: " + entry.func_74779_i("K"));
                  i.remove();
               }
            }

            i = dummiedItems.iterator();

            while(i.hasNext()) {
               entry = (NBTTagCompound)i.next();
               if (entry.func_74779_i("K").startsWith("pixelmon:")) {
                  Pixelmon.LOGGER.debug("Removing dummied item: " + entry.func_74779_i("K"));
                  i.remove();
               }
            }

            CompressedStreamTools.func_74799_a(compound, new FileOutputStream(file1));
         }

         if (modfix == null) {
            modfix = FMLCommonHandler.instance().getDataFixer().init("pixelmon", 9);
         }

         modfix.registerFix(FixTypes.ITEM_INSTANCE, new IFixableData() {
            public int func_188216_a() {
               return 8;
            }

            public NBTTagCompound func_188217_a(NBTTagCompound item) {
               String id = item.func_74779_i("id");
               if (!id.startsWith("pixelmon:")) {
                  return item;
               } else {
                  String name = id.substring("pixelmon:".length());
                  int count = item.func_74762_e("Count");
                  ItemStack stack = RemapHandler.findNewTMFor(name, count);
                  return stack != null ? stack.func_77955_b(item) : item;
               }
            }
         });
         CommonHelper.shutUpLogger(StatisticsManagerServer.class, ".*pixelmon.*");
         CommonHelper.shutUpLogger(FMLLog.class, ".*pixelmon:[tm|tr].*");
      } catch (Throwable var14) {
      }

   }

   public static ItemStack findNewTMFor(String id, int count) {
      if (id.startsWith("pixelmon:")) {
         id = id.substring("pixelmon:".length());
      }

      Matcher matcher = gen8TmItemPattern.matcher(id);
      int tm;
      ITechnicalMove move;
      ItemStack stack;
      if (matcher.matches()) {
         tm = Integer.parseInt(matcher.group(1));
         move = Gen8TechnicalMachines.getTm(tm);
         stack = PixelmonItemsTMs.createStackFor(move, count);
         if (stack != null) {
            return stack;
         }
      }

      matcher = gen8TrItemPattern.matcher(id);
      if (matcher.matches()) {
         tm = Integer.parseInt(matcher.group(1));
         move = Gen8TechnicalRecords.getTr(tm);
         stack = PixelmonItemsTMs.createStackFor(move, count);
         if (stack != null) {
            return stack;
         }
      }

      matcher = oldTmPattern.matcher(id);
      if (matcher.matches()) {
         tm = Integer.parseInt(matcher.group(1));
         String attackName = (String)oldTMNames.get(tm - 1);
         if (Gen8TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen8TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen7TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen7TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen6TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen6TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen5TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen5TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen4TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen4TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen3TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen3TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen2TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen2TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }

         if (Gen1TechnicalMachines.getTm(attackName) != null) {
            stack = PixelmonItemsTMs.createStackFor(Gen1TechnicalMachines.getTm(attackName), count);
            if (stack != null) {
               return stack;
            }
         }
      }

      return null;
   }
}
