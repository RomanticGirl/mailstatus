package ru.gkcdu.mailstatus.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.gkcdu.mailstatus.Shpi;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDBConnection() throws ClassNotFoundException, SQLException {

        dbConnection = DriverManager.getConnection(url, user, pass);

        return dbConnection;
    }

    // получение LOV справочника
    public List<String> getLOV() {
        ResultSet resSet = null;
        List<String> result = new ArrayList<>();
        String select = "select aale.return_value, aale.DISPLAY_VALUE from apex_lov_view aale where 1=1 and aale.list_of_values_name = upper('RESULT_FILES_LOV')";

        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                String return_value = resSet.getString(2);
                result.add(return_value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Заполнение LOV-справочника
    public void updateLOV(String operType, String operTypeId) {
        String insert = "INSERT INTO apex_lov_view VALUES(?,?,?,?)";
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(insert);
            prSt.setString(0, "");

            // prSt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // очистка временной таблицы
    public void clearTempoSHPI() {
        
    }

    // Заполнение временной таблицы ШПИ
    public void putTempoSHPI(List<Shpi> shpiSet) {
        String insert = "INSERT INTO tempo_shpi VALUES(?,?,?,?)";
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(insert);
            prSt.setString(0, "");

            // prSt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteTempoSHPI() {

    }

    // Заполнение боевой таблицы ШПИ
    public void putMainSHPI(List<Shpi> shpiSet) {
        String insert = "INSERT INTO tempo_shpi VALUES(?,?,?,?)";
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(insert);
            prSt.setString(0, "");
            
            // prSt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Получение шпи из боевой базы для сравнения с новыми на проверку дублей
    public List<Shpi> getSHPI_history() {
        ResultSet resSet = null;

        String select = "select * from shpi_waiting_list where rownum <= "; // check_shpi_dt is null and

        List<Shpi> result = new ArrayList<>();
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                String fileId = resSet.getString(1);
                String shpiId = resSet.getString(2);
                Shpi shpi = new Shpi(shpiId, fileId, new HashMap<String, String>());
                result.add(shpi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Получение всех ШПИ из временной таблицы
    public List<Shpi> getAllSHPI_tempo() {
        ResultSet resSet = null;

        String select = "select * from shpi_waiting_list where rownum <= "; // check_shpi_dt is null and

        List<Shpi> result = new ArrayList<>();
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                String fileId = resSet.getString(1);
                String shpiId = resSet.getString(2);
                Shpi shpi = new Shpi(shpiId, fileId, new HashMap<String, String>());
                result.add(shpi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    // получение ШПИ для запросов на почту
    public List<Shpi> getSHPI_List(int countRows) {
        ResultSet resSet = null;

        String select = "select * from shpi_waiting_list where rownum <= " + countRows; // check_shpi_dt is null and

        List<Shpi> result = new ArrayList<>();
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                String fileId = resSet.getString(1);
                String shpiId = resSet.getString(2);
                Shpi shpi = new Shpi(shpiId, fileId, new HashMap<String, String>());
                result.add(shpi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
