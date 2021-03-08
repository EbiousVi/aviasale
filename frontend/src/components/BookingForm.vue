<template>
    <div class="forms">
        <form v-on:submit.prevent="submitForm">
            <div class="passenger" v-for="(i, index) in passengers" v-bind:key="i">
                <div class="p">
                    <h2> Passenger : {{index + 1}}</h2>
                </div>
                <div class="p">
                    <label for="passengerName">Passenger Name
                        <input v-model="i.passengerName" id="passengerName" type="text"
                               name="passengerName"
                               placeholder="Passenger Name"
                               autocomplete="off">
                    </label>
                    <label for="passengerId">Passport
                        <input v-model="i.passengerId" id="passengerId" type="text" name="passengerId"
                               placeholder="Passport"
                               autocomplete="off">
                    </label>
                </div>
                <div class="p">
                    <label for="phone">Phone
                        <input v-model="i.phone" id="phone" type="tel" name="phone" placeholder="phone"
                               autocomplete="off">
                    </label>
                    <label for="email">Email
                        <input v-model="i.email" id="email" type="email" name="email" placeholder="email"
                               autocomplete="off">
                    </label>
                </div>
            </div>
            <button type="submit" name="button-submit">Confirm</button>
        </form>
    </div>
</template>

<script>
    import axios from "axios";
    import {validity} from "../utils/validateToken";
    import {refreshTokens} from "../utils/refreshTokens";
    import {bearer} from "../utils/bearer";

    export default {
        name: "BookingForm",
        emits: ["loaded"],
        props: {
            conn: Boolean,
        },
        data() {
            return {
                passengers: [],
                prices: [],
                bookingURL: "http://localhost:6060/booking",
                priceURL: "http://localhost:6060/prepare-booking",
                error: '',
            }
        },
        mounted() {
            for (let i = 0; i < this.getNumberOfTickets; i++) {
                let passenger = {
                    passengerName: '',
                    passengerId: '',
                    phone: '',
                    email: ''
                }
                this.passengers.push(passenger);
            }
        },
        computed: {
            getNumberOfTickets() {
                return this.$store.getters.getNumberOfTickets;
            }
        },

        methods: {
            repeat() {
                this.submitForm()
            },
            setPrice() {
                if (this.conn) {
                    console.log(this.$store.getters.getPrices + `DOUBLE PRICE`)
                    this.prices = this.$store.getters.getPrices;
                } else {
                    console.log(this.$store.getters.getPrice + `SOLO PRICE`)
                    this.prices = this.$store.getters.getPrice;
                }
            },
            sendPriceInfo() {
                this.setPrice();
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    axios.post(this.priceURL, this.prices, {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        }
                    })
                }
            },
            submitForm() {
                this.sendPriceInfo();
                const accessToken = localStorage.getItem("accessToken");
                const isValid = validity(accessToken);
                if (isValid) {
                    axios.post(this.bookingURL, this.passengers, {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        }
                    })
                        .then((response) => {
                            this.$store.commit("setBookingDto", response.data);
                            this.$router.push('/bookings');
                            this.$emit("loaded", [false, false]);
                        })
                        .catch((e) => {
                            console.log(e + `Error BookingForm`)
                        }).finally(() => {

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
        }
    }
</script>

<style scoped>
    .passenger {
        display: flex;
        border: 2px solid #150202;
    }

    .p {
        display: flex;
        flex-wrap: wrap;
    }

    form {
        border-radius: 10px;
        box-shadow: 0 2px 4px #ccc;
        letter-spacing: 1px;
        background-color: rgb(245, 245, 245);
    }

    input {
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
        font-size: large;
        width: 100%;
        height: 28px;
        border-radius: 5px;
        color: black;
        cursor: pointer;
        transition: 0.3s;
    }

    button:active {
        background-color: #00ffff;
        animation-delay: inherit;
    }
</style>