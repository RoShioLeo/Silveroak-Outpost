package cloud.lemonslice.silveroak.common.item;

import cloud.lemonslice.silveroak.SilveroakOutpost;
import cloud.lemonslice.silveroak.common.ISilveroakEntry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class SilveroakRegistry
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

    public static void registerItem(Item itemIn)
    {
        if (itemIn instanceof ISilveroakItem item)
        {
            Registry.register(Registries.ITEM, item.getRegistryID(), (Item) item);
            if (item.getItemGroup() != null)
            {
                ItemGroupEvents.modifyEntriesEvent(item.getItemGroup()).register(content -> content.add((Item) item));
            }
        }
        else
        {
            SilveroakOutpost.error("The item to be registered should implement ISilveroakItem");
        }
    }

    public static void registerBlock(Block blockIn)
    {
        register(Registries.BLOCK, blockIn);
    }

    public static void registerBlockEntity(BlockEntityType<?> blockIn)
    {
        register(Registries.BLOCK_ENTITY_TYPE, blockIn);
    }

    public static <T> void register(Registry<T> registry, T entryIn)
    {
        if (entryIn instanceof ISilveroakEntry entry)
        {
            Registry.register(registry, entry.getRegistryID(), entryIn);
        }
        else
        {
            SilveroakOutpost.error("The entry to be registered should implement ISilveroakEntry");
        }
    }
}
