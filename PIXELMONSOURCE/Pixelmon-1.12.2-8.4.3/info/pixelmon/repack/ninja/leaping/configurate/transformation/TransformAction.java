package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;

@FunctionalInterface
public interface TransformAction {
   Object[] visitPath(ConfigurationTransformation.NodePath var1, ConfigurationNode var2);
}
