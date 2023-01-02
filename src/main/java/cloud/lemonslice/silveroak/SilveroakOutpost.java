package cloud.lemonslice.silveroak;

import cloud.lemonslice.silveroak.common.item.SilveroakRegistry;
import cloud.lemonslice.silveroak.network.SimpleNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilveroakOutpost implements ModInitializer
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "silveroak";

    private static MinecraftServer server;

    public SilveroakOutpost()
    {

    }

    public static void error(String format, Object... data)
    {
        SilveroakOutpost.LOGGER.log(Level.ERROR, String.format(format, data));
    }

    public static void warn(String format, Object... data)
    {
        SilveroakOutpost.LOGGER.log(Level.WARN, String.format(format, data));
    }

    public static void info(String format, Object... data)
    {
        SilveroakOutpost.LOGGER.log(Level.INFO, String.format(format, data));
    }

    @Override
    public void onInitialize()
    {
        SilveroakRegistry.initItems();
        SimpleNetworkHandler.init();
        ServerLifecycleEvents.SERVER_STARTING.register(SilveroakOutpost::setServer);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> SilveroakOutpost.setServer(null));
    }

    public static MinecraftServer getCurrentServer()
    {
        return server;
    }

    public static void setServer(MinecraftServer server)
    {
        SilveroakOutpost.server = server;
    }
}
