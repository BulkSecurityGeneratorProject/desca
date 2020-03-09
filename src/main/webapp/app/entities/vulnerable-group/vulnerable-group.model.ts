import { BaseEntity } from './../../shared';

export class VulnerableGroup implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: boolean,
    ) {
        this.status = false;
    }
}
