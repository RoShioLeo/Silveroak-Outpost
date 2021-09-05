package cloud.lemonslice.silveroak.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class NormalItem extends Item
{
    public NormalItem(Properties properties)
    {
        super(properties);
    }

    public NormalItem(ItemGroup group)
    {
        super(getNormalItemProperties(group));
    }

    @Deprecated
    // Please use DeferredRegister rather than RegistryModule
    public NormalItem(String name, Properties properties)
    {
        super(properties);
        this.setRegistryName(name);
    }

    @Deprecated
    // Please use DeferredRegister rather than RegistryModule
    public NormalItem(String name, ItemGroup group)
    {
        super(getNormalItemProperties(group));
        this.setRegistryName(name);
    }

    public static Properties getNormalItemProperties(ItemGroup group)
    {
        return new Properties().group(group);
    }
}
