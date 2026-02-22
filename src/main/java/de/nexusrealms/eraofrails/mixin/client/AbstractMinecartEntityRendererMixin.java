package de.nexusrealms.eraofrails.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.client.EraOfRailsClient;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntityRenderer.class)
public abstract class AbstractMinecartEntityRendererMixin <T extends AbstractMinecartEntity, S extends MinecartEntityRenderState> extends EntityRenderer<T, S> {
    protected AbstractMinecartEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    private static final Identifier CHAIN = EraOfRails.id("textures/entity/linking_chain.png");

    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/AbstractMinecartEntityRenderer;TEXTURE:Lnet/minecraft/util/Identifier;"))
    public Identifier getCopperTexture(Identifier original, MinecartEntityRenderState state) {
        if (state.entityType.isIn(RailwaysEntities.Tags.COPPER_MINECARTS)) {
            return EraOfRailsClient.COPPER_MINECART_TEXTURE;
        }
        return original;
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;F)V", at = @At("TAIL"))
    public void addToRenderState(T abstractMinecartEntity, S minecartEntityRenderState, float f, CallbackInfo ci) {
        minecartEntityRenderState.setLinkedChildPos(abstractMinecartEntity.getLinkedChild().map(cartLinker -> cartLinker.asEntity().getEntityPos()).orElse(null));
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("TAIL"))
    public void renderLink(S minecartEntityRenderState, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        Vec3d origin = new Vec3d(minecartEntityRenderState.x, minecartEntityRenderState.y, minecartEntityRenderState.z);
        Vec3d child = minecartEntityRenderState.getLinkedChildPos();
        if (child != null) {
            int light = LightmapTextureManager.MAX_LIGHT_COORDINATE;
            float length = (float) origin.distanceTo(child);
            double chainPitch = Math.toDegrees(Math.atan(child.y - origin.y));
            double chainYaw = -Math.toDegrees(MathHelper.atan2(child.z - origin.z, child.x - origin.x));
            matrices.push();
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) (270 - chainYaw)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) -chainPitch));
            orderedRenderCommandQueue.submitCustom(matrices, RenderLayers.entitySmoothCutout(CHAIN), (entry, vertices) -> {
                float x1 = -0.2f;
                float y = 0.05f;
                float x2 = 0.2f;
                float minU = 0.25f;
                float maxU = 0.75f;
                float minV = 0f;
                float maxV = length / 8;
                Matrix4f matrix4f = entry.getPositionMatrix();
                vertices.vertex(matrix4f, x1, y, 0f).color(0xffffffff).texture(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x1, y, length).color(0xffffffff).texture(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x2, y, length).color(0xffffffff).texture(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x2, y, 0f).color(0xffffffff).texture(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0f, -1f, 0f);
            });
        }
    }
}

