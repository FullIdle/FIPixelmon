package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.function.Function;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class Teleport {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("teleport")).setName("pixelmon.moveskill.teleport.name").describe("pixelmon.moveskill.teleport.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/teleport.png")).setDefaultCooldownTicks(20).setUsePP(true).setAnyMoves("Teleport");
      return moveSkill.setBehaviourNoTarget(teleportAction(moveSkill, true));
   }

   public static Function teleportAction(MoveSkill moveSkill, boolean canChangeDim) {
      return (pixelmon) -> {
         if (pixelmon.hasOwner()) {
            EntityPlayerMP player = (EntityPlayerMP)pixelmon.func_70902_q();
            if (player == null) {
               return -1;
            } else {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               if (!canChangeDim && party.teleportPos.getDimension() != player.field_71093_bK) {
                  return -1;
               } else {
                  party.teleportPos.teleport(player);
                  return moveSkill.cooldownTicks + (int)Math.max(0.0F, (300.0F - (float)pixelmon.getStoragePokemonData().getStat(StatsType.Speed)) / 50.0F * 20.0F);
               }
            }
         } else {
            return -1;
         }
      };
   }
}
