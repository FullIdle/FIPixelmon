package com.pixelmonmod.pixelmon.battles.tasks;

import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.raids.GuiRaidCatch;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RaidDropsTask implements IBattleTask {
   private boolean canCatch;
   private RaidData raid;
   private boolean shiny;
   private ArrayList drops = new ArrayList();

   /** @deprecated */
   @Deprecated
   public RaidDropsTask() {
   }

   public RaidDropsTask(boolean canCatch, RaidData raid, boolean shiny, RaidData.RaidPlayer player) {
      this.canCatch = canCatch;
      this.raid = raid;
      this.shiny = shiny;
      this.drops = player.drops;
   }

   public boolean process(ClientBattleManager bm) {
      this.processClient();
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void processClient() {
      Minecraft.func_71410_x().func_152344_a(() -> {
         Minecraft mc = Minecraft.func_71410_x();
         mc.func_147108_a(new GuiRaidCatch(this.raid, this.shiny, (ItemStack[])this.drops.toArray(new ItemStack[0]), this.canCatch));
      });
   }

   @Nullable
   public UUID getPokemonID() {
      return null;
   }

   public void fromBytes(ByteBuf buf) {
      this.canCatch = buf.readBoolean();
      this.raid = new RaidData(buf);
      this.shiny = buf.readBoolean();
      int count = buf.readInt();

      for(int i = 0; i < count; ++i) {
         this.drops.add(ByteBufUtils.readItemStack(buf));
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.canCatch);
      this.raid.writeToByteBuf(buf);
      buf.writeBoolean(this.shiny);
      buf.writeInt(this.drops.size());
      Iterator var2 = this.drops.iterator();

      while(var2.hasNext()) {
         ItemStack stack = (ItemStack)var2.next();
         ByteBufUtils.writeItemStack(buf, stack);
      }

   }
}
