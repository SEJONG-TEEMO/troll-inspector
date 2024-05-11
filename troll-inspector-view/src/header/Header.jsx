import {Navbar, NavbarContent, NavbarItem, Link, Button, NavbarBrand} from "@nextui-org/react";
import {AcmeLogo} from "./AcmeLogo.jsx";

export function Header() {
    return (
        <Navbar>
            <NavbarBrand>
                <AcmeLogo />
                <p className="font-bold text-inherit">TEEMO.GG</p>
            </NavbarBrand>
            <NavbarContent className="sm:flex gap-4" justify="center">
                <NavbarItem isActive>
                    <Link color="foreground" href="#">
                        전적 검색
                    </Link>
                </NavbarItem>
                <NavbarItem>
                    <Link color="foreground" href="#">
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