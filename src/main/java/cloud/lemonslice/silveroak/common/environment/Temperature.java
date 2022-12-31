package cloud.lemonslice.silveroak.common.environment;

import cloud.lemonslice.silveroak.mixin.IBiomeInvoker;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public enum Temperature
{
    FREEZING(Formatting.BLUE, Float.NEGATIVE_INFINITY, 0.15F),
    COLD(Formatting.AQUA, 0.15F, 0.4F),
    COOL(Formatting.GREEN, 0.4F, 0.65F),
    WARM(Formatting.YELLOW, 0.65F, 0.9F),
    HOT(Formatting.GOLD, 0.9F, 1.25F),
    HEAT(Formatting.RED, 1.25F, Float.POSITIVE_INFINITY);

    private final Formatting color;
    private final float min;
    private final float max;

    Temperature(Formatting color, float min, float max)
    {
        this.color = color;
        this.min = min;
        this.max = max;
    }

    public int getId()
    {
        return this.ordinal() + 1;
    }

    public String getName()
    {
        return this.toString().toLowerCase();
    }

    public boolean isInTemperature(float temp)
    {
        return min < temp && temp <= max;
    }

    public float getMin()
    {
        return min;
    }

    public float getMax()
    {
        return max;
    }

    public float getWidth()
    {
        return max - min;
    }

    public Text getTranslation()
    {
        return Text.translatable("info.silveroak.environment.temperature." + getName()).formatted(color);
    }

    public static Temperature getTemperatureLevel(Biome biome, BlockPos pos)
    {
        return getTemperatureLevel(((IBiomeInvoker) (Object) biome).invokeComputeTemperature(pos));
    }

    public static Temperature getTemperatureLevel(float temp)
    {
        for (Temperature t : Temperature.values())
        {
            if (t.isInTemperature(temp))
            {
                return t;
            }
        }
        return Temperature.FREEZING;
    }
}
