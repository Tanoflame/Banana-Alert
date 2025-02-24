package net.tanoflame.bananaalert.util;

import net.minecraft.util.Formatting;

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
}
