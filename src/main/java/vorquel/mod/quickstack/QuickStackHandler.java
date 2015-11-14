package vorquel.mod.quickstack;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class QuickStackHandler {

    public KeyBinding keyBinding = new KeyBinding("Quick Stack", Keyboard.KEY_X, "Quick Stack");

    @SubscribeEvent
    public void quickStack(InputEvent.KeyInputEvent event) {
        if(!keyBinding.isPressed())
            return;
    }
}
