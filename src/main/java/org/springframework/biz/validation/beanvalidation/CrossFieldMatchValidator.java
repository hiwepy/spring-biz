/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.biz.validation.beanvalidation;

public class CrossFieldMatchValidator implements ConstraintValidator<CrossFieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private CrossFieldOperator operator;

    @Override
    public void initialize(final CrossFieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        operator=constraintAnnotation.operator();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            Class valueClass=value.getClass();
            final Field firstField = valueClass.getDeclaredField(firstFieldName);
            final Field secondField = valueClass.getDeclaredField(secondFieldName);
            //不支持为null的字段
            if(null==firstField||null==secondField){
                return false;
            }

            firstField.setAccessible(true);
            secondField.setAccessible(true);
            Object firstFieldValue= firstField.get(value);
            Object secondFieldValue= secondField.get(value);

            //不支持类型不同的字段
            if(!firstFieldValue.getClass().equals(secondFieldValue.getClass())){
                return false;
            }

            //整数支持 long int short
            //浮点数支持 double
            if(operator==CrossFieldOperator.EQ) {
                return firstFieldValue.equals(secondFieldValue);
            }
            else if(operator==CrossFieldOperator.GT){
                if(firstFieldValue.getClass().equals(Long.class)||firstFieldValue.getClass().equals(Integer.class)||firstFieldValue.getClass().equals(Short.class)) {
                    return (Long)firstFieldValue > (Long) secondFieldValue;
                }
                else if(firstFieldValue.getClass().equals(Double.class)) {
                    return (Double)firstFieldValue > (Double) secondFieldValue;
                }

            }
            else if(operator==CrossFieldOperator.GE){
                if(firstFieldValue.getClass().equals(Long.class)||firstFieldValue.getClass().equals(Integer.class)||firstFieldValue.getClass().equals(Short.class)) {
                    return Long.valueOf(firstFieldValue.toString()) >= Long.valueOf(secondFieldValue.toString());
                }
                else if(firstFieldValue.getClass().equals(Double.class)) {
                    return Double.valueOf(firstFieldValue.toString()) >= Double.valueOf(secondFieldValue.toString());
                }
            }
            else if(operator==CrossFieldOperator.LT){
                if(firstFieldValue.getClass().equals(Long.class)||firstFieldValue.getClass().equals(Integer.class)||firstFieldValue.getClass().equals(Short.class)) {
                    return (Long)firstFieldValue < (Long) secondFieldValue;
                }
                else if(firstFieldValue.getClass().equals(Double.class)) {
                    return (Double)firstFieldValue < (Double) secondFieldValue;
                }
            }
            else if(operator==CrossFieldOperator.LE){
                if(firstFieldValue.getClass().equals(Long.class)||firstFieldValue.getClass().equals(Integer.class)||firstFieldValue.getClass().equals(Short.class)) {
                    return Long.valueOf(firstFieldValue.toString()) <= Long.valueOf(secondFieldValue.toString());
                }
                else if(firstFieldValue.getClass().equals(Double.class)) {
                    return Double.valueOf(firstFieldValue.toString()) <= Double.valueOf(secondFieldValue.toString());
                }
            }
        }
        catch (final Exception ignore) {
            // ignore
        }
        return false;
    }
}
