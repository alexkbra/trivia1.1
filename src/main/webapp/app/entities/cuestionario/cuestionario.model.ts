import { BaseEntity } from './../../shared';

export class Cuestionario implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public reglas?: BaseEntity[],
        public preguntas?: BaseEntity[],
        public nivels?: BaseEntity[],
        public expedicionId?: number,
    ) {
    }
}
