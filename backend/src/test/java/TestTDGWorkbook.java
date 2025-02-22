import com.pratishthanventures.tdg.util.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestTDGWorkbook {

    private final String excelFilePath = "test.xlsx";

    @Test
    public void testCreateSheetWithAllCommands() {

        TDGWorkbook workbook = new TDGWorkbook("Data");

        AbstractFactory simpleCommandFactory = FactoryProducer.getFactory("SimpleCommand");
        AbstractFactory tableMapperFactory = FactoryProducer.getFactory("TableMapper");

        simpleCommandFactory.getPattern("We have a clean DB").process(workbook, "Data");

        tableMapperFactory.getPattern("Collectable Event").process(workbook, "Data");

        tableMapperFactory.getPattern("Demand Code").process(workbook, "Data");

        workbook.writeWorkbookToFile(excelFilePath);

        assertNotNull(workbook.getWorkbook());
    }


}
