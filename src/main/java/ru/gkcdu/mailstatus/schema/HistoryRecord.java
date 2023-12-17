package ru.gkcdu.mailstatus.schema;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HistoryRecord {
    private List<AddressParameters> addressParameters = new ArrayList<>();
    private List<FinanceParameters> financeParameters = new ArrayList<>();
    private List<ItemParameters> itemParameters = new ArrayList<>();
    private List<OperationParameters> operationParameters = new ArrayList<>();
    private List<UserParameters> userParameters = new ArrayList<>();
}