package cloud.lemonslice.silveroak.helper;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.concurrent.atomic.AtomicReference;

public final class VoxelShapeHelper
{
    public static VoxelShape createVoxelShape(double beginX, double beginY, double beginZ, double length, double height, double width)
    {
        return Block.createCuboidShape(beginX, beginY, beginZ, beginX + length, beginY + height, beginZ + width);
    }

    public static VoxelShape rotate(VoxelShape shapeIn, BlockRotation rot)
    {
        AtomicReference<VoxelShape> voxelShape = new AtomicReference<>(VoxelShapes.empty());
        if (rot == BlockRotation.CLOCKWISE_180)
        {
            shapeIn.forEachBox((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, VoxelShapes.union(shape, Block.createCuboidShape(z0, y0, 1 - x0, z1, y1, 1 - x1)));
            });
            return voxelShape.get();
        }
        else if (rot == BlockRotation.CLOCKWISE_90)
        {
            shapeIn.forEachBox((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, VoxelShapes.union(shape, Block.createCuboidShape(1 - x0, y0, 1 - z0, 1 - x1, y1, 1 - z1)));
            });
            return voxelShape.get();
        }
        else if (rot == BlockRotation.COUNTERCLOCKWISE_90)
        {
            shapeIn.forEachBox((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, VoxelShapes.union(shape, Block.createCuboidShape(1 - z0, y0, x0, 1 - z1, y1, x1)));
            });
            return voxelShape.get();
        }
        else return shapeIn;
    }
}
