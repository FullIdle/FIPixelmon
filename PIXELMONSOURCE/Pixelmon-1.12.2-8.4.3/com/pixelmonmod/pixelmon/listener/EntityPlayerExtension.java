package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EntityPlayerExtension {
   public static DataParameter dwPokeballs;
   public static DataParameter dwMegaItem;
   public static DataParameter dwMegaItemsUnlocked;
   public static DataParameter dwShinyCharm;
   public static DataParameter dwOvalCharm;
   public static DataParameter dwExpCharm;
   public static DataParameter dwCatchingCharm;
   public static DataParameter dwMarkCharm;
   public static DataParameter dwHween;

   @SubscribeEvent
   public static void handleConstruction(EntityEvent.EntityConstructing event) {
      if (event.getEntity() instanceof EntityPlayer) {
         EntityDataManager dw = event.getEntity().func_184212_Q();
         dw.func_187214_a(dwPokeballs, "-1,-1,-1,-1,-1,-1,");
         dw.func_187214_a(dwMegaItem, (byte)0);
         dw.func_187214_a(dwMegaItemsUnlocked, (byte)0);
         dw.func_187214_a(dwShinyCharm, (byte)0);
         dw.func_187214_a(dwOvalCharm, (byte)0);
         dw.func_187214_a(dwExpCharm, (byte)0);
         dw.func_187214_a(dwCatchingCharm, (byte)0);
         dw.func_187214_a(dwMarkCharm, (byte)0);
         dw.func_187214_a(dwHween, (byte)0);
      }

   }

   @SubscribeEvent
   public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         resendAll((EntityPlayerMP)event.player);
      }

   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         resendAll((EntityPlayerMP)event.player);
      }

   }

   private static void resendAll(EntityPlayerMP player) {
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      if (party != null) {
         updatePlayerPokeballs(player, party.getAll());
         updatePlayerMegaItem(player, party.getMegaItem());
         updatePlayerMegaItemsUnlocked(player, party.getMegaItemsUnlocked());
         updatePlayerOvalCharm(player, party.getOvalCharm());
         updatePlayerShinyCharm(player, party.getShinyCharm());
         updatePlayerHweenRobe(player, party.getHweenRobe());
      }

   }

   public static void updatePlayerPokeballs(EntityPlayerMP player, Pokemon[] pokemon) {
      if (player != null) {
         StringBuilder ballsList = new StringBuilder();

         for(int i = 0; i < pokemon.length; ++i) {
            if (pokemon[i] != null && !pokemon[i].isEgg()) {
               ballsList.append(pokemon[i].getCaughtBall().getIndex()).append(",");
            } else {
               ballsList.append("-1").append(",");
            }
         }

         player.func_184212_Q().func_187227_b(dwPokeballs, ballsList.toString());
      }
   }

   public static int[] getPlayerPokeballs(EntityPlayer player) {
      String ballsList = (String)player.func_184212_Q().func_187225_a(dwPokeballs);
      String[] splits = ballsList.split(",");
      int[] balls = new int[6];

      for(int i = 0; i < 6; ++i) {
         balls[i] = Integer.parseInt(splits[i]);
      }

      return balls;
   }

   public static void updatePlayerMegaItem(EntityPlayerMP player, EnumMegaItem megaItem) {
      player.func_184212_Q().func_187227_b(dwMegaItem, (byte)megaItem.ordinal());
   }

   public static EnumMegaItem getPlayerMegaItem(EntityPlayer player) {
      return EnumMegaItem.fromOrdinal((Byte)player.func_184212_Q().func_187225_a(dwMegaItem));
   }

   public static void updatePlayerMegaItemsUnlocked(EntityPlayerMP player, EnumMegaItemsUnlocked megaItemsUnlocked) {
      player.func_184212_Q().func_187227_b(dwMegaItemsUnlocked, (byte)megaItemsUnlocked.ordinal());
   }

   public static EnumMegaItemsUnlocked getPlayerMegaItemsUnlocked(EntityPlayer player) {
      return EnumMegaItemsUnlocked.fromOrdinal((Byte)player.func_184212_Q().func_187225_a(dwMegaItemsUnlocked));
   }

   public static void updatePlayerShinyCharm(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwShinyCharm, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerShinyCharm(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwShinyCharm)];
   }

   public static void updatePlayerOvalCharm(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwOvalCharm, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerOvalCharm(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwOvalCharm)];
   }

   public static void updatePlayerExpCharm(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwExpCharm, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerExpCharm(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwExpCharm)];
   }

   public static void updatePlayerCatchingCharm(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwCatchingCharm, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerCatchingCharm(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwCatchingCharm)];
   }

   public static void updatePlayerMarkCharm(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwMarkCharm, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerMarkCharm(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwMarkCharm)];
   }

   public static void updatePlayerHweenRobe(EntityPlayerMP player, EnumFeatureState charm) {
      player.func_184212_Q().func_187227_b(dwHween, (byte)charm.ordinal());
   }

   public static EnumFeatureState getPlayerHweenRobe(EntityPlayer player) {
      return EnumFeatureState.values()[(Byte)player.func_184212_Q().func_187225_a(dwHween)];
   }

   static {
      CommonHelper.shutUpLogger(EntityDataManager.class, ".*EntityPlayerExtension.*");
      dwPokeballs = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187194_d);
      dwMegaItem = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwMegaItemsUnlocked = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwShinyCharm = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwOvalCharm = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwExpCharm = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwCatchingCharm = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwMarkCharm = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
      dwHween = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
   }
}
