import {
    Avatar,
    Button, Card, CardBody, CardFooter, CardHeader,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Table, TableBody, TableCell,
    TableColumn,
    TableHeader, TableRow,
    Image, Spinner
} from "@nextui-org/react";
import PropTypes from "prop-types";
import searchNameAndTag, {updateUserPerformance} from "../axios/search.js";
import {useEffect, useState} from "react";
import {MySkeleton} from "./MySkeleton.jsx";

// Define columns
const columns = [
    {
        key: "championName",
        label: `챔피언`
    },
    {
        key: "recentGameCount",
        label: "게임 수"
    },
    {
        key: "winRate",
        label: "승률"
    },
    {
        key: "kda",
        label: "KDA"
    },
    {
        key: "kills",
        label: "킬"
    },
    {
        key: "deaths",
        label: "데쓰"
    },
    {
        key: "assists",
        label: "어시"
    },
    {
        key: "cs",
        label: "미니언 처치 수"
    },
    {
        key: "pentaKill",
        label: "펜타킬"
    },
    {
        key: "quadraKill",
        label: "쿼드라킬"
    },
    {
        key: "tripleKill",
        label: "트리플킬"
    },
    {
        key: "wins",
        label: "승리"
    },
    {
        key: "losses",
        label: "패배"
    }
];
export default function ResultModal({isOpen, onOpenChange, search}) {

    const season = "S2024";

    const [isLoading, setIsLoading] = useState(false);
    const [searchResult, setSearchResult] = useState({});

    useEffect(() => {
        if(search) {
            handleSearch(search)
        }
    }, [search])

    const handleSearch = (search) => {
        setIsLoading(true);
        const res = searchNameAndTag(search);
        res.then(response => {
            setSearchResult(response.data)
        }).catch(error => {
            alert("데이터를 가져오지 못했습니다.")
            console.error(error)
        }).finally(() => {
            setIsLoading(false);
        });
    }

    const handleUpdate = (updateData) => {
        setIsLoading(true);
        updateUserPerformance(updateData)
            .catch(error => {
                alert("업데이트를 진행할 수 없습니다. 잠시 후에 다시 진행해 주세요.")
                console.log(error);
            })
            .then(response => {
                setSearchResult(response.data)
            }).finally(() => {
                setIsLoading(false);
        })
    }

    return (
        <>
            <Modal isOpen={isOpen} onOpenChange={onOpenChange} size="5xl">
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">{season} 프로필 정보</ModalHeader>
                                <ModalBody>
                                    { searchResult.userProfileDto === undefined ? <MySkeleton /> :
                                        (<Card className="max-w-[340px]">
                                            <CardHeader className="justify-between">
                                                <div className="flex gap-5">
                                                    <Avatar className="mr-2" isBordered radius="full" size="lg" src={`https://ddragon.leagueoflegends.com/cdn/14.12.1/img/profileicon/${searchResult.userProfileDto.profileIconId}.png`}/>
                                                    <div className="ml-1 flex flex-col gap-1 items-start justify-center">
                                                        <h4 className="text-small font-semibold leading-none text-default-600">{searchResult.userProfileDto.gameName}</h4>
                                                        <h5 className="text-small tracking-tight text-default-400">#{searchResult.userProfileDto.tagLine}</h5>
                                                    </div>
                                                </div>
                                            <Image
                                                className="flex flex-col items-end"
                                                src={`/Rank=${searchResult.userProfileDto.tier}.png`}
                                                width={110}
                                                height={110}
                                            />
                                            </CardHeader>
                                            <CardBody className="px-3 py-0 text-small text-default-400">
                                                <p className="mb-2">
                                                    레벨: {searchResult.userProfileDto.summonerLevel}
                                                </p>
                                                <p>
                                                    전적: {searchResult.userProfileDto.total}전 {searchResult.userProfileDto.wins}승 {searchResult.userProfileDto.losses}패 (승률 {(searchResult.userProfileDto.wins / searchResult.userProfileDto.total * 100).toFixed(2)}%)
                                                </p>
                                                <span className="pt-2">
                                                  티어: {searchResult.userProfileDto.tier} {searchResult.userProfileDto.rank}
                                                </span>
                                            </CardBody>
                                            <CardFooter className="gap-3">
                                                <div className="flex gap-1">
                                                    {
                                                        (isLoading === true) ? (<Spinner className={"ml-2"} color={"primary"}/>) :
                                                            (
                                                                <Button className="flex flex-col items-start" color="primary" onClick={() => handleUpdate(`${searchResult.userProfileDto.gameName}#${searchResult.userProfileDto.tagLine}`)}>
                                                                    전적 갱신
                                                                </Button>
                                                            )
                                                    }
                                                </div>
                                            </CardFooter>
                                        </Card>)
                                    }
                                </ModalBody>
                            <ModalHeader className="flex flex-col gap-1">{season} 전적 정보 (최근 40게임 TOP 5)</ModalHeader>
                            <ModalBody>
                                <Table aria-label="Example static collection table">
                                    <TableHeader>
                                        {columns.map((column) =>
                                            <TableColumn key={column.key}>{column.label}</TableColumn>
                                        )}
                                    </TableHeader>
                                    <TableBody emptyContent={"No rows to display."}>
                                        {searchResult.userChampionPerformances?.map((userChampion, index) => (
                                            <TableRow key={index}>
                                                {columns.map((column) => (
                                                    <TableCell key={column.key}>
                                                        {column.key === 'championName' ? (
                                                            <Avatar isBordered radius="full" size="sm" src={`https://ddragon.leagueoflegends.com/cdn/14.12.1/img/champion/${userChampion[column.key]}.png`} />
                                                        ) : column.key === 'winRate' ? (
                                                            `${userChampion[column.key]}%`
                                                        ) : (
                                                            userChampion[column.key]
                                                        )}
                                                    </TableCell>
                                                ))}
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="danger" variant="light" onClick={onClose}>
                                    닫기
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    )
}


ResultModal.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    onOpenChange: PropTypes.func.isRequired,
    search: PropTypes.string.isRequired,
}