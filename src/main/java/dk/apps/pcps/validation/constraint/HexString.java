package dk.apps.pcps.validation.constraint;

import dk.apps.pcps.validation.impl.HexStringImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HexStringImpl.class)
public @interface HexString {
    String message() default "hex string is bad";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
