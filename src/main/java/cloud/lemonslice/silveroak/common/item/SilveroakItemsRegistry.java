package cloud.lemonslice.silveroak.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public final class SilveroakItemsRegistry
{
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> THERMOMETER = ITEM_REGISTER.register("thermometer", () -> new NormalItem(ItemGroup.TOOLS));
    public static final RegistryObject<Item> RAIN_GAUGE = ITEM_REGISTER.register("rain_gauge", () -> new NormalItem(ItemGroup.TOOLS));
    public static final RegistryObject<Item> HYGROMETER = ITEM_REGISTER.register("hygrometer", () -> new NormalItem(ItemGroup.TOOLS));
}
