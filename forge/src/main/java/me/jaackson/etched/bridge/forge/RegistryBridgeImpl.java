package me.jaackson.etched.bridge.forge;

import me.jaackson.etched.Etched;
import me.jaackson.etched.bridge.RegistryBridge;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author Jackson
 */
public class RegistryBridgeImpl {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Etched.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Etched.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Etched.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Etched.MOD_ID);

    public static <T extends SoundEvent> Supplier<T> registerSound(String name, T object) {
        return SOUND_EVENTS.register(name, () -> object);
    }

    public static <T extends Item> Supplier<T> registerItem(String name, T object) {
        return ITEMS.register(name, () -> object);
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, T object) {
        return BLOCKS.register(name, () -> object);
    }

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, RegistryBridge.MenuFactory<T> object) {
        return MENU_TYPES.register(name, () -> new MenuType<>(object::create));
    }

    @SafeVarargs
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColor(ItemColor color, Supplier<Item>... items) {
        FMLJavaModLoadingContext.get().getModEventBus().<ColorHandlerEvent.Item>addListener(e -> {
            for (Supplier<Item> item : items) {
                e.getItemColors().register(color, item.get());
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemOverride(Item item, ResourceLocation resourceLocation, ItemPropertyFunction itemPropertyFunction) {
        ItemProperties.register(item, resourceLocation, itemPropertyFunction);
    }
}
