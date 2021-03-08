<template>
    <div class="card" v-for="(f, i) of prepareFlights" v-bind:key="f">
        <div class="index">{{i + 1}}</div>
        <div class="main">
            <div class="main-left">
                <p> {{f.flight1.departureDate}}</p>
                <p>{{f.flight1.airportFrom}}</p>
                <p><span class="span-helper">Price 1</span>
                    {{f.price1.value}}</p>
            </div>
            <div class="main-mid">
                <p><span class="span-helper">Total Duration</span>
                    {{transfer(f.flight1.departureDate, f.flight2.arrivalDate)}}</p>
                <p><span class="span-helper">Total Price</span>
                    {{f.price1.value + f.price2.value}}</p>
                <p> ---->>>> </p>
            </div>
            <div class="main-right">
                <p> {{f.flight2.arrivalDate}}</p>
                <p>{{f.flight2.airportTo}}</p>
                <p><span class="span-helper">Price 2</span>
                    {{f.price2.value}}</p>
            </div>
        </div>
        <div class="flight-1">
            <div class="f-header">
                <span> {{f.flight1.flightNo}} </span>
                <span>
                    <span class="span-helper">Travel time</span>
                    {{transfer(f.flight1.departureDate, f.flight1.arrivalDate)}}
                </span>
                <span> <span class="span-helper">Aircraft</span> {{f.flight1.aircraft}} </span>
            </div>
            <div class="f-content">
                <p>{{f.flight1.departureDate}} {{f.airFrom1.airportCode}}
                    {{f.airFrom1.airportName}} {{f.airFrom1.city}}</p>
                <p>{{f.flight1.arrivalDate}} {{f.airTo1.airportCode}}
                    {{f.airTo1.airportName}} {{f.airTo1.city}}</p>
                <details @click="checkFreeSeats(f.flight1.flightId, f.flight1.aircraft)">
                    <summary>Details</summary>
                    <p><span class="span-helper">flightId</span> {{f.flight1.flightId}} </p>
                    <p><span class="span-helper">Free seats:</span> {{freeSeats.get(f.flight1.flightId)}} </p>
                </details>
            </div>
        </div>
        <div class="transfer">
            <p>Transfer in {{ f.airTo1.city }}({{f.airTo1.airportCode}}),
                waiting time {{transfer(f.flight1.arrivalDate, f.flight2.departureDate)}}</p></div>
        <div class="flight-2">
            <div class="f-header">
                <span> {{f.flight2.flightNo}} </span>
                <span>
                    <span class="span-helper">Travel time</span>
                    {{transfer(f.flight2.departureDate, f.flight2.arrivalDate)}}
                </span>
                <span> <span class="span-helper">Aircraft</span> {{f.flight2.aircraft}} </span>
            </div>
            <div class="f-content">
                <p>{{f.flight2.departureDate}} {{f.airFrom2.airportCode}}
                    {{f.airFrom2.airportName}} {{f.airFrom2.city}}</p>
                <p>{{f.flight2.arrivalDate}} {{f.airTo2.airportCode}}
                    {{f.airTo2.airportName}} {{f.airTo2.city}}</p>
                <details @click="checkFreeSeats(f.flight2.flightId, f.flight2.aircraft)">
                    <summary>Details</summary>
                    <p><span class="span-helper">flightId</span> {{f.flight2.flightId}} </p>
                    <p><span class="span-helper">Free seats:</span> {{freeSeats.get(f.flight2.flightId)}} </p>
                </details>
            </div>
        </div>
        <div v-show="button">
            <button @click="booking(i, f.price1, f.price2)" type="button">Booking</button>
        </div>
    </div>
</template>

<script>
    import {validity} from "../utils/validateToken";
    import axios from "axios";
    import {bearer} from "../utils/bearer";
    import {refreshTokens} from "../utils/refreshTokens";

    export default {
        name: "Flights",
        emits: ["booking", "conn"],
        computed: {
            prepareFlights() {
                return this.$store.getters.getConnectingFlight;
            },
        },
        data() {
            return {
                button: true,
                submitURL: 'http://localhost:6060/prepare-booking',
                freeSeats: new Map(),
            }
        },
        methods: {
            transfer(x, y) {
                const date1 = Date.parse(x);
                const date2 = Date.parse(y);
                const diff = date2 - date1;
                const d = Math.floor((diff / (1000 * 60 * 60 * 24)));
                const m = Math.floor((diff / (1000 * 60)) % 60);
                const h = Math.floor((diff / (1000 * 60 * 60)) % 24);
                if (d > 0) {
                    return d + "D " + h + "H " + m + "m";
                } else {
                    return h + "H " + m + "m";
                }
            },
            async checkFreeSeats(flightId, aircraft) {
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
                            console.log(response.status);
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
            setPrices(price1, price2) {
                let prices = [];
                prices.push(price1);
                prices.push(price2);
                this.$store.commit("setPrices", prices);
            },
            booking(i, price1, price2) {
                this.setPrices(price1, price2);
                this.prepareFlights.splice(i + 1, this.prepareFlights.length);
                this.prepareFlights.splice(0, i);
                this.button = false;
                this.$emit("booking", true);
                this.$emit("conn", true);
            },
        }
    }
</script>

<style scoped>
    .index {
        text-align: center;
        background-color: cyan;
    }

    .main-left {
        border: none;
    }

    .main-right {
        border: none;
    }

    .main-mid {
        border: none;
    }

    .main {
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;
    }

    .f-header {
        display: flex;
        justify-content: space-evenly;
        flex-wrap: wrap;
        border: none;
        background-color: #cccccc;
    }

    .f-content {
        align-items: center;
        border: 1px solid black;
        border-radius: 2px;
        margin: 10px 100px;
        font-size: 11pt;
    }

    .transfer {
        background-color: #ffbf00;
    }

    .card {
        display: flex;
        flex-direction: column;
        border: 2px solid black;
    }

    .span-helper {
        font-size: 10pt;
        font-weight: normal;
    }

    div {
        padding: 1px 2px;
        text-align: center;
        border-radius: 3px;
        background-color: gainsboro;
        font-weight: bold;
        font-size: 12pt;
        margin-bottom: 2px;
    }

    p {
        margin: 5px;
        padding: 0;
        font-family: sans-serif;
    }

    button {
        position: relative;
        border-radius: 2px;
        background-color: whitesmoke;
        color: black;
        width: 180px;
        text-align: center;
        transition-duration: 0.4s;
        text-decoration: none;
        overflow: hidden;
        cursor: pointer;
    }

    button:after {
        content: "";
        background: cyan;
        display: block;
        position: absolute;
        padding-top: 300%;
        padding-left: 350%;
        margin-left: -20px !important;
        margin-top: -120%;
        opacity: 0;
        transition: all 0.8s
    }

    button:active:after {
        padding: 0;
        margin: 0;
        opacity: 1;
        transition: 0s
    }

    details > summary {
        background-color: gray;
        border: none;
        box-shadow: 1px 1px 2px #bbbbbb;
        cursor: pointer;
    }

    details > p {
        background-color: #eeeeee;
        margin: 0;
        box-shadow: 1px 1px 2px #bbbbbb;
    }
</style>