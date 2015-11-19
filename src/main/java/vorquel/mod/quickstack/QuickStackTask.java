package vorquel.mod.quickstack;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("unchecked")
class QuickStackTask implements Runnable {
    
    private EntityPlayerMP player;
    private NullPlayer nullPlayer;
    
    public QuickStackTask(EntityPlayerMP player) {
        this.player = player;
        nullPlayer = new NullPlayer(player.worldObj);
    }
    
    @Override
    public void run() {
        World world = player.worldObj;
        for(IInteractionObject interactionObject : nearbyInteractiveObjects(world, player.getPosition()))
            quickStack(player.inventory.mainInventory, player, interactionObject);
        player.inventory.markDirty();
    }
    
    private List<IInteractionObject> nearbyInteractiveObjects(World world, BlockPos pos) {
        int minX = pos.getX() - 5, maxX = pos.getX() + 5;
        int minY = pos.getY() - 5, maxY = pos.getY() + 5;
        int minZ = pos.getZ() - 5, maxZ = pos.getZ() + 5;
        List<IInteractionObject> list = Lists.newArrayList();
        for(int x = minX; x <= maxX; ++x)
            for(int y = minY; y <= maxY; ++y)
                for(int z = minZ; z <= maxZ; ++z) {
                    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                    if(te instanceof IInventory && te instanceof IInteractionObject)
                        list.add((IInteractionObject) te);
                }
        return list;
    }
    
    private void quickStack(ItemStack[] stacks, EntityPlayerMP player, IInteractionObject interactionObject) {
        Container container = interactionObject.createContainer(nullPlayer.inventory, nullPlayer);
        for(int i = 9; i < stacks.length; ++i) {
            if(stacks[i] != null)
                stacks[i] = findEqual(container, stacks[i]);
        }
    }
    
    private ItemStack findEqual(Container container, ItemStack stack) {
        for(Slot slot : (List<Slot>) container.inventorySlots) {
            if(slot.inventory instanceof InventoryPlayer)
                continue;
            ItemStack currentStack = slot.getStack();
            if(equal(stack, currentStack))
                return findPlacement(container, stack);
        }
        return stack;
    }
    
    private ItemStack findPlacement(Container container, ItemStack stack) {
        for(Slot slot : (List<Slot>) container.inventorySlots) {
            if(slot.inventory instanceof InventoryPlayer)
                continue;
            ItemStack currentStack = slot.getStack();
            if(currentStack == null && slot.isItemValid(stack)) {
                stack = placeStack(slot, stack, 0);
                if(stack == null)
                    return null;
            } else if(equal(stack, currentStack) && slot.getSlotStackLimit() != currentStack.stackSize) {
                stack = placeStack(slot, stack, currentStack.stackSize);
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
    
    private ItemStack placeStack(Slot slot, ItemStack stack, int offset) {
        int max = Math.min(slot.getItemStackLimit(stack), stack.getMaxStackSize());
        int newSize = Math.min(max, offset + stack.stackSize);
        slot.putStack(stack.splitStack(newSize));
        stack.stackSize += offset;
        if(stack.stackSize > 0)
            return stack;
        else
            return null;
    }
}
