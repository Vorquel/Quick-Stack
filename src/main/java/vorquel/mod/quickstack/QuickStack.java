package vorquel.mod.quickstack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
