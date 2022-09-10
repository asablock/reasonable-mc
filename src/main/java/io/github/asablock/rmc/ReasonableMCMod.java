package io.github.asablock.rmc;

import io.github.asablock.rmc.block.*;
import io.github.asablock.rmc.block.entity.ChoppingBlockEntity;
import io.github.asablock.rmc.client.ClientRenderLayer;
import io.github.asablock.rmc.item.*;
import io.github.asablock.rmc.worldgen.AppleSaplingGenerator;
import io.github.asablock.rmc.worldgen.RMCWorldGenConsts;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.*;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ReasonableMCMod implements ModInitializer {
    public static ReasonableMCMod INSTANCE;

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(identifier("item_group"),
            groupIconSupplier());

    public static final Block BLOCK_STONE_MORTAR;
    public static final Block BLOCK_WATERED_MILK_BUCKET = new WateredMilkBucketBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).ticksRandomly());
    //@ClientRenderLayer(ClientRenderLayer.Layer.CUTOUT)
    public static final Block BLOCK_RICE = new RiceBlock(FabricBlockSettings.copyOf(Blocks.WHEAT));
    public static final Block BLOCK_VOID_FIELD = new VoidFieldBlock(FabricBlockSettings.copyOf(Blocks.BEDROCK));
    public static final Block BLOCK_VOID_EXTRACTOR = new VoidExtractorBlock(FabricBlockSettings.copyOf(Blocks.BEDROCK).ticksRandomly());
    public static final Block BLOCK_VOID_EXTRACTION_ARM = new VoidExtractionArmBlock(FabricBlockSettings.copyOf(Blocks.BEDROCK).ticksRandomly());
    @ClientRenderLayer(ClientRenderLayer.Layer.CUTOUT)
    public static final Block BLOCK_VOID_FLOWER = new VoidFlowerBlock(FabricBlockSettings.copyOf(Blocks.WITHER_ROSE));
    public static final Block BLOCK_BLACK_HOLE = new BlackHoleBlock(FabricBlockSettings.copyOf(BLOCK_VOID_FIELD).noCollision());
    public static final Block BLOCK_WHITE_HOLE = new WhiteHoleBlock(FabricBlockSettings.copyOf(BLOCK_BLACK_HOLE));
    public static final Block BLOCK_CHOPPING_BLOCK;
    public static final Block BLOCK_MILL;
    public static final Block BLOCK_APPLE = new AppleBlock(FabricBlockSettings.copyOf(Blocks.COCOA));
    @ClientRenderLayer(ClientRenderLayer.Layer.CUTOUT)
    public static final Block BLOCK_APPLE_SAPLING;
    public static final Block BLOCK_APPLE_LEAVES = new AppleLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES));

    /*public static final EntityType<CreamEntity> CREAM_ENTITY_TYPE = FabricEntityTypeBuilder.<CreamEntity>createMob()
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    CreamEntity::canSpawnCream)
            .build();*/

    public static final BlockEntityType<ChoppingBlockEntity> CHOPPING_BLOCK_ENTITY_TYPE;

    public static final Item ITEM_PESTLE = new Item(groupped().maxDamage(35));
    public static final Item ITEM_BAG = new Item(groupped());
    public static final Item ITEM_FLOUR_BAG = new BaggedItem(groupped().recipeRemainder(ITEM_BAG));
    public static final Item ITEM_STONE_MORTAR;
    public static final Item ITEM_WATERED_MILK_BUCKET = new BlockItem(BLOCK_WATERED_MILK_BUCKET, groupped().recipeRemainder(Items.BUCKET));
    public static final Item ITEM_YEAST_BAG = new BaggedItem(groupped().recipeRemainder(ITEM_BAG).maxDamage(25));
    public static final Item ITEM_DOUGH = new Item(groupped());
    public static final Item ITEM_RICE = new Item(groupped());
    public static final Item ITEM_KNIFE = new Item(groupped().maxDamage(60));
    public static final Item ITEM_ROLLING_PIN = new Item(groupped().maxDamage(100));
    public static final Item ITEM_RICE_SEEDS = new AliasedBlockItem(BLOCK_RICE, groupped());
    public static final Item ITEM_FERMENTED_DOUGH = new Item(groupped());
    public static final Item ITEM_REFINED_ROTTEN_FLESH = new Item(groupped().food(FoodComponents.ROTTEN_FLESH));
    public static final Item ITEM_WAFER = new Item(groupped());
    public static final Item ITEM_NOODLES = new ItemInBowl(groupped().food(RMCFoodComponents.NOODLES));
    public static final Item ITEM_COOKED_NOODLES = new ItemInBowl(groupped().food(RMCFoodComponents.COOKED_NOODLES));
    public static final Item ITEM_FERMENTED_WAFER = new Item(groupped());
    public static final Item ITEM_CHOPPING_BLOCK;
    public static final Item ITEM_PORK_STEAMED_BUN = new Item(groupped().food(RMCFoodComponents.BUN));
    public static final Item ITEM_COOKED_PORK_STEAMED_BUN = new Item(groupped().food(RMCFoodComponents.COOKED_BUN));
    public static final Item ITEM_MILL_ITEM;
    /*public static final Item CREAM_SPAWN_EGG_ITEM = new SpawnEggItem(CREAM_ENTITY_TYPE, 0x51A03E,
            0xB60808, new FabricItemSettings().group(ItemGroup.MISC));*/
    public static final Item ITEM_ICE_HELMET = new ArmorItem(RMCMaterials.ARMOR_ICE, EquipmentSlot.HEAD, groupped());
    public static final Item ITEM_ICE_CHESTPLATE = new ArmorItem(RMCMaterials.ARMOR_ICE, EquipmentSlot.CHEST, groupped());
    public static final Item ITEM_ICE_LEGGINGS = new ArmorItem(RMCMaterials.ARMOR_ICE, EquipmentSlot.LEGS, groupped());
    public static final Item ITEM_ICE_BOOTS = new ArmorItem(RMCMaterials.ARMOR_ICE, EquipmentSlot.FEET, groupped());
    public static final Item ITEM_END_PORTAL_FRAME_REMOVER = new EndPortalFrameRemoverItem(groupped().maxDamage(2));
    public static final Item ITEM_REMOVABLE_END_PORTAL_FRAME = new BlockItem(Blocks.END_PORTAL_FRAME, groupped().maxCount(4));
    public static final Item ITEM_VOID_EXTRACTOR = new BlockItem(BLOCK_VOID_EXTRACTOR, groupped().maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item ITEM_VOID_FLOWER = new BlockItem(BLOCK_VOID_FLOWER, groupped().maxCount(4).rarity(Rarity.UNCOMMON));
    public static final Item ITEM_VOID_ESSENCE = new Item(groupped().rarity(Rarity.UNCOMMON));
    public static final Item ITEM_BLACK_HOLE = new BlockItem(BLOCK_BLACK_HOLE, groupped().maxCount(1).rarity(Rarity.EPIC));
    public static final Item ITEM_WHITE_HOLE = new BlockItem(BLOCK_WHITE_HOLE, groupped().maxCount(1).rarity(Rarity.EPIC));
    public static final Item ITEM_KETTLE = new Item(groupped());
    public static final Item ITEM_LOCK = new LockItem(new FabricItemSettings().maxCount(1)); // Don't group it, or game crashes while trying to move cursor on it in creative inventory
    public static final Item ITEM_KEY = new KeyItem(groupped().maxCount(16));
    public static final Item ITEM_APPLE_CORE;
    public static final Item ITEM_APPLE_LEAVES = new BlockItem(BLOCK_APPLE_LEAVES, groupped());

    public static final SpecialRecipeSerializer<KeyRecipe> SPECIAL_KEY_RECIPE = new SpecialRecipeSerializer<>(KeyRecipe::new);

    public static final DamageSource TOO_DRY = new PDamageSource("tooDry").setUnblockable().setBypassesArmor();
    public static final DamageSource INTO_VOID = new PDamageSource("intoVoid").setUnblockable().setBypassesArmor();

    public static final SoundEvent SE_ITEM_ARMOR_EQUIP_ICE = new SoundEvent(identifier("item.armor.equip.ice"));
    public static final SoundEvent SE_ITEM_BLOCK_FLETCHING_TABLE_USE = new SoundEvent(identifier("block.fletching_table.use"));

    public static ScreenHandlerType<FletchingTableScreenHandler> SCH_FLETCHING_TABLE;


    public ReasonableMCMod() {
        INSTANCE = this;
    }

    @Override
    public void onInitialize() {
        registerBlockEntities();
        registerBlocks();
        registerItems();
        RMCStatusEffects.register();
        //Registry.register(Registry.ENTITY_TYPE, identifier("cream"), CREAM_ENTITY_TYPE);

        Registry.register(Registry.RECIPE_SERIALIZER, identifier("key"), SPECIAL_KEY_RECIPE);

        register(SE_ITEM_BLOCK_FLETCHING_TABLE_USE);

        SCH_FLETCHING_TABLE = ScreenHandlerRegistry.registerSimple(identifier("fletching_table"), FletchingTableScreenHandler::new);

        RMCWorldGenConsts.register();
    }

    private static void registerBlocks() {
        register(BLOCK_STONE_MORTAR, "stone_mortar");
        register(BLOCK_WATERED_MILK_BUCKET, "watered_milk_bucket");
        register(BLOCK_VOID_FIELD, "void_field");
        register(BLOCK_CHOPPING_BLOCK, "chopping_block");
        register(BLOCK_RICE, "rice");
        register(BLOCK_MILL, "mill");
        register(BLOCK_VOID_EXTRACTOR, "void_extractor");
        register(BLOCK_VOID_EXTRACTION_ARM, "void_extraction_arm");
        register(BLOCK_VOID_FLOWER, "void_flower");
        register(BLOCK_BLACK_HOLE, "black_hole");
        register(BLOCK_WHITE_HOLE, "white_hole");
        register(BLOCK_APPLE, "apple");
        register(BLOCK_APPLE_SAPLING, "apple_sapling");
        register(BLOCK_APPLE_LEAVES, "apple_leaves");
    }

    private static void registerBlockEntities() {
        register(CHOPPING_BLOCK_ENTITY_TYPE, "chopping_block");
    }

    private static void registerItems() {
        register(ITEM_PESTLE, "pestle");
        register(ITEM_FLOUR_BAG, "flour_bag");
        register(ITEM_STONE_MORTAR, "stone_mortar");
        register(ITEM_BAG, "bag");
        register(ITEM_WATERED_MILK_BUCKET, "watered_milk_bucket");
        register(ITEM_YEAST_BAG, "yeast_bag");
        register(ITEM_DOUGH, "dough");
        register(ITEM_KNIFE, "knife");
        register(ITEM_ROLLING_PIN, "rolling_pin");
        register(ITEM_FERMENTED_DOUGH, "fermented_dough");
        register(ITEM_REFINED_ROTTEN_FLESH, "refined_rotten_flesh");
        register(ITEM_CHOPPING_BLOCK, "chopping_block");
        register(ITEM_FERMENTED_WAFER, "fermented_wafer");
        register(ITEM_RICE, "rice");
        register(ITEM_WAFER, "wafer");
        register(ITEM_NOODLES, "noodles");
        register(ITEM_COOKED_NOODLES, "cooked_noodles");
        register(ITEM_PORK_STEAMED_BUN, "pork_steamed_bun");
        register(ITEM_COOKED_PORK_STEAMED_BUN, "cooked_pork_steamed_bun");
        register(ITEM_RICE_SEEDS, "rice_seeds");
        register(ITEM_MILL_ITEM, "mill");
        //register(CREAM_SPAWN_EGG_ITEM, "cream_spawn_egg");
        register(ITEM_ICE_HELMET, "ice_helmet");
        register(ITEM_ICE_CHESTPLATE, "ice_chestplate");
        register(ITEM_ICE_LEGGINGS, "ice_leggings");
        register(ITEM_ICE_BOOTS, "ice_boots");
        register(ITEM_END_PORTAL_FRAME_REMOVER, "end_portal_frame_remover");
        register(ITEM_REMOVABLE_END_PORTAL_FRAME, "removable_end_portal_frame");
        register(ITEM_VOID_ESSENCE, "void_essence");
        register(ITEM_VOID_EXTRACTOR, "void_extractor");
        register(ITEM_VOID_FLOWER, "void_flower");
        register(ITEM_BLACK_HOLE, "black_hole");
        register(ITEM_WHITE_HOLE, "white_hole");
        register(ITEM_LOCK, "lock");
        register(ITEM_KEY, "key");
        register(ITEM_APPLE_CORE, "apple_core");
        register(ITEM_APPLE_LEAVES, "apple_leaves");
    }

    private static void register(Item item, String path) {
        Registry.register(Registry.ITEM, identifier(path), item);
    }

    private static void register(Block block, String path) {
        Registry.register(Registry.BLOCK, identifier(path), block);
    }

    private static void register(BlockEntityType<?> blockEntityType, String path) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier(path), blockEntityType);
    }

    private static void register(SoundEvent soundEvent) {
        Registry.register(Registry.SOUND_EVENT, soundEvent.getId(), soundEvent);
    }

    public static FabricItemSettings groupped() {
        return new FabricItemSettings().group(ITEM_GROUP);
    }

    public static Identifier identifier(String path) {
        return new Identifier("reasonable-mc", path);
    }

    private static Supplier<ItemStack> groupIconSupplier() {
        return () -> new ItemStack(ITEM_KNIFE);
    }

    static {
        BLOCK_STONE_MORTAR = new StoneMortarBlock(FabricBlockSettings.copyOf(Blocks.STONE));
        BLOCK_MILL = new MillBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS));
        BLOCK_CHOPPING_BLOCK = new ChoppingBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
        BLOCK_APPLE_SAPLING = new PSaplingBlock(new AppleSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));

        CHOPPING_BLOCK_ENTITY_TYPE = MCUtil.newBlockEntityType(ChoppingBlockEntity::new, BLOCK_CHOPPING_BLOCK);

        ITEM_STONE_MORTAR = new BlockItem(BLOCK_STONE_MORTAR, groupped().maxCount(1));
        ITEM_MILL_ITEM = new BlockItem(BLOCK_MILL, groupped().maxCount(1));
        ITEM_CHOPPING_BLOCK = new BlockItem(BLOCK_CHOPPING_BLOCK, groupped());
        ITEM_APPLE_CORE = new AliasedBlockItem(BLOCK_APPLE_SAPLING, groupped());
    }
}
