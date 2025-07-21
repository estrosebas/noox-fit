package com.noox.fitness_tracker.config;

import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Ejercicio;
import com.noox.fitness_tracker.entity.Rol;
import com.noox.fitness_tracker.entity.Rutina;
import com.noox.fitness_tracker.entity.Usuario;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.EjercicioRepository;
import com.noox.fitness_tracker.repository.RolRepository;
import com.noox.fitness_tracker.repository.RutinaRepository;
import com.noox.fitness_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadEjercicios();
        loadUsuarioDemo();
        loadRutinasDemo();
    }

    private void loadRoles() {
        if (rolRepository.count() == 0) {
            Rol adminRole = new Rol("ADMIN", "Administrator role with full access");
            Rol userRole = new Rol("USER", "Regular user role with limited access");
            
            rolRepository.save(adminRole);
            rolRepository.save(userRole);
            
            System.out.println("Roles cargados en la base de datos");
        }
    }

    private void loadEjercicios() {
        if (ejercicioRepository.count() == 0) {
            // Crear y guardar ejercicios usando setters
            Ejercicio ejercicio1 = new Ejercicio();
            ejercicio1.setNombre("Press de Banca");
            ejercicio1.setDescripcion("Ejercicio básico para desarrollar los músculos pectorales, deltoides y tríceps. Acuéstate en el banco con los pies firmemente en el suelo.");
            ejercicio1.setImagenurl("/img/press-banca.jpg");
            ejercicio1.setUrlvideo("https://www.youtube.com/embed/rT7DgCr-3pg");
            ejercicio1.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio1);

            Ejercicio ejercicio2 = new Ejercicio();
            ejercicio2.setNombre("Flexiones de Pecho");
            ejercicio2.setDescripcion("Ejercicio de peso corporal que fortalece pectorales, hombros y tríceps. Mantén el cuerpo recto durante todo el movimiento.");
            ejercicio2.setImagenurl("/img/flexiones.svg");
            ejercicio2.setUrlvideo("https://www.youtube.com/embed/IODxDxX7oi4");
            ejercicio2.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio2);

            Ejercicio ejercicio3 = new Ejercicio();
            ejercicio3.setNombre("Press Inclinado con Mancuernas");
            ejercicio3.setDescripcion("Variante del press que enfatiza la parte superior del pectoral. Ajusta el banco a 30-45 grados de inclinación.");
            ejercicio3.setImagenurl("/img/press-inclinado.jpg");
            ejercicio3.setUrlvideo("https://www.youtube.com/embed/8iPEnn-ltC8");
            ejercicio3.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio3);

            Ejercicio ejercicio4 = new Ejercicio();
            ejercicio4.setNombre("Aperturas con Mancuernas");
            ejercicio4.setDescripcion("Ejercicio de aislamiento que estira y contrae los pectorales. Controla el peso en todo momento para evitar lesiones.");
            ejercicio4.setImagenurl("/img/aperturas.jpg");
            ejercicio4.setUrlvideo("https://www.youtube.com/embed/eozdVDA78K0");
            ejercicio4.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio4);

            Ejercicio ejercicio5 = new Ejercicio();
            ejercicio5.setNombre("Dominadas");
            ejercicio5.setDescripcion("Ejercicio de tracción que desarrolla el ancho de la espalda, bíceps y antebrazos. Excelente para construir fuerza funcional.");
            ejercicio5.setImagenurl("/img/dominadas.jpg");
            ejercicio5.setUrlvideo("https://www.youtube.com/embed/eGo4IYlbE5g");
            ejercicio5.setDificultad("Avanzado");
            ejercicioRepository.save(ejercicio5);

            Ejercicio ejercicio6 = new Ejercicio();
            ejercicio6.setNombre("Remo con Barra");
            ejercicio6.setDescripcion("Ejercicio fundamental para desarrollar grosor en la espalda media. Mantén la espalda recta y aprieta los omóplatos.");
            ejercicio6.setImagenurl("/img/remo-barra.jpg");
            ejercicio6.setUrlvideo("https://www.youtube.com/embed/FWJR5Ve8bnQ");
            ejercicio6.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio6);

            Ejercicio ejercicio7 = new Ejercicio();
            ejercicio7.setNombre("Pulldown al Pecho");
            ejercicio7.setDescripcion("Variante asistida de las dominadas, ideal para principiantes. Enfócate en tirar con los dorsales, no con los brazos.");
            ejercicio7.setImagenurl("/img/pulldown.jpg");
            ejercicio7.setUrlvideo("https://www.youtube.com/embed/CAwf7n6Luuc");
            ejercicio7.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio7);

            Ejercicio ejercicio8 = new Ejercicio();
            ejercicio8.setNombre("Sentadillas");
            ejercicio8.setDescripcion("Rey de los ejercicios de pierna. Desarrolla cuádriceps, glúteos e isquiotibiales. Mantén el pecho erguido y las rodillas alineadas.");
            ejercicio8.setImagenurl("/img/sentadillas.svg");
            ejercicio8.setUrlvideo("https://www.youtube.com/embed/ultWZbUMPL8");
            ejercicio8.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio8);

            Ejercicio ejercicio9 = new Ejercicio();
            ejercicio9.setNombre("Peso Muerto");
            ejercicio9.setDescripcion("Ejercicio compuesto que trabaja toda la cadena posterior. Enfócate en mantener la columna neutra durante todo el movimiento.");
            ejercicio9.setImagenurl("/img/peso-muerto.jpg");
            ejercicio9.setUrlvideo("https://www.youtube.com/embed/op9kVnSso6Q");
            ejercicio9.setDificultad("Avanzado");
            ejercicioRepository.save(ejercicio9);

            Ejercicio ejercicio10 = new Ejercicio();
            ejercicio10.setNombre("Prensa de Piernas");
            ejercicio10.setDescripcion("Ejercicio seguro para desarrollar fuerza en cuádriceps y glúteos. Permite usar cargas pesadas con menor riesgo.");
            ejercicio10.setImagenurl("/img/prensa-piernas.jpg");
            ejercicio10.setUrlvideo("https://www.youtube.com/embed/IZxyjW7MPJQ");
            ejercicio10.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio10);

            Ejercicio ejercicio11 = new Ejercicio();
            ejercicio11.setNombre("Zancadas");
            ejercicio11.setDescripcion("Ejercicio unilateral que mejora equilibrio y desarrolla piernas de forma funcional. Alterna las piernas en cada repetición.");
            ejercicio11.setImagenurl("/img/zancadas.jpg");
            ejercicio11.setUrlvideo("https://www.youtube.com/embed/D7KaRcUTQeE");
            ejercicio11.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio11);

            Ejercicio ejercicio12 = new Ejercicio();
            ejercicio12.setNombre("Curl de Bíceps con Barra");
            ejercicio12.setDescripcion("Ejercicio básico para desarrollar los bíceps. Mantén los codos fijos y controla tanto la subida como la bajada.");
            ejercicio12.setImagenurl("/img/curl-biceps.jpg");
            ejercicio12.setUrlvideo("https://www.youtube.com/embed/ykJmrZ5v0Oo");
            ejercicio12.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio12);

            Ejercicio ejercicio13 = new Ejercicio();
            ejercicio13.setNombre("Press Frances");
            ejercicio13.setDescripcion("Ejercicio de aislamiento para tríceps. Mantén los codos estables y enfócate en la extensión completa del codo.");
            ejercicio13.setImagenurl("/img/press-frances.jpg");
            ejercicio13.setUrlvideo("https://www.youtube.com/embed/-Vyt2QdsR7E");
            ejercicio13.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio13);

            Ejercicio ejercicio14 = new Ejercicio();
            ejercicio14.setNombre("Fondos en Paralelas");
            ejercicio14.setDescripcion("Ejercicio de peso corporal que desarrolla tríceps y pectorales inferiores. Progresa gradualmente en profundidad.");
            ejercicio14.setImagenurl("/img/fondos-paralelas.jpg");
            ejercicio14.setUrlvideo("https://www.youtube.com/embed/2z8JmcrW-As");
            ejercicio14.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio14);

            Ejercicio ejercicio15 = new Ejercicio();
            ejercicio15.setNombre("Press Militar");
            ejercicio15.setDescripcion("Ejercicio fundamental para desarrollar fuerza en hombros y core. Mantén el abdomen contraído durante todo el movimiento.");
            ejercicio15.setImagenurl("/img/press-militar.jpg");
            ejercicio15.setUrlvideo("https://www.youtube.com/embed/QAQ64hK4Xxs");
            ejercicio15.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio15);

            Ejercicio ejercicio16 = new Ejercicio();
            ejercicio16.setNombre("Elevaciones Laterales");
            ejercicio16.setDescripcion("Ejercicio de aislamiento para deltoides medios. Controla el peso y evita balancear el cuerpo.");
            ejercicio16.setImagenurl("/img/elevaciones-laterales.jpg");
            ejercicio16.setUrlvideo("https://www.youtube.com/embed/3VcKaXpzqRo");
            ejercicio16.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio16);

            Ejercicio ejercicio17 = new Ejercicio();
            ejercicio17.setNombre("Plancha");
            ejercicio17.setDescripcion("Ejercicio isométrico que fortalece todo el core. Mantén el cuerpo recto desde la cabeza hasta los pies.");
            ejercicio17.setImagenurl("/img/plancha.svg");
            ejercicio17.setUrlvideo("https://www.youtube.com/embed/ASdvN_XEl_c");
            ejercicio17.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio17);

            Ejercicio ejercicio18 = new Ejercicio();
            ejercicio18.setNombre("Crunches");
            ejercicio18.setDescripcion("Ejercicio básico para abdominales. Enfócate en la contracción del abdomen, no en levantar el torso completo.");
            ejercicio18.setImagenurl("/img/crunches.jpg");
            ejercicio18.setUrlvideo("https://www.youtube.com/embed/Xyd_fa5zoEU");
            ejercicio18.setDificultad("Principiante");
            ejercicioRepository.save(ejercicio18);

            Ejercicio ejercicio19 = new Ejercicio();
            ejercicio19.setNombre("Mountain Climbers");
            ejercicio19.setDescripcion("Ejercicio cardiovascular que trabaja core y piernas. Mantén un ritmo constante y el core activado.");
            ejercicio19.setImagenurl("/img/mountain-climbers.jpg");
            ejercicio19.setUrlvideo("https://www.youtube.com/embed/kLh-uczlPLg");
            ejercicio19.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio19);

            Ejercicio ejercicio20 = new Ejercicio();
            ejercicio20.setNombre("Burpees");
            ejercicio20.setDescripcion("Ejercicio de cuerpo completo que combina fuerza y cardio. Ideal para quemar calorías y mejorar condición física.");
            ejercicio20.setImagenurl("/img/burpees.jpg");
            ejercicio20.setUrlvideo("https://www.youtube.com/embed/auBLPXO8Fww");
            ejercicio20.setDificultad("Avanzado");
            ejercicioRepository.save(ejercicio20);

            Ejercicio ejercicio21 = new Ejercicio();
            ejercicio21.setNombre("Saltos en Cajón");
            ejercicio21.setDescripcion("Ejercicio pliométrico que desarrolla potencia en piernas. Enfócate en un aterrizaje suave y controlado.");
            ejercicio21.setImagenurl("/img/box-jumps.jpg");
            ejercicio21.setUrlvideo("https://www.youtube.com/embed/NBY9-kTuHEk");
            ejercicio21.setDificultad("Intermedio");
            ejercicioRepository.save(ejercicio21);

            System.out.println("Ejercicios cargados en la base de datos: " + ejercicioRepository.count() + " ejercicios");
        }
    }

    private void loadUsuarioDemo() {
        if (usuarioRepository.count() == 0) {
            // Crear usuario demo
            Usuario usuarioDemo = new Usuario();
            usuarioDemo.setNombre("Demo");
            usuarioDemo.setApellido("User");
            usuarioDemo.setEdad(25);
            usuarioDemo.setDireccion("Dirección de ejemplo");
            usuarioDemo.setSexo("M");
            usuarioRepository.save(usuarioDemo);

            // Crear cuenta demo
            Cuenta cuentaDemo = new Cuenta();
            cuentaDemo.setUsuario(usuarioDemo);
            cuentaDemo.setCorreo("demo@nooxfit.com");
            cuentaDemo.setContraseña(passwordEncoder.encode("demo123"));
            
            // Asignar rol USER
            Optional<Rol> rolUser = rolRepository.findByNombre("USER");
            if (rolUser.isPresent()) {
                cuentaDemo.setRol(rolUser.get());
            } else {
                // Crear rol USER si no existe
                Rol userRole = new Rol("USER", "Regular user role");
                rolRepository.save(userRole);
                cuentaDemo.setRol(userRole);
            }
            
            cuentaRepository.save(cuentaDemo);
            System.out.println("Usuario demo creado: " + cuentaDemo.getCorreo());
        }
    }

    private void loadRutinasDemo() {
        if (rutinaRepository.count() == 0) {
            // Obtener la primera cuenta (usuario demo)
            List<Cuenta> cuentas = cuentaRepository.findAll();
            if (cuentas.isEmpty()) {
                System.out.println("No hay cuentas disponibles para crear rutinas");
                return;
            }

            Cuenta cuentaDemo = cuentas.get(0);
            List<Ejercicio> ejercicios = ejercicioRepository.findAll();

            if (ejercicios.isEmpty()) {
                System.out.println("No hay ejercicios disponibles para crear rutinas");
                return;
            }

            // Rutina Lunes - Pecho y Tríceps
            String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
            
            // Lunes - Pecho y Tríceps
            asignarEjercicioARutina(cuentaDemo, "Lunes", buscarEjercicioPorNombre(ejercicios, "Press de Banca"));
            asignarEjercicioARutina(cuentaDemo, "Lunes", buscarEjercicioPorNombre(ejercicios, "Flexiones de Pecho"));
            asignarEjercicioARutina(cuentaDemo, "Lunes", buscarEjercicioPorNombre(ejercicios, "Press Frances"));
            asignarEjercicioARutina(cuentaDemo, "Lunes", buscarEjercicioPorNombre(ejercicios, "Fondos en Paralelas"));

            // Martes - Espalda y Bíceps
            asignarEjercicioARutina(cuentaDemo, "Martes", buscarEjercicioPorNombre(ejercicios, "Dominadas"));
            asignarEjercicioARutina(cuentaDemo, "Martes", buscarEjercicioPorNombre(ejercicios, "Remo con Barra"));
            asignarEjercicioARutina(cuentaDemo, "Martes", buscarEjercicioPorNombre(ejercicios, "Pulldown al Pecho"));
            asignarEjercicioARutina(cuentaDemo, "Martes", buscarEjercicioPorNombre(ejercicios, "Curl de Bíceps con Barra"));

            // Miércoles - Piernas
            asignarEjercicioARutina(cuentaDemo, "Miércoles", buscarEjercicioPorNombre(ejercicios, "Sentadillas"));
            asignarEjercicioARutina(cuentaDemo, "Miércoles", buscarEjercicioPorNombre(ejercicios, "Peso Muerto"));
            asignarEjercicioARutina(cuentaDemo, "Miércoles", buscarEjercicioPorNombre(ejercicios, "Prensa de Piernas"));
            asignarEjercicioARutina(cuentaDemo, "Miércoles", buscarEjercicioPorNombre(ejercicios, "Zancadas"));

            // Jueves - Hombros
            asignarEjercicioARutina(cuentaDemo, "Jueves", buscarEjercicioPorNombre(ejercicios, "Press Militar"));
            asignarEjercicioARutina(cuentaDemo, "Jueves", buscarEjercicioPorNombre(ejercicios, "Elevaciones Laterales"));
            asignarEjercicioARutina(cuentaDemo, "Jueves", buscarEjercicioPorNombre(ejercicios, "Press Inclinado con Mancuernas"));

            // Viernes - Core y Cardio
            asignarEjercicioARutina(cuentaDemo, "Viernes", buscarEjercicioPorNombre(ejercicios, "Plancha"));
            asignarEjercicioARutina(cuentaDemo, "Viernes", buscarEjercicioPorNombre(ejercicios, "Crunches"));
            asignarEjercicioARutina(cuentaDemo, "Viernes", buscarEjercicioPorNombre(ejercicios, "Mountain Climbers"));
            asignarEjercicioARutina(cuentaDemo, "Viernes", buscarEjercicioPorNombre(ejercicios, "Burpees"));

            // Sábado - Funcional
            asignarEjercicioARutina(cuentaDemo, "Sábado", buscarEjercicioPorNombre(ejercicios, "Saltos en Cajón"));
            asignarEjercicioARutina(cuentaDemo, "Sábado", buscarEjercicioPorNombre(ejercicios, "Burpees"));
            asignarEjercicioARutina(cuentaDemo, "Sábado", buscarEjercicioPorNombre(ejercicios, "Mountain Climbers"));

            System.out.println("Rutinas demo creadas: " + rutinaRepository.count() + " rutinas");
        }
    }

    private Ejercicio buscarEjercicioPorNombre(List<Ejercicio> ejercicios, String nombre) {
        return ejercicios.stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst()
                .orElse(ejercicios.get(0)); // Fallback al primer ejercicio
    }

    private void asignarEjercicioARutina(Cuenta cuenta, String dia, Ejercicio ejercicio) {
        if (ejercicio != null) {
            Rutina rutina = new Rutina();
            rutina.setCuenta(cuenta);
            rutina.setDia(dia);
            rutina.setEjercicio(ejercicio);
            rutinaRepository.save(rutina);
        }
    }
}
