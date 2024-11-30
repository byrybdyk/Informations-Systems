function isValidFloat(value) {
    return !isNaN(value) && value > 0;
}

function isValidInt(value) {
    return Number.isInteger(Number(value)) && value > 0;
}

function isValidLong(value) {
    return !isNaN(value) && Number.isInteger(Number(value)) && value > 0;
}

function isValidDouble(value) {
    return !isNaN(value) && value > 0;
}

function isValidInteger(value) {
    return Number.isInteger(Number(value)) && value > 0;
}

function validateFields() {
    const fieldsToValidate = [
        { name: 'newCoordinatesX', validator: isValidFloat },
        { name: 'newCoordinatesY', validator: isValidInt },
        { name: 'newPracticeHours', validator: isValidLong },
        { name: 'minimalPoint', validator: isValidFloat },
        { name: 'personalQualitiesMinimum', validator: isValidDouble },
        { name: 'personalQualitiesMaximum', validator: isValidFloat },
        { name: 'newAuthorWeight', validator: isValidDouble },
        { name: 'passportID', validator: isValidInteger },
        { name: 'newLocationX', validator: isValidFloat },
        { name: 'newLocationY', validator: isValidFloat }
    ];

    let isValid = true;

    fieldsToValidate.forEach(({ name, validator }) => {
        const field = document.querySelector(`[name="${name}"]`);

        if (field && field.offsetParent !== null) {
            const value = field.value;
            if (!validator(value)) {
                alert(`Поле "${name}" имеет неверное значение. Оно должно быть больше нуля и соответствовать типу.`);
                isValid = false;
            }
        }
    });

    return isValid;
}