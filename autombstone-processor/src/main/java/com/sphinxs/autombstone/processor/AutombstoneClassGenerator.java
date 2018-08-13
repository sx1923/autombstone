package com.sphinxs.autombstone.processor;

import com.sphinxs.autombstone.processor.constant.MethodName;
import com.sphinxs.autombstone.processor.constant.ParameterName;
import com.sphinxs.autombstone.processor.constant.TypeClass;
import com.sphinxs.autombstone.processor.utils.TypeConvertor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 生成墓碑化类的java文件
 */
public class AutombstoneClassGenerator {

    private final static String AUTOMBSTONE_END = "_Autombstone";
    /**
     * 注解所在的类
     */
    private TypeElement mAnnotationClassElement;
    /**
     * 注解集合
     */
    private ArrayList<Element> mAnnotationFieldElements = new ArrayList<>();

    /**
     * 墓碑化 保存函数构造
     */
    private MethodSpec.Builder mSaveMethodBuilder;

    /**
     * 墓碑化 恢复函数构造
     */
    private MethodSpec.Builder mRestoreMethodBuilder;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;

    public AutombstoneClassGenerator(TypeElement encloseElement, Elements elementUtils) {
        this.mAnnotationClassElement = encloseElement;
        this.mElementUtils = elementUtils;
    }

    /**
     * 添加注解元素
     *
     * @param element
     */
    public void addAnnotationElement(Element element) {
        mAnnotationFieldElements.add(element);
    }

    public JavaFile brewJava() {
        try {
            com.squareup.javapoet.TypeName cacheClass = ClassName.get(mAnnotationClassElement.asType());
            mSaveMethodBuilder = MethodSpec.methodBuilder(MethodName.SAVE)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(cacheClass, ParameterName.TARGET)
                    .addParameter(TypeClass.BUNDLE, ParameterName.OUTSTATE)
                    .addAnnotation(Override.class);

            mRestoreMethodBuilder = MethodSpec.methodBuilder(MethodName.RESTORE)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(cacheClass, ParameterName.TARGET)
                    .addParameter(TypeClass.BUNDLE, "savedInstanceState")
                    .addAnnotation(Override.class)
                    .addCode(""
                            + "if(savedInstanceState == null) {\n"
                            + "  return;\n"
                            + "}\n");

            int efficientAnnotationElement = 0;
            for (Element element : mAnnotationFieldElements) {
                // 注解属性变量名 如 mStudent
                Name fieldParamName = element.getSimpleName();
                // 注解属性类型
                String fieldTypeStr = TypeConvertor.getFieldType(mElementUtils, element.asType());
                efficientAnnotationElement++;
                if (fieldTypeStr.equals("unKnow")) {
                    // this field is not support yet
                    continue;
                }
                if (fieldTypeStr.equals("Serializable") || fieldTypeStr.equals("ParcelableArray")) {
                    addMethodStatementForClassCast(element, fieldParamName, fieldTypeStr);
                } else {
                    addMethodStatement(fieldParamName, fieldTypeStr);
                }
            }

            if (efficientAnnotationElement == 0) {
                return null;
            }

            MethodSpec saveMethod = mSaveMethodBuilder.build();
            MethodSpec recoverMethod = mRestoreMethodBuilder.build();
            String className = mAnnotationClassElement.getSimpleName().toString() + AUTOMBSTONE_END;
            TypeSpec cacheClassTypeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(TypeClass.IANNOTATION, cacheClass))
                    .addMethod(saveMethod)
                    .addMethod(recoverMethod)
                    .build();
            String packageName = mElementUtils.getPackageOf(mAnnotationClassElement).getQualifiedName().toString();
            return JavaFile.builder(packageName, cacheClassTypeSpec).build();
        } catch (Exception e) {
            return null;
        }
    }

    private void addMethodStatement(Name fieldParamName, String fieldTypeStr) {
        String fieldType = upperFirstWord(fieldTypeStr);
        mSaveMethodBuilder.addStatement(
                String.format("outState.put%s($S,target.$N)", fieldType), fieldParamName.toString().toUpperCase(), fieldParamName);
        mRestoreMethodBuilder.addStatement(
                String.format("target.$N = savedInstanceState.get%s($S)", fieldType), fieldParamName, fieldParamName.toString().toUpperCase());
    }

    private void addMethodStatementForClassCast(Element value, Name fieldParamName, String fieldTypeStr) {
        mSaveMethodBuilder.addStatement(
                String.format("outState.put%s($S,target.$N)", fieldTypeStr), fieldParamName.toString().toUpperCase(), fieldParamName);
        mRestoreMethodBuilder.addStatement(
                String.format("target.$N = ($T)savedInstanceState.get%s($S)", fieldTypeStr), fieldParamName, ClassName.get(value.asType()), fieldParamName.toString().toUpperCase());
    }

    private String upperFirstWord(String str) {
        if (str != null) {
            char[] ch = str.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            return new String(ch);
        } else {
            return "";
        }
    }
}
