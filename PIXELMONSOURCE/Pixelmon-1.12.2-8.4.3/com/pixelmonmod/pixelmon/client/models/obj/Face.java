package com.pixelmonmod.pixelmon.client.models.obj;

public class Face {
   public static int GL_TRIANGLES = 1;
   public static int GL_QUADS = 2;
   public static int POLY_FACE = 3;
   public int[] vertIndices;
   public int[] normIndices;
   public int[] texIndices;
   private Vertex[] vertices;
   private Vertex[] normals;
   private TextureCoordinate[] textures;
   private int type;

   public int[] getIndices() {
      return this.vertIndices;
   }

   public Vertex[] getVertices() {
      return this.vertices;
   }

   public void setIndices(int[] indices) {
      this.vertIndices = indices;
   }

   public void setVertices(Vertex[] vertices) {
      this.vertices = vertices;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public Vertex[] getNormals() {
      return this.normals;
   }

   public void setNormals(Vertex[] normals) {
      this.normals = normals;
   }

   public TextureCoordinate[] getTextures() {
      return this.textures;
   }

   public void setTextures(TextureCoordinate[] textures) {
      this.textures = textures;
   }
}
