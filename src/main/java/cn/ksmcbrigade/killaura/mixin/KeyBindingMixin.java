package cn.ksmcbrigade.killaura.mixin;

import cn.ksmcbrigade.killaura.KillauraMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Inject(method = "onKeyPressed",at = @At("HEAD"))
    private static void key(int keyCode, CallbackInfo ci){
        if(keyCode== Keyboard.KEY_R){
            KillauraMod.enabled = !KillauraMod.enabled;
            if(MinecraftClient.getInstance().player !=null) MinecraftClient.getInstance().player.sendMessage(new LiteralText(I18n.translate("key.killaura.killaura")+": "+KillauraMod.enabled));
        }
    }
}
