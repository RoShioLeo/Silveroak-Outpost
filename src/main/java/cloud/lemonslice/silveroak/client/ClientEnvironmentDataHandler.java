package cloud.lemonslice.silveroak.client;

import cloud.lemonslice.silveroak.SilveroakOutpost;
import cloud.lemonslice.silveroak.common.environment.Humidity;
import cloud.lemonslice.silveroak.common.environment.Rainfall;
import cloud.lemonslice.silveroak.common.environment.Temperature;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

import java.text.DecimalFormat;

public final class ClientEnvironmentDataHandler
{
    @SuppressWarnings("deprecation")
    public static Component getTemperatureInfo()
    {
        Player player = SilveroakOutpost.PROXY.getClientPlayer();
        Biome biome = player.getLevel().getBiome(player.blockPosition()).value();
        float tempF = biome.getTemperature(player.blockPosition());
        Temperature temperature = Temperature.getTemperatureLevel(tempF);
        return new TranslatableComponent("info.silveroak.environment.temperature", temperature.getTranslation(), new DecimalFormat("0.00").format(tempF));
    }

    public static Component getRainfallInfo()
    {
        Player player = SilveroakOutpost.PROXY.getClientPlayer();
        Biome biome = player.getLevel().getBiome(player.blockPosition()).value();
        float rainfallF = biome.getDownfall();
        Rainfall rainfall = Rainfall.getRainfallLevel(rainfallF);
        return new TranslatableComponent("info.silveroak.environment.rainfall", rainfall.getTranslation(), new DecimalFormat("0.00").format(rainfallF));
    }

    @SuppressWarnings("deprecation")
    public static Component getHumidityInfo()
    {
        Player player = SilveroakOutpost.PROXY.getClientPlayer();
        Biome biome = player.getLevel().getBiome(player.blockPosition()).value();
        float tempF = biome.getTemperature(player.blockPosition());
        float rainfallF = biome.getDownfall();
        Humidity humidity = Humidity.getHumid(rainfallF, tempF);
        return new TranslatableComponent("info.silveroak.environment.humidity", humidity.getTranslation());
    }
}
