import {createStore, createLogger} from 'vuex'
import oneWayFlight from "./modules/oneWayDirectFlight";
import connectingFlight from "./modules/connectingFlight";
import userBookings from "./modules/userBookings";

export const store = createStore({
    plugins: [createLogger()],
    modules: {
        oneWayFlight,
        userBookings,
        connectingFlight
    }
})