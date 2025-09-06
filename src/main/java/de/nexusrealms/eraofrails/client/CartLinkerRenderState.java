package de.nexusrealms.eraofrails.client;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface CartLinkerRenderState {
    default void setLinkedChildPos(Vec3d pos){
        throw new AssertionError("Implemented in Mixin!");
    }
    @Nullable
    default Vec3d getLinkedChildPos(){
        throw new AssertionError("Implemented in Mixin!");
    }
}
