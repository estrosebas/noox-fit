package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.ContactoDTO;
import com.noox.fitness_tracker.entity.Contacto;
import com.noox.fitness_tracker.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/contactos")
public class ContactoController {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private MessageSource messageSource;

    // Helper method to convert Entity to DTO
    private ContactoDTO convertToDTO(Contacto contacto) {
        return new ContactoDTO(
                contacto.getId(),
                contacto.getNombre(),
                contacto.getCorreo(),
                contacto.getMensaje()
        );
    }

    // Helper method to convert DTO to Entity
    private Contacto convertToEntity(ContactoDTO contactoDTO) {
        Contacto contacto = new Contacto();
        // ID is auto-generated, so not setting it from DTO
        contacto.setNombre(contactoDTO.getNombre());
        contacto.setCorreo(contactoDTO.getCorreo());
        contacto.setMensaje(contactoDTO.getMensaje());
        return contacto;
    }

    // This GET mapping is to ensure the form object is available if someone navigates to /contactos directly,
    // though the main form is on index.html.
    // It also helps if we want to display a dedicated contact page later.
    @GetMapping
    public String showContactForm(Model model) {
        if (!model.containsAttribute("contactoDTO")) {
            model.addAttribute("contactoDTO", new ContactoDTO());
        }
        return "index"; // Or a dedicated contact page if you create one e.g. "contact_form"
    }


    @PostMapping("/enviar")
    public String enviarContacto(@ModelAttribute("contactoDTO") ContactoDTO contactoDTO,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {
        try {
            Contacto contacto = convertToEntity(contactoDTO);
            contactoRepository.save(contacto);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("contact.form.success", null, locale));
        } catch (Exception e) {
            // Log the exception e
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("contact.form.error", null, locale));
        }
        // Redirect to the index page, specifically to the contact section
        return "redirect:/#contact";
    }
}
