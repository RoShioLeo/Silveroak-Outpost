package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class RainGaugeBarOverlay implements IGuiOverlay
{
    private final static ResourceLocation OVERLAY_BAR = new ResourceLocation(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float rainfall = 0;
    private static float level = 0;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight)
    {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null)
        {
            if (!clientPlayer.getMainHandItem().isEmpty())
            {
                Item handed = clientPlayer.getMainHandItem().getItem();
                if (handed.equals(SilveroakItemsRegistry.RAIN_GAUGE.get()))
                {
                    Biome biome = clientPlayer.level().getBiome(clientPlayer.blockPosition()).value();
                    float rainfall = biome.getModifiedClimateSettings().downfall();

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    level = rainfall;

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = getWidth(RainGaugeBarOverlay.rainfall);
                    GuiHelper.drawLayer(guiGraphics, offsetX + 1, offsetY + 1, OVERLAY_BAR, new TexturePos(1, 0, width, HEIGHT - 2));
                    GuiHelper.drawLayer(guiGraphics, offsetX, offsetY, OVERLAY_BAR, new TexturePos(0, 4, WIDTH, HEIGHT));

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

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
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
}
