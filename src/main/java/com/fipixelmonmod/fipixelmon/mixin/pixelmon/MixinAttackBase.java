package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.adapter.EnumHeldItemsAdapter;
import com.fipixelmonmod.fipixelmon.adapter.EnumSpeciesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimationTypeAdapter;
import com.pixelmonmod.pixelmon.battles.attacks.*;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationDataAdapter;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.util.ResourceLocationAdapter;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

@Mixin(value = AttackBase.class, remap = false)
public class MixinAttackBase {
    @Mutable
    @Shadow
    @Final
    public static Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            //增加物种适配器
            .registerTypeAdapter(EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
            .registerTypeAdapter(EffectBase.class, new EffectTypeAdapter())
            .registerTypeAdapter(AttackAnimation.class, AttackAnimationTypeAdapter.ADAPTER)
            .registerTypeAdapter(AttackAnimationData.class, AttackAnimationDataAdapter.ADAPTER)
            .registerTypeAdapter(MoveFlags.class, MoveFlags.ADAPTER)
            .registerTypeAdapter(ResourceLocation.class, ResourceLocationAdapter.ADAPTER)
            .registerTypeAdapter(ZMove.class, new ZMoveAdapter())
            .registerTypeAdapter(EnumHeldItems.class, EnumHeldItemsAdapter.INSTANCE)
            .create();

    @Shadow
    @Final
    public static transient ArrayList<AttackBase> ATTACKS;

    @Shadow
    @Final
    private static Map<String, AttackBase> ATTACK_MAP;

    @SneakyThrows
    @Inject(
            method = "loadAllAttacks",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/ArrayList;clear()V",
                    shift = At.Shift.AFTER
            )
    )
    private static void loadAllAttacks(CallbackInfo ci) {
        val files = new ArrayList<File>();
        RCFileHelper.recursiveJSONSearch(FIPixelmon.movesFolder.getPath(), files);
        for (File moveFile : files) {
            try {
                AttackBase ab = GSON.fromJson(new FileReader(moveFile), AttackBase.class);
                ATTACKS.add(ab);
                ATTACK_MAP.put(ab.getAttackName().toLowerCase(), ab);
            } catch (JsonIOException | FileNotFoundException | JsonSyntaxException e) {
                Pixelmon.LOGGER.error("Unable to load external move JSON " + moveFile.getName());
                e.printStackTrace();
            }
        }
    }
}
