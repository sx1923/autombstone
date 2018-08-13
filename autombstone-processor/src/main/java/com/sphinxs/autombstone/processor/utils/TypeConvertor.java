package com.sphinxs.autombstone.processor.utils;

import com.sphinxs.autombstone.processor.constant.TypeString;

import java.lang.reflect.Field;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class TypeConvertor {

    private final static String PARCELABLE = "Parcelable";

    private final static String SERIALIZABLE = "Serializable";

    public static String getFieldType(Elements elementUtils, TypeMirror typeMirror) throws Exception {
        String fieldType = typeMirror.toString();
        String type = "unKnow";
        if (fieldType.equals(TypeString.STRING)) {
            type = "String";
        } else if (fieldType.equals(TypeString.STRING + "[]")) {
            type = "StringArray";
        } else if (fieldType.equals("int") || fieldType.equals(TypeString.INTEGER)) {
            type = "Int";
        } else if (fieldType.equals("int[]")) {
            type = "IntArray";
        } else if (fieldType.equals("short") || fieldType.equals(TypeString.SHORT)) {
            type = "Short";
        }else if (fieldType.equals("short[]")) {
            type = "ShortArray";
        } else if (fieldType.equals("double") || fieldType.equals(TypeString.DOUBLE)) {
            type = "Double";
        } else if (fieldType.equals("double[]")) {
            type = "DoubleArray";
        } else if (fieldType.equals("float") || fieldType.equals(TypeString.FLOAT)) {
            type = "Float";
        } else if (fieldType.equals("float[]")) {
            type = "FloatArray";
        } else if (fieldType.equals("boolean") || fieldType.equals(TypeString.BOOLEAN)) {
            type = "Boolean";
        } else if (fieldType.equals("boolean[]")) {
            type = "BooleanArray";
        } else if (fieldType.equals("long") || fieldType.equals(TypeString.LONG)) {
            type = "Long";
        } else if (fieldType.equals("long[]")) {
            type = "LongArray";
        }else if (fieldType.equals("char")) {
            type = "Char";
        } else if (fieldType.equals("char[]")) {
            type = "CharArray";
        } else if (fieldType.equals("byte[]")) {
            type = "ByteArray";
        } else if (fieldType.equals(TypeString.SIZE)) {
            type = "Size";
        } else if (fieldType.equals(TypeString.SIZEF)) {
            type = "SizeF";
        } else if (fieldType.equals(TypeString.BUNDLE)) {
            type = "Bundle";
        } else {
            Class cla = typeMirror.getClass();
            Field outField = cla.getField("tsym");
            outField.setAccessible(true);
            String outFieldName = outField.get(typeMirror).toString();
            if (outFieldName.equals(TypeString.ARRAYLIST)) {
                Field innerField = cla.getField("typarams_field");
                innerField.setAccessible(true);
                List innerFieldList = (List) innerField.get(typeMirror);
                String innerFieldName = "";
                if (innerFieldList.size() > 0) {
                    innerFieldName = innerFieldList.get(0).toString();
                }
                if (innerFieldName.equals(TypeString.STRING)) {
                    type = "StringArrayList";
                } else if (innerFieldName.equals(TypeString.CHARSEQUENCE)) {
                    type = "CharSequenceArrayList";
                } else if (innerFieldName.equals(TypeString.INTEGER)) {
                    type = "IntegerArrayList";
                } else {
                    TypeElement typeElement = elementUtils.getTypeElement(innerFieldName);
                    List<TypeMirror> typeElementInterfaces = (List<TypeMirror>) typeElement.getInterfaces();
                    if (typeElementInterfaces.size() > 0) {
                        for (TypeMirror tm : typeElementInterfaces) {
                            if (tm.toString().equals(TypeString.PARCELABLE)) {
                                type = "ParcelableArrayList";
                                break;
                            }else {
                                type = SERIALIZABLE;
                            }
                        }
                    }
                }
            } else if (outFieldName.equals(TypeString.ARRAY)) {
                Field innerField = cla.getField("elemtype");
                innerField.setAccessible(true);
                String innerFieldName = innerField.get(typeMirror).toString();
                String fieldInterface = getFieldInterface(elementUtils, innerFieldName);
                if (fieldInterface.equals(PARCELABLE)) {
                    type = "ParcelableArray";
                }
            } else if (outFieldName.equals(TypeString.SPARSEARRAY)) {
                Field innerField = cla.getField("typarams_field");
                innerField.setAccessible(true);
                List innerFieldList = (List) innerField.get(typeMirror);
                String innerFieldName = "";
                if (innerFieldList.size() > 0) {
                    innerFieldName = innerFieldList.get(0).toString();
                }
                String fieldInterface = getFieldInterface(elementUtils, innerFieldName);
                if (fieldInterface.equals(PARCELABLE)) {
                    type = "SparseParcelableArray";
                }
            } else {
                type = getFieldInterface(elementUtils, outFieldName);
            }
        }
        return type;
    }

    private static String getFieldInterface(Elements elementUtils, String outFieldName) {
        String type = "unKnow";
        TypeElement typeElement = elementUtils.getTypeElement(outFieldName);
        List<TypeMirror> typeElementInterfaces = (List<TypeMirror>) typeElement.getInterfaces();
        if (typeElementInterfaces.size() > 0) {
            for (TypeMirror tm : typeElementInterfaces) {
                if (tm.toString().equals(TypeString.PARCELABLE)) {
                    type = PARCELABLE;
                    break;
                } else if (tm.toString().equals(TypeString.SERIALIZABLE)) {
                    type = SERIALIZABLE;
                    break;
                }
            }
        }
        if(type.equals("unKnow")){
            TypeMirror typeMirror = typeElement.getSuperclass();
            if(typeMirror.toString().startsWith("java.lang.Enum<")){
                type = SERIALIZABLE;
            }else if(!typeMirror.toString().equals(Object.class.getCanonicalName())){
                type = getFieldInterface(elementUtils, typeMirror.toString());
            }
        }
        return type;
    }

}
