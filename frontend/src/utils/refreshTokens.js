import axios from 'axios'
import {bearer} from "./bearer";


export function refreshTokens() {
    const token = localStorage.getItem("refreshToken");
    const url = "http://localhost:6060/api/auth/refresh";
    const body = {
        refreshToken: localStorage.getItem(token)
    }
    return axios.post(url, body, {
        headers: {
            'Access-Control-Allow-Origin': 'http://localhost:8080',
            'Authorization': bearer(token)
        },
    })
        .then((response) => {
            localStorage.setItem("accessToken", response.data.accessToken);
            localStorage.setItem("refreshToken", response.data.refreshToken);
            return response.status;
        })
        .catch(() => {
        });
}
