package vorquel.mod.quickstack;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageQuickStack implements IMessage{
    
    public static final MessageQuickStack instance = new MessageQuickStack();
    
    @Override
    public void fromBytes(ByteBuf buf) {}
    
    @Override
    public void toBytes(ByteBuf buf) {}
    
    public static class Handler implements IMessageHandler<MessageQuickStack, IMessage> {
        
        @Override
        public IMessage onMessage(MessageQuickStack message, final MessageContext ctx) {
            MinecraftServer.getServer().addScheduledTask(new QuickStackTask(ctx.getServerHandler().playerEntity));
            return null;
        }
        
        private static class QuickStackTask implements Runnable {
            
            private EntityPlayerMP player;
            
            public QuickStackTask(EntityPlayerMP player) {
                this.player = player;
            }
            
            @Override
            public void run() {
                World world = player.worldObj;
                BlockPos pos = player.getPosition();
                int minX = pos.getX() - 5, maxX = pos.getX() + 5;
                int minY = pos.getY() - 5, maxY = pos.getY() + 5;
                int minZ = pos.getZ() - 5, maxZ = pos.getZ() + 5;
                for(int x = minX; x <= maxX; ++x)
                    for(int y = minY; y <= maxY; ++y)
                        for(int z = minZ; z <= maxZ; ++z)
                            quickStack(player.inventory.mainInventory, world, new BlockPos(x, y, z));
                player.inventory.markDirty();
            }
            
            private void quickStack(ItemStack[] stacks, World world, BlockPos invPos) {
                TileEntity te = world.getTileEntity(invPos);
                if(te instanceof IInventory)
                    for(int i = 10; i < stacks.length; ++i) {
                        if(stacks[i] != null)
                            stacks[i] = findEqual((IInventory) te, stacks[i]);
                        te.markDirty();
                    }
            }
            
            private ItemStack findEqual(IInventory inventory, ItemStack stack) {
                for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack currentStack = inventory.getStackInSlot(i);
                    if(equal(stack, currentStack))
                        return findPlacement(inventory, stack);
                }
                return stack;
            }
            
            private ItemStack findPlacement(IInventory inventory, ItemStack stack) {
                for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack currentStack = inventory.getStackInSlot(i);
                    if(currentStack == null) {
                        stack = placeStack(inventory, i, stack, 0);
                        if(stack == null)
                            return null;
                    }
                    if(equal(stack, currentStack)) {
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
    }
}
