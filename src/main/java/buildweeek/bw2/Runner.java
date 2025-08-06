package buildweeek.bw2;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


@Component
public class Runner implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String dbURL;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pass;

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src/main/java/buildweeek/bw2/csv/province-italiane.csv";
        String line;
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Salta intestazione se presente
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(cvsSplitBy);
                inserisciInDatabase(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inserisciInDatabase(String[] data) {
        try(Connection conn = DriverManager.getConnection(dbURL, user, pass)) {
            String sql = "INSERT INTO provincia (id_provincia, sigla, provincia, regione) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                UUID uuid = UUID.randomUUID();

                ps.setObject(1, uuid);
                ps.setString(2, data[0]); // sigla
                ps.setString(3, data[1]); // provincia
                ps.setString(4, data[2]); // regione
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

