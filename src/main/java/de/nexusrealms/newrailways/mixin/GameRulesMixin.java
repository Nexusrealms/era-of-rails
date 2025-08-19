package de.nexusrealms.newrailways.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/featuretoggle/FeatureSet;of(Lnet/minecraft/resource/featuretoggle/FeatureFlag;)Lnet/minecraft/resource/featuretoggle/FeatureSet;"))
    private static FeatureSet alwaysEnableSpeedRule(FeatureSet original){
        return FeatureSet.empty();
    }
}
