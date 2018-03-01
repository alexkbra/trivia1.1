import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Expedicion } from './expedicion.model';
import { ExpedicionService } from './expedicion.service';

@Component({
    selector: 'jhi-expedicion-detail',
    templateUrl: './expedicion-detail.component.html'
})
export class ExpedicionDetailComponent implements OnInit, OnDestroy {

    expedicion: Expedicion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private expedicionService: ExpedicionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExpedicions();
    }

    load(id) {
        this.expedicionService.find(id).subscribe((expedicion) => {
            this.expedicion = expedicion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExpedicions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'expedicionListModification',
            (response) => this.load(this.expedicion.id)
        );
    }
}
