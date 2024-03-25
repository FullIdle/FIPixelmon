package com.pixelmonmod.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.starter.GuiChooseStarter;
import com.pixelmonmod.pixelmon.client.render.blockReveal.BlockRevealRenderer;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.GuiOpenClose;
import com.pixelmonmod.pixelmon.comm.packetHandlers.StarterListPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import com.pixelmonmod.pixelmon.util.Scheduling;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickHandler {
   boolean screenOpen = false;
   boolean initialised = false;
   public int worldCounter;
   private int ticksSincePlayersCheck = 0;
   static final ArrayList playerListForStartMenu = new ArrayList();

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != Phase.START) {
         if (event.side == Side.SERVER) {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
            if (player.field_71104_cf != null) {
               EntityFishHook hook = player.field_71104_cf;
               if (hook.field_146043_c instanceof EntityPokestop || hook.field_146043_c instanceof EntityDen) {
                  hook.field_146043_c = null;
               }
            }

            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
            Pixelmon.eggHatcher.tick(player, pps);
            Pixelmon.passiveHealer.tick(player, pps);
            Pixelmon.passiveEffects.tick(player, pps);
            if (pps.getLure() != null) {
               Pixelmon.lureExpirer.tick(player, pps, pps.getLureStack());
            }
         }

      }
   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != Phase.START) {
         BlockRevealRenderer.update();
         if (ServerStorageDisplay.starterListPacket != null && Minecraft.func_71410_x().field_71462_r == null) {
            try {
               if (ClientStorageManager.party != null && ClientStorageManager.party.countAll() > 0) {
                  ServerStorageDisplay.starterListPacket = null;
               } else {
                  Minecraft.func_71410_x().func_147108_a(new GuiChooseStarter());
               }
            } catch (NullPointerException var3) {
            }
         }

         if (Minecraft.func_71410_x().field_71462_r != null && (!this.screenOpen || !this.initialised) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChat)) {
            Pixelmon.network.sendToServer(new GuiOpenClose(true));
            this.screenOpen = true;
            this.initialised = true;
         } else if (this.screenOpen && Minecraft.func_71410_x().field_71462_r == null || !this.initialised) {
            Pixelmon.network.sendToServer(new GuiOpenClose(false));
            this.screenOpen = false;
            this.initialised = true;
            if (Minecraft.func_71410_x().field_71474_y.field_74319_N || !GuiIngameForge.renderHotbar) {
               Minecraft.func_71410_x().field_71474_y.field_74319_N = false;
               GuiIngameForge.renderHotbar = true;
               GuiIngameForge.renderCrosshairs = true;
               GuiIngameForge.renderExperiance = true;
               GuiIngameForge.renderAir = true;
               GuiIngameForge.renderHealth = true;
               GuiIngameForge.renderFood = true;
               GuiIngameForge.renderArmor = true;
            }
         }

      }
   }

   @SubscribeEvent
   public void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase == Phase.START) {
         Scheduling.tick();
      } else {
         if (this.worldCounter % (PixelmonConfig.timeUpdateInterval * 20) == 0) {
            TimeHandler.changeTime();
         }

         BattleControllerBase.currentAnimations.removeIf((animation) -> {
            try {
               return animation.user.entity != null && animation.target.entity != null ? animation.tickAnimation(++animation.ticks) : true;
            } catch (Exception var2) {
               var2.printStackTrace();
               return true;
            }
         });
         synchronized(playerListForStartMenu) {
            if (!playerListForStartMenu.isEmpty() && this.worldCounter % 100 == 0) {
               Iterator var3 = playerListForStartMenu.iterator();

               while(var3.hasNext()) {
                  EntityPlayerMP player = (EntityPlayerMP)var3.next();
                  Pixelmon.network.sendTo(new StarterListPacket(), player);
               }
            }
         }

         if (++this.ticksSincePlayersCheck >= 20) {
            this.ticksSincePlayersCheck = 0;
            Iterator var2 = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_181057_v().iterator();

            while(var2.hasNext()) {
               EntityPlayerMP player = (EntityPlayerMP)var2.next();
               Pixelmon.storageManager.getParty(player).checkPokerus();
            }
         }

         ++this.worldCounter;
      }
   }

   @SubscribeEvent
   public void onWorldTick(TickEvent.WorldTickEvent event) {
      if (event.side == Side.SERVER && event.phase == Phase.START) {
         WorldTime.updateWorldTime(event.world);
         EvolutionQueryList.tick(event.world);
         LearnMoveController.tick(event.world);
      }

   }

   public static void registerStarterList(EntityPlayerMP player) {
      synchronized(playerListForStartMenu) {
         if (playerListForStartMenu.contains(player)) {
            return;
         }
      }

      Pixelmon.network.sendTo(new StarterListPacket(), player);
      EntityPlayerMP listEntry = null;
      synchronized(playerListForStartMenu) {
         Iterator var3 = playerListForStartMenu.iterator();

         while(var3.hasNext()) {
            EntityPlayerMP listPlayer = (EntityPlayerMP)var3.next();
            if (listPlayer.func_110124_au().equals(player.func_110124_au())) {
               listEntry = listPlayer;
               break;
            }
         }

         if (listEntry == null) {
            playerListForStartMenu.add(player);
         }

      }
   }

   public static void deregisterStarterList(EntityPlayerMP player) {
      EntityPlayerMP listEntry = null;
      synchronized(playerListForStartMenu) {
         Iterator var3 = playerListForStartMenu.iterator();

         while(var3.hasNext()) {
            EntityPlayerMP listPlayer = (EntityPlayerMP)var3.next();
            if (listPlayer.func_110124_au().equals(player.func_110124_au())) {
               listEntry = listPlayer;
               break;
            }
         }

         playerListForStartMenu.remove(listEntry);
      }
   }

   @SubscribeEvent
   public void onPlayerJoinedServer(EntityJoinWorldEvent event) {
      if (event.getWorld().field_72995_K && event.getEntity() == Minecraft.func_71410_x().field_71439_g) {
         PlayerExtraDataStore.sendDisplayPacket();
      }

   }
}
