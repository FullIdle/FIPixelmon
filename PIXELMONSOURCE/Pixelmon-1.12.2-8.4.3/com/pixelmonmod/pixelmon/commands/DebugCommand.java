package com.pixelmonmod.pixelmon.commands;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.api.dialogue.DialogueInputScreen;
import com.pixelmonmod.pixelmon.api.drops.CustomDropScreen;
import com.pixelmonmod.pixelmon.api.overlay.notice.EnumOverlayLayout;
import com.pixelmonmod.pixelmon.api.overlay.notice.NoticeOverlay;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValueTypeAdapter;
import com.pixelmonmod.pixelmon.api.screens.CustomBadgeCaseScreen;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.util.SetLoader;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.blocks.enums.EnumAxis;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.RidingOffsets;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpheal;
import com.pixelmonmod.pixelmon.enums.forms.ICosmeticForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.items.EnumBadgeCase;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.DimensionHelper;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DebugCommand extends PixelmonCommand {
   private final HashMap subCommands = new HashMap();
   private final HashMap subCompletions = new HashMap();
   private EntityDen p = null;

   public DebugCommand() {
      super("pixeldebug", "", 4);
      this.registerSubCommand("test", this::test);
      this.registerSubCommand("checkabilities", this::checkabilities);
      this.registerSubCommand("dexfill", this::dexFill);
      this.registerSubCommand("lang", this::langCheck);
      this.registerSubCommand("battleExport", this::battleExport);
      this.registerSubCommand("sprites", this::spritesCheck);
      this.registerSubCommand("spec", this::spec);
      this.registerSubCommand("revertCosmetic", this::revertCosmetic);
      this.registerSubCommand("hween", this::hween);
      this.registerSubCommand("moveflags", this::moveFlags);
      this.registerSubCommand("fov", this::fov);
      this.registerSubCommand("render", this::render);
      this.registerSubCommand("reloadmodels", this::reloadModels);
      this.registerSubCommand("itemsprite", this::itemSprite);
      this.registerSubCommand("collection", this::collection);
      this.registerSubCommand("modelscale", this::modelScale, "model-scale");
      this.registerSubCommand("ridingoffset", this::ridingOffset);
      this.registerSubCommand("dialogue", this::dialogue);
      this.registerSubCommand("dialogueInput", this::dialogueInput);
      this.registerSubCommand("badgeCase", this::badgeCase);
      this.registerSubCommand("drops", this::drops);
      this.registerSubCommand("noticeoverlay", this::noticeoverlay);
      this.registerSubCommand("spawnwiki", this::spawnwiki);
      this.registerSubCommand("dimension", this::dimension);
      this.registerSubCommand("setheaditem", this::setheaditem);
      this.registerSubCompletion("hween", (sender, args, pos) -> {
         return args.length == 1 ? tabComplete(args, new String[]{"true", "false"}) : tabComplete(args, new String[0]);
      });
      this.registerSubCompletion("moveflags", (sender, args, pos) -> {
         return args.length == 1 ? tabComplete(args, new String[]{"authentic", "sound"}) : tabComplete(args, new String[0]);
      });
      this.registerSubCompletion("modelscale", (sender, args, pos) -> {
         return args.length == 1 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
      });
      this.registerSubCompletion("ridingoffset", (sender, args, pos) -> {
         return args.length == 1 ? tabCompletePokemon(args) : (args.length == 2 ? tabComplete(args, new String[]{"moving", "standing"}) : (args.length == 3 ? tabComplete(args, new String[]{"x", "y", "z"}) : tabComplete(args, new String[0])));
      });
      this.registerSubCompletion("modelscale", (sender, args, pos) -> {
         return args.length == 1 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
      });
      this.registerSubCompletion("model-scale", (sender, args, pos) -> {
         return args.length == 1 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
      });
      this.registerSubCompletion("noticeoverlay", (sender, args, pos) -> {
         return args.length == 1 ? tabComplete(args, new String[]{"pokemonsprite", "pokemon3d", "itemstack"}) : tabComplete(args, new String[0]);
      });
   }

   public void test(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      EnumSpheal.getWeightedRodForm(6);
   }

   public void checkabilities(ICommandSender sender, String[] args) throws CommandException {
      sender.func_145747_a(new TextComponentString("Checking all abilities..."));
      boolean spotted = false;
      EnumSpecies[] var4 = EnumSpecies.values();
      int var5 = var4.length;

      label66:
      for(int var6 = 0; var6 < var5; ++var6) {
         EnumSpecies species = var4[var6];
         Iterator var8 = species.getPossibleForms(true).iterator();

         while(true) {
            BaseStats stats;
            do {
               if (!var8.hasNext()) {
                  BaseStats stats = species.getBaseStats();
                  if (stats != null) {
                     String[] var17 = stats.getAbilitiesArray();
                     int var18 = var17.length;

                     for(int var19 = 0; var19 < var18; ++var19) {
                        String ability = var17[var19];
                        if (ability != null) {
                           Optional o = AbilityBase.getAbility(ability);
                           if (o.isPresent()) {
                              if (o.get() instanceof ComingSoon) {
                                 sender.func_145747_a(new TextComponentString("Invalid ability " + ability));
                                 spotted = true;
                              }
                           } else {
                              sender.func_145747_a(new TextComponentString("Invalid ability " + ability));
                              spotted = true;
                           }
                        }
                     }
                  }
                  continue label66;
               }

               IEnumForm form = (IEnumForm)var8.next();
               stats = species.getBaseStats(form);
            } while(stats == null);

            String[] var11 = stats.getAbilitiesArray();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               String ability = var11[var13];
               if (ability != null) {
                  Optional o = AbilityBase.getAbility(ability);
                  if (o.isPresent()) {
                     if (o.get() instanceof ComingSoon) {
                        sender.func_145747_a(new TextComponentString("Invalid ability " + ability));
                        spotted = true;
                     }
                  } else {
                     sender.func_145747_a(new TextComponentString("Invalid ability " + ability));
                     spotted = true;
                  }
               }
            }
         }
      }

      if (!spotted) {
         sender.func_145747_a(new TextComponentString("All abilities implemented!"));
      }

   }

   public void dexFill(ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayerMP) {
         EntityPlayerMP player = requireEntityPlayer(sender);
         PlayerPartyStorage store = Pixelmon.storageManager.getParty(player);
         EnumSpecies[] var5 = EnumSpecies.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            EnumSpecies species = var5[var7];
            store.pokedex.set(species.getNationalPokedexInteger(), EnumPokedexRegisterStatus.caught);
         }

         store.pokedex.update();
      }

   }

   public void battleExport(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player;
      if (args.length == 0) {
         player = requireEntityPlayer(sender);
      } else {
         player = requireEntityPlayer(args[0]);
      }

      BattleControllerBase battle = (BattleControllerBase)require(BattleRegistry.getBattle(player), "Player not in a battle!", new Object[0]);

      try {
         battle.battleLog.exportLogFile();
         ChatHandler.sendChat(player, "Exported battle log file to " + battle.battleLog.getFilename() + ".log");
      } catch (Exception var6) {
         var6.printStackTrace();
         ChatHandler.sendChat(player, "Error exporting battle log");
      }

   }

   public void langCheck(ICommandSender sender, String[] args) throws CommandException {
      this.sendMessage(sender, "Check console for details.", new Object[0]);
      checkMissingMoveAndAbilityLang();
      checkMissingBlockLangEntries();
      checkMissingItemLangEntries();
      checkMissingPokemonLangEntries();
   }

   public void spritesCheck(ICommandSender sender, String[] args) throws CommandException {
      EnumSpecies[] var3 = EnumSpecies.values();
      int var4 = var3.length;

      label68:
      for(int var5 = 0; var5 < var4; ++var5) {
         EnumSpecies species = var3[var5];
         Iterator var7 = species.getPossibleForms(true).iterator();

         while(true) {
            while(true) {
               if (!var7.hasNext()) {
                  continue label68;
               }

               IEnumForm form = (IEnumForm)var7.next();
               if (EnumSpecies.mfSprite.contains(species)) {
                  Gender[] var15 = new Gender[]{Gender.Female, Gender.Male};
                  int var16 = var15.length;

                  for(int var11 = 0; var11 < var16; ++var11) {
                     Gender gender = var15[var11];

                     for(int i = 0; i < 2; ++i) {
                        ResourceLocation rl = GuiResources.getPokemonSprite(species, form.getForm(), gender, "", i != 0);
                        if (!Pixelmon.proxy.resourceLocationExists(rl)) {
                           Pixelmon.LOGGER.warn("Missing sprite: " + rl.toString());
                        }
                     }
                  }
               } else {
                  for(int i = 0; i < 2; ++i) {
                     ResourceLocation rl = GuiResources.getPokemonSprite(species, form.getForm(), (Gender)null, "", i != 0);
                     if (!Pixelmon.proxy.resourceLocationExists(rl)) {
                        Pixelmon.LOGGER.warn("Missing sprite: " + rl.toString());
                     }
                  }
               }
            }
         }
      }

   }

   public void spec(ICommandSender sender, String[] args) throws CommandException {
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)sender);
      this.sendMessage(sender, "" + storage.get(0).getIVs().getStat(StatsType.HP), new Object[0]);
      PokemonSpec spec = new PokemonSpec(new String[]{"pikachu", "lvl:50", "ivhp:31"});
      Gson gson = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpecValue.class, new SpecValueTypeAdapter()).create();
      String json = gson.toJson(spec);
      System.out.println(json);
      spec = (PokemonSpec)gson.fromJson(json, PokemonSpec.class);
   }

   public void hween(ICommandSender sender, String[] args) throws CommandException {
      boolean enable = args.length == 1 && args[0].equalsIgnoreCase("true");
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(requireEntityPlayer(sender));
      if (enable) {
         storage.setHweenRobe(EnumFeatureState.Active);
         this.sendMessage(sender, "Active", new Object[0]);
      } else {
         storage.setHweenRobe(EnumFeatureState.Available);
         this.sendMessage(sender, "Available", new Object[0]);
      }

   }

   public void revertCosmetic(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
      Pokemon pokemon = (Pokemon)require(storage.get(requireInt(args[0], 1, 6, "Must be a number 1-6 for the slot of the pokemon you wish to revert", new Object[0]) - 1), "That slot is empty!", new Object[0]);
      require(pokemon.getFormEnum() instanceof ICosmeticForm, "That pokemon is not a special form.", new Object[0]);
      ICosmeticForm cosmetic = (ICosmeticForm)pokemon.getFormEnum();
      pokemon.setForm(cosmetic.getBaseFromCosmetic(pokemon));
   }

   public void moveFlags(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 1) {
         Predicate predicate;
         if (args[0].equalsIgnoreCase("authentic")) {
            predicate = (att) -> {
               return att != null && att.getFlags().authentic;
            };
         } else {
            if (!args[0].equalsIgnoreCase("sound")) {
               this.sendMessage(sender, "flag must be one of: authentic, sound", new Object[0]);
               return;
            }

            predicate = (att) -> {
               return att != null && att.getFlags().sound;
            };
         }

         List moves = (List)AttackBase.ATTACKS.stream().filter(predicate).map(AttackBase::getAttackName).collect(Collectors.toList());
         this.sendMessage(sender, "Moves with " + args[0] + ": " + StringUtils.join(moves, ", "), new Object[0]);
      } else {
         this.sendMessage(sender, "/pixeldebug moveflags <flag>", new Object[0]);
      }

   }

   public void fov(ICommandSender sender, String[] args) throws CommandException {
      Minecraft.func_71410_x().field_71474_y.field_74334_X = (float)Integer.parseInt(args[1]);
   }

   public void render(ICommandSender sender, String[] args) throws CommandException {
      Minecraft.func_71410_x().field_71474_y.field_151451_c = 1000;
   }

   public void reloadModels(ICommandSender sender, String[] args) throws CommandException {
      try {
         Method m = PixelmonModelRegistry.class.getDeclaredMethod("init");
         m.setAccessible(true);
         m.invoke((Object)null);
         Set loadedHolders = (Set)ReflectionHelper.getPrivateValue(ModelHolder.class, (Object)null, "loadedHolders");
         loadedHolders.forEach(ModelHolder::clear);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var5) {
         var5.printStackTrace();
      }

   }

   public void itemSprite(ICommandSender sender, String[] args) throws CommandException {
      ItemStack stack = new ItemStack(PixelmonItems.itemPixelmonSprite);
      NBTTagCompound tagCompound = new NBTTagCompound();
      tagCompound.func_74778_a("SpriteName", args[0]);
      stack.func_77982_d(tagCompound);
      ((EntityPlayerMP)sender).field_71071_by.func_70441_a(stack);
   }

   public void collection(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = (EntityPlayerMP)sender;
      int n = Integer.parseInt(args[0]);
      int radius = Integer.parseInt(args[1]);
      long tic = System.currentTimeMillis();

      for(int i = 0; i < n; ++i) {
         new BlockCollection(player, player.field_70170_p, (int)player.field_70165_t - radius, (int)player.field_70165_t + radius, (int)player.field_70163_u - radius, (int)player.field_70163_u + radius, (int)player.field_70161_v - radius, (int)player.field_70161_v + radius);
      }

      long toc = System.currentTimeMillis();
      this.sendMessage(sender, "Time to process " + n + " collections of diameter " + radius + " (" + n * 8 * radius * radius * radius + " blocks): " + (toc - tic) + " milliseconds.", new Object[0]);
   }

   public void modelScale(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 2) {
         try {
            String name = args[0];
            int form = -1;
            if (args[0].contains(":")) {
               name = args[0].split(":")[0];
               form = func_175755_a(args[0].split(":")[1]);
            }

            EnumSpecies species = EnumSpecies.getFromNameAnyCase(name);
            double scale = func_175765_c(args[1]);
            PixelmonModelBase model = (PixelmonModelBase)PixelmonModelRegistry.getModel(species, species.getFormEnum(form));
            if (model instanceof PixelmonModelSmd) {
               ValveStudioModel valve = ((PixelmonModelSmd)model).theModel;
               Field field = ValveStudioModel.class.getDeclaredField("scale");
               field.setAccessible(true);
               field.setFloat(valve, (float)scale);
               sender.func_145747_a(new TextComponentString("scale changed. This does not effect pokemon already spawned in the world."));
            } else {
               sender.func_145747_a(new TextComponentString("That model is not smd!"));
            }
         } catch (IllegalArgumentException var11) {
            sender.func_145747_a(new TextComponentString("No model by that name found!"));
         } catch (NoSuchFieldException | IllegalAccessException var12) {
            var12.printStackTrace();
         }
      } else {
         sender.func_145747_a(new TextComponentString("Usage: /pixeldebug model-scale <pokemon:form> <scale>"));
      }

   }

   public void ridingOffset(ICommandSender sender, String[] args) throws CommandException {
      if (args.length >= 1) {
         String name = args[0];
         int form = -1;
         if (args[0].contains(":")) {
            name = args[0].split(":")[0];
            form = func_175755_a(args[0].split(":")[1]);
         }

         EnumSpecies species = (EnumSpecies)require(EnumSpecies.getFromNameAnyCase(name), "Could not find a species by that name!", new Object[0]);
         RidingOffsets offsets = species.getBaseStats(species.getFormEnum(form)).ridingOffsets;
         if (args.length == 1) {
            if (offsets != null) {
               sender.func_145747_a(new TextComponentString("Standing: " + offsets.standing.toString()));
               sender.func_145747_a(new TextComponentString("Moving: " + offsets.moving.toString()));
            } else {
               sender.func_145747_a(new TextComponentString("This pokemon does not have offsets."));
            }
         } else if (args.length == 4) {
            boolean standing = args[1].equalsIgnoreCase("standing");
            EnumAxis axis = EnumAxis.valueOf(args[2].toUpperCase());
            float pos = (float)func_175765_c(args[3]);
            if (offsets == null) {
               species.getBaseStats(species.getFormEnum(form)).ridingOffsets = offsets = new RidingOffsets();
            }

            Vector3f vec = standing ? offsets.standing : offsets.moving;
            switch (axis) {
               case X:
                  vec.setX(pos);
                  break;
               case Y:
                  vec.setY(pos);
                  break;
               case Z:
                  vec.setZ(pos);
                  break;
               default:
                  endCommand("Axis must be one of x, y, z", new Object[0]);
            }
         } else {
            sender.func_145747_a(new TextComponentString("Usage: /pixeldebug ridingoffset <pokemon:form> <axis> <pos>"));
         }
      } else {
         sender.func_145747_a(new TextComponentString("Usage: /pixeldebug ridingoffset <pokemon:form> <standing|moving> <axis> <pos>"));
      }

   }

   public void dialogue(ICommandSender sender, String[] args) throws CommandException {
      ArrayList dialogues = new ArrayList();
      dialogues.add(Dialogue.builder().setName("Gus").setText("Hello, friend").addChoice(Choice.builder().setText("Hello, Gus").setHandle((e) -> {
         e.reply(Dialogue.builder().setName("Gus").setText("Enjoy your day!").build());
      }).build()).addChoice(Choice.builder().setText("You are everything wrong with the world.").setHandle((e) -> {
         e.reply(Dialogue.builder().setName("Gus").setText("Wow, ok then.").build());
      }).build()).build());
      Dialogue.setPlayerDialogueData((EntityPlayerMP)sender, dialogues, true);
   }

   public void dialogueInput(ICommandSender sender, String[] args) throws CommandException {
      DialogueInputScreen.builder().setTitle("Hello world!").setText("§aPlease enter your name: ____ §7This will allow you to ask a user to input any text they want, §dit can be used to accept any short text responses.").setDefaultText(sender.func_70005_c_()).sendTo(requireEntityPlayer(sender));
   }

   public void badgeCase(ICommandSender sender, String[] args) throws CommandException {
      CustomBadgeCaseScreen.builder().setTitle(sender.func_70005_c_()).setAllowEditing(false).setColor((EnumBadgeCase)RandomHelper.getRandomElementFromArray(EnumBadgeCase.values())).setBadges(Lists.newArrayList(new ItemStack[]{new ItemStack(PixelmonItemsBadges.balanceBadge)})).setPokemon(Lists.newArrayList(Pixelmon.storageManager.getParty(requireEntityPlayer(sender)).getAll())).sendTo(requireEntityPlayer(sender));
   }

   public void drops(ICommandSender sender, String[] args) throws CommandException {
      List items = Lists.newArrayList(new ItemStack[]{new ItemStack(Blocks.field_150346_d), new ItemStack(Items.field_151133_ar), new ItemStack(Items.field_151172_bF), new ItemStack(Items.field_151174_bG), new ItemStack(Items.field_151065_br), new ItemStack(Blocks.field_150428_aP), new ItemStack(Items.field_151034_e), new ItemStack(Items.field_151045_i), new ItemStack(Blocks.field_150432_aD), new ItemStack(Blocks.field_150351_n), new ItemStack(Items.field_151112_aM), new ItemStack(Items.field_151144_bL), new ItemStack(Items.field_151128_bU)});
      CustomDropScreen.openDropScreen((EntityPlayerMP)sender, new TextComponentString("Hello world!"), items, "Accept", "", "Drop");
   }

   public void noticeoverlay(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      NoticeOverlay.Builder builder = NoticeOverlay.builder().setLayout(EnumOverlayLayout.LEFT_AND_RIGHT).setLines("Debug");
      if (args.length >= 1) {
         switch (args[0].toLowerCase()) {
            case "pokemonsprite":
               if (args.length >= 2) {
                  builder.setPokemonSprite(new PokemonSpec((String[])ArrayUtils.subarray(args, 1, args.length)));
               } else {
                  builder.setPokemonSprite(new PokemonSpec(new String[]{"pikachu", "s"}));
               }
               break;
            case "pokemon3d":
               if (args.length >= 2) {
                  builder.setPokemon3D(new PokemonSpec((String[])ArrayUtils.subarray(args, 1, args.length)));
               } else {
                  builder.setPokemon3D(new PokemonSpec(new String[]{"pikachu", "s"}));
               }
               break;
            case "itemstack":
               builder.setItemStack(player.func_184614_ca());
               break;
            default:
               throw new CommandException("invalid argument: " + args[0].toLowerCase(), new Object[0]);
         }
      }

      builder.sendTo(player);
   }

   public void spawnwiki(ICommandSender sender, String[] args) throws CommandException {
      Function raiseFirstCap = (s) -> {
         return s.toUpperCase().charAt(0) + s.substring(1).toLowerCase();
      };
      BiFunction listToString = (list, cap) -> {
         if (list.isEmpty()) {
            return "";
         } else {
            String str = cap ? (String)raiseFirstCap.apply(list.get(0).toString()) : list.get(0).toString();

            for(int i = 1; i < list.size(); ++i) {
               str = str + ", " + (cap ? (String)raiseFirstCap.apply(list.get(i).toString()) : list.get(i).toString());
            }

            return str + "\n";
         }
      };
      File file = new File("wikidata.txt");

      try {
         file.createNewFile();
         PrintWriter pw = new PrintWriter(file);
         EnumSpecies[] var7 = EnumSpecies.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            EnumSpecies species = var7[var9];
            SpawnSet set = (SpawnSet)SetLoader.getSet(species.name);
            if (set == null) {
               System.out.println("Empty: " + species.name);
            } else {
               String wikiBit = "|'''#" + species.getNationalPokedexNumber() + "'''\n";
               wikiBit = wikiBit + "|" + species.name + "\n";
               ArrayList locations = new ArrayList();
               ArrayList biomes = new ArrayList();
               ArrayList times = new ArrayList();
               ArrayList weathers = new ArrayList();
               Iterator var17 = set.spawnInfos.iterator();

               while(var17.hasNext()) {
                  SpawnInfo info = (SpawnInfo)var17.next();
                  Iterator var19 = info.locationTypes.iterator();

                  while(var19.hasNext()) {
                     LocationType location = (LocationType)var19.next();
                     if (!locations.contains(location)) {
                        locations.add(location);
                     }
                  }

                  var19 = info.condition.biomes.iterator();

                  while(var19.hasNext()) {
                     Biome biome = (Biome)var19.next();
                     biomes.add(biome.field_76791_y);
                  }

                  var19 = info.condition.times.iterator();

                  while(var19.hasNext()) {
                     WorldTime time = (WorldTime)var19.next();
                     if (!times.contains(time)) {
                        times.add(time);
                     }
                  }

                  var19 = info.condition.weathers.iterator();

                  while(var19.hasNext()) {
                     WeatherType weather = (WeatherType)var19.next();
                     if (!weathers.contains(weather)) {
                        weathers.add(weather);
                     }
                  }
               }

               String locString = (String)listToString.apply(locations, true);
               if (!locString.equals("")) {
                  wikiBit = wikiBit + "|" + locString;
                  String biomeString = (String)listToString.apply(biomes, false);
                  if (!biomeString.equals("")) {
                     wikiBit = wikiBit + "|" + biomeString;
                     String timeString = (String)listToString.apply(times, true);
                     if (!timeString.equals("")) {
                        wikiBit = wikiBit + "|" + timeString;
                        String weatherString = (String)listToString.apply(weathers, true);
                        if (!weatherString.equals("")) {
                           wikiBit = wikiBit + "|" + weatherString;
                           wikiBit = wikiBit + "|-\n";
                           pw.write(wikiBit);
                        }
                     }
                  }
               }
            }
         }

         pw.flush();
         pw.close();
         System.out.println("Done");
      } catch (IOException var21) {
         var21.printStackTrace();
      }

   }

   public void dimension(ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayerMP && args.length == 1) {
         EntityPlayerMP player = (EntityPlayerMP)sender;
         DimensionHelper.forceTeleport(player, Integer.parseInt(args[0]), player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
      }

   }

   public void setheaditem(ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayerMP && args.length == 1) {
         EntityPlayerMP player = (EntityPlayerMP)sender;
         player.func_184201_a(EntityEquipmentSlot.HEAD, new ItemStack(Item.func_111206_d(args[0])));
      }

   }

   public String func_71518_a(ICommandSender sender) {
      StringJoiner joiner = new StringJoiner("|");
      this.subCommands.keySet().forEach(joiner::add);
      return "/pixeldebug [" + joiner.toString() + "]";
   }

   public final void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0) {
         if (this.subCommands.containsKey("")) {
            ((Execute)this.subCommands.get("")).execute(sender, args);
         } else {
            this.sendUsage(sender);
         }
      } else if (this.subCommands.containsKey(args[0].toLowerCase())) {
         ((Execute)this.subCommands.get(args[0].toLowerCase())).execute(sender, popFirst(args));
      } else {
         this.sendUsage(sender);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      if (args.length == 1) {
         return tabComplete(args, this.subCommands.keySet());
      } else {
         return args.length > 1 && this.subCompletions.containsKey(args[0]) ? ((TabCompletion)this.subCompletions.get(args[0])).completion(sender, popFirst(args), pos) : Collections.emptyList();
      }
   }

   public void sendUsage(ICommandSender sender) {
      sender.func_145747_a(new TextComponentTranslation("Usage: /" + this.name + " <" + arrayToString(0, (String[])this.subCommands.keySet().toArray(new String[0]), "|") + ">", new Object[0]));
   }

   protected void registerSubCommand(String name, Execute command) {
      this.subCommands.put(name.toLowerCase(), command);
   }

   protected void registerSubCommand(String name, Execute command, String... aliases) {
      this.subCommands.put(name.toLowerCase(), command);
      String[] var4 = aliases;
      int var5 = aliases.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String str = var4[var6];
         this.subCommands.put(str.toLowerCase(), command);
      }

   }

   protected void registerSubCompletion(String name, TabCompletion completion) {
      this.subCompletions.put(name.toLowerCase(), completion);
   }

   public static void recursiveJsonSearch(String dir, ArrayList jsons) {
      File file = new File(dir);
      String[] var3 = file.list();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         File subFile = new File(dir + "/" + name);
         if (subFile.isFile() && name.endsWith(".json")) {
            jsons.add(subFile);
         } else if (subFile.isDirectory()) {
            recursiveJsonSearch(dir + "/" + name, jsons);
         }
      }

   }

   private static void checkMissingPokemonLangEntries() {
      EnumSpecies[] var0 = EnumSpecies.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumSpecies species = var0[var2];
         String langEntryName = "pixelmon." + species.name.toLowerCase() + ".name";
         if (I18n.func_74838_a(langEntryName).equals(langEntryName)) {
            System.out.println("Pokemon " + species.name + " Doesn't have a lang entry " + langEntryName);
         }

         Iterator var5 = species.getPossibleForms(true).iterator();

         while(var5.hasNext()) {
            IEnumForm form = (IEnumForm)var5.next();
            if (I18n.func_74838_a(form.getUnlocalizedName()).equals(form.getUnlocalizedName())) {
               System.out.println("Pokemon " + species.name + "'s form " + form.getClass().getSimpleName() + " Doesn't have a lang entry " + form.getUnlocalizedName());
            }
         }
      }

   }

   private static void checkMissingItemLangEntries() {
   }

   private static void checkMissingMoveAndAbilityLang() {
      System.out.println(" =-= Missing abilities translations =-= ");
      Set abilities = new HashSet();
      Iterator var1 = BaseStats.allBaseStats.values().iterator();

      while(true) {
         BaseStats stats;
         do {
            if (!var1.hasNext()) {
               abilities.remove((Object)null);
               Iterator var6 = abilities.iterator();

               String key;
               while(var6.hasNext()) {
                  String str = (String)var6.next();
                  Optional ability = AbilityBase.getAbility(str);
                  if (ability.isPresent()) {
                     key = ((AbilityBase)ability.get()).getUnlocalizedName();
                     if (checkLangKey(key)) {
                        System.err.println("Missing ability translation: " + key);
                     }

                     key = "ability." + ((AbilityBase)ability.get()).getName() + ".description";
                     if (checkLangKey(key)) {
                        System.err.println("Missing ability description translation: " + key);
                     }
                  } else {
                     System.err.println("Could not find ability by name: " + str);
                  }
               }

               System.out.println(" =-= Missing move translations =-= ");
               var6 = AttackBase.ATTACKS.iterator();

               while(var6.hasNext()) {
                  AttackBase base = (AttackBase)var6.next();
                  key = base.getUnlocalizedName();
                  if (checkLangKey(key)) {
                     System.err.println("Missing move translation: " + key);
                  }

                  key = "attack." + base.getAttackName().toLowerCase().replace(" ", "_") + ".description";
                  if (checkLangKey(key)) {
                     System.err.println("Missing move description translation: " + key);
                  }
               }

               return;
            }

            stats = (BaseStats)var1.next();
            abilities.addAll(Arrays.asList(stats.getAbilitiesArray()));
         } while(stats.forms == null);

         Iterator var3 = stats.forms.values().iterator();

         while(var3.hasNext()) {
            BaseStats formStats = (BaseStats)var3.next();
            abilities.addAll(Arrays.asList(formStats.getAbilitiesArray()));
         }
      }
   }

   private static void checkMissingBlockLangEntries() {
   }

   private static boolean checkLangKey(String key) {
      String translated = I18n.func_74838_a(key);
      return translated.equals(key);
   }

   public static List tabCompleteUsernames(ICommandSender sender, String[] args, BlockPos pos) {
      return tabCompleteUsernames(args);
   }

   private static String[] popFirst(String[] array) {
      String[] pop = new String[array.length - 1];
      System.arraycopy(array, 1, pop, 0, pop.length);
      return pop;
   }

   private static String arrayToString(int index, String[] args, String intersect) {
      StringBuilder builder = new StringBuilder();
      if (args.length - index > 0) {
         builder.append(args[index++]);
      }

      for(int i = index; i < args.length; ++i) {
         builder.append(intersect).append(args[i]);
      }

      return builder.toString().trim();
   }

   protected interface TabCompletion {
      List completion(ICommandSender var1, String[] var2, @Nullable BlockPos var3);
   }

   protected interface Execute {
      void execute(ICommandSender var1, String[] var2) throws CommandException;
   }
}
