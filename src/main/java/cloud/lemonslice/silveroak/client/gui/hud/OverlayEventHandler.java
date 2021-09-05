package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public final class OverlayEventHandler
{
    public final static ResourceLocation DEFAULT = new ResourceLocation("minecraft", "textures/gui/icons.png");

    private final static ThermometerBarRenderer BAR_0 = new ThermometerBarRenderer(Minecraft.getInstance());
    public static boolean originThermometerBar = true;

    private final static RainGaugeBarRenderer BAR_1 = new RainGaugeBarRenderer(Minecraft.getInstance());
    private final static HygrometerBarRenderer BAR_2 = new HygrometerBarRenderer(Minecraft.getInstance());

    @SubscribeEvent(receiveCanceled = true)
    public static void onEvent(RenderGameOverlayEvent.Pre event)
    {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null)
        {
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            {
                if (!clientPlayer.getHeldItemMainhand().isEmpty())
                {
                    Item handed = clientPlayer.getHeldItemMainhand().getItem();
                    Biome biome = clientPlayer.getEntityWorld().getBiome(clientPlayer.getPosition());
                    if (originThermometerBar && handed.equals(SilveroakItemsRegistry.THERMOMETER.get()))
                    {
                        float temp = biome.getTemperature(clientPlayer.getPosition());
                        BAR_0.renderStatusBar(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(), temp);
                    }
                    else if (handed.equals(SilveroakItemsRegistry.RAIN_GAUGE.get()))
                    {
                        BAR_1.renderStatusBar(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(), biome.getDownfall());
                    }
                    else if (handed.equals(SilveroakItemsRegistry.HYGROMETER.get()))
                    {
                        BAR_2.renderStatusBar(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(), biome.getTemperature(clientPlayer.getPosition()), biome.getDownfall());
                    }
                }
            }
        }
    }
}
