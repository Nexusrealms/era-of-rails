package de.nexusrealms.newrailways.mixin.client;

import de.nexusrealms.newrailways.client.CartJukeboxHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
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
    @Inject(method = "syncWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/WorldEventHandler;processWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true)
    public void handleCartEvent(Entity source, int eventId, BlockPos pos, int data, CallbackInfo ci){
        if(source != null && (eventId == 1810 || eventId == 1811)){
            if(cartJukeboxHandler.handleCartJukeboxWorldEvent(source, data, eventId == 1811)){
                ci.cancel();
            }
        }
    }
}
