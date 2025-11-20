package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.helper.ScriptEngineHelper;
import jdk.internal.dynalink.beans.StaticClass;
import lombok.Getter;
import lombok.val;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Getter
public abstract class NeedExtendClassScriptConfig<T> {
    private final File file;
    private final Class<? extends T> representedClass;

    protected NeedExtendClassScriptConfig(File file) {
        this.file = file;
        try (
                val reader = new FileReader(file);
        ) {
            this.representedClass = (Class<? extends T>) ((StaticClass)
                    ScriptEngineHelper.usingEngine.eval(reader)).getRepresentedClass();
        } catch (IOException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
