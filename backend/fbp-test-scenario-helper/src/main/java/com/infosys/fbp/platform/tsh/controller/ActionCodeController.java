package com.infosys.fbp.platform.tsh.controller;

import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.dto.ActionCode;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.service.converter.factory.AbstractFactory;
import com.infosys.fbp.platform.tsh.service.converter.factory.FactoryProducer;
import com.infosys.fbp.platform.tsh.service.converter.processor.SetAndExecutePattern;
import com.infosys.fbp.platform.tsh.service.converter.processor.SimpleCommandPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.infosys.fbp.platform.tsh.PatternType.*;

@RestController
@CrossOrigin
@RequestMapping("/action-code")
public class ActionCodeController {

    AbstractFactory simpleCommandFactory = FactoryProducer.getFactory(SimpleCommand);
    AbstractFactory setAndExecuteFactory = FactoryProducer.getFactory(SetAndExecute);

    Map<String, Pattern> patternMap = new HashMap<>();

    @Autowired
    Map<String, Map<String, ActionCode>> componentActionCodeMap;


    @GetMapping
    public List<ActionCode> showCommandChainForm() {

        patternMap.putAll(simpleCommandFactory.getPatternMap());
        patternMap.putAll(setAndExecuteFactory.getPatternMap());

        List<ActionCode> actionCodes = new ArrayList<>();
        patternMap.forEach((actionCodeName, pattern) -> {
            ActionCode actionCode = new ActionCode();
            actionCode.setComponentName(pattern.getPattern().getComponentName());
            actionCode.setActionCodeGroupName(pattern.getPattern().getActionCodeGroupName());
            actionCode.setActionCode(actionCodeName);
            if (pattern instanceof SetAndExecutePattern) {
                actionCode.setType(PatternType.SetAndExecute);
            } else if (pattern instanceof SimpleCommandPattern) {
                actionCode.setType(PatternType.SimpleCommand);
            }
            actionCodes.add(actionCode);
        });

        componentActionCodeMap.forEach((component, ac_map) -> {
            ac_map.forEach((op, ac) -> actionCodes.add(ac));
        });

        return actionCodes;
    }


}