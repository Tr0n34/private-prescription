package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.helpers;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DDDAccessorNamingStrategyHelper {

    // ============================================================
    // Helpers (Mockito stubs) -> STRICT-STUBS FRIENDLY
    // ============================================================

    /**
     * SAFE: returnType stubbé en lenient uniquement.
     */
    public static ExecutableElement methodSafe(String name, TypeKind returnKind) {
        ExecutableElement m = mock(ExecutableElement.class);
        when(m.getSimpleName()).thenReturn(nameOf(name));

        TypeMirror returnType = mock(TypeMirror.class);
        lenient().when(returnType.getKind()).thenReturn(returnKind);
        lenient().when(m.getReturnType()).thenReturn(returnType);

        return m;
    }

    /**
     * Minimal for getPropertyName(): ONLY getSimpleName().
     */
    public static ExecutableElement methodForPropertyName(String name) {
        ExecutableElement m = mock(ExecutableElement.class);
        when(m.getSimpleName()).thenReturn(nameOf(name));
        return m;
    }

    /**
     * Accessor detection pour chemins "normaux" (params + returnType utilisés).
     * IMPORTANT: ne stubbe getParameters() que si paramCount != 0 (évite UnnecessaryStubbing).
     */
    public static ExecutableElement methodBasic(String name, int paramCount, TypeKind returnKind) {
        ExecutableElement m = mock(ExecutableElement.class);

        when(m.getSimpleName()).thenReturn(nameOf(name));
        if (paramCount != 0) {
            doReturn(paramsList(paramCount)).when(m).getParameters();
        }

        TypeMirror returnType = mock(TypeMirror.class);
        when(returnType.getKind()).thenReturn(returnKind);
        when(m.getReturnType()).thenReturn(returnType);

        return m;
    }

    /**
     * Fluent setter.
     */
    public static ExecutableElement methodFluentSetter(String name, TypeElement enclosing, String returnTypeToString) {
        ExecutableElement m = mock(ExecutableElement.class);

        when(m.getSimpleName()).thenReturn(nameOf(name));
        when(m.getEnclosingElement()).thenReturn(enclosing);
        doReturn(paramsList(1)).when(m).getParameters();

        TypeMirror returnType = mock(TypeMirror.class);
        when(returnType.getKind()).thenReturn(TypeKind.DECLARED);
        when(returnType.toString()).thenReturn(returnTypeToString);
        when(m.getReturnType()).thenReturn(returnType);

        return m;
    }

    /**
     * Field-backed getter.
     * IMPORTANT: ne stubbe getParameters() que si paramCount != 0 (évite UnnecessaryStubbing).
     */
    public static ExecutableElement methodFieldBackedGetter(
            String name,
            TypeElement enclosingWithFields,
            TypeKind returnKind,
            int paramCount
    ) {
        ExecutableElement m = mock(ExecutableElement.class);

        when(m.getSimpleName()).thenReturn(nameOf(name));
        lenient().when(m.getEnclosingElement()).thenReturn(enclosingWithFields);
        if (paramCount != 0) {
            doReturn(paramsList(paramCount)).when(m).getParameters();
        }

        TypeMirror returnType = mock(TypeMirror.class);
        when(returnType.getKind()).thenReturn(returnKind);
        when(m.getReturnType()).thenReturn(returnType);

        return m;
    }

    /**
     * Used ONLY by fluent setter tests.
     */
    public static TypeElement typeElementQualified(String qualifiedName) {
        TypeElement te = mock(TypeElement.class);
        when(te.getQualifiedName()).thenReturn(nameOf(qualifiedName));
        return te;
    }

    /**
     * Used ONLY by field-backed getter tests.
     *
     * CORRECTIF: plus de mocks Mockito dans la lambda -> plus de stubbings "inutiles".
     * On fournit de vrais VariableElement minimalistes (FakeVariableElement).
     */
    public static TypeElement typeElementWithFields(List<String> fieldNames) {
        TypeElement te = mock(TypeElement.class);

        List<Element> enclosed = new ArrayList<>(fieldNames.size());
        for (String fn : fieldNames) {
            enclosed.add((Element) new FakeVariableElement(fn));
        }

        // IMPORTANT: getEnclosedElements() retourne List<? extends Element>
        // => when(...).thenReturn(...) casse à cause des wildcards
        lenient().doReturn(enclosed).when(te).getEnclosedElements();

        return te;
    }

    private static List<VariableElement> paramsList(int count) {
        if (count <= 0) return List.of();

        List<VariableElement> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(mock(VariableElement.class));
        }
        return list;
    }

    private static Name nameOf(String s) {
        return new Name() {
            @Override public boolean contentEquals(CharSequence cs) { return s.contentEquals(cs); }
            @Override public int length() { return s.length(); }
            @Override public char charAt(int index) { return s.charAt(index); }
            @Override public CharSequence subSequence(int start, int end) { return s.subSequence(start, end); }
            @Override public String toString() { return s; }
        };
    }

    /**
     * VariableElement minimal non-mocké (évite Mockito strict-stubs).
     * On n’implémente "vraiment" que getSimpleName() et getKind().
     * Le reste retourne des valeurs neutres "safe".
     */
    private static final class FakeVariableElement implements VariableElement {

        private final Name name;

        private FakeVariableElement(String fieldName) {
            this.name = nameOf(fieldName);
        }

        @Override public Name getSimpleName() { return name; }

        @Override public ElementKind getKind() { return ElementKind.FIELD; }

        // --- Non utilisés par ta strategy : impl "safe defaults" ---
        @Override public Object getConstantValue() { return null; }
        @Override public TypeMirror asType() { return null; }
        @Override public List<? extends Element> getEnclosedElements() { return List.of(); }
        @Override public Element getEnclosingElement() { return null; }
        @Override public List<? extends javax.lang.model.element.AnnotationMirror> getAnnotationMirrors() { return List.of(); }
        @Override public <A extends Annotation> A getAnnotation(Class<A> annotationType) { return null; }
        @Override public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) { return null; }
        @Override public Set<Modifier> getModifiers() { return Set.of(); }

        @Override
        public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            return v.visitVariable(this, p);
        }
    }

}
