package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class ValveStudioModel implements IModel, IPixelmonModel {
   public static boolean debugModel = false;
   public SmdModel body;
   protected Bone root;
   ArrayList allBones;
   public SmdAnimationSequence currentSequence;
   public ResourceLocation resource;
   public boolean overrideSmoothShading;
   public boolean hasChanged;
   private boolean hasAnimations;
   public HashMap anims;
   protected String materialPath;
   protected boolean usesMaterials;
   private float scale;

   public ValveStudioModel(ValveStudioModel model) {
      this.hasChanged = true;
      this.hasAnimations = false;
      this.anims = new HashMap(4);
      this.usesMaterials = false;
      this.scale = -1.0F;
      this.body = new SmdModel(model.body, this);
      Iterator var2 = model.anims.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         this.anims.put(entry.getKey(), new SmdAnimationSequence((AnimationType)entry.getKey(), ((SmdAnimationSequence)entry.getValue()).sequence, this));
      }

      this.hasAnimations = model.hasAnimations;
      this.usesMaterials = model.usesMaterials;
      this.scale = model.scale;
      this.resource = model.resource;
      this.currentSequence = (SmdAnimationSequence)this.anims.get(AnimationType.IDLE);
      this.overrideSmoothShading = model.overrideSmoothShading;
   }

   public ValveStudioModel(ResourceLocation resource, boolean overrideSmoothShading) throws GabeNewellException {
      this.hasChanged = true;
      this.hasAnimations = false;
      this.anims = new HashMap(4);
      this.usesMaterials = false;
      this.scale = -1.0F;
      this.overrideSmoothShading = overrideSmoothShading;
      this.resource = resource;
      this.loadQC(resource);
      this.reformBones();
      this.precalculateAnimations();
   }

   public ValveStudioModel(ResourceLocation resource) throws GabeNewellException {
      this(resource, false);
   }

   public float getScale() {
      return this.scale;
   }

   public boolean hasAnimations() {
      return this.hasAnimations;
   }

   private void precalculateAnimations() {
      Iterator var1 = this.anims.values().iterator();

      while(var1.hasNext()) {
         SmdAnimationSequence anim = (SmdAnimationSequence)var1.next();
         anim.precalculate(this.body);
      }

   }

   public void renderAll(float partialTick) {
      GlStateManager.func_179103_j(7425);

      try {
         this.body.render(this.hasChanged, partialTick);
         this.hasChanged = false;
      } catch (Exception var3) {
         var3.printStackTrace();
         if (OpenGlHelper.func_176075_f()) {
            OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
         }
      }

      GlStateManager.func_179103_j(7424);
   }

   void sendBoneData(SmdModel model) {
      this.allBones = model.bones;
      if (!model.isBodyGroupPart) {
         this.root = model.root;
      }

   }

   private void reformBones() {
      this.root.reformChildren();
      this.allBones.forEach(Bone::invertRestMatrix);
   }

   public void animate() {
      if (this.body.currentAnim == null) {
         this.setAnimation(AnimationType.IDLE);
      }

      if (PixelmonConfig.smoothAnimations) {
         this.reverseFrame(this.body);
         this.resetVerts(this.body);
         this.root.setModified();
         this.allBones.forEach(Bone::applyModified);
         this.applyVertChange(this.body);
         this.hasChanged = true;
         this.nextFrame(this.body);
      }

      this.resetVerts(this.body);
      this.root.setModified();
      this.allBones.forEach(Bone::applyModified);
      this.applyVertChange(this.body);
      this.hasChanged = true;
   }

   private void reverseFrame(SmdModel model) {
      --model.currentAnim.currentFrameIndex;
      if (model.currentAnim.currentFrameIndex < 0) {
         if (this.currentSequence.animationType.isLooped) {
            model.currentAnim.currentFrameIndex = model.currentAnim.totalFrames - 1;
         } else {
            model.currentAnim.currentFrameIndex = 0;
         }
      }

   }

   private void nextFrame(SmdModel model) {
      if (!this.currentSequence.animationType.isLooped) {
         if (model.currentAnim.currentFrameIndex > 0) {
            ++model.currentAnim.currentFrameIndex;
         }
      } else {
         ++model.currentAnim.currentFrameIndex;
         if (model.currentAnim.currentFrameIndex == model.currentAnim.totalFrames) {
            model.currentAnim.currentFrameIndex = 0;
         }
      }

   }

   public void setAnimation(AnimationType animType) {
      if (this.anims.containsKey(animType)) {
         this.currentSequence = (SmdAnimationSequence)this.anims.get(animType);
      } else {
         this.currentSequence = (SmdAnimationSequence)this.anims.get(AnimationType.IDLE);
      }

      if (this.currentSequence != null) {
         this.body.setAnimation(this.currentSequence.current());
      } else {
         this.body.setAnimation((SmdAnimation)null);
      }

   }

   protected String getMaterialPath(String subFile) {
      String result = "/assets/pixelmon";
      if (!this.materialPath.startsWith("/")) {
         result = result + "/";
      }

      result = result + this.materialPath;
      if (!subFile.startsWith("/")) {
         result = result + "/";
      }

      result = result + subFile;
      int lastDot = result.lastIndexOf(".");
      result = lastDot == -1 ? result + ".mat" : result.substring(0, lastDot) + ".mat";
      return result;
   }

   private void resetVerts(SmdModel model) {
      if (model != null) {
         model.verts.forEach(DeformVertex::reset);
      }
   }

   private void applyVertChange(SmdModel model) {
      if (model != null) {
         model.verts.forEach(DeformVertex::applyChange);
      }
   }

   public Collection getDependencies() {
      return null;
   }

   public Collection getTextures() {
      return null;
   }

   public IBakedModel bake(IModelState state, VertexFormat format, Function bakedTextureGetter) {
      return null;
   }

   public IModelState getDefaultState() {
      return null;
   }

   public static void print(Object o) {
      if (debugModel) {
         System.out.println(o);
      }

   }

   private void loadQC(ResourceLocation resource) throws GabeNewellException {
      InputStream inputStream = Pixelmon.proxy.getStreamForResourceLocation(resource);
      int lineCount = 0;

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         Throwable var5 = null;

         try {
            String[] bodyParams = null;
            ArrayList animParams = new ArrayList();

            String currentLine;
            while((currentLine = reader.readLine()) != null) {
               ++lineCount;
               String[] params = RegexPatterns.MULTIPLE_WHITESPACE.split(currentLine);
               if (params[0].equalsIgnoreCase("$body")) {
                  bodyParams = params;
               } else if (params[0].equalsIgnoreCase("$anim")) {
                  this.hasAnimations = true;
                  animParams.add(params);
               } else if (params[0].equalsIgnoreCase("$cdmaterials")) {
                  this.usesMaterials = true;
                  this.materialPath = params[1];
               } else if (params[0].equalsIgnoreCase("$scale")) {
                  this.scale = Float.parseFloat(params[1]);
               }
            }

            if (this.scale == -1.0F) {
               this.scale = 1.0F;
               if (this.resource.func_110623_a().startsWith("models/pokemon")) {
                  Pixelmon.LOGGER.error("Model " + resource.func_110623_a() + " did not have a scale specified!");
               }
            }

            ResourceLocation modelPath = this.getResource(bodyParams[1]);
            this.body = new SmdModel(this, modelPath);
            HashMap recognizedAnimations = new HashMap();
            Iterator var11 = animParams.iterator();

            String[] animPars;
            String animName;
            while(var11.hasNext()) {
               animPars = (String[])var11.next();
               animName = animPars[1];
               ResourceLocation animPath = this.getResource(animPars[2]);
               if (!recognizedAnimations.containsKey(animPath)) {
                  SmdAnimation animation = new SmdAnimation(this, animName, animPath);
                  recognizedAnimations.put(animPath, animation);
               }
            }

            var11 = animParams.iterator();

            while(var11.hasNext()) {
               animPars = (String[])var11.next();
               animName = animPars[1];
               List sequence = new ArrayList();

               for(int i = 2; i < animPars.length; ++i) {
                  ResourceLocation animPath = this.getResource(animPars[i]);
                  if (recognizedAnimations.containsKey(animPath)) {
                     sequence.add(recognizedAnimations.get(animPath));
                  } else if (Pixelmon.devEnvironment) {
                     Pixelmon.LOGGER.error("Animation file " + animPath + " was not registered in " + this.resource + "!");
                  }
               }

               AnimationType animType = AnimationType.getTypeFor(animName);
               this.anims.put(animType, new SmdAnimationSequence(animType, sequence, this));
               if (animName.equalsIgnoreCase("idle")) {
                  this.currentSequence = (SmdAnimationSequence)this.anims.get(AnimationType.IDLE);
               }
            }

         } catch (Throwable var26) {
            var5 = var26;
            throw var26;
         } finally {
            if (reader != null) {
               if (var5 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var25) {
                     var5.addSuppressed(var25);
                  }
               } else {
                  reader.close();
               }
            }

         }
      } catch (GabeNewellException var28) {
         throw var28;
      } catch (Exception var29) {
         throw new GabeNewellException("An error occurred while reading the " + resource.toString() + " PQC file on line #" + lineCount, var29);
      }
   }

   public ResourceLocation getResource(String fileName) {
      String urlAsString = this.resource.func_110623_a();
      int lastIndex = urlAsString.lastIndexOf(47);
      String startString = urlAsString.substring(0, lastIndex);
      return new ResourceLocation("pixelmon", startString + "/" + fileName);
   }
}
