package com.example.task_service_jwt.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class BeanUtils {

    @SneakyThrows
    public static void copyProperties(Object receiver, Object source) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] receiverFields = receiver.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            Object value = sourceField.get(source);
            sourceField.setAccessible(false);

            if (value != null && !"id".equals(sourceField.getName())) {
                for (Field receiverField : receiverFields) {
                    if (receiverField.getName().equals(sourceField.getName())) {
                        receiverField.setAccessible(true);
                        receiverField.set(receiver, value);
                        receiverField.setAccessible(false);
                        break;
                    }
                }
            }
        }
    }
}