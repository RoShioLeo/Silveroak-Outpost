package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class RainGaugeBarRenderer extends GuiComponent
{
    private final static ResourceLocation OVERLAY_BAR = new ResourceLocation(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float rainfall = 0;
    private static float level = 0;

    public void renderStatusBar(PoseStack poseStack, int screenWidth, int screenHeight, float rainfall)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OVERLAY_BAR);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        level = (int) (rainfall * 100) / 100.0F;

        int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

        int width = getWidth(RainGaugeBarRenderer.rainfall);
        GuiHelper.drawLayer(poseStack, offsetX + 1, offsetY + 1, new TexturePos(1, 0, width, HEIGHT - 2));
        GuiHelper.drawLayer(poseStack, offsetX, offsetY, new TexturePos(0, 4, WIDTH, HEIGHT));

        RenderSystem.disableBlend();
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

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
        {
            if (level > rainfall)
            {
                rainfall += 0.01F;
            }
            else if (level < rainfall)
            {
                rainfall -= 0.01F;
            }
        }
    }
}
