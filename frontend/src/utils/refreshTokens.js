import axios from 'axios'
import {bearer} from "./bearer";
import {store} from "../store/store";


export function refreshTokens(token) {
    const url = "http://localhost:6060/api/auth/refresh";
    const body = {
        refreshToken: token
    }
    return axios.post(url, body, {
        headers: {
            'Access-Control-Allow-Origin': 'http://localhost:8080',
            'Authorization': bearer(token)
        },
    })
        .then((response) => {
            let accessToken = response.data.accessToken;
            let refreshToken = response.data.refreshToken;

            store.commit("setAccessToken", accessToken);
            store.commit("setRefreshToken", refreshToken);
            return response.status;
        })
        .catch(() => {
        });
}
