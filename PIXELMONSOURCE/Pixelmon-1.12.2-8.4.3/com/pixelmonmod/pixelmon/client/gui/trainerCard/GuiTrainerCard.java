package com.pixelmonmod.pixelmon.client.gui.trainerCard;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trainerCard.RequestTrainerCardPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trainerCard.TrainerCardColorPacket;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardUser;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.io.IOException;
import java.text.NumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTrainerCard extends GuiScreen {
   public EntityPlayer player;
   public Pokemon[] party = new Pokemon[6];
   public EnumTrainerCardColor color;
   public EnumTrainerCardUser user;
   public String money;
   public int caughtCount;
   private static final int SPRITE_HEIGHT = 40;
   private static final int SPRITE_WIDTH = 40;
   private static final int POKEBALL_HEIGHT = 43;
   private static final int POKEBALL_WIDTH = 44;

   public GuiTrainerCard() {
      this.color = EnumTrainerCardColor.WHITE;
      this.money = "0";
      this.caughtCount = 0;
      this.player = Minecraft.func_71410_x().field_71439_g;
      this.party = ClientStorageManager.party.getAll();
      this.color = ClientData.color;
      this.user = EnumTrainerCardUser.getFromPlayer(this.player);
      this.money = NumberFormat.getInstance().format((long)ClientData.playerMoney);
      this.caughtCount = ClientStorageManager.pokedex.countCaught();
   }

   public GuiTrainerCard(EntityPlayer player) {
      this.color = EnumTrainerCardColor.WHITE;
      this.money = "0";
      this.caughtCount = 0;
      this.player = player;
      this.user = EnumTrainerCardUser.getFromPlayer(this.player);
      Pixelmon.network.sendToServer(new RequestTrainerCardPacket(player.func_110124_au()));
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146297_k.func_147118_V().func_147682_a(PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
      if (this.player.equals(this.field_146297_k.field_71439_g)) {
         this.field_146292_n.clear();
         int i = 0;
         EnumTrainerCardColor[] var2 = EnumTrainerCardColor.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EnumTrainerCardColor color = var2[var4];
            if (color == EnumTrainerCardColor.GOLD && this.user == EnumTrainerCardUser.REGULAR) {
               return;
            }

            this.field_146292_n.add(new ThemedButton(i, color, this.field_146294_l / 2 - 158 + 13 * i++, 205));
         }
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.player == null) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179152_a(2.5F, 2.5F, 0.0F);
         this.func_73729_b((this.field_146294_l - 290) / 4, this.field_146295_m / 2 + 15, 227, 242, 10, 14);
         GlStateManager.func_179121_F();
      } else {
         GuiHelper.drawEntity(this.player, this.field_146294_l / 2 + 123, 151, 45.0F, 0.0F, 0.0F);
      }

      int xOffset = this.field_146294_l / 2 - 165;
      int yOffset = 30;
      this.field_146297_k.field_71446_o.func_110577_a(this.color.resource);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      Gui.func_146110_a(xOffset, yOffset, 0.0F, 0.0F, 335, 195, 335.0F, 195.0F);
      if (this.user.resource != null) {
         this.field_146297_k.field_71446_o.func_110577_a(this.user.resource);
         Gui.func_146110_a(xOffset + 62, yOffset - 5, 0.0F, 0.0F, 200, 60, 200.0F, 60.0F);
      }

      int firstRowHeight = 67;
      int secondRowHeight = 103;
      this.drawPokemon(this.party[0], xOffset, yOffset, 12, 65, 15, firstRowHeight);
      this.drawPokemon(this.party[1], xOffset, yOffset, 58, 65, 61, firstRowHeight);
      this.drawPokemon(this.party[2], xOffset, yOffset, 105, 65, 108, firstRowHeight);
      this.drawPokemon(this.party[3], xOffset, yOffset, 12, 101, 15, secondRowHeight);
      this.drawPokemon(this.party[4], xOffset, yOffset, 58, 101, 61, secondRowHeight);
      this.drawPokemon(this.party[5], xOffset, yOffset, 105, 101, 108, secondRowHeight);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedexItemIcon);
      Gui.func_146110_a(xOffset + 169, yOffset + 108, 0.0F, 0.0F, 14, 14, 14.0F, 14.0F);
      this.field_146289_q.func_78276_b(this.caughtCount + "/" + Pokedex.pokedexSize, xOffset + 188, yOffset + 112, 1);
      this.field_146289_q.func_78276_b("UUID: " + this.player.func_110124_au().toString(), xOffset + 46, yOffset + 153, 1);
      this.field_146289_q.func_78276_b(this.player.func_70005_c_(), xOffset + 10, yOffset + 55, 1);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pokedollar);
      Gui.func_146110_a(xOffset + 169, yOffset + 84, 0.0F, 0.0F, 6, 9, 6.0F, 9.0F);
      this.field_146289_q.func_78276_b(this.money, xOffset + 177, yOffset + 84, 1);
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   private void drawPokemon(Pokemon pokemon, int xOffset, int yOffset, int ballX, int ballY, int pokeX, int pokeY) {
      if (pokemon != null) {
         GuiHelper.bindPokeballTexture(pokemon.getCaughtBall());
         Gui.func_146110_a(xOffset + ballX, yOffset + ballY, 0.0F, 0.0F, 44, 43, 44.0F, 43.0F);
         if (pokemon.isEgg()) {
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation(GuiResources.prefix + "sprites/eggs/egg1.png"));
         } else {
            GuiHelper.bindPokemonSprite(pokemon, this.field_146297_k);
         }

         Gui.func_146110_a(xOffset + pokeX, yOffset + pokeY - 3, 0.0F, 0.0F, 40, 40, 40.0F, 40.0F);
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button instanceof ThemedButton) {
         this.color = ((ThemedButton)button).color;
         ClientData.color = this.color;
         Pixelmon.network.sendToServer(new TrainerCardColorPacket(this.color));
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      super.func_73869_a(typedChar, keyCode);
      if (keyCode == ClientProxy.trainerCardKeyBind.func_151463_i()) {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }

   public void func_146281_b() {
      this.field_146297_k.func_147118_V().func_147682_a(PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
   }

   public static class ThemedButton extends GuiButton {
      private static final ResourceLocation BUTTON;
      private EnumTrainerCardColor color;

      public ThemedButton(int buttonId, EnumTrainerCardColor color, int x, int y) {
         super(buttonId, x, y, 10, 10, "");
         this.color = color;
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         mc.field_71446_o.func_110577_a(BUTTON);
         GlStateManager.func_179124_c(this.color.red, this.color.green, this.color.blue);
         func_146110_a(this.field_146128_h, this.field_146129_i, 0.0F, 0.0F, this.field_146120_f, this.field_146121_g, (float)this.field_146120_f, (float)this.field_146121_g);
      }

      static {
         BUTTON = new ResourceLocation(GuiResources.prefix + "gui/trainercards/button.png");
      }
   }
}
