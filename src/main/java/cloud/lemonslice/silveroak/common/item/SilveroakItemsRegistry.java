package cloud.lemonslice.silveroak.common.item;

import cloud.lemonslice.silveroak.SilveroakOutpost;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class SilveroakItemsRegistry
{
    public static final Item THERMOMETER = new ThermometerItem();
    public static final Item RAIN_GAUGE = new RainGaugeItem();
    public static final Item HYGROMETER = new HygrometerItem();

    public static void initItems()
    {
        registerItem(THERMOMETER);
        registerItem(RAIN_GAUGE);
        registerItem(HYGROMETER);
    }

    private static void registerItem(Item itemIn)
    {
        if (itemIn instanceof ISilveroakItem item)
        {
            Registry.register(Registries.ITEM, item.getRegistryID(), (Item) item);
            if (item.getItemGroup() != null)
            {
                ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add((Item) item));
            }
        }
        else
        {
            SilveroakOutpost.error("The item to be registered should implement ISilveroakItem");
        }
    }
}
