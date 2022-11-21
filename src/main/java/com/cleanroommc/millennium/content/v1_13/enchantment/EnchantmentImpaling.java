package com.cleanroommc.millennium.content.v1_13.enchantment;

import com.cleanroommc.millennium.content.v1_13.Version1_13;
import com.cleanroommc.millennium.util.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EnumCreatureAttribute;

public class EnchantmentImpaling extends Enchantment {

    public EnchantmentImpaling() {
        super(Rarity.VERY_RARE, Version1_13.TRIDENT_ENCHANTMENT_TYPE, Constants.MAINHAND_ONLY);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        // if (EnumCreatureAttribute creatureType == EnumCreatureAttribute.AQUATIC) { TODO: Make EnumCreatureAttribute OCEANIC
        //     return level * 2.5F;
        // }
        return 0.0F;
    }

}
