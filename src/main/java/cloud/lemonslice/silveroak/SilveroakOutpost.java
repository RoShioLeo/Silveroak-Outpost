package cloud.lemonslice.silveroak;

import cloud.lemonslice.silveroak.common.config.NormalConfigs;
import cloud.lemonslice.silveroak.common.config.ServerConfig;
import cloud.lemonslice.silveroak.network.SimpleNetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry.ITEM_REGISTER;
import static cloud.lemonslice.silveroak.registry.RegisterManager.clearAll;

@Mod("silveroakoutpost")
public class SilveroakOutpost
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "silveroakoutpost";

    private static boolean verification = false;

    public SilveroakOutpost()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, NormalConfigs.SERVER_CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        SimpleNetworkHandler.init();
        ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        clearAll();
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartedEvent event)
    {
        if (verification)
        {
            if (!DigestUtils.sha1Hex(ServerConfig.Verification.password.get()).equals("56af65c4b29038deecf2e161bc2c4293ccee703d"))
            {
                throw new ModLoadingException(ModList.get().getModContainerById("silveroakoutpost").get().getModInfo(), ModLoadingStage.DONE, "info.silveroak.loading.alpha", new Exception());
            }
            else
            {
                info("Password was verified successfully!");
            }
        }
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

    public static void needVerification()
    {
        verification = true;
    }
}
