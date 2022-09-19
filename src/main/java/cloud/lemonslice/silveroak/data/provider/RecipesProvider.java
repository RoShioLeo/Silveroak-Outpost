package cloud.lemonslice.silveroak.data.provider;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public final class RecipesProvider extends RecipeProvider
{
    public RecipesProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(SilveroakItemsRegistry.THERMOMETER.get()).define('x', Items.WATER_BUCKET).define('#', Tags.Items.GLASS_COLORLESS).pattern("###").pattern("# #").pattern("#x#").group("thermometer").unlockedBy("has_water_bucket", this.has(Items.WATER_BUCKET)).save(consumer);
        ShapedRecipeBuilder.shaped(SilveroakItemsRegistry.RAIN_GAUGE.get()).define('#', Tags.Items.GLASS_COLORLESS).define('*', Items.BUCKET).pattern("# #").pattern("# #").pattern("#*#").group("rain_gauge").unlockedBy("has_bucket", this.has(Items.BUCKET)).save(consumer);
        ShapelessRecipeBuilder.shapeless(SilveroakItemsRegistry.HYGROMETER.get()).requires(SilveroakItemsRegistry.THERMOMETER.get()).requires(SilveroakItemsRegistry.RAIN_GAUGE.get()).group("hygrometer").unlockedBy("has_thermometer", this.has(SilveroakItemsRegistry.THERMOMETER.get())).save(consumer);
    }

    @Override
    public String getName()
    {
        return "Silveroak Recipes";
    }
}
