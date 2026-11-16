package com.itm.gestorfacturas.modules.tipocafe;

import java.util.List;
import java.util.UUID;

public interface ITipoCafeService {

    void guardarTipoCafe(TipoCafeRequestDTO tipoCafeRequest);

    TipoCafe consultarTipoCafeId(UUID idTipoCafe);

    void actualizarTipoCafe(UUID idTipoCafe, TipoCafeRequestDTO tipoCafeRequest);

    List<TipoCafe> listarTiposCafe();

}
