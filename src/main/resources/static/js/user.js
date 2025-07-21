const diasSemana = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"]

// Datos de ejercicios desde el backend (organizados por día)
let ejerciciosPorDia = {};
let todosLosEjercicios = [];

// Función para obtener el token JWT del localStorage
function getJwtToken() {
    return localStorage.getItem('jwt_token');
}

// Función para crear headers con autorización
function getAuthHeaders() {
    const token = getJwtToken();
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

// Función para verificar si el usuario está autenticado
function checkAuthentication() {
    const token = getJwtToken();
    if (!token) {
        console.log('No hay token JWT, pero permitiendo acceso para desarrollo');
        // Para desarrollo, permitir acceso sin token
        // En producción, descomentar la siguiente línea:
        // window.location.href = '/login';
        return true; // Cambiar a false en producción
    }
    return true;
}

// Función para mostrar información del usuario
function displayUserInfo() {
    const userEmail = localStorage.getItem('user_email');
    const username = localStorage.getItem('user_username');
    
    if (userEmail || username) {
        const displayName = username || userEmail.split('@')[0];
        
        // Actualizar elementos que muestren el usuario
        const userProfileElement = document.getElementById('user-profile-name');
        const userProfileElementMobile = document.getElementById('user-profile-name-mobile');
        
        if (userProfileElement) {
            userProfileElement.textContent = displayName;
        }
        if (userProfileElementMobile) {
            userProfileElementMobile.textContent = displayName;
        }
    }
}

// Función para logout
function logout() {
    // Limpiar localStorage
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_email');
    localStorage.removeItem('user_username');
    
    // Mostrar mensaje de despedida
    showToast('Sesión cerrada exitosamente', 'success');
    
    // Redirigir al login después de un breve delay
    setTimeout(() => {
        window.location.href = '/login';
    }, 1000);
}

// Función para cargar los ejercicios desde el backend
async function cargarEjercicios() {
    if (!checkAuthentication()) return;
    
    try {
        const response = await fetch('/api/ejercicios/rutinas/todos-los-dias', {
            headers: getAuthHeaders()
        });
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        ejerciciosPorDia = await response.json();
        
        // Crear array plano de todos los ejercicios para compatibilidad
        todosLosEjercicios = [];
        Object.values(ejerciciosPorDia).forEach(ejerciciosDia => {
            todosLosEjercicios.push(...ejerciciosDia);
        });
        
        console.log('Ejercicios cargados por día:', ejerciciosPorDia);
        console.log('Total ejercicios:', todosLosEjercicios.length);
        
        // Actualizar la UI después de cargar los datos
        refreshRutinasUI();
        
        // Cargar la vista inicial
        if (todosLosEjercicios.length > 0) {
            loadRutinaDia("Lunes");
        }
    } catch (error) {
        console.error('Error al cargar los ejercicios:', error);
        // Usar datos de fallback si hay error
        loadFallbackExercises();
    }
}

// Función para actualizar la UI de rutinas después de cargar los datos
function refreshRutinasUI() {
    const sidebar = document.getElementById("sidebar");
    const main = document.getElementById("main");
    const sidebarContainer = document.getElementById("sidebar-container");
    
    // Solo actualizar si estamos en la sección de rutinas y el sidebar está visible
    if (sidebar && main && !sidebarContainer.classList.contains('d-none')) {
        // Actualizar solo los badges de conteo en el sidebar
        const dayButtons = sidebar.querySelectorAll('.btn-day');
        dayButtons.forEach((btn, index) => {
            const dia = diasSemana[index];
            if (dia) {
                const ejerciciosDia = ejerciciosPorDia[dia] || [];
                const cantidadEjercicios = ejerciciosDia.length;
                const badge = btn.querySelector('.badge');
                if (badge) {
                    badge.textContent = cantidadEjercicios;
                    badge.className = cantidadEjercicios > 0 ? 'badge bg-primary' : 'badge bg-secondary';
                }
            }
        });
    }
}

// Función para marcar un ejercicio como completado/incompleto
async function toggleEjercicioStatus(idEjercicio) {
    if (!checkAuthentication()) return null;
    
    try {
        const response = await fetch(`/api/ejercicios/${idEjercicio}/toggle`, {
            method: 'POST',
            headers: getAuthHeaders()
        });
        if (!response.ok) {
            throw new Error('Error al actualizar el estado del ejercicio');
        }
        const ejercicioActualizado = await response.json();
        // Actualizar el ejercicio en el array local
        const index = todosLosEjercicios.findIndex(e => e.idejercicio === idEjercicio || e.nombre === idEjercicio);
        if (index !== -1) {
            todosLosEjercicios[index] = ejercicioActualizado;
        }
        return ejercicioActualizado;
    } catch (error) {
        console.error('Error:', error);
        return null;
    }
}

// Función para cargar datos de fallback
function loadFallbackExercises() {
    const fallbackEjercicios = [
        {
            idejercicio: 1,
            nombre: "Flexiones de Pecho",
            descripcion: "Ejercicio básico de pecho que se puede hacer sin equipo",
            imagenurl: "/img/flexiones.svg",
            urlvideo: "https://www.youtube.com/embed/IODxDxX7oi4",
            dificultad: "Principiante",
            dia: "Lunes",
            hecho: false
        },
        {
            idejercicio: 2,
            nombre: "Sentadillas",
            descripcion: "Ejercicio fundamental para piernas",
            imagenurl: "/img/sentadillas.svg",
            urlvideo: "https://www.youtube.com/embed/ultWZbUMPL8",
            dificultad: "Principiante",
            dia: "Miércoles",
            hecho: false
        },
        {
            idejercicio: 3,
            nombre: "Plancha",
            descripcion: "Ejercicio isométrico para fortalecer el core",
            imagenurl: "/img/plancha.svg",
            urlvideo: "https://www.youtube.com/embed/ASdvN_XEl_c",
            dificultad: "Principiante",
            dia: "Viernes",
            hecho: false
        }
    ];

    // Organizar por días
    ejerciciosPorDia = {};
    diasSemana.forEach(dia => {
        ejerciciosPorDia[dia] = fallbackEjercicios.filter(e => e.dia === dia);
    });
    
    todosLosEjercicios = fallbackEjercicios;
    console.log('Datos de fallback cargados:', todosLosEjercicios.length);
    loadRutinaDia("Lunes");
}

// Datos temporales en caso de error de conexión


// Variables globales para evitar duplicación
let navigationInitialized = false;
let currentSection = '';
let currentDay = '';

// Función para limpiar event listeners anteriores
function cleanupEventListeners() {
  // Limpiar listeners de navegación
  const navLinks = document.querySelectorAll('.nav-link[data-section]');
  navLinks.forEach(link => {
    const newLink = link.cloneNode(true);
    link.parentNode.replaceChild(newLink, link);
  });
  
  // Limpiar listeners de botones de día
  const dayButtons = document.querySelectorAll('.btn-day');
  dayButtons.forEach(btn => {
    const newBtn = btn.cloneNode(true);
    btn.parentNode.replaceChild(newBtn, btn);
  });
}

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  console.log('DOM Content Loaded - Iniciando aplicación');
  
  // Verificar autenticación al cargar la página
  if (!checkAuthentication()) {
    console.log('No autenticado, redirigiendo...');
    return;
  }
  
  console.log('Usuario autenticado, continuando...');
  
  // Mostrar información del usuario
  displayUserInfo();
  
  // Inicializar navegación solo una vez
  if (!navigationInitialized) {
    console.log('Inicializando navegación...');
    initializeNavigation();
    navigationInitialized = true;
  }
  
  // Cargar la sección de rutinas por defecto
  console.log('Cargando sección de rutinas...');
  loadSection('rutinas');
  updateActiveNav('rutinas');
  
  // Cargar ejercicios del backend
  console.log('Cargando ejercicios...');
  cargarEjercicios();
  
  // Llenar el select de días de la semana en el modal
  const selectDiaSemana = document.getElementById("selectDiaSemana");
  if (selectDiaSemana) {
    diasSemana.forEach((dia) => {
      const option = document.createElement("option");
      option.value = dia;
      option.textContent = dia;
      selectDiaSemana.appendChild(option);
    });
  }

  // Inicializar tooltips de Bootstrap
  initializeTooltips();
  
  // Inicializar otros componentes
  initializeComponents();
  
  // Agregar listener para cambios de tamaño de ventana
  window.addEventListener('resize', handleWindowResize);
  
  console.log('Aplicación inicializada correctamente');
});

// Función para manejar cambios de tamaño de ventana
function handleWindowResize() {
  // Recargar la sección actual para ajustar el layout
  if (currentSection) {
    const tempSection = currentSection;
    currentSection = ''; // Reset para forzar recarga
    loadSection(tempSection);
  }
}

// Función para inicializar la navegación una sola vez
function initializeNavigation() {
  const navLinks = document.querySelectorAll('.nav-link[data-section]');
  navLinks.forEach(link => {
    // Remover listeners anteriores si existen
    link.removeEventListener('click', handleNavClick);
    // Agregar nuevo listener
    link.addEventListener('click', handleNavClick);
  });
}

// Función para manejar clicks de navegación
function handleNavClick(e) {
  e.preventDefault();
  const section = this.getAttribute('data-section');
  
  // Cerrar offcanvas en móvil
  const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('offcanvasNavbar'));
  if (offcanvas) {
    offcanvas.hide();
  }
  
  // Actualizar navegación activa
  updateActiveNav(section);
  
  // Cargar sección
  loadSection(section);
}

// Función para inicializar tooltips
function initializeTooltips() {
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
  tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl));
}

// Función para inicializar componentes
function initializeComponents() {
  // Configurar eventos de teclado
  setupKeyboardNavigation();
  
  // Configurar animaciones
  setupAnimations();
}

// Función para actualizar navegación activa
function updateActiveNav(activeSection) {
  const navLinks = document.querySelectorAll('.nav-link[data-section]');
  navLinks.forEach(link => {
    const section = link.getAttribute('data-section');
    if (section === activeSection) {
      link.classList.add('active');
    } else {
      link.classList.remove('active');
    }
  });
  
  // Actualizar variable global
  currentSection = activeSection;
}

// Función para configurar navegación por teclado
function setupKeyboardNavigation() {
  document.addEventListener('keydown', function(e) {
    // Escape para cerrar modales
    if (e.key === 'Escape') {
      const modals = document.querySelectorAll('.modal.show');
      modals.forEach(modal => {
        const bsModal = bootstrap.Modal.getInstance(modal);
        if (bsModal) bsModal.hide();
      });
    }
    
    // Atajos de teclado para navegación
    if (e.ctrlKey || e.metaKey) {
      switch(e.key) {
        case '1':
          e.preventDefault();
          loadSection('rutinas');
          updateActiveNav('rutinas');
          break;
        case '2':
          e.preventDefault();
          loadSection('progresos');
          updateActiveNav('progresos');
          break;
        case '3':
          e.preventDefault();
          loadSection('informe');
          updateActiveNav('informe');
          break;
      }
    }
  });
}

// Función para configurar animaciones
function setupAnimations() {
  // Observador para animaciones al scroll
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('fade-in');
      }
    });
  }, { threshold: 0.1 });

  // Observar elementos que deben animarse
  const elementsToAnimate = document.querySelectorAll('.exercise-card, .stat-card, .report-card');
  elementsToAnimate.forEach(el => observer.observe(el));
}

function loadSection(section) {
  const sidebar = document.getElementById("sidebar");
  const sidebarContainer = document.getElementById("sidebar-container");
  const mainContainer = document.getElementById("main-content-container");
  const main = document.getElementById("main-content");

  // Evitar cargar la misma sección múltiples veces
  if (currentSection === section) return;

  // Remover animación anterior
  main.classList.remove('fade-in');

  // Limpiar contenido anterior
  sidebar.innerHTML = "";
  main.innerHTML = "";

  // Mostrar loading
  showLoading(main);

  // Simular pequeño delay para mostrar loading
  setTimeout(() => {
    // Verificar si estamos en móvil
    const isMobile = window.innerWidth < 992;
    
    if (section === "rutinas") {
      if (isMobile) {
        // En móvil, siempre usar layout completo
        sidebarContainer.classList.add('d-none');
        mainContainer.classList.remove('col-lg-9', 'col-xl-10');
        mainContainer.classList.add('col-12', 'full-width');
      } else {
        // En desktop, mostrar sidebar
        sidebarContainer.classList.remove('d-none');
        mainContainer.classList.remove('col-12', 'full-width');
        mainContainer.classList.add('col-lg-9', 'col-xl-10');
        renderRutinasSection(sidebar, main);
      }
      
      // Renderizar contenido principal
      if (isMobile) {
        renderRutinasMainContent(main);
      } else {
        renderRutinasSection(sidebar, main);
      }
      
      updateMobileNavigation(section);
    } else {
      // Ocultar sidebar y expandir contenido principal
      sidebarContainer.classList.add('d-none');
      mainContainer.classList.remove('col-lg-9', 'col-xl-10');
      mainContainer.classList.add('col-12', 'full-width');
      
      if (section === "progresos") {
        renderProgresos(main);
      } else if (section === "informe") {
        renderInformeSemanal(main);
      }
      
      updateMobileNavigation(section);
    }
    
    // Forzar reflow para asegurar que los cambios se apliquen
    mainContainer.offsetHeight;
    
    // Aplicar animaciones después de un pequeño delay
    setTimeout(() => {
      main.classList.add('fade-in');
    }, 50);
    
    // Actualizar sección actual
    currentSection = section;
  }, 200);
}

// Función para actualizar la navegación móvil con días de la semana
function updateMobileNavigation(section) {
  const offcanvasBody = document.querySelector('#offcanvasNavbar .offcanvas-body');
  const existingWeekDays = offcanvasBody.querySelector('.week-days-mobile');
  
  // Remover días existentes si los hay
  if (existingWeekDays) {
    existingWeekDays.remove();
  }
  
  // Solo agregar días de la semana si estamos en la sección de rutinas
  if (section === 'rutinas') {
    const weekDaysContainer = document.createElement('div');
    weekDaysContainer.className = 'week-days-mobile mt-3 pt-3 border-top border-secondary';
    
    const weekTitle = document.createElement('h6');
    weekTitle.className = 'text-muted mb-3';
    weekTitle.innerHTML = '<i class="bi bi-calendar-week me-2"></i>Días de la Semana';
    weekDaysContainer.appendChild(weekTitle);
    
    const weekDaysList = document.createElement('ul');
    weekDaysList.className = 'list-unstyled';
    
    diasSemana.forEach(dia => {
      const ejerciciosDia = ejerciciosPorDia[dia] || [];
      const tieneEjercicios = ejerciciosDia.length > 0;
      const cantidadEjercicios = ejerciciosDia.length;
      
      const listItem = document.createElement('li');
      listItem.className = 'mb-2';
      
      const dayButton = document.createElement('button');
      dayButton.className = 'btn btn-outline-light w-100 btn-day-mobile';
      dayButton.setAttribute('data-day', dia);
      
      dayButton.innerHTML = `
        <div class="d-flex justify-content-between align-items-center">
          <span>${dia}</span>
          ${tieneEjercicios ? 
            `<span class="badge bg-primary">${cantidadEjercicios}</span>` : 
            '<span class="badge bg-secondary">0</span>'
          }
        </div>
      `;
      
      dayButton.addEventListener('click', () => {
        loadRutinaDia(dia);
        // Cerrar offcanvas después de seleccionar día
        const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('offcanvasNavbar'));
        if (offcanvas) {
          offcanvas.hide();
        }
      });
      
      listItem.appendChild(dayButton);
      weekDaysList.appendChild(listItem);
    });
    
    weekDaysContainer.appendChild(weekDaysList);
    offcanvasBody.appendChild(weekDaysContainer);
  }
}

function renderRutinasSection(sidebar, main) {
  // Limpiar sidebar completamente antes de renderizar
  sidebar.innerHTML = "";
  
  // Resetear día actual
  currentDay = '';
  
  // Renderizar sidebar
  const titulo = document.createElement("h5");
  titulo.innerHTML = '<i class="bi bi-calendar-week"></i> <span>Días de la Semana</span>';
  sidebar.appendChild(titulo);

  diasSemana.forEach((dia) => {
    const btn = document.createElement("button");
    btn.className = "btn btn-outline-light btn-day";

    const ejerciciosDia = ejerciciosPorDia[dia] || [];
    const tieneEjercicios = ejerciciosDia.length > 0;
    const cantidadEjercicios = ejerciciosDia.length;

    btn.innerHTML = `
      <div class="d-flex justify-content-between align-items-center">
        <span>${dia}</span>
        ${tieneEjercicios ? 
          `<span class="badge bg-primary">${cantidadEjercicios}</span>` : 
          '<span class="badge bg-secondary">0</span>'
        }
      </div>
    `;

    // Usar addEventListener en lugar de onclick para evitar duplicados
    btn.addEventListener('click', () => loadRutinaDia(dia));
    sidebar.appendChild(btn);
  });

  // Cargar primer día
  loadRutinaDia("Lunes");
}

// Función para renderizar rutinas en móvil (sin sidebar)
function renderRutinasMainContent(main) {
  // Resetear día actual
  currentDay = '';
  
  // Cargar primer día directamente
  loadRutinaDia("Lunes");
}

function showLoading(container) {
  container.innerHTML = `
    <div class="d-flex justify-content-center align-items-center" style="min-height: 200px;">
      <div class="text-center">
        <div class="loading mb-3"></div>
        <p class="text-muted">Cargando contenido...</p>
      </div>
    </div>
  `;
}

function showToast(message, type = 'success') {
  const toastContainer = document.querySelector('.toast-container');
  const toast = document.getElementById('exerciseToast');
  const toastBody = document.getElementById('toastBody');
  
  if (toast && toastBody) {
    const icon = type === 'success' ? 'bi-check-circle-fill text-success' : 'bi-exclamation-triangle-fill text-warning';
    
    toast.querySelector('.toast-header i').className = `bi ${icon} me-2`;
    toastBody.textContent = message;
    
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
  }
}

function nextExercise() {
  // Función para ir al siguiente ejercicio (implementar lógica)
  console.log('Ir al siguiente ejercicio');
  showToast('Función próximamente disponible', 'info');
}

function loadRutinaDia(dia) {
  const main = document.getElementById("main-content");
  const ejerciciosDia = ejerciciosPorDia[dia] || [];

  // Evitar cargar el mismo día múltiples veces
  if (currentDay === dia) return;
  
  // Actualizar botón activo en el sidebar (desktop)
  const botones = document.querySelectorAll(".btn-day");
  botones.forEach((btn) => {
    btn.classList.remove("active");
    if (btn.textContent.trim().startsWith(dia)) {
      btn.classList.add("active");
    }
  });

  // Actualizar botón activo en navegación móvil
  const botonesMobile = document.querySelectorAll(".btn-day-mobile");
  botonesMobile.forEach((btn) => {
    btn.classList.remove("active");
    if (btn.getAttribute('data-day') === dia) {
      btn.classList.add("active");
    }
  });

  main.innerHTML = `
    <div class="container-fluid">
      <div class="row mb-4">
        <div class="col-12">
          <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4">
            <div class="mb-3 mb-md-0">
              <h3 class="mb-1">
                <i class="bi bi-calendar-check me-2"></i>
                <span class="text-gradient">Rutinas ${dia}</span>
              </h3>
              <p class="text-muted mb-0">
                ${ejerciciosDia.length > 0 ? 
                  `${ejerciciosDia.length} ejercicio${ejerciciosDia.length > 1 ? 's' : ''} programado${ejerciciosDia.length > 1 ? 's' : ''}` : 
                  'No hay ejercicios programados'
                }
              </p>
            </div>
            <div class="d-flex flex-wrap gap-2">
              <button class="btn btn-sm btn-outline-light glass-effect d-lg-none" onclick="toggleMobileWeekDays()">
                <i class="bi bi-calendar-week"></i>
                <span class="d-none d-sm-inline ms-1">Días</span>
              </button>
              <button class="btn btn-sm btn-outline-light glass-effect" onclick="sortExercises()">
                <i class="bi bi-arrow-up-down"></i>
                <span class="d-none d-sm-inline ms-1">Ordenar</span>
              </button>
              <button class="btn btn-sm btn-primary shadow-glow" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio" onclick="preseleccionarDia('${dia}')">
                <i class="bi bi-plus-lg"></i>
                <span class="d-none d-sm-inline ms-1">Agregar</span>
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="ejercicios-container">
            ${ejerciciosDia.length ? 
              renderEjerciciosCards(ejerciciosDia) : 
              renderEmptyState(dia)
            }
          </div>
        </div>
      </div>
    </div>
  `;

  // Actualizar día actual
  currentDay = dia;

  // Agregar event listeners para los checkboxes
  setupExerciseCheckboxes();
  
  // Aplicar animaciones
  setTimeout(() => {
    const cards = document.querySelectorAll('.exercise-card');
    cards.forEach((card, index) => {
      setTimeout(() => {
        card.classList.add('fade-in');
      }, index * 100);
    });
  }, 50);
}

function renderEmptyState(dia) {
  return `
    <div class="col-12">
      <div class="text-center py-5 empty-state">
        <div class="mb-4">
          <i class="bi bi-calendar-plus display-1 text-primary"></i>
        </div>
        <h4 class="text-light mb-3">No hay ejercicios para ${dia}</h4>
        <p class="text-light-dark mb-4">Comienza agregando algunos ejercicios para este día</p>
        <button class="btn btn-primary px-4 py-2 shadow-glow" data-bs-toggle="modal" data-bs-target="#modalAgregarEjercicio" onclick="preseleccionarDia('${dia}')">
          <i class="bi bi-plus-lg me-2"></i>
          Agregar primer ejercicio
        </button>
      </div>
    </div>
  `;
}

function setupExerciseCheckboxes() {
  const checkboxes = document.querySelectorAll(".exercise-checkbox");
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", async function (e) {
      e.stopPropagation();
      const ejercicioId = this.dataset.id;
      const card = this.closest('.exercise-card');
      
      // Mostrar loading en el card
      card.style.opacity = '0.6';
      card.style.pointerEvents = 'none';
      
      const ejercicioActualizado = await toggleEjercicioStatus(ejercicioId);
      
      if (ejercicioActualizado) {
        // Mostrar toast de éxito
        showToast(`Ejercicio ${ejercicioActualizado.hecho ? 'completado' : 'marcado como pendiente'}`, 'success');
        
        // Recargar la vista
        loadRutinaDia(ejercicioActualizado.dia);
      } else {
        // Revertir checkbox si hubo error
        this.checked = !this.checked;
        showToast('Error al actualizar el ejercicio', 'error');
      }
      
      // Restaurar interactividad
      card.style.opacity = '1';
      card.style.pointerEvents = 'auto';
    });
  });
}

function sortExercises() {
  showToast('Función de ordenamiento próximamente disponible', 'info');
}

// Función para toggle del menú de días en móvil
function toggleMobileWeekDays() {
  const offcanvas = new bootstrap.Offcanvas(document.getElementById('offcanvasNavbar'));
  offcanvas.show();
}

function renderEjerciciosCards(ejerciciosList) {
  return ejerciciosList
    .map((e, i) => `
      <div class="col">
        <div class="exercise-card h-100">
          <div class="card-img-container">
            <img src="${e.imagenurl && e.imagenurl !== '' ? e.imagenurl : '/img/exercise-default.svg'}" class="card-img-top" alt="${e.nombre}" loading="lazy" onerror="this.src='/img/exercise-default.svg'">
            <div class="card-img-overlay">
              <span class="badge ${e.hecho ? 'bg-success' : 'bg-primary'}">
                ${e.hecho ? 'Completado' : (e.dificultad || 'Intermedio')}
              </span>
            </div>
          </div>
          <div class="card-body">
            <h5 class="card-title">${e.nombre}</h5>
            <p class="card-text">${e.descripcion.substring(0, 80)}${e.descripcion.length > 80 ? "..." : ""}</p>
            
            <div class="form-check mb-3">
              <input class="form-check-input exercise-checkbox" type="checkbox" 
                id="${e.dia || 'default'}-ej${i}" ${e.hecho ? "checked" : ""} data-id="${e.idejercicio || e.nombre}">
              <label class="form-check-label" for="${e.dia || 'default'}-ej${i}">
                ${e.hecho ? 'Completado' : 'Marcar como completado'}
              </label>
            </div>
            
            <div class="card-actions">
              <button class="btn btn-sm btn-primary btn-view" 
                onclick="abrirModal('${e.nombre.replace(/'/g, "\\'")}', '${e.descripcion.replace(/'/g, "\\'")}', '${e.urlvideo || ''}', '${e.dificultad || 'Intermedio'}')">
                <i class="bi bi-play-circle me-1"></i>
                Ver ejercicio
              </button>
              <button class="btn btn-sm btn-outline-light" 
                data-bs-toggle="tooltip" title="Editar ejercicio">
                <i class="bi bi-pencil"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    `)
    .join("");
}

function abrirModal(titulo, descripcion, urlVideo, dificultad) {
  // Obtener el ejercicio completo
  const ejercicio = todosLosEjercicios.find((e) => e.nombre === titulo);
  if (!ejercicio) return;

  // Actualizar el título
  document.getElementById("modalEjercicioLabel").textContent = titulo;
  
  // Actualizar la descripción
  document.getElementById("modalDescripcion").textContent = descripcion;
  
  // Actualizar el video
  const videoFrame = document.getElementById("modalVideo");
  if (urlVideo && urlVideo !== '') {
    videoFrame.src = urlVideo;
  } else {
    videoFrame.src = "https://www.youtube.com/embed/dQw4w9WgXcQ"; // Video por defecto
  }
  
  // Actualizar el badge de dificultad
  const badgeDificultad = document.getElementById("modalDificultad");
  if (badgeDificultad) {
    badgeDificultad.textContent = `Dificultad: ${dificultad || "Intermedia"}`;
  }
  
  // Actualizar categoría si existe
  const badgeCategoria = document.getElementById("modalCategoria");
  if (badgeCategoria) {
    badgeCategoria.textContent = ejercicio.categoria || "General";
  }

  // Cargar instrucciones
  const instruccionesList = document.getElementById("modalInstrucciones");
  if (instruccionesList) {
    instruccionesList.innerHTML = "";
    
    const instrucciones = ejercicio.instrucciones || [
      "Mantén una postura correcta durante todo el ejercicio",
      "Realiza el movimiento de forma controlada",
      "Respira de manera constante",
      "No fuerces más allá de tus límites"
    ];
    
    instrucciones.forEach((instruccion) => {
      const li = document.createElement("li");
      li.textContent = instruccion;
      instruccionesList.appendChild(li);
    });
  }

  // Mostrar el modal
  const modalElement = document.getElementById("modalEjercicio");
  const modal = new bootstrap.Modal(modalElement);
  modal.show();

  // Configurar el botón de marcar como completado
  const btnMarcarCompletado = document.getElementById("btnMarcarCompletado");
  if (btnMarcarCompletado) {
    updateCompleteButton(btnMarcarCompletado, ejercicio);
    
    btnMarcarCompletado.onclick = async () => {
      // Mostrar loading
      const originalContent = btnMarcarCompletado.innerHTML;
      btnMarcarCompletado.innerHTML = '<span class="loading me-2"></span>Procesando...';
      btnMarcarCompletado.disabled = true;
      
      const ejercicioActualizado = await toggleEjercicioStatus(ejercicio.idejercicio || ejercicio.nombre);
      
      if (ejercicioActualizado) {
        updateCompleteButton(btnMarcarCompletado, ejercicioActualizado);
        showToast(`Ejercicio ${ejercicioActualizado.hecho ? 'completado' : 'marcado como pendiente'}`, 'success');
        
        // Actualizar la UI
        loadRutinaDia(ejercicioActualizado.dia);
      } else {
        // Restaurar botón en caso de error
        btnMarcarCompletado.innerHTML = originalContent;
        showToast('Error al actualizar el ejercicio', 'error');
      }
      
      btnMarcarCompletado.disabled = false;
    };
  }
}

function updateCompleteButton(button, ejercicio) {
  if (ejercicio.hecho) {
    button.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Completado';
    button.className = 'btn btn-success';
  } else {
    button.innerHTML = '<i class="bi bi-check-circle me-2"></i>Marcar como completado';
    button.className = 'btn btn-outline-success';
  }
}

function renderProgresos(container) {
  const completados = todosLosEjercicios.filter((e) => e.hecho).length;
  const total = todosLosEjercicios.length;
  const porcentaje = total > 0 ? Math.round((completados / total) * 100) : 0;

  container.innerHTML = `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="mb-4">
            <h3 class="mb-3">
              <i class="bi bi-graph-up me-2"></i>
              <span class="text-gradient">Tus Progresos</span>
            </h3>
            <p class="lead text-muted">Rastrea tu viaje fitness y ve qué tan lejos has llegado</p>
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <div class="progress-container">
            <h5 class="mb-4">
              <i class="bi bi-bar-chart me-2"></i>
              Resumen General
            </h5>
            <div class="row row-cols-1 row-cols-md-3 g-4">
              <div class="col">
                <div class="stat-card">
                  <div class="stat-icon text-success">
                    <i class="bi bi-check-circle"></i>
                  </div>
                  <div class="stat-value">${completados}</div>
                  <div class="stat-label">Ejercicios Completados</div>
                </div>
              </div>
              <div class="col">
                <div class="stat-card">
                  <div class="stat-icon text-warning">
                    <i class="bi bi-clock"></i>
                  </div>
                  <div class="stat-value">${total - completados}</div>
                  <div class="stat-label">Ejercicios Pendientes</div>
                </div>
              </div>
              <div class="col">
                <div class="stat-card">
                  <div class="stat-icon text-primary">
                    <i class="bi bi-percent"></i>
                  </div>
                  <div class="stat-value">${porcentaje}%</div>
                  <div class="stat-label">Progreso Total</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <div class="progress-container">
            <h5 class="mb-4">
              <i class="bi bi-calendar-week me-2"></i>
              Progreso por Día
            </h5>
            ${renderProgresoPorDia()}
          </div>
        </div>
      </div>
    </div>
  `;
}

function renderProgresoPorDia() {
  return diasSemana
    .map((dia) => {
      const ejerciciosDia = ejerciciosPorDia[dia] || []
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
  const completados = todosLosEjercicios.filter((e) => e.hecho).length;
  const total = todosLosEjercicios.length;
  const porcentajeProgreso = total > 0 ? Math.round((completados / total) * 100) : 0;

  container.innerHTML = `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="mb-4">
            <h3 class="mb-3">
              <i class="bi bi-clipboard-data me-2"></i>
              <span class="text-gradient">Informe Semanal</span>
            </h3>
            <p class="lead text-muted">Resumen de tu actividad y rendimiento durante la semana</p>
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <div class="report-card">
            <div class="report-header">
              <div class="report-title">Resumen de la Semana</div>
              <div class="report-date">15 - 21 Julio, 2025</div>
            </div>
            
            <div class="row mb-4">
              <div class="col-md-6">
                <div class="mb-3">
                  <p class="mb-2">
                    <i class="bi bi-check-circle text-success me-2"></i>
                    Esta semana has completado <strong class="text-success">${completados} de ${total}</strong> ejercicios programados.
                  </p>
                  <p class="mb-2">
                    <i class="bi bi-calendar-check text-primary me-2"></i>
                    Tu día más activo fue <strong class="text-primary">Lunes</strong> con ejercicios completados.
                  </p>
                  <p class="mb-0">
                    <i class="bi bi-clock text-warning me-2"></i>
                    Tienes <strong class="text-warning">${total - completados}</strong> ejercicios pendientes para completar.
                  </p>
                </div>
              </div>
              <div class="col-md-6">
                <div class="text-center">
                  <div class="progress mb-3" style="height: 20px;">
                    <div class="progress-bar bg-gradient" role="progressbar" 
                      style="width: ${porcentajeProgreso}%; background: linear-gradient(90deg, var(--primary), var(--success));" 
                      aria-valuenow="${completados}" aria-valuemin="0" aria-valuemax="${total}">
                      ${porcentajeProgreso}%
                    </div>
                  </div>
                  <p class="text-muted mb-0">Progreso semanal</p>
                </div>
              </div>
            </div>
            
            <div class="alert alert-info">
              <i class="bi bi-lightbulb me-2"></i>
              <strong>Consejo de la semana:</strong>
              Recuerda mantener una buena hidratación durante tus entrenamientos para mejorar tu rendimiento.
            </div>
          </div>
        </div>
      </div>
      
      <div class="row row-cols-1 row-cols-md-2 g-4">
        <div class="col">
          <div class="report-card">
            <div class="report-header">
              <div class="report-title">
                <i class="bi bi-star me-2"></i>
                Ejercicios Destacados
              </div>
            </div>
            <div class="list-group list-group-flush">
              <div class="list-group-item bg-transparent border-0 px-0">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <i class="bi bi-star-fill text-warning me-2"></i>
                    <span>Sentadillas</span>
                  </div>
                  <span class="badge bg-success">Completado</span>
                </div>
              </div>
              <div class="list-group-item bg-transparent border-0 px-0">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <i class="bi bi-star-fill text-warning me-2"></i>
                    <span>Push Ups</span>
                  </div>
                  <span class="badge bg-primary">Pendiente</span>
                </div>
              </div>
              <div class="list-group-item bg-transparent border-0 px-0">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <i class="bi bi-star-fill text-warning me-2"></i>
                    <span>Planchas</span>
                  </div>
                  <span class="badge bg-success">Completado</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="report-card">
            <div class="report-header">
              <div class="report-title">
                <i class="bi bi-trophy me-2"></i>
                Próximos Objetivos
              </div>
            </div>
            <div class="list-group list-group-flush">
              <div class="list-group-item bg-transparent border-0 px-0">
                <i class="bi bi-target text-primary me-2"></i>
                Completar todos los ejercicios de la semana
              </div>
              <div class="list-group-item bg-transparent border-0 px-0">
                <i class="bi bi-arrow-up-circle text-success me-2"></i>
                Aumentar la intensidad de los ejercicios
              </div>
              <div class="list-group-item bg-transparent border-0 px-0">
                <i class="bi bi-calendar-plus text-warning me-2"></i>
                Agregar nuevos ejercicios a la rutina
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `;
}

// Función para cerrar sesión
function logout() {
  // Limpiar localStorage
  localStorage.removeItem('jwt_token');
  localStorage.removeItem('user_email');
  localStorage.removeItem('user_username');
  
  // Redirigir al login
  window.location.href = '/login';
}

// Variables globales para ejercicios disponibles
let ejerciciosDisponibles = [];

// Función para cargar ejercicios disponibles
async function cargarEjerciciosDisponibles() {
  if (!checkAuthentication()) return;
  
  try {
    const response = await fetch('/api/ejercicios/disponibles', {
      headers: getAuthHeaders()
    });
    
    if (!response.ok) {
      throw new Error('Error al cargar ejercicios disponibles');
    }
    
    ejerciciosDisponibles = await response.json();
    console.log('Ejercicios disponibles cargados:', ejerciciosDisponibles.length);
    
    // Llenar el select del modal
    llenarSelectEjercicios();
    
  } catch (error) {
    console.error('Error al cargar ejercicios disponibles:', error);
    showToast('Error al cargar la lista de ejercicios', 'error');
  }
}

// Función para llenar el select de ejercicios en el modal
function llenarSelectEjercicios() {
  const selectEjercicio = document.getElementById('selectEjercicio');
  if (!selectEjercicio) return;
  
  // Limpiar opciones anteriores
  selectEjercicio.innerHTML = '<option value="">Seleccionar ejercicio...</option>';
  
  // Agregar ejercicios disponibles
  ejerciciosDisponibles.forEach(ejercicio => {
    const option = document.createElement('option');
    option.value = ejercicio.idejercicio;
    option.textContent = ejercicio.nombre;
    option.setAttribute('data-descripcion', ejercicio.descripcion);
    option.setAttribute('data-imagen', ejercicio.imagenurl || '/img/exercise-default.svg');
    option.setAttribute('data-dificultad', ejercicio.dificultad || 'Intermedio');
    selectEjercicio.appendChild(option);
  });
  
  // Agregar event listener para mostrar vista previa
  selectEjercicio.addEventListener('change', mostrarVistaPrevia);
}

// Función para mostrar vista previa del ejercicio seleccionado
function mostrarVistaPrevia() {
  const selectEjercicio = document.getElementById('selectEjercicio');
  const exercisePreview = document.getElementById('exercisePreview');
  const selectedOption = selectEjercicio.selectedOptions[0];
  
  if (selectedOption && selectedOption.value) {
    // Obtener datos del ejercicio
    const nombre = selectedOption.textContent;
    const descripcion = selectedOption.getAttribute('data-descripcion');
    const imagen = selectedOption.getAttribute('data-imagen');
    const dificultad = selectedOption.getAttribute('data-dificultad');
    
    // Actualizar elementos de vista previa
    document.getElementById('previewName').textContent = nombre;
    document.getElementById('previewDescription').textContent = descripcion.substring(0, 100) + (descripcion.length > 100 ? '...' : '');
    document.getElementById('previewImage').src = imagen;
    document.getElementById('previewDifficulty').textContent = `Dificultad: ${dificultad}`;
    
    // Mostrar vista previa
    exercisePreview.style.display = 'block';
  } else {
    // Ocultar vista previa
    exercisePreview.style.display = 'none';
  }
}

// Función para agregar ejercicio a la rutina
async function agregarEjercicioARutina() {
  if (!checkAuthentication()) return;
  
  const selectEjercicio = document.getElementById('selectEjercicio');
  const selectDiaSemana = document.getElementById('selectDiaSemana');
  
  const idEjercicio = selectEjercicio.value;
  const dia = selectDiaSemana.value;
  
  // Validaciones
  if (!idEjercicio) {
    showToast('Por favor selecciona un ejercicio', 'error');
    return;
  }
  
  if (!dia) {
    showToast('Por favor selecciona un día de la semana', 'error');
    return;
  }
  
  // Obtener idCuenta del usuario (por ahora usar 1 para desarrollo)
  const idCuenta = 1; // TODO: Obtener del token JWT o sesión
  
  try {
    // Mostrar loading
    const btnAgregar = document.querySelector('[onclick="agregarEjercicioARutina()"]');
    const originalText = btnAgregar.innerHTML;
    btnAgregar.innerHTML = '<span class="loading me-2"></span>Agregando...';
    btnAgregar.disabled = true;
    
    const response = await fetch('/api/ejercicios/agregar-a-rutina', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        idEjercicio: parseInt(idEjercicio),
        dia: dia,
        idCuenta: idCuenta
      })
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Error al agregar ejercicio');
    }
    
    const data = await response.json();
    
    if (data.success) {
      showToast('Ejercicio agregado exitosamente a la rutina', 'success');
      
      // Cerrar modal
      const modal = bootstrap.Modal.getInstance(document.getElementById('modalAgregarEjercicio'));
      modal.hide();
      
      // Limpiar formulario
      selectEjercicio.value = '';
      selectDiaSemana.value = '';
      document.getElementById('exercisePreview').style.display = 'none';
      
      // Recargar ejercicios para actualizar la UI
      await cargarEjercicios();
      
      // Actualizar la vista del día actual o cargar el día recién agregado
      if (currentDay === dia) {
        loadRutinaDia(dia);
      } else {
        loadRutinaDia(dia);
      }
      
    } else {
      throw new Error(data.message || 'Error desconocido');
    }
    
  } catch (error) {
    console.error('Error al agregar ejercicio:', error);
    showToast(`Error al agregar ejercicio: ${error.message}`, 'error');
  } finally {
    // Restaurar botón
    const btnAgregar = document.querySelector('[onclick="agregarEjercicioARutina()"]');
    if (btnAgregar) {
      btnAgregar.innerHTML = '<i class="bi bi-plus-lg me-2"></i>Agregar a Rutina';
      btnAgregar.disabled = false;
    }
  }
}

// Función para preseleccionar el día en el modal
function preseleccionarDia(dia) {
  setTimeout(() => {
    const selectDiaSemana = document.getElementById('selectDiaSemana');
    if (selectDiaSemana && dia) {
      selectDiaSemana.value = dia;
    }
  }, 100);
}

// Event listener para cuando se abre el modal
document.addEventListener('DOMContentLoaded', function() {
  const modalAgregarEjercicio = document.getElementById('modalAgregarEjercicio');
  if (modalAgregarEjercicio) {
    modalAgregarEjercicio.addEventListener('show.bs.modal', function() {
      // Cargar ejercicios disponibles si no están cargados
      if (ejerciciosDisponibles.length === 0) {
        cargarEjerciciosDisponibles();
      }
    });
  }
});
