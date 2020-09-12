package cloud.lemonslice.silveroak;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cloud.lemonslice.silveroak.registry.RegisterManager.clearAll;

@Mod("silveroakoutpost")
public class SilveroakOutpost
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "silveroakoutpost";

    public SilveroakOutpost()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        new SilveroakItemsRegistry();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        clearAll();
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
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
}
