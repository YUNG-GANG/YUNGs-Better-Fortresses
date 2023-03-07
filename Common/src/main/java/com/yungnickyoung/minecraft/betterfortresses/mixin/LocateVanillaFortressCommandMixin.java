package com.yungnickyoung.minecraft.betterfortresses.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Overrides behavior of /locate structure fortress.
 */
@Mixin(LocateCommand.class)
public class LocateVanillaFortressCommandMixin {
    private static final SimpleCommandExceptionType OLD_FORTRESS_EXCEPTION =
            new SimpleCommandExceptionType(new TextComponent("Use /locate betterfortresses:fortress instead!"));

    @Inject(method = "locate", at = @At(value = "HEAD"))
    private static void betterfortresses_overrideLocateVanillaFortress(CommandSourceStack cmdSource,
                                                                       ResourceOrTagLocationArgument.Result<ConfiguredStructureFeature<?, ?>> result,
                                                                       CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        Optional<ResourceKey<ConfiguredStructureFeature<?, ?>>> optional = result.unwrap().left();
        if (BetterFortressesCommon.CONFIG.general.disableVanillaFortresses && optional.isPresent() && optional.get().location().equals(new ResourceLocation("fortress"))) {
            throw OLD_FORTRESS_EXCEPTION.create();
        }
    }
}
