import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Galerias } from './galerias.model';
import { GaleriasPopupService } from './galerias-popup.service';
import { GaleriasService } from './galerias.service';

@Component({
    selector: 'jhi-galerias-delete-dialog',
    templateUrl: './galerias-delete-dialog.component.html'
})
export class GaleriasDeleteDialogComponent {

    galerias: Galerias;

    constructor(
        private galeriasService: GaleriasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.galeriasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'galeriasListModification',
                content: 'Deleted an galerias'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-galerias-delete-popup',
    template: ''
})
export class GaleriasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private galeriasPopupService: GaleriasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.galeriasPopupService
                .open(GaleriasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
