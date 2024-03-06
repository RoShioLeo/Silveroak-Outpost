package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.environment.Temperature;
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
import static cloud.lemonslice.silveroak.client.gui.hud.OverlayEventHandler.originThermometerBar;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class ThermometerBarOverlay implements IGuiOverlay
{
    private final static ResourceLocation OVERLAY_BAR = new ResourceLocation(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float temp = 0;
    private static float level = 0;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight)
    {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null && originThermometerBar)
        {
            if (!clientPlayer.getMainHandItem().isEmpty())
            {
                Item handed = clientPlayer.getMainHandItem().getItem();
                if (handed.equals(SilveroakItemsRegistry.THERMOMETER.get()))
                {
                    Biome biome = clientPlayer.level().getBiome(clientPlayer.blockPosition()).value();
                    float temp = biome.getHeightAdjustedTemperature(clientPlayer.blockPosition());

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    level = temp;

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = getWidth(ThermometerBarOverlay.temp);
                    GuiHelper.drawLayer(guiGraphics, offsetX + 1, offsetY + 1, OVERLAY_BAR, new TexturePos(1, 10, width, HEIGHT - 2));
                    GuiHelper.drawLayer(guiGraphics, offsetX, offsetY, OVERLAY_BAR, new TexturePos(0, 14, WIDTH, HEIGHT));

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

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
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
