export default {
    state() {
        return {
            oneWayFlight: [{
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
        setOneWayFlight(state, payload) {
            state.oneWayFlight = payload;
        },
        setNumberOfTickets(state, payload) {
            state.numberOfTickets = payload;
        },
        setPrice(state, payload) {
            state.price = payload;
        }
    },
    getters: {
        getOneWayFlight(state) {
            return state.oneWayFlight;
        },
        getNumberOfTickets(state) {
            return state.numberOfTickets;
        },
        getInterval(state) {
            if (state.oneWayFlight[0].interval === undefined) {
                return false;
            }
            return state.oneWayFlight[0].interval;
        },
        getPrice(state) {
            let prices = [];
            prices.push(state.price)
            return prices;
        }
    }
}