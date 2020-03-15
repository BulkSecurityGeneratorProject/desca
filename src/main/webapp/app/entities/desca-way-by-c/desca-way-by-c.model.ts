import { BaseEntity } from './../../shared';

export class DescaWayByC implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public enabled?: boolean,
    ) {
        this.enabled = false;
    }
}
