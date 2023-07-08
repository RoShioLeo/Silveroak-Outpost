package cloud.lemonslice.silveroak.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface ISilveroakItem
{
    RegistryKey<ItemGroup> getItemGroup();

    Identifier getRegistryID();
}
