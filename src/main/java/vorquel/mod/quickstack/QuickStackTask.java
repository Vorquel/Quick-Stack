package vorquel.mod.quickstack;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

class QuickStackTask implements Runnable {
    
    private EntityPlayerMP player;
    
    public QuickStackTask(EntityPlayerMP player) {
        this.player = player;
    }
    
    @Override
    public void run() {
        World world = player.worldObj;
        for(IInventory inventory : nearbyInventories(world, player, player.posX, player.posY, player.posZ))
            quickStack(player.inventory.mainInventory, inventory);
        player.inventory.markDirty();
    }
    
    private List<IInventory> nearbyInventories(World world, EntityPlayerMP player, double posX, double posY, double posZ) {
        int minX = (int) (posX - 5), maxX = (int) (posX + 5);
        int minY = (int) (posY - 5), maxY = (int) (posY + 5);
        int minZ = (int) (posZ - 5), maxZ = (int) (posZ + 5);
        List<IInventory> list = Lists.newArrayList();
        for(int x = minX; x <= maxX; ++x)
            for(int y = minY; y <= maxY; ++y)
                for(int z = minZ; z <= maxZ; ++z) {
                    TileEntity te = world.getTileEntity(x, y, z);
                    if(te instanceof IInventory && ((IInventory) te).isUseableByPlayer(player))
                        list.add((IInventory) te);
                }
        return list;
    }
    
    private void quickStack(ItemStack[] stacks, IInventory inventory) {
        for(int i = 9; i < stacks.length; ++i) {
            if(stacks[i] != null)
                stacks[i] = findEqual(inventory, stacks[i]);
        }
    }
    
    private ItemStack findEqual(IInventory inventory, ItemStack stack) {
        int size = inventory.getSizeInventory();
        for(int i = 0; i < size; ++i) {
            ItemStack currentStack = inventory.getStackInSlot(i);
            if(equal(stack, currentStack))
                return findPlacement(inventory, stack);
        }
        return stack;
    }
    
    private ItemStack findPlacement(IInventory inventory, ItemStack stack) {
        int size = inventory.getSizeInventory();
        for(int i = 0; i < size; ++i) {
            ItemStack currentStack = inventory.getStackInSlot(i);
            if(currentStack == null && inventory.isItemValidForSlot(i, stack)) {
                stack = placeStack(inventory, i, stack, 0);
                if(stack == null)
                    return null;
            } else if(equal(stack, currentStack) && inventory.getInventoryStackLimit() != currentStack.stackSize) {
                stack = placeStack(inventory, i, stack, currentStack.stackSize);
                if(stack == null)
                    return null;
            }
        }
        return stack;
    }
    
    private boolean equal(ItemStack left, ItemStack right) {
        if(right == null)
            return false;
        NBTTagCompound leftTag = left.getTagCompound(), rightTag = right.getTagCompound();
        return left.getItem() == right.getItem() && left.getMetadata() == right.getMetadata()
                       && (leftTag == null && rightTag == null || leftTag != null && leftTag.equals(rightTag));
    }
    
    private ItemStack placeStack(IInventory inventory, int index, ItemStack stack, int offset) {
        int max = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
        int newSize = Math.min(max, offset + stack.stackSize);
        inventory.setInventorySlotContents(index, stack.splitStack(newSize));
        stack.stackSize += offset;
        if(stack.stackSize > 0)
            return stack;
        else
            return null;
    }
}
