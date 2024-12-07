package com.example.lab5.controller;

import com.example.lab5.model.form;
import com.example.lab5.repository.formRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class formController {

    @Autowired
    private formRepository formRepository;

    // Procesar el formulario y guardarlo en la base de datos
    @PostMapping("/submitFormulario")
    public String procesarFormulario(@ModelAttribute form formulario, Model model) {
        formRepository.save(formulario); // Guardar el formulario en la base de datos
        model.addAttribute("formulario", formulario);
        return "redirect:/listarFormularios"; // Redirigir a la lista de formularios
    }

    // Mostrar todos los formularios guardados
    @GetMapping("/listarFormularios")
    public String listarFormularios(Model model) {
        List<form> formularios = formRepository.findAll(); // Obtener todos los formularios
        model.addAttribute("formularios", formularios);
        model.addAttribute("formulario", new form()); // Para agregar un nuevo formulario
        return "list"; // Vista list.html
    }

    // Mostrar formulario para editar un formulario existente
    @GetMapping("/editarFormulario/{id}")
    public String editarFormulario(@PathVariable("id") Long id, Model model) {
        form formulario = formRepository.findById(id).orElse(null);
        if (formulario != null) {
            model.addAttribute("formulario", formulario);
            return "form"; // Vista form.html
        }
        return "redirect:/listarFormularios"; // Redirigir si no se encuentra
    }

    // Procesar la edición del formulario
    @PostMapping("/submitEdicionFormulario")
    public String submitEdicionFormulario(@ModelAttribute form formulario, Model model) {
        if (formulario.getId() != null) {
            form formularioExistente = formRepository.findById(formulario.getId()).orElse(null);
            if (formularioExistente != null) {
                formularioExistente.setNombre(formulario.getNombre());
                formularioExistente.setEmail(formulario.getEmail());
                formularioExistente.setMensaje(formulario.getMensaje());
                formRepository.save(formularioExistente);

                model.addAttribute("formulario", formularioExistente);
            }
        }
        return "redirect:/listarFormularios"; // Redirigir después de guardar los cambios
    }

    // Eliminar un formulario
    @GetMapping("/eliminarFormulario/{id}")
    public String eliminarFormulario(@PathVariable("id") Long id) {
        formRepository.deleteById(id);
        return "redirect:/listarFormularios"; // Redirigir después de eliminar
    }
}
