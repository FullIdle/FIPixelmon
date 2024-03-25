package com.pixelmonmod.pixelmon.commands.client;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiExtrasEditor;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerExtrasPacket;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import org.apache.commons.lang3.StringUtils;

public class RedeemCommand extends PixelmonCommand implements IClientCommand {
   public String func_71517_b() {
      return "redeem";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/redeem <slot number/hat name/remove hat/toggle sash>";
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayer player = (EntityPlayer)sender;
      PixelExtrasData data = PlayerExtraDataStore.get(player);
      if (args.length == 0) {
         PlayerExtraDataStore.refreshClient((newData) -> {
            StringJoiner joiner = new StringJoiner(", ");
            StringBuilder sb = new StringBuilder("Available: ");
            if (newData.hasData()) {
               Iterator var6 = newData.getAvailableTextures().iterator();

               while(var6.hasNext()) {
                  EnumSpecies species = (EnumSpecies)var6.next();
                  if (species == EnumSpecies.Lugia) {
                     joiner.add("shadow_lugia");
                  }

                  if (species == EnumSpecies.Haunter) {
                     joiner.add("spectral_jeweller");
                  } else if (species == EnumSpecies.Wobbuffet) {
                     joiner.add("wobeefet");
                  }
               }

               if (sb.length() > 12) {
                  player.func_145747_a(new TextComponentString(sb.append(joiner).toString()));
               }

               this.sendMessage(sender, "pixelmon.command.redeem.gui", new Object[0]);
            } else {
               this.sendMessage(sender, "pixelmon.command.redeem.nodata", new Object[0]);
            }
         });
      } else if (data.hasData()) {
         if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
            ClientProxy.scheduleNextTick(() -> {
               Minecraft.func_71410_x().func_147108_a(new GuiExtrasEditor());
               return 0;
            });
         } else if (args.length == 2 && StringUtils.equalsAnyIgnoreCase(args[0], new CharSequence[]{"enable", "disable", "toggle"}) && StringUtils.equalsAnyIgnoreCase(args[1], new CharSequence[]{"hat", "sash", "robe", "monocle"})) {
            PixelExtrasData.Category category = args[1].equalsIgnoreCase("hat") ? PixelExtrasData.Category.HAT : (args[1].equalsIgnoreCase("sash") ? PixelExtrasData.Category.SASH : (args[1].equalsIgnoreCase("robe") ? PixelExtrasData.Category.ROBE : PixelExtrasData.Category.MONOCLE));
            if (args[0].equalsIgnoreCase("enable")) {
               data.setEnabled(category, true);
               PlayerExtraDataStore.saveAndSend();
               this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{category.toString().toLowerCase()});
            } else if (args[0].equalsIgnoreCase("disable")) {
               data.setEnabled(category, false);
               PlayerExtraDataStore.saveAndSend();
               this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.disabled", new Object[]{category.toString().toLowerCase()});
            } else if (args[0].equalsIgnoreCase("toggle")) {
               data.setEnabled(category, !data.isEnabled(category));
               if (data.isEnabled(category)) {
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{category.toString().toLowerCase()});
               } else {
                  this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.disabled", new Object[]{category.toString().toLowerCase()});
               }

               PlayerExtraDataStore.saveAndSend();
            } else {
               this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
            }
         } else {
            int slot;
            int g;
            int b;
            if (StringUtils.equalsAnyIgnoreCase(args[0], new CharSequence[]{"trainerhat", "trainercap"})) {
               if (data.setHatType(PixelExtrasData.HatType.TRAINER_CAP)) {
                  if (args.length == 4) {
                     try {
                        slot = Integer.parseInt(args[1]);
                        g = Integer.parseInt(args[2]);
                        b = Integer.parseInt(args[3]);
                        if (slot <= 255 && slot >= -1 && g <= 255 && g >= 0 && b <= 255 && b >= 0) {
                           data.setColours(PixelExtrasData.Category.HAT, new int[]{slot, g, b});
                        } else {
                           this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.trainerhat.invalidcolour", new Object[0]);
                        }
                     } catch (NumberFormatException var9) {
                        this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.trainerhat.invalidcolour", new Object[0]);
                     }
                  }

                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"trainercap"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("sash")) {
               if (args.length == 2) {
                  if (args[1].equalsIgnoreCase("regular")) {
                     if (data.setSashType(PixelExtrasData.SashType.REGULAR)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"regular sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("rainbow")) {
                     if (data.setSashType(PixelExtrasData.SashType.RAINBOW)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"rainbow sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("booster")) {
                     if (data.setSashType(PixelExtrasData.SashType.BOOSTER)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"booster sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("admin")) {
                     if (data.setSashType(PixelExtrasData.SashType.RANK_ADMIN)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"admin sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("jr")) {
                     if (data.setSashType(PixelExtrasData.SashType.RANK_JR)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"jr sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("dev")) {
                     if (data.setSashType(PixelExtrasData.SashType.RANK_DEV)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"dev sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("modeler")) {
                     if (data.setSashType(PixelExtrasData.SashType.RANK_MODELER)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"modeler sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (args[1].equalsIgnoreCase("support")) {
                     if (data.setSashType(PixelExtrasData.SashType.RANK_SUPPORT)) {
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"support sash"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else {
                     this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
                  }
               } else if (args.length == 4 && data.getSashType() == PixelExtrasData.SashType.REGULAR) {
                  try {
                     slot = Integer.parseInt(args[1]);
                     g = Integer.parseInt(args[2]);
                     b = Integer.parseInt(args[3]);
                     if (slot <= 255 && slot >= -1 && g <= 255 && g >= 0 && b <= 255 && b >= 0) {
                        data.setColours(PixelExtrasData.Category.SASH, new int[]{slot, g, b});
                     } else {
                        this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.trainerhat.invalidcolour", new Object[0]);
                     }
                  } catch (NumberFormatException var8) {
                     this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.trainerhat.invalidcolour", new Object[0]);
                  }

                  PlayerExtraDataStore.saveAndSend();
               }
            } else if (args[0].equalsIgnoreCase("tophat")) {
               if (data.setHatType(PixelExtrasData.HatType.TOP_HAT)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"tophat"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("fez")) {
               if (data.setHatType(PixelExtrasData.HatType.FEZ)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"fez"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("fedora")) {
               if (data.setHatType(PixelExtrasData.HatType.FEDORA)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"fedora"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("blackmonocle")) {
               if (data.setMonocleType(PixelExtrasData.MonocleType.BLACK_MONOCLE)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"blackmonocle"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("goldmonocle")) {
               if (data.setMonocleType(PixelExtrasData.MonocleType.GOLD_MONOCLE)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"goldmonocle"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("pikahood")) {
               if (data.setHatType(PixelExtrasData.HatType.PIKA_HOOD)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"pikahood"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("eeveehood")) {
               if (data.setHatType(PixelExtrasData.HatType.EEVEE_HOOD)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"eeveehood"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (args[0].equalsIgnoreCase("scorhood")) {
               if (data.setHatType(PixelExtrasData.HatType.SCOR_HOOD)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"scorhood"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (!args[0].equalsIgnoreCase("wizardrobe") && !args[0].equalsIgnoreCase("wizardrobe") && !args[0].equalsIgnoreCase("robe")) {
               if (!args[0].equalsIgnoreCase("alterrobe") && !args[0].equalsIgnoreCase("alter")) {
                  if (!args[0].equalsIgnoreCase("wiki") && !args[0].equalsIgnoreCase("grandcap")) {
                     if (!args[0].equalsIgnoreCase("spheal_hat") && !args[0].equalsIgnoreCase("sphealhat")) {
                        if (!data.getAvailableTextures().isEmpty()) {
                           try {
                              slot = Integer.parseInt(args[0]);
                              if (slot < 1 || slot > 6) {
                                 this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
                                 return;
                              }

                              Pixelmon.network.sendToServer(PlayerExtrasPacket.getSetTexturePacket(slot));
                           } catch (NumberFormatException var10) {
                              this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
                           }
                        } else {
                           this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]);
                        }
                     } else if (data.setHatType(PixelExtrasData.HatType.SPHEAL_HAT)) {
                        data.randomiseSpheal();
                        PlayerExtraDataStore.saveAndSend();
                        this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"sphealhat"});
                     } else {
                        endCommand("pixelmon.command.general.cannot", new Object[0]);
                     }
                  } else if (data.setHatType(PixelExtrasData.HatType.WIKI)) {
                     PlayerExtraDataStore.saveAndSend();
                     this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"grandcap"});
                  } else {
                     endCommand("pixelmon.command.general.cannot", new Object[0]);
                  }
               } else if (data.setRobeType(PixelExtrasData.RobeType.ALTER)) {
                  PlayerExtraDataStore.saveAndSend();
                  this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"alterrobe"});
               } else {
                  endCommand("pixelmon.command.general.cannot", new Object[0]);
               }
            } else if (data.setRobeType(PixelExtrasData.RobeType.WIZARD)) {
               PlayerExtraDataStore.saveAndSend();
               this.sendMessage(sender, TextFormatting.GREEN, "pixelmon.command.general.enabled", new Object[]{"wizardrobe"});
            } else {
               endCommand("pixelmon.command.general.cannot", new Object[0]);
            }
         }
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      EntityPlayer player = (EntityPlayer)sender;
      PixelExtrasData data = PlayerExtraDataStore.get(player);
      if (!data.hasData()) {
         return Lists.newArrayList();
      } else {
         List list = Lists.newArrayList();
         Iterator var8;
         if (data.hasData() && args.length == 1) {
            var8 = data.getAvailableHats().iterator();

            while(var8.hasNext()) {
               PixelExtrasData.HatType type = (PixelExtrasData.HatType)var8.next();
               list.add(type.toString().toLowerCase().replace("_", ""));
            }

            var8 = data.getAvailableMonocles().iterator();

            while(var8.hasNext()) {
               PixelExtrasData.MonocleType type = (PixelExtrasData.MonocleType)var8.next();
               list.add(type.toString().toLowerCase().replace("_", ""));
            }

            var8 = data.getAvailableRobes().iterator();

            while(var8.hasNext()) {
               PixelExtrasData.RobeType type = (PixelExtrasData.RobeType)var8.next();
               list.add(type.toString().toLowerCase() + "robe");
            }

            list.add("enable");
            list.add("disable");
            list.add("toggle");
         }

         if (args.length == 2 && args[0].equalsIgnoreCase("sash")) {
            var8 = data.getAvailableSashs().iterator();

            while(var8.hasNext()) {
               PixelExtrasData.SashType type = (PixelExtrasData.SashType)var8.next();
               list.add(type.toString().toLowerCase().replace("rank_", ""));
            }
         }

         if (args.length == 2 && StringUtils.equalsAnyIgnoreCase(args[0], new CharSequence[]{"enable", "disable", "toggle"})) {
            list.add("sash");
            list.add("robe");
            list.add("hat");
            list.add("monocle");
         }

         return func_175762_a(args, list);
      }
   }

   public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
      return false;
   }
}
