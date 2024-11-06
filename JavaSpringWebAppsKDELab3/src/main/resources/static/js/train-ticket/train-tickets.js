import {clearErrors, displayError, handleError} from '../util/error/error-handler.js';
import {validateCarriageNumber, validatePassportNumber, validatePassengerSurname} from './util/train-ticket-utils.js';


document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.querySelector('base').href.replace(/\/$/, '');
    const isForSpecificTrain = window.location.pathname.includes('/trains/');
    const pathParts = window.location.pathname.split('/');
    const currentTrainId = isForSpecificTrain ? pathParts[pathParts.length - 2] : null;

    const searchPassengerSurname = document.getElementById('searchPassengerSurname');
    const searchPassportNumber = document.getElementById('searchPassportNumber');
    const searchDepartureDate = document.getElementById('searchDepartureDate');
    const searchCarriageNumber = document.getElementById('searchCarriageNumber');
    const searchBySurnameBtn = document.getElementById('searchBySurnameBtn');
    const searchByPassportBtn = document.getElementById('searchByPassportBtn');
    const searchByDateBtn = document.getElementById('searchByDateBtn');
    const searchByDateAndCarriageBtn = document.getElementById('searchByDateAndCarriageBtn');
    const filterDateFrom = document.getElementById('filterDateFrom');
    const filterDateTo = document.getElementById('filterDateTo');
    const applyFiltersBtn = document.getElementById('applyFiltersBtn');
    const resetFiltersBtn = document.getElementById('resetFiltersBtn');
    const currentStatusElement = document.getElementById('currentStatus');
    const createTicketBtn = document.getElementById('createTicketBtn');

    const urlParams = new URLSearchParams(window.location.search);

    if (isForSpecificTrain && currentTrainId) {
        fetch(`${contextPath}/api/trains/${currentTrainId}`, {
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                const errorMessage = response.headers.get('Error-Message');
                if (response.status === 400) {
                    console.error(errorMessage);
                    document.getElementById('trainNumber').textContent = 'Unknown';
                    return;
                }
                if (response.status === 200) {
                    return response.json().then(data => {
                        document.getElementById('trainNumber').textContent = data.number;
                    });
                }
                if (response.status === 500 || response.status === 404) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }
                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching train details:', error);
                document.getElementById('trainNumber').textContent = 'Unknown';
            });
        const goToTrainBtn = document.getElementById('goToTrainBtn');
        goToTrainBtn.addEventListener('click', () => {
            window.location.href = `${contextPath}/trains/${currentTrainId}`;
        });
        createTicketBtn.addEventListener('click', () => {
            const trainNumber = document.getElementById('trainNumber').textContent;
            window.location.href = `${contextPath}/train-tickets/create?trainNumber=${trainNumber}`;
        });
    } else {
        createTicketBtn.addEventListener('click', () => {
            window.location.href = `${contextPath}/train-tickets/create`;
        });
    }


    if (currentStatusElement) {
        currentStatusElement.scrollIntoView({behavior: 'smooth'});
    }

    function updateUrl(params) {
        const url = new URL(window.location.href);
        url.search = params.toString();
        window.location.href = url.toString();
    }

    function displayCurrentSearch() {
        const currentSearch = document.getElementById('currentSearch');
        const searchParams = ['qPassengerSurname', 'qPassengerPassportNumber', 'qDepartureDate', 'qCarriageNumber'];
        const activeSearchParams = searchParams.filter(param => urlParams.has(param));

        if (activeSearchParams.length > 0) {
            const searchDescriptions = activeSearchParams.map(param => {
                const paramName = param.replace('q', '').replace(/([A-Z])/g, ' $1').trim();
                let paramValue = urlParams.get(param);

                if (param === 'qDepartureDate') {
                    paramValue = new Intl.DateTimeFormat('uk-UA', {
                        day: '2-digit',
                        month: 'short',
                        year: 'numeric'
                    }).format(new Date(paramValue));
                }

                return `${paramName}: ${paramValue}`;
            });
            currentSearch.innerHTML = `<strong>Search:</strong> Showing tickets with ${searchDescriptions.join(', ')}`;
        } else {
            currentSearch.innerHTML = '<strong>Search:</strong> Showing all results';
        }
    }

    function displayCurrentFilter() {
        const departureDateFrom = urlParams.get('fDepartureDateFrom');
        if (departureDateFrom) {
            filterDateFrom.value = departureDateFrom;
        }

        const departureDateTo = urlParams.get('fDepartureDateTo');
        if (departureDateTo) {
            filterDateTo.value = departureDateTo;
        }
    }

    searchBySurnameBtn.addEventListener('click', () => {
        clearErrors();
        const surname = searchPassengerSurname.value;
        if (!surname) {
            displayError('searchPassengerSurname', 'Please enter passenger surname');
            return;
        }
        if (!validatePassengerSurname(surname, 'searchPassengerSurname')) {
            return;
        }
        urlParams.set('qPassengerSurname', surname);
        urlParams.delete('qPassengerPassportNumber');
        urlParams.delete('qDepartureDate');
        urlParams.delete('qCarriageNumber');
        urlParams.delete('fDepartureDateFrom');
        urlParams.delete('fDepartureDateTo');
        updateUrl(urlParams);
    });

    searchByPassportBtn.addEventListener('click', () => {
        clearErrors();
        const passport = searchPassportNumber.value;
        if (!passport) {
            displayError('searchPassportNumber', 'Please enter passport number');
            return;
        }
        if (!validatePassportNumber(passport, "searchPassportNumber")) {
            return;
        }
        urlParams.set('qPassengerPassportNumber', passport);
        urlParams.delete('qPassengerSurname');
        urlParams.delete('qDepartureDate');
        urlParams.delete('qCarriageNumber');
        urlParams.delete('fDepartureDateFrom');
        urlParams.delete('fDepartureDateTo');
        updateUrl(urlParams);
    });

    searchByDateBtn.addEventListener('click', () => {
        clearErrors();
        const date = searchDepartureDate.value;
        if (!date) {
            displayError('searchDepartureDate', 'Please select departure date');
            return;
        }
        urlParams.set('qDepartureDate', date);
        urlParams.delete('qPassengerSurname');
        urlParams.delete('qPassengerPassportNumber');
        urlParams.delete('qCarriageNumber');
        urlParams.delete('fDepartureDateFrom');
        urlParams.delete('fDepartureDateTo');
        updateUrl(urlParams);
    });

    if (isForSpecificTrain) {
        searchByDateAndCarriageBtn.addEventListener('click', () => {
            clearErrors();
            const date = searchDepartureDate.value;
            const carriage = searchCarriageNumber.value;

            if (!date) {
                displayError('searchDepartureDate', 'Please select departure date');
                return;
            }
            if (!carriage) {
                displayError('searchCarriageNumber', 'Please enter carriage number');
                return;
            }

            if (!validateCarriageNumber(carriage, 'searchCarriageNumber')) {
                return;
            }

            urlParams.set('qDepartureDate', date);
            urlParams.set('qCarriageNumber', carriage);
            urlParams.delete('qPassengerSurname');
            urlParams.delete('qPassengerPassportNumber');
            urlParams.delete('fDepartureDateFrom');
            urlParams.delete('fDepartureDateTo');
            updateUrl(urlParams);
        });
    }

    applyFiltersBtn.addEventListener('click', () => {
        const dateFrom = filterDateFrom.value;
        const dateTo = filterDateTo.value;

        urlParams.delete('fDepartureDateFrom');
        urlParams.delete('fDepartureDateTo');

        if (dateFrom) urlParams.set('fDepartureDateFrom', dateFrom);
        if (dateTo) urlParams.set('fDepartureDateTo', dateTo);
        if (dateFrom || dateTo) {
            urlParams.delete('qDepartureDate');
        }

        updateUrl(urlParams);
    });

    resetFiltersBtn.addEventListener('click', () => {
        filterDateFrom.value = '';
        filterDateTo.value = '';
        urlParams.delete('fDepartureDateFrom');
        urlParams.delete('fDepartureDateTo');
        updateUrl(urlParams);
    });

    function fetchTickets() {
        const urlParams = new URLSearchParams(window.location.search);
        const fetchUrl = isForSpecificTrain ?
            `${contextPath}/api/trains/${currentTrainId}/tickets?${urlParams.toString()}` :
            `${contextPath}/api/train-tickets?${urlParams.toString()}`;

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
                        updateTicketsTable(data);
                    });
                }

                if (response.status === 204) {
                    console.log(successMessage);
                    document.getElementById('ticketsTableBody').innerHTML = `
                    <tr>
                        <td colspan="${isForSpecificTrain ? 6 : 7}" class="text-center text-muted">
                            No tickets found. Please change your search or filtering criteria.
                        </td>
                    </tr>`;
                    return;
                }

                if (response.status === 500) {
                    handleError(contextPath, errorMessage, response.status);
                    return;
                }

                throw new Error(`Unexpected response status: ${response.status}`);
            })
            .catch(error => {
                console.error('Error fetching tickets:', error);
            });
    }

    function updateTicketsTable(data) {
        const tableBody = document.getElementById('ticketsTableBody');
        tableBody.innerHTML = '';

        data.forEach(ticket => {
            const row = document.createElement('tr');
            let html = '';

            html += `
            <td class="ticket-details">${ticket.passengerSurname}</td>
            <td class="ticket-details">${ticket.passportNumber}</td>
            <td class="ticket-details">${new Intl.DateTimeFormat('uk-UA', {
                day: '2-digit',
                month: 'short',
                year: 'numeric'
            }).format(new Date(ticket.departureDate))}</td>
            <td class="ticket-details">${ticket.carriageNumber}</td>
            <td class="ticket-details">${ticket.seatNumber}</td>
        `;

            if (!isForSpecificTrain) {
                html += `
                <td class="train-link" data-train-number="${ticket.trainNumber}">
                    ${ticket.trainNumber}
                </td>
            `;
            }

            row.innerHTML = html;

            const ticketCells = row.querySelectorAll('.ticket-details');
            ticketCells.forEach(cell => {
                cell.style.cursor = 'pointer';
                cell.addEventListener('click', () => {
                    window.location.href = `${contextPath}/train-tickets/${ticket.id}`;
                });
            });

            const trainCell = row.querySelector('.train-link');
            if (trainCell) {
                trainCell.style.cursor = 'pointer';
                trainCell.addEventListener('click', (e) => {
                    const trainNumber = trainCell.dataset.trainNumber;
                    fetch(`${contextPath}/api/trains?qNumber=${trainNumber}`, {
                        method: 'GET',
                        headers: {
                            'Accept': 'application/json'
                        }
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.length > 0 && data[0].id) {
                                window.location.href = `${contextPath}/trains/${data[0].id}`;
                            } else {
                                console.error('Train not found');
                            }
                        })
                        .catch(error => {
                            console.error('Error fetching train:', error);
                        });
                });
            }

            tableBody.appendChild(row);
        });
    }

    displayCurrentSearch();
    displayCurrentFilter();
    fetchTickets();
});