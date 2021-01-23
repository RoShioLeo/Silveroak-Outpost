package cloud.lemonslice.silveroak;

import cloud.lemonslice.silveroak.common.config.NormalConfigs;
import cloud.lemonslice.silveroak.common.config.ServerConfig;
import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import com.google.common.collect.Lists;
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

import java.util.List;

import static cloud.lemonslice.silveroak.registry.RegisterManager.clearAll;

@Mod("silveroakoutpost")
public class SilveroakOutpost
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "silveroakoutpost";

    private static final List<String> LIST = Lists.newArrayList();

    public SilveroakOutpost()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, NormalConfigs.SERVER_CONFIG);
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
    public void onServerStarting(FMLServerStartedEvent event)
    {
        for (String modid : LIST)
        {
            ModList.get().getModContainerById(modid).ifPresent(modContainer ->
            {
                if (modContainer.getModInfo().getVersion().toString().contains("Alpha") && !DigestUtils.sha1Hex(ServerConfig.Verification.password.get()).equals("56af65c4b29038deecf2e161bc2c4293ccee703d"))
                {
                    throw new ModLoadingException(modContainer.getModInfo(), ModLoadingStage.DONE, "info.silveroak.loading.alpha", new Exception());
                }
            });
        }
        info("Password was verified successfully!");
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

    public static void addAlphaTestMod(String modid)
    {
        LIST.add(modid);
    }
}
