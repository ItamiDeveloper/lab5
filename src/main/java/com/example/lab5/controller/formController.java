package com.example.lab5.controller;

import com.example.lab5.model.form;  // Usé 'form' en minúsculas
import com.example.lab5.repository.formRepository;  // Usé 'formRepository' en minúsculas
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class formController {  // Usé 'formController' en minúsculas

    @Autowired
    private formRepository formRepository;  // Usé 'formRepository' en minúsculas

    // Mostrar el formulario para crear un nuevo formulario
    @GetMapping("/formulario")  // Esta es la URL donde se mostrará el formulario
    public String mostrarFormulario(Model model) {
        model.addAttribute("formulario", new form());  // Crea un nuevo objeto 'form' vacío
        return "form";  // Devuelve la vista form.html
    }

    // Procesar el formulario y guardarlo en la base de datos
    @PostMapping("/submitFormulario")
    public String procesarFormulario(@ModelAttribute form formulario, Model model) {
        // Guardar el formulario en la base de datos
        formRepository.save(formulario);

        // Agregar el formulario al modelo para mostrarlo en la vista
        model.addAttribute("formulario", formulario);

        // Redirigir a la lista de formularios
        return "redirect:/listarFormularios";
    }

    // Mostrar todos los formularios guardados
    @GetMapping("/listarFormularios")
    public String listarFormularios(Model model) {
        // Obtener todos los formularios desde la base de datos
        List<form> formularios = formRepository.findAll();

        // Agregar la lista de formularios al modelo
        model.addAttribute("formularios", formularios);

        // Crear un nuevo objeto formulario para el formulario de agregar nuevo
        model.addAttribute("formulario", new form());  // Usé 'form' en minúsculas

        // Devolver la vista de la lista de formularios (ahora hace referencia a list.html)
        return "list";  // Vista list.html
    }

    // Mostrar formulario para editar (cargar datos de la base)
    @GetMapping("/editarFormulario/{id}")
    public String editarFormulario(@PathVariable("id") Long id, Model model) {
        // Buscar el formulario en la base de datos
        form formulario = formRepository.findById(id).orElse(null);

        if (formulario != null) {
            // Cargar el formulario para editar
            model.addAttribute("formulario", formulario);
            return "form";  // Vista form.html
        }

        // Si no se encuentra el formulario, redirigir al listado
        return "redirect:/listarFormularios";
    }

    // Procesar la edición del formulario
    @PostMapping("/submitEdicionFormulario")
    public String submitEdicionFormulario(@ModelAttribute form formulario, Model model) {
        if (formulario.getId() != null) {
            // Buscar el formulario existente
            form formularioExistente = formRepository.findById(formulario.getId()).orElse(null);

            if (formularioExistente != null) {
                // Actualizamos los campos con los nuevos datos
                formularioExistente.setNombre(formulario.getNombre());
                formularioExistente.setEmail(formulario.getEmail());
                formularioExistente.setMensaje(formulario.getMensaje());

                // Guardamos el formulario actualizado (esto realiza un UPDATE)
                formRepository.save(formularioExistente);

                // Redirigir al listado de formularios
                return "redirect:/listarFormularios";
            }
        }

        // Si el formulario no existe, redirigir al listado
        return "redirect:/listarFormularios";
    }

    // Eliminar un formulario
    @GetMapping("/eliminarFormulario/{id}")
    public String eliminarFormulario(@PathVariable("id") Long id) {
        // Eliminamos el formulario por su ID
        formRepository.deleteById(id);

        // Redirigir al listado después de eliminar
        return "redirect:/listarFormularios";
    }
}
