package cloud.lemonslice.silveroak.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public final class NormalConfigs
{
    public static final ForgeConfigSpec SERVER_CONFIG = new ForgeConfigSpec.Builder().configure(ServerConfig::new).getRight();

    @SubscribeEvent
    public static void onReload(ModConfig.Reloading event)
    {
        ((CommentedFileConfig) event.getConfig().getConfigData()).load();
    }
}
