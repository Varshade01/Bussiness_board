package com.example.code_gen.generators;

import java.lang.System;

@com.google.auto.service.AutoService(value = {javax.annotation.processing.Processor.class})
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000  2\u00020\u0001:\u0001 B\u0005\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u001e\u0010\f\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J$\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u00122\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004H\u0002J\u0016\u0010\u0013\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0012H\u0002J\u001e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002J\u001e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004H\u0002J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u001e\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00070\u00172\u0006\u0010\u001e\u001a\u00020\u001fH\u0016\u00a8\u0006!"}, d2 = {"Lcom/example/code_gen/generators/AutoMapCodeGenerator;", "Ljavax/annotation/processing/AbstractProcessor;", "()V", "collectPropertiesInformation", "", "Lcom/example/code_gen/PropertyInfo;", "element", "Ljavax/lang/model/element/TypeElement;", "annotatedFields", "Ljavax/lang/model/element/Element;", "kmClass", "Lkotlinx/metadata/KmClass;", "createFromMapFunction", "Lcom/squareup/kotlinpoet/FunSpec;", "variables", "createMappers", "", "elements", "", "createProcessorClass", "createToMapFunction", "generateMappersForClass", "getSupportedAnnotationTypes", "", "", "getSupportedSourceVersion", "Ljavax/lang/model/SourceVersion;", "process", "", "annotations", "roundEnv", "Ljavax/annotation/processing/RoundEnvironment;", "Companion", "code-gen"})
public final class AutoMapCodeGenerator extends javax.annotation.processing.AbstractProcessor {
    @org.jetbrains.annotations.NotNull
    public static final com.example.code_gen.generators.AutoMapCodeGenerator.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PROCESSOR_CLASS_KDOC = "AutoMapProcessor contain functionality to help converting maps back to their original entities.";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RESOLVE_TO_ENTITY_KDOC = "Helps convert maps to their original entity. Important: only types that provide className value to their AutoMap annotation can be resolved.";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String FILE_COMMENT = "This is a generated file. Do not edit";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AUTO_MAP_CLASS_NAME_PARAMETER = "className";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String AUTO_MAP_PACKAGE = "auto_map.internal";
    @org.jetbrains.annotations.NotNull
    private static final com.squareup.kotlinpoet.MemberName mapOfFunction = null;
    @org.jetbrains.annotations.NotNull
    private static final com.squareup.kotlinpoet.ClassName mapClass = null;
    @org.jetbrains.annotations.NotNull
    private static final com.squareup.kotlinpoet.ParameterizedTypeName mapType = null;
    
    public AutoMapCodeGenerator() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.Set<java.lang.String> getSupportedAnnotationTypes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public javax.lang.model.SourceVersion getSupportedSourceVersion() {
        return null;
    }
    
    @java.lang.Override
    public boolean process(@org.jetbrains.annotations.NotNull
    java.util.Set<? extends javax.lang.model.element.TypeElement> annotations, @org.jetbrains.annotations.NotNull
    javax.annotation.processing.RoundEnvironment roundEnv) {
        return false;
    }
    
    private final void createProcessorClass(java.util.Collection<? extends javax.lang.model.element.TypeElement> elements) {
    }
    
    private final void createMappers(java.util.Collection<? extends javax.lang.model.element.TypeElement> elements, java.util.List<? extends javax.lang.model.element.Element> annotatedFields) {
    }
    
    @kotlin.OptIn(markerClass = {com.squareup.kotlinpoet.DelicateKotlinPoetApi.class})
    private final void generateMappersForClass(javax.lang.model.element.TypeElement element, java.util.List<? extends javax.lang.model.element.Element> annotatedFields) {
    }
    
    private final java.util.List<com.example.code_gen.PropertyInfo> collectPropertiesInformation(javax.lang.model.element.TypeElement element, java.util.List<? extends javax.lang.model.element.Element> annotatedFields, kotlinx.metadata.KmClass kmClass) {
        return null;
    }
    
    /**
     * Generates `toMap` function to convert a data class to Map<String, Any?>.
     *
     * If a `value` was provided to [AutoMap] parameter `className`, the map will have an additional
     * pair: `"class" to "your_className_value"`
     *
     * Generated code will look like:
     * ```
     * public fun MyEntity.toMap(): Map<String, Any?> {
     *     `val map: Map<String, Any?> = mapOf(
     *         "field1" to field1,
     *         "field2" to field2,
     *     )
     *     return map
     * }
     * ```
     * If data class properties are annotated with [AutoMap], they themselves will be converted to map:
     * ```
     * "field1" to field1.toMap()
     * ```
     */
    @kotlin.OptIn(markerClass = {com.squareup.kotlinpoet.DelicateKotlinPoetApi.class})
    private final com.squareup.kotlinpoet.FunSpec createToMapFunction(javax.lang.model.element.TypeElement element, java.util.List<com.example.code_gen.PropertyInfo> variables) {
        return null;
    }
    
    /**
     * Generates `toEntity` function to convert a `Map<String, Any?>` back to original data class.
     *
     * If a `value` was provided to [AutoMap] parameter `className`, the map will have an additional
     * pair: `"class" to "your_className_value"`
     *
     * Generated code will look like:
     * ```
     * public fun Map<String, Any?>.toMyEntity(): MyEntity {
     *     val field1: String?** = this["field1"] as? String**
     *     val field2: String?** = this["field1"] as? String**
     *
     *     return MyEntity(
     *         field1 = field1 ?: throw Exception("Missing property")***,
     *         field2 = field2 ?: throw Exception("Missing property"),
     *     )
     * }
     * ```
     * ** - type of fields will match their original type.
     *
     * *** - nullability checks will be added only to non-nullable types.
     *
     * If data class properties are annotated with [AutoMap], they themselves will be converted
     * back to their entities:
     * ```
     * val field1: Entity? = (this["field1"] as? Map<String, Any?>)?.toMyEntity()
     * ```
     */
    @kotlin.OptIn(markerClass = {com.squareup.kotlinpoet.DelicateKotlinPoetApi.class})
    private final com.squareup.kotlinpoet.FunSpec createFromMapFunction(javax.lang.model.element.TypeElement element, java.util.List<com.example.code_gen.PropertyInfo> variables) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/example/code_gen/generators/AutoMapCodeGenerator$Companion;", "", "()V", "AUTO_MAP_CLASS_NAME_PARAMETER", "", "AUTO_MAP_PACKAGE", "FILE_COMMENT", "PROCESSOR_CLASS_KDOC", "RESOLVE_TO_ENTITY_KDOC", "mapClass", "Lcom/squareup/kotlinpoet/ClassName;", "getMapClass", "()Lcom/squareup/kotlinpoet/ClassName;", "mapOfFunction", "Lcom/squareup/kotlinpoet/MemberName;", "getMapOfFunction", "()Lcom/squareup/kotlinpoet/MemberName;", "mapType", "Lcom/squareup/kotlinpoet/ParameterizedTypeName;", "getMapType", "()Lcom/squareup/kotlinpoet/ParameterizedTypeName;", "code-gen"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.squareup.kotlinpoet.MemberName getMapOfFunction() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.squareup.kotlinpoet.ClassName getMapClass() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.squareup.kotlinpoet.ParameterizedTypeName getMapType() {
            return null;
        }
    }
}