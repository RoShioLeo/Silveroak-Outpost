package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.common.environment.Humidity;
import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import cloud.lemonslice.silveroak.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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
public class HygrometerBarOverlay extends GuiComponent implements IGuiOverlay
{
    private final static ResourceLocation OVERLAY_BAR = new ResourceLocation(MODID, "textures/gui/hud/env.png");

    private final static int WIDTH = 31;
    private final static int HEIGHT = 5;

    private static float humidity = 0;
    private static int level = 0;

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight)
    {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null)
        {
            if (!clientPlayer.getMainHandItem().isEmpty())
            {
                Item handed = clientPlayer.getMainHandItem().getItem();
                if (handed.equals(SilveroakItemsRegistry.HYGROMETER.get()))
                {
                    Biome biome = clientPlayer.getLevel().getBiome(clientPlayer.blockPosition()).value();
                    float temperature = biome.getHeightAdjustedTemperature(clientPlayer.blockPosition());
                    float rainfall = biome.getDownfall();

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, OVERLAY_BAR);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    level = Humidity.getHumid(rainfall, temperature).getId();

                    int offsetX = (screenWidth - WIDTH + 1) / 2, offsetY = (screenHeight + 36 - HEIGHT) / 2;

                    int width = Math.round(humidity * 6);
                    GuiHelper.drawLayerBySize(poseStack, offsetX + 1, offsetY + 1, new TexturePos(1, 20, width, HEIGHT - 2), 256, 256);
                    GuiHelper.drawLayerBySize(poseStack, offsetX, offsetY, new TexturePos(0, 24, WIDTH, HEIGHT), 256, 256);

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
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
}
