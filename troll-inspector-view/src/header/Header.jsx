import {
    Navbar,
    NavbarContent,
    NavbarItem,
    Link,
    Button,
    NavbarBrand,
    Dropdown,
    DropdownMenu,
    DropdownTrigger, DropdownItem
} from "@nextui-org/react";
import {AcmeLogo} from "./AcmeLogo.jsx";
import {useNavigate} from "react-router-dom";
import {useMemo, useState} from "react";

export function Header() {

    const navigate = useNavigate();

    const [active, setActive] = useState('main');

    const onInGameClick = () => {
        navigate("/in-game");
        setActive('in-game');
    }

    const onSearchClick = () => {
        navigate("/");
        setActive('main');
    }

    const onMainClick = () => {
        navigate("/");
    }

    const items = [
        {
            key: "소환사 검색",
            label: "소환사 검색",
            description: "소환사를 검색하여 최근 40게임의 챔피언 전적을 조회합니다."
        },
        {
            key: "인게임 검색",
            label: "인게임 검색",
            description: "인게임에서 플레이하고 있는 유저의 챔피언 전적을 조회합니다."
        }
    ];

    const [selectedKeys, setSelectedKeys] = useState(new Set(["소환사 검색"]));

    const selectedValue = useMemo(
        () => Array.from(selectedKeys).join(", ").replaceAll("_", " "),
        [selectedKeys]
    );

    return (
        <Navbar>
            <NavbarBrand onClick={onMainClick}>
                <AcmeLogo />
                <p className="font-bold text-inherit">TEEMO.GG</p>
            </NavbarBrand>
            <NavbarContent className="sm:flex gap-4" justify="center">
                <NavbarItem isActive={active === 'main'} >
                    <Dropdown>
                        <DropdownTrigger>
                            <Button
                                variant="bordered"
                                className="capitalize"
                                color={"primary"}
                                size={"lg"}
                            >
                                {selectedValue}
                            </Button>
                        </DropdownTrigger>
                        <DropdownMenu aria-label="Dynamic Actions"
                                      variant="flat"
                                      disallowEmptySelection
                                      items={items}
                                      disabledKeys={"인게임 검색"}
                                      selectionMode="single"
                                      selectedKeys={selectedKeys}
                                      onSelectionChange={setSelectedKeys}
                        >
                            {(item) => (
                                <DropdownItem
                                    key={item.key}
                                    description={item.description}
                                    onClick={item.key === "소환사 검색" ? onSearchClick : onInGameClick}
                                >
                                    {item.label}
                                </DropdownItem>
                            )}
                        </DropdownMenu>
                    </Dropdown>
                </NavbarItem>
            </NavbarContent>
            <NavbarContent justify="end">
                <NavbarItem className="hidden lg:flex">
                    <Link href="#">Login</Link>
                </NavbarItem>
                <NavbarItem>
                    <Button isDisabled as={Link} color="primary" href="#" variant="flat">
                        로그인
                    </Button>
                </NavbarItem>
            </NavbarContent>
        </Navbar>
    )
}