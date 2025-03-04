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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Configuration
public class OpenAPISpecParser {


    private final Map<String, Map<String, ActionCode>> componentActionCodeMap = new HashMap<>();

    private final Map<String, String> componentMap = Map.of("Collection",
            "https://raw.githubusercontent.com/Pratishthan/test-data-accelerator/refs/heads/main/backend/fbp-test-scenario-helper/src/main/resources/schema/petstore.json");

    @Bean
    public Map<String, Map<String, ActionCode>> componentActionCodeMap() {
        componentMap.forEach((key, value) -> componentActionCodeMap.put(key, processOpenAPISpec(key, value)));
        return componentActionCodeMap;
    }

    private ActionCode createEmptyActionCode(String component) {

        ActionCode actionCode = new ActionCode();
        actionCode.setComponentName(component);

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

        return actionCode;

    }


    public <T> Map<String, ActionCode> processOpenAPISpec(String component, String path) {

        Map<String, ActionCode> actionCodeMap = new HashMap<>();

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
                if (ObjectUtils.isNotEmpty(value.getPost())) {

                    ActionCode postActionCode = createEmptyActionCode(component);
                    postActionCode.setActionCodeGroupName(value.getPost().getTags().get(0));
                    postActionCode.setActionCode(value.getPost().getOperationId());
                    postActionCode.setEndPoint(key);
                    postActionCode.setType(PatternType.PostAndVerify);

                    actionCodeMap.put(value.getPost().getOperationId(), postActionCode);

                    Schema<T> postBodySchema = value.getPost().getRequestBody().getContent().get("application/json").getSchema();
                    extractProperties(postBodySchema, "", key, postActionCode.getPropertyListMap().get(PropertyType.RequestBodyColumnList));

                    if (value.getPost().getResponses().containsKey("200")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, postActionCode.getPropertyListMap().get(PropertyType.ResponseBodyColumnList));
                    }

                    if (value.getPost().getResponses().containsKey("201")) {
                        Schema<T> postRespSchema = value.getPost().getResponses().get("201").getContent().get("application/json").getSchema();
                        extractProperties(postRespSchema, "", key, postActionCode.getPropertyListMap().get(PropertyType.ResponseBodyColumnList));
                    }

                    if (ObjectUtils.isNotEmpty(value.getPost().getParameters())) {
                        extractParameters(value.getPost().getParameters(), key,
                                postActionCode.getPropertyListMap().get(PropertyType.QueryParamList),
                                postActionCode.getPropertyListMap().get(PropertyType.PathParamList));
                    }

                }
                if (ObjectUtils.isNotEmpty(value.getGet())) {

                    ActionCode getActionCode = createEmptyActionCode(component);
                    getActionCode.setActionCodeGroupName(value.getGet().getTags().get(0));
                    getActionCode.setActionCode(value.getGet().getOperationId());
                    getActionCode.setType(PatternType.FetchAndVerify);
                    getActionCode.setEndPoint(key);

                    actionCodeMap.put(value.getGet().getOperationId(), getActionCode);


                    if (ObjectUtils.isNotEmpty(value.getGet().getRequestBody())) {
                        Schema<T> postBodySchema = value.getGet().getRequestBody().getContent().get("application/json").getSchema();
                        extractProperties(postBodySchema, "", key, getActionCode.getPropertyListMap().get(PropertyType.RequestBodyColumnList));
                    }
                    if (value.getGet().getResponses().containsKey("200")) {
                        Schema<T> getRespSchema = value.getGet().getResponses().get("200").getContent().get("application/json").getSchema();
                        extractProperties(getRespSchema, "", key, getActionCode.getPropertyListMap().get(PropertyType.ResponseBodyColumnList));
                    }

                    if (ObjectUtils.isNotEmpty(value.getGet().getParameters())) {
                        extractParameters(value.getGet().getParameters(), key,
                                getActionCode.getPropertyListMap().get(PropertyType.QueryParamList),
                                getActionCode.getPropertyListMap().get(PropertyType.PathParamList));
                    }
                }

            });
        }

        JSONObject jsonObject = new JSONObject(actionCodeMap);
        String actionCodeJson = jsonObject.toString();

        log.info("Action Code Map {}", actionCodeJson);
        return actionCodeMap;
    }

    private <T> void extractProperties(Schema<T> schema, String path, String endPoint, List<Property> finalPropertyList) {
        if (ObjectUtils.isNotEmpty(schema) && ObjectUtils.isNotEmpty(schema.getProperties())) {
            schema.getProperties().forEach((kp, prop) -> {
                Map<String, String> requiredMap = new HashMap<>();
                if (ObjectUtils.isNotEmpty(schema.getRequired())) {
                    requiredMap = schema.getRequired().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
                }
                if (prop instanceof ArraySchema) {
                    extractProperties(((ArraySchema) prop).getItems(), path + ":" + kp, endPoint, finalPropertyList);
                } else if (prop instanceof ComposedSchema) {
                    extractProperties(((ComposedSchema) prop).getItems(), path + ":" + kp, endPoint, finalPropertyList);
                } else if (prop instanceof ObjectSchema) {
                    extractProperties((ObjectSchema) prop, path + ":" + kp, endPoint, finalPropertyList);
                } else {
                    log.info("EndPoint: {} Property: {}:{}", endPoint, path, kp);
                    Property property = new Property();
                    property.setBusinessColumnName(kp); // todo: derive business column name
                    property.setTechnicalColumnName(kp);
                    property.setIsMandatory(requiredMap.containsKey(kp));
                    property.setDerivedDataType(path);
                    finalPropertyList.add(property);
                }
            });

        }
    }


    private void extractParameters(List<Parameter> parameterList, String endPoint, List<Property> queryPropertyList, List<Property> pathPropertyList) {
        parameterList.forEach(parameter -> {
            List<Property> finalPropertyList = "path".equals(parameter.getIn()) ? pathPropertyList : queryPropertyList;
            log.info("EndPoint: {} Parameter: {}", endPoint, parameter.getName());
            Property property = new Property();
            property.setBusinessColumnName(parameter.getName());
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
