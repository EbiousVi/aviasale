export default {
    state() {
        return {
            oneWayConnFlight: [{
                flight1: {
                    flightId: 0,
                    flightNo: "",
                    departureDate: new Date(),
                    arrivalDate: new Date(),
                    airportFrom: "",
                    airportTo: "",
                    status: "",
                    aircraft: ""
                },
                flight2: {
                    flightId: 0,
                    flightNo: "",
                    departureDate: new Date(),
                    arrivalDate: new Date(),
                    airportFrom: "",
                    airportTo: "",
                    status: "",
                    aircraft: ""
                },
                airFrom1: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    timeZone: ""
                },
                airTo1: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    timeZone: ""
                },
                airFrom2: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    timeZone: ""
                },
                airTo2: {
                    airportCode: "",
                    airportName: "",
                    city: "",
                    timeZone: ""
                },
                price1: {
                    flightId: 0,
                    value: 0.0
                },
                price2: {
                    flightId: 0,
                    value: 0.0
                }
            }],
            prices: [],
        }
    },
    mutations: {
        setOneWayConnFlight(state, payload) {
            state.oneWayConnFlight = payload;
        },
        setPrices(state, payload) {
            state.prices = payload;
        }
    },
    getters: {
        getOneWayConnFlight(state) {
            return state.oneWayConnFlight;
        },
        getPrices(state) {
            return state.prices;
        }
    }
}