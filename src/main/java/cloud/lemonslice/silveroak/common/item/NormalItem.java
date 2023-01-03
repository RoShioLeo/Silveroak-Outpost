package cloud.lemonslice.silveroak.common.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class NormalItem extends Item
{
    private final CreativeModeTab itemGroup;

    public NormalItem(Properties properties, CreativeModeTab itemGroup)
    {
        super(properties);
        this.itemGroup = itemGroup;
    }

    public NormalItem(CreativeModeTab itemGroup)
    {
        super(new Properties());
        this.itemGroup = itemGroup;
    }
}
