import React from "react";

export default function BackGroundImage(props) {

    return (
        <div style={{
            backgroundImage: `url(/img2.jpg)`,
            height: 800,
            backgroundRepeat: 'no-repeat',
        }}>
            {props.search}
        </div>
    )
}