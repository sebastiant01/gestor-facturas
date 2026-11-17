package com.itm.gestorfacturas.modules.usuario;

import com.itm.gestorfacturas.modules.enums.RolUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @ModelAttribute("roles")
    public RolUsuario[] cargarRoles() {
        return RolUsuario.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuariosActivos());
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormulario(Model model){
        model.addAttribute("usuarioRequest", new UsuarioRequestDTO());
        return "usuario/formulario";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioRequest") UsuarioRequestDTO usuarioRequest,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "usuario/formulario";
        }

        try {
            usuarioService.guardarUsuario(usuarioRequest);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado correctamente OwO!!!");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException erm) {
            model.addAttribute("mensajeError", erm.getMessage());
            return "usuario/formulario";
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String detalleUsuario(@PathVariable UUID id, Model model){
        model.addAttribute("usuario", usuarioService.consultarUsuarioId(id));
        return "usuario/detalle";
    }

    @GetMapping("/{id}/cambiar-contrasena")
    @PreAuthorize("isAuthenticated()")
    public String mostrarFormularioCambioContrasena(@PathVariable UUID id, Model model) {
        Usuario usuario = usuarioService.consultarUsuarioId(id);
        model.addAttribute("usuario", usuario);
        return "usuario/cambiar-contrasena";
    }

    @PostMapping("/{id}/cambiar-contrasena")
    @PreAuthorize("isAuthenticated()")
    public String cambiarContrasena(@PathVariable UUID id,
                                    @RequestParam("nuevaContrasena") String nuevaContrasena,
                                    @RequestParam("confirmarContrasena") String confirmarContrasena,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            model.addAttribute("usuario", usuarioService.consultarUsuarioId(id));
            model.addAttribute("mensajeError", "Las contraseñas no coinciden OnO");
            return "usuario/cambiar-contrasena";
        }

        try {
            usuarioService.cambiarContrasena(id, nuevaContrasena);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña actualizada con éxito OwO!!! Ya puedes usar el sistema");
            return "redirect:/inicio";
        } catch (IllegalArgumentException erm) {
            model.addAttribute("usuario", usuarioService.consultarUsuarioId(id));
            model.addAttribute("mensajeError", erm.getMessage());
            return "usuario/cambiar-contrasena";
        }
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarUsuario(@PathVariable UUID id,
                                    @ModelAttribute("usuario") Usuario usuarioActualizado,
                                    RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizarUsuario(id, usuarioActualizado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado OwO"); 
        } catch (IllegalArgumentException erm) {
            redirectAttributes.addFlashAttribute("mensajeError", erm.getMessage());
        }
        return "redirect:/usuarios/" + id;
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarUsuario(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminarUsuario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario desactivado del sistema y de Auth0 >n<");
        return "redirect:/usuarios";
    }

}
