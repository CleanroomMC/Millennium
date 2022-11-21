package com.cleanroommc.millennium.content.v1_13;

import com.cleanroommc.millennium.content.v1_13.enchantment.EnchantmentChanneling;
import com.cleanroommc.millennium.content.v1_13.enchantment.EnchantmentImpaling;
import com.cleanroommc.millennium.content.v1_13.enchantment.EnchantmentLoyalty;
import com.cleanroommc.millennium.content.v1_13.enchantment.EnchantmentRiptide;
import com.cleanroommc.millennium.content.v1_13.item.ItemTrident;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.EnumAction;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class Version1_13 { // implements VersionModule

    // EnumActions
    public static EnumAction TRIDENT_ACTION;

    // EnumEnchantmentType
    public static EnumEnchantmentType TRIDENT_ENCHANTMENT_TYPE;

    // Enchantments
    public static Enchantment LOYALTY_ENCHANTMENT;
    public static Enchantment IMPALING_ENCHANTMENT;
    public static Enchantment RIPTIDE_ENCHANTMENT;
    public static Enchantment CHANNELING_ENCHANTMENT;

    public static void initTrident() {
        TRIDENT_ACTION = EnumHelper.addAction("TRIDENT");

        TRIDENT_ENCHANTMENT_TYPE = EnumHelper.addEnchantmentType("TRIDENT", ItemTrident.class::isInstance);
    }

    public static void initTridentEnchantments(IForgeRegistry<Enchantment> registry) {
        registry.register(LOYALTY_ENCHANTMENT = new EnchantmentLoyalty());
        registry.register(IMPALING_ENCHANTMENT = new EnchantmentImpaling());
        registry.register(RIPTIDE_ENCHANTMENT = new EnchantmentRiptide());
        registry.register(CHANNELING_ENCHANTMENT = new EnchantmentChanneling());
    }

}
