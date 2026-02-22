package de.nexusrealms.eraofrails.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public record ChainRenderer(float length, int light) implements OrderedRenderCommandQueue.Custom {

    @Override
    public void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer) {

    }
}
