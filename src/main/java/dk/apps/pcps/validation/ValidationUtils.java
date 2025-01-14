package dk.apps.pcps.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtils {

    Validator validator;

    @Autowired
    public ValidationUtils(Validator validator){
        this.validator = validator;
    }

    public void validate(Object o){
        Set result = validator.validate(o);
        if (result.size() != 0)
            throw new ConstraintViolationException(result);
    }
}
