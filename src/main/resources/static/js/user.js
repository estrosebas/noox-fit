const diasSemana = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"]

// Datos de ejercicios desde el backend
let ejercicios = [];

// Función para cargar los ejercicios desde el backend
async function cargarEjercicios() {
  try {
    const response = await fetch('/api/exercises');
    if (!response.ok) {
      throw new Error('Error en la respuesta del servidor');
    }
    ejercicios = await response.json();
    // Carga la vista inicial con los ejercicios
    loadRutinaDia("Lunes");
  } catch (error) {
    console.error('Error al cargar los ejercicios:', error);
  }
}

// Función para marcar un ejercicio como completado/incompleto
async function toggleEjercicioStatus(nombre) {
  try {
    const response = await fetch(`/api/exercises/${nombre}/toggle`, {
      method: 'POST',
    });
    if (!response.ok) {
      throw new Error('Error al actualizar el estado del ejercicio');
    }
    const ejercicioActualizado = await response.json();
    // Actualizar el ejercicio en el array local
    const index = ejercicios.findIndex(e => e.nombre === nombre);
    if (index !== -1) {
      ejercicios[index] = ejercicioActualizado;
    }
    return ejercicioActualizado;
  } catch (error) {
    console.error('Error:', error);
    return null;
  }
}

// Datos temporales en caso de error de conexión


// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  // Cargar la sección de rutinas por defecto y activar el link
  loadSection('rutinas');
  const rutinasLink = document.querySelector('a[onclick="loadSection(\'rutinas\')"]');
  if (rutinasLink) {
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
    rutinasLink.classList.add('active');
  }
  
  // Cargar ejercicios del backend
  cargarEjercicios();
  
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

  if (section === "rutinas") {
    sidebar.style.display = "block"
    if (window.innerWidth < 992) {
      sidebar.classList.add("show")
    }

    const titulo = document.createElement("h5")
    titulo.innerHTML = '<i class="bi bi-calendar-week"></i> <span th:text="#{user.week.title}">Días de la Semana</span>'
    sidebar.appendChild(titulo)

    diasSemana.forEach((dia) => {
      const btn = document.createElement("button")
      btn.className = "btn btn-outline-light btn-day"

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
      <h3 class="mb-0"><i class="bi bi-calendar-check"></i> <span th:text="#{user.nav.routines}">Rutinas</span> ${dia}</h3>
      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-light">
          <i class="bi bi-arrow-left-right"></i> <span th:text="#{user.exercise.sort}">Cambiar orden</span>
        </button>
        <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio">
          <i class="bi bi-plus-lg"></i> <span th:text="#{user.exercise.add}">Agregar</span>
        </button>
      </div>
    </div>
    
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="ejercicios-container">
      ${
        ejerciciosDia.length
          ? renderEjerciciosCards(ejerciciosDia)
          : `<div class="col-12">
          <div class="alert alert-info">
            <i class="bi bi-info-circle"></i> <span th:text="#{user.exercise.no.exercises}">No hay ejercicios para este día.</span>
            <button class="btn btn-sm btn-primary ms-3" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio">
              <span th:text="#{user.exercise.add}">Agregar ejercicio</span>
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
  checkboxes.forEach((checkbox) => {    checkbox.addEventListener("change", async function (e) {
      e.stopPropagation() // Evitar que se abra el modal al hacer clic en el checkbox
      const ejercicioId = this.dataset.id
      const ejercicioActualizado = await toggleEjercicioStatus(ejercicioId)
      if (ejercicioActualizado) {
        loadRutinaDia(ejercicioActualizado.dia) // Recargar la vista para mostrar los cambios
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
            <span class="badge bg-${e.hecho ? "success" : "primary"}">
              ${e.hecho ? '<span th:text="#{user.exercise.completed}">Completado</span>' : e.dificultad}
            </span>
          </div>
        </div>
        <div class="card-body">
          <h5 class="card-title">${e.nombre}</h5>
          <p class="card-text text-muted">${e.descripcion.substring(0, 60)}${e.descripcion.length > 60 ? "..." : ""}</p>
          
          <div class="form-check">
            <input class="form-check-input exercise-checkbox" type="checkbox" id="${e.dia}-ej${i}" 
              ${e.hecho ? "checked" : ""} data-id="${e.nombre}">
            <label class="form-check-label" for="${e.dia}-ej${i}">
              <span th:text="#{user.exercise.mark.complete}">Marcar como completado</span>
            </label>
          </div>
          
          <div class="card-actions">
            <button class="btn btn-sm btn-primary btn-view" 
              onclick="abrirModal('${e.nombre}', '${e.descripcion}', '${e.urlVideo}', '${e.dificultad}')">
              <i class="bi bi-play-circle"></i> <span th:text="#{user.exercise.view}">Ver ejercicio</span>
            </button>
            <button class="btn btn-sm btn-outline-light" data-bs-toggle="tooltip" th:title="#{user.exercise.edit}">
              <i class="bi bi-pencil"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  `
    )
    .join("")
}

function abrirModal(titulo, descripcion, urlVideo, dificultad) {
  // Obtener el ejercicio completo
  const ejercicio = ejercicios.find((e) => e.nombre === titulo);
  if (!ejercicio) return;

  // Actualizar el título y descripción
  document.getElementById("modalEjercicioLabel").innerHTML = `<span th:text="#{user.modal.exercise.title}">${titulo}</span>`;
  document.getElementById("modalDescripcion").innerHTML = `<span th:text="#{user.modal.exercise.description}">${descripcion}</span>`;
  
  // Actualizar el video
  const videoFrame = document.getElementById("modalVideo");
  videoFrame.src = urlVideo;
  
  // Actualizar el badge de dificultad
  const badgeDificultad = document.getElementById("modalDificultad");
  if (badgeDificultad) {
    badgeDificultad.innerHTML = `<span th:text="#{user.modal.exercise.difficulty}">Dificultad:</span> ${dificultad || "Intermedia"}`;
  }

  // Cargar instrucciones
  const instruccionesList = document.getElementById("modalInstrucciones");
  if (instruccionesList && ejercicio.instrucciones) {
    instruccionesList.innerHTML = "";
    ejercicio.instrucciones.forEach((instruccion) => {
      const li = document.createElement("li");
      li.textContent = instruccion;
      instruccionesList.appendChild(li);
    });
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
        : '<i class="bi bi-check-circle"></i> Marcar como completado';
    btnMarcarCompletado.onclick = async () => {
      const ejercicioActualizado = await toggleEjercicioStatus(titulo)
      if (ejercicioActualizado) {
        btnMarcarCompletado.innerHTML = ejercicioActualizado.hecho
          ? '<i class="bi bi-check-circle-fill"></i> Completado'
          : '<i class="bi bi-check-circle"></i> Marcar como completado'
        // Actualizar la UI
        loadRutinaDia(ejercicioActualizado.dia)
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
      <h3 class="mb-3"><i class="bi bi-graph-up"></i> <span th:text="#{user.progress.title}"></span></h3>
      <p class="lead"><span th:text="#{user.progress.subtitle}"></span></p>
    </div>
    
    <div class="progress-container mb-4">
      <h5 class="mb-3"><span th:text="#{user.report.week.summary}">Resumen General</span></h5>
      <div class="row row-cols-1 row-cols-md-3 g-4">
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-primary">
              <i class="bi bi-check-circle"></i>
            </div>
            <div class="stat-value">${completados}</div>
            <div class="stat-label"><span th:text="#{user.progress.completed}">Ejercicios Completados</span></div>
          </div>
        </div>
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-warning">
              <i class="bi bi-lightning"></i>
            </div>
            <div class="stat-value">${total - completados}</div>
            <div class="stat-label"><span th:text="#{user.progress.pending}">Ejercicios Pendientes</span></div>
          </div>
        </div>
        <div class="col">
          <div class="stat-card">
            <div class="stat-icon text-success">
              <i class="bi bi-percent"></i>
            </div>
            <div class="stat-value">${porcentaje}%</div>
            <div class="stat-label"><span th:text="#{user.progress.total}">Progreso Total</span></div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="progress-container">
      <h5 class="mb-3"><span th:text="#{user.progress.day}">Progreso por Día</span></h5>
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
      <h3 class="mb-3"><i class="bi bi-clipboard-data"></i> <span th:text="#{user.report.week.title}">Informe Semanal</span></h3>
      <p class="lead"><span th:text="#{user.report.week.subtitle}">Resumen de tu actividad y rendimiento durante la semana.</span></p>
    </div>
    
    <div class="report-card">
      <div class="report-header">
        <div class="report-title"><span th:text="#{user.report.week.summary}">Resumen de la Semana</span></div>
        <div class="report-date">14 - 20 Abril, 2024</div>
      </div>
      
      <div class="row mb-4">
        <div class="col-md-6">
          <p><span th:text="#{user.report.week.completed}">Esta semana has completado</span> <strong>${completados} de ${total}</strong> <span th:text="#{user.report.week.exercises}">ejercicios programados.</span></p>
          <p><span th:text="#{user.report.week.active.day}">Tu día más activo fue</span> <strong>Lunes</strong> <span th:text="#{user.report.week.active.exercises}">con 2 ejercicios completados.</span></p>
          <p><span th:text="#{user.report.week.pending}">Tienes</span> <strong>${total - completados}</strong> <span th:text="#{user.report.week.pending.exercises}">ejercicios pendientes para completar esta semana.</span></p>
        </div>
        <div class="col-md-6">
          <div class="progress mb-3" style="height: 25px;">
            <div class="progress-bar bg-success" role="progressbar" 
              style="width: ${Math.round((completados / total) * 100)}%" 
              aria-valuenow="${completados}" aria-valuemin="0" aria-valuemax="${total}">
              ${Math.round((completados / total) * 100)}%
            </div>
          </div>
          <p class="text-center"><span th:text="#{user.report.week.progress}">Progreso semanal</span></p>
        </div>
      </div>
      
      <div class="alert alert-info">
        <i class="bi bi-lightbulb"></i> <strong><span th:text="#{user.report.week.tip.title}">Consejo de la semana:</span></strong> 
        <span th:text="#{user.report.week.tip.content}">Recuerda mantener una buena hidratación durante tus entrenamientos para mejorar tu rendimiento.</span>
      </div>
    </div>
    
    <div class="row row-cols-1 row-cols-md-2 g-4 mt-3">
      <div class="col">
        <div class="report-card">
          <div class="report-header">
            <div class="report-title"><span th:text="#{user.report.week.featured.exercises}">Ejercicios Destacados</span></div>
          </div>
          <ul class="list-group list-group-flush bg-transparent">
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-star-fill text-warning me-2"></i> Sentadillas
              <span class="badge bg-success float-end"><span th:text="#{user.exercise.completed}">Completado</span></span>
            </li>
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-star-fill text-warning me-2"></i> Push Ups
              <span class="badge bg-primary float-end"><span th:text="#{user.exercise.pending}">Pendiente</span></span>
            </li>
          </ul>
        </div>
      </div>
      <div class="col">
        <div class="report-card">
          <div class="report-header">
            <div class="report-title"><span th:text="#{user.report.week.next.goals}">Próximos Objetivos</span></div>
          </div>
          <ul class="list-group list-group-flush bg-transparent">
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-trophy text-warning me-2"></i> <span th:text="#{user.report.week.goal.complete.week}">Completar todos los ejercicios de la semana</span>
            </li>
            <li class="list-group-item bg-transparent border-bottom border-light">
              <i class="bi bi-trophy text-warning me-2"></i> <span th:text="#{user.report.week.goal.increase.intensity}">Aumentar la intensidad de los ejercicios</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  `
}
