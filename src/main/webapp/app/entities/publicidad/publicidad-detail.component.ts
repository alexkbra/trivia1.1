import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Publicidad } from './publicidad.model';
import { PublicidadService } from './publicidad.service';

@Component({
    selector: 'jhi-publicidad-detail',
    templateUrl: './publicidad-detail.component.html'
})
export class PublicidadDetailComponent implements OnInit, OnDestroy {

    publicidad: Publicidad;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private publicidadService: PublicidadService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPublicidads();
    }

    load(id) {
        this.publicidadService.find(id).subscribe((publicidad) => {
            this.publicidad = publicidad;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPublicidads() {
        this.eventSubscriber = this.eventManager.subscribe(
            'publicidadListModification',
            (response) => this.load(this.publicidad.id)
        );
    }
}
