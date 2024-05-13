export default function BackGroundImage(props) {

    return (
        <div style={{
            backgroundImage: `url(/src/assets/img2.jpg)`,
            height: 800,
            backgroundRepeat: 'no-repeat',
        }}>
            {props.search}
        </div>
    )
}