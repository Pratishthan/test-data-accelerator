package com.infosys.fbp.platform.tsh.service.parser;

import com.infosys.fbp.platform.tsh.model.Property;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;


@Slf4j
public class Parser {

    enum ParameterType {
        PATH_PARAM, QUERY_PARAM, REQUEST_BODY, RESPONSE_BODY
    }

    private static final Map<String, EnumMap<ParameterType, List<Property>>> pathPropertyListMap = new HashMap<>();


    private static void createEmptyEnumMap(String endPoint) {
        EnumMap<ParameterType, List<Property>> enumMap = new EnumMap<>(ParameterType.class);

        List<Property> queryParameterPropertyList = new ArrayList<>();
        List<Property> pathParameterPropertyList = new ArrayList<>();
        List<Property> bodyPropertyList = new ArrayList<>();
        List<Property> respPropertyList = new ArrayList<>();

        enumMap.put(ParameterType.QUERY_PARAM, queryParameterPropertyList);
        enumMap.put(ParameterType.PATH_PARAM, pathParameterPropertyList);
        enumMap.put(ParameterType.REQUEST_BODY, bodyPropertyList);
        enumMap.put(ParameterType.RESPONSE_BODY, respPropertyList);

        pathPropertyListMap.put(endPoint, enumMap);

    }


    public static <T> void main(String[] args) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true); // implicit
        parseOptions.setResolveFully(true);

        SwaggerParseResult result = new OpenAPIParser().readLocation(
                "https://raw.githubusercontent.com/Pratishthan/test-data-accelerator/refs/heads/main/backend/fbp-test-scenario-helper/src/main/resources/schema/petstore.json", null,
                parseOptions);

        OpenAPI openAPI = result.getOpenAPI();

        if (result.getMessages() != null)
            result.getMessages().forEach(System.err::println); // validation errors and warnings

        if (openAPI != null) {
            openAPI.getPaths().forEach((key, value) -> {
                createEmptyEnumMap(key);
                if (ObjectUtils.isNotEmpty(value.getPost())) {
                    Schema<T> postBodySchema = value.getPost().getRequestBody().getContent().get("application/json").getSchema();
                    extractProperties(postBodySchema, "", key, ParameterType.REQUEST_BODY);

                    if (value.getPost().getResponses().containsKey("200")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, ParameterType.RESPONSE_BODY);
                    }

                    if (value.getPost().getResponses().containsKey("201")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("201").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, ParameterType.RESPONSE_BODY);
                    }

                    if (ObjectUtils.isNotEmpty(value.getPost().getParameters())) {
                        extractParameters(value.getPost().getParameters(), key);
                    }

                } else if (ObjectUtils.isNotEmpty(value.getGet())) {
                    if (ObjectUtils.isNotEmpty(value.getGet().getRequestBody())) {
                        Schema<T> postBodySchema = value.getGet().getRequestBody().getContent().get("application/json").getSchema();
                        extractProperties(postBodySchema, "", key, ParameterType.REQUEST_BODY);
                    }
                    if (value.getGet().getResponses().containsKey("200")) {
                        Schema<T> getRespSchema = value.getGet().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(getRespSchema, "", key, ParameterType.RESPONSE_BODY);
                    }

                    if (ObjectUtils.isNotEmpty(value.getGet().getParameters())) {
                        extractParameters(value.getGet().getParameters(), key);
                    }
                }

            });
        }


        log.info("Property Map {}", pathPropertyListMap);
    }

    private static <T> void extractProperties(Schema<T> schema, String path, String endPoint, ParameterType parameterType) {
        if (ObjectUtils.isNotEmpty(schema) && ObjectUtils.isNotEmpty(schema.getProperties())) {
            List<Property> finalPropertyList = pathPropertyListMap.get(endPoint).get(parameterType);
            schema.getProperties().forEach((kp, prop) -> {
                if (prop instanceof ArraySchema) {
                    extractProperties(((ArraySchema) prop).getItems(), path + ":" + kp, endPoint, parameterType);
                } else if (prop instanceof ComposedSchema) {
                    extractProperties(((ComposedSchema) prop).getItems(), path + ":" + kp, endPoint, parameterType);
                } else if (prop instanceof ObjectSchema) {
                    extractProperties((ObjectSchema) prop, path + ":" + kp, endPoint, parameterType);
                } else {
                    log.info("EndPoint: {} Property: {}:{}", endPoint, path, kp);
                    Property property = new Property();
                    property.setTechnicalColumnName(kp);
                    property.setDerivedDataType(path);
                    finalPropertyList.add(property);
                }
            });

        }
    }


    private static void extractParameters(List<Parameter> parameterList, String endPoint) {
        parameterList.forEach(parameter -> {
            List<Property> finalPropertyList = pathPropertyListMap.get(endPoint).get("path".equals(parameter.getIn()) ? ParameterType.PATH_PARAM : ParameterType.QUERY_PARAM);
            log.info("EndPoint: {} Parameter: {}", endPoint, parameter.getName());
            Property property = new Property();
            property.setTechnicalColumnName(parameter.getName());
            property.setDerivedDataType(parameter.getDescription());
            property.setIsMandatory(parameter.getRequired());
            finalPropertyList.add(property);
        });
    }


}
