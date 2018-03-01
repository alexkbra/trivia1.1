import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Regla } from './regla.model';
import { ReglaService } from './regla.service';

@Component({
    selector: 'jhi-regla-detail',
    templateUrl: './regla-detail.component.html'
})
export class ReglaDetailComponent implements OnInit, OnDestroy {

    regla: Regla;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reglaService: ReglaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReglas();
    }

    load(id) {
        this.reglaService.find(id).subscribe((regla) => {
            this.regla = regla;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReglas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reglaListModification',
            (response) => this.load(this.regla.id)
        );
    }
}
