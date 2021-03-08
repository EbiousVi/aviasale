export default {
    state() {
        return {
            connectingFlight: [{
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
        setConnectingFlight(state, payload) {
            state.connectingFlight = payload;
        },
        setPrices(state, payload) {
            state.prices = payload;
        }
    },
    getters: {
        getConnectingFlight(state) {
            return state.connectingFlight;
        },
        getPrices(state) {
            return state.prices;
        }
    }
}