package vorquel.mod.quickstack;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class NullPlayer extends EntityPlayer {
    
    public NullPlayer(World world) {
        super(world, new GameProfile(new UUID(0, 0), ""));
    }
    
    @Override
    public boolean isSpectator() {
        return true;
    }
}
