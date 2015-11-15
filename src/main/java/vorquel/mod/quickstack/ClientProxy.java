package vorquel.mod.quickstack;


import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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
