import {Navbar, NavbarContent, NavbarItem, Link, Button, NavbarBrand} from "@nextui-org/react";
import {AcmeLogo} from "./AcmeLogo.jsx";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

export function Header() {

    const navigate = useNavigate();

    const [active, setActive] = useState('main');

    const onTrollClick = () => {
        navigate("/troll-inspector");
        setActive('troll');
    }

    const onSearchClick = () => {
        navigate("/");
        setActive('main');
    }

    const onMainClick = () => {
        navigate("/");
    }

    return (
        <Navbar>
            <NavbarBrand onClick={onMainClick}>
                <AcmeLogo />
                <p className="font-bold text-inherit">TEEMO.GG</p>
            </NavbarBrand>
            <NavbarContent className="sm:flex gap-4" justify="center">
                <NavbarItem isActive={active === 'main'} >
                    <Link color={active === 'main' ? "primary" : "foreground"} onClick={onSearchClick}>
                        인게임 분석
                    </Link>
                </NavbarItem>
                {/*<NavbarItem isActive={active === 'troll'}>*/}
                {/*    <Link color={active === 'troll' ? "primary" : "foreground"} onClick={onTrollClick}>*/}
                {/*        트롤 검색*/}
                {/*    </Link>*/}
                {/*</NavbarItem>*/}
            </NavbarContent>
            <NavbarContent justify="end">
                <NavbarItem className="hidden lg:flex">
                    <Link href="#">Login</Link>
                </NavbarItem>
                <NavbarItem>
                    <Button as={Link} color="primary" href="#" variant="flat">
                        로그인
                    </Button>
                </NavbarItem>
            </NavbarContent>
        </Navbar>
    )
}