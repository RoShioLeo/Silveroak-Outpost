package cloud.lemonslice.silveroak.data;

import cloud.lemonslice.silveroak.data.provider.RecipesProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGatherHandler
{
    @SubscribeEvent
    public static void onDataGather(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new RecipesProvider(gen.getPackOutput()));
    }
}
