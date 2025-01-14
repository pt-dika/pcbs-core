package dk.apps.pcps.main.controller;

import dk.apps.pcps.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseCtl {

    ValidationUtils validationUtils;

    @Autowired
    public BaseCtl(ValidationUtils validationUtils){
        this.validationUtils = validationUtils;
    }
}
