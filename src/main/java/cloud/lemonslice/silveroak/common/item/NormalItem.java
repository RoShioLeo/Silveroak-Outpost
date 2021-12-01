package cloud.lemonslice.silveroak.common.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class NormalItem extends Item
{
    public NormalItem(Properties properties)
    {
        super(properties);
    }

    public NormalItem(CreativeModeTab group)
    {
        super(getNormalItemProperties(group));
    }

    public static Properties getNormalItemProperties(CreativeModeTab group)
    {
        return new Properties().tab(group);
    }
}
