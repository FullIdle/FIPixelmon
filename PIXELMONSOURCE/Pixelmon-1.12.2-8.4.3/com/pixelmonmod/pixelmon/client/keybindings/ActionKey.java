package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.listener.SendoutListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.EnumKeyPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.KeyPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ActionKey extends TargetKeyBinding {
   public ActionKey() {
      super("key.pokemonaction", 47, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         if (Minecraft.func_71410_x().field_71441_e == null) {
            return;
         }

         Pokemon currentPokemon = ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
         if (currentPokemon == null || !SendoutListener.isInWorld(currentPokemon.getUUID(), Minecraft.func_71410_x().field_71441_e)) {
            return;
         }

         EntityPixelmon pixelmon = SendoutListener.getEntityInWorld(currentPokemon.getUUID(), Minecraft.func_71410_x().field_71441_e);
         RayTraceResult objectMouseOver = getTarget(this.targetLiquids);
         boolean isSamePokemon = false;
         if (objectMouseOver == null) {
            return;
         }

         if (objectMouseOver.field_72308_g instanceof EntityPixelmon && objectMouseOver.field_72308_g == pixelmon) {
            isSamePokemon = true;
         }

         if (objectMouseOver.field_72313_a == Type.ENTITY && !isSamePokemon && objectMouseOver.field_72308_g instanceof EntityLivingBase) {
            Entity entity = objectMouseOver.field_72308_g;
            GuiPixelmonOverlay.targetId = entity.func_145782_y();
            Pixelmon.network.sendToServer(new KeyPacket(GuiPixelmonOverlay.selectedPixelmon, entity.func_145782_y(), EnumKeyPacketMode.ActionKeyEntity));
         } else {
            GuiPixelmonOverlay.targetId = -1;
            Pixelmon.network.sendToServer(new KeyPacket(GuiPixelmonOverlay.selectedPixelmon, -1, EnumKeyPacketMode.ActionKeyEntity));
         }
      }

   }
}
