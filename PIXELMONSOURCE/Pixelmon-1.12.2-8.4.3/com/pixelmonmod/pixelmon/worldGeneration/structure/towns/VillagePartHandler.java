package com.pixelmonmod.pixelmon.worldGeneration.structure.towns;

import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import java.util.List;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillagePartHandler implements VillagerRegistry.IVillageCreationHandler {
   public static int weightSum = 0;
   public static int numCompSum = 0;

   public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new StructureVillagePieces.PieceWeight(ComponentTownPart.class, weightSum, numCompSum);
   }

   public Class getComponentClass() {
      return ComponentTownPart.class;
   }

   public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
      try {
         StructureInfo info = StructureRegistry.getRandomTownPiece(random, pieces);
         return ComponentTownPart.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5, info);
      } catch (Exception var11) {
         var11.printStackTrace();
         return null;
      }
   }
}
