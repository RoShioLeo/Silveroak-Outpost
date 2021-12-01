package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.environment.Humidity;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
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
public class HygrometerBarRenderer extends GuiComponent
{
    private final static ResourceLocation OVERLAY_BAR = new ResourceLocation(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float humidity = 0;
    private static int level = 0;

    private final Minecraft mc;

    public HygrometerBarRenderer(Minecraft mc)
    {
        this.mc = mc;
    }

    public void renderStatusBar(PoseStack poseStack, int screenWidth, int screenHeight, float temperature, float rainfall)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OVERLAY_BAR);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        level = Humidity.getHumid(rainfall, temperature).getId();

        int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

        int width = (int) (humidity * 6);
        GuiHelper.drawLayerBySize(poseStack, offsetX + 1, offsetY + 1, new TexturePos(1, 20, width, HEIGHT - 2), 256, 256);
        GuiHelper.drawLayerBySize(poseStack, offsetX, offsetY, new TexturePos(0, 24, WIDTH, HEIGHT), 256, 256);

        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
        {
            if (level > humidity)
            {
                humidity += 0.05F;
            }
            else if (level < humidity)
            {
                humidity -= 0.05F;
            }
        }
    }
}
