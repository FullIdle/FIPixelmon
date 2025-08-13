package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.EnumSpeciesBridge;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.val;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Mixin(value = EnumSpecies.class, remap = false)
public abstract class MixinEnumSpecies implements EnumSpeciesBridge {
    @Mutable
    @Shadow
    @Final
    public static EnumSpecies[] LEGENDARY_ENUMS;
    @Mutable
    @Shadow
    @Final
    private int nationalDex;

    @Shadow
    private static ListMultimap<EnumSpecies, IEnumForm> formList;

    @Mutable
    @Shadow
    @Final
    private static EnumSpecies[] $VALUES;


    @Mutable
    @Shadow
    @Final
    public String name;

    @Invoker("<init>")
    private static EnumSpecies create(String enumName, int ordinal, int dex, String name) {
        throw new IllegalStateException("Unreachable");
    }

    @Unique
    private static EnumSpecies fIPixelmon$create(int dex, String name) {
        val species = create(name, $VALUES.length, dex, name);
        (((MixinEnumSpecies) (Object) species)).fIPixelmon$createdInFIP = true;
        $VALUES = ArrayUtils.add($VALUES, species);
        return species;
    }

    @Inject(method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false)
    private static void registerEnumSpecies(CallbackInfo ci) {
        try (val stream = PokemonConfig.readAllConfigs()) {
            //本来想说增加的枚举最后直接用System.arraycopy合进去这样消耗应该更小，不过由于fip支持多个配置对同一个宝可梦修改，所以没法这样做。。。
            stream.sorted()
                    .forEachOrdered(config -> {
                        val fromDex = PokemonConfig.getFromDex($VALUES, config.getDex());
                        val species = fromDex == null ? fIPixelmon$create(config.getDex(), config.getName()) : fromDex;
                        (((MixinEnumSpecies) (Object) species)).fIPixelmon$pokemonConfig = config;
                        config.inject(species);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "<clinit>",
            at = @At("TAIL"),
            remap = false)
    private static void cliTail(CallbackInfo ci) {
        ArrayList<EnumSpecies> list = Lists.newArrayList(LEGENDARY_ENUMS);
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet())
            if (entry.getValue().isLegendary()) list.add(entry.getValue().getSpecies());
        LEGENDARY_ENUMS = list.toArray(new EnumSpecies[0]);
    }

    @Inject(method = "<clinit>",
            at = @At("TAIL"),
            remap = false)
    private static void formsRegister(CallbackInfo ci) {
        formList = MultimapBuilder.enumKeys(EnumSpecies.class).arrayListValues(1).build(formList);
        List<IEnumForm> forms;
        List<IEnumForm> temp;
        boolean isCovered;
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            val species = entry.getKey();
            val pokemonConfig = entry.getValue();

            /*==>形态初始化<==*/
            ArrayList<IEnumForm> iEnumForms = new ArrayList<>(pokemonConfig.getForms().length);
            for (EnumForm.FormData formData : pokemonConfig.getForms()) {
                EnumForm enumForm = EnumHelper.addEnum(EnumForm.class, formData.getFormName(), new Class<?>[]{EnumForm.FormData.class}, formData);
                formData.setEnumForm(enumForm);
                iEnumForms.add(enumForm);
            }
            pokemonConfig.setEnumForm(iEnumForms.toArray(new IEnumForm[0]));

            if (isCovered = (pokemonConfig.isEdit() && pokemonConfig.isEditReplace())) formList.removeAll(species);
            for (IEnumForm form : pokemonConfig.getEnumForm()) formList.put(species, form);

            //检查是否该精灵是否拥有形态
            if (formList.containsKey(species)) {
                //拥有形态则获取处理后的所有形态并算出非临时形态的数量
                temp = Lists.newArrayList(forms = formList.get(species));
                temp.removeIf(IEnumForm::isTemporary);

                //如果是覆盖的情况下,且没有NoForm则增加一个默认形态
                if (isCovered && !temp.contains(EnumNoForm.NoForm))
                    forms.add(0, EnumNoForm.NoForm);

                //排除已FIP增加的形态(因为上面的覆盖判断需要带上FIP增加的形态进行判断)
                temp.removeIf(form -> form instanceof EnumForm);

                //全是临时形态的时候增加一个非临时形态的普通形态
                if (temp.isEmpty()) forms.add(0, EnumNoForm.NoForm);
                continue;
            }
            //没有形态则增加一个默认形态
            System.out.println(species.getNationalPokedexInteger() + "增加了默认形态");
            formList.put(species, EnumNoForm.NoForm);
        }
        formList = Multimaps.unmodifiableListMultimap(formList);
    }
    /*==>bridge impl<==*/

    @Override
    public void fIPixelmon$setDex(int dex) {
        this.nationalDex = dex;
    }

    @Override
    public void fIPixelmon$setName(String name) {
        this.name = name;
    }

    @Unique
    private boolean fIPixelmon$createdInFIP = false;

    @Override
    public boolean fIPixelmon$isCreatedInFIP() {
        return fIPixelmon$createdInFIP;
    }

    @Unique
    private PokemonConfig fIPixelmon$pokemonConfig;

    @Override
    public PokemonConfig fIPixelmon$getPokemonConfig() {
        return fIPixelmon$pokemonConfig;
    }

    /*==><==*/
}