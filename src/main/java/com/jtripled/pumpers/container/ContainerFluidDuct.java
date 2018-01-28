package com.jtripled.pumpers.container;

import com.jtripled.pumpers.tile.TileFluidDuct;
import com.jtripled.voxen.container.ContainerTile;
import net.minecraft.entity.player.InventoryPlayer;

/**
 *
 * @author jtripled
 */
public final class ContainerFluidDuct extends ContainerTile<TileFluidDuct>
{
    public ContainerFluidDuct(TileFluidDuct tile, InventoryPlayer inventory)
    {
        super(1, inventory, tile);
    }
}
