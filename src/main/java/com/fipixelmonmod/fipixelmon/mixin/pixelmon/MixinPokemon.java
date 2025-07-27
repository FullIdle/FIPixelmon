package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.PokemonBridge;
import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.fipixelmonmod.fipixelmon.helper.EnumUpdateTypeHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ClientSet;
import io.netty.buffer.ByteBuf;
import lombok.val;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Pokemon.class, remap = false)
public abstract class MixinPokemon implements PokemonBridge {
    @Shadow public abstract void markDirty(EnumUpdateType... dataTypes);

    @Unique
    private EnumTeraType fIPixelmon$teraType;

    @Inject(
            method = "writeToNBT",
            at = @At("TAIL")
    )
    public void writeToNBT(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> cir) {
        if (this.fIPixelmon$teraType != null)
            nbt.setByte(EnumTeraType.TERA_TYPE_KEY, (byte) this.fIPixelmon$teraType.ordinal());
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    public void readFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey(EnumTeraType.TERA_TYPE_KEY))
            fIPixelmon$setTeraType(EnumTeraType.getEnumTeraTypeFormId(nbt.getByte(EnumTeraType.TERA_TYPE_KEY)));
    }

    @Override
    public void fIPixelmon$setTeraType(EnumTeraType teraType) {
        fIPixelmon$teraType = teraType;
        markDirty(EnumUpdateTypeHelper.getTeraType());
    }

    @Override
    public EnumTeraType fIPixelmon$getTeraType() {
        return fIPixelmon$teraType;
    }

    /**
     * 增加一个用于 {@link ClientSet} 包发送
     * 写入太晶属性数据
     */
    @Inject(
            method = "writeToByteBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/buffer/ByteBuf;writeLong(J)Lio/netty/buffer/ByteBuf;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    public void writeToByteBuffer(ByteBuf buf, EnumUpdateType[] data, CallbackInfo ci) {
        if (ArrayUtils.contains(data, EnumUpdateTypeHelper.getTeraType()))
            buf.writeByte(fIPixelmon$teraType == null ? (byte) -1 : ((byte) fIPixelmon$teraType.ordinal()));
    }

    /**
     * 同上
     */
    @Inject(
            method = "readFromByteBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/buffer/ByteBuf;readLong()J",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    public void readFromByteBuffer(ByteBuf buf, EnumUpdateType[] data, CallbackInfoReturnable<Pokemon> cir) {
        if (ArrayUtils.contains(data, EnumUpdateTypeHelper.getTeraType())) {
            val id = buf.readByte();
            fIPixelmon$teraType = id == -1 ? null : EnumTeraType.getEnumTeraTypeFormId(id);
        }
    }
}
