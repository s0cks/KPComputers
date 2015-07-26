package kpc.common.blocks;

import kpc.common.tile.TileEntityIOExpander;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class BlockIOExpander
extends BlockContainer {
    public BlockIOExpander(){
        super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("ioExpander");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityIOExpander();
    }
}