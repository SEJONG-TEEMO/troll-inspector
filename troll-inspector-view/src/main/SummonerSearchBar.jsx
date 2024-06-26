import React, {useState} from "react";
import {Button, Input} from "@nextui-org/react";
import {SearchIcon} from "./SearchIcon.jsx";
import ResultModal from "./ResultModal.jsx";

export function SummonerSearchBar() {

    const [search, setSearch] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            setSearch(event.target.value)
            setIsModalOpen(true);
        }
    };

    const handleSearchClick = (event) => {
        setSearch(event.target.value)
        setIsModalOpen(true)
    }

    return (
        <div className="w-screen h-screen p-8 flex items-center justify-center">
            <Input
                className="max-w-2xl"
                label="소환사 검색"
                isClearable
                radius="lg"
                placeholder="소환사 이름 + #태그를 입력해주세요. (솔랭 기준)"
                startContent={
                    <SearchIcon
                        className="text-black/50 mb-0.5 dark:text-white/90 text-slate-400 pointer-events-none flex-shrink-0"/>
                }
                onKeyDown={handleKeyDown}
            />
            <Button type="button" isIconOnly size={"lg"} onPress={handleSearchClick} color={"primary"}>
                <SearchIcon/>
            </Button>
            <ResultModal
                isOpen={isModalOpen}
                onOpenChange={setIsModalOpen}
                search={search}
            />
        </div>
    )
}