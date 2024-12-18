import {calculateArrivalTime} from './util/train-utils.js';
import {formatDuration, parseDuration, updateDurationLabels} from './util/duration.js';
import {handleError, clearErrors, displayError} from '../util/error/error-handler.js';


window.updateDurationLabels = updateDurationLabels;
document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const searchByNumberBtn = document.getElementById('searchByNumberBtn');
    const searchByDepartureBtn = document.getElementById('searchByDepartureBtn');
    const searchByArrivalBtn = document.getElementById('searchByArrivalBtn');
    const searchByDepartureArrivalBtn = document.getElementById('searchByDepartureArrivalBtn');
    const filterFromTime = document.getElementById('filterFromTime');
    const filterToTime = document.getElementById('filterToTime');
    const minDuration = document.getElementById('minDuration');
    const maxDuration = document.getElementById('maxDuration');
    const applyFiltersBtn = document.getElementById('applyFiltersBtn');
    const resetFiltersBtn = document.getElementById('resetFiltersBtn');
    const sortByNumberAscBtn = document.getElementById('sortByNumberAscBtn');
    const sortByNumberDescBtn = document.getElementById('sortByNumberDescBtn');
    const sortByDurationAscBtn = document.getElementById('sortByDurationAscBtn');
    const sortByDurationDescBtn = document.getElementById('sortByDurationDescBtn');
    const sortByDepartureTimeAscBtn = document.getElementById('sortByDepartureTimeAscBtn');
    const sortByDepartureTimeDescBtn = document.getElementById('sortByDepartureTimeDescBtn');
    const resetSortBtn = document.getElementById('resetSortBtn');
    const currentStatusElement = document.getElementById('currentStatus');
    const createTrainBtn = document.getElementById('createTrainBtn');
    const exportTrainsBtn = document.getElementById('exportTrainsBtn');
    const exportForm = document.getElementById('exportForm');
    const exportAlert = document.getElementById('exportAlert');
    const movementTypeTranslations = {};

    document.querySelectorAll('.form-check-input').forEach(input => {
        const id = input.id.replace('filter', '');
        movementTypeTranslations[id] = input.nextElementSibling.querySelector('span').textContent;
    });
    if (createTrainBtn) {
        createTrainBtn.addEventListener('click', () => {
            window.location.href = contextPath + '/trains/create';
        });
    }

    if (currentStatusElement) {
        currentStatusElement.scrollIntoView({behavior: 'smooth'});
    }

    const urlParams = new URLSearchParams(window.location.search);

    function updateUrl(params) {
        window.location.href = `${contextPath}/trains?${params.toString()}`;
    }

    function displayCurrentSearch() {
        const currentSearch = document.getElementById('currentSearch');
        const searchParams = ['qNumber', 'qDeparture', 'qArrival'];
        const activeSearchParams = searchParams.filter(param => urlParams.has(param));
        if (activeSearchParams.length > 0) {
            const searchDescriptions = activeSearchParams.map(param => {
                const paramName = param.replace('q', '');
                const paramValue = urlParams.get(param);
                return `${paramName.charAt(0).toUpperCase() + paramName.slice(1)}: ${paramValue}`;
            });
            currentSearch.innerHTML = `<strong>Search:</strong> Showing trains with ${searchDescriptions.join(', ')}`;
        } else {
            currentSearch.innerHTML = '<strong>Search:</strong> Showing all results';
        }
    }

    searchByNumberBtn.addEventListener('click', () => {
        clearErrors();
        const number = document.getElementById('searchNumber').value;
        if (!number) {
            displayError('searchNumber', 'Please enter a train number.');
            return;
        }
        urlParams.set('qNumber', number);
        urlParams.delete('qDeparture');
        urlParams.delete('qArrival');
        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');
        urlParams.delete('sortBy');
        updateUrl(urlParams);
    });

    searchByDepartureBtn.addEventListener('click', () => {
        clearErrors();
        const departure = document.getElementById('searchDeparture').value;
        if (!departure) {
            displayError('searchDeparture', 'Please enter a departure station.');
            return;
        }
        urlParams.set('qDeparture', departure);
        urlParams.delete('qNumber');
        urlParams.delete('qArrival');
        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');
        urlParams.delete('sortBy');
        updateUrl(urlParams);
    });

    searchByArrivalBtn.addEventListener('click', () => {
        clearErrors();
        const arrival = document.getElementById('searchArrival').value;
        if (!arrival) {
            displayError('searchArrival', 'Please enter an arrival station.');
            return;
        }
        urlParams.set('qArrival', arrival);
        urlParams.delete('qNumber');
        urlParams.delete('qDeparture');
        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');
        urlParams.delete('sortBy');
        updateUrl(urlParams);
    });

    searchByDepartureArrivalBtn.addEventListener('click', () => {
        clearErrors();
        const departure = document.getElementById('searchDeparture').value;
        const arrival = document.getElementById('searchArrival').value;
        if (!departure) {
            displayError('searchDeparture', 'Please enter a departure station.');
            return;
        }
        if (!arrival) {
            displayError('searchArrival', 'Please enter an arrival station.');
            return;
        }
        urlParams.set('qDeparture', departure);
        urlParams.set('qArrival', arrival);
        urlParams.delete('qNumber');
        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');
        urlParams.delete('sortBy');
        updateUrl(urlParams);
    });

    let isDurationChanged = false;

    function displayCurrentFilter() {
        const movementType = urlParams.get('fMovementType');

        if (movementType) {
            const movementTypes = movementType.split('T');

            movementTypes.forEach(type => {
                const checkboxes = document.querySelectorAll('input.form-check-input');

                checkboxes.forEach(checkbox => {
                    if (checkbox.value === type) {
                        checkbox.checked = true;
                    }
                });
            });
        }

        const depTimeFrom = urlParams.get('fDepTimeFrom');
        if (depTimeFrom) {
            filterFromTime.value = depTimeFrom.replace("-", ":");
        }

        const depTimeTo = urlParams.get('fDepTimeTo');
        if (depTimeTo) {
            filterToTime.value = depTimeTo.replace("-", ":");
        }

        const minDurationValue = urlParams.get('fMinDuration');
        if (minDurationValue) {
            minDuration.value = minDurationValue;
            isDurationChanged = true;
        } else {
            minDuration.value = 0;
        }

        const maxDurationValue = urlParams.get('fMaxDuration');
        if (maxDurationValue) {
            maxDuration.value = maxDurationValue;
            isDurationChanged = true;
        } else {
            maxDuration.value = 1440;
        }

        updateDurationLabels();
    }

    minDuration.addEventListener('input', () => {
        if (!isDurationChanged) {
            maxDuration.value = 1440;
            isDurationChanged = true;
        }
    });

    maxDuration.addEventListener('input', () => {
        if (!isDurationChanged) {
            minDuration.value = 0;
            isDurationChanged = true;
        }
    });

    function displayCurrentSort() {
        const currentSort = document.getElementById('currentSort');
        const sortBy = urlParams.get('sortBy');
        if (sortBy) {
            currentSort.innerHTML = `<strong>Sorting:</strong> ${sortBy.replace(/([A-Z])/g, ' $1').trim()}`;
        } else {
            currentSort.innerHTML = '<strong>Sorting:</strong> No sorting applied';
        }
    }

    sortByNumberAscBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "numberAsc");
        updateUrl(urlParams);
    });

    sortByNumberDescBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "numberDesc");
        updateUrl(urlParams);
    });

    sortByDurationAscBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "durationAsc");
        updateUrl(urlParams);
    });

    sortByDurationDescBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "durationDesc");
        updateUrl(urlParams);
    });

    sortByDepartureTimeAscBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "departureTimeAsc");
        updateUrl(urlParams);
    });

    sortByDepartureTimeDescBtn.addEventListener('click', () => {
        urlParams.set('sortBy', "departureTimeDesc");
        updateUrl(urlParams);
    });

    applyFiltersBtn.addEventListener('click', () => {
        const filterFromTimeValue = filterFromTime.value;
        const filterToTimeValue = filterToTime.value;

        let minDurationValue = null;
        let maxDurationValue = null;

        if (isDurationChanged) {
            minDurationValue = minDuration.value;
            maxDurationValue = maxDuration.value;
        }

        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');

        const movementTypes = [];
        document.querySelectorAll('.form-check-input[type="checkbox"]').forEach(checkbox => {
            if (checkbox.checked) {
                movementTypes.push(checkbox.value);
            }
        });

        if (movementTypes.length === 1) {
            urlParams.set('fMovementType', movementTypes[0]);
        } else if (movementTypes.length > 1) {
            urlParams.set('fMovementType', movementTypes.join('T'));
        }

        const formattedFromTime = filterFromTimeValue.replace(":", "-");
        const formattedToTime = filterToTimeValue.replace(":", "-");

        if (filterFromTimeValue) urlParams.set('fDepTimeFrom', formattedFromTime);
        if (filterToTimeValue) urlParams.set('fDepTimeTo', formattedToTime);

        if (minDurationValue) urlParams.set('fMinDuration', minDurationValue);
        if (maxDurationValue) urlParams.set('fMaxDuration', maxDurationValue);

        updateUrl(urlParams);
    });

    resetFiltersBtn.addEventListener('click', () => {
        isDurationChanged = false;
        document.querySelectorAll('.form-check-input[type="checkbox"]').forEach(checkbox => {
            checkbox.checked = false;
        });
        filterFromTime.value = '';
        filterToTime.value = '';
        minDuration.value = '';
        maxDuration.value = '';
        urlParams.delete('fMovementType');
        urlParams.delete('fDepTimeFrom');
        urlParams.delete('fDepTimeTo');
        urlParams.delete('fMinDuration');
        urlParams.delete('fMaxDuration');
        updateUrl(urlParams);
    });

    resetSortBtn.addEventListener('click', () => {
        urlParams.delete('sortBy');
        updateUrl(urlParams);
    });

    function fetchTrains() {
        const urlParams = new URLSearchParams(window.location.search);
        const fetchUrl = `${contextPath}/api/trains?${urlParams.toString()}`;

        fetch(fetchUrl, {
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                const successMessage = response.headers.get('Success-Message');
                if (response.status === 400) {
                    console.error(errorMessage);
                    return;
                }

                if (response.status === 200) {
                    return response.json().then(data => {
                        console.log(successMessage);
                        updateTrainTable(data);
                    });
                }

                if (response.status === 204) {
                    console.log(successMessage);
                    document.getElementById('trainTableBody').innerHTML = `
                <tr>
                    <td colspan="7" class="text-center text-muted no-trains-message">
                        No trains found. Please change your search or filtering criteria.
                    </td>
                </tr>`;
                    return;
                }

                if (response.status === 500 || response.status === 403) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching trains:', error);
            });
    }

    function updateTrainTable(data) {
        const tableBody = document.getElementById('trainTableBody');
        tableBody.innerHTML = '';

        data.forEach(train => {
            const row = document.createElement('tr');
            const duration = parseDuration(train.duration);
            const movementTypeUkr = movementTypeTranslations[train.movementType] || train.movementType;

            const [hours, minutes] = train.departureTime.split(':');
            const formattedDepartureTime = `${hours}:${minutes}`;

            row.innerHTML = `
            <td style="display:none;">${train.id}</td>
            <td class="train-details">${train.number}</td>
            <td class="train-details">${train.departureStation}</td>
            <td class="train-details">${train.arrivalStation}</td>
            <td class="train-details">${movementTypeUkr}</td>
            <td class="train-details">${formattedDepartureTime}</td>
            <td class="train-details">${calculateArrivalTime(train.departureTime, duration)}</td>
            <td class="train-details">${formatDuration(duration)}</td>
            <td class="text-center">
                <a href="${contextPath}/trains/${train.id}/tickets" class="btn btn-sm btn-outline-primary">
                    <i class="bi bi-ticket-perforated"></i> Tickets
                </a>
            </td>
        `;

            const detailsCells = row.querySelectorAll('.train-details');
            detailsCells.forEach(cell => {
                cell.style.cursor = 'pointer';
                cell.addEventListener('click', () => {
                    window.location.href = `${contextPath}/trains/${train.id}`;
                });
            });

            tableBody.appendChild(row);
        });
    }

    displayCurrentSort();
    displayCurrentSearch();
    displayCurrentFilter();
    fetchTrains();

    if (exportForm) {
        exportForm.addEventListener('submit', function (event) {
            event.preventDefault();
            clearErrors();

            const exportFormat = document.getElementById('exportFormat').value;

            const currentUrlParams = new URLSearchParams(window.location.search);

            const exportRequest = buildTrainExportRequestDTO(exportFormat, currentUrlParams);

            fetch(`${contextPath}/api/trains/export`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(exportRequest)
            })
                .then(response => {
                    if (response.status === 204) {
                        alert('Export failed: No data to export.');
                    } else {
                        exportAlert.classList.remove('d-none');
                        exportAlert.classList.add('show');
                        const exportModalElement = document.getElementById('exportModal');
                        const exportModal = bootstrap.Modal.getInstance(exportModalElement);
                        exportModal.hide();
                    }
                })
                .catch(error => {
                    console.error('Error during export:', error);
                    alert('An error occurred while exporting data.');
                });
        });

        function minutesToISODuration(minutes) {
            return `PT${minutes}M`;
        }

        function buildTrainExportRequestDTO(format, urlParams) {
            const queryParams = {};

            if (urlParams.has('qNumber')) {
                queryParams.searchingNumber = urlParams.get('qNumber');
            }
            if (urlParams.has('qDeparture')) {
                queryParams.searchingDepartureStation = urlParams.get('qDeparture');
            }
            if (urlParams.has('qArrival')) {
                queryParams.searchingArrivalStation = urlParams.get('qArrival');
            }
            if (urlParams.has('fMovementType')) {
                const movementTypes = urlParams.get('fMovementType').split('T');
                const reverseMovementTypeTranslations = Object.fromEntries(
                    Object.entries(movementTypeTranslations).map(([key, value]) => [value, key])
                );

                queryParams.filteringMovementTypes = movementTypes.map(type => reverseMovementTypeTranslations[type.charAt(0).toUpperCase() + type.slice(1).toLowerCase()]);
            }
            if (urlParams.has('fDepTimeFrom')) {
                const from = urlParams.get('fDepTimeFrom').replace('-', ':');
                queryParams.filteringFrom = from;
            }
            if (urlParams.has('fDepTimeTo')) {
                const to = urlParams.get('fDepTimeTo').replace('-', ':');
                queryParams.filteringTo = to;
            }
            if (urlParams.has('fMinDuration')) {
                const minDuration = parseInt(urlParams.get('fMinDuration'), 10);
                queryParams.filteringMinDuration = minutesToISODuration(minDuration);
            }
            if (urlParams.has('fMaxDuration')) {
                const maxDuration = parseInt(urlParams.get('fMaxDuration'), 10);
                queryParams.filteringMaxDuration = minutesToISODuration(maxDuration);
            }
            if (urlParams.has('sortBy')) {
                const sortBy = urlParams.get('sortBy');
                switch (sortBy) {
                    case 'numberAsc':
                        queryParams.sortedByTrainNumberAsc = true;
                        break;
                    case 'numberDesc':
                        queryParams.sortedByTrainNumberAsc = false;
                        break;
                    case 'durationAsc':
                        queryParams.sortedByDurationAsc = true;
                        break;
                    case 'durationDesc':
                        queryParams.sortedByDurationAsc = false;
                        break;
                    case 'departureTimeAsc':
                        queryParams.sortedByDepartureTimeAsc = true;
                        break;
                    case 'departureTimeDesc':
                        queryParams.sortedByDepartureTimeAsc = false;
                        break;
                    default:
                        break;
                }
            }

            return {
                format: format,
                queryParams: queryParams
            };
        }
    }
});


