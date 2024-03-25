package com.pixelmonmod.pixelmon.enums;

public enum EnumGuiScreen {
   ChooseStarter,
   Dialogue,
   Healer,
   PC,
   PickPokemon,
   Pokedex,
   Battle,
   Trading,
   Doctor,
   ItemDrops,
   CustomItemsDrop,
   PixelmonSpawner,
   TrainerEditor,
   ChooseMoveset,
   RanchBlock,
   NPCTrade,
   NPCTraderGui,
   StatueEditor,
   ExtendRanch,
   InputScreen,
   CreateNpc,
   DeleteNpc,
   NPCChatEditor,
   NPCChat,
   NPCQuestGiver,
   NPCQuestGiverEditor,
   Relearner,
   Tutor,
   TutorEditor,
   HealerNurseJoy,
   Shopkeeper,
   ShopkeeperEditor,
   VendingMachine,
   PokemonEditor,
   EditedPlayer,
   MegaItem,
   BattleRulesPlayer,
   BattleRulesFixed,
   TeamSelect,
   Mail,
   Badgecase,
   SelectMove,
   ShinyCharm,
   OvalCharm,
   ExpCharm,
   CatchingCharm,
   MarkCharm,
   BottleCap,
   FishingLog,
   FishingLogMenu,
   FishingLogInformation,
   ZygardeAssembly,
   ZygardeCube,
   QuestEditor,
   CurryDex;

   public int getIndex() {
      return this.ordinal();
   }

   public static boolean hasGUI(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public static EnumGuiScreen getFromOrdinal(int id) {
      return values()[id];
   }
}
