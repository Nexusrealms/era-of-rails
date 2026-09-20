package de.nexusrealms.eraofrails.mixin.client;

import de.nexusrealms.eraofrails.client.CartJukeboxHandler;
import de.nexusrealms.eraofrails.client.CartJukeboxHandlerProvider;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements CartJukeboxHandlerProvider {
    @Unique
    private CartJukeboxHandler cartJukeboxHandler;
    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(ClientPacketListener networkHandler,
                       ClientLevel.ClientLevelData properties,
                       ResourceKey<Level> registryRef,
                       Holder<DimensionType> dimensionType,
                       int loadDistance,
                       int simulationDistance,
                       LevelRenderer worldRenderer,
                       boolean debugWorld,
                       long seed,
                       int seaLevel,
                       CallbackInfo ci){
        cartJukeboxHandler = new CartJukeboxHandler((ClientLevel) (Object) (this));
    }

    @Override
    public CartJukeboxHandler getHandler() {
        return cartJukeboxHandler;
    }
}
