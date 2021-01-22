package cloud.lemonslice.silveroak.data.provider;

import cloud.lemonslice.silveroak.common.item.SilveroakItemsRegistry;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public final class RecipesProvider extends RecipeProvider
{
    public RecipesProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(SilveroakItemsRegistry.THERMOMETER).key('x', Items.WATER_BUCKET).key('#', Tags.Items.GLASS_COLORLESS).patternLine("###").patternLine("# #").patternLine("#x#").setGroup("thermometer").addCriterion("has_water_bucket", this.hasItem(Items.WATER_BUCKET)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(SilveroakItemsRegistry.RAIN_GAUGE).key('#', Tags.Items.GLASS_COLORLESS).key('*', Items.BUCKET).patternLine("# #").patternLine("# #").patternLine("#*#").setGroup("rain_gauge").addCriterion("has_bucket", this.hasItem(Items.BUCKET)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(SilveroakItemsRegistry.HYGROMETER).addIngredient(SilveroakItemsRegistry.THERMOMETER).addIngredient(SilveroakItemsRegistry.RAIN_GAUGE).setGroup("hygrometer").addCriterion("has_thermometer", this.hasItem(SilveroakItemsRegistry.THERMOMETER)).build(consumer);
    }

    @Override
    public String getName()
    {
        return "Silveroak Recipes";
    }
}
