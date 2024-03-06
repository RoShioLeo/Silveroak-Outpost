package cloud.lemonslice.silveroak.common.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SilveroakItemsRegistry
{
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> THERMOMETER = ITEM_REGISTER.register("thermometer", ThermometerItem::new);
    public static final RegistryObject<Item> RAIN_GAUGE = ITEM_REGISTER.register("rain_gauge", RainGaugeItem::new);
    public static final RegistryObject<Item> HYGROMETER = ITEM_REGISTER.register("hygrometer", HygrometerItem::new);

    @SubscribeEvent
    public static void registerCreativeModeTabContents(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES))
        {
            ITEM_REGISTER.getEntries().forEach(event::accept);
        }
    }
}
