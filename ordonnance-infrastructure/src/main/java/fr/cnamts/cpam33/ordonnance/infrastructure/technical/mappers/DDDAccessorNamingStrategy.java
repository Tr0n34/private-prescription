package fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers;

import org.mapstruct.ap.spi.AccessorNamingStrategy;
import org.mapstruct.ap.spi.MethodType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Cette classe redéfinit la stratégie de génération des <class>Mapper</class> masptruct.<br/>
 * Il n'est pas possible de la modifier par des options de mapstruct alors on utilise le mécanisme de Java SPI.<br/>
 * Warning : Attention à l'annotation processor qui va générer avant la compilation qu'il faut désactiver.
 */
public class DDDAccessorNamingStrategy implements AccessorNamingStrategy {

    public static final String GET_PREFIX = "get";
    public static final String IS_PREFIX = "is";
    public static final String SET_PREFIX = "set";

    @Override
    public MethodType getMethodType(ExecutableElement method) {
        MethodType result = MethodType.OTHER;
        String name = method.getSimpleName().toString();
        if ( !isIgnoredObjectMethod(name) ) {
            boolean noParams = hasNoParameters(method);
            boolean oneParam = hasOneParameter(method);
            boolean returnsVoid = returnsVoid(method);
            if ( isStandardGetter(method, name, noParams, returnsVoid)
                    || isFieldBackedGetter(method, name, noParams, returnsVoid) ) {
                result = MethodType.GETTER;
            } else if ( isStandardSetter(method, name, oneParam, returnsVoid) ) {
                result = MethodType.SETTER;
            }
        }
        return result;
    }

    private static boolean isIgnoredObjectMethod(String name) {
        return "getClass".equals(name) || "hashCode".equals(name) || "toString".equals(name) || "equals".equals(name);
    }

    private static boolean hasNoParameters(ExecutableElement method) {
        return method.getParameters().isEmpty();
    }

    private static boolean hasOneParameter(ExecutableElement method) {
        return method.getParameters().size() == 1;
    }

    private static boolean returnsVoid(ExecutableElement method) {
        return method.getReturnType().getKind() == TypeKind.VOID;
    }

    private boolean isStandardGetter(ExecutableElement method, String name, boolean noParams, boolean returnsVoid) {
        boolean getter = false;
        if ( noParams && !returnsVoid ) {
            if ( name.startsWith(GET_PREFIX) && name.length() > 3 ) {
                getter = true;
            } else if (name.startsWith(IS_PREFIX) && name.length() > 2 ) {
                getter = true;
            }
        }
        return getter;
    }

    private boolean isFieldBackedGetter(ExecutableElement method, String name, boolean noParams, boolean returnsVoid) {
        return noParams && !returnsVoid && isFieldBackedProperty(method, name);
    }

    private boolean isStandardSetter(ExecutableElement method, String name, boolean oneParam, boolean returnsVoid) {
        boolean setter = false;
        if ( oneParam && name.startsWith(SET_PREFIX) && name.length() > 3 ) {
            if (returnsVoid || returnsEnclosingType(method)) {
                setter = true;
            }
        }
        return setter;
    }

    @Override
    public String getPropertyName(ExecutableElement method) {
        String name = method.getSimpleName().toString();
        int prefix = propertyPrefixLength(name);
        return prefix == 0
                ? name
                : decapitalize(name.substring(prefix));
    }

    private static int propertyPrefixLength(String name) {
        int prefix = 0;
        if ( (name.startsWith(GET_PREFIX) || name.startsWith(SET_PREFIX)) && name.length() > 3 ) {
            prefix = 3;
        } else if ( name.startsWith(IS_PREFIX) && name.length() > 2 ) {
            prefix = 2;
        }
        return prefix;
    }

    @Override
    public String getElementName(ExecutableElement element) {
        return element.getSimpleName().toString();
    }

    @Override
    public String getCollectionGetterName(String property) {
        return GET_PREFIX + capitalize(property);
    }

    private static boolean returnsEnclosingType(ExecutableElement method) {
        return method.getEnclosingElement() instanceof TypeElement te
                && te.getQualifiedName().contentEquals(method.getReturnType().toString());
    }

    private static boolean isFieldBackedProperty(ExecutableElement method, String propertyName) {
        return method.getEnclosingElement() instanceof TypeElement type
                && type.getEnclosedElements().stream()
                .filter(VariableElement.class::isInstance)
                .map(VariableElement.class::cast)
                .anyMatch(ve -> ve.getSimpleName().contentEquals(propertyName));
    }

    private static String decapitalize(String s) {
        boolean isDecapitalized = s == null
                || s.isEmpty()
                || (s.length() > 1
                && Character.isUpperCase(s.charAt(0))
                && Character.isUpperCase(s.charAt(1)));
        return isDecapitalized
                ? s
                : Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private static String capitalize(String s) {
        boolean isCapitalized = (s == null || s.isEmpty());
        return isCapitalized
                ? s
                : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
