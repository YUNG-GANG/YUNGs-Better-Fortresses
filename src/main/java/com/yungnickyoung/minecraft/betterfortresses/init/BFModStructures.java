package com.yungnickyoung.minecraft.betterfortresses.init;

import com.google.common.collect.ImmutableMap;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortresses;
import com.yungnickyoung.minecraft.betterfortresses.config.BFConfig;
import com.yungnickyoung.minecraft.betterfortresses.mixin.ChunkGeneratorAccessor;
import com.yungnickyoung.minecraft.betterfortresses.world.BetterFortressStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BFModStructures {
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterFortresses.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> BETTER_FORTRESS = DEFERRED_REGISTRY.register("fortress", BetterFortressStructure::new);

    public static void init() {
        // Register our deferred registry
        DEFERRED_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BFModStructures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(BFModStructures::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(BFModStructures::onBiomeLoad);
    }

    /**
     * Set up Better Fortresses structures.
     */
    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add fortress to the structure map
            Structure.NAME_STRUCTURE_BIMAP.put("Better Fortress".toLowerCase(Locale.ROOT), BETTER_FORTRESS.get());

            // Add structure + spacing settings to default dimension structures.
            // Note that we make a similar change in the WorldEvent.Load handler
            // as a safety for custom dimension support.
            DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.field_236191_b_)
                    .put(BETTER_FORTRESS.get(), new StructureSeparationSettings(10, 5, 597039957))
                    .build();

            // Register the configured structure features
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterFortresses.MOD_ID, "fortress"), BFModConfiguredStructures.CONFIGURED_BETTER_FORTRESS);

            // Add structure features to this to prevent any issues if other mods' custom ChunkGenerators use FlatGenerationSettings.STRUCTURES
            FlatGenerationSettings.STRUCTURES.put(BETTER_FORTRESS.get(), BFModConfiguredStructures.CONFIGURED_BETTER_FORTRESS);

            // Register separation settings for mods that might need it, like Terraforged
            WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
                Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();

                // Precaution in case a mod makes the structure map immutable like data packs do
                if (structureMap instanceof ImmutableMap) {
                    Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                    tempMap.put(BETTER_FORTRESS.get(), DimensionStructuresSettings.field_236191_b_.get(BETTER_FORTRESS.get()));
                    settings.getValue().getStructures().field_236193_d_ = tempMap;
                } else {
                    structureMap.put(BETTER_FORTRESS.get(), DimensionStructuresSettings.field_236191_b_.get(BETTER_FORTRESS.get()));
                }
            });
        });
    }

    /**
     * Adds the appropriate structure to each biome as it loads in.
     */
    private static void onBiomeLoad(BiomeLoadingEvent event) {
        // Ensure non-null biome name.
        // This should never happen, but we check to prevent a NPE just in case.
        if (event.getName() == null) {
            BetterFortresses.LOGGER.error("Missing biome name! This is a critical error and should not occur.");
            BetterFortresses.LOGGER.error("Try running the game with the Blame mod for a more detailed breakdown.");
            BetterFortresses.LOGGER.error("Please report this issue!");
            return;
        }

        // Remove vanilla fortresses
        event.getGeneration().getStructures().removeIf(supplier -> supplier.get().field_236268_b_ == Structure.FORTRESS);

        // Don't spawn in non-Nether biomes
        if (event.getCategory() != Biome.Category.NETHER) {
            return;
        }

        // Don't spawn in blacklisted/not whitelisted biomes
        boolean isBiomeInList = BetterFortresses.blacklistedBiomes.contains(event.getName().toString());
        if ((BFConfig.isBlacklist.get() && isBiomeInList) || (!BFConfig.isBlacklist.get()) && !isBiomeInList) {
            return;
        }

        // Add Better Fortress to biome generation settings
        event.getGeneration().getStructures().add(() -> BFModConfiguredStructures.CONFIGURED_BETTER_FORTRESS);
    }

    private static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
            // Credits to TelepathicGrunt for this.
            try {
                ResourceLocation chunkGenResourceLocation = Registry.CHUNK_GENERATOR_CODEC.getKey(((ChunkGeneratorAccessor) serverWorld.getChunkProvider().generator).betterfortresses_getCodec());
                if (chunkGenResourceLocation != null && chunkGenResourceLocation.getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                BetterFortresses.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            // We use a temp map because some mods handle immutable maps
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());

            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                // Don't spawn in superflat worlds
                tempMap.keySet().remove(BFModStructures.BETTER_FORTRESS.get());
            } else {
                // Otherwise, we add the structure to the world
                tempMap.put(BFModStructures.BETTER_FORTRESS.get(), DimensionStructuresSettings.field_236191_b_.get(BFModStructures.BETTER_FORTRESS.get()));
            }

            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
