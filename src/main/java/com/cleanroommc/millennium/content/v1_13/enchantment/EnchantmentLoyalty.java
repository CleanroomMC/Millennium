package com.cleanroommc.millennium.content.v1_13.enchantment;

import com.cleanroommc.millennium.content.v1_13.Version1_13;
import com.cleanroommc.millennium.util.Constants;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentLoyalty extends Enchantment {

    public EnchantmentLoyalty() {
        super(Rarity.UNCOMMON, Version1_13.TRIDENT_ENCHANTMENT_TYPE, Constants.MAINHAND_ONLY);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + enchantmentLevel * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

}
