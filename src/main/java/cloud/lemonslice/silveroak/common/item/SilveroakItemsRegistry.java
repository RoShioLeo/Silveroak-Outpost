package cloud.lemonslice.silveroak.common.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public final class SilveroakItemsRegistry
{
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> THERMOMETER = ITEM_REGISTER.register("thermometer", ThermometerItem::new);
    public static final RegistryObject<Item> RAIN_GAUGE = ITEM_REGISTER.register("rain_gauge", RainGaugeItem::new);
    public static final RegistryObject<Item> HYGROMETER = ITEM_REGISTER.register("hygrometer", HygrometerItem::new);
}
