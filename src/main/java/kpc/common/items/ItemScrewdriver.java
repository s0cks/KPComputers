package kpc.common.items;

import kpc.common.KPComputers;
import kpc.common.entity.EntityRobit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemScrewdriver
extends Item {
    public ItemScrewdriver(){
        super();
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("screwdriver");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(entity instanceof EntityRobit){
            if(player.isSneaking()){
                player.openGui(KPComputers.instance, 1, player.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
                return true;
            }
        }

        return false;
    }
}