package info.pixelmon.repack.ninja.leaping.configurate.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface CommentHandler {
   Optional extractHeader(BufferedReader var1) throws IOException;

   Collection toComment(Collection var1);
}
