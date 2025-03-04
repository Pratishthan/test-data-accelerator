package com.infosys.fbp.platform.tsh.service.parser;

import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.PropertyType;
import com.infosys.fbp.platform.tsh.dto.ActionCode;
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
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Component
public class OpenAPISpecParser {

    private final Map<String, ActionCode> actionCodeMap = new HashMap<>();

    private void createEmptyActionCode(String component, String endPoint) {
        ActionCode actionCode = new ActionCode();
        actionCode.setComponentName(component);
        actionCode.setEndPoint(endPoint);

        EnumMap<PropertyType, List<Property>> enumMap = new EnumMap<>(PropertyType.class);
        actionCode.setPropertyListMap(enumMap);

        List<Property> queryParameterPropertyList = new ArrayList<>();
        List<Property> pathParameterPropertyList = new ArrayList<>();
        List<Property> bodyPropertyList = new ArrayList<>();
        List<Property> respPropertyList = new ArrayList<>();

        enumMap.put(PropertyType.QueryParamList, queryParameterPropertyList);
        enumMap.put(PropertyType.PathParamList, pathParameterPropertyList);
        enumMap.put(PropertyType.RequestBodyColumnList, bodyPropertyList);
        enumMap.put(PropertyType.ResponseBodyColumnList, respPropertyList);

        actionCodeMap.put(endPoint, actionCode);

    }


    public <T> Map<String, ActionCode> processOpenAPISpec(String component, String path) {

        actionCodeMap.clear();

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true); // implicit
        parseOptions.setResolveFully(true);

        SwaggerParseResult result = new OpenAPIParser().readLocation(
                path, null,
                parseOptions);

        OpenAPI openAPI = result.getOpenAPI();

        if (result.getMessages() != null)
            result.getMessages().forEach(System.err::println); // validation errors and warnings

        if (openAPI != null) {
            openAPI.getPaths().forEach((key, value) -> {
                createEmptyActionCode(component, key);
                if (ObjectUtils.isNotEmpty(value.getPost())) {
                    actionCodeMap.get(key).setActionCodeGroupName(value.getPost().getTags().get(0));
                    actionCodeMap.get(key).setActionCode(value.getPost().getOperationId());
                    actionCodeMap.get(key).setType(PatternType.PostAndVerify);
                    Schema<T> postBodySchema = value.getPost().getRequestBody().getContent().get("application/json").getSchema();
                    extractProperties(postBodySchema, "", key, PropertyType.RequestBodyColumnList);

                    if (value.getPost().getResponses().containsKey("200")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, PropertyType.ResponseBodyColumnList);
                    }

                    if (value.getPost().getResponses().containsKey("201")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("201").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, PropertyType.ResponseBodyColumnList);
                    }

                    if (ObjectUtils.isNotEmpty(value.getPost().getParameters())) {
                        extractParameters(value.getPost().getParameters(), key);
                    }

                } else if (ObjectUtils.isNotEmpty(value.getGet())) {
                    actionCodeMap.get(key).setActionCodeGroupName(value.getGet().getTags().get(0));
                    actionCodeMap.get(key).setActionCode(value.getGet().getOperationId());
                    actionCodeMap.get(key).setType(PatternType.FetchAndVerify);
                    if (ObjectUtils.isNotEmpty(value.getGet().getRequestBody())) {
                        Schema<T> postBodySchema = value.getGet().getRequestBody().getContent().get("application/json").getSchema();
                        extractProperties(postBodySchema, "", key, PropertyType.RequestBodyColumnList);
                    }
                    if (value.getGet().getResponses().containsKey("200")) {
                        Schema<T> getRespSchema = value.getGet().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(getRespSchema, "", key, PropertyType.ResponseBodyColumnList);
                    }

                    if (ObjectUtils.isNotEmpty(value.getGet().getParameters())) {
                        extractParameters(value.getGet().getParameters(), key);
                    }
                }

            });
        }

        JSONObject jsonObject = new JSONObject(actionCodeMap);
        String actionCodeJson = jsonObject.toString();

        log.info("Action Code Map {}", actionCodeJson);
        return actionCodeMap;
    }

    private <T> void extractProperties(Schema<T> schema, String path, String endPoint, PropertyType PropertyType) {
        if (ObjectUtils.isNotEmpty(schema) && ObjectUtils.isNotEmpty(schema.getProperties())) {
            List<Property> finalPropertyList = actionCodeMap.get(endPoint).getPropertyListMap().get(PropertyType);
            schema.getProperties().forEach((kp, prop) -> {
                Map<String, String> requiredMap = new HashMap<>();
                if (ObjectUtils.isNotEmpty(schema.getRequired())) {
                    requiredMap = schema.getRequired().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
                }
                if (prop instanceof ArraySchema) {
                    extractProperties(((ArraySchema) prop).getItems(), path + ":" + kp, endPoint, PropertyType);
                } else if (prop instanceof ComposedSchema) {
                    extractProperties(((ComposedSchema) prop).getItems(), path + ":" + kp, endPoint, PropertyType);
                } else if (prop instanceof ObjectSchema) {
                    extractProperties((ObjectSchema) prop, path + ":" + kp, endPoint, PropertyType);
                } else {
                    log.info("EndPoint: {} Property: {}:{}", endPoint, path, kp);
                    Property property = new Property();
                    property.setTechnicalColumnName(kp);
                    property.setIsMandatory(requiredMap.containsKey(kp));
                    property.setDerivedDataType(path);
                    finalPropertyList.add(property);
                }
            });

        }
    }


    private void extractParameters(List<Parameter> parameterList, String endPoint) {
        parameterList.forEach(parameter -> {
            List<Property> finalPropertyList = actionCodeMap.get(endPoint).getPropertyListMap().get("path".equals(parameter.getIn()) ? PropertyType.PathParamList : PropertyType.QueryParamList);
            log.info("EndPoint: {} Parameter: {}", endPoint, parameter.getName());
            Property property = new Property();
            property.setTechnicalColumnName(parameter.getName());
            property.setDerivedDataType(parameter.getDescription());
            property.setIsMandatory(parameter.getRequired());
            finalPropertyList.add(property);
        });
    }

    public static void main(String[] args) {
        OpenAPISpecParser openAPISpecParser = new OpenAPISpecParser();
        openAPISpecParser.processOpenAPISpec("Collection",
                "https://raw.githubusercontent.com/Pratishthan/test-data-accelerator/refs/heads/main/backend/fbp-test-scenario-helper/src/main/resources/schema/petstore.json");
    }

}
