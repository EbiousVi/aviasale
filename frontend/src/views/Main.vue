<template>
    <div class="main">
        <flight-form v-on:loaded="formHandler"></flight-form>
        <one-way-flight v-if="oneWayDirectFlightDto" v-on:booking="bookingHandler"
                        v-on:conn="isConnectionFlight"></one-way-flight>
        <connecting-flight v-if="connectingFlight" v-on:booking="bookingHandler"
                           v-on:conn="isConnectionFlight"></connecting-flight>
        <booking-form v-if="booking" v-bind:conn="conn"></booking-form>
    </div>
</template>

<script>
    import FlightForm from "../components/FlightForm";
    import BookingForm from "../components/BookingForm";
    import ConnectingFlight from "../components/OneWayConnFlight";
    import OneWayFlight from "../components/OneWayDirectFlight";

    export default {
        name: 'App',
        components: {
            FlightForm,
            OneWayFlight,
            ConnectingFlight,
            BookingForm,
        },
        data() {
            return {
                oneWayDirectFlightDto: false,
                connectingFlight: false,
                booking: false,
                conn: false,
            }
        },
        methods: {
            formHandler(data) {
                console.log(data)
                this.booking = data[0];
                this.oneWayDirectFlightDto = data[1];
                this.connectingFlight = data[2];
            },
            bookingHandler(data) {
                this.booking = data;
            },
            isConnectionFlight(data) {
                this.conn = data;
            }
        },
    }
</script>

<style>
    main {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        margin-left: 10%;
        margin-right: 10%;
    }
</style>