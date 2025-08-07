package buildweeek.bw2.services;

import buildweeek.bw2.DTO.NewIndirizzoSedeLegaleDTO;
import buildweeek.bw2.DTO.NewIndirizzoSedeOperativaDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.DataInserimentoDTO;
import buildweeek.bw2.DTO.NewClienteDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.DataUltimoContattoDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.FatturatoAnnualeDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.PartialNameDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Comune;
import buildweeek.bw2.entities.Indirizzo;
import buildweeek.bw2.enums.CustomerType;
import buildweeek.bw2.enums.IndirizzoType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComuneService comuneService;

    @Autowired
    private IndirizzoService indirizzoService;

    public Page<Cliente> findAllClienti(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.clienteRepository.findAll(pageable);
    }

    public Cliente findClienteById(UUID idCliente) {
        return this.clienteRepository.findById(idCliente).orElseThrow(()-> new NotFoundException(idCliente));
    }

    public Cliente saveCliente(NewClienteDTO payloadCliente, NewIndirizzoSedeLegaleDTO payloadIndirizzoSL , NewIndirizzoSedeOperativaDTO payloadIndirizzoSO)
    {
        Comune comuneFoundSL =this.comuneService.findComuneById(payloadIndirizzoSL.comune());
        if (comuneFoundSL == null) throw new BadRequestException("Il comune della sede legale: " + payloadIndirizzoSL.comune() + " non esiste, assicurati di aver inserito un comune esistente");
        Comune comuneFoundSO =this.comuneService.findComuneById(payloadIndirizzoSO.comune());
        if (comuneFoundSO == null) throw new BadRequestException("Il comune della sede operativa: " + payloadIndirizzoSO.comune() + " non esiste, assicurati di aver inserito un comune esistente");

        this.clienteRepository.findByEmail(payloadCliente.email()).ifPresent(cliente -> {
            throw new BadRequestException("L'email: " + cliente.getEmail() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByPec(payloadCliente.pec()).ifPresent(cliente -> {
            throw new BadRequestException("La pec: " + cliente.getPec() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByPartitaIva(payloadCliente.partitaIva()).ifPresent(cliente -> {
            throw new BadRequestException("La partita iva: " + cliente.getPartitaIva() + " appartiene già ad un'altro cliente!");
        });

        this.clienteRepository.findByTelefono(payloadCliente.telefono()).ifPresent(cliente -> {
            throw new BadRequestException("Il telefono: " + cliente.getTelefono() + " appartiene già ad un'altro cliente!");
        });

        try {
            LocalDate dataInserimento = LocalDate.parse(payloadCliente.dataInserimento());
            LocalDate dataUltimoContatto = LocalDate.parse(payloadCliente.dataUltimoContatto());

            if(dataInserimento.isAfter(LocalDate.now())) throw new BadRequestException("La data di inserimento non può essere dopo oggi!");
            if(dataUltimoContatto.isAfter(LocalDate.now()) && dataUltimoContatto.isBefore(dataInserimento))
                throw new BadRequestException("La data di ultimo contatto non può essere dopo oggi o prima della data di inserimento!");

            String customerTypeStr = payloadCliente.customerType();
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

            Indirizzo newIndirizzoSL = new Indirizzo(payloadIndirizzoSL.via(), payloadIndirizzoSL.civico(), payloadIndirizzoSL.localita(),
                    payloadIndirizzoSL.cap(), comuneFoundSL, IndirizzoType.SEDELEGALE, comuneFoundSL.getProvincia());

            Indirizzo newIndirizzoSO = new Indirizzo(payloadIndirizzoSO.via(), payloadIndirizzoSO.civico(), payloadIndirizzoSO.localita(),
                    payloadIndirizzoSO.cap(), comuneFoundSO, IndirizzoType.SEDEOPERATIVA, comuneFoundSO.getProvincia());

            this.indirizzoService.save(newIndirizzoSL);
            this.indirizzoService.save(newIndirizzoSO);
            List<Indirizzo> listaIndirizzi = new ArrayList<>();
            listaIndirizzi.add(newIndirizzoSL);
            listaIndirizzi.add(newIndirizzoSO);

            Cliente newCliente = new Cliente(payloadCliente.email(),payloadCliente.pec(), payloadCliente.telefono(), payloadCliente.partitaIva(), payloadCliente.ragioneSociale(),
                    dataInserimento,dataUltimoContatto, payloadCliente.fatturatoAnnuale(), payloadCliente.emailContatto(), payloadCliente.nomeContatto(),
                    payloadCliente.cognomeContatto(), payloadCliente.telefonoContatto(),type,listaIndirizzi);
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

            if(dataInserimento.isAfter(LocalDate.now())) throw new BadRequestException("La data di inserimento non può essere dopo oggi!");
            if(dataUltimoContatto.isAfter(LocalDate.now()) && dataUltimoContatto.isBefore(dataInserimento))
                throw new BadRequestException("La data di ultimo contatto non può essere dopo oggi o prima della data di inserimento!");

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

//    public Page<Cliente> findByProvinciaSL(int pageNumber, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        return clienteRepository.findClientiAndOrderByProvinciaOfIndirizzoSL(pageable);
//    }


    public List<Cliente> findClientiByFatturatoMaggiore(FatturatoAnnualeDTO payload)
    {
        List<Cliente> clienti = this.clienteRepository.findAll();
        List<Cliente> clientiFiltrati = clienti.stream()
                .filter(cliente -> cliente.getFatturatoAnnuale() >= payload.fatturatoAnnuale()).toList();

        if(clientiFiltrati.isEmpty()) throw new NotFoundException("Non ci sono clienti con fatturato maggiore di: " + payload.fatturatoAnnuale());

        return clientiFiltrati;
    }

    public List<Cliente> findClientiByFatturatoMinore(FatturatoAnnualeDTO payload)
    {
        List<Cliente> clienti = this.clienteRepository.findAll();
        List<Cliente> clientiFiltrati = clienti.stream()
                .filter(cliente -> cliente.getFatturatoAnnuale() < payload.fatturatoAnnuale()).toList();

        if(clientiFiltrati.isEmpty()) throw new NotFoundException("Non ci sono clienti con fatturato minore di: " + payload.fatturatoAnnuale());


        return clientiFiltrati;
    }

    public List<Cliente> findClientiByBeforeDataInserimento(DataInserimentoDTO payload)
    {
        try {
            LocalDate dataInserimento = LocalDate.parse(payload.dataInserimento());
            List<Cliente> clienti = this.clienteRepository.findAll();

            List<Cliente> clientiFiltrati = clienti.stream()
                    .filter(cliente -> cliente.getDataInserimento().isBefore(dataInserimento)).toList();

            if(clientiFiltrati.isEmpty()) throw new NotFoundException("Non ci sono clienti inseriti prima di: " + payload.dataInserimento());


            return clientiFiltrati;

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public List<Cliente> findClientiByAfterDataInserimento(DataInserimentoDTO payload)
    {
        try {
            LocalDate dataInserimento = LocalDate.parse(payload.dataInserimento());
            List<Cliente> clienti = this.clienteRepository.findAll();

            List<Cliente> clientiFiltrati = clienti.stream()
                    .filter(cliente -> cliente.getDataInserimento().isAfter(dataInserimento)).toList();

            if(clientiFiltrati.isEmpty()) throw new NotFoundException("Non ci sono clienti inseriti dopo di: " + payload.dataInserimento());


            return clientiFiltrati;

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public List<Cliente> findClientiByDataUltimoContatto(DataUltimoContattoDTO payload)
    {
        try {
            LocalDate dataInserimento = LocalDate.parse(payload.dataUltimoContatto());
            List<Cliente> clienti = this.clienteRepository.findAll();

            List<Cliente> clientiFiltrati = clienti.stream()
                    .filter(cliente -> cliente.getDataUltimoContatto().isEqual(dataInserimento)).toList();

            if(clientiFiltrati.isEmpty()) throw new NotFoundException("Non ci sono clienti contattati il giorno: " + payload.dataUltimoContatto());

            return clientiFiltrati;



        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public List<Cliente> findClientiByPartialName(PartialNameDTO payload)
    {
        List<Cliente> clientiTrovati = this.clienteRepository.findByNomeContattoContainingIgnoreCase(payload.nomeContatto());

        if(clientiTrovati.isEmpty()) throw new NotFoundException("Nessun cliente trovato!");

       return clientiTrovati;
    }

    public Indirizzo findClienteByIdAndUpdateIndirizzoSL(UUID idCliente, NewIndirizzoSedeLegaleDTO payload)
    {
        Cliente foundCliente = findClienteById(idCliente);
        List<Indirizzo> indirizziCliente = foundCliente.getIndirizzi();

        Optional<Indirizzo> indirizzoFound = indirizziCliente.stream()
                .filter(indirizzo -> indirizzo.getIndirizzoType().equals(IndirizzoType.SEDELEGALE))
                .findFirst();

        Indirizzo indirizzoToUpdate = indirizzoFound.orElseThrow(() ->
                new NotFoundException("Nessun indirizzo con tipo SEDELEGALE trovato.")
        );

        Comune comuneFound = this.comuneService.findComuneById(payload.comune());
        if (comuneFound == null) {
            throw new BadRequestException("Il comune con id " + payload.comune() + " non esiste.");
        }

        indirizzoToUpdate.setVia(payload.via());
        indirizzoToUpdate.setCap(payload.cap());
        indirizzoToUpdate.setCivico(payload.civico());
        indirizzoToUpdate.setLocalita(payload.localita());
        indirizzoToUpdate.setComune(comuneFound);
        indirizzoToUpdate.setProvincia(comuneFound.getProvincia());

        Indirizzo indirizzoSaved = this.indirizzoService.save(indirizzoToUpdate);
        System.out.println("Indirizzo modificato correttamente");
        return indirizzoSaved;
    }

    public Indirizzo findClienteByIdAndUpdateIndirizzoSO(UUID idCliente, NewIndirizzoSedeOperativaDTO payload)
    {
        Cliente foundCliente = findClienteById(idCliente);
        List<Indirizzo> indirizziCliente = foundCliente.getIndirizzi();

        Optional<Indirizzo> indirizzoFound = indirizziCliente.stream()
                .filter(indirizzo -> indirizzo.getIndirizzoType().equals(IndirizzoType.SEDEOPERATIVA))
                .findFirst();

        Indirizzo indirizzoToUpdate = indirizzoFound.orElseThrow(() ->
                new NotFoundException("Nessun indirizzo con tipo SEDEOPERATIVA trovato.")
        );

        Comune comuneFound = this.comuneService.findComuneById(payload.comune());
        if (comuneFound == null) {
            throw new BadRequestException("Il comune con id " + payload.comune() + " non esiste.");
        }

        indirizzoToUpdate.setVia(payload.via());
        indirizzoToUpdate.setCap(payload.cap());
        indirizzoToUpdate.setCivico(payload.civico());
        indirizzoToUpdate.setLocalita(payload.localita());
        indirizzoToUpdate.setComune(comuneFound);
        indirizzoToUpdate.setProvincia(comuneFound.getProvincia());

        Indirizzo indirizzoSaved = this.indirizzoService.save(indirizzoToUpdate);
        System.out.println("Indirizzo modificato correttamente");
        return indirizzoSaved;
    }

}
