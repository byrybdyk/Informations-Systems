var labWorks = /*[[${labWorks}]]*/ [];
var stompClient = null;
const pageSize = 5;
let currentPage = 1;
let totalPages = 1;
let currentSortColumn = null;
let currentSortOrder = 'asc';
let currentFilters = {};

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/labworks', function (message) {
            var labWork = JSON.parse(message.body);
            console.log('---------------------------------', message);

            if (labWork.type === 'delete' && labWork.labWorkId) {
                onLabWorkDelete(labWork.labWorkId);
            } else {
                onLabWorkUpdate(labWork);
            }
        });
    }, function (error) {
        console.error('WebSocket connection failed: ' + error);
    });
}

function onLabWorkUpdate(labWork) {
    console.log('LabWork updated:', labWork);

    if (labWork.id == null) {
        return;
    }

    const row = document.querySelector(`tr[data-id="${labWork.id}"]`);
    if (row) {
        row.innerHTML = `
                    <td>${labWork.id}</td>
                    <td>${labWork.owner.username}</td>
                    <td>${formatUTCDate(labWork.creationDate)}</td>
                    <td>${labWork.name}</td>
                    <td>${labWork.discipline.name}</td>
                    <td>${labWork.discipline.practiceHours}</td>
                    <td>${labWork.description}</td>
                    <td>${labWork.difficulty}</td>
                    <td>${labWork.coordinates.x}</td>
                    <td>${labWork.coordinates.y}</td>
                    <td>${labWork.minimalPoint}</td>
                    <td>${labWork.personalQualitiesMaximum}</td>
                    <td>${labWork.personalQualitiesMinimum}</td>
                    <td>${labWork.author.name}</td>
                    <td>${labWork.author.weight}</td>
                    <td>${labWork.author.eyeColor}</td>
                    <td>${labWork.author.hairColor}</td>
                    <td>${labWork.author.location.x}</td>
                    <td>${labWork.author.location.y}</td>
                    <td>${labWork.author.location.name}</td>
                    <td>${labWork.author.passportID}</td>
                    <td><button onclick="openFormModal(${labWork.id})">Edit</button></td>
                    <td><button onclick="confirmDeleteLabWork(${labWork.id})">Delete</button></td>
                `;
    }
    else{
        renderTable(currentPage);
    }
}

function onLabWorkDelete(labWorkId) {
    labWorks = labWorks.filter(labWork => labWork.id !== labWorkId);

    const row = document.querySelector(`tr[data-id="${labWorkId}"]`);
    if (row) {
        row.remove();
        console.log(`LabWork with ID ${labWorkId} has been deleted from the table.`);
    }

    renderTable(currentPage);
}

function renderTable(page) {
    const pageSize = 6;
    let url = `/labworks/home?page=${page}&size=${pageSize}`;

    if (currentSortColumn) {
        url += `&sort=${currentSortColumn}&order=${currentSortOrder}`;
    }

    Object.keys(currentFilters).forEach(key => {
        if (currentFilters[key]) {
            url += `&${key}=${encodeURIComponent(currentFilters[key])}`;
        }
    });

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('labWorkTableBody');
            tableBody.innerHTML = "";
            data.labWorks.forEach(labWork => {
                const row = document.createElement('tr');
                row.setAttribute('data-id', labWork.id);
                row.innerHTML = `
                    <td>${labWork.id}</td>
                    <td>${labWork.owner.username}</td>
                    <td>${formatUTCDate(labWork.creationDate)}</td>
                    <td>${labWork.name}</td>
                    <td>${labWork.discipline.name}</td>
                    <td>${labWork.discipline.practiceHours}</td>
                    <td>${labWork.description}</td>
                    <td>${labWork.difficulty}</td>
                    <td>${labWork.coordinates.x}</td>
                    <td>${labWork.coordinates.y}</td>
                    <td>${labWork.minimalPoint}</td>
                    <td>${labWork.personalQualitiesMaximum}</td>
                    <td>${labWork.personalQualitiesMinimum}</td>
                    <td>${labWork.author.name}</td>
                    <td>${labWork.author.weight}</td>
                    <td>${labWork.author.eyeColor}</td>
                    <td>${labWork.author.hairColor}</td>
                    <td>${labWork.author.location.x}</td>
                    <td>${labWork.author.location.y}</td>
                    <td>${labWork.author.location.name}</td>
                    <td>${labWork.author.passportID}</td>
                    <td><button onclick="openFormModal(${labWork.id})">Edit</button></td>
                    <td><button onclick="confirmDeleteLabWork(${labWork.id})">Delete</button></td>
                `;
                tableBody.appendChild(row);
            });

            currentPage = data.currentPage;
            totalPages = data.totalPages;

            document.getElementById('currentPage').textContent = currentPage;
            document.getElementById('totalPages').textContent = totalPages;
        })
        .catch(error => console.error('Error:', error));
}

function nextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        renderTable(currentPage);
    }
}

function previousPage() {
    if (currentPage > 1) {
        currentPage--;
        renderTable(currentPage);
    }
}

window.onload = function () {
    connect();
    renderTable(currentPage);
};

async function addLabWork() {

    removeRequiredIfHidden();

    const formData = new FormData(document.getElementById('labWorkForm'));

    const labWorkDTO = {
        name: formData.get('name'),

        coordinates: formData.get('coordinatesId') ? { id: formData.get('coordinatesId') } : {
            x: formData.get('newCoordinatesX'),
            y: formData.get('newCoordinatesY')
        },

        description: formData.get('description'),

        difficulty: formData.get('difficulty'),

        discipline: formData.get('disciplineId') ? { id: formData.get('disciplineId') } : {
            name: formData.get('newDisciplineName'),
            practiceHours: formData.get('newPracticeHours')
        },

        minimalPoint: formData.get('minimalPoint'),
        personalQualitiesMinimum: formData.get('personalQualitiesMinimum'),
        personalQualitiesMaximum: formData.get('personalQualitiesMaximum'),
        author: formData.get('authorId') ? { id: formData.get('authorId')}  : {
            name: formData.get('newAuthorName'),
            weight: formData.get('newAuthorWeight'),
            location: formData.get('locationId') ? { id: formData.get('locationId') } : {
                name: formData.get('newLocationName'),
                x: formData.get('newLocationX'),
                y: formData.get('newLocationY')
            },
            eyeColor: formData.get('newAuthorEyeColor'),
            hairColor: formData.get('newAuthorHairColor'),
            passportID: formData.get('passportID')
        },
        ownerName: document.getElementById("userName").textContent
    };

    stompClient.send("/app/labworks/add", {}, JSON.stringify(labWorkDTO));

    closeModal();
}

async function editLabWork(event, labWorkId) {
    if (event && event.preventDefault) {
        event.preventDefault();
    }
    removeRequiredIfHidden();
    const formData = new FormData(document.getElementById('labWorkForm'));
    const owner = document.getElementById('ownerField').value;
    const labWorkDTO = {
        id: labWorkId,
        name: formData.get('name'),

        coordinates: formData.get('coordinatesId') ? { id: formData.get('coordinatesId') } : {
            x: formData.get('newCoordinatesX'),
            y: formData.get('newCoordinatesY')
        },

        description: formData.get('description'),

        difficulty: formData.get('difficulty'),

        discipline: formData.get('disciplineId') ? { id: formData.get('disciplineId') } : {
            name: formData.get('newDisciplineName'),
            practiceHours: formData.get('newPracticeHours')
        },

        minimalPoint: formData.get('minimalPoint'),
        personalQualitiesMinimum: formData.get('personalQualitiesMinimum'),
        personalQualitiesMaximum: formData.get('personalQualitiesMaximum'),
        author: formData.get('authorId') ? { id: formData.get('authorId')}  : {
            name: formData.get('newAuthorName'),
            weight: formData.get('newAuthorWeight'),
            location: formData.get('locationId') ? { id: formData.get('locationId') } : {
                name: formData.get('newLocationName'),
                x: formData.get('newLocationX'),
                y: formData.get('newLocationY')
            },
            eyeColor: formData.get('newAuthorEyeColor'),
            hairColor: formData.get('newAuthorHairColor'),
            passportID: formData.get('passportID')
        },
        ownerName: owner
    };

    stompClient.send("/app/labworks/update", {}, JSON.stringify(labWorkDTO));

    closeModal();
}

async function openFormModal(labWorkId = null) {
    let labWork = null;
    if (labWorkId) {
        try {
            const response = await fetch(`/labworks/${labWorkId}`);
            if (response.ok) {
                labWork = await response.json();
            } else {
                console.error('Lab work not found');
                Swal.fire({
                    title: "Error",
                    icon: "error",
                    text: "Lab Work not found.",
                    confirmButtonText: "OK",
                });
                return;
            }
        } catch (error) {
            console.error('Error fetching lab work data:', error);
            Swal.fire({
                title: "Error",
                icon: "error",
                text: "An error occurred while fetching the lab work data.",
                confirmButtonText: "OK",
            });
            return;
        }
    }

    document.getElementById("newCoordinatesFields").style.display = "none";
    document.getElementById("newDisciplineFields").style.display = "none";
    document.getElementById("newAuthorFields").style.display = "none";
    document.getElementById("newLocationFields").style.display = "none";
    const submitButton = document.getElementById("submitButton");

    if (labWork) {
        const currentUserName = document.getElementById("userName").textContent;
        if ( await canEdit(labWorkId) === true) {
            document.getElementById("modalTitle").textContent = "Edit Lab Work";
            submitButton.textContent = "Update Lab Work";
            submitButton.setAttribute("value", "update");

            document.getElementById("labWorkId").value = labWork.id || "";
            document.getElementById("labWorkName").value = labWork.name || "";
            document.getElementById("labWorkDescription").value = labWork.description || "";
            document.getElementById("labWorkDifficulty").value = labWork.difficulty || "";
            document.getElementById("minimalPoint").value = labWork.minimalPoint || "";
            document.getElementById("personalQualitiesMinimum").value = labWork.personalQualitiesMinimum || "";
            document.getElementById("personalQualitiesMaximum").value = labWork.personalQualitiesMaximum || "";
            document.getElementById("ownerField").value = labWork.owner.username || "";
        }
        else {
            closeModal()
            return
        }
    } else {
        resetForm()
        restoreRequiredFields();
        document.getElementById("modalTitle").textContent = "Add Lab Work";
        submitButton.textContent = "Add Lab Work";
        submitButton.setAttribute("value", "add");
    }

    fetch('/labworks/form-data')
        .then(response => response.json())
        .then(data => {
            populateSelectOptions('disciplineId', data.disciplines, 'name');
            populateSelectOptions('coordinatesId', data.coordinates, coord => `X: ${coord.x}, Y: ${coord.y}`);
            populateSelectOptions('authorId', data.authors, 'name');
            populateSelectOptions('locationId', data.locations, 'name');

            if (labWork) {
                document.getElementById("coordinatesId").value = labWork.coordinates.id || "";
                document.getElementById("disciplineId").value = labWork.discipline.id || "";
                document.getElementById("authorId").value = labWork.author.id || "";
                document.getElementById("locationId").value = labWork.author.location.id || "";
            }
        })
        .catch(error => {
            console.error('Error fetching form data:', error);
        });

    document.getElementById("labWorkModal").style.display = "block";
}

async function handleSubmit(event, labWorkId) {
    event.preventDefault();

    if (!validateFields()) {
        return;
    }
    const form = document.getElementById('labWorkForm');
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    const action = document.getElementById("submitButton").value;

    if ((action === 'update' && labWorkId)) {
        editLabWork(event, labWorkId);
    } else {
        addLabWork(data);
    }
    closeModal();
}

document.getElementById('labWorkForm').addEventListener('submit', handleSubmit);

function closeModal() {
    document.getElementById("labWorkModal").style.display = "none";
    resetForm();
}

async function canEdit(labWorkId) {

    const response = await fetch(`/labworks/${labWorkId}/can-edit`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    const canDelete = await response.json();
    if (canDelete){
        return true;
    }
    else {
        closeModal()
        console.error('Permission denied to edit this lab work');
        Swal.fire({
            title: "Permission Denied",
            icon: "error",
            text: "You can only edit your own lab works.",
            confirmButtonText: "OK",
        });
        return false;
    }
}


async function confirmDeleteLabWork(labWorkId) {
    if ( await canEdit(labWorkId) === true) {
        stompClient.send('/app/labworks/delete', {}, JSON.stringify({
            labWorkId: labWorkId}));
    }
}

async function deleteByAuthor() {
    const author = document.getElementById('deleteAuthor').value;
    const response = await fetch(`/labworks/deleteByAuthor/${author}`, { method: 'DELETE' });
    const result = await response.text();
    document.getElementById('deleteResult').textContent = result;
}

async function countByAuthorLessThenWeight() {
    const weight = document.getElementById('countAuthor').value;
    const response = await fetch(`/labworks/countByAuthorLessThenWeight/${weight}`, { method: 'GET' });

    if (response.ok) {
        const result = await response.text();
        document.getElementById('countResult').textContent = result;
    } else {
        document.getElementById('countResult').textContent = "Error: " + response.statusText;
    }
}

async function decreaseDifficulty() {
    const labWorkId = document.getElementById('labWorkId').value;
    const steps = document.getElementById('difficultySteps').value;
    const response = await fetch(`/labworks/decreaseDifficulty/${labWorkId}/${steps}`, { method: 'POST' });
    if (response.ok) {
        const result = await response.text();
        document.getElementById('decreaseResult').textContent = result;
    } else {
        document.getElementById('decreaseResult').textContent = "Error: " + response.statusText;
    }
}

async function findByDescriptionPrefix() {
    const prefix = document.getElementById('descriptionPrefix').value;
    const response = await fetch(`/labworks/findByDescriptionPrefix/${prefix}`, { method: 'GET' });

    if (response.ok) {
        const result = await response.text();
        document.getElementById('findResult').textContent = result;
    } else {
        document.getElementById('findResult').textContent = "Error: " + response.statusText;
    }
}