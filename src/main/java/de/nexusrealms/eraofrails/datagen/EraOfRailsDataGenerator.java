package de.nexusrealms.eraofrails.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EraOfRailsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(TagGen.ItemGen::new);
		pack.addProvider(TagGen.BlockGen::new);
		pack.addProvider(TagGen.EntityGen::new);
		pack.addProvider(LangGen::new);
		pack.addProvider(LootTableGen.Block::new);
		pack.addProvider(LootTableGen.Entity::new);
		pack.addProvider(ModelGen::new);
		pack.addProvider(RecipeGen::new);
	}
}
