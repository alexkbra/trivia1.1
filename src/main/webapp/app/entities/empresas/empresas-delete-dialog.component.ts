import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Empresas } from './empresas.model';
import { EmpresasPopupService } from './empresas-popup.service';
import { EmpresasService } from './empresas.service';

@Component({
    selector: 'jhi-empresas-delete-dialog',
    templateUrl: './empresas-delete-dialog.component.html'
})
export class EmpresasDeleteDialogComponent {

    empresas: Empresas;

    constructor(
        private empresasService: EmpresasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.empresasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'empresasListModification',
                content: 'Deleted an empresas'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-empresas-delete-popup',
    template: ''
})
export class EmpresasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private empresasPopupService: EmpresasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.empresasPopupService
                .open(EmpresasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
