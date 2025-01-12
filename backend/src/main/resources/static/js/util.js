function populateSelectOptions(selectId, items, selectedId) {
    const selectElement = document.querySelector(`[name=${selectId}]`);
    selectElement.innerHTML = '<option value="">-- Select --</option>';

    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = typeof item.name !== 'undefined' ? item.name : `${item.x}, ${item.y}`;
        if (item.id === selectedId) {
            option.selected = true;
        }
        selectElement.appendChild(option);
    });
}

function formatUTCDate(dateString) {
    let date = new Date(dateString);
    return date.getUTCFullYear() + "-" +
        ("0" + (date.getUTCMonth() + 1)).slice(-2) + "-" +
        ("0" + date.getUTCDate()).slice(-2) + " " +
        ("0" + date.getUTCHours()).slice(-2) + ":" +
        ("0" + date.getUTCMinutes()).slice(-2) + ":" +
        ("0" + date.getUTCSeconds()).slice(-2) + "." +
        ("00" + date.getUTCMilliseconds()).slice(-3);
}

function removeRequiredIfHidden() {
    const hiddenFields = document.querySelectorAll('[required]');
    hiddenFields.forEach(field => field.removeAttribute('required'));
}

function toggleCreateCoordinates() {
    const fields = document.getElementById("newCoordinatesFields");
    const select = document.getElementById("coordinatesId");
    const inputs = fields.querySelectorAll('input');

    if (select.value === '') {
        fields.style.display = 'block';
        inputs.forEach(input => input.required = true);
    } else {
        fields.style.display = 'none';
        inputs.forEach(input => input.required = false);
    }
}

function createNewCoordinates() {
    const fields = document.getElementById("newCoordinatesFields");
    const select = document.getElementById("coordinatesId");

    select.required = false;

    const inputs = fields.querySelectorAll('input');
    inputs.forEach(input => input.required = true);

    select.value = "";
    fields.style.display = "block";
}

function toggleCreateDiscipline() {
    const fields = document.getElementById("newDisciplineFields");
    const select = document.getElementById("disciplineId");
    const inputs = fields.querySelectorAll('input');

    if (select.value === '') {
        fields.style.display = 'block';
        inputs.forEach(input => input.required = true);
    } else {
        fields.style.display = 'none';
        inputs.forEach(input => input.required = false);
    }
}

function createNewDiscipline() {
    const fields = document.getElementById("newDisciplineFields");
    const select = document.getElementById("disciplineId");

    select.required = false;

    const inputs = fields.querySelectorAll('input');
    inputs.forEach(input => input.required = true);

    select.value = "";
    fields.style.display = "block";
}

function toggleCreateAuthor() {
    const fields = document.getElementById("newAuthorFields");
    const select = document.getElementById("authorId");
    const inputs = fields.querySelectorAll('input');

    if (select.value === '') {
        fields.style.display = 'block';
        inputs.forEach(input => input.required = true);
    } else {
        fields.style.display = 'none';
        inputs.forEach(input => input.required = false);
    }
}

function createNewAuthor() {
    const fields = document.getElementById("newAuthorFields");
    const select = document.getElementById("authorId");

    select.required = false;

    const inputs = fields.querySelectorAll('input');
    inputs.forEach(input => input.required = true);

    select.value = "";
    fields.style.display = "block";
}

function toggleCreateLocation() {
    const fields = document.getElementById("newLocationFields");
    const select = document.getElementById("locationId");
    const inputs = fields.querySelectorAll('input');

    if (select.value === '') {
        fields.style.display = 'block';
        inputs.forEach(input => input.required = true);
    } else {
        fields.style.display = 'none';
        inputs.forEach(input => input.required = false);
    }
}

function createNewLocation() {
    const fields = document.getElementById("newLocationFields");
    const select = document.getElementById("locationId");

    select.required = false;

    const inputs = fields.querySelectorAll('input');
    inputs.forEach(input => input.required = true);

    select.value = "";
    fields.style.display = "block";
}

function restoreRequiredFields() {
    const requiredFields = [
        "labWorkName",
        "labWorkDescription",
        "labWorkDifficulty",
        "coordinatesId",
        "disciplineId",
        "minimalPoint",
        "personalQualitiesMinimum",
        "personalQualitiesMaximum",
        "authorId"
    ];

    requiredFields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.required = true;
        }
    });
}

document.getElementById('coordinatesId').addEventListener('change', function () {
    if (this.value) {
        document.getElementById('newCoordinatesFields').style.display = 'none';
    }
});
document.getElementById('disciplineId').addEventListener('change', function () {
    if (this.value) {
        document.getElementById('newDisciplineFields').style.display = 'none';
    }
});
document.getElementById('authorId').addEventListener('change', function () {
    if (this.value) {
        document.getElementById('newAuthorFields').style.display = 'none';
    }
});
document.getElementById('locationId').addEventListener('change', function () {
    if (this.value) {
        document.getElementById('newLocationFields').style.display = 'none';
    }
});

function resetForm() {
    const form = document.getElementById('labWorkForm');
    form.reset();
}