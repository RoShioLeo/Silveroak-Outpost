package cloud.lemonslice.silveroak.common.environment;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

public enum Temperature
{
    FREEZING(ChatFormatting.BLUE, Float.NEGATIVE_INFINITY, 0.15F),
    COLD(ChatFormatting.AQUA, 0.15F, 0.4F),
    COOL(ChatFormatting.GREEN, 0.4F, 0.65F),
    WARM(ChatFormatting.YELLOW, 0.65F, 0.9F),
    HOT(ChatFormatting.GOLD, 0.9F, 1.25F),
    HEAT(ChatFormatting.RED, 1.25F, Float.POSITIVE_INFINITY);

    private final ChatFormatting color;
    private final float min;
    private final float max;

    Temperature(ChatFormatting color, float min, float max)
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

    public Component getTranslation()
    {
        return Component.translatable("info.silveroak.environment.temperature." + getName()).withStyle(color);
    }

    public static Temperature getTemperatureLevel(Biome biome, BlockPos pos)
    {
        return getTemperatureLevel(biome.getHeightAdjustedTemperature(pos));
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
