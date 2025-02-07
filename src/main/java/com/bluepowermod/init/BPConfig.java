/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import net.minecraftforge.common.ForgeConfigSpec;

public class BPConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General CONFIG = new General(BUILDER);

    public static class General {
        public final ForgeConfigSpec.BooleanValue generateTungsten;
        public final ForgeConfigSpec.ConfigValue<Integer> minTungstenY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxTungstenY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountTungsten;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeTungsten;
        public final ForgeConfigSpec.BooleanValue generateSilver;
        public final ForgeConfigSpec.ConfigValue<Integer> minSilverY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxSilverY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountSilver;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeSilver;
        public final ForgeConfigSpec.BooleanValue generateZinc;
        public final ForgeConfigSpec.ConfigValue<Integer> minZincY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxZincY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountZinc;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeZinc;
        public final ForgeConfigSpec.BooleanValue generateTeslatite;
        public final ForgeConfigSpec.ConfigValue<Integer> minTeslatiteY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxTeslatiteY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountTeslatite;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeTeslatite;
        public final ForgeConfigSpec.BooleanValue generateRuby;
        public final ForgeConfigSpec.ConfigValue<Integer> minRubyY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxRubyY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountRuby;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeRuby;
        public final ForgeConfigSpec.BooleanValue generateAmethyst;
        public final ForgeConfigSpec.ConfigValue<Integer> minAmethystY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxAmethystY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountAmethyst;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeAmethyst;
        public final ForgeConfigSpec.BooleanValue generateSapphire;
        public final ForgeConfigSpec.ConfigValue<Integer> minSapphireY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxSapphireY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountSapphire;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeSapphire;
        public final ForgeConfigSpec.BooleanValue generateGreenSapphire;
        public final ForgeConfigSpec.ConfigValue<Integer> minGreenSapphireY;
        public final ForgeConfigSpec.ConfigValue<Integer> maxGreenSapphireY;
        public final ForgeConfigSpec.ConfigValue<Integer> veinCountGreenSapphire;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeGreenSapphire;
        public final ForgeConfigSpec.BooleanValue generateVolcano;
        public final ForgeConfigSpec.ConfigValue<Double> volcanoActiveToInactiveRatio;
        public final ForgeConfigSpec.ConfigValue<Double> volcanoSpawnChance; // chance of a volcano spawning per chunk.
        public final ForgeConfigSpec.ConfigValue<Integer> flowerSpawnChance;
        public final ForgeConfigSpec.ConfigValue<String> volcanoBiomeCategoryWhitelist;
        public final ForgeConfigSpec.BooleanValue generateTungstenInVolcano;
        public final ForgeConfigSpec.ConfigValue<String> alloyFurnaceBlacklist;
        public final ForgeConfigSpec.ConfigValue<Boolean> alloyFurnaceDatapackGenerator;
        public final ForgeConfigSpec.ConfigValue<Float> albedoBrightness;
        //public final ForgeConfigSpec.BooleanValue enableTubeCaching;
        public final ForgeConfigSpec.BooleanValue enableGateSounds;
        public final ForgeConfigSpec.BooleanValue generateMarble;
        public final ForgeConfigSpec.ConfigValue<Integer> veinSizeMarble;
        public final ForgeConfigSpec.BooleanValue serverCircuitSavingOpOnly;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("WorldGen").comment("Toggle blocks being generated into the world");
                builder.push("Tungsten").comment("Tungsten related configs");
                    generateTungsten = builder.comment("Generate Tungsten").translation("bluepower.config." + Refs.CONFIG_TUNGSTEN + ".generate").define("generateTungsten", true);
                    minTungstenY = builder.comment("Tungsten Min Y").translation("bluepower.config." + Refs.CONFIG_TUNGSTEN + ".min_y").define("minTungstenY", 1);
                    maxTungstenY = builder.comment("Tungsten Max Y").translation("bluepower.config." + Refs.CONFIG_TUNGSTEN + ".max_y").define("maxTungstenY", 10);
                    veinCountTungsten = builder.comment("Vein Count Tungsten").translation("bluepower.config." + Refs.CONFIG_TUNGSTEN + ".vein_count").define("veinCountTungsten", 2);
                    veinSizeTungsten = builder.comment("Vein Size Tungsten").translation("bluepower.config." + Refs.CONFIG_TUNGSTEN + ".vein_size").define("veinSizeTungsten", 3);
                builder.pop();
                builder.push("Zinc").comment("Zinc related configs");
                    generateZinc = builder.comment("Generate Zinc").translation("bluepower.config." + Refs.CONFIG_ZINC + ".generate").define("generateZinc", true);
                    minZincY = builder.comment("Zinc Min Y").translation("bluepower.config." + Refs.CONFIG_ZINC + ".min_y").define("minZincY", 15);
                    maxZincY = builder.comment("Zinc Max Y").translation("bluepower.config." + Refs.CONFIG_ZINC + ".max_y").define("maxZincY", 40);
                    veinCountZinc = builder.comment("Vein Count Zinc").translation("bluepower.config." + Refs.CONFIG_ZINC + ".vein_count").define("veinCountZinc", 6);
                    veinSizeZinc = builder.comment("Vein Size Zinc").translation("bluepower.config." + Refs.CONFIG_ZINC + ".vein_size").define("veinSizeZinc", 6);
                builder.pop();
                builder.push("Silver").comment("Silver related configs");
                    generateSilver = builder.comment("Generate Silver").translation("bluepower.config." + Refs.CONFIG_SILVER + ".generate").define("generateSilver", true);
                    minSilverY = builder.comment("Silver Min Y").translation("bluepower.config." + Refs.CONFIG_SILVER + ".min_y").define("minSilverY", 1);
                    maxSilverY = builder.comment("Silver Max Y").translation("bluepower.config." + Refs.CONFIG_SILVER + ".max_y").define("maxSilverY", 20);
                    veinCountSilver = builder.comment("Vein Count Silver").translation("bluepower.config." + Refs.CONFIG_SILVER + ".vein_count").define("veinCountSilver", 3);
                    veinSizeSilver = builder.comment("Vein Size Silver").translation("bluepower.config." + Refs.CONFIG_SILVER + ".vein_size").define("veinSizeSilver", 6);
                builder.pop();
                builder.push("Teslatite").comment("Teslatite related configs");
                    generateTeslatite = builder.comment("Generate Teslatite").translation("bluepower.config." + Refs.CONFIG_TESLATITE + ".generate").define("generateTeslatite", true);
                    minTeslatiteY = builder.comment("Teslatite Min Y").translation("bluepower.config." + Refs.CONFIG_TESLATITE + ".min_y").define("minTeslatiteY", 1);
                    maxTeslatiteY = builder.comment("Teslatite Max Y").translation("bluepower.config." + Refs.CONFIG_TESLATITE + ".max_y").define("maxTeslatiteY", 20);
                    veinCountTeslatite = builder.comment("Vein Count Teslatite").translation("bluepower.config." + Refs.CONFIG_TESLATITE + ".vein_count").define("veinCountTeslatite", 4);
                    veinSizeTeslatite = builder.comment("Vein Size Teslatite").translation("bluepower.config." + Refs.CONFIG_TESLATITE + ".vein_size").define("veinSizeTeslatite", 8);
                builder.pop();
                builder.push("Ruby").comment("Ruby related configs");
                    generateRuby = builder.comment("Generate Ruby").translation("bluepower.config." + Refs.CONFIG_RUBY + ".generate").define("generateRuby", true);
                    minRubyY = builder.comment("Ruby Min Y").translation("bluepower.config." + Refs.CONFIG_RUBY + ".min_y").define("minRubyY", 0);
                    maxRubyY = builder.comment("Ruby Max Y").translation("bluepower.config." + Refs.CONFIG_RUBY + ".max_y").define("maxRubyY", 48);
                    veinCountRuby = builder.comment("Vein Count Ruby").translation("bluepower.config." + Refs.CONFIG_RUBY + ".vein_count").define("veinCountRuby", 2);
                    veinSizeRuby = builder.comment("Vein Size Ruby").translation("bluepower.config." + Refs.CONFIG_RUBY + ".vein_size").define("veinSizeRuby", 5);
                builder.pop();
                builder.push("Amethyst").comment("Amethyst related configs");
                    generateAmethyst = builder.comment("Generate Amethyst").translation("bluepower.config." + Refs.CONFIG_AMETHYST + ".generate").define("generateAmethyst", true);
                    minAmethystY = builder.comment("Amethyst Min Y").translation("bluepower.config." + Refs.CONFIG_AMETHYST + ".min_y").define("minAmethystY", 0);
                    maxAmethystY = builder.comment("Amethyst Max Y").translation("bluepower.config." + Refs.CONFIG_AMETHYST + ".max_y").define("maxAmethystY", 48);
                    veinCountAmethyst = builder.comment("Vein Count Amethyst").translation("bluepower.config." + Refs.CONFIG_AMETHYST + ".vein_count").define("veinCountAmethyst", 2);
                    veinSizeAmethyst = builder.comment("Vein Size Amethyst").translation("bluepower.config." + Refs.CONFIG_AMETHYST + ".vein_size").define("veinSizeAmethyst", 5);
                builder.pop();
                builder.push("Sapphire").comment("Sapphire related configs");
                    generateSapphire = builder.comment("Generate Sapphire").translation("bluepower.config." + Refs.CONFIG_SAPPHIRE + ".generate").define("generateSapphire", true);
                    minSapphireY = builder.comment("Sapphire Min Y").translation("bluepower.config." + Refs.CONFIG_SAPPHIRE + ".min_y").define("minSapphireY", 0);
                    maxSapphireY = builder.comment("Sapphire Max Y").translation("bluepower.config." + Refs.CONFIG_SAPPHIRE + ".max_y").define("maxSapphireY", 48);
                    veinCountSapphire = builder.comment("Vein Count Sapphire").translation("bluepower.config." + Refs.CONFIG_SAPPHIRE + ".vein_count").define("veinCountSapphire", 2);
                    veinSizeSapphire = builder.comment("Vein Size Sapphire").translation("bluepower.config." + Refs.CONFIG_SAPPHIRE + ".vein_size").define("veinSizeSapphire", 5);
                builder.pop();
                builder.push("GreenSapphire").comment("Green Sapphire related configs");
                    generateGreenSapphire = builder.comment("Generate Green Sapphire").translation("bluepower.config." + Refs.CONFIG_GREENSAPPHIRE + ".generate").define("generateGreenSapphire", true);
                    minGreenSapphireY = builder.comment("Green Sapphire Min Y").translation("bluepower.config." + Refs.CONFIG_GREENSAPPHIRE + ".min_y").define("minGreenSapphireY", 0);
                    maxGreenSapphireY = builder.comment("Green Sapphire Max Y").translation("bluepower.config." + Refs.CONFIG_GREENSAPPHIRE + ".max_y").define("maxGreenSapphireY", 48);
                    veinCountGreenSapphire = builder.comment("Vein Count Green Sapphire").translation("bluepower.config." + Refs.CONFIG_GREENSAPPHIRE + ".vein_count").define("veinCountGreenSapphire", 2);
                    veinSizeGreenSapphire = builder.comment("Vein Size Green Sapphire").translation("bluepower.config." + Refs.CONFIG_GREENSAPPHIRE + ".vein_size").define("veinSizeGreenSapphire", 5);
                builder.pop();
                builder.push("IndigoFlower").comment("Indigo Flower related configs");
                    flowerSpawnChance = builder.comment("Indigo Flower Spawn Chance").translation("bluepower.config.flower_spawn_chance").define("flowerSpawnChance", 1);
                builder.pop();
                builder.push("Volcano").comment("Volcano related configs");
                    generateVolcano = builder.comment("Generate Volcano").translation("bluepower.config.volcano.generate").define("generateVolcano", true);
                    volcanoSpawnChance = builder.comment("Volcano Spawn Chance").translation("bluepower.config.volcano_spawn_chance").define("volcanoSpawnChance", 0.005);
                    generateTungstenInVolcano = builder.comment("Possible to generate Tungsten in the Volcano").translation("bluepower.config.volcano.tungsten.generate").define("generateTungstenInVolcano", true);
                    volcanoActiveToInactiveRatio = builder.comment("Volcano Active To Inactive Ratio").translation("bluepower.config.volcano_inactive_ratio").define("volcanoActiveToInactiveRatio", 0.3);
                    volcanoBiomeCategoryWhitelist = builder.comment("Biomes that volcanoes should generate").translation("bluepower.config.volcano_biomecategory_whitelist").define("volcanoBiomeCategoryWhitelist", "taiga,extreme_hills,jungle,mesa,plains,savanna,icy,beach,forest,ocean,desert,river,swamp,mushroom");
                builder.pop();
                builder.push("Marble").comment("Marble related configs");
                    generateMarble = builder.comment("Generate Marble").translation("bluepower.config.marble.generate").define("generateMarble", true);
                    veinSizeMarble = builder.comment("veinSizeMarble").translation("bluepower.config.marble.vein_size").define("veinSizeMarble", 2048);
                builder.pop();
            builder.pop();
            builder.push("Recipes").comment("Toggle recipes to be enabled or not");
                alloyFurnaceBlacklist = builder.comment( "Any item name (minecraft:bucket,minecraft:minecart) added here will be blacklisted from being able to melt down into its raw materials.").translation("bluepower.config.alloy_furnace.blacklist").define("alloyFurnaceBlacklist", "minecraft:iron_nugget,minecraft:gold_nugget,minecraft:gold_ingot,minecraft:iron_ingot");
                alloyFurnaceDatapackGenerator = builder.comment( "Generate Json Datapack for Alloy Furnace (Only used to generate recycling recipes)").translation("bluepower.config.alloy_furnace.datapack").define("alloyFurnaceDatapackGenerator", true);
            builder.pop();

            /*
            builder.push(Refs.CONFIG_TUBES).comment("Tube Options");
            Property prop = config.get(Refs.CONFIG_TUBES, "Enable Tube Caching", true);
            prop.setComment("When enabled, the Tube routing is more friendly for the CPU. In return it uses a bit more RAM. Caching also may contain bugs still.");
            enableTubeCaching = prop.getBoolean();

            prop = config.get(Refs.CONFIG_TUBES, "Tube Render Mode", "auto");
            prop.setComment("When encountering FPS issues with tubes with lots of items in it. Valid modes: \"normal\": Normal rendering, \"reduced\": All items going through tubes will display as 'one' item, \"none\": Only a small dot renders, \"auto\": will switch to \"normal\" on fancy graphics mode, and to \"reduced\" otherwise.");
            String tubeRenderMode = prop.getString();
            if (!tubeRenderMode.equals("normal") && !tubeRenderMode.equals("reduced") && !tubeRenderMode.equals("none")) {
                tubeRenderMode = "auto";
            }*/

            builder.push("Other").comment("Miscellaneous other configs");
                serverCircuitSavingOpOnly = builder.comment("Server Template Saving by Ops only").translation("bluepower.config.template_ops_only").define("ServerTemplateOpsonly", false);
                enableGateSounds = builder.comment("Enable Gate Ticking Sounds").translation("bluepower.config.ticking_sounds").define("tickingSounds", true);
                albedoBrightness = builder.comment("Albedo Support Lamp Brightness").translation("bluepower.config.albedo_brightness").define("albedoBrightness", 0.01F);
            builder.pop();
            }

    }
    public static final ForgeConfigSpec spec = BUILDER.build();

}
