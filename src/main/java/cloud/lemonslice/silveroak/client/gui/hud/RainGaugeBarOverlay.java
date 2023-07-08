package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.inter.IBiomeDownfallAccess;
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

public class RainGaugeBarOverlay extends DrawableHelper implements HudRenderCallback
{
    private final static Identifier OVERLAY_BAR = new Identifier(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float rainfall = 0;
    private static float level = 0;

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
                if (handed.equals(SilveroakRegistry.RAIN_GAUGE))
                {
                    Biome biome = clientPlayer.getWorld().getBiome(clientPlayer.getBlockPos()).value();
                    float rainfall = ((IBiomeDownfallAccess) (Object) biome).getDownfall();

                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    level = rainfall;

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = getWidth(RainGaugeBarOverlay.rainfall);
                    GuiHelper.drawLayer(matrixStack, offsetX + 1, offsetY + 1, new TexturePos(1, 0, width, HEIGHT - 2));
                    GuiHelper.drawLayer(matrixStack, offsetX, offsetY, new TexturePos(0, 4, WIDTH, HEIGHT));

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    public int getWidth(float rainfall)
    {
        int width = 0;

        if (rainfall <= 0)
        {
            return width;
        }
        else if (rainfall <= 1.0F)
        {
            return Math.round((WIDTH - 2) * rainfall);
        }
        else
        {
            return WIDTH - 2;
        }
    }

    public static void onClientTick(ClientWorld client)
    {
        if (level - 0.0078125F > rainfall)
        {
            rainfall += 0.01F;
        }
        else if (level + 0.0078125F < rainfall)
        {
            rainfall -= 0.01F;
        }
    }
}
