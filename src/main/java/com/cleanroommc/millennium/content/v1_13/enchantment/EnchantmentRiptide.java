package com.cleanroommc.millennium.content.v1_13.enchantment;

import com.cleanroommc.millennium.content.v1_13.Version1_13;
import com.cleanroommc.millennium.util.Constants;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentRiptide extends Enchantment {

    public EnchantmentRiptide() {
        super(Rarity.VERY_RARE, Version1_13.TRIDENT_ENCHANTMENT_TYPE, Constants.MAINHAND_ONLY);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment != Version1_13.LOYALTY_ENCHANTMENT && enchantment != Version1_13.CHANNELING_ENCHANTMENT;
    }

}
