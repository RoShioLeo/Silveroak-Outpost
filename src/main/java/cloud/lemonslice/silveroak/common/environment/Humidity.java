package cloud.lemonslice.silveroak.common.environment;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public enum Humidity
{
    ARID(Formatting.RED, 0.9F),
    DRY(Formatting.GOLD, 0.95F),
    AVERAGE(Formatting.WHITE, 1.0F),
    MOIST(Formatting.DARK_AQUA, 1.1F),
    HUMID(Formatting.DARK_GREEN, 1.2F);

    private final Formatting color;
    private final float tempCoefficient;

    Humidity(Formatting color, float tempCoefficient)
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

    public Text getTranslation()
    {
        return Text.translatable("info.silveroak.environment.humidity." + getName()).formatted(color);
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
