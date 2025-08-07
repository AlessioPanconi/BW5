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

        if (nomeProvinciaCsv.equalsIgnoreCase("Valle d'Aosta/Vallée d'Aoste")) {
            nomeProvinciaCsv = "Aosta";
        } else if (nomeProvinciaCsv.equalsIgnoreCase("Verbano-Cusio-Ossola")) {
            nomeProvinciaCsv = "Verbania";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Monza e della Brianza")) {
            nomeProvinciaCsv = "Monza-Brianza";
        } else if (nomeProvinciaCsv.equalsIgnoreCase("Bolzano/Bozen")) {
            nomeProvinciaCsv = "Bolzano";
        } else if (nomeProvinciaCsv.equalsIgnoreCase("La Spezia")) {
            nomeProvinciaCsv = "La-Spezia";
        } else if (nomeProvinciaCsv.equalsIgnoreCase("Reggio nell'Emilia")) {
            nomeProvinciaCsv = "Reggio-Emilia";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Forlì-Cesena")) {
            nomeProvinciaCsv = "Forli-Cesena";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Pesaro e Urbino")) {
            nomeProvinciaCsv = "Pesaro-Urbino";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Ascoli Piceno")) {
            nomeProvinciaCsv = "Ascoli-Piceno";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Reggio Calabria")) {
            nomeProvinciaCsv = "Reggio-Calabria";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Vibo Valentia")) {
            nomeProvinciaCsv = "Vibo-Valentia";
        }else if (nomeProvinciaCsv.equalsIgnoreCase("Sud Sardegna")) {
            nomeProvinciaCsv = "Cagliari";
        }

        try (Connection conn = DriverManager.getConnection(dbURL, user, pass)) {
            String provinciaTrovata = null;

            String sqlProvincia = "SELECT provincia FROM provincia WHERE provincia ILIKE ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlProvincia)) {
                ps.setString(1, "%" + nomeProvinciaCsv + "%");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) provinciaTrovata = rs.getString("provincia");
            }

            if (provinciaTrovata != null) {
                String sqlInsert = "INSERT INTO comune (comune, provincia) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setString(1, nomeComuneCsv);
                    ps.setString(2, provinciaTrovata);
                    ps.executeUpdate();
                }
            } else {
                System.out.println("Provincia non trovata per: " + nomeProvinciaCsv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

