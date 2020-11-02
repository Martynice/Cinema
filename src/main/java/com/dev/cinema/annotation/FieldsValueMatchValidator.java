package com.dev.cinema.annotation;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {
    private String filed;
    private String fieldMatch;

    public void initialize(FieldsValueMatch constraint) {
        this.filed = constraint.field();
        this.fieldMatch = constraint.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(filed);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
        return Objects.equals(fieldValue, fieldMatchValue);
    }
}
