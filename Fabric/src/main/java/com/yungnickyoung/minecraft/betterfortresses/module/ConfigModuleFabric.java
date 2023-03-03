package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.config.BNFConfigFabric;
import com.yungnickyoung.minecraft.betterfortresses.world.ItemFrameChances;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.InteractionResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigModuleFabric {
    public static final String CUSTOM_CONFIG_PATH = BetterFortressesCommon.MOD_ID;
    public static final String VERSION_PATH = "fabric-1_19_3";

    public static void init() {
        initCustomFiles();
        AutoConfig.register(BNFConfigFabric.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(BNFConfigFabric.class).registerSaveListener(ConfigModuleFabric::bakeConfig);
        AutoConfig.getConfigHolder(BNFConfigFabric.class).registerLoadListener(ConfigModuleFabric::bakeConfig);
        bakeConfig(AutoConfig.getConfigHolder(BNFConfigFabric.class).get());
    }

    private static InteractionResult bakeConfig(ConfigHolder<BNFConfigFabric> configHolder, BNFConfigFabric configFabric) {
        bakeConfig(configFabric);
        loadItemFramesJSON();
        return InteractionResult.SUCCESS;
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
        loadItemFramesJSON();
    }

    private static void createDirectory() {
        File parentDir = new File(FabricLoader.getInstance().getConfigDir().toString(), CUSTOM_CONFIG_PATH);
        File customConfigDir = new File(parentDir, VERSION_PATH);
        try {
            String filePath = customConfigDir.getCanonicalPath();
            if (customConfigDir.mkdirs()) {
                BetterFortressesCommon.LOGGER.info("Creating directory for additional Better Nether Fortresses configuration at {}", filePath);
            }
        } catch (IOException e) {
            BetterFortressesCommon.LOGGER.error("ERROR creating Better Nether Fortresses config directory: {}", e.toString());
        }
    }

    private static void createBaseReadMe() {
        Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            This directory is for a few additional options for YUNG's Better Nether Fortresses.
                            Options provided may vary by version.
                            This directory contains subdirectories for supported versions. The first time you run Better Nether Fortresses, a version subdirectory will be created if that version supports advanced options.
                            For example, the first time you use Better Nether Fortresses for MC 1.19.2 on Fabric, the 'fabric-1_19' subdirectory will be created in this folder.
                            If no subdirectory for your version is created, then that version probably does not support the additional options.
                            NOTE -- Most of this mod's config settings can be found in a config file outside this folder!
                            For example, on Fabric 1.19.2 the file is 'betterfortresses-fabric-1_19.toml'.
                            Also note that many of the structure's settings such as spawn rate & spawn conditions can only be modified via data pack.""";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterFortressesCommon.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            ######################################
                            #          itemframes.json          #
                            ######################################
                              This file contains ItemRandomizers describing the probability distribution of items in item frames.
                            Item frames only spawn in certain rooms and hallway pieces.
                            For information on ItemRandomizers, see the bottom of this README.
                            ######################################
                            #         ItemRandomizers           #
                            ######################################
                            Describes a set of items and the probability of each item being chosen.
                             - entries: An object where each entry's key is an item, and each value is that item's probability of being chosen.
                                  The total sum of all probabilities SHOULD NOT exceed 1.0!
                             - defaultItem: The item used for any leftover probability ranges.
                                  For example, if the total sum of all the probabilities of the entries is 0.6, then
                                  there is a 0.4 chance of the defaultItem being selected.
                            Here's an example ItemRandomizer:
                            {
                              "entries": {
                                "minecraft:cobblestone": 0.25,
                                "minecraft:air": 0.2,
                                "minecraft:stone_sword": 0.1
                              },
                              "defaultItem": "minecraft:iron_axe"
                            }
                            This randomizer has a 25% chance of returning cobblestone, 20% chance of choosing air,
                            10% chance of choosing a stone sword, and a 100 - (25 + 20 + 10) = 45% chance of choosing iron axe (since it's the default item).
                            """;

            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterFortressesCommon.LOGGER.error("Unable to create item frames README file!");
            }
        }
    }

    /**
     * If a JSON file already exists, it loads its contents into ItemFrameChances.
     * Otherwise, it creates a default JSON and from the default options in ItemFrameChances.
     */
    private static void loadItemFramesJSON() {
        Path jsonPath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "itemframes.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, ItemFrameChances.get());
            } catch (IOException e) {
                BetterFortressesCommon.LOGGER.error("Unable to create itemframes.json file: {}", e.toString());
            }
        } else {
            // If file already exists, load data into ItemFrameChances singleton instance
            if (!jsonFile.canRead()) {
                BetterFortressesCommon.LOGGER.error("Better Nether Fortresses itemframes.json file not readable! Using default configuration...");
                return;
            }

            try {
                ItemFrameChances.instance = JSON.loadObjectFromJsonFile(jsonPath, ItemFrameChances.class);
            } catch (IOException e) {
                BetterFortressesCommon.LOGGER.error("Error loading Better Nether Fortresses itemframes.json file: {}", e.toString());
                BetterFortressesCommon.LOGGER.error("Using default configuration...");
            }
        }
    }

    private static void bakeConfig(BNFConfigFabric configFabric) {
        BetterFortressesCommon.CONFIG.general.disableVanillaFortresses = configFabric.general.disableVanillaFortresses;
    }
}
