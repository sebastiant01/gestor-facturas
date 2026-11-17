package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;
import com.itm.gestorfacturas.modules.pedido.IPedidoService;
import com.itm.gestorfacturas.modules.pedido.Pedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final IFacturaService facturaService;
    private final IPedidoService pedidoService;

    @GetMapping("/nueva/{idPedido}")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarFacturaFormulario(@PathVariable UUID idPedido, Model model) {
        Pedido pedido = this.pedidoService.consultarPedidoId(idPedido);

        String empleadoActual = SecurityContextHolder.getContext().getAuthentication().getName();

        FacturaRequestDTO requestDTO = FacturaRequestDTO.builder()
                .idPedido(pedido.getIdPedido())
                .nombreEmpleado(empleadoActual)
                .build();

        model.addAttribute("pedido", pedido);
        model.addAttribute("facturaRequestDTO", requestDTO);
        return "facturas/crear-factura";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarFactura(
            @Valid @ModelAttribute("facturaRequestDTO") FacturaRequestDTO requestDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            Pedido pedido = this.pedidoService.consultarPedidoId(requestDTO.getIdPedido());
            model.addAttribute("pedido", pedido);
            return "facturas/crear-factura";
        }

        try {
            this.facturaService.guardarFactura(requestDTO);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito", "¡Factura generada y registrada con éxito!");
            return "redirect:/facturas";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/pedidos/cola";
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String listarFacturas(
            @RequestParam(value = "empleado", required = false) String empleado,
            @RequestParam(value = "estado", required = false) EstadoFactura estado,
            Model model
    ) {
        List<Factura> facturas;

        if (empleado != null && !empleado.isBlank()) {
            facturas = this.facturaService.buscarPorEmpleado(empleado.trim());
        } else if (estado != null) {
            facturas = this.facturaService.buscarPorEstado(estado);
        } else {
            facturas = this.facturaService.listarFacturas();
        }

        model.addAttribute("facturas", facturas);
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("empleadoBuscado", empleado);
        model.addAttribute("estadoSeleccionado", estado);

        return "facturas/listar-facturas";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String verDetalleFactura(@PathVariable("id") UUID idFactura, Model model) {
        Factura factura = this.facturaService.consultarFacturaId(idFactura);
        model.addAttribute("factura", factura);
        return "facturas/detalle-factura";
    }
}
