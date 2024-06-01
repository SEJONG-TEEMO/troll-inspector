import React, {useEffect, useState} from "react";
import {Button, Input} from "@nextui-org/react";
import {SearchIcon} from "./SearchIcon.jsx";
import searchNameAndTag from "../axios/search.js";
import ResultModal from "./ResultModal.jsx";

export function SummonerSearchBar() {

    const [search, setSearch] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [searchResult, setSearchResult] = useState([]);

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            handleSearch(event.target.value);
            setSearch('');
        }
    };

    const handleSearch = (search) => {
        const res = searchNameAndTag(search);
        res.then(response => {
            setSearchResult(response.data)
            setIsModalOpen(true);
        }).catch(error => {
            alert("데이터를 가져오지 못했습니다.")
            console.error(error)
        });
    }

    const handleSearchClick = () => {
        handleSearch(search);
        setSearch('');
    }

    return (
        <div className="w-screen h-screen p-8 flex items-center justify-center">
            <Input
                className="max-w-2xl"
                type="search"
                label="검색"
                isClearable
                radius="lg"
                placeholder="소환사 이름 + #태그를 입력해주세요."
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
            <Button type="button" isIconOnly size={"lg"} onPress={handleSearchClick}>
                <SearchIcon/>
            </Button>
            <ResultModal
                isOpen={isModalOpen}
                onOpenChange={setIsModalOpen}
                searchResult={searchResult}
            />
        </div>
    )
}