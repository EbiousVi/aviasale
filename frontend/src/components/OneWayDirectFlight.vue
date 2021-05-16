<template>
    <div v-for="(fl,index) of prepareFlights" v-bind:key="fl">
        <table v-bind:class="index">
            <thead>
            <tr>
                <td colspan="3" bgcolor="#00ffff">{{index + 1}}</td>
            </tr>
            </thead>
            <tr>
                <td>
                    <span>Departure Date</span><br>
                    {{fl.flight.departureDate}}
                </td>
                <td>
                    <span>FlightId</span>
                    {{fl.flight.flightId}}<br>
                    <span>Free seats</span>
                    {{freeSeats.get(fl.flight.flightId)}}
                </td>
                <td>
                    <span>Arrival Date</span><br>
                    {{fl.flight.arrivalDate}}
                </td>
            </tr>
            <tr>
                <td>
                    <span>City From</span><br>
                    {{fl.airportFrom.city}}
                </td>
                <td>
                    <span>Aircraft model</span><br>
                    {{fl.flight.aircraft}}
                </td>
                <td>
                    <span>City To</span><br>
                    {{fl.airportTo.city}}
                </td>
            </tr>
            <tr>
                <td>
                    <span>Airport From Name</span><br>
                    {{fl.airportFrom.airportName}}
                </td>
                <td>
                    <span>Flight Number </span><br>
                    {{fl.flight.flightNo}}
                </td>
                <td>
                    <span>Airport To Name</span><br>
                    {{fl.airportTo.airportName}}
                </td>
            </tr>
            <tr>
                <td>
                    <span>Airport Code From</span><br>
                    {{fl.flight.airportFrom}}
                </td>
                <td>
                    <span>Price</span><br>
                    {{fl.price.value.toFixed(2)}}
                </td>
                <td>
                    <span>Airport Code To</span><br>
                    {{fl.flight.airportTo}}
                </td>
            </tr>
            <tr v-if="button">
                <td colspan="3">
                    <button type="button" v-on:click="booking(index, fl.price)">Booking</button>
                </td>
            </tr>
        </table>
    </div>
</template>

<script>

    import {validity} from "../utils/validateToken";
    import axios from "axios";
    import {bearer} from "../utils/bearer";
    import {refreshTokens} from "../utils/refreshTokens";

    export default {
        name: "OneWayFlight",
        emits: ["booking", "conn"],
        data() {
            return {
                button: true,
                table: true,
                submitURL: 'http://localhost:6060/prepare-booking',
                freeSeats: new Map(),
            }
        },
        mounted() {
            let flights = this.$store.getters.getOneWayDirectFlight;
            for (let i = 0; i < flights.length; i++) {
                this.getFreeSeats(flights[i].flight.flightId, flights[i].flight.aircraft)
            }
        },
        computed: {
            prepareFlights() {
                return this.$store.getters.getOneWayDirectFlight;
            },
        },
        methods: {
            booking(i, price) {
                this.$store.commit("setPrice", price);
                this.prepareFlights.splice(i + 1, this.prepareFlights.length);
                this.prepareFlights.splice(0, i);
                this.$emit("booking", true);
                this.$emit("conn", false);
                this.button = false;
                this.table = false;
            },
            leaveSelectedFlight(i) {
                if (i === 1) {
                    return;
                }
                this.prepareFlights.splice(i + 1, this.prepareFlights.length);
                this.prepareFlights.splice(0, i);
            },
            async getFreeSeats(flightId, aircraft) {
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    await axios.get("http://localhost:6060/freeSeats", {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        },
                        params: {
                            flightId: flightId,
                            aircraft: aircraft
                        }
                    })
                        .then(response => {
                            if (response.status === 200) {
                                this.freeSeats.set(flightId, response.data);
                            }
                        });
                } else {
                    let promise = refreshTokens();
                    promise.then(result => {
                        if (result === 200) {
                            console.log(result)
                        }
                    })
                }
            },
        },

    }
</script>

<style scoped>
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