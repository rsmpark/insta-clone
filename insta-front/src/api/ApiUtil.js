import {API_BASE_URL, ACCESS_TOKEN} from "../common/constants";

const request = options => {
    const headers = new Headers();

    if (options.setContentType !== false) {
        headers.append("Content-Type", "application/json");
    }

    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append(
            "Authorization",
            "Bearer " + localStorage.getItem(ACCESS_TOKEN)
        );
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: "POST",
        body: JSON.stringify(loginRequest)
    });
}