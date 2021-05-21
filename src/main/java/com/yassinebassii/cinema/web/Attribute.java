package com.yassinebassii.cinema.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@ToString
class Attribute {
    String name;
    String title;
    String type;

    public Object getValue(Object object) throws InvocationTargetException, IllegalAccessException, IntrospectionException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(name);
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
        Method readAttribute = propertyDescriptor.getReadMethod();
        return readAttribute.invoke(object);
    }
}
