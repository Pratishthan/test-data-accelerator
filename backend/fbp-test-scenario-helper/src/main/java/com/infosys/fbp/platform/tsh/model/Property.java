package com.infosys.fbp.platform.tsh.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {
    private String businessColumnName;
    private String technicalColumnName;
    private Boolean isMandatory;
    private String derivedDataType;

    public Parameter getParameter() {
        return new Parameter(businessColumnName, businessColumnName, "");
    }
}
