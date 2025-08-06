package buildweeek.bw2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


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
        String filePath2 = "src/main/java/buildweeek/bw2/csv/comuni-italiani.csv";
        String line;
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(cvsSplitBy);
                inserisciInDatabaseProvince(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath2))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(cvsSplitBy);
                inserisciInDatabaseComuni(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inserisciInDatabaseProvince(String[] data) {
        try(Connection conn = DriverManager.getConnection(dbURL, user, pass)) {
            String sql = "INSERT INTO provincia (provincia , sigla , regione) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, data[1]);
                ps.setString(2, data[0]);
                ps.setString(3, data[2]);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserisciInDatabaseComuni(String[] data) {
        String nomeComuneCsv = data[2].trim();
        String nomeProvinciaCsv = data[3].trim();

        try (Connection conn = DriverManager.getConnection(dbURL, user, pass)) {
            String SQLQueryForFindProvinciaNelDb = "SELECT provincia FROM provincia WHERE provincia ILIKE ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(SQLQueryForFindProvinciaNelDb)) {
                checkStmt.setString(1, nomeProvinciaCsv);

                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    String provinciaTrovata = rs.getString("provincia");
                    String sqlInsertComune = "INSERT INTO comune (comune, provincia) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertComune)) {
                        insertStmt.setString(1, nomeComuneCsv);
                        insertStmt.setString(2, provinciaTrovata);
                        insertStmt.executeUpdate();
                    }
                } else {
                    System.out.println("⚠ Provincia non trovata per il comune: " + nomeComuneCsv + " (Provincia CSV: " + nomeProvinciaCsv + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

