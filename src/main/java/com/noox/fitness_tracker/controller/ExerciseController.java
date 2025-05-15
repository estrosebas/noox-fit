package com.noox.fitness_tracker.controller;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.noox.fitness_tracker.model.Exercise;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    private List<Exercise> exercises;

    public ExerciseController() {
        exercises = new ArrayList<>();
        initializeExercises();
    }

    @GetMapping
    public List<Exercise> getAllExercises() {
        return exercises;
    }

    @GetMapping("/day/{day}")
    public List<Exercise> getExercisesByDay(@PathVariable String day) {
        return exercises.stream()
                .filter(exercise -> exercise.getDia().equalsIgnoreCase(day))
                .collect(java.util.stream.Collectors.toList());
    }

    @PostMapping("/{nombre}/toggle")
    public Exercise toggleExerciseStatus(@PathVariable String nombre) {
        Exercise exercise = exercises.stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);

        if (exercise != null) {
            exercise.setHecho(!exercise.isHecho());
        }

        return exercise;
    }

    private void initializeExercises() {
        // Push Ups
        Exercise pushUps = new Exercise();
        pushUps.setNombre("Push Ups");
        pushUps.setDescripcion("Flexiones clásicas para trabajar el pecho y tríceps.");
        pushUps.setHecho(false);
        pushUps.setImagen("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=2070&auto=format&fit=crop");
        pushUps.setDia("Lunes");
        pushUps.setUrlVideo("https://www.youtube.com/embed/SCVCLChPQFY");
        pushUps.setDificultad("Intermedia");
        pushUps.setInstrucciones(Arrays.asList(
            "Colócate en posición de plancha con las manos a la altura de los hombros",
            "Mantén el cuerpo recto y el core activado",
            "Baja el cuerpo doblando los codos hasta casi tocar el suelo",
            "Empuja hacia arriba hasta extender los brazos por completo"
        ));
        exercises.add(pushUps);

        // Sentadillas
        Exercise sentadillas = new Exercise();
        sentadillas.setNombre("Sentadillas");
        sentadillas.setDescripcion("Fortalece tus piernas y glúteos con este ejercicio básico.");
        sentadillas.setHecho(false);
        sentadillas.setImagen("https://images.unsplash.com/photo-1574680178050-55c6a6a96e0a?q=80&w=2069&auto=format&fit=crop");
        sentadillas.setDia("Lunes");
        sentadillas.setUrlVideo("https://www.youtube.com/embed/YaXPRqUwItQ");
        sentadillas.setDificultad("Básica");
        sentadillas.setInstrucciones(Arrays.asList(
            "Colócate de pie con los pies separados al ancho de los hombros",
            "Mantén la espalda recta y el pecho hacia adelante",
            "Baja flexionando las rodillas como si fueras a sentarte",
            "Regresa a la posición inicial empujando con los talones"
        ));
        exercises.add(sentadillas);

        // Plancha Abdominal
        Exercise plancha = new Exercise();
        plancha.setNombre("Plancha Abdominal");
        plancha.setDescripcion("Ejercicio isométrico para fortalecer el core y mejorar la estabilidad.");
        plancha.setHecho(false);
        plancha.setImagen("https://images.unsplash.com/photo-1566241142559-40e1dab266c6?q=80&w=2070&auto=format&fit=crop");
        plancha.setDia("Martes");
        plancha.setUrlVideo("https://www.youtube.com/embed/ASdvN_XEl_c");
        plancha.setDificultad("Básica");
        plancha.setInstrucciones(Arrays.asList(
            "Apóyate sobre los antebrazos y las puntas de los pies",
            "Mantén el cuerpo recto formando una línea desde la cabeza hasta los talones",
            "Contrae el abdomen y mantén la posición",
            "Respira de manera constante durante todo el ejercicio"
        ));
        exercises.add(plancha);

        // Burpees
        Exercise burpees = new Exercise();
        burpees.setNombre("Burpees");
        burpees.setDescripcion("Ejercicio de cuerpo completo de alta intensidad para mejorar la resistencia.");
        burpees.setHecho(false);
        burpees.setImagen("https://images.unsplash.com/photo-1599058917765-a780eda07a3e?q=80&w=2069&auto=format&fit=crop");
        burpees.setDia("Miércoles");
        burpees.setUrlVideo("https://www.youtube.com/embed/TU8QYVW0gDU");
        burpees.setDificultad("Avanzada");
        burpees.setInstrucciones(Arrays.asList(
            "Comienza de pie con los pies separados al ancho de los hombros",
            "Baja a posición de cuclillas y coloca las manos en el suelo",
            "Salta llevando los pies hacia atrás quedando en posición de plancha",
            "Haz una flexión, salta llevando los pies hacia las manos y salta hacia arriba"
        ));
        exercises.add(burpees);
    }
}
