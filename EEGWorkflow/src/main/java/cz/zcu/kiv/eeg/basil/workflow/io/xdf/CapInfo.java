package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CapInfo {
    private String name;
    private int size;
    private String labelscheme;
    private String filetype;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLabelscheme() {
        return labelscheme;
    }

    public void setLabelscheme(String labelscheme) {
        this.labelscheme = labelscheme;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
}
