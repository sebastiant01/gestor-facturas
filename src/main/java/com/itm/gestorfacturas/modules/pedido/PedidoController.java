package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoService pedidoService;

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormulario(Model model) {
        model.addAttribute("pedidoRequest", new PedidoRequestDTO());
        return "pedido/formulario";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarPedido(@Valid @ModelAttribute("pedidoRequest") PedidoRequestDTO pedidoRequest,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pedido/formulario";
        }
        pedidoService.guardarPedido(pedidoRequest);
        redirectAttributes.addFlashAttribute("mensaje", "Pedido registrado correctamente.");
        return "redirect:/pedido";
    }

    /*
        El parámetro "estado" debe ser "PENDIENTE" o "FACTURADO", ej:

        .../pedidos?estado=PENDIENTE
     */
    @GetMapping
    public String listarPedidos(@RequestParam(required = false) EstadoPedido estado,
                                Model model) {
        if (estado != null) {
            model.addAttribute("pedidos", pedidoService.listarPedidosPorEstado(estado));
            model.addAttribute("estadoFiltro", estado);
        } else {
            model.addAttribute("pedidos", pedidoService.listarPedidos());
        }
        return "pedido/lista";
    }

    @GetMapping("/{id}")
    public String detallePedido(@PathVariable UUID id, Model model) {
        model.addAttribute("pedido", pedidoService.consultarPedidoId(id));
        return "pedido/detalle";
    }

}
