package buildweeek.bw2.DTO;

import buildweeek.bw2.enums.CustomerType;

public record NewClienteDTO(String email,
                            String pec,
                            String telefono,
                            String partitaIva,
                            String ragioneSociale,
                            String dataInserimento,
                            String dataUltimoContatto,
                            double fatturatoAnnuale,
                            String emailContatto,
                            String nomeContatto,
                            String cognomeContatto,
                            String telefonoContatto,
                            String customerType) {
}
