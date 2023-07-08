package cloud.lemonslice.silveroak.mixin;

import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author cnlimiter
 * CreateTime 2023/5/29 1:47
 * Name BiomeBuilderMixin
 * Description
 */

@Mixin(Biome.Builder.class)
public class BiomeBuilderMixin {

    @Shadow
    private @Nullable Float downfall;

    @Inject(method = "build", at = @At("RETURN"))
    private void apoli$storeDownfall(CallbackInfoReturnable<Biome> cir) {
        ((BiomeWeatherAccess)(Object)cir.getReturnValue()).setDownfall(downfall.floatValue());
    }
}
