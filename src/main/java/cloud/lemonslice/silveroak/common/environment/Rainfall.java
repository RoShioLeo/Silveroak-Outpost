package cloud.lemonslice.silveroak.common.environment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

public enum Rainfall
{
    RARE(ChatFormatting.RED, Float.NEGATIVE_INFINITY, 0.1F),
    SCARCE(ChatFormatting.GOLD, 0.1F, 0.3F),
    MODERATE(ChatFormatting.WHITE, 0.3F, 0.6F),
    ADEQUATE(ChatFormatting.DARK_AQUA, 0.6F, 0.8F),
    ABUNDANT(ChatFormatting.DARK_GREEN, 0.8F, Float.POSITIVE_INFINITY);

    private final float min;
    private final float max;
    private final ChatFormatting color;

    Rainfall(ChatFormatting color, float min, float max)
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

    public Component getTranslation()
    {
        return Component.translatable("info.silveroak.environment.rainfall." + getName()).withStyle(color);
    }

    public static Rainfall getRainfallLevel(Biome biome)
    {
        return getRainfallLevel(biome.getModifiedClimateSettings().downfall());
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
