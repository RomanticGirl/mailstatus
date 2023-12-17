package ru.gkcdu.mailstatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;

@SpringBootApplication
public class App {

    private static final String login = "wlFawocmHpEric";
    private static final String password = "YfMZWDIu5b4x";
    private static final String test = "80104390892323";

    // public static void main(String[] args) throws Exception {

    // /*
    // * Пример запроса:
    // * <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    // * xmlns:pos="http://fclient.russianpost.org/postserver">
    // * <soapenv:Header/>
    // * <soapenv:Body>
    // * <pos:answerByTicketRequest>
    // * <ticket>20150917162048476CLIENTID</ticket>
    // * <login>my_login</login>
    // * <password>my_password</password>
    // * </pos:answerByTicketRequest>
    // * </soapenv:Body>
    // * </soapenv:Envelope>
    // */

    // // Thread.sleep(100000); // задержка 15 минут.

    // // Сначала создаем соединение
    // SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
    // SOAPConnection connection = soapConnFactory.createConnection();
    // String url = "https://tracking.russianpost.ru/fc";
    // System.setProperty("javax.xml.soap.SAAJMetaFactory",
    // "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
    // // Затем создаем сообщение
    // MessageFactory messageFactory = MessageFactory.newInstance("SOAP 1.1
    // Protocol");
    // SOAPMessage message = messageFactory.createMessage();

    // // Создаем объекты, представляющие различные компоненты сообщения
    // SOAPPart soapPart = message.getSOAPPart();
    // SOAPEnvelope envelope = soapPart.getEnvelope();
    // SOAPBody body = envelope.getBody();

    // envelope.addNamespaceDeclaration("soapenv",
    // "http://schemas.xmlsoap.org/soap/envelope/");
    // envelope.addNamespaceDeclaration("pos",
    // "http://fclient.russianpost.org/postserver");
    // SOAPElement answerByTicketRequest =
    // body.addChildElement("answerByTicketRequest", "pos");
    // SOAPElement ticket = answerByTicketRequest.addChildElement("ticket");
    // SOAPElement login = answerByTicketRequest.addChildElement("login");
    // SOAPElement password = answerByTicketRequest.addChildElement("password");

    // // Заполняем значения
    // ticket.addTextNode(App.test + "CLIENTID");

    // login.addTextNode("wlFawocmHpEric");
    // password.addTextNode("YfMZWDIu5b4x");

    // // Сохранение сообщения
    // message.saveChanges();

    // // Отправляем запрос и выводим ответ на экран
    // SOAPMessage soapResponse = connection.call(message, url);
    // Source sourceContent = soapResponse.getSOAPPart().getContent();
    // Transformer t = TransformerFactory.newInstance().newTransformer();
    // t.setOutputProperty(OutputKeys.METHOD, "xml");
    // t.setOutputProperty(OutputKeys.INDENT, "yes");
    // StreamResult result = new StreamResult(System.out);
    // t.transform(sourceContent, result);

    // // Закрываем соединение
    // connection.close();

    // // SpringApplication.run(App.class, args);
    // }

    /*
     * Данный код создает запрос для получения информации о
     * конкретном отправлении по Идентификатору отправления (barcode).
     * Ответ на запрос выводится на экран в формате xml.
     * Пример запроса:
     * <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
     * xmlns:oper="http://russianpost.org/operationhistory"
     * xmlns:data="http://russianpost.org/operationhistory/data"
     * xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
     * <soap:Header/>
     * <soap:Body>
     * <oper:getOperationHistory>
     * <data:OperationHistoryRequest>
     * <data:Barcode>RA644000001RU</data:Barcode>
     * <data:MessageType>0</data:MessageType>
     * <data:Language>RUS</data:Language>
     * </data:OperationHistoryRequest>
     * <data:AuthorizationHeader soapenv:mustUnderstand="1">
     * <data:login>myLogin</data:login>
     * <data:password>myPassword</data:password>
     * </data:AuthorizationHeader>
     * </oper:getOperationHistory>
     * </soap:Body>
     * </soap:Envelope>
     */

    public static void main(String args[]) throws Exception {

        // Cоздаем соединение
        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = soapConnFactory.createConnection();
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
        barcode.addTextNode("80104390892323"); // RA644000001RU
        messageType.addTextNode("0");
        language.addTextNode("RUS");
        login.addTextNode("wlFawocmHpEric");
        password.addTextNode("YfMZWDIu5b4x");

        // Сохранение сообщения
        message.saveChanges();

        // Отправляем запрос и выводим ответ на экран
        SOAPMessage soapResponse = connection.call(message, url);
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        String dest = "test.XML";
        StreamResult result = new StreamResult(dest);
        // t.transform(sourceContent, result);

        // Закрываем соединение
        connection.close();

        BufferedReader br = new BufferedReader(new FileReader("./test.xml"));
        String body1 = br.lines().collect(Collectors.joining());
        StringReader reader = new StringReader(body1);
        JAXBContext context = JAXBContext.newInstance(Mail.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Mail screen = (Mail) unmarshaller.unmarshal(reader);

        StringWriter writer = new StringWriter();
        Marshaller marshaller = context.createMarshaller();
        // marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // screen.setTitle("Изменили заглавие");
        // screen.getButtonList().get(0).getDList().get(0).setColor("black");
        // screen.getButtonList().get(0).getDList().get(0).setD("Цвет кнопки черный");
        // marshaller.marshal(screen, writer);
    }

}
