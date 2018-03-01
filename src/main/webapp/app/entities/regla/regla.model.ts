import { BaseEntity } from './../../shared';

export class Regla implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public cuestionarioId?: number,
    ) {
    }
}
