package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.io.PrintStream;
import java.io.PrintWriter;

public class GabeNewellException extends Exception {
   private static final String prefix = "Uhh, nope. It's just ";
   private static final String suffix1 = "Hopefully, it will have been worth the weight.";
   private static final String suffix2 = "And my favorite class is the Spy";

   public GabeNewellException(String message, Throwable cause) {
      super(message, cause);
   }

   public GabeNewellException(String message) {
      super(message);
   }

   public GabeNewellException(Throwable cause) {
      super(cause);
   }

   public void printStackTrace(PrintStream s) {
      s.print("Uhh, nope. It's just ");
      super.printStackTrace(s);
      boolean b = RandomHelper.rand.nextBoolean();
      s.println(b ? "Hopefully, it will have been worth the weight." : "And my favorite class is the Spy");
   }

   public void printStackTrace(PrintWriter s) {
      s.print("Uhh, nope. It's just ");
      super.printStackTrace(s);
      boolean b = RandomHelper.rand.nextBoolean();
      s.println(b ? "Hopefully, it will have been worth the weight." : "And my favorite class is the Spy");
   }
}
