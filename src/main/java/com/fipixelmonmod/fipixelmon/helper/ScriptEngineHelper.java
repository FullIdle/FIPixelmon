package com.fipixelmonmod.fipixelmon.helper;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;

/**
 * 脚本引擎辅助工具
 */
public class ScriptEngineHelper {
    public static ScriptEngine usingEngine = new NashornScriptEngineFactory().getScriptEngine();
}
