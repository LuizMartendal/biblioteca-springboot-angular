export interface Book {
    id?: string,
    createdIn?: Date,
    createdBy?: string,
    updatedIn?: Date,
    updatedBy?: string,
    author: string,
    launchDate: Date,
    price: number,
    title: string
}