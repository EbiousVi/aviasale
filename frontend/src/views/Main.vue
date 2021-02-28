<template>
    <div class="main">
        <flight-form v-on:loaded="formHandler"></flight-form>
        <find-flights v-if="showSingle" v-on:booking="bookingHandler"></find-flights>
        <booking-form v-if="booking"></booking-form>
        <flights v-if="showMultiply" v-on:booking="bookingHandler"></flights>
    </div>
</template>

<script>
    import FlightForm from "../components/FlightForm";
    import FindFlights from "../components/FindFlights";
    import BookingForm from "../components/BookingForm";
    import Flights from "../components/Flights";

    export default {
        name: 'App',
        components: {
            Flights,
            BookingForm,
            FindFlights,
            FlightForm,
        },
        data() {
            return {
                showSingle: false,
                showMultiply: false,
                booking: false,
            }
        },
        methods: {
            formHandler(data) {
                console.log(data)
                this.booking = data[0];
                this.showSingle = data[1];
                this.showMultiply = data[2];
            },
            bookingHandler(data) {
                this.booking = data;
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