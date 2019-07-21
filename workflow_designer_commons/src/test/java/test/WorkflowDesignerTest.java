package test;

import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import org.json.JSONArray;
import org.junit.Test;
import java.lang.reflect.InvocationTargetException;

public class WorkflowDesignerTest {
    @Test
    public void testBlock() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        JSONArray blocksArray=new BlockWorkFlow(ClassLoader.getSystemClassLoader(),":cz.zcu.kiv.commons",null,"").initializeBlocks();
        assert blocksArray.length()==21;
    }
}
