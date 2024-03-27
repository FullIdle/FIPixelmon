package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.items.ItemHammer;
import com.pixelmonmod.pixelmon.items.ItemPixelmonBoots;
import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.DawnstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.DuskstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.FirestoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.LeafstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.MoonstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.SpeedModifier;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.SunstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.ThunderstoneEffect;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.WaterstoneEffect;
import com.pixelmonmod.pixelmon.items.tools.GenericAxe;
import com.pixelmonmod.pixelmon.items.tools.GenericHammer;
import com.pixelmonmod.pixelmon.items.tools.GenericHoe;
import com.pixelmonmod.pixelmon.items.tools.GenericPickaxe;
import com.pixelmonmod.pixelmon.items.tools.GenericShovel;
import com.pixelmonmod.pixelmon.items.tools.GenericSword;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class PixelmonItemsTools {
   public static final Item.ToolMaterial ALUMINIUM = EnumHelper.addToolMaterial("ALUMINUM", 2, 200, 6.5F, 2.0F, 14);
   public static final Item.ToolMaterial RUBY = EnumHelper.addToolMaterial("RUBY", 2, 300, 6.5F, 2.0F, 14);
   public static final Item.ToolMaterial SAPPHIRE = EnumHelper.addToolMaterial("SAPHIRE", 2, 300, 6.5F, 2.0F, 14);
   public static final Item.ToolMaterial AMETHYST = EnumHelper.addToolMaterial("AMETHYST", 2, 300, 6.5F, 2.0F, 14);
   public static final Item.ToolMaterial CRYSTAL = EnumHelper.addToolMaterial("CRYSTAL", 2, 300, 6.5F, 2.0F, 14);
   public static final Item.ToolMaterial FIRESTONE = EnumHelper.addToolMaterial("FIRESTONE", 3, 1561, 8.0F, 3.0F, 10);
   public static final Item.ToolMaterial WATERSTONE = EnumHelper.addToolMaterial("WATERSTONE", 3, 1561, 8.0F, 3.0F, 10);
   public static final Item.ToolMaterial LEAFSTONE = EnumHelper.addToolMaterial("LEAFSTONE", 2, 250, 6.0F, 2.0F, 14);
   public static final Item.ToolMaterial THUNDERSTONE = EnumHelper.addToolMaterial("ELEMENTSTONE", 3, 1561, 12.0F, 0.0F, 22);
   public static final Item.ToolMaterial SUNSTONE = EnumHelper.addToolMaterial("ELEMENTSTONE", 3, 1561, 12.0F, 0.0F, 22);
   public static final Item.ToolMaterial MOONSTONE = EnumHelper.addToolMaterial("ELEMENTSTONE", 3, 1561, 12.0F, 0.0F, 22);
   public static final Item.ToolMaterial DAWNSTONE = EnumHelper.addToolMaterial("ELEMENTSTONE", 3, 1561, 12.0F, 0.0F, 22);
   public static final Item.ToolMaterial DUSKSTONE = EnumHelper.addToolMaterial("ELEMENTSTONE", 3, 1561, 12.0F, 0.0F, 22);
   public static final ItemArmor.ArmorMaterial ALUMINIUMARMOR;
   public static final ItemArmor.ArmorMaterial RUNNINGARMOR;
   public static final ItemArmor.ArmorMaterial OLDRUNNINGARMOR;
   public static final ItemArmor.ArmorMaterial RUBYARMORMAT;
   public static final ItemArmor.ArmorMaterial SAPPHIREARMORMAT;
   public static final ItemArmor.ArmorMaterial PLASMAARMORMAT;
   public static final ItemArmor.ArmorMaterial GALACTICARMORMAT;
   public static final ItemArmor.ArmorMaterial NEOARMORMAT;
   public static final ItemArmor.ArmorMaterial ROCKETARMORMAT;
   public static final ItemArmor.ArmorMaterial FIRESTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial WATERSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial LEAFSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial THUNDERSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial SUNSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial MOONSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial DAWNSTONEARMORMAT;
   public static final ItemArmor.ArmorMaterial DUSKSTONEARMORMAT;
   public static Item newRunningShoes;
   public static Item oldRunningShoes;
   public static Item rubyHammerItem;
   public static Item rubyAxeItem;
   public static Item rubyHoeItem;
   public static Item rubyPickaxeItem;
   public static Item rubyShovelItem;
   public static Item rubySwordItem;
   public static Item sapphireHammerItem;
   public static Item sapphireAxeItem;
   public static Item sapphireHoeItem;
   public static Item sapphirePickaxeItem;
   public static Item sapphireShovelItem;
   public static Item sapphireSwordItem;
   public static Item amethystHammerItem;
   public static Item amethystAxeItem;
   public static Item amethystHoeItem;
   public static Item amethystPickaxeItem;
   public static Item amethystShovelItem;
   public static Item amethystSwordItem;
   public static Item crystalHammerItem;
   public static Item crystalAxeItem;
   public static Item crystalHoeItem;
   public static Item crystalPickaxeItem;
   public static Item crystalShovelItem;
   public static Item crystalSwordItem;
   public static Item firestoneHammerItem;
   public static Item firestoneAxeItem;
   public static Item firestoneHoeItem;
   public static Item firestonePickaxeItem;
   public static Item firestoneShovelItem;
   public static Item firestoneSwordItem;
   public static Item waterstoneHammerItem;
   public static Item waterstoneAxeItem;
   public static Item waterstoneHoeItem;
   public static Item waterstonePickaxeItem;
   public static Item waterstoneShovelItem;
   public static Item waterstoneSwordItem;
   public static Item leafstoneHammerItem;
   public static Item leafstoneAxeItem;
   public static Item leafstoneHoeItem;
   public static Item leafstonePickaxeItem;
   public static Item leafstoneShovelItem;
   public static Item leafstoneSwordItem;
   public static Item thunderstoneHammerItem;
   public static Item thunderstoneAxeItem;
   public static Item thunderstoneHoeItem;
   public static Item thunderstonePickaxeItem;
   public static Item thunderstoneShovelItem;
   public static Item thunderstoneSwordItem;
   public static Item sunstoneHammerItem;
   public static Item sunstoneAxeItem;
   public static Item sunstoneHoeItem;
   public static Item sunstonePickaxeItem;
   public static Item sunstoneShovelItem;
   public static Item sunstoneSwordItem;
   public static Item moonstoneHammerItem;
   public static Item moonstoneAxeItem;
   public static Item moonstoneHoeItem;
   public static Item moonstonePickaxeItem;
   public static Item moonstoneShovelItem;
   public static Item moonstoneSwordItem;
   public static Item dawnstoneHammerItem;
   public static Item dawnstoneAxeItem;
   public static Item dawnstoneHoeItem;
   public static Item dawnstonePickaxeItem;
   public static Item dawnstoneShovelItem;
   public static Item dawnstoneSwordItem;
   public static Item duskstoneHammerItem;
   public static Item duskstoneAxeItem;
   public static Item duskstoneHoeItem;
   public static Item duskstonePickaxeItem;
   public static Item duskstoneShovelItem;
   public static Item duskstoneSwordItem;
   public static Item swordAluminium;
   public static Item rubyHelm;
   public static Item rubyPlate;
   public static Item rubyLegs;
   public static Item rubyBoots;
   public static Item sapphireHelm;
   public static Item sapphirePlate;
   public static Item sapphireLegs;
   public static Item sapphireBoots;
   public static Item plasmaHelm;
   public static Item plasmaPlate;
   public static Item plasmaLegs;
   public static Item plasmaBoots;
   public static Item galacticHelm;
   public static Item galacticPlate;
   public static Item galacticLegs;
   public static Item galacticBoots;
   public static Item neoHelm;
   public static Item neoPlate;
   public static Item neoLegs;
   public static Item neoBoots;
   public static Item rocketHelm;
   public static Item rocketPlate;
   public static Item rocketLegs;
   public static Item rocketBoots;
   public static Item firestoneHelm;
   public static Item firestonePlate;
   public static Item firestoneLegs;
   public static Item firestoneBoots;
   public static Item waterstoneHelm;
   public static Item waterstonePlate;
   public static Item waterstoneLegs;
   public static Item waterstoneBoots;
   public static Item leafstoneHelm;
   public static Item leafstonePlate;
   public static Item leafstoneLegs;
   public static Item leafstoneBoots;
   public static Item thunderstoneHelm;
   public static Item thunderstonePlate;
   public static Item thunderstoneLegs;
   public static Item thunderstoneBoots;
   public static Item sunstoneHelm;
   public static Item sunstonePlate;
   public static Item sunstoneLegs;
   public static Item sunstoneBoots;
   public static Item moonstoneHelm;
   public static Item moonstonePlate;
   public static Item moonstoneLegs;
   public static Item moonstoneBoots;
   public static Item dawnstoneHelm;
   public static Item dawnstonePlate;
   public static Item dawnstoneLegs;
   public static Item dawnstoneBoots;
   public static Item duskstoneHelm;
   public static Item duskstonePlate;
   public static Item duskstoneLegs;
   public static Item duskstoneBoots;
   public static Item hammerWood;
   public static Item hammerStone;
   public static Item hammerIron;
   public static Item hammerGold;
   public static Item hammerDiamond;
   public static Item hammerAluminium;
   public static Item axeAluminium;
   public static Item spadeAluminium;
   public static Item pickaxeAluminium;
   public static Item hoeAluminium;
   public static Item helmetAluminium;
   public static Item chestplateAluminium;
   public static Item leggingsAluminium;
   public static Item bootsAluminium;

   public static void load() {
      newRunningShoes = new ItemPixelmonBoots(RUNNINGARMOR, 0, EntityEquipmentSlot.FEET, "new_running_boots");
      oldRunningShoes = new ItemPixelmonBoots(OLDRUNNINGARMOR, 0, EntityEquipmentSlot.FEET, "old_running_boots");
      hammerWood = new ItemHammer(ToolMaterial.WOOD, "wood_hammer");
      hammerStone = new ItemHammer(ToolMaterial.STONE, "stone_hammer");
      hammerIron = new ItemHammer(ToolMaterial.IRON, "iron_hammer");
      hammerGold = new ItemHammer(ToolMaterial.GOLD, "gold_hammer");
      hammerDiamond = new ItemHammer(ToolMaterial.DIAMOND, "diamond_hammer");
      hammerAluminium = new ItemHammer(ALUMINIUM, "aluminium_hammer");
      axeAluminium = new GenericAxe(ALUMINIUM, "aluminium_axe");
      hoeAluminium = new GenericHoe(ALUMINIUM, "aluminium_hoe");
      pickaxeAluminium = new GenericPickaxe(ALUMINIUM, "aluminium_pickaxe");
      spadeAluminium = new GenericShovel(ALUMINIUM, "aluminium_spade");
      swordAluminium = new GenericSword(ALUMINIUM, "aluminium_sword");
      rubyHammerItem = new GenericHammer(RUBY, "ruby_hammer");
      rubyAxeItem = new GenericAxe(RUBY, "ruby_axe");
      rubyHoeItem = new GenericHoe(RUBY, "ruby_hoe");
      rubyPickaxeItem = new GenericPickaxe(RUBY, "ruby_pickaxe");
      rubyShovelItem = new GenericShovel(RUBY, "ruby_shovel");
      rubySwordItem = new GenericSword(RUBY, "ruby_sword");
      sapphireHammerItem = new GenericHammer(SAPPHIRE, "sapphire_hammer");
      sapphireAxeItem = new GenericAxe(SAPPHIRE, "sapphire_axe");
      sapphireHoeItem = new GenericHoe(SAPPHIRE, "sapphire_hoe");
      sapphirePickaxeItem = new GenericPickaxe(SAPPHIRE, "sapphire_pickaxe");
      sapphireShovelItem = new GenericShovel(SAPPHIRE, "sapphire_shovel");
      sapphireSwordItem = new GenericSword(SAPPHIRE, "sapphire_sword");
      amethystHammerItem = new GenericHammer(AMETHYST, "amethyst_hammer");
      amethystAxeItem = new GenericAxe(AMETHYST, "amethyst_axe");
      amethystHoeItem = new GenericHoe(AMETHYST, "amethyst_hoe");
      amethystPickaxeItem = new GenericPickaxe(AMETHYST, "amethyst_pickaxe");
      amethystShovelItem = new GenericShovel(AMETHYST, "amethyst_shovel");
      amethystSwordItem = new GenericSword(AMETHYST, "amethyst_sword");
      crystalHammerItem = new GenericHammer(CRYSTAL, "crystal_hammer");
      crystalAxeItem = new GenericAxe(CRYSTAL, "crystal_axe");
      crystalHoeItem = new GenericHoe(CRYSTAL, "crystal_hoe");
      crystalPickaxeItem = new GenericPickaxe(CRYSTAL, "crystal_pickaxe");
      crystalShovelItem = new GenericShovel(CRYSTAL, "crystal_shovel");
      crystalSwordItem = new GenericSword(CRYSTAL, "crystal_sword");
      firestoneHammerItem = new GenericHammer(FIRESTONE, "fire_stone_hammer");
      firestoneAxeItem = new GenericAxe(FIRESTONE, "fire_stone_axe");
      firestoneHoeItem = new GenericHoe(FIRESTONE, "fire_stone_hoe");
      firestonePickaxeItem = new GenericPickaxe(FIRESTONE, "fire_stone_pickaxe");
      firestoneShovelItem = new GenericShovel(FIRESTONE, "fire_stone_shovel");
      firestoneSwordItem = new GenericSword(FIRESTONE, "fire_stone_sword");
      waterstoneHammerItem = new GenericHammer(WATERSTONE, "water_stone_hammer");
      waterstoneAxeItem = new GenericAxe(WATERSTONE, "water_stone_axe");
      waterstoneHoeItem = new GenericHoe(WATERSTONE, "water_stone_hoe");
      waterstonePickaxeItem = new GenericPickaxe(WATERSTONE, "water_stone_pickaxe");
      waterstoneShovelItem = new GenericShovel(WATERSTONE, "water_stone_shovel");
      waterstoneSwordItem = new GenericSword(WATERSTONE, "water_stone_sword");
      leafstoneHammerItem = new GenericHammer(LEAFSTONE, "leaf_stone_hammer");
      leafstoneAxeItem = new GenericAxe(LEAFSTONE, "leaf_stone_axe");
      leafstoneHoeItem = new GenericHoe(LEAFSTONE, "leaf_stone_hoe");
      leafstonePickaxeItem = new GenericPickaxe(LEAFSTONE, "leaf_stone_pickaxe");
      leafstoneShovelItem = new GenericShovel(LEAFSTONE, "leaf_stone_shovel");
      leafstoneSwordItem = new GenericSword(LEAFSTONE, "leaf_stone_sword");
      thunderstoneHammerItem = new GenericHammer(THUNDERSTONE, "thunder_stone_hammer");
      thunderstoneAxeItem = new GenericAxe(THUNDERSTONE, "thunder_stone_axe");
      thunderstoneHoeItem = new GenericHoe(THUNDERSTONE, "thunder_stone_hoe");
      thunderstonePickaxeItem = new GenericPickaxe(THUNDERSTONE, "thunder_stone_pickaxe");
      thunderstoneShovelItem = new GenericShovel(THUNDERSTONE, "thunder_stone_shovel");
      thunderstoneSwordItem = new GenericSword(THUNDERSTONE, "thunder_stone_sword");
      sunstoneHammerItem = new GenericHammer(SUNSTONE, "sun_stone_hammer");
      sunstoneAxeItem = new GenericAxe(SUNSTONE, "sun_stone_axe");
      sunstoneHoeItem = new GenericHoe(SUNSTONE, "sun_stone_hoe");
      sunstonePickaxeItem = new GenericPickaxe(SUNSTONE, "sun_stone_pickaxe");
      sunstoneShovelItem = new GenericShovel(SUNSTONE, "sun_stone_shovel");
      sunstoneSwordItem = new GenericSword(SUNSTONE, "sun_stone_sword");
      moonstoneHammerItem = new GenericHammer(MOONSTONE, "moon_stone_hammer");
      moonstoneAxeItem = new GenericAxe(MOONSTONE, "moon_stone_axe");
      moonstoneHoeItem = new GenericHoe(MOONSTONE, "moon_stone_hoe");
      moonstonePickaxeItem = new GenericPickaxe(MOONSTONE, "moon_stone_pickaxe");
      moonstoneShovelItem = new GenericShovel(MOONSTONE, "moon_stone_shovel");
      moonstoneSwordItem = new GenericSword(MOONSTONE, "moon_stone_sword");
      dawnstoneHammerItem = new GenericHammer(DAWNSTONE, "dawn_stone_hammer");
      dawnstoneAxeItem = new GenericAxe(DAWNSTONE, "dawn_stone_axe");
      dawnstoneHoeItem = new GenericHoe(DAWNSTONE, "dawn_stone_hoe");
      dawnstonePickaxeItem = new GenericPickaxe(DAWNSTONE, "dawn_stone_pickaxe");
      dawnstoneShovelItem = new GenericShovel(DAWNSTONE, "dawn_stone_shovel");
      dawnstoneSwordItem = new GenericSword(DAWNSTONE, "dawn_stone_sword");
      duskstoneHammerItem = new GenericHammer(DUSKSTONE, "dusk_stone_hammer");
      duskstoneAxeItem = new GenericAxe(DUSKSTONE, "dusk_stone_axe");
      duskstoneHoeItem = new GenericHoe(DUSKSTONE, "dusk_stone_hoe");
      duskstonePickaxeItem = new GenericPickaxe(DUSKSTONE, "dusk_stone_pickaxe");
      duskstoneShovelItem = new GenericShovel(DUSKSTONE, "dusk_stone_shovel");
      duskstoneSwordItem = new GenericSword(DUSKSTONE, "dusk_stone_sword");
      helmetAluminium = new GenericArmor("aluminium_helmet", ALUMINIUMARMOR, EntityEquipmentSlot.HEAD);
      chestplateAluminium = new GenericArmor("aluminium_chestplate", ALUMINIUMARMOR, EntityEquipmentSlot.CHEST);
      leggingsAluminium = new GenericArmor("aluminium_leggings", ALUMINIUMARMOR, EntityEquipmentSlot.LEGS);
      bootsAluminium = new GenericArmor("aluminium_boots", ALUMINIUMARMOR, EntityEquipmentSlot.FEET);
      rubyHelm = new GenericArmor("ruby_helm", RUBYARMORMAT, EntityEquipmentSlot.HEAD);
      rubyPlate = new GenericArmor("ruby_plate", RUBYARMORMAT, EntityEquipmentSlot.CHEST);
      rubyLegs = new GenericArmor("ruby_legs", RUBYARMORMAT, EntityEquipmentSlot.LEGS);
      rubyBoots = new GenericArmor("ruby_boots", RUBYARMORMAT, EntityEquipmentSlot.FEET);
      sapphireHelm = new GenericArmor("sapphire_helm", SAPPHIREARMORMAT, EntityEquipmentSlot.HEAD);
      sapphirePlate = new GenericArmor("sapphire_plate", SAPPHIREARMORMAT, EntityEquipmentSlot.CHEST);
      sapphireLegs = new GenericArmor("sapphire_legs", SAPPHIREARMORMAT, EntityEquipmentSlot.LEGS);
      sapphireBoots = new GenericArmor("sapphire_boots", SAPPHIREARMORMAT, EntityEquipmentSlot.FEET);
      plasmaHelm = new GenericArmor("plasma_helm", PLASMAARMORMAT, EntityEquipmentSlot.HEAD);
      plasmaPlate = new GenericArmor("plasma_plate", PLASMAARMORMAT, EntityEquipmentSlot.CHEST);
      plasmaLegs = new GenericArmor("plasma_legs", PLASMAARMORMAT, EntityEquipmentSlot.LEGS);
      plasmaBoots = new GenericArmor("plasma_boots", PLASMAARMORMAT, EntityEquipmentSlot.FEET);
      galacticHelm = new GenericArmor("galactic_helm", GALACTICARMORMAT, EntityEquipmentSlot.HEAD);
      galacticPlate = new GenericArmor("galactic_plate", GALACTICARMORMAT, EntityEquipmentSlot.CHEST);
      galacticLegs = new GenericArmor("galactic_legs", GALACTICARMORMAT, EntityEquipmentSlot.LEGS);
      galacticBoots = new GenericArmor("galactic_boots", GALACTICARMORMAT, EntityEquipmentSlot.FEET);
      neoHelm = new GenericArmor("neo_plasma_helm", NEOARMORMAT, EntityEquipmentSlot.HEAD);
      neoPlate = new GenericArmor("neo_plasma_plate", NEOARMORMAT, EntityEquipmentSlot.CHEST);
      neoLegs = new GenericArmor("neo_plasma_legs", NEOARMORMAT, EntityEquipmentSlot.LEGS);
      neoBoots = new GenericArmor("neo_plasma_boots", NEOARMORMAT, EntityEquipmentSlot.FEET);
      rocketHelm = new GenericArmor("rocket_helm", ROCKETARMORMAT, EntityEquipmentSlot.HEAD);
      rocketPlate = new GenericArmor("rocket_plate", ROCKETARMORMAT, EntityEquipmentSlot.CHEST);
      rocketLegs = new GenericArmor("rocket_legs", ROCKETARMORMAT, EntityEquipmentSlot.LEGS);
      rocketBoots = new GenericArmor("rocket_boots", ROCKETARMORMAT, EntityEquipmentSlot.FEET);
      firestoneHelm = new GenericArmor("fire_stone_helm", FIRESTONEARMORMAT, EntityEquipmentSlot.HEAD);
      firestonePlate = new GenericArmor("fire_stone_plate", FIRESTONEARMORMAT, EntityEquipmentSlot.CHEST);
      firestoneLegs = new GenericArmor("fire_stone_legs", FIRESTONEARMORMAT, EntityEquipmentSlot.LEGS);
      firestoneBoots = (new GenericArmor("fire_stone_boots", FIRESTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new FirestoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      waterstoneHelm = new GenericArmor("water_stone_helm", WATERSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      waterstonePlate = new GenericArmor("water_stone_plate", WATERSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      waterstoneLegs = new GenericArmor("water_stone_legs", WATERSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      waterstoneBoots = (new GenericArmor("water_stone_boots", WATERSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new WaterstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      leafstoneHelm = new GenericArmor("leaf_stone_helm", LEAFSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      leafstonePlate = new GenericArmor("leaf_stone_plate", LEAFSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      leafstoneLegs = new GenericArmor("leaf_stone_legs", LEAFSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      leafstoneBoots = (new GenericArmor("leaf_stone_boots", LEAFSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new LeafstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      thunderstoneHelm = new GenericArmor("thunder_stone_helm", THUNDERSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      thunderstonePlate = new GenericArmor("thunder_stone_plate", THUNDERSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      thunderstoneLegs = new GenericArmor("thunder_stone_legs", THUNDERSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      thunderstoneBoots = (new GenericArmor("thunder_stone_boots", THUNDERSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new ThunderstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      sunstoneHelm = new GenericArmor("sun_stone_helm", SUNSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      sunstonePlate = new GenericArmor("sun_stone_plate", SUNSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      sunstoneLegs = new GenericArmor("sun_stone_legs", SUNSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      sunstoneBoots = (new GenericArmor("sun_stone_boots", SUNSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new SunstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      moonstoneHelm = new GenericArmor("moon_stone_helm", MOONSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      moonstonePlate = new GenericArmor("moon_stone_plate", MOONSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      moonstoneLegs = new GenericArmor("moon_stone_legs", MOONSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      moonstoneBoots = (new GenericArmor("moon_stone_boots", MOONSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new MoonstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      dawnstoneHelm = new GenericArmor("dawn_stone_helm", DAWNSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      dawnstonePlate = new GenericArmor("dawn_stone_plate", DAWNSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      dawnstoneLegs = new GenericArmor("dawn_stone_legs", DAWNSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      dawnstoneBoots = (new GenericArmor("dawn_stone_boots", DAWNSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new DawnstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      duskstoneHelm = new GenericArmor("dusk_stone_helm", DUSKSTONEARMORMAT, EntityEquipmentSlot.HEAD);
      duskstonePlate = new GenericArmor("dusk_stone_plate", DUSKSTONEARMORMAT, EntityEquipmentSlot.CHEST);
      duskstoneLegs = new GenericArmor("dusk_stone_legs", DUSKSTONEARMORMAT, EntityEquipmentSlot.LEGS);
      duskstoneBoots = (new GenericArmor("dusk_stone_boots", DUSKSTONEARMORMAT, EntityEquipmentSlot.FEET)).setEffect(new DuskstoneEffect()).setItemAttributeModifiers(new SpeedModifier(0.5F));
      ALUMINIUM.setRepairItem(new ItemStack(PixelmonItems.aluminiumIngot));
      RUBY.setRepairItem(new ItemStack(PixelmonItems.ruby));
      SAPPHIRE.setRepairItem(new ItemStack(PixelmonItems.sapphire));
      AMETHYST.setRepairItem(new ItemStack(PixelmonItems.amethyst));
      CRYSTAL.setRepairItem(new ItemStack(PixelmonItems.crystal));
      FIRESTONE.setRepairItem(new ItemStack(PixelmonItems.fireStone));
      WATERSTONE.setRepairItem(new ItemStack(PixelmonItems.waterStone));
      LEAFSTONE.setRepairItem(new ItemStack(PixelmonItems.leafStone));
      THUNDERSTONE.setRepairItem(new ItemStack(PixelmonItems.thunderStone));
      SUNSTONE.setRepairItem(new ItemStack(PixelmonItems.sunStone));
      MOONSTONE.setRepairItem(new ItemStack(PixelmonItems.moonStone));
      DAWNSTONE.setRepairItem(new ItemStack(PixelmonItems.dawnStone));
      DUSKSTONE.setRepairItem(new ItemStack(PixelmonItems.duskStone));
      ALUMINIUMARMOR.setRepairItem(new ItemStack(PixelmonItems.aluminiumIngot));
      OLDRUNNINGARMOR.setRepairItem(new ItemStack(Items.field_151116_aA));
      RUBYARMORMAT.setRepairItem(new ItemStack(PixelmonItems.ruby));
      SAPPHIREARMORMAT.setRepairItem(new ItemStack(PixelmonItems.sapphire));
      PLASMAARMORMAT.setRepairItem(new ItemStack(PixelmonItems.crystal));
      GALACTICARMORMAT.setRepairItem(new ItemStack(PixelmonItems.siliconItem));
      NEOARMORMAT.setRepairItem(new ItemStack(PixelmonItems.crystal));
      ROCKETARMORMAT.setRepairItem(new ItemStack(PixelmonItems.amethyst));
      FIRESTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.fireStone));
      WATERSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.waterStone));
      LEAFSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.leafStone));
      THUNDERSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.thunderStone));
      SUNSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.sunStone));
      MOONSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.moonStone));
      DAWNSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.dawnStone));
      DUSKSTONEARMORMAT.setRepairItem(new ItemStack(PixelmonItems.duskStone));
   }

   static {
      ALUMINIUMARMOR = EnumHelper.addArmorMaterial("ALUMINUM", "pixelmon:aluminum", 15, new int[]{2, 6, 5, 2}, 8, SoundEvents.field_187725_r, 0.0F);
      RUNNINGARMOR = EnumHelper.addArmorMaterial("RUNNING", "aluminum", 66, new int[]{3, 8, 6, 3}, 22, SoundEvents.field_187725_r, 0.0F);
      OLDRUNNINGARMOR = EnumHelper.addArmorMaterial("OLDRUNNING", "aluminum", 999999, new int[]{2, 6, 5, 1}, 13, SoundEvents.field_187725_r, 0.0F);
      RUBYARMORMAT = EnumHelper.addArmorMaterial("RUBY", "pixelmon:ruby", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      SAPPHIREARMORMAT = EnumHelper.addArmorMaterial("SAPPHIRE", "pixelmon:sapphire", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      PLASMAARMORMAT = EnumHelper.addArmorMaterial("CRYSTAL", "pixelmon:crystal", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      GALACTICARMORMAT = EnumHelper.addArmorMaterial("GALATIC", "pixelmon:galactic", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187713_n, 0.0F);
      NEOARMORMAT = EnumHelper.addArmorMaterial("NEO", "pixelmon:neoplasma", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187716_o, 0.0F);
      ROCKETARMORMAT = EnumHelper.addArmorMaterial("ROCKET", "pixelmon:rocket", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      FIRESTONEARMORMAT = EnumHelper.addArmorMaterial("FIRESTONE", "pixelmon:firestone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      WATERSTONEARMORMAT = EnumHelper.addArmorMaterial("WATERSTONE", "pixelmon:waterstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      LEAFSTONEARMORMAT = EnumHelper.addArmorMaterial("LEAFSTONE", "pixelmon:leafstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      THUNDERSTONEARMORMAT = EnumHelper.addArmorMaterial("THUNDERSTONE", "pixelmon:thunderstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      SUNSTONEARMORMAT = EnumHelper.addArmorMaterial("SUNSTONE", "pixelmon:sunstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      MOONSTONEARMORMAT = EnumHelper.addArmorMaterial("MOONSTONE", "pixelmon:moonstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      DAWNSTONEARMORMAT = EnumHelper.addArmorMaterial("DAWNSTONE", "pixelmon:dawnstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
      DUSKSTONEARMORMAT = EnumHelper.addArmorMaterial("DUSKSTONE", "pixelmon:duskstone", 200, new int[]{3, 7, 6, 3}, 10, SoundEvents.field_187725_r, 0.0F);
   }
}