package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.client.materials.Cubemap;
import com.pixelmonmod.pixelmon.client.materials.EnumMaterialOption;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Material {
   private boolean translucent = false;
   private boolean wireframe = false;
   private boolean unlit = false;
   private boolean noCull = false;
   private boolean depthMask = true;
   private SimpleTexture texture;
   private Cubemap cubemap;
   private File directory;
   private String resourceLocationDirectory;

   public Material(File materialFile) throws IOException {
      this.directory = materialFile.getParentFile();
      String path = this.directory.getAbsolutePath();
      this.resourceLocationDirectory = path.substring(path.indexOf("pixelmon\\") + 9);
      BufferedReader reader = new BufferedReader(new FileReader(materialFile));
      String line = null;

      while((line = reader.readLine()) != null) {
         String[] params = RegexPatterns.MULTIPLE_WHITESPACE.split(line);
         String resLocString;
         ResourceLocation resloc;
         if (params[0].equalsIgnoreCase("$texture")) {
            resLocString = this.resourceLocationDirectory.endsWith("/") ? this.resourceLocationDirectory + params[1] : this.resourceLocationDirectory + "/" + params[1];
            resloc = new ResourceLocation("pixelmon", resLocString);
            System.out.println("resloc = " + resloc);
            Minecraft.func_71410_x().func_110434_K().func_110577_a(resloc);
            this.texture = (SimpleTexture)Minecraft.func_71410_x().func_110434_K().func_110581_b(resloc);
         } else if (params[0].equalsIgnoreCase("$cubemap")) {
            resLocString = path.endsWith("/") ? path + params[1] : path + "/" + params[1];
            resloc = new ResourceLocation(resLocString);
            this.cubemap = new Cubemap(resloc);
         } else if (params[0].equalsIgnoreCase("$nocull")) {
            this.noCull = Boolean.parseBoolean(params[1]);
         } else if (params[0].equalsIgnoreCase("$translucent")) {
            this.translucent = Boolean.parseBoolean(params[1]);
         } else if (params[0].equalsIgnoreCase("$wireframe")) {
            this.wireframe = Boolean.parseBoolean(params[1]);
         } else if (params[0].equalsIgnoreCase("$unlit")) {
            this.unlit = Boolean.parseBoolean(params[1]);
         } else if (params[0].equalsIgnoreCase("$depthmask")) {
            this.depthMask = Boolean.parseBoolean(params[1]);
         }
      }

      reader.close();
   }

   public void pre() {
      GL11.glBindTexture(3553, this.texture.func_110552_b());
      if (this.translucent) {
         EnumMaterialOption.TRANSPARENCY.begin();
      }

      if (this.wireframe) {
         EnumMaterialOption.WIREFRAME.begin();
      }

      if (this.unlit) {
         EnumMaterialOption.NO_LIGHTING.begin();
      }

      if (this.noCull) {
         EnumMaterialOption.NOCULL.begin();
      } else {
         EnumMaterialOption.NOCULL.end();
      }

      if (!this.depthMask) {
         GlStateManager.func_179132_a(false);
      }

      if (this.cubemap != null) {
         this.cubemap.start();
      }

   }

   public void post() {
      if (this.translucent) {
         EnumMaterialOption.TRANSPARENCY.end();
      }

      if (this.wireframe) {
         EnumMaterialOption.WIREFRAME.end();
      }

      if (this.unlit) {
         EnumMaterialOption.NO_LIGHTING.end();
      }

      if (!this.noCull) {
         EnumMaterialOption.NOCULL.end();
      }

      if (!this.depthMask) {
         GlStateManager.func_179132_a(true);
      }

      if (this.cubemap != null) {
         this.cubemap.end();
      }

   }
}
