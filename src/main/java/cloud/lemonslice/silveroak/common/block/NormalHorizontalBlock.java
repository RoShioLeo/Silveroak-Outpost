package cloud.lemonslice.silveroak.common.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class NormalHorizontalBlock extends HorizontalDirectionalBlock
{
    public NormalHorizontalBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext)
    {
        if (pContext.getPlayer() != null)
        {
            return defaultBlockState().setValue(FACING, pContext.getPlayer().getDirection().getOpposite());
        }
        return defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
        pBuilder.add(FACING);
    }
}
