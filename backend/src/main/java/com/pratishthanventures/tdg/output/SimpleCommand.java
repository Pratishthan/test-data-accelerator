package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import org.apache.poi.ss.usermodel.Sheet;

public interface SimpleCommand {

    void addCommandToSheet(Sheet sheet, Integer rowNumber, SimpleCommandHelper simpleCommandHelper);
}
