/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ExpedicionDetailComponent } from '../../../../../../main/webapp/app/entities/expedicion/expedicion-detail.component';
import { ExpedicionService } from '../../../../../../main/webapp/app/entities/expedicion/expedicion.service';
import { Expedicion } from '../../../../../../main/webapp/app/entities/expedicion/expedicion.model';

describe('Component Tests', () => {

    describe('Expedicion Management Detail Component', () => {
        let comp: ExpedicionDetailComponent;
        let fixture: ComponentFixture<ExpedicionDetailComponent>;
        let service: ExpedicionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [ExpedicionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ExpedicionService,
                    JhiEventManager
                ]
            }).overrideTemplate(ExpedicionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExpedicionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExpedicionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Expedicion(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.expedicion).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
