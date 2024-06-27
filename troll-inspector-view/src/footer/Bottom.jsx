import {Button, Link} from "@nextui-org/react";

export default function bottom() {
    return (
        <footer id="footer">
            <div>
                <div className="px-3 py-3">
                    <strong>TEEMO.GG (BETA)</strong>
                </div>
                <hr className={"mb-2"}/>
                <Button
                    href={"https://velog.io/@swager253/series/teemo.gg"}
                    as={Link}
                    color="primary"
                    showAnchorIcon
                    variant="solid"
                    className={"ml-2 mb-2 mt-2"}
                >
                    개발 노트
                </Button>
                <div className="px-5 py-3 p-6 flex flex-col items-center justify-center">
                    <div>
                        <p>
                            TEEMO.GG isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or
                            anyone officially involved in producing or managing League of Legends. League of Legends and Riot
                            Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games,
                            Inc.
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    )
}