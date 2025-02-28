package com.pratishthanventures.tdg.controller;

import com.pratishthanventures.tdg.PatternType;
import com.pratishthanventures.tdg.dto.ActionCode;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;
import com.pratishthanventures.tdg.service.converter.processor.FetchAndVerifyPattern;
import com.pratishthanventures.tdg.service.converter.processor.SetAndExecutePattern;
import com.pratishthanventures.tdg.service.converter.processor.SimpleCommandPattern;
import com.pratishthanventures.tdg.service.converter.processor.TableMapperPattern;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pratishthanventures.tdg.PatternType.*;

@RestController
@CrossOrigin
@RequestMapping("/action-code")
public class ActionCodeController {

    AbstractFactory simpleCommandFactory = FactoryProducer.getFactory(SimpleCommand);
    AbstractFactory tableMapperFactory = FactoryProducer.getFactory(TableMapper);
    AbstractFactory fetchAndVerifyFactory = FactoryProducer.getFactory(FetchAndVerify);
    AbstractFactory setAndExecuteFactory = FactoryProducer.getFactory(SetAndExecute);

    Map<String, Pattern> patternMap = new HashMap<>();


    @GetMapping
    public List<ActionCode> showCommandChainForm() {

        patternMap.putAll(simpleCommandFactory.getPatternMap());
        patternMap.putAll(tableMapperFactory.getPatternMap());
        patternMap.putAll(fetchAndVerifyFactory.getPatternMap());
        patternMap.putAll(setAndExecuteFactory.getPatternMap());

        List<ActionCode> actionCodes = new ArrayList<>();
        patternMap.forEach((actionCodeName, pattern) -> {
            ActionCode actionCode = new ActionCode();
            actionCode.setCode(actionCodeName);
            if (pattern instanceof SetAndExecutePattern) {
                actionCode.setType(PatternType.SetAndExecute);
            } else if (pattern instanceof SimpleCommandPattern) {
                actionCode.setType(PatternType.SimpleCommand);
            } else if (pattern instanceof TableMapperPattern) {
                actionCode.setType(PatternType.TableMapper);
                actionCode.setColumns(((TableMapperPattern) pattern).getTableMapper().getColumnNameMap().keySet().stream().toList());
                actionCode.setDefaultData(((TableMapperPattern) pattern).getTableMapper().getData());
            } else if (pattern instanceof FetchAndVerifyPattern) {
                actionCode.setType(PatternType.FetchAndVerify);
                actionCode.setColumns(((FetchAndVerifyPattern) pattern).getFetchAndVerify().getResultColumnList());
            }
            actionCodes.add(actionCode);
        });

        return actionCodes;
    }


}