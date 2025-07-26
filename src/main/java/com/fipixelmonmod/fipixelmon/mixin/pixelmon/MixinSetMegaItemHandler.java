package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemHelper;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.val;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = SetMegaItem.Handler.class,
        remap = false
)
public class MixinSetMegaItemHandler {
    @Inject(
            method = "onMessage(Lcom/pixelmonmod/pixelmon/comm/packetHandlers/SetMegaItem;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMessage(SetMegaItem message, MessageContext ctx, CallbackInfoReturnable<IMessage> cir) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
        val megaItem = (EnumMegaItem) ReflectionHelper.getPrivateValue(SetMegaItem.class, message, "megaItem");
        val megaItemsUnlocked = storage.getMegaItemsUnlocked();
        if (storage.canEquipMegaItem() &&
                (megaItem.canMega() && megaItemsUnlocked.canMega()
                        || megaItem.canDynamax() && megaItemsUnlocked.canDynamax()
                        || EnumMegaItemHelper.isTerastal(megaItem) && EnumMegaItemsUnlockedHelper.canTerastal(megaItemsUnlocked) ||
                        megaItem == EnumMegaItem.None
                )
        ) {
            storage.setMegaItem(megaItem, false);
        }

        cir.setReturnValue(null);
        cir.cancel();
        return;
    }
}
