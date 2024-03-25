package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.log.DuelLog;
import com.pixelmonmod.tcg.duel.log.DuelLogItem;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDuelLog {
   private final Minecraft mc = Minecraft.func_71410_x();
   private final GuiTCG parent;

   public GuiDuelLog(GuiTCG parent) {
      this.parent = parent;
   }

   @SideOnly(Side.CLIENT)
   public void draw(DuelLog log, int mX, int mY, float zL) {
      if (this.parent != null && this.parent.controller != null) {
         int scaledHeight = this.parent.scaledHeight;
         this.mc.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/game/log/bar.png"));
         GlStateManager.func_179141_d();
         GlStateManager.func_179140_f();
         CardHelper.resetColour(1.0F);
         GuiHelper.drawImageQuad(10.0, (double)(this.parent.scaledHeight / 2 - 87), 28.0, 175.0F, 0.0, 0.0, 1.0, 1.0, zL);
         if (log != null) {
            List items = log.getItems(8);

            for(int i = 0; i < items.size(); ++i) {
               DuelLogItem item = (DuelLogItem)items.get(i);
               this.mc.field_71446_o.func_110577_a(item.getType().getResourceLocation());
               GuiHelper.drawImageQuad(15.0, (double)(scaledHeight / 2 - 87 + 5 + 21 * i), 18.0, 18.0F, 0.0, 0.0, 1.0, 1.0, zL);
               if (item.getPlayerSide() >= 0) {
                  try {
                     if (item.getPlayerSide() == this.parent.controller.getClient().getPlayerIndex()) {
                        this.mc.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/game/log/actions/self.png"));
                        GuiHelper.drawImageQuad(15.0, (double)(scaledHeight / 2 - 87 + 5 + 21 * i), 18.0, 18.0F, 0.0, 0.0, 1.0, 1.0, zL);
                     } else {
                        this.mc.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/game/log/actions/opponent.png"));
                        GuiHelper.drawImageQuad(15.0, (double)(scaledHeight / 2 - 87 + 5 + 21 * i), 18.0, 18.0F, 0.0, 0.0, 1.0, 1.0, zL);
                     }
                  } catch (NullPointerException var13) {
                     TCG.logger.error("There was an error rendering the battle log, please report this with the full log below!");
                     var13.printStackTrace();
                  }
               }

               if (mX > 15 && mX < 33 && mY > scaledHeight / 2 - 87 + 5 + 21 * i && mY < scaledHeight / 2 - 87 + 5 + 21 * i + 18) {
                  List lines = new ArrayList();
                  lines.add(item.toString());
                  String playerName = item.getPlayerSide() == this.parent.controller.getClient().getPlayerIndex() ? this.parent.controller.getClient().getMe().getPlayerName() : this.parent.controller.getClient().getOpponent().getPlayerName();
                  if (!this.parent.isSpectating && item.getPlayerSide() == this.parent.controller.getClient().getPlayerIndex()) {
                     playerName = "You";
                  }

                  String name;
                  switch (item.getType()) {
                     case ATTACK:
                        lines.remove(0);
                        lines.add(String.format("%s attacked %s.", LanguageMapTCG.translateKey(item.getAttackParameters().getAttacker().getData().getName().toLowerCase()), LanguageMapTCG.translateKey(item.getAttackParameters().getAttacking().getData().getName().toLowerCase())));
                        break;
                     case ABILITY:
                        ImmutableCard card = item.getAbilityParameters().getCard().getData();
                        lines.remove(0);
                        lines.add(String.format("%s used ability %s.", LanguageMapTCG.translateKey(card.getName().toLowerCase()), LanguageMapTCG.translateKey(card.getAbility().getName().toLowerCase())));
                        break;
                     case ATTACH:
                        lines.remove(0);
                        lines.add(String.format("%s attached %s to %s.", playerName, LanguageMapTCG.translateKey(item.getAttachCardParameters().getAttachment().getData().getName()), LanguageMapTCG.translateKey(item.getAttachCardParameters().getPokemon().getData().getName())));
                        break;
                     case DRAW:
                        lines.remove(0);
                        lines.add(String.format("%s drew %s card(s).", playerName, item.getDrawParameters().getNumber()));
                        break;
                     case DISCARD:
                        lines.remove(0);
                        name = "a card";
                        if (item.getDiscardParameters().getCard() != null) {
                           name = LanguageMapTCG.translateKey(item.getDiscardParameters().getCard().getData().getName());
                        }

                        lines.add(String.format("%s discarded %s.", playerName, name));
                        break;
                     case KNOCKOUT:
                        lines.remove(0);
                        lines.add(String.format("%s has been knocked out.", LanguageMapTCG.translateKey(item.getKnockoutParameters().getCard().getData().getName())));
                        break;
                     case PLAY:
                        lines.remove(0);
                        name = "a card";
                        if (item.getPlayParameters().getCard().getData() != null && item.getPlayParameters().getCard().getData().getID() != ImmutableCard.FACE_DOWN_ID) {
                           name = item.getPlayParameters().getCard().getData().getName();
                        }

                        lines.add(String.format("%s played %s.", playerName, LanguageMapTCG.translateKey(name)));
                        break;
                     case EVOLVE:
                        lines.remove(0);
                        lines.add(String.format(item.getPlayerSide() == this.parent.controller.getClient().getPlayerIndex() ? "Your %s evolved into %s." : "Opponent %s evolved into %s.", LanguageMapTCG.translateKey(item.getEvolveParameters().getFrom().getData().getName()), LanguageMapTCG.translateKey(item.getEvolveParameters().getTo().getData().getName())));
                        break;
                     case SWITCH:
                        lines.remove(0);
                        lines.add(String.format("%s switched in %s.", playerName, LanguageMapTCG.translateKey(item.getSwitchParameters().getCard().getData().getName())));
                        break;
                     case PASSTURN:
                        lines.remove(0);
                        lines.add(String.format("%s started new turn.", playerName));
                        break;
                     case STARTGAME:
                        lines.remove(0);
                        lines.add("The game is started.");
                  }

                  this.parent.drawHoveringText(lines, mX, mY, this.mc.field_71466_p, Energy.COLORLESS);
                  GlStateManager.func_179140_f();
                  CardHelper.resetColour(1.0F);
               }
            }
         }

         CardHelper.resetColour(1.0F);
         GlStateManager.func_179118_c();
      }
   }
}
