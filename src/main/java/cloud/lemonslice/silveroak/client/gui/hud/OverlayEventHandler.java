package cloud.lemonslice.silveroak.client.gui.hud;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class OverlayEventHandler
{
    private final static ThermometerBarOverlay BAR_0 = new ThermometerBarOverlay();
    public static boolean originThermometerBar = true;

    private final static RainGaugeBarOverlay BAR_1 = new RainGaugeBarOverlay();
    private final static HygrometerBarOverlay BAR_2 = new HygrometerBarOverlay();

    @SubscribeEvent(receiveCanceled = true)
    public static void onEvent(RegisterGuiOverlaysEvent event)
    {
        event.registerBelowAll("thermometer", BAR_0);
        event.registerBelowAll("rain_gauge", BAR_1);
        event.registerBelowAll("hygrometer", BAR_2);
    }
}
