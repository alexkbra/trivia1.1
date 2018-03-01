import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Expedicion } from './expedicion.model';
import { ExpedicionPopupService } from './expedicion-popup.service';
import { ExpedicionService } from './expedicion.service';

@Component({
    selector: 'jhi-expedicion-delete-dialog',
    templateUrl: './expedicion-delete-dialog.component.html'
})
export class ExpedicionDeleteDialogComponent {

    expedicion: Expedicion;

    constructor(
        private expedicionService: ExpedicionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.expedicionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'expedicionListModification',
                content: 'Deleted an expedicion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-expedicion-delete-popup',
    template: ''
})
export class ExpedicionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private expedicionPopupService: ExpedicionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.expedicionPopupService
                .open(ExpedicionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
