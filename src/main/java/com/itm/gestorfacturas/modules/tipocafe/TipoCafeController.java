package com.itm.gestorfacturas.modules.tipocafe;

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
@RequestMapping("/tipos-cafe")
@RequiredArgsConstructor
public class TipoCafeController {

    private final ITipoCafeService tipoCafeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String listarTiposCafe(Model model) {
        model.addAttribute("tiposCafe", tipoCafeService.listarTiposCafe());
        return "tipocafe/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("tipoCafeRequest", new TipoCafeRequestDTO());
        return "tipocafe/formulario";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarTipoCafe(@Valid @ModelAttribute("tipoCafeRequest") TipoCafeRequestDTO tipoCafeRequest,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tipocafe/formulario";
        }
        tipoCafeService.guardarTipoCafe(tipoCafeRequest);
        redirectAttributes.addFlashAttribute(
                "mensaje", "Tipo de café creado correctamente.");
        return "redirect:/tipos-cafe";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable UUID id, Model model) {
        TipoCafe tipoCafe = tipoCafeService.consultarTipoCafeId(id);
        TipoCafeRequestDTO tipoCafeRequest = new TipoCafeRequestDTO();
        tipoCafeRequest.setNombreTipoCafe(tipoCafe.getNombreTipoCafe());
        tipoCafeRequest.setDescripcionTipoCafe(tipoCafe.getDescripcionTipoCafe());
        model.addAttribute("tipoCafeRequest", tipoCafeRequest);
        model.addAttribute("idTipoCafe", id);
        return "tipocafe/formulario";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarTipoCafe(@PathVariable UUID id,
                                     @Valid @ModelAttribute("tipoCafeRequest") TipoCafeRequestDTO tipoCafeRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tipocafe/formulario";
        }
        tipoCafeService.actualizarTipoCafe(id, tipoCafeRequest);
        redirectAttributes.addFlashAttribute(
                "mensaje", "Tipo de café actualizado correctamente.");
        return "redirect:/tipos-cafe";
    }
}
