package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting;

import java.lang.reflect.Method;
import java.util.Comparator;

public final class ComparableReader {

    private ComparableReader() {
        // prevent instantiation
    }

    @SuppressWarnings({"rawtypes"})
    public static Comparable<Object> readComparable(Method accessor, Object target) {
        Object raw = readRaw(accessor, target);
        Comparable<Object> comparable = null;
        if ( raw != null ) {
            comparable = switch (raw) {
                case String s -> (Comparable) s;
                case Enum e -> (Comparable) e.name();
                case Comparable c -> c;
                default -> (Comparable) raw.toString();
            };
        }
        return comparable;
    }

    private static Object readRaw(Method accessor, Object target) {
        try {
            return accessor.invoke(target);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access sort field: " + accessor.getName(), e);
        }
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static Comparator<Object> comparableComparatorFor(Class<?> returnType) {
        return Comparator.nullsLast(
                returnType == String.class
                        ? (o1, o2) -> ((String) o1).compareToIgnoreCase((String) o2)
                        : (o1, o2) -> ((Comparable) o1).compareTo(o2)
        );
    }

}
