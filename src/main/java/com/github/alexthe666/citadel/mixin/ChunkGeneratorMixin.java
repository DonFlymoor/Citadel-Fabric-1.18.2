package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.event.EventMergeStructureSpawns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

    @Inject(at = @At("RETURN"), remap = CitadelConstants.REMAPREFS, cancellable = true,
            method = "getMobsAt(Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/util/random/WeightedRandomList;")
    private void citadel_getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        WeightedRandomList<MobSpawnSettings.SpawnerData> biomeSpawns = biome.value().getMobSettings().getMobs(mobCategory);
        if(biomeSpawns != cir.getReturnValue()){
            EventMergeStructureSpawns event = new EventMergeStructureSpawns(structureManager, pos, mobCategory, cir.getReturnValue(), biomeSpawns);
            var result = EventMergeStructureSpawns.EVENT.invoker().onMergeStructureSpawns(event);
            if(result.asMinecraft().consumesAction()){
                cir.setReturnValue(event.getStructureSpawns());
            }
        }
    }

}
