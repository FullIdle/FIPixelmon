package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(value = GuiScreenPokeChecker.class, remap = false)
public abstract class MixinGuiScreenPokeChecker extends GuiScreen {
    @Shadow
    protected Pokemon pokemon;

    @Shadow
    @Final
    private static Color[] DYNAMAX_LEVELS;

    @Shadow
    protected abstract void drawBasePokemonInfo();

    @Shadow
    public abstract void drawHealthBar(int x, int y, int width, int height, Pokemon pokemon);

    @Shadow
    protected abstract void drawExpBar(int x, int y, int width, int height, Pokemon pokemon);

    @Shadow
    protected int xSize;

    @Shadow
    protected int ySize;

    @Shadow
    protected abstract void drawPokemonName();

    @Shadow
    public abstract void drawArrows(int mouseX, int mouseY);

    /**
     * @author FIGSQ
     * @reason 增加太晶内容
     */
    @Overwrite
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        if (!this.pokemon.isEgg()) {
            this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.lvl") + " " + this.pokemon.getLevel(), 10, -14, 16777215);
            this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.number") + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, 16777215);
            if (this.pokemon.getHealth() <= 0) {
                this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.fainted"), 117, -13, 16777215);
            } else {
                this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getHealth() + "/" + this.pokemon.getMaxHealth(), 140, -14, 14540253);
            }
        } else {
            this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.lvl") + " ???", 10, -14, 16777215);
            this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.number") + " ???", -30, -14, 16777215);
            this.drawCenteredString(this.mc.fontRenderer, "???/???", 140, -13, 14540253);
        }

        this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.status"), -9, 111, 16777215);
        this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.texp"), 107, 32, 16777215);
        this.drawCenteredString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.levelup"), 134, 56, 16777215);
        this.drawCenteredString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.dynamaxlevel"), 134, 80, 16777215);
        if (!this.pokemon.isEgg()) {
            String xp = String.valueOf(this.pokemon.getExperience());
            String xptl = String.valueOf(this.pokemon.getExperienceToLevelUp());
            this.mc.fontRenderer.drawString(xp, 135 - this.mc.fontRenderer.getStringWidth(xp) / 2, 44, 16777215);
            this.mc.fontRenderer.drawString(xptl, 135 - this.mc.fontRenderer.getStringWidth(xptl) / 2, 68, 16777215);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.mc.renderEngine.bindTexture(GuiResources.parallelogram);

            for (int i = 0; i < 10; ++i) {
                if (this.pokemon.getDynamaxLevel() > i) {
                    Color c = DYNAMAX_LEVELS[i];
                    GlStateManager.color((float) c.getRed() / 256.0F, (float) c.getGreen() / 256.0F, (float) c.getBlue() / 256.0F, 1.0F);
                } else {
                    GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                }

                GuiHelper.drawImageQuad(82 + i * 10, 91.0F, 13.0F, 13.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.zLevel);
            }

            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            this.drawCenteredString(this.mc.fontRenderer, "???", 135, 44, 16777215);
            this.drawCenteredString(this.mc.fontRenderer, "???", 135, 68, 16777215);
        }

        this.drawString(this.mc.fontRenderer, I18n.format("gui.screenpokechecker.ability"), 75, 116, 16777215);
        if (!this.pokemon.isEgg()) {
            try {
                this.drawString(this.mc.fontRenderer, I18n.format("ability." + this.pokemon.getAbility().getName() + ".name"), 130, 116, 16777215);
                this.mc.fontRenderer.drawSplitString(I18n.format("ability." + this.pokemon.getAbility().getName() + ".description"), 62, 131, 145, 16777215);
            } catch (Exception var7) {
                this.drawString(this.mc.fontRenderer, I18n.format("ability.ComingSoon.name"), 130, 117, 16777215);
                this.mc.fontRenderer.drawSplitString(I18n.format("ability.ComingSoon.description"), 62, 131, 145, 16777215);
            }
        } else {
            this.drawString(this.mc.fontRenderer, I18n.format("ability.Egg.name"), 130, 116, 16777215);
            this.mc.fontRenderer.drawSplitString(I18n.format(this.pokemon.getEggDescription()), 62, 131, 145, 16777215);
        }

        this.drawBasePokemonInfo();
    }

    /**
     * @author FIGSQ
     * @reason 增加太晶内容
     */
    @Overwrite
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.mc.renderEngine.bindTexture(GuiResources.summarySummary);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 - 40, (this.height - this.ySize) / 2 - 25, 0, 0, 256, 205);
        this.drawHealthBar((this.width - this.xSize) / 2 + 59, (this.height - this.ySize) / 2 - 18, 154, 14, this.pokemon);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 59, (this.height - this.ySize) / 2 - 18, 103, 222, 150, 16);
        this.drawExpBar((this.width - this.xSize) / 2 + 86, (this.height - this.ySize) / 2, 122, 14, this.pokemon);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 59, (this.height - this.ySize) / 2, 104, 239, 150, 16);
        float[] texturePair = StatusType.getTexturePos(this.pokemon.getStatus().type);
        float textureX1 = texturePair[0];
        float textureY1 = texturePair[1];
        this.mc.renderEngine.bindTexture(GuiResources.status);
        if (textureX1 != -1.0F) {
            GuiHelper.drawImageQuad((float) (this.width - this.xSize) / 2.0F - 6.0F, (float) (this.height - this.ySize) / 2.0F + 130.0F, 24.0F, 24.0F, textureX1 / 768.0F, textureY1 / 512.0F, (textureX1 + 240.0F) / 768.0F, (textureY1 + 240.0F) / 512.0F, this.zLevel);
        } else {
            GuiHelper.drawImageQuad((float) (this.width - this.xSize) / 2.0F - 6.0F, (float) (this.height - this.ySize) / 2.0F + 130.0F, 24.0F, 24.0F, 0.34375F, 0.515625F, 0.65625F, 0.984375F, this.zLevel);
        }

        this.drawPokemonName();
        this.drawArrows(mouseX, mouseY);
        GlStateManager.disableBlend();
    }
}
