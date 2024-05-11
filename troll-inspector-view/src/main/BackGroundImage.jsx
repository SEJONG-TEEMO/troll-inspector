import {SummonerSearchBar} from "./SummonerSearchBar.jsx";

export default function BackGroundImage() {
    return (
        <div style={{
            backgroundImage: `url(/src/assets/img2.jpg)`,
            height: 800,
            backgroundRepeat: 'no-repeat',
        }}>
            <SummonerSearchBar/>
        </div>
    )
}