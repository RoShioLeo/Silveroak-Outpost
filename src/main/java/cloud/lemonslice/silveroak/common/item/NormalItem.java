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
        super(new Properties().tab(group));
    }
}
