package cloud.lemonslice.silveroak.data.provider;

import cloud.lemonslice.silveroak.common.item.SilveroakRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public final class RecipesProvider extends FabricRecipeProvider
{
    public RecipesProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter)
    {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, SilveroakRegistry.HYGROMETER)
                .input(SilveroakRegistry.THERMOMETER)
                .input(SilveroakRegistry.RAIN_GAUGE)
                .group("hygrometer")
                .criterion(hasItem(SilveroakRegistry.THERMOMETER), conditionsFromItem(SilveroakRegistry.THERMOMETER))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SilveroakRegistry.THERMOMETER)
                .input('x', Items.WATER_BUCKET)
                .input('#', Items.GLASS)
                .pattern("###")
                .pattern("# #")
                .pattern("#x#")
                .group("thermometer")
                .criterion(hasItem(Items.WATER_BUCKET), conditionsFromItem(Items.WATER_BUCKET))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SilveroakRegistry.RAIN_GAUGE)
                .input('x', Items.BUCKET)
                .input('#', Items.GLASS)
                .pattern("# #")
                .pattern("# #")
                .pattern("#x#")
                .group("rain_gauge")
                .criterion(hasItem(Items.WATER_BUCKET), conditionsFromItem(Items.WATER_BUCKET))
                .offerTo(exporter);
    }

    @Override
    public String getName()
    {
        return "Silveroak Recipes";
    }
}
