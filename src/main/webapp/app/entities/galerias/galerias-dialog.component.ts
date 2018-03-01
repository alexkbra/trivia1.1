import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Galerias } from './galerias.model';
import { GaleriasPopupService } from './galerias-popup.service';
import { GaleriasService } from './galerias.service';
import { Publicidad, PublicidadService } from '../publicidad';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-galerias-dialog',
    templateUrl: './galerias-dialog.component.html'
})
export class GaleriasDialogComponent implements OnInit {

    galerias: Galerias;
    isSaving: boolean;

    publicidads: Publicidad[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private galeriasService: GaleriasService,
        private publicidadService: PublicidadService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.publicidadService.query()
            .subscribe((res: ResponseWrapper) => { this.publicidads = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.galerias.id !== undefined) {
            this.subscribeToSaveResponse(
                this.galeriasService.update(this.galerias));
        } else {
            this.subscribeToSaveResponse(
                this.galeriasService.create(this.galerias));
        }
    }

    private subscribeToSaveResponse(result: Observable<Galerias>) {
        result.subscribe((res: Galerias) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Galerias) {
        this.eventManager.broadcast({ name: 'galeriasListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackPublicidadById(index: number, item: Publicidad) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-galerias-popup',
    template: ''
})
export class GaleriasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private galeriasPopupService: GaleriasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.galeriasPopupService
                    .open(GaleriasDialogComponent as Component, params['id']);
            } else {
                this.galeriasPopupService
                    .open(GaleriasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
