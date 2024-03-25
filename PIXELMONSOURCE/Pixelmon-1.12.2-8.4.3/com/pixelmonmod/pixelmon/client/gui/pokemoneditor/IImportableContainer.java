package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import net.minecraft.client.gui.GuiScreen;

public interface IImportableContainer {
   String getExportText();

   String importText(String var1);

   GuiScreen getScreen();
}
