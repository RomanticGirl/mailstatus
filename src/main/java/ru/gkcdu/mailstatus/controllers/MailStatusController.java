package ru.gkcdu.mailstatus.controllers;

import java.util.UUID;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

public class MailStatusController {

    public SOAPConnection getConnection() throws SOAPException {
        // Cоздаем соединение
        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = soapConnFactory.createConnection();
        return connection;
    }

    public String getMailStatusXML(String shpi, String name, String pass, SOAPConnection connection)
            throws SOAPException, TransformerFactoryConfigurationError, TransformerException {

        String url = "https://tracking.russianpost.ru/rtm34";

        // Cоздаем сообщение
        MessageFactory messageFactory = MessageFactory.newInstance("SOAP 1.2 Protocol");
        SOAPMessage message = messageFactory.createMessage();

        // Создаем объекты, представляющие различные компоненты сообщения
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();
        envelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");
        envelope.addNamespaceDeclaration("oper", "http://russianpost.org/operationhistory");
        envelope.addNamespaceDeclaration("data", "http://russianpost.org/operationhistory/data");
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        SOAPElement operElement = body.addChildElement("getOperationHistory", "oper");
        SOAPElement dataElement = operElement.addChildElement("OperationHistoryRequest", "data");
        SOAPElement barcode = dataElement.addChildElement("Barcode", "data");
        SOAPElement messageType = dataElement.addChildElement("MessageType", "data");
        SOAPElement language = dataElement.addChildElement("Language", "data");
        SOAPElement dataAuth = operElement.addChildElement("AuthorizationHeader", "data");
        SOAPFactory sf = SOAPFactory.newInstance();
        Name must = sf.createName("mustUnderstand", "soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        dataAuth.addAttribute(must, "1");
        SOAPElement login = dataAuth.addChildElement("login", "data");
        SOAPElement password = dataAuth.addChildElement("password", "data");

        // Заполняем значения
        barcode.addTextNode(shpi); // RA644000001RU
        messageType.addTextNode("0");
        language.addTextNode("RUS");
        login.addTextNode(name);
        password.addTextNode(pass);

        // Сохранение сообщения
        message.saveChanges();

        // Отправляем запрос и выводим ответ на экран
        SOAPMessage soapResponse = connection.call(message, url);
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        String uniqueID = UUID.randomUUID().toString();

        String dest = uniqueID + ".xml";
        StreamResult result = new StreamResult(dest);

        t.transform(sourceContent, result);
        return dest;
    }

    // Закрываем соединение
    public void closeConnection(SOAPConnection connection) throws SOAPException {
        connection.close();
        return;
    }
}
