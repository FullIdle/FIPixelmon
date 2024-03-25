package com.pixelmonmod.pixelmon.enums;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.MovesetSyncEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.forms.EnumAegislash;
import com.pixelmonmod.pixelmon.enums.forms.EnumAlcremie;
import com.pixelmonmod.pixelmon.enums.forms.EnumAmoonguss;
import com.pixelmonmod.pixelmon.enums.forms.EnumArbok;
import com.pixelmonmod.pixelmon.enums.forms.EnumArceus;
import com.pixelmonmod.pixelmon.enums.forms.EnumAuthentic;
import com.pixelmonmod.pixelmon.enums.forms.EnumBasculin;
import com.pixelmonmod.pixelmon.enums.forms.EnumBidoof;
import com.pixelmonmod.pixelmon.enums.forms.EnumBlastoise;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurmy;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurningSalt;
import com.pixelmonmod.pixelmon.enums.forms.EnumCalyrex;
import com.pixelmonmod.pixelmon.enums.forms.EnumCastform;
import com.pixelmonmod.pixelmon.enums.forms.EnumCharizard;
import com.pixelmonmod.pixelmon.enums.forms.EnumCherrim;
import com.pixelmonmod.pixelmon.enums.forms.EnumClobbopus;
import com.pixelmonmod.pixelmon.enums.forms.EnumCramorant;
import com.pixelmonmod.pixelmon.enums.forms.EnumDarmanitan;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumEiscue;
import com.pixelmonmod.pixelmon.enums.forms.EnumEternatus;
import com.pixelmonmod.pixelmon.enums.forms.EnumFeebas;
import com.pixelmonmod.pixelmon.enums.forms.EnumFlabebe;
import com.pixelmonmod.pixelmon.enums.forms.EnumGastrodon;
import com.pixelmonmod.pixelmon.enums.forms.EnumGenesect;
import com.pixelmonmod.pixelmon.enums.forms.EnumGengar;
import com.pixelmonmod.pixelmon.enums.forms.EnumGigantamax;
import com.pixelmonmod.pixelmon.enums.forms.EnumGiratina;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.EnumGroudon;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import com.pixelmonmod.pixelmon.enums.forms.EnumHoopa;
import com.pixelmonmod.pixelmon.enums.forms.EnumKeldeo;
import com.pixelmonmod.pixelmon.enums.forms.EnumKyurem;
import com.pixelmonmod.pixelmon.enums.forms.EnumLunatone;
import com.pixelmonmod.pixelmon.enums.forms.EnumLycanroc;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeloetta;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeowth;
import com.pixelmonmod.pixelmon.enums.forms.EnumMimikyu;
import com.pixelmonmod.pixelmon.enums.forms.EnumMinior;
import com.pixelmonmod.pixelmon.enums.forms.EnumMissingNo;
import com.pixelmonmod.pixelmon.enums.forms.EnumMorpeko;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.EnumOricorio;
import com.pixelmonmod.pixelmon.enums.forms.EnumOrigin;
import com.pixelmonmod.pixelmon.enums.forms.EnumPichu;
import com.pixelmonmod.pixelmon.enums.forms.EnumPikachu;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.EnumRotom;
import com.pixelmonmod.pixelmon.enums.forms.EnumSandile;
import com.pixelmonmod.pixelmon.enums.forms.EnumShaymin;
import com.pixelmonmod.pixelmon.enums.forms.EnumShearable;
import com.pixelmonmod.pixelmon.enums.forms.EnumShellos;
import com.pixelmonmod.pixelmon.enums.forms.EnumSilvally;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowbro;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowking;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowpoke;
import com.pixelmonmod.pixelmon.enums.forms.EnumSnorlax;
import com.pixelmonmod.pixelmon.enums.forms.EnumSolgaleo;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpheal;
import com.pixelmonmod.pixelmon.enums.forms.EnumTherian;
import com.pixelmonmod.pixelmon.enums.forms.EnumToxtricity;
import com.pixelmonmod.pixelmon.enums.forms.EnumUnown;
import com.pixelmonmod.pixelmon.enums.forms.EnumUrshifu;
import com.pixelmonmod.pixelmon.enums.forms.EnumVenusaur;
import com.pixelmonmod.pixelmon.enums.forms.EnumVivillon;
import com.pixelmonmod.pixelmon.enums.forms.EnumWishiwashi;
import com.pixelmonmod.pixelmon.enums.forms.EnumWormadam;
import com.pixelmonmod.pixelmon.enums.forms.EnumXerneas;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.enums.forms.FormAttributes;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import com.pixelmonmod.pixelmon.enums.forms.SeasonForm;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.util.text.translation.I18n;

public enum EnumSpecies implements ITranslatable {
   MissingNo(0, "MissingNo"),
   Bulbasaur(1, "Bulbasaur"),
   Ivysaur(2, "Ivysaur"),
   Venusaur(3, "Venusaur"),
   Charmander(4, "Charmander"),
   Charmeleon(5, "Charmeleon"),
   Charizard(6, "Charizard"),
   Squirtle(7, "Squirtle"),
   Wartortle(8, "Wartortle"),
   Blastoise(9, "Blastoise"),
   Caterpie(10, "Caterpie"),
   Metapod(11, "Metapod"),
   Butterfree(12, "Butterfree"),
   Weedle(13, "Weedle"),
   Kakuna(14, "Kakuna"),
   Beedrill(15, "Beedrill"),
   Pidgey(16, "Pidgey"),
   Pidgeotto(17, "Pidgeotto"),
   Pidgeot(18, "Pidgeot"),
   Rattata(19, "Rattata"),
   Raticate(20, "Raticate"),
   Spearow(21, "Spearow"),
   Fearow(22, "Fearow"),
   Ekans(23, "Ekans"),
   Arbok(24, "Arbok"),
   Pikachu(25, "Pikachu"),
   Raichu(26, "Raichu"),
   Sandshrew(27, "Sandshrew"),
   Sandslash(28, "Sandslash"),
   Nidoranfemale(29, "Nidoranfemale"),
   Nidorina(30, "Nidorina"),
   Nidoqueen(31, "Nidoqueen"),
   Nidoranmale(32, "Nidoranmale"),
   Nidorino(33, "Nidorino"),
   Nidoking(34, "Nidoking"),
   Clefairy(35, "Clefairy"),
   Clefable(36, "Clefable"),
   Vulpix(37, "Vulpix"),
   Ninetales(38, "Ninetales"),
   Jigglypuff(39, "Jigglypuff"),
   Wigglytuff(40, "Wigglytuff"),
   Zubat(41, "Zubat"),
   Golbat(42, "Golbat"),
   Oddish(43, "Oddish"),
   Gloom(44, "Gloom"),
   Vileplume(45, "Vileplume"),
   Paras(46, "Paras"),
   Parasect(47, "Parasect"),
   Venonat(48, "Venonat"),
   Venomoth(49, "Venomoth"),
   Diglett(50, "Diglett"),
   Dugtrio(51, "Dugtrio"),
   Meowth(52, "Meowth"),
   Persian(53, "Persian"),
   Psyduck(54, "Psyduck"),
   Golduck(55, "Golduck"),
   Mankey(56, "Mankey"),
   Primeape(57, "Primeape"),
   Growlithe(58, "Growlithe"),
   Arcanine(59, "Arcanine"),
   Poliwag(60, "Poliwag"),
   Poliwhirl(61, "Poliwhirl"),
   Poliwrath(62, "Poliwrath"),
   Abra(63, "Abra"),
   Kadabra(64, "Kadabra"),
   Alakazam(65, "Alakazam"),
   Machop(66, "Machop"),
   Machoke(67, "Machoke"),
   Machamp(68, "Machamp"),
   Bellsprout(69, "Bellsprout"),
   Weepinbell(70, "Weepinbell"),
   Victreebel(71, "Victreebel"),
   Tentacool(72, "Tentacool"),
   Tentacruel(73, "Tentacruel"),
   Geodude(74, "Geodude"),
   Graveler(75, "Graveler"),
   Golem(76, "Golem"),
   Ponyta(77, "Ponyta"),
   Rapidash(78, "Rapidash"),
   Slowpoke(79, "Slowpoke"),
   Slowbro(80, "Slowbro"),
   Magnemite(81, "Magnemite"),
   Magneton(82, "Magneton"),
   Farfetchd(83, "Farfetchd"),
   Doduo(84, "Doduo"),
   Dodrio(85, "Dodrio"),
   Seel(86, "Seel"),
   Dewgong(87, "Dewgong"),
   Grimer(88, "Grimer"),
   Muk(89, "Muk"),
   Shellder(90, "Shellder"),
   Cloyster(91, "Cloyster"),
   Gastly(92, "Gastly"),
   Haunter(93, "Haunter"),
   Gengar(94, "Gengar"),
   Onix(95, "Onix"),
   Drowzee(96, "Drowzee"),
   Hypno(97, "Hypno"),
   Krabby(98, "Krabby"),
   Kingler(99, "Kingler"),
   Voltorb(100, "Voltorb"),
   Electrode(101, "Electrode"),
   Exeggcute(102, "Exeggcute"),
   Exeggutor(103, "Exeggutor"),
   Cubone(104, "Cubone"),
   Marowak(105, "Marowak"),
   Hitmonlee(106, "Hitmonlee"),
   Hitmonchan(107, "Hitmonchan"),
   Lickitung(108, "Lickitung"),
   Koffing(109, "Koffing"),
   Weezing(110, "Weezing"),
   Rhyhorn(111, "Rhyhorn"),
   Rhydon(112, "Rhydon"),
   Chansey(113, "Chansey"),
   Tangela(114, "Tangela"),
   Kangaskhan(115, "Kangaskhan"),
   Horsea(116, "Horsea"),
   Seadra(117, "Seadra"),
   Goldeen(118, "Goldeen"),
   Seaking(119, "Seaking"),
   Staryu(120, "Staryu"),
   Starmie(121, "Starmie"),
   MrMime(122, "MrMime"),
   Scyther(123, "Scyther"),
   Jynx(124, "Jynx"),
   Electabuzz(125, "Electabuzz"),
   Magmar(126, "Magmar"),
   Pinsir(127, "Pinsir"),
   Tauros(128, "Tauros"),
   Magikarp(129, "Magikarp"),
   Gyarados(130, "Gyarados"),
   Lapras(131, "Lapras"),
   Ditto(132, "Ditto"),
   Eevee(133, "Eevee"),
   Vaporeon(134, "Vaporeon"),
   Jolteon(135, "Jolteon"),
   Flareon(136, "Flareon"),
   Porygon(137, "Porygon"),
   Omanyte(138, "Omanyte"),
   Omastar(139, "Omastar"),
   Kabuto(140, "Kabuto"),
   Kabutops(141, "Kabutops"),
   Aerodactyl(142, "Aerodactyl"),
   Snorlax(143, "Snorlax"),
   Articuno(144, "Articuno"),
   Zapdos(145, "Zapdos"),
   Moltres(146, "Moltres"),
   Dratini(147, "Dratini"),
   Dragonair(148, "Dragonair"),
   Dragonite(149, "Dragonite"),
   Mewtwo(150, "Mewtwo"),
   Mew(151, "Mew"),
   Chikorita(152, "Chikorita"),
   Bayleef(153, "Bayleef"),
   Meganium(154, "Meganium"),
   Cyndaquil(155, "Cyndaquil"),
   Quilava(156, "Quilava"),
   Typhlosion(157, "Typhlosion"),
   Totodile(158, "Totodile"),
   Croconaw(159, "Croconaw"),
   Feraligatr(160, "Feraligatr"),
   Sentret(161, "Sentret"),
   Furret(162, "Furret"),
   Hoothoot(163, "Hoothoot"),
   Noctowl(164, "Noctowl"),
   Ledyba(165, "Ledyba"),
   Ledian(166, "Ledian"),
   Spinarak(167, "Spinarak"),
   Ariados(168, "Ariados"),
   Crobat(169, "Crobat"),
   Chinchou(170, "Chinchou"),
   Lanturn(171, "Lanturn"),
   Pichu(172, "Pichu"),
   Cleffa(173, "Cleffa"),
   Igglybuff(174, "Igglybuff"),
   Togepi(175, "Togepi"),
   Togetic(176, "Togetic"),
   Natu(177, "Natu"),
   Xatu(178, "Xatu"),
   Mareep(179, "Mareep"),
   Flaaffy(180, "Flaaffy"),
   Ampharos(181, "Ampharos"),
   Bellossom(182, "Bellossom"),
   Marill(183, "Marill"),
   Azumarill(184, "Azumarill"),
   Sudowoodo(185, "Sudowoodo"),
   Politoed(186, "Politoed"),
   Hoppip(187, "Hoppip"),
   Skiploom(188, "Skiploom"),
   Jumpluff(189, "Jumpluff"),
   Aipom(190, "Aipom"),
   Sunkern(191, "Sunkern"),
   Sunflora(192, "Sunflora"),
   Yanma(193, "Yanma"),
   Wooper(194, "Wooper"),
   Quagsire(195, "Quagsire"),
   Espeon(196, "Espeon"),
   Umbreon(197, "Umbreon"),
   Murkrow(198, "Murkrow"),
   Slowking(199, "Slowking"),
   Misdreavus(200, "Misdreavus"),
   Unown(201, "Unown"),
   Wobbuffet(202, "Wobbuffet"),
   Girafarig(203, "Girafarig"),
   Pineco(204, "Pineco"),
   Forretress(205, "Forretress"),
   Dunsparce(206, "Dunsparce"),
   Gligar(207, "Gligar"),
   Steelix(208, "Steelix"),
   Snubbull(209, "Snubbull"),
   Granbull(210, "Granbull"),
   Qwilfish(211, "Qwilfish"),
   Scizor(212, "Scizor"),
   Shuckle(213, "Shuckle"),
   Heracross(214, "Heracross"),
   Sneasel(215, "Sneasel"),
   Teddiursa(216, "Teddiursa"),
   Ursaring(217, "Ursaring"),
   Slugma(218, "Slugma"),
   Magcargo(219, "Magcargo"),
   Swinub(220, "Swinub"),
   Piloswine(221, "Piloswine"),
   Corsola(222, "Corsola"),
   Remoraid(223, "Remoraid"),
   Octillery(224, "Octillery"),
   Delibird(225, "Delibird"),
   Mantine(226, "Mantine"),
   Skarmory(227, "Skarmory"),
   Houndour(228, "Houndour"),
   Houndoom(229, "Houndoom"),
   Kingdra(230, "Kingdra"),
   Phanpy(231, "Phanpy"),
   Donphan(232, "Donphan"),
   Porygon2(233, "Porygon2"),
   Stantler(234, "Stantler"),
   Smeargle(235, "Smeargle"),
   Tyrogue(236, "Tyrogue"),
   Hitmontop(237, "Hitmontop"),
   Smoochum(238, "Smoochum"),
   Elekid(239, "Elekid"),
   Magby(240, "Magby"),
   Miltank(241, "Miltank"),
   Blissey(242, "Blissey"),
   Raikou(243, "Raikou"),
   Entei(244, "Entei"),
   Suicune(245, "Suicune"),
   Larvitar(246, "Larvitar"),
   Pupitar(247, "Pupitar"),
   Tyranitar(248, "Tyranitar"),
   Lugia(249, "Lugia"),
   Hooh(250, "Ho-Oh"),
   Celebi(251, "Celebi"),
   Treecko(252, "Treecko"),
   Grovyle(253, "Grovyle"),
   Sceptile(254, "Sceptile"),
   Torchic(255, "Torchic"),
   Combusken(256, "Combusken"),
   Blaziken(257, "Blaziken"),
   Mudkip(258, "Mudkip"),
   Marshtomp(259, "Marshtomp"),
   Swampert(260, "Swampert"),
   Poochyena(261, "Poochyena"),
   Mightyena(262, "Mightyena"),
   Zigzagoon(263, "Zigzagoon"),
   Linoone(264, "Linoone"),
   Wurmple(265, "Wurmple"),
   Silcoon(266, "Silcoon"),
   Beautifly(267, "Beautifly"),
   Cascoon(268, "Cascoon"),
   Dustox(269, "Dustox"),
   Lotad(270, "Lotad"),
   Lombre(271, "Lombre"),
   Ludicolo(272, "Ludicolo"),
   Seedot(273, "Seedot"),
   Nuzleaf(274, "Nuzleaf"),
   Shiftry(275, "Shiftry"),
   Taillow(276, "Taillow"),
   Swellow(277, "Swellow"),
   Wingull(278, "Wingull"),
   Pelipper(279, "Pelipper"),
   Ralts(280, "Ralts"),
   Kirlia(281, "Kirlia"),
   Gardevoir(282, "Gardevoir"),
   Surskit(283, "Surskit"),
   Masquerain(284, "Masquerain"),
   Shroomish(285, "Shroomish"),
   Breloom(286, "Breloom"),
   Slakoth(287, "Slakoth"),
   Vigoroth(288, "Vigoroth"),
   Slaking(289, "Slaking"),
   Nincada(290, "Nincada"),
   Ninjask(291, "Ninjask"),
   Shedinja(292, "Shedinja"),
   Whismur(293, "Whismur"),
   Loudred(294, "Loudred"),
   Exploud(295, "Exploud"),
   Makuhita(296, "Makuhita"),
   Hariyama(297, "Hariyama"),
   Azurill(298, "Azurill"),
   Nosepass(299, "Nosepass"),
   Skitty(300, "Skitty"),
   Delcatty(301, "Delcatty"),
   Sableye(302, "Sableye"),
   Mawile(303, "Mawile"),
   Aron(304, "Aron"),
   Lairon(305, "Lairon"),
   Aggron(306, "Aggron"),
   Meditite(307, "Meditite"),
   Medicham(308, "Medicham"),
   Electrike(309, "Electrike"),
   Manectric(310, "Manectric"),
   Plusle(311, "Plusle"),
   Minun(312, "Minun"),
   Volbeat(313, "Volbeat"),
   Illumise(314, "Illumise"),
   Roselia(315, "Roselia"),
   Gulpin(316, "Gulpin"),
   Swalot(317, "Swalot"),
   Carvanha(318, "Carvanha"),
   Sharpedo(319, "Sharpedo"),
   Wailmer(320, "Wailmer"),
   Wailord(321, "Wailord"),
   Numel(322, "Numel"),
   Camerupt(323, "Camerupt"),
   Torkoal(324, "Torkoal"),
   Spoink(325, "Spoink"),
   Grumpig(326, "Grumpig"),
   Spinda(327, "Spinda"),
   Trapinch(328, "Trapinch"),
   Vibrava(329, "Vibrava"),
   Flygon(330, "Flygon"),
   Cacnea(331, "Cacnea"),
   Cacturne(332, "Cacturne"),
   Swablu(333, "Swablu"),
   Altaria(334, "Altaria"),
   Zangoose(335, "Zangoose"),
   Seviper(336, "Seviper"),
   Lunatone(337, "Lunatone"),
   Solrock(338, "Solrock"),
   Barboach(339, "Barboach"),
   Whiscash(340, "Whiscash"),
   Corphish(341, "Corphish"),
   Crawdaunt(342, "Crawdaunt"),
   Baltoy(343, "Baltoy"),
   Claydol(344, "Claydol"),
   Lileep(345, "Lileep"),
   Cradily(346, "Cradily"),
   Anorith(347, "Anorith"),
   Armaldo(348, "Armaldo"),
   Feebas(349, "Feebas"),
   Milotic(350, "Milotic"),
   Castform(351, "Castform"),
   Kecleon(352, "Kecleon"),
   Shuppet(353, "Shuppet"),
   Banette(354, "Banette"),
   Duskull(355, "Duskull"),
   Dusclops(356, "Dusclops"),
   Tropius(357, "Tropius"),
   Chimecho(358, "Chimecho"),
   Absol(359, "Absol"),
   Wynaut(360, "Wynaut"),
   Snorunt(361, "Snorunt"),
   Glalie(362, "Glalie"),
   Spheal(363, "Spheal"),
   Sealeo(364, "Sealeo"),
   Walrein(365, "Walrein"),
   Clamperl(366, "Clamperl"),
   Huntail(367, "Huntail"),
   Gorebyss(368, "Gorebyss"),
   Relicanth(369, "Relicanth"),
   Luvdisc(370, "Luvdisc"),
   Bagon(371, "Bagon"),
   Shelgon(372, "Shelgon"),
   Salamence(373, "Salamence"),
   Beldum(374, "Beldum"),
   Metang(375, "Metang"),
   Metagross(376, "Metagross"),
   Regirock(377, "Regirock"),
   Regice(378, "Regice"),
   Registeel(379, "Registeel"),
   Latias(380, "Latias"),
   Latios(381, "Latios"),
   Kyogre(382, "Kyogre"),
   Groudon(383, "Groudon"),
   Rayquaza(384, "Rayquaza"),
   Jirachi(385, "Jirachi"),
   Deoxys(386, "Deoxys"),
   Turtwig(387, "Turtwig"),
   Grotle(388, "Grotle"),
   Torterra(389, "Torterra"),
   Chimchar(390, "Chimchar"),
   Monferno(391, "Monferno"),
   Infernape(392, "Infernape"),
   Piplup(393, "Piplup"),
   Prinplup(394, "Prinplup"),
   Empoleon(395, "Empoleon"),
   Starly(396, "Starly"),
   Staravia(397, "Staravia"),
   Staraptor(398, "Staraptor"),
   Bidoof(399, "Bidoof"),
   Bibarel(400, "Bibarel"),
   Kricketot(401, "Kricketot"),
   Kricketune(402, "Kricketune"),
   Shinx(403, "Shinx"),
   Luxio(404, "Luxio"),
   Luxray(405, "Luxray"),
   Budew(406, "Budew"),
   Roserade(407, "Roserade"),
   Cranidos(408, "Cranidos"),
   Rampardos(409, "Rampardos"),
   Shieldon(410, "Shieldon"),
   Bastiodon(411, "Bastiodon"),
   Burmy(412, "Burmy"),
   Wormadam(413, "Wormadam"),
   Mothim(414, "Mothim"),
   Combee(415, "Combee"),
   Vespiquen(416, "Vespiquen"),
   Pachirisu(417, "Pachirisu"),
   Buizel(418, "Buizel"),
   Floatzel(419, "Floatzel"),
   Cherubi(420, "Cherubi"),
   Cherrim(421, "Cherrim"),
   Shellos(422, "Shellos"),
   Gastrodon(423, "Gastrodon"),
   Ambipom(424, "Ambipom"),
   Drifloon(425, "Drifloon"),
   Drifblim(426, "Drifblim"),
   Buneary(427, "Buneary"),
   Lopunny(428, "Lopunny"),
   Mismagius(429, "Mismagius"),
   Honchkrow(430, "Honchkrow"),
   Glameow(431, "Glameow"),
   Purugly(432, "Purugly"),
   Chingling(433, "Chingling"),
   Stunky(434, "Stunky"),
   Skuntank(435, "Skuntank"),
   Bronzor(436, "Bronzor"),
   Bronzong(437, "Bronzong"),
   Bonsly(438, "Bonsly"),
   MimeJr(439, "MimeJr"),
   Happiny(440, "Happiny"),
   Chatot(441, "Chatot"),
   Spiritomb(442, "Spiritomb"),
   Gible(443, "Gible"),
   Gabite(444, "Gabite"),
   Garchomp(445, "Garchomp"),
   Munchlax(446, "Munchlax"),
   Riolu(447, "Riolu"),
   Lucario(448, "Lucario"),
   Hippopotas(449, "Hippopotas"),
   Hippowdon(450, "Hippowdon"),
   Skorupi(451, "Skorupi"),
   Drapion(452, "Drapion"),
   Croagunk(453, "Croagunk"),
   Toxicroak(454, "Toxicroak"),
   Carnivine(455, "Carnivine"),
   Finneon(456, "Finneon"),
   Lumineon(457, "Lumineon"),
   Mantyke(458, "Mantyke"),
   Snover(459, "Snover"),
   Abomasnow(460, "Abomasnow"),
   Weavile(461, "Weavile"),
   Magnezone(462, "Magnezone"),
   Lickilicky(463, "Lickilicky"),
   Rhyperior(464, "Rhyperior"),
   Tangrowth(465, "Tangrowth"),
   Electivire(466, "Electivire"),
   Magmortar(467, "Magmortar"),
   Togekiss(468, "Togekiss"),
   Yanmega(469, "Yanmega"),
   Leafeon(470, "Leafeon"),
   Glaceon(471, "Glaceon"),
   Gliscor(472, "Gliscor"),
   Mamoswine(473, "Mamoswine"),
   PorygonZ(474, "Porygon-Z"),
   Gallade(475, "Gallade"),
   Probopass(476, "Probopass"),
   Dusknoir(477, "Dusknoir"),
   Froslass(478, "Froslass"),
   Rotom(479, "Rotom"),
   Uxie(480, "Uxie"),
   Mesprit(481, "Mesprit"),
   Azelf(482, "Azelf"),
   Dialga(483, "Dialga"),
   Palkia(484, "Palkia"),
   Heatran(485, "Heatran"),
   Regigigas(486, "Regigigas"),
   Giratina(487, "Giratina"),
   Cresselia(488, "Cresselia"),
   Phione(489, "Phione"),
   Manaphy(490, "Manaphy"),
   Darkrai(491, "Darkrai"),
   Shaymin(492, "Shaymin"),
   Arceus(493, "Arceus"),
   Victini(494, "Victini"),
   Snivy(495, "Snivy"),
   Servine(496, "Servine"),
   Serperior(497, "Serperior"),
   Tepig(498, "Tepig"),
   Pignite(499, "Pignite"),
   Emboar(500, "Emboar"),
   Oshawott(501, "Oshawott"),
   Dewott(502, "Dewott"),
   Samurott(503, "Samurott"),
   Patrat(504, "Patrat"),
   Watchog(505, "Watchog"),
   Lillipup(506, "Lillipup"),
   Herdier(507, "Herdier"),
   Stoutland(508, "Stoutland"),
   Purrloin(509, "Purrloin"),
   Liepard(510, "Liepard"),
   Pansage(511, "Pansage"),
   Simisage(512, "Simisage"),
   Pansear(513, "Pansear"),
   Simisear(514, "Simisear"),
   Panpour(515, "Panpour"),
   Simipour(516, "Simipour"),
   Munna(517, "Munna"),
   Musharna(518, "Musharna"),
   Pidove(519, "Pidove"),
   Tranquill(520, "Tranquill"),
   Unfezant(521, "Unfezant"),
   Blitzle(522, "Blitzle"),
   Zebstrika(523, "Zebstrika"),
   Roggenrola(524, "Roggenrola"),
   Boldore(525, "Boldore"),
   Gigalith(526, "Gigalith"),
   Woobat(527, "Woobat"),
   Swoobat(528, "Swoobat"),
   Drilbur(529, "Drilbur"),
   Excadrill(530, "Excadrill"),
   Audino(531, "Audino"),
   Timburr(532, "Timburr"),
   Gurdurr(533, "Gurdurr"),
   Conkeldurr(534, "Conkeldurr"),
   Tympole(535, "Tympole"),
   Palpitoad(536, "Palpitoad"),
   Seismitoad(537, "Seismitoad"),
   Throh(538, "Throh"),
   Sawk(539, "Sawk"),
   Sewaddle(540, "Sewaddle"),
   Swadloon(541, "Swadloon"),
   Leavanny(542, "Leavanny"),
   Venipede(543, "Venipede"),
   Whirlipede(544, "Whirlipede"),
   Scolipede(545, "Scolipede"),
   Cottonee(546, "Cottonee"),
   Whimsicott(547, "Whimsicott"),
   Petilil(548, "Petilil"),
   Lilligant(549, "Lilligant"),
   Basculin(550, "Basculin"),
   Sandile(551, "Sandile"),
   Krokorok(552, "Krokorok"),
   Krookodile(553, "Krookodile"),
   Darumaka(554, "Darumaka"),
   Darmanitan(555, "Darmanitan"),
   Maractus(556, "Maractus"),
   Dwebble(557, "Dwebble"),
   Crustle(558, "Crustle"),
   Scraggy(559, "Scraggy"),
   Scrafty(560, "Scrafty"),
   Sigilyph(561, "Sigilyph"),
   Yamask(562, "Yamask"),
   Cofagrigus(563, "Cofagrigus"),
   Tirtouga(564, "Tirtouga"),
   Carracosta(565, "Carracosta"),
   Archen(566, "Archen"),
   Archeops(567, "Archeops"),
   Trubbish(568, "Trubbish"),
   Garbodor(569, "Garbodor"),
   Zorua(570, "Zorua"),
   Zoroark(571, "Zoroark"),
   Minccino(572, "Minccino"),
   Cinccino(573, "Cinccino"),
   Gothita(574, "Gothita"),
   Gothorita(575, "Gothorita"),
   Gothitelle(576, "Gothitelle"),
   Solosis(577, "Solosis"),
   Duosion(578, "Duosion"),
   Reuniclus(579, "Reuniclus"),
   Ducklett(580, "Ducklett"),
   Swanna(581, "Swanna"),
   Vanillite(582, "Vanillite"),
   Vanillish(583, "Vanillish"),
   Vanilluxe(584, "Vanilluxe"),
   Deerling(585, "Deerling"),
   Sawsbuck(586, "Sawsbuck"),
   Emolga(587, "Emolga"),
   Karrablast(588, "Karrablast"),
   Escavalier(589, "Escavalier"),
   Foongus(590, "Foongus"),
   Amoonguss(591, "Amoonguss"),
   Frillish(592, "Frillish"),
   Jellicent(593, "Jellicent"),
   Alomomola(594, "Alomomola"),
   Joltik(595, "Joltik"),
   Galvantula(596, "Galvantula"),
   Ferroseed(597, "Ferroseed"),
   Ferrothorn(598, "Ferrothorn"),
   Klink(599, "Klink"),
   Klang(600, "Klang"),
   Klinklang(601, "Klinklang"),
   Tynamo(602, "Tynamo"),
   Eelektrik(603, "Eelektrik"),
   Eelektross(604, "Eelektross"),
   Elgyem(605, "Elgyem"),
   Beheeyem(606, "Beheeyem"),
   Litwick(607, "Litwick"),
   Lampent(608, "Lampent"),
   Chandelure(609, "Chandelure"),
   Axew(610, "Axew"),
   Fraxure(611, "Fraxure"),
   Haxorus(612, "Haxorus"),
   Cubchoo(613, "Cubchoo"),
   Beartic(614, "Beartic"),
   Cryogonal(615, "Cryogonal"),
   Shelmet(616, "Shelmet"),
   Accelgor(617, "Accelgor"),
   Stunfisk(618, "Stunfisk"),
   Mienfoo(619, "Mienfoo"),
   Mienshao(620, "Mienshao"),
   Druddigon(621, "Druddigon"),
   Golett(622, "Golett"),
   Golurk(623, "Golurk"),
   Pawniard(624, "Pawniard"),
   Bisharp(625, "Bisharp"),
   Bouffalant(626, "Bouffalant"),
   Rufflet(627, "Rufflet"),
   Braviary(628, "Braviary"),
   Vullaby(629, "Vullaby"),
   Mandibuzz(630, "Mandibuzz"),
   Heatmor(631, "Heatmor"),
   Durant(632, "Durant"),
   Deino(633, "Deino"),
   Zweilous(634, "Zweilous"),
   Hydreigon(635, "Hydreigon"),
   Larvesta(636, "Larvesta"),
   Volcarona(637, "Volcarona"),
   Cobalion(638, "Cobalion"),
   Terrakion(639, "Terrakion"),
   Virizion(640, "Virizion"),
   Tornadus(641, "Tornadus"),
   Thundurus(642, "Thundurus"),
   Reshiram(643, "Reshiram"),
   Zekrom(644, "Zekrom"),
   Landorus(645, "Landorus"),
   Kyurem(646, "Kyurem"),
   Keldeo(647, "Keldeo"),
   Meloetta(648, "Meloetta"),
   Genesect(649, "Genesect"),
   Chespin(650, "Chespin"),
   Quilladin(651, "Quilladin"),
   Chesnaught(652, "Chesnaught"),
   Fennekin(653, "Fennekin"),
   Braixen(654, "Braixen"),
   Delphox(655, "Delphox"),
   Froakie(656, "Froakie"),
   Frogadier(657, "Frogadier"),
   Greninja(658, "Greninja"),
   Bunnelby(659, "Bunnelby"),
   Diggersby(660, "Diggersby"),
   Fletchling(661, "Fletchling"),
   Fletchinder(662, "Fletchinder"),
   Talonflame(663, "Talonflame"),
   Scatterbug(664, "Scatterbug"),
   Spewpa(665, "Spewpa"),
   Vivillon(666, "Vivillon"),
   Litleo(667, "Litleo"),
   Pyroar(668, "Pyroar"),
   Flabebe(669, "Flabebe"),
   Floette(670, "Floette"),
   Florges(671, "Florges"),
   Skiddo(672, "Skiddo"),
   Gogoat(673, "Gogoat"),
   Pancham(674, "Pancham"),
   Pangoro(675, "Pangoro"),
   Furfrou(676, "Furfrou"),
   Espurr(677, "Espurr"),
   Meowstic(678, "Meowstic"),
   Honedge(679, "Honedge"),
   Doublade(680, "Doublade"),
   Aegislash(681, "Aegislash"),
   Spritzee(682, "Spritzee"),
   Aromatisse(683, "Aromatisse"),
   Swirlix(684, "Swirlix"),
   Slurpuff(685, "Slurpuff"),
   Inkay(686, "Inkay"),
   Malamar(687, "Malamar"),
   Binacle(688, "Binacle"),
   Barbaracle(689, "Barbaracle"),
   Skrelp(690, "Skrelp"),
   Dragalge(691, "Dragalge"),
   Clauncher(692, "Clauncher"),
   Clawitzer(693, "Clawitzer"),
   Helioptile(694, "Helioptile"),
   Heliolisk(695, "Heliolisk"),
   Tyrunt(696, "Tyrunt"),
   Tyrantrum(697, "Tyrantrum"),
   Amaura(698, "Amaura"),
   Aurorus(699, "Aurorus"),
   Sylveon(700, "Sylveon"),
   Hawlucha(701, "Hawlucha"),
   Dedenne(702, "Dedenne"),
   Carbink(703, "Carbink"),
   Goomy(704, "Goomy"),
   Sliggoo(705, "Sliggoo"),
   Goodra(706, "Goodra"),
   Klefki(707, "Klefki"),
   Phantump(708, "Phantump"),
   Trevenant(709, "Trevenant"),
   Pumpkaboo(710, "Pumpkaboo"),
   Gourgeist(711, "Gourgeist"),
   Bergmite(712, "Bergmite"),
   Avalugg(713, "Avalugg"),
   Noibat(714, "Noibat"),
   Noivern(715, "Noivern"),
   Xerneas(716, "Xerneas"),
   Yveltal(717, "Yveltal"),
   Zygarde(718, "Zygarde"),
   Diancie(719, "Diancie"),
   Hoopa(720, "Hoopa"),
   Volcanion(721, "Volcanion"),
   Rowlet(722, "Rowlet"),
   Dartrix(723, "Dartrix"),
   Decidueye(724, "Decidueye"),
   Litten(725, "Litten"),
   Torracat(726, "Torracat"),
   Incineroar(727, "Incineroar"),
   Popplio(728, "Popplio"),
   Brionne(729, "Brionne"),
   Primarina(730, "Primarina"),
   Pikipek(731, "Pikipek"),
   Trumbeak(732, "Trumbeak"),
   Toucannon(733, "Toucannon"),
   Yungoos(734, "Yungoos"),
   Gumshoos(735, "Gumshoos"),
   Grubbin(736, "Grubbin"),
   Charjabug(737, "Charjabug"),
   Vikavolt(738, "Vikavolt"),
   Crabrawler(739, "Crabrawler"),
   Crabominable(740, "Crabominable"),
   Oricorio(741, "Oricorio"),
   Cutiefly(742, "Cutiefly"),
   Ribombee(743, "Ribombee"),
   Rockruff(744, "Rockruff"),
   Lycanroc(745, "Lycanroc"),
   Wishiwashi(746, "Wishiwashi"),
   Mareanie(747, "Mareanie"),
   Toxapex(748, "Toxapex"),
   Mudbray(749, "Mudbray"),
   Mudsdale(750, "Mudsdale"),
   Dewpider(751, "Dewpider"),
   Araquanid(752, "Araquanid"),
   Fomantis(753, "Fomantis"),
   Lurantis(754, "Lurantis"),
   Morelull(755, "Morelull"),
   Shiinotic(756, "Shiinotic"),
   Salandit(757, "Salandit"),
   Salazzle(758, "Salazzle"),
   Stufful(759, "Stufful"),
   Bewear(760, "Bewear"),
   Bounsweet(761, "Bounsweet"),
   Steenee(762, "Steenee"),
   Tsareena(763, "Tsareena"),
   Comfey(764, "Comfey"),
   Oranguru(765, "Oranguru"),
   Passimian(766, "Passimian"),
   Wimpod(767, "Wimpod"),
   Golisopod(768, "Golisopod"),
   Sandygast(769, "Sandygast"),
   Palossand(770, "Palossand"),
   Pyukumuku(771, "Pyukumuku"),
   TypeNull(772, "TypeNull"),
   Silvally(773, "Silvally"),
   Minior(774, "Minior"),
   Komala(775, "Komala"),
   Turtonator(776, "Turtonator"),
   Togedemaru(777, "Togedemaru"),
   Mimikyu(778, "Mimikyu"),
   Bruxish(779, "Bruxish"),
   Drampa(780, "Drampa"),
   Dhelmise(781, "Dhelmise"),
   Jangmoo(782, "Jangmo-o"),
   Hakamoo(783, "Hakamo-o"),
   Kommoo(784, "Kommo-o"),
   Tapu_Koko(785, "TapuKoko"),
   Tapu_Lele(786, "TapuLele"),
   Tapu_Bulu(787, "TapuBulu"),
   Tapu_Fini(788, "TapuFini"),
   Cosmog(789, "Cosmog"),
   Cosmoem(790, "Cosmoem"),
   Solgaleo(791, "Solgaleo"),
   Lunala(792, "Lunala"),
   Nihilego(793, "Nihilego"),
   Buzzwole(794, "Buzzwole"),
   Pheromosa(795, "Pheromosa"),
   Xurkitree(796, "Xurkitree"),
   Celesteela(797, "Celesteela"),
   Kartana(798, "Kartana"),
   Guzzlord(799, "Guzzlord"),
   Necrozma(800, "Necrozma"),
   Magearna(801, "Magearna"),
   Marshadow(802, "Marshadow"),
   Poipole(803, "Poipole"),
   Naganadel(804, "Naganadel"),
   Stakataka(805, "Stakataka"),
   Blacephalon(806, "Blacephalon"),
   Zeraora(807, "Zeraora"),
   Meltan(808, "Meltan"),
   Melmetal(809, "Melmetal"),
   Grookey(810, "Grookey"),
   Thwackey(811, "Thwackey"),
   Rillaboom(812, "Rillaboom"),
   Scorbunny(813, "Scorbunny"),
   Raboot(814, "Raboot"),
   Cinderace(815, "Cinderace"),
   Sobble(816, "Sobble"),
   Drizzile(817, "Drizzile"),
   Inteleon(818, "Inteleon"),
   Skwovet(819, "Skwovet"),
   Greedent(820, "Greedent"),
   Rookidee(821, "Rookidee"),
   Corvisquire(822, "Corvisquire"),
   Corviknight(823, "Corviknight"),
   Blipbug(824, "Blipbug"),
   Dottler(825, "Dottler"),
   Orbeetle(826, "Orbeetle"),
   Nickit(827, "Nickit"),
   Thievul(828, "Thievul"),
   Gossifleur(829, "Gossifleur"),
   Eldegoss(830, "Eldegoss"),
   Wooloo(831, "Wooloo"),
   Dubwool(832, "Dubwool"),
   Chewtle(833, "Chewtle"),
   Drednaw(834, "Drednaw"),
   Yamper(835, "Yamper"),
   Boltund(836, "Boltund"),
   Rolycoly(837, "Rolycoly"),
   Carkol(838, "Carkol"),
   Coalossal(839, "Coalossal"),
   Applin(840, "Applin"),
   Flapple(841, "Flapple"),
   Appletun(842, "Appletun"),
   Silicobra(843, "Silicobra"),
   Sandaconda(844, "Sandaconda"),
   Cramorant(845, "Cramorant"),
   Arrokuda(846, "Arrokuda"),
   Barraskewda(847, "Barraskewda"),
   Toxel(848, "Toxel"),
   Toxtricity(849, "Toxtricity"),
   Sizzlipede(850, "Sizzlipede"),
   Centiskorch(851, "Centiskorch"),
   Clobbopus(852, "Clobbopus"),
   Grapploct(853, "Grapploct"),
   Sinistea(854, "Sinistea"),
   Polteageist(855, "Polteageist"),
   Hatenna(856, "Hatenna"),
   Hattrem(857, "Hattrem"),
   Hatterene(858, "Hatterene"),
   Impidimp(859, "Impidimp"),
   Morgrem(860, "Morgrem"),
   Grimmsnarl(861, "Grimmsnarl"),
   Obstagoon(862, "Obstagoon"),
   Perrserker(863, "Perrserker"),
   Cursola(864, "Cursola"),
   Sirfetchd(865, "Sirfetchd"),
   MrRime(866, "MrRime"),
   Runerigus(867, "Runerigus"),
   Milcery(868, "Milcery"),
   Alcremie(869, "Alcremie"),
   Falinks(870, "Falinks"),
   Pincurchin(871, "Pincurchin"),
   Snom(872, "Snom"),
   Frosmoth(873, "Frosmoth"),
   Stonjourner(874, "Stonjourner"),
   Eiscue(875, "Eiscue"),
   Indeedee(876, "Indeedee"),
   Morpeko(877, "Morpeko"),
   Cufant(878, "Cufant"),
   Copperajah(879, "Copperajah"),
   Dracozolt(880, "Dracozolt"),
   Arctozolt(881, "Arctozolt"),
   Dracovish(882, "Dracovish"),
   Arctovish(883, "Arctovish"),
   Duraludon(884, "Duraludon"),
   Dreepy(885, "Dreepy"),
   Drakloak(886, "Drakloak"),
   Dragapult(887, "Dragapult"),
   Zacian(888, "Zacian"),
   Zamazenta(889, "Zamazenta"),
   Eternatus(890, "Eternatus"),
   Kubfu(891, "Kubfu"),
   Urshifu(892, "Urshifu"),
   Zarude(893, "Zarude"),
   Regieleki(894, "Regieleki"),
   Regidrago(895, "Regidrago"),
   Glastrier(896, "Glastrier"),
   Spectrier(897, "Spectrier"),
   Calyrex(898, "Calyrex"),
   Wyrdeer(899, "Wyrdeer"),
   Kleavor(900, "Kleavor"),
   Ursaluna(901, "Ursaluna"),
   Basculegion(902, "Basculegion"),
   Sneasler(903, "Sneasler"),
   Overqwil(904, "Overqwil"),
   Enamorus(905, "Enamorus");

   private static final EnumSpecies[] VALUES = values();
   private static List pokemonNameList = new ArrayList(VALUES.length);
   public static final EnumSpecies[] LEGENDARY_ENUMS;
   public static Set legendaries;
   public static Set ultrabeasts;
   public static Set onlineTextured;
   public static Set zombieTextured;
   public static Set drownedTextured;
   public static Set valentineTextured;
   public static Set rainbowTextured;
   public static Set alienTextured;
   public static Set alterTextured;
   public static Set crystalTextured;
   public static Set pinkTextured;
   public static Set summerTextured;
   public static Set valencianTextured;
   public static Set creatorTextured;
   public static Set strikeTextured;
   public static Set ashenTextured;
   public static Set spiritTextured;
   public static Set halloweenForm;
   public static Set mfTextured;
   public static Set mfSprite;
   public static Set genderForm;
   public static Set cannotDynamax;
   public static Set illegalShinies;
   public static Set illegalShiniesGalarian;
   public static final Map baseStats;
   private static final Map lowerCaseNameMap;
   private static ListMultimap formList;
   public final String name;
   private final int nationalDex;
   private static int[] GEN_COUNTS;

   private static void addForm(Set list, EnumSpecial form) {
      EnumSpecies species;
      for(Iterator var2 = list.iterator(); var2.hasNext(); formList.put(species, form)) {
         species = (EnumSpecies)var2.next();
         if (formList.get(species).isEmpty()) {
            formList.put(species, EnumNoForm.NoForm);
         }
      }

   }

   private EnumSpecies(int dex, String name) {
      this.nationalDex = dex;
      this.name = name;
   }

   public static boolean hasPokemon(String name) {
      return getFromName(name).isPresent();
   }

   /** @deprecated */
   @Deprecated
   public static EnumSpecies get(String name) {
      return (EnumSpecies)getFromName(name).get();
   }

   public static boolean hasPokemonAnyCase(String name) {
      EnumSpecies species = getFromNameAnyCase(name);
      return species != null;
   }

   public static Optional contains(String containsString) {
      EnumSpecies[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumSpecies e = var1[var3];
         if (containsString.replace("-", "").contains(e.name.replace("-", "").toLowerCase())) {
            return Optional.of(e);
         }
      }

      return Optional.empty();
   }

   /** @deprecated */
   @Deprecated
   public static EnumSpecies getFromOrdinal(int ordinal) {
      return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : null;
   }

   public static EnumSpecies randomPoke() {
      return randomPoke(true);
   }

   public static EnumSpecies randomPoke(boolean canBeLegendary) {
      boolean isValid = false;
      EnumSpecies randomPokemon = Bulbasaur;

      while(true) {
         while(!isValid) {
            int pick = RandomHelper.rand.nextInt(VALUES.length);
            randomPokemon = VALUES[pick];
            isValid = true;
            if (!canBeLegendary && legendaries.contains(randomPokemon)) {
               isValid = false;
            } else if (!PixelmonConfig.allGenerationsDisabled() && !PixelmonConfig.isGenerationEnabled(randomPokemon.getGeneration())) {
               isValid = false;
            } else if (randomPokemon == MissingNo) {
               isValid = false;
            }
         }

         return randomPokemon;
      }
   }

   public static EnumSpecies randomLegendary() {
      return LEGENDARY_ENUMS[RandomHelper.rand.nextInt(LEGENDARY_ENUMS.length)];
   }

   public static EnumSpecies getFromNameAnyCase(String name) {
      EnumSpecies[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumSpecies pokemon = var1[var3];
         if (pokemon.name.equalsIgnoreCase(name) || I18n.func_74838_a("pixelmon." + pokemon.name.toLowerCase() + ".name").equalsIgnoreCase(name)) {
            return pokemon;
         }
      }

      return null;
   }

   public static EnumSpecies getFromNameAnyCaseNoTranslate(String name) {
      return (EnumSpecies)lowerCaseNameMap.get(name.toLowerCase());
   }

   public static Optional getFromName(String name) {
      try {
         return Optional.of(valueOf(name));
      } catch (Exception var2) {
         return Optional.ofNullable(getFromNameAnyCase(name));
      }
   }

   public static EnumSpecies getFromDex(int nationalDex) {
      if (nationalDex < 0) {
         return null;
      } else if (nationalDex < VALUES.length && VALUES[nationalDex].nationalDex == nationalDex) {
         return VALUES[nationalDex];
      } else {
         for(int i = VALUES.length - 1; i >= 0; --i) {
            if (VALUES[i].nationalDex == nationalDex) {
               return VALUES[i];
            }

            if (VALUES[i].nationalDex < nationalDex) {
               break;
            }
         }

         return null;
      }
   }

   public static int getPokedexNumber(String name) {
      Optional pokemon = getFromName(name);
      return (Integer)pokemon.map(EnumSpecies::getNationalPokedexInteger).orElse(-1);
   }

   public static ImmutableList getNameList() {
      return ImmutableList.copyOf(pokemonNameList);
   }

   public String getNationalPokedexNumber() {
      return String.format("%03d", this.nationalDex);
   }

   public int getNationalPokedexInteger() {
      return this.nationalDex;
   }

   public String getPokemonName() {
      return this.name;
   }

   public int getGeneration() {
      return this.nationalDex < 1 ? 0 : (this.nationalDex < 152 ? 1 : (this.nationalDex < 252 ? 2 : (this.nationalDex < 387 ? 3 : (this.nationalDex < 494 ? 4 : (this.nationalDex < 650 ? 5 : (this.nationalDex < 722 ? 6 : (this.nationalDex < 808 ? 7 : 8)))))));
   }

   public static int getGeneration(int nationalDex) {
      return nationalDex < 1 ? 0 : (nationalDex < 152 ? 1 : (nationalDex < 252 ? 2 : (nationalDex < 387 ? 3 : (nationalDex < 494 ? 4 : (nationalDex < 650 ? 5 : (nationalDex < 722 ? 6 : (nationalDex < 808 ? 7 : 8)))))));
   }

   public static int getCountInGeneration(int generation) {
      if (GEN_COUNTS == null) {
         GEN_COUNTS = new int[8];
         EnumSpecies[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumSpecies species = var1[var3];
            if (species.getGeneration() > 0) {
               int var10002 = GEN_COUNTS[species.getGeneration() - 1]++;
            }
         }
      }

      return generation >= 1 && generation <= GEN_COUNTS.length ? GEN_COUNTS[generation - 1] : 0;
   }

   public boolean isLegendary() {
      return legendaries.contains(this);
   }

   public boolean isUltraBeast() {
      return ultrabeasts.contains(this);
   }

   public IEnumForm getFormEnum(int form) {
      if (form != -1 && formList.containsKey(this)) {
         Iterator var2 = formList.get(this).iterator();

         IEnumForm enumForm;
         do {
            if (!var2.hasNext()) {
               return (IEnumForm)formList.get(this).get(0);
            }

            enumForm = (IEnumForm)var2.next();
         } while(enumForm.getForm() != form);

         return enumForm;
      } else {
         return (IEnumForm)(formList.containsKey(this) ? (IEnumForm)formList.get(this).get(0) : EnumNoForm.NoForm);
      }
   }

   public IEnumForm getFormEnum(String suffix) {
      if (formList.containsKey(this)) {
         Iterator var2 = formList.get(this).iterator();

         IEnumForm enumForm;
         do {
            if (!var2.hasNext()) {
               return (IEnumForm)formList.get(this).get(0);
            }

            enumForm = (IEnumForm)var2.next();
         } while(!enumForm.getFormSuffix().replace("-", "").equalsIgnoreCase(suffix));

         return enumForm;
      } else {
         return (IEnumForm)(formList.containsKey(this) ? (IEnumForm)formList.get(this).get(0) : EnumNoForm.NoForm);
      }
   }

   public int getNumForms(boolean temporary) {
      int forms = 0;
      if (formList.containsKey(this)) {
         Iterator var3 = formList.get(this).iterator();

         while(true) {
            IEnumForm enumForm;
            do {
               if (!var3.hasNext()) {
                  return forms;
               }

               enumForm = (IEnumForm)var3.next();
            } while(enumForm.isTemporary() && !temporary);

            ++forms;
         }
      } else {
         return forms;
      }
   }

   public List getDefaultForms() {
      List forms = this.getPossibleForms(false);
      forms.removeIf((form) -> {
         return !form.isDefaultForm() && form != EnumNoForm.NoForm;
      });
      return forms.isEmpty() ? this.getPossibleForms(false) : forms;
   }

   public List getPossibleForms(boolean temporary) {
      if (!formList.containsKey(this)) {
         return Lists.newArrayList(new IEnumForm[]{EnumNoForm.NoForm});
      } else {
         List forms = Lists.newArrayList();
         Iterator var3 = formList.get(this).iterator();

         while(true) {
            IEnumForm form;
            do {
               if (!var3.hasNext()) {
                  return forms;
               }

               form = (IEnumForm)var3.next();
            } while(!temporary && form.isTemporary());

            forms.add(form);
         }
      }
   }

   public List getPossibleFormsFor(FormAttributes attribute) {
      if (!formList.containsKey(this)) {
         return Collections.emptyList();
      } else {
         List forms = Lists.newArrayList();
         Iterator var3 = formList.get(this).iterator();

         while(var3.hasNext()) {
            IEnumForm form = (IEnumForm)var3.next();
            if (form.getFormAttributes().contains(attribute)) {
               forms.add(form);
            }
         }

         return forms;
      }
   }

   public BaseStats getBaseStats() {
      return (BaseStats)baseStats.get(this);
   }

   public BaseStats getBaseStats(IEnumForm form) {
      if (form == null) {
         System.out.println("There was a null IEnumForm passed into the base stat getter for " + this.name);
      }

      return this.getBaseStats(form.getForm());
   }

   public BaseStats getBaseStats(int form) {
      BaseStats coreBaseStats = (BaseStats)baseStats.get(this);
      return coreBaseStats != null && coreBaseStats.forms != null && coreBaseStats.forms.containsKey(form) ? (BaseStats)coreBaseStats.forms.get(form) : coreBaseStats;
   }

   public EnumSpecies getBaseSpecies() {
      try {
         BaseStats store = this.getBaseStats();
         if (store.legacyPreEvolutions != null && store.legacyPreEvolutions.length > 0) {
            EnumSpecies preEvolution = store.legacyPreEvolutions[0];
            return !PixelmonConfig.isGenerationEnabled(preEvolution.getGeneration()) && PixelmonConfig.isGenerationEnabled(this.getGeneration()) ? this : preEvolution.getBaseSpecies();
         } else {
            return this;
         }
      } catch (Exception var3) {
         Pixelmon.LOGGER.error("Error getting basic Pokémon form for " + this.name + ".");
         var3.printStackTrace();
         return this;
      }
   }

   public boolean hasMega() {
      return EnumMegaPokemon.getMega(this) != null || this == Necrozma;
   }

   public boolean is(EnumSpecies... species) {
      EnumSpecies[] var2 = species;
      int var3 = species.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumSpecies s = var2[var4];
         if (this == s) {
            return true;
         }
      }

      return false;
   }

   public String getUnlocalizedName() {
      return "pixelmon." + this.name.toLowerCase() + ".name";
   }

   /** @deprecated */
   @Deprecated
   public boolean hasSpecialTexture() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public List getSpecialTextures() {
      return Collections.emptyList();
   }

   public Map getMoveset(byte form, String texture) {
      Map moveset = new HashMap();
      BaseStats stats = this.getBaseStats(form);
      EnumMovesetGroup[] var5 = EnumMovesetGroup.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumMovesetGroup group = var5[var7];
         if (group.getId() > 0) {
            moveset.put(group, stats.getTMMovesFor(group.getId()));
         }
      }

      ((List)moveset.get(EnumMovesetGroup.Galar)).addAll(stats.getTrMoves());
      moveset.put(EnumMovesetGroup.LevelUp, stats.getFormatLevelMoves());
      moveset.put(EnumMovesetGroup.Egg, stats.getEggMoves());
      MovesetSyncEvent evt = new MovesetSyncEvent(this, form, texture, moveset);
      Pixelmon.EVENT_BUS.post(evt);
      return evt.getMoveset();
   }

   static {
      EnumSpecies[] megas = VALUES;
      int var1 = megas.length;

      int var2;
      for(var2 = 0; var2 < var1; ++var2) {
         EnumSpecies pokemon = megas[var2];
         pokemonNameList.add(pokemon.name);
      }

      LEGENDARY_ENUMS = new EnumSpecies[]{Articuno, Zapdos, Moltres, Mewtwo, Mew, Raikou, Entei, Suicune, Lugia, Hooh, Celebi, Regirock, Regice, Registeel, Latias, Latios, Kyogre, Groudon, Rayquaza, Jirachi, Deoxys, Uxie, Mesprit, Azelf, Dialga, Palkia, Heatran, Regigigas, Giratina, Cresselia, Phione, Manaphy, Darkrai, Shaymin, Arceus, Cobalion, Terrakion, Virizion, Tornadus, Thundurus, Landorus, Reshiram, Zekrom, Kyurem, Keldeo, Meloetta, Genesect, Victini, Xerneas, Yveltal, Zygarde, Diancie, Hoopa, Volcanion, TypeNull, Silvally, Tapu_Koko, Tapu_Lele, Tapu_Bulu, Tapu_Fini, Cosmog, Cosmoem, Solgaleo, Lunala, Necrozma, Marshadow, Magearna, Zeraora, Meltan, Melmetal, Zacian, Zamazenta, Eternatus, Kubfu, Urshifu, Zarude, Regieleki, Regidrago, Glastrier, Spectrier, Calyrex, Enamorus};
      legendaries = Sets.immutableEnumSet(Articuno, new EnumSpecies[]{Zapdos, Moltres, Mewtwo, Mew, Raikou, Entei, Suicune, Lugia, Hooh, Celebi, Regirock, Regice, Registeel, Latias, Latios, Kyogre, Groudon, Rayquaza, Jirachi, Deoxys, Uxie, Mesprit, Azelf, Dialga, Palkia, Heatran, Regigigas, Giratina, Cresselia, Phione, Manaphy, Darkrai, Shaymin, Arceus, Cobalion, Terrakion, Virizion, Tornadus, Thundurus, Landorus, Reshiram, Zekrom, Kyurem, Keldeo, Meloetta, Genesect, Victini, Xerneas, Yveltal, Zygarde, Diancie, Hoopa, Volcanion, TypeNull, Silvally, Tapu_Koko, Tapu_Lele, Tapu_Bulu, Tapu_Fini, Cosmog, Cosmoem, Solgaleo, Lunala, Necrozma, Marshadow, Magearna, Zeraora, Meltan, Melmetal, Zacian, Zamazenta, Eternatus, Kubfu, Urshifu, Zarude, Regieleki, Regidrago, Glastrier, Spectrier, Calyrex, Enamorus});
      ultrabeasts = Sets.immutableEnumSet(Nihilego, new EnumSpecies[]{Buzzwole, Pheromosa, Xurkitree, Celesteela, Kartana, Guzzlord, Blacephalon, Poipole, Naganadel, Stakataka});
      onlineTextured = Sets.immutableEnumSet(Gastly, new EnumSpecies[]{Haunter, Gengar, Lugia, Hooh, Wobbuffet, Greninja, Sentret, Mewtwo, Lucario, Cinderace, Mothim, Spheal});
      zombieTextured = Sets.immutableEnumSet(Venusaur, new EnumSpecies[]{Charizard, Blastoise, Arcanine, Gyarados, Ditto, Meganium, Typhlosion, Feraligatr, Sceptile, Blaziken, Swampert, Torterra, Infernape, Empoleon, Serperior, Emboar, Samurott, Chesnaught, Delphox, Sableye});
      drownedTextured = Sets.immutableEnumSet(Octillery, new EnumSpecies[]{Claydol, Finneon, Gyarados, Kingdra, Lugia, Lumineon, Omastar, Pikachu, Pyukumuku, Qwilfish, Rapidash, Relicanth, Remoraid, Sableye, Starmie, Tentacruel, Flygon});
      valentineTextured = Sets.immutableEnumSet(Koffing, new EnumSpecies[]{Weezing, Gardevoir, Spinda});
      rainbowTextured = Sets.immutableEnumSet(Beautifly, new EnumSpecies[]{Crawdaunt, Cresselia, Dustox, Feebas, Infernape, Kecleon, Milotic, Ponyta, Rapidash, Hooh, Skarmory, Weavile});
      alienTextured = Sets.immutableEnumSet(Celebi, new EnumSpecies[0]);
      alterTextured = Sets.immutableEnumSet(Bagon, new EnumSpecies[]{Baltoy, Doublade, Froakie, Frogadier, Honedge, Kecleon, Marill, Porygon2, Porygon, Shelgon, Zorua, Pyukumuku, Rayquaza, Zoroark, Azumarill, Shedinja, Claydol, Salamence, PorygonZ, Volcarona});
      crystalTextured = Sets.immutableEnumSet(Onix, new EnumSpecies[]{Steelix});
      pinkTextured = Sets.immutableEnumSet(Beedrill, new EnumSpecies[]{Butterfree, Misdreavus, Mismagius, Nidoking, Nidoqueen, Venomoth, Venonat, Caterpie, Kakuna, Metapod, Weedle, Nidoranfemale, Nidoranmale, Bellsprout, Diglett, Dodrio, Doduo, Dugtrio, Mankey, Nidorina, Nidorino, Primeape, Rhydon, Rhyhorn, Rhyperior, Victreebel, Weepinbell, Oddish, Gloom, Vileplume, Bellossom, Exeggcute, Exeggutor, Paras, Parasect, Pidgey, Pidgeotto, Pidgeot, Rattata, Raticate});
      summerTextured = Sets.immutableEnumSet(Lopunny, new EnumSpecies[]{Raichu, Electrode, Snorlax, Omanyte});
      valencianTextured = Sets.immutableEnumSet(Bellossom, new EnumSpecies[]{Bellsprout, Butterfree, Blissey, Caterpie, Chansey, Cloyster, Gyarados, Happiny, Magikarp, Metapod, Nidoking, Nidoqueen, Nidoranfemale, Nidoranmale, Nidorina, Nidorino, Paras, Parasect, Raticate, Rattata, Shellder, Victreebel, Weepinbell, Politoed, Poliwag, Poliwrath, Poliwhirl, Oddish, Gloom, Vileplume});
      creatorTextured = Sets.immutableEnumSet(Dragonite, new EnumSpecies[]{Eevee});
      strikeTextured = Sets.immutableEnumSet(Boltund, new EnumSpecies[]{Electivire, Gallade, Jolteon, Ninetales, Poliwrath, Vivillon, Weavile, Crobat, Eevee, Espeon, Gardevoir, Golbat, Kirlia, Leafeon, Ralts, Sneasel, Umbreon, Vaporeon, Vulpix, Yamper, Zubat});
      ashenTextured = Sets.immutableEnumSet(Arcanine, new EnumSpecies[]{Centiskorch, Dragonite, Dustox, Gengar, Houndoom, Hydreigon, Roserade, Talonflame, Umbreon});
      spiritTextured = Sets.immutableEnumSet(Breloom, new EnumSpecies[]{Cresselia, Dragapult, Drakloak, Dreepy, Eevee, Espeon, Flareon, Glaceon, Jolteon, Leafeon, Mareanie, Milotic, Porygon2, Regieleki, Shroomish, Suicune, Sylveon, Toxapex, Umbreon, Vaporeon});
      halloweenForm = Sets.immutableEnumSet(Ivysaur, new EnumSpecies[0]);
      mfTextured = Sets.immutableEnumSet(Magikarp, new EnumSpecies[]{Buizel, Floatzel, Girafarig, Hippopotas, Hippowdon, Snover, Wobbuffet, Combee});
      mfSprite = Sets.immutableEnumSet(Hippopotas, new EnumSpecies[]{Hippowdon, Wobbuffet, Combee});
      genderForm = Sets.immutableEnumSet(Meowstic, new EnumSpecies[]{Pyroar, Frillish, Jellicent, Unfezant, Indeedee, Shinx, Luxio, Luxray, Basculegion});
      cannotDynamax = Sets.immutableEnumSet(Zacian, new EnumSpecies[]{Zamazenta, Eternatus});
      illegalShinies = Sets.immutableEnumSet(Victini, new EnumSpecies[]{Keldeo, Meloetta, Hoopa, Volcanion, Cosmog, Cosmoem, Magearna, Marshadow, Zacian, Zamazenta, Eternatus, Kubfu, Urshifu, Zarude, Glastrier, Spectrier, Calyrex});
      illegalShiniesGalarian = Sets.immutableEnumSet(Articuno, new EnumSpecies[]{Zapdos, Moltres});
      baseStats = Collections.unmodifiableMap(BaseStats.allBaseStats);
      lowerCaseNameMap = Maps.newHashMap();
      formList = MultimapBuilder.enumKeys(EnumSpecies.class).arrayListValues(1).build();
      megas = new EnumSpecies[]{Alakazam, Kangaskhan, Pinsir, Gyarados, Aerodactyl, Ampharos, Scizor, Heracross, Houndoom, Tyranitar, Blaziken, Gardevoir, Mawile, Aggron, Medicham, Manectric, Banette, Absol, Garchomp, Lucario, Abomasnow, Beedrill, Pidgeot, Steelix, Sceptile, Swampert, Sableye, Sharpedo, Camerupt, Altaria, Glalie, Salamence, Metagross, Latias, Latios, Rayquaza, Lopunny, Gallade, Audino, Diancie};
      EnumSpecies[] alolans = megas;
      var2 = megas.length;

      int var11;
      for(var11 = 0; var11 < var2; ++var11) {
         EnumSpecies species = alolans[var11];
         if (species.hasMega()) {
            formList.put(species, EnumMega.Normal);
            formList.put(species, EnumMega.Mega);
         }
      }

      formList.put(Mewtwo, EnumMega.Normal);
      formList.put(Mewtwo, EnumMega.MegaX);
      formList.put(Mewtwo, EnumMega.MegaY);
      alolans = new EnumSpecies[]{Diglett, Dugtrio, Exeggutor, Sandshrew, Sandslash, Persian, Grimer, Muk, Marowak, Vulpix, Ninetales, Geodude, Graveler, Golem, Rattata, Raticate, Raichu};
      EnumSpecies[] galarian = alolans;
      var11 = alolans.length;

      int var12;
      for(var12 = 0; var12 < var11; ++var12) {
         EnumSpecies species = galarian[var12];
         formList.put(species, RegionalForms.NORMAL);
         formList.put(species, RegionalForms.ALOLAN);
      }

      galarian = new EnumSpecies[]{MrMime, Farfetchd, Darumaka, Ponyta, Rapidash, Zigzagoon, Linoone, Corsola, Yamask, Stunfisk, Weezing, Articuno, Zapdos, Moltres};
      EnumSpecies[] hisuian = galarian;
      var12 = galarian.length;

      EnumSpecies specie;
      int var14;
      for(var14 = 0; var14 < var12; ++var14) {
         specie = hisuian[var14];
         if (!formList.get(specie).contains(RegionalForms.NORMAL)) {
            formList.put(specie, RegionalForms.NORMAL);
         }

         formList.put(specie, RegionalForms.GALARIAN);
      }

      hisuian = new EnumSpecies[]{Growlithe, Arcanine, Voltorb, Electrode, Typhlosion, Qwilfish, Sneasel, Samurott, Lilligant, Zorua, Zoroark, Braviary, Sliggoo, Goodra, Avalugg, Decidueye};
      EnumSpecies[] giga = hisuian;
      var14 = hisuian.length;

      int var16;
      for(var16 = 0; var16 < var14; ++var16) {
         EnumSpecies species = giga[var16];
         if (!formList.get(species).contains(RegionalForms.NORMAL)) {
            formList.put(species, RegionalForms.NORMAL);
         }

         formList.put(species, RegionalForms.HISUIAN);
      }

      giga = new EnumSpecies[]{Butterfree, Pikachu, Machamp, Kingler, Lapras, Eevee, Garbodor, Melmetal, Corviknight, Orbeetle, Drednaw, Coalossal, Flapple, Appletun, Sandaconda, Centiskorch, Hatterene, Grimmsnarl, Copperajah, Duraludon, Rillaboom, Cinderace, Inteleon};
      EnumSpecies[] var17 = giga;
      var16 = giga.length;

      EnumSpecies value;
      int var20;
      for(var20 = 0; var20 < var16; ++var20) {
         value = var17[var20];
         if (value != Meowth) {
            formList.put(value, EnumGigantamax.Normal);
         }

         formList.put(value, EnumGigantamax.Gigantamax);
      }

      EnumCharizard[] var18 = EnumCharizard.values();
      var16 = var18.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumCharizard form = var18[var20];
         formList.put(Charizard, form);
      }

      EnumBlastoise[] var19 = EnumBlastoise.values();
      var16 = var19.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumBlastoise form = var19[var20];
         formList.put(Blastoise, form);
      }

      EnumVenusaur[] var21 = EnumVenusaur.values();
      var16 = var21.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumVenusaur form = var21[var20];
         formList.put(Venusaur, form);
      }

      EnumMeowth[] var22 = EnumMeowth.values();
      var16 = var22.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumMeowth form = var22[var20];
         formList.put(Meowth, form);
      }

      EnumSlowpoke[] var23 = EnumSlowpoke.values();
      var16 = var23.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumSlowpoke form = var23[var20];
         formList.put(Slowpoke, form);
      }

      EnumSlowbro[] var25 = EnumSlowbro.values();
      var16 = var25.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumSlowbro form = var25[var20];
         formList.put(Slowbro, form);
      }

      EnumSlowking[] var27 = EnumSlowking.values();
      var16 = var27.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumSlowking form = var27[var20];
         formList.put(Slowking, form);
      }

      EnumGengar[] var29 = EnumGengar.values();
      var16 = var29.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumGengar form = var29[var20];
         formList.put(Gengar, form);
      }

      EnumSnorlax[] var31 = EnumSnorlax.values();
      var16 = var31.length;

      for(var20 = 0; var20 < var16; ++var20) {
         EnumSnorlax form = var31[var20];
         formList.put(Snorlax, form);
      }

      EnumToxtricity[] var33 = EnumToxtricity.values();
      var16 = var33.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var33[var20];
         formList.put(Toxtricity, form);
      }

      Iterator var35 = genderForm.iterator();

      while(var35.hasNext()) {
         specie = (EnumSpecies)var35.next();
         formList.put(specie, Gender.Male);
         formList.put(specie, Gender.Female);
      }

      EnumMissingNo[] var37 = EnumMissingNo.values();
      var16 = var37.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var37[var20];
         formList.put(MissingNo, form);
      }

      EnumPichu[] var39 = EnumPichu.values();
      var16 = var39.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var39[var20];
         formList.put(Pichu, form);
      }

      EnumShearable[] var41 = EnumShearable.values();
      var16 = var41.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var41[var20];
         formList.put(Mareep, form);
         formList.put(Wooloo, form);
         formList.put(Dubwool, form);
      }

      EnumUnown[] var43 = EnumUnown.values();
      var16 = var43.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var43[var20];
         formList.put(Unown, form);
      }

      EnumBurmy[] var45 = EnumBurmy.values();
      var16 = var45.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var45[var20];
         formList.put(Burmy, form);
      }

      EnumWormadam[] var47 = EnumWormadam.values();
      var16 = var47.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var47[var20];
         formList.put(Wormadam, form);
      }

      EnumCastform[] var49 = EnumCastform.values();
      var16 = var49.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var49[var20];
         formList.put(Castform, form);
      }

      EnumPrimal[] var51 = EnumPrimal.values();
      var16 = var51.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var51[var20];
         formList.put(Groudon, form);
         formList.put(Kyogre, form);
      }

      EnumOrigin[] var53 = EnumOrigin.values();
      var16 = var53.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var53[var20];
         formList.put(Palkia, form);
         formList.put(Dialga, form);
      }

      EnumGroudon[] var55 = EnumGroudon.values();
      var16 = var55.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var55[var20];
         formList.put(Groudon, form);
      }

      EnumClobbopus[] var57 = EnumClobbopus.values();
      var16 = var57.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var57[var20];
         formList.put(Clobbopus, form);
      }

      EnumBurningSalt[] var59 = EnumBurningSalt.values();
      var16 = var59.length;

      EnumBurningSalt form;
      for(var20 = 0; var20 < var16; ++var20) {
         form = var59[var20];
         formList.put(Slugma, form);
      }

      var59 = EnumBurningSalt.values();
      var16 = var59.length;

      for(var20 = 0; var20 < var16; ++var20) {
         form = var59[var20];
         formList.put(Magmar, form);
      }

      EnumSpheal[] var62 = EnumSpheal.values();
      var16 = var62.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var62[var20];
         formList.put(Spheal, form);
      }

      EnumBidoof[] var64 = EnumBidoof.values();
      var16 = var64.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var64[var20];
         formList.put(Bidoof, form);
      }

      EnumFeebas[] var66 = EnumFeebas.values();
      var16 = var66.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var66[var20];
         formList.put(Feebas, form);
      }

      EnumDeoxys[] var67 = EnumDeoxys.values();
      var16 = var67.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var67[var20];
         formList.put(Deoxys, form);
      }

      EnumShellos[] var69 = EnumShellos.values();
      var16 = var69.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var69[var20];
         formList.put(Shellos, form);
      }

      EnumGastrodon[] var71 = EnumGastrodon.values();
      var16 = var71.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var71[var20];
         formList.put(Gastrodon, form);
      }

      EnumCherrim[] var73 = EnumCherrim.values();
      var16 = var73.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var73[var20];
         formList.put(Cherrim, form);
      }

      EnumCramorant[] var75 = EnumCramorant.values();
      var16 = var75.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var75[var20];
         formList.put(Cramorant, form);
      }

      EnumRotom[] var77 = EnumRotom.values();
      var16 = var77.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var77[var20];
         formList.put(Rotom, form);
      }

      EnumGiratina[] var79 = EnumGiratina.values();
      var16 = var79.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var79[var20];
         formList.put(Giratina, form);
      }

      EnumShaymin[] var81 = EnumShaymin.values();
      var16 = var81.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var81[var20];
         formList.put(Shaymin, form);
      }

      EnumArceus[] var83 = EnumArceus.values();
      var16 = var83.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var83[var20];
         formList.put(Arceus, form);
      }

      EnumLunatone[] var85 = EnumLunatone.values();
      var16 = var85.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var85[var20];
         formList.put(Lunatone, form);
      }

      EnumPikachu[] var87 = EnumPikachu.values();
      var16 = var87.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var87[var20];
         formList.put(Pikachu, form);
      }

      EnumSilvally[] var89 = EnumSilvally.values();
      var16 = var89.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var89[var20];
         formList.put(Silvally, form);
      }

      EnumBasculin[] var91 = EnumBasculin.values();
      var16 = var91.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var91[var20];
         formList.put(Basculin, form);
      }

      EnumMagikarp[] var93 = EnumMagikarp.values();
      var16 = var93.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var93[var20];
         formList.put(Magikarp, form);
      }

      EnumArbok[] var95 = EnumArbok.values();
      var16 = var95.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var95[var20];
         formList.put(Arbok, form);
      }

      EnumAmoonguss[] var97 = EnumAmoonguss.values();
      var16 = var97.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var97[var20];
         formList.put(Amoonguss, form);
      }

      EnumSandile[] var99 = EnumSandile.values();
      var16 = var99.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var99[var20];
         formList.put(Sandile, form);
      }

      EnumSolgaleo[] var101 = EnumSolgaleo.values();
      var16 = var101.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var101[var20];
         formList.put(Solgaleo, form);
      }

      EnumDarmanitan[] var103 = EnumDarmanitan.values();
      var16 = var103.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var103[var20];
         formList.put(Darmanitan, form);
      }

      EnumMeloetta[] var105 = EnumMeloetta.values();
      var16 = var105.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var105[var20];
         formList.put(Meloetta, form);
      }

      EnumKeldeo[] var107 = EnumKeldeo.values();
      var16 = var107.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var107[var20];
         formList.put(Keldeo, form);
      }

      EnumGenesect[] var109 = EnumGenesect.values();
      var16 = var109.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var109[var20];
         formList.put(Genesect, form);
      }

      EnumKyurem[] var111 = EnumKyurem.values();
      var16 = var111.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var111[var20];
         formList.put(Kyurem, form);
      }

      EnumVivillon[] var113 = EnumVivillon.values();
      var16 = var113.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var113[var20];
         formList.put(Vivillon, form);
      }

      EnumWishiwashi[] var115 = EnumWishiwashi.values();
      var16 = var115.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var115[var20];
         formList.put(Wishiwashi, form);
      }

      EnumXerneas[] var117 = EnumXerneas.values();
      var16 = var117.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var117[var20];
         formList.put(Xerneas, form);
      }

      EnumHoopa[] var119 = EnumHoopa.values();
      var16 = var119.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var119[var20];
         formList.put(Hoopa, form);
      }

      EnumEiscue[] var121 = EnumEiscue.values();
      var16 = var121.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var121[var20];
         formList.put(Eiscue, form);
      }

      EnumAlcremie[] var123 = EnumAlcremie.values();
      var16 = var123.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var123[var20];
         formList.put(Alcremie, form);
      }

      EnumFlabebe[] var125 = EnumFlabebe.values();
      var16 = var125.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var125[var20];
         if (form != EnumFlabebe.AZ) {
            formList.put(Flabebe, form);
            formList.put(Florges, form);
         }

         formList.put(Floette, form);
      }

      EnumGreninja[] var127 = EnumGreninja.values();
      var16 = var127.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var127[var20];
         formList.put(Greninja, form);
      }

      EnumAuthentic[] var129 = EnumAuthentic.values();
      var16 = var129.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var129[var20];
         formList.put(Sinistea, form);
         formList.put(Polteageist, form);
      }

      SeasonForm[] var131 = SeasonForm.values();
      var16 = var131.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var131[var20];
         formList.put(Deerling, form);
         formList.put(Sawsbuck, form);
      }

      EnumOricorio[] var133 = EnumOricorio.values();
      var16 = var133.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var133[var20];
         formList.put(Oricorio, form);
      }

      EnumLycanroc[] var135 = EnumLycanroc.values();
      var16 = var135.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var135[var20];
         formList.put(Lycanroc, form);
      }

      EnumMinior[] var137 = EnumMinior.values();
      var16 = var137.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var137[var20];
         formList.put(Minior, form);
      }

      EnumZygarde[] var139 = EnumZygarde.values();
      var16 = var139.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var139[var20];
         formList.put(Zygarde, form);
      }

      EnumNecrozma[] var141 = EnumNecrozma.values();
      var16 = var141.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var141[var20];
         formList.put(Necrozma, form);
      }

      EnumTherian[] var143 = EnumTherian.values();
      var16 = var143.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var143[var20];
         formList.put(Landorus, form);
         formList.put(Tornadus, form);
         formList.put(Thundurus, form);
         formList.put(Enamorus, form);
      }

      EnumAegislash[] var145 = EnumAegislash.values();
      var16 = var145.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var145[var20];
         formList.put(Aegislash, form);
      }

      EnumMimikyu[] var147 = EnumMimikyu.values();
      var16 = var147.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var147[var20];
         formList.put(Mimikyu, form);
      }

      EnumMorpeko[] var149 = EnumMorpeko.values();
      var16 = var149.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var149[var20];
         formList.put(Morpeko, form);
      }

      EnumHeroDuo[] var151 = EnumHeroDuo.values();
      var16 = var151.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var151[var20];
         formList.put(Zacian, form);
         formList.put(Zamazenta, form);
      }

      EnumEternatus[] var153 = EnumEternatus.values();
      var16 = var153.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var153[var20];
         formList.put(Eternatus, form);
      }

      EnumCalyrex[] var155 = EnumCalyrex.values();
      var16 = var155.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var155[var20];
         formList.put(Calyrex, form);
      }

      EnumUrshifu[] var157 = EnumUrshifu.values();
      var16 = var157.length;

      for(var20 = 0; var20 < var16; ++var20) {
         IEnumForm form = var157[var20];
         formList.put(Urshifu, form);
      }

      addForm(onlineTextured, EnumSpecial.Online);
      addForm(zombieTextured, EnumSpecial.Zombie);
      addForm(drownedTextured, EnumSpecial.Drowned);
      addForm(valentineTextured, EnumSpecial.Valentine);
      addForm(rainbowTextured, EnumSpecial.Rainbow);
      addForm(alienTextured, EnumSpecial.Alien);
      addForm(alterTextured, EnumSpecial.Alter);
      addForm(crystalTextured, EnumSpecial.Crystal);
      addForm(pinkTextured, EnumSpecial.Pink);
      addForm(summerTextured, EnumSpecial.Summer);
      addForm(valencianTextured, EnumSpecial.Valencian);
      addForm(creatorTextured, EnumSpecial.Creator);
      addForm(strikeTextured, EnumSpecial.Strike);
      addForm(ashenTextured, EnumSpecial.Ashen);
      addForm(spiritTextured, EnumSpecial.Spirit);
      addForm(halloweenForm, EnumSpecial.Halloween);
      formList = Multimaps.unmodifiableListMultimap(formList);
      var17 = values();
      var16 = var17.length;

      for(var20 = 0; var20 < var16; ++var20) {
         value = var17[var20];
         lowerCaseNameMap.put(value.name.toLowerCase(), value);
      }

      GEN_COUNTS = null;
   }
}
