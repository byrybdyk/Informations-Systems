function applySorting(column) {
    if (currentSortColumn === column) {
        currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
    } else {
        currentSortColumn = column;
        currentSortOrder = 'asc';
    }
    renderTable(currentPage);
}

function applyFilters() {
    currentFilters = {
        id: document.getElementById('filterId').value,
        ownerUsername: document.getElementById('filterOwnerUsername').value,
        creationDate: document.getElementById('filterCreationDate').value,
        name: document.getElementById('filterName').value,
        disciplineName: document.getElementById('filterDisciplineName').value,
        disciplinePracticeHours: document.getElementById('filterDisciplinePracticeHours').value,
        description: document.getElementById('filterDescription').value,
        difficulty: document.getElementById('filterDifficulty').value,
        coordinatesX: document.getElementById('filterCoordinatesX').value,
        coordinatesY: document.getElementById('filterCoordinatesY').value,
        minimalPoint: document.getElementById('filterMinimalPoint').value,
        personalQualitiesMaximum: document.getElementById('filterPersonalQualitiesMaximum').value,
        personalQualitiesMinimum: document.getElementById('filterPersonalQualitiesMinimum').value,
        authorName: document.getElementById('filterAuthorName').value,
        authorWeight: document.getElementById('filterAuthorWeight').value,
        authorEyeColor: document.getElementById('filterAuthorEyeColor').value,
        authorHairColor: document.getElementById('filterAuthorHairColor').value,
        authorLocationX: document.getElementById('filterAuthorLocationX').value,
        authorLocationY: document.getElementById('filterAuthorLocationY').value,
        authorLocationName: document.getElementById('filterAuthorLocationName').value,
        authorPassportID: document.getElementById('filterAuthorPassportID').value,
    };

    renderTable(currentPage);
}

function clearFilters() {
    document.getElementById('filterName').value = "";
    document.getElementById('filterDescription').value = "";
    document.getElementById('filterAuthorName').value = "";

    currentFilters = {};
    renderTable(1);
}