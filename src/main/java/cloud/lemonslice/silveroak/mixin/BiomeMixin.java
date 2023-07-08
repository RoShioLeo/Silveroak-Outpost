package cloud.lemonslice.silveroak.mixin;


import cloud.lemonslice.silveroak.common.inter.IBiomeDownfallAccess;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public class BiomeMixin implements IBiomeDownfallAccess
{
    @Final
    @Shadow
    private Biome.Weather weather;

    @Override
    public float _1_20$getDownfall()
    {
        return weather.downfall();
    }
}
