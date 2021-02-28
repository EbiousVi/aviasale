import {createStore, createLogger} from 'vuex'
import flightData from "./modules/findFlightsData";
import jwtTokens from "./modules/jwtTokens";
import bookingData from "./modules/userBookings";
import flightsDto from "./modules/flightsDto";

export const store = createStore({
    plugins: [createLogger()],
    modules: {
        flightData,
        jwtTokens,
        bookingData,
        flightsDto
    }
})