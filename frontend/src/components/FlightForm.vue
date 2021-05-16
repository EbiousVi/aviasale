<template>
    <div class="form-container">
        <form v-on:submit.prevent="submitForm" method="post">
            <div class="wrap">
                <AutoComplete id="cityFrom" v-on:autocomplete-input="bindCityFrom">City From</AutoComplete>
                <AutoComplete id="cityTo" v-on:autocomplete-input="bindCityTo">City To</AutoComplete>
                <label for="dateThere">Date There
                    <input v-model="form.date" type="date" class="form-control" id="dateThere" placeholder="Date"
                           min="2017-07-16" max="2017-09-15" required>
                </label>
            </div>
            <div class="wrap">
                <label for="numberOfTickets">Number Of Tickets
                    <input v-model.number="form.numberOfTickets" type="number" class="form-control"
                           id="numberOfTickets"
                           placeholder="Number Of Tickets" min="1">
                </label>

                <label for="conditions">Pick conditions :
                    <select v-model="form.conditions" class="form-control" id="conditions">
                        <option value="Economy" selected>Economy</option>
                        <option value="Comfort">Comfort</option>
                        <option value="Business">Business</option>
                    </select>
                </label>
                <button type="submit">Submit</button>
                <button type="button" @click="getBookings">My Bookings</button>
            </div>
        </form>
    </div>
    <h2 v-if="warning">{{message}}</h2>
</template>

<script>
    import AutoComplete from "./AutoComplete";
    import axios from "axios";
    import {validity} from "../utils/validateToken"
    import {bearer} from "../utils/bearer";
    import {refreshTokens} from "../utils/refreshTokens";

    export default {
        name: "FlightForm",
        components: {AutoComplete},
        emits: ["loaded"],
        data() {
            return {
                oneWayFlightsURL: 'http://localhost:6060/one-way/flight',
                bookingsURL: 'http://localhost:6060/bookings',
                form: {
                    cityFrom: String,
                    cityTo: String,
                    date: "2017-09-08",
                    conditions: "Economy",
                    numberOfTickets: 1
                },
                warning: false,
                message: "",
            }
        },
        methods: {
            bindCityFrom(data) {
                this.form.cityFrom = data;
            },
            bindCityTo(data) {
                this.form.cityTo = data;
            },
            repeat() {
                this.submitForm()
            },
            submitForm() {
                this.warning = false;
                this.$emit("loaded", [false, false, false]);
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    axios.post(this.oneWayFlightsURL, this.form, {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        },
                    })
                        .then((response) => {
                            if (response.status === 200) {
                                if (response.data[0].conn === true) {
                                    this.$store.commit("setOneWayConnFlight", response.data);
                                    this.$store.commit("setNumberOfTickets", this.form.numberOfTickets);
                                    this.$emit("loaded", [false, false, true]);
                                } else if (response.data[0].direct === true) {
                                    this.$store.commit("setOneWayDirectFlight", response.data);
                                    this.$store.commit("setNumberOfTickets", this.form.numberOfTickets);
                                    this.$emit("loaded", [false, true, false]);
                                }
                            }
                        })
                        .catch((error) => {
                            if (error.response) {
                                this.warning = true;
                                this.message = error.response.data;
                            }
                        });
                } else {
                    let promise = refreshTokens();
                    promise.then(result => {
                        if (result === 200) {
                            this.repeat();
                        }
                    })
                }
            },
            getBookings() {
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    axios.get(this.bookingsURL, {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        }
                    })
                        .then((response) => {
                            this.$store.commit("setBookingDto", response.data);
                            this.$router.push('/bookings');
                        })
                        .catch(() => {
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
    }
</script>

<style scoped>
    .wrap {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-evenly;
        align-items: baseline;
    }

    .form-container {
        margin-top: 75px;
        border: 2px solid #150202;
        border-radius: 3px;
        background-color: #cccccc;
    }

    label, input, select {
        margin: 0;
        outline: none;
        display: block;
        width: 150px;
        color: black;
        padding: 0.5rem 1rem;
        border-radius: 3px;
        font-size: 1rem;
    }

    button {
        border-radius: 3px;
        width: 80px;
        height: 50px;
        display: block;
        color: black;
        font-size: 1rem;
        align-self: center;
    }

    button:active {
        background-color: #00ffff;
        animation-delay: inherit;
    }

    h2 {
        text-align: center;
        color: red;
    }
</style>