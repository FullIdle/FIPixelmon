package com.pixelmonmod.pixelmon.battles.controller.participants;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.tasks.BattleMessageTask;
import com.pixelmonmod.pixelmon.battles.tasks.HPUpdateTask;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Spectator {
   private EntityPlayerMP player;
   public String watchedName;

   public Spectator(EntityPlayerMP player, String watchedName) {
      this.player = player;
      this.watchedName = watchedName;
   }

   public EntityPlayerMP getEntity() {
      return this.player;
   }

   public void sendDamagePacket(PixelmonWrapper user, int damage) {
      this.sendMessage(new HPUpdateTask(user, -damage));
   }

   public void sendHealPacket(PixelmonWrapper target, int amount) {
      this.sendMessage(new HPUpdateTask(target, amount));
   }

   public void sendBattleMessage(TextComponentTranslation message) {
      this.sendMessage(new BattleMessageTask(message));
   }

   public void sendMessage(IMessage message) {
      Pixelmon.network.sendTo(message, this.player);
   }
}
