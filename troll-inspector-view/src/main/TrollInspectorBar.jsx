import React, {useState} from "react";
import {Button, Input} from "@nextui-org/react";
import {SearchIcon} from "./SearchIcon.jsx";
import {trollInspectorNameAndTag} from "../axios/search.js";

export function TrollInspectorBar() {

    const [search, setSearch] = useState('');

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            trollInspectorNameAndTag(event.target.value);
            setSearch('');
        }
    };

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
            <Button type="submit" isIconOnly size={"lg"} onClick={trollInspectorNameAndTag}>
                <SearchIcon/>
            </Button>
        </div>
    )
}