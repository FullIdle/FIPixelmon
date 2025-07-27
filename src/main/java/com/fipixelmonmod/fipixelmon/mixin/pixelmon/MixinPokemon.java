package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.PokemonBridge;
import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Pokemon.class, remap = false)
public class MixinPokemon implements PokemonBridge {
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
        this.fIPixelmon$teraType = teraType;
    }

    @Override
    public EnumTeraType fIPixelmon$getTeraType() {
        return this.fIPixelmon$teraType;
    }
}
