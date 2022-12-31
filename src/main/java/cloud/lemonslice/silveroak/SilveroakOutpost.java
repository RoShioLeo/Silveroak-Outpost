package cloud.lemonslice.silveroak;

import cloud.lemonslice.silveroak.common.item.SilveroakRegistry;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilveroakOutpost implements ModInitializer
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "silveroak";

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
    }
}
