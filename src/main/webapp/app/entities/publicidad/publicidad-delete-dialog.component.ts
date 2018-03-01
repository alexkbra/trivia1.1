import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Publicidad } from './publicidad.model';
import { PublicidadPopupService } from './publicidad-popup.service';
import { PublicidadService } from './publicidad.service';

@Component({
    selector: 'jhi-publicidad-delete-dialog',
    templateUrl: './publicidad-delete-dialog.component.html'
})
export class PublicidadDeleteDialogComponent {

    publicidad: Publicidad;

    constructor(
        private publicidadService: PublicidadService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.publicidadService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'publicidadListModification',
                content: 'Deleted an publicidad'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-publicidad-delete-popup',
    template: ''
})
export class PublicidadDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private publicidadPopupService: PublicidadPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.publicidadPopupService
                .open(PublicidadDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
