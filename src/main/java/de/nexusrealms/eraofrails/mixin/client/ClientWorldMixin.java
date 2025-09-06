package de.nexusrealms.eraofrails.mixin.client;

import de.nexusrealms.eraofrails.client.CartJukeboxHandler;
import de.nexusrealms.eraofrails.client.CartJukeboxHandlerProvider;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements CartJukeboxHandlerProvider {
    @Unique
    private CartJukeboxHandler cartJukeboxHandler;
    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(ClientPlayNetworkHandler networkHandler,
                       ClientWorld.Properties properties,
                       RegistryKey<World> registryRef,
                       RegistryEntry<DimensionType> dimensionType,
                       int loadDistance,
                       int simulationDistance,
                       WorldRenderer worldRenderer,
                       boolean debugWorld,
                       long seed,
                       int seaLevel,
                       CallbackInfo ci){
        cartJukeboxHandler = new CartJukeboxHandler((ClientWorld) (Object) (this));
    }

    @Override
    public CartJukeboxHandler getHandler() {
        return cartJukeboxHandler;
    }
}
