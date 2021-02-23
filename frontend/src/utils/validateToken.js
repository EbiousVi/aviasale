import jwt_decode from "jwt-decode"

export function validity(token) {
    const decode = jwt_decode(token);
    const now = Date.now();
    const expireDate = new Date(decode.exp * 1000)
    const limit = 120000;
    return (expireDate - now) > limit;
}