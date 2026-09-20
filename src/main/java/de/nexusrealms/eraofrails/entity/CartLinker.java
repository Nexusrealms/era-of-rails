package de.nexusrealms.eraofrails.entity;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public interface CartLinker {
    Codec<Optional<LazyCartReference>> MINECART_REFERENCE_CODEC = ExtraCodecs.optionalEmptyMap(LazyCartReference.CODEC);
    default Optional<CartLinker> getLinkedParent() {
        throw new AssertionError("Implemented in Mixin!");
    }

    default Optional<CartLinker> getLinkedChild() {
        throw new AssertionError("Implemented in Mixin!");
    }

    default void setLinkedParent(CartLinker cartLinker) {
        throw new AssertionError("Implemented in Mixin!");

    }

    default void setLinkedChild(CartLinker cartLinker) {
        throw new AssertionError("Implemented in Mixin!");

    }

    default AbstractMinecart asEntity() {
        throw new AssertionError("Implemented in Mixin!");
    }

}
