package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigIncluder;
import info.pixelmon.repack.com.typesafe.config.ConfigIncluderClasspath;
import info.pixelmon.repack.com.typesafe.config.ConfigIncluderFile;
import info.pixelmon.repack.com.typesafe.config.ConfigIncluderURL;

interface FullIncluder extends ConfigIncluder, ConfigIncluderFile, ConfigIncluderURL, ConfigIncluderClasspath {
}
