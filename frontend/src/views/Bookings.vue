<template>
    <div v-for="(b, index) of getBookingDto" v-bind:key="b">
        <table v-if="b.show">
            <tr class="head">
                <td>
                    {{index + 1}}
                </td>
                <td>
                    <span>BookRef</span><br>
                    {{b.booking.bookRef}}
                </td>
                <td>
                    <span>Arrival Date</span><br>
                    {{b.booking.bookDate}}
                </td>
                <td>
                    <span>Total Amount</span><br>
                    {{b.booking.totalAmount.toFixed(2)}}
                </td>
                <td>
                    <button @click="b.show = false, cancelBooking(b.booking.bookRef)">Cancel Booking</button>
                </td>
            </tr>
            <tr v-for="(t, i) of b.tickets" v-bind:key="t" v-show="remove !== t.ticketNumber">
                <td>
                    {{index+1}}.{{i + 1}}
                </td>
                <td>
                    <span>TicketNo</span><br>
                    {{t.ticketNumber}}
                </td>
                <td>
                    <span>Passenger Name</span><br>
                    {{t.passengerName}}
                </td>
                <td>
                    <span>Contacts</span><br>
                    email: {{JSON.parse(t.contactDataJsonb).email}}<br>
                    phone: {{JSON.parse(t.contactDataJsonb).phone}}
                </td>
                <td>
                    <button @click="removePassenger(t.ticketNumber)">Drop Passenger</button>
                </td>
            </tr>
        </table>
    </div>
    <button @click="backToMain">На главную</button>
</template>

<script>
    import {axiosDelete} from "../utils/axiosDelete";

    export default {
        name: "Reservation",
        emits: ["loaded"],
        computed: {
            getBookingDto() {
                return this.$store.getters.getBookingDto;
            }
        },
        data() {
            return {
                remove: "",
            }
        },
        methods: {
            backToMain() {
                this.$emit("loaded", [false, false]);
                this.$router.push('/main');
            },
            cancelBooking(bookRef) {
                let URL = "http://localhost:6060/delete-booking/".concat(bookRef);
                axiosDelete("Booking canceled!", URL)
            },
            removePassenger(ticketNumber) {
                let URL = "http://localhost:6060/remove-passenger/".concat(ticketNumber);
                axiosDelete("Passenger removed!", URL);
                this.remove = ticketNumber;
            }
        },
    }
</script>

<style scoped>
    .head {
        background-color: #00ffff;
    }

    h1 {
        text-align: center;
        color: red;
    }

    h3 {
        text-align: center;
        color: #ffbf00;
    }

    table {
        table-layout: fixed;
        width: 100%;
        border-collapse: collapse;
        border: 3px solid black;
        font-size: 12pt;
        font-weight: bold;
        padding: 15px 20px;
    }

    thead {
        height: 25%;
    }

    tr {
        padding: 5px 10px;
    }

    td {
        padding: 5px 10px;
        border: 1px solid black;
        text-align: center;
    }

    span {
        font-size: 10pt;
        font-style: italic;
        font-weight: normal;
    }

    button:active {
        background-color: #00ffff;
        animation-delay: inherit;
    }
</style>