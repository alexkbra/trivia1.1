import { BaseEntity } from './../../shared';

export class TipoPregunta implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public constant?: string,
        public descripcion?: string,
        public preguntas?: BaseEntity[],
    ) {
    }
}
