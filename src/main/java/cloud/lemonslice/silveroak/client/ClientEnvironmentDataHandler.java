package cloud.lemonslice.silveroak.client;

import cloud.lemonslice.silveroak.common.environment.Humidity;
import cloud.lemonslice.silveroak.common.environment.Rainfall;
import cloud.lemonslice.silveroak.common.environment.Temperature;
import cloud.lemonslice.silveroak.mixin.BiomeWeatherAccess;
import cloud.lemonslice.silveroak.mixin.IBiomeInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;

import java.text.DecimalFormat;

public final class ClientEnvironmentDataHandler
{
    @SuppressWarnings("deprecation")
    public static Text getTemperatureInfo()
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Biome biome = player.getWorld().getBiome(player.getBlockPos()).value();
        float tempF = ((IBiomeInvoker) (Object) biome).invokeComputeTemperature(player.getBlockPos());
        Temperature temperature = Temperature.getTemperatureLevel(tempF);
        return Text.translatable("info.silveroak.environment.temperature", temperature.getTranslation(), new DecimalFormat("0.00").format(tempF));
    }

    public static Text getRainfallInfo()
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Biome biome = player.getWorld().getBiome(player.getBlockPos()).value();
        float rainfallF = ((BiomeWeatherAccess)(Object)biome).getDownfall();
        Rainfall rainfall = Rainfall.getRainfallLevel(rainfallF);
        return Text.translatable("info.silveroak.environment.rainfall", rainfall.getTranslation(), new DecimalFormat("0.00").format(rainfallF));
    }

    @SuppressWarnings("deprecation")
    public static Text getHumidityInfo()
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Biome biome = player.getWorld().getBiome(player.getBlockPos()).value();
        float tempF = ((IBiomeInvoker) (Object) biome).invokeComputeTemperature(player.getBlockPos());
        float rainfallF = ((BiomeWeatherAccess)(Object)biome).getDownfall();
        Humidity humidity = Humidity.getHumid(rainfallF, tempF);
        return Text.translatable("info.silveroak.environment.humidity", humidity.getTranslation());
    }
}
