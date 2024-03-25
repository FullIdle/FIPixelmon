package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.listener.SendoutListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.moveskills.UseMoveSkill;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class UseMoveSkillKey extends TargetKeyBinding {
   public UseMoveSkillKey() {
      super("key.pokemonexternal", 34, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         WorldClient world = Minecraft.func_71410_x().field_71441_e;
         if (world == null) {
            return;
         }

         Pokemon pokemon = ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
         if (pokemon == null || !SendoutListener.isInWorld(pokemon.getUUID(), Minecraft.func_71410_x().field_71441_e)) {
            return;
         }

         EntityPixelmon pixelmon = SendoutListener.getEntityInWorld(pokemon.getUUID(), world);
         String skillId = GuiPixelmonOverlay.selectedMoveSkill;
         ArrayList moveSkills = ClientProxy.getMoveSkills(pokemon);
         if (skillId == null) {
            return;
         }

         MoveSkill moveSkill = (MoveSkill)CollectionHelper.find(moveSkills, (ms) -> {
            return ms.id.equals(skillId);
         });
         if (moveSkill == null) {
            GuiPixelmonOverlay.selectedMoveSkill = null;
            return;
         }

         if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.BLOCK)) {
            this.targetLiquids = true;
         }

         if (pokemon.isMoveSkillCoolingDown(moveSkill)) {
            return;
         }

         RayTraceResult traceResult = getTarget(this.targetLiquids);
         boolean isSamePokemon = false;
         if (traceResult == null) {
            return;
         }

         if (traceResult.field_72308_g instanceof EntityPixelmon && traceResult.field_72308_g == pixelmon) {
            isSamePokemon = true;
         }

         if (traceResult.field_72313_a == Type.ENTITY && !isSamePokemon) {
            Entity entity = traceResult.field_72308_g;
            if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.POKEMON) && entity instanceof EntityPixelmon) {
               Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill, entity.func_145782_y(), MoveSkill.EnumTargetType.POKEMON));
            } else if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.PLAYER) && entity instanceof EntityPlayer) {
               Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill, entity.func_145782_y(), MoveSkill.EnumTargetType.PLAYER));
            } else if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.MISC_ENTITY)) {
               Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill, entity.func_145782_y(), MoveSkill.EnumTargetType.MISC_ENTITY));
            } else {
               Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill));
            }
         } else if (traceResult.field_72313_a == Type.BLOCK && moveSkill.hasTargetType(MoveSkill.EnumTargetType.BLOCK)) {
            Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill, traceResult.func_178782_a(), traceResult.field_178784_b));
         } else if (moveSkill.hasTargetType(MoveSkill.EnumTargetType.NOTHING)) {
            Pixelmon.network.sendToServer(new UseMoveSkill(GuiPixelmonOverlay.selectedPixelmon, moveSkill));
         }

         pixelmon.func_70642_aH();
      }

   }
}
