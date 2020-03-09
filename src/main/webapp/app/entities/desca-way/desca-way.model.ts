import { BaseEntity } from './../../shared';

export class DescaWay implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: boolean,
    ) {
        this.status = false;
    }
}
