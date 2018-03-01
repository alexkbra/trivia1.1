import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Participar } from './participar.model';
import { ParticiparService } from './participar.service';

@Component({
    selector: 'jhi-participar-detail',
    templateUrl: './participar-detail.component.html'
})
export class ParticiparDetailComponent implements OnInit, OnDestroy {

    participar: Participar;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private participarService: ParticiparService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInParticipars();
    }

    load(id) {
        this.participarService.find(id).subscribe((participar) => {
            this.participar = participar;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInParticipars() {
        this.eventSubscriber = this.eventManager.subscribe(
            'participarListModification',
            (response) => this.load(this.participar.id)
        );
    }
}
