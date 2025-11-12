package training.g2.helper;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import training.g2.model.AttributeValue;

public class SkuHelper {

    private SkuHelper() {
    }

    public static String generateSku(String prefix, List<AttributeValue> values) {
        String prefixToken = toSkuToken(prefix);

        String tail = values.stream()
                .sorted(Comparator.comparing(v -> v.getAttribute().getId()))
                .map(AttributeValue::getValue)
                .map(SkuHelper::toSkuToken)
                .filter(t -> !t.isBlank())
                .collect(Collectors.joining("-"));

        if (prefixToken.isBlank())
            return tail;
        if (tail.isBlank())
            return prefixToken;

        return prefixToken + "-" + tail;
    }

    public static String toSkuToken(String input) {
        if (input == null || input.isBlank())
            return "";

        String s = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");

        s = s.replace('đ', 'd').replace('Đ', 'D');

        s = s.replaceAll("[^A-Za-z0-9]+", "-");

        s = s.replaceAll("^-+|-+$", "")
                .replaceAll("-{2,}", "-")
                .toUpperCase();

        return s;
    }
}
