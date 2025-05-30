package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.geo.GeoRender;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.val;
import net.minecraft.client.model.ModelBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity2Client.class)
public abstract class MixinEntity2Client extends Entity1Base {
    public MixinEntity2Client(World par1World) {
        super(par1World);
    }

    /**
     * {@link Entity2Client#onUpdate()} 中调用了 {@link Entity2Client#checkAnimation()} 会报错
     * 报错后会在日志中提示模型名字，这里修改了 {@link Entity2Client#checkAnimation()} 内调用
     * {@link Entity2Client#getModel()} 的代码，使其在有geo模型的时候返回null
     */
    @Inject(
            method = "getModel",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void getModel(CallbackInfoReturnable<ModelBase> cir) {
        if (((Object) this) instanceof EntityPixelmon) {
            val ep = (EntityPixelmon) (Object) this;
            if (PokemonConfig.extraPokemonConfig.containsKey(ep.getSpecies()))
                if (GeoRender.hasGeoModel(ep, PokemonConfig.extraPokemonConfig.get(ep.getSpecies()))) {
                    cir.setReturnValue(null);
                    cir.cancel();
                }
        }
    }
}
