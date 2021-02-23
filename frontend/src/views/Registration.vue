<template>
    <div class="form-container">
        <div class="wrap">
            <h1>Registration</h1>
        </div>
        <form>
            <div class="wrap">
                <label for="email">Email
                    <input v-model.trim="regForm.email" type="text" class="form-control"
                           id="email" placeholder="Email">
                </label>
            </div>
            <div class="wrap">
                <label for="numberOfTickets">Password
                    <input v-model.trim="regForm.password" type="password" class="form-control"
                           id="numberOfTickets" placeholder="Password">
                </label>
            </div>
            <div class="wrap">
                <button type="button" @click="submitForm">Submit</button>
            </div>
            <div class="wrap">
                <button type="button" @click="$router.push('/login')">To Log</button>
            </div>
        </form>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "Registration",
        data() {
            return {
                regURL: "http://localhost:6060/api/auth/reg",
                regForm: {
                    email: "",
                    password: "",
                },
            }
        },
        methods: {
            submitForm() {
                axios.post(this.regURL, this.regForm, {
                    headers: {
                        'Access-Control-Allow-Origin': 'http://localhost:8080',
                    },
                })
                    .then((response) => {
                        if (response.status === 200) {
                            this.$router.push('/');
                        }
                    })
                    .catch((e) => {
                        console.log(e + ` ERROR REGISTRATION.VUE`)
                    });
            }
        }
    }
</script>

<style scoped>
    .form-container {
        margin-top: 75px;
        border: 2px solid #150202;
        border-radius: 3px;
        background-color: #cccccc;
    }

    .wrap {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-evenly;
        align-items: baseline;
    }

    label, input, select {
        margin: 0;
        outline: none;
        display: block;
        width: 250px;
        color: black;
        padding: 0.5rem 1rem;
        border-radius: 3px;
        font-size: 1rem;
    }

    button {
        margin-bottom: 10px;
        border-radius: 3px;
        width: 100px;
        height: 30px;
        display: block;
        color: black;
        font-size: 1rem;
        align-self: center;
    }

    button:active {
        background-color: #00ffff;
        animation-delay: inherit;
    }
</style>