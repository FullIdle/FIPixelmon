package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.fipixelmonmod.fipixelmon.helper.EnumSpeciesHelper;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraftforge.common.util.EnumHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

@Getter
@Setter
public class PokemonConfig implements Comparable<PokemonConfig> {
    public static final Map<EnumSpecies, PokemonConfig> extraPokemonConfig = new HashMap<>();
    private String name = null;
    private int dex = -1;
    private boolean legendary = false;
    private String model = null;
    private String flyingModel = null;
    private EnumForm.FormData[] forms = new EnumForm.FormData[]{};
    private boolean editReplace = false;

    //geo
    private String geoModel = null;
    private String geoAnimation = null;

    transient private IEnumForm[] enumForm;
    transient private EnumSpecies species;
    transient private File fromZip = null;
    @Deprecated
    transient private boolean isEdit = false;


    //在EnumSpecies的$VAULT被初始化并被第一次条用之前进行注入
    public void inject(EnumSpecies species) {
        this.species = species;
        if (!this.species.name.equals(this.name)) EnumSpeciesHelper.setName(this.species, this.name);
        FIPixelmon.logger.info(
                (this.isEdit = !EnumSpeciesHelper.isCreatedFIP(this.species)) ?
                        "REGISTERED ENUM [name:{},dex:{}]" :
                        "EDIT ENUM [name:{},dex:{}]",
                this.name,
                this.dex
        );
        extraPokemonConfig.put(this.species, this);
    }

    public boolean isFromZip() {
        return this.fromZip != null;
    }

    //旧的获取方法
    public static EnumSpecies getFromDex(EnumSpecies[] $VALUES, int nationalDex) {
        if ($VALUES == null) $VALUES = EnumSpecies.values();

        if (nationalDex < 0) {
            return null;
        } else if (nationalDex < $VALUES.length && $VALUES[nationalDex].getNationalPokedexInteger() == nationalDex) {
            return $VALUES[nationalDex];
        } else {
            for (int i = $VALUES.length - 1; i >= 0; --i) {
                if ($VALUES[i].getNationalPokedexInteger() == nationalDex) return $VALUES[i];
                if ($VALUES[i].getNationalPokedexInteger() < nationalDex) break;
            }
            return null;
        }
    }

    /**
     * 返回的流是并行的
     */
    public static Stream<PokemonConfig> readAllConfigs() throws IOException {
        return Stream.concat(
                readPokemonFolder(),
                readDataFolderZip());
    }

    public static Stream<PokemonConfig> readPokemonFolder() throws IOException {
        return Files.walk(FIPixelmon.pokemonFolder.toPath())
                .map(Path::toFile)
                .filter(file -> file.getName().endsWith(".json"))
                .map(file -> {
                    try (
                            val reader = new FileReader(file);
                    ) {
                        return FIPixelmon.GSON.fromJson(reader, PokemonConfig.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static Stream<PokemonConfig> readDataFolderZip() throws IOException {
        return Files.walk(FIPixelmon.fiPixelmonFolder.toPath())
                .map(Path::toFile)
                .filter(FileHelper::isZip)
                .flatMap(file -> {
                    try (
                            ZipFile zipFile = new ZipFile(file);
                    ) {
                        return zipFile.stream()
                                .filter(s -> s.getName().startsWith("pokemon") && s.getName().endsWith(".json"))
                                .map(zipEntry -> {
                                    try (
                                            InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipEntry))
                                    ) {
                                        PokemonConfig config = FIPixelmon.GSON.fromJson(reader, PokemonConfig.class);
                                        config.setFromZip(file);
                                        return config;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public int compareTo(PokemonConfig o) {
        return Integer.compare(this.dex, o.dex);
    }
}
