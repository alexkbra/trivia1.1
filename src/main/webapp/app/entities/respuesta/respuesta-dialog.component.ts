import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Respuesta } from './respuesta.model';
import { RespuestaPopupService } from './respuesta-popup.service';
import { RespuestaService } from './respuesta.service';
import { Pregunta, PreguntaService } from '../pregunta';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-respuesta-dialog',
    templateUrl: './respuesta-dialog.component.html'
})
export class RespuestaDialogComponent implements OnInit {

    respuesta: Respuesta;
    isSaving: boolean;

    preguntas: Pregunta[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private respuestaService: RespuestaService,
        private preguntaService: PreguntaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.preguntaService.query()
            .subscribe((res: ResponseWrapper) => { this.preguntas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.respuesta.id !== undefined) {
            this.subscribeToSaveResponse(
                this.respuestaService.update(this.respuesta));
        } else {
            this.subscribeToSaveResponse(
                this.respuestaService.create(this.respuesta));
        }
    }

    private subscribeToSaveResponse(result: Observable<Respuesta>) {
        result.subscribe((res: Respuesta) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Respuesta) {
        this.eventManager.broadcast({ name: 'respuestaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackPreguntaById(index: number, item: Pregunta) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-respuesta-popup',
    template: ''
})
export class RespuestaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private respuestaPopupService: RespuestaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.respuestaPopupService
                    .open(RespuestaDialogComponent as Component, params['id']);
            } else {
                this.respuestaPopupService
                    .open(RespuestaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
