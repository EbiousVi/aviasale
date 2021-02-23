import {createRouter, createWebHistory} from 'vue-router'
import Login from "./views/Login";
import Main from "./views/Main";
import Registration from "./views/Registration";
import Reservation from "./views/Bookings";

export default createRouter({
    history: createWebHistory(),// сохранения маршрутов переход по слещам
    routes: [
        {path: '/login', component: Login, alias: ''},
        {path: '/main', component: Main},
        {path: '/registration', component: Registration},
        {path: '/bookings', component: Reservation}
    ]
})