package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = Pokemon.class, remap = false)
public class MixinPokemon extends PokemonBase {
    /**
     * 我感觉不安全~
     * 让特性设置slot无限制
     * 待实际使用
     *
     * @param i 原版重铸 == 2
     * @return 最终限制的数量
     */
    @ModifyConstant(
            method = "setAbilitySlot",
            constant = @Constant(intValue = 2)
    )
    private int limitMaxInjected(int i) {
        return this.getBaseStats().getAbilitiesArray().length;
    }
}
