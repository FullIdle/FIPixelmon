package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.Face;
import com.pixelmonmod.pixelmon.client.models.obj.Group;
import com.pixelmonmod.pixelmon.client.models.obj.TextureCoordinate;
import com.pixelmonmod.pixelmon.client.models.obj.Vertex;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class FaceParser extends LineParser {
   private Face face;
   public int[] vindices;
   public int[] nindices;
   public int[] tindices;
   private Vertex[] vertices;
   private Vertex[] normals;
   private TextureCoordinate[] textures;
   private WavefrontObject object = null;

   public FaceParser(WavefrontObject object) {
      this.object = object;
   }

   public void parse() {
      this.face = new Face();
      switch (this.words.length) {
         case 4:
            this.parseTriangles();
            break;
         case 5:
            this.parseQuad();
            break;
         default:
            this.parsePolyFace(this.words.length - 1);
      }

   }

   private void parseTriangles() {
      this.face.setType(Face.GL_TRIANGLES);
      this.parseLine(3);
   }

   private void parseLine(int vertexCount) {
      String[] rawFaces = null;
      this.vindices = new int[vertexCount];
      this.nindices = new int[vertexCount];
      this.tindices = new int[vertexCount];
      this.vertices = new Vertex[vertexCount];
      this.normals = new Vertex[vertexCount];
      this.textures = new TextureCoordinate[vertexCount];

      for(int i = 1; i <= vertexCount; ++i) {
         rawFaces = this.words[i].split("/");
         int currentValue = Integer.parseInt(rawFaces[0]);
         this.vindices[i - 1] = currentValue - 1;
         this.vertices[i - 1] = (Vertex)this.object.getVertices().get(currentValue - 1);
         if (rawFaces.length != 1 && rawFaces.length != 2) {
            if (!"".equals(rawFaces[1])) {
               currentValue = Integer.parseInt(rawFaces[1]);
               if (currentValue <= this.object.getTextureList().size()) {
                  this.tindices[i - 1] = currentValue - 1;
                  this.textures[i - 1] = (TextureCoordinate)this.object.getTextureList().get(currentValue - 1);
               }
            }

            currentValue = Integer.parseInt(rawFaces[2]);
            this.nindices[i - 1] = currentValue - 1;
            this.normals[i - 1] = (Vertex)this.object.getNormals().get(currentValue - 1);
         }
      }

   }

   private void parseQuad() {
      this.face.setType(Face.GL_QUADS);
      this.parseLine(4);
   }

   private void parsePolyFace(int verticesCount) {
      this.face.setType(Face.POLY_FACE);
      this.parseLine(verticesCount);
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Group group = wavefrontObject.getCurrentGroup();
      if (group == null) {
         group = new Group("Default created by loader");
         wavefrontObject.getGroups().add(group);
         wavefrontObject.getGroupsDirectAccess().put(group.getName(), group);
         wavefrontObject.setCurrentGroup(group);
      }

      for(int i = 0; i < this.vertices.length; ++i) {
         group.vertices.add(this.vertices[i]);
         group.normals.add(this.normals[i]);
         group.texcoords.add(this.textures[i]);
         group.indices.add(group.indexCount++);
      }

      this.face.vertIndices = this.vindices;
      this.face.normIndices = this.nindices;
      this.face.texIndices = this.tindices;
      this.face.setNormals(this.normals);
      this.face.setVertices(this.vertices);
      this.face.setTextures(this.textures);
      wavefrontObject.getCurrentGroup().addFace(this.face);
   }
}
