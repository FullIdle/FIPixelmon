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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
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

    //旧版本的获取逻辑
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
     * 旧版本的获取逻辑
     */
    public static EnumSpecies getFromDex(int nationalDex) {
        return getFromDex(EnumSpecies.values(), nationalDex);
    }

    /**
     * 更全面的获取
     */
    public static EnumSpecies fromDex(EnumSpecies[] VALUES, int nationalDex) {
        if (nationalDex < 0) return null;
        try {
            if (VALUES[nationalDex].getNationalPokedexInteger() == nationalDex) return VALUES[nationalDex];
        } catch (Exception e) {
            for (EnumSpecies value : VALUES) if (value.getNationalPokedexInteger() == nationalDex) return value;
        }
        return null;
    }

    /**
     * 返回的流是并行的
     */
    public static Collection<PokemonConfig> readAllConfigs() throws IOException {
        return readDataFolderZipTo(readPokemonFolderTo(new ArrayList<>()));
    }

    public static <T extends Collection<PokemonConfig>> T readPokemonFolderTo(T configs) throws IOException {
        try(
                val walk = Files.walk(FIPixelmon.pokemonFolder.toPath())
        ) {
            walk.map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".json"))
                    .forEach(file -> {
                        try (
                                val reader = new FileReader(file);
                        ) {
                            configs.add(FIPixelmon.GSON.fromJson(reader, PokemonConfig.class));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return configs;
    }

    public static <T extends Collection<PokemonConfig>> T readDataFolderZipTo(T configs) throws IOException {
        try (
                val walk = Files.walk(FIPixelmon.fiPixelmonFolder.toPath())
        ) {
            walk.map(Path::toFile)
                    .forEach(file -> {
                        if (FileHelper.isZip(file)) try (
                                ZipFile zipFile = new ZipFile(file)
                        ) {
                            val entries = zipFile.entries();
                            while (entries.hasMoreElements()) {
                                val entry = entries.nextElement();
                                val entryName = entry.getName();
                                if (entryName.startsWith("pokemon") && entryName.endsWith(".json")) {
                                    try (
                                            InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(entry))
                                    ) {
                                        PokemonConfig config = FIPixelmon.GSON.fromJson(reader, PokemonConfig.class);
                                        config.setFromZip(file);
                                        configs.add(config);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return configs;
    }

    @Override
    public int compareTo(PokemonConfig o) {
        return Integer.compare(this.dex, o.dex);
    }
}
