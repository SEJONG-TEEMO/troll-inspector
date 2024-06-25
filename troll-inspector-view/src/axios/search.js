import axios from "axios";

const baseUrl = "http://localhost:8081/api/v1/";
const searchUrl = "performance";
const inGameUrl = "in-game"

export default async function searchNameAndTag(search) {

    console.log(search);

    if(search.includes("#")) {
        const split = search.split("#");
        const name = split[0].trim();
        const tag = split[1].trim();

        console.log(name, tag);
        return await axios.get(baseUrl + searchUrl, {params: {gameName: name, tagLine: tag}});
    } else {
        alert("잘못된 입력입니다.")
    }
}

export async function inGameNameAndTag(search) {

    console.log(search);

    if(search.includes("#")) {
        const split = search.split("#");
        const name = split[0].trim();
        const tag = split[1].trim();

        console.log(name, tag);
        return await axios.get(baseUrl + inGameUrl + `${name}` + "/" + `${tag}`)
            .catch(error => console.log(error))
            .then(response => response.data);
    } else {
        alert("잘못된 입력입니다.")
    }
}

export async function updateUserPerformance(updateData) {

    if(updateData.includes("#")) {
        const split = updateData.split("#");
        const name = split[0].trim();
        const tag = split[1].trim();

        console.log(name, tag);
        return await axios.patch(baseUrl + searchUrl, {params: {gameName: name, tagLine: tag}});
    } else {
        alert("잘못된 입력입니다.")
    }
}