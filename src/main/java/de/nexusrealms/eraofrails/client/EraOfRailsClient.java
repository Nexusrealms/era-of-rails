package de.nexusrealms.eraofrails.client;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;

public class EraOfRailsClient implements ClientModInitializer {
    public static final ResourceLocation COPPER_MINECART_TEXTURE = EraOfRails.id("textures/entity/copper_minecart.png");

    @Override
    public void onInitializeClient() {
        initBlockRenderLayers();
        initEntityRenderers();
    }
    private static void initBlockRenderLayers(){
        BlockRenderLayerMap.INSTANCE.putBlock(RailwaysBlocks.COPPER_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RailwaysBlocks.SWITCH_RAIL, RenderType.cutout());
        //BlockRenderLayerMap.putBlock(RailwaysBlocks.LOCKED_SWITCH_RAIL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RailwaysBlocks.INPUT_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RailwaysBlocks.HALT_RAIL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(RailwaysBlocks.CROSS_RAIL, RenderType.cutout());

    }
    private static void initEntityRenderers(){
        EntityRendererRegistry.register(RailwaysEntities.JUKEBOX_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.REDSTONE_BLOCK_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.INPUT_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));

        EntityRendererRegistry.register(RailwaysEntities.CHEST_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.CHEST_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.COMMAND_BLOCK_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.FURNACE_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.FURNACE_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.HOPPER_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.HOPPER_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.TNT_COPPER_MINECART, TntMinecartRenderer::new);
        EntityRendererRegistry.register(RailwaysEntities.SPAWNER_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.SPAWNER_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.JUKEBOX_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.INPUT_COPPER_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));

    }
}
