<template>
    <div class="main">
        <flight-form v-on:loaded="formHandler"></flight-form>
        <simple-flight v-if="showSingle" v-on:booking="bookingHandler"></simple-flight>
        <connecting-flight v-if="showMultiply" v-on:booking="bookingHandler"></connecting-flight>
        <booking-form v-if="booking"></booking-form>
    </div>
</template>

<script>
    import FlightForm from "../components/FlightForm";
    import BookingForm from "../components/BookingForm";
    import SimpleFlight from "../components/SimpleFlight";
    import ConnectingFlight from "../components/ConnectingFlight";

    export default {
        name: 'App',
        components: {
            SimpleFlight,
            BookingForm,
            ConnectingFlight,
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