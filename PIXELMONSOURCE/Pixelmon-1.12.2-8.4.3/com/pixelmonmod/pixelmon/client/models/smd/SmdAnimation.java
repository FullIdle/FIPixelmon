package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import com.pixelmonmod.pixelmon.util.helpers.VectorHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Matrix4f;

public class SmdAnimation {
   public final ValveStudioModel owner;
   public ArrayList frames = new ArrayList();
   public ArrayList bones = new ArrayList();
   public int currentFrameIndex = 0;
   public int lastFrameIndex;
   public int totalFrames;
   public String animationName;
   private int frameIDBank = 0;

   public SmdAnimation(ValveStudioModel owner, String animationName, ResourceLocation resloc) throws GabeNewellException {
      this.owner = owner;
      this.animationName = animationName;
      if (resloc.func_110623_a().endsWith(".bmd")) {
         this.loadBmdModel(resloc);
      } else {
         this.loadSmdAnim(resloc);
      }

      this.setBoneChildren();
      this.reform();
   }

   public SmdAnimation(SmdAnimation anim, ValveStudioModel owner) {
      this.owner = owner;
      this.animationName = anim.animationName;
      Iterator var3 = anim.bones.iterator();

      while(var3.hasNext()) {
         Bone b = (Bone)var3.next();
         this.bones.add(new Bone(b, b.parent != null ? (Bone)this.bones.get(b.parent.ID) : null, (SmdModel)null));
      }

      this.frames.addAll((Collection)anim.frames.stream().map((f) -> {
         return new AnimFrame(f, this);
      }).collect(Collectors.toList()));
      this.totalFrames = anim.totalFrames;
   }

   private void loadSmdAnim(ResourceLocation resloc) throws GabeNewellException {
      InputStream inputStream = Pixelmon.proxy.getStreamForResourceLocation(resloc);
      String currentLine = null;
      int lineCount = 0;

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         Throwable var6 = null;

         try {
            while((currentLine = reader.readLine()) != null) {
               ++lineCount;
               if (!currentLine.startsWith("version")) {
                  if (currentLine.startsWith("nodes")) {
                     ++lineCount;

                     while(!(currentLine = reader.readLine()).startsWith("end")) {
                        ++lineCount;
                        this.parseBone(currentLine, lineCount);
                     }
                  } else if (currentLine.startsWith("skeleton")) {
                     this.startParsingAnimation(reader, lineCount, resloc);
                  }
               }
            }
         } catch (Throwable var16) {
            var6 = var16;
            throw var16;
         } finally {
            if (reader != null) {
               if (var6 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var15) {
                     var6.addSuppressed(var15);
                  }
               } else {
                  reader.close();
               }
            }

         }

      } catch (IOException var18) {
         if (lineCount == -1) {
            throw new GabeNewellException("there was a problem opening the model file : " + resloc, var18);
         } else {
            throw new GabeNewellException("an error occurred reading the SMD file \"" + resloc + "\" on line #" + lineCount, var18);
         }
      }
   }

   private void loadBmdModel(ResourceLocation modelLoc) throws GabeNewellException {
      InputStream inputStream = Pixelmon.proxy.getStreamForResourceLocation(modelLoc);

      try {
         DataInputStream in = new DataInputStream(inputStream);
         Throwable var4 = null;

         try {
            byte version = in.readByte();

            assert version == 1;

            int numNodes = in.readShort();
            CommonHelper.ensureIndex(this.bones, numNodes - 1);

            int numSkeletons;
            short time;
            for(int i = 0; i < numNodes; ++i) {
               numSkeletons = in.readShort();
               time = in.readShort();
               String name = readNullTerm(in);
               Bone parent = time != -1 ? (Bone)this.bones.get(time) : null;
               this.bones.set(numSkeletons, new Bone(name, numSkeletons, parent, (SmdModel)null));
            }

            Map skeletonMap = new LinkedHashMap();
            numSkeletons = this.totalFrames = in.readShort();

            for(time = 0; time < numSkeletons; ++time) {
               skeletonMap.put(time, new LinkedHashMap());
               Map frame = (Map)skeletonMap.get(time);
               int numBones = in.readShort();

               for(int j = 0; j < numBones; ++j) {
                  short boneId = in.readShort();
                  float locX = in.readFloat();
                  float locY = in.readFloat();
                  float locZ = in.readFloat();
                  float rotX = in.readFloat();
                  float rotY = in.readFloat();
                  float rotZ = in.readFloat();
                  Matrix4f skeleton = VectorHelper.matrix4FromLocRot(locX, -locY, -locZ, rotX, -rotY, -rotZ);
                  frame.put(boneId, skeleton);
               }

               short i;
               for(i = 0; i < numNodes; ++i) {
                  if (!frame.containsKey(i)) {
                     if (time == 0) {
                        throw new IOException("Missing bone definitions in first frame");
                     }

                     frame.put(i, ((Map)skeletonMap.get((short)(time - 1))).get(i));
                  }
               }

               this.frames.add(time, new AnimFrame(this));

               for(i = 0; i < frame.size(); ++i) {
                  ((AnimFrame)this.frames.get(time)).addTransforms(i, (Matrix4f)frame.get(i));
               }
            }
         } catch (Throwable var29) {
            var4 = var29;
            throw var29;
         } finally {
            if (in != null) {
               if (var4 != null) {
                  try {
                     in.close();
                  } catch (Throwable var28) {
                     var4.addSuppressed(var28);
                  }
               } else {
                  in.close();
               }
            }

         }

      } catch (IOException var31) {
         throw new GabeNewellException(var31);
      }
   }

   private void parseBone(String line, int lineCount) {
      String[] params = line.split("\"");
      int id = Integer.parseInt(RegexPatterns.SPACE_SYMBOL.matcher(params[0]).replaceAll(""));
      String boneName = params[1];
      int parentID = Integer.parseInt(RegexPatterns.SPACE_SYMBOL.matcher(params[2]).replaceAll(""));
      Bone parent = parentID >= 0 ? (Bone)this.bones.get(parentID) : null;
      this.bones.add(id, new Bone(boneName, id, parent, (SmdModel)null));
      ValveStudioModel.print(boneName);
   }

   private void startParsingAnimation(BufferedReader reader, int count, ResourceLocation resloc) throws GabeNewellException {
      int currentTime = 0;
      int lineCount = count + 1;
      String currentLine = null;

      try {
         while(true) {
            while((currentLine = reader.readLine()) != null) {
               ++lineCount;
               String[] params = RegexPatterns.MULTIPLE_WHITESPACE.split(currentLine);
               if (params[0].equalsIgnoreCase("time")) {
                  currentTime = Integer.parseInt(params[1]);
                  this.frames.add(currentTime, new AnimFrame(this));
               } else {
                  if (currentLine.startsWith("end")) {
                     this.totalFrames = this.frames.size();
                     ValveStudioModel.print("Total number of frames = " + this.totalFrames);
                     return;
                  }

                  int boneIndex = Integer.parseInt(params[0]);
                  float[] locRots = new float[6];

                  for(int i = 1; i < 7; ++i) {
                     locRots[i - 1] = Float.parseFloat(params[i]);
                  }

                  Matrix4f animated = VectorHelper.matrix4FromLocRot(locRots[0], -locRots[1], -locRots[2], locRots[3], -locRots[4], -locRots[5]);
                  ((AnimFrame)this.frames.get(currentTime)).addTransforms(boneIndex, animated);
               }
            }

            return;
         }
      } catch (Exception var11) {
         throw new GabeNewellException("an error occurred reading the SMD file \"" + resloc + "\" on line #" + lineCount, var11);
      }
   }

   public int requestFrameID() {
      int result = this.frameIDBank++;
      return result;
   }

   private void setBoneChildren() {
      for(int i = 0; i < this.bones.size(); ++i) {
         Bone theBone = (Bone)this.bones.get(i);
         this.bones.stream().filter((child) -> {
            return child.parent == theBone;
         }).forEach(theBone::addChild);
      }

   }

   public void reform() {
      int rootID = this.owner.body.root.ID;
      Iterator var2 = this.frames.iterator();

      while(var2.hasNext()) {
         AnimFrame frame = (AnimFrame)var2.next();
         frame.fixUp(rootID, 0.0F);
         frame.reform();
      }

   }

   public void precalculateAnimation(SmdModel model) {
      Iterator var2 = this.frames.iterator();

      while(var2.hasNext()) {
         AnimFrame frame1 = (AnimFrame)var2.next();
         model.resetVerts();
         AnimFrame frame = frame1;

         for(int j = 0; j < model.bones.size(); ++j) {
            Bone bone = (Bone)model.bones.get(j);
            Matrix4f animated = (Matrix4f)frame.transforms.get(j);
            bone.preloadAnimation(frame, animated);
         }
      }

   }

   public int getNumFrames() {
      return this.frames.size();
   }

   public void setCurrentFrame(int i) {
      if (this.lastFrameIndex != i) {
         this.currentFrameIndex = i;
         this.lastFrameIndex = i;
      }

   }

   private static String readNullTerm(DataInputStream in) throws IOException {
      StringBuilder str = new StringBuilder();
      char ch = 0;

      do {
         if (ch != 0) {
            str.append(ch);
         }

         ch = in.readChar();
      } while(ch != 0);

      return str.toString();
   }
}
