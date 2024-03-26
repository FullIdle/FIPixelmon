package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.adapter.EnumSpeciesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimationTypeAdapter;
import com.pixelmonmod.pixelmon.battles.attacks.*;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationDataAdapter;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.ResourceLocationAdapter;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AttackBase.class,remap = false)
public class MixinAttackBase {
    @Mutable
    @Shadow
    @Final
    public static Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            .registerTypeAdapter(EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
            .registerTypeAdapter(EffectBase.class, new EffectTypeAdapter())
            .registerTypeAdapter(AttackAnimation.class, AttackAnimationTypeAdapter.ADAPTER)
            .registerTypeAdapter(AttackAnimationData.class, AttackAnimationDataAdapter.ADAPTER)
            .registerTypeAdapter(MoveFlags.class, MoveFlags.ADAPTER)
            .registerTypeAdapter(ResourceLocation.class, ResourceLocationAdapter.ADAPTER)
            .registerTypeAdapter(ZMove.class, new ZMoveAdapter())
            .create();
}
