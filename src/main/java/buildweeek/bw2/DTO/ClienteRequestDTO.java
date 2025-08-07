package buildweeek.bw2.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {
    private NewClienteDTO cliente;
    private  NewIndirizzoSedeLegaleDTO sedeLegale;
    private NewIndirizzoSedeOperativaDTO sedeOperativa;
}
