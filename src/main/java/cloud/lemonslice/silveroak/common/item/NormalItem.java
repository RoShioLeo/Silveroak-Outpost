package cloud.lemonslice.silveroak.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class NormalItem extends Item implements ISilveroakItem
{
    private final ItemGroup itemGroup;
    private final Identifier registryID;

    public NormalItem(Identifier id, FabricItemSettings properties, ItemGroup itemGroup)
    {
        super(properties);
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    public NormalItem(Identifier id, ItemGroup itemGroup)
    {
        super(new FabricItemSettings());
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    @Nullable
    @Override
    public ItemGroup getItemGroup()
    {
        return itemGroup;
    }

    @Override
    public Identifier getRegistryID()
    {
        return registryID;
    }
}
