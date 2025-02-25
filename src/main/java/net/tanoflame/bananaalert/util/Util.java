package net.tanoflame.bananaalert.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util extends net.minecraft.util.Util{
    public static final List<Formatting> COLORS = Stream.of(Formatting.values())
            .filter(Formatting::isColor)
            .collect(Collectors.toList());
    
    public static <K, V, F> V findByField(Map<K, V> map, Function<V, F> fieldExtractor, F targetValue) {
        return map.values().stream()
                .filter(value -> fieldExtractor.apply(value).equals(targetValue))
                .findFirst()
                .orElse(null);
    }

    public static EntityHitResult raycastEntities(Entity camera, double maxRange, float tickDelta) {
        Vec3d startPos = camera.getCameraPosVec(tickDelta);
        Vec3d direction = camera.getRotationVec(tickDelta);
        Vec3d endPos = startPos.add(direction.multiply(maxRange));
        Box searchBox = camera.getBoundingBox().stretch(direction.multiply(maxRange)).expand(1.0, 1.0, 1.0);

        return ProjectileUtil.raycast(
                camera,
                startPos,
                endPos,
                searchBox,
                entity -> !entity.isSpectator() && entity.canHit(),
                maxRange * maxRange
        );
    }

}
