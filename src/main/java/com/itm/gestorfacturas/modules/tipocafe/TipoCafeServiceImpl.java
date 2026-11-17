package com.itm.gestorfacturas.modules.tipocafe;

import com.itm.gestorfacturas.shared.exception.RecursoDuplicado;
import com.itm.gestorfacturas.shared.exception.RecursoNoEncontrado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TipoCafeServiceImpl implements ITipoCafeService {

    private final TipoCafeRepository tipoCafeRepository;

    @Override
    @Transactional
    public void guardarTipoCafe(TipoCafeRequestDTO tipoCafeRequest) {

        Optional<TipoCafe> tipoCafeExistente = this.tipoCafeRepository
                .findByNombreTipoCafeContainingIgnoreCase(tipoCafeRequest.getNombreTipoCafe());
        if (tipoCafeExistente.isPresent()) {
            throw new RecursoDuplicado("Este tipo de café ya existe.");
        }

        TipoCafe tipoCafe = new TipoCafe(tipoCafeRequest.getNombreTipoCafe(),
                tipoCafeRequest.getDescripcionTipoCafe());

        this.tipoCafeRepository.save(tipoCafe);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCafe consultarTipoCafeId(UUID idTipoCafe) {
        return this.tipoCafeRepository.findById(idTipoCafe)
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "Tipo de café no encontrado con ID: " + idTipoCafe));
    }

    @Override
    @Transactional
    public void actualizarTipoCafe(UUID idTipoCafe, TipoCafeRequestDTO tipoCafeRequest) {
        TipoCafe tipoCafe = this.tipoCafeRepository.findById(idTipoCafe)
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "Tipo de café no encontrado con id: " + idTipoCafe));

        tipoCafe.setNombreTipoCafe(tipoCafeRequest.getNombreTipoCafe());
        tipoCafe.setDescripcionTipoCafe(tipoCafeRequest.getDescripcionTipoCafe());

        this.tipoCafeRepository.save(tipoCafe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoCafe> listarTiposCafe() {
        return this.tipoCafeRepository.findAll();
    }
}
