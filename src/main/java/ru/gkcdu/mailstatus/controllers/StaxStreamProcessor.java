package ru.gkcdu.mailstatus.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import ru.gkcdu.mailstatus.Shpi;
import ru.gkcdu.mailstatus.config.DatabaseHandler;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(is);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) { // empty
            }
        }
    }

    public Shpi xmlReader(Shpi shpi, List<String> lov) throws XMLStreamException, IOException {
        try (
                StaxStreamProcessor processor = new StaxStreamProcessor(
                        Files.newInputStream(Paths.get("test1.xml")))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) { // while not end of XML
                int event = reader.next();
                String operDate = null;
                String operType = null;
                String operTypeId = null;
                if (event == XMLEvent.START_ELEMENT) {
                    if (reader.hasText()) {

                    }
                    if (reader.getName().getLocalPart().equals("OperDate")) {
                        operDate = reader.getElementText();
                        System.out.println(operDate);
                    }
                    if (reader.getName().getLocalPart().equals("OperType")) {
                        while (event != XMLEvent.START_ELEMENT || !reader.getName().getLocalPart().equals("Name")) {
                            event = reader.next();
                        }
                        if (event == XMLEvent.START_ELEMENT) {
                            operType = reader.getElementText();
                            if (!lov.contains(operType)) {
                                // добавление LOV
                                DatabaseHandler dbHandler = new DatabaseHandler();
                                // dbHandler.updateLOV(operType, operTypeId);
                            }
                            System.out.println(operType);
                        }
                    }
                    if (operType != null || operDate != null) {
                        shpi.operTypesAndDates.put(operDate, operType);
                    }
                }
            }
        }
        return shpi;
    }

}
