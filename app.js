document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'timeGridWeek',
        editable: true,
        selectable: true,
        events: [], // Сюда будем загружать доступность и события
    });

    calendar.render();

    // Функция для загрузки и отображения доступности и событий
    async function loadSchedule() {
        const teacherId = prompt("Enter Teacher ID to view schedule:", "22222222-2222-2222-2222-222222222222");
        if (!teacherId) return;

        // Загружаем доступность
        const availabilityResponse = await fetch(`http://localhost:8080/availability/teacher/${teacherId}`);
        const availabilities = await availabilityResponse.json();

        availabilities.forEach(slot => {
            calendar.addEvent({
                title: 'Available',
                start: slot.startTime,
                end: slot.endTime,
                backgroundColor: '#28a745', // Зеленый цвет для доступности
                borderColor: '#28a745',
                rendering: 'background' // Делаем доступность как фон
            });
        });

        // Загружаем события
        const eventsResponse = await fetch(`http://localhost:8080/events/teacher/${teacherId}`);
        const events = await eventsResponse.json();

        events.forEach(event => {
            calendar.addEvent({
                title: `Event: ${event.eventType}`,
                start: event.startTime,
                end: event.endTime,
                backgroundColor: '#007bff', // Синий цвет для событий
                borderColor: '#007bff'
            });
        });
    }

    loadSchedule();

    // Создание нового события
    const createEventForm = document.getElementById('create-event-form');
    createEventForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const teacherId = document.getElementById('teacherId').value;
        const studentIds = document.getElementById('studentIds').value.split(',').map(id => id.trim());
        const startTime = document.getElementById('startTime').value;
        const endTime = document.getElementById('endTime').value;
        const eventType = document.getElementById('eventType').value;
        const status = document.getElementById('status').value;
        const createdByType = document.getElementById('createdByType').value;
        const createdById = document.getElementById('createdById').value;

        const eventData = {
            teacherId,
            studentIds,
            startTime,
            endTime,
            eventType,
            status,
            createdByType,
            createdById
        };

        const response = await fetch('http://localhost:8080/events', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventData)
        });

        const result = await response.json();
        document.getElementById('create-event-result').textContent = `Event Created: ${JSON.stringify(result)}`;

        // Обновление календаря
        calendar.addEvent({
            title: `Event: ${result.eventType}`,
            start: result.startTime,
            end: result.endTime,
            backgroundColor: '#007bff',
            borderColor: '#007bff'
        });
    });

    // Добавление доступности учителя
    const addAvailabilityForm = document.getElementById('add-availability-form');
    addAvailabilityForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const teacherId = document.getElementById('availabilityTeacherId').value;
        const startTime = document.getElementById('availabilityStartTime').value;
        const endTime = document.getElementById('availabilityEndTime').value;

        const availabilityData = {
            teacherId,
            startTime,
            endTime
        };

        const response = await fetch('http://localhost:8080/availability', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(availabilityData)
        });

        const result = await response.json();
        document.getElementById('availability-result').textContent = `Availability Added: ${JSON.stringify(result)}`;

        // Обновление календаря
        calendar.addEvent({
            title: 'Available',
            start: result.startTime,
            end: result.endTime,
            backgroundColor: '#28a745',
            borderColor: '#28a745',
            rendering: 'background'
        });
    });
});