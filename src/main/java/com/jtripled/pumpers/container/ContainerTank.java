package com.jtripled.pumpers.container;

import com.jtripled.pumpers.tile.TileTank;
import com.jtripled.voxen.container.ContainerTile;
import net.minecraft.entity.player.InventoryPlayer;

/**
 *
 * @author jtripled
 */
public class ContainerTank extends ContainerTile<TileTank>
{
    public ContainerTank(TileTank tile, InventoryPlayer inventory)
    {
        super(1, inventory, tile);
    }
}
