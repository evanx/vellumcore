/*
 Source https://github.com/evanx by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership. The ASF licenses this file to
       you under the Apache License, Version 2.0 (the "License").
       You may not use this file except in compliance with the
       License. You may obtain a copy of the License at:

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package vellum.util;

import vellum.exception.Exceptions;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.jx.JMap;

/**
 *
 * @author evan.summers
 */
public class Beans {

    public static Map<String, Field> getFieldMap(Class beanClass) {
        Map<String, Field> map = new HashMap();
        for (Field field : beanClass.getDeclaredFields()) {
            map.put(field.getName(), field);
        }
        return map;
    }

    public static Map<String, PropertyDescriptor> getPropertyMap(Class beanClass) {
        Map<String, PropertyDescriptor> map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                map.put(property.getName(), property);
            }
            return map;
        } catch (IntrospectionException e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static List<PropertyDescriptor> getPropertyList(Class beanClass) {
        List<PropertyDescriptor> list = new ArrayList();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            list.addAll(Arrays.asList(beanInfo.getPropertyDescriptors()));
            return list;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static void parse(Object bean, PropertyDescriptor property, String string) throws ParseException {
        Object value = Types.parse(property.getPropertyType(), string);
        try {
            property.getWriteMethod().invoke(bean, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static void convert(Object bean, PropertyDescriptor property, Object value) throws ParseException {
        value = Types.convert(property.getPropertyType(), value);
        try {
            property.getWriteMethod().invoke(bean, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw Exceptions.newRuntimeException(e);
        }
    }
    
    public static void setBean(Object bean, JMap map) throws ParseException {
        for (PropertyDescriptor property : Beans.getPropertyMap(bean.getClass()).values()) {
            String stringValue = map.getString(property.getName(), null);
            if (stringValue != null) {
                Beans.parse(bean, property, stringValue);
            }
        }
    }

    

}
