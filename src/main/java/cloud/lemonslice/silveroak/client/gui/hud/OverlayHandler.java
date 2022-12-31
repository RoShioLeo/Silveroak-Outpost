package cloud.lemonslice.silveroak.client.gui.hud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class OverlayHandler implements ClientModInitializer
{
    private final static ThermometerBarOverlay BAR_0 = new ThermometerBarOverlay();
    public static boolean originThermometerBar = true;

    private final static RainGaugeBarOverlay BAR_1 = new RainGaugeBarOverlay();
    private final static HygrometerBarOverlay BAR_2 = new HygrometerBarOverlay();

    @Override
    public void onInitializeClient()
    {
        ClientTickEvents.START_WORLD_TICK.register(ThermometerBarOverlay::onClientTick);
        ClientTickEvents.START_WORLD_TICK.register(RainGaugeBarOverlay::onClientTick);
        ClientTickEvents.START_WORLD_TICK.register(HygrometerBarOverlay::onClientTick);

        HudRenderCallback.EVENT.register(BAR_0);
        HudRenderCallback.EVENT.register(BAR_1);
        HudRenderCallback.EVENT.register(BAR_2);
    }
}
