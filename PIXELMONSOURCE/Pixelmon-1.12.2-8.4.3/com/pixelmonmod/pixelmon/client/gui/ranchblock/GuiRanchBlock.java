package com.pixelmonmod.pixelmon.client.gui.ranchblock;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.EnumRanchServerPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.RanchBlockServerPacket;
import java.util.Objects;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiRanchBlock extends GuiScreen {
   private TileEntityRanchBlock ranch;
   private GuiButton claimEgg;

   public GuiRanchBlock(TileEntityRanchBlock ranch) {
      Preconditions.checkArgument(ranch != null, "The provided ranch cannot be null");
      this.ranch = ranch;
   }

   public GuiRanchBlock(World world, int x, int y, int z) {
      this((TileEntityRanchBlock)world.func_175625_s(new BlockPos(x, y, z)));
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 50, this.field_146295_m * 2 / 3 - 40, 100, 20, I18n.func_135052_a("gui.ranch.managePokemon", new Object[0])));
      this.field_146292_n.add(this.claimEgg = new GuiButton(2, this.field_146294_l / 2 - 20, this.field_146295_m * 2 / 3, 110, 20, I18n.func_135052_a("gui.ranch.claimEgg", new Object[0])));
      this.claimEgg.field_146124_l = false;
      this.claimEgg.field_146125_m = false;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.ranch.getPokemonData().isEmpty()) {
         String s = I18n.func_135052_a("gui.ranch.empty", new Object[0]);
         this.field_146289_q.func_78276_b(s, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(s) / 2, this.field_146295_m / 3 - 45, 16777215);
      }

      for(int i = 0; i < this.ranch.getPokemonData().size(); ++i) {
         Pokemon pokemon = ((TileEntityRanchBlock.RanchPoke)this.ranch.getPokemonData().get(i)).getPokemon(ClientStorageManager.openPC);
         if (pokemon == null || !Objects.equals(pokemon.getUUID(), ((TileEntityRanchBlock.RanchPoke)this.ranch.getPokemonData().get(i)).uuid)) {
            this.field_146297_k.field_71439_g.func_71053_j();
            return;
         }

         boolean isGen6Sprite = pokemon.getSpecies().getNationalPokedexInteger() > 649 && !pokemon.isEgg();
         GuiHelper.bindPokemonSprite(pokemon, this.field_146297_k);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + (i == 0 ? -65 : 15)), (double)(this.field_146295_m / 3 - 35 - (isGen6Sprite ? 3 : 0)), 50.0, 50.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

      if (this.ranch.hasEgg()) {
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.getEggSprite(this.ranch.getEggSpecies(), 20));
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 100), (double)(this.field_146295_m * 2 / 3 - 40), 80.0, 80.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   public void func_73876_c() {
      super.func_73876_c();
      this.claimEgg.field_146124_l = this.ranch.hasEgg();
      this.claimEgg.field_146125_m = this.claimEgg.field_146124_l;
   }

   public void func_146284_a(GuiButton button) {
      if (button.field_146127_k == 0) {
         this.field_146297_k.func_147108_a(new GuiPCRanch(this.ranch));
      } else if (button.field_146127_k == 2 && this.ranch.hasEgg()) {
         Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), EnumRanchServerPacketMode.CollectEgg));
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
