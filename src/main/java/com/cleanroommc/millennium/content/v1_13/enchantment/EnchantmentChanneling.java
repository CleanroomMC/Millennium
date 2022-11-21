package com.cleanroommc.millennium.content.v1_13.enchantment;

import com.cleanroommc.millennium.content.v1_13.Version1_13;
import com.cleanroommc.millennium.util.Constants;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentChanneling extends Enchantment {

    public EnchantmentChanneling() {
        super(Rarity.VERY_RARE, Version1_13.TRIDENT_ENCHANTMENT_TYPE, Constants.MAINHAND_ONLY);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

}
