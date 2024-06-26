import {Card, CardBody, CardFooter, CardHeader, Skeleton} from "@nextui-org/react";

export function MySkeleton(){
    return (
        <Card className="max-w-[340px]">
            <CardHeader className="justify-between">
                <div className="flex gap-5">
                    <Skeleton className="rounded-lg">
                        <div className="h-24 rounded-lg bg-default-300"></div>
                    </Skeleton>
                </div>
                <div className={"mt-2"}>
                    <Skeleton className="w-3/5 rounded-lg">
                        <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
                    </Skeleton>
                </div>
            </CardHeader>
            <CardBody className="px-3 py-0 text-small text-default-400">
                <div className={"mt-2"}>
                    <Skeleton className="w-3/5 rounded-lg">
                        <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
                    </Skeleton>
                </div>
                <div className={"mt-2"}>
                    <Skeleton className="w-3/5 rounded-lg">
                        <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
                    </Skeleton>
                </div>
                <div className={"mt-2"}>
                    <Skeleton className="w-3/5 rounded-lg">
                        <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
                    </Skeleton>
                </div>
            </CardBody>
            <CardFooter className="gap-3">
                <div className="flex gap-1">
                    <Skeleton className="w-3/5 rounded-lg">
                        <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
                    </Skeleton>
                </div>
            </CardFooter>
        </Card>
    )
}