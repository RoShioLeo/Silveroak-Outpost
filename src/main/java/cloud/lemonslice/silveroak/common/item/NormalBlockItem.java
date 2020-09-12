package cloud.lemonslice.silveroak.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

public class NormalBlockItem extends BlockItem
{
    public NormalBlockItem(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.setRegistryName(blockIn.getRegistryName());
    }

    public NormalBlockItem(Block blockIn, ItemGroup group)
    {
        super(blockIn, NormalItem.getNormalItemProperties(group));
        this.setRegistryName(blockIn.getRegistryName());
    }
}
