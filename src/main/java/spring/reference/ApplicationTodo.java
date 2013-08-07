package spring.reference;

import spring.reference.meta.COMMON;
import spring.reference.meta.TODO;
import spring.reference.meta.TODOTag;

@COMMON
public final class ApplicationTodo {
    private ApplicationTodo() {
    }

    @TODO(tags = { TODOTag.MISSING_FEATURE, TODOTag.JPA_2_1 }, value = "Add example for @Converter. Probably Person.deleted is a good candidate.")
    public static final String JPA_CONVERTER = "JPA 2.1";

    @TODO(tags = { TODOTag.MISSING_FEATURE, TODOTag.JPA_2_1 }, value = "Add example for unsynchronized persistence context.")
    public static final String UNSYNCHRONIZED_PERSISTENCE_CONTEXT = "JPA 2.1";

    @TODO(tags = { TODOTag.MISSING_FEATURE, TODOTag.JPA_2_1 }, value = "Add example for schema generation.")
    public static final String SCHEMA_GENERATION = "JPA 2.1";
}
