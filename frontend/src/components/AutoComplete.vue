<template>
    <div class="autocomplete">
        <div class="autocomplete-input">
            <label v-bind:for="id">
                <slot></slot>
                <input v-bind:id="id" v-model.trim="input" @input="findCity" v-bind:placeholder="id"
                       @keyup.down="buttonDown" @keyup.up="buttonUp" @keydown.enter.prevent="enter"
                       autocomplete="off" type="search" required>
            </label>
        </div>
        <div class="bordered" v-if="display">
            <ul ref="scrollContainer" class="autocomplete-results">
                <li ref="options" v-for="(city, i) in filtered" v-bind:key="i"
                    :class="{ 'is-active': i === pointer }"
                    v-on:click="setInput(city)" class="autocomplete-result">{{ city }}
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    import {validity} from "../utils/validateToken";
    import axios from "axios";
    import {bearer} from "../utils/bearer";
    import {refreshTokens} from "../utils/refreshTokens";

    export default {
        name: "AutoComplete",
        props: ["id"],
        emits: ["autocomplete-input"],
        data() {
            return {
                input: "",
                //cities: ["Абакан", "Анадырь", "Анапа", "Архангельск", "Астрахань", "Барнаул", "Белгород", "Белоярский", "Благовещенск", "Братск", "Брянск", "Бугульма", "Владивосток", "Владикавказ", "Волгоград", "Воркута", "Воронеж", "Геленджик", "Горно-Алтайск", "Грозный", "Екатеринбург", "Иваново", "Ижевск", "Иркутск", "Йошкар-Ола", "Казань", "Калининград", "Калуга", "Кемерово", "Киров", "Когалым", "Комсомольск-на-Амуре", "Краснодар", "Красноярск", "Курган", "Курск", "Кызыл", "Липецк", "Магадан", "Магнитогорск", "Махачкала", "Минеральные Воды", "Мирный", "Москва", "Мурманск", "Надым", "Нальчик", "Нарьян-Мар", "Нерюнгри", "Нефтеюганск", "Нижневартовск", "Нижнекамск", "Нижний Новгород", "Новокузнецк", "Новосибирск", "Новый Уренгой", "Норильск", "Ноябрьск", "Нягань", "Омск", "Оренбург", "Орск", "Пенза", "Пермь", "Петрозаводск", "Петропавловск-Камчатский", "Псков", "Ростов-на-Дону", "Салехард", "Самара", "Санкт-Петербург", "Саранск", "Саратов", "Советский", "Сочи", "Ставрополь", "Стрежевой", "Сургут", "Сыктывкар", "Тамбов", "Томск", "Тюмень", "Удачный", "Улан-Удэ", "Ульяновск", "Урай", "Усинск", "Усть-Илимск", "Усть-Кут", "Уфа", "Ухта", "Хабаровск", "Ханты-Мансийск", "Чебоксары", "Челябинск", "Череповец", "Чита", "Элиста", "Южно-Сахалинск", "Якутск", "Ярославль"],
                cities: [],
                filtered: [],
                pointer: 0,
                display: false,
            }
        },
        async mounted() {
            const accessToken = localStorage.getItem("accessToken");
            const isValid = validity(accessToken);
            if (isValid) {
                await axios
                    .get("http://localhost:6060/airports", {
                        headers: {
                            'Access-Control-Allow-Origin': 'http://localhost:8080',
                            'Authorization': bearer(accessToken)
                        },
                    })
                    .then(response => {
                        this.cities = response.data;
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
        methods: {
            fixScrolling() {
                this.$refs.scrollContainer.scrollIntoView({behavior: 'smooth', block: 'nearest', inline: 'start'});
            },
            enter() {
                this.input = this.filtered[this.pointer];
                this.pointer = -1;
                this.display = false;
                this.$emit("autocomplete-input", this.input);
            },
            buttonDown(ev) {
                ev.preventDefault();
                if (this.pointer < this.filtered.length) {
                    this.display = true;
                    this.pointer = this.pointer + 1;
                    if (this.pointer === this.filtered.length) {
                        this.pointer = 0;
                    }
                    this.fixScrolling();
                }
            },
            buttonUp(ev) {
                ev.preventDefault();
                if (this.input.length === 0) {
                    this.display = false;
                } else if (this.pointer > 0) {
                    if (this.pointer === this.filtered.length) {
                        this.this.arrowCounter = 0;
                    }
                    this.pointer = this.pointer - 1;
                    this.fixScrolling();
                }
            },
            setInput(value) {
                this.cities.filter(value1 => {
                    return value1 === value1
                })
                if (this.cities.length > 0) {
                    this.input = value
                    this.display = false;
                    this.$emit("autocomplete-input", this.input);
                }
            },
            findCity(ev) {
                ev.preventDefault();
                if (this.input.length === 0) {
                    this.display = false;
                } else if (this.input.length > 0) {
                    this.filtered = this.cities.filter(input => {
                        return input.toLowerCase().startsWith(this.input.toLowerCase());
                    });
                    if (this.filtered.length !== 0) {
                        this.display = true;
                    }
                }
            }
        },
    }
</script>
<style scoped>
    .autocomplete {
        padding-bottom: 15px;
    }

    .bordered {
        border: 1px solid black;
        position: absolute;
        display: block;
        background-color: #f5f5f5;
        width: 150px;
    }

    .autocomplete-input input, label {
        margin: 0;
        outline: none;
        display: block;
        width: 200px;
        color: black;
        padding: 0.5rem 1rem;
        border-radius: 3px;
        font-size: 1rem;
    }

    .autocomplete-results {
        padding: 0;
        margin: 0;
        border: 1px solid #eeeeee;
        overflow: auto;
    }

    .autocomplete-result {
        list-style: none;
        text-align: left;
        padding: 4px 2px;
        cursor: pointer;
        width: 100%;
    }

    .autocomplete-result.is-active,
    .autocomplete-result:hover {
        background-color: lightskyblue;
    }
</style>