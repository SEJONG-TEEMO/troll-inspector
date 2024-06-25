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
    Image, getKeyValue, User
} from "@nextui-org/react";
import PropTypes from "prop-types";

export default function ResultModal({isOpen, onOpenChange, searchResult}) {

    const season = "S2024";

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

    return (
        <>
            <Modal isOpen={isOpen} onOpenChange={onOpenChange} size="5xl">
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">{season} 프로필 정보</ModalHeader>
                            <ModalBody>
                                <Card className="max-w-[340px]">
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
                                        {/* 챔피언 사진 */}
                                        <div className="flex gap-1">
                                            <Button className="flex flex-col items-start" color="primary">
                                                전적 갱신
                                            </Button>
                                        </div>
                                    </CardFooter>
                                </Card>
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
                                        {
                                            searchResult.userChampionPerformances.map((userChampion) =>
                                                <TableRow key={userChampion.key}>
                                                    {columnKey => (
                                                        <TableCell>
                                                            {columnKey === 'championName' ? (
                                                                // 챔피언 이미지를 Base64로 인코딩된 데이터 URI로 처리
                                                                <Avatar isBordered radius="full" size="sm" src={`https://ddragon.leagueoflegends.com/cdn/14.12.1/img/champion/${getKeyValue(userChampion, columnKey)}.png`}/>
                                                            ) : columnKey === 'winRate' ? (
                                                                // 그 외의 경우는 getKeyValue 함수를 사용
                                                                `${getKeyValue(userChampion, columnKey)}%`
                                                            ) : (
                                                                getKeyValue(userChampion, columnKey)
                                                            )}
                                                        </TableCell>
                                                    )}
                                                </TableRow>
                                            )
                                        }
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
    searchResult: PropTypes.object.isRequired,
}