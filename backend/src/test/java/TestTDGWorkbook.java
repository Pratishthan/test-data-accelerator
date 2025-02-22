import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import com.pratishthanventures.tdg.output.SimpleCommandImpl;
import com.pratishthanventures.tdg.output.TDGWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestTDGWorkbook {

    private final String excelFilePath = "test.xlsx";

    @Test
    public void testCreateSheet() {

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        SimpleCommandImpl simpleCommand = new SimpleCommandImpl();
        SimpleCommandHelper simpleCommandHelper = new SimpleCommandHelper("simpleCommandFunction", "This is a simple command");
        simpleCommand.addCommandToSheet(sheet, 0, simpleCommandHelper);


        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
        assertNotNull(sheet);
    }
}
