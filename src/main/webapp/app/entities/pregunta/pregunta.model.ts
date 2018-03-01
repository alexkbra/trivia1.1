import { BaseEntity } from './../../shared';

export class Pregunta implements BaseEntity {
    constructor(
        public id?: number,
        public cortaDescripcion?: string,
        public descripcion?: string,
        public respuestas?: BaseEntity[],
        public pistas?: BaseEntity[],
        public cuestionarioId?: number,
        public tipoPreguntaId?: number,
    ) {
    }
}
