import {validity} from "./validateToken";
import axios from "axios";
import {bearer} from "./bearer";


export function axiosDelete(text, url) {
    const accessToken = localStorage.getItem("accessToken")
    const isValid = validity(accessToken);
    if (isValid) {
        axios.delete(url, {
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:8080',
                'Authorization': bearer(accessToken)
            },
        })
            .then((response) => {
                if (response.status === 200) {
                    alert(text);
                    this.$emit("loaded", [false, false]);
                }
            })
            .catch(() => {
            });
    }
}