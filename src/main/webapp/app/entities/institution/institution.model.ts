import { BaseEntity } from './../../shared';

export class Institution implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public enabled?: boolean,
    ) {
        this.enabled = false;
    }
}
