package vorquel.mod.quickstack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "QuickStack", name = "Quick Stack", useMetadata = true)
public class QuickStack {
    
    @Mod.Instance
    public static QuickStack quickStack;
    
    @SidedProxy(serverSide = "vorquel.mod.quickstack.Proxy", clientSide = "vorquel.mod.quickstack.ClientProxy")
    public static Proxy proxy;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
