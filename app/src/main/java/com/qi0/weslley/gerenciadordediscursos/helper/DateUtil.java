package com.qi0.weslley.gerenciadordediscursos.helper;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String fomatarData(String currentDate) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formato.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        String dataSelecionada = dataFormatada.format(data);
        return  dataSelecionada;
    }
}
