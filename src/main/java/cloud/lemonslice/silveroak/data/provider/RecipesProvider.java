package cloud.lemonslice.silveroak.data.provider;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public final class RecipesProvider extends RecipeProvider
{
    public RecipesProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SilveroakItemsRegistry.THERMOMETER.get())
                .define('x', Items.WATER_BUCKET)
                .define('#', Tags.Items.GLASS_COLORLESS)
                .pattern("###")
                .pattern("# #")
                .pattern("#x#")
                .group("thermometer")
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SilveroakItemsRegistry.RAIN_GAUGE.get())
                .define('#', Tags.Items.GLASS_COLORLESS)
                .define('*', Items.BUCKET)
                .pattern("# #")
                .pattern("# #")
                .pattern("#*#")
                .group("rain_gauge")
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SilveroakItemsRegistry.HYGROMETER.get())
                .requires(SilveroakItemsRegistry.THERMOMETER.get())
                .requires(SilveroakItemsRegistry.RAIN_GAUGE.get())
                .group("hygrometer")
                .unlockedBy("has_thermometer", has(SilveroakItemsRegistry.THERMOMETER.get()))
                .save(consumer);
    }
}
