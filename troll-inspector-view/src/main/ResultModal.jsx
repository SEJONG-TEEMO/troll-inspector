import {
    Button,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Table, TableBody, TableCell,
    TableColumn,
    TableHeader, TableRow
} from "@nextui-org/react";
import PropTypes from "prop-types";

export default function ResultModal({isOpen, onOpenChange, searchResult}) {

    const season = "S2024";

    // Define columns
    const columns = [
        { key: "championId", label: "챔피언" },
        { key: "gameName", label: "소환사" },
        { key: "level", label: "레벨" },
        { key: "tier", label: `${season} 티어` },
        { key: "rateOfWin", label: "솔랭 승률" },
        { key: "championInfo", label: `${season} 챔피언 정보` },
    ];

    // Function to get value by key
    const getKeyValue = (row, key) => {
        switch (key) {
            case "championInfo":
                return `챔피언 승률 ${row.rateOfChampionWin} (${row.countChampion} games) KDA: ${row.totalKDA}`; // Custom value for champion info column
            default:
                return row[key];
        }
    };

    const first = searchResult.length > 0 ? searchResult.slice(0, 5) : [];
    const second = searchResult.length > 5 ? searchResult.slice(5) : [];

    return (
        <>
            <Modal isOpen={isOpen} onOpenChange={onOpenChange} size="5xl">
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">인게임 정보</ModalHeader>
                            <ModalBody>
                                <Table aria-label="Example static collection table">
                                    <TableHeader columns={columns}>
                                        {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
                                    </TableHeader>
                                    <TableBody items={first}>
                                        {(item) =>
                                            <TableRow key={item.gameName}>
                                                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
                                            </TableRow>
                                        }
                                    </TableBody>
                                </Table>
                                <Table aria-label="Example static collection table">
                                    <TableHeader columns={columns}>
                                        {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
                                    </TableHeader>
                                    <TableBody items={second}>
                                        {(item) =>
                                            <TableRow key={item.gameName}>
                                                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
                                            </TableRow>
                                        }
                                    </TableBody>
                                </Table>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="danger" variant="light" onClick={onClose}>
                                    close
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
    searchResult: PropTypes.array.isRequired,
}