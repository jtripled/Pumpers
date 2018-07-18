package com.jtripled.pumpers.container;

import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.voxen.container.ContainerTile;
import com.jtripled.voxen.container.ContainerTileItemSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 *
 * @author jtripled
 */
public final class ContainerPump extends ContainerTile<TilePump>
{
    public ContainerPump(TilePump tile, InventoryPlayer inventory)
    {
        super(2, inventory, tile);
        addSlotToContainer(new ContainerTileItemSlot<>(tile, tile.getBucketInput(), 0, 55, 36));
        addSlotToContainer(new ContainerTileItemSlot<TilePump>(tile, tile.getBucketInput(), 0, 105, 36)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });
    }
}
