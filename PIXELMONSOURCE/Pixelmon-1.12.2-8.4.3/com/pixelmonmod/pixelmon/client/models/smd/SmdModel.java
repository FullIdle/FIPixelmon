package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import com.pixelmonmod.pixelmon.util.helpers.VectorHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.GL11;

public class SmdModel {
   public final ValveStudioModel owner;
   public ArrayList faces = new ArrayList(0);
   public ArrayList verts = new ArrayList(0);
   public ArrayList bones = new ArrayList(0);
   public HashMap nameToBoneMapping = new HashMap();
   public HashMap materialsByName;
   public HashMap facesByMaterial;
   public SmdAnimation currentAnim;
   private int vertexIDBank = 0;
   protected boolean isBodyGroupPart;
   int lineCount = -1;
   public Bone root;
   public int vertexVbo = -1;
   public int textureVbo = -1;
   public int normalsVbo = -1;
   private FloatBuffer vertexBuffer;
   private FloatBuffer normalBuffer;

   SmdModel(SmdModel model, ValveStudioModel owner) {
      this.owner = owner;
      this.isBodyGroupPart = model.isBodyGroupPart;
      Iterator var3 = model.faces.iterator();

      while(var3.hasNext()) {
         NormalizedFace face = (NormalizedFace)var3.next();
         DeformVertex[] vertices = new DeformVertex[face.vertices.length];

         for(int i = 0; i < vertices.length; ++i) {
            DeformVertex d = new DeformVertex(face.vertices[i]);
            CommonHelper.ensureIndex(this.verts, d.ID);
            this.verts.set(d.ID, d);
         }
      }

      this.faces.addAll((Collection)model.faces.stream().map((facex) -> {
         return new NormalizedFace(facex, this.verts);
      }).collect(Collectors.toList()));

      int i;
      Bone b;
      for(i = 0; i < model.bones.size(); ++i) {
         b = (Bone)model.bones.get(i);
         this.bones.add(new Bone(b, (Bone)null, this));
      }

      for(i = 0; i < model.bones.size(); ++i) {
         b = (Bone)model.bones.get(i);
         b.copy.setChildren(b, this.bones);
      }

      this.root = model.root.copy;
      owner.sendBoneData(this);
   }

   SmdModel(ValveStudioModel owner, ResourceLocation resloc) throws GabeNewellException {
      this.owner = owner;
      this.isBodyGroupPart = false;
      if (resloc.func_110623_a().endsWith(".bmd")) {
         this.loadBmdModel(resloc, (SmdModel)null);
      } else {
         this.loadSmdModel(resloc, (SmdModel)null);
      }

      this.setBoneChildren();
      this.determineRoot();
      owner.sendBoneData(this);
      ValveStudioModel.print("Number of vertices = " + this.verts.size());
   }

   SmdModel(ValveStudioModel owner, ResourceLocation resloc, SmdModel body) throws GabeNewellException {
      this.owner = owner;
      this.isBodyGroupPart = true;
      if (resloc.func_110623_a().endsWith(".bmd")) {
         this.loadBmdModel(resloc, body);
      } else {
         this.loadSmdModel(resloc, body);
      }

      this.setBoneChildren();
      this.determineRoot();
      owner.sendBoneData(this);
   }

   private void loadSmdModel(ResourceLocation resloc, SmdModel body) throws GabeNewellException {
      InputStream inputStream = Pixelmon.proxy.getStreamForResourceLocation(resloc);

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         Throwable var6 = null;

         try {
            this.lineCount = 0;

            String currentLine;
            while((currentLine = reader.readLine()) != null) {
               ++this.lineCount;
               if (!currentLine.startsWith("version")) {
                  if (currentLine.startsWith("nodes")) {
                     ++this.lineCount;

                     while(!(currentLine = reader.readLine()).startsWith("end")) {
                        ++this.lineCount;
                        this.parseBone(currentLine, this.lineCount, body);
                     }

                     ValveStudioModel.print("Number of model bones = " + this.bones.size());
                  } else if (currentLine.startsWith("skeleton")) {
                     ++this.lineCount;
                     reader.readLine();
                     ++this.lineCount;

                     while(!(currentLine = reader.readLine()).startsWith("end")) {
                        ++this.lineCount;
                        if (!this.isBodyGroupPart) {
                           this.parseBoneValues(currentLine, this.lineCount);
                        }
                     }
                  } else if (currentLine.startsWith("triangles")) {
                     ++this.lineCount;

                     while(!(currentLine = reader.readLine()).startsWith("end")) {
                        Material mat = this.owner.usesMaterials ? this.requestMaterial(currentLine) : null;
                        String[] params = new String[3];

                        for(int i = 0; i < 3; ++i) {
                           ++this.lineCount;
                           params[i] = reader.readLine();
                        }

                        this.parseFace(params, this.lineCount, mat);
                     }
                  }
               }
            }
         } catch (Throwable var18) {
            var6 = var18;
            throw var18;
         } finally {
            if (reader != null) {
               if (var6 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var17) {
                     var6.addSuppressed(var17);
                  }
               } else {
                  reader.close();
               }
            }

         }
      } catch (Exception var20) {
         if (this.lineCount == -1) {
            throw new GabeNewellException("there was a problem opening the model file : " + resloc, var20);
         }

         throw new GabeNewellException("an error occurred reading the SMD file \"" + resloc + "\" on line #" + this.lineCount, var20);
      }

      ValveStudioModel.print("Number of faces = " + this.faces.size());
   }

   private void loadBmdModel(ResourceLocation modelLoc, SmdModel body) throws GabeNewellException {
      InputStream inputStream = Pixelmon.proxy.getStreamForResourceLocation(modelLoc);

      try {
         DataInputStream in = new DataInputStream(inputStream);
         Throwable var5 = null;

         try {
            byte version = in.readByte();

            assert version == 1;

            int numNodes = in.readShort();
            CommonHelper.ensureIndex(this.bones, numNodes - 1);

            short i;
            short numMaterial;
            for(int i = 0; i < numNodes; ++i) {
               i = in.readShort();
               numMaterial = in.readShort();
               String name = readNullTerm(in);
               Bone parent = numMaterial != -1 ? (Bone)this.bones.get(numMaterial) : null;
               this.bones.set(i, new Bone(name, i, parent, this));
            }

            int numSkeletons = in.readShort();

            float x;
            int vertexCount;
            short numTriangles;
            for(i = 0; i < numSkeletons; ++i) {
               numMaterial = in.readShort();

               for(vertexCount = 0; vertexCount < numMaterial; ++vertexCount) {
                  numTriangles = in.readShort();
                  float locX = in.readFloat();
                  float locY = in.readFloat();
                  float locZ = in.readFloat();
                  float rotX = in.readFloat();
                  float rotY = in.readFloat();
                  x = in.readFloat();
                  Bone theBone = (Bone)this.bones.get(numTriangles);
                  theBone.setRest(VectorHelper.matrix4FromLocRot(locX, -locY, -locZ, rotX, -rotY, -x));
               }
            }

            List material = new ArrayList();
            numMaterial = in.readShort();

            for(vertexCount = 0; vertexCount < numMaterial; ++vertexCount) {
               material.add(readNullTerm(in));
            }

            vertexCount = 0;
            numTriangles = in.readShort();

            for(int i = 0; i < numTriangles; ++i) {
               String mat = (String)material.get(in.readByte());
               DeformVertex[] faceVerts = new DeformVertex[3];
               TextureCoordinate[] uvs = new TextureCoordinate[3];

               for(int j = 0; j < 3; ++j) {
                  in.readShort();
                  x = in.readFloat();
                  float y = -in.readFloat();
                  float z = -in.readFloat();
                  float normX = in.readFloat();
                  float normY = -in.readFloat();
                  float normZ = -in.readFloat();
                  float u = in.readFloat();
                  float v = in.readFloat();
                  int id = vertexCount++;
                  DeformVertex dv = this.getExisting(x, y, z);
                  if (dv == null) {
                     if (PixelmonConfig.smoothAnimations) {
                        faceVerts[j] = new DeformVertexSmooth(x, y, z, normX, normY, normZ, this.vertexIDBank);
                     } else {
                        faceVerts[j] = new DeformVertex(x, y, z, normX, normY, normZ, this.vertexIDBank);
                     }
                  } else {
                     faceVerts[j] = dv;
                  }

                  byte links = in.readByte();

                  for(int w = 0; w < links; ++w) {
                     int boneID = in.readShort();
                     float weight = in.readFloat();
                     ((Bone)this.bones.get(boneID)).addVertex(faceVerts[j], weight);
                  }

                  CommonHelper.ensureIndex(this.verts, id);
                  this.verts.set(id, faceVerts[j]);
                  uvs[j] = new TextureCoordinate(u, 1.0F - v);
               }

               NormalizedFace face = new NormalizedFace(faceVerts, uvs);
               face.vertices = faceVerts;
               face.textureCoordinates = uvs;
               this.faces.add(face);
               if (this.owner.usesMaterials) {
                  Material mater = this.requestMaterial(mat);
                  if (this.facesByMaterial == null) {
                     this.facesByMaterial = new HashMap();
                  }

                  ArrayList list = (ArrayList)this.facesByMaterial.computeIfAbsent(mater, (k) -> {
                     return new ArrayList();
                  });
                  list.add(face);
               }
            }
         } catch (Throwable var40) {
            var5 = var40;
            throw var40;
         } finally {
            if (in != null) {
               if (var5 != null) {
                  try {
                     in.close();
                  } catch (Throwable var39) {
                     var5.addSuppressed(var39);
                  }
               } else {
                  in.close();
               }
            }

         }

      } catch (IOException var42) {
         throw new GabeNewellException("An error occurred while reading BMD " + modelLoc.func_110623_a(), var42);
      }
   }

   private Material requestMaterial(String materialName) throws GabeNewellException {
      if (!this.owner.usesMaterials) {
         return null;
      } else {
         if (this.materialsByName == null) {
            this.materialsByName = new HashMap();
         }

         Material result = (Material)this.materialsByName.get(materialName);
         if (result != null) {
            return result;
         } else {
            String materialPath = this.owner.getMaterialPath(materialName);
            URL materialURL = SmdModel.class.getResource(materialPath);

            try {
               File materialFile = new File(materialURL.toURI());
               result = new Material(materialFile);
               this.materialsByName.put(materialName, result);
               return result;
            } catch (Exception var6) {
               throw new GabeNewellException(var6);
            }
         }
      }
   }

   private void parseBone(String line, int lineCount, SmdModel body) {
      String[] params = line.split("\"");
      int id = Integer.parseInt(RegexPatterns.SPACE_SYMBOL.matcher(params[0]).replaceAll(""));
      String boneName = params[1];
      Bone theBone = body != null ? body.getBoneByName(boneName) : null;
      if (theBone == null) {
         int parentID = Integer.parseInt(RegexPatterns.SPACE_SYMBOL.matcher(params[2]).replaceAll(""));
         Bone parent = parentID >= 0 ? (Bone)this.bones.get(parentID) : null;
         theBone = new Bone(boneName, id, parent, this);
      }

      CommonHelper.ensureIndex(this.bones, id);
      this.bones.set(id, theBone);
      this.nameToBoneMapping.put(boneName, theBone);
      ValveStudioModel.print(boneName);
   }

   private void parseBoneValues(String line, int lineCount) {
      String[] params = RegexPatterns.MULTIPLE_WHITESPACE.split(line);
      int id = Integer.parseInt(params[0]);
      float[] locRots = new float[6];

      for(int i = 1; i < 7; ++i) {
         locRots[i - 1] = Float.parseFloat(params[i]);
      }

      Bone theBone = (Bone)this.bones.get(id);
      theBone.setRest(VectorHelper.matrix4FromLocRot(locRots[0], -locRots[1], -locRots[2], locRots[3], -locRots[4], -locRots[5]));
   }

   private void parseFace(String[] params, int lineCount, Material mat) {
      DeformVertex[] faceVerts = new DeformVertex[3];
      TextureCoordinate[] uvs = new TextureCoordinate[3];

      for(int i = 0; i < 3; ++i) {
         String[] values = RegexPatterns.MULTIPLE_WHITESPACE.split(params[i]);
         float x = Float.parseFloat(values[1]);
         float y = -Float.parseFloat(values[2]);
         float z = -Float.parseFloat(values[3]);
         float xn = Float.parseFloat(values[4]);
         float yn = -Float.parseFloat(values[5]);
         float zn = -Float.parseFloat(values[6]);
         DeformVertex v = this.getExisting(x, y, z);
         if (v == null) {
            if (PixelmonConfig.smoothAnimations) {
               faceVerts[i] = new DeformVertexSmooth(x, y, z, xn, yn, zn, this.vertexIDBank);
            } else {
               faceVerts[i] = new DeformVertex(x, y, z, xn, yn, zn, this.vertexIDBank);
            }

            CommonHelper.ensureIndex(this.verts, this.vertexIDBank);
            this.verts.set(this.vertexIDBank, faceVerts[i]);
            ++this.vertexIDBank;
         } else {
            faceVerts[i] = v;
         }

         uvs[i] = new TextureCoordinate(Float.parseFloat(values[7]), 1.0F - Float.parseFloat(values[8]));
         if (values.length > 10) {
            this.doBoneWeights(values, faceVerts[i]);
         }
      }

      NormalizedFace face = new NormalizedFace(faceVerts, uvs);
      face.vertices = faceVerts;
      face.textureCoordinates = uvs;
      this.faces.add(face);
      if (mat != null) {
         if (this.facesByMaterial == null) {
            this.facesByMaterial = new HashMap();
         }

         ArrayList list = (ArrayList)this.facesByMaterial.get(mat);
         if (list == null) {
            this.facesByMaterial.put(mat, list = new ArrayList());
         }

         list.add(face);
      }

   }

   private DeformVertex getExisting(float x, float y, float z) {
      Iterator var4 = this.verts.iterator();

      DeformVertex v;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         v = (DeformVertex)var4.next();
      } while(!v.equals(x, y, z));

      return v;
   }

   private void doBoneWeights(String[] values, DeformVertex vert) {
      int links = Integer.parseInt(values[9]);
      float[] weights = new float[links];
      float sum = 0.0F;

      int i;
      for(i = 0; i < links; ++i) {
         weights[i] = Float.parseFloat(values[i * 2 + 11]);
         sum += weights[i];
      }

      for(i = 0; i < links; ++i) {
         int boneID = Integer.parseInt(values[i * 2 + 10]);
         float weight = weights[i] / sum;
         ((Bone)this.bones.get(boneID)).addVertex(vert, weight);
      }

   }

   private void setBoneChildren() {
      for(int i = 0; i < this.bones.size(); ++i) {
         Bone theBone = (Bone)this.bones.get(i);
         this.bones.stream().filter((child) -> {
            return child.parent == theBone;
         }).forEach(theBone::addChild);
      }

   }

   private void determineRoot() {
      Iterator var1 = this.bones.iterator();

      Bone b;
      while(var1.hasNext()) {
         b = (Bone)var1.next();
         if (b.parent == null && !b.children.isEmpty()) {
            this.root = b;
            break;
         }
      }

      if (this.root == null) {
         var1 = this.bones.iterator();

         while(var1.hasNext()) {
            b = (Bone)var1.next();
            if (!b.name.equals("blender_implicit")) {
               this.root = b;
               break;
            }
         }
      }

   }

   public void setAnimation(SmdAnimation anim) {
      this.currentAnim = anim;
   }

   public Bone getBoneByID(int id) {
      try {
         return (Bone)this.bones.get(id);
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public Bone getBoneByName(String name) {
      Iterator var2 = this.bones.iterator();

      Bone b;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         b = (Bone)var2.next();
      } while(!b.name.equals(name));

      return b;
   }

   public AnimFrame currentFrame() {
      return this.currentAnim == null ? null : (this.currentAnim.frames == null ? null : (this.currentAnim.frames.isEmpty() ? null : (AnimFrame)this.currentAnim.frames.get(this.currentAnim.currentFrameIndex)));
   }

   public void resetVerts() {
      this.verts.forEach(DeformVertex::reset);
   }

   public void render(boolean hasChanged, float partialTick) {
      boolean isPokeball = false;
      if (this.owner.resource.func_110623_a().contains("pokeballs")) {
         isPokeball = true;
      }

      boolean smooth = isPokeball ? PixelmonConfig.enableSmoothPokeballShading : PixelmonConfig.enableSmoothPokemonShading;
      if (this.owner.overrideSmoothShading) {
         smooth = false;
      }

      if (!OpenGlHelper.func_176075_f()) {
         GL11.glBegin(4);
         Iterator var5;
         if (!this.owner.usesMaterials) {
            var5 = this.faces.iterator();

            while(var5.hasNext()) {
               NormalizedFace f = (NormalizedFace)var5.next();
               f.addFaceForRender(smooth);
            }
         } else {
            var5 = this.facesByMaterial.entrySet().iterator();

            label57:
            while(true) {
               Map.Entry entry;
               do {
                  if (!var5.hasNext()) {
                     break label57;
                  }

                  entry = (Map.Entry)var5.next();
               } while(entry.getKey() == null);

               ((Material)entry.getKey()).pre();
               Iterator var7 = ((ArrayList)entry.getValue()).iterator();

               while(var7.hasNext()) {
                  NormalizedFace face = (NormalizedFace)var7.next();
                  face.addFaceForRender(smooth);
               }

               ((Material)entry.getKey()).post();
            }
         }

         GL11.glEnd();
      } else {
         if (hasChanged) {
            if (this.vertexVbo == -1) {
               this.vertexVbo = ARBBufferObject.glGenBuffersARB();
               this.textureVbo = ARBBufferObject.glGenBuffersARB();
               this.normalsVbo = ARBBufferObject.glGenBuffersARB();
               this.buildVBO(smooth, partialTick);
            } else {
               this.updateVBO(smooth, partialTick);
            }
         }

         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.vertexVbo);
         GL11.glVertexPointer(3, 5126, 0, 0L);
         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.textureVbo);
         GL11.glTexCoordPointer(2, 5126, 0, 0L);
         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.normalsVbo);
         GL11.glNormalPointer(5126, 0, 0L);
         GL11.glEnableClientState(32884);
         GL11.glEnableClientState(32888);
         GL11.glEnableClientState(32885);
         GL11.glDrawArrays(4, 0, this.faces.size() * 3);
         GL11.glDisableClientState(32884);
         GL11.glDisableClientState(32888);
         GL11.glDisableClientState(32885);
         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
      }

   }

   private void updateVBO(boolean smoothShading, float partialTick) {
      this.vertexBuffer.clear();
      this.normalBuffer.clear();
      Iterator var3 = this.faces.iterator();

      while(var3.hasNext()) {
         NormalizedFace face = (NormalizedFace)var3.next();
         face.addFaceForRender(this.vertexBuffer, this.normalBuffer, smoothShading, partialTick);
      }

      this.vertexBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.vertexVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, this.vertexBuffer, 35044);
      this.normalBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.normalsVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, this.normalBuffer, 35044);
   }

   private void buildVBO(boolean smoothShading, float partialTick) {
      this.vertexBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 9);
      FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 6);
      this.normalBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 9);
      Iterator var4 = this.faces.iterator();

      while(var4.hasNext()) {
         NormalizedFace face = (NormalizedFace)var4.next();
         face.addFaceForRender(this.vertexBuffer, textureBuffer, this.normalBuffer, smoothShading, partialTick);
      }

      this.vertexBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.vertexVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, this.vertexBuffer, 35044);
      textureBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.textureVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, textureBuffer, 35044);
      this.normalBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.normalsVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, this.normalBuffer, 35044);
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
