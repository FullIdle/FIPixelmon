package com.pixelmonmod.pixelmon.client.camera;

public interface ICameraTarget {
   double getX();

   double getXSeeOffset();

   double getY();

   double getYSeeOffset();

   double getZ();

   double getZSeeOffset();

   double minimumCameraDistance();

   boolean isValidTarget();

   boolean hasChanged();

   boolean setTargetData(Object var1);

   Object getTargetData();
}
