package vorquel.mod.quickstack;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
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
    }
}
