package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.GuiMegaItemBridge;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMegaItem.class)
public abstract class MixinGuiMegaItem extends GuiScreen implements GuiMegaItemBridge {
    @Shadow(remap = false)
    @Final
    private boolean isDynamax;
    @Shadow(remap = false)
    @Final
    private static ResourceLocation DYNAMAX_BAND;

    @Shadow(remap = false)
    protected abstract void drawIcon(int x, int y, int mouseX, int mouseY);

    @Shadow(remap = false)
    @Final
    private static ResourceLocation MEGA_BRACELET_ORAS;
    @Shadow(remap = false)
    @Final
    private static ResourceLocation MEGA_GLASSES;
    @Shadow(remap = false)
    @Final
    private static ResourceLocation MEGA_ANCHOR;
    @Shadow(remap = false)
    @Final
    private static ResourceLocation MEGA_TIARA;
    @Shadow(remap = false)
    @Final
    private static ResourceLocation MEGA_NECKLESS;

    @Shadow(remap = false)
    protected abstract void checkIconClick(int x, int y, int mouseX, int mouseY, EnumMegaItem item);

    @Unique
    private boolean fIPixelmon$isTerastal = false;

    @Override
    public boolean fIPixelmon$isTerastal() {
        return fIPixelmon$isTerastal;
    }

    @Override
    public GuiMegaItemBridge fIPixelmon$setTerastal(boolean terastal) {
        this.fIPixelmon$isTerastal = terastal;
        return this;
    }

    @Unique
    private static final ResourceLocation TERASTAL_BEAD = new ResourceLocation("fipixelmon", "textures/gui/megaItems/terastal_bead.png");

    /**
     * @author FIGSQ
     * @reason 多判断个Terastal
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiHelper.drawGradientRect(0, 0, this.zLevel, this.width, this.height, new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), true);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        String s = I18n.format(this.isDynamax ? this.fIPixelmon$isTerastal ?
                "您发现了一个太晶珠！想要准备它吗？" :
                "gui.dynamaxitem.message" :
                "gui.megaitem.message");
        this.drawCenteredString(this.mc.fontRenderer, s, centerX, centerY - 75, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        /*==>Terastal<==*/
        if (this.fIPixelmon$isTerastal) {
            GuiHelper.bindTexture(TERASTAL_BEAD);
            this.drawIcon(50, -10, mouseX, mouseY);
            this.mc.renderEngine.bindTexture(GuiResources.noItem);
            this.drawIcon(-40, -10, mouseX, mouseY);
            return;
        }
        /*== == == == ==*/

        if (this.isDynamax) {
            GuiHelper.bindTexture(DYNAMAX_BAND);
            this.drawIcon(50, -10, mouseX, mouseY);
            this.mc.renderEngine.bindTexture(GuiResources.noItem);
            this.drawIcon(-40, -10, mouseX, mouseY);
        } else {
            this.mc.renderEngine.bindTexture(MEGA_BRACELET_ORAS);
            this.drawIcon(-80, -10, mouseX, mouseY);
            this.mc.renderEngine.bindTexture(MEGA_GLASSES);
            this.drawIcon(5, -10, mouseX, mouseY);
            this.mc.renderEngine.bindTexture(MEGA_ANCHOR);
            this.drawIcon(90, -10, mouseX, mouseY);
            PixelExtrasData extras = PlayerExtraDataStore.get(Minecraft.getMinecraft().player);
            if (!extras.hasBoostedNecklace()) {
                this.mc.renderEngine.bindTexture(MEGA_TIARA);
                this.drawIcon(-40, 70, mouseX, mouseY);
                this.mc.renderEngine.bindTexture(GuiResources.noItem);
                this.drawIcon(50, 70, mouseX, mouseY);
            } else {
                this.mc.renderEngine.bindTexture(MEGA_TIARA);
                this.drawIcon(-80, 70, mouseX, mouseY);
                this.mc.renderEngine.bindTexture(MEGA_NECKLESS);
                this.drawIcon(5, 70, mouseX, mouseY);
                this.mc.renderEngine.bindTexture(GuiResources.noItem);
                this.drawIcon(90, 70, mouseX, mouseY);
            }
        }
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (this.fIPixelmon$isTerastal) {
            this.checkIconClick(50, -10, mouseX, mouseY, EnumMegaItemHelper.TERASTAL);
            this.checkIconClick(-40, -10, mouseX, mouseY, EnumMegaItem.None);
            ci.cancel();
            return;
        }
        if (this.isDynamax) {
            this.checkIconClick(50, -10, mouseX, mouseY, EnumMegaItem.DynamaxBand);
            this.checkIconClick(-40, -10, mouseX, mouseY, EnumMegaItem.None);
        } else {
            this.checkIconClick(-80, -10, mouseX, mouseY, EnumMegaItem.BraceletORAS);
            this.checkIconClick(5, -10, mouseX, mouseY, EnumMegaItem.MegaGlasses);
            this.checkIconClick(90, -10, mouseX, mouseY, EnumMegaItem.MegaAnchor);
            PixelExtrasData extras = PlayerExtraDataStore.get(Minecraft.getMinecraft().player);
            if (!extras.hasBoostedNecklace()) {
                this.checkIconClick(-40, 70, mouseX, mouseY, EnumMegaItem.MegaTiara);
                this.checkIconClick(50, 70, mouseX, mouseY, EnumMegaItem.None);
            } else {
                this.checkIconClick(-80, 70, mouseX, mouseY, EnumMegaItem.MegaTiara);
                this.checkIconClick(5, 70, mouseX, mouseY, EnumMegaItem.BoostNecklace);
                this.checkIconClick(90, 70, mouseX, mouseY, EnumMegaItem.None);
            }
        }

    }
}
