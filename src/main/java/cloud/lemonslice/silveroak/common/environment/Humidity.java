package cloud.lemonslice.silveroak.common.environment;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.biome.Biome;

public enum Humidity
{
    ARID(ChatFormatting.RED, 0.9F),
    DRY(ChatFormatting.GOLD, 0.95F),
    AVERAGE(ChatFormatting.WHITE, 1.0F),
    MOIST(ChatFormatting.DARK_AQUA, 1.1F),
    HUMID(ChatFormatting.DARK_GREEN, 1.2F);

    private final ChatFormatting color;
    private final float tempCoefficient;

    Humidity(ChatFormatting color, float tempCoefficient)
    {
        this.color = color;
        this.tempCoefficient = tempCoefficient;
    }

    public int getId()
    {
        return this.ordinal() + 1;
    }

    public String getName()
    {
        return this.toString().toLowerCase();
    }

    public Component getTranslation()
    {
        return new TranslatableComponent("info.silveroak.environment.humidity." + getName()).withStyle(color);
    }

    public float getCoefficient()
    {
        return tempCoefficient;
    }

    public static Humidity getHumid(Rainfall rainfall, Temperature temperature)
    {
        int rOrder = rainfall.ordinal();
        int tOrder = temperature.ordinal();
        int level = Math.max(0, rOrder - Math.abs(rOrder - tOrder) / 2);
        return Humidity.values()[level];
    }

    public static Humidity getHumid(Biome biome, BlockPos pos)
    {
        return getHumid(Rainfall.getRainfallLevel(biome), Temperature.getTemperatureLevel(biome, pos));
    }

    public static Humidity getHumid(float rainfall, float temperature)
    {
        return Humidity.getHumid(Rainfall.getRainfallLevel(rainfall), Temperature.getTemperatureLevel(temperature));
    }
}
