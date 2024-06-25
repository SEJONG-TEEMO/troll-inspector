import React, {useState} from "react";
import {Button, Input} from "@nextui-org/react";
import {SearchIcon} from "./SearchIcon.jsx";
import {inGameNameAndTag} from "../axios/search.js";

export function InGameSearchBar() {

    const [search, setSearch] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [searchResult, setSearchResult] = useState([]);

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            inGameNameAndTag(event.target.value);
            setSearch('');
        }
    };

    const handleSearch = (search) => {
        const res = inGameNameAndTag(search);
        res.then(response => {
            setSearchResult(response.data)
            setIsModalOpen(true);
        }).catch(error => {
            alert("데이터를 가져오지 못했습니다.")
            console.error(error)
        });
    }

    return (
        <div className="w-screen h-screen p-8 flex items-center justify-center">
            <Input
                className="max-w-2xl"
                type="search"
                label="인게임 검색"
                isClearable
                radius="lg"
                placeholder="자신의 소환사 이름 + #태그를 입력해주세요. (솔랭 기준)"
                startContent={
                    <SearchIcon
                        className="text-black/50 mb-0.5 dark:text-white/90 text-slate-400 pointer-events-none flex-shrink-0"/>
                }
                value={search}
                onChange={(e) => {
                    setSearch(e.target.value);
                }}
                onKeyDown={handleKeyDown}
            />
            <Button type="submit" isIconOnly size={"lg"} onClick={inGameNameAndTag}>
                <SearchIcon/>
            </Button>
        </div>
    )
}