package de.nexusrealms.eraofrails.mixin.client;

import de.nexusrealms.eraofrails.client.CartLinkerRenderState;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecartEntityRenderState.class)
public abstract class MinecartEntityRenderStateMixin implements CartLinkerRenderState {
    @Nullable
    private Vec3d childPos;

    @Override
    public @Nullable Vec3d getLinkedChildPos() {
        return childPos;
    }

    @Override
    public void setLinkedChildPos(Vec3d pos) {
        childPos = pos;
    }
}
