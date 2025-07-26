package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.PlayerPartyStorageBridge;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemHelper;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerPartyStorage.class, remap = false)
public abstract class MixinPlayerPartyStorage implements PlayerPartyStorageBridge {
    @Shadow
    public abstract EnumMegaItemsUnlocked getMegaItemsUnlocked();

    @Shadow
    public abstract void unlockMega();

    @Shadow
    public abstract void setMegaItemsUnlocked(EnumMegaItemsUnlocked megaItemsUnlocked);

    @Shadow
    public EnumTrainerCardColor trainerCardColor;

    @Shadow
    public abstract void setMegaItem(EnumMegaItem megaItem, boolean giveChoice);

    @Override
    public void fIPixelmon$unlockTerastal() {
        val megaItemsUnlocked = getMegaItemsUnlocked();
        val canMega = megaItemsUnlocked.canMega();
        val canDynamax = megaItemsUnlocked.canDynamax();
        //两个都没有
        if (!canMega && !canDynamax) {
            setMegaItemsUnlocked(EnumMegaItemsUnlockedHelper.TERASTAL);
            //只有这一个的时候第一次解锁会进行提示 会打开选择界面
            setMegaItem(EnumMegaItemHelper.TERASTAL, true);
            return;
        }
        if (canMega && canDynamax) {
            setMegaItemsUnlocked(EnumMegaItemsUnlockedHelper.THREE);
            return;
        }
        if (canMega) {
            setMegaItemsUnlocked(EnumMegaItemsUnlockedHelper.BOTH_MEGA);
            return;
        }
        setMegaItemsUnlocked(EnumMegaItemsUnlockedHelper.BOTH_DYN);
    }
}
