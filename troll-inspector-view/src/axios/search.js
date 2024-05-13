import axios from "axios";

const baseUrl = "http://localhost:8080/api/v1/";
const searchUrl = "crawling/";
const trollUrl = "troll-inspector/"

export default function searchNameAndTag(search) {

    console.log(search);

    if(search.includes("#")) {
        const split = search.split("#");
        const name = split[0].trim();
        const tag = split[1].trim();

        console.log(name, tag);
        return axios.get(baseUrl + searchUrl + `${name}` + "/" + `${tag}`)
            .catch(error => console.log(error))
            .then(response => response.data);
    } else {
        alert("잘못된 입력입니다.")
    }
}

export function trollInspectorNameAndTag(search) {

    console.log(search);

    if(search.includes("#")) {
        const split = search.split("#");
        const name = split[0].trim();
        const tag = split[1].trim();

        console.log(name, tag);
        return axios.get(baseUrl + trollUrl + `${name}` + "/" + `${tag}`)
            .catch(error => console.log(error))
            .then(response => response.data);
    } else {
        alert("잘못된 입력입니다.")
    }
}