package vorquel.mod.quickstack;


import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends Proxy {
    
    QuickStackHandler handler = new QuickStackHandler();
    
    @Override
    public void preInit() {
        ClientRegistry.registerKeyBinding(handler.keyBinding);
    }
    
    @Override
    public void init() {
        FMLCommonHandler.instance().bus().register(handler);
    }
}
