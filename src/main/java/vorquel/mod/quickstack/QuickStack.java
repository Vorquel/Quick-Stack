package vorquel.mod.quickstack;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "QuickStack", name = "Quick Stack", useMetadata = true)
public class QuickStack {
    
    @Mod.Instance
    public static QuickStack quickStack;
    
    @SidedProxy(serverSide = "vorquel.mod.quickstack.Proxy", clientSide = "vorquel.mod.quickstack.ClientProxy")
    public static Proxy proxy;
    
    public static SimpleNetworkWrapper network;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        network = NetworkRegistry.INSTANCE.newSimpleChannel("QuickStack");
        network.registerMessage(MessageQuickStack.Handler.class, MessageQuickStack.class, 0, Side.SERVER);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
