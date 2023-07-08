package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.environment.Temperature;
import cloud.lemonslice.silveroak.common.item.SilveroakRegistry;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import cloud.lemonslice.silveroak.mixin.IBiomeInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;
import static cloud.lemonslice.silveroak.client.gui.hud.OverlayHandler.originThermometerBar;

public class ThermometerBarOverlay implements HudRenderCallback
{
    private final static Identifier OVERLAY_BAR = new Identifier(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private float temp = 0;
    private float level = 0;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        int screenWidth = minecraft.getWindow().getScaledWidth();
        int screenHeight = minecraft.getWindow().getScaledHeight();
        ClientPlayerEntity clientPlayer = minecraft.player;
        if (clientPlayer != null && originThermometerBar)
        {
            if (!clientPlayer.getMainHandStack().isEmpty())
            {
                Item handed = clientPlayer.getMainHandStack().getItem();
                if (handed.equals(SilveroakRegistry.THERMOMETER))
                {
                    Biome biome = clientPlayer.getWorld().getBiome(clientPlayer.getBlockPos()).value();
                    float temp = ((IBiomeInvoker) (Object) biome).invokeComputeTemperature(clientPlayer.getBlockPos());

                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    level = temp;

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = getWidth(this.temp);
                    GuiHelper.drawLayer(drawContext.getMatrices(), offsetX + 1, offsetY + 1, new TexturePos(1, 10, width, HEIGHT - 2));
                    GuiHelper.drawLayer(drawContext.getMatrices(), offsetX, offsetY, new TexturePos(0, 14, WIDTH, HEIGHT));

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    public int getWidth(float temp)
    {
        Temperature t = Temperature.getTemperatureLevel(temp);
        if (temp <= 0)
        {
            return 0;
        }
        else if (temp <= Temperature.FREEZING.getMax())
        {
            return Math.round(5 * temp / 0.15F);
        }
        else if (temp > Temperature.HEAT.getMin())
        {
            temp -= t.getMin();
            int id = t.getId() - 1;
            float width = id * 5;
            width += 5 * temp / 0.35F;
            return Math.round(width);
        }
        else
        {
            temp -= t.getMin();
            int id = t.getId() - 1;
            float width = id * 5;
            width += 5 * temp / t.getWidth();
            return Math.round(width);
        }
    }

    public void onClientTick(ClientWorld client)
    {
        if (level - 0.0078125F > temp)
        {
            temp += 0.01F;
        }
        else if (level + 0.0078125F < temp)
        {
            temp -= 0.01F;
        }
    }
}
