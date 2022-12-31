package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.environment.Humidity;
import cloud.lemonslice.silveroak.common.item.SilveroakRegistry;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public class HygrometerBarOverlay extends DrawableHelper implements HudRenderCallback
{
    private final static Identifier OVERLAY_BAR = new Identifier(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float humidity = 0;
    private static int level = 0;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        int screenWidth = minecraft.getWindow().getScaledWidth();
        int screenHeight = minecraft.getWindow().getScaledHeight();
        ClientPlayerEntity clientPlayer = minecraft.player;
        if (clientPlayer != null)
        {
            if (!clientPlayer.getMainHandStack().isEmpty())
            {
                Item handed = clientPlayer.getMainHandStack().getItem();
                if (handed.equals(SilveroakRegistry.HYGROMETER))
                {
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    Biome biome = clientPlayer.getWorld().getBiome(clientPlayer.getBlockPos()).value();
                    level = Humidity.getHumid(biome, clientPlayer.getBlockPos()).getId();

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = Math.round(humidity * 6);
                    GuiHelper.drawLayerBySize(matrixStack, offsetX + 1, offsetY + 1, new TexturePos(1, 20, width, HEIGHT - 2), 256, 256);
                    GuiHelper.drawLayerBySize(matrixStack, offsetX, offsetY, new TexturePos(0, 24, WIDTH, HEIGHT), 256, 256);

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    public static void onClientTick(ClientWorld client)
    {
        if (level - 0.0078125F > humidity)
        {
            humidity += 0.05F;
        }
        else if (level + 0.0078125F < humidity)
        {
            humidity -= 0.05F;
        }
    }
}
