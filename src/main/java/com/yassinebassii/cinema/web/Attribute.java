package com.yassinebassii.cinema.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Data
@ToString
class Attribute {
    private String name;
    private String title;
    private String type;
    private boolean write;
    private List<ListOption> options;

    public Attribute(String name, String title, String type, boolean write, List<ListOption> options) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.write = write;
        this.options = options;
    }

    public Object getValue(Object object) throws InvocationTargetException, IllegalAccessException, IntrospectionException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(name);
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
        Method readAttribute = propertyDescriptor.getReadMethod();
        return readAttribute.invoke(object);
    }
}
