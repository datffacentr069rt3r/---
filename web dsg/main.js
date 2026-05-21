// main.js для лендинга DSG с анимацией печатания текста

// ===============================
// Mobile menu
// ===============================
const menuToggle = document.getElementById("menuToggle");
const mobileMenu = document.getElementById("mobileMenu");

if (menuToggle && mobileMenu) {
  menuToggle.addEventListener("click", () => {
    const isOpen = mobileMenu.classList.toggle("is-open");
    menuToggle.setAttribute("aria-expanded", String(isOpen));
  });
  document.addEventListener("click", (e) => {
    if (!mobileMenu.contains(e.target) && !menuToggle.contains(e.target)) {
      mobileMenu.classList.remove("is-open");
      menuToggle.setAttribute("aria-expanded", "false");
    }
  });
}

// ===============================
// Testimonials с плавным fade и переключением
// ===============================
const testimonialText = document.getElementById("testimonialText");
const testimonialDots = document.querySelectorAll("#testimonialDots .dot");
const testimonials = [
  "Deaf Smart полностью изменила мою жизнь.",
  "Я использую эти очки каждый день.",
  "Самое ценное — это простота."
];

let currentIndex = 0;
function renderTestimonial(index) {
  if (!testimonialText) return;
  testimonialText.style.opacity = 0;
  setTimeout(() => {
    testimonialText.textContent = testimonials[index];
    testimonialText.style.opacity = 1;
    testimonialDots.forEach((dot, i) => dot.classList.toggle("is-active", i === index));
  }, 300);
}

testimonialDots.forEach(dot => {
  dot.addEventListener("click", () => {
    currentIndex = Number(dot.dataset.slide);
    renderTestimonial(currentIndex);
  });
});

let testimonialInterval = setInterval(() => {
  currentIndex = (currentIndex + 1) % testimonials.length;
  renderTestimonial(currentIndex);
}, 5000);

testimonialText.parentElement.addEventListener("mouseenter", () => clearInterval(testimonialInterval));
testimonialText.parentElement.addEventListener("mouseleave", () => {
  testimonialInterval = setInterval(() => {
    currentIndex = (currentIndex + 1) % testimonials.length;
    renderTestimonial(currentIndex);
  }, 5000);
});

// ===============================
// IntersectionObserver для fade-up анимации и typing effect
// ===============================
const animateElements = document.querySelectorAll("[data-animate]");
animateElements.forEach(el => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add("animate");
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.5 });
  observer.observe(el);
});

// ===============================
// Typing effect для всех заголовков
// ===============================
const typingTitles = document.querySelectorAll('.typing-title');

typingTitles.forEach(title => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if(entry.isIntersecting){
        entry.target.classList.add('animate'); // запускаем CSS анимацию печати
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.5 });
  observer.observe(title);
});