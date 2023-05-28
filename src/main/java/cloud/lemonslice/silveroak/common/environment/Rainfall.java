package cloud.lemonslice.silveroak.common.environment;

import cloud.lemonslice.silveroak.mixin.BiomeWeatherAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.Biome;

public enum Rainfall
{
    RARE(Formatting.RED, Float.NEGATIVE_INFINITY, 0.1F),
    SCARCE(Formatting.GOLD, 0.1F, 0.3F),
    MODERATE(Formatting.WHITE, 0.3F, 0.6F),
    ADEQUATE(Formatting.DARK_AQUA, 0.6F, 0.8F),
    ABUNDANT(Formatting.DARK_GREEN, 0.8F, Float.POSITIVE_INFINITY);

    private final float min;
    private final float max;
    private final Formatting color;

    Rainfall(Formatting color, float min, float max)
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

    public boolean isInRainfall(float rainfall)
    {
        return min < rainfall && rainfall <= max;
    }

    public float getMin()
    {
        return min;
    }

    public float getMax()
    {
        return max;
    }

    public Text getTranslation()
    {
        return Text.translatable("info.silveroak.environment.rainfall." + getName()).formatted(color);
    }

    public static Rainfall getRainfallLevel(Biome biome)
    {
        return getRainfallLevel(((BiomeWeatherAccess)(Object)biome).getDownfall());
    }

    public static Rainfall getRainfallLevel(float rainfall)
    {
        for (Rainfall r : Rainfall.values())
        {
            if (r.isInRainfall(rainfall))
            {
                return r;
            }
        }
        return Rainfall.RARE;
    }
}
