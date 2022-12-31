package cloud.lemonslice.silveroak.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

public class NormalHorizontalBlock extends HorizontalFacingBlock
{
    public NormalHorizontalBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        if (ctx.getPlayer() != null)
        {
            return getDefaultState().with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
        }
        return getDefaultState();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        super.appendProperties(builder);
        builder.add(FACING);
    }
}
