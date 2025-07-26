package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.MegaStoneConfig;
import com.fipixelmonmod.fipixelmon.data.PokeBallConfig;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.RegistryListener;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = RegistryListener.class, remap = false)
public class MixinRegistryListener {
    @Inject(
            method = "registerAllFields",
            at = @At("TAIL")
    )
    private static <T extends IForgeRegistryEntry<T>> void registerAllFields(Class clazzWithFields, Class<T> type, IForgeRegistry<T> registry, CallbackInfo ci) {
        if (clazzWithFields == PixelmonItemsPokeballs.class) {
            for (Map.Entry<EnumPokeballs, PokeBallConfig> entry : PokeBallConfig.extraPokeBallConfig.entrySet()) {
                PokeBallConfig value = entry.getValue();
                registry.register((T) (value.item = new ItemPokeball(entry.getKey())));
            }
            return;
        }
        if (clazzWithFields == PixelmonItemsHeld.class) {
            List<Item> list = ReflectionHelper.getPrivateValue(PixelmonItemsHeld.class, null, "megaStones");
            for (Map.Entry<EnumMegaPokemon, MegaStoneConfig> entry : MegaStoneConfig.extraMegaStoneConfig.entrySet()) {
                MegaStoneConfig value = entry.getValue();
                if (list != null && !list.contains(value.item))
                    list.add(value.item);
                registry.register((T) value.item);
            }
        }
    }

    @Inject(
            method = "registerItemRenderersByFields",
            at = @At("HEAD")
    )
    @SideOnly(Side.CLIENT)
    private static void registerItemRenderersByFields(Class clazzWithFields, CallbackInfo ci) {
        if (clazzWithFields == PixelmonItemsPokeballs.class) {
            for (Map.Entry<EnumPokeballs, PokeBallConfig> entry : PokeBallConfig.extraPokeBallConfig.entrySet()) {
                PokeBallConfig value = entry.getValue();
                Item item = value.item;
                value.guiTexResourceLocation = new ResourceLocation(GuiResources.prefix + value.getGuiTex());
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
            return;
        }
        if (clazzWithFields == PixelmonItemsHeld.class) {
            for (Map.Entry<EnumMegaPokemon, MegaStoneConfig> entry : MegaStoneConfig.extraMegaStoneConfig.entrySet()) {
                MegaStoneConfig value = entry.getValue();
                ModelLoader.setCustomModelResourceLocation(value.item, 0, new ModelResourceLocation(value.item.getRegistryName(), "inventory"));
            }
        }
    }
}
