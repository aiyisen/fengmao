package com.qy.ntf.util;


import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by songYu on 2018/6/27 8:41
 * DESC : 对象工具
 */
public class ObjectUtil {

    /**
     * 对象内字符串属性的值由空串转null
     *
     * @param model
     */
    public static void stringEmptyTrunNull(Object model) {
        Field[] field = model.getClass().getDeclaredFields();
        Field[] superField = model.getClass().getSuperclass().getDeclaredFields();
        int str1Length = field.length;
        int str2length = superField.length;
        field = Arrays.copyOf(field, str1Length + str2length);
        System.arraycopy(superField, 0, field, str1Length, str2length);
        String[] modelName = new String[field.length];
        String[] modelType = new String[field.length];
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            modelName[i] = name;
            String type = field[i].getGenericType().toString();
            modelType[i] = type;
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());
            try {
                if (type.equals("class java.lang.String")) {
                    Method m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model);
                    if (null != value) {
                        value = value.trim();
                    }
                    if ("".equals(value)) {
                        Method set = model.getClass().getMethod("set" + name, String.class);
                        set.invoke(model, new Object[]{null});
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对象内字符串属性的值由null转空串
     *
     * @param model
     */
    public static void stringNullTrunEmpty(Object model) {
        Field[] field = model.getClass().getDeclaredFields();
        Field[] superField = model.getClass().getSuperclass().getDeclaredFields();
        int str1Length = field.length;
        int str2length = superField.length;
        field = Arrays.copyOf(field, str1Length + str2length);
        System.arraycopy(superField, 0, field, str1Length, str2length);
        String[] modelName = new String[field.length];
        String[] modelType = new String[field.length];
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            modelName[i] = name;
            String type = field[i].getGenericType().toString();
            modelType[i] = type;
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());
            try {
                if (type.equals("class java.lang.String")) {
                    Method m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model);
                    if (null == value) {
                        Method set = model.getClass().getMethod("set" + name, String.class);
                        set.invoke(model, new Object[]{""});
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 对象转Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objTrunMap(Object obj) {
        List<String> nameList = getFiledName(obj.getClass());
        Map<String, Object> map = new HashMap<>(nameList.size());
        nameList.forEach(name -> {
            map.put(name, dynamicGet(obj, name));
        });
        return map;
    }

    /**
     * 对比两个对象是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b)) || String.valueOf(a).equals(String.valueOf(b));
    }

    /**
     * 判断对象是不是集合
     *
     * @param obj
     * @return
     */
    public static boolean isList(Object obj) {
        return obj instanceof List;
    }


    /**
     * 获取属性名数组
     */
    public static List<String> getFiledName(Class o) {
        Field[] fields = o.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return Arrays.asList(fieldNames);
    }


    /**
     * 通过属性名给对象内属赋值
     *
     * @param obj          对象
     * @param propertyName 属性名
     * @param qtySum       值
     */
    public static void dynamicSet(Object obj, String propertyName, Object qtySum) {
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, qtySum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据属性名调用get方法
     *
     * @param object
     * @param propertyName
     * @return
     */
    public static Object dynamicGet(Object object, String propertyName) {
        try {
            Field field = object.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Object qty = field.get(object);
            return qty;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用get方法异常!");
        }
    }

    /**
     * 对象不为空
     *
     * @param object
     * @return
     */
    public static boolean notNull(Object object) {
        if (Objects.isNull(object)) {
            return false;
        }
        if (Strings.isEmpty(object.toString())) {
            return false;
        }
        return true;
    }
    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * Model mapper property setting are specified in the following block.
     * Default property matching strategy is set to Strict see {@link MatchingStrategies}
     * Custom mappings are added using {@link ModelMapper#addMappings(PropertyMap)}
     */
    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    /**
     * Maps {@code source} to {@code destination}.
     *
     * @param source      object to map from
     * @param destination object to map to
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
