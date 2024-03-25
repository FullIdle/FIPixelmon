package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.LineParserFactory;
import com.pixelmonmod.pixelmon.client.models.obj.NormalParser;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.CommentParser;

public class ObjLineParserFactory extends LineParserFactory {
   public ObjLineParserFactory(WavefrontObject object) {
      this.object = object;
      this.parsers.put("v", new VertexParser());
      this.parsers.put("vn", new NormalParser());
      this.parsers.put("vp", new FreeFormParser());
      this.parsers.put("vt", new TextureCooParser());
      this.parsers.put("f", new FaceParser(object));
      this.parsers.put("#", new CommentParser());
      this.parsers.put("usemtl", new MaterialParser());
      this.parsers.put("g", new GroupParser());
   }
}
