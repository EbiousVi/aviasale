export default {
    state() {
        return {
            flights: [{
                show: false,
                interval: false,
                flight: {
                    flightId: 0,
                    flightNo: "",
                    departureDate: new Date(),
                    arrivalDate: new Date(),
                    airportFrom: "",
                    airportTo: "",
                    status: "",
                    aircraft: "",
                    actualArrival: new Date(),
                    actualDeparture: new Date(),
                },
                airportFrom: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    point: "",
                    timeZone: "",
                },
                airportTo: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    point: "",
                    timeZone: "",
                },
                price: {
                    flightId: 0,
                    value: 0,
                }
            }],
            numberOfTickets: 0,
            price: {
                flightId: 0,
                value: 0,
            },
        }
    },
    mutations: {
        setFlights(state, payload) {
            state.flights = payload;
        },
        setNumberOfTickets(state, payload) {
            state.numberOfTickets = payload;
        },
        setPrice(state, payload) {
            state.price = payload;
        }
    },
    getters: {
        getFlights(state) {
            return state.flights;
        },
        getNumberOfTickets(state) {
            return state.numberOfTickets;
        },
        getInterval(state) {
            if (state.flights[0].interval === undefined) {
                console.log("PIDARICQ");
                return false;
            }
            return state.flights[0].interval;
        },
        getPrice(state) {
            return state.price;
        }
    }
}