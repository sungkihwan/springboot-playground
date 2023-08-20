package com.demo.springboot.common.validation;

import com.demo.springboot.common.annotation.EachEnum;
import com.demo.springboot.common.enums.Roles;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RolesEnumValidator implements ConstraintValidator<EachEnum, List<Roles>> {

    private List<String> enumValues;

    @Override
    public void initialize(EachEnum constraintAnnotation) {
        Class<? extends Enum<?>> enumSelected = constraintAnnotation.enumClass();
        this.enumValues = Arrays.stream(enumSelected.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<Roles> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }

        for (Roles value : values) {
            if (!enumValues.contains(value.name())) {
                return false;
            }
        }

        return true;
    }
}


