package cloud.lemonslice.silveroak.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.class)
public interface IBiomeInvoker
{
    @Invoker("computeTemperature")
    float invokeComputeTemperature(BlockPos pos);
}
