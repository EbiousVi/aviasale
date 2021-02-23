<template>
    <div class="main">
        <flight-form v-on:loaded="formHandler"></flight-form>
        <find-flights v-if="loaded" v-on:booking="bookingHandler"></find-flights>
        <booking-form v-if="booking"></booking-form>
    </div>
</template>

<script>
    import FlightForm from "../components/FlightForm";
    import FindFlights from "../components/FindFlights";
    import BookingForm from "../components/BookingForm";

    export default {
        name: 'App',
        components: {
            BookingForm,
            FindFlights,
            FlightForm,
        },
        data() {
            return {
                loaded: false,
                booking: false,
            }
        },
        methods: {
            formHandler(data) {
                this.loaded = data[0];
                this.booking = data[1];

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