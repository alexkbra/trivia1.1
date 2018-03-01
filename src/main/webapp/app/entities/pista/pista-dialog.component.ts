import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pista } from './pista.model';
import { PistaPopupService } from './pista-popup.service';
import { PistaService } from './pista.service';
import { Pregunta, PreguntaService } from '../pregunta';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pista-dialog',
    templateUrl: './pista-dialog.component.html'
})
export class PistaDialogComponent implements OnInit {

    pista: Pista;
    isSaving: boolean;

    preguntas: Pregunta[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private pistaService: PistaService,
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
        if (this.pista.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pistaService.update(this.pista));
        } else {
            this.subscribeToSaveResponse(
                this.pistaService.create(this.pista));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pista>) {
        result.subscribe((res: Pista) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pista) {
        this.eventManager.broadcast({ name: 'pistaListModification', content: 'OK'});
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
    selector: 'jhi-pista-popup',
    template: ''
})
export class PistaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pistaPopupService: PistaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pistaPopupService
                    .open(PistaDialogComponent as Component, params['id']);
            } else {
                this.pistaPopupService
                    .open(PistaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
