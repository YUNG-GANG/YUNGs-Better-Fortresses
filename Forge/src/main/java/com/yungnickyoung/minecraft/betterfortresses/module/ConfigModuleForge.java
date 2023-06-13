package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.config.BNFConfigForge;
import com.yungnickyoung.minecraft.betterfortresses.world.ItemFrameChances;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigModuleForge {
    public static final String CUSTOM_CONFIG_PATH = BetterFortressesCommon.MOD_ID;
    public static final String VERSION_PATH = "forge-1_20";

    public static void init() {
        initCustomFiles();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BNFConfigForge.SPEC, "betterfortresses-forge-1_20.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
        loadItemFramesJSON();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BNFConfigForge.SPEC) {
            bakeConfig();
            loadItemFramesJSON();
        }
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
        loadItemFramesJSON();
    }

    private static void createDirectory() {
        File parentDir = new File(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH);
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
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            This directory is for a few additional options for YUNG's Better Nether Fortresses.
                            Options provided may vary by version.
                            This directory contains subdirectories for supported versions. The first time you run Better Nether Fortresses, a version subdirectory will be created if that version supports advanced options.
                            For example, the first time you use Better Nether Fortresses for MC 1.19.2 on Forge, the 'forge-1_19' subdirectory will be created in this folder.
                            If no subdirectory for your version is created, then that version probably does not support the additional options.
                            NOTE -- Most of this mod's config settings can be found in a config file outside this folder!
                            For example, on Forge 1.19.2 the file is 'betterfortresses-forge-1_19.toml'.
                            Also note that many of the structure's settings such as spawn rate & spawn conditions can only be modified via data pack.""";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterFortressesCommon.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "README.txt");
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
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "itemframes.json");
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

    private static void bakeConfig() {
        BetterFortressesCommon.CONFIG.general.disableVanillaFortresses = BNFConfigForge.general.disableVanillaFortresses.get();
    }
}
