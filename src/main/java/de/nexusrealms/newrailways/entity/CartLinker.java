package de.nexusrealms.newrailways.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public interface CartLinker {
    Codec<Optional<LazyEntityReference<AbstractMinecartEntity>>> MINECART_REFERENCE_CODEC = Codecs.optional(LazyEntityReference.createCodec());
    default Optional<CartLinker> getLinkedParent() {
        throw new UnsupportedOperationException("Implemented in Mixin!");
    }

    default Optional<CartLinker> getLinkedChild() {
        throw new UnsupportedOperationException("Implemented in Mixin!");
    }

    default void setLinkedParent(CartLinker cartLinker) {
        throw new UnsupportedOperationException("Implemented in Mixin!");

    }

    default void setLinkedChild(CartLinker cartLinker) {
        throw new UnsupportedOperationException("Implemented in Mixin!");

    }

    default AbstractMinecartEntity asEntity() {
        throw new UnsupportedOperationException("Implemented in Mixin!");
    }

}
