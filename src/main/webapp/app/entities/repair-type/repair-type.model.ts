import { BaseEntity } from './../../shared';

export class RepairType implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public enabled?: boolean,
    ) {
        this.enabled = false;
    }
}
