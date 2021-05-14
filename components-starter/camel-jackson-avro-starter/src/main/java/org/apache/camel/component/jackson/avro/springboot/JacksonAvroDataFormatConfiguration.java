/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jackson.avro.springboot;

import javax.annotation.Generated;
import org.apache.camel.spring.boot.DataFormatConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Marshal POJOs to Avro and back using Jackson.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.springboot.maven.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.dataformat.avro-jackson")
public class JacksonAvroDataFormatConfiguration
        extends
            DataFormatConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the avro-jackson data format.
     * This is enabled by default.
     */
    private Boolean enabled;
    /**
     * Lookup and use the existing ObjectMapper with the given id when using
     * Jackson.
     */
    private String objectMapper;
    /**
     * Whether to lookup and use default Jackson ObjectMapper from the registry.
     */
    private Boolean useDefaultObjectMapper = true;
    /**
     * Class name of the java type to use when unmarshalling
     */
    private String unmarshalTypeName;
    /**
     * When marshalling a POJO to JSON you might want to exclude certain fields
     * from the JSON output. With Jackson you can use JSON views to accomplish
     * this. This option is to refer to the class which has JsonView annotations
     */
    private String jsonViewTypeName;
    /**
     * If you want to marshal a pojo to JSON, and the pojo has some fields with
     * null values. And you want to skip these null values, you can set this
     * option to NON_NULL
     */
    private String include;
    /**
     * Used for JMS users to allow the JMSType header from the JMS spec to
     * specify a FQN classname to use to unmarshal to.
     */
    private Boolean allowJmsType = false;
    /**
     * Refers to a custom collection type to lookup in the registry to use. This
     * option should rarely be used, but allows to use different collection
     * types than java.util.Collection based as default.
     */
    private String collectionTypeName;
    /**
     * To unmarshal to a List of Map or a List of Pojo.
     */
    private Boolean useList = false;
    /**
     * To use custom Jackson modules com.fasterxml.jackson.databind.Module
     * specified as a String with FQN class names. Multiple classes can be
     * separated by comma.
     */
    private String moduleClassNames;
    /**
     * To use custom Jackson modules referred from the Camel registry. Multiple
     * modules can be separated by comma.
     */
    private String moduleRefs;
    /**
     * Set of features to enable on the Jackson
     * com.fasterxml.jackson.databind.ObjectMapper. The features should be a
     * name that matches a enum from
     * com.fasterxml.jackson.databind.SerializationFeature,
     * com.fasterxml.jackson.databind.DeserializationFeature, or
     * com.fasterxml.jackson.databind.MapperFeature Multiple features can be
     * separated by comma
     */
    private String enableFeatures;
    /**
     * Set of features to disable on the Jackson
     * com.fasterxml.jackson.databind.ObjectMapper. The features should be a
     * name that matches a enum from
     * com.fasterxml.jackson.databind.SerializationFeature,
     * com.fasterxml.jackson.databind.DeserializationFeature, or
     * com.fasterxml.jackson.databind.MapperFeature Multiple features can be
     * separated by comma
     */
    private String disableFeatures;
    /**
     * If enabled then Jackson is allowed to attempt to use the
     * CamelJacksonUnmarshalType header during the unmarshalling. This should
     * only be enabled when desired to be used.
     */
    private Boolean allowUnmarshallType = false;
    /**
     * If set then Jackson will use the Timezone when marshalling/unmarshalling.
     */
    private String timezone;
    /**
     * If set to true then Jackson will lookup for an objectMapper into the
     * registry
     */
    private Boolean autoDiscoverObjectMapper = false;
    /**
     * Whether the data format should set the Content-Type header with the type
     * from the data format. For example application/xml for data formats
     * marshalling to XML, or application/json for data formats marshalling to
     * JSON
     */
    private Boolean contentTypeHeader = true;
    /**
     * Optional schema resolver used to lookup schemas for the data in transit.
     */
    private String schemaResolver;
    /**
     * When not disabled, the SchemaResolver will be looked up into the registry
     */
    private Boolean autoDiscoverSchemaResolver = true;

    public String getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(String objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Boolean getUseDefaultObjectMapper() {
        return useDefaultObjectMapper;
    }

    public void setUseDefaultObjectMapper(Boolean useDefaultObjectMapper) {
        this.useDefaultObjectMapper = useDefaultObjectMapper;
    }

    public String getUnmarshalTypeName() {
        return unmarshalTypeName;
    }

    public void setUnmarshalTypeName(String unmarshalTypeName) {
        this.unmarshalTypeName = unmarshalTypeName;
    }

    public String getJsonViewTypeName() {
        return jsonViewTypeName;
    }

    public void setJsonViewTypeName(String jsonViewTypeName) {
        this.jsonViewTypeName = jsonViewTypeName;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public Boolean getAllowJmsType() {
        return allowJmsType;
    }

    public void setAllowJmsType(Boolean allowJmsType) {
        this.allowJmsType = allowJmsType;
    }

    public String getCollectionTypeName() {
        return collectionTypeName;
    }

    public void setCollectionTypeName(String collectionTypeName) {
        this.collectionTypeName = collectionTypeName;
    }

    public Boolean getUseList() {
        return useList;
    }

    public void setUseList(Boolean useList) {
        this.useList = useList;
    }

    public String getModuleClassNames() {
        return moduleClassNames;
    }

    public void setModuleClassNames(String moduleClassNames) {
        this.moduleClassNames = moduleClassNames;
    }

    public String getModuleRefs() {
        return moduleRefs;
    }

    public void setModuleRefs(String moduleRefs) {
        this.moduleRefs = moduleRefs;
    }

    public String getEnableFeatures() {
        return enableFeatures;
    }

    public void setEnableFeatures(String enableFeatures) {
        this.enableFeatures = enableFeatures;
    }

    public String getDisableFeatures() {
        return disableFeatures;
    }

    public void setDisableFeatures(String disableFeatures) {
        this.disableFeatures = disableFeatures;
    }

    public Boolean getAllowUnmarshallType() {
        return allowUnmarshallType;
    }

    public void setAllowUnmarshallType(Boolean allowUnmarshallType) {
        this.allowUnmarshallType = allowUnmarshallType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getAutoDiscoverObjectMapper() {
        return autoDiscoverObjectMapper;
    }

    public void setAutoDiscoverObjectMapper(Boolean autoDiscoverObjectMapper) {
        this.autoDiscoverObjectMapper = autoDiscoverObjectMapper;
    }

    public Boolean getContentTypeHeader() {
        return contentTypeHeader;
    }

    public void setContentTypeHeader(Boolean contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public String getSchemaResolver() {
        return schemaResolver;
    }

    public void setSchemaResolver(String schemaResolver) {
        this.schemaResolver = schemaResolver;
    }

    public Boolean getAutoDiscoverSchemaResolver() {
        return autoDiscoverSchemaResolver;
    }

    public void setAutoDiscoverSchemaResolver(Boolean autoDiscoverSchemaResolver) {
        this.autoDiscoverSchemaResolver = autoDiscoverSchemaResolver;
    }
}