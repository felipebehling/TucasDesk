package com.example.Tucasdesk.mappers;

import com.example.Tucasdesk.dtos.*;
import com.example.Tucasdesk.model.*;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class responsible for converting {@link Chamado} entities into their response DTO representations.
 */
public final class ChamadoMapper {

    private ChamadoMapper() {
    }

    /**
     * Maps a {@link Chamado} to {@link ChamadoResponseDTO} including its interactions.
     *
     * @param chamado     the ticket entity.
     * @param interacoes  the ordered list of interactions associated with the ticket.
     * @return the mapped response DTO.
     */
    @Nullable
    public static ChamadoResponseDTO toChamadoResponseDTO(Chamado chamado, List<Interacao> interacoes) {
        if (chamado == null) {
            return null;
        }

        List<InteracaoResponseDTO> interacaoDTOS = Collections.emptyList();
        if (interacoes != null && !interacoes.isEmpty()) {
            interacaoDTOS = interacoes.stream()
                    .map(ChamadoMapper::toInteracaoResponseDTO)
                    .collect(Collectors.toList());
        }

        return new ChamadoResponseDTO(
                chamado.getIdChamado(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                toLookupResponseDTO(chamado.getCategoria()),
                toLookupResponseDTO(chamado.getStatus()),
                toLookupResponseDTO(chamado.getPrioridade()),
                toUsuarioResumoDTO(chamado.getUsuario()),
                toUsuarioResumoDTO(chamado.getTecnico()),
                chamado.getDataAbertura(),
                chamado.getDataFechamento(),
                interacaoDTOS
        );
    }

    /**
     * Maps a {@link Interacao} entity to {@link InteracaoResponseDTO}.
     *
     * @param interacao the interaction entity to map.
     * @return the mapped DTO.
     */
    @Nullable
    public static InteracaoResponseDTO toInteracaoResponseDTO(Interacao interacao) {
        if (interacao == null) {
            return null;
        }
        return new InteracaoResponseDTO(
                interacao.getIdInteracao(),
                interacao.getMensagem(),
                interacao.getAnexoUrl(),
                interacao.getDataInteracao(),
                toUsuarioResumoDTO(interacao.getUsuario())
        );
    }

    @Nullable
    private static LookupResponseDTO toLookupResponseDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new LookupResponseDTO(categoria.getIdCategoria(), categoria.getNome());
    }

    @Nullable
    private static LookupResponseDTO toLookupResponseDTO(Status status) {
        if (status == null) {
            return null;
        }
        return new LookupResponseDTO(status.getIdStatus(), status.getNome());
    }

    @Nullable
    private static LookupResponseDTO toLookupResponseDTO(Prioridade prioridade) {
        if (prioridade == null) {
            return null;
        }
        return new LookupResponseDTO(prioridade.getIdPrioridade(), prioridade.getNome());
    }

    @Nullable
    private static UsuarioResumoDTO toUsuarioResumoDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResumoDTO(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail());
    }
}
