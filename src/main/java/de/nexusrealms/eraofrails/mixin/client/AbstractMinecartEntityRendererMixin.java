package de.nexusrealms.eraofrails.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.client.EraOfRailsClient;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO(Ravel): can not resolve target class AbstractMinecartEntityRenderer
// TODO(Ravel): can not resolve target class AbstractMinecartEntityRenderer
@Mixin(AbstractMinecartEntityRenderer.class)
public abstract class AbstractMinecartEntityRendererMixin <T extends AbstractMinecart, S extends MinecartEntityRenderState> extends EntityRenderer<T, S> {
    protected AbstractMinecartEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation CHAIN = EraOfRails.id("textures/entity/linking_chain.png");

    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/AbstractMinecartEntityRenderer;TEXTURE:Lnet/minecraft/resources/ResourceLocation;"))
    public ResourceLocation getCopperTexture(ResourceLocation original, MinecartEntityRenderState state) {
        if (state.entityType.isIn(RailwaysEntities.Tags.COPPER_MINECARTS)) {
            return EraOfRailsClient.COPPER_MINECART_TEXTURE;
        }
        return original;
    }

    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;F)V", at = @At("TAIL"))
    public void addToRenderState(T abstractMinecartEntity, S minecartEntityRenderState, float f, CallbackInfo ci) {
        minecartEntityRenderState.setLinkedChildPos(abstractMinecartEntity.getLinkedChild().map(cartLinker -> cartLinker.asEntity().getEntityPos()).orElse(null));
    }

    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/MinecartEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("TAIL"))
    public void renderLink(S minecartEntityRenderState, PoseStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        Vec3 origin = new Vec3(minecartEntityRenderState.x, minecartEntityRenderState.y, minecartEntityRenderState.z);
        Vec3 child = minecartEntityRenderState.getLinkedChildPos();
        if (child != null) {
            int light = LightTexture.FULL_BRIGHT;
            float length = (float) origin.distanceTo(child);
            double chainPitch = Math.toDegrees(Math.atan(child.y - origin.y));
            double chainYaw = -Math.toDegrees(Mth.atan2(child.z - origin.z, child.x - origin.x));
            matrices.pushPose();
            matrices.mulPose(Axis.YN.rotationDegrees((float) (270 - chainYaw)));
            matrices.mulPose(Axis.XP.rotationDegrees((float) -chainPitch));
            orderedRenderCommandQueue.submitCustom(matrices, ItemBlockRenderTypes.entitySmoothCutout(CHAIN), (entry, vertices) -> {
                float x1 = -0.2f;
                float y = 0.05f;
                float x2 = 0.2f;
                float minU = 0.25f;
                float maxU = 0.75f;
                float minV = 0f;
                float maxV = length / 8;
                Matrix4f matrix4f = entry.getPositionMatrix();
                vertices.vertex(matrix4f, x1, y, 0f).color(0xffffffff).texture(minU, minV).overlay(OverlayTexture.NO_OVERLAY).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x1, y, length).color(0xffffffff).texture(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x2, y, length).color(0xffffffff).texture(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).light(light).normal(entry, 0f, -1f, 0f);
                vertices.vertex(matrix4f, x2, y, 0f).color(0xffffffff).texture(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).light(light).normal(entry, 0f, -1f, 0f);
            });
            matrices.popPose();
        }
    }
}

