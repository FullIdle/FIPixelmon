package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiRoundButton;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.AcceptDeclineBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleQueryPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EnumBattleQueryResponse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class GuiAcceptDeny extends GuiVersus {
   public static BattleQueryPacket opponent;
   boolean accepted = false;
   boolean changeRules = false;
   int battleQueryID;
   private GuiRoundButton acceptButton;
   private GuiRoundButton denyButton;
   private GuiRoundButton rulesButton;
   private boolean responded;

   public GuiAcceptDeny(int battleQueryID) {
      this.battleQueryID = battleQueryID;
      int acceptDenyHeight = 152;
      this.acceptButton = new GuiRoundButton(25, acceptDenyHeight, I18n.func_74838_a("gui.acceptdeny.accept"));
      this.denyButton = new GuiRoundButton(145, acceptDenyHeight, I18n.func_74838_a("gui.acceptdeny.deny"));
      this.rulesButton = new GuiRoundButton(85, 182, I18n.func_74838_a("gui.battlerules.title"));
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      super.func_73863_a(mouseX, mouseY, f);
      if (opponent == null) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         EntityPlayer opponentEntity = this.field_146297_k.field_71441_e.func_152378_a(opponent.opponentUUID);
         if (opponentEntity == null) {
            this.field_146297_k.field_71439_g.func_71053_j();
         } else {
            int[] pokeballs1 = new int[6];

            int i;
            for(i = 0; i < 6; ++i) {
               Pokemon pokemon = ClientStorageManager.party.get(i);
               if (pokemon != null) {
                  pokeballs1[i] = pokemon.getCaughtBall().getIndex();
                  if (pokemon.getHealth() <= 0) {
                     pokeballs1[i] = pokeballs1[i] * -1 - 1;
                  }
               } else {
                  pokeballs1[i] = -999;
               }
            }

            this.drawPokeBalls(pokeballs1, this.playerPartyX, this.playerPartyY, this.offset1);
            i = 0;
            int[] var11 = opponent.pokeballs;
            int var8 = var11.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               int pokeBall = var11[var9];
               if (pokeBall > -1) {
                  ++i;
               }
            }

            this.drawOpponentPokeBalls(i);
            GlStateManager.func_179140_f();
            GlStateManager.func_179147_l();
            if (this.ticks == 0) {
               this.acceptButton.drawButton(this.leftX, this.topY, mouseX, mouseY, this.field_73735_i, this.accepted);
               this.denyButton.drawButton(this.leftX, this.topY, mouseX, mouseY, this.field_73735_i);
               this.rulesButton.drawButton(this.leftX, this.topY, mouseX, mouseY, this.field_73735_i, this.changeRules);
            }

            GlStateManager.func_179084_k();
         }
      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int par3) {
      EnumBattleQueryResponse response = null;
      if (!this.accepted && this.acceptButton.isMouseOver(this.leftX, this.topY, mouseX, mouseY)) {
         this.accepted = true;
         this.changeRules = false;
         response = EnumBattleQueryResponse.Accept;
      } else if (this.denyButton.isMouseOver(this.leftX, this.topY, mouseX, mouseY)) {
         this.field_146297_k.field_71439_g.func_71053_j();
         response = EnumBattleQueryResponse.Decline;
      } else if (!this.changeRules && this.rulesButton.isMouseOver(this.leftX, this.topY, mouseX, mouseY)) {
         this.accepted = false;
         this.changeRules = true;
         response = EnumBattleQueryResponse.Rules;
      }

      if (response != null) {
         this.responded = true;
         Pixelmon.network.sendToServer(new AcceptDeclineBattle(this.battleQueryID, response));
      }

   }

   public void func_146281_b() {
      if (!this.responded) {
         Pixelmon.network.sendToServer(new AcceptDeclineBattle(this.battleQueryID, EnumBattleQueryResponse.Decline));
      }

   }

   protected EntityLivingBase getOpponent() {
      return this.field_146297_k.field_71441_e.func_152378_a(opponent.opponentUUID);
   }
}
