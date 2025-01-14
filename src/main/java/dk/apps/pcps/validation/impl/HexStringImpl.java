package dk.apps.pcps.validation.impl;

import dk.apps.pcps.validation.constraint.HexString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HexStringImpl implements ConstraintValidator<HexString, String> {

    public void initialize(HexString constraint) {
    }

    public boolean isValid(String hexString, ConstraintValidatorContext context) {
        if (hexString == null || hexString.isEmpty())
            return false;
        return hexString.length() % 2 == 0;
    }

}
