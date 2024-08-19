package cn.ksmcbrigade.killaura.mixin;

import cn.ksmcbrigade.killaura.KillauraMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayerEntity {
    @Shadow protected MinecraftClient client;

    public ClientPlayerMixin(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public abstract void swingHand();

    @Inject(method = "tick",at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        if(!KillauraMod.enabled) return;
        Entity entity = this.client.world.getEntitiesByClass(Entity.class,new Box(this.getBlockPos(),this.getBlockPos()).expand(KillauraMod.reach,KillauraMod.reach,KillauraMod.reach), this.client.player);
        if (entity != null && entity.isAttackable()) {
            for (int i = 0; i < KillauraMod.times; i++) {
                try {
                    this.networkHandler.sendPacket(new PlayerInteractEntityC2SPacket(entity, PlayerInteractEntityC2SPacket.Type.ATTACK));
                    if(KillauraMod.swing){
                        this.swingHand();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
