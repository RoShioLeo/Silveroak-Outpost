package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public final class OverlayEventHandler
{
    private final static ThermometerBarRenderer BAR_0 = new ThermometerBarRenderer();
    public static boolean originThermometerBar = true;

    private final static RainGaugeBarRenderer BAR_1 = new RainGaugeBarRenderer();
    private final static HygrometerBarRenderer BAR_2 = new HygrometerBarRenderer();

    @SubscribeEvent(receiveCanceled = true)
    @SuppressWarnings("deprecation")
    public static void onEvent(RenderGameOverlayEvent.Pre event)
    {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null)
        {
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            {
                if (!clientPlayer.getMainHandItem().isEmpty())
                {
                    Item handed = clientPlayer.getMainHandItem().getItem();
                    Biome biome = clientPlayer.getLevel().getBiome(clientPlayer.blockPosition()).value();
                    if (originThermometerBar && handed.equals(SilveroakItemsRegistry.THERMOMETER.get()))
                    {
                        BAR_0.renderStatusBar(event.getMatrixStack(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), biome.getTemperature(clientPlayer.blockPosition()));
                    }
                    else if (handed.equals(SilveroakItemsRegistry.RAIN_GAUGE.get()))
                    {
                        BAR_1.renderStatusBar(event.getMatrixStack(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), biome.getDownfall());
                    }
                    else if (handed.equals(SilveroakItemsRegistry.HYGROMETER.get()))
                    {
                        BAR_2.renderStatusBar(event.getMatrixStack(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), biome.getTemperature(clientPlayer.blockPosition()), biome.getDownfall());
                    }
                }
            }
        }
    }
}
