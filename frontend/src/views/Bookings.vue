<template>
    <h2>My Bookings</h2>
    <div class="main" v-for="(b, index) of getBookingDto" v-bind:key="b">
        <div class="container">
            <div class="blocks" id="booking">
                <div class="index">
                    <div class="block_title"></div>
                    <div class="circle">{{index + 1}}</div>
                </div>
                <div class="block">
                    <div class="block_title">BookRef</div>
                    <div class="block_text">{{b.booking.bookRef}}</div>
                </div>
                <div class="block">
                    <div class="block_title">Created Time</div>
                    <div class="block_text">{{b.booking.bookDate}}</div>
                </div>
                <div class="block">
                    <div class="block_title">Total Amount</div>
                    <div class="block_text">{{b.booking.totalAmount.toFixed(2)}}</div>
                </div>
                <div class="block">
                    <div class="block_title"></div>
                    <div class="block_text">
                        <button @click="cancelBooking(getBookingDto, index, b.booking.bookRef)">Cancel Booking</button>
                    </div>
                </div>
            </div>

            <div class="blocks" v-for="(t, i) of b.tickets" v-bind:key="t">
                <div class="index">
                    <div class="block_title"></div>
                    <div class="circle">{{index+1}}.{{i + 1}}</div>
                </div>
                <div class="block">
                    <div class="block_title">TicketNo</div>
                    <div class="block_text">{{t.ticketNumber}}</div>
                </div>
                <div class="block">
                    <div class="block_title">Passenger Name</div>
                    <div class="block_text">{{t.passengerName}}</div>
                </div>
                <div class="block">
                    <div class="block_title"><span>Contacts</span></div>
                    <div class="block_text">
                        <span>email:</span> {{JSON.parse(t.contactDataJsonb).email}}<br>
                        <span>phone:</span> {{JSON.parse(t.contactDataJsonb).phone}}
                    </div>
                </div>
                <div class="block">
                    <div class="block_title"></div>
                    <div class="block_text">
                        <button @click="removePassenger(b.tickets, i, t.ticketNumber)">Drop Passenger</button>
                    </div>
                </div>
                <div class="blocks" v-if="loaded">
                    <div class="index">
                        <div class="block_title"></div>
                        <div class="circle-1">info</div>
                    </div>
                    <div class="block">
                        <div class="block_title">Airport From</div>
                        <div class="block_text">{{oneWayFlightDto.get(t.ticketNumber).airportFrom}}</div>
                    </div>
                    <div class="block">
                        <div class="block_title">Airport To</div>
                        <div class="block_text">{{oneWayFlightDto.get(t.ticketNumber).airportTo}}</div>
                    </div>
                    <div class="block">
                        <div class="block_title">Departure Date</div>
                        <div class="block_text">{{oneWayFlightDto.get(t.ticketNumber).departureDate}}</div>
                    </div>
                    <div class="block">
                        <div class="block_title">Arrival Date</div>
                        <div class="block_text">{{oneWayFlightDto.get(t.ticketNumber).arrivalDate}}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <button @click="backToMain">На главную</button>
</template>

<script>
    import {axiosDelete} from "../utils/axiosDelete";
    import axios from "axios";
    import {validity} from "../utils/validateToken";
    import {bearer} from "../utils/bearer";
    import {refreshTokens} from "../utils/refreshTokens";

    export default {
        name: "Reservation",
        emits: ["loaded"],
        async mounted() {
            const store = this.$store.getters.getBookingDto;
            for (let i = 0; i < store.length; i++) {
                for (let j = 0; j < store[i].tickets.length; j++) {
                    await this.getDetails(store[i].tickets[j].ticketNumber);
                }
            }
            this.loaded = true;
        },
        computed: {
            getBookingDto() {
                return this.$store.getters.getBookingDto;
            }
        }
        ,
        data() {
            return {
                loaded: false,
                flights: new Map(),
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
            }
        }
        ,
        methods: {
            backToMain() {
                this.$emit("loaded", [false, false]);
                this.$router.push('/main');
            }
            ,
            cancelBooking(bookings, index, bookRef) {
                let URL = "http://localhost:6060/delete-booking/".concat(bookRef);
                axiosDelete("Booking canceled!", URL)
                bookings.splice(index, 1);
            }
            ,
            removePassenger(tickets, index, ticketNumber) {
                let URL = "http://localhost:6060/remove-passenger/".concat(ticketNumber);
                axiosDelete("Passenger removed!", URL);
                tickets.splice(index, 1);
            }
            ,
            async getDetails(ticketNo) {
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    await axios
                        .get("http://localhost:6060/booking/details", {
                            params: {
                                ticketNo: ticketNo,
                            },
                            headers: {
                                'Access-Control-Allow-Origin': 'http://localhost:8080',
                                'Authorization': bearer(accessToken)
                            },
                        })
                        .then(response => {
                            this.flights.set(ticketNo, response.data);
                        });
                } else {
                    let promise = refreshTokens();
                    promise.then(result => {
                        if (result === 200) {
                            this.repeat();
                        }
                    })
                }
            }
        }
        ,
    }
</script>

<style scoped>

    #booking {
        background-color: cyan;
    }

    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .blocks {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        width: 100%;
    }

    .block {
        flex-grow: 1;
        align-self: center;
        width: calc(100% * (1 / 5) - 2px);
        text-align: center;
        font-size: 12pt;
        font-weight: bold;
        border-right: 2px solid black;
    }

    .index {
        margin-right: 10px;
        align-self: center;
        width: auto;
    }


    .circle {
        background: #12B6F9;
        padding: 15px;
        margin: 5px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 50%;
        width: 10px;
        height: 10px;
        max-width: 10px;
        max-height: 10px;
    }

    .circle-1 {
        background: burlywood;
        padding: 15px;
        margin: 5px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 50%;
        width: 10px;
        height: 10px;
        max-width: 10px;
        max-height: 10px;
    }

    .block_title {
        font-size: 10pt;
        font-style: italic;
        font-weight: normal;
    }

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

    span {
        font-size: 10pt;
        font-style: italic;
        font-weight: normal;
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


    button:active {
        background-color: #00ffff;
        animation-delay: inherit;
    }
</style>