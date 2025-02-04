package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.event.EventGetFluidRenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin  {


    @Inject(at = @At("TAIL"), remap = CitadelConstants.REMAPREFS, cancellable = true,
            method = "getRenderLayer(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/client/renderer/RenderType;")
    private static void citadel_getFluidRenderLayer(FluidState fluidState, CallbackInfoReturnable<RenderType> cir) {
        EventGetFluidRenderType event = new EventGetFluidRenderType(fluidState, cir.getReturnValue());
        var result = EventGetFluidRenderType.EVENT.invoker().onGetFluidRenderType(event);
        if(result.asMinecraft().consumesAction()){
            cir.setReturnValue(event.getRenderType());
        }
    }
}
