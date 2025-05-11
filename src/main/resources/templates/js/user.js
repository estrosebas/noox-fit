const diasSemana = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"]

// Datos de ejercicios ampliados
const ejercicios = [
  {
    nombre: "Push Ups",
    descripcion: "Flexiones clásicas para trabajar el pecho y tríceps.",
    hecho: false,
    imagen: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=2070&auto=format&fit=crop",
    dia: "Lunes",
    urlVideo: "https://www.youtube.com/embed/SCVCLChPQFY",
    dificultad: "Intermedia",
    instrucciones: [
      "Colócate en posición de plancha con las manos a la altura de los hombros",
      "Mantén el cuerpo recto y el core activado",
      "Baja el cuerpo doblando los codos hasta casi tocar el suelo",
      "Empuja hacia arriba hasta extender los brazos por completo",
    ],
  },
  {
    nombre: "Sentadillas",
    descripcion: "Fortalece tus piernas y glúteos con este básico.",
    hecho: true,
    imagen: "https://images.unsplash.com/photo-1574680178050-55c6a6a96e0a?q=80&w=2069&auto=format&fit=crop",
    dia: "Lunes",
    urlVideo: "https://www.youtube.com/embed/YaXPRqUwItQ",
    dificultad: "Básica",
    instrucciones: [
      "Colócate de pie con los pies separados al ancho de los hombros",
      "Mantén la espalda recta y el pecho hacia adelante",
      "Baja flexionando las rodillas como si fueras a sentarte",
      "Regresa a la posición inicial empujando con los talones",
    ],
  },
  {
    nombre: "Plancha Abdominal",
    descripcion: "Ejercicio isométrico para fortalecer el core y mejorar la estabilidad.",
    hecho: false,
    imagen: "https://images.unsplash.com/photo-1566241142559-40e1dab266c6?q=80&w=2070&auto=format&fit=crop",
    dia: "Martes",
    urlVideo: "https://www.youtube.com/embed/ASdvN_XEl_c",
    dificultad: "Básica",
    instrucciones: [
      "Apóyate sobre los antebrazos y las puntas de los pies",
      "Mantén el cuerpo recto formando una línea desde la cabeza hasta los talones",
      "Contrae el abdomen y mantén la posición",
      "Respira de manera constante durante todo el ejercicio",
    ],
  },
  {
    nombre: "Burpees",
    descripcion: "Ejercicio de cuerpo completo de alta intensidad para mejorar la resistencia.",
    hecho: false,
    imagen: "https://images.unsplash.com/photo-1599058917765-a780eda07a3e?q=80&w=2069&auto=format&fit=crop",
    dia: "Miércoles",
    urlVideo: "https://www.youtube.com/embed/TU8QYVW0gDU",
    dificultad: "Avanzada",
    instrucciones: [
      "Comienza de pie con los pies separados al ancho de los hombros",
      "Baja a posición de cuclillas y coloca las manos en el suelo",
      "Salta llevando los pies hacia atrás quedando en posición de plancha",
      "Haz una flexión, salta llevando los pies hacia las manos y salta hacia arriba",
    ],
  },
]

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  // Llenar el select de días de la semana en el modal
  const selectDiaSemana = document.getElementById("selectDiaSemana")
  if (selectDiaSemana) {
    diasSemana.forEach((dia) => {
      const option = document.createElement("option")
      option.value = dia
      option.textContent = dia
      selectDiaSemana.appendChild(option)
    })
  }

  // Inicializar tooltips de Bootstrap
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))

  // Manejar el toggle del sidebar en móvil
  const navbarToggler = document.querySelector(".navbar-toggler")
  if (navbarToggler) {
    navbarToggler.addEventListener("click", () => {
      const sidebar = document.getElementById("sidebar")
      if (window.innerWidth < 992 && sidebar.classList.contains("show")) {
        sidebar.classList.remove("show")
      }
    })
  }
})

function loadSection(section) {
  const sidebar = document.getElementById("sidebar")
  const main = document.getElementById("main-content")

  sidebar.innerHTML = ""
  main.innerHTML = ""

  // Mostrar/ocultar sidebar según la sección
  if (section === "rutinas") {
    sidebar.style.display = "block"
    if (window.innerWidth < 992) {
      sidebar.classList.add("show")
    }

    const titulo = document.createElement("h5")
    titulo.innerHTML = '<i class="bi bi-calendar-week"></i> Días de la Semana'
    sidebar.appendChild(titulo)

    diasSemana.forEach((dia) => {
      const btn = document.createElement("button")
      btn.className = "btn btn-outline-light btn-day"

      // Verificar si hay ejercicios para este día
      const tieneEjercicios = ejercicios.some((e) => e.dia === dia)

      btn.innerHTML = `
        ${dia} 
        ${
          tieneEjercicios
            ? '<span class="badge bg-primary float-end">' + ejercicios.filter((e) => e.dia === dia).length + "</span>"
            : ""
        }
      `

      btn.onclick = () => loadRutinaDia(dia)
      sidebar.appendChild(btn)
    })

    // Cargar el primer día por defecto
    loadRutinaDia("Lunes")
  } else {
    sidebar.style.display = "none"

    if (section === "progresos") {
      renderProgresos(main)
    }

    if (section === "informe") {
      renderInformeSemanal(main)
    }
  }
}

function loadRutinaDia(dia) {
  const main = document.getElementById("main-content")
  const ejerciciosDia = ejercicios.filter((e) => e.dia.toLowerCase() === dia.toLowerCase())

  // Actualizar botón activo en el sidebar
  const botones = document.querySelectorAll(".btn-day")
  botones.forEach((btn) => {
    if (btn.textContent.trim().startsWith(dia)) {
      btn.classList.add("active")
    } else {
      btn.classList.remove("active")
    }
  })

  main.innerHTML = `
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3 class="mb-0"><i class="bi bi-calendar-check"></i> Rutina de ${dia}</h3>
      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-light">
          <i class="bi bi-arrow-left-right"></i> Cambiar orden
        </button>
        <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio">
          <i class="bi bi-plus-lg"></i> Agregar
        </button>
      </div>
    </div>
    
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="ejercicios-container">
      ${
        ejerciciosDia.length
          ? renderEjerciciosCards(ejerciciosDia)
          : `<div class="col-12">
          <div class="alert alert-info">
            <i class="bi bi-info-circle"></i> No hay ejercicios para este día.
            <button class="btn btn-sm btn-primary ms-3" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio">
              Agregar ejercicio
            </button>
          </div>
        </div>`
      }
    </div>
    
    <button class="btn btn-primary floating-btn" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio">
      <i class="bi bi-plus-lg"></i>
    </button>
  `

  // Agregar event listeners para los checkboxes
  const checkboxes = document.querySelectorAll(".exercise-checkbox")
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", function (e) {
      e.stopPropagation() // Evitar que se abra el modal al hacer clic en el checkbox
      const ejercicioId = this.dataset.id
      const ejercicio = ejercicios.find((e) => e.nombre === ejercicioId)
      if (ejercicio) {
        ejercicio.hecho = this.checked
      }
    })
  })
}

function renderEjerciciosCards(ejerciciosList) {
  return ejerciciosList
    .map(
      (e, i) => `
    <div class="col">
      <div class="exercise-card">
        <div class="card-img-container">
          <img src="${e.imagen}" class="card-img-top" alt="${e.nombre}">
          <div class="card-img-overlay">
            <span class="badge bg-${e.hecho ? "success" : "primary"}">${e.hecho ? "Completado" : e.dificultad}</span>
          </div>
        </div>
        <div class="card-body">
          <h5 class="card-title">${e.nombre}</h5>
          <p class="card-text text-muted">${e.descripcion.substring(0, 60)}${e.descripcion.length > 60 ? "..." : ""}</p>
          
          <div class="form-check">
            <input class="form-check-input exercise-checkbox" type="checkbox" id="${e.dia}-ej${i}" 
              ${e.hecho ? "checked" : ""} data-id="${e.nombre}">
            <label class="form-check-label" for="${e.dia}-ej${i}">
              Marcar como completado
            </label>
          </div>
          
          <div class="card-actions">
            <button class="btn btn-sm btn-primary btn-view" 
              onclick="abrirModal('${e.nombre}', '${e.descripcion}', '${e.urlVideo}', '${e.dificultad}')">
              <i class="bi bi-play-circle"></i> Ver ejercicio
            </button>
            <button class="btn btn-sm btn-outline-light" data-bs-toggle="tooltip" title="Editar">
              <i class="bi bi-pencil"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
    )
    .join("")
}

function abrirModal(titulo, descripcion, urlVideo, dificultad) {
  document.getElementById("modalEjercicioLabel").innerText = titulo
  document.getElementById("modalDescripcion").innerText = descripcion
  document.getElementById("modalVideo").src = urlVideo

  // Actualizar la dificultad en el badge
  const badgeDificultad = document.querySelector(".exercise-details .badge")
  if (badgeDificultad) {
    badgeDificultad.innerText = `Dificultad: ${dificultad || "Intermedia"}`
  }

  // Cargar instrucciones
  const instruccionesList = document.getElementById("modalInstrucciones")
  if (instruccionesList) {
    instruccionesList.innerHTML = ""
    const ejercicio = ejercicios.find((e) => e.nombre === titulo)
    if (ejercicio && ejercicio.instrucciones) {
      ejercicio.instrucciones.forEach((instruccion) => {
        const li = document.createElement("li")
        li.textContent = instruccion
        instruccionesList.appendChild(li)
      })
    }
  }

  const modalElement = document.getElementById("modalEjercicio")
  const modal = new bootstrap.Modal(modalElement)
  modal.show()

  // Configurar el botón de marcar como completado
  const btnMarcarCompletado = document.getElementById("btnMarcarCompletado")
  if (btnMarcarCompletado) {
    const ejercicio = ejercicios.find((e) => e.nombre === titulo)
    btnMarcarCompletado.innerHTML =
      ejercicio && ejercicio.hecho
        ? '<i class="bi bi-check-circle-fill"></i> Completado'
        : '<i class="bi bi-check-circle"></i> Marcar como completado'

    btnMarcarCompletado.onclick = () => {
      if (ejercicio) {
        ejercicio.hecho = !ejercicio.hecho
        btnMarcarCompletado.innerHTML = ejercicio.hecho
          ? '<i class="bi bi-check-circle-fill"></i> Completado'
          : '<i class="bi bi-check-circle"></i> Marcar como completado'

        // Actualizar la UI
        loadRutinaDia(ejercicio.dia)
      }
    }
  }
}

function renderProgresos(container) {
  const completados = ejercicios.filter((e) => e.hecho).length
  const total = ejercicios.length
  const porcentaje = total > 0 ? Math.round((completados / total) * 100) : 0

  container.innerHTML = `
    <div class="mb-4">
      <h3 class="mb-3"><i class="bi bi-graph-up"></i> Tus Progresos</h3>
      <p class="lead">Visualiza tu rendimiento y progreso en el entrenamiento.</p>
    </div>
    
    <div class="progress-container mb-4">
      <h5 class="mb-3">Resumen General</h5>
      <div class="row row-cols-1 row-cols-md-3 g-4">
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-primary">
              <i class="bi bi-check-circle"></i>
            </div>
            <div class="stat-value">${completados}</div>
            <div class="stat-label">Ejercicios Completados</div>
          </div>
        </div>
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-warning">
              <i class="bi bi-lightning"></i>
            </div>
            <div class="stat-value">${total - completados}</div>
            <div class="stat-label">Ejercicios Pendientes</div>
          </div>
        </div>
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-success">
              <i class="bi bi-percent"></i>
            </div>
            <div class="stat-value">${porcentaje}%</div>
            <div class="stat-label">Progreso Total</div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="progress-container">
      <h5 class="mb-3">Progreso por Día</h5>
      <div class="row">
        <div class="col-12">
          ${renderProgresoPorDia()}
        </div>
      </div>
    </div>
  `
}

function renderProgresoPorDia() {
  return diasSemana
    .map((dia) => {
      const ejerciciosDia = ejercicios.filter((e) => e.dia === dia)
      const completados = ejerciciosDia.filter((e) => e.hecho).length
      const total = ejerciciosDia.length
      const porcentaje = total > 0 ? Math.round((completados / total) * 100) : 0

      return `
      <div class="day-progress">
        <div class="day-label">${dia}</div>
        <div class="progress">
          <div class="progress-bar" role="progressbar" style="width: ${porcentaje}%" 
            aria-valuenow="${porcentaje}" aria-valuemin="0" aria-valuemax="100"></div>
        </div>
        <div class="ms-3">${completados}/${total}</div>
      </div>
    `
    })
    .join("")
}

function renderInformeSemanal(container) {
  const completados = ejercicios.filter((e) => e.hecho).length
  const total = ejercicios.length

  container.innerHTML = `
    <div class="mb-4">
      <h3 class="mb-3"><i class="bi bi-clipboard-data"></i> Informe Semanal</h3>
      <p class="lead">Resumen de tu actividad y rendimiento durante la semana.</p>
    </div>
    
    <div class="report-card">
      <div class="report-header">
        <div class="report-title">Resumen de la Semana</div>
        <div class="report-date">14 - 20 Abril, 2024</div>
      </div>
      
      <div class="row mb-4">
        <div class="col-md-6">
          <p>Esta semana has completado <strong>${completados} de ${total}</strong> ejercicios programados.</p>
          <p>Tu día más activo fue <strong>Lunes</strong> con 2 ejercicios completados.</p>
          <p>Tienes <strong>${total - completados}</strong> ejercicios pendientes para completar esta semana.</p>
        </div>
        <div class="col-md-6">
          <div class="progress mb-3" style="height: 25px;">
            <div class="progress-bar bg-success" role="progressbar" 
              style="width: ${Math.round((completados / total) * 100)}%" 
              aria-valuenow="${completados}" aria-valuemin="0" aria-valuemax="${total}">
              ${Math.round((completados / total) * 100)}%
            </div>
          </div>
          <p class="text-center">Progreso semanal</p>
        </div>
      </div>
      
      <div class="alert alert-info">
        <i class="bi bi-lightbulb"></i> <strong>Consejo de la semana:</strong> 
        Recuerda mantener una buena hidratación durante tus entrenamientos para mejorar tu rendimiento.
      </div>
    </div>
    
    <div class="row row-cols-1 row-cols-md-2 g-4 mt-3">
      <div class="col">
        <div class="report-card">
          <div class="report-header">
            <div class="report-title">Ejercicios Destacados</div>
          </div>
          <ul class="list-group list-group-flush bg-transparent">
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-star-fill text-warning me-2"></i> Sentadillas
              <span class="badge bg-success float-end">Completado</span>
            </li>
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-star-fill text-warning me-2"></i> Push Ups
              <span class="badge bg-primary float-end">Pendiente</span>
            </li>
          </ul>
        </div>
      </div>
      <div class="col">
        <div class="report-card">
          <div class="report-header">
            <div class="report-title">Próximos Objetivos</div>
          </div>
          <ul class="list-group list-group-flush bg-transparent">
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-trophy text-warning me-2"></i> Completar todos los ejercicios de la semana
            </li>
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-trophy text-warning me-2"></i> Aumentar la intensidad de los ejercicios
            </li>
          </ul>
        </div>
      </div>
    </div>
  `
}
