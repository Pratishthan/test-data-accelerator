import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import com.pratishthanventures.tdg.model.TableMapperHelper;
import com.pratishthanventures.tdg.output.SimpleCommandImpl;
import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.output.TableMapperImpl;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class TestTDGWorkbook {

    private final String excelFilePath = "test.xlsx";

    @Test
    public void testCreateSheetWithSingleSimpleCommand() {

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        SimpleCommandImpl simpleCommand = new SimpleCommandImpl();
        SimpleCommandHelper simpleCommandHelper = new SimpleCommandHelper("simpleCommandFunction", "This is a simple command");
        simpleCommand.addCommandToSheet(workbook, "Data", simpleCommandHelper);

        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
        assertNotNull(sheet);
    }

    @Test
    public void testCreateSheetWithSimpleCommand() {

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        SimpleCommandImpl simpleCommand = new SimpleCommandImpl();
        SimpleCommandHelper simpleCommandHelper = new SimpleCommandHelper("simpleCommandFunction", "This is a simple command");
        simpleCommand.addCommandToSheet(workbook, "Data", simpleCommandHelper);
        simpleCommand.addCommandToSheet(workbook, "Data", simpleCommandHelper);
        simpleCommand.addCommandToSheet(workbook, "Data", simpleCommandHelper);

        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
        assertNotNull(sheet);
    }


    @Test
    public void testCreateSheetWithPopulatedCommands() {

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        TableMapperImpl tableMapper = new TableMapperImpl();
        TableMapperHelper tableMapperHelper = new TableMapperHelper("targetTable",
                Arrays.asList("column1", "column2"),
                List.of(Map.of("column1", "value1", "column2", "value2")));
        tableMapper.addMapperToSheet(workbook, "Data", tableMapperHelper);

        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
        assertNotNull(sheet);
    }


    @Test
    public void testCreateSheetWithAllCommands() {

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        SimpleCommandImpl simpleCommand = new SimpleCommandImpl();
        SimpleCommandHelper simpleCommandHelper = new SimpleCommandHelper("simpleCommandFunction", "This is a simple command");
        simpleCommand.addCommandToSheet(workbook, "Data", simpleCommandHelper);

        TableMapperImpl tableMapper = new TableMapperImpl();
        TableMapperHelper tableMapperHelper = new TableMapperHelper("targetTable",
                Arrays.asList("column1", "column2"), new ArrayList<>());
        tableMapper.addMapperToSheet(workbook, "Data", tableMapperHelper);

        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
        assertNotNull(sheet);
    }
}
