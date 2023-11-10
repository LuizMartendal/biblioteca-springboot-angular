export interface User {
    id?: string,
    createdIn?: Date,
    createdBy?: string,
    updatedIn?: Date,
    updatedBy?: string,
    firstName: string,
    lastName: string,
    username: string,
    password: string,
    address: string,
    gender: string,
    role: string
}