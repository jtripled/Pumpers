package com.jtripled.pumpers.block;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.gui.GUIPump;
import com.jtripled.pumpers.tile.TilePump;
import com.jtripled.voxen.block.BlockBase;
import com.jtripled.voxen.gui.GUIHolder;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jtripled
 */
public class BlockPump extends BlockBase implements GUIHolder
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", (@Nullable EnumFacing face) -> face != EnumFacing.UP);
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    
    public BlockPump()
    {
        super(Pumpers.INSTANCE, "pump", Material.IRON);
        this.setTab(CreativeTabs.REDSTONE);
        this.setItem();
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, true));
        this.setTileClass(TilePump.class);
        this.setIgnoredProperties(new IProperty[] {ENABLED});
        this.setFullCube(false);
        this.setOpaque(false);
        this.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();
        if (enumfacing == EnumFacing.UP)
            enumfacing = EnumFacing.DOWN;
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, true);
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        this.updateState(world, pos, state);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.updateState(worldIn, pos, state);
    }

    private void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = !worldIn.isBlockPowered(pos);
        if (flag != ((boolean)state.getValue(ENABLED)))
            worldIn.setBlockState(pos, state.withProperty(ENABLED, flag), 4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(ENABLED, (meta & 8) != 8);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
        if (!((boolean)state.getValue(ENABLED)))
            i |= 8;
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, ENABLED});
    }
    
    @Override
    public ContainerPump getServerGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new ContainerPump((TilePump) world.getTileEntity(pos), player.inventory);
    }
    
    @Override
    public GUIPump getClientGUI(EntityPlayer player, World world, BlockPos pos)
    {
        return new GUIPump(getServerGUI(player, world, pos));
    }
}
