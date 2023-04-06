import { ACCESS_TOKEN, API_BASE_URL } from "../common/constants";

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

    const defaults = { headers };
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

export function login (loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: "POST",
        body: JSON.stringify(loginRequest)
    });
}

export function uploadImage (uploadImageRequest) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject(new Error("No access token set."));
    }

    return request({
        setContentType: false,
        url: API_BASE_URL + "/media/images",
        method: "POST",
        body: uploadImageRequest
    });
}

export function createPost (createPostRequest) {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject(new Error("No access token set."));
    }

    return request({
        url: API_BASE_URL + "/post/posts",
        method: "POST",
        body: JSON.stringify(createPostRequest)
    });
}
