export default {
    state() {
        return {
            BookingDto: {
                show : true,
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
            state.BookingDto = payload;
        }
    },
    getters: {
        getBookingDto(state) {
            return state.BookingDto;
        }
    }

}