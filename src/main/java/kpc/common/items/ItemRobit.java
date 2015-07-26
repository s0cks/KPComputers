package kpc.common.items;

import kpc.common.entity.EntityRobit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ItemRobit
extends Item {
    public ItemRobit() {
        super();
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("robit");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            EntityRobit robit = new EntityRobit(world);
            robit.setPosition(x, y, z);
            world.spawnEntityInWorld(robit);
            return true;
        }

        return false;
    }
}