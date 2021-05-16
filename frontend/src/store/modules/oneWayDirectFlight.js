export default {
    state() {
        return {
            oneWayDirectFlight: [{
                flight: {
                    flightId: 0,
                    flightNo: "",
                    departureDate: new Date(),
                    arrivalDate: new Date(),
                    airportFrom: "",
                    airportTo: "",
                    status: "",
                    aircraft: "",
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
            price: {},
        }
    },
    mutations: {
        setOneWayDirectFlight(state, payload) {
            state.oneWayDirectFlight = payload;
        },
        setNumberOfTickets(state, payload) {
            state.numberOfTickets = payload;
        },
        setPrice(state, payload) {
            state.price = payload;
        }
    },
    getters: {
        getOneWayDirectFlight(state) {
            return state.oneWayDirectFlight;
        },
        getNumberOfTickets(state) {
            return state.numberOfTickets;
        },
        getPrice(state) {
            let prices = [];
            prices.push(state.price)
            return prices;
        }
    }
}