package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.client.models.bikes.ModelBike;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumBike;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

public class SharedModels {
   private static final GenericModelHolder baseBall;
   private static final GenericModelHolder greatBall;
   private static final GenericModelHolder heavyBall;
   private static final GenericModelHolder netBall;
   private static final GenericModelHolder timerBall;
   private static final GenericModelHolder cherishBall;
   private static final GenericModelHolder masterBall;
   private static final GenericModelHolder beastBall;
   public static final GenericModelHolder helixFossil;
   public static final GenericModelHolder domeFossil;
   public static final GenericModelHolder oldAmber;
   public static final GenericModelHolder rootFossil;
   public static final GenericModelHolder clawFossil;
   public static final GenericModelHolder skullFossil;
   public static final GenericModelHolder armorFossil;
   public static final GenericModelHolder coverFossil;
   public static final GenericModelHolder plumeFossil;
   public static final GenericModelHolder sailFossil;
   public static final GenericModelHolder jawFossil;
   public static final GenericModelHolder birdFossil;
   public static final GenericModelHolder dinoFossil;
   public static final GenericModelHolder drakeFossil;
   public static final GenericModelHolder fishFossil;
   public static final GenericModelHolder avFossil;
   public static final GenericModelHolder azFossil;
   public static final GenericModelHolder dvFossil;
   public static final GenericModelHolder dzFossil;
   private static final GenericModelHolder roadBike;
   private static final GenericModelHolder acroBike;

   public static GenericModelHolder getPokeballModel(EnumPokeballs pokeball) {
      switch (pokeball) {
         case GreatBall:
            return greatBall;
         case HeavyBall:
            return heavyBall;
         case NetBall:
            return netBall;
         case TimerBall:
            return timerBall;
         case CherishBall:
            return cherishBall;
         case MasterBall:
            return masterBall;
         case BeastBall:
            return beastBall;
         default:
            return baseBall;
      }
   }

   public static GenericModelHolder getBikeModel(EnumBike bike) {
      return bike == EnumBike.Acro ? acroBike : roadBike;
   }

   static {
      baseBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Pokeball});
      greatBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Greatball});
      heavyBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Heavyball});
      netBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Netball});
      timerBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Timerball});
      cherishBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Cherishball});
      masterBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Masterball});
      beastBall = new GenericModelHolder(ModelPokeballs.class, new Object[]{EnumCustomModel.Beastball});
      helixFossil = new GenericModelHolder("fossils/helix.pqc");
      domeFossil = new GenericModelHolder("fossils/dome.pqc");
      oldAmber = new GenericModelHolder("fossils/oldamber.pqc");
      rootFossil = new GenericModelHolder("fossils/root.pqc");
      clawFossil = new GenericModelHolder("fossils/claw.pqc");
      skullFossil = new GenericModelHolder("fossils/skull.pqc");
      armorFossil = new GenericModelHolder("fossils/armor.pqc");
      coverFossil = new GenericModelHolder("fossils/cover.pqc");
      plumeFossil = new GenericModelHolder("fossils/plume.pqc");
      sailFossil = new GenericModelHolder("fossils/sail.pqc");
      jawFossil = new GenericModelHolder("fossils/jaw.pqc");
      birdFossil = new GenericModelHolder("fossils/bird.pqc");
      dinoFossil = new GenericModelHolder("fossils/dino.pqc");
      drakeFossil = new GenericModelHolder("fossils/drake.pqc");
      fishFossil = new GenericModelHolder("fossils/fish.pqc");
      avFossil = new GenericModelHolder("fossils/av.pqc");
      azFossil = new GenericModelHolder("fossils/az.pqc");
      dvFossil = new GenericModelHolder("fossils/dv.pqc");
      dzFossil = new GenericModelHolder("fossils/dz.pqc");
      roadBike = new GenericModelHolder(ModelBike.class, new Object[]{EnumCustomModel.MachBike});
      acroBike = new GenericModelHolder(ModelBike.class, new Object[]{EnumCustomModel.AcroBike});
   }
}
