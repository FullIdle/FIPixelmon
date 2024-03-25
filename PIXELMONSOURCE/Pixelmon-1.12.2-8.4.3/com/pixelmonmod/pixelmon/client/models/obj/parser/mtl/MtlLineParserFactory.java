package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.LineParserFactory;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.CommentParser;

public class MtlLineParserFactory extends LineParserFactory {
   public MtlLineParserFactory(WavefrontObject object) {
      this.object = object;
      this.parsers.put("newmtl", new MaterialParser());
      this.parsers.put("Ka", new KaParser());
      this.parsers.put("Kd", new KdParser());
      this.parsers.put("Ks", new KsParser());
      this.parsers.put("Ns", new NsParser());
      this.parsers.put("map_Kd", new KdMapParser(object));
      this.parsers.put("#", new CommentParser());
   }
}
