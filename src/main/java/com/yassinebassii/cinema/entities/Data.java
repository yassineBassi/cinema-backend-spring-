package com.yassinebassii.cinema.entities;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Data {
    public Object getAttribute(Object object, String name){
        try {
            Field field = object.getClass().getDeclaredField(name);
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
            Method readAttribute = propertyDescriptor.getReadMethod();
            return readAttribute.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException | NoSuchFieldException e) {
            e.printStackTrace();
            return "undefined";
        }
    }
}
