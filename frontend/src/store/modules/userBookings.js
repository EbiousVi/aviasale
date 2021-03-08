export default {
    state() {
        return {
            bookingDto: {
                show: true,
                booking: {
                    bookRef: "",
                    bookDate: Date,
                    totalAmount: 0,
                },
                tickets: [{
                    ticketNumber: '',
                    bookRef: '',
                    passengerId: '',
                    passengerName: '',
                    contactDataJsonb: '',
                }],
            }
        }
    },
    mutations: {
        setBookingDto(state, payload) {
            state.bookingDto = payload;
        }
    },
    getters: {
        getBookingDto(state) {
            return state.bookingDto;
        }
    }

}