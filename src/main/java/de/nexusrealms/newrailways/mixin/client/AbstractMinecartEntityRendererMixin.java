package de.nexusrealms.newrailways.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.nexusrealms.newrailways.client.NewRailwaysClient;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecartEntityRenderer.class)
public abstract class AbstractMinecartEntityRendererMixin <T extends AbstractMinecartEntity, S extends MinecartEntityRenderState> extends EntityRenderer<T, S> {
    protected AbstractMinecartEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/AbstractMinecartEntityRenderer;TEXTURE:Lnet/minecraft/util/Identifier;"))
    public Identifier getCopperTexture(Identifier original, S minecartEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i){
        if (minecartEntityRenderState.entityType.isIn(RailwaysEntities.Tags.COPPER_MINECARTS)){
            return NewRailwaysClient.COPPER_MINECART_TEXTURE;
        }
        return original;
    }
}
