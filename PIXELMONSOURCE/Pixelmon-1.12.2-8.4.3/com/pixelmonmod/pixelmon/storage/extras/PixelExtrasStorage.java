package com.pixelmonmod.pixelmon.storage.extras;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PixelExtrasDisplayPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@EventBusSubscriber(
   modid = "pixelmon"
)
public class PixelExtrasStorage {
   public static final Map playerExtras = new HashMap();

   public static PixelExtrasData getData(UUID uuid) {
      if (!playerExtras.containsKey(uuid)) {
         playerExtras.put(uuid, new PixelExtrasData(uuid));
         ExtrasContact.fromCache((PixelExtrasData)playerExtras.get(uuid));
      }

      return (PixelExtrasData)playerExtras.get(uuid);
   }

   public static void addAndDistribute(PixelExtrasData data) {
      playerExtras.put(data.id, data);
      if (data.isReady() && data.hasData()) {
         Pixelmon.network.sendToAll(new PixelExtrasDisplayPacket(data));
      }

   }

   @SubscribeEvent
   public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
      playerExtras.remove(event.player.func_110124_au());
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         if (getData(player.func_110124_au()).has(ExtrasContact.Groups.Developer)) {
            ITextComponent parent = new TextComponentString("");
            parent.func_150256_b().func_150238_a(TextFormatting.AQUA);
            ITextComponent star = new TextComponentString("★");
            parent.func_150257_a(star.func_150259_f());
            parent.func_150257_a(new TextComponentTranslation("pixelmon.dev.name", new Object[0]));
            parent.func_150257_a(star.func_150259_f());
            parent.func_150258_a(" ");
            ITextComponent playerName = new TextComponentString(player.getDisplayNameString());
            playerName.func_150256_b().func_150238_a(TextFormatting.AQUA);
            playerName.func_150256_b().func_150228_d(true);
            parent.func_150257_a(playerName);
            parent.func_150258_a(" ");
            parent.func_150257_a(new TextComponentTranslation("pixelmon.dev.join", new Object[0]));
            event.player.func_184102_h().func_184103_al().func_148539_a(parent);
         }

      }
   }
}
