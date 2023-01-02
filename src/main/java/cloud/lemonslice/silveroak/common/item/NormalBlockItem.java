package cloud.lemonslice.silveroak.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class NormalBlockItem extends BlockItem implements ISilveroakItem
{
    private final ItemGroup itemGroup;
    private final Identifier registryID;

    public NormalBlockItem(Block block, Identifier id, Settings settings, ItemGroup itemGroup)
    {
        super(block, settings);
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    public NormalBlockItem(Block block, Identifier id, ItemGroup itemGroup)
    {
        super(block, new FabricItemSettings());
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

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
