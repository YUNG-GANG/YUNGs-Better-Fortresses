package com.yungnickyoung.minecraft.betterfortresses.world;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.module.StructureProcessorTypeModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Fills item frames with a random item.
 * The type of random item depends on the item already in the frame.
 */
@ParametersAreNonnullByDefault
public class ItemFrameProcessor extends StructureProcessor {
    public static final ItemFrameProcessor INSTANCE = new ItemFrameProcessor();
    public static final MapCodec<StructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader levelReader,
                                                               BlockPos structurePiecePos,
                                                               StructureTemplate.StructureEntityInfo localEntityInfo,
                                                               StructureTemplate.StructureEntityInfo globalEntityInfo,
                                                               StructurePlaceSettings structurePlaceSettings,
                                                               StructureTemplate template) {
        if (globalEntityInfo.nbt.getString("id").equals("minecraft:item_frame")) {
            RandomSource random = structurePlaceSettings.getRandom(globalEntityInfo.blockPos);

            // Determine which pool we are grabbing from
            String item;
            try {
                item = globalEntityInfo.nbt.getCompound("Item").get("id").toString();
            } catch (Exception e) {
                BetterFortressesCommon.LOGGER.info("Unable to randomize item frame at {}", globalEntityInfo.blockPos);
                return globalEntityInfo;
            }

            // Set the item in the item frame's NBT
            CompoundTag newNBT = globalEntityInfo.nbt.copy();
            switch (item) {
                case "\"minecraft:stone_sword\"": { // Weapon pool
                    String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getWeaponItem(random)).toString();
                    if (randomItemString.equals("minecraft:air")) {
                        return null;
                    }
                    newNBT.getCompound("Item").putString("id", randomItemString);
                    break;
                }
                case "\"minecraft:iron_ingot\"": { // Loot pool
                    String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getLootItem(random)).toString();
                    if (randomItemString.equals("minecraft:air")) {
                        return null;
                    }
                    newNBT.getCompound("Item").putString("id", randomItemString);
                    break;
                }
                case "\"minecraft:cobweb\"": { // Study pool
                    String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getStudyItem(random)).toString();
                    if (randomItemString.equals("minecraft:air")) {
                        return null;
                    }
                    // Special case: enchanted books
                    if (randomItemString.equals("minecraft:enchanted_book")) {
                        // Choose enchantment and level
                        float f = random.nextFloat();
                        String enchantment;
                        if (f < 0.2f) enchantment = "minecraft:fire_aspect";
                        else if (f < 0.4f) enchantment = "minecraft:fire_protection";
                        else if (f < 0.6f) enchantment = "minecraft:flame";
                        else if (f < 0.8f) enchantment = "minecraft:smite";
                        else enchantment = "minecraft:binding_curse";
                        int lvl;

                        // Flame and Binding Curse can only be level 1
                        if (enchantment.equals("minecraft:flame") || enchantment.equals("minecraft:binding_curse")) {
                            lvl = 1;
                        } else {
                            lvl = random.nextFloat() < 0.75f ? 1 : 2;
                        }

                        CompoundTag componentsTag = newNBT.getCompound("Item").getCompound("components");
                        componentsTag.put("minecraft:stored_enchantments", Util.make(new CompoundTag(), enchantmentsTag -> {
                            enchantmentsTag.put("levels", Util.make(new CompoundTag(), levelsTag -> {
                                levelsTag.putInt(enchantment, lvl);
                            }));
                        }));
                        newNBT.getCompound("Item").put("components", componentsTag);
                    }
                    newNBT.getCompound("Item").putString("id", randomItemString);
                    break;
                }
                case "\"minecraft:apple\"": { // Mess Hall pool
                    String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getMessHallItem(random)).toString();
                    if (randomItemString.equals("minecraft:air")) {
                        return null;
                    }
                    newNBT.getCompound("Item").putString("id", randomItemString);
                    break;
                }
                case "\"minecraft:nether_wart\"": { // Alchemy ingredients pool
                    String randomItemString = BuiltInRegistries.ITEM.getKey(ItemFrameChances.get().getAlchemyItem(random)).toString();
                    if (randomItemString.equals("minecraft:air")) {
                        return null;
                    }
                    newNBT.getCompound("Item").putString("id", randomItemString);
                    break;
                }
                case "\"minecraft:glowstone_dust\"":  // In alchemy room. 50% chance of blaze powder
                    if (random.nextBoolean()) {
                        newNBT.getCompound("Item").putString("id", "minecraft:blaze_powder");
                    } else {
                        return null;
                    }
                    break;
            }

            // Required to suppress dumb log spam
            newNBT.putInt("TileX", globalEntityInfo.blockPos.getX());
            newNBT.putInt("TileY", globalEntityInfo.blockPos.getY());
            newNBT.putInt("TileZ", globalEntityInfo.blockPos.getZ());

            // Randomize rotation
            int minRotation = item.equals("\"minecraft:chiseled_nether_bricks\"") ? 1 : 0; // Special case for puzzle room
            int randomRotation = Mth.randomBetweenInclusive(random, minRotation, 7);
            newNBT.putByte("ItemRotation", (byte) randomRotation);

            globalEntityInfo = new StructureTemplate.StructureEntityInfo(globalEntityInfo.pos, globalEntityInfo.blockPos, newNBT);
        }
        return globalEntityInfo;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.ITEM_FRAME_PROCESSOR;
    }
}
