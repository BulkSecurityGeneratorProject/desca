import { BaseEntity } from './../../shared';

export class Repair implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public enabled?: boolean,
        public repairTypeId?: number,
    ) {
        this.enabled = false;
    }
}