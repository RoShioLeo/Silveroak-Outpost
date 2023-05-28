package cloud.lemonslice.silveroak.mixin;


import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
/**
 * Author cnlimiter
 * CreateTime 2023/5/29 1:48
 * Name BiomeMixin
 * Description
 */

@Mixin(Biome.class)
public class BiomeMixin implements BiomeWeatherAccess{
    @Unique
    private float apoli$downfall;

    @Override
    public float getDownfall() {
        return apoli$downfall;
    }

    @Override
    public void setDownfall(float downfall) {
        apoli$downfall = downfall;
    }
}
