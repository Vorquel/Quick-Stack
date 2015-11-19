package vorquel.mod.quickstack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageQuickStack implements IMessage{
    
    public static final MessageQuickStack instance = new MessageQuickStack();
    
    @Override
    public void fromBytes(ByteBuf buf) {}
    
    @Override
    public void toBytes(ByteBuf buf) {}
    
    public static class Handler implements IMessageHandler<MessageQuickStack, IMessage> {
        
        @Override
        public IMessage onMessage(MessageQuickStack message, final MessageContext ctx) {
            new QuickStackTask(ctx.getServerHandler().playerEntity).run();
            return null;
        }
    }
}
