package vorquel.mod.quickstack;


import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

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
    
    public static class QuickStackHandler {
    
        public KeyBinding keyBinding = new KeyBinding("Quick Stack", Keyboard.KEY_X, "Quick Stack");
    
        @SubscribeEvent
        public void quickStack(InputEvent.KeyInputEvent event) {
            if(!keyBinding.isPressed())
                return;
            QuickStack.network.sendToServer(MessageQuickStack.instance);
        }
    }
}
