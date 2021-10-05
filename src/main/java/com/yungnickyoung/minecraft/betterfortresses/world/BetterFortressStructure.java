package com.yungnickyoung.minecraft.betterfortresses.world;

import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortresses;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BetterFortressStructure extends Structure<NoFeatureConfig> {
    public BetterFortressStructure() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.UNDERGROUND_DECORATION;
    }

    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
        new MobSpawnInfo.Spawners(EntityType.BLAZE, 10, 2, 3),
        new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4),
        new MobSpawnInfo.Spawners(EntityType.WITHER_SKELETON, 8, 5, 5),
        new MobSpawnInfo.Spawners(EntityType.SKELETON, 2, 5, 5),
        new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 3, 4, 4)
    );

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            // Generate from the center of the chunk
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int minY = 48;
            int maxY = 70;
//            int y = rand.nextInt(maxY - minY) + minY;
            int y = 35;

            BlockPos blockpos = new BlockPos(x, y, z);
            YungJigsawConfig jigsawConfig = new YungJigsawConfig(
                () -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY).getOrDefault(new ResourceLocation(BetterFortresses.MOD_ID, "battle_bridge_start")),
                40
            );

            // Generate the structure
            YungJigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                jigsawConfig,
                chunkGenerator,
                templateManagerIn,
                blockpos,
                this.components,
                this.rand,
                false,
                false
            );

            // Set the bounds of the structure once it's assembled
            this.recalculateStructureSize();

            // Debug log the coordinates of the center starting piece.
            BetterFortresses.LOGGER.debug("Better Nether Fortress at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }
    }
}
