package de.nexusrealms.eraofrails.client;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.entity.types.InputMinecartEntity;
import de.nexusrealms.eraofrails.network.SetInputCartDataPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class InputCartScreen extends Screen {
    private static final ResourceLocation BACKGROUND = EraOfRails.id("textures/gui/input_cart_screen_background.png");
    private static final ResourceLocation DISABLED = EraOfRails.id("textures/gui/input_switch_inactive.png");
    private static final ResourceLocation DISABLED_ON = EraOfRails.id("textures/gui/input_switch_inactive_on.png");
    private static final ResourceLocation SWITCH = EraOfRails.id("textures/gui/input_switch.png");
    private static final ResourceLocation SWITCH_ON = EraOfRails.id("textures/gui/input_switch_on.png");
    private static final ResourceLocation SWITCH_NEXT = EraOfRails.id("textures/gui/input_switch_next.png");
    private static final ResourceLocation SWITCH_ON_NEXT = EraOfRails.id("textures/gui/input_switch_on_next.png");
    private int originX, originY, switchOriginX, switchOriginY;
    private final InputMinecartEntity minecart;

    public InputCartScreen(InputMinecartEntity minecart) {
        super(Component.translatable(Blocks.STRUCTURE_BLOCK.getDescriptionId()));
        this.minecart = minecart;

    }

    @Override
    protected void init() {
        originX = (width / 2) - 128;
        originY = (height / 2) - 128;
        switchOriginX = originX + 24;
        switchOriginY = originY + 24;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks);

        context.blit(BACKGROUND, originX, originY, 0, 0, 256, 256, 256, 256);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        for (int i = 0; i < 64; i++) {
            renderSwitch(context, i);
        }
    }
    public void renderSwitch(GuiGraphics context, int i){
        int x = switchOriginX + (26 * (i % 8));
        int y = switchOriginY + (26 * (i / 8));
        context.blit(getSwitchTexture(i), x, y, 0, 0, 26, 26, 26, 26);
    }
    private ResourceLocation getSwitchTexture(int i){
        boolean isDisabled = i >= minecart.getActiveInsts();
        boolean isActive = ((minecart.getSequence() >> i) & 1) != 0;
        if(isDisabled){
            return isActive ? DISABLED_ON : DISABLED;
        } else {
            boolean isNext = i == minecart.getNextInst();
            return isNext ? (isActive ? SWITCH_ON_NEXT : SWITCH_NEXT) : (isActive ? SWITCH_ON : SWITCH);
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(isWithinSwitchBounds(mouseX, mouseY)){
            double xOffset = mouseX - switchOriginX;
            double yOffset = mouseY - switchOriginY;
            int switchX = (int) Math.floor(xOffset / 26.0);
            int switchY = (int) Math.floor(yOffset / 26.0);
            int i = 8 * switchY + switchX;
            if(button == GLFW.GLFW_MOUSE_BUTTON_1){
                long newSequence = minecart.getSequence() ^ (1L << i);
                ClientPlayNetworking.send(new SetInputCartDataPacket(minecart.getId(), Optional.of(newSequence), Optional.empty()));
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_2){
                int newActiveInsts = i + 1;
                ClientPlayNetworking.send(new SetInputCartDataPacket(minecart.getId(), Optional.empty(), Optional.of(newActiveInsts)));
            }
            minecraft.player.playSound(SoundEvents.LEVER_CLICK, 1f, 1f);

        }
        return false;
    }

    private boolean isWithinSwitchBounds(double x, double y){
        return x > switchOriginX && x < switchOriginX + 8 * 26 && y > switchOriginY && y < switchOriginY + 8 * 26;
    }
}
