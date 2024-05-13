import {Navbar, NavbarContent, NavbarItem, Link, Button, NavbarBrand} from "@nextui-org/react";
import {AcmeLogo} from "./AcmeLogo.jsx";
import {useNavigate} from "react-router-dom";

export function Header() {

    const navigate = useNavigate();

    const onTrollClick = () => {
        navigate("/troll-inspector");
    }

    const onSearchClick = () => {
        navigate("/");
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
                <NavbarItem isActive>
                    <Link color="foreground" onClick={onSearchClick}>
                        전적 검색
                    </Link>
                </NavbarItem>
                <NavbarItem>
                    <Link color="foreground" onClick={onTrollClick}>
                        트롤 검색
                    </Link>
                </NavbarItem>
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