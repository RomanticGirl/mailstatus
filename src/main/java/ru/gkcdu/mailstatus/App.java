package ru.gkcdu.mailstatus;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.gkcdu.mailstatus.bindings.ObjectFactory;
import ru.gkcdu.mailstatus.config.DatabaseHandler;
import ru.gkcdu.mailstatus.controllers.MailStatusController;
import ru.gkcdu.mailstatus.controllers.StaxStreamProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

@SpringBootApplication
public class App {

    private static final String test = "80104390892323";

    private static final String login = "wlFawocmHpEric";
    private static final String password = "YfMZWDIu5b4x";

    private static final int countRows = 1500; // Количество строк для запроса с базы и отправки запросов на почту

    public static void main(String args[]) throws Exception {

        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.deleteTempoSHPI();
        List<Shpi> shpiSet = dbHandler.getSHPI_List(countRows);

        while (shpiSet.size() > 0) {
            // Thread.sleep(600000);

            
            // Подключение к бд и получение из неё шпи в нужном количества
            List<String> lov = dbHandler.getLOV(); // получение значений справочника LOV

            MailStatusController mSc = new MailStatusController();
            SOAPConnection connection = mSc.getConnection();
            for (int i = 0; i < shpiSet.size(); i++) {

                String xmlDoc = mSc.getMailStatusXML(shpiSet.get(i).shpiId, login, password, connection);

                StaxStreamProcessor res = new StaxStreamProcessor(Files.newInputStream(Paths.get(xmlDoc)));
                res.xmlReader(shpiSet.get(i), lov);
                System.out.println(shpiSet.get(i).shpiId);
                // Проверка справочника LOV на содержание текущей операции (обновление
                // справочника при её отсутствии)

                // Удаление xml файлов, полученных из ответа после обработки
                try {
                    Files.delete(Paths.get(xmlDoc));
                } catch (NoSuchFileException x) {
                    System.err.format("%s: no such" + " file or directory%n", Paths.get(xmlDoc));
                } catch (DirectoryNotEmptyException x) {
                    System.err.format("%s not empty%n", Paths.get(xmlDoc));
                } catch (IOException x) {
                    // File permission problems are caught here.
                    System.err.println(x);
                }
            }

            System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

            mSc.closeConnection(connection);
            // Помещение в временную базу пачек собранных ШПИ
            dbHandler.putTempoSHPI(shpiSet);

        }

        // Проверка и сравнение готовых ШПИ в временной базе и в боевой
        List<Shpi> shpiSetHistory = dbHandler.getSHPI_history();
        List<Shpi> shpiSetTempo = dbHandler.getAllSHPI_tempo();
        for (int i = 0; i < shpiSetTempo.size(); i++) {
            if (shpiSetHistory.contains(shpiSetTempo.get(i))) {
                shpiSetTempo.remove(i);
            }
        }

        dbHandler.putMainSHPI(shpiSetTempo);
        // Добавить проверку на перемещение в основную базу по количеству строк
    }
}
