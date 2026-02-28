package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting;

import java.lang.reflect.Method;
import java.util.Comparator;

public final class ComparableReader {

    @SuppressWarnings("rawtypes")
    public static Comparable<?> readComparable(Method accessor, Object target) {
        Object v = readRaw(accessor, target);
        return v == null ? null :
                v instanceof String s ? s :
                        v instanceof Enum<?> e ? e.name() :
                                v instanceof Comparable<?> c ? c :
                                        v.toString();
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
