package com.pixelmonmod.pixelmon.client.models.animations;

public enum EnumGeomData {
   xloc,
   yloc,
   zloc,
   xrot,
   yrot,
   zrot;

   public boolean isRotation() {
      return this == xrot || this == yrot || this == zrot;
   }
}
