package cloud.lemonslice.silveroak.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public interface ISilveroakItem
{
    ItemGroup getItemGroup();

    Identifier getRegistryID();
}
