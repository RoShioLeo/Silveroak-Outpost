package cloud.lemonslice.silveroak.helper;


import net.minecraft.core.Direction;

public final class BlockHelper
{
    public static Direction getNextHorizontal(Direction facing)
    {
        int index = (facing.get2DDataValue() + 1) % 4;
        return Direction.from2DDataValue(index);
    }

    public static Direction getPreviousHorizontal(Direction facing)
    {
        int index = (facing.get2DDataValue() + 3) % 4;
        return Direction.from2DDataValue(index);
    }
}
