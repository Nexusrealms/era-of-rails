package de.nexusrealms.eraofrails.client;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class EraOfRailsClient implements ClientModInitializer {
    public static final Identifier COPPER_MINECART_TEXTURE = EraOfRails.id("textures/entity/copper_minecart.png");

    @Override
    public void onInitializeClient() {
        initBlockRenderLayers();
        initEntityRenderers();
    }
    private static void initBlockRenderLayers(){
        BlockRenderLayerMap.putBlock(RailwaysBlocks.COPPER_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(RailwaysBlocks.SWITCH_RAIL, BlockRenderLayer.CUTOUT);
        //BlockRenderLayerMap.putBlock(RailwaysBlocks.LOCKED_SWITCH_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(RailwaysBlocks.INPUT_RAIL, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(RailwaysBlocks.HALT_RAIL, BlockRenderLayer.CUTOUT);
    }
    private static void initEntityRenderers(){
        EntityRendererRegistry.register(RailwaysEntities.JUKEBOX_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.REDSTONE_BLOCK_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.INPUT_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));

        EntityRendererRegistry.register(RailwaysEntities.CHEST_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.CHEST_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.COMMAND_BLOCK_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.FURNACE_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.FURNACE_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.HOPPER_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.HOPPER_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.TNT_COPPER_MINECART, TntMinecartEntityRenderer::new);
        EntityRendererRegistry.register(RailwaysEntities.SPAWNER_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.SPAWNER_MINECART));
        EntityRendererRegistry.register(RailwaysEntities.JUKEBOX_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));
        EntityRendererRegistry.register(RailwaysEntities.INPUT_COPPER_MINECART, ctx -> new MinecartEntityRenderer(ctx, EntityModelLayers.MINECART));

    }
}
