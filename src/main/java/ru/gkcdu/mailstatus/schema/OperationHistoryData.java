package ru.gkcdu.mailstatus.schema;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OperationHistoryData {
    private List<HistoryRecord> historyRecordList = new ArrayList<>();
}