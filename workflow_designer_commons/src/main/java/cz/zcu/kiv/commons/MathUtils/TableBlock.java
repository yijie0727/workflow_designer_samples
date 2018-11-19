package cz.zcu.kiv.commons.MathUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

@BlockType(type="TABLE",family = "MATH")
public class TableBlock implements Serializable {
    @BlockInput(name="multiplier",type=NUMBER)
    private int multiplier;

    @BlockInput(name="multiplicand",type=NUMBER)
    private int multiplicand;

    @BlockExecute
    public Table process(){
        Table table = new Table();
        table.setColumnHeaders(Arrays.asList(new String[]{"Multiplier","Multiplicand","Product"}));
        List<List<String>>rows=new ArrayList<>();
        for(int i=1;i<=multiplicand;i++){
            List<String>columns=Arrays.asList(
                    new String[]{
                            String.valueOf(multiplier),
                            String.valueOf(i),
                            String.valueOf(multiplier*i)
                    });
            rows.add(columns);
        }
        table.setRows(rows);
        table.setCaption("Multiplication Table");
        return table;
    }

}
