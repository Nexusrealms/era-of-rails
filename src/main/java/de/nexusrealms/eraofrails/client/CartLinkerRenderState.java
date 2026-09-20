package de.nexusrealms.eraofrails.client;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface CartLinkerRenderState {
    default void setLinkedChildPos(Vec3 pos){
        throw new AssertionError("Implemented in Mixin!");
    }
    @Nullable
    default Vec3 getLinkedChildPos(){
        throw new AssertionError("Implemented in Mixin!");
    }
}
