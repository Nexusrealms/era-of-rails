package de.nexusrealms.eraofrails.mixin.client;

import de.nexusrealms.eraofrails.client.CartLinkerRenderState;
import net.minecraft.client.render.entity.state.MinecartEntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

// TODO(Ravel): can not resolve target class MinecartEntityRenderState
// TODO(Ravel): can not resolve target class MinecartEntityRenderState
@Mixin(MinecartEntityRenderState.class)
public abstract class MinecartEntityRenderStateMixin implements CartLinkerRenderState {
    @Nullable
    private Vec3 childPos;

    @Override
    public @Nullable Vec3 getLinkedChildPos() {
        return childPos;
    }

    @Override
    public void setLinkedChildPos(Vec3 pos) {
        childPos = pos;
    }
}
