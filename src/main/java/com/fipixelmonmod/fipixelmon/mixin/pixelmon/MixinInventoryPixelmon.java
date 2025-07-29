package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.GuiMegaItemBridge;
import com.fipixelmonmod.fipixelmon.bridge.IInventoryPixelmonBridge;
import com.fipixelmonmod.fipixelmon.bridge.SlotInventoryPixelmonBridge;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemHelper;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper;
import com.fipixelmonmod.fipixelmon.helper.ResourcesHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonPokeChecker;
import com.pixelmonmod.pixelmon.client.gui.inventory.SlotInventoryPixelmon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetCharm;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetHeldItem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.ChangeLurePacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerSwap;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.ClientData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.text.NumberFormat;

@Mixin(
        targets = "com.pixelmonmod.pixelmon.client.gui.inventory.InventoryPixelmon",
        remap = false
)
public abstract class MixinInventoryPixelmon<T extends InventoryEffectRenderer & IInventoryPixelmonBridge> {
    @Shadow
    private T gui;

    @Shadow
    private SlotInventoryPixelmon[] pixelmonSlots;

    @Shadow
    private int partyWidth;

    @Shadow
    private StoragePosition selected;

    @Shadow
    private int[] lureSlot;

    @Shadow
    private int drawerOffset;

    @Shadow
    private boolean drawerOut;

    @Shadow private int ticksTillClick;

    @Shadow private GuiButtonPokeChecker pokeChecker;

    @Shadow protected abstract void reloadSlots();

    @Shadow protected abstract boolean heldItemQualifies(Pokemon pokemon);

    /**
     * @author FIGSQ
     * @reason 不建议得写法，等我想到更好得方式修改我就换掉！
     */
    @Overwrite
    void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.gui.mc.entityRenderer.setupOverlayRendering();
        this.gui.mc.fontRenderer.setUnicodeFlag(true);

        for (SlotInventoryPixelmon slot : this.pixelmonSlots) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (slot != null) {
                Pokemon pokemon = ClientStorageManager.party.get(slot.position);
                if (pokemon != null) {
                    ((SlotInventoryPixelmonBridge) slot).fIPixelmon$setX(this.gui.fIPixelmon$getGUILeft() - this.partyWidth + 8);
                    GuiHelper.bindPokemonSprite(pokemon, this.gui.mc);
                    GlStateManager.disableLighting();
                    GuiHelper.drawImageQuad(slot.x, slot.y, 16.0F, 16.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    if (!pokemon.isEgg()) {
                        if (pokemon.getHeldItem() != ItemStack.EMPTY) {
                            this.gui.mc.getRenderItem().renderItemIntoGUI(pokemon.getHeldItem(), slot.heldItemX, slot.heldItemY);
                        } else {
                            this.gui.mc.renderEngine.bindTexture(GuiResources.heldItem);
                            GuiHelper.drawImageQuad(slot.heldItemX + 3, slot.heldItemY + 3, 10.0F, 10.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                        }
                    }

                    if (slot.position.equals(this.selected)) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.pcResources);
                        GuiHelper.drawImageQuad((float) slot.x + 0.5F, (float) slot.y + 0.5F, 15.0F, 15.0F, 0.0F, 0.11328125F, 0.11328125F, 0.2265625F, this.gui.fIPixelmon$getZLevel());
                    }
                }
            }
        }

        if ((ClientStorageManager.party.getLure() != null || this.gui.mc.player.inventory.getItemStack().getItem() instanceof ItemLure) && mouseX >= this.lureSlot[0] && mouseX <= this.lureSlot[0] + 16 && mouseY >= this.lureSlot[1] && mouseY <= this.lureSlot[1] + 16) {
            this.gui.mc.renderEngine.bindTexture(GuiResources.pixelmonOverlayExtended2);
            GuiHelper.drawImageQuad(this.lureSlot[0] - 1, this.lureSlot[1] - 1, 18.0F, 18.0F, 0.23046875F, 0.7265625F, 0.30078125F, 0.796875F, this.gui.fIPixelmon$getZLevel());
        }

        this.gui.mc.renderEngine.bindTexture(GuiResources.pixelmonOverlayExtended2);
        int bX = this.gui.fIPixelmon$getGUILeft() - this.partyWidth;
        int bY = this.gui.height / 2;
        if (this.drawerOffset > 0) {
            //点开后得背景
            GuiHelper.drawImageQuad(
                    bX - 16 - this.drawerOffset, //向左移动 14
                    bY - 83,
                    21 + this.drawerOffset, //宽度增加 14
                    166.0F,
                    0.5546875F, //原0.609375F 现 0.5546875
                    0.0F,
                    (163.0F + (float) this.drawerOffset) / 256.0F,
                    0.6484375F,
                    this.gui.fIPixelmon$getZLevel()
            );
            //箭头
            if (this.drawerOut && mouseX >= bX - 38 && mouseX <= bX - 28 && mouseY >= bY - 18 && mouseY <= bY + 18) {
                GuiHelper.drawImageQuad(bX - 35, bY - 5, 5.0F, 10.0F, 0.01953125F, 0.671875F, 0.0390625F, 0.7109375F, this.gui.fIPixelmon$getZLevel());
            }

            //渲染得项目 (就是会覆盖背景得带色彩得项)
            if (this.drawerOffset > 40) {
                EnumFeatureState markCharm = EntityPlayerExtension.getPlayerMarkCharm(this.gui.mc.player);
                if (markCharm.isAvailable()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.markCharmBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 55, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!markCharm.isActive()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 55, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                EnumFeatureState catchingCharm = EntityPlayerExtension.getPlayerCatchingCharm(this.gui.mc.player);
                if (catchingCharm.isAvailable()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.catchingCharmBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 32, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!catchingCharm.isActive()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 32, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                EnumFeatureState expCharm = EntityPlayerExtension.getPlayerExpCharm(this.gui.mc.player);
                if (expCharm.isAvailable()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.expCharmBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 9, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!expCharm.isActive()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY + 9, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                EnumFeatureState shinyCharm = EntityPlayerExtension.getPlayerShinyCharm(this.gui.mc.player);
                if (shinyCharm.isAvailable()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.shinyCharmBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 14, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!shinyCharm.isActive()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 14, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                EnumFeatureState ovalCharm = EntityPlayerExtension.getPlayerOvalCharm(this.gui.mc.player);
                if (ovalCharm.isAvailable()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.ovalCharmBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 36, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!ovalCharm.isActive()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 36, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                EnumMegaItemsUnlocked megaItems = EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.mc.player);
                if (megaItems.canMega()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.keyStoneBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 59, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!EntityPlayerExtension.getPlayerMegaItem(this.gui.mc.player).canMega()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 59, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                if (megaItems.canDynamax()) {
                    this.gui.mc.renderEngine.bindTexture(GuiResources.dynamaxBandBig);
                    GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 81, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!EntityPlayerExtension.getPlayerMegaItem(this.gui.mc.player).canDynamax()) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX + 17 - this.drawerOffset, bY - 81, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }

                /*
                  太晶项
                 */
                if (EnumMegaItemsUnlockedHelper.canTerastal(megaItems)) {
                    this.gui.mc.renderEngine.bindTexture(ResourcesHelper.TERA_ORB_ITEM);
                    GuiHelper.drawImageQuad(bX - 11 - this.drawerOffset, bY - 81, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    if (!EnumMegaItemHelper.isTerastal(EntityPlayerExtension.getPlayerMegaItem(this.gui.mc.player))) {
                        this.gui.mc.renderEngine.bindTexture(GuiResources.disabled);
                        GuiHelper.drawImageQuad(bX - 11 - this.drawerOffset, bY - 81, 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, this.gui.fIPixelmon$getZLevel());
                    }
                }
            }
        } else if (mouseX >= bX - 9 && mouseX <= bX + 1 && mouseY >= bY - 18 && mouseY <= bY + 18) {
            GuiHelper.drawImageQuad(bX - 6, bY - 5, 5.0F, 10.0F, 0.0F, 0.671875F, 0.01953125F, 0.7109375F, this.gui.fIPixelmon$getZLevel());
        }

        if (this.drawerOffset > 0) {
            this.gui.mc.renderEngine.bindTexture(GuiResources.pixelmonOverlayExtended2);
            GuiHelper.drawImageQuad(bX, bY - 83, 5.0F, 166.0F, 0.1484375F, 0.0F, 0.16796875F, 0.6484375F, this.gui.fIPixelmon$getZLevel());
        }

        GuiHelper.drawStringRightAligned(NumberFormat.getInstance().format(ClientData.playerMoney), (float) (this.gui.fIPixelmon$getGUILeft() - this.partyWidth + 42), (float) this.gui.height / 2.0F + 66.0F, 15790320, false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.gui.mc.fontRenderer.setUnicodeFlag(false);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
    }

    /**
     * @author FIGSQ
     * @reason 不好得方式以后有机会改掉!
     */
    @Overwrite
    boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= this.lureSlot[0] && mouseX <= this.lureSlot[0] + 16 && mouseY >= this.lureSlot[1] && mouseY <= this.lureSlot[1] + 16 && this.ticksTillClick <= 0) {
            ItemStack held = this.gui.mc.player.inventory.getItemStack();
            ItemStack lure = ClientStorageManager.party.getLureStack();
            boolean creative = this.gui.mc.player.capabilities.isCreativeMode;
            if (!held.isEmpty() || !lure.isEmpty()) {
                if (held.getItem() instanceof ItemLure) {
                    if (lure.isEmpty()) {
                        ClientStorageManager.party.setLureStack(lure);
                        this.gui.mc.player.inventory.setItemStack(ItemStack.EMPTY);
                        Pixelmon.network.sendToServer(creative ? new ChangeLurePacket(ChangeLurePacket.Change.PUT, held.getItem(), held.getItemDamage()) : new ChangeLurePacket(ChangeLurePacket.Change.PUT));
                    } else {
                        this.gui.mc.player.inventory.setItemStack(lure);
                        ClientStorageManager.party.setLureStack(held);
                        Pixelmon.network.sendToServer(creative ? new ChangeLurePacket(ChangeLurePacket.Change.SWAP, held.getItem(), held.getItemDamage()) : new ChangeLurePacket(ChangeLurePacket.Change.SWAP));
                    }
                } else if (!lure.isEmpty() && held.isEmpty()) {
                    this.gui.mc.player.inventory.setItemStack(lure);
                    ClientStorageManager.party.setLureStack(ItemStack.EMPTY);
                    Pixelmon.network.sendToServer(new ChangeLurePacket(ChangeLurePacket.Change.TAKE));
                }

                this.ticksTillClick = 10;
            }
            return false;
        } else if (this.pokeChecker.isMouseOver()) {
            if (mouseButton == 0) {
                this.pokeChecker.mouseClicked(mouseX, mouseY);
            }

            return false;
        } else {
            for(int i = 0; i < this.pixelmonSlots.length; ++i) {
                SlotInventoryPixelmon slot = this.pixelmonSlots[i];
                StoragePosition position = slot == null ? null : slot.position;
                Pokemon pokemon = position == null ? null : ClientStorageManager.party.get(position);
                int slotX;
                int slotY;
                if (slot != null) {
                    slotX = slot.x;
                    slotY = slot.y;
                } else {
                    slotX = this.gui.fIPixelmon$getGUILeft() - this.partyWidth + 6;
                    slotY = this.gui.height / 2 + i * 18 - 75;
                }

                if (mouseX >= slotX && mouseX <= slotX + 16 && mouseY >= slotY && mouseY <= slotY + 16) {
                    if (mouseButton == 1) {
                        this.pokeChecker.setPokemon(ClientStorageManager.party, position, pokemon, mouseX, mouseY);
                    } else {
                        this.pokeChecker.setPokemon(null, null, null, mouseX, mouseY);
                    }

                    if (mouseButton == 0) {
                        if (this.selected == null) {
                            this.selected = position;
                        } else {
                            Pixelmon.network.sendToServer(new ServerSwap(this.selected, ClientStorageManager.party.get(this.selected), new StoragePosition(-1, i), pokemon));
                            ClientStorageManager.party.swap(this.selected.order, i);
                            this.selected = null;
                            this.reloadSlots();
                        }
                    }

                    return false;
                }

                if (this.ticksTillClick <= 0 && pokemon != null && !pokemon.isEgg() && this.heldItemQualifies(pokemon) && slot.getHeldItemBounds().contains(mouseX, mouseY)) {
                    SetHeldItem packet = new SetHeldItem(position, pokemon.getUUID());
                    InventoryPlayer inventory = this.gui.mc.player.inventory;
                    ItemStack currentItem = inventory.getItemStack();
                    ItemStack oldItem = pokemon.getHeldItem();
                    if (this.gui.mc.player.capabilities.isCreativeMode) {
                        if (!currentItem.isEmpty()) {
                            ItemStack singleItem = currentItem.copy();
                            singleItem.setCount(1);
                            pokemon.setHeldItem(singleItem);
                            packet.setItem(currentItem.getItem());
                        } else {
                            pokemon.setHeldItem(ItemStack.EMPTY);
                            packet.setItem(null);
                        }
                    } else {
                        if (oldItem.isEmpty()) {
                            if (!currentItem.isEmpty()) {
                                ItemStack singleItem = currentItem.copy();
                                singleItem.setCount(1);
                                pokemon.setHeldItem(singleItem);
                                if (currentItem.getCount() <= 1) {
                                    inventory.setItemStack(ItemStack.EMPTY);
                                } else {
                                    currentItem.shrink(1);
                                }
                            }
                        } else if (currentItem.isEmpty()) {
                            pokemon.setHeldItem(ItemStack.EMPTY);
                            inventory.setItemStack(oldItem);
                        } else if (ItemStack.areItemsEqual(oldItem, currentItem) && ItemStack.areItemStackTagsEqual(oldItem, currentItem)) {
                            pokemon.setHeldItem(ItemStack.EMPTY);
                            currentItem.grow(1);
                        } else if (currentItem.getCount() <= 1) {
                            ItemStack singleItem = currentItem.copy();
                            singleItem.setCount(1);
                            pokemon.setHeldItem(singleItem);
                            inventory.setItemStack(oldItem);
                        } else {
                            ItemStack singleItem = currentItem.copy();
                            singleItem.setCount(1);
                            pokemon.setHeldItem(singleItem);
                            currentItem.shrink(1);
                            inventory.addItemStackToInventory(oldItem);
                        }

                        ItemStack playerItem = inventory.getItemStack();
                        if (!playerItem.isEmpty() && playerItem.getCount() > 64) {
                            playerItem.setCount(64);
                        }
                    }

                    Pixelmon.network.sendToServer(packet);
                    this.ticksTillClick = 10;
                    return false;
                }
            }

            int bX = this.gui.fIPixelmon$getGUILeft() - this.partyWidth;
            int bY = this.gui.height / 2;
            if (this.drawerOut) {
                if (mouseX >= bX - 38 && mouseX <= bX - 28 && mouseY >= bY - 18 && mouseY <= bY + 18) {
                    this.drawerOut = false;
                    return false;
                }

                if (mouseX >= bX - 25 && mouseX <= bX - 1) {
                    if (mouseY > bY + 55 && mouseY < bY + 79) {
                        EnumFeatureState charm = EntityPlayerExtension.getPlayerMarkCharm(this.gui.mc.player);
                        if (charm.isAvailable()) {
                            Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Mark, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                        }

                        return false;
                    }

                    if (mouseY > bY + 33 && mouseY < bY + 55) {
                        EnumFeatureState charm = EntityPlayerExtension.getPlayerCatchingCharm(this.gui.mc.player);
                        if (charm.isAvailable()) {
                            Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Catching, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                        }

                        return false;
                    }

                    if (mouseY > bY + 9 && mouseY < bY + 33) {
                        EnumFeatureState charm = EntityPlayerExtension.getPlayerExpCharm(this.gui.mc.player);
                        if (charm.isAvailable()) {
                            Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Exp, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                        }

                        return false;
                    }

                    if (mouseY > bY - 13 && mouseY < bY + 9) {
                        EnumFeatureState charm = EntityPlayerExtension.getPlayerShinyCharm(this.gui.mc.player);
                        if (charm.isAvailable()) {
                            Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Shiny, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                        }

                        return false;
                    }

                    if (mouseY > bY - 35 && mouseY < bY - 13) {
                        EnumFeatureState charm = EntityPlayerExtension.getPlayerOvalCharm(this.gui.mc.player);
                        if (charm.isAvailable()) {
                            Pixelmon.network.sendToServer(new SetCharm(EnumCharms.Oval, charm.isActive() ? EnumFeatureState.Available : EnumFeatureState.Active));
                        }

                        return false;
                    }

                    if (mouseY > bY - 58 && mouseY < bY - 36) {
                        if (EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.mc.player).canMega()) {
                            this.gui.mc.displayGuiScreen(new GuiMegaItem(false));
                        }

                        return false;
                    }

                    if (mouseY > bY - 79 && mouseY < bY - 58) {
                        if (EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.mc.player).canDynamax()) {
                            this.gui.mc.displayGuiScreen(new GuiMegaItem(true));
                        }

                        return false;
                    }
                }
                //太晶珠点击
                if (mouseX >= bX - 54 && mouseX <= bX - 30) {
                    //靠左一行
                    if (mouseY > bY - 79 && mouseY < bY - 58) {
                        //和极具一个高度
                        if (EnumMegaItemsUnlockedHelper.canTerastal(EntityPlayerExtension.getPlayerMegaItemsUnlocked(this.gui.mc.player))) {
                            this.gui.mc.displayGuiScreen(fIPixelmon$CastTeraGui(new GuiMegaItem(true)));
                        }
                        return false;
                    }
                }
            } else if (mouseX >= bX - 9 && mouseX <= bX + 1 && mouseY >= bY - 18 && mouseY <= bY + 18) {
                this.drawerOut = true;
                return false;
            }

            return true;
        }
    }

    @Unique
    private GuiMegaItem fIPixelmon$CastTeraGui(GuiMegaItem gui) {
        ((GuiMegaItemBridge) gui).fIPixelmon$setTerastal(true);
        return gui;
    }
}
