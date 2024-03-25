package com.pixelmonmod.pixelmon.client.models.obj;

import java.util.ArrayList;
import java.util.Iterator;

public class Group {
   private String name;
   private Vertex min = null;
   private Material material;
   private ArrayList faces = new ArrayList();
   public ArrayList indices = new ArrayList();
   public ArrayList vertices = new ArrayList();
   public ArrayList normals = new ArrayList();
   public ArrayList texcoords = new ArrayList();
   public int indexCount = 0;

   public Group(String name) {
      this.name = name;
   }

   public void setMaterial(Material material) {
      this.material = material;
   }

   public void addFace(Face face) {
      this.faces.add(face);
   }

   public void pack() {
      float minX = 0.0F;
      float minY = 0.0F;
      float minZ = 0.0F;
      Face currentFace = null;
      Vertex currentVertex = null;
      Iterator var6 = this.faces.iterator();

      while(var6.hasNext()) {
         Face face = (Face)var6.next();
         currentFace = face;

         for(int j = 0; j < currentFace.getVertices().length; ++j) {
            currentVertex = currentFace.getVertices()[j];
            if (Math.abs(currentVertex.getX()) > minX) {
               minX = Math.abs(currentVertex.getX());
            }

            if (Math.abs(currentVertex.getY()) > minY) {
               minY = Math.abs(currentVertex.getY());
            }

            if (Math.abs(currentVertex.getZ()) > minZ) {
               minZ = Math.abs(currentVertex.getZ());
            }
         }
      }

      this.min = new Vertex(minX, minY, minZ);
   }

   public String getName() {
      return this.name;
   }

   public Material getMaterial() {
      return this.material;
   }

   public ArrayList getFaces() {
      return this.faces;
   }

   public Vertex getMin() {
      return this.min;
   }
}
