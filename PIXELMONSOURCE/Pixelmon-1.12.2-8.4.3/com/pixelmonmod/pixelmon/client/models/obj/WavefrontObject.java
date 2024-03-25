package com.pixelmonmod.pixelmon.client.models.obj;

import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;
import com.pixelmonmod.pixelmon.client.models.obj.parser.obj.ObjLineParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.GL11;

public class WavefrontObject implements IPixelmonModel, IModel {
   private ArrayList vertices;
   private ArrayList normals;
   private ArrayList textures;
   private ArrayList groups;
   private HashMap groupsDirectAccess;
   HashMap materials;
   private ObjLineParserFactory parserFactory;
   private Material currentMaterial;
   private Group currentGroup;
   public double radius;
   public float xScale;
   public float yScale;
   public float zScale;
   public Vertex translate;
   public Vertex rotate;
   public int displayListId;
   public int vertexVbo;
   public int textureVbo;
   public int normalsVbo;
   public int vertexCount;

   public WavefrontObject(InputStream stream) {
      this(stream, 1.0F, 1.0F, 1.0F, new Vertex(), new Vertex());
   }

   public WavefrontObject(InputStream stream, float xScale, float yScale, float zScale) {
      this(stream, xScale, yScale, zScale, new Vertex(), new Vertex());
   }

   public WavefrontObject(InputStream stream, float scale) {
      this(stream, scale, scale, scale, new Vertex(), new Vertex());
   }

   public WavefrontObject(InputStream stream, float scale, Vertex translation, Vertex rotation) {
      this(stream, scale, scale, scale, translation, rotation);
   }

   public WavefrontObject(InputStream stream, float xScale, float yScale, float zScale, Vertex translation, Vertex rotation) {
      this.vertices = new ArrayList();
      this.normals = new ArrayList();
      this.textures = new ArrayList();
      this.groups = new ArrayList();
      this.groupsDirectAccess = new HashMap();
      this.materials = new HashMap();
      this.radius = 0.0;
      this.displayListId = 0;
      this.vertexVbo = -1;
      this.textureVbo = -1;
      this.normalsVbo = -1;
      this.vertexCount = 0;

      try {
         this.translate = translation;
         this.rotate = rotation;
         this.xScale = xScale;
         this.yScale = yScale;
         this.zScale = zScale;
         this.parse(stream);
         this.calculateRadius();
      } catch (Exception var8) {
         System.out.println("Error, could not load obj");
      }

   }

   private void calculateRadius() {
      Iterator var3 = this.vertices.iterator();

      while(var3.hasNext()) {
         Vertex vertex = (Vertex)var3.next();
         double currentNorm = vertex.norm();
         if (currentNorm > this.radius) {
            this.radius = currentNorm;
         }
      }

   }

   public void parse(InputStream fileInput) {
      this.parserFactory = new ObjLineParserFactory(this);
      BufferedReader in = null;

      try {
         in = new BufferedReader(new InputStreamReader(fileInput));
         String currentLine = null;

         while((currentLine = in.readLine()) != null) {
            this.parseLine(currentLine);
         }

         in.close();
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new RuntimeException("Error reading file");
      }
   }

   private void parseLine(String currentLine) {
      if (!"".equals(currentLine)) {
         LineParser parser = this.parserFactory.getLineParser(currentLine);
         parser.parse();
         parser.incoporateResults(this);
      }
   }

   public void render() {
      boolean useVBO = OpenGlHelper.func_176075_f();
      if (!useVBO) {
         if (this.displayListId != 0) {
            GL11.glCallList(this.displayListId);
            return;
         }

         this.displayListId = GL11.glGenLists(1);
         GL11.glNewList(this.displayListId, 4864);

         for(int i = 0; i < this.getGroups().size(); ++i) {
            this.renderGroup((Group)this.getGroups().get(i));
         }

         GL11.glEndList();
         GL11.glCallList(this.displayListId);
      } else {
         if (this.vertexVbo == -1) {
            this.vertexVbo = ARBBufferObject.glGenBuffersARB();
            this.textureVbo = ARBBufferObject.glGenBuffersARB();
            this.normalsVbo = ARBBufferObject.glGenBuffersARB();
            this.buildVBO();
            this.vertexCount = 0;

            Group g;
            for(Iterator var4 = this.groups.iterator(); var4.hasNext(); this.vertexCount += g.vertices.size()) {
               g = (Group)var4.next();
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
         GL11.glDrawArrays(4, 0, this.vertexCount);
         GL11.glDisableClientState(32884);
         GL11.glDisableClientState(32888);
         GL11.glDisableClientState(32885);
         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
      }

   }

   public void buildVBO() {
      int vertexCount = 0;
      int texCount = 0;
      int normalCount = 0;

      Group g;
      for(Iterator var4 = this.groups.iterator(); var4.hasNext(); normalCount += g.normals.size()) {
         g = (Group)var4.next();
         vertexCount += g.vertices.size();
         texCount += g.texcoords.size();
      }

      FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
      Iterator var10 = this.groups.iterator();

      Iterator var7;
      while(var10.hasNext()) {
         Group group = (Group)var10.next();
         var7 = group.vertices.iterator();

         while(var7.hasNext()) {
            Vertex vertex = (Vertex)var7.next();
            vertexBuffer.put(vertex.getX());
            vertexBuffer.put(vertex.getY());
            vertexBuffer.put(vertex.getZ());
         }
      }

      vertexBuffer.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.vertexVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, vertexBuffer, 35044);
      FloatBuffer textureCoords = BufferUtils.createFloatBuffer(texCount * 2);
      Iterator var12 = this.groups.iterator();

      while(var12.hasNext()) {
         Group group = (Group)var12.next();
         group.texcoords.stream().filter((texCoord) -> {
            return texCoord != null;
         }).forEach((texCoord) -> {
            textureCoords.put(texCoord.getU());
            textureCoords.put(texCoord.getV());
         });
      }

      textureCoords.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.textureVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, textureCoords, 35044);
      FloatBuffer normals = BufferUtils.createFloatBuffer(normalCount * 3);
      var7 = this.groups.iterator();

      while(var7.hasNext()) {
         Group group = (Group)var7.next();
         group.normals.stream().filter((vertexx) -> {
            return vertexx != null;
         }).forEach((vertexx) -> {
            normals.put(vertexx.getX());
            normals.put(vertexx.getY());
            normals.put(vertexx.getZ());
         });
      }

      normals.flip();
      OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.normalsVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.field_176089_P, normals, 35044);
   }

   protected void renderGroup(Group group) {
      this.currentGroup = group;
      Iterator var2 = this.currentGroup.getFaces().iterator();

      while(var2.hasNext()) {
         Face face = (Face)var2.next();
         GL11.glBegin(4);

         for(int vertexID = 0; vertexID < face.getVertices().length; ++vertexID) {
            Vertex vertex = face.getVertices()[vertexID];
            if (vertexID < face.getNormals().length && face.getNormals()[vertexID] != null) {
               Vertex normal = face.getNormals()[vertexID];
               GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
            }

            if (vertexID < face.getTextures().length && face.getTextures()[vertexID] != null) {
               TextureCoordinate textureCoord = face.getTextures()[vertexID];
               GL11.glTexCoord2d((double)textureCoord.getU(), (double)textureCoord.getV());
            }

            GL11.glVertex3f(vertex.getX(), vertex.getY(), vertex.getZ());
         }

         GL11.glEnd();
      }

   }

   public void setMaterials(HashMap materials) {
      this.materials = materials;
   }

   public void setTextures(ArrayList textures) {
      this.textures = textures;
   }

   public ArrayList getTextureList() {
      return this.textures;
   }

   public void setVertices(ArrayList vertices) {
      this.vertices = vertices;
   }

   public ArrayList getVertices() {
      return this.vertices;
   }

   public void setNormals(ArrayList normals) {
      this.normals = normals;
   }

   public ArrayList getNormals() {
      return this.normals;
   }

   public HashMap getMaterials() {
      return this.materials;
   }

   public Material getCurrentMaterial() {
      return this.currentMaterial;
   }

   public void setCurrentMaterial(Material currentMaterial) {
      this.currentMaterial = currentMaterial;
   }

   public ArrayList getGroups() {
      return this.groups;
   }

   public HashMap getGroupsDirectAccess() {
      return this.groupsDirectAccess;
   }

   public Group getCurrentGroup() {
      return this.currentGroup;
   }

   public void setCurrentGroup(Group currentGroup) {
      this.currentGroup = currentGroup;
   }

   public String getBoudariesText() {
      float minX = 0.0F;
      float maxX = 0.0F;
      float minY = 0.0F;
      float maxY = 0.0F;
      float minZ = 0.0F;
      float maxZ = 0.0F;
      Vertex currentVertex = null;

      for(int i = 0; i < this.getVertices().size(); ++i) {
         currentVertex = (Vertex)this.getVertices().get(i);
         if (currentVertex.getX() > maxX) {
            maxX = currentVertex.getX();
         }

         if (currentVertex.getX() < minX) {
            minX = currentVertex.getX();
         }

         if (currentVertex.getY() > maxY) {
            maxY = currentVertex.getY();
         }

         if (currentVertex.getY() < minY) {
            minY = currentVertex.getY();
         }

         if (currentVertex.getZ() > maxZ) {
            maxZ = currentVertex.getZ();
         }

         if (currentVertex.getZ() < minZ) {
            minZ = currentVertex.getZ();
         }
      }

      return "maxX=" + maxX + " minX=" + minX + " maxY=" + maxY + " minY=" + minY + " maxZ=" + maxZ + " minZ=" + minZ;
   }

   public void printBoudariesText() {
      System.out.println(this.getBoudariesText());
   }

   public void renderAll(float partialTick) {
      try {
         this.render();
      } catch (Exception var3) {
         var3.printStackTrace();
         if (OpenGlHelper.func_176075_f()) {
            OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
         }
      }

   }

   public Collection getDependencies() {
      return null;
   }

   public IBakedModel bake(IModelState state, VertexFormat format, Function bakedTextureGetter) {
      return null;
   }

   public IModelState getDefaultState() {
      return null;
   }

   public Collection getTextures() {
      return null;
   }
}
