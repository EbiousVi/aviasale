export default {
    state() {
        return {
            accessToken: "",
            refreshToken: "",
        }
    },
    mutations: {
        setAccessToken(state, payload) {
            state.accessToken = payload;
        },
        setRefreshToken(state, payload) {
            state.refreshToken = payload;
        }
    },
    getters: {
        getAccessToken(state) {
            return state.accessToken;
        },
        getRefreshToken(state) {
            return state.refreshToken;
        },
    }
}