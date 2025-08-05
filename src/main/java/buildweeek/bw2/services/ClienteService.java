package buildweeek.bw2.services;

import buildweeek.bw2.DTO.NewClienteDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.enums.CustomerType;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Page<Cliente> findAllClienti(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.clienteRepository.findAll(pageable);
    }

    public Cliente findClienteById(UUID idCliente) {
        return this.clienteRepository.findById(idCliente).orElseThrow(()-> new NotFoundException(idCliente));
    }

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

            if ("PA".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.PA;
            } else if ("SAS".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.SAS;
            } else if ("SPA".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.SPA;
            } else if ("SRL".equalsIgnoreCase(customerTypeStr)) {
            type = CustomerType.SRL;
            } else {
                throw new BadRequestException("Inerisci un tipo di cliente valido!");
            }

            Cliente newCliente = new Cliente(payload.email(),payload.pec(), payload.telefono(), payload.partitaIva(), payload.ragioneSociale(),
                    dataInserimento,dataUltimoContatto, payload.fatturatoAnnuale(), payload.emailContatto(), payload.nomeContatto(),
                    payload.cognomeContatto(), payload.telefonoContatto(),type);
            Cliente savedCliente = this.clienteRepository.save(newCliente);
            System.out.println("Il cliente con id: " + savedCliente.getIdCliente() + " è stato salvato correttamente!");
            return savedCliente;

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }

    }

    public Cliente findClienteByIdAndUpdate(UUID idCliente, NewClienteDTO payload)
    {
        Cliente found = findClienteById(idCliente);

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

            if ("PA".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.PA;
            } else if ("SAS".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.SAS;
            } else if ("SPA".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.SPA;
            } else if ("SRL".equalsIgnoreCase(customerTypeStr)) {
                type = CustomerType.SRL;
            } else {
                throw new BadRequestException("Inerisci un tipo di cliente valido!");
            }

            found.setEmail(payload.email());
            found.setPec(payload.pec());
            found.setTelefono(payload.telefono());
            found.setPartitaIva(payload.partitaIva());
            found.setRagioneSociale(payload.ragioneSociale());
            found.setDataInserimento(dataInserimento);
            found.setDataUltimoContatto(dataUltimoContatto);
            found.setFatturatoAnnuale(payload.fatturatoAnnuale());
            found.setEmailContatto(payload.emailContatto());
            found.setNomeContatto(payload.nomeContatto());
            found.setCognomeContatto(payload.cognomeContatto());
            found.setTelefonoContatto(payload.telefonoContatto());
            found.setCustomerType(type);

            Cliente modifiedCliente = this.clienteRepository.save(found);

            System.out.println("Il cliente con id: " + modifiedCliente.getIdCliente() + " è stato salvato correttamente!");
            return modifiedCliente;

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public void findClienteByIdAndDelete(UUID idCliente) {
        Cliente found = findClienteById(idCliente);
        this.clienteRepository.delete(found);
    }

    public Page<Cliente> findAll(int pageNumber, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize , Sort.by(sort) );
        return this.clienteRepository.findAll(pageable);
    }

    public Page<Cliente> findAllReverse(int pageNumber, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize , Sort.by(Sort.Direction.DESC,sort) );
        return this.clienteRepository.findAll(pageable);
    }


}
