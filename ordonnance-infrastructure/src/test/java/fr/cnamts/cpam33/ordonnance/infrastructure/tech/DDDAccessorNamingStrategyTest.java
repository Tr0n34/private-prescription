package fr.cnamts.cpam33.ordonnance.infrastructure.tech;

import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.DDDAccessorNamingStrategyHelper;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.DDDAccessorNamingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.ap.spi.MethodType;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.util.List;

import static fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.DDDAccessorNamingStrategyHelper.*;
import static fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.DDDAccessorNamingStrategyHelper.methodBasic;
import static fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.DDDAccessorNamingStrategyHelper.methodFieldBackedGetter;
import static fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.DDDAccessorNamingStrategyHelper.typeElementWithFields;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DDDAccessorNamingStrategyTest {

    private final DDDAccessorNamingStrategy strategy = new DDDAccessorNamingStrategy();

    @Test
    void should_ignore_object_methods() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("toString", TypeKind.DECLARED)));
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("hashCode", TypeKind.INT)));
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("equals", TypeKind.BOOLEAN)));
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("getClass", TypeKind.DECLARED)));
    }

    @Test
    void should_detect_standard_getter_getX() {
        assertEquals(MethodType.GETTER, strategy.getMethodType(methodBasic("getNom", 0, TypeKind.DECLARED)));
    }

    @Test
    void should_detect_standard_getter_isX() {
        assertEquals(MethodType.GETTER, strategy.getMethodType(methodBasic("isActif", 0, TypeKind.BOOLEAN)));
    }

    @Test
    void should_not_detect_getter_when_returns_void() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("getNom", 0, TypeKind.VOID)));
    }

    @Test
    void should_detect_standard_setter_void() {
        assertEquals(MethodType.SETTER, strategy.getMethodType(methodBasic("setNom", 1, TypeKind.VOID)));
    }

    @Test
    void should_detect_fluent_setter_returning_enclosing_type() {
        TypeElement enclosing = DDDAccessorNamingStrategyHelper.typeElementQualified("com.acme.PatientEntity");
        ExecutableElement m = DDDAccessorNamingStrategyHelper.methodFluentSetter("setNom", enclosing, "com.acme.PatientEntity");
        assertEquals(MethodType.SETTER, strategy.getMethodType(m));
    }

    @Test
    void should_not_detect_setter_when_one_param_but_returns_other_type() {
        TypeElement enclosing = typeElementQualified("com.acme.PatientEntity");
        ExecutableElement m = methodFluentSetter("setNom", enclosing, "java.lang.String");
        assertEquals(MethodType.OTHER, strategy.getMethodType(m));
    }

    @Test
    void should_detect_field_backed_getter_when_method_name_matches_a_field() {
        TypeElement enclosing = typeElementWithFields(List.of("nom", "prenom"));
        ExecutableElement m = methodFieldBackedGetter("nom", enclosing, TypeKind.DECLARED, 0);
        assertEquals(MethodType.GETTER, strategy.getMethodType(m));
    }

    @Test
    void should_not_detect_field_backed_getter_when_field_does_not_exist() {
        TypeElement enclosing = typeElementWithFields(List.of("prenom"));
        ExecutableElement m = methodFieldBackedGetter("nom", enclosing, TypeKind.DECLARED, 0);
        assertEquals(MethodType.OTHER, strategy.getMethodType(m));
    }

    @Test
    void should_extract_property_name_for_getter() {
        assertEquals("nom", strategy.getPropertyName(methodForPropertyName("getNom")));
    }

    @Test
    void should_extract_property_name_for_boolean_is_getter() {
        assertEquals("actif", strategy.getPropertyName(methodForPropertyName("isActif")));
    }

    @Test
    void should_keep_acronym_as_is_when_decapitalizing() {
        assertEquals("URL", strategy.getPropertyName(methodForPropertyName("getURL")));
    }

    @Test
    void should_return_name_itself_when_no_prefix() {
        assertEquals("nom", strategy.getPropertyName(methodForPropertyName("nom")));
    }

    @Test
    void should_build_collection_getter_name() {
        assertEquals("getItems", strategy.getCollectionGetterName("items"));
    }

    @Test
    void should_capitalize_only_first_letter_for_collection_getter_name() {
        assertEquals("getURL", strategy.getCollectionGetterName("uRL"));
    }

    @Test
    void should_not_detect_getter_when_name_is_only_get() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("get", TypeKind.DECLARED)));
    }

    @Test
    void should_not_detect_getter_when_name_is_only_is() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("is", TypeKind.BOOLEAN)));
    }

    @Test
    void should_not_detect_setter_when_name_is_only_set() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("set", TypeKind.VOID)));
    }

    @Test
    void should_not_detect_getter_when_getter_has_parameters() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("getNom", 1, TypeKind.DECLARED)));
    }

    @Test
    void should_not_detect_is_getter_when_has_parameters() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("isActif", 1, TypeKind.BOOLEAN)));
    }

    @Test
    void should_not_detect_setter_when_has_no_parameter() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("setNom", 0, TypeKind.VOID)));
    }

    @Test
    void should_not_detect_setter_when_has_two_parameters() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("setNom", 2, TypeKind.VOID)));
    }

    @Test
    void should_not_detect_setter_when_returns_non_void_and_not_enclosing_type() {
        TypeElement enclosing = typeElementQualified("com.acme.PatientEntity");
        ExecutableElement m = methodFluentSetter("setNom", enclosing, "java.lang.Integer");
        assertEquals(MethodType.OTHER, strategy.getMethodType(m));
    }

    @Test
    void should_not_detect_getter_when_returns_void_even_if_prefix_matches() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("getNom", 0, TypeKind.VOID)));
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodBasic("isActif", 0, TypeKind.VOID)));
    }

    @Test
    void should_not_detect_field_backed_getter_when_method_has_parameters() {
        TypeElement enclosing = typeElementWithFields(List.of("nom"));
        ExecutableElement m = methodFieldBackedGetter("nom", enclosing, TypeKind.DECLARED, 1);
        assertEquals(MethodType.OTHER, strategy.getMethodType(m));
    }

    @Test
    void should_not_detect_field_backed_getter_when_returns_void() {
        TypeElement enclosing = typeElementWithFields(List.of("nom"));
        ExecutableElement m = methodFieldBackedGetter("nom", enclosing, TypeKind.VOID, 0);
        assertEquals(MethodType.OTHER, strategy.getMethodType(m));
    }

    @Test
    void should_not_detect_random_method_as_accessor() {
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("computeHash", TypeKind.DECLARED)));
        assertEquals(MethodType.OTHER, strategy.getMethodType(methodSafe("withNom", TypeKind.DECLARED)));
    }

    // ---------------------------
    // Faux positifs
    // ---------------------------

    @Test
    void should_get_property_name_as_is_when_prefix_too_short() {
        assertEquals("get", strategy.getPropertyName(methodForPropertyName("get")));
        assertEquals("is", strategy.getPropertyName(methodForPropertyName("is")));
        assertEquals("set", strategy.getPropertyName(methodForPropertyName("set")));
    }

    @Test
    void should_not_strip_prefix_when_name_is_getX_but_too_short_after_prefix() {
        assertEquals("a", strategy.getPropertyName(methodForPropertyName("getA")));
    }

    @Test
    void should_keep_original_when_no_prefix_even_if_starts_similarly() {
        assertEquals("terNom", strategy.getPropertyName(methodForPropertyName("getterNom")));
    }



}
