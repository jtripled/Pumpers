package com.jtripled.pumpers.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

/**
 *
 * @author jtripled
 */
public class ItemBlockTank extends ItemBlock
{
    public ItemBlockTank(Block block)
    {
        super(block);
        this.setUnlocalizedName(block.getUnlocalizedName());
        this.setRegistryName(block.getRegistryName());
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
}
