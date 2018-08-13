package com.sphinxs.autombstone.processor;

import com.sphinxs.autombstone.annotation.SafeUiData;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 通过注解生成文件
 */
public class AutombstoneProcessor extends AbstractProcessor {
    /**
     * 文件相关的辅助类
     */
    private Filer mFilerUtils;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessagerUtils;
    /**
     *
     */
    private Types mTypeUtils;

    /**
     * Key：类名
     * Value：类的生成器
     */
    private Map<String, AutombstoneClassGenerator> mAutombstoneClassGeneratorMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFilerUtils = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessagerUtils = processingEnvironment.getMessager();
        mTypeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(SafeUiData.class);
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(SafeUiData.class)) {
            // 注解所在的类
            TypeElement annotationClassElement = (TypeElement) element.getEnclosingElement();
            // 注解所在的类名
            String annotationClassString = annotationClassElement.getQualifiedName().toString();
            // 注解所在类的生成器
            AutombstoneClassGenerator annotatedClassGenerator = mAutombstoneClassGeneratorMap.get(annotationClassString);
            if (annotatedClassGenerator == null) {
                annotatedClassGenerator = new AutombstoneClassGenerator(annotationClassElement, mElementUtils);
                mAutombstoneClassGeneratorMap.put(annotationClassString, annotatedClassGenerator);
            }
            annotatedClassGenerator.addAnnotationElement(element);
        }

        for (AutombstoneClassGenerator generator : mAutombstoneClassGeneratorMap.values()) {
            try {
                JavaFile javaFile = generator.brewJava();
                if (javaFile == null) {
                    continue;
                }
                javaFile.writeTo(mFilerUtils);
            } catch (IOException e) {
            }
        }

        return true;
    }
}
