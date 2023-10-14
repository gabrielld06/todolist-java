package com.supimpa.todolist.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.HashSet;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static void copyNonNullPropertires(Object source, Object target) throws Throwable {
        try {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch(Exception e) {
            throw e.getCause().getCause();
        }
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd : pds) {
            if(src.getPropertyValue(pd.getName()) == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }

}
