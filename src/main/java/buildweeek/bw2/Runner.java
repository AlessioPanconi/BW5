package buildweeek.bw2;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;


@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        String filePath = "buildweeek.bw2.csv.province-italiane.csv;";
        String line;
        String cvsSplitBy = ";";

        CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                inserisciInDatabase(data);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void inserisciInDatabase(String[] data) {
        try{
            Connection conn = DriverManager.getConnection()
        }
    }
}
