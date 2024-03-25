package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigMergeable;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;

interface MergeableValue extends ConfigMergeable {
   ConfigValue toFallbackValue();
}
