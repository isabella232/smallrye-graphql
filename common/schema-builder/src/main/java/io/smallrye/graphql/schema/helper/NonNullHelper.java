package io.smallrye.graphql.schema.helper;

import org.jboss.jandex.Type;
import org.jboss.logging.Logger;

import io.smallrye.graphql.schema.Annotations;
import io.smallrye.graphql.schema.Classes;

/**
 * Helping to figure out of some should be marked as Non null
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class NonNullHelper {
    private static final Logger LOG = Logger.getLogger(NonNullHelper.class.getName());

    private NonNullHelper() {
    }

    /**
     * Check if we should mark a certain type as non null.
     * 
     * @param type the type
     * @param annotations the applicable annotations
     * @return true if we should
     */
    public static boolean markAsNonNull(Type type, Annotations annotations) {
        return markAsNonNull(type, annotations, false);
    }

    /**
     * Check if we should mark a certain type as non null.
     * 
     * By default primitives is non null.
     * 
     * @param type the type
     * @param annotations the applicable annotations
     * @param ignorePrimitiveCheck ignore the primitive rule
     * @return true if we should
     */
    public static boolean markAsNonNull(Type type, Annotations annotations, boolean ignorePrimitiveCheck) {
        // check if the @NonNull annotation is present
        boolean hasNonNull = hasNonNull(annotations) || hasNonNullOnClassOrPackage(annotations);

        if (Classes.isOptional(type) || hasNullable(annotations)) {
            hasNonNull = false;
        }

        // true if this is a primitive
        if (!ignorePrimitiveCheck && type.kind().equals(Type.Kind.PRIMITIVE)) {
            hasNonNull = true; // By implication
        }

        // check if the @DefaultValue annotation is present
        boolean hasDefaultValue = hasDefaultValue(annotations);
        if (hasDefaultValue) {
            if (hasNonNull && !type.kind().equals(Type.Kind.PRIMITIVE)) {
                LOG.warn("Ignoring non null on [" + type.name() + "] as there is a @DefaultValue");
            }
            return false;
        }

        return hasNonNull;
    }

    private static boolean hasNonNull(Annotations annotations) {

        return annotations.containsOneOfTheseAnnotations(Annotations.NON_NULL,
                Annotations.BEAN_VALIDATION_NOT_NULL,
                Annotations.BEAN_VALIDATION_NOT_EMPTY,
                Annotations.BEAN_VALIDATION_NOT_BLANK,
                Annotations.KOTLIN_NOT_NULL);
    }

    private static boolean hasNullable(Annotations annotations) {
        return annotations.containsOneOfTheseAnnotations(Annotations.NULLABLE);
    }

    private static boolean hasNonNullOnClassOrPackage(Annotations annotations) {
        return annotations.containsOneOfTheseInheritableAnnotations(Annotations.DEFAULT_NON_NULL);
    }

    private static boolean hasDefaultValue(Annotations annotations) {
        return annotations.containsKeyAndValidValue(Annotations.DEFAULT_VALUE);
    }
}
