package com.pixelmonmod.pixelmon.api.command;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.commands.ExecutePixelmonCommandEvent;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class PixelmonCommand extends CommandBase {
   protected String name;
   protected String usage;
   protected int permission = 4;
   public List aliases = Lists.newArrayList();

   protected abstract void execute(ICommandSender var1, String[] var2) throws CommandException;

   public PixelmonCommand() {
   }

   protected PixelmonCommand(String name, String usage, int permissionLevel) {
      this.name = name;
      this.usage = usage;
      this.permission = permissionLevel;
   }

   public String func_71517_b() {
      return this.name;
   }

   public String func_71518_a(ICommandSender sender) {
      return this.usage;
   }

   public final void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      ExecutePixelmonCommandEvent event = new ExecutePixelmonCommandEvent(sender, this, args);
      if (!Pixelmon.EVENT_BUS.post(event)) {
         try {
            event.getCommand().execute(event.sender, event.getArgs());
         } catch (TargetSelectorExit var6) {
         }
      }

   }

   public int func_82362_a() {
      return this.permission;
   }

   public List func_71514_a() {
      return this.aliases;
   }

   protected void resendWithMultipleTargets(ICommandSender sender, String[] args, int playerIndex) throws CommandException {
      if (args.length > playerIndex) {
         String selector = args[playerIndex];
         if (selector.startsWith("@")) {
            List playerMatches = EntitySelector.func_179656_b(sender, selector, EntityPlayerMP.class);
            require((Object)playerMatches, "commands.generic.selector.notFound", selector);
            Iterator var6 = playerMatches.iterator();

            while(var6.hasNext()) {
               EntityPlayerMP player = (EntityPlayerMP)var6.next();
               String[] newArgs = (String[])args.clone();
               newArgs[playerIndex] = player.func_70005_c_();
               this.execute(sender, newArgs);
            }

            sender.func_174794_a(Type.AFFECTED_ENTITIES, playerMatches.size());
            throw new TargetSelectorExit();
         }
      }
   }

   @Nonnull
   public static EntityPlayerMP requireEntityPlayer(ICommandSender sender) throws CommandException {
      if (sender instanceof EntityPlayerMP) {
         return (EntityPlayerMP)sender;
      } else {
         throw getException("pixelmon.command.general.needtobeplayer");
      }
   }

   @Nonnull
   public static EntityPlayerMP requireEntityPlayer(String username) throws CommandException {
      EntityPlayerMP player = getEntityPlayer(username);
      if (player != null) {
         return player;
      } else {
         throw getException("commands.generic.player.notFound", username);
      }
   }

   public static EntityPlayerMP getEntityPlayer(String username) {
      if (username == null) {
         return null;
      } else if (username.length() == 36) {
         UUID uuid = UUID.fromString(username);
         return getEntityPlayer(uuid);
      } else if (username.length() == 32) {
         BigInteger bigInteger = new BigInteger(username, 16);
         UUID uuid = new UUID(bigInteger.shiftRight(64).longValue(), bigInteger.longValue());
         return getEntityPlayer(uuid);
      } else {
         return PixelmonPlayerUtils.getUniquePlayerStartingWith(username);
      }
   }

   public static EntityPlayerMP getEntityPlayer(UUID uuid) {
      return uuid == null ? null : FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(uuid);
   }

   public static GameProfile findProfile(String username) {
      if (username == null) {
         return null;
      } else {
         EntityPlayerMP playerMP = getEntityPlayer(username);
         if (playerMP != null) {
            return playerMP.func_146103_bH();
         } else {
            PlayerProfileCache cache = FMLCommonHandler.instance().getMinecraftServerInstance().func_152358_ax();
            if (username.length() == 36) {
               UUID uuid = UUID.fromString(username);
               return cache.func_152652_a(uuid);
            } else if (username.length() == 32) {
               BigInteger bigInteger = new BigInteger(username, 16);
               UUID uuid = new UUID(bigInteger.shiftRight(64).longValue(), bigInteger.longValue());
               return cache.func_152652_a(uuid);
            } else {
               return PixelmonPlayerUtils.getGameProfileNoLookup(cache, username);
            }
         }
      }
   }

   public static int requireInt(String input, String message) throws CommandException {
      try {
         return Integer.parseInt(input);
      } catch (NumberFormatException var3) {
         throw getException(message, input);
      }
   }

   public static int requireInt(String input, int min, int max, String message, Object... data) throws CommandException {
      try {
         int i = Integer.parseInt(input);
         if (i < min) {
            throw getException("commands.generic.num.tooSmall", input, min);
         } else if (i > max) {
            throw getException("commands.generic.num.tooBig", input, max);
         } else {
            return i;
         }
      } catch (NumberFormatException var6) {
         throw getException(message, input, data);
      }
   }

   @Nonnull
   public static Object require(Optional optional, String message, Object... data) throws CommandException {
      if (!optional.isPresent()) {
         endCommand(message, data);
      }

      return optional.get();
   }

   @Nonnull
   public static Object require(Object obj, String message, Object... data) throws CommandException {
      if (obj == null) {
         endCommand(message, data);
      } else if (obj instanceof Boolean && !(Boolean)obj) {
         endCommand(message, data);
      } else if (obj instanceof Collection && ((Collection)obj).isEmpty()) {
         endCommand(message, data);
      }

      return obj;
   }

   public static void endCommand(String message, Object... data) throws CommandException {
      throw getException(message, data);
   }

   private static CommandException getException(String translation, Object... data) {
      return new CommandException(translation, data) {
         public synchronized Throwable fillInStackTrace() {
            return this;
         }
      };
   }

   @Nullable
   public static EntityPlayerMP getPlayer(String name) {
      return PixelmonPlayerUtils.getUniquePlayerStartingWith(name);
   }

   @Nullable
   public static EntityPlayerMP getPlayer(ICommandSender sender, String name) {
      return PixelmonPlayerUtils.getUniquePlayerStartingWith(name, sender);
   }

   @Nullable
   public static PlayerPartyStorage getPlayerStorage(EntityPlayerMP player) {
      return Pixelmon.storageManager.getParty(player);
   }

   public static PCStorage getComputerStorage(EntityPlayerMP player) {
      return Pixelmon.storageManager.getPCForPlayer(player);
   }

   public static MinecraftServer getServer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance();
   }

   public static List tabComplete(String[] args, String... options) {
      return tabComplete(args, (Collection)Arrays.asList(options));
   }

   public static List tabComplete(String[] args, Collection options) {
      return options.isEmpty() ? Collections.emptyList() : CommandBase.func_175762_a(args, options);
   }

   public static List tabCompleteUsernames(String[] args) {
      return func_71530_a(args, getServer().func_71213_z());
   }

   public static List tabCompletePokemon(String[] args) {
      return func_175762_a(args, EnumSpecies.getNameList());
   }

   public static ITextComponent format(TextFormatting color, String lang, Object... args) {
      ITextComponent message = new TextComponentTranslation(lang, args);
      message.func_150256_b().func_150238_a(color);
      return message;
   }

   public static ITextComponent format(String lang, Object... args) {
      return format(TextFormatting.GRAY, lang, args);
   }

   public void sendMessage(ICommandSender sender, TextFormatting color, String string, Object... data) {
      TextComponentTranslation text = new TextComponentTranslation(string, data);
      text.func_150256_b().func_150238_a(color);
      sender.func_145747_a(text);
   }

   public void sendMessage(ICommandSender sender, String string, Object... data) {
      this.sendMessage(sender, TextFormatting.GRAY, string, data);
   }

   private static class TargetSelectorExit extends CommandException {
      public TargetSelectorExit() {
         super("pixelmon.command.general.invalid", new Object[0]);
      }

      public synchronized Throwable fillInStackTrace() {
         return this;
      }
   }
}
