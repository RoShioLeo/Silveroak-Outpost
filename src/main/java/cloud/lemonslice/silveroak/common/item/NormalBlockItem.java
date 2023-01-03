package cloud.lemonslice.silveroak.common.item;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class NormalBlockItem extends BlockItem implements ISilveroakItem
{
    private final CreativeModeTab itemGroup;

    public NormalBlockItem(Block block, Properties properties, CreativeModeTab itemGroup)
    {
        super(block, properties);
        this.itemGroup = itemGroup;
    }

    public NormalBlockItem(Block block, CreativeModeTab itemGroup)
    {
        super(block, new Properties());
        this.itemGroup = itemGroup;
    }

    @Override
    public CreativeModeTab getItemGroup()
    {
        return itemGroup;
    }
}
