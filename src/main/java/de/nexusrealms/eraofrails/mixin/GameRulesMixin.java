package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.rule.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// TODO(Ravel): can not resolve target class GameRules
// TODO(Ravel): can not resolve target class GameRules
@Mixin(GameRules.class)
public abstract class GameRulesMixin {
    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/flag/FeatureFlagSet;of(Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static FeatureFlagSet alwaysEnableSpeedRule(FeatureFlagSet original){
        return FeatureFlagSet.of();
    }
}
