package ru.gkcdu.mailstatus;

import java.util.Map;

public class Shpi {
    public String shpiId;
    public String fileId;
    public Map<String, String> operTypesAndDates;

    public Shpi(String shpiId, String fileId, Map<String, String> operTypesAndDates) {
        this.shpiId = shpiId;
        this.fileId = fileId;
        this.operTypesAndDates = operTypesAndDates;
    }

}
