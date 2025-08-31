// Универсальный API сервис
const apiService = {
    config: null,

    async init() {
        if (!this.config) {
            try {
                const configResponse = await fetch('/api-config.json');
                this.config = await configResponse.json();
            } catch (error) {
                console.error('Error loading API configuration:', error);
                throw error;
            }
        }
    },

    async call(endpointKey, params = {}, data = null) {
        await this.init();

        const endpoint = this.config.endpoints[endpointKey];
        if (!endpoint) {
            throw new Error(`Endpoint ${endpointKey} not found in configuration`);
        }

        // Подставляем параметры в URL
        let url = endpoint.url;
        Object.keys(params).forEach(key => {
            url = url.replace(`{${key}}`, params[key]);
        });

        const options = {
            method: endpoint.method,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(url, options);

        if (!response.ok) {
            try {
                const error = await response.json();
                throw error;
            } catch {
                const errorText = await response.text();
                throw new Error('HTTP ${response.status}: ${errorText || response.statusText}');
            }
        }

        if (response.status === 204 || response.headers.get('Content-Length') === '0') {
            return null;
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        } else {
            const text = await response.text();
            return text ? JSON.parse(text) : null;
        }
    }
};

// Сервис для работы с select элементами
const selectService = {
    fillAuthors(selectElement, authors, selectedId = null) {
        selectElement.innerHTML = '';
        authors.forEach(author => {
            const option = document.createElement("option");
            option.text = author.fullName;
            option.value = author.id;
            if (selectedId && author.id === selectedId) {
                option.selected = true;
            }
            selectElement.add(option);
        });
    },

    fillGenres(selectElement, genres, selectedId = null) {
        selectElement.innerHTML = '';
        genres.forEach(genre => {
            const option = document.createElement("option");
            option.text = genre.name;
            option.value = genre.id;
            if (selectedId && genre.id === selectedId) {
                option.selected = true;
            }
            selectElement.add(option);
        });
    },

    async loadReferenceData() {
        const [authors, genres] = await Promise.all([
            apiService.call('getAuthors'),
            apiService.call('getGenres')
        ]);
        return { authors, genres };
    }
};

// Утилиты для работы с URL параметрами
const urlUtils = {
    getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },

    getAllQueryParams() {
        const urlParams = new URLSearchParams(window.location.search);
        const params = {};
        for (const [key, value] of urlParams) {
            params[key] = value;
        }
        return params;
    }
};

// Сервис для работы с DOM элементами
const domService = {
    clearContainer(container) {
        while (container.firstChild) {
            container.removeChild(container.lastChild);
        }
    }
};
