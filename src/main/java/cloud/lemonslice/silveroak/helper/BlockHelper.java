package cloud.lemonslice.silveroak.helper;

import net.minecraft.util.math.Direction;

public final class BlockHelper
{
    public static Direction getNextHorizontal(Direction facing)
    {
        int index = (facing.getHorizontal() + 1) % 4;
        return Direction.fromHorizontal(index);
    }

    public static Direction getPreviousHorizontal(Direction facing)
    {
        int index = (facing.getHorizontal() + 3) % 4;
        return Direction.fromHorizontal(index);
    }
}
