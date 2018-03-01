import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cuestionario } from './cuestionario.model';
import { CuestionarioPopupService } from './cuestionario-popup.service';
import { CuestionarioService } from './cuestionario.service';
import { Nivel, NivelService } from '../nivel';
import { Expedicion, ExpedicionService } from '../expedicion';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cuestionario-dialog',
    templateUrl: './cuestionario-dialog.component.html'
})
export class CuestionarioDialogComponent implements OnInit {

    cuestionario: Cuestionario;
    isSaving: boolean;

    nivels: Nivel[];

    expedicions: Expedicion[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private cuestionarioService: CuestionarioService,
        private nivelService: NivelService,
        private expedicionService: ExpedicionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.nivelService.query()
            .subscribe((res: ResponseWrapper) => { this.nivels = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.expedicionService.query()
            .subscribe((res: ResponseWrapper) => { this.expedicions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cuestionario.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cuestionarioService.update(this.cuestionario));
        } else {
            this.subscribeToSaveResponse(
                this.cuestionarioService.create(this.cuestionario));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cuestionario>) {
        result.subscribe((res: Cuestionario) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cuestionario) {
        this.eventManager.broadcast({ name: 'cuestionarioListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackNivelById(index: number, item: Nivel) {
        return item.id;
    }

    trackExpedicionById(index: number, item: Expedicion) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-cuestionario-popup',
    template: ''
})
export class CuestionarioPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cuestionarioPopupService: CuestionarioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cuestionarioPopupService
                    .open(CuestionarioDialogComponent as Component, params['id']);
            } else {
                this.cuestionarioPopupService
                    .open(CuestionarioDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
