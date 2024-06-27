package com.fipixelmonmod.fipixelmon.common.util;

import com.pixelmonmod.pixelmon.client.gui.*;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesFixed;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.GuiChooseMoveset;
import com.pixelmonmod.pixelmon.client.gui.curryDex.GuiCurryDex;
import com.pixelmonmod.pixelmon.client.gui.custom.GuiInputScreen;
import com.pixelmonmod.pixelmon.client.gui.custom.dialogue.GuiDialogue;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLog;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLogInformation;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLogMenu;
import com.pixelmonmod.pixelmon.client.gui.mail.GuiMail;
import com.pixelmonmod.pixelmon.client.gui.npc.*;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.*;
import com.pixelmonmod.pixelmon.client.gui.pc.GuiPC;
import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiEditedPlayer;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import com.pixelmonmod.pixelmon.client.gui.ranchblock.GuiExtendRanch;
import com.pixelmonmod.pixelmon.client.gui.ranchblock.GuiRanchBlock;
import com.pixelmonmod.pixelmon.client.gui.selectMove.SelectMoveScreen;
import com.pixelmonmod.pixelmon.client.gui.spawner.GuiPixelmonSpawner;
import com.pixelmonmod.pixelmon.client.gui.starter.GuiChooseStarter;
import com.pixelmonmod.pixelmon.client.gui.statueEditor.GuiStatueEditor;
import com.pixelmonmod.pixelmon.client.gui.vendingmachine.GuiVendingMachine;
import com.pixelmonmod.pixelmon.client.gui.zygarde.GuiZygardeCube;
import com.pixelmonmod.pixelmon.client.gui.zygarde.GuiZygardeReassemblyUnit;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.quests.client.editor.GuiQuestEditor;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import net.minecraft.client.gui.GuiScreen;

import static com.pixelmonmod.pixelmon.enums.EnumGuiScreen.*;

public class GuiHelper {
    public static EnumGuiScreen getPixelmonGuiType(GuiScreen screen) {
        if (screen == null) {
            return null;
        }
        if (screen instanceof GuiChooseStarter) {
            return ChooseStarter;
        }
        if (screen instanceof GuiDialogue) {
            return Dialogue;
        }
        if (screen instanceof GuiHealer) {
            return Healer;
        }
        if (screen instanceof GuiPC) {
            return PC;
        }
        if (screen instanceof GuiSelectPartyPokemon) {
            return PickPokemon;
        }
        if (screen instanceof GuiPokedex) {
            return Pokedex;
        }
        if (screen instanceof GuiBattle) {
            return Battle;
        }
        if (screen instanceof GuiTrading) {
            return Trading;
        }
        if (screen instanceof GuiDoctor) {
            return Doctor;
        }
        if (screen instanceof GuiItemDrops) {
            return ItemDrops;
        }
        if (screen instanceof GuiPixelmonSpawner) {
            return PixelmonSpawner;
        }
        if (screen instanceof GuiTrainerEditor) {
            return TrainerEditor;
        }
        if (screen instanceof GuiChooseMoveset) {
            return ChooseMoveset;
        }
        if (screen instanceof GuiRanchBlock) {
            return RanchBlock;
        }
        if (screen instanceof GuiTradeEditor) {
            return NPCTrade;
        }
        if (screen instanceof GuiNPCTrader) {
            return NPCTraderGui;
        }
        if (screen instanceof GuiStatueEditor) {
            return StatueEditor;
        }
        if (screen instanceof GuiExtendRanch) {
            return ExtendRanch;
        }
        if (screen instanceof GuiInputScreen) {
            return InputScreen;
        }
        if (screen instanceof GuiCreateNPC) {
            return CreateNpc;
        }
        if (screen instanceof GuiConfirmDeleteNPC) {
            return DeleteNpc;
        }
        if (screen instanceof GuiChattingNPCEditor) {
            return NPCChatEditor;
        }
        if (screen instanceof GuiChattingNPC) {
            return NPCChat;
        }
        if (screen instanceof GuiQuestGiverNPCEditor) {
            return NPCQuestGiverEditor;
        }
        if (screen instanceof GuiQuestGiverNPC) {
            return NPCQuestGiver;
        }
        if (screen instanceof GuiRelearner) {
            return Relearner;
        }
        if (screen instanceof GuiTutor) {
            return Tutor;
        }
        if (screen instanceof GuiTutorEditor) {
            return TutorEditor;
        }
        if (screen instanceof GuiHealer) {
            return HealerNurseJoy;
        }
        if (screen instanceof GuiShopkeeper) {
            return Shopkeeper;
        }
        if (screen instanceof GuiShopkeeperEditor) {
            return ShopkeeperEditor;
        }
        if (screen instanceof GuiVendingMachine) {
            return VendingMachine;
        }
        if (screen instanceof GuiPokemonEditorParty) {
            return PokemonEditor;
        }
        if (screen instanceof GuiEditedPlayer) {
            return EditedPlayer;
        }
        if (screen instanceof GuiMegaItem) {
            return MegaItem;
        }
        if (screen instanceof GuiBattleRulesPlayer) {
            return BattleRulesPlayer;
        }
        if (screen instanceof GuiBattleRulesFixed) {
            return BattleRulesFixed;
        }
        if (screen instanceof GuiTeamSelect) {
            return TeamSelect;
        }
        if (screen instanceof GuiMail) {
            return Mail;
        }
        if (screen instanceof SelectMoveScreen) {
            return SelectMove;
        }
        if (screen instanceof GuiCharm) {
            GuiCharm charm = (GuiCharm) screen;
            EnumCharms enumCharms = ReflectionHelper.getPrivateValue(GuiCharm.class, charm, "charm");
            if (enumCharms == EnumCharms.Shiny)
                return ShinyCharm;
            if (enumCharms == EnumCharms.Oval)
                return OvalCharm;
            if (enumCharms == EnumCharms.Exp)
                return ExpCharm;
            if (enumCharms == EnumCharms.Catching)
                return CatchingCharm;
            if (enumCharms == EnumCharms.Mark){
                return MarkCharm;
            }
        }
        if (screen instanceof GuiSelectStat) {
            return BottleCap;
        }
        if (screen instanceof GuiCurryDex) {
            return CurryDex;
        }
        if (screen instanceof GuiFishingLog) {
            return FishingLog;
        }
        if (screen instanceof GuiFishingLogMenu) {
            return FishingLogMenu;
        }
        if (screen instanceof GuiFishingLogInformation) {
            return FishingLogInformation;
        }
        if (screen instanceof GuiZygardeReassemblyUnit) {
            return ZygardeAssembly;
        }
        if (screen instanceof GuiZygardeCube) {
            return ZygardeCube;
        }
        if (screen instanceof GuiQuestEditor) {
            return QuestEditor;
        }
        return null;
    }
}
