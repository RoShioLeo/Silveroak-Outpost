package cloud.lemonslice.silveroak.helper;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.atomic.AtomicReference;

public final class VoxelShapeHelper
{
    public static VoxelShape createVoxelShape(double beginX, double beginY, double beginZ, double length, double height, double width)
    {
        return Block.box(beginX, beginY, beginZ, beginX + length, beginY + height, beginZ + width);
    }

    public static VoxelShape rotate(VoxelShape shapeIn, Rotation rot)
    {
        AtomicReference<VoxelShape> voxelShape = new AtomicReference<>(Shapes.empty());
        if (rot == Rotation.CLOCKWISE_180)
        {
            shapeIn.forAllBoxes((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, Shapes.or(shape, Block.box(z0, y0, 1 - x0, z1, y1, 1 - x1)));
            });
            return voxelShape.get();
        }
        else if (rot == Rotation.CLOCKWISE_90)
        {
            shapeIn.forAllBoxes((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, Shapes.or(shape, Block.box(1 - x0, y0, 1 - z0, 1 - x1, y1, 1 - z1)));
            });
            return voxelShape.get();
        }
        else if (rot == Rotation.COUNTERCLOCKWISE_90)
        {
            shapeIn.forAllBoxes((x0, y0, z0, x1, y1, z1) ->
            {
                VoxelShape shape = voxelShape.get();
                voxelShape.compareAndSet(shape, Shapes.or(shape, Block.box(1 - z0, y0, x0, 1 - z1, y1, x1)));
            });
            return voxelShape.get();
        }
        else return shapeIn;
    }
}
