package com.pixelmonmod.tcg.api.loader;

import java.util.List;

public interface TCGLoader {
   List loadCards() throws Exception;

   List loadAttacks() throws Exception;

   List loadAbilities() throws Exception;

   List loadThemeDecks() throws Exception;

   List loadCardSets() throws Exception;
}
