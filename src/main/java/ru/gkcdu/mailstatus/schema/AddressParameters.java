package ru.gkcdu.mailstatus.schema;

import java.util.ArrayList;
import java.util.List;

public class AddressParameters {
    private List<DestinationAddress> destinationAddresses = new ArrayList<>();
    private List<OperationAddress> operationAddresses = new ArrayList<>();
    private List<MailDirect> mailDirects = new ArrayList<>();
    private List<CountryFrom> countryFroms = new ArrayList<>();
    private List<CountryOper> countryOpers = new ArrayList<>();
}
