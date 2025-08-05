package buildweeek.bw2.services;

import buildweeek.bw2.DTO.NewClienteDTO;
import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.enums.CustomerType;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;



    public Cliente saveCliente(NewClienteDTO payload)
    {
        this.clienteRepository.findByEmail(payload.email()).ifPresent(cliente -> {
            throw new BadRequestException("L'email: " + cliente.getEmail() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByPec(payload.pec()).ifPresent(cliente -> {
            throw new BadRequestException("La pec: " + cliente.getPec() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByPartitaIva(payload.partitaIva()).ifPresent(cliente -> {
            throw new BadRequestException("La partita iva: " + cliente.getPartitaIva() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByTelefono(payload.telefono()).ifPresent(cliente -> {
            throw new BadRequestException("Il telefono: " + cliente.getTelefono() + " appartiene già ad un'altro cliente!");
        });

        try {
            LocalDate dataInserimento = LocalDate.parse(payload.dataInserimento());
            LocalDate dataUltimoContatto = LocalDate.parse(payload.dataUltimoContatto());

        String customerTypeStr = payload.customerType();
        CustomerType type = null;

        if ("PA".equals(customerTypeStr.toUpperCase())) {
            type = CustomerType.PA;
        } else if ("SAS".equals(customerTypeStr.toUpperCase())) {
            type = CustomerType.SAS;
        } else if ("SPA".equals(customerTypeStr.toUpperCase())) {
            type = CustomerType.SPA;
        } else if ("SRL".equals(customerTypeStr.toUpperCase())) {
            type = CustomerType.SRL;
        } else {
            throw new BadRequestException("Inerisci un tipo di cliente valido!");
        }
        Cliente newCliente = new Cliente(payload.email(), payload.pec(), payload.telefono(), payload.partitaIva(), payload.ragioneSociale(),
                dataInserimento, dataUltimoContatto, payload.fatturatoAnnuale(), payload.emailContatto(),
                payload.nomeContatto(), payload.cognomeContatto(), payload.telefonoContatto(), type);

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }




    }

    Cliente savedCliente = this.clienteRepository.save(newCliente);

    return newCliente;

}
