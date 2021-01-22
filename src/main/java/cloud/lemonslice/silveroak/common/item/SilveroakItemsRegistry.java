package cloud.lemonslice.silveroak.common.item;

import cloud.lemonslice.silveroak.registry.RegistryModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public class SilveroakItemsRegistry extends RegistryModule
{
    public SilveroakItemsRegistry()
    {
        super(MODID);
    }

    public static final Item THERMOMETER = new NormalItem("thermometer", ItemGroup.TOOLS);
    public static final Item RAIN_GAUGE = new NormalItem("rain_gauge", ItemGroup.TOOLS);
    public static final Item HYGROMETER = new NormalItem("hygrometer", ItemGroup.TOOLS);
}
